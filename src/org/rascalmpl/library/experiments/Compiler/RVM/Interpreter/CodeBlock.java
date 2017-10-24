package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.*;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.CompilerIDs;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.IRVMWireInputStream;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.serialize.IRVMWireOutputStream;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.io.binary.util.WindowSizes;
import io.usethesource.vallang.io.binary.wire.IWireInputStream;
import io.usethesource.vallang.type.Type;

/**
 * CodeBlock contains all instructions needed for a single RVM function
 *
 * CodeBlock is serialized by write and read defined here
 */
public class CodeBlock  {
	
	// Transient fields
	transient public IValueFactory vf;
	//transient private static TypeSerializer typeserializer;
	transient int pc;
	transient int labelIndex = 0;
	transient private ArrayList<Instruction> insList;
	transient private HashMap<String, LabelInfo> labelInfo;
	
	// Serializable fields
	String name;
	
	private Map<IValue, Integer> constantMap;
	private ArrayList<IValue> constantStore;
	IValue[] finalConstantStore;
	
	private Map<Type, Integer> typeConstantMap;
	private ArrayList<Type> typeConstantStore;
	protected Type[] finalTypeConstantStore;
	
	Map<String, Integer> functionMap;
	Map<String, Integer> resolver;
	Map<String, Integer> constructorMap;
	
	public long[] finalCode;
	
	CodeBlock(String name, Map<IValue, Integer> constantMap, ArrayList<IValue> constantStore, IValue[] finalConstantStore,
			Map<Type, Integer> typeConstantMap, ArrayList<Type> typeConstantStore, Type[] finalTypeConstantStore,
			Map<String, Integer> functionMap, Map<String, Integer> resolver, Map<String, Integer> constructorMap, long[] finalCode
			){
			this.name = name;	
			this.constantMap = constantMap;
			this.constantStore = constantStore;
			this.finalConstantStore = finalConstantStore;
			this.typeConstantMap = typeConstantMap;
			this.typeConstantStore = typeConstantStore;
			this.finalTypeConstantStore = finalTypeConstantStore;
			this.functionMap = functionMap;
			this.resolver = resolver;
			this.constructorMap = constructorMap;
			this.finalCode = finalCode;
	}
	
	public CodeBlock(String name, IValueFactory factory){
		labelInfo = new HashMap<String, LabelInfo>();
		insList = new ArrayList<Instruction>();
		pc = 0;
		this.name = name;
		this.vf = factory;
		constantMap = new HashMap<IValue, Integer>();
		this.constantStore = new ArrayList<IValue>();
		this.typeConstantMap = new HashMap<Type, Integer>();
		this.typeConstantStore = new ArrayList<Type>();
	}
	
	public void defLabel(String label, Instruction ins){
		LabelInfo info = labelInfo.get(label);
		if(info == null){
			labelInfo.put(label, new LabelInfo(ins, labelIndex++, pc));
		} else {
			if(info.isResolved()){
				throw new InternalCompilerError("In function " + name + ": double declaration of label " + label);
			}
			info.instruction = ins;
			info.PC = pc;
		}
	}
	
	protected int useLabel(String label){
		LabelInfo info = labelInfo.get(label);
		if(info == null){
			info = new LabelInfo(labelIndex++);
			labelInfo.put(label, info);
		}
		return info.index;
	}
	
	public int getLabelPC(String label){
		LabelInfo info = labelInfo.get(label);
		if(info == null){
			throw new InternalCompilerError("In function " + name + " undefined label " + label);
		}
		return info.PC;
	}
	
	public Instruction getLabelInstruction(String label){
		LabelInfo info = labelInfo.get(label);
		if(info == null){
			throw new InternalCompilerError("In function " + name + ": undefined label " + label);
		}
		return info.instruction;
	}
	
