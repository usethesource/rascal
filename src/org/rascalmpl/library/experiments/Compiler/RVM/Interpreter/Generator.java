package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Generator implements Opcodes {
	private ClassWriter cw = null;
	private MethodVisitor mv = null;
	private String className = null;
	private String packageName = null;
	private String fullClassName = null;
	private String[] funcArray = null;

	private HashMap<String, Label> labelMap = new HashMap<String, Label>();

	public Generator() {

	}

	public void emitClass(String pName, String cName) {
		this.className = cName;
		this.packageName = pName;
		this.fullClassName = packageName + "/" + className;
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, fullClassName, null, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRun",
				new String[] { "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/IDynamicRun" });
	}

	public void emitMethod(String name) {
		mv = cw.visitMethod(ACC_PUBLIC, name, "()Ljava/lang/Object;", null, null);
		labelMap.clear();
		mv.visitCode();
	}

	public void closeMethod() {
		// mv.visitInsn(RETURN); // TODO remove to keep decompiler happy.
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	public void emitJMP(String targetLabel) {
		Label lb = labelMap.get(targetLabel);
		if (lb == null) {
			lb = new Label();
			labelMap.put(targetLabel, lb);
		}
		mv.visitJumpInsn(GOTO, lb);
	}

	public void emitJMPTRUE(String targetLabel) {
		Label l0 = new Label();
		Label l1 = new Label();

		Label lb = labelMap.get(targetLabel);
		if (lb == null) {
			lb = new Label();
			labelMap.put(targetLabel, lb);
		}

		emitPOP();

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(AALOAD);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "TRUE", "Ljava/lang/Boolean;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");

		mv.visitJumpInsn(IFNE, l0); // Direct goto possible

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(AALOAD);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "Rascal_TRUE", "Lorg/eclipse/imp/pdb/facts/IBool;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");
		mv.visitJumpInsn(IFEQ, l1);

		mv.visitLabel(l0);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitJumpInsn(GOTO, lb);
		mv.visitLabel(l1);
	}

	public void emitJMPFALSE(String targetLabel) {
		Label l0 = new Label();
		Label l1 = new Label();

		Label lb = labelMap.get(targetLabel);
		if (lb == null) {
			lb = new Label();
			labelMap.put(targetLabel, lb);
		}

		emitPOP();

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(AALOAD);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "FALSE", "Ljava/lang/Boolean;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");

		mv.visitJumpInsn(IFNE, l0); // Direct goto possible

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(AALOAD);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "Rascal_FALSE", "Lorg/eclipse/imp/pdb/facts/IBool;");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z");
		mv.visitJumpInsn(IFEQ, l1);

		mv.visitLabel(l0);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitJumpInsn(GOTO, lb);
		mv.visitLabel(l1);
	}

	public void emitPOP() {
		mv.visitVarInsn(ALOAD, 0);
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETFIELD, fullClassName, "sp", "I");
		mv.visitInsn(ICONST_1);
		mv.visitInsn(ISUB);
		mv.visitFieldInsn(PUTFIELD, fullClassName, "sp", "I");
	}

	public void emitLabel(String targetLabel) {
		Label lb = labelMap.get(targetLabel);
		if (lb == null) {
			lb = new Label();
			labelMap.put(targetLabel, lb);
		}
		mv.visitLabel(lb);
	}

	public void emitCall(String fname) {
		mv.visitVarInsn(ALOAD, 0); // Load this on stack.
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "()V");
	}

	public void emitCall(String fname, int arg1) {
		mv.visitVarInsn(ALOAD, 0); // Load this on stack.
		mv.visitIntInsn(BIPUSH, arg1);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(I)V");
	}

	public void emitCall(String fname, int arg1, int arg2) {
		mv.visitVarInsn(ALOAD, 0); // Load this on stack.
		mv.visitIntInsn(BIPUSH, arg1);
		mv.visitIntInsn(BIPUSH, arg2);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(II)V");
	}

	byte[] finalizeCode() {
		cw.visitEnd();
		return cw.toByteArray();
	}

	public void emitReturn0() {
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);
	}

	public void emitReturn1() {
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);
	}

	public void emitFailreturn() {
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "FAILRETURN", "Lorg/eclipse/imp/pdb/facts/IString;");
		mv.visitInsn(ARETURN);
	}

	public void dump() {
		byte[] result = finalizeCode();

		try {
			FileOutputStream fos = new FileOutputStream("/Users/ferryrietveld/Runner.class");
			fos.write(result);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] argv) {
		byte[] result = null;
		System.out.println("Getting started!\n");
		Generator emittor = new Generator();

		emittor.emitClass("org/rascalmpl/library/experiments/Compiler/RVM/Interpreter", "Runner");
		emittor.emitMethod("main");
		emittor.emitLabel("entrypoint");
		emittor.emitCall("main");
		emittor.emitCall("main", 10, 20);
		emittor.emitJMPTRUE("entrypoint");
		emittor.emitPOP();
		emittor.emitJMP("entrypoint");
		emittor.closeMethod();
		result = emittor.finalizeCode();

		try {
			FileOutputStream fos = new FileOutputStream("/Users/ferryrietveld/Runner.class");
			fos.write(result);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void emitDynPrelude() {
		mv = cw.visitMethod(ACC_PUBLIC, "dynRun", "(Ljava/lang/String;[Lorg/eclipse/imp/pdb/facts/IValue;)Ljava/lang/Object;", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "functionMap", "Ljava/util/Map;");
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;");
		mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I");
		mv.visitVarInsn(ISTORE, 3);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "functionStore", "Ljava/util/ArrayList;");
		mv.visitVarInsn(ILOAD, 3);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "get", "(I)Ljava/lang/Object;");
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function");
		mv.visitVarInsn(ASTORE, 4);
		mv.visitTypeInsn(NEW, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function", "scopeId", "I");
		mv.visitInsn(ACONST_NULL);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function", "maxstack", "I");
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "<init>",
				"(ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;)V");
		mv.visitVarInsn(ASTORE, 5);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitVarInsn(ALOAD, 5);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRunBody", "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 6);
		Label l0 = new Label();
		mv.visitJumpInsn(GOTO, l0);
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ILOAD, 6);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitVarInsn(ILOAD, 6);
		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
		mv.visitIincInsn(6, 1);
		mv.visitLabel(l0);
		mv.visitVarInsn(ILOAD, 6);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitInsn(ARRAYLENGTH);
		mv.visitJumpInsn(IF_ICMPLT, l1);
	}

	public void emitDynDispatch(int numberOfFunctions) {
		System.out.println("DYNCALL CASE count :" + numberOfFunctions);
		funcArray = new String[numberOfFunctions];
	}

	public void emitDynCaLL(String fname, Integer value) {
		funcArray[value] = fname;
	}

	public void emitDynFinalize() {
		int nrFuncs = funcArray.length;
		//nrFuncs = 4;
		//
		// for (int i = 0; i < nrFuncs ; i++) {
		// System.out.println("DUNCALL :" + i + " " + funcArray[i]);
		// }
		
		Label[] caseLabels = new Label[nrFuncs];
	
		for (int i = 0; i < nrFuncs ; i++) {
			caseLabels[i] = new Label();
		}
		Label defaultlabel = new Label();

		// Case switch on int at loc 3 (java stack)
		mv.visitVarInsn(ILOAD, 3);
		mv.visitTableSwitchInsn(0, nrFuncs-1, defaultlabel, caseLabels);
		for (int i = 0; i < nrFuncs; i++) {
			mv.visitLabel(caseLabels[i]);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, NameMangler.mangle(funcArray[i]), "()Ljava/lang/Object;");
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
}
