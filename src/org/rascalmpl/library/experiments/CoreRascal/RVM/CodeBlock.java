package org.rascalmpl.library.experiments.CoreRascal.RVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Call;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.CallPrim;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Halt;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Instruction;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Jmp;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.JmpFalse;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.JmpTrue;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Label;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.LoadCon;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.LoadLoc;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.LoadVar;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Opcode;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Pop;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.Return;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.StoreLoc;
import org.rascalmpl.library.experiments.CoreRascal.RVM.Instructions.StoreVar;

public class CodeBlock {

	int pc;
	ArrayList<Instruction> insList;
	public HashMap<String,Integer> labels;
	private ArrayList<String> labelList;
	public Map<String, Integer> constMap;
	public Map<String, Integer> codeMap;
	public int[] finalCode;

	public CodeBlock(){
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
		throw new RuntimeException("Cannot happen: undefined constant index " + n);
	}
	
	public String findCodeName(int n){
		for(String cname : codeMap.keySet()){
			if(codeMap.get(cname) == n){
				return cname;
			}
		}
		throw new RuntimeException("Cannot happen: undefined code index " + n);
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
	
	public CodeBlock ret(){
		return add(new Return(this));
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