	public IValue getConstantValue(long finalCode2){
	    if(finalCode2 < constantStore.size()){
	        return constantStore.get((int) finalCode2);
	    }
//		for(IValue constant : constantMap.keySet()){
//			if(constantMap.get(constant) == finalCode2){
//				return constant;
//			}
//		}
		throw new InternalCompilerError("In function " + name + ": undefined constant index " + finalCode2);
	}
	
	public int getConstantIndex(IValue v){
		Integer n = constantMap.get(v);
		if(n == null){
			n = constantStore.size();
			constantStore.add(v);
			constantMap.put(v,  n);
		}
		return n;
	}
	
	public Type getConstantType(int n){
	    if(n < typeConstantStore.size()){
	        return typeConstantStore.get(n);
	    } else 
//		for(Type type : typeConstantMap.keySet()){
//			if(typeConstantMap.get(type) == n){
//				return type;
//			}
//		}
		throw new InternalCompilerError("In function " + name + ": undefined type constant index " + n);
	}
	
	public int getTypeConstantIndex(Type type){
		Integer n = typeConstantMap.get(type);
		if(n == null){
			n = typeConstantStore.size();
			typeConstantStore.add(type);
			typeConstantMap.put(type, n);
		}
		return n;
	}
	
	public String getFunctionName(int n){
		for(String fname : functionMap.keySet()){
			if(functionMap.get(fname) == n){
				return fname;
			}
		}
		throw new InternalCompilerError("In function " + name + ": undefined function index " + n);
	}
	
	public String getFunctionName(String name){
		int n = getFunctionIndex(name);
		for(String fname : functionMap.keySet()){
			if(functionMap.get(fname) == n){
				return fname;
			}
		}
		throw new InternalCompilerError("In function " + name + ": undefined function index " + n);
	}
	
	public int getFunctionIndex(String name){
		Integer n = functionMap.get(name);
		if(n == null){
			throw new InternalCompilerError("In function " + name + ": undefined function name " + name);
		}
		return n;
	}
	
	public String getOverloadedFunctionName(int n){
		for(String fname : resolver.keySet()){
			if(resolver.get(fname) == n) {
				return fname;
			}
		}
		throw new InternalCompilerError("In function " + name + ": undefined overloaded function index " + n);
	}
	
	public int getOverloadedFunctionIndex(String name){
		Integer n = resolver.get(name);
		if(n == null){
			return getFunctionIndex(name);
			//throw new CompilerError("In function " + name + ": undefined overloaded function name " + name);
		}
		return n;
	}
	
	public String getConstructorName(int n) {
		for(String cname : constructorMap.keySet()) {
			if(constructorMap.get(cname) == n)
				return cname;
		}
		throw new InternalCompilerError("In function " + name + ": undefined constructor index " + n);
	}
	
	public int getConstructorIndex(String name) {
		Integer n = constructorMap.get(name);
		if(n == null)
			throw new InternalCompilerError("In function " + name + ": undefined constructor name " + name);
		return n;
	}
	
	public String getModuleVarName(int n){
		return ((IString)getConstantValue(n)).getValue();
	}
	
	public int getModuleVarIndex(String name){
		return getConstantIndex(vf.string(name));		
	}
	
	CodeBlock add(Instruction ins){
		insList.add(ins);
		pc += ins.pcIncrement();
		return this;
	}
	
	public void addCode(long c){
		finalCode[pc++] = c;
	}
	
	public void addCode0(int op){
		finalCode[pc++] = op;
	} 
	
	public void addCode1(int op, int arg1){
		finalCode[pc++] = encode1(op, arg1);
	}
	
	public void addCode2(int op, int arg1, int arg2){
		finalCode[pc++] = encode2(op, arg1, arg2);
	}
	
	/*
	 * Instruction encoding:
	 * 
	 * 	   argument2    argument1       op
	 *  |-----------| |-----------| |---------|
	 *  sizeArg2 bits sizeArg1 bits sizeOp bits
	 */
	
