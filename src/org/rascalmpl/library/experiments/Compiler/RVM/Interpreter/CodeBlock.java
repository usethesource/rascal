package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Call;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.CallConstr;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.CallDyn;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.CallMuPrim;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.CallPrim;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Create;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.CreateDyn;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Dup;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.FailReturn;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Halt;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.HasNext;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Init;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Instruction;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Jmp;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.JmpFalse;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.JmpTrue;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Label;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadBool;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadCon;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadConstr;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadFun;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadInt;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLoc;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLocRef;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadLocDeref;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadNestedFun;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadOFun;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadVar;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadVarRef;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.LoadVarDeref;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Next0;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Next1;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.OCall;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.OCallDyn;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Opcode;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Pop;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Println;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Return0;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Return1;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.StoreLoc;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.StoreLocDeref;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.StoreVar;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.StoreVarDeref;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Yield0;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.Instructions.Yield1;

public class CodeBlock {

	private final IValueFactory vf;
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
	
	public CodeBlock(IValueFactory factory){
		labelInfo = new HashMap<String, LabelInfo>();
		insList = new ArrayList<Instruction>();
		new ArrayList<Integer>();
		pc = 0;
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
			throw new RuntimeException("PANIC: undefined label " + label);
		}
		return info.PC;
	}
	
	public Instruction getLabelInstruction(String label){
		LabelInfo info = labelInfo.get(label);
		if(info == null){
			throw new RuntimeException("PANIC: undefined label " + label);
		}
		return info.instruction;
	}
	
	public IValue getConstantValue(int n){
		for(IValue constant : constantMap.keySet()){
			if(constantMap.get(constant) == n){
				return constant;
			}
		}
		throw new RuntimeException("PANIC: undefined constant index " + n);
	}
	
	private int getConstantIndex(IValue v){
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
		throw new RuntimeException("PANIC: undefined type constant index " + n);
	}
	
	private int getTypeConstantIndex(Type type){
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
		throw new RuntimeException("PANIC: undefined function index " + n);
	}
	
	public int getFunctionIndex(String name){
		Integer n = functionMap.get(name);
		if(n == null){
			throw new RuntimeException("PANIC: undefined function name " + name);
		}
		return n;
	}
	
	public String getOverloadedFunctionName(int n){
		for(String fname : resolver.keySet()){
			if(resolver.get(fname) == n) {
				return fname;
			}
		}
		throw new RuntimeException("PANIC: undefined overloaded function index " + n);
	}
	
	public int getOverloadedFunctionIndex(String name){
		Integer n = resolver.get(name);
		if(n == null){
			throw new RuntimeException("PANIC: undefined overloaded function name " + name);
		}
		return n;
	}
	
	public String getConstructorName(int n) {
		for(String cname : constructorMap.keySet()) {
			if(constructorMap.get(cname) == n)
				return cname;
		}
		throw new RuntimeException("PANIC: undefined constructor index " + n);
	}
	
	public int getConstructorIndex(String name) {
		Integer n = constructorMap.get(name);
		if(n == null)
			throw new RuntimeException("PANIC: undefined constructor name " + name);
		return n;
	}
	
	CodeBlock add(Instruction ins){
		insList.add(ins);
		pc += ins.pcIncrement();
		return this;
	}
	
	public void addCode(int c){
		finalCode[pc++] = c;
	}
	
	public CodeBlock POP(){
		return add(new Pop(this));
	}
	
	public  CodeBlock HALT(){
		return add(new Halt(this));
	}
	
	public CodeBlock RETURN0() {
		return add(new Return0(this));
	}
	
	public CodeBlock RETURN1(){
		return add(new Return1(this));
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
		return add(new LoadLoc(this, pos));
	}
	
	public CodeBlock STORELOC (int pos){
		return add(new StoreLoc(this, pos));
	}
	
	public CodeBlock LOADVAR (String fuid, int pos){
		return add(new LoadVar(this, fuid, pos));
	}
	
	public CodeBlock STOREVAR (String fuid, int pos){
		return add(new StoreVar(this, fuid, pos));
	}
	
	public CodeBlock CALLPRIM (RascalPrimitive prim, int arity){
		return add(new CallPrim(this, prim, arity));
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
	
	public CodeBlock INIT(int arity) {
		return add(new Init(this, arity));
	}
	
	public CodeBlock CREATE(String fuid, int arity) {
		return add(new Create(this, fuid, arity));
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
	
	public CodeBlock YIELD1() {
		return add(new Yield1(this));
	}
	
	public CodeBlock CREATEDYN(int arity) {
		return add(new CreateDyn(this, arity));
	}
	
	public CodeBlock HASNEXT() {
		return add(new HasNext(this));
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
	
	public CodeBlock CALLCONSTR(String name, int arity) {
		return add(new CallConstr(this, name, arity));
	}
	
	public CodeBlock LOADNESTEDFUN(String fuid, String scopeIn) {
		return add(new LoadNestedFun(this, fuid, scopeIn));
	}
	
	public CodeBlock LOADTYPE(Type type) {
		return add(new LoadType(this, getTypeConstantIndex(type)));
	}
	
	public CodeBlock DUP(){
		return add(new Dup(this));
	}
	
	public CodeBlock FAILRETURN(){
		return add(new FailReturn(this));
	}
	
	public CodeBlock LOADOFUN(String fuid) {
		return add(new LoadOFun(this, fuid));
	}
	
	public CodeBlock OCALL(String fuid, int arity) {
		return add(new OCall(this, fuid, arity));
	}
	
	public CodeBlock OCALLDYN(int arity) {
		return add(new OCallDyn(this, arity));
	}
		
	public CodeBlock done(String fname, Map<String, Integer> codeMap, Map<String, Integer> constructorMap, Map<String, Integer> resolver, boolean listing) {
		System.out.println("CodeBlock.done for " + fname);
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
    
    public String toString(int n){
    	Opcode opc = Opcode.fromInteger(finalCode[n]);
    	return Opcode.toString(this, opc, n);
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
}
