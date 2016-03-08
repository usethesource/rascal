/*******************************************************************************
 * Copyright (c) 2009-2016 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Ferry Rietveld - f.rietveld@hva.nl - HvA
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/


package org.rascalmpl.library.experiments.Compiler.RVM.ToJVM;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.DatatypeConverter;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.MuPrimitive;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RascalPrimitive;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.io.StandardTextWriter;

public class BytecodeGenerator implements Opcodes {

	// Locations of the variables in a compiled RVM function.
	
	// Common local variables
	public static final int THIS = 0;			// Current class (generated for the top level Rascal module)
	public static final int CF = 1;				// Current frame
	public static final int SP = 2;				// RVM stack pointer: int sp
	public static final int ACCU = 3;			// RVM accumulator: Object accu
	public static final int STACK = 4;			// RVM stack: Object stack[]

	
	// Local variables in coroutines
	
	public static final int LPRECONDITION = 5;	// Boolean variable used in guard code
	public static final int TMP1 = 5;			// Temp variable outside Guard code
	public static final int LCOROUTINE = 6;		// Local coroutine instance in guard code
	public static final int TMP2 = 6;			// Temp variable outside Guard code
	public static final int EXCEPTION = 7;
	
	// Only needed when function needed constant store or type store
	public static final int CS = 8;			// Constant store
	public static final int TS = 9;			// Type constant store
	
	
	// Arguments of the RVM constructor
	public static final int RVM_EXEC = 1;
	public static final int RVM_REX = 2;

	byte[] endCode = null;
	
	private ClassWriter cw = null;
	private MethodVisitor mv = null;
	private FieldVisitor fv = null;
	private String className = null;
	private String packageName = null;
	private String fullClassName = null;

	private String[] funcArray = null;			// List of compiler-generated function names

	// Administration per function
	private HashMap<String, Label> labelMap = new HashMap<String, Label>();
	private Set<String> catchTargetLabels = new HashSet<String>();			
	private Set<String> storeCreators = new HashSet<String>();
	private Map<String, ExceptionLine> catchTargets = new HashMap<String, ExceptionLine>();
	private Label[] hotEntryLabels = null;		// entry labels for coroutines
	private Label exitLabel = null;				// special case for labels without code
												// TODO: peephole optimizer now removes them; can disappear?

	ArrayList<Function> functionStore;
	ArrayList<OverloadedFunction> overloadedStore;
	Map<String, Integer> functionMap;
	Map<String, Integer> constructorMap;
	Map<String, Integer> resolver;

	private Label getNamedLabel(String targetLabel) {
		Label lb = labelMap.get(targetLabel);
		if (lb == null) {
			lb = new Label();
			labelMap.put(targetLabel, lb);
		}
		return lb;
	}

	public BytecodeGenerator(ArrayList<Function> functionStore, ArrayList<OverloadedFunction> overloadedStore,
			Map<String, Integer> functionMap, Map<String, Integer> constructorMap, Map<String, Integer> resolver) {

		this.functionStore = functionStore;
		this.overloadedStore = overloadedStore;
		this.functionMap = functionMap;
		this.resolver = resolver;
		this.constructorMap = constructorMap;
	}

	Function currentFunction;
	
	public void buildClass(String packageName, String className, boolean debug) {

		emitClass(packageName,className);

		for (Function f : functionStore) {
			//System.err.println(f.getName());
			currentFunction = f;
			emitMethod(f, debug);
			//System.err.println(f.toString() );
		}
		
		// All functions are created create int based dispatcher
		emitDynDispatch(functionMap.size());
		for (Map.Entry<String, Integer> e : functionMap.entrySet()) {
			String fname = e.getKey();
			emitDynCaLL(fname, e.getValue());
		}
		emitDynFinalize();

		OverloadedFunction[] overloadedStoreV2 = overloadedStore.toArray(new OverloadedFunction[overloadedStore.size()]);
		emitConstructor(overloadedStoreV2);
	}

	public String finalName() {
		return fullClassName;
	}

	static final int CHUNKSIZE = 1024 ; // test value.
	private void emitStringValue(String str) {
		int len = str.length() ;
		int chunks = len / CHUNKSIZE ;
		
		int i = 0 ;
		if ( len > CHUNKSIZE ) { 
			for ( i = 0 ; i < chunks ; i++) {
				mv.visitLdcInsn(str.substring(i * CHUNKSIZE, i* CHUNKSIZE + CHUNKSIZE));	
			}
			if ( len > (chunks * CHUNKSIZE) ) {
				/// final partial chunk
				mv.visitLdcInsn(str.substring(i*CHUNKSIZE, len));					
			}
			else {
				chunks-- ;  // No partial chunk perfect fit.
			}
			while ( chunks != 0 ) {
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false);
				chunks-- ;
			}
		}
		else {
			mv.visitLdcInsn(str);				
		}
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

	public static String anySerialize(Object o) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.close();
		return DatatypeConverter.printBase64Binary(baos.toByteArray());
	}

	public static Object anyDeserialize(String s) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(s));
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object o = ois.readObject();
		ois.close();
		return o;
	}

	/*
	 * Generate the constructor method for the generated class
	 */
	public void emitConstructor(OverloadedFunction[] overloadedStore) {

		mv = cw.visitMethod(ACC_PUBLIC, "<init>",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMExecutable;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)V",
				null, null);

		mv.visitCode();
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, RVM_EXEC); 
		mv.visitVarInsn(ALOAD, RVM_REX);
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMonJVM", "<init>",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMExecutable;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)V",false);

		// Serialize/Deserialize overloadedStore.
		try {
			mv.visitVarInsn(ALOAD, THIS);
			
			emitStringValue(anySerialize(overloadedStore));		
			
			mv.visitMethodInsn(INVOKESTATIC, fullClassName, "anyDeserialize", "(Ljava/lang/String;)Ljava/lang/Object;",false);
			mv.visitTypeInsn(CHECKCAST, "[Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/OverloadedFunction;");
			mv.visitFieldInsn(PUTFIELD, fullClassName, "overloadedStore", "[Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/OverloadedFunction;");
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (String fname : storeCreators) {
			mv.visitVarInsn(ALOAD, THIS);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "()V",false);
		}

		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	/*
	 * Generate the complete class
	 */
	public void emitClass(String pName, String cName) {

		this.className = cName;
		this.packageName = pName;
		this.fullClassName = packageName.replace('.', '/') + "/" + className;
		//this.fullClassName = "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMRunner";
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, fullClassName, null, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMonJVM", null);
	}
	
	/*
	 * Dump the code of the generated class for debugging purposes
	 */
	
	public void dumpClass(OutputStream fos) {
		if (endCode == null)
			finalizeCode();
		try {
			fos.write(endCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Generate constant and/or type stores when present (per function)
	 */
	public void emitStoreInitializer(Function f) {

		// Create the statics to contain a functions constantStore and typeConstantStore (Why static?)
		if (f.constantStore.length > 0) {
			fv = cw.visitField(ACC_PUBLIC + ACC_STATIC, "cs_" + NameMangler.mangle(f.getName()), "[Ljava/lang/Object;", null, null);
			fv.visitEnd();
		}

		if (f.typeConstantStore.length > 0) {
			fv = cw.visitField(ACC_PUBLIC + ACC_STATIC, "tcs_" + NameMangler.mangle(f.getName()), "[Ljava/lang/Object;", null, null);
			fv.visitEnd();
		}

		if (f.constantStore.length == 0 && f.typeConstantStore.length == 0)
			return;

		// TODO: create initialiser function to fill them. ( TODO: call them through constructor )
		mv = cw.visitMethod(ACC_PUBLIC, "init_" + NameMangler.mangle(f.getName()), "()V", null, null);
		mv.visitCode();

		mv.visitTypeInsn(NEW, "org/rascalmpl/value/io/StandardTextReader");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/value/io/StandardTextReader", "<init>", "()V",false);
		mv.visitVarInsn(ASTORE, 1);		// TODO: NAME!

		if (f.constantStore.length > 0) {
			emitIntValue(f.constantStore.length);
			mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
			mv.visitFieldInsn(PUTSTATIC, fullClassName, "cs_" + NameMangler.mangle(f.getName()), "[Ljava/lang/Object;");
		}
		Label l0 = new Label();
		Label l1 = new Label();	
		Label l2 = new Label();
		Label l3 = new Label();

		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");

		mv.visitLabel(l0);

		StringWriter w = null;
		for (int i = 0; i < f.constantStore.length; i++) {
			mv.visitFieldInsn(GETSTATIC, fullClassName, "cs_" + NameMangler.mangle(f.getName()), "[Ljava/lang/Object;");
			emitIntValue(i);
			mv.visitVarInsn(ALOAD, 1);	// TODO: NAME!
			emitValueFactory();
			mv.visitTypeInsn(NEW, "java/io/StringReader");
			mv.visitInsn(DUP);

			w = new StringWriter();
			try {
				new StandardTextWriter().write(f.constantStore[i], w);
				// System.err.println(w.toString().length() + " = " + w.toString());
				emitStringValue(w.toString());
			} catch (Exception e) {
			}

			mv.visitMethodInsn(INVOKESPECIAL, "java/io/StringReader", "<init>", "(Ljava/lang/String;)V",false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/value/io/StandardTextReader", "read",
					"(Lorg/rascalmpl/value/IValueFactory;Ljava/io/Reader;)Lorg/rascalmpl/value/IValue;",false);
			mv.visitInsn(AASTORE);
		}
		/* */mv.visitInsn(NOP); // so there is no empty try block
		mv.visitLabel(l1);

		mv.visitJumpInsn(GOTO, l3);

		mv.visitLabel(l2);
		mv.visitVarInsn(ASTORE, 2);	// TODO: NAME!
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
		mv.visitVarInsn(ALOAD, 2);	// TODO: NAME!
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "getMessage", "()Ljava/lang/String;",false);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V",false);

		mv.visitLabel(l3);

		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		// Force function to be called in the constructor.
		storeCreators.add("init_" + NameMangler.mangle(f.getName()));
	}

	/*
	 * Generate a method for one RVM function
	 */
	public void emitMethod(Function f, boolean debug) {
	
		labelMap.clear(); // New set of labels.
		catchTargetLabels.clear();
		catchTargets.clear();

		// Create method	 TODO: make it private
		mv = cw.visitMethod(ACC_PUBLIC, NameMangler.mangle(f.getName()), "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;", null, null);
		mv.visitCode();

		emitExceptionTable(f.fromLabels, f.toLabels, f.fromSPsCorrected, f.types, f.handlerLabels);

		/*
		 * Fetch sp, stack from current frame and assign to local variable
		 */
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, SP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ASTORE, STACK);
		
		mv.visitInsn(ACONST_NULL);				// TODO: Is this necessary?
		mv.visitVarInsn(ASTORE, ACCU);

		/*
		 * Fetch constant store from current frame and (when present) assign to local variable
		 */
		if (f.constantStore.length != 0) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "function",
					"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;");
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function", "constantStore", "[Lorg/rascalmpl/value/IValue;");
			mv.visitVarInsn(ASTORE, CS);
		}
		/*
		 * Fetch type store from current frame and (when present) assign to local variable
		 */

		if (f.typeConstantStore.length != 0) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "function",
					"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;");
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function", "typeConstantStore", "[Lorg/rascalmpl/value/type/Type;");
			mv.visitVarInsn(ASTORE, TS);
		}

		/*
		 * Handle coroutine
		 */
		if (f.continuationPoints != 0) {
			hotEntryLabels = new Label[f.continuationPoints + 1]; // Add entry 0
			exitLabel = new Label();

			for (int i = 0; i < hotEntryLabels.length; i++)
				hotEntryLabels[i] = new Label();

			mv.visitCode();
			mv.visitVarInsn(ALOAD, CF);
			mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "hotEntryPoint", "I");
			mv.visitTableSwitchInsn(0, hotEntryLabels.length - 1, exitLabel, hotEntryLabels);

			mv.visitLabel(hotEntryLabels[0]); // Start at 'address' 0
		} else {
			exitLabel = null;
		}

		f.codeblock.genByteCode(this, debug);

		if (exitLabel != null) {
			mv.visitLabel(exitLabel);
			mv.visitVarInsn(ALOAD, THIS);
			mv.visitFieldInsn(GETFIELD, fullClassName, "PANIC", "Lorg/rascalmpl/value/IString;");
			mv.visitInsn(ARETURN);
		}
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}
	
	public void emitHotEntryJumpTable(int continuationPoints, boolean debug) {
		hotEntryLabels = new Label[continuationPoints + 1]; // Add default 0 entry point.
	}
	
	public byte[] finalizeCode() {
		if (endCode == null) {
			cw.visitEnd();
			endCode = cw.toByteArray();
		}
		return endCode;
	}
	
	/************************************************************************************************/
	/* Utilities for instruction emitters															*/
	/************************************************************************************************/

	private void emitValueFactory(){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "vf", "Lorg/rascalmpl/value/IValueFactory;");
	}
	
	private void emitRex(){
		mv.visitVarInsn(ALOAD, THIS);		// rex
		mv.visitFieldInsn(GETFIELD, fullClassName, "rex", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;");
	}
	
	public void emitIncSP(int n){
		mv.visitIincInsn(SP, n);
	}
	
	public void emitReturnValue2ACCU(){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "returnValue", "Ljava/lang/Object;");
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emitReturnNONE(){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "NONE", "Lorg/rascalmpl/value/IString;");
		mv.visitInsn(ARETURN);
	}
	
	public void emitJMP(String targetLabel) {
		Label lb = getNamedLabel(targetLabel);
		mv.visitJumpInsn(GOTO, lb);
	}
	
	private void emitIntFromTopOfStack(){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
	}
	
	private void  emitIntFromAccu(){
		mv.visitVarInsn(ALOAD, ACCU);			// ((Integer) accu).intValue()
		mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
	}
	
	private void emitObjectFromTopOfStack(){
		mv.visitVarInsn(ALOAD, STACK);			
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
	}
	
	private void emitObjectFromAccu(){
		mv.visitVarInsn(ALOAD, ACCU);			
	}
	
	private void emitGetSpfromFrame() {
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, SP);
	}

	private void emitPutSpInFrame() {
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
	}

	public void emitInlineLoadInt(int nval, boolean debug) {
		emitIntValue(nval);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;",false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	/************************************************************************************************/
	/* Emitters for various instructions															*/
	/************************************************************************************************/

	public void emitJMPTRUEorFALSE(boolean tf, String targetLabel, boolean debug) {
		Label target = getNamedLabel(targetLabel);

		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IBool", "getValue", "()Z",true);
		if (tf)
			mv.visitJumpInsn(IFNE, target);
		else
			mv.visitJumpInsn(IFEQ, target);
	}

	public void emitLabel(String targetLabel) {
		Label lb = getNamedLabel(targetLabel);
		mv.visitLabel(lb);

		ExceptionLine el = catchTargets.get(targetLabel);
		if (el != null) {
			emitCatchLabelEpilogue(el.type, el.newsp);
		}
	}
	
	// AddInt: accu = ((Integer) stack[--sp]) + ((Integer) accu)
	 
	public void emitInlineAddInt(){
		emitIntFromTopOfStack();				// left int
		emitIntFromAccu();						// right int
		
		mv.visitInsn(IADD);						// left + right
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// SubtractInt: accu = ((Integer) stack[--sp]) - ((Integer) accu)
	
	public void emitInlineSubtractInt(){
		emitIntFromTopOfStack();				// left int
		emitIntFromAccu();						// right int
		
		mv.visitInsn(ISUB);						// left - right
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// SubscriptArray: accu = ((Object[]) stack[--sp])[((Integer) accu)];
	
	public void emitInlineSubscriptArray(){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, "[Ljava/lang/Object;");
		emitIntFromAccu();
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// SubscriptList: accu = ((IList) stack[--sp]).get((Integer) accu);
	
	public void emitInlineSubscriptList(){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IList");
		emitIntFromAccu();
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IList", "get", "(I)Lorg/rascalmpl/value/IValue;", true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// LessInt: accu = vf.bool(((Integer) stack[--sp]) < ((Integer) accu));
	
	public void emitInlineLessInt(){
		emitIntFromTopOfStack();				// left int
		emitIntFromAccu();						// right int
		
		Label l1 = new Label();
		mv.visitJumpInsn(IF_ICMPGE, l1);
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMonJVM", "Rascal_TRUE", "Lorg/rascalmpl/value/IBool;");
		Label l2 = new Label();
		mv.visitJumpInsn(GOTO, l2);
		mv.visitLabel(l1);

		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMonJVM", "Rascal_FALSE", "Lorg/rascalmpl/value/IBool;");
		mv.visitLabel(l2);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// GreaterEqualInt: accu = vf.bool(((Integer) stack[--sp]) >= ((Integer) accu));
	
	public void emitInlineGreaterEqualInt(){
		emitIntFromTopOfStack();				// left int
		emitIntFromAccu();						// right int
		
		Label l1 = new Label();
		mv.visitJumpInsn(IF_ICMPLT, l1);
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMonJVM", "Rascal_TRUE", "Lorg/rascalmpl/value/IBool;");
		Label l2 = new Label();
		mv.visitJumpInsn(GOTO, l2);
		mv.visitLabel(l1);

		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMonJVM", "Rascal_FALSE", "Lorg/rascalmpl/value/IBool;");
		mv.visitLabel(l2);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// AndBool: accu = ((IBool) stack[--sp]).and((IBool) accu);
	
	public void emitInlineAndBool(){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IBool");
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IBool");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IBool", "and", "(Lorg/rascalmpl/value/IBool;)Lorg/rascalmpl/value/IBool;", true);
		mv.visitVarInsn(ASTORE, ACCU);
	}

	// SubType: accu = vf.bool(((Type) stack[--sp]).isSubtypeOf((Type) accu));
	
	public void emitInlineSubType(){
//		mv.visitVarInsn(ALOAD, THIS);			// left
//		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RVMonJVM", "vf", "Lorg/rascalmpl/value/IValueFactory;");
		
		emitValueFactory();
		
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/type/Type");
		
		mv.visitVarInsn(ALOAD, ACCU);			// right
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/type/Type");
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/value/type/Type", "isSubtypeOf", "(Lorg/rascalmpl/value/type/Type;)Z", false);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValueFactory", "bool", "(Z)Lorg/rascalmpl/value/IBool;", true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// LoadLocRef: accu = new Reference(stack, pos);
	
	public void emitInlineLoadLocRef(int pos){
		mv.visitTypeInsn(NEW, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference", "<init>", "([Ljava/lang/Object;I)V", false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// PushLocRef: stack[sp++] = new Reference(stack, pos);
	
	public void emitInlinePushLocRef(int pos){
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitTypeInsn(NEW, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference", "<init>", "([Ljava/lang/Object;I)V", false);
		mv.visitInsn(AASTORE);
	}
	
	// LoadLocDeref:
	//	Reference ref = (Reference) stack[pos];
	//	accu = ref.stack[ref.pos];

	public void emitInlineLoadLocDeref(int pos){
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitInsn(AALOAD);
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference");
		mv.visitVarInsn(ASTORE, TMP1);
		
		mv.visitVarInsn(ALOAD, TMP1);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference", "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, TMP1);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference", "pos", "I");
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// PushLocDeref:
	// Reference ref = (Reference) stack[pos];
	// stack[sp++] = ref.stack[ref.pos];
	
	public void emitInlinePushLocDeref(int pos){
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitInsn(AALOAD);
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference");
		
		mv.visitVarInsn(ASTORE, TMP1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitVarInsn(ALOAD, TMP1);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference", "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, TMP1);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference", "pos", "I");
		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
	}
	
	// StoreLocDeref:
	// Reference ref = (Reference) stack[pos];
	// ref.stack[ref.pos] = accu;

	public void emitInlineStoreLocDeref(int pos){
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitInsn(AALOAD);
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference");
		mv.visitVarInsn(ASTORE, TMP1);
		
		mv.visitVarInsn(ALOAD, TMP1);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference", "stack", "[Ljava/lang/Object;");
		mv.visitVarInsn(ALOAD, TMP1);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference", "pos", "I");
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitInsn(AASTORE);
	}
	
	public void emitInlineExhaust(boolean dcode) {		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "exhaustHelper", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;",false);
		mv.visitInsn(ARETURN);
	}

	public void emitInlineReturn(int wReturn, boolean debug) {
		if(wReturn != 0){
			mv.visitVarInsn(ALOAD, THIS);
			mv.visitVarInsn(ALOAD, ACCU);
			mv.visitFieldInsn(PUTFIELD, fullClassName, "returnValue", "Ljava/lang/Object;");
		}
		
		emitReturnNONE();
	}
	
	public void emitInlineCoReturn(int wReturn, boolean debug) {
		mv.visitVarInsn(ALOAD, THIS);

		if (wReturn == 0) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "coreturn0Helper",
					"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)V",false);
		} else {
			mv.visitVarInsn(ALOAD, STACK);
			mv.visitVarInsn(ILOAD, SP);
			mv.visitVarInsn(ALOAD, CF);
			emitIntValue(wReturn);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "coreturn1Helper",
					"([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)V",false);
		}

		emitReturnNONE();
	}
	
	public void emitInlineVisit(boolean direction, boolean progress, boolean fixedpoint, boolean rebuild, boolean debug) {
		Label returnLabel = new Label();
		Label continueLabel = new Label();
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		emitIntValue(direction ? 1 : 0);
		emitIntValue(progress ? 1 : 0);
		emitIntValue(fixedpoint ? 1 : 0);
		emitIntValue(rebuild ? 1 : 0);
	
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "VISIT", "([Ljava/lang/Object;IZZZZ)I",false);
		mv.visitInsn(DUP);
		mv.visitJumpInsn(IFLE, returnLabel);
		
		mv.visitVarInsn(ISTORE, SP);
		mv.visitJumpInsn(GOTO, continueLabel);
		
		mv.visitLabel(returnLabel);
		mv.visitInsn(INEG);
		mv.visitVarInsn(ISTORE, SP);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
		emitInlineReturn(1, debug);
		
		mv.visitLabel(continueLabel);
	}
	
	public void emitInlineCheckMemo(boolean debug) {
		Label returnLabel = new Label();
		Label continueLabel = new Label();
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "CHECKMEMO", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)I",false);
		mv.visitInsn(DUP);							// [..., sp, sp]
		mv.visitJumpInsn(IFLE, returnLabel);
													// Here sp > 0
		mv.visitVarInsn(ISTORE, SP);				// sp = sp - 1	
		mv.visitIincInsn(SP, -1);
		
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
		mv.visitJumpInsn(GOTO, continueLabel);
		
		mv.visitLabel(returnLabel);					// Here; sp <= 0
		mv.visitInsn(INEG);
		mv.visitVarInsn(ISTORE, SP);				// sp = -sp
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);				// accu = stack[sp - 1]
		emitInlineReturn(1, debug);
		
		mv.visitLabel(continueLabel);
	}
	
	public void emitInlineFailreturn() {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "FAILRETURN", "Lorg/rascalmpl/value/IString;");
		mv.visitInsn(ARETURN);
	}

	public void emitDynDispatch(int numberOfFunctions) {
		funcArray = new String[numberOfFunctions];
	}

	public void emitDynCaLL(String fname, Integer value) {
		funcArray[value] = fname;
	}

	public void emitDynFinalize() {
		int nrFuncs = funcArray.length;
		Label[] caseLabels = new Label[nrFuncs];

		for (int i = 0; i < nrFuncs; i++) {
			caseLabels[i] = new Label();
		}
		Label defaultlabel = new Label();

		mv = cw.visitMethod(ACC_PUBLIC, "dynRun", "(ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;", null, null);
		mv.visitCode();

		// Case switch on int at loc 1 (java stack)
		mv.visitVarInsn(ILOAD, 1);	// TODO: NAME!
		mv.visitTableSwitchInsn(0, nrFuncs - 1, defaultlabel, caseLabels);
		for (int i = 0; i < nrFuncs; i++) {
			mv.visitLabel(caseLabels[i]);
			mv.visitVarInsn(ALOAD, THIS);
			mv.visitVarInsn(ALOAD, 2); // TODO: NAME! BEWARE: CF in second argument differs from generated functions.
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, NameMangler.mangle(funcArray[i]), "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;",false);
			mv.visitInsn(ARETURN);
		}
		mv.visitLabel(defaultlabel);

		// Function exit
		emitValueFactory();
		mv.visitInsn(ICONST_0);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValueFactory", "bool", "(Z)Lorg/rascalmpl/value/IBool;",true);
		mv.visitInsn(ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	public void emitInlineGuard(int hotEntryPoint, boolean dcode) {
		mv.visitVarInsn(ALOAD, CF);				// cf.hotEntryPoint = hotEntryPoint
		emitIntValue(hotEntryPoint);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "hotEntryPoint", "I");
		
		mv.visitInsn(ACONST_NULL);				// coroutine = null
		mv.visitVarInsn(ASTORE, LCOROUTINE);
				
		mv.visitVarInsn(ALOAD, THIS);			
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "guardHelper", "(Ljava/lang/Object;)Z",false);
		mv.visitVarInsn(ISTORE, LPRECONDITION);	// precondition = guardHelper(accu)
		
		mv.visitVarInsn(ALOAD, CF);				// if(cf != cccf) goto l0
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cccf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		
		Label l0 = new Label();
		mv.visitJumpInsn(IF_ACMPNE, l0);
												// Here: cf == cff
		mv.visitVarInsn(ILOAD, LPRECONDITION);	// if(!precondition) goto l1

		Label l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);
												// Here: cf == cff && precondition
		mv.visitTypeInsn(NEW, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine");
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cccf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitMethodInsn(INVOKESPECIAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "<init>",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)V",false);
		mv.visitVarInsn(ASTORE, LCOROUTINE);
		mv.visitVarInsn(ALOAD, LCOROUTINE);
		mv.visitInsn(ICONST_1);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "isInitialized", "Z");
		mv.visitVarInsn(ALOAD, LCOROUTINE);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "entryFrame",
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		mv.visitVarInsn(ALOAD, LCOROUTINE);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine", "suspend",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)V",false);
		
		mv.visitLabel(l1);						// Here cf != cff && !precondition
		
		mv.visitVarInsn(ALOAD, THIS);			// cccf = null
		mv.visitInsn(ACONST_NULL);
		mv.visitFieldInsn(PUTFIELD, fullClassName, "cccf", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;");
		
		mv.visitVarInsn(ALOAD, CF);				// cf.sp = sp

		mv.visitVarInsn(ILOAD, SP);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ILOAD, LPRECONDITION);	
		
		Label l2 = new Label();
		mv.visitJumpInsn(IFEQ, l2);				// if(!precondition) goto l2
		
		mv.visitVarInsn(ALOAD, LCOROUTINE);		
		Label l3 = new Label();
		mv.visitJumpInsn(GOTO, l3);
		
		mv.visitLabel(l2);						// Here: !precondition
		mv.visitFieldInsn(GETSTATIC, fullClassName, "exhausted", "Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Coroutine;");
	
		mv.visitLabel(l3);
	
		mv.visitFieldInsn(PUTFIELD, fullClassName, "returnValue", "Ljava/lang/Object;");

		emitReturnNONE();
		
		mv.visitLabel(l0);						// Here: cf != cccf
		mv.visitVarInsn(ILOAD, LPRECONDITION);
		Label l4 = new Label();
		mv.visitJumpInsn(IFNE, l4);				// if(precondition) goto l4;
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/rascalmpl/value/IBool;");
		mv.visitFieldInsn(PUTFIELD, fullClassName, "returnValue", "Ljava/lang/Object;");
				
		mv.visitVarInsn(ALOAD, CF);				
		mv.visitVarInsn(ILOAD, SP);				// cf.sp = sp
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");

		emitReturnNONE();
		
		mv.visitLabel(l4);						// Here: precondition == true
		mv.visitLabel(hotEntryLabels[hotEntryPoint]);
	}

	public void emitInlineLoadLocN(int pos, boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	public void emitInlinePushLocN(int pos, boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
	}

	// Experimemtal local copy of constantStore and typeConstantStore probably needed in final version.
	public void emitInlineLoadConOrType(int n, boolean conOrType, boolean debug) {
		if (conOrType)
			mv.visitVarInsn(ALOAD, CS);
		else
			mv.visitVarInsn(ALOAD, TS);

		emitIntValue(n);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	public void emitInlinePushConOrType(int n, boolean conOrType, boolean debug) { 
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);

		if (conOrType)
			mv.visitVarInsn(ALOAD, CS);
		else
			mv.visitVarInsn(ALOAD, TS);

		emitIntValue(n);
		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
	}

	public void emitInlinePop(boolean debug) {
		mv.visitIincInsn(SP, -1);
	}

	public void emitInlineStoreLoc(int loc, boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(loc);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitInsn(AASTORE);
	}

	public void emitInlineTypeSwitch(IList labels, boolean dcode) {
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

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "typeSwitchHelper", "(Ljava/lang/Object;)I",false);

		mv.visitTableSwitchInsn(0, nrLabels - 1, exitLabel, switchTable);
	}

	public void emitInlineYield(int arity, int hotEntryPoint, boolean debug) {

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		if (arity > 0) {
			emitIntValue(arity);
			emitIntValue(hotEntryPoint);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "yield1Helper", "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;[Ljava/lang/Object;III)V",false);
		} else {
			emitIntValue(hotEntryPoint);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "yield0Helper", "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;[Ljava/lang/Object;II)V",false);
		}
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/rascalmpl/value/IString;");
		mv.visitInsn(ARETURN);

		mv.visitLabel(hotEntryLabels[hotEntryPoint]);
	}

	public void emitPanicReturn() {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "PANIC", "Lorg/rascalmpl/value/IString;");
		mv.visitInsn(ARETURN);
	}

	public void emitEntryLabel(int continuationPoint) {
		mv.visitLabel(hotEntryLabels[continuationPoint]);
	}
	
	public void emitOptimizedCall(String fuid, int functionIndex, int arity, int continuationPoint, boolean debug){
		switch(fuid){
		case "Library/MAKE_SUBJECT":
			emitInlineCallMAKE_SUBJECT(continuationPoint);
			return;
		
		case "Library/GET_SUBJECT_LIST":
			emitInlineCallGET_SUBJECT_LIST(continuationPoint);
			return;
			
		case "Library/GET_SUBJECT_CURSOR":
			emitInlineCallGET_SUBJECT_CURSOR(continuationPoint);
			return;
//		case "Library/ACCEPT_LIST_MATCH":
//			emitInlineCallACCEPT_LIST_MATCH(continuationPoint);
//			return;
		default:
			emitInlineCall(functionIndex, arity, continuationPoint, debug);
		}
	}
	
	public void emitInlineCallMAKE_SUBJECT(int continuationPoint){
		emitEntryLabel(continuationPoint);
		emitIntValue(2);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		
		mv.visitInsn(DUP);
		
		mv.visitVarInsn(ASTORE, ACCU);		// accu = new Object[2];
		
		mv.visitInsn(DUP);

		emitIntValue(1);
		emitObjectFromTopOfStack();
		mv.visitInsn(AASTORE);				// subject[1] = stack[--sp]  == cursor
		
		emitIntValue(0);
		emitObjectFromTopOfStack();
		mv.visitInsn(AASTORE);				// subject[0] = stack[--sp] = subject
	
	}
	
	public void emitInlineCallGET_SUBJECT_LIST(int continuationPoint){
		emitEntryLabel(continuationPoint);
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, "[Ljava/lang/Object;");
		emitIntValue(0);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ASTORE, ACCU);	
	}
	
	public void emitInlineCallGET_SUBJECT_CURSOR(int continuationPoint){
		emitEntryLabel(continuationPoint);
		
		emitObjectFromTopOfStack();
		
		mv.visitTypeInsn(CHECKCAST, "[Ljava/lang/Object;");
		emitIntValue(1);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ASTORE, ACCU);	
	}

	public void emitInlineCall(int functionIndex, int arity, int continuationPoint, boolean debug) {
		Label l0 = new Label();

		emitEntryLabel(continuationPoint);

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);

		emitIntValue(functionIndex);
		emitIntValue(arity);
		emitIntValue(continuationPoint);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "callHelper", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;III)Ljava/lang/Object;",false);
		mv.visitInsn(DUP);

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/rascalmpl/value/IString;");
		mv.visitJumpInsn(IF_ACMPNE, l0);
		
		mv.visitInsn(ARETURN);

		mv.visitLabel(l0);						// result callHelper != "YIELD"
		mv.visitInsn(POP);
		
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, SP);
		
		emitReturnValue2ACCU();
	}
	
	public void emitInlineCreateDyn(int arity, boolean debug){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F
		emitIntValue(arity); 			// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "jvmCREATEDYN", "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
		mv.visitIincInsn(SP, -arity-1);
	}

	public void emitInlineCalldyn(int arity, int continuationPoint, boolean debug) {
		Label l0 = new Label();

		emitEntryLabel(continuationPoint);

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);

		emitIntValue(arity);
		emitIntValue(continuationPoint);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "calldynHelper",
				"([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)Ljava/lang/Object;",false);
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "YIELD", "Lorg/rascalmpl/value/IString;");
		mv.visitJumpInsn(IF_ACMPNE, l0);
		mv.visitInsn(ARETURN);

		mv.visitLabel(l0);					// result callDynHelper != "YIELD"
		
		mv.visitInsn(POP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, SP);
		
		emitReturnValue2ACCU();
	}
	
	/********************************************************************************************************************/
	/*		CallMuPrim	/ PushCallMuPrim																				*/	
	/*																													*/
	/*		Emits an inline version of CallMuPrim[012N] or PushCallMuPrim[012N] instruction and uses a direct call to 	*/
	/*		the static enum execute method. In some cases, the muprim is special cased									*/
	/********************************************************************************************************************/
	
	// CallMuPrim0
	
	public void emitInlineCallMuPrim0(MuPrimitive muprim, boolean debug) {
		emitInlineCallMuPrim0General(muprim, debug);
	}

	private void emitInlineCallMuPrim0General(MuPrimitive muprim, boolean debug) {
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "execute0", "()Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU);		// accu = callMuPrim0()
	}
		
	// PushCallMuPrim0
	
	public void emitInlinePushCallMuPrim0(MuPrimitive muprim, boolean debug){
		emitInlinePushCallMuPrim0General(muprim, debug);
	}
	
	private void emitInlinePushCallMuPrim0General(MuPrimitive muprim, boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");
	
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "execute0", "()Ljava/lang/Object;",false);
		mv.visitInsn(AASTORE);				// stack[sp] = callMuPrim0()
		mv.visitIincInsn(SP, 1);			// sp += 1
	}
	
	// CallMuPrim1
	
	public void emitInlineCallMuPrim1(MuPrimitive muprim, boolean debug) {
		switch(muprim.name()){
		
		case "iterator_hasNext":
			emit_iterator_hasNext(); return;
		case "iterator_next":
			emit_iterator_next(); return;
			
		case "is_defined":
			emit_is_defined(); return;
			
		case "is_bool" :
			emit_is_predicate("isBool"); return;
		case "is_datetime":
			emit_is_predicate("isDateTime"); return;
		case "is_int":
			emit_is_predicate("isInteger"); return;
		case "is_list":
			emit_is_predicate("isList"); return;
		case "is_lrel":
			emit_is_predicate("isListRelation"); return;
		case "is_loc":
			emit_is_predicate("isSourceLocation"); return;
		case "is_map":
			emit_is_predicate("isMap"); return;
		case "is_node":
			emit_is_predicate("isNode"); return;
		case "is_num":
			emit_is_predicate("isNum"); return;
		case "is_real":
			emit_is_predicate("isReal"); return;
		case "is_rat":
			emit_is_predicate("isRational"); return;
		case "is_rel":
			emit_is_predicate("isRelation"); return;
		case "is_set":
			emit_is_predicate("isSet"); return;
		case "is_str":
			emit_is_predicate("isString"); return;
		case "is_tuple":
			emit_is_predicate("isTuple"); return;
			
		case "mint":
			emit_mint(); return;
		}
		emitInlineCallMuPrim1General(muprim, debug);
	}
	
	private void emitInlineCallMuPrim1General(MuPrimitive muprim, boolean debug) {
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "execute1", "(Ljava/lang/Object;)Ljava/lang/Object;",false);
		
		mv.visitVarInsn(ASTORE, ACCU);		// accu = callMuPrim1(arg_1)
	}
	
	private void emit_is_predicate(String predicate){
		emitValueFactory();
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IValue");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValue", "getType", "()Lorg/rascalmpl/value/type/Type;", true);
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/value/type/Type", predicate, "()Z", false);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValueFactory", "bool", "(Z)Lorg/rascalmpl/value/IBool;", true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emit_is_defined(){
		emitValueFactory();
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference");
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Reference", "isDefined", "()Z", false);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValueFactory", "bool", "(Z)Lorg/rascalmpl/value/IBool;", true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emit_iterator_hasNext(){
		emitValueFactory();
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValueFactory", "bool", "(Z)Lorg/rascalmpl/value/IBool;", true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emit_iterator_next(){
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, "java/util/Iterator");
		mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emit_mint(){
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(INSTANCEOF, "org/rascalmpl/value/IInteger");
		Label l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IInteger");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IInteger", "intValue", "()I", true);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
		mv.visitVarInsn(ASTORE, ACCU);
		
		mv.visitLabel(l1);
	}
	
	// PushCallMuPrim1
	
	public void emitInlinePushCallMuPrim1(MuPrimitive muprim, boolean debug) {
		emitInlinePushCallMuPrim1General(muprim, debug);
	}
	
	private void emitInlinePushCallMuPrim1General(MuPrimitive muprim, boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "execute1", "(Ljava/lang/Object;)Ljava/lang/Object;",false);
		
		mv.visitInsn(AASTORE);				// stack[sp] = callMuPrim1(arg_1)
		mv.visitIincInsn(SP, 1);			// sp += 1
	}
	
	// CallMuPrim2
	
	public void emitInlineCallMuPrim2(MuPrimitive muprim, boolean debug) {
		switch(muprim.name()){
		
		case "equal_mint_mint":
			emitComparisonMinMint(IF_ICMPNE); return; 
		case "greater_equal_mint_mint":
			emitComparisonMinMint(IF_ICMPLT); return; 
		case "greater_mint_mint":
			emitComparisonMinMint(IF_ICMPLE); return;
		case "less_equal_mint_mint":
			emitComparisonMinMint(IF_ICMPGT); return; 
		case "less_mint_mint":
			emitComparisonMinMint(IF_ICMPGE); return; 
		case "not_equal_mint_mint":
			emitComparisonMinMint(IF_ICMPEQ); return; 

		case "size_array":
			emit_size_array(); return;
		case "size_list":
			emit_size("IList"); return;
		case "size_set":
			emit_size("ISet"); return;
		case "size_map":
			emit_size("IMap"); return;
		case "size_str":
			emit_size("IString"); return;
		case "size_tuple":
			emit_size("ITuple"); return;
			
		case "subscript_array_mint":
			emitInlineCallMuPrim2_subscript_array_mint(debug);
			return;
			
		case "subscript_list_mint":
			emitInlineCallMuPrim2_subscript_list_mint(debug);
			return;
		}
		emitInlineCallMuPrim2General(muprim, debug);
	}
	
	private void emitInlineCallMuPrim2General(MuPrimitive muprim, boolean debug) {
		mv.visitIincInsn(SP, -1);			// sp -= 1
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");
		
		mv.visitVarInsn(ALOAD, STACK);		// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "execute2", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",false);
		
		mv.visitVarInsn(ASTORE, ACCU);		// accu = callMuPrim2(arg_2, arg_1)
	}
	
	// Special cases
	
	private void emitComparisonMinMint(int if_op){
		emitValueFactory();
		
		emitIntFromTopOfStack();            // arg_2
		emitIntFromAccu();					// arg_1
		
		Label l1 = new Label();
		mv.visitJumpInsn(if_op, l1);
		mv.visitInsn(ICONST_1);
		Label l2 = new Label();
		mv.visitJumpInsn(GOTO, l2);
		mv.visitLabel(l1);
		mv.visitInsn(ICONST_0);
		mv.visitLabel(l2);

		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValueFactory", "bool", "(Z)Lorg/rascalmpl/value/IBool;", true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emit_size_array(){
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, "[Ljava/lang/Object;");
		mv.visitInsn(ARRAYLENGTH);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emit_size(String type){
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/" + type);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/" + type, "length", "()I", true);
		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// accu = ((Object[]) stack[--sp])[((int) arg_1)];
	
	private void emitInlineCallMuPrim2_subscript_array_mint(boolean debug){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, "[Ljava/lang/Object;");
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emitInlineCallMuPrim2_subscript_list_mint(boolean debug){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IList");
		emitIntFromAccu();
		
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IList", "get", "(I)Lorg/rascalmpl/value/IValue;", true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// PushCallMuPrim2
	
	public void emitInlinePushCallMuPrim2(MuPrimitive muprim, boolean debug) {
		switch(muprim.name()){
		case "subscript_array_mint":
			emitInlinePushCallMuPrim2_subscript_array_mint(debug);
			return;
		}
		emitInlinePushCallMuPrim2General(muprim, debug);
	}
	
	private void emitInlinePushCallMuPrim2General(MuPrimitive muprim, boolean debug) {
		mv.visitIincInsn(SP, -1);			// sp -= 1
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");
		
		mv.visitVarInsn(ALOAD, STACK);		// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "execute2", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",false);
		
		mv.visitInsn(AASTORE);				// stack[sp] = callMuPrim2(arg_2, arg_1)
		mv.visitIincInsn(SP, 1);			// sp++
	}
	
	// Special cases
	
	// stack[sp++] = ((Object[]) stack[sp - 1])[((int) accu)];
	
	private void emitInlinePushCallMuPrim2_subscript_array_mint(boolean debug){
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(ICONST_M1);
		mv.visitInsn(IADD);	
		mv.visitInsn(AALOAD);
		mv.visitTypeInsn(CHECKCAST, "[Ljava/lang/Object;");

		emitIntFromAccu();
		mv.visitInsn(AALOAD);

		mv.visitInsn(AASTORE);
		mv.visitIincInsn(SP, 1);
	}
	
	// CallMuPrimN
	
	public void emitInlineCallMuPrimN(MuPrimitive muprim, int arity, boolean debug) {
		emitInlineCallMuPrimNGeneral(muprim, arity, debug);
	}

	private void emitInlineCallMuPrimNGeneral(MuPrimitive muprim, int arity, boolean debug) {
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");

		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp

		emitIntValue(arity);				// arity

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "executeN", "([Ljava/lang/Object;II)I",false);
											// sp = callMuPrimN(stack, sp, arity)
		mv.visitInsn(ICONST_M1);
		mv.visitInsn(IADD);					// sp--
		mv.visitVarInsn(ISTORE, SP);		// sp = callMuPrimN(stack, sp, arity) - 1
		
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);		// accu = stack[--sp];
	}

	// PushCallMuPrimN
	
	public void emitInlinePushCallMuPrimN(MuPrimitive muprim, int arity, boolean debug) {
		emitInlinePushCallMuPrimNGeneral(muprim, arity, debug);
	}
	
	private void emitInlinePushCallMuPrimNGeneral(MuPrimitive muprim, int arity, boolean debug) {
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", muprim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive;");

		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp

		emitIntValue(arity);				// arity

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/MuPrimitive", "executeN", "([Ljava/lang/Object;II)I",false);
		mv.visitVarInsn(ISTORE, SP);		// sp = callMuPrimN(stach, sp, arity)
	}
	
	/********************************************************************************************************************/
	/*		CallPrim	/ PushCallPrim																					*/	
	/*																													*/
	/*		Emits an inline version of CallPrim[012N] or PushCallPrim[012N] instruction and uses a direct call to 		*/
	/*		the static enum execute method. In some cases, the muprim is special cased									*/
	/********************************************************************************************************************/
	
	// CallPrim0
	
	public void emitInlineCallPrim0(RascalPrimitive prim, boolean debug) {
		emitInlineCallPrim0General(prim, debug);
	}
	
	private void emitInlineCallPrim0General(RascalPrimitive prim, boolean debug) {
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");
		
		mv.visitVarInsn(ALOAD, CF);			// currentFrame
		
		emitRex();

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "execute0",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)Ljava/lang/Object;",false);
		
		mv.visitVarInsn(ASTORE, ACCU);		// accu = callPrim0()
	}
	
	// PushCallPrim0
	
	public void emitInlinePushCallPrim0(RascalPrimitive prim, boolean debug) {
		emitInlinePushCallPrim0General(prim, debug);
	}
	
	private void emitInlinePushCallPrim0General(RascalPrimitive prim, boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");
		
		mv.visitVarInsn(ALOAD, CF);			// currentFrame
		
		emitRex();

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "execute0",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)Ljava/lang/Object;",false);
		
		mv.visitInsn(AASTORE);				// stack[sp] callPrim0()
		mv.visitIincInsn(SP, 1);			// sp += 1
	}
	
	// CallPrim1
	
	public void emitInlineCallPrim1(RascalPrimitive prim, boolean debug) {
		emitInlineCallPrim1General(prim, debug);
	}
	
	private void emitInlineCallPrim1General(RascalPrimitive prim, boolean debug) {
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		
		mv.visitVarInsn(ALOAD, CF);		// currentFrame

		emitRex();

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "execute1",
				"(Ljava/lang/Object;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)Ljava/lang/Object;",false);
		
		mv.visitVarInsn(ASTORE, ACCU);		// accu = callPrim1(arg_1)
	}
	
	// PushCallPrim1
	
	public void emitInlinePushCallPrim1(RascalPrimitive prim, boolean debug) {
		emitInlinePushCallPrim1General(prim, debug);
	}
	
	private void emitInlinePushCallPrim1General(RascalPrimitive prim, boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		
		mv.visitVarInsn(ALOAD, CF);		// currentFrame

		emitRex();

		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "execute1",
				"(Ljava/lang/Object;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)Ljava/lang/Object;",false);
		
		mv.visitInsn(AASTORE);			// stack[sp++] = callPrim1(arg_1)
		mv.visitIincInsn(SP, 1);
	}
	
	// CallPrim2
	
	public void emitInlineCallPrim2(RascalPrimitive prim, boolean debug) {
		switch(prim.name()){
			case "subtype_value_type":
				emitInlineCallPrim2_subtype_value_type();
				return;
			case "subtype_value_value":
				emitInlineCallPrim2_subtype_value_value();
				return;
		}
	
		emitInlineCallPrim2General(prim, debug);
	}
	
	private void emitInlineCallPrim2General(RascalPrimitive prim, boolean debug) {
		mv.visitIincInsn(SP, -1);
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");
		
		mv.visitVarInsn(ALOAD, STACK);	// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		
		mv.visitVarInsn(ALOAD, CF);		// currentFrame

		emitRex();
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "execute2",
				"(Ljava/lang/Object;Ljava/lang/Object;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)Ljava/lang/Object;",false);
		
		mv.visitVarInsn(ASTORE, ACCU);	// accu = CallPrim2(arg_2, arg_1)
	}
	
	private void emitInlineCallPrim2_subtype_value_type(){
		emitValueFactory();
		
		emitObjectFromTopOfStack();		// arg_2
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IValue");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValue", "getType", "()Lorg/rascalmpl/value/type/Type;", true);
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/type/Type");
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/value/type/Type", "isSubtypeOf", "(Lorg/rascalmpl/value/type/Type;)Z", false);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValueFactory", "bool", "(Z)Lorg/rascalmpl/value/IBool;", true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emitInlineCallPrim2_subtype_value_value(){
		emitValueFactory();
		
		emitObjectFromTopOfStack();		// arg_2
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IValue");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValue", "getType", "()Lorg/rascalmpl/value/type/Type;", true);
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IValue");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValue", "getType", "()Lorg/rascalmpl/value/type/Type;", true);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/value/type/Type", "isSubtypeOf", "(Lorg/rascalmpl/value/type/Type;)Z", false);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValueFactory", "bool", "(Z)Lorg/rascalmpl/value/IBool;", true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// PushCallPrim2
	
	public void emitInlinePushCallPrim2(RascalPrimitive prim, boolean debug) {
		switch(prim.name()){
		case "subtype_value_type":
			emitInlinePushCallPrim2_subtype_value_type();
			return;
		case "subtype_value_value":
			emitInlinePushCallPrim2_subtype_value_value();
			return;
		}
		emitInlinePushCallPrim2General(prim, debug);
	}
	
	private void emitInlinePushCallPrim2General(RascalPrimitive prim, boolean debug) {
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");
		
		mv.visitVarInsn(ALOAD, STACK);	// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		
		mv.visitVarInsn(ALOAD, CF);		// currentFrame

		emitRex();
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "execute2",
				"(Ljava/lang/Object;Ljava/lang/Object;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)Ljava/lang/Object;",false);
		
		mv.visitInsn(AASTORE);
		mv.visitIincInsn(SP, 1);
	}
	
	private void emitInlinePushCallPrim2_subtype_value_type(){
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		
		emitValueFactory();
		
		mv.visitVarInsn(ALOAD, STACK);	// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IValue");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValue", "getType", "()Lorg/rascalmpl/value/type/Type;", true);
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/type/Type");
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/value/type/Type", "isSubtypeOf", "(Lorg/rascalmpl/value/type/Type;)Z", false);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValueFactory", "bool", "(Z)Lorg/rascalmpl/value/IBool;", true);
		mv.visitInsn(AASTORE);
		mv.visitIincInsn(SP, 1);
	}
	
	private void emitInlinePushCallPrim2_subtype_value_value(){
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		
		emitValueFactory();
		
		mv.visitVarInsn(ALOAD, STACK);	// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IValue");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValue", "getType", "()Lorg/rascalmpl/value/type/Type;", true);
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IValue");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValue", "getType", "()Lorg/rascalmpl/value/type/Type;", true);
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/value/type/Type", "isSubtypeOf", "(Lorg/rascalmpl/value/type/Type;)Z", false);
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValueFactory", "bool", "(Z)Lorg/rascalmpl/value/IBool;", true);
		
		mv.visitInsn(AASTORE);
		mv.visitIincInsn(SP, 1);
	}
	
	// CallPrimN
	
	public void emitInlineCallPrimN(RascalPrimitive prim, int arity, boolean debug) {
		emitInlineCallPrimNGeneral(prim, arity, debug);
	}
	
	private void emitInlineCallPrimNGeneral(RascalPrimitive prim, int arity, boolean debug) {
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		emitIntValue(arity);

		mv.visitVarInsn(ALOAD, CF);
		
		emitRex();
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "executeN",
				"([Ljava/lang/Object;IILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)I",false);
		
		mv.visitInsn(ICONST_M1);
		mv.visitInsn(IADD);					// sp--
		mv.visitVarInsn(ISTORE, SP);		// sp = callPrimN(stack, sp, arity, cf, rex) - 1
		
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);		// accu = stack[--sp];
	}
	
	// PushCallPrimN
	
	public void emitInlinePushCallPrimN(RascalPrimitive prim, int arity, boolean debug) {
		emitInlinePushCallPrimNGeneral(prim, arity, debug);
	}
	
	private void emitInlinePushCallPrimNGeneral(RascalPrimitive prim, int arity, boolean debug) {
		mv.visitFieldInsn(GETSTATIC, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", prim.name(),
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive;");

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		emitIntValue(arity);

		mv.visitVarInsn(ALOAD, CF);
		
		emitRex();
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalPrimitive", "executeN",
				"([Ljava/lang/Object;IILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/RascalExecutionContext;)I",false);
		mv.visitVarInsn(ISTORE, SP);
	}
	
	// End of CallMuPrim[012N] / PushCallMuPrim[012N]
	
	public void emitInlineLoadBool(boolean b, boolean debug) {
		if (b) {
			mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", "Lorg/rascalmpl/value/IBool;");
		} else {
			mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/rascalmpl/value/IBool;");
		}
		mv.visitVarInsn(ASTORE, ACCU);		// accu = bool;
	}

	/*
	 * emitOptimizedOcall emits a call to a full ocall implementation unless: 
	 * 1: There is only one function => emit direct call  (DONE) 
	 * 2: There is only a constructor => call constructor (DONE)
	 */
	public void emitOptimizedOcall(String fuid, int overloadedFunctionIndex, int arity, boolean dcode) {
		OverloadedFunction of = overloadedStore.get(overloadedFunctionIndex);
		int[] functions = of.getFunctions();
		if (functions.length == 1) {
			int[] ctors = of.getConstructors();
			if (ctors.length == 0) {
				Function fu = functionStore.get(functions[0]);
				if (of.getScopeFun().equals("")) {
					emitOcallSingle(NameMangler.mangle(fu.getName()), functions[0], arity);
				} else {
					// Nested function needs link to containing frame
					emitCallWithArgsSSFII_A("jvmOCALL", overloadedFunctionIndex, arity, dcode);
					mv.visitIincInsn(SP, -arity);
					emitReturnValue2ACCU();
				}
			} else {
				// Has a constructor.
				emitCallWithArgsSSFII_A("jvmOCALL", overloadedFunctionIndex, arity, dcode);
				mv.visitIincInsn(SP, -arity);
				emitReturnValue2ACCU();
			}
		} else {
			if(functions.length == 0 && of.getConstructors().length == 1){
				// Specialize for single constructor
				emitCallWithArgsSSFII_A("jvmOCALLSingleConstructor", overloadedFunctionIndex, arity, dcode);
			} else {
				emitCallWithArgsSSFII_A("jvmOCALL", overloadedFunctionIndex, arity, dcode);
			}
			mv.visitIincInsn(SP, -arity);
			emitReturnValue2ACCU();
		}
	}

	private void emitOcallSingle(String funName, int fun, int arity) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "functionStore", "Ljava/util/ArrayList;");
		emitIntValue(fun);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "get", "(I)Ljava/lang/Object;",false);

		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function");
		mv.visitVarInsn(ALOAD, CF);
		
		emitIntValue(arity);
		mv.visitVarInsn(ILOAD, SP);

		mv.visitMethodInsn(
				INVOKEVIRTUAL,
				"org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame",
				"getFrame",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;",false);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, funName, "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)Ljava/lang/Object;",false);
		mv.visitInsn(POP);

		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ISTORE, SP);
		
		emitReturnValue2ACCU();
	}

	public void emitInlineSwitch(IMap caseLabels, String caseDefault, boolean concrete, boolean debug) {
		Map<Integer, String> jumpBlock = new TreeMap<Integer, String>(); // This map is sorted on its keys.

		int nrLabels = caseLabels.size();

		Label[] switchTable = new Label[nrLabels];
		int[] intTable = new int[nrLabels];

		for (IValue vlabel : caseLabels) {
			jumpBlock.put(((IInteger) vlabel).intValue(), ((IString) caseLabels.get(vlabel)).getValue());
		}

		nrLabels = 0;
		for (Map.Entry<Integer, String> entry : jumpBlock.entrySet()) {
			intTable[nrLabels] = entry.getKey();
			switchTable[nrLabels++] = getNamedLabel(entry.getValue());
		}

		Label trampolineLabel = getNamedLabel(caseDefault + "_trampoline");

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, ACCU);

		if (concrete)
			mv.visitInsn(ICONST_1);
		else
			mv.visitInsn(ICONST_0);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "switchHelper", "(Ljava/lang/Object;Z)I",false);
		mv.visitLookupSwitchInsn(trampolineLabel, intTable, switchTable);

		emitLabel(caseDefault + "_trampoline");

		// In case of default push RASCAL_FALSE on stack. Ask Paul why?.. done