	public final static int sizeOp = 8;
	public final static int sizeArg1 = 28;
	public final static int sizeArg2 = 28;
	public final static long maskOp = (1L << sizeOp) - 1;
	public final static long maskArg1 = (1L << sizeArg1) - 1;
	public final static long maskArg2 = (1L << sizeArg2) - 1;
	public final static int shiftArg1 = sizeOp;
	public final static int shiftArg2 = sizeOp + sizeArg1;

	public final static int maxArg1 = (int) ((1L << sizeArg1) - 1);
	public final static int maxArg2 = (int) ((1L << sizeArg2) - 1);
	public final static int maxArg = Math.min(maxArg1,maxArg2);

	public final static long encode0(int op){
		return op;
	}
	
	public final static long encode1(int op, int arg1){
		assert arg1 < (1L << sizeArg1);
		long larg1 = arg1;
		return (larg1 << shiftArg1) | op;
	}
	
	public final static long encode2(int op, int arg1, int arg2){
		assert arg1 < (1L << sizeArg1) && arg2 < (1L << sizeArg2);
		long larg1 = arg1;
		long larg2 = arg2;
		return (larg2 << shiftArg2) | (larg1 << shiftArg1) | op;
	}
	
	public final static int fetchOp(long instruction){
		return (int) (instruction & maskOp);
	}
	
	public final static int fetchArg1(long instruction){
		return (int) ((instruction >> shiftArg1) & maskArg1);
	}
	
	public final static int fetchArg2(long instruction){
		return (int) ((instruction >> shiftArg2) & maskArg2);
	}
	
	public final static boolean isMaxArg1(int arg){
		return arg == maskArg1;
	}
	
	public final static boolean isMaxArg2(int arg){
		return arg == maskArg2;
	}
	
	/*
	 * All Instructions
	 */
	
	public CodeBlock POP(){
		return add(new Pop(this));
	}
	
	public CodeBlock HALT(){
		return add(new Halt(this));
	}
	
	public CodeBlock RETURN0() {
		return add(new Return0(this));
	}
	
	public CodeBlock RETURN1(){
		return add(new Return1(this));
	}
	
	public CodeBlock CORETURN0() {
		return add(new CoReturn0(this));
	}
	
	public CodeBlock CORETURN1(int arity){
		return add(new CoReturn1(this,arity));
	}
	
	public CodeBlock LABEL(String arg){
		return add(new Label(this, arg));
	}
	
	public CodeBlock LOADCON(boolean arg){
		return add(new LoadCon(this, getConstantIndex(vf.bool(arg))));
	}
	
	public CodeBlock LOADCON(int arg){
		return add(new LoadCon(this, getConstantIndex(vf.integer(arg))));
	}
	
	public CodeBlock LOADCON(String arg){
		return add(new LoadCon(this, getConstantIndex(vf.string(arg))));
	}
	
	public CodeBlock LOADCON(IValue val){
		return add(new LoadCon(this, getConstantIndex(val)));
	}
	
	public CodeBlock PUSHCON(IValue val){
		return add(new PushCon(this, getConstantIndex(val)));
	}
	
	public CodeBlock LOADBOOL(boolean bool){
		return add(new LoadBool(this, bool));
	}
	
	public CodeBlock LOADINT(int n){
		return add(new LoadInt(this, n));
	}
	
	public CodeBlock CALL(String fuid, int arity,int ctpt){
		return add(new Call(this, fuid, arity, ctpt));
	}
	
	public CodeBlock JMP(String arg){
		return add(new Jmp(this, arg));
	}
	
	public CodeBlock JMPTRUE(String arg){
		return add(new JmpTrue(this, arg));
	}
	
	public CodeBlock JMPFALSE(String arg){
		return add(new JmpFalse(this, arg));
	}
	
	public CodeBlock LOADLOC (int pos){
		switch(pos){
		case 0: return add(new LoadLoc0(this));
		case 1: return add(new LoadLoc1(this));
		case 2: return add(new LoadLoc2(this));
		case 3: return add(new LoadLoc3(this));
		case 4: return add(new LoadLoc4(this));
		case 5: return add(new LoadLoc5(this));
		case 6: return add(new LoadLoc6(this));
		case 7: return add(new LoadLoc7(this));
		case 8: return add(new LoadLoc8(this));
		case 9: return add(new LoadLoc9(this));
		default:
			return add(new LoadLoc(this, pos));
		}
	}
	
