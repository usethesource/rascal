/*******************************************************************************
 * Copyright (c) 2009-2017 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Ferry Rietveld - f.rietveld@hva.nl - HvA
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Jurgen Vinju - Jurgen.Vinju@cwi.nl
*******************************************************************************/


package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.FileOutputStream;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class BytecodeGenerator implements Opcodes {

  // constant references to types used in emitting method calls and field references
  private  final Type BOOLEAN_TYPE = Type.BOOLEAN_TYPE;
  private  final Type OBJECT_A_TYPE = getType(Object[].class);
  private  final Type IVALUE_TYPE = getType(IValue.class);
  private  final String FRAME_NAME = getInternalName(Frame.class);
  private  final String FUNCTION_NAME = getInternalName(Function.class);
  private  final Type FUNCTION_TYPE = getType(Function.class);
  private  final Type TYPE_TYPE = getType(io.usethesource.vallang.type.Type.class);
  private  final String INIT_NAME = "<init>";
  private  final Type INT_TYPE = Type.INT_TYPE;
  private  final Type VOID_TYPE = Type.VOID_TYPE;
  private  final Type FRAME_TYPE = getType(Frame.class);
  private  final Type OBJECT_TYPE = getType(Object.class);
 
  private Type getType(Class<?> cls) {
      Type res;
      if (classRenamings == null || classRenamings.isEmpty()) {
          res = Type.getType(cls);
      }
      else {
          String descriptor = Type.getType(cls).getDescriptor();
          
          for (String key : classRenamings.keySet()) {
              descriptor = descriptor.replaceAll(key, Type.getType(classRenamings.get(key)).getInternalName());
          }
          
          res = Type.getType(descriptor);
      }
      //System.err.println("getType: " + res);
      return res;
  }
  
  private String getInternalName(Class<?> cls) {
      String name = Type.getInternalName(cls);
      
      if (classRenamings != null && !classRenamings.isEmpty()) {
          for (String key : classRenamings.keySet()) {
              name = name.replaceAll(key, Type.getType(classRenamings.get(key)).getInternalName());
          }
      }
      //System.err.println("getInternalName: " + name);
      return name;
  }
  
  private String getDescriptor(Class<?> cls) {
      String descr = Type.getDescriptor(cls);
      
      if (classRenamings != null && !classRenamings.isEmpty()) {
          for (String key : classRenamings.keySet()) {
              descr = descr.replaceAll(key, Type.getType(classRenamings.get(key)).getDescriptor());
          }
      }
      //System.err.println("getDescriptor: " + descr);
      return descr;
  }
  
  private String getMethodDescriptor(Type returnType, Type... argumentTypes) {
      String descr = Type.getMethodDescriptor(returnType, argumentTypes);
      
      if (classRenamings != null && !classRenamings.isEmpty()) {
          for (String key : classRenamings.keySet()) {
              descr = descr.replaceAll(key, Type.getType(classRenamings.get(key)).getDescriptor());
          }
      }
      //System.err.println("getMethodDescriptor: " + descr);
      return descr;
  }
  
  
  
  // Locations of the variables in a compiled RVM function.
	
    // Common local variables
	public static final int THIS = 0;			// Current class (generated for the top level Rascal module)
	public static final int CF = 1;				// Current frame
	public static final int SP = 2;				// RVM stack pointer: int sp
	public static final int ACCU = 3;			// RVM accumulator: Object accu
	public static final int STACK = 4;			// RVM stack: Object stack[]

	
	// Local variables in coroutines
	
	public static final int LPRECONDITION = 5;	// Boolean variable used in guard code
	//public static final int TMP1 = 5;			// Temp variable outside Guard code
	public static final int LCOROUTINE = 6;		// Local coroutine instance in guard code
	//public static final int TMP2 = 6;			// Temp variable outside Guard code
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
	private String fullClassNameDotted = null;

	// Administration per function
	private HashMap<String, Label> labelMap = new HashMap<String, Label>();
	private Set<String> catchTargetLabels = new HashSet<String>();			
	private Map<String, ExceptionLine> catchTargets = new HashMap<String, ExceptionLine>();
	private Label[] hotEntryLabels = null;		// entry labels for coroutines
	private Label exitLabel = null;				// special case for labels without code
											    // TODO: peephole optimizer now removes them; can disappear?

	final Function[] functionStore;
	final OverloadedFunction[] overloadedStore;
	final Map<String, Integer> functionMap;
	final Map<String, Integer> constructorMap;
	
    private final Map<String, String> classRenamings;
    
    private final Set<Integer> uniqueFunctions;

	private Label getNamedLabel(String targetLabel) {
		Label lb = labelMap.get(targetLabel);
		if (lb == null) {
			lb = new Label();
			labelMap.put(targetLabel, lb);
		}
		return lb;
	}

	public BytecodeGenerator(Function[] functionStore, OverloadedFunction[] overloadedStore,
			Map<String, Integer> functionMap, Map<String, Integer> constructorMap, Map<String,String> classRenamings) {
		this.functionStore = functionStore;
		this.overloadedStore = overloadedStore;
		this.functionMap = functionMap;
		this.constructorMap = constructorMap;
		this.classRenamings = classRenamings;
		this.uniqueFunctions = new HashSet<>();
		findUniqueFunctions();
	}
	
	private void findUniqueFunctions(){
	    for(int i = 0; i < functionStore.length; i++){
	        Function fun = functionStore[i];
//	        System.err.println("findUnique, considering " + nm);
	        if(fun.simpleArgs && !fun.isCoroutine){
	            uniqueFunctions.add(i);
	        }
	    }
	    
	    for(OverloadedFunction ovf : overloadedStore){
	        if(ovf.functions.length > 1){
	            for(int fn : ovf.functions){
	                uniqueFunctions.remove(fn);
	            };
	        } 
	    }
	    
//	    for(int fn : uniqueFunctions){
//	        System.err.println("unique: " + functionStore[fn].getName());
//	    }
	}
	
	public void buildClass(String packageName, String className, boolean debug) {
		emitClass(packageName,className);

		for (int fn = 0; fn < functionStore.length; fn++){
		    emitMethod(fn,  debug);
		}
		
		emitConstructor();
	    //dumpClass();
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
	public void emitConstructor() {

		mv = cw.visitMethod(ACC_PUBLIC, INIT_NAME, 
				getMethodDescriptor(VOID_TYPE, getType(RVMExecutable.class), getType(RascalExecutionContext.class)),
				null, null);

		mv.visitCode();
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, RVM_EXEC); 
		mv.visitVarInsn(ALOAD, RVM_REX);
		mv.visitMethodInsn(INVOKESPECIAL, getInternalName(RVMonJVM.class), INIT_NAME,
				getMethodDescriptor(VOID_TYPE, getType(RVMExecutable.class), getType(RascalExecutionContext.class)),false);

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
		this.fullClassNameDotted = packageName.isEmpty() ? className : packageName + "." + className;
		this.fullClassName = fullClassNameDotted.replace('.', '/');
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, fullClassName, null, getInternalName(RVMonJVM.class), null);
	}
	
	/*
	 * Dump the code of the generated class for debugging purposes
	 */
	
	public void dumpClass() {
		if (endCode == null) {
			finalizeCode();
		}
		
		try {
			FileOutputStream fos = new FileOutputStream("/tmp/Class.class");
			fos.write(endCode);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Generate a method per function
	 * 
	 * Unique functions are optimized as follows:
	 * - CHECKARGTYPEANDCOPY (only copies parameters) + JUMPFALSE are eliminated
	 * - Access to a parameter as usual (via the position it is copied to by CHECKARGTYPEANDCOPY)
	 */

	boolean inUniqueFunction = false;  // Remember whether we are generated code inside a unique function
	
	boolean inUniqueFunction() {
//	    return false;
        return inUniqueFunction;
    }
	
	/*
	 * Generate a method for one RVM function
	 */
	public void emitMethod(int fn, boolean debug) {
	    Function f = functionStore[fn];
	    
	    boolean unique = uniqueFunctions.contains(fn);
	    boolean hasJavaTag = f.tags.get(ValueFactoryFactory.getValueFactory().string("java")) != null;
	    if(!unique || hasJavaTag || f.isCoroutine || !f.simpleArgs){
	        inUniqueFunction = false;
	    } else {
	        inUniqueFunction = true;
	    }

		labelMap.clear(); // New set of labels.
		catchTargetLabels.clear();
		catchTargets.clear();

		// Create method	 TODO: make it private
		mv = cw.visitMethod(ACC_PUBLIC|ACC_FINAL, rvm2jvmName(f.getName()), getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE), null, null);
		mv.visitCode();

		emitExceptionTable(f.fromLabels, f.toLabels, f.fromSPsCorrected, f.types, f.handlerLabels);

		/*
		 * Fetch sp, stack from current frame and assign to local variable
		 */
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, FRAME_NAME, "sp", Type.INT_TYPE.getDescriptor());
		mv.visitVarInsn(ISTORE, SP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, FRAME_NAME, "stack", getDescriptor(Object[].class));
		mv.visitVarInsn(ASTORE, STACK);
		
		mv.visitInsn(ACONST_NULL);				// TODO: Is this necessary?
		mv.visitVarInsn(ASTORE, ACCU);

		/*
		 * Fetch constant store from current frame and (when present) assign to local variable
		 */
		if (f.constantStore.length != 0) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitFieldInsn(GETFIELD, FRAME_NAME, "function", getDescriptor(Function.class));
			mv.visitFieldInsn(GETFIELD, FUNCTION_NAME, "constantStore", getDescriptor(IValue[].class) );
			mv.visitVarInsn(ASTORE, CS);
		}
		/*
		 * Fetch type store from current frame and (when present) assign to local variable
		 */

		if (f.typeConstantStore.length != 0) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitFieldInsn(GETFIELD, FRAME_NAME, "function", getDescriptor(Function.class));
			mv.visitFieldInsn(GETFIELD, FUNCTION_NAME, "typeConstantStore", getDescriptor(io.usethesource.vallang.type.Type[].class));
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

		f.codeblock.genByteCode(this, inUniqueFunction(), debug);

		if (exitLabel != null) {
			mv.visitLabel(exitLabel);
			emitPanicReturn();
		}
		mv.visitMaxs(0, 0);
		mv.visitEnd();
	}
	
	public void emitHotEntryJumpTable(int continuationPoints) {
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
		mv.visitFieldInsn(GETFIELD, fullClassName, "vf", getType(IValueFactory.class).getDescriptor());
	}
	
	private void emitRex(){
		mv.visitVarInsn(ALOAD, THIS);		// rex
		mv.visitFieldInsn(GETFIELD, fullClassName, "rex", getType(RascalExecutionContext.class).getDescriptor());
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
		mv.visitFieldInsn(GETSTATIC, getInternalName(RVMonJVM.class), "NONE", getType(IString.class).getDescriptor());
		mv.visitInsn(ARETURN);
	}
	
	public void emitJMP(String targetLabel) {
		Label lb = getNamedLabel(targetLabel);
		mv.visitJumpInsn(GOTO, lb);
	}
	
	private void emitIntFromTopOfStack(){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, getInternalName(Integer.class));
		
		mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(Integer.class), "intValue", getMethodDescriptor(INT_TYPE), false);
	}
	
	private void  emitIntFromAccu(){
		mv.visitVarInsn(ALOAD, ACCU);			// ((Integer) accu).intValue()
		mv.visitTypeInsn(CHECKCAST, getInternalName(Integer.class));
		mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(Integer.class), "intValue", getMethodDescriptor(INT_TYPE), false);
	}
	
	private void emitObjectFromTopOfStack(){
	    mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
	}
	
	public void emitInlineLoadInt(int nval) {
		emitIntValue(nval);
		mv.visitMethodInsn(INVOKESTATIC, getInternalName(Integer.class), "valueOf", getMethodDescriptor(getType(Integer.class), INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	/********************************************************************************************/
	/*	Utitities for calling FrameObservers													*/
	/********************************************************************************************/

	public void emitInlineFrameUpdateSrc(int srcIndex){
		//mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		emitIntValue(srcIndex);
		
		mv.visitMethodInsn(INVOKESTATIC, getInternalName(RVMonJVM.class), "frameUpdateSrc", getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE), false);
	}
	
	public void emitInlineFrameObserve(int srcIndex){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		emitIntValue(srcIndex);
		mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(RVMonJVM.class), "frameObserve", getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE), false);
	}
	
	public void emitInlineFrameEnter(int srcIndex){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		emitIntValue(srcIndex);
		mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(RVMonJVM.class), "frameEnter", getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE), false);
	}
	
    public void emitInlineFrameLeave(int srcIndex){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "returnValue", OBJECT_TYPE.getDescriptor());
		mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(RVMonJVM.class), "frameLeave", getMethodDescriptor(VOID_TYPE, FRAME_TYPE, IVALUE_TYPE), false);
	}
	
	/************************************************************************************************/
	/* Emitters for various instructions															*/
	/************************************************************************************************/

	public void emitJMPTRUEorFALSE(boolean tf, String targetLabel) {
		Label target = getNamedLabel(targetLabel);

		mv.visitVarInsn(ALOAD, ACCU);
		
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IBool.class), "getValue", getMethodDescriptor(BOOLEAN_TYPE),true);
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
		mv.visitMethodInsn(INVOKESTATIC, getInternalName(Integer.class), "valueOf", getMethodDescriptor(getType(Integer.class), INT_TYPE), false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// SubtractInt: accu = ((Integer) stack[--sp]) - ((Integer) accu)
	
	public void emitInlineSubtractInt(){
		emitIntFromTopOfStack();				// left int
		emitIntFromAccu();						// right int
		
		mv.visitInsn(ISUB);						// left - right
		mv.visitMethodInsn(INVOKESTATIC, getInternalName(Integer.class), "valueOf", getMethodDescriptor(getType(Integer.class), INT_TYPE), false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// SubscriptArray: accu = ((Object[]) stack[--sp])[((Integer) accu)];
	
	public void emitInlineSubscriptArray(){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, getDescriptor(Object[].class));
		emitIntFromAccu();
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// SubscriptList: accu = ((IList) stack[--sp]).get((Integer) accu);
	
	public void emitInlineSubscriptList(){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, getInternalName(IList.class));
		emitIntFromAccu();
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IList.class), "get", getMethodDescriptor(IVALUE_TYPE, INT_TYPE), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// LessInt: accu = vf.bool(((Integer) stack[--sp]) < ((Integer) accu));
	
	public void emitInlineLessInt(){
		emitIntFromTopOfStack();				// left int
		emitIntFromAccu();						// right int
		
		Label l1 = new Label();
		mv.visitJumpInsn(IF_ICMPGE, l1);
		mv.visitFieldInsn(GETSTATIC, getInternalName(RascalPrimitive.class), "Rascal_TRUE", getDescriptor(IBool.class));
		Label l2 = new Label();
		mv.visitJumpInsn(GOTO, l2);
		mv.visitLabel(l1);

		mv.visitFieldInsn(GETSTATIC, getInternalName(RascalPrimitive.class), "Rascal_FALSE", getDescriptor(IBool.class));
		mv.visitLabel(l2);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// GreaterEqualInt: accu = vf.bool(((Integer) stack[--sp]) >= ((Integer) accu));
	
	public void emitInlineGreaterEqualInt(){
		emitIntFromTopOfStack();				// left int
		emitIntFromAccu();						// right int
		
		Label l1 = new Label();
		mv.visitJumpInsn(IF_ICMPLT, l1);
		mv.visitFieldInsn(GETSTATIC, getInternalName(RascalPrimitive.class), "Rascal_TRUE", getDescriptor(IBool.class));
		Label l2 = new Label();
		mv.visitJumpInsn(GOTO, l2);
		mv.visitLabel(l1);

		mv.visitFieldInsn(GETSTATIC, getInternalName(RascalPrimitive.class), "Rascal_FALSE", getDescriptor(IBool.class));
		mv.visitLabel(l2);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// AndBool: accu = ((IBool) stack[--sp]).and((IBool) accu);
	
	public void emitInlineAndBool(){
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, getInternalName(IBool.class));
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, getInternalName(IBool.class));
		
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IBool.class), "and", getMethodDescriptor(getType(IBool.class), getType(IBool.class)), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}

	// SubType: accu = vf.bool(((Type) stack[--sp]).isSubtypeOf((Type) accu));
	
	public void emitInlineSubType(){		
		emitValueFactory();
		
		emitObjectFromTopOfStack();
		mv.visitTypeInsn(CHECKCAST, getInternalName(io.usethesource.vallang.type.Type.class));
		
		mv.visitVarInsn(ALOAD, ACCU);			// right
		mv.visitTypeInsn(CHECKCAST, getInternalName(io.usethesource.vallang.type.Type.class));
		mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(io.usethesource.vallang.type.Type.class), "isSubtypeOf", getMethodDescriptor(BOOLEAN_TYPE, TYPE_TYPE), false);
		
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValueFactory.class), "bool", getMethodDescriptor(getType(IBool.class), BOOLEAN_TYPE), true);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// LoadLocRef: accu = new Reference(stack, pos);
	
	public void emitInlineLoadLocRef(int pos){
		mv.visitTypeInsn(NEW, getInternalName(Reference.class));
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitMethodInsn(INVOKESPECIAL, getInternalName(Reference.class), INIT_NAME, getMethodDescriptor(VOID_TYPE, OBJECT_A_TYPE, INT_TYPE), false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// PushLocRef: stack[sp++] = new Reference(stack, pos);
	
	public void emitInlinePushLocRef(int pos){
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitTypeInsn(NEW, getInternalName(Reference.class));
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		
		mv.visitMethodInsn(INVOKESPECIAL, getInternalName(Reference.class), INIT_NAME, getMethodDescriptor(VOID_TYPE, OBJECT_A_TYPE, INT_TYPE), false);
		mv.visitInsn(AASTORE);
	}
	
	// LoadLocDeref:
	//	Reference ref = (Reference) stack[pos];
	//	accu = ref.getValue();

	public void emitInlineLoadLocDeref(int pos){
	    mv.visitVarInsn(ALOAD, STACK);
        emitIntValue(pos);
        mv.visitInsn(AALOAD);
        mv.visitTypeInsn(CHECKCAST, getInternalName(Reference.class));
        
        mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(Reference.class), "getValue", getMethodDescriptor(OBJECT_TYPE), false);
        mv.visitVarInsn(ASTORE, ACCU);
	}
	
	// PushLocDeref:
	// Reference ref = (Reference) stack[pos];
	// stack[sp++] = ref.getValue();
	
	public void emitInlinePushLocDeref(int pos){
	    mv.visitVarInsn(ALOAD, STACK);
        mv.visitVarInsn(ILOAD, SP);
        mv.visitIincInsn(SP, 1);
        
	    mv.visitVarInsn(ALOAD, STACK);
        emitIntValue(pos);
        mv.visitInsn(AALOAD);
        mv.visitTypeInsn(CHECKCAST, getInternalName(Reference.class));
        
        mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(Reference.class), "getValue", getMethodDescriptor(OBJECT_TYPE), false);
        mv.visitInsn(AASTORE);
	}
	
	// StoreLocDeref:
	// Reference ref = (Reference) stack[pos];
	// ref.storeValue(accu);

	public void emitInlineStoreLocDeref(int pos){
	    
	    mv.visitVarInsn(ALOAD, STACK);
        emitIntValue(pos);
        mv.visitInsn(AALOAD);
        mv.visitTypeInsn(CHECKCAST, getInternalName(Reference.class));
    
        mv.visitVarInsn(ALOAD, ACCU);
        mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(Reference.class), "setValue", getMethodDescriptor(VOID_TYPE, OBJECT_TYPE), false);
	}
	
	public void emitInlineExhaust() {		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "exhaustHelper", getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE),false);
		mv.visitInsn(ARETURN);
	}

	public void emitInlineReturn(int wReturn) {
		if(wReturn != 0){
			mv.visitVarInsn(ALOAD, THIS);
			mv.visitVarInsn(ALOAD, ACCU);
			mv.visitFieldInsn(PUTFIELD, fullClassName, "returnValue", OBJECT_TYPE.getDescriptor());
		}
		
		emitReturnNONE();
	}
	
	public void emitInlineCoReturn(int wReturn) {
		mv.visitVarInsn(ALOAD, THIS);

		if (wReturn == 0) {
			mv.visitVarInsn(ALOAD, CF);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "coreturn0Helper",
			    getMethodDescriptor(VOID_TYPE, FRAME_TYPE),false);
		} else {
			mv.visitVarInsn(ALOAD, STACK);
			mv.visitVarInsn(ILOAD, SP);
			mv.visitVarInsn(ALOAD, CF);
			emitIntValue(wReturn);
			
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "coreturn1Helper",
			    getMethodDescriptor(VOID_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE),false);
		}

		emitReturnNONE();
	}
	
	public void emitInlineVisit(boolean direction, boolean progress, boolean fixedpoint, boolean rebuild) {
		Label returnLabel = new Label();
		Label continueLabel = new Label();
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		emitIntValue(direction ? 1 : 0);
		emitIntValue(progress ? 1 : 0);
		emitIntValue(fixedpoint ? 1 : 0);
		emitIntValue(rebuild ? 1 : 0);
	
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "VISIT", getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, BOOLEAN_TYPE, BOOLEAN_TYPE, BOOLEAN_TYPE, BOOLEAN_TYPE),false);
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
		emitInlineReturn(1);
		
		mv.visitLabel(continueLabel);
	}
	
	public void emitInlineCheckMemo() {
		Label returnLabel = new Label();
		Label continueLabel = new Label();
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "CHECKMEMO", getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE),false);
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
		emitInlineReturn(1);
		
		mv.visitLabel(continueLabel);
	}
	
	public void emitInlineFailreturn() {
		mv.visitFieldInsn(GETSTATIC, getInternalName(RVMonJVM.class), "FAILRETURN", getType(IString.class).getDescriptor());
		mv.visitInsn(ARETURN);
	}

	public void emitInlineGuard(int hotEntryPoint) {
		mv.visitVarInsn(ALOAD, CF);				// cf.hotEntryPoint = hotEntryPoint
		emitIntValue(hotEntryPoint);
		mv.visitFieldInsn(PUTFIELD, FRAME_NAME, "hotEntryPoint", Type.INT_TYPE.getDescriptor());
		
		mv.visitInsn(ACONST_NULL);				// coroutine = null
		mv.visitVarInsn(ASTORE, LCOROUTINE);
				
		mv.visitVarInsn(ALOAD, THIS);			
		mv.visitVarInsn(ALOAD, ACCU);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "guardHelper", getMethodDescriptor(BOOLEAN_TYPE, OBJECT_TYPE), false);
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
		mv.visitTypeInsn(NEW, getInternalName(Coroutine.class));
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETFIELD, fullClassName, "cccf", FRAME_TYPE.getDescriptor());
	
		mv.visitMethodInsn(INVOKESPECIAL, getInternalName(Coroutine.class), INIT_NAME,
		    getMethodDescriptor(VOID_TYPE, FRAME_TYPE),false);
		mv.visitVarInsn(ASTORE, LCOROUTINE);
		mv.visitVarInsn(ALOAD, LCOROUTINE);
		mv.visitInsn(ICONST_1);
		mv.visitFieldInsn(PUTFIELD, getInternalName(Coroutine.class), "isInitialized", BOOLEAN_TYPE.getDescriptor());
		mv.visitVarInsn(ALOAD, LCOROUTINE);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(PUTFIELD, getInternalName(Coroutine.class), "entryFrame", FRAME_TYPE.getDescriptor());
		mv.visitVarInsn(ALOAD, LCOROUTINE);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(Coroutine.class), "suspend",
		    getMethodDescriptor(VOID_TYPE, FRAME_TYPE),false);
		
		mv.visitLabel(l1);						// Here cf != cff && !precondition
		
		mv.visitVarInsn(ALOAD, THIS);			// cccf = null
		mv.visitInsn(ACONST_NULL);
		
		mv.visitFieldInsn(PUTFIELD, fullClassName, "cccf", getType(Frame.class).getDescriptor());
		
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
		
		mv.visitFieldInsn(GETSTATIC, fullClassName, "exhausted", getType(Coroutine.class).getDescriptor());
	
		mv.visitLabel(l3);
	
		mv.visitFieldInsn(PUTFIELD, fullClassName, "returnValue", OBJECT_TYPE.getDescriptor());

		emitReturnNONE();
		
		mv.visitLabel(l0);						// Here: cf != cccf
		mv.visitVarInsn(ILOAD, LPRECONDITION);
		Label l4 = new Label();
		mv.visitJumpInsn(IFNE, l4);				// if(precondition) goto l4;
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitFieldInsn(GETSTATIC, getInternalName(RascalPrimitive.class), "Rascal_FALSE", getDescriptor(IBool.class));
		mv.visitFieldInsn(PUTFIELD, fullClassName, "returnValue", OBJECT_TYPE.getDescriptor());
				
		mv.visitVarInsn(ALOAD, CF);				
		mv.visitVarInsn(ILOAD, SP);				// cf.sp = sp
		mv.visitFieldInsn(PUTFIELD, FRAME_NAME, "sp", Type.INT_TYPE.getDescriptor());

		emitReturnNONE();
		
		mv.visitLabel(l4);						// Here: precondition == true
		mv.visitLabel(hotEntryLabels[hotEntryPoint]);
	}

	public void emitInlineLoadLocN(int pos) {
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	public void emitInlinePushLocN(int pos) {
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitIincInsn(SP, 1);
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
	}

	// Experimemtal local copy of constantStore and typeConstantStore probably needed in final version.
	public void emitInlineLoadConOrType(int n, boolean conOrType) {
		if (conOrType) {
			mv.visitVarInsn(ALOAD, CS);
		} else {
			mv.visitVarInsn(ALOAD, TS);
		}

		emitIntValue(n);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	public void emitInlinePushConOrType(int n, boolean conOrType) { 
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

	public void emitInlinePop() {
		mv.visitIincInsn(SP, -1);
	}

	public void emitInlineStoreLoc(int loc) {
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(loc);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitInsn(AASTORE);
	}

	public void emitInlineTypeSwitch(IList labels) {
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
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "typeSwitchHelper", getMethodDescriptor(INT_TYPE, OBJECT_TYPE), false);

		mv.visitTableSwitchInsn(0, nrLabels - 1, exitLabel, switchTable);
	}

	public void emitInlineYield(int arity, int hotEntryPoint) {

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		if (arity > 0) {
			emitIntValue(arity);
			emitIntValue(hotEntryPoint);
			
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "yield1Helper", getMethodDescriptor(VOID_TYPE, FRAME_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE, INT_TYPE),false);
		} else {
			emitIntValue(hotEntryPoint);
			mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "yield0Helper", getMethodDescriptor(VOID_TYPE, FRAME_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE),false);
		}
			
		mv.visitFieldInsn(GETSTATIC, getInternalName(RVMonJVM.class), "YIELD", getType(IString.class).getDescriptor());
		mv.visitInsn(ARETURN);

		mv.visitLabel(hotEntryLabels[hotEntryPoint]);
	}

	public void emitPanicReturn() {
		mv.visitFieldInsn(GETSTATIC, getInternalName(RVMonJVM.class), "PANIC", getType(IString.class).getDescriptor());
		mv.visitInsn(ARETURN);
	}

	public void emitEntryLabel(int continuationPoint) {
		mv.visitLabel(hotEntryLabels[continuationPoint]);
	}
	
	public void emitOptimizedCall(String fuid, int functionIndex, int arity, int continuationPoint){
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
			emitInlineCall(functionIndex, arity, continuationPoint);
		}
	}
	
	public void emitInlineCallMAKE_SUBJECT(int continuationPoint){
		emitEntryLabel(continuationPoint);
		emitIntValue(2);
		
		mv.visitTypeInsn(ANEWARRAY, getInternalName(Object.class));
		
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
		mv.visitTypeInsn(CHECKCAST, getDescriptor(Object[].class));
		emitIntValue(0);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ASTORE, ACCU);	
	}
	
	public void emitInlineCallGET_SUBJECT_CURSOR(int continuationPoint){
		emitEntryLabel(continuationPoint);
		
		emitObjectFromTopOfStack();
		
		mv.visitTypeInsn(CHECKCAST, getDescriptor(Object[].class));
		emitIntValue(1);
		mv.visitInsn(AALOAD);
		
		mv.visitVarInsn(ASTORE, ACCU);	
	}

	public void emitInlineCall(int functionIndex, int arity, int continuationPoint) {
		Label l0 = new Label();

		emitEntryLabel(continuationPoint);

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);

		emitIntValue(functionIndex);
		emitIntValue(arity);
		emitIntValue(continuationPoint);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "callHelper", getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitInsn(DUP);

		mv.visitFieldInsn(GETSTATIC, getInternalName(RVMonJVM.class), "YIELD", getType(IString.class).getDescriptor());
		mv.visitJumpInsn(IF_ACMPNE, l0);
		
		mv.visitInsn(ARETURN);

		mv.visitLabel(l0);						// result callHelper != "YIELD"
		mv.visitInsn(POP);
		
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, FRAME_NAME, "sp", Type.INT_TYPE.getDescriptor());
		mv.visitVarInsn(ISTORE, SP);
		
		emitReturnValue2ACCU();
	}
	
	public void emitInlineCreateDyn(int arity){
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F
		emitIntValue(arity); 			// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "jvmCREATEDYN", getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
		mv.visitIincInsn(SP, -arity-1);
	}

	public void emitInlineCalldyn(int arity, int continuationPoint) {
		Label l0 = new Label();

		emitEntryLabel(continuationPoint);

		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, CF);

		emitIntValue(arity);
		emitIntValue(continuationPoint);

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "calldynHelper",
				getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETSTATIC, getInternalName(RVMonJVM.class), "YIELD", getType(IString.class).getDescriptor());
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
	/*		Emits an inline version of CallMuPrim[012N] or PushCallMuPrim[012N] instruction and uses invokeDynamic      */
	/*      to call the static enum execute method. In some cases, the muprim is special cased.                         */
	/*                                                                                                                  */
	/* 		bootstrapMuPrimitive is used on the first call to a MuPrimitive to locate its execute method,             	*/
	/*      also see MuPrimitive.bootstrapMuPrimitive                                                                   */
	/*                                                                                                                  */
	/*      The same code is used for CallMuPrim[012N] or PushCallMuPrim[012N], the case is differentiated by argument  */
	/*      "push".                                                                                                     */
	/********************************************************************************************************************/
	
	Handle bootstrapMuPrimitive(){
        MethodType bmt = MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class);
        Handle bootstrap = new Handle(Opcodes.H_INVOKESTATIC, (MuPrimitive.class.getName()).replace('.', '/'), "bootstrapMuPrimitive",
            bmt.toMethodDescriptorString());
        return bootstrap;
    }
	
	// prepare[0-2] set up stack and arguments for a primitive call
	
	private void prepare0(boolean push){
        if(push){
            mv.visitVarInsn(ALOAD, STACK);      // stack
            mv.visitVarInsn(ILOAD, SP);         // sp
        }
    }
	
	private void prepare1(boolean push){
        if(push){
            mv.visitVarInsn(ALOAD, STACK);      // stack
            mv.visitVarInsn(ILOAD, SP);         // sp
        }
        mv.visitVarInsn(ALOAD, ACCU);
    }
	
	private void prepare2(boolean push){
	    mv.visitIincInsn(SP, -1);           // sp -= 1
	    
        if(push){
            mv.visitVarInsn(ALOAD, STACK);      // stack
            mv.visitVarInsn(ILOAD, SP);         // sp
        }
        
        mv.visitVarInsn(ALOAD, STACK);      // arg_2
        mv.visitVarInsn(ILOAD, SP);
        mv.visitInsn(AALOAD);
        
        mv.visitVarInsn(ALOAD, ACCU);       // arg_1 from accu
    }
	
	// result returns the result of a primitive
	
	private void result(boolean push){
	    if(push){
            mv.visitInsn(AASTORE);              // stack[sp] = result
            mv.visitIincInsn(SP, 1);            // sp += 1 
        } else {
            mv.visitVarInsn(ASTORE, ACCU);      // accu = result
        }
	}
	
	// CallMuPrim0
	
	public void emitInlineCallMuPrim0(MuPrimitive muprim) {
		emitInlineCallMuPrim0General(muprim, false);
	}

	private void emitInlineCallMuPrim0General(MuPrimitive muprim, boolean push) {
	    prepare0(push);
	    
	    mv.visitInvokeDynamicInsn(muprim.name(), 
	        getMethodDescriptor(OBJECT_TYPE), bootstrapMuPrimitive());
	    
	    result(push);
	}
		
	// PushCallMuPrim0
	
	public void emitInlinePushCallMuPrim0(MuPrimitive muprim){
		emitInlineCallMuPrim0General(muprim, true);
	}
	
	// CallMuPrim1
	
	public void emitInlineCallMuPrim1(MuPrimitive muprim) {
	    emitInlineCallMuPrim1(muprim, false);
	}
	
	private void emitInlineCallMuPrim1(MuPrimitive muprim, boolean push) {
		switch(muprim.name()){
		
		case "iterator_hasNext":
			emit_iterator_hasNext1(push); return;
		case "iterator_next":
			emit_iterator_next1(push); return;
		case "is_defined":
			emit_is_defined1(push); return;
		case "is_bool":
			emit_is_predicate1("isBool", push); return;
		case "is_datetime":
			emit_is_predicate1("isDateTime", push); return;
		case "is_int":
			emit_is_predicate1("isInteger", push); return;
		case "is_list":
			emit_is_predicate1("isList", push); return;
		case "is_lrel":
			emit_is_predicate1("isListRelation", push); return;
		case "is_loc":
			emit_is_predicate1("isSourceLocation", push); return;
		case "is_map":
			emit_is_predicate1("isMap", push); return;
		case "is_node":
			emit_is_predicate1("isNode", push); return;
		case "is_num":
			emit_is_predicate1("isNum", push); return;
		case "is_real":
			emit_is_predicate1("isReal", push); return;
		case "is_rat":
			emit_is_predicate1("isRational", push); return;
		case "is_rel":
			emit_is_predicate1("isRelation", push); return;
		case "is_set":
			emit_is_predicate1("isSet", push); return;
		case "is_str":
			emit_is_predicate1("isString", push); return;
		case "is_tuple":
			emit_is_predicate1("isTuple", push); return;		
		case "mint":
			emit_mint1(push); return;
		case "size_array":
            emit_size_array1(push); return;
        case "size_list":
            emit_size1(getInternalName(IList.class), push); return;
        case "size_set":
            emit_size1(getInternalName(ISet.class), push); return;
        case "size_map":
            emit_size1(getInternalName(IMap.class), push); return;
        case "size_str":
            emit_size1(getInternalName(IString.class), push); return;
        case "size_tuple":
            emit_size1(getInternalName(ITuple.class), push); return;
		}
		emitInlineCallMuPrim1General(muprim, push);
	}
	
	private void emitInlineCallMuPrim1General(MuPrimitive muprim, boolean push) {
	    prepare1(push);

	    mv.visitInvokeDynamicInsn(muprim.name(), 
	        getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE), bootstrapMuPrimitive());

	    result(push);
	}
	
	// Special cases
	
	private void emit_is_predicate1(String predicate, boolean push){
		emitValueFactory();
		
		prepare1(push);
		mv.visitTypeInsn(CHECKCAST, getInternalName(IValue.class));
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValue.class), "getType", getMethodDescriptor(TYPE_TYPE), true);
		mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(io.usethesource.vallang.type.Type.class), predicate, getMethodDescriptor(BOOLEAN_TYPE), false);
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValueFactory.class), "bool", getMethodDescriptor(getType(IBool.class), BOOLEAN_TYPE), true);
		result(push);
	}
	
	private void emit_is_defined1(boolean push){
		emitValueFactory();
		
		prepare1(push);
		mv.visitTypeInsn(CHECKCAST, getInternalName(Reference.class));
		mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(Reference.class), "isDefined", getMethodDescriptor(BOOLEAN_TYPE), false);
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValueFactory.class), "bool", getMethodDescriptor(getType(IBool.class), BOOLEAN_TYPE), true);
		result(push);
	}
	
	private void emit_iterator_hasNext1(boolean push){
		emitValueFactory();
		
		prepare1(push);
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(Iterator.class), "hasNext", getMethodDescriptor(BOOLEAN_TYPE), true);
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValueFactory.class), "bool", getMethodDescriptor(getType(IBool.class), BOOLEAN_TYPE), true);
		result(push);
	}
	
	private void emit_iterator_next1(boolean push){
	    prepare1(push);
		mv.visitTypeInsn(CHECKCAST, getInternalName(Iterator.class));
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(Iterator.class), "next", getMethodDescriptor(OBJECT_TYPE), true);
		result(push);
	}
	
	private void emit_mint1(boolean push){
	    prepare1(push);
		mv.visitTypeInsn(INSTANCEOF, getInternalName(IInteger.class));
		Label l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);
		
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitTypeInsn(CHECKCAST, getInternalName(IInteger.class));
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IInteger.class), "intValue", getMethodDescriptor(INT_TYPE), true);
		mv.visitMethodInsn(INVOKESTATIC, getInternalName(Integer.class), "valueOf", getMethodDescriptor(getType(Integer.class), INT_TYPE), false);
		result(push);
		
		mv.visitLabel(l1);
	}
	
	private void emit_size_array1(boolean push){
        prepare1(push);
        mv.visitTypeInsn(CHECKCAST, getDescriptor(Object[].class));
        mv.visitInsn(ARRAYLENGTH);
        mv.visitMethodInsn(INVOKESTATIC, getInternalName(Integer.class), "valueOf", getMethodDescriptor(getType(Integer.class), INT_TYPE), false);
        result(push);
    }
    
    private void emit_size1(String type, boolean push){
        prepare1(push);
        mv.visitTypeInsn(CHECKCAST, type);
        mv.visitMethodInsn(INVOKEINTERFACE, type, "length", getMethodDescriptor(INT_TYPE), true);
        mv.visitMethodInsn(INVOKESTATIC, getInternalName(Integer.class), "valueOf", getMethodDescriptor(getType(Integer.class), INT_TYPE), false);
        result(push);
    }
	
	// PushCallMuPrim1
	
	public void emitInlinePushCallMuPrim1(MuPrimitive muprim) {
		emitInlineCallMuPrim1General(muprim, true);
	}
	
	// CallMuPrim2
	
	public void emitInlineCallMuPrim2(MuPrimitive muprim){
	    emitInlineCallMuPrim2(muprim, false);
	}
	
	private void emitInlineCallMuPrim2(MuPrimitive muprim, boolean push) {
		switch(muprim.name()){
		
		case "equal_mint_mint":
			emitComparisonMinMint2(IF_ICMPNE, push); return; 
		case "greater_equal_mint_mint":
			emitComparisonMinMint2(IF_ICMPLT, push); return; 
		case "greater_mint_mint":
			emitComparisonMinMint2(IF_ICMPLE, push); return;
		case "less_equal_mint_mint":
			emitComparisonMinMint2(IF_ICMPGT, push); return; 
		case "less_mint_mint":
			emitComparisonMinMint2(IF_ICMPGE, push); return; 
		case "not_equal_mint_mint":
			emitComparisonMinMint2(IF_ICMPEQ, push); return; 
			
		case "subscript_array_mint":
			emitSubscriptArrayMint2(push);
			return;
			
		case "subscript_list_mint":
			emitSubscriptListMint2(push);
			return;
		}
		emitInlineCallMuPrim2General(muprim, push);
	}
	
	private void emitInlineCallMuPrim2General(MuPrimitive muprim, boolean push) {    
	    prepare2(push);
		
	    mv.visitInvokeDynamicInsn(muprim.name(), 
            getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE), bootstrapMuPrimitive());
		
		result(push);
	}
	
	// Special cases
	
	private void emitComparisonMinMint2(int if_op, boolean push){
	    if(push){                            // args are pushed below
	        mv.visitVarInsn(ALOAD, STACK);   // stack
	        mv.visitVarInsn(ILOAD, SP);      // sp
	    }      

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

	    mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValueFactory.class), "bool", getMethodDescriptor(getType(IBool.class), BOOLEAN_TYPE), true);
	    result(push);
	}
	
	// accu = ((Object[]) stack[--sp])[((int) arg_1)];
	
	private void emitSubscriptArrayMint2(boolean push){
	    mv.visitIincInsn(SP, -1);              // sp -= 1

	    if(push){
	        mv.visitVarInsn(ALOAD, STACK);     // stack
	        mv.visitVarInsn(ILOAD, SP);        // sp
	    }

	    emitObjectFromTopOfStack();
	    mv.visitTypeInsn(CHECKCAST, getDescriptor(Object[].class));

	    mv.visitVarInsn(ALOAD, ACCU);
	    mv.visitTypeInsn(CHECKCAST, getInternalName(Integer.class));
	    mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(Integer.class), "intValue", getMethodDescriptor(INT_TYPE), false);
	    mv.visitInsn(AALOAD);
	    result(push);
	}
	
	private void emitSubscriptListMint2(boolean push){
	    mv.visitIincInsn(SP, -1);              // sp -= 1

	    if(push){
	        mv.visitVarInsn(ALOAD, STACK);     // stack
	        mv.visitVarInsn(ILOAD, SP);        // sp
	    }

	    emitObjectFromTopOfStack();
	    mv.visitTypeInsn(CHECKCAST, getInternalName(IList.class));
	    emitIntFromAccu();
	    mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IList.class), "get", getMethodDescriptor(IVALUE_TYPE, INT_TYPE), true);
	    result(push);
	}
	
	// PushCallMuPrim2
	
	public void emitInlinePushCallMuPrim2(MuPrimitive muprim){
	    emitInlineCallMuPrim2(muprim, true);
	}
	
	// CallMuPrim3
    
	public void emitInlineCallMuPrim3(MuPrimitive muprim){
	    emitInlineCallMuPrim3(muprim, false);
	}

	private void emitInlineCallMuPrim3(MuPrimitive muprim, boolean push) {
	    switch(muprim.name()){

	    }
	    emitInlineCallMuPrim3General(muprim, push);
	}
	private void emitInlineCallMuPrim3General(MuPrimitive muprim, boolean push) {    
	    prepare2(push);

	    mv.visitInvokeDynamicInsn(muprim.name(), 
	        getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE), bootstrapMuPrimitive());

	    result(push);
	}
	
	// PushCallMuPrim3
	
	public void emitInlinePushCallMuPrim3(MuPrimitive muprim){
        emitInlineCallMuPrim3(muprim, true);
    }
	
	// CallMuPrimN
	
	public void emitInlineCallMuPrimN(MuPrimitive muprim, int arity) {
		emitInlineCallMuPrimNGeneral(muprim, arity);
	}

	private void emitInlineCallMuPrimNGeneral(MuPrimitive muprim, int arity) {
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp

		emitIntValue(arity);				// arity

		                                    // sp = callMuPrimN(stack, sp, arity)
	    mv.visitInvokeDynamicInsn(muprim.name(), 
            getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE), bootstrapMuPrimitive());
						
		mv.visitInsn(ICONST_M1);
		mv.visitInsn(IADD);					// sp--
		mv.visitVarInsn(ISTORE, SP);		// sp = callMuPrimN(stack, sp, arity) - 1
		
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);		// accu = stack[--sp];
	}

	// PushCallMuPrimN
	
	public void emitInlinePushCallMuPrimN(MuPrimitive muprim, int arity) {
		emitInlinePushCallMuPrimNGeneral(muprim, arity);
	}
	
	private void emitInlinePushCallMuPrimNGeneral(MuPrimitive muprim, int arity) {
	    mv.visitVarInsn(ALOAD, STACK);		// stack
	    mv.visitVarInsn(ILOAD, SP);			// sp

	    emitIntValue(arity);				// arity

	    mv.visitInvokeDynamicInsn(muprim.name(), 
	        getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE), bootstrapMuPrimitive());

	    mv.visitVarInsn(ISTORE, SP);		// sp = callMuPrimN(stach, sp, arity)
	}
	
	/********************************************************************************************************************/
    /*      CallPrim  / PushCallPrim                                                                                    */  
    /*                                                                                                                  */
    /*      Emits an inline version of CallPrim[012N] or PushCallPrim[012N] instruction and uses invokeDynamic          */
    /*      to call the static enum execute method. In some cases, the prim is special cased.                           */
    /*                                                                                                                  */
    /*      bootstrapRascalPrimitive is used on the first call to a RascalPrimitive to locate its execute method,       */
    /*      also see RascalPrimitive.bootstrapRascalPrimitive                                                           */
    /********************************************************************************************************************/

	Handle bootstrapRascalPrimitive(){
	    MethodType bmt = MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class);
	    Handle bootstrap = new Handle(Opcodes.H_INVOKESTATIC, (RascalPrimitive.class.getName()).replace('.', '/'), "bootstrapRascalPrimitive",
	        bmt.toMethodDescriptorString());
	    return bootstrap;
	}
    
	// CallPrim0
	
	public void emitInlineCallPrim0(RascalPrimitive prim, int srcIndex) {
		emitInlineCallPrim0General(prim, srcIndex, false);
	}
	
	private void emitInlineCallPrim0General(RascalPrimitive prim, int srcIndex, boolean push) {
	    emitInlineFrameObserve(srcIndex);
	    prepare0(push);
	    mv.visitVarInsn(ALOAD, CF);			// currentFrame
	    emitRex();

	    mv.visitInvokeDynamicInsn(prim.name(), 
	        getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE, getType(RascalExecutionContext.class)), bootstrapRascalPrimitive());

	    result(push);
	}
	
	// PushCallPrim0
	
	public void emitInlinePushCallPrim0(RascalPrimitive prim, int srcIndex) {
		emitInlineCallPrim0General(prim, srcIndex, true);
	}
	
	// CallPrim1
	
	public void emitInlineCallPrim1(RascalPrimitive prim, int srcIndex) {
	    emitInlineCallPrim1(prim, srcIndex, false);
	}
	
	private void emitInlineCallPrim1(RascalPrimitive prim, int srcIndex, boolean push) {
	    switch(prim.name()){
	        case "is_bool":
	            emit_is_predicate1("isBool", push); return;
	        case "is_int":
                emit_is_predicate1("isInteger", push); return;
	        case "is_str":
                emit_is_predicate1("isString", push); return;
	        case "is_list":
                emit_is_predicate1("isList", push); return;
	        case "is_set":
                emit_is_predicate1("isSet", push); return;
	        case "is_map":
                emit_is_predicate1("isMap", push); return;
	        case "is_appl":
	            emit_is_appl1(push); return;
	        case "is_node":
	            emit_is_predicate1("isNode", push); return;
	    }
		emitInlineCallPrim1General(prim, srcIndex, push);
	}
	
	private void emitInlineCallPrim1General(RascalPrimitive prim, int srcIndex, boolean push) {
        emitInlineFrameObserve(srcIndex);
        prepare1(push);
        mv.visitVarInsn(ALOAD, CF);     // currentFrame
        emitRex();

        mv.visitInvokeDynamicInsn(prim.name(), 
            getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, FRAME_TYPE, getType(RascalExecutionContext.class)), bootstrapRascalPrimitive());

        result(push);
    }
	
	private void emit_is_appl1(boolean push){ 
	    emitValueFactory();
	    prepare1(push);
	    mv.visitTypeInsn(INSTANCEOF,  getInternalName(org.rascalmpl.values.uptr.ITree.class));
	    Label l1 = new Label();
	    mv.visitJumpInsn(IFEQ, l1);
	    mv.visitVarInsn(ALOAD, ACCU);  // arg_1 from accu
	    mv.visitTypeInsn(CHECKCAST, getInternalName(org.rascalmpl.values.uptr.ITree.class));
	    mv.visitMethodInsn(INVOKESTATIC, getInternalName(org.rascalmpl.values.uptr.TreeAdapter.class), "isAppl", getMethodDescriptor(BOOLEAN_TYPE, getType(org.rascalmpl.values.uptr.ITree.class)), false);
	    mv.visitJumpInsn(IFEQ, l1);
	    mv.visitInsn(ICONST_1);
	    Label l2 = new Label();
	    mv.visitJumpInsn(GOTO, l2);
	    mv.visitLabel(l1);
	    mv.visitInsn(ICONST_0);
	    mv.visitLabel(l2);
	    mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValueFactory.class), "bool", getMethodDescriptor(getType(IBool.class), BOOLEAN_TYPE), true);
	    result(push);
	}
	
	// PushCallPrim1
	
	public void emitInlinePushCallPrim1(RascalPrimitive prim, int srcIndex) {
		emitInlineCallPrim1General(prim, srcIndex, true);
	}
	
	// CallPrim2
	
	public void emitInlineCallPrim2(RascalPrimitive prim, int srcIndex){
	    emitInlineCallPrim2(prim, srcIndex, false);
	}
	
	private void emitInlineCallPrim2(RascalPrimitive prim, int srcIndex, boolean push) {
		switch(prim.name()){
			case "subtype_value_type":
				emitSubtypeValueType2(srcIndex, push);
				return;
			case "subtype_value_value":
				emitSubtypeValueValue2(srcIndex, push);
				return;
		}
	
		emitInlineCallPrim2General(prim, srcIndex, push);
	}
	
	private void emitInlineCallPrim2General(RascalPrimitive prim, int srcIndex, boolean push) {
	    emitInlineFrameObserve(srcIndex);
	    prepare2(push);

	    mv.visitVarInsn(ALOAD, CF);		// currentFrame
	    emitRex();

	    mv.visitInvokeDynamicInsn(prim.name(), 
	        getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, FRAME_TYPE, getType(RascalExecutionContext.class)), bootstrapRascalPrimitive());

	    result(push);
	}
	
	private void emitSubtypeValueType2(int srcIndex, boolean push){
	    emitInlineFrameObserve(srcIndex);
	    if(push){
	        mv.visitVarInsn(ALOAD, STACK); // stack
	        mv.visitVarInsn(ILOAD, SP);    // sp
	    }
	    emitValueFactory();		

	    if(!push){
	        mv.visitIincInsn(SP, -1);
	    }
	    mv.visitVarInsn(ALOAD, STACK);
	    mv.visitVarInsn(ILOAD, SP);
	    mv.visitInsn(AALOAD);              // arg_2
	    //

	    mv.visitTypeInsn(CHECKCAST, getInternalName(IValue.class));
	    mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValue.class), "getType", getMethodDescriptor(TYPE_TYPE), true);

	    mv.visitVarInsn(ALOAD, ACCU);      // arg_1 from accu
	    mv.visitTypeInsn(CHECKCAST, getInternalName(io.usethesource.vallang.type.Type.class));
	    mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(io.usethesource.vallang.type.Type.class), "isSubtypeOf", getMethodDescriptor(BOOLEAN_TYPE, TYPE_TYPE), false);
	    mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValueFactory.class), "bool", getMethodDescriptor(getType(IBool.class), BOOLEAN_TYPE), true);
	    result(push);
	}
	
	private void emitSubtypeValueValue2(int srcIndex, boolean push){
		emitInlineFrameObserve(srcIndex);
		
		 if(push){
             mv.visitVarInsn(ALOAD, STACK); // stack
             mv.visitVarInsn(ILOAD, SP);    // sp
         }
		emitValueFactory();
		
		if(!push){
            mv.visitIincInsn(SP, -1);
        }
        mv.visitVarInsn(ALOAD, STACK);
        mv.visitVarInsn(ILOAD, SP);
        mv.visitInsn(AALOAD);               // arg_2
		
		mv.visitTypeInsn(CHECKCAST, getInternalName(IValue.class));
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValue.class), "getType", getMethodDescriptor(TYPE_TYPE), true);
		
		mv.visitVarInsn(ALOAD, ACCU);		// arg_1 from accu
		mv.visitTypeInsn(CHECKCAST, getInternalName(IValue.class));
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValue.class), "getType", getMethodDescriptor(TYPE_TYPE), true);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(io.usethesource.vallang.type.Type.class), "isSubtypeOf", getMethodDescriptor(BOOLEAN_TYPE, TYPE_TYPE), false);
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValueFactory.class), "bool", getMethodDescriptor(getType(IBool.class), BOOLEAN_TYPE), true);
		result(push);
	}
	
	// PushCallPrim2
	
	public void emitInlinePushCallPrim2(RascalPrimitive prim, int srcIndex){
	    emitInlineCallPrim2(prim, srcIndex, true);
	}
	
	// CallPrim3
    
    public void emitInlineCallPrim3(RascalPrimitive prim, int srcIndex){
        emitInlineCallPrim3(prim, srcIndex, false);
    }
    
    private void emitInlineCallPrim3(RascalPrimitive prim, int srcIndex, boolean push) {
    
        emitInlineCallPrim3General(prim, srcIndex, push);
    }
    
    private void emitInlineCallPrim3General(RascalPrimitive prim, int srcIndex, boolean push) {
        emitInlineFrameObserve(srcIndex);
        prepare2(push);

        mv.visitVarInsn(ALOAD, CF);     // currentFrame
        emitRex();

        mv.visitInvokeDynamicInsn(prim.name(), 
            getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, FRAME_TYPE, getType(RascalExecutionContext.class)), bootstrapRascalPrimitive());

        result(push);
    }
    
    // PushCallPrim3
    
    public void emitInlinePushCallPrim3(RascalPrimitive prim, int srcIndex){
        emitInlineCallPrim3(prim, srcIndex, true);
    }
    
    // CallPrim4
    
    public void emitInlineCallPrim4(RascalPrimitive prim, int srcIndex){
        emitInlineCallPrim4(prim, srcIndex, false);
    }
    
    private void emitInlineCallPrim4(RascalPrimitive prim, int srcIndex, boolean push) {
    
        emitInlineCallPrim4General(prim, srcIndex, push);
    }
    
    private void emitInlineCallPrim4General(RascalPrimitive prim, int srcIndex, boolean push) {
        emitInlineFrameObserve(srcIndex);
        prepare2(push);

        mv.visitVarInsn(ALOAD, CF);     // currentFrame
        emitRex();

        mv.visitInvokeDynamicInsn(prim.name(), 
            getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, FRAME_TYPE, getType(RascalExecutionContext.class)), bootstrapRascalPrimitive());

        result(push);
    }
    
    // PushCallPrim4
    
    public void emitInlinePushCallPrim4(RascalPrimitive prim, int srcIndex){
        emitInlineCallPrim4(prim, srcIndex, true);
    }
    
    // CallPrim5
    
    public void emitInlineCallPrim5(RascalPrimitive prim, int srcIndex){
        emitInlineCallPrim4(prim, srcIndex, false);
    }
    
    private void emitInlineCallPrim5(RascalPrimitive prim, int srcIndex, boolean push) {
    
        emitInlineCallPrim5General(prim, srcIndex, push);
    }
    
    private void emitInlineCallPrim5General(RascalPrimitive prim, int srcIndex, boolean push) {
        emitInlineFrameObserve(srcIndex);
        prepare2(push);

        mv.visitVarInsn(ALOAD, CF);     // currentFrame
        emitRex();

        mv.visitInvokeDynamicInsn(prim.name(), 
            getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE, FRAME_TYPE, getType(RascalExecutionContext.class)), bootstrapRascalPrimitive());

        result(push);
    }
    
    // PushCallPrim5
    
    public void emitInlinePushCallPrim5(RascalPrimitive prim, int srcIndex){
        emitInlineCallPrim5(prim, srcIndex, true);
    }
	
	// CallPrimN
	
	public void emitInlineCallPrimN(RascalPrimitive prim, int arity, int srcIndex) {
		emitInlineCallPrimNGeneral(prim, arity, srcIndex);
	}
	
	private void emitInlineCallPrimNGeneral(RascalPrimitive prim, int arity, int srcIndex) {
		emitInlineFrameObserve(srcIndex);

		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);

		emitIntValue(arity);
		mv.visitVarInsn(ALOAD, CF);
		emitRex();
		
        mv.visitInvokeDynamicInsn(prim.name(), 
            getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE, FRAME_TYPE, getType(RascalExecutionContext.class)), bootstrapRascalPrimitive());
		
		mv.visitInsn(ICONST_M1);
		mv.visitInsn(IADD);					// sp--
		mv.visitVarInsn(ISTORE, SP);		// sp = callPrimN(stack, sp, arity, cf, rex) - 1
		
		mv.visitVarInsn(ALOAD, STACK);		// stack
		mv.visitVarInsn(ILOAD, SP);			// sp
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);		// accu = stack[--sp];
	}
	
	// PushCallPrimN
	
	public void emitInlinePushCallPrimN(RascalPrimitive prim, int arity, int srcIndex) {
		emitInlinePushCallPrimNGeneral(prim, arity, srcIndex);
	}
	
	private void emitInlinePushCallPrimNGeneral(RascalPrimitive prim, int arity, int srcIndex) {
	    emitInlineFrameObserve(srcIndex);
	    
	    mv.visitVarInsn(ALOAD, STACK);
	    mv.visitVarInsn(ILOAD, SP);

	    emitIntValue(arity);
	    mv.visitVarInsn(ALOAD, CF);
	    emitRex();

	    mv.visitInvokeDynamicInsn(prim.name(), 
	        getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE, FRAME_TYPE, getType(RascalExecutionContext.class)), bootstrapRascalPrimitive());

	    mv.visitVarInsn(ISTORE, SP);
	}
	
	// End of CallMuPrim[012N] / PushCallMuPrim[012N]
	
	public void emitInlineLoadBool(boolean b) {
		if (b) {
			mv.visitFieldInsn(GETSTATIC, getInternalName(RascalPrimitive.class), "Rascal_TRUE", getDescriptor(IBool.class));
		} else {
			mv.visitFieldInsn(GETSTATIC, getInternalName(RascalPrimitive.class), "Rascal_FALSE", getDescriptor(IBool.class));
		}
		mv.visitVarInsn(ASTORE, ACCU);		// accu = bool;
	}

	/*
	 * emitOptimizedOcall emits a call to a full ocall implementation unless: 
	 * 1: There is only one function => emit direct call  (DONE) 
	 * 2: There is only a constructor => call constructor (DONE)
	 */
    public void emitOptimizedOcall(String fuid, int overloadedFunctionIndex, int arity, int srcIndex) {
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
					emitCallWithArgsSSFII_A("jvmOCALL", overloadedFunctionIndex, arity);
					mv.visitIincInsn(SP, -arity);
					emitReturnValue2ACCU();
				}
			} else {
				// Has a constructor.
				// TODO srcIndex
				emitCallWithArgsSSFII_A("jvmOCALL", overloadedFunctionIndex, arity);
				mv.visitIincInsn(SP, -arity);
				emitReturnValue2ACCU();
			}
		} else {
			if(functions.length == 0 && of.getConstructors().length == 1){
				// Specialize for single constructor
				// TODO srcIndex
				emitCallWithArgsSSFII_A("jvmOCALLSingleConstructor", overloadedFunctionIndex, arity);
			} else {
				// TODO srcIndex
				emitCallWithArgsSSFII_A("jvmOCALL", overloadedFunctionIndex, arity);
			}
			mv.visitIincInsn(SP, -arity);
			emitReturnValue2ACCU();
		}
	}
    
    // EXPERIMENTAL2
    private void emitOcallSingle(String funName, int fun, int arity, int srcIndex) {
        mv.visitVarInsn(ALOAD, CF);
        
        mv.visitVarInsn(ALOAD, CF);
        mv.visitVarInsn(ILOAD, SP);
        emitInlineFrameEnter(srcIndex);
        
        // Set up invokeDynamic of getFrame
        MethodType bmt = MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, Object.class, Object.class, Object.class);
        Handle bootstrap = new Handle(Opcodes.H_INVOKESTATIC, (RVMonJVM.class.getName()).replace('.', '/'), "bootstrapGetFrameAndCall",
              bmt.toMethodDescriptorString());
         
        mv.visitInvokeDynamicInsn("getFrameAndCall", 
                getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE, FRAME_TYPE, INT_TYPE), bootstrap, fullClassNameDotted, fun, arity);

        mv.visitInsn(POP);

        mv.visitVarInsn(ALOAD, CF);
        mv.visitFieldInsn(GETFIELD, FRAME_NAME, "sp", INT_TYPE.getDescriptor());
        mv.visitVarInsn(ISTORE, SP);
        
        emitInlineFrameLeave(srcIndex);
        emitReturnValue2ACCU();
    }

