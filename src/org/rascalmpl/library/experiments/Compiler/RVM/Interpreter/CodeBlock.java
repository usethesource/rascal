package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.AddInt;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.AndBool;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Apply;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.ApplyDyn;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Call;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.CallConstr;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.CallDyn;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.CallJava;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.CallMuPrim;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.CallPrim;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.CheckArgTypeAndCopy;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Create;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.CreateDyn;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Exhaust;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.FailReturn;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.FilterReturn;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.GreaterEqualInt;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Guard;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Halt;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Instruction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Jmp;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.JmpFalse;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.JmpIndexed;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.JmpTrue;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Label;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LessInt;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadBool;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadCon;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadConstr;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadCont;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadFun;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadInt;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLoc;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLoc0;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLoc1;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLoc2;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLoc3;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLoc4;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLoc5;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLoc6;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLoc7;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLoc8;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLoc9;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLocDeref;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLocKwp;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLocRef;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadNestedFun;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadOFun;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadVar;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadVarDeref;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadVarKwp;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadVarRef;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Next0;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Next1;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.OCall;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.OCallDyn;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Pop;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Println;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Reset;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.ResetLocs;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Return0;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Return1;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Shift;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.StoreLoc;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.StoreLocDeref;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.StoreLocKwp;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.StoreVar;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.StoreVarDeref;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.StoreVarKwp;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.SubType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.SubscriptArray;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.SubscriptList;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.SubtractInt;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Switch;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Throw;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.TypeOf;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.TypeSwitch;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.UnwrapThrownLoc;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.UnwrapThrownVar;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Yield0;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Yield1;

public class CodeBlock {

	private final String name;
	public final IValueFactory vf;
	int pc;
	int labelIndex = 0;
	
	private final ArrayList<Instruction> insList;
	
	private final HashMap<String, LabelInfo> labelInfo;
	
	private final Map<IValue, Integer> constantMap;
	private final ArrayList<IValue> constantStore;
	private IValue[] finalConstantStore;
	
	private final Map<Type, Integer> typeConstantMap;
	private final ArrayList<Type> typeConstantStore;
	private Type[] finalTypeConstantStore;
	
	private Map<String, Integer> functionMap;
	private Map<String, Integer> resolver;
	private Map<String, Integer> constructorMap;
	
	public int[] finalCode;
	
