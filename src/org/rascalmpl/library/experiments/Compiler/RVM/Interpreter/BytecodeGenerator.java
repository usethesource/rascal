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


package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jruby.org.objectweb.asm.Type;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.IValueFactory;

import com.ibm.icu.util.BytesTrie.Iterator;

public class BytecodeGenerator implements Opcodes {

  // constant references to types used in emitting method calls and field references
  private static final Type BOOLEAN_TYPE = Type.BOOLEAN_TYPE;
  private static final Type OBJECT_A_TYPE = Type.getType(Object[].class);
  private static final Type IVALUE_TYPE = Type.getType(IValue.class);
  private static final String FRAME_NAME = Type.getInternalName(Frame.class);
  private static final String FUNCTION_NAME = Type.getInternalName(Function.class);
  private static final Type FUNCTION_TYPE = Type.getType(Function.class);
  private static final Type TYPE_TYPE = Type.getType(org.rascalmpl.value.type.Type.class);
  private static final String INIT_NAME = "<init>";
  private static final Type INT_TYPE = Type.INT_TYPE;
  private static final Type VOID_TYPE = Type.VOID_TYPE;
  private static final Type FRAME_TYPE = Type.getType(Frame.class);
  private static final Type OBJECT_TYPE = Type.getType(Object.class);
 
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
	private String className = null;
	private String packageName = null;
	private String fullClassName = null;

	private String[] funcArray = null;			// List of compiler-generated function names

	// Administration per function
	private HashMap<String, Label> labelMap = new HashMap<String, Label>();
	private Set<String> catchTargetLabels = new HashSet<String>();			
	private Map<String, ExceptionLine> catchTargets = new HashMap<String, ExceptionLine>();
	private Label[] hotEntryLabels = null;		// entry labels for coroutines
	private Label exitLabel = null;				// special case for labels without code
												// TODO: peephole optimizer now removes them; can disappear?

	Function[] functionStore;
	OverloadedFunction[] overloadedStore;
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

	public BytecodeGenerator(Function[] functionStore, OverloadedFunction[] overloadedStore,
			Map<String, Integer> functionMap, Map<String, Integer> constructorMap, Map<String, Integer> resolver) {

		this.functionStore = functionStore;
		this.overloadedStore = overloadedStore;
		this.functionMap = functionMap;
		this.resolver = resolver;
		this.constructorMap = constructorMap;
	}

	//Function currentFunction;
	
	public void buildClass(String packageName, String className, boolean debug) {

		emitClass(packageName,className);

		for (Function f : functionStore) {
			//System.err.println(f.getName());
//			currentFunction = f;
		    emitMethod(f, debug);
			//System.err.println(f.toString() );
		}
		
		// All functions are created create int based dispatcher
		if(functionMap.size() > 0){
			emitDynDispatch(functionMap.size());
			for (Map.Entry<String, Integer> e : functionMap.entrySet()) {
				String fname = e.getKey();
				emitDynCaLL(fname, e.getValue());
			}
			emitDynFinalize();
		}

		OverloadedFunction[] overloadedStoreV2 = new OverloadedFunction[overloadedStore.length];
		emitConstructor(overloadedStoreV2);
	}

