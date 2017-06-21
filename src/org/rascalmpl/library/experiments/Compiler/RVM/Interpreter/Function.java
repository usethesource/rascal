package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Random;

import org.rascalmpl.interpreter.ITestResultListener;
import org.rascalmpl.interpreter.TypeReifier;
import org.rascalmpl.interpreter.result.util.MemoizationCache;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.CompilerIDs;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.IRVMWireInputStream;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.IRVMWireOutputStream;
import org.rascalmpl.values.ValueFactoryFactory;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.io.binary.util.WindowSizes;
import io.usethesource.vallang.io.binary.wire.IWireInputStream;
import io.usethesource.vallang.random.RandomValueGenerator;
import io.usethesource.vallang.random.util.TypeParameterBinder;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/**
 * Function contains all data needed for a single RVM function
 *
 * Function is serialized by write and read defined here
 */

public class Function {
    
	private static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	private static final IString ignoreTag = vf.string("ignore");
	private static final IString IgnoreTag = vf.string("Ignore");
	private static final IString ignoreCompilerTag = vf.string("ignoreCompiler");
	private static final IString IgnoreCompilerTag = vf.string("IgnoreCompiler");
	
	String name;
	public Type ftype;
	public Type kwType;
	int scopeId;
	String funIn;
	public int scopeIn = -1;
	public int nformals;
	private int nlocals;
	boolean isDefault;
	boolean isTest;
	boolean simpleArgs;
	IMap tags;
	int maxstack;
	public CodeBlock codeblock;
	public IValue[] constantStore;			
	public Type[] typeConstantStore;
	boolean concreteArg = false;
	int abstractFingerprint = 0;
	int concreteFingerprint = 0;

	int[] froms;
	int[] tos;
	public int[] types;
	int[] handlers;
	int[] fromSPs;
	int lastHandler = -1;

	public Integer funId; // USED in dynRun to find the function, in the JVM version only.

	public String[] fromLabels;
	public String[] toLabels;
    public String[] handlerLabels;
    public int[] fromSPsCorrected;
	
	public int continuationPoints = 0;
	
	boolean isCoroutine = false;
	int[] refs;

	boolean isVarArgs = false;

	public ISourceLocation src;			
	public IMap localNames;
	
	// transient fields 
	transient SoftReference<MemoizationCache<IValue>> memoization;

    private transient Class<?> javaClazz;
    private transient Method javaMethod;
    
    public MethodHandle handle;
	
	public Function(final String name, final Type ftype, final Type kwType, final String funIn, final int nformals, final int nlocals, boolean isDefault, boolean isTest, 
			 boolean simpleArgs, final IMap tags, final IMap localNames,
			final int maxstack, boolean concreteArg, int abstractFingerprint, int concreteFingerprint, final CodeBlock codeblock, final ISourceLocation src, int ctpt){
		this.name = name;
		this.ftype = ftype;
		this.kwType = (kwType == null) ? TypeFactory.getInstance().tupleEmpty() : kwType;
		this.funIn = funIn;
		this.nformals = nformals;
		this.setNlocals(nlocals);
		this.isDefault = isDefault;
		this.isTest = isTest;
		this.simpleArgs = simpleArgs;
		this.tags = (tags == null) ? ValueFactoryFactory.getValueFactory().mapWriter().done() : tags;
		this.localNames = localNames;
		this.maxstack = maxstack;
		this.concreteArg = concreteArg;
		this.abstractFingerprint = abstractFingerprint;
		this.concreteFingerprint = concreteFingerprint;
		this.codeblock = codeblock;
		this.src = src;
		this.continuationPoints = ctpt ;
	}
	
