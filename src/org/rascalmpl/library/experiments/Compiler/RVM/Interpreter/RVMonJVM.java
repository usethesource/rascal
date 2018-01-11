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

import static java.lang.invoke.MethodHandles.filterReturnValue;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class RVMonJVM extends RVMCore {
    
    /********************************************************************************/
    /* RVMonJVM extends RVMCore and is itself extended by the class produced by     */
    /* BytecodeGenerator.                                                           */
    /********************************************************************************/
    
    /*
     * The following instance variables are only used by executeProgram
     */
    public Frame root; // Root frame of a program
//    Thrown thrown;

    // TODO : ccf, cccf and activeCoroutines needed to allow exception handling in coroutines. :(
    
    // Return types of dynRun
    
    static protected final IString NONE         = ValueFactoryFactory.getValueFactory().string("$none$");
    static protected final IString YIELD        = ValueFactoryFactory.getValueFactory().string("$yield$");
    static protected final IString FAILRETURN   = ValueFactoryFactory.getValueFactory().string("$failreturn$");
    static protected final IString PANIC        = ValueFactoryFactory.getValueFactory().string("$panic$");
    
    protected Object returnValue = null;        // Actual return value of functions
    
    //EXPERIMENTAL
    static HashMap<String,RVMonJVM> currentRVMonJVM = new HashMap<>();
    static HashMap<String,Function[]> functionStores = new HashMap<>();

    public RVMonJVM(RVMExecutable rvmExec, RascalExecutionContext rex) {
        super(rvmExec, rex);
        String generatedClassName = rvmExec.getGeneratedClassQualifiedName();
        currentRVMonJVM.put(generatedClassName, this);
        functionStores.put(generatedClassName, functionStore);
    }
    
    /************************************************************************************/
    /*      Implementation of abstract methods in RVMCore for RVMonJVM                  */
    /************************************************************************************/
    
    /* (non-Javadoc)
     * Implements abstract function for RVMonJVM
     * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore#executeRVMFunction(org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function, io.usethesource.vallang.IValue[], java.util.Map)
     */
    @Override
    public Object executeRVMFunction(Function func, IValue[] posArgs, Map<String,IValue> kwArgs){
        // Assumption here is that the function called is not a nested one
        // and does not use global variables
        Frame root = new Frame(func.scopeId, null, func.maxstack, func);
        Frame cf = root;
        
        // Pass the program arguments to main
        for(int i = 0; i < posArgs.length; i++){
            cf.stack[i] = posArgs[i]; 
        }
        cf.stack[func.nformals-1] =  kwArgs; // new HashMap<String, IValue>();
        cf.sp = func.getNlocals();
        //cf.stack[func.nformals] = kwArgs == null ? new HashMap<String, IValue>() : kwArgs;
        
        try {
            func.handle.invoke(this,cf);
            if(returnValue instanceof Thrown){
                frameObserver.exception(cf, (Thrown) returnValue);
                throw (Thrown) returnValue;
            }
            return returnValue;
        }
        catch (RuntimeException e) {
            // all unchecked exceptions are passed on
            throw e;
        }
        catch (InvocationTargetException e) {
             Throwable targetException = e.getTargetException();
             if (targetException instanceof RuntimeException) {
                 throw (RuntimeException) targetException;
             }
             
             throw new RuntimeException(targetException);
        }
        catch (Throwable e){
            // the other (checked) exceptions are wrapped 
            throw new RuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * Implements abstract function for RVMonJVM
     * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore#executeRVMFunction(org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.FunctionInstance, io.usethesource.vallang.IValue[])
     */
    @Override
    public IValue executeRVMFunction(FunctionInstance func, IValue[] posArgs, Map<String, IValue> kwArgs) {

//        Thrown oldthrown = thrown;

        Frame root = new Frame(func.function.scopeId, null, func.env, func.function.maxstack, func.function);
        root.sp = func.function.getNlocals();

        // Pass the program arguments to main
        for (int i = 0; i < posArgs.length; i++) {
            root.stack[i] = posArgs[i];
        }
        root.stack[posArgs.length] = kwArgs;

        try {
            func.function.handle.invoke(this, root);
        } catch (Throwable e) {
            if(e instanceof Thrown){
                throw (Thrown) e;
            } 
            else {
                throw new RuntimeException(e);
            }
        }
//        thrown = oldthrown;

        if (returnValue instanceof Thrown) {
            frameObserver.exception(root, (Thrown) returnValue);
            throw (Thrown) returnValue;
        }
        if(returnValue instanceof IValue){
          IValue v = (IValue) returnValue;
          Type tp = v.getType();
          if(tp.isAbstractData() && tp.getName().equals("RuntimeException")){
            Thrown th = Thrown.getInstance(v, null, null);
            frameObserver.exception(root, th);
            throw th;
          }
        }
        return narrow(returnValue);
    }
    
    /* (non-Javadoc)
     * Implements abstract function for RVMonJVM
     * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore#executeRVMFunction(org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunctionInstance, io.usethesource.vallang.IValue[])
     */
    @Override
    public IValue executeRVMFunction(OverloadedFunctionInstance func, IValue[] posArgs, Map<String, IValue> kwArgs){        
        Function firstFunc = func.getFunctions()[0]; // TODO: null?
        int arity = posArgs.length + 1;
        int scopeId = func.env == null ? 0 : func.env.scopeId;
        Frame root = new Frame(scopeId, null, func.env, arity+2, firstFunc);

        // Pass the program arguments to func
        for(int i = 0; i < posArgs.length; i++) {
            root.stack[i] = posArgs[i]; 
        }
        root.stack[arity - 1] = kwArgs;
        root.sp = arity;
        root.previousCallFrame = null;

        OverloadedFunctionInstanceCall ofunCall = new OverloadedFunctionInstanceCall(root, func.getFunctions(), func.getConstructors(), func.env, null, arity, rex);

        Frame frame = ofunCall.nextFrame();
        while (frame != null) {
            
            Object result;
            try {
                result = frame.function.handle.invoke(this, frame);
            }
            catch (Throwable e) {
                if(e instanceof Thrown){
                    throw (Thrown) e;
                } else {
                    throw new RuntimeException(e);
                }
            }
            if (result == NONE) {
                return narrow(returnValue); // Alternative matched.
            }
            frame = ofunCall.nextFrame();
        }
        Type constructor = ofunCall.nextConstructor();

        return vf.constructor(constructor, ofunCall.getConstructorArguments(constructor.getArity()));
    }
    
    /* (non-Javadoc)
     * Implements abstract function for RVMonJVM
     * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore#executeRVMFunctionInVisit(org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame)
     */
    @Override
    public IValue executeRVMFunctionInVisit(Frame root){
        root.sp = root.function.getNlocals();   // TODO: should be done at frame creation.
        try {
            root.function.handle.invoke(this, root);
        } catch (Throwable e) {
            if(e instanceof Thrown){
                throw (Thrown) e;
            } else {
                throw new RuntimeException(e);
            }
        }
        if(returnValue instanceof Thrown){
            frameObserver.exception(root, (Thrown) returnValue);
            throw (Thrown) returnValue;
        }
        return (IValue) returnValue;
    }
    
    /* (non-Javadoc)
     * Implements abstract function for RVMonJVM
     * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore#executeRVMProgram(java.lang.String, java.lang.String, io.usethesource.vallang.IValue[], java.util.HashMap)
     */
    @Override
    public IValue executeRVMProgram(String moduleName, String uid_main, IValue[] posArgs, Map<String,IValue> kwArgs) {
        
        try {
            increaseActivationDepth();

            rex.setFullModuleName(moduleName);

            Function main_function = functionStore[functionMap.get(uid_main)];

            if (main_function == null) {
                throw new RuntimeException("PANIC: No function " + uid_main + " found");
            }

            //Thrown oldthrown = thrown;    //<===

            executeRVMProgram(main_function, posArgs, kwArgs);

            //thrown = oldthrown;

            Object o = returnValue;
            if (o != null){
                if(o instanceof Thrown) {
                    //TODO: frameObserver.exception(cf, (Thrown) thrown);
                    throw (Thrown) o;
                }
                if(o instanceof IValue){
                    IValue v = (IValue) o;
                    Type tp = v.getType();
                    if(tp.isAbstractData() && tp.getName().equals("RuntimeException")){
                        throw Thrown.getInstance(v, null, null);
                    }

                }
            }
            return narrow(o);
        } finally {
            decreaseActivationDepth();
        }
    }
    
    private Object executeRVMProgram(Function func, final IValue[] args, Map<String, IValue> kwArgs) {
        
        root = new Frame(func.scopeId, null, func.maxstack, func);

        root.stack[0] = vf.list(args); // pass the program argument to
        root.stack[1] = kwArgs;
        root.sp = func.getNlocals();

        try {
            return func.handle.invoke(this, root);
        } catch (Throwable e) {
            if(e instanceof Thrown){
                throw (Thrown) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }
    
    /********************************************************************************************/
    /*  When the BytecodeGenerator is called with debug == true, calls will be generated to the */
    /*  following methods that produce a runtime trace similar to that of the RVM interpreter.  */
    /********************************************************************************************/
    
    private static final boolean verbose = true;        // Turn debug output during JVM execution on/off
    
    private static final int MAXLEN = 80;
    
    private static String abbrev(String sval){
        if(sval.length() > MAXLEN){
            sval = sval.substring(0, MAXLEN) + " ...";
        }
        return sval;
    }
    
    private static boolean interesting(Frame cf) {
        return verbose; // && cf.function.name.contains("extractScopes");
    }
    
    private static void printFrameAndStackAndAccu(Frame cf, int sp, Object accu){
        System.err.println(cf + ", scope " + cf.scopeId);
//      System.err.println("sp: " + sp);
        for(int i = 0; i < sp; i++){
            if(cf.stack[i] != null){
                String isLocal = i < cf.function.getNlocals() ? "*" : " ";
                System.err.println("\t" + isLocal + i + ": " + abbrev(asString(cf.stack[i])));
            }
        }

        System.err.println("\tacc: " + abbrev(asString(accu)));
    }
    
    public static void debugINSTRUCTION(String insName, Frame cf, int sp, Object accu){
        if(interesting(cf)){
            printFrameAndStackAndAccu(cf, sp, accu);
            System.err.println(insName +"\n");
        }
    }
    
    public static void debugINSTRUCTION1(String insName, int arg1, Frame cf, int sp, Object accu){
        if(interesting(cf)){
            printFrameAndStackAndAccu(cf, sp, accu);
            System.err.println(insName + " " + arg1 + "\n");
        }
    }
    
    public static void debugINSTRUCTION2(String insName, String arg1, int arg2, Frame cf, int sp, Object accu){
        if(interesting(cf)){
            printFrameAndStackAndAccu(cf, sp, accu);
            System.err.println(insName + " " + abbrev(arg1) + ", " + arg2 + "\n");
        }
    }
    
    /********************************************************************************************/
    /*  Utitities for FrameObservers                                                            */
    /********************************************************************************************/
    
    public static void frameUpdateSrc(Frame cf, int srcIndex){
        cf.src = (ISourceLocation) cf.function.constantStore[srcIndex];
    }
    
    public void frameObserve(Frame cf, int srcIndex){
        cf.src = (ISourceLocation) cf.function.constantStore[srcIndex];
        frameObserver.observe(cf);
    }
    
    public void frameEnter(Frame cf, int srcIndex){
        cf.src = (ISourceLocation) cf.function.constantStore[srcIndex];
        frameObserver.enter(cf);
    }
    
    public void frameLeave(Frame cf, IValue rval){
        frameObserver.leave(cf, rval);
    }
    
    public void frameException(Frame cf, Thrown thrown){
        frameObserver.exception(cf, thrown);
    }
    
    /************************************************************************************************/
    /*  All following functions are called from generated byte code                                 */
    /*  - insn* methods completely implement one RVM instruction                                    */
    /*  - *helper methods are called as part of the inline implementation of one RVM instruction    */
    /************************************************************************************************/
    
    /**
     * Bootstrap method used by emitOcallSingle in BytecodeGenerator
     * - Object className  name of the generated bytecode class (String)
     * - Object funId,     id of the function to be called (Integer)
     * - Object arity      arity of that function (Integer)
     * This method will create a static call site that
     * - first calls getFrame
     * - then applies the function with funId to it.
     */
    public static CallSite bootstrapGetFrameAndCall(MethodHandles.Lookup caller, String name, MethodType type, Object className, Object funId, Object arity) throws NoSuchMethodException, IllegalAccessException, ClassNotFoundException {
        MethodHandles.Lookup lookup = caller;
        String generatedClassName = (String) className;
        Function func = functionStores.get(generatedClassName)[(Integer) funId];
        MethodType getFrameType = MethodType.methodType(Frame.class, Function.class, Frame.class, int.class, int.class);

        MethodHandle getFrame = lookup.findVirtual(Frame.class, "getFrame", getFrameType);
        MethodHandle getFrame1 = MethodHandles.insertArguments(getFrame, 1, func);
        MethodHandle getFrame2 = MethodHandles.insertArguments(getFrame1, 2, (Integer) arity);
         
        MethodHandle funToCall = func.handle;
        MethodHandle funToCall1 = MethodHandles.insertArguments(funToCall, 0, currentRVMonJVM.get(generatedClassName));
        MethodHandle combined = filterReturnValue(getFrame2, funToCall1);
        
        return new ConstantCallSite(combined.asType(type));
    }

    public int insnUNWRAPTHROWNLOC(Object[] stack, int sp, int target) {
        stack[target] = ((Thrown) stack[--sp]).getValue();
        return sp;
    }

    public int insnPUSH_ROOT_FUN(Object[] stack, int sp, int fun) {
        stack[sp++] = new FunctionInstance(functionStore[fun], root, this);
        return sp;
    }

    public int insnPUSH_NESTED_FUN(Object[] stack, int sp, Frame cf, int fun, int scopeIn) {
        stack[sp++] = FunctionInstance.computeFunctionInstance(functionStore[fun], cf, scopeIn, this);
        return sp;
    }

    public int insnPUSHOFUN(Object[] stack, int sp, Frame cf, int ofun) {
        OverloadedFunction of = overloadedStore[ofun];
        stack[sp++] = of.getScopeIn() == -1 ? new OverloadedFunctionInstance(of.functionsAsFunction, of.constructorsAsType, root, this) 
                                            : OverloadedFunctionInstance.computeOverloadedFunctionInstance(of.functionsAsFunction, of.constructorsAsType, cf, of.getScopeIn(), this);
        return sp;
    }

    public int insnPUSHCONSTR(Object[] stack, int sp, int construct) {
        Type constructor = constructorStore[construct];
        stack[sp++] = constructor;
        return sp;
    }

    public int insnCALLJAVA(Object[] stack, int sp, Frame cf, int m, int c, int p, int k, int reflect) {
        int newsp = sp;
        Type parameterTypes = cf.function.typeConstantStore[p];
        Type keywordTypes = cf.function.typeConstantStore[k];

        Class<?> clazz = cf.function.getJavaClass();
        Method method = cf.function.getJavaMethod();
        if (method == null || clazz == null) {
            String methodName = ((IString) cf.function.constantStore[m]).getValue();
            String className = ((IString) cf.function.constantStore[c]).getValue();
            clazz = getJavaClass(className);
            method = getJavaMethod(clazz, methodName, className, parameterTypes, keywordTypes, reflect);
            cf.function.setJavaMetaObjects(clazz, method);
        }

        try {
            newsp = callJavaMethod(clazz, method, parameterTypes, keywordTypes, reflect, stack, sp);
        } catch (Throw e) {
//            thrown = Thrown.getInstance(e.getException(), e.getLocation(), cf);
            throw Thrown.getInstance(e.getException(), e.getLocation(), cf);
        } catch (Thrown e) {
//            thrown = e;
            throw e;
        } catch (ParseError e){
            throw e;
        } catch (RuntimeException e) { // rethrow unchecked exceptions
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e); // wrap checked exceptions 
        }
        
        return newsp;
    }

    public int insnAPPLY(Object[] stack, int sp, int function, int arity) {
        FunctionInstance fun_instance;
        Function fun = functionStore[function];
        assert arity <= fun.nformals;
        assert fun.scopeIn == -1;
        fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, stack, sp);
        sp = sp - arity;
        stack[sp++] = fun_instance;
        return sp;
    }

    public int insnAPPLYDYN(Object[] stack, int sp, int arity) {
        FunctionInstance fun_instance;
        Object src = stack[--sp];
        if (src instanceof FunctionInstance) {
            fun_instance = (FunctionInstance) src;
            assert arity + fun_instance.next <= fun_instance.function.nformals;
            fun_instance = fun_instance.applyPartial(arity, stack, sp);
        } else {
            throw new RuntimeException("Unexpected argument type for APPLYDYN: " + asString(src));
        }
        sp = sp - arity;
        stack[sp++] = fun_instance;
        return sp;
    }

    public Object insnSUBSCRIPTARRAY(Object arg_2, Object arg_1) {
        return ((Object[]) arg_2)[((Integer) arg_1)];
    }

    public Object insnSUBSCRIPTLIST(Object arg_2, Object arg_1) {
        return ((IList) arg_2).get((Integer) arg_1);
    }

    public Object insnLESSINT(Object arg_2, Object arg_1) {
        return ((Integer) arg_2) < ((Integer) arg_1) ? RascalPrimitive.Rascal_TRUE : RascalPrimitive.Rascal_FALSE;
    }

    public Object insnGREATEREQUALINT(Object arg_2, Object arg_1) {
        return ((Integer) arg_2) >= ((Integer) arg_1) ? RascalPrimitive.Rascal_TRUE : RascalPrimitive.Rascal_FALSE;
    }

    public Object insnADDINT(Object arg_2, Object arg_1) {
        return  ((Integer) arg_2) + ((Integer) arg_1);
    }

    public Object insnSUBTRACTINT(Object arg_2, Object arg_1) {
        return ((Integer) arg_2) - ((Integer) arg_1);
    }

    public Object insnANDBOOL(Object arg_2, Object arg_1) {
        return ((IBool) arg_2).and((IBool) arg_1);
    }

    public Object insnTYPEOF(Object arg_1) {
        if (arg_1 instanceof HashSet<?>) { // For the benefit of set matching
            @SuppressWarnings("unchecked")
            HashSet<IValue> mset = (HashSet<IValue>) arg_1;
            if (mset.isEmpty()) {
                return tf.setType(tf.voidType());
            } else {
                IValue v = mset.iterator().next();
                return tf.setType(v.getType());
            }
        } else {
            return ((IValue) arg_1).getType();
        }
    }

    public Object insnSUBTYPE(Object arg_2, Object arg_1) {
        return vf.bool(((Type) arg_2).isSubtypeOf((Type) arg_1));
    }

    public Object insnCHECKARGTYPEANDCOPY(Object[] lstack, int lsp, Frame cof, int loc, int type, int toLoc) {
        Type argType = ((IValue) lstack[loc]).getType();
        Type paramType = cof.function.typeConstantStore[type];

        if (argType.isSubtypeOf(paramType)) {
            lstack[toLoc] = lstack[loc];
            return RascalPrimitive.Rascal_TRUE;
        } else {
            return RascalPrimitive.Rascal_FALSE;
        }
    }

    public void insnLABEL() {
        throw new RuntimeException("label instruction at runtime");
    }

    public void insnHALT(Object[] stack, int sp) {
        stdout.println("Program halted:");
        for (int i = 0; i < sp; i++) {
            stdout.println(i + ": " + stack[i]);
        }
        return;
    }
    
    /************************************************************************************************/
    /* JVM helper methods, called from inline jvm code generated for an RVM instruction             */
    /************************************************************************************************/
    
    public void coreturn0Helper(final Frame cof) {
        if (cof == ccf) {
            activeCoroutines.pop();
            ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
        }

        returnValue = RascalPrimitive.Rascal_TRUE;
    }
    
    public void coreturn1Helper(final Object[] lstack, int sop, final Frame cof, final int arity) {
        int[] refs = cof.function.refs;
        if (arity != refs.length) {
            throw new RuntimeException("Coroutine " + cof.function.name + ": arity of return (" + arity + ") unequal to number of reference parameters (" + refs.length + ")");
        }
        for (int i = 0; i < arity; i++) {
            Reference ref = (Reference) lstack[refs[arity - 1 - i]];
            //ref.stack[ref.pos] = lstack[--sop];
            ref.setValue(lstack[--sop]);
        }

        returnValue = RascalPrimitive.Rascal_TRUE;
    }
    
    public Object jvmCREATE(final Object[] stack, final int sp, final Frame cf, final int fun, final int arity) {
        Function func = functionStore[fun];
        cccf = cf.getCoroutineFrame(func, root, arity, sp);
        cccf.previousCallFrame = cf;

        // lcf.sp = modified by getCoroutineFrame.
        // Run untill guard, leaves coroutine instance in stack.
        try {
            func.handle.invoke(this, cccf); 
        } catch (Throwable e) {
            if(e instanceof Thrown){
                throw (Thrown) e;
            } else {
                throw new RuntimeException(e);
            }
        }
        return returnValue;
    }

    public Object jvmCREATEDYN(final Object[] stack, int sp, final Frame cf, final int arity) {
        FunctionInstance fun_instance;

        Object src = stack[--sp];

        if (!(src instanceof FunctionInstance)) {
            throw new RuntimeException("Unexpected argument type for CREATEDYN: " + src.getClass() + ", " + src);
        }

        // In case of partial parameter binding
        fun_instance = (FunctionInstance) src;
        cccf = cf.getCoroutineFrame(fun_instance, arity, sp);
        sp = cf.sp;
        cccf.previousCallFrame = cf;

        // cf.sp = modified by getCoroutineFrame.
        try {
            fun_instance.function.handle.invoke(this, cccf);
        } catch (Throwable e) {
            if(e instanceof Thrown){
                throw (Thrown) e;
            } else {
                throw new RuntimeException(e);
            }
        }
        return returnValue;
    }

    public int typeSwitchHelper(final Object accu) { // stackpointer calc is done in the inline part.
        IValue val = (IValue) accu;
        Type t = null;
        if (val instanceof IConstructor) {
            t = ((IConstructor) val).getConstructorType();
        } else {
            t = val.getType();
        }
        return ToplevelType.getToplevelTypeAsInt(t);
    }

    public int switchHelper(final Object accu, final boolean useConcreteFingerprint) {
        IValue val = (IValue) accu;
        IInteger fp = vf.integer(ToplevelType.getFingerprint(val, useConcreteFingerprint));
        int toReturn = fp.intValue();
        return toReturn;
    }

    public boolean guardHelper(final Object accu) {
        boolean precondition;
        if (accu instanceof IBool) {
            precondition = ((IBool) accu).getValue();
        } else {
            throw new RuntimeException("Guard's expression has to be boolean!");
        }
        return precondition;
    }

    public void yield1Helper(final Frame cf, Object[] stack, int sp, final int arity, final int ep) {
        // Stores a RascalPrimitive.Rascal_TRUE value into the stack of the NEXT? caller.
        // The inline yield1 does the return

        Coroutine coroutine = activeCoroutines.pop();
        ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;

        returnValue = RascalPrimitive.Rascal_TRUE;
        int[] refs = cf.function.refs;

        for (int i = 0; i < arity; i++) {
            Reference ref = (Reference) stack[refs[arity - 1 - i]];
            //ref.stack[ref.pos] = stack[--sp];
            ref.setValue(stack[--sp]);
        }

        cf.hotEntryPoint = ep;
        cf.sp = sp;

        coroutine.frame = cf;
        coroutine.suspended = true;
    }

    public void yield0Helper(final Frame cf, final Object[] stack, final int sp, final int ep) {
        // Stores a RascalPrimitive.Rascal_TRUE value into the stack of the NEXT? caller.
        // The inline yield0 does the return

        Coroutine coroutine = activeCoroutines.pop();
        ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;

        returnValue = RascalPrimitive.Rascal_TRUE;
        
        cf.hotEntryPoint = ep;
        cf.sp = sp;

        coroutine.frame = cf;
        coroutine.suspended = true;
    }

    public Object callHelper(final Object[] stack, int sp, final Frame cf, final int funid, final int arity, final int ep) {
        Frame tmp;
        Function fun;
        Object result;

        if (cf.hotEntryPoint != ep) {
            fun = functionStore[funid];
            // In case of partial parameter binding
            if (arity < fun.nformals) {
                FunctionInstance fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, stack, sp);
                sp = sp - arity;
                returnValue = fun_instance;
                cf.sp = sp;
                return NONE;
            }
            tmp = cf.getFrame(fun, root, arity, sp);
            cf.nextFrame = tmp;
        } else {
            tmp = cf.nextFrame;
            fun = tmp.function;
        }
        tmp.previousCallFrame = cf;
     
        try {
            result = fun.handle.invoke(this, tmp);
        } catch (Throwable e) {
            if(e instanceof Thrown){
                throw (Thrown) e;
            } else {
                throw new RuntimeException(e);
            }
        }
        
        if (result == YIELD) {
            // drop my stack
            cf.hotEntryPoint = ep;
            return YIELD; // Will cause the inline call to return YIELD
        } else {
            cf.hotEntryPoint = 0;
            cf.nextFrame = null; // Allow GC to clean
            return NONE; // Inline call will continue execution
        }
    }

    public Object jvmNEXT0(final Frame cf, final Object accu) {
        
        Coroutine coroutine = (Coroutine) accu;
        
        if (!coroutine.hasNext()) {
            return RascalPrimitive.Rascal_FALSE;
        } 
        // put the coroutine onto the stack of active coroutines
        activeCoroutines.push(coroutine);
        ccf = coroutine.start;
        coroutine.next(cf);

        // Push something on the stack of the prev yielding function
        coroutine.frame.stack[coroutine.frame.sp++] = null;

        coroutine.frame.previousCallFrame = cf;
        try {
            coroutine.entryFrame.function.handle.invoke(this, coroutine.entryFrame);
        } catch (Throwable e) {
            if(e instanceof Thrown){
                throw (Thrown) e;
            } else {
                throw new RuntimeException(e);
            }
        }
        return returnValue;
    }

    public Object exhaustHelper(final Object[] stack, final int sp, final Frame cf) {
        
        if (cf == ccf) {
            activeCoroutines.pop();
            ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
        }

        returnValue = RascalPrimitive.Rascal_FALSE;
        
        if (cf.previousCallFrame == null) {
            return RascalPrimitive.Rascal_FALSE;
        }
        
        return NONE;// i.e., signal a failure;
    }
    public Object jvmOCALL(final Object[] stack, final int sp, final Frame cf, final int ofun, final int arity) {
        cf.sp = sp;

        OverloadedFunction of = overloadedStore[ofun];
        
        Object arg0 = stack[sp - arity];
        Function[] functions = of.getFunctions(arg0);
        
        Frame previousScope = cf;        
        frameObserver.enter(root);
        
        for(int fid = 0; fid < functions.length; fid++){
            Frame frame = cf.getFrame(functions[fid], previousScope, arity, sp);
            try {
                if(frame.function.handle.invoke(this, frame) == NONE){
                    frameObserver.leave(root, returnValue);
                    return returnValue; // Alternative matched.
                }
            } catch (Throwable e) {
                if(e instanceof Thrown){
                    throw (Thrown) e;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
        Type[] constructors = of.getConstructors(arg0);
        if(constructors.length > 0){
            Type constructor;
            try {
                constructor = constructors[0];
            } catch (Throwable e) {
                if(e instanceof Thrown){
                    throw (Thrown) e;
                } else {
                    throw new RuntimeException(e);
                }
            }

            cf.sp = sp - arity;

            int carity = constructor.getArity();
            IValue[] cargs = new IValue[carity];
            int basesp = sp - carity - 1;
            for(int i = 0; i < carity; i++) {
                cargs[i] = (IValue) stack[basesp + i]; 
            }

            returnValue = vf.constructor(constructor, cargs);
            //frameObserver.leave(frame, returnValue);
            return returnValue;
        }
        
        IListWriter w = vf.listWriter();
        int base = sp - arity;
        for (int i = 0; i < arity; i++) {
            Object arg = stack[base + i];
            
            if (arg instanceof IValue) {
                w.append((IValue) arg);
            }
        }
        
        Thrown exc = RascalRuntimeException.failed(cf.src, w.done(), cf);
        System.err.println(exc.getValue());
        throw exc;
    }

//  public Object jvmOCALL_OLD(final Object[] stack, int sp, final Frame cf, final int ofun, final int arity) {
//      cf.sp = sp;
//
//      OverloadedFunction of = overloadedStore[ofun];
//      
//      Object arg0 = stack[sp - arity];
//      OverloadedFunctionInstanceCall ofun_call = 
//          of.getScopeIn() == -1 ? new OverloadedFunctionInstanceCall(cf, of.getFunctions(arg0), of.getConstructors(arg0), cf, null, arity, rex) 
//                                : OverloadedFunctionInstanceCall.computeOverloadedFunctionInstanceCall(cf, of.getFunctions(arg0), of.getConstructors(arg0), of.getScopeIn(), null, arity, rex);
//      
//      Frame frame = ofun_call.nextFrame();
//      
//      frameObserver.enter(root);
//      
//      while (frame != null) { 
//          try {
//              if(frame.function.handle.invoke(this, frame) == NONE){
//                  frameObserver.leave(root, returnValue);
//                  return returnValue; // Alternative matched.
//              }
//          } catch (Throwable e) {
//              if(e instanceof Thrown){
//                  throw (Thrown) e;
//              } else {
//                  throw new RuntimeException(e);
//              }
//          }
//          frame = ofun_call.nextFrame();
//      }
//      Type constructor;
//      try {
//          constructor = ofun_call.nextConstructor();
//      } catch (Throwable e) {
//          if(e instanceof Thrown){
//              throw (Thrown) e;
//          } else {
//              throw new RuntimeException(e);
//          }
//      }
//      
//      sp = sp - arity;
//      cf.sp = sp;
//  
//      returnValue = vf.constructor(constructor, ofun_call.getConstructorArguments(constructor.getArity()));
//      //frameObserver.leave(frame, returnValue);
//      return returnValue;
//  }
    
    public Object jvmOCALLSingleConstructor(final Object[] stack, int sp, final Frame cf, final int ofun, final int arity) {
        cf.sp = sp;

        OverloadedFunction of = overloadedStore[ofun];
        
        Type constructor = of.constructorsAsType[0];
        
        IValue[] args = new IValue[arity - 1];  // Ignore kw parameters, see OverloadedFunctionInstanceCall.getConstructorArguments
        for(int i = 0; i < arity - 1; i++) {        
            args[i] = (IValue) stack[sp - arity + i]; 
        }
        
        sp = sp - arity;
        cf.sp = sp;
        
        returnValue = vf.constructor(constructor, args);
        return returnValue;
    }
    
    public int jvmOCALLDYN(final Object[] stack, int sp, final Frame cf, final int typesel, final int arity) {
        Object funcObject = stack[--sp];
        OverloadedFunctionInstanceCall ofunCall = null;
        cf.sp = sp;

        // Get function types to perform a type-based dynamic resolution
        Type types = cf.function.typeConstantStore[typesel];
        
        // Objects of two types may appear on the stack:
        // 1. FunctionInstance due to closures whom will have no overloading
        if (funcObject instanceof FunctionInstance) {
            FunctionInstance fun_instance = (FunctionInstance) funcObject;
            Frame frame = cf.getFrame(fun_instance.function, fun_instance.env, arity, sp);
            frame.previousCallFrame = cf;
            
            try {
                frame.function.handle.invoke(this, frame);
            } catch (Throwable e) {
                if(e instanceof Thrown){
                    throw (Thrown) e;
                } else {
                    throw new RuntimeException(e);
                }
            }
            return cf.sp;
        }
        // 2. OverloadedFunctionInstance due to named Rascal
        // functions
        OverloadedFunctionInstance of_instance = (OverloadedFunctionInstance) funcObject;
        ofunCall = new OverloadedFunctionInstanceCall(cf, of_instance.getFunctions(), of_instance.getConstructors(), of_instance.env, types, arity, rex);

        boolean stackPointerAdjusted = false;
        Frame frame = ofunCall.nextFrame();
        while (frame != null) {
            stackPointerAdjusted = true; // See text at OCALL
            Object result;
            
            try {
                result = frame.function.handle.invoke(this, frame);
            } catch (Throwable e) {
                if(e instanceof Thrown){
                    throw (Thrown) e;
                } else {
                    throw new RuntimeException(e);
                }
            }
            if (result == NONE) {
                return cf.sp; // Alternative matched.
            }
            frame = ofunCall.nextFrame();
        }
        Type constructor = ofunCall.nextConstructor();
        if (stackPointerAdjusted == false) {
            sp = sp - arity;
        }
        returnValue = vf.constructor(constructor, ofunCall.getConstructorArguments(constructor.getArity()));
        cf.sp = sp;
        return sp;
    }

    public Object calldynHelper(final Object[] stack, int sp, final Frame cf, int arity, final int ep) {
        Frame tmp;
        Object result;

        if (cf.hotEntryPoint != ep) {
            if (stack[sp - 1] instanceof Type) {
                Type constr = (Type) stack[--sp];
                arity = constr.getArity();
                IValue[] args = new IValue[arity];
                for (int i = arity - 1; i >= 0; i--) {
                    args[i] = (IValue) stack[sp - arity + i];
                }
                sp = sp - arity;
                returnValue = vf.constructor(constr, args);
                cf.sp = sp;
                return NONE; // DO not return continue execution
            }

            if (stack[sp - 1] instanceof FunctionInstance) {
                FunctionInstance fun_instance = (FunctionInstance) stack[--sp];
                // In case of partial parameter binding
                if (fun_instance.next + arity < fun_instance.function.nformals) {
                    fun_instance = fun_instance.applyPartial(arity, stack, sp);
                    sp = sp - arity;
                    returnValue = fun_instance;
                    cf.sp = sp;
                    return NONE;
                }
                tmp = cf.getFrame(fun_instance.function, fun_instance.env, fun_instance.args, arity, sp);
                cf.nextFrame = tmp;
            } else {
                throw new RuntimeException("Unexpected argument type for CALLDYN: " + asString(stack[sp - 1]));
            }
        } else {
            tmp = cf.nextFrame;
        }

        tmp.previousCallFrame = cf;

        try {
            result = tmp.function.handle.invoke(this, tmp);
        } catch (Throwable e) {
            if(e instanceof Thrown){
                throw (Thrown) e;
            } else {
                throw new RuntimeException(e);
            }
        }
        if (result == YIELD) {
            // Save reentry point
            cf.hotEntryPoint = ep;
            return YIELD; // Will cause the inline call to return YIELD
        } else {
            cf.hotEntryPoint = 0;
            cf.nextFrame = null; // Allow GC to clean
            return NONE; // Inline call will continue execution
        }
    }

    public Thrown thrownHelper(final Frame cf, final Object[] stack, final int sp) {
        Object obj = stack[sp];
        Thrown thrown = null;
        if (obj instanceof IValue) {
            thrown = Thrown.getInstance((IValue) obj, null, cf);
        } else {
            thrown = (Thrown) obj;
        }
        return thrown;
    }

}
