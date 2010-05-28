package org.rascalmpl.parser.sgll.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.parser.sgll.util.ArrayList;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class ContainerNode implements INode{
	private final static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	private IConstructor firstProduction;
	private INode[] firstAlternative;
	private ArrayList<IConstructor> productions;
	private ArrayList<INode[]> alternatives;
	
	public ContainerNode(){
		super();
	}
	
	public void addAlternative(IConstructor production, INode[] children){
		if(firstAlternative == null){
			firstProduction = production;
			firstAlternative = children;
		}else{
			if(alternatives == null){
				productions = new ArrayList<IConstructor>(1);
				alternatives = new ArrayList<INode[]>(1);
			}
			productions.add(production);
			alternatives.add(children);
		}
	}
	
	public boolean isEpsilon(){
		return false;
	}
	
	private void printAlternative(IConstructor production, INode[] children, StringBuilder sb){
		sb.append("appl(");
		sb.append(production);
		sb.append(',');
		sb.append('[');
		sb.append(children[0]);
		for(int i = 1; i < children.length; i++){
			sb.append(',');
			sb.append(children[i]);
		}
		sb.append(']');
		sb.append(')');
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		if(alternatives == null){
			printAlternative(firstProduction, firstAlternative, sb);
		}else{
			sb.append("amb({");
			for(int i = alternatives.size() - 1; i >= 0; i--){
				printAlternative(productions.get(i), alternatives.get(i), sb);
				sb.append(',');
			}
			printAlternative(firstProduction, firstAlternative, sb);
			sb.append('}');
			sb.append(')');
		}
		
		return sb.toString();
	}
	
	private IValue buildAlternative(IConstructor production, INode[] children){
		IListWriter childrenListWriter = vf.listWriter(Factory.Tree);
		for(int i = children.length - 1; i >= 0; i--){
			childrenListWriter.insert(children[i].toTerm());
		}
		
		return vf.constructor(Factory.Tree_Appl, production, childrenListWriter.done());
	}
	
	public IValue toTerm(){
		if(alternatives == null){
			return buildAlternative(firstProduction, firstAlternative);
		}
		
		ISetWriter ambListWriter = vf.setWriter(Factory.Tree);
		for(int i = alternatives.size() - 1; i >= 0; i--){
			ambListWriter.insert(buildAlternative(productions.get(i), alternatives.get(i)));
		}
		ambListWriter.insert(buildAlternative(firstProduction, firstAlternative));
		
		return vf.constructor(Factory.Tree_Amb, ambListWriter.done());
	}
}