	public String finalName() {
		return fullClassName;
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

	/*
	 * Generate the constructor method for the generated class
	 */
	public void emitConstructor(OverloadedFunction[] overloadedStore) {

		mv = cw.visitMethod(ACC_PUBLIC, INIT_NAME, 
				Type.getMethodDescriptor(VOID_TYPE, Type.getType(RVMExecutable.class), Type.getType(RascalExecutionContext.class)),
				null, null);

		mv.visitCode();
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, RVM_EXEC); 
		mv.visitVarInsn(ALOAD, RVM_REX);
		mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(RVMonJVM.class), INIT_NAME,
				Type.getMethodDescriptor(VOID_TYPE, Type.getType(RVMExecutable.class), Type.getType(RascalExecutionContext.class)),false);

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
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, fullClassName, null, Type.getInternalName(RVMonJVM.class), null);
	}
	
	/*
	 * Dump the code of the generated class for debugging purposes
	 */
	
	public void dumpClass() {
		if (endCode == null) {
			finalizeCode();
		}
		
		try {
			FileOutputStream fos = new FileOutputStream("/tmp/Class.jvm");
			fos.write(endCode);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Generate a method for one RVM function
	 */
	public void emitMethod(Function f, boolean debug) {
	System.err.println("Emitting " + f);
	debug = true;
		labelMap.clear(); // New set of labels.
		catchTargetLabels.clear();
		catchTargets.clear();

		// Create method	 TODO: make it private
		mv = cw.visitMethod(ACC_PUBLIC, rvm2jvmName(f.getName()), Type.getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE), null, null);
		mv.visitCode();

		emitExceptionTable(f.fromLabels, f.toLabels, f.fromSPsCorrected, f.types, f.handlerLabels);

		/*
		 * Fetch sp, stack from current frame and assign to local variable
		 */
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, FRAME_NAME, "sp", Type.INT_TYPE.getDescriptor());
		mv.visitVarInsn(ISTORE, SP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, FRAME_NAME, "stack", Type.getDescriptor(Object[].class));
		mv.visitVarInsn(ASTORE, STACK);
		
		mv.visitInsn(ACONST_NULL);				// TODO: Is this necessary?
		mv.visitVarInsn(ASTORE, ACCU);

		/*
		 * Fetch constant store from current frame and (when present) assign to local variable
		 */
		if (f.constantStore.length != 0) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitFieldInsn(GETFIELD, FRAME_NAME, "function", Type.getDescriptor(Function.class));
			mv.visitFieldInsn(GETFIELD, FUNCTION_NAME, "constantStore", Type.getDescriptor(IValue[].class) );
			mv.visitVarInsn(ASTORE, CS);
		}
		/*
		 * Fetch type store from current frame and (when present) assign to local variable
		 */

		if (f.typeConstantStore.length != 0) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitFieldInsn(GETFIELD, FRAME_NAME, "function", Type.getDescriptor(Function.class));
			mv.visitFieldInsn(GETFIELD, FUNCTION_NAME, "typeConstantStore", Type.getDescriptor(org.rascalmpl.value.type.Type[].class));
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
			mv.visitFieldInsn(GETFIELD, FRAME_NAME, "hotEntryPoint", Type.INT_TYPE.getDescriptor());
			mv.visitTableSwitchInsn(0, hotEntryLabels.length - 1, exitLabel, hotEntryLabels);

			mv.visitLabel(hotEntryLabels[0]); // Start at 'address' 0
		} else {
			exitLabel = null;
		}

		f.codeblock.genByteCode(this, debug);

		if (exitLabel != null) {
			mv.visitLabel(exitLabel);
			emitPanicReturn();
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
		mv.visitFieldInsn(GETFIELD, fullClassName, "vf", Type.getType(IValueFactory.class).getDescriptor());
	}
	
	private void emitRex(){
		mv.visitVarInsn(ALOAD, THIS);		// rex
		mv.visitFieldInsn(GETFIELD, fullClassName, "rex", Type.getType(RascalExecutionContext.class).getDescriptor());
	}
	
	public void emitIncSP(int n){
		mv.visitIincInsn(SP, n);
	}
	
	public void emitReturnValue2ACCU(){
		mv.visitVarInsn(ALOAD, THIS);
		
		mv.visitFieldInsn(GETFIELD, fullClassName, "returnValue", OBJECT_TYPE.getDescriptor());
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emitReturnNONE(){
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RVMonJVM.class), "NONE", Type.getType(IString.class).getDescriptor());
		mv.visitInsn(ARETURN);
	}
	
	public void emitJMP(String targetLabel) {
		Label lb = getNamedLabel(targetLabel);
		mv.visitJumpInsn(GOTO, lb);
	}
	
	private void emitIntFromTopOfStack(){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(Integer.class));
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Integer.class), "intValue", Type.getMethodDescriptor(INT_TYPE), false);
	}
	
	private void  emitIntFromAccu(){
		mv.visitVarInsn(ALOAD, ACCU);			// ((Integer) accu).intValue()
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(Integer.class));
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Integer.class), "intValue", Type.getMethodDescriptor(INT_TYPE), false);
	}
	
	private void emitObjectFromTopOfStack(){
		mv.visitVarInsn(ALOAD, STACK);			
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
	}
	
	public void emitInlineLoadInt(int nval, boolean debug) {
		emitIntValue(nval);
		mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Integer.class), "valueOf", Type.getMethodDescriptor(Type.getType(Integer.class), INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	/********************************************************************************************/
	/*	Utitities for calling FrameObservers													*/
	/********************************************************************************************/

	public void emitInlineFrameUpdateSrc(int srcIndex){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		emitIntValue(srcIndex);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(RVMonJVM.class), "frameUpdateSrc", Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE), false);
	}
	
	public void emitInlineFrameObserve(int srcIndex){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		emitIntValue(srcIndex);
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(RVMonJVM.class), "frameObserve", Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE), false);
	}
	
	public void emitInlineFrameEnter(int srcIndex){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		emitIntValue(srcIndex);
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(RVMonJVM.class), "frameEnter", Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE), false);
	}
	
	public void emitInlineFrameLeave(int srcIndex){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "returnValue", OBJECT_TYPE.getDescriptor());
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(RVMonJVM.class), "frameLeave", Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE, IVALUE_TYPE), false);
	}
	
	/************************************************************************************************/
	/* Emitters for various instructions															*/
	/************************************************************************************************/

	public void emitJMPTRUEorFALSE(boolean tf, String targetLabel, boolean debug) {
		Label target = getNamedLabel(targetLabel);

		mv.visitVarInsn(ALOAD, ACCU);
		
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IBool.class), "getValue", Type.getMethodDescriptor(BOOLEAN_TYPE),true);
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
		mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Integer.class), "valueOf", Type.getMethodDescriptor(Type.getType(Integer.class), INT_TYPE), false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// SubtractInt: accu = ((Integer) stack[--sp]) - ((Integer) accu)
	
	public void emitInlineSubtractInt(){
		emitIntFromTopOfStack();				// left int
		emitIntFromAccu();						// right int
		
		mv.visitInsn(ISUB);						// left - right
		mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Integer.class), "valueOf", Type.getMethodDescriptor(Type.getType(Integer.class), INT_TYPE), false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// SubscriptArray: accu = ((Object[]) stack[--sp])[((Integer) accu)];
	
	public void emitInlineSubscriptArray(){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, Type.getDescriptor(Object[].class));
		emitIntFromAccu();
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// SubscriptList: accu = ((IList) stack[--sp]).get((Integer) accu);
	
	public void emitInlineSubscriptList(){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(IList.class));
		emitIntFromAccu();
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IList.class), "get", Type.getMethodDescriptor(IVALUE_TYPE, INT_TYPE), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// LessInt: accu = vf.bool(((Integer) stack[--sp]) < ((Integer) accu));
	
	public void emitInlineLessInt(){
		emitIntFromTopOfStack();				// left int
		emitIntFromAccu();						// right int
		
		Label l1 = new Label();
		mv.visitJumpInsn(IF_ICMPGE, l1);
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RVMonJVM.class), "Rascal_TRUE", Type.getDescriptor(IBool.class));
		Label l2 = new Label();
		mv.visitJumpInsn(GOTO, l2);
		mv.visitLabel(l1);

		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RVMonJVM.class), "Rascal_FALSE", Type.getDescriptor(IBool.class));
		mv.visitLabel(l2);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// GreaterEqualInt: accu = vf.bool(((Integer) stack[--sp]) >= ((Integer) accu));
	
	public void emitInlineGreaterEqualInt(){
		emitIntFromTopOfStack();				// left int
		emitIntFromAccu();						// right int
		
		Label l1 = new Label();
		mv.visitJumpInsn(IF_ICMPLT, l1);
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RVMonJVM.class), "Rascal_TRUE", Type.getDescriptor(IBool.class));
		Label l2 = new Label();
		mv.visitJumpInsn(GOTO, l2);
		mv.visitLabel(l1);

		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RVMonJVM.class), "Rascal_FALSE", Type.getDescriptor(IBool.class));
		mv.visitLabel(l2);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// AndBool: accu = ((IBool) stack[--sp]).and((IBool) accu);
	
	public void emitInlineAndBool(){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(IBool.class));
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(IBool.class));
		
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IBool.class), "and", Type.getMethodDescriptor(Type.getType(IBool.class), Type.getType(IBool.class)), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}

	// SubType: accu = vf.bool(((Type) stack[--sp]).isSubtypeOf((Type) accu));
	
	public void emitInlineSubType(){		
		emitValueFactory();
		
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(org.rascalmpl.value.type.Type.class));
		
		mv.visitVarInsn(ALOAD, ACCU);			// right
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(org.rascalmpl.value.type.Type.class));
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(org.rascalmpl.value.type.Type.class), "isSubtypeOf", Type.getMethodDescriptor(BOOLEAN_TYPE, TYPE_TYPE), false);
		
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValueFactory.class), "bool", Type.getMethodDescriptor(Type.getType(IBool.class), BOOLEAN_TYPE), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// LoadLocRef: accu = new Reference(stack, pos);
	
	public void emitInlineLoadLocRef(int pos){
		mv.visitTypeInsn(NEW, Type.getInternalName(Reference.class));
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Reference.class), INIT_NAME, Type.getMethodDescriptor(VOID_TYPE, OBJECT_A_TYPE, INT_TYPE), false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// PushLocRef: stack[sp++] = new Reference(stack, pos);
	
	public void emitInlinePushLocRef(int pos){
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitTypeInsn(NEW, Type.getInternalName(Reference.class));
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		
		mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Reference.class), INIT_NAME, Type.getMethodDescriptor(VOID_TYPE, OBJECT_A_TYPE, INT_TYPE), false);
		mv.visitInsn(AASTORE);
	}
	
	// LoadLocDeref:
	//	Reference ref = (Reference) stack[pos];
	//	accu = ref.stack[ref.pos];

	public void emitInlineLoadLocDeref(int pos){
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitInsn(AALOAD);
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(Reference.class));
		mv.visitVarInsn(ASTORE, TMP1);
		
		mv.visitVarInsn(ALOAD, TMP1);
		mv.visitFieldInsn(GETFIELD, Type.getInternalName(Reference.class), "stack", Type.getDescriptor(Object[].class));
		mv.visitVarInsn(ALOAD, TMP1);
		mv.visitFieldInsn(GETFIELD, Type.getInternalName(Reference.class), "pos", Type.INT_TYPE.getDescriptor());
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
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(Reference.class));
		
		mv.visitVarInsn(ASTORE, TMP1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitVarInsn(ALOAD, TMP1);
		mv.visitFieldInsn(GETFIELD, Type.getInternalName(Reference.class), "stack", Type.getDescriptor(Object[].class));
		mv.visitVarInsn(ALOAD, TMP1);
		mv.visitFieldInsn(GETFIELD, Type.getInternalName(Reference.class), "pos", Type.INT_TYPE.getDescriptor());
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
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(Reference.class));
		mv.visitVarInsn(ASTORE, TMP1);
		
		mv.visitVarInsn(ALOAD, TMP1);
		mv.visitFieldInsn(GETFIELD, Type.getInternalName(Reference.class), "stack", Type.getDescriptor(Object[].class));
		mv.visitVarInsn(ALOAD, TMP1);
		mv.visitFieldInsn(GETFIELD, Type.getInternalName(Reference.class), "pos", Type.INT_TYPE.getDescriptor());
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitInsn(AASTORE);
	}
	
	public void emitInlineExhaust(boolean dcode) {		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "exhaustHelper", Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE),false);
		mv.visitInsn(ARETURN);
	}

	public void emitInlineReturn(int wReturn, boolean debug) {
		if(wReturn != 0){
			mv.visitVarInsn(ALOAD, THIS);
			mv.visitVarInsn(ALOAD, ACCU);
			mv.visitFieldInsn(PUTFIELD, fullClassName, "returnValue", OBJECT_TYPE.getDescriptor());
		}
		
		emitReturnNONE();
	}
	
	public void emitInlineCoReturn(int wReturn, boolean debug) {
		mv.visitVarInsn(ALOAD, THIS);

		if (wReturn == 0) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "coreturn0Helper",
			    Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE),false);
		} else {
			mv.visitVarInsn(ALOAD, STACK);
			mv.visitVarInsn(ILOAD, SP);
			mv.visitVarInsn(ALOAD, CF);
			emitIntValue(wReturn);
			
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "coreturn1Helper",
			    Type.getMethodDescriptor(VOID_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE),false);
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
	
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "VISIT", Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, BOOLEAN_TYPE, BOOLEAN_TYPE, BOOLEAN_TYPE, BOOLEAN_TYPE),false);
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
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "CHECKMEMO", Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE),false);
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
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RVMonJVM.class), "FAILRETURN", Type.getType(IString.class).getDescriptor());
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

		mv = cw.visitMethod(ACC_PUBLIC, "dynRun", Type.getMethodDescriptor(OBJECT_TYPE, INT_TYPE, FRAME_TYPE), null, null);
		mv.visitCode();

		// Case switch on int at loc 1 (java stack)
		mv.visitVarInsn(ILOAD, 1);	// TODO: NAME!
		mv.visitTableSwitchInsn(0, nrFuncs - 1, defaultlabel, caseLabels);
		for (int i = 0; i < nrFuncs; i++) {
			mv.visitLabel(caseLabels[i]);
			mv.visitVarInsn(ALOAD, THIS);
			mv.visitVarInsn(ALOAD, 2); // TODO: NAME! BEWARE: CF in second argument differs from generated functions.
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, rvm2jvmName(funcArray[i]), Type.getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE),false);
			mv.visitInsn(ARETURN);
		}
		mv.visitLabel(defaultlabel);

		// Function exit
		emitValueFactory();
		mv.visitInsn(ICONST_0);
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValueFactory.class), "bool", Type.getMethodDescriptor(Type.getType(IBool.class), BOOLEAN_TYPE),true);
		mv.visitInsn(ARETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}

	public void emitInlineGuard(int hotEntryPoint, boolean dcode) {
		mv.visitVarInsn(ALOAD, CF);				// cf.hotEntryPoint = hotEntryPoint
		emitIntValue(hotEntryPoint);
		mv.visitFieldInsn(PUTFIELD, FRAME_NAME, "hotEntryPoint", Type.INT_TYPE.getDescriptor());
		
		mv.visitInsn(ACONST_NULL);				// coroutine = null
		mv.visitVarInsn(ASTORE, LCOROUTINE);
				
		mv.visitVarInsn(ALOAD, THIS);			
		mv.visitVarInsn(ALOAD, ACCU);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "guardHelper", Type.getMethodDescriptor(BOOLEAN_TYPE, OBJECT_TYPE), false);
		mv.visitVarInsn(ISTORE, LPRECONDITION);	// precondition = guardHelper(accu)
		
		mv.visitVarInsn(ALOAD, CF);				// if(cf != cccf) goto l0
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cccf", FRAME_TYPE.getDescriptor());
		
		Label l0 = new Label();
		mv.visitJumpInsn(IF_ACMPNE, l0);
												// Here: cf == cff
		mv.visitVarInsn(ILOAD, LPRECONDITION);	// if(!precondition) goto l1

		Label l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);
												// Here: cf == cff && precondition
		mv.visitTypeInsn(NEW, Type.getInternalName(Coroutine.class));
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cccf", FRAME_TYPE.getDescriptor());
	
		mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Coroutine.class), INIT_NAME,
		    Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE),false);
		mv.visitVarInsn(ASTORE, LCOROUTINE);
		mv.visitVarInsn(ALOAD, LCOROUTINE);
		mv.visitInsn(ICONST_1);
		mv.visitFieldInsn(PUTFIELD, Type.getInternalName(Coroutine.class), "isInitialized", BOOLEAN_TYPE.getDescriptor());
		mv.visitVarInsn(ALOAD, LCOROUTINE);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(PUTFIELD, Type.getInternalName(Coroutine.class), "entryFrame", FRAME_TYPE.getDescriptor());
		mv.visitVarInsn(ALOAD, LCOROUTINE);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Coroutine.class), "suspend",
		    Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE),false);
		
		mv.visitLabel(l1);						// Here cf != cff && !precondition
		
		mv.visitVarInsn(ALOAD, THIS);			// cccf = null
		mv.visitInsn(ACONST_NULL);
		
		mv.visitFieldInsn(PUTFIELD, fullClassName, "cccf", Type.getType(Frame.class).getDescriptor());
		
		mv.visitVarInsn(ALOAD, CF);				// cf.sp = sp

		mv.visitVarInsn(ILOAD, SP);
		mv.visitFieldInsn(PUTFIELD, FRAME_NAME, "sp", Type.INT_TYPE.getDescriptor());
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ILOAD, LPRECONDITION);	
		
		Label l2 = new Label();
		mv.visitJumpInsn(IFEQ, l2);				// if(!precondition) goto l2
		
		mv.visitVarInsn(ALOAD, LCOROUTINE);		
		Label l3 = new Label();
		mv.visitJumpInsn(GOTO, l3);
		
		mv.visitLabel(l2);						// Here: !precondition
		
		mv.visitFieldInsn(GETSTATIC, fullClassName, "exhausted", Type.getType(Coroutine.class).getDescriptor());
	
		mv.visitLabel(l3);
	
		mv.visitFieldInsn(PUTFIELD, fullClassName, "returnValue", OBJECT_TYPE.getDescriptor());

		emitReturnNONE();
		
		mv.visitLabel(l0);						// Here: cf != cccf
		mv.visitVarInsn(ILOAD, LPRECONDITION);
		Label l4 = new Label();
		mv.visitJumpInsn(IFNE, l4);				// if(precondition) goto l4;
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", Type.getDescriptor(IBool.class));
		mv.visitFieldInsn(PUTFIELD, fullClassName, "returnValue", OBJECT_TYPE.getDescriptor());
				
		mv.visitVarInsn(ALOAD, CF);				
		mv.visitVarInsn(ILOAD, SP);				// cf.sp = sp
		mv.visitFieldInsn(PUTFIELD, FRAME_NAME, "sp", Type.INT_TYPE.getDescriptor());

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
		if (conOrType) {
			mv.visitVarInsn(ALOAD, CS);
		} else {
			mv.visitVarInsn(ALOAD, TS);
		}

		emitIntValue(n);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	public void emitInlinePushConOrType(int n, boolean conOrType, boolean debug) { 
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);

		if (conOrType) {
			mv.visitVarInsn(ALOAD, CS);
		} else {
			mv.visitVarInsn(ALOAD, TS);
		}

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
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "typeSwitchHelper", Type.getMethodDescriptor(INT_TYPE, OBJECT_TYPE), false);

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
			
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "yield1Helper", Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE, INT_TYPE),false);
		} else {
			emitIntValue(hotEntryPoint);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "yield0Helper", Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE),false);
		}
			
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RVMonJVM.class), "YIELD", Type.getType(IString.class).getDescriptor());
		mv.visitInsn(ARETURN);

		mv.visitLabel(hotEntryLabels[hotEntryPoint]);
	}

	public void emitPanicReturn() {
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RVMonJVM.class), "PANIC", Type.getType(IString.class).getDescriptor());
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
		default:
			emitInlineCall(functionIndex, arity, continuationPoint, debug);
		}
	}
	
	public void emitInlineCallMAKE_SUBJECT(int continuationPoint){
		emitEntryLabel(continuationPoint);
		emitIntValue(2);
		
		mv.visitTypeInsn(ANEWARRAY, Type.getInternalName(Object.class));
		
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
		mv.visitTypeInsn(CHECKCAST, Type.getDescriptor(Object[].class));
		emitIntValue(0);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ASTORE, ACCU);	
	}
	
	public void emitInlineCallGET_SUBJECT_CURSOR(int continuationPoint){
		emitEntryLabel(continuationPoint);
		
		emitObjectFromTopOfStack();
		
		mv.visitTypeInsn(CHECKCAST, Type.getDescriptor(Object[].class));
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

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "callHelper", Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitInsn(DUP);

		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RVMonJVM.class), "YIELD", Type.getType(IString.class).getDescriptor());
		mv.visitJumpInsn(IF_ACMPNE, l0);
		
		mv.visitInsn(ARETURN);

		mv.visitLabel(l0);						// result callHelper != "YIELD"
		mv.visitInsn(POP);
		
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, FRAME_NAME, "sp", Type.INT_TYPE.getDescriptor());
		mv.visitVarInsn(ISTORE, SP);
		
		emitReturnValue2ACCU();
	}
	
	public void emitInlineCreateDyn(int arity, boolean debug){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F
		emitIntValue(arity); 			// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "jvmCREATEDYN", Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE),false);
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
				Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RVMonJVM.class), "YIELD", Type.getType(IString.class).getDescriptor());
		mv.visitJumpInsn(IF_ACMPNE, l0);
		mv.visitInsn(ARETURN);

		mv.visitLabel(l0);					// result callDynHelper != "YIELD"
		
		mv.visitInsn(POP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, FRAME_NAME, "sp", Type.INT_TYPE.getDescriptor());
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
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(MuPrimitive.class), muprim.name(),
				Type.getDescriptor(MuPrimitive.class));

		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(MuPrimitive.class), "execute0", Type.getMethodDescriptor(OBJECT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);		// accu = callMuPrim0()
	}
		
	// PushCallMuPrim0
	
	public void emitInlinePushCallMuPrim0(MuPrimitive muprim, boolean debug){
		emitInlinePushCallMuPrim0General(muprim, debug);
	}
	
	private void emitInlinePushCallMuPrim0General(MuPrimitive muprim, boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(MuPrimitive.class), muprim.name(),
				Type.getDescriptor(MuPrimitive.class));
	
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(MuPrimitive.class), "execute0", Type.getMethodDescriptor(OBJECT_TYPE),false);
		mv.visitInsn(AASTORE);				// stack[sp] = callMuPrim0()
		mv.visitIincInsn(SP, 1);			// sp += 1
	}
	
	// CallMuPrim1
	
	public void emitInlineCallMuPrim1(MuPrimitive muprim, boolean debug) {
		switch(muprim){
		
		case iterator_hasNext:
			emit_iterator_hasNext(); return;
		case iterator_next:
			emit_iterator_next(); return;
			
		case is_defined:
			emit_is_defined(); return;
			
		case is_bool :
			emit_is_predicate("isBool"); return;
		case is_datetime:
			emit_is_predicate("isDateTime"); return;
		case is_int:
			emit_is_predicate("isInteger"); return;
		case is_list:
			emit_is_predicate("isList"); return;
		case is_lrel:
			emit_is_predicate("isListRelation"); return;
		case is_loc:
			emit_is_predicate("isSourceLocation"); return;
		case is_map:
			emit_is_predicate("isMap"); return;
		case is_node:
			emit_is_predicate("isNode"); return;
		case is_num:
			emit_is_predicate("isNum"); return;
		case is_real:
			emit_is_predicate("isReal"); return;
		case is_rat:
			emit_is_predicate("isRational"); return;
		case is_rel:
			emit_is_predicate("isRelation"); return;
		case is_set:
			emit_is_predicate("isSet"); return;
		case is_str:
			emit_is_predicate("isString"); return;
		case is_tuple:
			emit_is_predicate("isTuple"); return;
			
		case mint:
			emit_mint(); return;
		default:
		}
		emitInlineCallMuPrim1General(muprim, debug);
	}
	
	private void emitInlineCallMuPrim1General(MuPrimitive muprim, boolean debug) {
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(MuPrimitive.class), muprim.name(),
				Type.getDescriptor(MuPrimitive.class));
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(MuPrimitive.class), "execute1", Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE),false);
		
		mv.visitVarInsn(ASTORE, ACCU);		// accu = callMuPrim1(arg_1)
	}
	
	private void emit_is_predicate(String predicate){
		emitValueFactory();
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(IValue.class));
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValue.class), "getType", Type.getMethodDescriptor(TYPE_TYPE), true);
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(org.rascalmpl.value.type.Type.class), predicate, Type.getMethodDescriptor(BOOLEAN_TYPE), false);
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValueFactory.class), "bool", Type.getMethodDescriptor(Type.getType(IBool.class), BOOLEAN_TYPE), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emit_is_defined(){
		emitValueFactory();
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(Reference.class));
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Reference.class), "isDefined", Type.getMethodDescriptor(BOOLEAN_TYPE), false);
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValueFactory.class), "bool", Type.getMethodDescriptor(Type.getType(IBool.class), BOOLEAN_TYPE), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emit_iterator_hasNext(){
		emitValueFactory();
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(Iterator.class), "hasNext", Type.getMethodDescriptor(BOOLEAN_TYPE), true);
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValueFactory.class), "bool", Type.getMethodDescriptor(Type.getType(IBool.class), BOOLEAN_TYPE), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emit_iterator_next(){
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(Iterator.class));
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(Iterator.class), "next", Type.getMethodDescriptor(OBJECT_TYPE), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emit_mint(){
		mv.visitVarInsn(ALOAD, ACCU);
		
		mv.visitTypeInsn(INSTANCEOF, Type.getInternalName(IInteger.class));
		Label l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(IInteger.class));
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IInteger.class), "intValue", Type.getMethodDescriptor(INT_TYPE), true);
		mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Integer.class), "valueOf", Type.getMethodDescriptor(Type.getType(Integer.class), INT_TYPE), false);
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
		
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(MuPrimitive.class), muprim.name(),
				Type.getDescriptor(MuPrimitive.class));
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(MuPrimitive.class), "execute1", Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE),false);
		
		mv.visitInsn(AASTORE);				// stack[sp] = callMuPrim1(arg_1)
		mv.visitIincInsn(SP, 1);			// sp += 1
	}
	
	// CallMuPrim2
	
	public void emitInlineCallMuPrim2(MuPrimitive muprim, boolean debug) {
		switch(muprim){
		
		case equal_mint_mint:
			emitComparisonMinMint(IF_ICMPNE); return; 
		case greater_equal_mint_mint:
			emitComparisonMinMint(IF_ICMPLT); return; 
		case greater_mint_mint:
			emitComparisonMinMint(IF_ICMPLE); return;
		case less_equal_mint_mint:
			emitComparisonMinMint(IF_ICMPGT); return; 
		case less_mint_mint:
			emitComparisonMinMint(IF_ICMPGE); return; 
		case not_equal_mint_mint:
			emitComparisonMinMint(IF_ICMPEQ); return; 

		case size_array:
			emit_size_array(); return;
		case size_list:
			emit_size(Type.getInternalName(IList.class)); return;
		case size_set:
			emit_size(Type.getInternalName(ISet.class)); return;
		case size_map:
			emit_size(Type.getInternalName(IMap.class)); return;
		case size_str:
			emit_size(Type.getInternalName(IString.class)); return;
		case size_tuple:
			emit_size(Type.getInternalName(ITuple.class)); return;
			
		case subscript_array_mint:
			emitInlineCallMuPrim2_subscript_array_mint(debug);
			return;
			
		case subscript_list_mint:
			emitInlineCallMuPrim2_subscript_list_mint(debug);
			return;
		default:
		}
		emitInlineCallMuPrim2General(muprim, debug);
	}
	
	private void emitInlineCallMuPrim2General(MuPrimitive muprim, boolean debug) {
		mv.visitIincInsn(SP, -1);			// sp -= 1
		
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(MuPrimitive.class), muprim.name(),
				Type.getDescriptor(MuPrimitive.class));
		
		mv.visitVarInsn(ALOAD, STACK);		// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(MuPrimitive.class), "execute2", Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE),false);
		
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

		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValueFactory.class), "bool", Type.getMethodDescriptor(Type.getType(IBool.class), BOOLEAN_TYPE), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emit_size_array(){
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, Type.getDescriptor(Object[].class));
		mv.visitInsn(ARRAYLENGTH);
		mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Integer.class), "valueOf", Type.getMethodDescriptor(Type.getType(Integer.class), INT_TYPE), false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emit_size(String type){
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, type);
		mv.visitMethodInsn(INVOKEINTERFACE, type, "length", Type.getMethodDescriptor(INT_TYPE), true);
		mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(Integer.class), "valueOf", Type.getMethodDescriptor(Type.getType(Integer.class), INT_TYPE), false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// accu = ((Object[]) stack[--sp])[((int) arg_1)];
	
	private void emitInlineCallMuPrim2_subscript_array_mint(boolean debug){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, Type.getDescriptor(Object[].class));
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(Integer.class));
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(Integer.class), "intValue", Type.getMethodDescriptor(INT_TYPE), false);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emitInlineCallMuPrim2_subscript_list_mint(boolean debug){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(IList.class));
		emitIntFromAccu();
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IList.class), "get", Type.getMethodDescriptor(IVALUE_TYPE, INT_TYPE), true);
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
		
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(MuPrimitive.class), muprim.name(),
				Type.getDescriptor(MuPrimitive.class));
		
		mv.visitVarInsn(ALOAD, STACK);		// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(MuPrimitive.class), "execute2", Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE),false);
		
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
		mv.visitTypeInsn(CHECKCAST, Type.getDescriptor(Object[].class));

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
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(MuPrimitive.class), muprim.name(),
				Type.getDescriptor(MuPrimitive.class));

		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp

		emitIntValue(arity);				// arity

		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(MuPrimitive.class), "executeN", Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE),false);
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
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(MuPrimitive.class), muprim.name(),
				Type.getDescriptor(MuPrimitive.class));

		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp

		emitIntValue(arity);				// arity

		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(MuPrimitive.class), "executeN", Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);		// sp = callMuPrimN(stach, sp, arity)
	}
	
	/********************************************************************************************************************/
	/*		CallPrim	/ PushCallPrim																					*/	
	/*																													*/
	/*		Emits an inline version of CallPrim[012N] or PushCallPrim[012N] instruction and uses a direct call to 		*/
	/*		the static enum execute method. In some cases, the muprim is special cased									*/
	/********************************************************************************************************************/
	
	// CallPrim0
	
	public void emitInlineCallPrim0(RascalPrimitive prim, int srcIndex, boolean debug) {
		emitInlineCallPrim0General(prim, srcIndex, debug);
	}
	
	private void emitInlineCallPrim0General(RascalPrimitive prim, int srcIndex, boolean debug) {
		emitInlineFrameObserve(srcIndex);
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RascalPrimitive.class), prim.name(),
				Type.getDescriptor(RascalPrimitive.class));
		
		mv.visitVarInsn(ALOAD, CF);			// currentFrame
		
		emitRex();
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(RascalPrimitive.class), "execute0",
				Type.getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE, Type.getType(RascalExecutionContext.class)),false);
		
		mv.visitVarInsn(ASTORE, ACCU);		// accu = callPrim0()
	}
	
	// PushCallPrim0
	
	public void emitInlinePushCallPrim0(RascalPrimitive prim, int srcIndex, boolean debug) {
		emitInlinePushCallPrim0General(prim, srcIndex, debug);
	}
	
	private void emitInlinePushCallPrim0General(RascalPrimitive prim, int srcIndex, boolean debug) {
		emitInlineFrameObserve(srcIndex);
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RascalPrimitive.class), prim.name(),
				Type.getDescriptor(RascalPrimitive.class));
		
		mv.visitVarInsn(ALOAD, CF);			// currentFrame
		
		emitRex();

		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(RascalPrimitive.class), "execute0",
				Type.getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE, Type.getType(RascalExecutionContext.class)),false);
		
		mv.visitInsn(AASTORE);				// stack[sp] callPrim0()
		mv.visitIincInsn(SP, 1);			// sp += 1
	}
	
	// CallPrim1
	
	public void emitInlineCallPrim1(RascalPrimitive prim, int srcIndex, boolean debug) {
		emitInlineCallPrim1General(prim, srcIndex, debug);
	}
	
	private void emitInlineCallPrim1General(RascalPrimitive prim, int srcIndex, boolean debug) {
		emitInlineFrameObserve(srcIndex);
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RascalPrimitive.class), prim.name(),
				Type.getDescriptor(RascalPrimitive.class));
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		
		mv.visitVarInsn(ALOAD, CF);		// currentFrame

		emitRex();

		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(RascalPrimitive.class), "execute1",
		    Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, FRAME_TYPE, Type.getType(RascalExecutionContext.class)),false);
		
		mv.visitVarInsn(ASTORE, ACCU);		// accu = callPrim1(arg_1)
	}
	
	// PushCallPrim1
	
	public void emitInlinePushCallPrim1(RascalPrimitive prim, int srcIndex, boolean debug) {
		emitInlinePushCallPrim1General(prim, srcIndex, debug);
	}
	
	private void emitInlinePushCallPrim1General(RascalPrimitive prim, int srcIndex, boolean debug) {
		emitInlineFrameObserve(srcIndex);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RascalPrimitive.class), prim.name(),
				Type.getDescriptor(RascalPrimitive.class));
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		
		mv.visitVarInsn(ALOAD, CF);		// currentFrame

		emitRex();

		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(RascalPrimitive.class), "execute1",
		    Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, FRAME_TYPE, Type.getType(RascalExecutionContext.class)),false);
		
		mv.visitInsn(AASTORE);			// stack[sp++] = callPrim1(arg_1)
		mv.visitIincInsn(SP, 1);
	}
	
	// CallPrim2
	
	public void emitInlineCallPrim2(RascalPrimitive prim, int srcIndex, boolean debug) {
		switch(prim.name()){
			case "subtype_value_type":
				emitInlineCallPrim2_subtype_value_type(srcIndex);
				return;
			case "subtype_value_value":
				emitInlineCallPrim2_subtype_value_value(srcIndex);
				return;
		}
	
		emitInlineCallPrim2General(prim, srcIndex, debug);
	}
	
	private void emitInlineCallPrim2General(RascalPrimitive prim, int srcIndex, boolean debug) {
		emitInlineFrameObserve(srcIndex);
		mv.visitIincInsn(SP, -1);
		
		System.err.println(Type.getDescriptor(RascalPrimitive.class));
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RascalPrimitive.class), prim.name(),
				Type.getDescriptor(RascalPrimitive.class));
		
		mv.visitVarInsn(ALOAD, STACK);	// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		
		mv.visitVarInsn(ALOAD, CF);		// currentFrame

		emitRex();
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(RascalPrimitive.class), "execute2",
		    Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, FRAME_TYPE, Type.getType(RascalExecutionContext.class)),false);
		
		mv.visitVarInsn(ASTORE, ACCU);	// accu = CallPrim2(arg_2, arg_1)
	}
	
	private void emitInlineCallPrim2_subtype_value_type(int srcIndex){
		emitInlineFrameObserve(srcIndex);
		emitValueFactory();
		
		emitObjectFromTopOfStack();		// arg_2
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(IValue.class));
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValue.class), "getType", Type.getMethodDescriptor(TYPE_TYPE), true);
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(org.rascalmpl.value.type.Type.class));
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(org.rascalmpl.value.type.Type.class), "isSubtypeOf", Type.getMethodDescriptor(BOOLEAN_TYPE, TYPE_TYPE), false);
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValueFactory.class), "bool", Type.getMethodDescriptor(Type.getType(IBool.class), BOOLEAN_TYPE), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	private void emitInlineCallPrim2_subtype_value_value(int srcIndex){
		emitInlineFrameObserve(srcIndex);
		emitValueFactory();
		
		emitObjectFromTopOfStack();		// arg_2
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(IValue.class));
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValue.class), "getType", Type.getMethodDescriptor(TYPE_TYPE), true);
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(IValue.class));
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValue.class), "getType", Type.getMethodDescriptor(TYPE_TYPE), true);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(org.rascalmpl.value.type.Type.class), "isSubtypeOf", Type.getMethodDescriptor(BOOLEAN_TYPE, TYPE_TYPE), false);
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValueFactory.class), "bool", Type.getMethodDescriptor(Type.getType(IBool.class), BOOLEAN_TYPE), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// PushCallPrim2
	
	public void emitInlinePushCallPrim2(RascalPrimitive prim, int srcIndex, boolean debug) {
		switch(prim.name()){
		case "subtype_value_type":
			emitInlinePushCallPrim2_subtype_value_type(srcIndex);
			return;
		case "subtype_value_value":
			emitInlinePushCallPrim2_subtype_value_value(srcIndex);
			return;
		}
		emitInlinePushCallPrim2General(prim, srcIndex, debug);
	}
	
	private void emitInlinePushCallPrim2General(RascalPrimitive prim, int srcIndex, boolean debug) {
		emitInlineFrameObserve(srcIndex);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RascalPrimitive.class), prim.name(),
				Type.getDescriptor(RascalPrimitive.class));
		
		mv.visitVarInsn(ALOAD, STACK);	// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		
		mv.visitVarInsn(ALOAD, CF);		// currentFrame

		emitRex();
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(RascalPrimitive.class), "execute2",
		    Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, FRAME_TYPE, Type.getType(RascalExecutionContext.class)), false);
		
		mv.visitInsn(AASTORE);
		mv.visitIincInsn(SP, 1);
	}
	
	private void emitInlinePushCallPrim2_subtype_value_type(int srcIndex){
		emitInlineFrameObserve(srcIndex);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		
		emitValueFactory();
		
		mv.visitVarInsn(ALOAD, STACK);	// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(IValue.class));
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValue.class), "getType", Type.getMethodDescriptor(TYPE_TYPE), true);
		
		mv.visitVarInsn(ALOAD, ACCU);	// arg_1 from accu
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(org.rascalmpl.value.type.Type.class));
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(org.rascalmpl.value.type.Type.class), "isSubtypeOf", Type.getMethodDescriptor(BOOLEAN_TYPE, TYPE_TYPE), false);
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValueFactory.class), "bool", Type.getMethodDescriptor(Type.getType(IBool.class), BOOLEAN_TYPE), true);
		mv.visitInsn(AASTORE);
		mv.visitIincInsn(SP, 1);
	}
	
	private void emitInlinePushCallPrim2_subtype_value_value(int srcIndex){
		emitInlineFrameObserve(srcIndex);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		
		emitValueFactory();
		
		mv.visitVarInsn(ALOAD, STACK);	// arg_2
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(IValue.class));
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValue.class), "getType", Type.getMethodDescriptor(TYPE_TYPE), true);
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		mv.visitTypeInsn(CHECKCAST, Type.getInternalName(IValue.class));
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValue.class), "getType", Type.getMethodDescriptor(TYPE_TYPE), true);
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(org.rascalmpl.value.type.Type.class), "isSubtypeOf", Type.getMethodDescriptor(BOOLEAN_TYPE, TYPE_TYPE), false);
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValueFactory.class), "bool", Type.getMethodDescriptor(Type.getType(IBool.class), BOOLEAN_TYPE), true);
		
		mv.visitInsn(AASTORE);
		mv.visitIincInsn(SP, 1);
	}
	
	// CallPrimN
	
	public void emitInlineCallPrimN(RascalPrimitive prim, int arity, int srcIndex, boolean debug) {
		emitInlineCallPrimNGeneral(prim, arity, srcIndex, debug);
	}
	
	private void emitInlineCallPrimNGeneral(RascalPrimitive prim, int arity, int srcIndex, boolean debug) {
		emitInlineFrameObserve(srcIndex);
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RascalPrimitive.class), prim.name(),
				Type.getDescriptor(RascalPrimitive.class));

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		emitIntValue(arity);

		mv.visitVarInsn(ALOAD, CF);
		
		emitRex();
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(RascalPrimitive.class), "executeN",
				Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE, FRAME_TYPE, Type.getType(RascalExecutionContext.class)),false);
		
		mv.visitInsn(ICONST_M1);
		mv.visitInsn(IADD);					// sp--
		mv.visitVarInsn(ISTORE, SP);		// sp = callPrimN(stack, sp, arity, cf, rex) - 1
		
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);		// accu = stack[--sp];
	}
	
	// PushCallPrimN
	
	public void emitInlinePushCallPrimN(RascalPrimitive prim, int arity, int srcIndex, boolean debug) {
		emitInlinePushCallPrimNGeneral(prim, arity, srcIndex, debug);
	}
	
	private void emitInlinePushCallPrimNGeneral(RascalPrimitive prim, int arity, int srcIndex, boolean debug) {
		emitInlineFrameObserve(srcIndex);
		mv.visitFieldInsn(GETSTATIC, Type.getInternalName(RascalPrimitive.class), prim.name(),
				Type.getDescriptor(RascalPrimitive.class));

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		emitIntValue(arity);

		mv.visitVarInsn(ALOAD, CF);
		
		emitRex();
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(RascalPrimitive.class), "executeN",
				Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE, FRAME_TYPE, Type.getType(RascalExecutionContext.class)),false);
		
		mv.visitVarInsn(ISTORE, SP);
	}
	
	// End of CallMuPrim[012N] / PushCallMuPrim[012N]
	
	public void emitInlineLoadBool(boolean b, boolean debug) {
		if (b) {
			mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", Type.getDescriptor(IBool.class));
		} else {
			mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", Type.getDescriptor(IBool.class));
		}
		mv.visitVarInsn(ASTORE, ACCU);		// accu = bool;
	}

	/*
	 * emitOptimizedOcall emits a call to a full ocall implementation unless: 
	 * 1: There is only one function => emit direct call  (DONE) 
	 * 2: There is only a constructor => call constructor (DONE)
	 */
	public void emitOptimizedOcall(String fuid, int overloadedFunctionIndex, int arity, int srcIndex, boolean dcode) {
		OverloadedFunction of = overloadedStore[overloadedFunctionIndex];
		int[] functions = of.getFunctions();
		if (functions.length == 1) {
			int[] ctors = of.getConstructors();
			if (ctors.length == 0) {
				Function fu = functionStore[functions[0]];
				if (of.getScopeFun().equals("")) {
					emitOcallSingle(rvm2jvmName(fu.getName()), functions[0], arity, srcIndex);
				} else {
					// Nested function needs link to containing frame
					// TODO srcIndex
					emitCallWithArgsSSFII_A("jvmOCALL", overloadedFunctionIndex, arity, dcode);
					mv.visitIincInsn(SP, -arity);
					emitReturnValue2ACCU();
				}
			} else {
				// Has a constructor.
				// TODO srcIndex
				emitCallWithArgsSSFII_A("jvmOCALL", overloadedFunctionIndex, arity, dcode);
				mv.visitIincInsn(SP, -arity);
				emitReturnValue2ACCU();
			}
		} else {
			if(functions.length == 0 && of.getConstructors().length == 1){
				// Specialize for single constructor
				// TODO srcIndex
				emitCallWithArgsSSFII_A("jvmOCALLSingleConstructor", overloadedFunctionIndex, arity, dcode);
			} else {
				// TODO srcIndex
				emitCallWithArgsSSFII_A("jvmOCALL", overloadedFunctionIndex, arity, dcode);
			}
			mv.visitIincInsn(SP, -arity);
			emitReturnValue2ACCU();
		}
	}

	private void emitOcallSingle(String funName, int fun, int arity, int srcIndex) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "functionStore", Type.getDescriptor(Function[].class));
		emitIntValue(fun);
		mv.visitInsn(AALOAD);

		mv.visitVarInsn(ALOAD, CF);
		
		emitIntValue(arity);
		mv.visitVarInsn(ILOAD, SP);
		emitInlineFrameEnter(srcIndex);

		mv.visitMethodInsn(
				INVOKEVIRTUAL,
				FRAME_NAME,
				"getFrame",
				Type.getMethodDescriptor(FRAME_TYPE, FUNCTION_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE),false);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, funName, Type.getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE),false);
		mv.visitInsn(POP);

		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, FRAME_NAME, "sp", INT_TYPE.getDescriptor());
		mv.visitVarInsn(ISTORE, SP);
		
		emitInlineFrameLeave(srcIndex);
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

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "switchHelper", Type.getMethodDescriptor(INT_TYPE, OBJECT_TYPE, BOOLEAN_TYPE),false);
		mv.visitLookupSwitchInsn(trampolineLabel, intTable, switchTable);

		emitLabel(caseDefault + "_trampoline");

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

			mv.visitTryCatchBlock(fromLabel, toLabel, handlerLabel, Type.getInternalName(Thrown.class));
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
		mv.visitFieldInsn(GETFIELD, Type.getInternalName(Thrown.class), "value", Type.getDescriptor(IValue.class));
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValue.class), "getType", Type.getMethodDescriptor(TYPE_TYPE),true);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, FRAME_NAME, "function", Type.getDescriptor(Function.class));
		mv.visitFieldInsn(GETFIELD, FUNCTION_NAME, "typeConstantStore", Type.getDescriptor(org.rascalmpl.value.type.Type[].class));

		emitIntValue(type);

		mv.visitInsn(AALOAD);
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(org.rascalmpl.value.type.Type.class), "isSubtypeOf", Type.getMethodDescriptor(BOOLEAN_TYPE, TYPE_TYPE),false);
		mv.visitJumpInsn(IFNE, noReThrow);
		mv.visitVarInsn(ALOAD, EXCEPTION);
		mv.visitInsn(ATHROW);
		mv.visitLabel(noReThrow);
	}

	public void emitInlineThrow(boolean debug) {
		mv.visitVarInsn(ALOAD, CF);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitFieldInsn(PUTFIELD, FRAME_NAME, "sp", Type.INT_TYPE.getDescriptor());
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "thrownHelper",
				Type.getMethodDescriptor(Type.getType(Thrown.class), FRAME_TYPE, OBJECT_A_TYPE, INT_TYPE),false);
		 
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
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "jvmRESETVAR", Type.getMethodDescriptor(VOID_TYPE, INT_TYPE, INT_TYPE, FRAME_TYPE),false);
	}

	// TODO: compare with performance of insnCHECKARGTYPEANDCOPY
	public void emitInlineCheckArgTypeAndCopy(int pos1, int type, int pos2, boolean debug) {
		Label l1 = new Label();
		Label l5 = new Label();

		mv.visitVarInsn(ALOAD, STACK);

		/* sourceLoc */
		emitIntValue(pos1);

		mv.visitInsn(AALOAD);
		
		mv.visitMethodInsn(INVOKEINTERFACE, Type.getInternalName(IValue.class), "getType", Type.getMethodDescriptor(TYPE_TYPE),true);
		mv.visitVarInsn(ALOAD, TS);

		/* type */
		emitIntValue(type);

		mv.visitInsn(AALOAD);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(org.rascalmpl.value.type.Type.class), "isSubtypeOf", Type.getMethodDescriptor(BOOLEAN_TYPE, TYPE_TYPE),false);
		mv.visitJumpInsn(IFEQ, l1);
		mv.visitVarInsn(ALOAD, STACK);

		/* toloc */
		emitIntValue(pos2);

		mv.visitVarInsn(ALOAD, STACK);

		/* sourceLoc */
		emitIntValue(pos1);

		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
		
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_TRUE", Type.getDescriptor(IBool.class));
		mv.visitVarInsn(ASTORE, ACCU);
		mv.visitJumpInsn(GOTO, l5);
		
		mv.visitLabel(l1);
		mv.visitFieldInsn(GETSTATIC, fullClassName, "Rascal_FALSE", Type.getDescriptor(IBool.class));
		mv.visitVarInsn(ASTORE, ACCU);
		mv.visitLabel(l5);
	}

	public void emitInlinePushEmptyKwMap(boolean debug) {
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitTypeInsn(NEW, Type.getInternalName(java.util.HashMap.class));
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(java.util.HashMap.class), INIT_NAME, Type.getMethodDescriptor(VOID_TYPE), false);
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
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "VALUESUBTYPE", Type.getMethodDescriptor(OBJECT_TYPE, TYPE_TYPE, OBJECT_TYPE),false);
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
	/*		Utilities																			*/
	/********************************************************************************************/
	
	/*
	 * Map RVM function names to valid JVM method names. 
	 * The characeters not allowed in JVM method names are (according to 4.2.2. Unqualified Names): . ; [ / < >
	 */
	 private String rvm2jvmName(String s) {
		char[] b = s.toCharArray();
		boolean modified = false;
		for (int i = 0; i < b.length; i++) {
			switch (b[i]) {
			
			case '.': b[i] = '!'; modified = true; break;
			case ';': b[i] = ':'; modified = true; break;
			case '[': b[i] = '('; modified = true; break;
			case ']': b[i] = ')'; modified = true; break; // added for symmetry
		    case '/': b[i] = '\\'; modified = true; break;  
		    case '<': b[i] = '{'; modified = true; break;
		    case '>': b[i] = '}'; modified = true; break;
			}
		}
		return modified ? new String(b) : s;
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
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	public void emitCallWithArgsPA_A(String fname) {
		mv.visitVarInsn(ALOAD, THIS);
		
		mv.visitIincInsn(SP, -1);		// sp--
		
		mv.visitVarInsn(ALOAD, STACK);	
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);			// P: arg_2
		
		mv.visitVarInsn(ALOAD, ACCU);	// A: arg_1 from accu
		
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}

	public void emitCallWithArgsSSI_S(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		emitIntValue(i);				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsSI_A(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		emitIntValue(i);				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}

	public void emitCallWithArgsSSII_S(String fname, int i, int j, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		emitIntValue(i);				// I
		emitIntValue(j);				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsSSII_A(String fname, int i, int j, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		emitIntValue(i);				// I
		emitIntValue(j);				// I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE, INT_TYPE),false);
		
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
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsFI_A(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitCallWithArgsSSFI_A(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitCallWithArgsSFI_A(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, FRAME_TYPE, INT_TYPE),false);
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
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}

	public void emitVoidCallWithArgsSSI_S(String fname, int i, boolean dbg) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		emitIntValue(i);				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(VOID_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE),false);
	}

	public void emitCallWithArgsSSFII_S(String fname, int i, int j, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		emitIntValue(j); 				// I

		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsSSFII_A(String fname, int i, int j, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		emitIntValue(j); 				// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitCallWithArgsFII_A(String fname, int i, int j, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		
		mv.visitVarInsn(ALOAD, CF);		// F

		emitIntValue(i); 				// I
		emitIntValue(j); 				// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitVoidCallWithArgsFIIA(String fname, int what, int pos, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(what); 			// I
		emitIntValue(pos); 				// I
		mv.visitVarInsn(ALOAD, ACCU);	// A

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE, OBJECT_TYPE),false);
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

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitVoidCallWithArgsSFIA(String fname, int pos, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ALOAD, CF); 	// F
		emitIntValue(pos); 				// I
		mv.visitVarInsn(ALOAD, ACCU);	// A
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(VOID_TYPE, OBJECT_A_TYPE, FRAME_TYPE, INT_TYPE, OBJECT_TYPE),false);
	}
	
	public void emitVoidCallWithArgsFIA(String fname, int pos, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF); 	// F
		emitIntValue(pos); 				// I
		mv.visitVarInsn(ALOAD, ACCU);	// A
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE, OBJECT_TYPE),false);
	}
	
	public void emitVoidCallWithArgsFII(String fname, int scope, int pos, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF); 	// F
		emitIntValue(scope); 			// I
		emitIntValue(pos); 				// I	
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE),false);
	}


	public void emitCallWithArgsSSF_S(String fname, boolean dcode) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP);    	// S

		mv.visitVarInsn(ALOAD, CF);		// F

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsSSF_A(String fname, boolean dcode) {
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP);    	// S

		mv.visitVarInsn(ALOAD, CF);		// F

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE),false);
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
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, Type.getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE, OBJECT_TYPE),false);
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
		mv.visitMethodInsn(INVOKESTATIC, fullClassName, "debugINSTRUCTION", Type.getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE, OBJECT_TYPE),false);
	}
	
	public void emitDebugCall1(String ins, int arg1) {
		mv.visitLdcInsn(ins);
		mv.visitLdcInsn(arg1);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKESTATIC, fullClassName, "debugINSTRUCTION1", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(String.class), INT_TYPE, FRAME_TYPE, INT_TYPE, OBJECT_TYPE),false);
	}
	
	public void emitDebugCall2(String ins, String arg1, int arg2) {
		mv.visitLdcInsn(ins);
		mv.visitLdcInsn(abbrev(arg1));
		mv.visitLdcInsn(arg2);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKESTATIC, fullClassName, "debugINSTRUCTION2", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(String.class),  Type.getType(String.class), INT_TYPE, FRAME_TYPE, INT_TYPE, OBJECT_TYPE),false);
	}
}