	Function(final String name, final Type ftype, final Type kwType, final String funIn, final int nformals, final int nlocals, boolean isDefault, boolean isTest, 
			 boolean simpleArgs, final IMap tags, final IMap localNames,
			final int maxstack, boolean concreteArg, int abstractFingerprint, int concreteFingerprint, final CodeBlock codeblock, final ISourceLocation src,
			int scopeIn, IValue[] constantStore, Type[] typeConstantStore, int[] froms, int[] tos, int[] types, int[] handlers,
			int[] fromSPs, int lastHandler, int scopeId, boolean isCoroutine, int[] refs, boolean isVarArgs, int ctpt){
		this.name = name;
		this.ftype = ftype;
        this.kwType = (kwType == null) ? TypeFactory.getInstance().tupleEmpty() : kwType;
		this.funIn = funIn;
		this.nformals = nformals;
		this.setNlocals(nlocals);
		this.isDefault = isDefault;
		this.isTest = isTest;
        this.tags = (tags == null) ? ValueFactoryFactory.getValueFactory().mapWriter().done() : tags;
		this.localNames = localNames;
		this.maxstack = maxstack;
		this.concreteArg = concreteArg;
		this.abstractFingerprint = abstractFingerprint;
		this.concreteFingerprint = concreteFingerprint;
		this.codeblock = codeblock;
		this.src = src;
		this.scopeIn = scopeIn;
		this.constantStore = constantStore;
		this.typeConstantStore = typeConstantStore;
		this.froms = froms;
		this.tos = tos;
		this.types = types;
		this.handlers = handlers;
		this.fromSPs = fromSPs;
		this.lastHandler = lastHandler;
		this.scopeId = scopeId;
		this.isCoroutine = isCoroutine;
		this.refs = refs;
		this.isVarArgs = isVarArgs;
		this.continuationPoints = ctpt ;
	}
	
	public void  finalize(final Map<String, Integer> functionMap, final Map<String, Integer> constructorMap, final Map<String, Integer> resolver){
		if(constructorMap == null){
			System.out.println("finalize: null");
		}
		codeblock.done(name, functionMap, constructorMap, resolver);
		this.scopeId = codeblock.getFunctionIndex(name);
		if(funIn.length() != 0) {
			this.scopeIn = codeblock.getFunctionIndex(funIn);
		}
		this.constantStore = codeblock.getConstants();
		this.typeConstantStore = codeblock.getTypeConstants();
	}
	
	public void clearForJVM(){
		codeblock.clearForJVM();
	}
	
	public void attachExceptionTable(final IList exceptions) {
			froms = new int[exceptions.length()];
			tos = new int[exceptions.length()];
			types = new int[exceptions.length()];
			handlers = new int[exceptions.length()];
			fromSPs = new int[exceptions.length()];
			fromSPsCorrected = new int[exceptions.length()];

			fromLabels = new String[exceptions.length()];
			toLabels = new String[exceptions.length()];
			handlerLabels = new String[exceptions.length()];
					
			int i = 0;
			for(IValue entry : exceptions) {
				ITuple tuple = (ITuple) entry;
				String from = ((IString) tuple.get(0)).getValue();
				String to = ((IString) tuple.get(1)).getValue();
				Type type = new TypeReifier(vf).symbolToType((IConstructor) tuple.get(2));
				String handler = ((IString) tuple.get(3)).getValue();
				int fromSP =  ((IInteger) tuple.get(4)).intValue();
				
				froms[i] = codeblock.getLabelPC(from);
				tos[i] = codeblock.getLabelPC(to);
				types[i] = codeblock.getTypeConstantIndex(type);
				handlers[i] = codeblock.getLabelPC(handler);	
				fromSPs[i] = fromSP;
				fromSPsCorrected[i] = fromSP + getNlocals();
				fromLabels[i] = from;
				toLabels[i] = to;
				handlerLabels[i] = handler;			

				i++;
			}
	}
	
	public int getHandler(final int pc, final Type type) {
		int i = 0;
		lastHandler = -1;
		for(int from : froms) {
			if(pc >= from) {
				if(pc < tos[i]) {
					// In the range...
					if(type.isSubtypeOf(codeblock.getConstantType(types[i]))) {
						lastHandler = i;
						return handlers[i];
					}
				}
			}
			i++;
		}
		return -1;
	}
	
