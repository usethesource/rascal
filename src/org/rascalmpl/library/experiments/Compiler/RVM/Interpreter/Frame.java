package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.types.FunctionType;

public class Frame {
	public int scopeId;
    public Frame previousCallFrame;
    public final Frame previousScope;
	public final Object[] stack;
	public int sp;
	int pc;
	public final Function function;
	
	public final boolean isCoroutine;
		
	public Frame(int scopeId, Frame previousCallFrame, int stackSize, Function function){
		this(scopeId, previousCallFrame, previousCallFrame, stackSize, function);
	}
	
	public Frame(int scopeId, Frame previousCallFrame, Frame previousScope, int stackSize, Function function){
		this(scopeId, previousCallFrame, previousScope, function, new Object[stackSize]);
	}
	
	private Frame(int scopeId, Frame previousCallFrame, Frame previousScope, Function function, final Object[] stack) {
		this.scopeId = scopeId;
		this.previousCallFrame = previousCallFrame;
		this.previousScope = previousScope;
		this.stack = stack;
		this.pc = 0;
		this.sp = 0;
		this.function = function;
		this.isCoroutine = function.isCoroutine;
	}
	
	/**
	 * Assumption: scopeIn != -1; 
	 */
	public Frame getCoroutineFrame(Function f, int scopeIn, int arity, int sp) {
		for(Frame env = this; env != null; env = env.previousCallFrame) {
			if (env.scopeId == f.scopeIn) {
				return getCoroutineFrame(f, env, arity, sp);
			}
		}
		throw new RuntimeException("Could not find a matching scope when computing a nested coroutine instance: " + f.scopeIn);
	}
	
	/**
	 * Given a current frame (this), 
	 * creates a new frame (frame) to be wrapped inside a coroutine object,
	 * pushes arguments from the current frame's stack to the stack of the new frame (given an arity),
	 * (re)setting the stack pointer of both the current and new frame, and
	 * returns the new frame.
	 */
	public Frame getCoroutineFrame(Function f, Frame env, int arity, int sp) {
		Frame frame = new Frame(f.scopeId, null, env, f.maxstack, f);
		// The main function of a coroutine instance may have formal parameters;
		// therefore, creation of a coroutine instance may take a number of arguments <= formal parameters
		if(arity > f.nformals) {
			throw new RuntimeException("Too many arguments have been passed to create a coroutine instance, expected <= " + f.nformals);
		}
		for (int i = arity - 1; i >= 0; i--) {
			frame.stack[i] = stack[sp - arity + i];
		}
		frame.sp = arity;
		this.sp = sp - arity;
		return frame;
	}
	
	/**
	 * Accounts for the case of partial parameter binding,
	 * creates a new frame (frame) (similar to the method above) to be wrapped inside a coroutine object,
	 * that gives a coroutine instance to be immediately initialized (i.e., all the coroutine arguments are assumed to be provided)
	 * 
	 * Assumption: fun_instance.next + arity == fun_instance.function.nformals
	 */
	public Frame getCoroutineFrame(FunctionInstance fun_instance, int arity, int sp) {
		Frame frame = getFrame(fun_instance.function, fun_instance.env, fun_instance.args, arity, sp);
		return frame;
	}
	
	/**
	 * Given a current frame (this), 
	 * creates a new frame (frame), 
	 * pushes arguments from the current frame's stack to the stack of the new frame,
	 * (re)setting the stack pointer of both the current and the new frame, and 
	 * returns the new frame to the caller.
	 */
	public Frame getFrame(Function f, Frame env, int arity, int sp) {
		Frame frame = new Frame(f.scopeId, this, env, f.maxstack, f);
		this.sp = frame.pushFunctionArguments(arity, this.stack, sp);
		return frame;
	}
		
	public Frame getFrame(Function f, Frame env, Object[] args) {
		Frame frame = new Frame(f.scopeId, this, env, f.maxstack, f);
		this.sp = frame.pushFunctionArguments(args.length, args, args.length);
		return frame;
	}
	
	public Frame getFrame(Function f, Frame env, Object[] args, int arity, int sp) {
		Frame frame = new Frame(f.scopeId, this, env, f.maxstack, f);
		if(args != null) {
			for(Object arg : args) {
				if(arg == null) {
					break;
				}
				frame.stack[frame.sp++] = arg;
			}
		}
		if(arity == 0) {
			this.sp = sp;
			frame.sp = frame.function.nlocals;
			return frame;
		}
		this.sp = frame.pushFunctionArguments(arity, this.stack, sp);
		return frame;
	}
	
	/**
	 * Given a current frame (this), 
	 * pushes arguments from a given stack (stack) to the current frame's stack (given an arity and the number of formal parameters of a function),
	 * resetting the current frame's stack pointer to the number of local variables of its function, and
	 * returns a new stack pointer for the given stack
	 * (in case of the given stack being an array of arguments: stack.length == arity == sp && start == 0).
	 */
	private int pushFunctionArguments(int arity, Object[] stack, int sp) {
		int start = sp - arity;
		if(!function.isVarArgs) {
			assert this.sp + arity == function.nformals;
			for(int i = 0; i < arity; i++){
				this.stack[this.sp++] = stack[start + i]; 
			}
		} else {
			int posArityMinusOne = function.nformals - 2; // The number of positional arguments minus one
			for(int i = 0; i < posArityMinusOne; i++) {
				this.stack[this.sp++] = stack[start + i];
			}
			Type argTypes = ((FunctionType) function.ftype).getArgumentTypes();
			if(function.nformals == arity && ((IValue) stack[start + posArityMinusOne]).getType().isSubtypeOf(argTypes.getFieldType(posArityMinusOne))) {
				this.stack[this.sp++] = stack[start + posArityMinusOne];
			} else {
				IListWriter writer = ValueFactory.getInstance().listWriter();
				for(int i = posArityMinusOne; i < arity - 1; i++) {
					writer.append((IValue) stack[start + i]);
				}
				this.stack[this.sp++] = writer.done();
			}
			this.stack[this.sp++] = stack[start + arity - 1]; // The keyword arguments
		}		
		this.sp = function.nlocals;
		return start;
	}
	
	public Frame copy() {
		if(pc != 0)
			throw new RuntimeException("Copying the frame with certain instructions having been already executed.");
		Frame newFrame = new Frame(scopeId, previousCallFrame, previousScope, function, stack.clone());
		newFrame.sp = sp; 
		return newFrame;
	}
	
}