//    // EXPERIMENTAL1
//	private void emitOcallSingle(String funName, int fun, int arity, int srcIndex) {
//		mv.visitVarInsn(ALOAD, THIS);
//		mv.visitVarInsn(ALOAD, CF);
//		
//		mv.visitVarInsn(ALOAD, CF);
//		mv.visitVarInsn(ILOAD, SP);
//		emitInlineFrameEnter(srcIndex);
//		
//		// Set up invokeDynamic of getFrame
//		MethodType bmt = MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, Object.class);
//		Handle bootstrap = new Handle(Opcodes.H_INVOKESTATIC, (RVMonJVM.class.getName()).replace('.', '/'), "bootstrapGetFrame",
//	          bmt.toMethodDescriptorString());
//		 
//	    mv.visitInvokeDynamicInsn("getFrame", 
//	            getMethodDescriptor(FRAME_TYPE, FRAME_TYPE, FRAME_TYPE, INT_TYPE), bootstrap, fun);
//
//		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, funName, getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE),false);
//		mv.visitInsn(POP);
//
//		mv.visitVarInsn(ALOAD, CF);
//		mv.visitFieldInsn(GETFIELD, FRAME_NAME, "sp", INT_TYPE.getDescriptor());
//		mv.visitVarInsn(ISTORE, SP);
//		
//		emitInlineFrameLeave(srcIndex);
//		emitReturnValue2ACCU();
//	}
	
