package org.rascalmpl.interpreter.matching;

import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.QualifiedName;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.utils.Names;

public class NodePattern extends AbstractMatchingResult {
	private IMatchingResult name;
	private List<IMatchingResult> children;
	private INode treeSubject;
	private boolean firstMatch = false;
	private boolean debug = false;
	private final TypeFactory tf = TypeFactory.getInstance();
	private final QualifiedName qname;
	private int nextChild;
	
	public NodePattern(IEvaluatorContext ctx, IMatchingResult matchPattern, QualifiedName name, List<IMatchingResult> list){
		super(ctx);
		this.name = matchPattern;
		this.qname = name;
		this.children = list;
		if(debug){
			System.err.println("NodePattern: " + name + ", #children: " + list.size());
			System.err.println("NodePattern name:" + name != null ? name : qname);
			for(IMatchingResult ap : list){
				System.err.println(ap);
			}
		}
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		if(debug){
			System.err.println("NodePattern: initMatch");
			System.err.println("NodePattern: subject type=" + subject.getType());
		}
		super.initMatch(subject);
		hasNext = false;
		if(!(subject.getValue().getType().isNodeType() || subject.getValue().getType().isAbstractDataType())){
			return;
		}
		treeSubject = (INode) subject.getValue();
		if(debug){
			System.err.println("NodePattern: pattern=" + name != null ? name : qname);
			System.err.println("NodePattern: treeSubject=" + treeSubject);
			System.err.println("NodePattern: treeSubject.arity() =" + treeSubject.arity());
			System.err.println("NodePattern: children.size() =" + children.size());
		}
		if(treeSubject.arity() != children.size()){
			return;
		}
		
		if (name != null) {
			Environment env = ctx.getCurrentEnvt();
			Type nameType = name.getType(env);
			
			if (nameType.isStringType()) {
				name.initMatch(ResultFactory.makeResult(tf.stringType(), ctx.getValueFactory().string(treeSubject.getName()), ctx));
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
		
		hasNext = true;
		firstMatch = true;
		
		for (int i = 0; i < children.size(); i += 1){
			IValue childValue = treeSubject.get(i);
			IMatchingResult child = children.get(i);
			child.initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
			hasNext &= child.hasNext();
		}
		
		nextChild = children.size() - 1;
	}
	
	@Override
	public Type getType(Environment env) {
		Type type = getConstructorType(env);
		
		if (type.isConstructorType()) {
			return getConstructorType(env).getAbstractDataType();
		}
		return type;
	}
	
	public Type getConstructorType(Environment env) {
		 Type[] types = new Type[children.size()];

		 for (int i = 0; i < children.size(); i += 1) {
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
		
		for (int i = 0; i < children.size(); i += 1) {
			types[i] =  children.get(i).getType(env);
			vals[i] =  children.get(i).toIValue(env);
		}
		Type signature = tf.tupleType(types);
		
		if (qname != null) {
			if(env.isTreeConstructorName(qname, signature)){
				Type consType = env.getConstructor(Names.name(Names.lastName(qname)), signature);

				return ctx.getValueFactory().constructor(consType, vals);
			}
		}
		return ctx.getValueFactory().node(name.toString(), vals);
	}

	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String> ();
		for (int i = 0; i < children.size(); i += 1) {
			res.addAll(children.get(i).getVariables());
		 }
		return res;
	}
	
	@Override
	public boolean hasNext(){
		if (!initialized) {
			return false;
		}
		
		if (firstMatch) {
			return true;
		}
		
		if (!hasNext) {
			return false;
		}

		while (nextChild >= 0) {
			IMatchingResult child = children.get(nextChild);

			if (child.hasNext()) {
				for (int i = nextChild + 1; i < children.size(); i++) {
					IValue childValue = treeSubject.get(i);
					IMatchingResult tailChild = children.get(i);
					tailChild.initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
				}
				return true;
			}
			nextChild--;
		}
		
		// try everything again with the next match of the node name
		if (name != null && name.hasNext()) {
			hasNext = true;
			for (int i = 0; i < children.size(); i++) {
				IValue childValue = treeSubject.get(i);
				IMatchingResult tailChild = children.get(i);
				tailChild.initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
			}
			return true;
		}
		
		hasNext = false;
		return false;
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		
		if(!(firstMatch || hasNext))
			return false;

		if (firstMatch) {
			firstMatch = false;
			if (name != null) {
				boolean nameNext = name.next();
				if (!nameNext) {
					firstMatch = hasNext = false;
					return false;
				}
			}
			
			for (IMatchingResult child : children) {
				if (!child.next()) {
					return false;
				}
			}
			
			nextChild = children.size() - 1;
			return true;
		}
		else {
			// backtracking is on!
			if (nextChild == -1) { // no children
				if (!name.next()) {
					return false;// TODO: backtrack for the node name
				}
				nextChild = 0;
			}
			
			// redo the current child and the suffix
			for (int i = nextChild; i < children.size(); i++) {
				if (!children.get(i).next()) {
					return false;
				}
			}
			nextChild = children.size() - 1;
			return true;
		}
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