	public int getFromSP(){
		return getNlocals() + fromSPs[lastHandler];
	}
	
	public String getName() {
		return name;
	}
	
	public int getNlocals() {
		return nlocals;
	}

	public void setNlocals(int nlocals) {
		this.nlocals = nlocals;
	}

	public String getPrintableName(){
	    int comp = name.lastIndexOf("::companion");
	    if(comp >= 0){
	      String tmpName = name.substring(0, comp);
	      int from = tmpName.lastIndexOf("::")+2;
	      int to = tmpName.indexOf("(", from);
	      return tmpName.substring(from, to);
	    }
		int from = name.lastIndexOf("/")+1;
		int to = name.indexOf("(", from);
		if(to < 0){
			to = name.length();
		}
		return name.substring(from, to);
	}
	
	public String getQualifiedName(){
		return name.substring(0, name.indexOf("("));
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FUNCTION ").append(name).append(" ->> ").append(ftype).append("\n");
		if(kwType.getArity() > 0){
		  sb.append("kwType: " + kwType);
		}
		for(int i = 0; i < constantStore.length; i++){
			sb.append("\t constant "). append(i).append(": "). append(constantStore[i]).append("\n");
		}
		for(int i = 0; i < typeConstantStore.length; i++){
			sb.append("\t type constant "). append(i).append(": "). append(typeConstantStore[i]).append("\n");
		}
		sb.append(codeblock.toString());
		return sb.toString();
	}
	
	public boolean isIgnored(){
	  return    tags.containsKey(ignoreTag) 
	         || tags.containsKey(IgnoreTag)
	         || tags.containsKey(ignoreCompilerTag)
	         || tags.containsKey(IgnoreCompilerTag)
	         ;
	}
	
	private static final int MAXDEPTH = 5;
	private static final int MAXWIDTH = 5;
    private static final int TRIES = 500;
    
    public int getTries(){
      if(ftype.getFieldTypes().getArity() == 0){
        return 1;
      }
      IValue itries = tags.get(vf.string("tries"));
      return itries == null ? TRIES : Integer.parseInt(((IString) itries).getValue());
    }
    
    public int getDepth(){
      IValue imaxDepth = tags.get(vf.string("maxDepth"));
      return imaxDepth == null ? MAXDEPTH : Integer.parseInt(((IString) imaxDepth).getValue());
    }
    
    public int getWidth(){
        IValue imaxWidth = tags.get(vf.string("maxWidth"));
        return imaxWidth == null ? MAXWIDTH : Integer.parseInt(((IString) imaxWidth).getValue());
    }
	
    /**
     * Execute current function as test
     * @param testResultListener TODO
     * @param typeStore TODO
     * @param rex TODO
     **/

    public ITuple executeTest(ITestResultListener testResultListener, TypeStore typeStore, RascalExecutionContext rex) {
      String fun = name;
      if(isIgnored()){
        testResultListener.ignored(computeTestName(), src);
        return vf.tuple(src,  vf.integer(2), vf.string(""));
      }
      
      IValue iexpected =  tags.get(vf.string("expected"));
      String expected = iexpected == null ? "" : ((IString) iexpected).getValue();
      
      int maxDepth = getDepth();
      int maxWidth = getWidth();
      int tries = getTries();

      Type requestedType = ftype.getFieldTypes();
      int nargs = requestedType.getArity();
      IValue[] args = new IValue[nargs];

      Map<Type, Type> tpbindings = new TypeParameterBinder().bind(requestedType);
      RandomValueGenerator randomValue = new RandomValueGenerator(vf, new Random(), maxDepth, maxWidth);
      //RandomValueTypeVisitor randomValue = new RandomValueTypeVisitor(vf, maxDepth, tpbindings, typeStore);

      boolean passed = true;
      String message = "";
      Throwable exception = null;
      for(int i = 0; i < tries; i++){
        if(nargs > 0){
          message = "test fails for arguments: ";
          for(int j = 0; j < nargs; j++){
            args[j] = randomValue.generate(requestedType.getFieldType(j), typeStore, tpbindings);
            message = message + args[j].toString() + " ";
          }
        }
        try {
   
          IValue res = (IValue) rex.getRVM().executeRVMFunction(fun, args, null); 
          passed = ((IBool) res).getValue();
          if(!passed){
            break;
          }
        } catch (Thrown e){
          String ename;
          if(e.getValue() instanceof IConstructor){
            ename = ((IConstructor) e.getValue()).getName();
          } else {
            ename = e.toString();
          }
          if(!ename.equals(expected)){
            message = e.toString() + message;
            passed = false;
            exception = e;
            break;
          }
        }
        catch (Exception e){
          message = e.getMessage() + message;
          passed = false;
          break;
        }
      }
      if(passed)
        message = "";

      testResultListener.report(passed, computeTestName(), src, message, exception);
      return vf.tuple(src,  vf.integer(passed ? 1 : 0), vf.string(message));
    }
    
