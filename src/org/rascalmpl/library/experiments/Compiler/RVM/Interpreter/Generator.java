package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.FileOutputStream;
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

	private HashMap<String, Label> labelMap = new HashMap<String, Label>();

	public Generator() {

	}

	public void emitClass(String pName, String cName) {
		this.className = cName;
		this.packageName = pName;
		this.fullClassName = packageName + "/" + className;
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, fullClassName, null, "Generator/RunnerBase", new String[] { "Generator/IRun" });
	}

	public void emitMethod(String name) {
		mv = cw.visitMethod(ACC_PUBLIC, name, "()V", null, null);
		mv.visitCode();
	}

	public void closeMethod() {
		mv.visitInsn(RETURN); // TODO remove to keep decompiler happy.
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

	public void emitReturn() {
		mv.visitInsn(RETURN); // TODO remove to keep decompiler happy.		
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
}