	public CodeBlock PUSHLOC (int pos){
		return add(new PushLoc(this, pos));
	}
	
	public CodeBlock RESETLOC (int pos){
		return add(new ResetLoc(this, pos));
	}
	
	public CodeBlock STORELOC (int pos){
		return add(new StoreLoc(this, pos));
	}
	
	public CodeBlock LOADVAR(String fuid, int pos){
		if(pos == -1){
			getConstantIndex(vf.string(fuid));
		}
		return add(new LoadVar(this, fuid, pos));
	}
	
	public CodeBlock PUSHVAR(String fuid, int pos){
		if(pos == -1){
			getConstantIndex(vf.string(fuid));
		}
		return add(new PushVar(this, fuid, pos));
	}
	
	public CodeBlock STOREVAR (String fuid, int pos){
		if(pos == -1){
			getConstantIndex(vf.string(fuid));
		}
		return add(new StoreVar(this, fuid, pos));
	}
	
	public CodeBlock RESETVAR (String fuid, int pos){
		if(pos == -1){
			getConstantIndex(vf.string(fuid));
		}
		return add(new ResetVar(this, fuid, pos));
	}
	
	public CodeBlock CALLMUPRIM0 (MuPrimitive muprim){
		return add(new CallMuPrim0(this, muprim));
	}
	public CodeBlock PUSHCALLMUPRIM0 (MuPrimitive muprim){
		return add(new PushCallMuPrim0(this, muprim));
	}
	
	public CodeBlock CALLMUPRIM1 (MuPrimitive muprim){
		return add(new CallMuPrim1(this, muprim));
	}
	
	public CodeBlock PUSHCALLMUPRIM1 (MuPrimitive muprim){
		return add(new PushCallMuPrim1(this, muprim));
	}
	
	public CodeBlock CALLMUPRIM2 (MuPrimitive muprim){
		return add(new CallMuPrim2(this, muprim));
	}
	
	public CodeBlock PUSHCALLMUPRIM2 (MuPrimitive muprim){
		return add(new PushCallMuPrim2(this, muprim));
	}
	
	public CodeBlock CALLMUPRIMN (MuPrimitive muprim, int arity){
		return add(new CallMuPrimN(this, muprim, arity));
	}
	
	public CodeBlock PUSHCALLMUPRIMN (MuPrimitive muprim, int arity){
		return add(new PushCallMuPrimN(this, muprim, arity));
	}
	
	public CodeBlock CALLPRIM0 (RascalPrimitive prim, ISourceLocation src){
		return add(new CallPrim0(this, prim, src));
	}
	
	public CodeBlock PUSHCALLPRIM0 (RascalPrimitive prim, ISourceLocation src){
		return add(new PushCallPrim0(this, prim, src));
	}
	
	public CodeBlock CALLPRIM1 (RascalPrimitive prim, ISourceLocation src){
		return add(new CallPrim1(this, prim, src));
	}
	
	public CodeBlock PUSHCALLPRIM1 (RascalPrimitive prim, ISourceLocation src){
		return add(new PushCallPrim1(this, prim, src));
	}
	
	public CodeBlock CALLPRIM2 (RascalPrimitive prim, ISourceLocation src){
		return add(new CallPrim2(this, prim, src));
	}
	
	public CodeBlock PUSHCALLPRIM2 (RascalPrimitive prim, ISourceLocation src){
		return add(new PushCallPrim2(this, prim, src));
	}
	
	public CodeBlock CALLPRIMN (RascalPrimitive prim, int arity, ISourceLocation src){
		return add(new CallPrimN(this, prim, arity, src));
	}
	
