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

	private HashMap<String, Label> labelMap = new HashMap<String, Label>();

	public Generator() {

	}

	void emitClass() {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "Generator/Runner", null, "Generator/RunnerBase", new String[] { "Generator/IRun" });
	}

	void emitMethod(String name) {
		mv = cw.visitMethod(ACC_PUBLIC, name, "()V", null, null);
		mv.visitCode();
	}

	void closeMethod() {
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	void emitJMP(String targetLabel) {
		Label lb = labelMap.get(targetLabel);
		if (lb == null) {
			lb = new Label();
			labelMap.put(targetLabel, lb);
		}
		mv.visitJumpInsn(GOTO, lb);
	}
	
	void emitJMPFALSE(String targetLabel) {
		// TODO
	}
	
	void emitJMPTRUE(String targetLabel) {
		// TODO 
	}
	
	void emitLabel(String targetLabel) {
		Label lb = labelMap.get(targetLabel);
		if (lb == null) {
			lb = new Label();
			labelMap.put(targetLabel, lb);
		}
		mv.visitLabel(lb);
	}

	void emitCall(String fname) {
		mv.visitVarInsn(ALOAD, 0); // Load this on stack.
		mv.visitMethodInsn(INVOKEVIRTUAL, "Generator/Runner", fname, "()V") ;
	}
	
	void emitCall(String fname,int arg1, int arg2) {
		mv.visitVarInsn(ALOAD, 0); // Load this on stack.
		mv.visitIntInsn(BIPUSH, arg1);
		mv.visitIntInsn(BIPUSH, arg2);
		mv.visitMethodInsn(INVOKEVIRTUAL, "Generator/Runner", fname, "(II)V") ;
	}
	
	byte[] finalizeCode() {
		cw.visitEnd();
		return cw.toByteArray();
	}
	public static void main(String[] argv) {
		byte[] result = null ;
		System.out.println("Getting started!\n");
		Generator emittor = new Generator() ;
		
		emittor.emitClass(); 
		emittor.emitMethod("main");
		emittor.emitLabel("entrypoint");
		emittor.emitCall("main");
		emittor.emitCall("main",10,20);
		emittor.emitJMP("entrypoint");
		emittor.closeMethod();
		result = emittor.finalizeCode() ;
		
		try {
			FileOutputStream fos = new FileOutputStream("/Users/ferryrietveld/Runner.class");
			fos.write(result);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
