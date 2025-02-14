/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * 
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Emilie Balland - (CWI)
 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI 
 *******************************************************************************/
package org.rascalmpl.interpreter;

import static org.rascalmpl.semantics.dynamic.Import.parseFragments;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Command;
import org.rascalmpl.ast.Commands;
import org.rascalmpl.ast.Declaration;
import org.rascalmpl.ast.EvalCommand;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.debug.AbstractInterpreterEventTrigger;
import org.rascalmpl.debug.IRascalFrame;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.debug.IRascalRuntimeInspection;
import org.rascalmpl.debug.IRascalSuspendTrigger;
import org.rascalmpl.debug.IRascalSuspendTriggerListener;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.StackTrace;
import org.rascalmpl.ideservices.IDEServices;
import org.rascalmpl.interpreter.asserts.NotYetImplemented;
import org.rascalmpl.interpreter.callbacks.IConstructorDeclared;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.Insert;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.control_exceptions.Return;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.env.Pair;
import org.rascalmpl.interpreter.load.IRascalSearchPathContributor;
import org.rascalmpl.interpreter.load.RascalSearchPath;
import org.rascalmpl.interpreter.load.URIContributor;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.OverloadedFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.CommandlineError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredFunction;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariable;
import org.rascalmpl.interpreter.staticErrors.UnguardedFail;
import org.rascalmpl.interpreter.staticErrors.UnguardedInsert;
import org.rascalmpl.interpreter.staticErrors.UnguardedReturn;
import org.rascalmpl.interpreter.utils.JavaBridge;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.interpreter.utils.Profiler;
import org.rascalmpl.library.lang.rascal.syntax.RascalParser;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.parser.Parser;
import org.rascalmpl.parser.ParserGenerator;
import org.rascalmpl.parser.gtd.io.InputConverter;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.parser.uptr.action.NoActionExecutor;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.RascalFunctionValueFactory;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class Evaluator implements IEvaluator<Result<IValue>>, IRascalSuspendTrigger, IRascalRuntimeInspection {
   
    private final RascalFunctionValueFactory vf; // sharable
    private static final TypeFactory tf = TypeFactory.getInstance(); // always shared
    protected volatile Environment currentEnvt; // not sharable

    private final GlobalEnvironment heap; // shareable if frozen
    private final Configuration config = new Configuration();
    /**
     * True if an interrupt has been signalled and we should abort execution
     */
    private volatile boolean interrupt = false;

    /**
     * State to manage call tracing
     */
    private int callNesting = 0;
	private boolean callTracing = false;

    @Override
    public int getCallNesting() {
       return callNesting;
    }

    @Override
    public boolean getCallTracing() {
        return callTracing;
    }

    @Override
    public void setCallTracing(boolean callTracing) {
        this.callTracing = callTracing;
    }

    @Override
    public void incCallNesting() {
        callNesting++;
    }

    @Override
    public void decCallNesting() {
        callNesting--;
    }


    private JavaBridge javaBridge; //  sharable if synchronized

    /**
     * Used in runtime error messages
     */
    private AbstractAST currentAST;

    /**
     * True if we're doing profiling
     */
    private boolean doProfiling = false;

    /**
     * Track if we already started a profiler, to avoid starting duplicates after a callback to an eval function.
     */
    private boolean profilerRunning = false;


    /**
     * This flag helps preventing non-terminating bootstrapping cycles. If 
     * it is set we do not allow loading of another nested Parser Generator.
     */
    private boolean isBootstrapper = false;

    private final TypeDeclarationEvaluator typeDeclarator; // not sharable

    private final List<ClassLoader> classLoaders; // sharable if frozen
    private final ModuleEnvironment rootScope; // sharable if frozen

    private final PrintWriter defOutWriter;
    private final PrintWriter defErrWriter;
    private final Reader defInput;
    
    private PrintWriter curOutWriter = null;
    private PrintWriter curErrWriter = null;
    private Reader curInput = null;

    /**
     * Probably not sharable
     */
    private ITestResultListener testReporter;
    /**
     * To avoid null pointer exceptions, avoid passing this directly to other classes, use
     * the result of getMonitor() instead. 
     */
    private IRascalMonitor monitor;

    private AbstractInterpreterEventTrigger eventTrigger; // can this be shared?	

    private final List<IRascalSuspendTriggerListener> suspendTriggerListeners;	 //  can this be shared?

    private Stack<Accumulator> accumulators = new Stack<>(); // not sharable
    private final Stack<IString> indentStack = new Stack<>(); // not sharable
    private final RascalSearchPath rascalPathResolver; // sharable if frozen

    private final URIResolverRegistry resolverRegistry; // sharable

    private final Map<IConstructorDeclared,Object> constructorDeclaredListeners; // can this be shared?
    
    private static final Object dummy = new Object();	

    /**
     * Promotes the monitor to the PrintWriter automatically if so required.
     */
    public Evaluator(IValueFactory f, Reader input, PrintWriter stderr, PrintWriter stdout, IRascalMonitor monitor, ModuleEnvironment scope, GlobalEnvironment heap) {
        this(f, input, stderr, monitor instanceof PrintWriter ? (PrintWriter) monitor : stdout, scope, heap, new ArrayList<ClassLoader>(Collections.singleton(Evaluator.class.getClassLoader())), new RascalSearchPath());
    }

    /**
     * If your monitor should wrap stdout (like TerminalProgressBarMonitor) then you can use this constructor.
     */
    public <M extends PrintWriter & IRascalMonitor> Evaluator(IValueFactory f, Reader input, PrintWriter stderr, M monitor, ModuleEnvironment scope, GlobalEnvironment heap) {
        this(f, input, stderr, monitor, scope, heap, new ArrayList<ClassLoader>(Collections.singleton(Evaluator.class.getClassLoader())), new RascalSearchPath());
        setMonitor(monitor);
    }

    public Evaluator(IValueFactory f, Reader input, PrintWriter stderr, PrintWriter stdout, ModuleEnvironment scope, GlobalEnvironment heap, IRascalMonitor monitor) {
        this(f, input, stderr, monitor instanceof PrintWriter ? (PrintWriter) monitor : stdout, scope, heap, new ArrayList<ClassLoader>(Collections.singleton(Evaluator.class.getClassLoader())), new RascalSearchPath());
        setMonitor(monitor);
    }

    public Evaluator(IValueFactory vf, Reader input, PrintWriter stderr, PrintWriter stdout, ModuleEnvironment scope, GlobalEnvironment heap, List<ClassLoader> classLoaders, RascalSearchPath rascalPathResolver) {
        super();

        this.vf = new RascalFunctionValueFactory(this);
        this.heap = heap;
        this.typeDeclarator = new TypeDeclarationEvaluator(this);
        this.currentEnvt = scope;
        this.rootScope = scope;
        heap.addModule(scope);
        this.classLoaders = classLoaders;
        this.javaBridge = new JavaBridge(classLoaders, vf, config);
        this.rascalPathResolver = rascalPathResolver;
        this.resolverRegistry = rascalPathResolver.getRegistry();
        this.defInput = input;
        this.defErrWriter = stderr;
        this.defOutWriter = stdout;
        this.constructorDeclaredListeners = new HashMap<IConstructorDeclared,Object>();
        this.suspendTriggerListeners = new CopyOnWriteArrayList<IRascalSuspendTriggerListener>();

        updateProperties();

        if (stderr == null) {
            throw new NullPointerException();
        }
        if (stdout == null) {
            throw new NullPointerException();
        }

        // default event trigger to swallow events
        setEventTrigger(AbstractInterpreterEventTrigger.newNullEventTrigger());
    }

    private Evaluator(Evaluator source, ModuleEnvironment scope) {
        super();

        // this.accumulators = source.accumulators;
        // this.testReporter = source.testReporter;
        this.vf = source.vf;
        this.heap = source.heap;
        this.typeDeclarator = new TypeDeclarationEvaluator(this);
        // this is probably not OK
        this.currentEnvt = scope;
        this.rootScope = scope;
        // this is probably not OK
        heap.addModule(scope);
        this.classLoaders = source.classLoaders;
        // the Java bridge is probably sharable if its methods are synchronized
        this.javaBridge = new JavaBridge(classLoaders, vf, config);
        this.rascalPathResolver = source.rascalPathResolver;
        this.resolverRegistry = source.resolverRegistry;
        this.defInput = source.defInput;
        this.defErrWriter = source.defErrWriter;
        this.defOutWriter = source.defOutWriter;
        this.constructorDeclaredListeners = new HashMap<IConstructorDeclared,Object>(source.constructorDeclaredListeners);
        this.suspendTriggerListeners = new CopyOnWriteArrayList<IRascalSuspendTriggerListener>(source.suspendTriggerListeners);

        updateProperties();

        // default event trigger to swallow events
        setEventTrigger(AbstractInterpreterEventTrigger.newNullEventTrigger());
    }

    public void resetJavaBridge() {
        this.javaBridge = new JavaBridge(classLoaders, vf, config);
    }

    @Override
    public IRascalMonitor setMonitor(IRascalMonitor monitor) {
        if (monitor == this) {
            return monitor;
        }

        interrupt = false;
        IRascalMonitor old = this.monitor;
        this.monitor = monitor;

        return old;
    }

    @Override	
    public int jobEnd(String name, boolean succeeded) {
        if (monitor != null)
            return monitor.jobEnd(name, succeeded);
        return 0;
    }

    @Override
    public void endAllJobs() {
        if (monitor != null) {
            monitor.endAllJobs();
        }
    }

    @Override	
    public void jobStep(String name, String msg, int inc) {
        if (monitor != null)
            monitor.jobStep(name, msg, inc);
    }

    @Override	
    public void jobStart(String name, int workShare, int totalWork) {
        if (monitor != null)
            monitor.jobStart(name, workShare, totalWork);
    }

    @Override	
    public void jobTodo(String name, int work) {
        if (monitor != null)
            monitor.jobTodo(name, work);
    }

    @Override	
    public boolean jobIsCanceled(String name) {
        if(monitor == null)
            return false;
        else
            return monitor.jobIsCanceled(name);
    }

    @Override
    public void registerConstructorDeclaredListener(IConstructorDeclared iml) {
        constructorDeclaredListeners.put(iml,dummy);
    }

    @Override	
    public void notifyConstructorDeclaredListeners() {
        for (IConstructorDeclared iml : constructorDeclaredListeners.keySet()) {
            if (iml != null) {
                iml.handleConstructorDeclaredEvent();
            }
        }
        constructorDeclaredListeners.clear();
    }

    @Override
    public List<ClassLoader> getClassLoaders() {
        return Collections.unmodifiableList(classLoaders);
    }

    @Override
    public ModuleEnvironment __getRootScope() {
        return rootScope;
    }

    @Override
    public PrintWriter getOutPrinter() {
        return curOutWriter == null ? defOutWriter : curOutWriter;
    }
    
    

    @Override
    public Reader getInput() {
        return curInput == null ? defInput : curInput;
    }

    @Override	
    public TypeDeclarationEvaluator __getTypeDeclarator() {
        return typeDeclarator;
    }

    @Override	
    public GlobalEnvironment __getHeap() {
        return heap;
    }

    @Override	
    public void __setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }


    @Override	
    public Stack<Accumulator> __getAccumulators() {
        return accumulators;
    }

    @Override	
    public IValueFactory __getVf() {
        return vf;
    }

    public static TypeFactory __getTf() {
        return tf;
    }

    @Override	
    public JavaBridge __getJavaBridge() {
        return javaBridge;
    }

    @Override	
    public void interrupt() {
        __setInterrupt(true);
    }

    @Override	
    public boolean isInterrupted() {
        return interrupt;
    }

    @Override
    public PrintWriter getErrorPrinter() {
        return curErrWriter == null ? defErrWriter : curErrWriter;
    }

    public void setTestResultListener(ITestResultListener l) {
        testReporter = l;
    }

    public JavaBridge getJavaBridge() {
        return javaBridge;
    }

    @Override
    public RascalSearchPath getRascalResolver() {
        return rascalPathResolver;
    }

    @Override
    public void indent(IString n) {
        indentStack.push(n);
    }

    @Override	
    public void unindent() {
        indentStack.pop();
    }

    @Override	
    public IString getCurrentIndent() {
        return indentStack.peek();
    }

    /**
     * Call a Rascal function with a number of arguments
     * 
     * @return either null if its a void function, or the return value of the
     *         function.
     */
    @Override
    public IValue call(IRascalMonitor monitor, String name, IValue... args) {
        IRascalMonitor old = setMonitor(monitor);
        try {
            return call(name, args);
        }
        finally {
            setMonitor(old);
        }
    }

    @Override
    public IValue call(String adt, String name, IValue... args) {
        List<AbstractFunction> candidates = new LinkedList<>();

        Type[] types = new Type[args.length];

        int i = 0;
        for (IValue v : args) {
            types[i++] = v.getType();
        }

        getCurrentEnvt().getAllFunctions(name, candidates);

        if (candidates.isEmpty()) {
            throw new UndeclaredFunction(name, types, this, getCurrentAST());
        }

        List<AbstractFunction> filtered = new LinkedList<>();

        for (AbstractFunction candidate : candidates) {
            if (candidate.getReturnType().isAbstractData() && candidate.getReturnType().getName().equals(adt)) {
                filtered.add(candidate);
            }
        }

        if (filtered.isEmpty()) {
            throw new UndeclaredFunction(adt + "::" + name, types, this, getCurrentAST());  
        }

        ICallableValue func = new OverloadedFunction(name, filtered);

        return func.call(getMonitor(), types, args, Collections.emptyMap()).getValue();
    };

    @Override
    public IValue call(String name, IValue... args) {
        return call(name, (Map<String,IValue>) null, args);
    }

    /**
     * Call a Rascal function with a number of arguments
     * 
     * @return either null if its a void function, or the return value of the
     *         function.
     */
    @Override
    public IValue call(IRascalMonitor monitor, String module, String name, IValue... args) {
        IRascalMonitor old = setMonitor(monitor);
        Environment oldEnv = getCurrentEnvt();

        try {
            ModuleEnvironment modEnv = getHeap().getModule(module);
            setCurrentEnvt(modEnv);
            return call(name, (Map<String,IValue>) null, args);
        }
        finally {
            setMonitor(old);
            setCurrentEnvt(oldEnv);
        }
    }

    /**
     * This function processes commandline parameters as they are typically passed
     * to a Rascal/Java program running on the commandline (a list of strings). 
     * 
     * The strings are interpreted as follows. If the first character is a '-' or the first two are '--'
     * then the string is the name of a keyword parameter of the main function. The type of the
     * declared parameter is used to determine how to parse the next string or strings. Note that
     * several strings in a row that do not start with '-' or '--' will be composed into a list or
     * a set depending on the type of the respective keyword parameter.
     */
    public IValue main(IRascalMonitor monitor, String module, String function, String[] commandline) {
        IRascalMonitor old = setMonitor(monitor);
        Environment oldEnv = getCurrentEnvt();

        try {
            ModuleEnvironment modEnv = getHeap().getModule(module);
            setCurrentEnvt(modEnv);

            Name name = Names.toName(function, modEnv.getLocation());

            Result<IValue> func = getCurrentEnvt().getVariable(name);

            if (func instanceof OverloadedFunction) {
                OverloadedFunction overloaded = (OverloadedFunction) getCurrentEnvt().getVariable(name);
                func = overloaded.getFunctions().get(0); 
            }

            if (func == null) {
                throw new UndeclaredVariable(function, name);
            }

            if (!(func instanceof AbstractFunction)) {
                throw new UnsupportedOperationException("main should be function");
            }

            AbstractFunction main = (AbstractFunction) func;

            if (main.getArity() == 1 && main.getFormals().getFieldType(0).isSubtypeOf(tf.listType(tf.stringType()))) {
                return main.call(getMonitor(), new Type[] { tf.listType(tf.stringType()) },new IValue[] { parsePlainCommandLineArgs(commandline)}, null).getValue();
            }
            else if (main.hasKeywordArguments() && main.getArity() == 0) {
                Map<String, IValue> args = parseKeywordCommandLineArgs(monitor, commandline, main);
                return main.call(getMonitor(), new Type[] { },new IValue[] {}, args).getValue();
            }
            else {
                throw new CommandlineError("main function should either have one argument of type list[str], or keyword parameters", main);
            }
        }
        catch (MatchFailed e) {
            getOutPrinter().println("Main function should either have a list[str] as a single parameter like so: \'void main(list[str] args)\', or a set of keyword parameters with defaults like so: \'void main(bool myOption=false, str input=\"\")\'");
            return null;
        }
        finally {
            setMonitor(old);
            setCurrentEnvt(oldEnv);
        }
    }

    private IList parsePlainCommandLineArgs(String[] commandline) {
        IListWriter w = vf.listWriter();
        for (String arg : commandline) {
            w.append(vf.string(arg));
        }
        return w.done();
    }

    public Map<String, IValue> parseKeywordCommandLineArgs(IRascalMonitor monitor, String[] commandline, AbstractFunction func) {
        Map<String, Type> expectedTypes = new HashMap<>();
        Type kwTypes = func.getKeywordArgumentTypes(getCurrentEnvt());

        for (String kwp : kwTypes.getFieldNames()) {
            expectedTypes.put(kwp, kwTypes.getFieldType(kwp));
        }

        Map<String, IValue> params = new HashMap<>();

        for (int i = 0; i < commandline.length; i++) {
            if (commandline[i].equals("-help")) {
                throw new CommandlineError("Help", func);
            }
            else if (commandline[i].startsWith("-")) {
                String label = commandline[i].replaceFirst("^-+", "");
                Type expected = expectedTypes.get(label);

                if (expected == null) {
                    throw new CommandlineError("unknown argument: " + label, func);
                }

                if (expected.isSubtypeOf(tf.boolType())) {
                    if (i == commandline.length - 1 || commandline[i+1].startsWith("-")) {
                        params.put(label, vf.bool(true));
                    }
                    else if (i < commandline.length - 1) {
                        String arg = commandline[++i].trim();
                        if (arg.equals("1") || arg.equals("true")) {
                            params.put(label, vf.bool(true));
                        }
                        else {
                            params.put(label, vf.bool(false));
                        }
                    }

                    continue;
                }
                else if (i == commandline.length - 1 || commandline[i+1].startsWith("-")) {
                    throw new CommandlineError("expected option for " + label, func);
                }
                else if (expected.isSubtypeOf(tf.listType(tf.valueType()))) {
                    IListWriter writer = vf.listWriter();

                    while (i + 1 < commandline.length && !commandline[i+1].startsWith("-")) {
                        writer.append(parseCommandlineOption(func, expected.getElementType(), commandline[++i]));
                    }

                    params.put(label, writer.done());
                }
                else if (expected.isSubtypeOf(tf.setType(tf.valueType()))) {
                    ISetWriter writer = vf.setWriter();

                    while (i + 1 < commandline.length && !commandline[i+1].startsWith("-")) {
                        writer.insert(parseCommandlineOption(func, expected.getElementType(), commandline[++i]));
                    }

                    params.put(label, writer.done());
                }
                else {
                    params.put(label, parseCommandlineOption(func, expected, commandline[++i]));
                }
            }
        }

        return params;
    }

    private IValue parseCommandlineOption(AbstractFunction main, Type expected, String option) {
        if (expected.isSubtypeOf(tf.stringType())) {
            return vf.string(option);
        }
        else {
            StringReader reader = new StringReader(option);
            try {
                return new StandardTextReader().read(vf, expected, reader);
            } catch (FactTypeUseException e) {
                throw new CommandlineError("expected " + expected + " but got " + option + " (" + e.getMessage() + ")", main);
            } catch (IOException e) {
                throw new CommandlineError("unxped problem while parsing commandline:" + e.getMessage(), main);
            }
        }
    }

    @Override
    public IValue call(String name, String module, Map<String, IValue> kwArgs, IValue... args) {
        IRascalMonitor old = setMonitor(monitor);
        Environment oldEnv = getCurrentEnvt();

        try {
            ModuleEnvironment modEnv = getHeap().getModule(module);
            setCurrentEnvt(modEnv);
            return call(name, kwArgs, args);
        }
        finally {
            setMonitor(old);
            setCurrentEnvt(oldEnv);
        }
    }

    private IValue call(String name, Map<String,IValue> kwArgs, IValue... args) {
        QualifiedName qualifiedName = Names.toQualifiedName(name, getCurrentEnvt().getLocation());
        setCurrentAST(qualifiedName);
        return call(qualifiedName, kwArgs, args);
    }

    @Override
    public IValue call(QualifiedName qualifiedName, Map<String,IValue> kwArgs, IValue... args) {
        ICallableValue func = (ICallableValue) getCurrentEnvt().getVariable(qualifiedName);
        Type[] types = new Type[args.length];

        int i = 0;
        for (IValue v : args) {
            types[i++] = v.getType();
        }

        if (func == null) {
            throw new UndeclaredFunction(Names.fullName(qualifiedName), types, this, getCurrentAST());
        }

        return func.call(getMonitor(), types, args, kwArgs).getValue();
    }

    // @Override	
    // public ITree parseObject(IConstructor grammar, ISet filters, ISourceLocation location, char[] input,  boolean allowAmbiguity, boolean hasSideEffects) {
    //     RascalFunctionValueFactory vf = new RascalFunctionValueFactory(this);
    //     IFunction parser = vf.parser(grammar, vf.bool(allowAmbiguity), vf.bool(hasSideEffects), vf.bool(false), filters);

    //     return (ITree) parser.call(vf.string(new String(input)), location);
    // }

    @Override
    public IConstructor getGrammar(Environment env) {
        ModuleEnvironment root = (ModuleEnvironment) env.getRoot();
        return getParserGenerator().getGrammarFromModules(monitor, root.getName(), root.getSyntaxDefinition());
    }

    public IValue diagnoseAmbiguity(IRascalMonitor monitor, IConstructor parseTree) {
        IRascalMonitor old = setMonitor(monitor);
        try {
            ParserGenerator pgen = getParserGenerator();
            return pgen.diagnoseAmbiguity(parseTree);
        }
        finally {
            setMonitor(old);
        }
    }

    public IConstructor getExpandedGrammar(IRascalMonitor monitor, ISourceLocation uri) {
        IRascalMonitor old = setMonitor(monitor);
        try {
            ParserGenerator pgen = getParserGenerator();
            String main = uri.getAuthority();
            ModuleEnvironment env = getHeap().getModule(main);
            return pgen.getExpandedGrammar(monitor, main, env.getSyntaxDefinition());
        }
        finally {
            setMonitor(old);
        }
    }

    public ISet getNestingRestrictions(IRascalMonitor monitor, IConstructor g) {
        IRascalMonitor old = setMonitor(monitor);
        try {
            ParserGenerator pgen = getParserGenerator();
            return pgen.getNestingRestrictions(monitor, g);
        }
        finally {
            setMonitor(old);
        }
    }

    private volatile ParserGenerator parserGenerator;


    public ParserGenerator getParserGenerator() {
        if (parserGenerator == null ){
            if (isBootstrapper()) {
                throw new ImplementationError("Cyclic bootstrapping is occurring, probably because a module in the bootstrap dependencies is using the concrete syntax feature.");
            }

            Evaluator self = this;

            synchronized (self) {
                if (parserGenerator == null) {

                    parserGenerator = new ParserGenerator(getMonitor(), (monitor instanceof PrintWriter) ? (PrintWriter)monitor : getErrorPrinter(), classLoaders, getValueFactory(), config);
                }
            }
        }

        return parserGenerator;
    }

    @Override	
    public void setCurrentAST(AbstractAST currentAST) {
        this.currentAST = currentAST;
    }

    @Override	
    public AbstractAST getCurrentAST() {
        return currentAST;
    }

    public void addRascalSearchPathContributor(IRascalSearchPathContributor contrib) {
        rascalPathResolver.addPathContributor(contrib);
    }

    public void addRascalSearchPath(final ISourceLocation uri) {
        rascalPathResolver.addPathContributor(new URIContributor(uri));
    }

    public void addClassLoader(ClassLoader loader) {
        // later loaders have precedence
        classLoaders.add(0, loader);
    }

    @Override	
    public StackTrace getStackTrace() {
        StackTrace trace = new StackTrace();
        Environment env = currentEnvt;
        while (env != null) {
            trace.add(env.getLocation(), env.getName());
            env = env.getCallerScope();
        }
        return trace.freeze();
    }

    /**
     * Evaluate a statement
     * 
     * Note, this method is not supposed to be called within another overriden
     * eval(...) method, because it triggers and idle event after evaluation is
     * done.
     * 
     * @param stat
     * @return
     */
    @Override	
    public Result<IValue> eval(Statement stat) {
        __setInterrupt(false);
        try {
            Profiler profiler = null;
            if (doProfiling && !profilerRunning) {
                profiler = new Profiler(this);
                profiler.start();
                profilerRunning = true;
            }
            currentAST = stat;
           
            try {
                return stat.interpret(this);
            } finally {
                if (profiler != null) {
                    profiler.pleaseStop();
                    profiler.report();
                    profilerRunning = false;
                }
                getEventTrigger().fireIdleEvent();
                endAllJobs();
            }
        } 
        catch (Return e) {
            throw new UnguardedReturn(stat);
        } 
        catch (Failure e) {
            throw new UnguardedFail(stat, e);
        } 
        catch (Insert e) {
            throw new UnguardedInsert(stat);
        }
    }

    /**
     * Parse and evaluate a command in the current execution environment
     * 
     * Note, this method is not supposed to be called within another overriden
     * eval(...) method, because it triggers and idle event after evaluation is
     * done.
     * 
     * @param command
     * @return
     */
    @Override
    public Result<IValue> eval(IRascalMonitor monitor, String command, ISourceLocation location) {
        IRascalMonitor old = setMonitor(monitor);
        try {
            return eval(command, location);
        }
        finally {
            endAllJobs();
            setMonitor(old);
            getEventTrigger().fireIdleEvent();
        }
    }

    /**
     * Parse and evaluate a command in the current execution environment
     * 
     * Note, this method is not supposed to be called within another overriden
     * eval(...) method, because it triggers and idle event after evaluation is
     * done.	 
     * 
     * @param command
     * @return
     */
    @Override
    public Result<IValue> evalMore(IRascalMonitor monitor, String commands, ISourceLocation location) {
        IRascalMonitor old = setMonitor(monitor);
        try {
            return evalMore(commands, location);
        }
        finally {
            endAllJobs();
            setMonitor(old);
            getEventTrigger().fireIdleEvent();
        }
    }

    private Result<IValue> eval(String command, ISourceLocation location) throws ImplementationError {
        __setInterrupt(false);
        IActionExecutor<ITree> actionExecutor =  new NoActionExecutor();
        ITree tree = new RascalParser().parse(Parser.START_COMMAND, location.getURI(), command.toCharArray(), actionExecutor, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(false));

        if (!noBacktickOutsideStringConstant(command)) {
            ModuleEnvironment curMod = getCurrentModuleEnvironment();
            IFunction parsers = parserForCurrentModule(vf, curMod);
            tree = org.rascalmpl.semantics.dynamic.Import.parseFragments(vf, getMonitor(), parsers, tree, location, getCurrentModuleEnvironment());
        }

        Command stat = new ASTBuilder().buildCommand(tree);

        if (stat == null) {
            throw new ImplementationError("Disambiguation failed: it removed all alternatives");
        }

        return eval(stat);
    }

    private IFunction parserForCurrentModule(RascalFunctionValueFactory vf, ModuleEnvironment curMod) {
        IConstructor dummy = vf.constructor(RascalFunctionValueFactory.Symbol_Empty); // I just need _any_ ok non-terminal
        IMap syntaxDefinition = curMod.getSyntaxDefinition();
        IMap grammar = (IMap) getParserGenerator().getGrammarFromModules(getMonitor(), curMod.getName(), syntaxDefinition).get("rules");
        IConstructor reifiedType = vf.reifiedType(dummy, grammar);
        return vf.parsers(reifiedType, vf.bool(false), vf.bool(false), vf.bool(false), vf.bool(false), vf.set()); 
    }

    private Result<IValue> evalMore(String command, ISourceLocation location)
        throws ImplementationError {
        __setInterrupt(false);
        ITree tree;

        IActionExecutor<ITree> actionExecutor = new NoActionExecutor();
        tree = new RascalParser().parse(Parser.START_COMMANDS, location.getURI(), command.toCharArray(), actionExecutor, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(false));

        if (!noBacktickOutsideStringConstant(command)) {
            IFunction parsers = parserForCurrentModule(vf, getCurrentModuleEnvironment());
            tree = org.rascalmpl.semantics.dynamic.Import.parseFragments(vf, getMonitor(), parsers, tree, location, getCurrentModuleEnvironment());
        }

        Commands stat = new ASTBuilder().buildCommands(tree);

        if (stat == null) {
            throw new ImplementationError("Disambiguation failed: it removed all alternatives");
        }

        return eval(stat);
    }

    /*
     * This is dangereous, since inside embedded concrete fragments there may be unbalanced
     * double quotes as well as unbalanced backticks. For now it is a workaround that prevents
     * generation of parsers when some backtick is inside a string constant.
     */
    private boolean noBacktickOutsideStringConstant(String command) {
        boolean instring = false;
        byte[] b = command.getBytes();

        for (int i = 0; i < b.length; i++) {
            if (b[i] == '\"') {
                instring = !instring;
            }
            else if (!instring && b[i] == '`') {
                return false;
            }
        }

        return true;
    }

    @Override
    public ITree parseCommand(IRascalMonitor monitor, String command, ISourceLocation location) {
        IRascalMonitor old = setMonitor(monitor);
        try {
            return parseCommand(command, location);
        }
        finally {
            setMonitor(old);
        }
    }	

    private ITree parseCommand(String command, ISourceLocation location) {
        __setInterrupt(false);
        IActionExecutor<ITree> actionExecutor =  new NoActionExecutor();
        ITree tree =  new RascalParser().parse(Parser.START_COMMAND, location.getURI(), command.toCharArray(), actionExecutor, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(false));
        if (!noBacktickOutsideStringConstant(command)) {
            tree = org.rascalmpl.semantics.dynamic.Import.parseFragments(vf, getMonitor(), parserForCurrentModule(vf, getCurrentModuleEnvironment()), tree, location, getCurrentModuleEnvironment());
        }

        return tree;
    }

    @Override
    public ITree parseCommands(IRascalMonitor monitor, String commands, ISourceLocation location) {
        IRascalMonitor old = setMonitor(monitor);
        try {
            __setInterrupt(false);
            IActionExecutor<ITree> actionExecutor =  new NoActionExecutor();
            ITree tree = new RascalParser().parse(Parser.START_COMMANDS, location.getURI(), commands.toCharArray(), actionExecutor, new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(false));

            if (!noBacktickOutsideStringConstant(commands)) {
                tree = parseFragments(vf, getMonitor(), parserForCurrentModule(vf, getCurrentModuleEnvironment()), tree, location, getCurrentModuleEnvironment());
            }

            return tree;
        }
        finally {
            setMonitor(old);
        }
    }

    @Override
    public Result<IValue> eval(IRascalMonitor monitor, Command command) {
        IRascalMonitor old = setMonitor(monitor);
        try {
            return eval(command);
        }
        finally {
            endAllJobs();
            setMonitor(old);
            getEventTrigger().fireIdleEvent();
        }
    }

    private Result<IValue> eval(Commands commands) {
        __setInterrupt(false);
        Profiler profiler = null;
        if (doProfiling && !profilerRunning) {
            profiler = new Profiler(this);
            profiler.start();
            profilerRunning = true;
        }
        try {
            Result<IValue> last = ResultFactory.nothing();
            for (EvalCommand command : commands.getCommands()) {
                last = command.interpret(this);
            }
            return last;
        } finally {
            if (profiler != null) {
                profiler.pleaseStop();
                profiler.report();
                profilerRunning = false;
            }
        }
    }

    private Result<IValue> eval(Command command) {
        __setInterrupt(false);
        Profiler profiler = null;
        if (doProfiling && !profilerRunning) {
            profiler = new Profiler(this);
            profiler.start();
            profilerRunning = true;
        }
        try {
            if (command.isImport()) {
                return job(LOADING_JOB_CONSTANT, 1, (jobName, step) -> {
                    try {
                        return command.interpret(this);
                    }
                    finally{
                        step.accept(jobName, 1);
                    }
                });
            }
            else {
                return command.interpret(this);
            }
        } finally {
            if (profiler != null) {
                profiler.pleaseStop();
                profiler.report();
                profilerRunning = false;
            }
        }
    }

    /**
     * Evaluate a declaration
     * 
     * @param declaration
     * @return
     */
    public Result<IValue> eval(IRascalMonitor monitor, Declaration declaration) {
        IRascalMonitor old = setMonitor(monitor);
        try {
            __setInterrupt(false);
            currentAST = declaration;
            Result<IValue> r = declaration.interpret(this);
            if (r != null) {
                return r;
            }

            throw new NotYetImplemented(declaration);
        }
        finally {
            setMonitor(old);
        }
    }

    /**
     * This starts a comprehensive batch of imports with its
     * own monitoring job. The bar will grow as new modules
     * are discovered to be recursively imported or extended.
     * @param monitor
     * @param string
     */
    public void doImport(IRascalMonitor monitor, String... string) {
        IRascalMonitor old = setMonitor(monitor);
        interrupt = false;
        try {
            monitor.jobStart(LOADING_JOB_CONSTANT, string.length);
            ISourceLocation uri = URIUtil.rootLocation("import");
            for (String module : string) {
                monitor.jobStep(LOADING_JOB_CONSTANT, "Starting on " + module);
                org.rascalmpl.semantics.dynamic.Import.importModule(module, uri, this);
            }
        }
        finally {
            monitor.jobEnd(LOADING_JOB_CONSTANT, true);
            setMonitor(old);
            setCurrentAST(null);
        }
    }

    /**
     * This is if a LOADING_JOB_CONSTANT-named job is already running, and
     * we need to execute the next few imports. It assumed at least names.length
     * work is on the todo list for the monitor. The todo work will grow
     * as recursively imported or extended modules are discovered.
     */
    public void doNextImport(String jobName, String... names) {
        IRascalMonitor old = setMonitor(monitor);
        interrupt = false;
        try {
            ISourceLocation uri = URIUtil.rootLocation("import");
            for (String module : names) {
                monitor.jobStep(jobName, "Starting on " + module);
                org.rascalmpl.semantics.dynamic.Import.importModule(module, uri, this);
            }
        }
        finally {
            setMonitor(old);
            setCurrentAST(null);
        }
    }

    public Set<String> reloadModules(IRascalMonitor monitor, Set<String> names, ISourceLocation errorLocation) {
        Set<String> reloaded = new HashSet<>();
        reloadModules(monitor, names, errorLocation, true, reloaded);
        return Collections.unmodifiableSet(reloaded);
    }

    private void reloadModules(IRascalMonitor monitor, Set<String> names, ISourceLocation errorLocation, boolean recurseToExtending, Set<String> affectedModules) {
        SaveWarningsMonitor wrapped = new SaveWarningsMonitor(monitor, getErrorPrinter());
        IRascalMonitor old = setMonitor(wrapped);

        try {
            Set<String> onHeap = new HashSet<>();
            Set<String> extendingModules = new HashSet<>();
            PrintWriter errStream = getErrorPrinter();
 
            for (String mod : names) {
                if (heap.existsModule(mod)) {
                    onHeap.add(mod);
                    if (recurseToExtending) {
                        extendingModules.addAll(heap.getExtendingModules(mod));
                    }
                    heap.removeModule(heap.getModule(mod));
                }
            }
            extendingModules.removeAll(names);

            job(LOADING_JOB_CONSTANT, onHeap.size(), (jobName, step) ->  {
                for (String mod : onHeap) {
                    try {
                        wrapped.clear();
                        if (!heap.existsModule(mod)) {
                            reloadModule(mod, errorLocation, affectedModules, jobName);
                            if (!heap.existsModule(mod)) {
                                // something went wrong with reloading, let's print that:
                                errStream.println("** Something went wrong while reloading module " + mod + ":");
                                for (String s : wrapped.getWarnings()) {
                                    errStream.println(s);
                                }
                                errStream.println("*** Note: after fixing the error, you will have to manually reimport the modules that you already imported.");
                                errStream.println("*** if the error persists, start a new console session.");
                            }
                        }    
                    } finally {
                        step.accept(mod, 1);
                    }      
                }

                Set<String> dependingImports = new HashSet<>();
                Set<String> dependingExtends = new HashSet<>();
                dependingImports.addAll(getImportingModules(names));
                dependingExtends.addAll(getExtendingModules(names));

                for (String mod : dependingImports) {
                    ModuleEnvironment env = heap.getModule(mod);
                    Set<String> todo = new HashSet<>(env.getImports());
                    
                    for (String imp : todo) {
                        if (names.contains(imp)) {
                            env.unImport(imp);
                            ModuleEnvironment imported = heap.getModule(imp);
                            if (imported != null) {
                                env.addImport(imp, imported);
                                affectedModules.add(mod);
                            }
                            else {
                                errStream.println("Could not reimport" + imp + " at " + errorLocation);
                                warning("could not reimport " + imp, errorLocation);
                            }
                        }
                    }
                }

                for (String mod : dependingExtends) {
                    ModuleEnvironment env = heap.getModule(mod);
                    Set<String> todo = new HashSet<>(env.getExtends());

                    for (String ext : todo) {
                        if (names.contains(ext)) {
                            env.unExtend(ext);
                            ModuleEnvironment extended = heap.getModule(ext);
                            if (extended != null) {
                                env.addExtend(ext);
                            }
                            else {
                                errStream.println("Could not re-extend" + ext + " at " + errorLocation);
                                warning("could not re-extend " + ext, errorLocation);
                            }
                        }
                    }
                }

                if (recurseToExtending && !extendingModules.isEmpty()) {
                    reloadModules(monitor, extendingModules, errorLocation, false, affectedModules);
                }

                if (!names.isEmpty()) {
                    notifyConstructorDeclaredListeners();
                }

                return true;
            });
        }
        finally {
            setMonitor(old);
        }
    }

    private void reloadModule(String name, ISourceLocation errorLocation, Set<String> reloaded, String jobName) {
        try {
            org.rascalmpl.semantics.dynamic.Import.loadModule(errorLocation, name, this);
            reloaded.add(name);
        }
        catch (Throwable e) {
            // warnings should have been reported about this already 
        }
    }

    /**
     * transitively compute which modules depend on the given modules
     * @param names
     * @return
     */
    private Set<String> getImportingModules(Set<String> names) {
        Set<String> found = new HashSet<>();
        LinkedList<String> todo = new LinkedList<>(names);

        while (!todo.isEmpty()) {
            String mod = todo.pop();
            Set<String> dependingModules = heap.getImportingModules(mod);
            dependingModules.removeAll(found);
            found.addAll(dependingModules);
            todo.addAll(dependingModules);
        }

        return found;
    }

    private Set<String> getExtendingModules(Set<String> names) {
        Set<String> found = new HashSet<>();
        LinkedList<String> todo = new LinkedList<>(names);

        while (!todo.isEmpty()) {
            String mod = todo.pop();
            Set<String> dependingModules = heap.getExtendingModules(mod);
            dependingModules.removeAll(found);
            found.addAll(dependingModules);
            todo.addAll(dependingModules);
        }

        return found;
    }

    @Override	
    public void unwind(Environment old) {
        setCurrentEnvt(old);
    }

    @Override	
    public void pushEnv() {
        Environment env = new Environment(getCurrentEnvt(), getCurrentLocation(), getCurrentEnvt().getName());
        setCurrentEnvt(env);
    }

    private ISourceLocation getCurrentLocation() {
        return currentAST != null ? currentAST.getLocation() : getCurrentEnvt().getLocation();
    }

    @Override  
    public void pushEnv(String name) {
        Environment env = new Environment(getCurrentEnvt(), getCurrentLocation(), name);
        setCurrentEnvt(env);
    }

    @Override	
    public Environment pushEnv(Statement s) {
        /* use the same name as the current envt */
        Environment env = new Environment(getCurrentEnvt(), s.getLocation(), getCurrentEnvt().getName());
        setCurrentEnvt(env);
        return env;
    }


    @Override	
    public void printHelpMessage(PrintWriter out) {
        out.println("Welcome to the Rascal command shell.");
        out.println();
        out.println("Shell commands:");
        out.println(":help                      Prints this message");
        out.println(":quit or EOF               Quits the shell");
        out.println(":set <option> <expression> Sets an option");
        out.println("e.g. profiling    true/false");
        out.println("     tracing      true/false");
        out.println("     errors       true/false");
        out.println(":edit <modulename>         Opens an editor for that module");
        out.println(":test                      Runs all unit tests currently loaded");
        out.println();
        out.println("Example rascal statements and declarations:");
        out.println("1 + 1;                     Expressions simply print their output and (static) type");
        out.println("int a;                     Declarations allocate a name in the current scope");
        out.println("a = 1;                     Assignments store a value in a (optionally previously declared) variable");
        out.println("int a = 1;                 Declaration with initialization");
        out.println("import IO;                 Importing a module makes its public members available");
        out.println("println(\"Hello World\")     Function calling");
        out.println();
        out.println("Please read the manual for further information");
        out.flush();
    }

    // Modules -------------------------------------------------------------
    @Override	
    public ModuleEnvironment getCurrentModuleEnvironment() {
        if (!(currentEnvt instanceof ModuleEnvironment)) {
            throw new ImplementationError("Current env should be a module environment");
        }
        return ((ModuleEnvironment) currentEnvt);
    }

    private char[] getResourceContent(ISourceLocation location) throws IOException{
        char[] data;
        Reader textStream = null;

        try {
            textStream = resolverRegistry.getCharacterReader(location);
            data = InputConverter.toChar(textStream);
        }
        finally{
            if(textStream != null){
                textStream.close();
            }
        }

        return data;
    }

    /**
     * Parse a module. Practical for implementing IDE features or features that
     * use Rascal to implement Rascal. Parsing a module currently has the side
     * effect of declaring non-terminal types in the given environment.
     */
    @Override
    public ITree parseModuleAndFragments(IRascalMonitor monitor, ISourceLocation location, String jobName) throws IOException{
        return parseModuleAndFragments(monitor, jobName, getResourceContent(location), location);
    }

    public ITree parseModuleAndFragments(IRascalMonitor monitor, String jobName, char[] data, ISourceLocation location){
        IRascalMonitor old = setMonitor(monitor);
        try {
            return org.rascalmpl.semantics.dynamic.Import.parseModuleAndFragments(data, location, jobName, this);
        }
        finally{
            setMonitor(old);
        }
    }

    @Override	
    public void updateProperties() {
        doProfiling = config.getProfilingProperty();

        setCallTracing(config.getTracingProperty());
    }

    public Configuration getConfiguration() {
        return config;
    }

    public Stack<IRascalFrame> getCallStack() {
        Stack<IRascalFrame> stack = new Stack<>();
        Environment env = currentEnvt;
        while (env != null) {
            stack.add(0, env);
            env = env.getCallerScope();
        }
        return stack;
    }

    @Override	
    public Environment getCurrentEnvt() {
        return currentEnvt;
    }

    @Override	
    public void setCurrentEnvt(Environment env) {
        currentEnvt = env;
    }

    @Override	
    public IEvaluator<Result<IValue>> getEvaluator() {
        return this;
    }

    @Override	
    public GlobalEnvironment getHeap() {
        return __getHeap();
    }

    @Override	
    public boolean runTests(IRascalMonitor monitor) {
        IRascalMonitor old = setMonitor(monitor);
        try {
            final boolean[] allOk = new boolean[] { true };
            final ITestResultListener l = testReporter != null ? testReporter : new DefaultTestResultListener(getOutPrinter(), getErrorPrinter());

            new TestEvaluator(this, new ITestResultListener() {

                @Override
                public void report(boolean successful, String test, ISourceLocation loc, String message, Throwable t) {
                    if (!successful)
                        allOk[0] = false;
                    l.report(successful, test, loc, message, t);
                }

                @Override
                public void done() {
                    l.done();
                }

                @Override
                public void ignored(String test, ISourceLocation loc) {
                    l.ignored(test, loc);
                }

                @Override
                public void start(String context, int count) {
                    l.start(context, count);
                }
            }).test();
            return allOk[0];
        }
        finally {
            setMonitor(old);
        }
    }

    @Override	
    public IValueFactory getValueFactory() {
        return __getVf();
    }

    @Override	
    public RascalFunctionValueFactory getFunctionValueFactory() {
        return (RascalFunctionValueFactory) __getVf();
    }

    public void setAccumulators(Accumulator accu) {
        __getAccumulators().push(accu);
    }

    @Override	
    public Stack<Accumulator> getAccumulators() {
        return __getAccumulators();
    }

    @Override
    public void setAccumulators(Stack<Accumulator> accumulators) {
        this.accumulators = accumulators;
    }

    @Override
    public IRascalMonitor getMonitor() {
        if (monitor != null)
            return monitor;

        return new NullRascalMonitor();
    }

    public void overrideDefaultWriters(Reader newInput, PrintWriter newStdOut, PrintWriter newStdErr) {
        this.curInput = newInput;
        this.curErrWriter = newStdErr;
        this.curOutWriter = newStdOut;
    }

    public void revertToDefaultWriters() {
        this.curInput = null;
        if (curOutWriter != null) {
            curOutWriter.flush();
        }
        this.curOutWriter = null;
        if (curErrWriter != null) {
            curErrWriter.flush();
        }
        this.curErrWriter = null;
    }

    public Result<IValue> call(IRascalMonitor monitor, ICallableValue fun, Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) {
        Profiler profiler = null;
        if (doProfiling && !profilerRunning) {
            profiler = new Profiler(this);
            profiler.start();
            profilerRunning = true;
            try {
                return fun.call(monitor, argTypes, argValues, keyArgValues);
            } finally {
                if (profiler != null) {
                    profiler.pleaseStop();
                    profiler.report();
                    profilerRunning = false;
                }
            }
        }
        else {
            return fun.call(monitor, argTypes, argValues, keyArgValues);
        }
    }

    public Result<IValue> call(IRascalMonitor monitor, ICallableValue fun, Type[] argTypes, IValue... argValues) {
        return call(monitor, fun, argTypes, argValues, null);
    }

    @Override
    public void addSuspendTriggerListener(IRascalSuspendTriggerListener listener) {
        suspendTriggerListeners.add(listener);
    }

    @Override
    public void removeSuspendTriggerListener(
        IRascalSuspendTriggerListener listener) {
        suspendTriggerListeners.remove(listener);
    }

    @Override
    public void notifyAboutSuspension(AbstractAST currentAST) {
        if (!suspendTriggerListeners.isEmpty() && currentAST.isBreakable()) {
            for (IRascalSuspendTriggerListener listener : suspendTriggerListeners) {
                listener.suspended(this, () -> getCallStack().size(), currentAST.getLocation());
            }
        }
    }

    public AbstractInterpreterEventTrigger getEventTrigger() {
        return eventTrigger;
    }

    public void setEventTrigger(AbstractInterpreterEventTrigger eventTrigger) {
        this.eventTrigger = eventTrigger;
    }

    @Override
    public void freeze() {
    
    }

    @Override
    public IEvaluator<Result<IValue>> fork() {
        return new Evaluator(this, rootScope);
    }

    public void setBootstrapperProperty(boolean b) {
        this.isBootstrapper = b;
    }

    public boolean isBootstrapper() {
        return isBootstrapper;
    }

    public void removeSearchPathContributor(IRascalSearchPathContributor contrib) {
        rascalPathResolver.remove(contrib);
    }

    @Override
    public List<IRascalSuspendTriggerListener> getSuspendTriggerListeners() {
        return suspendTriggerListeners;
    }

    @Override
    public void warning(String message, ISourceLocation src) {
        if (monitor != null) {
            monitor.warning(message, src);
        }
    }

    private Stack<TraversalEvaluator> teStack = new Stack<TraversalEvaluator>();

    @Override
    public TraversalEvaluator __getCurrentTraversalEvaluator() {
        if (teStack.size() > 0)
            return teStack.peek();
        return null;
    }

    @Override
    public void __pushTraversalEvaluator(TraversalEvaluator te) {
        teStack.push(te);
    }

    @Override
    public TraversalEvaluator __popTraversalEvaluator() {
        return teStack.pop();
    }
    
    @Override
    public Map<String, String> completePartialIdentifier(String qualifier, String partialIdentifier) {
        if (partialIdentifier.startsWith("\\")) {
            partialIdentifier = partialIdentifier.substring(1);
        }
        
        String partialModuleName = qualifier + "::" + partialIdentifier;
        SortedMap<String, String> result = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                if (a.charAt(0) == '\\') {
                    a = a.substring(1);
                }
                if (b.charAt(0) == '\\') {
                    b = b.substring(1);
                }
                return a.compareTo(b);
            }
        });

        addCompletionsForModule(qualifier, partialIdentifier, partialModuleName, result, __getRootScope(), false);
        
        for (String mod: __getRootScope().getImports()) {
            addCompletionsForModule(qualifier, partialIdentifier, partialModuleName, result, __getRootScope().getImport(mod), true);
        }

        return result;
    }

    private void addCompletionsForModule(String qualifier, String partialIdentifier, String partialModuleName, SortedMap<String, String> result, ModuleEnvironment env, boolean skipPrivate) {
        for (Pair<String, List<AbstractFunction>> p : env.getFunctions()) {
            for (AbstractFunction f : p.getSecond()) {
                String module = ((ModuleEnvironment)f.getEnv()).getName();
                
                if (skipPrivate && env.isNamePrivate(f.getName())) {
                    continue;
                }
                
                if (module.startsWith(qualifier)) {
                    addCandidate(result, "function", p.getFirst(), qualifier.isEmpty() ? "" : module, module.startsWith(partialModuleName) ? "" : partialIdentifier);
                }
            }
        }
        boolean inQualifiedModule = env.getName().equals(qualifier) || qualifier.isEmpty();
        if (inQualifiedModule) {
            for (Entry<String, Result<IValue>> entry : env.getVariables().entrySet()) {
                if (skipPrivate && env.isNamePrivate(entry.getKey())) {
                    continue;
                }
                addCandidate(result, "variable", entry.getKey(), qualifier, partialIdentifier);
            }
            
            for (Type t: env.getAbstractDatatypes()) {
                if (inQualifiedModule) {
                    addCandidate(result, "ADT", t.getName(), qualifier, partialIdentifier);
                }
            }
            
            for (Type t: env.getAliases()) {
                addCandidate(result, "alias", t.getName(), qualifier, partialIdentifier);
            }
        }
        if (qualifier.isEmpty()) {
            Map<Type, Map<String, Type>> annos = env.getAnnotations();
            for (Type t: annos.keySet()) {
                for (String k: annos.get(t).keySet()) {
                    addCandidate(result, "annotation", k, "", partialIdentifier);
                }
            }
        }
    }

    private static void addCandidate(SortedMap<String, String> result, String category, String name, String qualifier, String originalTerm) {
        if (name.startsWith(originalTerm) && !name.equals(originalTerm)) {
            if (name.contains("-")) {
                name = "\\" + name;
            }
            if (!qualifier.isEmpty() && !name.startsWith(qualifier)) {
                name = qualifier + "::" + name;
            }
            result.put(name, category);
        }
    }

    @Override
    public Stack<IRascalFrame> getCurrentStack() {
        return getCallStack();
    }

    @Override
    public IRascalFrame getTopFrame() {
        return getCurrentEnvt();
    }

    @Override
    public ISourceLocation getCurrentPointOfExecution() {
        AbstractAST cpe = getCurrentAST();
        return cpe != null ? cpe.getLocation() : getCurrentEnvt().getLocation();
    }

    @Override
    public IRascalFrame getModule(String name) {
        return heap.getModule(name);
    }


    private static final class SaveWarningsMonitor implements IDEServices {

        private final List<String> warnings;
        private final IRascalMonitor monitor;
        private final PrintWriter stderr;
        
        public SaveWarningsMonitor(IRascalMonitor monitor, PrintWriter stderr) {
            this.monitor = monitor;
            this.warnings = new ArrayList<>();
            this.stderr = stderr;
        }

        @Override
        public PrintWriter stderr() {
            return stderr;
        }

        @Override
        public void jobStart(String name, int workShare, int totalWork) {
            monitor.jobStart(name, workShare, totalWork);
        }

        @Override
        public void jobStep(String name, String msg, int inc) {
            monitor.jobStep(name, msg, inc);
        }

        @Override
        public int jobEnd(String name, boolean succeeded) {
            return monitor.jobEnd(name, succeeded);
        }

        @Override
        public boolean jobIsCanceled(String name) {
            return monitor.jobIsCanceled(name);
        }

        @Override
        public void jobTodo(String name, int work) {
            monitor.jobTodo(name, work);
        }

        @Override
        public void endAllJobs() {
            monitor.endAllJobs();
        }

        @Override
        public void warning(String message, ISourceLocation src) {
            warnings.add(src + ":" + message);
            monitor.warning(message, src);
        }

        public void clear() {
            warnings.clear();
        }

        public List<String> getWarnings() {
            return warnings;
        }

        @Override
        public void registerLanguage(IConstructor language) {
            if (monitor instanceof IDEServices) {
                ((IDEServices) monitor).registerLanguage(language);
            }
            else {
                IDEServices.super.registerLanguage(language);
            }
        }

        @Override
        public ISourceLocation resolveProjectLocation(ISourceLocation input) {
            if (monitor instanceof IDEServices) {
                return ((IDEServices) monitor).resolveProjectLocation(input);
            }
            else {
                return IDEServices.super.resolveProjectLocation(input);
            }
        }

        @Override
        public void applyDocumentsEdits(IList edits) {
            if (monitor instanceof IDEServices) {
                ((IDEServices) monitor).applyDocumentsEdits(edits);
            }
            else {
                IDEServices.super.applyDocumentsEdits(edits);
            }
        }

        @Override
        public void browse(URI uri, String title, int viewColumn) {
            if (monitor instanceof IDEServices) {
                ((IDEServices) monitor).browse(uri, title, viewColumn);
            }
            else {
                return;
            }
        }

        @Override
        public void edit(ISourceLocation path) {
            if (monitor instanceof IDEServices) {
                ((IDEServices) monitor).edit(path);
            }
            else {
                return;
            }
        }

        @Override
        public void registerLocations(IString scheme, IString auth, IMap map) {
            if (monitor instanceof IDEServices) {
                ((IDEServices) monitor).registerLocations(scheme, auth, map);
            }
            else {
                return;
            }
        }

        @Override
        public void unregisterLocations(IString scheme, IString auth) {
            if (monitor instanceof IDEServices) {
                ((IDEServices) monitor).unregisterLocations(scheme, auth);
            }
            else {
                return;
            }
        }

        @Override
        public void registerDiagnostics(IList messages) {
            if (monitor instanceof IDEServices) {
                ((IDEServices) monitor).registerDiagnostics(messages);
            }
            else {
                return;
            }
        }

        @Override
        public void unregisterDiagnostics(IList resources) {
            if (monitor instanceof IDEServices) {
                ((IDEServices) monitor).unregisterDiagnostics(resources);
            }
            else {
                return;
            }
        }

        @Override
        public void logMessage(IConstructor msg) {
            if (monitor instanceof IDEServices) {
                ((IDEServices) monitor).logMessage(msg);
            }
            else {
                return;
            }
        }

        @Override
        public void showMessage(IConstructor message) {
            if (monitor instanceof IDEServices) {
                ((IDEServices) monitor).showMessage(message);
            }
            else {
                return;
            }
        }
    }


    public void overwritePrintWriter(PrintWriter outWriter, PrintWriter errWriter) {
        this.curOutWriter = outWriter;
        this.curErrWriter = errWriter;
    }
}
