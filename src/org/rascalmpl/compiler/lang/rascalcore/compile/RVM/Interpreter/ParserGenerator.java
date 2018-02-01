/*******************************************************************************
 * Copyright (c) 2009-2016 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.Evaluator;							// TODO: remove import: YES
import org.rascalmpl.interpreter.control_exceptions.Throw;			// TODO: remove import: LATER
import org.rascalmpl.interpreter.env.GlobalEnvironment;				// TODO: remove import: YES
import org.rascalmpl.interpreter.env.ModuleEnvironment;				// TODO: remove import: YES
import org.rascalmpl.interpreter.load.StandardLibraryContributor;	// remove import: NO
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.JavaBridge;					// remove import: NO
import org.rascalmpl.parser.gtd.IGTD;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.RascalValueFactory;

public class ParserGenerator {
  private Evaluator evaluator;
  private final JavaBridge bridge;
  private final IValueFactory vf;
  private final TypeFactory tf;
  private static RVMCore rvmParserGenerator;
  private Function getParserMethodNameFunction;
  private Function newGenerateFunction;
  private Function createHoleFunction;
  private static final String packageName = "org.rascalmpl.java.parser.object";
  private static final boolean debug = false;
  private static final boolean profile = false;
  private static final boolean useCompiledParserGenerator = true;

  public ParserGenerator(RascalExecutionContext rex) throws IOException {
    this.vf = rex.getValueFactory();
    this.tf = TypeFactory.getInstance();
    this.bridge = new JavaBridge(Collections.emptyList(), rex.getValueFactory(), rex.getConfiguration());

      if(useCompiledParserGenerator && rvmParserGenerator == null) {
        RascalExecutionContext rex2 = 
            RascalExecutionContextBuilder.normalContext(rex.getPathConfig(), System.out, System.err)
            .forModule("$parsergenerator$")
            .jvm(true)                   // options for complete repl
            .profile(profile)
            .build();
        rvmParserGenerator = RVMCore.readFromFileAndInitialize(rex.getParserGenerator(), rex2);
      }

      if(useCompiledParserGenerator){			
        Type symSort = RascalTypeFactory.getInstance().nonTerminalType(vf.constructor(RascalValueFactory.Symbol_Sort, vf.string("Sym")));
        getParserMethodNameFunction = rvmParserGenerator.getFunction("getParserMethodName", tf.stringType(), tf.tupleType(symSort));
        if(getParserMethodNameFunction == null){
          throw new InternalCompilerError("Function getParserMethodName not found");
        }

        newGenerateFunction = rvmParserGenerator.getFunction("newGenerate", tf.stringType(), 
            tf.tupleType(tf.stringType(), tf.stringType(), 
                tf.abstractDataType(rex.getTypeStore(), "Grammar")));
        if(newGenerateFunction == null){
          throw new InternalCompilerError("Function newGenerate not found");
        }

        Type cpLex = RascalTypeFactory.getInstance().nonTerminalType(vf.constructor(RascalValueFactory.Symbol_Lex, vf.string("ConcretePart")));
        createHoleFunction = rvmParserGenerator.getFunction("createHole", tf.stringType(), 
            tf.tupleType(cpLex, tf.integerType()));
        if(createHoleFunction == null){
          throw new InternalCompilerError("Function createHole not found");
        }
      } else {
        GlobalEnvironment heap = new GlobalEnvironment();
        ModuleEnvironment scope = new ModuleEnvironment("$parsergenerator$", heap);
        this.evaluator = new Evaluator(ValueFactoryFactory.getValueFactory(), rex.getStdOut(), rex.getStdErr(), scope, heap);
        evaluator.addRascalSearchPathContributor(StandardLibraryContributor.getInstance());		this.evaluator.setBootstrapperProperty(true);

        IRascalMonitor monitor = rex.getMonitor();

        monitor.startJob("Compiled -- Loading interpreted parser generator, 2", 100, 139);
        try {
          evaluator.doImport(monitor, "lang::rascal::grammar::ParserGenerator");
          evaluator.doImport(monitor, "lang::rascal::grammar::ConcreteSyntax");
          evaluator.doImport(monitor, "lang::rascal::grammar::definition::Modules");
          evaluator.doImport(monitor, "lang::rascal::grammar::definition::Priorities");
          evaluator.doImport(monitor, "lang::rascal::grammar::definition::Regular");
          evaluator.doImport(monitor, "lang::rascal::grammar::definition::Keywords");
          evaluator.doImport(monitor, "lang::rascal::grammar::definition::Literals");
          evaluator.doImport(monitor, "lang::rascal::grammar::definition::Parameters");
          evaluator.doImport(monitor, "lang::rascal::grammar::definition::Symbols");
          evaluator.doImport(monitor, "analysis::grammars::Ambiguity");
        }
        finally {
          monitor.endJob(true);
        }
      }
    }

    private void debugOutput(String classString, String file) {
      if (debug) {
        System.err.println("Saving debug output in " + file);
        try (FileOutputStream s = new FileOutputStream(file)) {
          s.write(classString.getBytes());
          s.flush();
        } catch (IOException e) {
          throw new RuntimeException(e);
        } 
      }
    }

    public IConstructor convertMapToGrammar(IMap definition) {
      TypeFactory TF = TypeFactory.getInstance();
      TypeStore TS = new TypeStore(RascalValueFactory.getStore()); //new TypeStore();
      Type Grammar = TF.abstractDataType(TS, "Grammar");
      Type Symbol = TF.abstractDataType(TS, "Symbol");
      Type Production = TF.abstractDataType(TS, "Production");
      Type grammar = TF.constructor(TS, Grammar, "grammar", TF.setType(Symbol), "starts", TF.mapType(Symbol, Production), "rules");

      return vf.constructor(grammar, vf.set(), definition);
    }

    /** 
     * Produces the name generated by the parser generator for a parse method for the given symbol
     * @param rex TODO
     */
    public String getParserMethodName(IConstructor symbol, RascalExecutionContext rex) {
      if(useCompiledParserGenerator){
        return ((IString) rvmParserGenerator.executeRVMFunction(getParserMethodNameFunction, new IValue[]{ symbol }, new HashMap<String, IValue>())).getValue();
      } else {
        return ((IString) evaluator.call((IRascalMonitor) null, "getParserMethodName", symbol)).getValue();
      }
    }

    //	/**
    //	 * Converts the parse tree of a symbol to a UPTR symbol
    //	 */
    //	public IConstructor symbolTreeToSymbol(IConstructor symbol) {
    //	  return (IConstructor) evaluator.call((IRascalMonitor) null,"sym2symbol", symbol);
    //	}

    /**
     * Generate a parser from a Rascal syntax definition (a set of production rules).
     * 
     * @param monitor a progress monitor; this method will contribute 100 work units
     * @param loc     a location for error reporting
     * @param name    the name of the parser for use in code generation and for later reference
     * @param definition a map of syntax definitions (which are imports in the Rascal grammar)
     * @param rex TODO
     * @return A parser class, ready for instantiation
     */
    public Class<IGTD<IConstructor, ITree, ISourceLocation>> getNewParser(IRascalMonitor monitor, ISourceLocation loc, String name, IMap definition, RascalExecutionContext rex) {
      return getNewParser(loc, name, convertMapToGrammar(definition),  rex);
    }

    /**
     * Generate a parser from a Rascal grammar.
     * @param loc     a location for error reporting
     * @param name    the name of the parser for use in code generation and for later reference
     * @param grammar a grammar
     * @param rex TODO
     * 
     * @return A parser class, ready for instantiation
     */
    public Class<IGTD<IConstructor, ITree, ISourceLocation>> getNewParser(ISourceLocation loc, String name, IConstructor grammar, RascalExecutionContext rex) {
      rex.startJob("Compiled -- Generating new parser:" + name, 100, 60);
      try {

        String normName = name.replaceAll("::", "_").replaceAll("\\\\", "_");
        rex.event("Generating java source code for parser: " + name,30);
        IString classString;
        if(useCompiledParserGenerator){
          classString = (IString) rvmParserGenerator.executeRVMFunction(newGenerateFunction, new IValue[]{ vf.string(packageName), vf.string(normName), grammar }, new HashMap<String, IValue>());
          if (profile) {
              rvmParserGenerator.getFrameObserver().report();
          }
        } else {
          classString = (IString) evaluator.call(rex.getMonitor(), "newGenerate", vf.string(packageName), vf.string(normName), grammar);
        }

        debugOutput(grammar.toString(),  System.getProperty("java.io.tmpdir") + "/grammar.trm");
        debugOutput(classString.getValue(), System.getProperty("java.io.tmpdir") + "/parser.java");
        rex.event("Compiling generated java code: " + name, 30);
        return bridge.compileJava(loc, packageName + "." + normName, this.getClass(), classString.getValue());
      }  catch (ClassCastException e) {
        throw new InternalCompilerError("parser generator:" + e.getMessage(), e);
      } catch (Throw e) {
        throw new InternalCompilerError("parser generator: " + e.getMessage(), e);
      } catch (Thrown e) {
        throw new InternalCompilerError("parser generator: " + e.value, e);
      } catch (Throwable e) {
        throw new InternalCompilerError("parser generator: " + e.getMessage(), e);  
      } finally {
        rex.endJob(true);
      }
    }

    public String createHole(IConstructor part, int size, RascalExecutionContext rex) {
      if(useCompiledParserGenerator){
        return ((IString) rvmParserGenerator.executeRVMFunction(createHoleFunction, new IValue[]{ part, vf.integer(size) }, new HashMap<String, IValue>())).getValue();
      } else {
        return ((IString) evaluator.call("createHole", part, vf.integer(size))).getValue();
      }
    }
  }
