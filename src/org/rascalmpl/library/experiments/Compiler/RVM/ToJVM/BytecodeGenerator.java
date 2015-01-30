package org.rascalmpl.library.experiments.Compiler.RVM.ToJVM;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;

public class BytecodeGenerator implements Opcodes {
	byte[] endCode = null;
	private ClassWriter cw = null;
	private MethodVisitor mv = null;
	private String className = null;
	private String packageName = null;
	private String fullClassName = null;
	private String[] funcArray = null;
	private boolean emit = true;
	private boolean isCoroutine = false;

	private HashMap<String, Label> labelMap = new HashMap<String, Label>();
	private Label[] hotEntryLabels = null;
	private Label exitLabel = null;

	//
	// Optimize some ocalls.
	//
	ArrayList<Function> functionStore;
	ArrayList<OverloadedFunction> overloadedStore;

	private Label getNamedLabel(String targetLabel) {
		Label lb = labelMap.get(targetLabel);
		if (lb == null) {
			lb = new Label();
			labelMap.put(targetLabel, lb);
		}
		return lb;
	}

	public BytecodeGenerator(String packageName2, String className2, ArrayList<Function> functionStore, ArrayList<OverloadedFunction> overloadedStore) {
		fullClassName = packageName2 + "." + className2;
		fullClassName = fullClassName.replace('.', '/');
		this.functionStore = functionStore;
		this.overloadedStore = overloadedStore;
	}

	public BytecodeGenerator() {
		emit = false;
	}

	void enableOutput(boolean flag) {
		emit = flag;
	}

	private void emitIntValue(int value) {
		switch (value) {
		case 0:
			mv.visitInsn(ICONST_0);
			return;
		case 1:
			mv.visitInsn(ICONST_1);
			return;
		case 2:
			mv.visitInsn(ICONST_2);
			return;
		case 3:
			mv.visitInsn(ICONST_3);
			return;
		case 4:
			mv.visitInsn(ICONST_4);
			return;
		case 5:
			mv.visitInsn(ICONST_5);
			return;
		}
		if (value >= -128 && value <= 127)
			mv.visitIntInsn(BIPUSH, value); // 8 bit
		else if (value >= -32768 && value <= 32767)
			mv.visitIntInsn(SIPUSH, value); // 16 bit
		else
			mv.visitLdcInsn(new Integer(value)); // REST
	}

	public void emitClass(String pName, String cName) {
		if (!emit)
			return;

		this.className = cName;
		this.packageName = pName;
		this.fullClassName = packageName + "/" + className;
		this.fullClassName = "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRunner";
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, fullClassName, null, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRun", null);

		// Add constructor initialzing super.
		// Give it REX 15-5-2014
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRun", "<init>",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)V");
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	public void emitMethod(String name, boolean isCoroutine, int continuationPoints, boolean debug) {
		if (!emit)
			return;

		this.isCoroutine = isCoroutine;
		labelMap.clear(); // New set of labels.

		mv = cw.visitMethod(ACC_PUBLIC, name, "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, 2);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ASTORE, 3);

		if (continuationPoints != 0) {
			hotEntryLabels = new Label[continuationPoints + 1]; // Add entry 0
			exitLabel = new Label();

			for (int i = 0; i < hotEntryLabels.length; i++)
				hotEntryLabels[i] = new Label();

			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "hotEntryPoint", "I");
			mv.visitTableSwitchInsn(0, hotEntryLabels.length - 1, exitLabel, hotEntryLabels);

			mv.visitLabel(hotEntryLabels[0]); // Start at 'address' 0
		} else {
			exitLabel = null;
		}
	}

	public void closeMethod() {
		if (!emit)
			return;
		
		// This label should never be reached, it is
		// placed to
		// keep JVM verifier happy. (Reason: code generated 
		// by the compiler jumps sometimes to a non existing 
		// location)
		if (exitLabel != null) { 
			mv.visitLabel(exitLabel);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, fullClassName, "PANIC", "Lorg/eclipse/imp/pdb/facts/IString;");
			mv.visitInsn(ARETURN);
		}
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	public void emitJMP(String targetLabel) {
		if (!emit)
			return;
		Label lb = getNamedLabel(targetLabel);
		mv.visitJumpInsn(GOTO, lb);
	}