	public CodeBlock PUSHCALLPRIMN (RascalPrimitive prim, int arity, ISourceLocation src){
		return add(new PushCallPrimN(this, prim, arity, src));
	}
	
	public CodeBlock PUSH_ROOT_FUN (String fuid){
		return add(new PushRootFun(this, fuid));
	}
	
	public CodeBlock CALLDYN(int arity, int ctpt){
		return add(new CallDyn(this, arity, ctpt));
	}
	
	public CodeBlock CREATE(String fuid, int arity) {
		return add(new Create(this, fuid, arity));
	}
	
	public CodeBlock CREATEDYN(int arity) {
		return add(new CreateDyn(this, arity));
	}
	
	public CodeBlock NEXT0() {
		return add(new Next0(this));
	}
	
	public CodeBlock NEXT1() {
		return add(new Next1(this));
	}
	
	public CodeBlock YIELD0(int ctpt) {
		return add(new Yield0(this, ctpt));
	}
	
	public CodeBlock YIELD1(int arity, int ctpt) {
		return add(new Yield1(this, arity, ctpt));
	}
	
	public CodeBlock PRINTLN(int arity){
		return add(new Println(this, arity));
	}
    
	public CodeBlock LOADLOCREF(int pos) {
		return add(new LoadLocRef(this, pos));
	}
	
	public CodeBlock PUSHLOCREF(int pos) {
		return add(new PushLocRef(this, pos));
	}
	
	public CodeBlock LOADVARREF(String fuid, int pos) {
		return add(new LoadVarRef(this, fuid, pos));
	}
	
	public CodeBlock PUSHVARREF(String fuid, int pos) {
		return add(new PushVarRef(this, fuid, pos));
	}
	
	public CodeBlock LOADLOCDEREF(int pos) {
		return add(new LoadLocDeref(this, pos));
	}
	
	public CodeBlock PUSHLOCDEREF(int pos) {
		return add(new PushLocDeref(this, pos));
	}
	
	public CodeBlock LOADVARDEREF(String fuid, int pos) {
		return add(new LoadVarDeref(this, fuid, pos));
	}
	
	public CodeBlock PUSHVARDEREF(String fuid, int pos) {
		return add(new PushVarDeref(this, fuid, pos));
	}
	
	public CodeBlock STORELOCDEREF(int pos) {
		return add(new StoreLocDeref(this, pos));
	}
	
	public CodeBlock STOREVARDEREF(String fuid, int pos) {
		return add(new StoreVarDeref(this, fuid, pos));
	}
	
	public CodeBlock PUSHCONSTR(String name) {
		return add(new PushConstr(this, name));
	}
	
	public CodeBlock CALLCONSTR(String name, int arity/*, ISourceLocation src*/) {
		return add(new CallConstr(this, name, arity/*, src*/));
	}
	
	public CodeBlock PUSHNESTEDFUN(String fuid, String scopeIn) {
		return add(new PushNestedFun(this, fuid, scopeIn));
	}
	
	public CodeBlock LOADTYPE(Type type) {
		return add(new LoadType(this, getTypeConstantIndex(type)));
	}
	
	public CodeBlock PUSHTYPE(Type type) {
		return add(new PushType(this, getTypeConstantIndex(type)));
	}
	
	public CodeBlock FAILRETURN(){
		return add(new FailReturn(this));
	}
	
	public CodeBlock PUSHOFUN(String fuid) {
		return add(new PushOFun(this, fuid));
	}
	
	public CodeBlock OCALL(String fuid, int arity, ISourceLocation src) {
		return add(new OCall(this, fuid, arity, src));
	}
	
	public CodeBlock OCALLDYN(Type types, int arity, ISourceLocation src) {
		return add(new OCallDyn(this, getTypeConstantIndex(types), arity, src));
	}
	
	public CodeBlock CALLJAVA(String methodName, String className, Type parameterTypes, Type keywordTypes, int reflect){
		return add(new CallJava(this, getConstantIndex(vf.string(methodName)), 
									  getConstantIndex(vf.string(className)), 
								      getTypeConstantIndex(parameterTypes),
								      getTypeConstantIndex(keywordTypes),
								      reflect));
	}
	