    public String computeTestName(){    // Resembles Function.getPrintableName
      String base = name;
      int colons = name.lastIndexOf("::");
      if(colons > 0){
        base = name.substring(colons+2, name.indexOf("(")).replaceAll("/",  "::"); 
      } else {
        base = name.substring(name.indexOf("/")+1, name.indexOf("(")); 
      }
      return base + ": <" + src.getOffset() +"," + src.getLength() +">";
    }
    
    public static void printTypeStore(TypeStore ts){
      for(Type adt : ts.getAbstractDataTypes()){
        System.err.println("adt: " + adt);
      }
      for(Type cons : ts.getConstructors()){
        System.err.println("cons: " + cons);
      }
    }
    
    public void write(IRVMWireOutputStream out) throws IOException{
        out.startMessage(CompilerIDs.Function.ID);
       
        out.writeField(CompilerIDs.Function.NAME, name);

        out.writeField(CompilerIDs.Function.FTYPE, ftype, WindowSizes.TINY_WINDOW);

        out.writeField(CompilerIDs.Function.KWTYPE, kwType, WindowSizes.TINY_WINDOW);

        out.writeField(CompilerIDs.Function.SCOPE_ID, scopeId);

        out.writeField(CompilerIDs.Function.FUN_IN, funIn);

        out.writeField(CompilerIDs.Function.SCOPE_IN, scopeIn);

        out.writeField(CompilerIDs.Function.NFORMALS, nformals);

        out.writeField(CompilerIDs.Function.NLOCALS, getNlocals());

        if(isDefault){ 
            out.writeField(CompilerIDs.Function.IS_DEFAULT, 1); 
        }
        
        if(isTest){
            out.writeField(CompilerIDs.Function.IS_TEST, 1);
        }
        
        if(simpleArgs){
            out.writeField(CompilerIDs.Function.SIMPLEARGS, 1);
        }
        
        if(tags != null){
            out.writeField(CompilerIDs.Function.TAGS, tags, WindowSizes.TINY_WINDOW);
        }

        out.writeField(CompilerIDs.Function.MAX_STACK, maxstack);

        out.writeNestedField(CompilerIDs.Function.CODEBLOCK);
        codeblock.write(out);
        
        out.writeField(CompilerIDs.Function.CONSTANT_STORE, constantStore);

        out.writeField(CompilerIDs.Function.TYPE_CONSTANT_STORE, typeConstantStore, WindowSizes.TINY_WINDOW);

        if(concreteArg){
            out.writeField(CompilerIDs.Function.CONCRETE_ARG, 1);
        }

        out.writeField(CompilerIDs.Function.ABSTRACT_FINGERPRINT, abstractFingerprint);

        out.writeField(CompilerIDs.Function.CONCRETE_FINGERPRINT, concreteFingerprint);

        out.writeField(CompilerIDs.Function.FROMS, froms);

        out.writeField(CompilerIDs.Function.TOS, tos);

        out.writeField(CompilerIDs.Function.TYPES, types);

        out.writeField(CompilerIDs.Function.HANDLERS, handlers);

        out.writeField(CompilerIDs.Function.FROM_SPS, fromSPs);

        out.writeField(CompilerIDs.Function.LAST_HANDLER, lastHandler);
        
        out.writeField(CompilerIDs.Function.FUN_ID, funId.intValue()); // Why Integer and not int?

        if(isCoroutine){
            out.writeField(CompilerIDs.Function.IS_COROUTINE, 1);
        }

        if (refs != null) {
            out.writeField(CompilerIDs.Function.REFS, refs);
        }

        if(isVarArgs){
            out.writeField(CompilerIDs.Function.IS_VARARGS, 1);
        }

        out.writeField(CompilerIDs.Function.SRC, src, WindowSizes.NO_WINDOW);

        out.writeField(CompilerIDs.Function.LOCAL_NAMES, localNames, WindowSizes.TINY_WINDOW);

        out.writeField(CompilerIDs.Function.CONTINUATION_POINTS, continuationPoints);
        
        out.endMessage();
    }
    
