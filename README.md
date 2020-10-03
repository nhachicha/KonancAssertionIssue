# Build the plugin
```
./gradlew shadowJar
```

# Compile a KLIB using the compiler plugin
```
cd example
konanc Api.kt -Xplugin=../build/shaded/konan_assertion_issue.jar -p library -o mylib
```

# Producing a kexe fails
```
konanc Client.kt -g -ea -target macos_x64 -p program -o main -l mylib.klib
```

## fails with
```
konanc Client.kt -g -ea -target macos_x64 -p program -o main -l mylib.klib            ✔  6s  18:33:03
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by com.intellij.util.ReflectionUtil (file:/Users/nabil/Dev/kotlin-native/dist/konan/lib/kotlin-native.jar) to method java.util.ResourceBundle.setParent(java.util.ResourceBundle)
WARNING: Please consider reporting this to the maintainers of com.intellij.util.ReflectionUtil
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
error: compilation failed: Assertion failed

* Source files: Client.kt
* Compiler version info: Konan: 1.4.20-dev / Kotlin: 1.4.20
* Output kind: PROGRAM

exception: java.lang.AssertionError: Assertion failed
 at org.jetbrains.kotlin.backend.konan.llvm.DebugUtilsKt.location(DebugUtils.kt:99)
 at org.jetbrains.kotlin.backend.konan.llvm.DebugUtilsKt.line(DebugUtils.kt:108)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.startLine(IrToBitcode.kt:1910)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.debugFieldDeclaration(IrToBitcode.kt:1936)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitField(IrToBitcode.kt:841)
 at org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid$DefaultImpls.visitField(IrElementVisitorVoid.kt:62)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitField(IrToBitcode.kt:200)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitField(IrToBitcode.kt:200)
 at org.jetbrains.kotlin.ir.declarations.IrField.accept(IrField.kt:35)
 at org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoidKt.acceptVoid(IrElementVisitorVoid.kt:271)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitProperty(IrToBitcode.kt:834)
 at org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid$DefaultImpls.visitProperty(IrElementVisitorVoid.kt:59)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitProperty(IrToBitcode.kt:200)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitProperty(IrToBitcode.kt:200)
 at org.jetbrains.kotlin.ir.declarations.IrProperty.accept(IrProperty.kt:46)
 at org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoidKt.acceptVoid(IrElementVisitorVoid.kt:271)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitClass(IrToBitcode.kt:788)
 at org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid$DefaultImpls.visitClass(IrElementVisitorVoid.kt:44)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitClass(IrToBitcode.kt:200)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitClass(IrToBitcode.kt:200)
 at org.jetbrains.kotlin.ir.declarations.IrClass.accept(IrClass.kt:55)
 at org.jetbrains.kotlin.ir.declarations.impl.IrFileImpl.acceptChildren(IrFileImpl.kt:65)
 at org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoidKt.acceptChildrenVoid(IrElementVisitorVoid.kt:275)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitFile(IrToBitcode.kt:509)
 at org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid$DefaultImpls.visitFile(IrElementVisitorVoid.kt:38)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitFile(IrToBitcode.kt:200)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitFile(IrToBitcode.kt:200)
 at org.jetbrains.kotlin.ir.declarations.impl.IrFileImpl.accept(IrFileImpl.kt:62)
 at org.jetbrains.kotlin.ir.declarations.impl.IrModuleFragmentImpl.acceptChildren(IrModuleFragmentImpl.kt:40)
 at org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoidKt.acceptChildrenVoid(IrElementVisitorVoid.kt:275)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitModuleFragment(IrToBitcode.kt:350)
 at org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoid$DefaultImpls.visitModuleFragment(IrElementVisitorVoid.kt:28)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitModuleFragment(IrToBitcode.kt:200)
 at org.jetbrains.kotlin.backend.konan.llvm.CodeGeneratorVisitor.visitModuleFragment(IrToBitcode.kt:200)
 at org.jetbrains.kotlin.ir.declarations.impl.IrModuleFragmentImpl.accept(IrModuleFragmentImpl.kt:37)
 at org.jetbrains.kotlin.ir.visitors.IrElementVisitorVoidKt.acceptVoid(IrElementVisitorVoid.kt:271)
 at org.jetbrains.kotlin.backend.konan.llvm.BitcodePhasesKt$codegenPhase$1.invoke(BitcodePhases.kt:253)
 at org.jetbrains.kotlin.backend.konan.llvm.BitcodePhasesKt$codegenPhase$1.invoke(BitcodePhases.kt)
 at org.jetbrains.kotlin.backend.konan.KonanLoweringPhasesKt$makeKonanModuleOpPhase$1.invoke(KonanLoweringPhases.kt:62)
 at org.jetbrains.kotlin.backend.konan.KonanLoweringPhasesKt$makeKonanModuleOpPhase$1.invoke(KonanLoweringPhases.kt:60)
 at org.jetbrains.kotlin.backend.common.phaser.NamedCompilerPhase.invoke(CompilerPhase.kt:94)
 at org.jetbrains.kotlin.backend.common.phaser.CompositePhase.invoke(PhaseBuilders.kt:30)
 at org.jetbrains.kotlin.backend.common.phaser.NamedCompilerPhase.invoke(CompilerPhase.kt:94)
 at org.jetbrains.kotlin.backend.common.phaser.CompositePhase.invoke(PhaseBuilders.kt:30)
 at org.jetbrains.kotlin.backend.common.phaser.NamedCompilerPhase.invoke(CompilerPhase.kt:94)
 at org.jetbrains.kotlin.backend.common.phaser.CompositePhase.invoke(PhaseBuilders.kt:23)
 at org.jetbrains.kotlin.backend.common.phaser.NamedCompilerPhase.invoke(CompilerPhase.kt:94)
 at org.jetbrains.kotlin.backend.common.phaser.CompositePhase.invoke(PhaseBuilders.kt:30)
 at org.jetbrains.kotlin.backend.common.phaser.NamedCompilerPhase.invoke(CompilerPhase.kt:94)
 at org.jetbrains.kotlin.backend.common.phaser.CompilerPhaseKt.invokeToplevel(CompilerPhase.kt:41)
 at org.jetbrains.kotlin.backend.konan.KonanDriverKt.runTopLevelPhases(KonanDriver.kt:29)
 at org.jetbrains.kotlin.cli.bc.K2Native.doExecute(K2Native.kt:78)
 at org.jetbrains.kotlin.cli.bc.K2Native.doExecute(K2Native.kt:35)
 at org.jetbrains.kotlin.cli.common.CLICompiler.execImpl(CLICompiler.kt:88)
 at org.jetbrains.kotlin.cli.common.CLICompiler.execImpl(CLICompiler.kt:44)
 at org.jetbrains.kotlin.cli.common.CLITool.exec(CLITool.kt:98)
 at org.jetbrains.kotlin.cli.common.CLITool.exec(CLITool.kt:76)
 at org.jetbrains.kotlin.cli.common.CLITool.exec(CLITool.kt:45)
 at org.jetbrains.kotlin.cli.common.CLITool$Companion.doMainNoExit(CLITool.kt:227)
 at org.jetbrains.kotlin.cli.common.CLITool$Companion.doMainNoExit$default(CLITool.kt:225)
 at org.jetbrains.kotlin.cli.common.CLITool$Companion.doMain(CLITool.kt:214)
 at org.jetbrains.kotlin.cli.bc.K2Native$Companion$main$1.invoke(K2Native.kt:265)
 at org.jetbrains.kotlin.cli.bc.K2Native$Companion$main$1.invoke(K2Native.kt:262)
 at org.jetbrains.kotlin.util.UtilKt.profileIf(Util.kt:27)
 at org.jetbrains.kotlin.util.UtilKt.profile(Util.kt:21)
 at org.jetbrains.kotlin.cli.bc.K2Native$Companion.main(K2Native.kt:264)
 at org.jetbrains.kotlin.cli.bc.K2NativeKt.main(K2Native.kt:458)
 at org.jetbrains.kotlin.cli.utilities.MainKt$main$1.invoke(main.kt:37)
 at org.jetbrains.kotlin.cli.utilities.MainKt$main$1.invoke(main.kt)
 at org.jetbrains.kotlin.cli.utilities.MainKt.mainImpl(main.kt:17)
 at org.jetbrains.kotlin.cli.utilities.MainKt.main(main.kt:37)
```