	public CodeBlock THROW(ISourceLocation src) {
		return add(new Throw(this, src));
	}
	
	public CodeBlock TYPESWITCH(IList labels){
		return add(new TypeSwitch(this, labels));
	}
	
	public CodeBlock UNWRAPTHROWNLOC(int pos) {
		return add(new UnwrapThrownLoc(this, pos));
	}
	
	public CodeBlock FILTERRETURN(){
		return add(new FilterReturn(this));
	}
	
	public CodeBlock EXHAUST() {
		return add(new Exhaust(this));
	}
	
	public CodeBlock GUARD(int ctpt) {
		return add(new Guard(this,ctpt));
	}
	
	public CodeBlock SUBSCRIPTARRAY() {
		return add(new SubscriptArray(this));
	}
	
	public CodeBlock SUBSCRIPTLIST() {
		return add(new SubscriptList(this));
	}
	
	public CodeBlock LESSINT() {
		return add(new LessInt(this));
	}
	
	public CodeBlock GREATEREQUALINT() {
		return add(new GreaterEqualInt(this));
	}
	
	public CodeBlock ADDINT() {
		return add(new AddInt(this));
	}
	
	public CodeBlock SUBTRACTINT() {
		return add(new SubtractInt(this));
	}
	
	public CodeBlock ANDBOOL() {
		return add(new AndBool(this));
	}

	public CodeBlock TYPEOF() {
		return add(new TypeOf(this));
	}
	
	public CodeBlock SUBTYPE() {
		return add(new SubType(this));
	}
	
	public CodeBlock CHECKARGTYPEANDCOPY(int pos1, Type type, int pos2) {
		return add(new CheckArgTypeAndCopy(this, pos1, getTypeConstantIndex(type), pos2));
	}
	
	public CodeBlock LOADLOCKWP(String name) {
		return add(new LoadLocKwp(this, name));
	}
	
	public CodeBlock PUSHLOCKWP(String name) {
		return add(new PushLocKwp(this, name));
	}
	
	public CodeBlock LOADVARKWP(String fuid, String name) {
		return add(new LoadVarKwp(this, fuid, name));
	}
	
	public CodeBlock PUSHVARKWP(String fuid, String name) {
		return add(new PushVarKwp(this, fuid, name));
	}
	
	public CodeBlock STORELOCKWP(String name) {
		return add(new StoreLocKwp(this, name));
	}
	
	public CodeBlock STOREVARKWP(String fuid, String name) {
		return add(new StoreVarKwp(this, fuid, name));
	}
	
	public CodeBlock UNWRAPTHROWNVAR(String fuid, int pos) {
		return add(new UnwrapThrownVar(this, fuid, pos));
	}
	
	public CodeBlock APPLY(String fuid, int arity) {
		return add(new Apply(this, fuid, arity));
	}
	
	public CodeBlock APPLYDYN(int arity) {
		return add(new ApplyDyn(this, arity));
	}
	
	public CodeBlock SWITCH(IMap caseLabels, String caseDefault, boolean useConcreteFingerprint) {
		return add(new Switch(this, caseLabels, caseDefault, useConcreteFingerprint));
	}
	
	public CodeBlock RESETLOCS(IList positions) {
		return add(new ResetLocs(this, getConstantIndex(positions)));
	}
	
	public CodeBlock VISIT(boolean direction, boolean progress, boolean fixedpoint, boolean rebuild){
		return add(new Visit(this, 
				getConstantIndex(vf.bool(direction)),
				getConstantIndex(vf.bool(progress)),
				getConstantIndex(vf.bool(fixedpoint)),
				getConstantIndex(vf.bool(rebuild))));
	}
	
	public CodeBlock CHECKMEMO(){
		return add(new CheckMemo(this));
	}
	
