package org.rascalmpl.library.experiments.CoreRascal.RVM.AllInstructions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Instructions {

	int[] finalCode;
	int pc;
	ArrayList<Instruction> insList;
	HashMap<String,Integer> labels;
	private ArrayList<String> labelList;
	Map<String, Integer> constMap;
	Map<String, Integer> codeMap;

	public Instructions(){
		labels = new HashMap<String,Integer>();
		labelList = new ArrayList<String>();
		insList = new ArrayList<Instruction>();
		new ArrayList<Integer>();
		pc = 0;
	}
	
	protected void defLabel(String label){
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
	
	protected String findConstantName(int n){
		for(String cname : constMap.keySet()){
			if(constMap.get(cname) == n){
				return cname;
			}
		}
		throw new RuntimeException("Cannot happen: undefined constant index " + n);
	}
	
	protected String findCodeName(int n){
		for(String cname : codeMap.keySet()){
			if(codeMap.get(cname) == n){
				return cname;
			}
		}
		throw new RuntimeException("Cannot happen: undefined code index " + n);
	}
	
	Instructions add(Instruction ins){
		insList.add(ins);
		pc += ins.pcIncrement();
		return this;
	}
	
	void addCode(int c){
		finalCode[pc++] = c;
	}
	
	public Instructions pop(){
		return add(new Pop(this));
	}
	
	public  Instructions halt(){
		return add(new Halt(this));
	}
	
	public Instructions ret(){
		return add(new Return(this));
	}
	
	public Instructions label(String arg){
		return add(new Label(this, arg));
	}
	
	public Instructions loadcon(String arg){
		return add(new LoadCon(this, arg));
	}
	
	public Instructions call(String arg){
		return add(new Call(this, arg));
	}
	
	public Instructions jmp(String arg){
		return add(new Jmp(this, arg));
	}
	
	public Instructions jmptrue(String arg){
		return add(new JmpTrue(this, arg));
	}
	
	public Instructions jmpfalse(String arg){
		return add(new JmpFalse(this, arg));
	}
	
	public Instructions loadloc (int pos){
		return add(new LoadLoc(this, pos));
	}
	
	public Instructions storeloc (int pos){
		return add(new StoreLoc(this, pos));
	}
	
	public Instructions loadvar (int scope, int pos){
		return add(new LoadVar(this, scope, pos));
	}
	
	public Instructions storevar (int scope, int pos){
		return add(new StoreVar(this, scope, pos));
	}
	
	public Instructions callprim (int arg){
		return add(new CallPrim(this, arg));
	}
    
	public Instructions done(String fname, Map<String,Integer> constMap, Map<String, Integer> codeMap){
		this.constMap = constMap;
		this.codeMap = codeMap;
		int codeSize = pc;
		pc = 0;
		finalCode = new int[codeSize];
		for(Instruction ins : insList){
			ins.generate();
		}
    	listing(fname);
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
