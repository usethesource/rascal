package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class Execute {

	private IValueFactory vf;

	public Execute(IValueFactory vf) {
		this.vf = vf;
	}
	
	// Get Boolean field from an instruction
	
	private boolean getBooleanField(IConstructor instruction, String field) {
		return ((IBool) instruction.get(field)).getValue();
	}

	// Get integer field from an instruction

	private int getIntField(IConstructor instruction, String field) {
		return ((IInteger) instruction.get(field)).intValue();
	}

	// Get String field from an instruction

	private String getStrField(IConstructor instruction, String field) {
		return ((IString) instruction.get(field)).getValue();
	}

	// Library function to execute a RVM program from Rascal

	public ITuple executeProgram(IConstructor program, IBool debug,
			IInteger repeat, IBool testsuite, IEvaluatorContext ctx) {
		
		boolean isTestSuite = testsuite.getValue();
		
		String main = isTestSuite ? "/testsuite(list(value());)#0" : "/main(list(value());)#0";
		String mu_main = isTestSuite ? "/testsuite(1)" : "/main(1)";
		String module_init = isTestSuite ? "/#module_init_testsuite()#0" : "/#module_init_main()#0";
		String mu_module_init = isTestSuite ? "/#module_init_testsuite(0)" : "/#module_init_main(0)";
		
		String uid_main = null;
		String uid_module_init = null;
		
		RVM rvm = new RVM(vf, ctx.getStdOut(), debug.getValue());

		IList types = (IList) program.get("types");
		for(IValue type : types) {
			rvm.declareConstructor((IConstructor) type);
		}
		IMap declarations = (IMap) program.get("declarations");

		for (IValue dname : declarations) {
			IConstructor declaration = (IConstructor) declarations.get(dname);

			if (declaration.getName().contentEquals("FUNCTION")) {

				String name = ((IString) declaration.get("qname")).getValue();
				if(name.endsWith(main) || name.endsWith(mu_main)) {
					// Get the main's uid
					uid_main = name;
				}
				if(name.endsWith(module_init) || name.endsWith(mu_module_init)) {
					// Get the module_init's uid
					uid_module_init = name;
				}
				Integer nlocals = ((IInteger) declaration.get("nlocals")).intValue();
				Integer nformals = ((IInteger) declaration.get("nformals")).intValue();
				Integer maxstack = ((IInteger) declaration.get("maxStack")).intValue();
				IList code = (IList) declaration.get("instructions");
				CodeBlock codeblock = new CodeBlock(vf);

				// Loading instructions
				for (int i = 0; i < code.length(); i++) {
					IConstructor instruction = (IConstructor) code.get(i);
					String opcode = instruction.getName();

					switch (opcode) {
					case "LOADCON":
						codeblock.LOADCON(instruction.get("val"));
						break;
						
					case "LOADVAR":
						codeblock.LOADVAR(getStrField(instruction, "fuid"), getIntField(instruction, "pos"));
						break;
						
					case "LOADLOC":
						codeblock.LOADLOC(getIntField(instruction, "pos"));
						break;
						
					case "STOREVAR":
						codeblock.STOREVAR(getStrField(instruction, "fuid"), getIntField(instruction, "pos"));
						break;
						
					case "STORELOC":
						codeblock.STORELOC(getIntField(instruction, "pos"));
						break;
						
					case "LABEL":
						codeblock = codeblock.LABEL(getStrField(instruction, "label"));
						break;
						
					case "CALLPRIM":
						codeblock.CALLPRIM(RascalPrimitive.valueOf(getStrField(instruction, "name")), getIntField(instruction, "arity"));
						break;
						
					case "CALLMUPRIM":
						codeblock.CALLMUPRIM(MuPrimitive.valueOf(getStrField(instruction, "name")), getIntField(instruction, "arity"));
						break;
						
					case "CALL":
						codeblock.CALL(getStrField(instruction, "fuid"), getIntField(instruction, "arity"));
						break;
						
					case "CALLDYN":
						codeblock.CALLDYN( getIntField(instruction, "arity"));
						break;
						
					case "LOADFUN":
						codeblock.LOADFUN(getStrField(instruction, "fuid"));
						break;
						
					case "RETURN0":
						codeblock.RETURN0();
						break;
						
					case "RETURN1":
						codeblock.RETURN1();
						break;
						
					case "JMP":
						codeblock.JMP(getStrField(instruction, "label"));
						break;
						
					case "JMPTRUE":
						codeblock.JMPTRUE(getStrField(instruction, "label"));
						break;
						
					case "JMPFALSE":
						codeblock.JMPFALSE(getStrField(instruction, "label"));
						break;
						
					case "HALT":
						codeblock.HALT();
						break;
						
					case "CREATE":
						codeblock.CREATE(getStrField(instruction, "fuid"), getIntField(instruction, "arity"));
						break;
						
					case "CREATEDYN":
						codeblock.CREATEDYN(getIntField(instruction, "arity"));
						break;
						
					case "INIT":
						codeblock.INIT(getIntField(instruction, "arity"));
						break;
						
					case "NEXT0":
						codeblock.NEXT0();
						break;
						
					case "NEXT1":
						codeblock.NEXT1();
						break;
						
					case "YIELD0":
						codeblock.YIELD0();
						break;
						
					case "YIELD1":
						codeblock.YIELD1();
						break;
						
					case "HASNEXT":
						codeblock.HASNEXT();
						break;
						
					case "PRINTLN":
						codeblock.PRINTLN(getIntField(instruction, "arity"));
						break;
						
					case "POP":
						codeblock.POP();
						break;
						
					case "LOADLOCREF":
						codeblock.LOADLOCREF(getIntField(instruction, "pos"));
						break;
						
					case "LOADVARREF":
						codeblock.LOADVARREF(getStrField(instruction, "fuid"), getIntField(instruction, "pos"));
						break;
						
					case "LOADLOCDEREF":
						codeblock.LOADLOCDEREF(getIntField(instruction, "pos"));
						break;
						
					case "LOADVARDEREF":
						codeblock.LOADVARDEREF(getStrField(instruction, "fuid"), getIntField(instruction, "pos"));
						break;
					
					case "STORELOCDEREF":
						codeblock.STORELOCDEREF(getIntField(instruction, "pos"));
						break;
						
					case "STOREVARDEREF":
						codeblock.STOREVARDEREF(getStrField(instruction, "fuid"), getIntField(instruction, "pos"));
						break;
						
					case "LOAD_NESTED_FUN":
						codeblock.LOADNESTEDFUN(getStrField(instruction, "fuid"), getStrField(instruction, "scopeIn"));
						break;
						
					case "LOADCONSTR":
						codeblock.LOADCONSTR(getStrField(instruction, "fuid"));
						break;
						
					case "CALLCONSTR":
						codeblock.CALLCONSTR(getStrField(instruction, "fuid"), getIntField(instruction, "arity"));
						break;
						
					case "LOADTYPE":
						codeblock.LOADTYPE(rvm.symbolToType((IConstructor) instruction.get("type")));
						break;
					case "LOADBOOL":
						codeblock.LOADBOOL(getBooleanField(instruction, "bval"));
						break;
						
					case "LOADINT":
						codeblock.LOADINT(getIntField(instruction, "nval"));
						break;
						
					case "DUP":
						codeblock.DUP();
						break;
						
					case "FAILRETURN":
						codeblock.FAILRETURN();
						break;
										
					default:
						throw new RuntimeException("PANIC: Unknown instruction: " + opcode + " has been used");
					}

				}
				rvm.declare(new Function(name, nformals, nlocals,
						maxstack, codeblock));
			}
		}

		long start = System.currentTimeMillis();
		Object result = null;
		for (int i = 0; i < repeat.intValue(); i++)
			result = rvm.executeProgram(uid_main, uid_module_init, new IValue[] {});
		long now = System.currentTimeMillis();
		return vf.tuple((IValue) result, vf.integer(now - start));
	}

}