	public CodeBlock PUSHEMPTYKWMAP(){
		return add(new PushEmptyKwMap(this));
	}
	
	public CodeBlock VALUESUBTYPE(Type type){
		return add(new ValueSubtype(this, getTypeConstantIndex(type)));
	}
	
	public CodeBlock PUSHACCU(){
		return add(new PushAccu(this));
	}
	
	
	public CodeBlock POPACCU(){
		return add(new PopAccu(this));
	}
	
	
			
	public CodeBlock done(String fname, Map<String, Integer> functionMap, Map<String, Integer> constructorMap, Map<String, Integer> resolver) {
		this.functionMap = functionMap;
		this.constructorMap = constructorMap;
		this.resolver = resolver;
		int codeSize = pc;
		pc = 0;
		finalCode = new long[codeSize];
		for(Instruction ins : insList){
			ins.generate();
		}
		finalConstantStore = new IValue[constantStore.size()];
		for(int i = 0; i < constantStore.size(); i++ ){
			finalConstantStore[i] = constantStore.get(i);
		}
		finalTypeConstantStore = new Type[typeConstantStore.size()];
		for(int i = 0; i < typeConstantStore.size(); i++) {
			finalTypeConstantStore[i] = typeConstantStore.get(i);
		}
		
		if(constantStore.size() >= maxArg){
			throw new InternalCompilerError("In function " + fname + ": constantStore size " + constantStore.size() + "exceeds limit " + maxArg);
		}
		if(typeConstantStore.size() >= maxArg){
			throw new InternalCompilerError("In function " + fname + ": typeConstantStore size " + typeConstantStore.size() + "exceeds limit " + maxArg);
		}
	
    	return this;
    }
	
	public void setMaps(Map<String, Integer> functionMap, Map<String, Integer> constructorMap, Map<String, Integer> resolver){
	    this.functionMap = functionMap;
        this.constructorMap = constructorMap;
        this.resolver = resolver;
	}
    
    public long[] getInstructions(){
    	return finalCode;
    }
    
    public IValue[] getConstants(){
    	return finalConstantStore;
    }
    
    public Type[] getTypeConstants() {
    	return finalTypeConstantStore;
    }
    
