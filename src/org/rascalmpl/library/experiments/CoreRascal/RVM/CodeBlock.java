package org.rascalmpl.library.experiments.CoreRascal.RVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Call;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.CallDyn;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.CallPrim;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Create;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.CreateDyn;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Halt;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.HasNext;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Instruction;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Jmp;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.JmpFalse;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.JmpTrue;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Label;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.LoadCon;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.LoadFun;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.LoadLoc;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.LoadVar;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Opcode;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Pop;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Next0;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Next1;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Print;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Return0;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Return1;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Init;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.StoreLoc;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.StoreVar;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Yield0;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Yield1;

public class CodeBlock {

	int pc;
	ArrayList<Instruction> insList;
	public HashMap<String,Integer> labels;
	private ArrayList<String> labelList;
	public Map<String, Integer> constMap;
	public Map<String, Integer> codeMap;
	public int[] finalCode;

	public CodeBlock(IValueFactory factory){
		labels = new HashMap<String,Integer>();
		labelList = new ArrayList<String>();
		insList = new ArrayList<Instruction>();
		new ArrayList<Integer>();
		pc = 0;
	}
	
	public void defLabel(String label){
		int idx = labelList.indexOf(label);
		if(idx < 0){
			labelList.add(label);
		}
		labels.put(label, pc);
	}
	
	protected int useLabel(String label){
		int idx = labelList.indexOf(label);
		if(idx < 0){
			idx = labelList.size();
			labelList.add(label);
		}
		return idx;
	}
	
	public String findConstantName(int n){
		for(String cname : constMap.keySet()){
			if(constMap.get(cname) == n){
				return cname;
			}
		}
		throw new RuntimeException("PANIC: undefined constant index " + n);
	}
	
	public String findFunctionName(int n){
		for(String fname : codeMap.keySet()){
			if(codeMap.get(fname) == n){
				return fname;
			}
		}
		throw new RuntimeException("PANIC: undefined function index " + n);
	}
	
	public String findCodeName(int n){
		for(String cname : codeMap.keySet()){
			if(codeMap.get(cname) == n){
				return cname;
			}
		}
		throw new RuntimeException("PANIC: undefined code index " + n);
	}
	
	CodeBlock add(Instruction ins){
		insList.add(ins);
		pc += ins.pcIncrement();
		return this;
	}
	
	public void addCode(int c){
		finalCode[pc++] = c;
	}
	
	public CodeBlock pop(){
		return add(new Pop(this));
	}
	
	public  CodeBlock halt(){
		return add(new Halt(this));
	}
	
	public CodeBlock ret0() {
		return add(new Return0(this));
	}
	
	public CodeBlock ret1(){
		return add(new Return1(this));
	}
	
	public CodeBlock label(String arg){
		return add(new Label(this, arg));
	}
	
	public CodeBlock loadcon(String arg){
		return add(new LoadCon(this, arg));
	}
	
	public CodeBlock call(String arg){
		return add(new Call(this, arg));
	}
	
	public CodeBlock jmp(String arg){
		return add(new Jmp(this, arg));
	}
	
	public CodeBlock jmptrue(String arg){
		return add(new JmpTrue(this, arg));
	}
	
	public CodeBlock jmpfalse(String arg){
		return add(new JmpFalse(this, arg));
	}
	
	public CodeBlock loadloc (int pos){
		return add(new LoadLoc(this, pos));
	}
	
	public CodeBlock storeloc (int pos){
		return add(new StoreLoc(this, pos));
	}
	
	public CodeBlock loadvar (int scope, int pos){
		return add(new LoadVar(this, scope, pos));
	}
	
	public CodeBlock storevar (int scope, int pos){
		return add(new StoreVar(this, scope, pos));
	}
	
	public CodeBlock callprim (Primitive prim){
		return add(new CallPrim(this, prim));
	}
	
	public CodeBlock loadfun (String name){
		return add(new LoadFun(this, name));
	}
	
	public CodeBlock calldyn(){
		return add(new CallDyn(this));
	}
	
	public CodeBlock init() {
		return add(new Init(this));
	}
	
	public CodeBlock create(String name) {
		return add(new Create(this, name));
	}
	
	public CodeBlock next0() {
		return add(new Next0(this));
	}
	
	public CodeBlock next1() {
		return add(new Next1(this));
	}
	
	public CodeBlock yield0() {
		return add(new Yield0(this));
	}
	
	public CodeBlock yield1() {
		return add(new Yield1(this));
	}
	
	public CodeBlock createdyn() {
		return add(new CreateDyn(this));
	}
	
	public CodeBlock hasNext() {
		return add(new HasNext(this));
	}
	
	public CodeBlock print(String arg){
		return add(new Print(this, arg));
	}
    
	public CodeBlock done(String fname, Map<String,Integer> constMap, Map<String, Integer> codeMap, boolean listing){
		this.constMap = constMap;
		this.codeMap = codeMap;
		int codeSize = pc;
		pc = 0;
		finalCode = new int[codeSize];
		for(Instruction ins : insList){
			ins.generate();
		}
		if(listing){
			listing(fname);
		}
    	return this;
    }
    
    public int[] getInstructions(){
    	return finalCode;
    }
    
    void listing(String fname){
    	int pc = 0;
    	while(pc < finalCode.length){
    		Opcode opc = Opcode.fromInteger(finalCode[pc]);
    		System.out.println(fname + "[" + pc +"]: " + Opcode.toString(this, opc, pc));
    		pc += opc.getIncrement();
    	}
    	System.out.println();
    }
    
    public String toString(int n){
    	Opcode opc = Opcode.fromInteger(finalCode[n]);
    	return Opcode.toString(this, opc, n);
    }
}
