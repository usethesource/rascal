package org.rascalmpl.interpreter.matching;

import java.util.List;

import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;

public class TuplePattern extends AbstractMatchingResult {
	private List<IMatchingResult> children;
	private ITuple treeSubject;
	private final TypeFactory tf = TypeFactory.getInstance();
	private int nextChild;
	
	public TuplePattern(IEvaluatorContext ctx, Expression x, List<IMatchingResult> list){
		super(ctx, x);
		this.children = list;
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		hasNext = false;
		
		if (!subject.getValue().getType().isTupleType()) {
			return;
		}
		treeSubject = (ITuple) subject.getValue();

		if (treeSubject.arity() != children.size()){
			return;
		}
		
		hasNext = true;
		
		for (int i = 0; i < children.size(); i += 1){
			IValue childValue = treeSubject.get(i);
			IMatchingResult child = children.get(i);
			child.initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
			hasNext &= child.hasNext();
		}
		
		nextChild = 0;
	}
	
	@Override
	public Type getType(Environment env) {
		Type fieldTypes[] = new Type[children.size()];
		for(int i = 0; i < children.size(); i++){
			fieldTypes[i] = children.get(i).getType(env);
		}
		return tf.tupleType(fieldTypes);
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
	public boolean next(){
		checkInitialized();
		
		if (!hasNext) {
			return false;
		}

		while (nextChild >= 0) {
			IMatchingResult nextPattern = children.get(nextChild);

			if (nextPattern.hasNext() && nextPattern.next()) {
				if (nextChild == children.size() - 1) {
					// We need to make sure if there are no
					// more possible matches for any of the tuple's fields, 
					// then the next call to hasNext() will definitely returns false.
					hasNext = false;
					for (int i = nextChild; i >= 0; i--) {
						hasNext |= children.get(i).hasNext();
					}
					return true;
				}
				else {
					nextChild++;
				}
			}
			else {
				nextChild--;

				if (nextChild >= 0) {
					for (int i = nextChild + 1; i < children.size(); i++) {
						IValue childValue = treeSubject.get(i);
						IMatchingResult tailChild = children.get(i);
						tailChild.initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
					}
				}
			}
		}
	    hasNext = false;
	    return false;
	}
	
	@Override
	public String toString(){
		StringBuilder res = new StringBuilder();
		res.append("<");
		String sep = "";
		for (IBooleanResult mp : children){
			res.append(sep);
			sep = ", ";
			res.append(mp.toString());
		}
		res.append(">");
		
		return res.toString();
	}
}