	public CodeBlock(String name, IValueFactory factory){
		labelInfo = new HashMap<String, LabelInfo>();
		insList = new ArrayList<Instruction>();
		new ArrayList<Integer>();
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
				throw new CompilerError("In function " + name + ": double declaration of label " + label);
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
			throw new CompilerError("In function " + name + " undefined label " + label);
		}
		return info.PC;
	}
	
	public Instruction getLabelInstruction(String label){
		LabelInfo info = labelInfo.get(label);
		if(info == null){
			throw new CompilerError("In function " + name + ": undefined label " + label);
		}
		return info.instruction;
	}
	
	public IValue getConstantValue(int n){
		for(IValue constant : constantMap.keySet()){
			if(constantMap.get(constant) == n){
				return constant;
			}
		}
		throw new CompilerError("In function " + name + ": undefined constant index " + n);
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
		for(Type type : typeConstantMap.keySet()){
			if(typeConstantMap.get(type) == n){
				return type;
			}
		}
		throw new CompilerError("In function " + name + ": undefined type constant index " + n);
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
		throw new CompilerError("In function " + name + ": undefined function index " + n);
	}
	
	public int getFunctionIndex(String name){
		Integer n = functionMap.get(name);
		if(n == null){
			throw new CompilerError("In function " + name + ": undefined function name " + name);
		}
		return n;
	}
	
	public String getOverloadedFunctionName(int n){
		for(String fname : resolver.keySet()){
			if(resolver.get(fname) == n) {
				return fname;
			}
		}
		throw new CompilerError("In function " + name + ": undefined overloaded function index " + n);
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
		throw new CompilerError("In function " + name + ": undefined constructor index " + n);
	}
	
	public int getConstructorIndex(String name) {
		Integer n = constructorMap.get(name);
		if(n == null)
			throw new CompilerError("In function " + name + ": undefined constructor name " + name);
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
	
	public void addCode(int c){
		finalCode[pc++] = c;
	}
	
	public void addCode0(int op){
		finalCode[pc++] = op;
	}
	
	public void addCode1(int op, int arg1){
//		finalCode[pc++] = op;
//		finalCode[pc++] = arg1;
		finalCode[pc++] = encode1(op, arg1);
	}
	
	public void addCode2(int op, int arg1, int arg2){
//		finalCode[pc++] = op;
//		finalCode[pc++] = arg1;
//		finalCode[pc++] = arg2;
		finalCode[pc++] = encode2(op, arg1, arg2);
	}
	
	/*
	 * Proposed instruction encoding:
	 * 
	 * 	   argument2    argument1       op
	 *  |-----------| |-----------| |---------|
	 *  sizeArg2 bits sizeArg1 bits sizeOp bits
	 */
	
	public final static int sizeOp = 7;
	public final static int sizeArg1 = 13;
	public final static int sizeArg2 = 12;
	public final static int maskOp = (1 << sizeOp) - 1;
	public final static int maskArg1 = (1 << sizeArg1) - 1;
	public final static int maskArg2 = (1 << sizeArg2) - 1;
	public final static int shiftArg1 = sizeOp;
	public final static int shiftArg2 = sizeOp + sizeArg1;
	
	public final static int maxArg = (1 << Math.min(sizeArg1,sizeArg2)) - 1;

	public static int encode0(int op){
		return op;
	}
	
	public static int encode1(int op, int arg1){
		assert arg1 < (1 << sizeArg1);
		return (arg1 << shiftArg1) | op;
	}
	
	public static int encode2(int op, int arg1, int arg2){
		assert arg1 < (1 << sizeArg1) && arg2 < (1 << sizeArg2);
		return (arg2 << shiftArg2) | (arg1 << shiftArg1) | op;
	}
	
	public static int fetchOp(int instr){
		return instr & maskOp;
	}
	
	public static int fetchArg1(int instr){
		return (instr >> shiftArg1) & maskArg1;
	}
	
	public static int fetchArg2(int instr){
		return (instr >> shiftArg2) & maskArg2;
	}
	
	public static boolean isMaxArg1(int arg){
		return arg == maskArg1;
	}
	public static boolean isMaxArg2(int arg){
		return arg == maskArg2;
	}
	
	
	/*
	 * All Instructions
	 */
	
	public CodeBlock POP(){
		return add(new Pop(this));
	}
	
	public  CodeBlock HALT(){
		return add(new Halt(this));
	}
	
	public CodeBlock RETURN0() {
		return add(new Return0(this));
	}
	
	public CodeBlock RETURN1(int arity){
		return add(new Return1(this,arity));
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
	
	public CodeBlock LOADBOOL(boolean bool){
		return add(new LoadBool(this, bool));
	}
	
	public CodeBlock LOADINT(int n){
		return add(new LoadInt(this, n));
	}
	
	public CodeBlock CALL(String fuid, int arity){
		return add(new Call(this, fuid, arity));
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
	
	public CodeBlock STORELOC (int pos){
		return add(new StoreLoc(this, pos));
	}
	
	public CodeBlock LOADVAR(String fuid, int pos){
		if(pos == -1){
			getConstantIndex(vf.string(fuid));
		}
		return add(new LoadVar(this, fuid, pos));
	}
	
	public CodeBlock STOREVAR (String fuid, int pos){
		if(pos == -1){
			getConstantIndex(vf.string(fuid));
		}
		return add(new StoreVar(this, fuid, pos));
	}
	
	public CodeBlock CALLPRIM (RascalPrimitive prim, int arity, ISourceLocation src){
		return add(new CallPrim(this, prim, arity, src));
	}
	
	public CodeBlock CALLMUPRIM (MuPrimitive muprim, int arity){
		return add(new CallMuPrim(this, muprim, arity));
	}
	
	public CodeBlock LOADFUN (String fuid){
		return add(new LoadFun(this, fuid));
	}
	
	public CodeBlock CALLDYN(int arity){
		return add(new CallDyn(this, arity));
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
	
	public CodeBlock YIELD0() {
		return add(new Yield0(this));
	}
	
	public CodeBlock YIELD1(int arity) {
		return add(new Yield1(this, arity));
	}
	
	public CodeBlock PRINTLN(int arity){
		return add(new Println(this, arity));
	}
    
	public CodeBlock LOADLOCREF(int pos) {
		return add(new LoadLocRef(this, pos));
	}
	
	public CodeBlock LOADVARREF(String fuid, int pos) {
		return add(new LoadVarRef(this, fuid, pos));
	}
	
	public CodeBlock LOADLOCDEREF(int pos) {
		return add(new LoadLocDeref(this, pos));
	}
	
	public CodeBlock LOADVARDEREF(String fuid, int pos) {
		return add(new LoadVarDeref(this, fuid, pos));
	}
	
	public CodeBlock STORELOCDEREF(int pos) {
		return add(new StoreLocDeref(this, pos));
	}
	
	public CodeBlock STOREVARDEREF(String fuid, int pos) {
		return add(new StoreVarDeref(this, fuid, pos));
	}
	
	public CodeBlock LOADCONSTR(String name) {
		return add(new LoadConstr(this, name));
	}
	
	public CodeBlock CALLCONSTR(String name, int arity/*, ISourceLocation src*/) {
		return add(new CallConstr(this, name, arity/*, src*/));
	}
	
	public CodeBlock LOADNESTEDFUN(String fuid, String scopeIn) {
		return add(new LoadNestedFun(this, fuid, scopeIn));
	}
	
	public CodeBlock LOADTYPE(Type type) {
		return add(new LoadType(this, getTypeConstantIndex(type)));
	}
	
	public CodeBlock FAILRETURN(){
		return add(new FailReturn(this));
	}
	
	public CodeBlock LOADOFUN(String fuid) {
		return add(new LoadOFun(this, fuid));
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
	
	public CodeBlock GUARD() {
		return add(new Guard(this));
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
		
	public CodeBlock JMPINDEXED(IList labels){
		return add(new JmpIndexed(this, labels));
	}
	
	public CodeBlock LOADLOCKWP(String name) {
		return add(new LoadLocKwp(this, name));
	}
	
	public CodeBlock LOADVARKWP(String fuid, String name) {
		return add(new LoadVarKwp(this, fuid, name));
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
	
	public CodeBlock LOADCONT(String fuid) {
		return add(new LoadCont(this, fuid));
	}
	
	public CodeBlock RESET() {
		return add(new Reset(this));
	}
	
	public CodeBlock SHIFT() {
		return add(new Shift(this));
	}
	
	public CodeBlock SWITCH(IMap caseLabels, String caseDefault, boolean useConcreteFingerprint) {
		return add(new Switch(this, caseLabels, caseDefault, useConcreteFingerprint));
	}
	
	public CodeBlock RESETLOCS(IList positions) {
		return add(new ResetLocs(this, getConstantIndex(positions)));
	}
			
	public CodeBlock done(String fname, Map<String, Integer> codeMap, Map<String, Integer> constructorMap, Map<String, Integer> resolver, boolean listing) {
		this.functionMap = codeMap;
		this.constructorMap = constructorMap;
		this.resolver = resolver;
		int codeSize = pc;
		pc = 0;
		finalCode = new int[codeSize];
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
			throw new CompilerError("In function " + fname + ": constantStore size " + constantStore.size() + "exceeds limit " + maxArg);
		}
		if(typeConstantStore.size() >= maxArg){
			throw new CompilerError("In function " + fname + ": typeConstantStore size " + typeConstantStore.size() + "exceeds limit " + maxArg);
		}
		
		if(listing){
			listing(fname);
		}
    	return this;
    }
    
    public int[] getInstructions(){
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
    		Opcode opc = Opcode.fromInteger(finalCode[pc]);
    		System.out.println(fname + "[" + pc +"]: " + Opcode.toString(this, opc, pc));
    		pc += opc.getPcIncrement();
    	}
    	System.out.println();
    }
    
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	int pc = 0;
    	while(pc < finalCode.length){
    		Opcode opc = Opcode.fromInteger(finalCode[pc]);
    		sb.append("[").append(pc).append("]: ").append(Opcode.toString(this, opc, pc));
    		pc += opc.getPcIncrement();
    	}
    	return sb.toString();
    }
    
    public String toString(int n){
    	Opcode opc = Opcode.fromInteger(fetchOp(finalCode[n]));
    	return Opcode.toString(this, opc, n);
    }
    
    public static void main(String[] args) {
    	int w = encode2(13, 100, -2);
    	System.out.println("op = " + fetchOp(w));
    	System.out.println("arg1 = " + fetchArg1(w));
    	System.out.println("arg2 = " + fetchArg2(w));
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