	public void emitJMPTRUE(String targetLabel, boolean debug) {
		if (!emit)
			return;
		Label lb = getNamedLabel(targetLabel);

		if (debug)
			emitCall("dinsnJMPTRUE", 1);

		emitInlinePop(false); // pop part of jmp...

		mv.visitVarInsn(ALOAD, 3);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(AALOAD);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/eclipse/imp/pdb/facts/IBool", "getValue", "()Z");
		mv.visitJumpInsn(IFNE, lb);
	}

	public void emitJMPFALSE(String targetLabel, boolean debug) {
		if (!emit)
			return;
		if (debug)
			emitCall("dinsnJMPFALSE", 2);

		Label lb = getNamedLabel(targetLabel);

		emitInlinePop(false); // pop part of jmp...

		mv.visitVarInsn(ALOAD, 3);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(AALOAD);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/eclipse/imp/pdb/facts/IBool", "getValue", "()Z");
		mv.visitJumpInsn(IFEQ, lb);
	}

	public void emitLabel(String targetLabel) {
		if (!emit)
			return;
		Label lb = getNamedLabel(targetLabel);
		mv.visitLabel(lb);
	}

	// A call to a RVM instruction not CALL or OCALL
	public void emitCall(String fname) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0); // Load this on stack.
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "()V");
	}

	public void emitCall(String fname, int arg1) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0); // Load this on stack.
		emitIntValue(arg1);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(I)V");
	}

	public void emitCall(String fname, int arg1, int arg2) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0); // Load this on stack.
		emitIntValue(arg1);
		emitIntValue(arg2);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(II)V");
	}

	public void emitCallIII(String fname, int arg1, int arg2, int arg3) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0); // Load this on stack.
		emitIntValue(arg1);
		emitIntValue(arg2);
		emitIntValue(arg3);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(III)V");
	}

	public void emitCallIIB(String fname, int arg1, int arg2, boolean arg3) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0); // Load this on stack.

		emitIntValue(arg1);
		emitIntValue(arg2);

		if (arg3)
			mv.visitInsn(ICONST_1);
		else
			mv.visitInsn(ICONST_0);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(IIZ)V");
	}

	public byte[] finalizeCode() {
		if (!emit)
			return null;
		if (endCode == null) {
			cw.visitEnd();
			endCode = cw.toByteArray();
		}
		return endCode;
	}

	public void emitInlineReturn0(boolean debug) {
		if (!emit)
			return;
		
		Label normalReturn = new Label();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "return0Helper", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;");
		mv.visitVarInsn(ASTORE, 1);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitJumpInsn(IFNONNULL, normalReturn);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitInsn(ARETURN);
		mv.visitLabel(normalReturn);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);
	}

	public void emitInlineExhaust() {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "exhaustHelper", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;");
		mv.visitInsn(ARETURN);
	}

	public void emitInlineReturn1() {
		if (!emit)
			return;
		Label l0 = new Label();

		if (!emit)
			return;
		// 0 this
		// 1 Frame cf;
		// 2 int sp ;
		// 3 stack ;
		// 4 Object rval ;

		// does : Object rval = return1Helper(Object[] stack) ;

		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "return1Helper", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;");
		mv.visitVarInsn(ASTORE, 7);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitJumpInsn(IFNONNULL, l0);
		mv.visitVarInsn(ALOAD, 7); // LOAD rval
		mv.visitInsn(ARETURN);

		mv.visitLabel(l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);
	}

	public void emitInlineFailreturn() {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "failReturnHelper", "()V");

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "FAILRETURN", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);
	}

	public void emitDynDispatch(int numberOfFunctions) {
		if (!emit)
			return;
		funcArray = new String[numberOfFunctions];
	}

	public void emitDynCaLL(String fname, Integer value) {
		if (!emit)
			return;
		funcArray[value] = fname;
	}

	public void emitDynFinalize() {
		if (!emit)
			return;
		int nrFuncs = funcArray.length;
		Label[] caseLabels = new Label[nrFuncs];

		for (int i = 0; i < nrFuncs; i++) {
			caseLabels[i] = new Label();
		}
		Label defaultlabel = new Label();

		mv = cw.visitMethod(ACC_PUBLIC, "dynRun", "(ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;", null, null);
		mv.visitCode();

		// Case switch on int at loc 1 (java stack)
		mv.visitVarInsn(ILOAD, 1);
		mv.visitTableSwitchInsn(0, nrFuncs - 1, defaultlabel, caseLabels);
		for (int i = 0; i < nrFuncs; i++) {
			mv.visitLabel(caseLabels[i]);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2); // CF in second argument
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, NameMangler.mangle(funcArray[i]), "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;");
			mv.visitInsn(ARETURN);
		}
		mv.visitLabel(defaultlabel);

		// Function exit
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "vf", "Lorg/eclipse/imp/pdb/facts/IValueFactory;");
		mv.visitInsn(ICONST_0);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/eclipse/imp/pdb/facts/IValueFactory", "bool", "(Z)Lorg/eclipse/imp/pdb/facts/IBool;");
		mv.visitInsn(ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	// Emit inlineGuard needs a rearangment of local variables.
	public void emitInlineGuard(int hotEntryPoint, boolean dcode) {
		if (!emit)
			return;
		if (dcode)
			emitCall("dinsnGUARD");

		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		Label l3 = new Label();
		Label l4 = new Label();

		// 0 this
		// 1 Object rval used in return1
		// 2 boolean
		// 3 coroutine
		// 4 frame

		// Get guard precondition
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "guardHelper", "()Z");
		mv.visitVarInsn(ISTORE, 2);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");

		emitIntValue(hotEntryPoint);

		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "hotEntryPoint", "I");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cccf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");

		mv.visitJumpInsn(IF_ACMPNE, l0);
		mv.visitInsn(ACONST_NULL);
		mv.visitVarInsn(ASTORE, 3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "previousCallFrame",
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ASTORE, 4);
		mv.visitVarInsn(ILOAD, 2);

		mv.visitJumpInsn(IFEQ, l1);
		mv.visitTypeInsn(NEW, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cccf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "<init>",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)V");
		mv.visitVarInsn(ASTORE, 3);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitInsn(ICONST_1);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "isInitialized", "Z");
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "entryFrame",
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "suspend",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)V");
		mv.visitLabel(l1);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(ACONST_NULL);
		mv.visitFieldInsn(PUTFIELD, fullClassName, "cccf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(ICONST_1);
		mv.visitInsn(ISUB);
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitFieldInsn(PUTFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "stack", "[Ljava/lang/Object;");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(DUP_X1);
		mv.visitInsn(ICONST_1);
		mv.visitInsn(IADD);
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ILOAD, 2);

		mv.visitJumpInsn(IFEQ, l2);
		mv.visitVarInsn(ALOAD, 3);

		mv.visitJumpInsn(GOTO, l3);
		mv.visitLabel(l2);

		mv.visitFieldInsn(GETSTATIC, fullClassName, "exhausted", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine;");
		mv.visitLabel(l3);

		mv.visitInsn(AASTORE);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);
		mv.visitLabel(l0);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ILOAD, 2);

		mv.visitJumpInsn(IFNE, l4);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "previousCallFrame",
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "stack", "[Ljava/lang/Object;");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(DUP_X1);
		mv.visitInsn(ICONST_1);
		mv.visitInsn(IADD);
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");

		// mv.visitVarInsn(ALOAD, 0);
		// mv.visitFieldInsn(GETFIELD, fullClassName, "Rascal_FALSE", "Lorg/eclipse/imp/pdb/facts/IBool;");

		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/eclipse/imp/pdb/facts/IBool;");

		mv.visitInsn(AASTORE);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);
		mv.visitLabel(l4);

		if (exitLabel != null) { 
			// The label for the hot entry
			// Store reentry label in current frame.
			// System.out.println(currentName + " GU : entrypoint :"
			// + hotEntryPoint);
			mv.visitLabel(hotEntryLabels[hotEntryPoint]);
		} else {
			System.err.println("Guard and no hotentry label!");
		}
	}

	public void emitInlineLoadLocN(int n, boolean debug) {
		// Assumes Object[] stack is copied to local variable

		if (!emit)
			return;
		if (debug) {
			emitCall("dinsnLOADLOCN");
		}

		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(DUP_X1);
		mv.visitInsn(ICONST_1);
		mv.visitInsn(IADD);
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 3);

		emitIntValue(n);

		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);

	}

	public void emitInlinePop(boolean debug) {
		if (!emit)
			return;
		if (debug)
			emitCall("dinsnPOP");

		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "insnPOP", "(I)I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
	}

	public void emitInlineStoreLoc(int loc, boolean debug) {
		if (!emit)
			return;

		if (debug) { // That we can trace the method call!
			emitCall("insnSTORELOC", loc);
			return;
		}
		mv.visitVarInsn(ALOAD, 3);
		emitIntValue(loc);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(ICONST_1);
		mv.visitInsn(ISUB);
		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);

	}

	public void emitInlineTypeSwitch(IList labels, boolean dcode) {
		if (!emit)
			return;
		if (dcode)
			emitCall("dinsnTYPESWITCH", 1);

		Label[] switchTable;

		int nrLabels = labels.length();
		switchTable = new Label[nrLabels];
		int lcount = 0;
		for (IValue vlabel : labels) {
			String label = ((IString) vlabel).getValue();
			switchTable[lcount++] = getNamedLabel(label);
		}

		if (exitLabel == null)
			exitLabel = new Label();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "typeSwitchHelper", "()I");
		mv.visitTableSwitchInsn(0, nrLabels - 1, exitLabel, switchTable);

	}

	public void emitHotEntryJumpTable(int continuationPoints, boolean debug) {
		if (!emit)
			return;

		if (debug) {
		}

		hotEntryLabels = new Label[continuationPoints + 1]; // Add default 0
															// entry point.

	}

	public void emitCallJava(int className2, int methodName, int parameterTypes, int reflect, boolean debug) {
		if (!emit)
			return;

		mv.visitVarInsn(ALOAD, 0); // Load this on stack.

		emitIntValue(className2);
		emitIntValue(methodName);
		emitIntValue(parameterTypes);
		emitIntValue(reflect);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "insnCALLJAVA", "(IIII)V");
	}

	public void emitInlineJmpIndexed(IList labels, boolean dcode) {
		if (!emit)
			return;
		if (dcode)
			emitCall("dinsnJMPINDEXED", 1);

		Label[] switchTable;

		int nrLabels = labels.length();
		switchTable = new Label[nrLabels];

		int lcount = 0;
		for (IValue vlabel : labels) {
			String label = ((IString) vlabel).getValue();
			switchTable[lcount++] = getNamedLabel(label);
		}

		if (exitLabel == null)
			exitLabel = new Label();

		mv.visitVarInsn(ALOAD, 3);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(ICONST_1);
		mv.visitInsn(ISUB);
		mv.visitInsn(DUP_X1);
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
		mv.visitInsn(AALOAD);
		mv.visitTypeInsn(CHECKCAST, "org/eclipse/imp/pdb/facts/IInteger");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/eclipse/imp/pdb/facts/IInteger", "intValue", "()I");

		mv.visitTableSwitchInsn(0, nrLabels - 1, exitLabel, switchTable);
	}

	public void emitInlineYield1(int arity, int hotEntryPoint, boolean debug) {
		if (!emit)
			return;
		if (debug)
			emitCall("dinsnYIELD1", 1);

		Label l0 = new Label();
		mv.visitVarInsn(ALOAD, 0);

		emitIntValue(arity);
		emitIntValue(hotEntryPoint);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "yield1Helper", "(II)V");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitJumpInsn(IFNONNULL, l0);

		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", "Lorg/eclipse/imp/pdb/facts/IBool;");

		mv.visitInsn(ARETURN);
		mv.visitLabel(l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);

		if (exitLabel != null) { 
			// The label for the hot entry
			// Store reentry label in current frame.
			// System.out.println(currentName + " GU : entrypoint :"
			// + hotEntryPoint);
			mv.visitLabel(hotEntryLabels[hotEntryPoint]);
		} else {
			System.err.println("Yield1 and no hotentry label!");
		}
	}

	public void emitInlineYield0(int hotEntryPoint, boolean debug) {
		if (!emit)
			return;
		if (debug)
			emitCall("dinsnYIELD0", 1);

		Label l0 = new Label();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitVarInsn(ALOAD, 1);

		emitIntValue(hotEntryPoint);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "yield0Helper", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)V");
		mv.visitVarInsn(ALOAD, 0);

		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitJumpInsn(IFNONNULL, l0);

		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", "Lorg/eclipse/imp/pdb/facts/IBool;");

		mv.visitInsn(ARETURN);
		mv.visitLabel(l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);

		if (exitLabel != null) { 
			// The label for the hot entry
			// Store reentry label in current frame.
			// System.out.println(currentName + " GU : entrypoint :"
			// + hotEntryPoint);
			mv.visitLabel(hotEntryLabels[hotEntryPoint]);
		} else {
			System.err.println("Yield1 and no hotentry label!");
		}
	}

	public void emitPanicReturn() {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "PANIC", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);
	}

	public void emitEntryLabel(int continuationPoint) {
		mv.visitLabel(hotEntryLabels[continuationPoint]);
	}

	public void emitInlineCall(int functionIndex, int arity, int continuationPoint, boolean debug) {
		if (!emit)
			return;
		if (debug)
			emitCall("dinsnCALL", functionIndex);

		Label l0 = new Label();

		emitEntryLabel(continuationPoint);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitVarInsn(ALOAD, 1);

		emitIntValue(functionIndex);
		emitIntValue(arity);
		emitIntValue(continuationPoint);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "callHelper", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;III)Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");
		mv.visitJumpInsn(IFEQ, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);

		mv.visitLabel(l0);
	}

	public void emitInlineCalldyn(int arity, int continuationPoint, boolean debug) {
		if (!emit)
			return;
		if (debug)
			emitCall("dinsnCALLDYN", 1);

		Label l0 = new Label();

		emitEntryLabel(continuationPoint);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitVarInsn(ALOAD, 1);

		emitIntValue(arity);
		emitIntValue(continuationPoint);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "calldynHelper",
				"([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");
		mv.visitJumpInsn(IFEQ, l0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);

		mv.visitLabel(l0);
	}

	/**
	 * Emits a inline version of the CallMUPrime instructions. Uses a direct call to the static enum execute method.
	 * 
	 */
	public void emitInlineCallMuPrime(MuPrimitive muprim, int arity, boolean debug) {
		if (!emit)
			return;
		if (debug)
			emitCall("dinsnCALLMUPRIM", 1);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");

		mv.visitVarInsn(ALOAD, 3);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");

		emitIntValue(arity);

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "execute", "([Ljava/lang/Object;II)I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
	}

	public void emitInlineCallPrime(RascalPrimitive prim, int arity, boolean debug) {
		if (!emit)
			return;
		if (debug)
			emitCall("dinsnCALLPRIM", 1);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");
		mv.visitVarInsn(ALOAD, 3);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");

		emitIntValue(arity);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "stacktrace", "Ljava/util/ArrayList;");

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "execute", "([Ljava/lang/Object;IILjava/util/List;)I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
	}

	public void emitInlineLoadBool(boolean b, boolean debug) {
		if (!emit)
			return;

		if (debug) {
			if (b) {
				emitCall("insnLOADBOOLTRUE");
			} else {
				emitCall("insnLOADBOOLFALSE");
			}
		} else {
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
			mv.visitInsn(DUP_X1);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IADD);
			mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");

			if (b) {
				mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", "Lorg/eclipse/imp/pdb/facts/IBool;");
			} else {
				mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/eclipse/imp/pdb/facts/IBool;");
			}
			mv.visitInsn(AASTORE);
		}
	}

	public void emitCallWithArgsSS(String fname) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;I)I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
	}

	public void emitCallWithArgsSSI(String fname, int i, boolean dbg) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		emitIntValue(i);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;II)I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
	}

	public void emitCallWithArgsSSII(String fname, int i, int j, boolean dbg) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		emitIntValue(i);
		emitIntValue(j);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;III)I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
	}

	public void emitCallWithArgsSSFI(String fname, int i, boolean dbg) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3); // Stack

		mv.visitVarInsn(ALOAD, 0); // SP
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 1); // CF

		emitIntValue(i); // I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
	}

	public void emitVoidCallWithArgsSSI(String fname, int i, boolean dbg) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		emitIntValue(i);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;II)V");
	}

	public void emitCallWithArgsSSFII(String fname, int i, int j, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3); // Stack

		mv.visitVarInsn(ALOAD, 0); // SP
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 1); // CF

		emitIntValue(i); // I
		emitIntValue(j); // I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
	}

	public void emitCallWithArgsSSFIIZ(String fname, int what, int pos, boolean b, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3); // Stack

		mv.visitVarInsn(ALOAD, 0); // SP
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 1); // CF

		emitIntValue(what); // I
		emitIntValue(pos); // I

		if (b)
			mv.visitInsn(ICONST_1);
		else
			mv.visitInsn(ICONST_0);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IIZ)I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
	}

	public void emitVoidCallWithArgsSSFIIZ(String fname, int what, int pos, boolean b, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3); // Stack

		mv.visitVarInsn(ALOAD, 0); // SP
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 1); // CF

		emitIntValue(what); // I
		emitIntValue(pos); // I

		if (b)
			mv.visitInsn(ICONST_1);
		else
			mv.visitInsn(ICONST_0);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IIZ)V");
	}

	public void emitVoidCallWithArgsSSFII(String fname, int what, int pos, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3); // Stack

		mv.visitVarInsn(ALOAD, 0); // SP
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 1); // CF

		emitIntValue(what); // I
		emitIntValue(pos); // I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)V");
	}

	public void emitCallWithArgsSSFIIII(String fname, int methodName, int className2, int parameterTypes, int reflect, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3); // Stack

		mv.visitVarInsn(ALOAD, 0); // SP
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 1); // CF

		emitIntValue(methodName); // I
		emitIntValue(className2); // I
		emitIntValue(parameterTypes); // I
		emitIntValue(reflect); // I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IIII)I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
	}

	public void emitVoidCallWithArgsSS(String fname, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3); // Stack
		mv.visitVarInsn(ALOAD, 0); // SP
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;I)V");
	}

	public void emitVoidCallWithArgsSSFI(String fname, int pos, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3); // Stack
		mv.visitVarInsn(ALOAD, 0); // SP
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 1); // CF
		emitIntValue(pos); // I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)V");
	}

	public void emitCallWithArgsSSF(String fname, boolean dcode) {
		if (!emit)
			return;
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 3); // Stack

		mv.visitVarInsn(ALOAD, 0); // SP
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 1); // CF

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
	}

	/*
	 * emitOptimizedOcall emits a call to a full ocall implementation or: 
	 * 1: There is only one funtion emit direct call 
	 * 2: There is only a constructor call constructor
	 */
	public void emitOptimizedOcall(String fuid, int overloadedFunctionIndex, int arity, boolean dcode) {
		if (!emit)
			return;
		OverloadedFunction of = overloadedStore.get(overloadedFunctionIndex);
		int[] functions = of.getFuntions();
		if (functions.length == 1) {
			int[] ctors = of.getConstructors();
			if (ctors.length == 0) {
				Function fu = functionStore.get(functions[0]);
				if (of.getScopeFun() == null) {
					emitOcallSingle(NameMangler.mangle(fu.getName()), functions[0], arity) ;
				} else {
					// Nested fucntion needs link to containing frame
					emitVoidCallWithArgsSSFII("jvmOCALL", overloadedFunctionIndex, arity, dcode);
				}
			} else {
				// Has a constructor.
				emitVoidCallWithArgsSSFII("jvmOCALL", overloadedFunctionIndex, arity, dcode);
			}
		}
		else {
			emitVoidCallWithArgsSSFII("jvmOCALL", overloadedFunctionIndex, arity, dcode);			
		}
	}

	private void emitOcallSingle(String funName, int fun, int arity) {
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "functionStore", "Ljava/util/ArrayList;");

		emitIntValue(fun);

		mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "get", "(I)Ljava/lang/Object;");
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "root", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");

		emitIntValue(arity);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitMethodInsn(
				INVOKEVIRTUAL,
				"org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame",
				"getFrame",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "stack", "[Ljava/lang/Object;");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, funName, "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;");
		mv.visitInsn(POP);
	}
}
