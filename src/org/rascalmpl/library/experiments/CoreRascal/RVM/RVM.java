package org.rascalmpl.library.experiments.CoreRascal.RVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.ast.Visibility.Public;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Opcode;
import org.rascalmpl.values.ValueFactoryFactory;

public class RVM {

	public IValueFactory vf;
	private IBool TRUE;
	private IBool FALSE;
	private boolean debug = true;
	private boolean listing = false;
	
	private Map<String, Integer> constantMap;
	ArrayList<IValue> constantStore;
	
	private ArrayList<Function> functionStore;
	private Map<String, Integer> functionMap;

	public RVM(IValueFactory vf) {
		this.vf = vf;
		TRUE = vf.bool(true);
		FALSE = vf.bool(false);
		constantStore = new ArrayList<IValue>();
		functionStore = new ArrayList<Function>();
		constantMap = new HashMap<String, Integer>();
		functionMap = new HashMap<String, Integer>();
	}
	
	public void declare(Function f){
		if(functionMap.get(f.name) != null){
			throw new RuntimeException("PANIC: Double declaration of function: " + f.name);
		}
		functionMap.put(f.name, functionStore.size());
		functionStore.add(f);
	}
	
	public void declareConst(String name, IValue val){
		if(constantMap.get(name) != null){
			throw new RuntimeException("PANIC: Double declaration of constant: " + name);
		}
		constantMap.put(name, constantStore.size());
		constantStore.add(val);
	}
	
	public void setDebug(boolean b){
		debug = b;
	}
	
	public void setListing(boolean b){
		listing = b;
	}
	
	public Object executeProgram(String main, IValue[] args) {

		for(Function f : functionStore){
			f.instructions.done(f.name, constantMap, functionMap, listing);
		}
		// Perform a call to "main"
		
		Function function = functionStore.get(functionMap.get(main));
		if (function == null) {
			throw new RuntimeException("PANIC: Code for main not found: " + main);
		}
		Frame cf = new Frame(0, null, function.maxstack, function);
		Object[] stack = cf.stack;
		if (args.length != function.nformals) {
			throw new RuntimeException("PANIC: " + main	+ " called with wrong number of arguaments: " + args.length);
		}
		for (int i = 0; i < args.length; i++) {
			stack[i] = args[i];
		}
		
		int[] instructions = function.instructions.getInstructions();
		int pc = 0;
		int sp = function.nlocals;

	NEXT_INSTRUCTION: while (true) {
			int op = instructions[pc++];
		
			if (debug) {
				int startpc = pc -1;
				for (int i = 0; i < sp; i++) {
					System.out.println("\t" + i + ": " + stack[i]);
				}
				System.out.println(cf.function.name + "[" + startpc + "] " + cf.function.instructions.toString(startpc));
			}
			
			switch (op) {

			case Opcode.OP_LOADCON:
				stack[sp++] = constantStore.get(instructions[pc++]);
				continue;
				
			case Opcode.OP_LOADFUN:
				stack[sp++] = functionStore.get(instructions[pc++]);
				continue;

			case Opcode.OP_LOADLOC:
					stack[sp++] = stack[instructions[pc++]];
					continue;
			
			case Opcode.OP_LOADVAR: {
					int s = instructions[pc++];
					int pos = instructions[pc++];
					for (Frame fr = cf.previous; fr != null; fr = fr.previous) {
						if (fr.scope == s) {
							stack[sp++] = fr.stack[pos];
							continue NEXT_INSTRUCTION;
						}
					}
					throw new RuntimeException("PANIC: load var cannot find matching scope: " + s);
				}
			
			case Opcode.OP_STORELOC: {
					stack[instructions[pc++]] = stack[--sp];
					continue;
				}
			
			case Opcode.OP_STOREVAR:
				int s = instructions[pc++];
				int pos = instructions[pc++];
				
				for (Frame fr = cf.previous; fr != null; fr = fr.previous) {
					if (fr.scope == s) {
						fr.stack[pos] = stack[--sp];
						continue NEXT_INSTRUCTION;
					}
				}
				
				throw new RuntimeException("PANIC: load var cannot find matching scope: " + s);

			case Opcode.OP_JMP:
				pc = instructions[pc];
				continue;

			case Opcode.OP_JMPTRUE:
				if (stack[sp - 1].equals(TRUE)) {
					pc = instructions[pc];
				} else
					pc++;
				sp--;
				continue;
				
			case Opcode.OP_JMPFALSE:
				if (stack[sp - 1].equals(FALSE)) {
					pc = instructions[pc];
				} else
					pc++;
				sp--;
				continue;
				
			case Opcode.OP_POP:
				sp--;
				continue;

			case Opcode.OP_LABEL:
				throw new RuntimeException("PANIC: label instruction at runtime");

			case Opcode.OP_CALLDYN:
			case Opcode.OP_CALL:
				Function fun = (op == Opcode.OP_CALL) ? functionStore.get(instructions[pc++]) : (Function)stack[--sp];
				instructions = fun.instructions.getInstructions();
				Frame nextFrame = new Frame(fun.scope, cf, fun.maxstack, fun);
				for (int i = fun.nformals - 1; i >= 0; i--) {
					nextFrame.stack[i] = stack[sp - fun.nformals + i];
				}
				cf.pc = pc;
				cf.sp = sp - fun.nlocals;
				cf = nextFrame;
				stack = cf.stack;
				sp = fun.nlocals;
				pc = 0;
				continue;

			case Opcode.OP_RETURN:
				Object rval = stack[sp - 1];
				cf = cf.previous;
				if (cf == null)
					return rval;
				instructions = cf.function.instructions.getInstructions();
				stack = cf.stack;
				sp = cf.sp;
				pc = cf.pc;
				stack[sp++] = rval;
				continue;
				
			case Opcode.OP_HALT:
				if (debug) {
					System.out.println("Program halted:");
					for (int i = 0; i < sp; i++) {
						System.out.println(i + ": " + stack[i]);
					}
				}
				return stack[sp - 1];
				
			/* Handle all primitives */
				
			case Opcode.OP_CALLPRIM:
				Primitive prim = Primitive.fromInteger(instructions[pc++]);
				switch (prim) {
				
			/* addition */
				case addition_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).add((IInteger) stack[sp - 1]);
					sp--;
					continue;
				case addition_list_list:
					stack[sp - 2] = Primitives.addition_list_list((IList) stack[sp - 2], (IList) stack[sp - 1]);
					sp--;
					continue;
			/* appendAfter */
			/* asType */
			/* composition */
			/* division */
			/* equals */
				case equal_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).equal((IInteger) stack[sp - 1]).getValue() ? TRUE : FALSE;
					sp--;
					continue;
			/* fieldAccess */
			/* fieldUpdate */
			/* fieldProject */
			/* getAnnotation */
			/* greater */
				case greater_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).greater((IInteger) stack[sp - 1]).getValue() ? TRUE : FALSE;
					sp--;
					continue;
			/* greaterThan */
			/* greaterThanOrEq */
			/* has */
			/* insertBefore */
			/* intersection */
			/* in */
			/* is */
			/* isDefined */
			/* join */
			/* lessThan */
			/* lessThanOrEq */
			/* mod */
			/* multiplication */
				case multiplication_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).multiply((IInteger) stack[sp - 1]);
					sp--;
					continue;
			/* negation */
			/* negative */
			/* nonEquals */
			/* product */
			/* remainder */
			/* slice */
			/* splice */
			/* setAnnotation */
			
