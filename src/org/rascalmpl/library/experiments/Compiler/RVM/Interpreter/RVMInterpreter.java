package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.control_exceptions.Throw;	// TODO: remove import: NOT YET: JavaCalls generate a Throw
import org.rascalmpl.interpreter.types.RascalType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;
import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class RVMInterpreter extends RVMCore {
	
	public RVMInterpreter(RVMExecutable rvmExec, RascalExecutionContext rex) {
		super(rvmExec, rex);
	}
	
	Configuration getConfiguration() { return rex.getConfiguration(); }
	
	/************************************************************************************/
	/*		Implementation of abstract methods in RVMCore for RVMInterpreter			*/
	/************************************************************************************/
	
	/* (non-Javadoc)
	 * Implements abstract function for RVM interpreter
	 * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore#executeRVMFunction(org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Function, io.usethesource.vallang.IValue[], java.util.Map)
	 */
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
		//cf.stack[func.nformals] = kwArgs == null ? new HashMap<String, IValue>() : kwArgs;
		Object o = interpretRVMProgram(root, cf);
		if(o instanceof Thrown){
			throw (Thrown) o;
		}
		return o;
	}
	
	/* (non-Javadoc)
	 * Implements abstract function for RVM interpreter
	 * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore#executeRVMFunction(org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.FunctionInstance, io.usethesource.vallang.IValue[])
	 */
	public IValue executeRVMFunction(FunctionInstance func, IValue[] posArgs, Map<String, IValue> kwArgs){
		Frame root = new Frame(func.function.scopeId, null, func.env, func.function.maxstack, func.function);
		Frame cf = root;

		// Pass the program arguments to main
		for(int i = 0; i < posArgs.length; i++) {
			cf.stack[i] = posArgs[i]; 
		}
		cf.stack[func.args.length-1] =  kwArgs;       // CHECK
		Object o = interpretRVMProgram(root, cf);
		if(o instanceof Thrown){
			throw (Thrown) o;
		}
		return narrow(o);
	}
	
	/* (non-Javadoc)
	 * Implements abstract function for RVM interpreter
	 * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore#executeRVMFunction(org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.OverloadedFunctionInstance, io.usethesource.vallang.IValue[])
	 */
	public IValue executeRVMFunction(OverloadedFunctionInstance func, IValue[] posArgs, Map<String, IValue> kwArgs){
		Function firstFunc = func.getFunctions()[0]; // TODO: null?
		int arity = posArgs.length + 1;
		int scopeId = func.env == null ? 0 : func.env.scopeId;
		Frame root = new Frame(scopeId, null, func.env, arity+2, firstFunc);
		root.sp = arity;
		
		OverloadedFunctionInstanceCall c_ofun_call_next = 
				scopeId == -1 ? new OverloadedFunctionInstanceCall(root, func.getFunctions(), func.getConstructors(), root, null, arity, rex)  // changed root to cf
        					  : OverloadedFunctionInstanceCall.computeOverloadedFunctionInstanceCall(root, func.getFunctions(), func.getConstructors(), scopeId, null, arity, rex);
				
		Frame cf = c_ofun_call_next.nextFrame();
		// Pass the program arguments to func
		for(int i = 0; i < posArgs.length; i++) {
			cf.stack[i] = posArgs[i]; 
		}
		cf.stack[arity - 1] = kwArgs;
		cf.sp = arity;
		cf.previousCallFrame = null;		// ensure that func will return here
		Object o = interpretRVMProgram(root, cf, c_ofun_call_next);
		if(o instanceof Thrown){
			throw (Thrown) o;
		}
		return narrow(o); 
	}
	
	/* (non-Javadoc)
	 * Implements abstract function for RVM interpreter
	 * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore#executeRVMFunctionInVisit(org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Frame)
	 */
	public IValue executeRVMFunctionInVisit(Frame root){
		Frame cf = root;
		// Pass the subject argument

		Object o = interpretRVMProgram(root, cf);
		if(o instanceof Thrown){
			throw (Thrown) o;
		}
		return (IValue)o;
	}
	
	/* (non-Javadoc)
	 *  Implements abstract function for RVM interpreter
	 * @see org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.RVMCore#executeRVMProgram(java.lang.String, java.lang.String, io.usethesource.vallang.IValue[], java.util.HashMap)
	 */
	public IValue executeRVMProgram(String moduleName, String uid_main, IValue[] args, Map<String,IValue> kwArgs) {
		
		String oldModuleName = rex.getFullModuleName();
		rex.setFullModuleName(moduleName);
		
		Function main_function = functionStore[functionMap.get(uid_main)];

		if (main_function == null) {
			throw RascalRuntimeException.noMainFunction(null);
		  //throw new RuntimeException("No main function found");
		}
		
		Frame root = new Frame(main_function.scopeId, null, main_function.maxstack, main_function);
		Frame cf = root;
		//cf.stack[0] = vf.list(args); // pass the program argument to main_function as a IList object
		cf.stack[0] = kwArgs == null ? new HashMap<String, IValue>() : kwArgs;
		cf.src = main_function.src;
		
		Object o = interpretRVMProgram(root, cf);
		if(o != null && o instanceof Thrown){
			throw (Thrown) o;
		}
		IValue res = narrow(o);

		rex.setFullModuleName(oldModuleName);
		return res;
	}
	
	/********************************************************************************/
	/*			Auxiliary functions that implement specific instructions			*/
	/*			that are only used by RVM ineterpreter								*/
	/********************************************************************************/
	
	private Object VALUESUBTYPE(Type reqType, Object accu){
		return vf.bool(((IValue) accu).getType().isSubtypeOf(reqType));
		//return vf.bool(rex.isSubtypeOf(((IValue) accu).getType(), reqType));
	}
	
	private int APPLY(Object[] stack, int sp, Frame cf, Function fun, int arity, Frame root){
		assert arity <= fun.nformals : "APPLY, too many arguments at " + cf.src;
		assert fun.scopeIn == -1 : "APPLY, illegal scope at " + cf.src;
		FunctionInstance fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, stack, sp);
		sp = sp - arity;
		stack[sp++] = fun_instance;
		return sp;
	}
	
	private int APPLYDYN(int arity, Frame cf, Object[] stack, int sp){
		FunctionInstance fun_instance;
		Object src = stack[--sp];
		if(src instanceof FunctionInstance) {
			fun_instance = (FunctionInstance) src;
			assert arity + fun_instance.next <= fun_instance.function.nformals : "APPLYDYN, too many arguments at " + cf.src;
			fun_instance = fun_instance.applyPartial(arity, stack, sp);
		} else {
			throw new InternalCompilerError("Unexpected argument type for APPLYDYN: " + asString(src), cf);
		}
		sp = sp - arity;
		stack[sp++] = fun_instance;
		return sp;
	}
	
	private Object interpretRVMProgram(Frame root, Frame cf) {
		return interpretRVMProgram(root, cf, null);
	}
	
	/********************************************************************************/
	/*		The actual RVM interpreter												*/
	/********************************************************************************/
	
	@SuppressWarnings("unchecked")
	private Object interpretRVMProgram(final Frame root, Frame cf, OverloadedFunctionInstanceCall c_ofun_call) {
		Object[] stack = cf.stack;		                              		// current stack
		int sp = cf.function.getNlocals();				                  	// current stack pointer
		long [] instructions = cf.function.codeblock.getInstructions(); 	// current instruction sequence
		int pc = 0;				                                      		// current program counter
		int postOp = 0;														// postprocessing operator (following main switch)
		int pos = 0;
		ArrayList<Frame> stacktrace = new ArrayList<Frame>();
		Thrown thrown = null;
		int arity;
		long instruction;
		int op;
		Object rval;
		
		Object accu = null;
		
		if(rex.getJVM()){
			throw new InternalCompilerError("*** SHOULD NOT BE CALLED IN JVM MODE: interpretRVMProgram: " + cf.toString());
		}
		// Overloading specific
		Stack<OverloadedFunctionInstanceCall> ocalls = new Stack<OverloadedFunctionInstanceCall>();
		if(c_ofun_call != null){
			ocalls.push(c_ofun_call);
		}
		
		frameObserver.enter(cf);
		 
		try {
			NEXT_INSTRUCTION: while (true) {
				
				frameObserver.observeRVM(this, cf, pc, stack, sp, accu);
				
				instruction = instructions[pc++];
				op = CodeBlock.fetchOp(instruction);
				
				INSTRUCTION: switch (op) {
				
				case Opcode.OP_PUSHACCU:
					stack[sp++] = accu;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_POPACCU:
					accu = stack[--sp];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_POP:
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC0:
//					assert 0 < cf.function.nlocals : "LOADLOC0: pos larger that nlocals at " + cf.src;
//					assert stack[0] != null: "Local variable 0 is null";
					accu = stack[0]; //if(a == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC1:
//					assert 1 < cf.function.nlocals : "LOADLOC1: pos larger that nlocals at " + cf.src;
//					assert stack[1] != null: "Local variable 1 is null";
					accu = stack[1]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION; 
					
				case Opcode.OP_LOADLOC2:
//					assert 2 < cf.function.nlocals : "LOADLOC2: pos larger that nlocals at " + cf.src;
//					assert stack[2] != null: "Local variable 2 is null";
					accu = stack[2]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION; 
					
				case Opcode.OP_LOADLOC3:
//					assert 3 < cf.function.nlocals : "LOADLOC3: pos larger that nlocals at " + cf.src;
//					assert stack[3] != null: "Local variable 3 is null";
					accu = stack[3]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC4:
//					assert 4 < cf.function.nlocals : "LOADLOC4: pos larger that nlocals at " + cf.src;
//					assert stack[4] != null: "Local variable 4 is null";
					accu = stack[4]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC5:
//					assert 5 < cf.function.nlocals : "LOADLOC5: pos larger that nlocals at " + cf.src;
//					assert stack[5] != null: "Local variable 5 is null";
					accu = stack[5]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC6:
//					assert 6 < cf.function.nlocals : "LOADLOC6: pos larger that nlocals at " + cf.src;
//					assert stack[6] != null: "Local variable 6 is null";
					accu = stack[6]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC7:
//					assert 7 < cf.function.nlocals : "LOADLOC7: pos larger that nlocals at " + cf.src;
//					assert stack[7] != null: "Local variable 7 is null";
					accu = stack[7]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC8:
//					assert 8 < cf.function.nlocals : "LOADLOC8: pos larger that nlocals at " + cf.src;
//					assert stack[8] != null: "Local variable 8 is null";
					accu = stack[8]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOC9:
//					assert 9 < cf.function.nlocals : "LOADLOC9: pos larger that nlocals at " + cf.src;
//					assert stack[9] != null: "Local variable 9 is null";
					accu = stack[9]; // if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADLOC:
					pos = CodeBlock.fetchArg1(instruction);
//					assert pos < cf.function.nlocals : "LOADLOC: pos larger that nlocals at " + cf.src;
//					assert stack[pos] != null: "Local variable " + pos + " is null";
					accu = stack[pos]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_PUSHLOC:
					pos = CodeBlock.fetchArg1(instruction);
//					assert pos < cf.function.nlocals : "LOADLOC: pos larger that nlocals at " + cf.src;
//					assert stack[pos] != null: "Local variable " + pos + " is null";
					accu = stack[pos]; //if(accu == null){ postOp = Opcode.POSTOP_CHECKUNDEF; break; }
					stack[sp++] = accu;
					continue NEXT_INSTRUCTION;	
					
				case Opcode.OP_RESETLOCS:
					IList positions = (IList) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					for(IValue v : positions){
						stack[((IInteger) v).intValue()] = null;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_RESETLOC:	
					stack[CodeBlock.fetchArg1(instruction)] = null;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADBOOL:
					accu = CodeBlock.fetchArg1(instruction) == 1 ? Rascal_TRUE : Rascal_FALSE;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADINT:
					accu = CodeBlock.fetchArg1(instruction);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADCON:
					accu = cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCON:
					accu = stack[sp++] = cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADLOCREF:
					accu = new Reference(stack, CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHLOCREF:
					stack[sp++] = new Reference(stack, CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHEMPTYKWMAP:
					// TODO: use unique copy of emptyKeywordMap and delay creation of new copy to assignment
					// to keyword parameter
					//stack[sp++] = emptyKeywordMap;
					stack[sp++] = new HashMap<String,IValue>();
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_CALLMUPRIM0:	
					accu = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute0();
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLMUPRIM0:	
					stack[sp++] = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute0();
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLMUPRIM1:	
					accu = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute1(accu);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLMUPRIM1:	
					stack[sp++] = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute1(accu);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLMUPRIM2:	
					accu = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute2(stack[sp - 1], accu);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLMUPRIM2:	
					stack[sp - 1] = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].execute2(stack[sp - 1], accu);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLMUPRIMN:
					sp = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].executeN(stack, sp, CodeBlock.fetchArg2(instruction));
					accu = stack[--sp];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLMUPRIMN:
					sp = MuPrimitive.values[CodeBlock.fetchArg1(instruction)].executeN(stack, sp, CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;	
				
				case Opcode.OP_JMP:
					pc = CodeBlock.fetchArg1(instruction);
					continue NEXT_INSTRUCTION;

				case Opcode.OP_JMPTRUE:
					if (((IBool) accu).getValue()) {
						pc = CodeBlock.fetchArg1(instruction);
					}
					continue NEXT_INSTRUCTION;

				case Opcode.OP_JMPFALSE:
					if (!((IBool) accu).getValue()) {
						pc = CodeBlock.fetchArg1(instruction);
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_TYPESWITCH:
					Type t = null;
					if(accu instanceof IConstructor) {
						t = ((IConstructor) accu).getConstructorType();
					} else {
						t = ((IValue)accu).getType();
					}
					int labelIndex = ToplevelType.getToplevelTypeAsInt(t);
					IList labels = (IList) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					pc = ((IInteger) labels.get(labelIndex)).intValue();
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_SWITCH:
					IMap caseLabels = (IMap) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					int caseDefault = CodeBlock.fetchArg2(instruction);
					boolean useConcreteFingerprint = instructions[pc++] == 1;
					IInteger fp = vf.integer(ToplevelType.getFingerprint((IValue)accu, useConcreteFingerprint));
					
					IInteger x = (IInteger) caseLabels.get(fp);
					//stdout.println("SWITCH: fp = " + fp  + ", val = " + val + ", x = " + x + ", useConcreteFingerprint = " + useConcreteFingerprint);
					if(x == null){
							pc = caseDefault;
					} else {
						pc = x.intValue();
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADTYPE:
					accu = cf.function.typeConstantStore[CodeBlock.fetchArg1(instruction)];
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHTYPE:
					stack[sp++] = cf.function.typeConstantStore[CodeBlock.fetchArg1(instruction)];
					continue NEXT_INSTRUCTION;	 
					
				case Opcode.OP_LOADLOCDEREF: {
					Reference ref = (Reference) stack[CodeBlock.fetchArg1(instruction)];
					accu = ref.getValue();
					continue NEXT_INSTRUCTION;
				}
				case Opcode.OP_PUSHLOCDEREF: {
					Reference ref = (Reference) stack[CodeBlock.fetchArg1(instruction)];
					stack[sp++] = ref.getValue();
					continue NEXT_INSTRUCTION;
				}
				
				case Opcode.OP_STORELOC:
					pos = CodeBlock.fetchArg1(instruction);
					assert pos < cf.function.getNlocals() : "STORELOC: pos larger that nlocals at " + cf.src;
					stack[pos] = accu;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_STORELOCDEREF:
					Reference ref = (Reference) stack[CodeBlock.fetchArg1(instruction)];
					ref.setValue(accu); // TODO: We need to re-consider how to guarantee safe use of both Java objects and IValues    
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_PUSH_ROOT_FUN:
					// Loads functions that are defined at the root
					stack[sp++] = new FunctionInstance(functionStore[CodeBlock.fetchArg1(instruction)], root, this);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSH_NESTED_FUN: { 
					// Loads nested functions and closures (anonymous nested functions)
					stack[sp++] = FunctionInstance.computeFunctionInstance(functionStore[CodeBlock.fetchArg1(instruction)], cf, CodeBlock.fetchArg2(instruction), this);
					continue NEXT_INSTRUCTION;
				}
				
				case Opcode.OP_PUSHOFUN:
					OverloadedFunction of = overloadedStore[CodeBlock.fetchArg1(instruction)];
					stack[sp++] = of.getScopeIn() == -1 ? new OverloadedFunctionInstance(of.functionsAsFunction, of.constructorsAsType, root, this)
					                               : OverloadedFunctionInstance.computeOverloadedFunctionInstance(of.functionsAsFunction, of.constructorsAsType, cf, of.getScopeIn(), this);
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_PUSHCONSTR:
					Type constructor = constructorStore[CodeBlock.fetchArg1(instruction)];  
					stack[sp++] = constructor;
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADVAR:
					 accu = LOADVAR(cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction));
//					 if(accu == null){ 
//						 postOp = Opcode.POSTOP_CHECKUNDEF; break; 
//					 }
					 continue NEXT_INSTRUCTION;
					 
				case Opcode.OP_PUSHVAR:
					 sp = PUSHVAR(stack, sp, cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction));
//					 if(accu == null){ 
//						 postOp = Opcode.POSTOP_CHECKUNDEF; break; 
//					 }
//					 stack[sp++] = accu;
					 continue NEXT_INSTRUCTION;
					 
				case Opcode.OP_RESETVAR:
					RESETVAR(cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADVARREF: 
					accu = LOADVARREF(cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHVARREF: 
					stack[sp++] = LOADVARREF(cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_LOADVARDEREF: 
					accu = LOADVARDEREF(cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction));
//					if(accu == null){ 
//						postOp = Opcode.POSTOP_CHECKUNDEF; break;
//					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHVARDEREF: 
					accu = LOADVARDEREF(cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction));
//					if(accu == null){ 
//						postOp = Opcode.POSTOP_CHECKUNDEF; break;
//					}
					stack[sp++] = accu;
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_STOREVAR:
					STOREVAR(cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), accu);
					continue NEXT_INSTRUCTION;
						
				case Opcode.OP_STOREVARDEREF:
					STOREVARDEREF(cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), accu);
					continue NEXT_INSTRUCTION;
									
				case Opcode.OP_CALLCONSTR:
					sp = CALLCONSTR(stack, sp, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction));
					accu = stack[--sp];
					continue NEXT_INSTRUCTION;
										
				case Opcode.OP_CALLDYN:				
				case Opcode.OP_CALL:
					if(!frameObserver.observe(cf)){
						return Rascal_FALSE;
					}
					// In case of CALLDYN, the stack top value of type 'Type' leads to a constructor call
					if(op == Opcode.OP_CALLDYN && stack[sp - 1] instanceof Type) {
						Type constr = (Type) stack[--sp];
						arity = constr.getArity();
						IValue[] args = new IValue[arity]; 
						for(int i = arity - 1; i >= 0; i--) {
							args[i] = (IValue) stack[sp - arity + i];
						}
						sp = sp - arity;
						accu = vf.constructor(constr, args);
						continue NEXT_INSTRUCTION;
					}
					
					cf.pc = pc;
					if(op == Opcode.OP_CALLDYN && stack[sp - 1] instanceof FunctionInstance){
						FunctionInstance fun_instance = (FunctionInstance) stack[--sp];
						arity = CodeBlock.fetchArg1(instruction);
						// In case of partial parameter binding
						if(fun_instance.next + arity < fun_instance.function.nformals) {
							fun_instance = fun_instance.applyPartial(arity, stack, sp);
							sp = sp - arity;
						    accu = fun_instance;
						    continue NEXT_INSTRUCTION;
						}
						cf = cf.getFrame(fun_instance.function, fun_instance.env, fun_instance.args, arity, sp);
					} else if(op == Opcode.OP_CALL) {
						Function fun = functionStore[CodeBlock.fetchArg1(instruction)];
						arity = CodeBlock.fetchArg2(instruction);
						// In case of partial parameter binding
						if(arity < fun.nformals) {
							FunctionInstance fun_instance = FunctionInstance.applyPartial(fun, root, this, arity, stack, sp);
							sp = sp - arity;
						    accu = fun_instance;
						    continue NEXT_INSTRUCTION;
						}
						cf = cf.getFrame(fun, root, arity, sp);
						
					} else {
						throw new InternalCompilerError("Unexpected argument type for CALLDYN: " + asString(stack[sp - 1]), cf);
					}
					
					frameObserver.enter(cf);
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					//accu = stack[--sp];	// TODO
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_OCALLDYN:
				case Opcode.OP_OCALL:					
					Object funcObject = (op == Opcode.OP_OCALLDYN) ? stack[--sp] : null;
					// Get function arguments from the stack
					arity = CodeBlock.fetchArg2(instruction);
					
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					if(!frameObserver.observe(cf)){
						return Rascal_FALSE;
					}
					cf.sp = sp;
					cf.pc = pc;
					
					OverloadedFunctionInstanceCall c_ofun_call_next = null;
					
					if(op == Opcode.OP_OCALLDYN) {
						// Get function types to perform a type-based dynamic resolution
						Type types = cf.function.codeblock.getConstantType(CodeBlock.fetchArg1(instruction));
						// Objects of three types may appear on the stack:
						// 	1. FunctionInstance due to closures
						if(funcObject instanceof FunctionInstance) {
							FunctionInstance fun_instance = (FunctionInstance) funcObject;
							cf = cf.getFrame(fun_instance.function, fun_instance.env, arity, sp);
							instructions = cf.function.codeblock.getInstructions();
							stack = cf.stack;
							sp = cf.sp;
							pc = cf.pc;
							frameObserver.enter(cf);;
							continue NEXT_INSTRUCTION;
						}
					 	// 2. OverloadedFunctionInstance due to named Rascal functions
						OverloadedFunctionInstance of_instance = (OverloadedFunctionInstance) funcObject;
						c_ofun_call_next = new OverloadedFunctionInstanceCall(cf, of_instance.getFunctions(), of_instance.getConstructors(), of_instance.env, types, arity, rex);
					} else {
						of = overloadedStore[CodeBlock.fetchArg1(instruction)];
						Object arg0 = stack[sp - arity];
						c_ofun_call_next = of.getScopeIn() == -1 ? new OverloadedFunctionInstanceCall(cf, of.getFunctions(arg0), of.getConstructors(arg0), cf, null, arity, rex)  // changed root to cf
								                            : OverloadedFunctionInstanceCall.computeOverloadedFunctionInstanceCall(cf, of.getFunctions(arg0), of.getConstructors(arg0), of.getScopeIn(), null, arity, rex);
					}
					
					
					Frame frame = c_ofun_call_next.nextFrame();
					
					if(frame != null) {
						c_ofun_call = c_ofun_call_next;
						ocalls.push(c_ofun_call);
					
						cf = frame;
						frameObserver.enter(cf);
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
					} else {
						constructor = c_ofun_call_next.nextConstructor();
						sp = sp - arity;
						accu = vf.constructor(constructor, c_ofun_call_next.getConstructorArguments(constructor.getArity()));
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CHECKARGTYPEANDCOPY:
					pos = CodeBlock.fetchArg1(instruction);
					Type argType = ((IValue) stack[pos]).getType();
					Type paramType = cf.function.typeConstantStore[CodeBlock.fetchArg2(instruction)];
					
					int pos2 = (int) instructions[pc++];
					
					if(argType.isSubtypeOf(paramType)){
						stack[pos2] = stack[pos];
						accu = vf.bool(true);
						continue NEXT_INSTRUCTION;
					}
					if(argType instanceof RascalType){
						RascalType atype = (RascalType) argType;
						RascalType ptype = (RascalType) paramType;
						if(ptype.isNonterminal() &&  atype.isSubtypeOfNonTerminal(ptype)){
							stack[pos2] = stack[pos];
							accu = vf.bool(true);
							continue NEXT_INSTRUCTION;
						}
					}
						
					accu = vf.bool(false);
					//System.out.println("OP_CHECKARGTYPEANDCOPY: " + argType + ", " + paramType + " => false");
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_FAILRETURN:
					assert cf.previousCallFrame == c_ofun_call.cf : "FAILRETURN, incorrect frame at" + cf.src;
					
					frame = c_ofun_call.nextFrame();				
					if(frame != null) {
												
						cf = frame;
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
					} else {
						cf = c_ofun_call.cf;
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
						constructor = c_ofun_call.nextConstructor();
						accu = vf.constructor(constructor, c_ofun_call.getConstructorArguments(constructor.getArity()));
						ocalls.pop();
						c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_VISIT:
					boolean direction = ((IBool) cf.function.constantStore[CodeBlock.fetchArg1(instruction)]).getValue();
					boolean progress = ((IBool) cf.function.constantStore[CodeBlock.fetchArg2(instruction)]).getValue();
					boolean fixedpoint = ((IBool) cf.function.constantStore[(int)instructions[pc++]]).getValue();
					boolean rebuild = ((IBool) cf.function.constantStore[(int)instructions[pc++]]).getValue();
					sp = VISIT(stack, sp, direction, progress, fixedpoint, rebuild);
					if(sp > 0){
						continue NEXT_INSTRUCTION;
					}
					// Fall through to force a function return;
					sp = -sp;
					accu = stack[--sp];
					op = Opcode.OP_RETURN1;

					// fall through
				case Opcode.OP_RETURN1:
					// Overloading specific
					if(c_ofun_call != null && cf.previousCallFrame == c_ofun_call.cf) {
						ocalls.pop();
						c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
					}
					
					frameObserver.leave(cf, accu);
					cf = cf.previousCallFrame;
					
					if(cf == null) {
						return accu;
					}
					
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					//stack[sp++] = accu;
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_FILTERRETURN:
				case Opcode.OP_RETURN0:
					
					// Overloading specific
					if(c_ofun_call != null && cf.previousCallFrame == c_ofun_call.cf) {
						ocalls.pop();
						c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
					}
				
					rval = null;
					boolean returns = op != Opcode.OP_RETURN0;
					
					frameObserver.leave(cf, rval);
					cf = cf.previousCallFrame;
					
					if(cf == null) {
						return returns ? rval : NOVALUE;
					}
					
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					if(returns) {
						accu = rval;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CORETURN0:
				case Opcode.OP_CORETURN1:
					
					rval = Rascal_TRUE;
					if(op == Opcode.OP_CORETURN1) {
						arity = CodeBlock.fetchArg1(instruction);
						int[] refs = cf.function.refs;
						if(arity != refs.length) {
							throw new InternalCompilerError("Coroutine " + cf.function.name + ": arity of return (" + arity  + ") unequal to number of reference parameters (" +  refs.length + ")", cf);
						}
						for(int i = 0; i < arity; i++) {
							ref = (Reference) stack[refs[arity - 1 - i]];
							//ref.stack[ref.pos] = stack[--sp];
							ref.setValue(stack[--sp]);
						}
					}
					
					// if the current frame is the frame of a top active coroutine, 
					// then pop this coroutine from the stack of active coroutines
					
					activeCoroutines.pop();
					ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					
					frameObserver.leave(cf, rval);
					cf = cf.previousCallFrame;
					
					if(cf == null) {
						return rval; 
					}
					
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					
					accu = rval;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLJAVA:
					String methodName =  ((IString) cf.function.constantStore[(int) instructions[pc++]]).getValue();
					String className =  ((IString) cf.function.constantStore[(int) instructions[pc++]]).getValue();
					Type parameterTypes = cf.function.typeConstantStore[(int) instructions[pc++]];
					Type keywordTypes = cf.function.typeConstantStore[(int) instructions[pc++]];
					int reflect = (int) instructions[pc++];
					arity = parameterTypes.getArity();
					try {
						//int sp1 = sp;
					    sp = callJavaMethod(methodName, className, parameterTypes, keywordTypes, reflect, stack, sp);
					    //assert sp == sp1 - arity + 1;
					} catch (Throw e) {
						stacktrace.add(cf);
						thrown = Thrown.getInstance(e.getException(), e.getLocation(), cf);
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
					} catch (Thrown e){
						stacktrace.add(cf);
						thrown = e;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
					} catch (Throwable e){
						thrown = Thrown.getInstance(e, cf.src, cf);
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; break INSTRUCTION;
					} 
					
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_CREATE:
				case Opcode.OP_CREATEDYN:
					if(op == Opcode.OP_CREATE) {
						cccf = cf.getCoroutineFrame(functionStore[CodeBlock.fetchArg1(instruction)], root, CodeBlock.fetchArg2(instruction), sp);
					} else {
						arity = CodeBlock.fetchArg1(instruction);
						Object src = stack[--sp];
						if(src instanceof FunctionInstance) {
							// In case of partial parameter binding
							FunctionInstance fun_instance = (FunctionInstance) src;
							cccf = cf.getCoroutineFrame(fun_instance, arity, sp);
						} else {
							throw new InternalCompilerError("Unexpected argument type for INIT: " + src.getClass() + ", " + src, cf);
						}
					}
					sp = cf.sp;
					// Instead of suspending a coroutine instance during CREATE, execute it until GUARD;
					// Let CREATE postpone creation of an actual coroutine instance (delegated to GUARD), 
					// which also implies no stack management of active coroutines until GUARD;
					cccf.previousCallFrame = cf;
					
					cf.sp = sp;
					cf.pc = pc;
					instructions = cccf.function.codeblock.getInstructions();
					cf = cccf;
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_GUARD:
					//rval = stack[sp - 1];
					boolean precondition;
					if(accu instanceof IBool) {
						precondition = ((IBool) accu).getValue();
//					} else if(rval instanceof Boolean) {
//						precondition = (Boolean) rval;
					} else {
						throw new InternalCompilerError("Guard's expression has to be boolean!", cf);
					}
					
					if(cf == cccf) {
						Coroutine coroutine = null;
						Frame prev = cf.previousCallFrame;
						if(precondition) {
							coroutine = new Coroutine(cccf);
							coroutine.isInitialized = true;
							coroutine.suspend(cf);
						}
						cccf = null;
						//--sp;
						cf.pc = pc;
						cf.sp = sp;
						cf = prev;
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
						accu = precondition ? coroutine : exhausted;
						continue NEXT_INSTRUCTION;
					}
					
					if(!precondition) {
						cf.pc = pc;
						cf.sp = sp;
						cf = cf.previousCallFrame;
						instructions = cf.function.codeblock.getInstructions();
						stack = cf.stack;
						sp = cf.sp;
						pc = cf.pc;
						accu = Rascal_FALSE;
						continue NEXT_INSTRUCTION;
					}
					//--sp;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_APPLY:
					sp =  APPLY(stack, sp, cf, functionStore[CodeBlock.fetchArg1(instruction)], CodeBlock.fetchArg2(instruction), root);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_APPLYDYN:
					sp = APPLYDYN(CodeBlock.fetchArg1(instruction), cf, stack, sp);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_NEXT0:
				case Opcode.OP_NEXT1:
					Coroutine coroutine = (Coroutine) accu;
					
					if(!coroutine.hasNext()) {
						if(op == Opcode.OP_NEXT1) {
							--sp;
						}
						accu = Rascal_FALSE;
						continue NEXT_INSTRUCTION;
					}
					// put the coroutine onto the stack of active coroutines
					activeCoroutines.push(coroutine);
					ccf = coroutine.start;
					coroutine.next(cf);
					
					instructions = coroutine.frame.function.codeblock.getInstructions();
				
					// Transmit NEXT's arg (if present) to the corresponding YIELD in the coroutine; 
					// but always leave an entry on the stack
					coroutine.frame.stack[coroutine.frame.sp++] = (op == Opcode.OP_NEXT1) ? stack[--sp] : null;
					
					cf.pc = pc;
					cf.sp = sp;
					
					cf = coroutine.frame;
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_YIELD0:	
				case Opcode.OP_YIELD1:
					coroutine = activeCoroutines.pop();
					ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					Frame prev = coroutine.start.previousCallFrame;
					
					if(op == Opcode.OP_YIELD1) {
						arity = CodeBlock.fetchArg1(instruction);
						int[] refs = cf.function.refs; 
						
						if(arity != refs.length) {
							throw new InternalCompilerError("YIELD requires same number of arguments as the number of coroutine's reference parameters; arity: " + arity + "; reference parameter number: " + refs.length, cf);
						}
						
						// Assign the reference parameters of the currently active coroutine instance
						for(int i = 0; i < arity; i++) {
							ref = (Reference) stack[refs[arity - 1 - i]]; 
							//ref.stack[ref.pos] = stack[--sp];
							ref.setValue(stack[--sp]);;
						}
					}
					cf.pc = pc;
					cf.sp = sp;
					coroutine.suspend(cf);
					cf = prev;
					if(op == Opcode.OP_YIELD1 && cf == null) {
						return Rascal_TRUE;
					}
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					accu = Rascal_TRUE;	 		// YIELD always returns TRUE to the corresponding NEXT
					
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_EXHAUST:
					if(cf == ccf) {
						activeCoroutines.pop();
						ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					}
					
					cf = cf.previousCallFrame;
					if(cf == null) {
						return Rascal_FALSE;    // EXHAUST always returns FALSE, to the corresponding NEXT
					}
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					accu = Rascal_FALSE;  		// EXHAUST always returns FALSE, to the corresponding NEXT
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLPRIMN:
					arity = CodeBlock.fetchArg2(instruction);
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						sp = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].executeN(stack, sp, CodeBlock.fetchArg2(instruction), cf, rex);
						accu = stack[--sp];
					} catch (Thrown exception) {
						thrown = exception;
						sp = sp - arity;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLPRIMN:
					arity = CodeBlock.fetchArg2(instruction);
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						sp = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].executeN(stack, sp, CodeBlock.fetchArg2(instruction), cf, rex);
					} catch (Thrown exception) {
						thrown = exception;
						sp = sp - arity;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLPRIM0:
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						accu = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute0(cf, rex);
					} catch (Thrown exception) {
						thrown = exception;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLPRIM0:
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						stack[sp++] = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute0(cf, rex);
					} catch (Thrown exception) {
						thrown = exception;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLPRIM1:
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						accu = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute1(accu, cf, rex);
					} catch (Thrown exception) {
						thrown = exception;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLPRIM1:
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						stack[sp++] = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute1(accu, cf, rex);
					} catch (Thrown exception) {
						thrown = exception;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CALLPRIM2:
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						accu = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute2(stack[sp - 1], accu, cf, rex);
						sp--;
					} catch (Thrown exception) {
						thrown = exception;
						sp = sp - 1;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHCALLPRIM2:
					cf.src = (ISourceLocation) cf.function.constantStore[(int) instructions[pc++]];
					frameObserver.observe(cf);
					try {
						stack[sp - 1] = RascalPrimitive.values[CodeBlock.fetchArg1(instruction)].execute2(stack[sp - 1], accu, cf, rex);
					} catch (Thrown exception) {
						thrown = exception;
						sp = sp - 1;
						postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
						break INSTRUCTION;
					}
					continue NEXT_INSTRUCTION;
				
				case Opcode.OP_UNWRAPTHROWNLOC: {
					pos = CodeBlock.fetchArg1(instruction);
					assert pos < cf.function.getNlocals() : "UNWRAPTHROWNLOC: pos larger that nlocals at " + cf.src;
					stack[pos] = ((Thrown) stack[--sp]).getValue();
					continue NEXT_INSTRUCTION;
				}
				
				case Opcode.OP_UNWRAPTHROWNVAR:
					sp = UNWRAPTHROWNVAR(stack, sp, cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;
					
				// Some specialized MuPrimitives
					
				case Opcode.OP_SUBSCRIPTARRAY:
					accu = ((Object[]) stack[sp - 1])[((Integer) accu)];
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SUBSCRIPTLIST:
					accu = ((IList) stack[sp - 1]).get((Integer) accu);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LESSINT:
					accu = vf.bool(((Integer) stack[sp - 1]) < ((Integer) accu));
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_GREATEREQUALINT:
					accu = vf.bool(((Integer) stack[sp - 1]) >= ((Integer) accu));
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_ADDINT:
					accu = ((Integer) stack[sp - 1]) + ((Integer) accu);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SUBTRACTINT:
					accu = ((Integer) stack[sp - 1]) - ((Integer) accu);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_ANDBOOL:
					accu = ((IBool) stack[sp - 1]).and((IBool) accu);
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_TYPEOF:
					if(accu instanceof HashSet<?>){	// For the benefit of set matching
						HashSet<IValue> mset = (HashSet<IValue>) accu;
						if(mset.isEmpty()){
							accu = tf.setType(tf.voidType());
						} else {
							IValue v = mset.iterator().next();
							accu = tf.setType(v.getType());
						}
					} else {
						accu = ((IValue) accu).getType();
					}
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_SUBTYPE:
					accu = vf.bool(((Type) stack[sp - 1]).isSubtypeOf((Type) accu));
					sp--;
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_VALUESUBTYPE:
					accu = VALUESUBTYPE(cf.function.typeConstantStore[CodeBlock.fetchArg1(instruction)], accu);
					continue NEXT_INSTRUCTION;
								
				case Opcode.OP_LABEL:
					throw new InternalCompilerError("LABEL instruction at runtime", cf);
					
				case Opcode.OP_HALT:
					return stack[sp - 1];

				case Opcode.OP_PRINTLN:
					sp =  PRINTLN(stack, sp, CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;	
					
				case Opcode.OP_THROW:
					Object obj = stack[--sp];
					thrown = null;
					cf.src = (ISourceLocation) cf.function.constantStore[CodeBlock.fetchArg1(instruction)];
					frameObserver.observe(cf);
					
					if(obj instanceof IValue) {
						//stacktrace = new ArrayList<Frame>();
						//stacktrace.add(cf);
						thrown = Thrown.getInstance((IValue) obj, null, cf);
					} else {
						// Then, an object of type 'Thrown' is on top of the stack
						thrown = (Thrown) obj;
					}
					postOp = Opcode.POSTOP_HANDLEEXCEPTION; 
					break INSTRUCTION;
					
				case Opcode.OP_LOADLOCKWP:
					accu = LOADLOCKWP(stack, cf, CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHLOCKWP:
					stack[sp++] = LOADLOCKWP(stack, cf, CodeBlock.fetchArg1(instruction));
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_LOADVARKWP:
					accu = LOADVARKWP(cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_PUSHVARKWP:
					stack[sp++] = LOADVARKWP(cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction));
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_STORELOCKWP:
					STORELOCKWP(stack, cf, CodeBlock.fetchArg1(instruction), accu);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_STOREVARKWP:
					STOREVARKWP(cf, CodeBlock.fetchArg1(instruction), CodeBlock.fetchArg2(instruction), accu);
					continue NEXT_INSTRUCTION;
					
				case Opcode.OP_CHECKMEMO:
					sp = CHECKMEMO(stack, sp, cf);
					if(sp > 0){
						accu = stack[--sp];
						continue NEXT_INSTRUCTION;
					}
					sp = - sp;
					op = Opcode.OP_RETURN1;
					
					// Specialized copy of RETURN code
					
					// Overloading specific
					if(c_ofun_call != null && cf.previousCallFrame == c_ofun_call.cf) {
						ocalls.pop();
						c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
					}
					
					rval = stack[sp - 1];

					assert sp ==  cf.function.getNlocals() + 1
							: "On return from " + cf.function.name + ": " + (sp - cf.function.getNlocals()) + " spurious stack elements";
					
					// if the current frame is the frame of a top active coroutine, 
					// then pop this coroutine from the stack of active coroutines
					if(cf == ccf) {
						activeCoroutines.pop();
						ccf = activeCoroutines.isEmpty() ? null : activeCoroutines.peek().start;
					}
					
					if(!frameObserver.leave(cf,  rval)){
						return Rascal_FALSE;
					}
					cf = cf.previousCallFrame;
					
					if(cf == null) {
						return rval; 
					}
					
					instructions = cf.function.codeblock.getInstructions();
					stack = cf.stack;
					sp = cf.sp;
					pc = cf.pc;
					accu = rval;
					continue NEXT_INSTRUCTION;
								
				default:
					throw new InternalCompilerError("RVM main loop -- cannot decode instruction", cf);
				}
				
				switch(postOp){
				
				case Opcode.POSTOP_CHECKUNDEF:
				case Opcode.POSTOP_HANDLEEXCEPTION:
					// EXCEPTION HANDLING
					if(postOp == Opcode.POSTOP_CHECKUNDEF) {
						//stacktrace = new ArrayList<Frame>();
						//stacktrace.add(cf);
						thrown = RascalRuntimeException.uninitializedVariable("name to be provided", cf);
					}
					cf.pc = pc;
					// First, try to find a handler in the current frame function,
					// given the current instruction index and the value type,
					// then, if not found, look up the caller function(s)
					
					for(Frame f = cf; f != null; f = f.previousCallFrame) {
						int handler = f.function.getHandler(f.pc - 1, thrown.getValue().getType());
						if(handler != -1) {
							int fromSP = f.function.getFromSP();
							if(f != cf) {
								cf = f;
								instructions = cf.function.codeblock.getInstructions();
								stack = cf.stack;
								sp = cf.sp;
								pc = cf.pc;
							}
							pc = handler;
							sp = fromSP;
							stack[sp++] = thrown;
							thrown = null;
							continue NEXT_INSTRUCTION;
						}
						if(c_ofun_call != null && f.previousCallFrame == c_ofun_call.cf) {
							ocalls.pop();
							c_ofun_call = ocalls.isEmpty() ? null : ocalls.peek();
						}
					}
					// If a handler has not been found in the caller functions...
//					stdout.println("EXCEPTION " + thrown + " at: " + cf.src);
//					for(Frame f = cf; f != null; f = f.previousCallFrame) {
//						stdout.println("\t" + f.toString());
//					}
//					stdout.flush();
					if(frameObserver.exception(cf, thrown)){
						continue NEXT_INSTRUCTION;
					} else {
						return thrown;
					}
				}
				
			}
		}
		catch (Thrown e) {
			throw e; 
			// this is a normal Rascal exception, but we want to handle the next case here exceptionally, which 
			// should not happen normally and hints at a compiler or run-time bug:
		}
		catch (Exception e) {
			stdout.println("EXCEPTION " + e + " at: " + cf.src);
			for(Frame f = cf; f != null; f = f.previousCallFrame) {
				stdout.println("\t" + f.toString());
			}
			stdout.flush();
			e.printStackTrace(stderr);
			String e2s = (e instanceof InternalCompilerError) ? e.getMessage() : e.toString();
			throw new InternalCompilerError(e2s + "; function: " + cf + "; instruction: " + cf.function.codeblock.toString(pc - 1), cf, e);
		}
	}
	
}