//	private void emitOcallSingle(String funName, int fun, int arity, int srcIndex) {
//        mv.visitVarInsn(ALOAD, THIS);
//        mv.visitVarInsn(ALOAD, CF);
//        
//        mv.visitVarInsn(ALOAD, THIS);
//        mv.visitFieldInsn(GETFIELD, fullClassName, "functionStore", getDescriptor(Function[].class));
//        emitIntValue(fun);
//        mv.visitInsn(AALOAD);
//
//        mv.visitVarInsn(ALOAD, CF);
//        
//        emitIntValue(arity);
//        mv.visitVarInsn(ILOAD, SP);
//        emitInlineFrameEnter(srcIndex);
//
//        mv.visitMethodInsn(
//                INVOKEVIRTUAL,
//                FRAME_NAME,
//                "getFrame",
//                getMethodDescriptor(FRAME_TYPE, FUNCTION_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE),false);
//        
//        mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, funName, getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE),false);
//        mv.visitInsn(POP);
//
//        mv.visitVarInsn(ALOAD, CF);
//        mv.visitFieldInsn(GETFIELD, FRAME_NAME, "sp", INT_TYPE.getDescriptor());
//        mv.visitVarInsn(ISTORE, SP);
//        
//        emitInlineFrameLeave(srcIndex);
//        emitReturnValue2ACCU();
//    }

	public void emitInlineSwitch(IMap caseLabels, String caseDefault, boolean concrete) {
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

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "switchHelper", getMethodDescriptor(INT_TYPE, OBJECT_TYPE, BOOLEAN_TYPE),false);
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

			mv.visitTryCatchBlock(fromLabel, toLabel, handlerLabel, getInternalName(Thrown.class));
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
		mv.visitFieldInsn(GETFIELD, getInternalName(Thrown.class), "value", getDescriptor(IValue.class));
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValue.class), "getType", getMethodDescriptor(TYPE_TYPE),true);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitFieldInsn(GETFIELD, FRAME_NAME, "function", getDescriptor(Function.class));
		mv.visitFieldInsn(GETFIELD, FUNCTION_NAME, "typeConstantStore", getDescriptor(io.usethesource.vallang.type.Type[].class));

		emitIntValue(type);

		mv.visitInsn(AALOAD);
		mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(io.usethesource.vallang.type.Type.class), "isSubtypeOf", getMethodDescriptor(BOOLEAN_TYPE, TYPE_TYPE),false);
		mv.visitJumpInsn(IFNE, noReThrow);
		mv.visitVarInsn(ALOAD, EXCEPTION);
		mv.visitInsn(ATHROW);
		mv.visitLabel(noReThrow);
	}

	public void emitInlineThrow() {
		mv.visitVarInsn(ALOAD, CF);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitFieldInsn(PUTFIELD, FRAME_NAME, "sp", Type.INT_TYPE.getDescriptor());
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "thrownHelper",
				getMethodDescriptor(getType(Thrown.class), FRAME_TYPE, OBJECT_A_TYPE, INT_TYPE),false);
		 
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
	
	public void emitInlineResetLoc(int pos){
		mv.visitVarInsn(ALOAD, STACK);
		emitIntValue(pos);
		mv.visitInsn(ACONST_NULL);
		mv.visitInsn(AASTORE);
	}

	public void emitInlineResetLocs(IValue positions) {
		IList il = (IList) positions;
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
	
	public void emitInlineResetVar(int what, int pos) {
		mv.visitVarInsn(ALOAD, THIS);
		emitIntValue(what);
		emitIntValue(pos);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "jvmRESETVAR", getMethodDescriptor(VOID_TYPE, INT_TYPE, INT_TYPE, FRAME_TYPE),false);
	}

	// TODO: compare with performance of insnCHECKARGTYPEANDCOPY
	public void emitInlineCheckArgTypeAndCopy(int pos1, int type, int pos2) {
	    
	    if(inUniqueFunction()){
	        emitInlineCheckArgTypeAndCopyUnique(pos1, pos2);
	        return;
	    }
		Label l1 = new Label();
		Label l5 = new Label();

		mv.visitVarInsn(ALOAD, STACK);

		/* sourceLoc */
		emitIntValue(pos1);

		mv.visitInsn(AALOAD);
		
		mv.visitMethodInsn(INVOKEINTERFACE, getInternalName(IValue.class), "getType", getMethodDescriptor(TYPE_TYPE),true);
		mv.visitVarInsn(ALOAD, TS);

		/* type */
		emitIntValue(type);

		mv.visitInsn(AALOAD);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, getInternalName(io.usethesource.vallang.type.Type.class), "isSubtypeOf", getMethodDescriptor(BOOLEAN_TYPE, TYPE_TYPE),false);
		mv.visitJumpInsn(IFEQ, l1);
		mv.visitVarInsn(ALOAD, STACK);

		/* toloc */
		emitIntValue(pos2);

		mv.visitVarInsn(ALOAD, STACK);

		/* sourceLoc */
		emitIntValue(pos1);

		mv.visitInsn(AALOAD);
		mv.visitInsn(AASTORE);
		
		mv.visitFieldInsn(GETSTATIC, getInternalName(RascalPrimitive.class), "Rascal_TRUE", getDescriptor(IBool.class));
		mv.visitVarInsn(ASTORE, ACCU);
		mv.visitJumpInsn(GOTO, l5);
		
		mv.visitLabel(l1);
		mv.visitFieldInsn(GETSTATIC, getInternalName(RascalPrimitive.class), "Rascal_FALSE", getDescriptor(IBool.class));
		mv.visitVarInsn(ASTORE, ACCU);
		mv.visitLabel(l5);
	}
	
	public void emitInlineCheckArgTypeAndCopyUnique(int pos1, int pos2) {
        mv.visitVarInsn(ALOAD, STACK);
        
        /* toloc */
        emitIntValue(pos2);
        
        mv.visitVarInsn(ALOAD, STACK);
        /* sourceLoc */
        emitIntValue(pos1);
        mv.visitInsn(AALOAD);
        
        mv.visitInsn(AASTORE);
        
//        mv.visitFieldInsn(GETSTATIC, getInternalName(RascalPrimitive.class), "Rascal_TRUE", getDescriptor(IBool.class));
//        mv.visitVarInsn(ASTORE, ACCU);
    }

	public void emitInlinePushEmptyKwMap() {
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitTypeInsn(NEW, getInternalName(java.util.HashMap.class));
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, getInternalName(java.util.HashMap.class), INIT_NAME, getMethodDescriptor(VOID_TYPE), false);
		mv.visitInsn(AASTORE);
		mv.visitIincInsn(SP, 1);
	}

	// TODO: eliminate call
	public void emitInlineValueSubtype(int type) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, TS);
		emitIntValue(type);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, "VALUESUBTYPE", getMethodDescriptor(OBJECT_TYPE, TYPE_TYPE, OBJECT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);
	}

	public void emitInlinePopAccu() {
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}

	public void emitInlinePushAccu() {
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
	 protected static String rvm2jvmName(String s) {
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
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);
	}
	
	public void emitCallWithArgsPA_A(String fname) {
		mv.visitVarInsn(ALOAD, THIS);
		
		mv.visitIincInsn(SP, -1);		// sp--
		
		mv.visitVarInsn(ALOAD, STACK);	
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);			// P: arg_2
		
		mv.visitVarInsn(ALOAD, ACCU);	// A: arg_1 from accu
		
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE, OBJECT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}

	public void emitCallWithArgsSSI_S(String fname, int i) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		emitIntValue(i);				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsSI_A(String fname, int i) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		emitIntValue(i);				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}

	public void emitCallWithArgsSSII_S(String fname, int i, int j) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		emitIntValue(i);				// I
		emitIntValue(j);				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsSSII_A(String fname, int i, int j) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		emitIntValue(i);				// I
		emitIntValue(j);				// I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE, INT_TYPE),false);
		
		mv.visitVarInsn(ISTORE, SP);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU);
	}

	public void emitCallWithArgsSSFI_S(String fname, int i) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsFI_A(String fname, int i) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitCallWithArgsSSFI_A(String fname, int i) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitCallWithArgsSFI_A(String fname, int i) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, FRAME_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitCallWithArgsSSFIII_A(String fname, int i, int j, int k) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		emitIntValue(j); 				// I
		emitIntValue(k); 				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}

	public void emitVoidCallWithArgsSSI_S(String fname, int i) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		emitIntValue(i);				// I
		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(VOID_TYPE, OBJECT_A_TYPE, INT_TYPE, INT_TYPE),false);
	}

	public void emitCallWithArgsSSFII_S(String fname, int i, int j) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		emitIntValue(j); 				// I

		
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsSSFII_A(String fname, int i, int j) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP); 	// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(i); 				// I
		emitIntValue(j); 				// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(OBJECT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitCallWithArgsFII_A(String fname, int i, int j) {
		mv.visitVarInsn(ALOAD, THIS);
		
		mv.visitVarInsn(ALOAD, CF);		// F

		emitIntValue(i); 				// I
		emitIntValue(j); 				// I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ASTORE, ACCU);	// _A
	}
	
	public void emitVoidCallWithArgsFIIA(String fname, int what, int pos) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(what); 			// I
		emitIntValue(pos); 				// I
		mv.visitVarInsn(ALOAD, ACCU);	// A

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE, OBJECT_TYPE),false);
	}

	public void emitCallWithArgsSSFIIIII_S(String fname, int methodName, int className, int parameterTypes, int keywordTypes, int reflect) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP);		// S
		mv.visitVarInsn(ALOAD, CF); 	// F

		emitIntValue(methodName); 		// I
		emitIntValue(className); 		// I
		emitIntValue(parameterTypes); 	// I
		emitIntValue(keywordTypes); 	// I
		emitIntValue(reflect); // I

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitVoidCallWithArgsSFIA(String fname, int pos) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK);	// S
		mv.visitVarInsn(ALOAD, CF); 	// F
		emitIntValue(pos); 				// I
		mv.visitVarInsn(ALOAD, ACCU);	// A
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(VOID_TYPE, OBJECT_A_TYPE, FRAME_TYPE, INT_TYPE, OBJECT_TYPE),false);
	}
	
	public void emitVoidCallWithArgsFIA(String fname, int pos) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF); 	// F
		emitIntValue(pos); 				// I
		mv.visitVarInsn(ALOAD, ACCU);	// A
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE, OBJECT_TYPE),false);
	}
	
	public void emitVoidCallWithArgsFII(String fname, int scope, int pos) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF); 	// F
		emitIntValue(scope); 			// I
		emitIntValue(pos); 				// I	
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(VOID_TYPE, FRAME_TYPE, INT_TYPE, INT_TYPE),false);
	}


	public void emitCallWithArgsSSF_S(String fname) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP);    	// S

		mv.visitVarInsn(ALOAD, CF);		// F

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);	// _S
	}
	
	public void emitCallWithArgsSSF_A(String fname) {
		
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, STACK); 	// S
		mv.visitVarInsn(ILOAD, SP);    	// S

		mv.visitVarInsn(ALOAD, CF);		// F

		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(INT_TYPE, OBJECT_A_TYPE, INT_TYPE, FRAME_TYPE),false);
		mv.visitVarInsn(ISTORE, SP);
		mv.visitIincInsn(SP, -1);
		mv.visitVarInsn(ALOAD, STACK); 
		mv.visitVarInsn(ILOAD, SP);
		mv.visitInsn(AALOAD);
		mv.visitVarInsn(ASTORE, ACCU); 
	}
	
	public void emitCallWithArgsFA_A(String fname) {
		mv.visitVarInsn(ALOAD, THIS);
		mv.visitVarInsn(ALOAD, CF);		// F
		mv.visitVarInsn(ALOAD, ACCU);	// A
		mv.visitMethodInsn(INVOKEVIRTUAL, fullClassName, fname, getMethodDescriptor(OBJECT_TYPE, FRAME_TYPE, OBJECT_TYPE),false);
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
		mv.visitMethodInsn(INVOKESTATIC, fullClassName, "debugINSTRUCTION", getMethodDescriptor(VOID_TYPE, getType(String.class), FRAME_TYPE, INT_TYPE, OBJECT_TYPE),false);
	}
	
	public void emitDebugCall1(String ins, int arg1) {
		mv.visitLdcInsn(ins);
		mv.visitLdcInsn(arg1);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKESTATIC, fullClassName, "debugINSTRUCTION1", getMethodDescriptor(Type.VOID_TYPE, getType(String.class), INT_TYPE, FRAME_TYPE, INT_TYPE, OBJECT_TYPE),false);
	}
	
	public void emitDebugCall2(String ins, String arg1, int arg2) {
		mv.visitLdcInsn(ins);
		mv.visitLdcInsn(abbrev(arg1));
		mv.visitLdcInsn(arg2);
		mv.visitVarInsn(ALOAD, CF);
		mv.visitVarInsn(ILOAD, SP);
		mv.visitVarInsn(ALOAD, ACCU);
		mv.visitMethodInsn(INVOKESTATIC, fullClassName, "debugINSTRUCTION2", getMethodDescriptor(Type.VOID_TYPE, getType(String.class),  getType(String.class), INT_TYPE, FRAME_TYPE, INT_TYPE, OBJECT_TYPE),false);
	}
}