			/* subscript */
				case subscript_list_int:
					stack[sp - 2] = Primitives.subscript_list_int((IList) stack[sp - 2], (IInteger) stack[sp - 1]);
					sp--;
					continue;
					
				case subscript_map:
					stack[sp - 2] = Primitives.subscript_map((IMap) stack[sp - 2], (IValue) stack[sp - 1]);
					sp--;
					continue;
					
				case subscript2:
					stack[sp - 3] = Primitives.subscript2((IValue) stack[sp - 3],  (IValue) stack[sp - 2], (IValue) stack[sp - 1]);
					sp--;
					continue;
					
			/* substraction */	
				case substraction_int_int:
					stack[sp - 2] = ((IInteger) stack[sp - 2]).subtract((IInteger) stack[sp - 1]);
					sp--;
					continue;
			/* transitiveClosure */
			/* transitiveRefleixiveClosure */
					
				default:
					throw new RuntimeException("PANIC: unknown primitive  " + instructions[pc-1]);
				}
				
			default:
				throw new RuntimeException("PANIC: RVM main loop -- cannot decode instruction");
			}
		}
	}

	public ITuple executeProgram(IList directives, IInteger repeats, IEvaluatorContext ctx) {
		String func = "main";
		RVM rvm = new RVM(ValueFactoryFactory.getValueFactory());
		List<IValue> functions = new ArrayList<>();
		for(IValue directive : directives) {
			String constr = ((IConstructor) directive).getName();
			String name = null;
			
			// Loading constants
			if(constr.equals("intconst")) {
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				rvm.declareConst(name, rvm.vf.integer(name));
			} else if(constr.equals("relconst")) {
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				rvm.declareConst(name, rvm.vf.real(name));
			} else if(constr.equals("ratconst")) {
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				rvm.declareConst(name, rvm.vf.rational(name));
			} else if(constr.equals("boolconst")) {
				name = ((IString) ((IConstructor) directive).get("value")).getValue();
				if(name.equals("TRUE")) {
					rvm.declareConst(name, rvm.TRUE);
				} else if(name.equals("FALSE")) {
					rvm.declareConst(name, rvm.FALSE);
				}
			} else if(constr.equals("function")) {
				functions.add(directive);
			} else {
				throw new RuntimeException("PANIC: Unknown directive: " + constr);
			}
		}
		
		// Loading directives
		for(IValue f : functions) {
			IConstructor directive = (IConstructor) f;
			String name = ((IString) directive.get("name")).getValue();
			Integer scope = ((IInteger) directive.get("scope")).intValue();
			Integer nlocals = ((IInteger) directive.get("nlocals")).intValue();
			Integer nformals = ((IInteger) directive.get("nformals")).intValue();
			Integer maxstack = ((IInteger) directive.get("maxStack")).intValue();
			IList code = (IList) directive.get("instructions");
			CodeBlock instructions = new CodeBlock();
			
			// Loading instructions
			for(int i = 0; i < code.length(); i++) {
				IConstructor instruction = (IConstructor) code.get(i);
				String opcode = ((IString) instruction.get("opcode")).getValue();
				IList operands = (IList) instruction.get("operands");
				
				if(opcode.equals("LOADCON")) {
					instructions = instructions.loadcon(((IString) operands.get(0)).getValue());
				} else if(opcode.equals("LOADVAR")) {
					instructions = instructions.loadvar(Integer.parseInt(((IString) operands.get(0)).getValue()), 
														Integer.parseInt(((IString) operands.get(1)).getValue()));
				} else if(opcode.equals("LOADLOC")) {
					instructions = instructions.loadloc(Integer.parseInt(((IString) operands.get(0)).getValue()));
				} else if(opcode.equals("STOREVAR")) {
					instructions = instructions.storevar(Integer.parseInt(((IString) operands.get(0)).getValue()), 
														 Integer.parseInt(((IString) operands.get(1)).getValue()));
				} else if(opcode.equals("STORELOC")) {
					instructions = instructions.storeloc(Integer.parseInt(((IString) operands.get(0)).getValue()));
				} else if(opcode.equals("LABEL")) {
					instructions = instructions.label(((IString) operands.get(0)).getValue());
				} else if(opcode.equals("CALLPRIM")) {
					String operand = ((IString) operands.get(0)).getValue();
					
					if(operand.equals("addition_int_int")) {
						instructions = instructions.callprim(Primitive.addition_int_int);
					} else if(operand.equals("equal_int_int")) {
						instructions = instructions.callprim(Primitive.equal_int_int);
					} else if(operand.equals("greater_int_int")) {
						instructions = instructions.callprim(Primitive.greater_int_int);
					} else if(operand.equals("multiplication_int_int")) {
						instructions = instructions.callprim(Primitive.multiplication_int_int);
					} else if(operand.equals("substraction_int_int")) {
						instructions = instructions.callprim(Primitive.substraction_int_int);
					} else {
						throw new RuntimeException("PANIC: Unknown primitive operation: " + operand);
					}
					
				} else if(opcode.equals("CALL")) {
					instructions = instructions.call(((IString) operands.get(0)).getValue());
				} else if(opcode.equals("RETURN")) {
					instructions = instructions.ret();
				} else if(opcode.equals("JMP")) {
					instructions = instructions.jmp(((IString) operands.get(0)).getValue());
				} else if(opcode.equals("JMPTRUE")) {
					instructions = instructions.jmptrue(((IString) operands.get(0)).getValue());
				} else if(opcode.equals("JMPFALSE")) {
					instructions = instructions.jmpfalse(((IString) operands.get(0)).getValue());
				} else if(opcode.equals("HALT")) {
					instructions = instructions.halt();
				} else { 
					throw new RuntimeException("PANIC: Unknown instruction: " + opcode + " has been used");
				}
				
			}
			rvm.declare(new Function(name, scope, nformals, nlocals, maxstack, instructions));
		}
		
		long start = System.currentTimeMillis();
		Object result = null;
		for(int i = 0; i < repeats.intValue(); i++)
			result = rvm.executeProgram(func, new IValue[] {});
		long now = System.currentTimeMillis();
		return vf.tuple((IValue)result, vf.integer(now - start));

	}

}