//		mv.visitVarInsn(ALOAD, STACK);
//		mv.visitVarInsn(ILOAD, SP);
//		mv.visitIincInsn(SP, 1);
//		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/rascalmpl/value/IBool;");
//		mv.visitInsn(AASTORE);

		emitJMP(caseDefault);
	}



	private void emitExceptionTable(String[] fromLabels, String[] toLabels, int[] fromSp, int[] types, String[] handlerLabels) {
		//System.err.println("emitExceptionTable " + toLabels + " " + fromSp + " " + types + " " + handlerLabels);;
		int len = handlerLabels.length;
		for (int i = 0; i < len; i++) {

			if(fromLabels[i].startsWith("FINALLY") && toLabels[i].startsWith("FINALLY")){	// Finally clauses are expanded inline by Rascal compiler and can be ignored here
//				System.err.println("*** Skip in " + currentFunction.getName() + ": " + fromLabels[i] + " to " + toLabels[i] + " handled by " + handlerLabels[i]);
				continue;
			}

			String toLab = toLabels[i];			// TRY_TO_L
			toLab = toLab.substring(toLab.lastIndexOf("_"), toLab.length());

			Label fromLabel = getNamedLabel(fromLabels[i]);
			Label toLabel = getNamedLabel(toLabels[i]);
			Label handlerLabel = getNamedLabel(handlerLabels[i]);

			catchTargetLabels.add(handlerLabels[i]);
			catchTargets.put(handlerLabels[i], new ExceptionLine(handlerLabels[i], fromSp[i], types[i]));

			//System.err.println("*** Add in " + currentFunction.getName() + ": " + fromLabels[i] + " to " + toLabels[i] + " handled by " + handlerLabels[i] );

			mv.visitTryCatchBlock(fromLabel, toLabel, handlerLabel, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Thrown");
		}
	}

	public void emitCatchLabelEpilogue(int type, int newsp) {

		// --- Code implements.
		// catch(Thrown e) {
		// sp = newsp ;
		// stack[sp++] = e ;
		// if ( ! e.value.getType().isSubtypeOf(cf.function.typeConstantStore[type]) )
		// throw e ;
		// }
		// .....
	
		Label noReThrow = new Label();

		mv.visitVarInsn(ASTORE, EXCEPTION);

		emitIntValue(newsp);

		mv.visitVarInsn(ISTORE, SP);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitVarInsn(ALOAD, EXCEPTION);
		mv.visitInsn(AASTORE);
		mv.visitVarInsn(ALOAD, EXCEPTION);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Thrown", "value", "Lorg/rascalmpl/value/IValue;");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValue", "getType", "()Lorg/rascalmpl/value/type/Type;",true);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "function",
				"Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function;");
		mv.visitFieldInsn(GETFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Function", "typeConstantStore", "[Lorg/rascalmpl/value/type/Type;");

		emitIntValue(type);

		mv.visitInsn(AALOAD);
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/value/type/Type", "isSubtypeOf", "(Lorg/rascalmpl/value/type/Type;)Z",false);
		mv.visitJumpInsn(IFNE, noReThrow);
		mv.visitVarInsn(ALOAD, EXCEPTION);
		mv.visitInsn(ATHROW);
		mv.visitLabel(noReThrow);
	}

	public void emitInlineThrow(boolean debug) {
		mv.visitVarInsn(ALOAD, CF);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitFieldInsn(PUTFIELD, "org/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame", "sp", "I");
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "thrownHelper",
				"(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;[Ljava/lang/Object;I)Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Thrown;",false);
		mv.visitInsn(ATHROW);
	}
	
	private class ExceptionLine {
		@SuppressWarnings("unused")
		String catchLabel;
		int newsp;
		int type;

		public ExceptionLine(String lbl, int sp, int type) {
			this.catchLabel = lbl;
			this.newsp = sp;
			this.type = type;
		}
	}
	
	public void emitInlineResetLoc(int position, boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(position);
		mv.visitInsn(ACONST_NULL);
		mv.visitInsn(AASTORE);
	}

	public void emitInlineResetLocs(int positions, IValue constantValues, boolean debug) {
		IList il = (IList) constantValues;
		for (IValue v : il) {
			int stackPos = ((IInteger) v).intValue();
			mv.visitVarInsn(ALOAD, STACK);
			emitIntValue(stackPos);
		}
		mv.visitInsn(ACONST_NULL);

		for (int i = 1; i < il.length(); i++) {
			mv.visitInsn(DUP_X2);
			mv.visitInsn(AASTORE);
		}
		mv.visitInsn(AASTORE);
	}
	
	public void emitInlineResetVar(int what, int pos, boolean debug) {
		mv.visitVarInsn(ALOAD, THIS);
		emitIntValue(what);
		emitIntValue(pos);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "jvmRESETVAR", "(IILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)V",false);
	}

	

	// TODO: compare with performance of insnCHECKARGTYPEANDCOPY
	public void emitInlineCheckArgTypeAndCopy(int pos1, int type, int pos2, boolean debug) {
		Label l1 = new Label();
		Label l5 = new Label();

		mv.visitVarInsn(ALOAD, STACK);

		/* sourceLoc */
		emitIntValue(pos1);

		mv.visitInsn(AALOAD);
//		mv.visitTypeInsn(CHECKCAST, "org/rascalmpl/value/IValue");
		mv.visitMethodInsn(INVOKEINTERFACE, "org/rascalmpl/value/IValue", "getType", "()Lorg/rascalmpl/value/type/Type;",true);
		mv.visitVarInsn(ALOAD, TS);

		/* type */
		emitIntValue(type);

		mv.visitInsn(AALOAD);
		mv.visitMethodInsn(INVOKEVIRTUAL, "org/rascalmpl/value/type/Type", "isSubtypeOf", "(Lorg/rascalmpl/value/type/Type;)Z",false);
		mv.visitJumpInsn(IFEQ, l1);
		mv.visitVarInsn(ALOAD, STACK);

		/* toloc */
		emitIntValue(pos2);

		mv.visitVarInsn(ALOAD, STACK);

		/* sourceLoc */
		emitIntValue(pos1);

		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
		
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", "Lorg/rascalmpl/value/IBool;");
		mv.visitVarInsn(ASTORE, ACCU);
		mv.visitJumpInsn(GOTO, l5);
		
		mv.visitLabel(l1);
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", "Lorg/rascalmpl/value/IBool;");
		mv.visitVarInsn(ASTORE, ACCU);
		mv.visitLabel(l5);
	}

	public void emitInlinePushEmptyKwMap(boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitTypeInsn(NEW, "java/util/HashMap");
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V",false);
		mv.visitInsn(AASTORE);
		mv.visitIincInsn(SP, 1);
	}

	// TODO: eliminate call
	public void emitInlineValueSubtype(int type, boolean debug) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, TS);
		emitIntValue(type);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "VALUESUBTYPE", "(Lorg/rascalmpl/value/type/Type;Ljava/lang/Object;)Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU);
	}

	public void emitInlinePopAccu(boolean debug) {
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}

	public void emitInlinePushAccu(boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitInsn(AASTORE);
		mv.visitIincInsn(SP, 1);
	}

	/********************************************************************************************/
	/*	Emit calls with various parameter combinations											*/
	/*  S:  STACK																				*/
	/*  SS: STACK followed by stack pointer SP													*/
	/*  F:	CF, current frame																	*/
	/*  I:  int																					*/
	/*  P:  value pushed on the stack															*/
	/*  A:  ACCU																				*/
	/*																							*/
	/* emitVoidCallWithArgs* calls a void function												*/
	/* emitCallWithArgs*_A calls a non-void function and leaves result in ACCU					*/
	/* emitCallWithArgs*_S calls a non-void function and leaves result on the STACK				*/
	/********************************************************************************************/
	
	public void emitCallWithArgsA_A(String fname) {
		mv.visitVarInsn(ALOAD, THIS);
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(Ljava/lang/Object;)Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	public void emitCallWithArgsPA_A(String fname) {
		mv.visitVarInsn(ALOAD, THIS);
		
		mv.visitIincInsn(SP, -1);		// sp--
		
		mv.visitVarInsn(ALOAD, STACK);	
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);			// P: arg_2
		
		mv.visitVarInsn(ALOAD, ACCU);	// A: arg_1 from accu
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}

	public void emitCallWithArgsSSI_S(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		emitIntValue(i);				// I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;II)I",false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsSI_A(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		emitIntValue(i);				// I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;I)Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}

	public void emitCallWithArgsSSII_S(String fname, int i, int j, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		emitIntValue(i);				// I
		emitIntValue(j);				// I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;III)I",false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsSSII_A(String fname, int i, int j, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		emitIntValue(i);				// I
		emitIntValue(j);				// I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;III)I",false);
		
		mv.visitVarInsn(ISTORE, SP);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}

	public void emitCallWithArgsSSFI_S(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)I",false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsFI_A(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitCallWithArgsSSFI_A(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitCallWithArgsSFI_A(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;I)Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitCallWithArgsSSFIII_A(String fname, int i, int j, int k, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		emitIntValue(j); 				// I
		emitIntValue(k); 				// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;III)Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}

	public void emitVoidCallWithArgsSSI_S(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		emitIntValue(i);				// I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;II)V",false);
	}

	public void emitCallWithArgsSSFII_S(String fname, int i, int j, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		emitIntValue(j); 				// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)I",false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsSSFII_A(String fname, int i, int j, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		emitIntValue(j); 				// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitCallWithArgsFII_A(String fname, int i, int j, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		
		mv.visitVarInsn(ALOAD, CF);		// F

		emitIntValue(i); 				// I
		emitIntValue(j); 				// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitVoidCallWithArgsFIIA(String fname, int what, int pos, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(what); 			// I
		emitIntValue(pos); 				// I
		mv.visitVarInsn(ALOAD, ACCU);	// A

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IILjava/lang/Object;)V",false);
	}

	public void emitCallWithArgsSSFIIIII_S(String fname, int methodName, int className, int parameterTypes, int keywordTypes, int reflect, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(methodName); 		// I
		emitIntValue(className); 		// I
		emitIntValue(parameterTypes); 	// I
		emitIntValue(keywordTypes); 	// I
		emitIntValue(reflect); // I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;IIIII)I",false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitVoidCallWithArgsSFIA(String fname, int pos, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ALOAD, CF); 	// F
		emitIntValue(pos); 				// I
		mv.visitVarInsn(ALOAD, ACCU);	// A
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;ILjava/lang/Object;)V",false);
	}
	
	public void emitVoidCallWithArgsFIA(String fname, int pos, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF); 	// F
		emitIntValue(pos); 				// I
		mv.visitVarInsn(ALOAD, ACCU);	// A
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;ILjava/lang/Object;)V",false);
	}
	
	public void emitVoidCallWithArgsFII(String fname, int scope, int pos, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF); 	// F
		emitIntValue(scope); 			// I
		emitIntValue(pos); 				// I	
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;II)V",false);
	}


	public void emitCallWithArgsSSF_S(String fname, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP);    	// S

		mv.visitVarInsn(ALOAD, CF);		// F

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)I",false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsSSF_A(String fname, boolean dcode) {
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP);    	// S

		mv.visitVarInsn(ALOAD, CF);		// F

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "([Ljava/lang/Object;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;)I",false);
		mv.visitVarInsn(ISTORE, SP);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK); 
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU); 
	}
	
	public void emitCallWithArgsFA_A(String fname, boolean dcode) {
		
		mv.visitVarInsn(ALOAD, THIS);

		mv.visitVarInsn(ALOAD, CF);		// F
		mv.visitVarInsn(ALOAD, ACCU);	// A

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, "(Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;Ljava/lang/Object;)Ljava/lang/Object;",false);
		mv.visitVarInsn(ASTORE, ACCU); 
	}
	
	// Debug calls for tracing the execution of RVM instructions
	// Output resembles outbut of the debugRVM flag so traces can be compared.
	
	private final int MAXLEN = 80;
	
	public String abbrev(String sval){
		if(sval.length() > MAXLEN){
			sval = sval.substring(0, MAXLEN) + " ...";
		}
		return sval;
	}
	
	public void emitDebugCall(String ins) {
		mv.visitLdcInsn(ins);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKESTATIC, fullClassName, "debugINSTRUCTION", "(Ljava/lang/String;Lorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;ILjava/lang/Object;)V",false);
	}
	
	public void emitDebugCall1(String ins, int arg1) {
		mv.visitLdcInsn(ins);
		mv.visitLdcInsn(arg1);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKESTATIC, fullClassName, "debugINSTRUCTION1", "(Ljava/lang/String;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;ILjava/lang/Object;)V",false);
	}
	
	public void emitDebugCall2(String ins, String arg1, int arg2) {
		mv.visitLdcInsn(ins);
		mv.visitLdcInsn(abbrev(arg1));
		mv.visitLdcInsn(arg2);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKESTATIC, fullClassName, "debugINSTRUCTION2", "(Ljava/lang/String;Ljava/lang/String;ILorg/rascalmpl/library/experiments/Compiler/RVM/Interpreter/Frame;ILjava/lang/Object;)V",false);
	}
}
