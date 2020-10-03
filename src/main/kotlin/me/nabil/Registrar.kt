package me.nabil

import org.jetbrains.kotlin.backend.common.ClassLoweringPass
import org.jetbrains.kotlin.backend.common.checkDeclarationParents
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.ir.copyTo
import org.jetbrains.kotlin.backend.common.lower
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrExpressionBodyImpl
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.name.Name
import java.time.Instant


class Registrar : ComponentRegistrar {
    override fun registerProjectComponents(
            project: MockProject,
            configuration: CompilerConfiguration
    ) {
        messageCollector = configuration.get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)
        IrGenerationExtension.registerExtension(project, object : IrGenerationExtension {
            override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
                PropertyGenLowering(pluginContext).lower(moduleFragment)
                moduleFragment.checkDeclarationParents()
            }
        })
    }
}


private class PropertyGenLowering(private val pluginContext: IrPluginContext) : ClassLoweringPass {
    override fun lower(irClass: IrClass) {
        logger("ClassLoweringPass lowering ${irClass.name}")
        irClass.addProperty(Name.identifier("generatedProperty"), pluginContext.irBuiltIns.longType.makeNullable())
    }

    private fun IrClass.addProperty(propertyName: Name, propertyType: IrType) {
        val property = addProperty {
            name = propertyName
            visibility = DescriptorVisibilities.PUBLIC
            modality = Modality.FINAL
            isVar = true
        }
        property.backingField = pluginContext.irFactory.buildField {
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            name = property.name
            visibility = DescriptorVisibilities.PRIVATE
            modality = property.modality
            type = propertyType

        }.apply {
            initializer = IrExpressionBodyImpl(startOffset, endOffset, irNull(startOffset, endOffset))
        }
        property.backingField?.parent = this
        property.backingField?.correspondingPropertySymbol = property.symbol


        val getter = property.addGetter {
            visibility = DescriptorVisibilities.PUBLIC
            modality = Modality.FINAL
            returnType = propertyType
            origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
        }
        getter.dispatchReceiverParameter = thisReceiver!!.copyTo(getter)

        getter.body = pluginContext.blockBody(getter.symbol) {
            +irReturn(
                    irGetField(irGet(getter.dispatchReceiverParameter!!), property.backingField!!)
            )
        }
        val setter = property.addSetter() {
            visibility = DescriptorVisibilities.PUBLIC
            modality = Modality.FINAL
            returnType = pluginContext.irBuiltIns.unitType
            origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
        }
        setter.dispatchReceiverParameter = thisReceiver!!.copyTo(setter)
        setter.correspondingPropertySymbol = property.symbol
        val valueParameter = setter.addValueParameter {
            this.name = Name.special("<set-?>")
            this.type = propertyType
        }
        setter.body = DeclarationIrBuilder(pluginContext, setter.symbol).irBlockBody {
            +irSetField(irGet(setter.dispatchReceiverParameter!!), property.backingField!!, irGet(valueParameter))
        }
    }

    private fun irNull(startOffset: Int, endOffset: Int): IrConstImpl<Nothing?> {
        return IrConstImpl.constNull(startOffset, endOffset, pluginContext.irBuiltIns.nothingNType)
    }

    private fun IrPluginContext.blockBody(
            symbol: IrSymbol,
            block: IrBlockBodyBuilder.() -> Unit
    ): IrBlockBody =
            DeclarationIrBuilder(this, symbol).irBlockBody { block() }

    inline fun IrProperty.addSetter(builder: IrFunctionBuilder.() -> Unit = {}): IrSimpleFunction =
            IrFunctionBuilder().run {
                factory.buildFun {
                    this.name = Name.special("<set-${this@addSetter.name}>")
                    builder()
                }.also { setter ->
                    this@addSetter.setter = setter
                    setter.correspondingPropertySymbol = this@addSetter.symbol
                    setter.parent = this@addSetter.parent
                }
            }

}

lateinit var messageCollector: MessageCollector
fun logger(message: String, severity: CompilerMessageSeverity = CompilerMessageSeverity.WARNING) {
    val formattedMessage = "[Kotlin Compiler] ${Instant.now()} $message\n"
    messageCollector.report(severity, formattedMessage)
}