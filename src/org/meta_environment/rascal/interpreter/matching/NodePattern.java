package org.meta_environment.rascal.interpreter.matching;

import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.QualifiedName;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.AbstractFunction;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.utils.Names;

public class NodePattern extends AbstractMatchingResult {
	private IMatchingResult name;
	private List<IMatchingResult> children;
	private INode treeSubject;
	private boolean firstMatch = false;
	private boolean debug = false;
	private final TypeFactory tf = TypeFactory.getInstance();
	private final QualifiedName qname;
	
	public NodePattern(IValueFactory vf, IEvaluatorContext ctx, IMatchingResult matchPattern, QualifiedName name, List<IMatchingResult> list){
		super(vf, ctx);
		this.name = matchPattern;
		this.qname = name;
		this.children = list;
		if(debug){
			System.err.println("AbstractPatternNode: " + name + ", #children: " + list.size() );
			System.err.println("AbstractPatternNode name:" + name != null ? name : qname);
			for(IMatchingResult ap : list){
				System.err.println(ap);
			}
		}
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		if(debug){
			System.err.println("AbstractPatternNode: initMatch");
			System.err.println("AbstractPatternNode: subject type=" + subject.getType());
		}
		super.initMatch(subject);
		hasNext = false;
		if(!(subject.getType().isNodeType() || subject.getType().isAbstractDataType())){
			return;
		}
		treeSubject = (INode) subject.getValue();
		if(debug){
			System.err.println("AbstractPatternNode: pattern=" + name != null ? name : qname);
			System.err.println("AbstractPatternNode: treeSubject=" + treeSubject);
			System.err.println("AbstractPatternNode: treeSubject.arity() =" + treeSubject.arity());
			System.err.println("AbstractPatternNode: children.size() =" + children.size());
		}
		if(treeSubject.arity() != children.size()){
			return;
		}
		
		if (name != null) {
			Environment env = ctx.getCurrentEnvt();
			Type nameType = name.getType(env);
			
			if (nameType.isStringType()) {
				name.initMatch(ResultFactory.makeResult(tf.stringType(), vf.string(treeSubject.getName()), ctx));
			}
			else if (nameType.isExternalType()) {
				if (treeSubject instanceof IConstructor) {
					Result<IValue> funcSubject = ctx.getCurrentEnvt().getVariable(treeSubject.getName());
					name.initMatch(funcSubject);
				}
			}
			else {
				throw new UnexpectedTypeError(tf.stringType(), nameType, name.getAST());
			}
		}
		else {
			if(!Names.name(Names.lastName(qname)).equals(treeSubject.getName().toString())) {
				return;
			}
		}
		
		for (int i = 0; i < children.size(); i++){
			IValue childValue = treeSubject.get(i);
			// TODO: see if we can use a static type here!?
			children.get(i).initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
		}
		firstMatch = hasNext = true;
	}
	
	@Override
	public Type getType(Environment env) {
		 Type[] types = new Type[children.size()];

		 for (int i = 0; i < children.size(); i++) {
			 types[i] =  children.get(i).getType(env);
		 }
		 
		 Type signature = tf.tupleType(types);
		 
		 if (qname != null) {
			 Result<IValue> constructors = env.getVariable(qname);
			 
			 if (constructors != null && constructors instanceof OverloadedFunctionResult) {
				 for (AbstractFunction d : ((OverloadedFunctionResult) constructors).iterable()) {
					 if (d.match(signature)) {
						 return env.getConstructor(d.getReturnType(), Names.name(Names.lastName(qname)), signature);
					 }
				 }
			 }
		 }
		 
	     return tf.nodeType();
	}
	
	@Override
	public IValue toIValue(Environment env){
		Type[] types = new Type[children.size()];
		IValue[] vals = new IValue[children.size()];
		
		for (int i = 0; i < children.size(); i++) {
			types[i] =  children.get(i).getType(env);
			vals[i] =  children.get(i).toIValue(env);
		}
		Type signature = tf.tupleType(types);
		
		if (qname != null) {
			if(env.isTreeConstructorName(qname, signature)){
				Type consType = env.getConstructor(Names.name(Names.lastName(qname)), signature);

				return vf.constructor(consType, vals);
			}
		}
		return vf.node(name.toString(), vals);
	}

	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String> ();
		for (int i = 0; i < children.size(); i++) {
			res.addAll(children.get(i).getVariables());
		 }
		return res;
	}
	
	@Override
	public boolean hasNext(){
		if(!initialized)
			return false;
		if(firstMatch)
			return true;
		if(!hasNext)
			return false;
		
		if(name == null || name.hasNext()) {
			if(children.size() > 0){
				for (int i = 0; i < children.size(); i++) {
					if(children.get(i).hasNext()){
						return true;
					}
				}
			}
		}
		hasNext = false;
		return false;
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		
		if(!(firstMatch || hasNext))
			return false;

		if (name != null) {
			boolean nameNext = name.next();
			if (!nameNext) {
				firstMatch = hasNext = false;
				return false;
			}
		}
		
		if(children.size() == 0){
			boolean res = firstMatch;
			firstMatch = hasNext = false;
			return res;	
		}
	   
		firstMatch = false;
		hasNext = matchChildren(treeSubject.getChildren().iterator(), children.iterator());
		
		return hasNext;
	}
	
	@Override
	public String toString(){
		StringBuilder res = new StringBuilder();
		
		if (name != null) {
			res.append(name);
		}
		else {
		    res.append(Names.name(Names.lastName(qname)));
		}
		
		res.append("(");
		
		String sep = "";
		for(IBooleanResult mp : children){
			res.append(sep);
			sep = ", ";
			res.append(mp.toString());
		}
		res.append(")");
		return res.toString();
	}
}