    void listing(String fname){
    	int pc = 0;
    	while(pc < finalCode.length){
    		Opcode opc = Opcode.fromInteger(fetchOp((int) finalCode[pc]));
    		System.out.println(fname + "[" + pc +"]: " + Opcode.toString(this, opc, pc));
    		pc += opc.getPcIncrement();
    	}
    	System.out.println();
    }
    
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("\n") ;
    	boolean prevLabel = false ;
    	if(insList != null){
    		for (Instruction ins : insList ) {
    			if ( ins instanceof Label ) {
    				sb.append(ins).append(": ");
    				prevLabel = true ;
    			}
    			else {
    				if ( prevLabel ) {
    					sb.append("\t").append(ins).append("\n") ;
    					prevLabel = false ;
    				}
    				else {
    					sb.append("\t\t").append(ins).append("\n") ;    				
    				}
    			}
    		}
    	}
    	return sb.toString();
    }
    
    public String toString(int n){
    	Opcode opc = Opcode.fromInteger(fetchOp(finalCode[n]));
    	return Opcode.toString(this, opc, n);
    }

    public void genByteCode(BytecodeGenerator gen, boolean suppressCheckArgTypeAndCopy, boolean debug) {
        if(suppressCheckArgTypeAndCopy){
            int i = 0;
            int n = insList.size();
            while(i < n){
                Instruction ins = insList.get(i);
                ins.generateByteCode(gen, debug);
                if(ins instanceof CheckArgTypeAndCopy){
                    // CheckArgTypeAndCopy will only copy from/to positions; suppress the test that follows it
                    i++;
                }
                i++;
            }
        } else {
            for(Instruction ins : insList){
                ins.generateByteCode(gen, debug);
            }
        }
        if (insList.get(insList.size() - 1) instanceof Label) {
            // The mu2rvm code generator emits faulty code and jumps outside existing space
            // put in a panic return, code is also generated on a not used label.
            // Activate the peephole optimizer :).
            gen.emitPanicReturn();
        }
    }
	
	public void write(IRVMWireOutputStream out) throws IOException{ 
	    out.startMessage(CompilerIDs.CodeBlock.ID);

        out.writeField(CompilerIDs.CodeBlock.NAME, name);

        out.writeField(CompilerIDs.CodeBlock.FINAL_CONSTANT_STORE, finalConstantStore, WindowSizes.SMALL_WINDOW);

        out.writeField(CompilerIDs.CodeBlock.FINAL_TYPECONSTANT_STORE, finalTypeConstantStore, WindowSizes.TINY_WINDOW);

        out.writeField(CompilerIDs.CodeBlock.FINAL_CODE, finalCode);
       
	    out.endMessage();
	}

    public static CodeBlock read(IRVMWireInputStream in, Map<String, Integer> functionMap, Map<String, Integer> constructorMap, Map<String, Integer> resolver) throws IOException {
        String name = "unitialized name";
        
        Map<IValue, Integer> constantMap = new HashMap<>();
        ArrayList<IValue> constantStore = new ArrayList<>();
        IValue[] finalConstantStore = new IValue[0];
        
        Map<Type, Integer> typeConstantMap = new HashMap<>();
        ArrayList<Type> typeConstantStore = new ArrayList<>();
        Type[] finalTypeConstantStore = new Type[0];
        
        long[] finalCode = new long[0];
        
        in.next();
        assert in.current() == IWireInputStream.MESSAGE_START;
        if(in.message() != CompilerIDs.CodeBlock.ID){
            throw new IOException("Unexpected message: " + in.message());
        }
        while(in.next() != IWireInputStream.MESSAGE_END){
            switch(in.field()){
                
                case  CompilerIDs.CodeBlock.NAME: {
                    name = in.getString(); 
                    break;
                }
                
                case CompilerIDs.CodeBlock.FINAL_CONSTANT_STORE: {
                    finalConstantStore = in.readIValues();
                    constantMap = new HashMap<IValue, Integer> (finalConstantStore.length);
                    constantStore = new ArrayList<IValue>(finalConstantStore.length);
                    for (int i = 0; i < finalConstantStore.length; i++) {
                        constantMap.put(finalConstantStore[i], i);
                        constantStore.add(finalConstantStore[i]);
                    }
                    break;
                }
                
                case CompilerIDs.CodeBlock.FINAL_TYPECONSTANT_STORE: {
                    finalTypeConstantStore = in.readTypes();
                    typeConstantMap = new HashMap<Type, Integer>(finalTypeConstantStore.length);
                    typeConstantStore = new ArrayList<Type>(finalTypeConstantStore.length);
                    for(int i = 0; i < finalTypeConstantStore.length; i++){
                        typeConstantMap.put(finalTypeConstantStore[i], i);
                        typeConstantStore.add(finalTypeConstantStore[i]);
                    }
                    break;
                }
                
                case CompilerIDs.CodeBlock.FINAL_CODE: {
                    finalCode = in.readLongs();
                    break;
                }
                
                default: {
                    System.err.println("CodeBlock.read, skips " + in.field());
                    // skip field, normally next takes care of it
                    in.skipNestedField();
                }
            }
        }
        
        return new CodeBlock(name, constantMap, constantStore, finalConstantStore, typeConstantMap, typeConstantStore, finalTypeConstantStore, 
            functionMap, resolver, constructorMap, finalCode);
    }

    
}

class LabelInfo {
	final int index;
	int PC;
	Instruction instruction;
	
	LabelInfo(Instruction ins, int index, int pc){
		this.instruction = ins;
		this.index = index;
		this.PC = pc;
	}

	public LabelInfo(int index) {
		this.index = index;
		PC = -1;
	}
	
	public boolean isResolved(){
		return PC >= 0;
	}
}