    static Function read(IRVMWireInputStream in, Map<String, Integer> functionMap, Map<String, Integer> constructorMap, Map<String, Integer> resolver) throws IOException {    
        String name = "unitialized name";
        Type ftype = null;
        Type kwType = null;
        int scopeId = 0;
        String funIn = "unitialized funIn";
        int scopeIn = -1;
        int nformals = 0;
        int nlocals = 0;
        boolean isDefault = false;
        boolean isTest = false;
        boolean simpleArgs = false;
        
        IMap emptyIMap = vf.mapWriter().done();
        IMap tags = emptyIMap;
        int maxstack = 0;
        CodeBlock codeblock = null;
        IValue[] constantStore = new IValue[0];          
        Type[] typeConstantStore = new Type[0];
        boolean concreteArg = false;
        int abstractFingerprint = 0;
        int concreteFingerprint = 0;

        int[] froms = new int[0];
        int[] tos = new int[0];
        int[] types = new int[0];
        int[] handlers = new int[0];
        int[] fromSPs = new int[0];
        int lastHandler = -1;

        Integer funId = -1; // USED in dynRun to find the function, in the JVM version only.
        
        int continuationPoints = 0;
        
        boolean isCoroutine = false;
        int[] refs = null;

        boolean isVarArgs = false;

        ISourceLocation src = vf.sourceLocation("uninitialized/src");         
        IMap localNames = emptyIMap;
        
        in.next();
        assert in.current() == IWireInputStream.MESSAGE_START;
        if(in.message() != CompilerIDs.Function.ID){
            throw new IOException("Unexpected message: " + in.message());
        }
        while(in.next() != IWireInputStream.MESSAGE_END){
            switch(in.field()){
                
                case CompilerIDs.Function.NAME: {
                    name = in.getString(); 
                    break;
                }
                
                case CompilerIDs.Function.FTYPE: {
                    ftype = in.readType();
                    break;
                }
                
                case CompilerIDs.Function.KWTYPE: {
                    kwType = in.readType();
                    break;
                }
                
                case CompilerIDs.Function.SCOPE_ID: {
                    scopeId = in.getInteger();
                    break;
                }
                
                case CompilerIDs.Function.FUN_IN : {
                    funIn = in.getString();
                    break;
                }
                
                case CompilerIDs.Function.SCOPE_IN: {
                    scopeIn = in.getInteger();
                    break;
                }
                
                case CompilerIDs.Function.NFORMALS: {
                    nformals = in.getInteger();
                    break;
                }
                
                case CompilerIDs.Function.NLOCALS: {
                    nlocals = in.getInteger();
                    break;
                }
                
                case CompilerIDs.Function.IS_DEFAULT: {
                    int n = in.getInteger();
                    isDefault = n == 1 ? true : false;
                    break;
                }
                
                case CompilerIDs.Function.IS_TEST: {
                    int n = in.getInteger();
                    isTest = n == 1 ? true : false;
                    break;
                }
                
                case CompilerIDs.Function.SIMPLEARGS: {
                    int n = in.getInteger();
                    simpleArgs = n == 1 ? true : false;
                    break;
                }
                
                case CompilerIDs.Function.TAGS: {
                    tags = in.readIValue();
                    break;
                }
                
                case CompilerIDs.Function.MAX_STACK: {
                    maxstack = in.getInteger();
                    break;
                }
                    
                case CompilerIDs.Function.CODEBLOCK: {
                    codeblock = CodeBlock.read(in, functionMap, constructorMap, resolver);
                    break;
                }
                
                case CompilerIDs.Function.CONSTANT_STORE: {
                    constantStore = in.readIValues();
                    break;
                }
                
                case CompilerIDs.Function.TYPE_CONSTANT_STORE: {
                    typeConstantStore = in.readTypes();
                    break;
                }
                
                case CompilerIDs.Function.CONCRETE_ARG: {
                    int n = in.getInteger();
                    concreteArg = n == 1 ? true : false;
                    break;
                }
                
                case CompilerIDs.Function.ABSTRACT_FINGERPRINT:{
                    abstractFingerprint = in.getInteger();
                    break;
                }
                
                case CompilerIDs.Function.CONCRETE_FINGERPRINT:{
                    concreteFingerprint = in.getInteger();
                    break;
                }
                
                case CompilerIDs.Function.FROMS:{
                    froms = in.getIntegers();
                    break;
                }
                
                case CompilerIDs.Function.TOS: {
                    tos = in.getIntegers();
                    break;
                }
                
                case CompilerIDs.Function.TYPES: {
                    types = in.getIntegers();
                    break;
                }
                
                case CompilerIDs.Function.HANDLERS: {
                    handlers = in.getIntegers();
                    break;
                }
                
                case CompilerIDs.Function.FROM_SPS:{
                    fromSPs = in.getIntegers();
                    break;
                }
                
                case CompilerIDs.Function.LAST_HANDLER: {
                    lastHandler = in.getInteger();
                    break;
                }
                
                case CompilerIDs.Function.FUN_ID: {
                    funId = in.getInteger();
                    break;
                }
                
                case CompilerIDs.Function.IS_COROUTINE: {
                    int n = in.getInteger();
                    isCoroutine = n == 1 ? true : false;
                    break;
                }
                
                case CompilerIDs.Function.REFS: {
                    refs = in.getIntegers();
                    break;
                }
                
                case CompilerIDs.Function.IS_VARARGS: {
                    int n = in.getInteger();
                    isVarArgs = n == 1 ? true : false;
                    break;
                }
                
                case CompilerIDs.Function.SRC: {
                    src = in.readIValue();
                    break;
                }
                
                case CompilerIDs.Function.LOCAL_NAMES:{
                    localNames = in.readIValue();
                    break;
                }
                
                case CompilerIDs.Function.CONTINUATION_POINTS: {
                    continuationPoints = in.getInteger();
                    break;
                }
                
                default: {
                    System.err.println("Function.read, skips " + in.field());
                    // skip field, normally next takes care of it
                    in.skipNestedField();
                }
            }
        }
     
        // TODO: check fields are valid
        
        Function func = new Function(name, ftype, kwType, funIn, nformals, nlocals, isDefault, isTest, simpleArgs, tags, localNames, maxstack, 
            concreteArg, abstractFingerprint, concreteFingerprint, codeblock, src,
            scopeIn, constantStore, typeConstantStore, froms, tos, types, handlers,
            fromSPs, lastHandler, scopeId, isCoroutine, refs, isVarArgs, continuationPoints);
        func.funId = funId;
        return func;
    }

    public Class<?> getJavaClass() {
        return javaClazz;
    }

    public Method getJavaMethod() {
        return javaMethod;
    }

    public void setJavaMetaObjects(Class<?> clazz, Method method) {
        assert javaClazz == null && javaMethod == null;
        this.javaClazz = clazz;
        this.javaMethod = method;
    }
}