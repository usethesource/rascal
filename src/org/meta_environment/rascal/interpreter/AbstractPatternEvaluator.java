package org.meta_environment.rascal.interpreter;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Expression.Addition;
import org.meta_environment.rascal.ast.Expression.All;
import org.meta_environment.rascal.ast.Expression.And;
import org.meta_environment.rascal.ast.Expression.Anti;
import org.meta_environment.rascal.ast.Expression.Any;
import org.meta_environment.rascal.ast.Expression.Bracket;
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.ast.Expression.Closure;
import org.meta_environment.rascal.ast.Expression.ClosureCall;
import org.meta_environment.rascal.ast.Expression.Composition;
import org.meta_environment.rascal.ast.Expression.Comprehension;
import org.meta_environment.rascal.ast.Expression.Descendant;
import org.meta_environment.rascal.ast.Expression.Enumerator;
import org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy;
import org.meta_environment.rascal.ast.Expression.Equals;
import org.meta_environment.rascal.ast.Expression.Equivalence;
import org.meta_environment.rascal.ast.Expression.FieldProject;
import org.meta_environment.rascal.ast.Expression.FieldUpdate;
import org.meta_environment.rascal.ast.Expression.FunctionAsValue;
import org.meta_environment.rascal.ast.Expression.GetAnnotation;
import org.meta_environment.rascal.ast.Expression.GreaterThan;
import org.meta_environment.rascal.ast.Expression.GreaterThanOrEq;
import org.meta_environment.rascal.ast.Expression.Guarded;
import org.meta_environment.rascal.ast.Expression.IfThenElse;
import org.meta_environment.rascal.ast.Expression.Implication;
import org.meta_environment.rascal.ast.Expression.In;
import org.meta_environment.rascal.ast.Expression.LessThan;
import org.meta_environment.rascal.ast.Expression.LessThanOrEq;
import org.meta_environment.rascal.ast.Expression.List;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Expression.Map;
import org.meta_environment.rascal.ast.Expression.Match;
import org.meta_environment.rascal.ast.Expression.Modulo;
import org.meta_environment.rascal.ast.Expression.MultiVariable;
import org.meta_environment.rascal.ast.Expression.Negation;
import org.meta_environment.rascal.ast.Expression.Negative;
import org.meta_environment.rascal.ast.Expression.NoMatch;
import org.meta_environment.rascal.ast.Expression.NonEmptyBlock;
import org.meta_environment.rascal.ast.Expression.NonEquals;
import org.meta_environment.rascal.ast.Expression.NotIn;
import org.meta_environment.rascal.ast.Expression.OperatorAsValue;
import org.meta_environment.rascal.ast.Expression.Or;
import org.meta_environment.rascal.ast.Expression.QualifiedName;
import org.meta_environment.rascal.ast.Expression.Range;
import org.meta_environment.rascal.ast.Expression.Set;
import org.meta_environment.rascal.ast.Expression.SetAnnotation;
import org.meta_environment.rascal.ast.Expression.StepRange;
import org.meta_environment.rascal.ast.Expression.TransitiveClosure;
import org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure;
import org.meta_environment.rascal.ast.Expression.Tuple;
import org.meta_environment.rascal.ast.Expression.TypedVariable;
import org.meta_environment.rascal.ast.Expression.TypedVariableBecomes;
import org.meta_environment.rascal.ast.Expression.VariableBecomes;
import org.meta_environment.rascal.ast.Expression.Visit;
import org.meta_environment.rascal.ast.Expression.VoidClosure;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedPatternError;


/* package */ abstract class AbstractPattern implements MatchPattern {
	protected IValue subject = null;
	protected Environment env = null;
	protected boolean initialized = false;
	protected boolean hasNext = true;
	protected TypeFactory tf = TypeFactory.getInstance();
	protected IValueFactory vf;
	protected EvaluatorContext ctx;
	protected Evaluator evaluator;
	
	public AbstractPattern(IValueFactory vf, EvaluatorContext ctx) {
		this.vf = vf;
		this.ctx = ctx;
		this.evaluator = ctx.getEvaluator();
	}
	
	public AbstractAST getAST(){
		return ctx.getCurrentAST();
	}
	
	public void initMatch(IValue subject, Environment env){
		this.subject = subject;
		this.env = env;
		this.initialized = true;
		this.hasNext = true;
	}
	
	public boolean mayMatch(IValue subject, Environment env){
		return evaluator.mayMatch(getType(env), subject.getType());
	}
	
	protected void checkInitialized(){
		if(!initialized){
			throw new ImplementationError("hasNext or match called before initMatch");
		}
	}
	
	public boolean hasNext()
	{
		//System.err.println("hasNext: " + (initialized && hasNext) + this);
		return initialized && hasNext;
	}
	
	public java.util.List<String> getVariables(){
		return new java.util.LinkedList<String>();
	}
	
	abstract public IValue toIValue(Environment env);
	
	boolean matchChildren(Iterator<IValue> subjChildren, Iterator<AbstractPattern> patChildren, Environment ev){
		while (patChildren.hasNext()) {
			if (!patChildren.next().next()){
				return false;
			}
		}
		return true;
	}

	abstract public Type getType(Environment env);

	abstract public boolean next();
}

/* package */ class AbstractPatternLiteral extends AbstractPattern {

	private IValue literal;
	
	AbstractPatternLiteral(IValueFactory vf, EvaluatorContext ctx, IValue literal){
		super(vf, ctx);
		this.literal = literal;
	}
	
	@Override
	public Type getType(Environment env) {
			return literal.getType();
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if(!hasNext)
			return false;
		hasNext = false;
		if (subject.getType().comparable(literal.getType())) {
			return makeResult(subject.getType(), subject, ctx).equals(makeResult(literal.getType(), literal, ctx), ctx).isTrue();
			// TODO move to call to Result.equals
//			return Evaluator.equals(new Result(subject.getType(), subject), new Result(literal.getType(), literal));
		}
		return false;
	}
	
	@Override
	public IValue toIValue(Environment env){
		return literal;
	}
	
	@Override
	public String toString(){
		return "pattern: " + literal;
	}
}

/* package */ class AbstractPatternNode extends AbstractPattern {
	private org.meta_environment.rascal.ast.QualifiedName name;
	private java.util.List<AbstractPattern> children;
	private INode treeSubject;
	private boolean firstMatch = false;
	private final TypeFactory tf = TypeFactory.getInstance();
	
	AbstractPatternNode(IValueFactory vf, EvaluatorContext ctx, org.meta_environment.rascal.ast.QualifiedName qualifiedName, java.util.List<AbstractPattern> children){
		super(vf, ctx);
		this.name = qualifiedName;
		this.children = children;
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject, env);
		hasNext = false;
		if(!(subject.getType().isNodeType() || subject.getType().isAbstractDataType())){
			return;
		}
		treeSubject = (INode) subject;
		if(treeSubject.arity() != children.size()){
			return;
		}
		if(!Names.name(Names.lastName(name)).equals(treeSubject.getName().toString()))
				return;
		
		for (int i = 0; i < children.size(); i++){
			children.get(i).initMatch(treeSubject.get(i), env);
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
		 if (env.isTreeConstructorName(name, signature)) {
			 return env.getConstructor(Names.name(Names.lastName(name)), signature); //.getAbstractDataType();
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
		
		if(env.isTreeConstructorName(name, signature)){
			Type consType = env.getConstructor(name.toString(), signature);
			
			return vf.constructor(consType, vals);
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
		if(children.size() > 0){
			for (int i = 0; i < children.size(); i++) {
				if(children.get(i).hasNext()){
					return true;
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
		firstMatch = false;

		hasNext = matchChildren(treeSubject.getChildren().iterator(), children.iterator(), env);

		return hasNext;
	}
}

/* package */ class AbstractPatternList extends AbstractPattern implements MatchPattern {
	private java.util.List<AbstractPattern> patternChildren;	// The elements of this list pattern
	private int patternSize;						// The number of elements in this list pattern
	private IList listSubject;						// The subject as list
	private Type listSubjectType;					// The type of the subject
	private Type listSubjectElementType;			// The type of list elements
	private int subjectSize;						// Length of the subject
	private int minSubjectSize;				   	    // Minimum subject length for this pattern to match
	private boolean [] isListVar;					// Determine which elements are list or variables
	private boolean [] isBindingVar;				// Determine which elements are binding occurrences of variables
	private String [] varName;						// Name of ith variable
	private HashSet<String> allVars;				// Names of list variables declared in this pattern
	private int [] listVarStart;					// Cursor start position list variable; indexed by pattern position
	private int [] listVarLength;					// Current length matched by list variable
	private int [] listVarMinLength;				// Minimal length to be matched by list variable
	private int [] listVarMaxLength;				// Maximal length that can be matched by list variable
	private int [] listVarOccurrences;				// Number of occurrences of list variable in the pattern

	private int subjectCursor;						// Cursor in the subject
	private int patternCursor;						// Cursor in the pattern
	
	private boolean firstMatch;					// First match after initialization?
	private boolean forward;						// Moving to the right?
	
	private boolean debug = false;

	
	AbstractPatternList(IValueFactory vf, EvaluatorContext ctx, java.util.List<AbstractPattern> children){
		super(vf, ctx);
		this.patternChildren = children;					
		this.patternSize = children.size();			
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String> ();
		for (int i = 0; i < patternChildren.size(); i++) {
			res.addAll(patternChildren.get(i).getVariables());
		 }
		return res;
	}
	
	@Override
	public IValue toIValue(Environment env){
		IValue[] vals = new IValue[patternChildren.size()];
		for (int i = 0; i < patternChildren.size(); i++) {
			 vals[i] =  patternChildren.get(i).toIValue(env);
		 }
		return vf.list(vals);
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject, env);
		
		if(debug)System.err.println("initMatch: " + subject);
		
		if (!subject.getType().isListType()) {
			hasNext = false;
			return;
		}
		
		listSubject = (IList) subject;
		listSubjectType = listSubject.getType();
		listSubjectElementType = listSubject.getElementType();
		subjectCursor = 0;
		patternCursor = 0;
		subjectSize = ((IList) subject).length();
		
		isListVar = new boolean[patternSize];	
		isBindingVar = new boolean[patternSize];
		varName = new String[patternSize];
		allVars = new HashSet<String>();			
		listVarStart = new int[patternSize];		
		listVarLength = new int[patternSize];		
		listVarMinLength = new int[patternSize];	
		listVarMaxLength = new int[patternSize];	
		listVarOccurrences = new int[patternSize];
		
		int nListVar = 0;
		/*
		 * Pass #1: determine the list variables
		 */
		for(int i = 0; i < patternSize; i++){
			AbstractPattern child = patternChildren.get(i);
			isListVar[i] = false;
			isBindingVar[i] = false;
			if(child instanceof AbstractPatternTypedVariable && child.getType(env).isListType()){
				AbstractPatternTypedVariable patVar = (AbstractPatternTypedVariable) child;
				Type childType = child.getType(env);
				String name = patVar.getName();
				varName[i] = name;
				if(!patVar.isAnonymous() && allVars.contains(name)){
					throw new RedeclaredVariableError(name, getAST());
				}
				if(childType.comparable(listSubject.getType())){
					/*
					 * An explicitly declared list variable.
					 */
					if(!patVar.isAnonymous())
						allVars.add(name);
					isListVar[i] = childType.isListType();
					isBindingVar[i] = true;
					listVarOccurrences[i] = 1;
					nListVar++;
				} else {
					throw new UnexpectedTypeError(listSubject.getType(),childType, getAST());
				}
			} else if(child instanceof AbstractPatternMultiVariable){
				AbstractPatternMultiVariable multiVar = (AbstractPatternMultiVariable) child;
				String name = multiVar.getName();
				if(!multiVar.isAnonymous() && allVars.contains(name)){
					throw new RedeclaredVariableError(name, getAST());
				}
				varName[i] = name;
				isListVar[i] = true;
				if(!multiVar.isAnonymous())
					allVars.add(name);
				isBindingVar[i] = true;
				listVarOccurrences[i] = 1;
				nListVar++;
			} else if(child instanceof AbstractPatternQualifiedName){
				AbstractPatternQualifiedName qualName = (AbstractPatternQualifiedName) child;
				String name = qualName.getName();
				varName[i] = name;
				if(!qualName.isAnonymous() && allVars.contains(name)){
					/*
					 * A variable that was declared earlier in the pattern
					 */
					isListVar[i] = true;
			    	nListVar++;
			    	listVarOccurrences[i]++;
				} else if(qualName.isAnonymous()){
					/*
					 * Nothing to do
					 */
				} else {
					Result<IValue> varRes = env.getVariable(null, name);
					
					if(varRes == null){
						// A completely new variable, nothing to do
					} else {
					
				        Type varType = varRes.getType();
				        if (varType.isListType()){
				        	/*
				        	 * A variable declared in the current scope.
				        	 */
				        	if(varType.comparable(listSubjectType)){
				        		isListVar[i] = true;
				        		isBindingVar[i] = varRes.getValue() == null;
				        		nListVar++;			        		
				        	} else {
				        		throw new UnexpectedTypeError(listSubjectType,varType, getAST());
				        	}
				        } else {
				        	if(!varType.comparable(listSubjectElementType)){
				        		throw new UnexpectedTypeError(listSubjectType, varType, getAST());
				        	}
				        }
					}
				}
			} else {
				Type childType = child.getType(env);
				if(!childType.comparable(listSubjectElementType)){
					throw new UnexpectedTypeError(listSubjectType,childType, getAST());
				}
				java.util.List<String> childVars = child.getVariables();
				if(!childVars.isEmpty()){
					allVars.addAll(childVars);
					isListVar[nListVar] = false;
					nListVar++;
				} 
			}
		}
		/*
		 * Pass #2: assign minimum and maximum length to each list variable
		 */
		for(int i = 0; i < patternSize; i++){
			if(isListVar[i]){
				// TODO: reduce max length according to number of occurrences
				listVarMaxLength[i] = Math.max(subjectSize - (patternSize - nListVar), 0);
				listVarLength[i] = 0;
				listVarMinLength[i] = (nListVar == 1) ? Math.max(subjectSize - patternSize - 1, 0) : 0;
			}
		}
	
		firstMatch = true;

		minSubjectSize = patternSize - nListVar;
		hasNext = subject.getType().isListType() && subjectSize >= minSubjectSize;
		
		if(debug)System.err.println("hasNext=" + hasNext);
	}
	
	@Override
	public Type getType(Environment env) {
		if(patternSize == 0){
			return tf.listType(tf.voidType());
		}
		
		Type elemType = tf.voidType();
		for(int i = 0; i < patternSize; i++){
			Type childType = patternChildren.get(i).getType(env);
			if(childType.isListType()){
				elemType = elemType.lub(childType.getElementType());
			} else {
				elemType = elemType.lub(childType);
			}
		}
		if(debug)System.err.println("ListPattern.getType: " + tf.listType(elemType));
		return tf.listType(elemType);
	}
	
	@Override
	public boolean hasNext(){
		return initialized && hasNext;
	}
	
	private IList makeSubList(){
		assert isListVar[patternCursor];
		
		int start = listVarStart[patternCursor];
		int length = listVarLength[patternCursor];
		
		return listSubject.sublist(start, length);
	}
	
	private void matchBoundListVar(IList previousBinding){

		if(debug) System.err.println("matchBoundListVar: " + previousBinding);
		assert isListVar[patternCursor];
		
		int start = listVarStart[patternCursor];
		int length = listVarLength[patternCursor];
		
		for(int i = 0; i < previousBinding.length(); i++){
			if(debug)System.err.println("comparing: " + previousBinding.get(i) + " and " + listSubject.get(subjectCursor + i));
			if(!previousBinding.get(i).isEqual(listSubject.get(subjectCursor + i))){
				forward = false;
				listVarLength[patternCursor] = 0;
				patternCursor--;
				if(debug)System.err.println("child fails");
				return;
			}
		}
		subjectCursor = start + length;
		if(debug)System.err.println("child matches, subjectCursor=" + subjectCursor);
		patternCursor++;
	}
	
	/*
	 * We are positioned in the pattern at a list variable and match it with
	 * the current subject starting at the current position.
	 * On success, the cursors are advanced.
	 * On failure, switch to backtracking (forward = false) mode.
	 */
	private void matchBindingListVar(MatchPattern child){
		
		assert isListVar[patternCursor];
		
		int start = listVarStart[patternCursor];
		int length = listVarLength[patternCursor];
		
		IList sublist = makeSubList();
		if(debug)System.err.println("matchBindingListVar: init child #" + patternCursor + " (" + child + ") with " + sublist);
		child.initMatch(sublist, env);
	
		if(child.next()){
			subjectCursor = start + length;
			if(debug)System.err.println("child matches, subjectCursor=" + subjectCursor);
			patternCursor++;
		} else {
			forward = false;
			listVarLength[patternCursor] = 0;
			patternCursor--;
			if(debug)System.err.println("child fails, subjectCursor=" + subjectCursor);
		}	
	}
	
	/* 
	 * Perform a list match. When forward=true we move to the right in the pattern
	 * and try to match the corresponding elements of the subject. When the end of the pattern
	 * and the subject are reaching, match returns true.
	 * 
	 * When a non-matching element is encountered, we switch to moving to the left (forward==false)
	 * and try to find alternative options in list variables.
	 * 
	 * When the left-hand side of the pattern is reached while moving to the left, match return false,
	 * and no more options are available: hasNext() will return false.
	 * 
	 * @see org.meta_environment.rascal.interpreter.MatchPattern#match()
	 */
	@Override
	public boolean next(){
		checkInitialized();
		if(debug)System.err.println("AbstractPatternList.match: " + subject);
		
		if(!hasNext)
			return false;
		
		forward = firstMatch;
		firstMatch = false;
		
		do {
			
		/*
		 * Determine the various termination conditions.
		 */
			
			if(forward){
				if(patternCursor == patternSize){
					if(subjectCursor == subjectSize){
						if(debug)System.err.println(">>> match returns true");
						return true;
					}
					forward = false;
					patternCursor--;
				}
			} else {
				if(patternCursor == patternSize){
					patternCursor--;
					subjectCursor--; // Ok?
				}
			}
			
			if(patternCursor < 0 || subjectCursor < 0){
				hasNext = false;
				if(debug)System.err.println(">>> match returns false: patternCursor=" + patternCursor + ", forward=" + forward + ", subjectCursor=" + subjectCursor);
				return false;
			}
			
			/*
			 * Perform actions for the current pattern element
			 */
			
			AbstractPattern child = patternChildren.get(patternCursor);
			if(debug){
				System.err.println(this);
				System.err.println("loop: patternCursor=" + patternCursor + 
					               ", forward=" + forward + 
					               ", subjectCursor= " + subjectCursor + 
					               ", child=" + child +
					               ", isListVar=" + isListVar[patternCursor] +
					               ", class=" + child.getClass());
			}
			
			/*
			 * A binding occurrence of a list variable
			 */
	
			if(isListVar[patternCursor] && isBindingVar[patternCursor]){
				if(forward){
					listVarStart[patternCursor] = subjectCursor;
					if(patternCursor == patternSize -1){
						listVarLength[patternCursor] =  subjectSize - subjectCursor;
					} else {
						listVarLength[patternCursor] = listVarMinLength[patternCursor];
					}
				} else {
					listVarLength[patternCursor]++;
					forward = true;
				}
				if(debug)System.err.println("list var: start: " + listVarStart[patternCursor] +
						           ", len=" + listVarLength[patternCursor] + 
						           ", minlen=" + listVarMinLength[patternCursor] +
						           ", maxlen=" + listVarMaxLength[patternCursor]);
				if(listVarLength[patternCursor] > listVarMaxLength[patternCursor]  ||
				   listVarStart[patternCursor] + listVarLength[patternCursor] > subjectSize){
					
					subjectCursor = listVarStart[patternCursor];
					if(debug)System.err.println("Length failure, subjectCursor=" + subjectCursor);
					
					forward = false;
					listVarLength[patternCursor] = 0;
					patternCursor--;
				} else {
					matchBindingListVar(child);
				}
			
			/*
			 * Reference to a previously defined list variable
			 */
			} 
			else if(isListVar[patternCursor] && 
					!isBindingVar[patternCursor] && 
					env.getVariable(null, varName[patternCursor]).getType().isListType()){
				if(forward){
					listVarStart[patternCursor] = subjectCursor;
					
					Result<IValue> varRes = env.getVariable(null, varName[patternCursor]);
					IValue varVal = varRes.getValue();
					
					if(varRes.getType().isListType()){
					    assert varVal != null && varVal.getType().isListType();
					    
					    int varLength = ((IList)varVal).length();
						listVarLength[patternCursor] = varLength;
								           
						if(subjectCursor + varLength > subjectSize){
							forward = false;
							patternCursor--;
						} else {
							matchBoundListVar((IList) varVal);
						}
					}
				} else {
					subjectCursor = listVarStart[patternCursor];
					patternCursor--;
				}
			
			/*
			 * Any other element of the pattern
			 */
			} else {
				if(forward && subjectCursor < subjectSize){
					if(debug)System.err.println("AbstractPatternList.match: init child " + patternCursor + " with " + listSubject.get(subjectCursor));
					child.initMatch(listSubject.get(subjectCursor), env);
					if(child.next()){
						subjectCursor++;
						patternCursor++;
						if(debug)System.err.println("AbstractPatternList.match: child matches, subjectCursor=" + subjectCursor);
					} else {
						forward = false;
						patternCursor--;
					}
				} else {
					if(subjectCursor < subjectSize && child.next()){
						if(debug)System.err.println("child has next:" + child);
						forward = true;
						subjectCursor++;
						patternCursor++;
					} else {
						forward = false;
						subjectCursor--;
						patternCursor--;
					}
				}
			}
			
		} while (true);
	}
	
	@Override
	public String toString(){
		StringBuffer s = new StringBuffer();
		s.append("[");
		if(initialized){
			String sep = "";
			for(int i = 0; i < patternCursor; i++){
				s.append(sep).append(patternChildren.get(i).toString());
				sep = ", ";
			}
			if(patternCursor < patternSize){
				s.append("...");
			}
			s.append("]").append("==").append(subject.toString());
		} else {
			s.append("**uninitialized**]");
		}
		return s.toString();
	}
}

/*
 * SubSetGenerator produces all subsets of a given set.
 */

class SubSetGenerator implements Iterator<ISet> {
	
	private ISet remainingElements;
	private Iterator<IValue> elementGen;
	private SubSetGenerator subsetGen;
	private IValue currentElement;
	private IValueFactory vf;
	private boolean hasNext;

	SubSetGenerator(ISet elements){
		this.remainingElements = elements;
		elementGen = elements.iterator();
		this.vf = ValueFactoryFactory.getValueFactory();
		this.hasNext = true;
	}
	
	public boolean hasNext() {
		return hasNext;
	}

	public ISet next() {
		if(subsetGen == null || !subsetGen.hasNext()){
			if(elementGen.hasNext()){
				currentElement = elementGen.next();
				remainingElements = remainingElements.subtract(vf.set(currentElement));
				subsetGen = new SubSetGenerator(remainingElements);
			} else {
				hasNext = false;
				return vf.set();
			}
		}
		return subsetGen.next().insert(currentElement);
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in SubSetGenerator");
	}
}

/*
 * SingleElementGenerator produces all elements of a set as (single element) set.
 */

class SingleElementGenerator implements Iterator<ISet> {
	private Iterator<IValue> elementIter;
	
	SingleElementGenerator(ISet elements){
		this.elementIter = elements.iterator();
	}

	public boolean hasNext() {
		return elementIter.hasNext();
	}

	public ISet next() {
		return ValueFactoryFactory.getValueFactory().set(elementIter.next());
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in SingleElementGenerator");
	}
}

/* package */ class AbstractPatternSet extends AbstractPattern implements MatchPattern {
	private java.util.List<AbstractPattern> patternChildren; // The elements of the set pattern
	private int patternSize;					// Number of elements in the set pattern
	private ISet setSubject;					// Current subject	
	private Type setSubjectType;				// Type of the subject
	private Type setSubjectElementType;		// Type of the elements of current subject

	private ISet fixedSetElements;				// The fixed, non-variable elements in the pattern
	private ISet availableSetElements;			// The elements in the subject that are available:
												// = setSubject - fixedSetElements
	/*
	 * The variables are indexed from 0, ..., nVar-1 in the order in which they occur in the pattern.
	 * There are three kinds:
	 * - a list variable
	 * - an element variable
	 * - a non-literal pattern that contains variables
	 */
	private int nVar;							// Number of variables
	private HashSet<String> patVars;           // List of names of variables at top-level of the pattern
	private HashSet<String> allVars;			// List of names of all the variables in the pattern 
												// (including nested subpatterns)
	private String[] varName;					// Name of each variable
	private ISet[] varVal;						// Value of each variable
	private AbstractPattern[] varPat;			// The pattern value for non-literal patterns
	private boolean[] isSetVar;				// Is this a set variables?			
	private Iterator<?>[] varGen;				// Value generator for this variables
	
	private int currentVar;					// The currently matched variable
    private boolean firstMatch;				// First match of this pattern?
	
	private boolean debug = false;
	
	AbstractPatternSet(IValueFactory vf, EvaluatorContext ctx, java.util.List<AbstractPattern> children){
		super(vf, ctx);
		this.patternChildren = children;
		this.patternSize = children.size();
	}
	
	@Override
	public Type getType(Environment env) {
		if(patternSize == 0){
			return tf.setType(tf.voidType());
		}
		
		Type elemType = tf.voidType();
		for(int i = 0; i < patternSize; i++){
			Type childType = patternChildren.get(i).getType(env);
			if(childType.isSetType()){
				elemType = elemType.lub(childType.getElementType());
			} else {
				elemType = elemType.lub(childType);
			}
		}
		return tf.setType(elemType);
	}
	
	@Override
	public IValue toIValue(Environment env){
		IValue[] vals = new IValue[patternChildren.size()];
		for (int i = 0; i < patternChildren.size(); i++) {
			 vals[i] =  patternChildren.get(i).toIValue(env);
		 }
		return vf.set(vals);
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String> ();
		for (int i = 0; i < patternChildren.size(); i++) {
			res.addAll(patternChildren.get(i).getVariables());
		 }
		return res;
	}
	
	// Sort the variables: element variables and non-literal patterns should 
	// go before list variables since only set variables may be empty.
	
	private void sortVars(){
		String[] newVarName = new String[patternSize];
		ISet[]newVarVal= new ISet[patternSize];
		AbstractPattern[] newVarPat = new AbstractPattern[patternSize];
		boolean[] newIsSetVar = new boolean[patternSize];
		
		int nw = 0;
		for(int i = 0; i < nVar; i++){
			if(!isSetVar[i]){
				newVarName[nw] = varName[i];
				newVarVal[nw] = varVal[i];
				newVarPat[nw] = varPat[i];
				newIsSetVar[nw] = isSetVar[i];
				nw++;
			}
		}
		for(int i = 0; i < nVar; i++){
			if(isSetVar[i]){
				newVarName[nw] = varName[i];
				newVarVal[nw] = varVal[i];
				newVarPat[nw] = varPat[i];
				newIsSetVar[nw] = isSetVar[i];
				nw++;
			}
		}
		
		assert nw == nVar;
		for(int i = 0; i < nVar; i++){
			varName[i] = newVarName[i];
			varVal[i] = newVarVal[i];
			varPat[i] = newVarPat[i];
			isSetVar[i] = newIsSetVar[i];
		}
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		
		super.initMatch(subject, env);
		
		if (!subject.getType().isSetType()) {
			hasNext = false;
			return;
		}
		
		setSubject = (ISet) subject;
		setSubjectType = setSubject.getType();
		setSubjectElementType = setSubject.getElementType();
		fixedSetElements = vf.set(getType(env).getElementType());
		
		nVar = 0;
		patVars = new HashSet<String>();
		allVars = new HashSet<String>();
		varName = new String[patternSize];  			// Some overestimations
		isSetVar = new boolean[patternSize];
		varVal = new ISet[patternSize];
		varPat = new AbstractPattern[patternSize];
		varGen = new Iterator<?>[patternSize];
		/*
		 * Pass #1: determine the (ordinary and set) variables in the pattern
		 */
		for(int i = 0; i < patternSize; i++){
			AbstractPattern child = patternChildren.get(i);
			if(child instanceof AbstractPatternTypedVariable){
				AbstractPatternTypedVariable patVar = (AbstractPatternTypedVariable) child;
				Type childType = child.getType(env);
				String name = ((AbstractPatternTypedVariable)child).getName();
				if(!patVar.isAnonymous() && allVars.contains(name)){
					throw new RedeclaredVariableError(name, getAST());
				}
				if(childType.comparable(setSubjectType) || childType.comparable(setSubjectElementType)){
					/*
					 * An explicitly declared set or element variable.
					 */
					if(!patVar.isAnonymous()){
						patVars.add(name);
						allVars.add(name);
					}
					varName[nVar] = name;
					varPat[nVar] = child;
					isSetVar[nVar] = childType.isSetType();
					nVar++;
				} else {
					throw new UnexpectedTypeError(setSubject.getType(), childType, getAST());
				}
				
			} else if(child instanceof AbstractPatternMultiVariable){
				AbstractPatternMultiVariable multiVar = (AbstractPatternMultiVariable) child;
				String name = multiVar.getName();
				if(!multiVar.isAnonymous() && allVars.contains(name)){
					throw new RedeclaredVariableError(name, getAST());
				}
				varName[nVar] = name;
				varPat[nVar] = child;
				isSetVar[nVar] = true;
				nVar++;
			} else if(child instanceof AbstractPatternQualifiedName){
				AbstractPatternQualifiedName qualName = (AbstractPatternQualifiedName) child;
				String name = qualName.getName();
				if(!qualName.isAnonymous() && allVars.contains(name)){
					/*
					 * A set/element variable that was declared earlier in the pattern itself,
					 * or in a preceding nested pattern element.
					 */
					if(!patVars.contains(name)){
						/*
						 * It occurred in an earlier nested subpattern.
						 */
						varName[nVar] = name;
						varPat[nVar] = child;
						isSetVar[nVar] = true; //TODO: childType.isSetType();
						nVar++;
					} else {
						/*
						 * Ignore it (we are dealing with sets, remember).
						 */
					}
				} else if(qualName.isAnonymous()){
					varName[nVar] = name;
					varPat[nVar] = child;
					isSetVar[nVar] = false;
					nVar++;
				} else  {
					Result<IValue> varRes = env.getVariable(null, name);
					
					if(varRes == null){
						// Completely new variable
						varName[nVar] = name;
						varPat[nVar] = child;
						isSetVar[nVar] = false;
						nVar++;
						env.storeInnermostVariable(name, null);
					} else {
					    if(varRes.getValue() != null){
					        Type varType = varRes.getType();
					        if (varType.comparable(setSubjectType)){
					        	/*
					        	 * A set variable declared in the current scope: add its elements
					        	 */
					        	fixedSetElements = fixedSetElements.union((ISet)varRes.getValue());
					        } else if(varType.comparable(setSubjectElementType)){
					        	/*
					        	 * An element variable in the current scope, add its value.
					        	 */
					        	fixedSetElements = fixedSetElements.insert(varRes.getValue());
					        } else {
					        	throw new UnexpectedTypeError(setSubject.getType(),varType, getAST());
					        }
					    } 
				    }
				}
			} else if(child instanceof AbstractPatternLiteral){
				IValue lit = child.toIValue(env);
				Type childType = child.getType(env);
				if(!childType.comparable(setSubjectElementType)){
					throw new UnexpectedTypeError(setSubject.getType(), childType, getAST());
				}
				fixedSetElements = fixedSetElements.insert(lit);
			} else {
				Type childType = child.getType(env);
				if(!childType.comparable(setSubjectElementType)){
					throw new UnexpectedTypeError(setSubject.getType(), childType, getAST());
				}
				java.util.List<String> childVars = child.getVariables();
				if(!childVars.isEmpty()){
					allVars.addAll(childVars);
					varName[nVar] = child.toString();
					varPat[nVar] = child;
					isSetVar[nVar] = false;
					nVar++;
				} else {
					//System.err.println("child =" + child + ", getType=" + child.getType(env));
					//System.err.println("child.toIValue(env)=" + child.toIValue(ev) + ", type =" + child.toIValue(env).getType());
					fixedSetElements = fixedSetElements.insert(child.toIValue(env));
				}
			}
		}
		/*
		 * Pass #2: set up subset generation
		 */
		firstMatch = true;
		hasNext = fixedSetElements.isSubsetOf(setSubject);
		availableSetElements = setSubject.subtract(fixedSetElements);
		sortVars();
	}
	
	@Override
	public boolean hasNext(){
		return initialized && hasNext;
	}
	
	private ISet available(){
		ISet avail = availableSetElements;
		for(int j = 0; j < currentVar; j++){
			avail = avail.subtract(varVal[j]);
		}
		return avail;
	}
	
	private boolean makeGen(int i, ISet elements){
		if(varPat[i] instanceof AbstractPatternQualifiedName){
			AbstractPatternQualifiedName qualName = (AbstractPatternQualifiedName) varPat[i];
			String name = qualName.getName();
			if(qualName.isAnonymous()){
				varGen[i] = new SingleElementGenerator(elements);
			} else if(env.getVariable(null, name) == null){
				varGen[i] = new SingleElementGenerator(elements);
			} else {
				varGen[i] = new SingleIValueIterator(env.getVariable(null, name).getValue());
			}
		}
		if(isSetVar[i]){
			varGen[i] = new SubSetGenerator(elements);
		} else {
			if(elements.size() == 0)
				return false;
			varGen[i] = new SingleElementGenerator(elements);
		}
		return true;
	}
	
	private boolean matchVar(int i, ISet elements){
		varVal[i] = elements;
		IValue elem ;
		if(isSetVar[i]){
			elem = elements;
		} else {
			assert elements.size() == 1;
			elem = elements.iterator().next();
		}
		varPat[i].initMatch(elem, env);
		return varPat[i].next();
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		
		if(!hasNext)
			return false;
		
		if(firstMatch){
			firstMatch = hasNext = false;
			if(nVar == 0){
				return fixedSetElements.isEqual(setSubject);
			}
			if(!fixedSetElements.isSubsetOf(setSubject)){
				return false;
			}
			
			if(nVar == 1){
				if(isSetVar[0] || availableSetElements.size() == 1){
					return matchVar(0, availableSetElements);
				}
				return false;
			}
			
			currentVar = 0;
			if(!makeGen(currentVar, availableSetElements)){
				return false;
			}
		} else {
			currentVar = nVar - 2;
		}
		hasNext = true;

		if(debug)System.err.println("start assigning Vars");

		main: 
		do {
			if(debug)System.err.println("currentVar=" + currentVar + "; nVar=" + nVar);
			while(varGen[currentVar].hasNext()){
				if(matchVar(currentVar, (ISet)varGen[currentVar].next())){
					currentVar++;
					if(currentVar <= nVar - 1){
						if(!makeGen(currentVar, available())){
							varGen[currentVar] = null;
							currentVar--;
						}
					}
					continue main;
				}
			}
			varGen[currentVar] = null;
			currentVar--;
		} while(currentVar >= 0 && currentVar < nVar);


		if(currentVar < 0){
			hasNext = false;
			return false;
		}
		return true;
	}			
}

/* package */ class AbstractPatternTuple extends AbstractPattern implements MatchPattern {
	private java.util.List<AbstractPattern> children;
	private boolean firstMatch;
	
	AbstractPatternTuple(IValueFactory vf, EvaluatorContext ctx, java.util.List<AbstractPattern> children){
		super(vf, ctx);
		this.children = children;
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
	public IValue toIValue(Environment env){
		IValue[] vals = new IValue[children.size()];
		for (int i = 0; i < children.size(); i++) {
			 vals[i] =  children.get(i).toIValue(env);
		 }
		return vf.tuple(vals);
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject, env);
		hasNext = false;
		if (!subject.getType().isTupleType()) {
			return;
		}
		ITuple tupleSubject = (ITuple) subject;
		if(tupleSubject.arity() != children.size()){
			return;
		}
		for(int i = 0; i < children.size(); i++){
			children.get(i).initMatch(tupleSubject.get(i), env);
		}
		firstMatch = hasNext = true;
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
	public boolean hasNext(){
		if(firstMatch)
			return true;
		if(!hasNext)
			return false;
		hasNext = false;
		for(int i = 0; i < children.size(); i++){
			if(children.get(i).hasNext()){
				hasNext = true;
				break;
			}	
		}
		return hasNext;
	}
	
	@Override
	public boolean next() {
		checkInitialized();
		
		if(!(firstMatch || hasNext))
			return false;
		firstMatch = false;
		
		hasNext =  matchChildren(((ITuple) subject).iterator(), children.iterator(), env);
			
		return hasNext;
	}
}

/* package */ class AbstractPatternMap extends AbstractPattern implements MatchPattern {
	private java.util.List<AbstractPattern> children;
	
	AbstractPatternMap(IValueFactory vf, EvaluatorContext ctx, java.util.List<AbstractPattern> children){
		super(vf, ctx);
		this.children = children;
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
	public IValue toIValue(Environment env){
		IValue[] vals = new IValue[children.size()];
		for (int i = 0; i < children.size(); i++) {
			 vals[i] =  children.get(i).toIValue(env);
		 }
		return null; //TODO: make correct
	}
	
	@Override
	public Type getType(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		throw new ImplementationError("AbstractPatternMap.match not implemented");
	}
}

/* package */ class AbstractPatternQualifiedName extends AbstractPattern implements MatchPattern {
	protected org.meta_environment.rascal.ast.QualifiedName name;
	private Type type;
	protected boolean anonymous = false;
	private boolean debug = false;
	protected Environment env; 
	
	AbstractPatternQualifiedName(IValueFactory vf, Environment env, EvaluatorContext ctx, org.meta_environment.rascal.ast.QualifiedName name){
		super(vf, ctx);
		this.name = name;
		this.anonymous = name.toString().equals("_");
		this.env = env;
		// Look for this variable while we are constructing this pattern
		if(anonymous){
			type = TypeFactory.getInstance().valueType();
		} else {
			Result<IValue> varRes = env.getVariable(name);
			if(varRes == null || varRes.getValue() == null){
				type = TypeFactory.getInstance().valueType();
			} else {
				type = varRes.getType();
			}
		}
	}
	
	@Override
	public Type getType(Environment env) {
		return type;
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String>();
		res.addFirst(name.toString());
		return res;
	}
	
	@Override
	public IValue toIValue(Environment env){
		throw new UnsupportedOperationException("toIValue on Variable");
	}
	
	public String getName(){
		return name.toString();
	}
	
	public boolean isAnonymous(){
		return anonymous;
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if(!hasNext)
			return false;
		hasNext = false;
		if(debug)System.err.println("AbstractPatternQualifiedName.match: " + name);
		
		// Anonymous variables matches always
		if(anonymous) {
			return true;
		}
	
		Result<IValue> varRes = env.getVariable(name);
		if((varRes == null) || (varRes.getValue() == null)){
			// Is the variable still undefined?
			if(debug)System.err.println("name= " + name + ", subject=" + subject + ",");
			type = subject.getType();
			env.storeInnermostVariable(name.toString(), makeResult(type, subject, ctx));
			return true;
		}
		
		// ... or has it already received a value during matching?
		IValue varVal = varRes.getValue();
		if(debug)System.err.println("AbstractPatternQualifiedName.match: " + name + ", subject=" + subject + ", value=" + varVal);
		if (subject.getType().isSubtypeOf(varRes.getType())) {
			if(debug) {
				System.err.println("returns " + makeResult(subject.getType(),subject, ctx).equals(varRes));
			}
			return makeResult(subject.getType(),subject, ctx).equals(varRes, ctx).isTrue();
		}
		
		return false;
	}
	
	@Override
	public String toString(){
		return name + "==" + subject;
	}
}

/* package */ class AbstractPatternMultiVariable extends AbstractPatternQualifiedName {

	AbstractPatternMultiVariable(IValueFactory vf, Environment env,
			EvaluatorContext ctx, org.meta_environment.rascal.ast.QualifiedName name) {
		super(vf, env, ctx, name);
	}
	
	@Override
	public boolean next(){
		checkInitialized();
		if(!hasNext)
			return false;
		
		// If not anonymous, store the value.
		if(!anonymous) {
			Type type = subject.getType();
			env.storeInnermostVariable(name.toString(), makeResult(type, subject, ctx));
		}
		return true;
	}
	
}

/* package */class AbstractPatternTypedVariable extends AbstractPattern implements MatchPattern {
	private String name;
	org.eclipse.imp.pdb.facts.type.Type declaredType;
	private boolean anonymous = false;
	private boolean debug = false;
	private Environment env;

	
	// TODO: merge code of the following two constructors.
	
	AbstractPatternTypedVariable(IValueFactory vf, Environment env, EvaluatorContext ctx, org.eclipse.imp.pdb.facts.type.Type type,
			org.meta_environment.rascal.ast.QualifiedName qname) {
		super(vf, ctx);
		this.name = getAST().toString();
		this.declaredType = type;
		this.env = env;
		this.anonymous = name.equals("_");
		
		Result<IValue> localRes = env.getLocalVariable(qname);
		if(localRes != null){
			if(localRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, qname);
			}
			if(!localRes.getType().equivalent(type)){
				throw new UnexpectedTypeError(localRes.getType(), type, qname);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(qname, makeResult(localRes.getType(), null, ctx));
			return;
		}
		Result<IValue> globalRes = env.getVariable(qname);
		if(globalRes != null){
			if(globalRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, qname);
			}
			if(!globalRes.getType().equivalent(type)){
				throw new UnexpectedTypeError(globalRes.getType(), type, qname);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(qname, makeResult(globalRes.getType(), null, ctx));
			return;
		}
	}
	
	AbstractPatternTypedVariable(IValueFactory vf, Environment env, EvaluatorContext ctx, 
			org.eclipse.imp.pdb.facts.type.Type type, org.meta_environment.rascal.ast.Name name) {
		super(vf, ctx);
		this.name = name.toString();
		this.declaredType = type;
		this.env = env;
		this.anonymous = name.toString().equals("_");
		
		Result<IValue> localRes = env.getLocalVariable(name);
		if(localRes != null){
			if(localRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, name);
			}
			if(!localRes.getType().equivalent(type)){
				throw new UnexpectedTypeError(localRes.getType(), type, name);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(name, makeResult(localRes.getType(), null, ctx));
			return;
		}
	
		Result<IValue> globalRes = env.getVariable(name, this.name);
		if(globalRes != null){
			if(globalRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, name);
			}
			if(!globalRes.getType().equivalent(type)){
				throw new UnexpectedTypeError(globalRes.getType(), type, name);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(name, makeResult(globalRes.getType(), null, ctx));
			return;
		}
	}
	
	@Override
	public Type getType(Environment env) {
		return declaredType;
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String>();
		res.addFirst(name);
		return res;
	}
	
	@Override
	public IValue toIValue(Environment env){
		throw new UnsupportedOperationException("toIValue on Variable");
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isAnonymous(){
		return anonymous;
	}

	@Override
	public boolean next() {
		checkInitialized();
		if(!hasNext)
			return false;
		hasNext = false;
		if(debug)System.out.println("AbstractTypedVariable.next: " + subject + "(type=" + subject.getType() + ") with " + declaredType + " " + name);
		
		if (subject.getType().isSubtypeOf(declaredType)) {
			if(!anonymous)
				env.storeInnermostVariable(name, makeResult(declaredType, subject, ctx));
			if(debug)System.out.println("matches");
			return true;
		}
		if(debug)System.out.println("no match");
		return false;
	}
	
	@Override
	public String toString(){
		return declaredType + " " + name + "==" + subject;
	}
}

class AbstractPatternTypedVariableBecomes extends AbstractPattern implements MatchPattern {
	
	private String name;
	private Type declaredType;
	private Environment env;
	private MatchPattern pat;
	private boolean debug = false;

	AbstractPatternTypedVariableBecomes(IValueFactory vf, Environment env, EvaluatorContext ctx,
			org.eclipse.imp.pdb.facts.type.Type type, org.meta_environment.rascal.ast.Name aname, MatchPattern pat){
		super(vf, ctx);
		this.name = aname.toString();
		this.declaredType = type;
		this.env = env;
		this.pat = pat;
		
		if(debug) System.err.println("AbstractPatternTypedVariableBecomes: " + type + " " + name);
		Result<IValue> localRes = env.getLocalVariable(name);
		if(localRes != null){
			if(localRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, aname);
			}
			if(!localRes.getType().equivalent(type)){
				throw new UnexpectedTypeError(localRes.getType(), type, aname);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(name, makeResult(localRes.getType(), null, ctx));
			return;
		}
		Result<IValue> globalRes = env.getVariable(null,name);
		if(globalRes != null){
			if(globalRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, aname);
			}
			if(!globalRes.getType().equivalent(type)){
				throw new UnexpectedTypeError(globalRes.getType(), type, aname);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(name, makeResult(globalRes.getType(), null, ctx));
			return;
		}
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject,env);
		pat.initMatch(subject, env);
		if(!evaluator.mayMatch(pat.getType(env), declaredType))
			throw new UnexpectedTypeError(pat.getType(env), declaredType, ctx.getCurrentAST());
	}
	
	@Override
	public Type getType(Environment env) {
		return declaredType;
	}
	
	@Override
	public boolean hasNext(){
		return pat.hasNext();
	}

	@Override
	public boolean next() {
		if(debug) System.err.println("AbstractPatternTypedVariableBecomes:  next");
		if(pat.next()){
			Result<IValue> r = ResultFactory.makeResult(declaredType, subject, ctx);
			env.storeVariable(name, r);
			return true;
		}
		return false;
	}

	@Override
	public IValue toIValue(Environment env) {
		return null;
	}
}

class AbstractPatternVariableBecomes extends AbstractPattern implements MatchPattern {
	
	private String name;
	private Environment env;
	private MatchPattern pat;

	AbstractPatternVariableBecomes(IValueFactory vf, Environment env, EvaluatorContext ctx, 
			org.meta_environment.rascal.ast.Name aname, MatchPattern pat){
		super(vf, ctx);
		this.name = aname.toString();
		this.env = env;
		this.pat = pat;
		
		Result<IValue> localRes = env.getLocalVariable(name);
		if(localRes != null){
			if(localRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, aname);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(name, makeResult(localRes.getType(), null, ctx));
			return;
		}
		Result<IValue> globalRes = env.getVariable(null,name);
		if(globalRes != null){
			if(globalRes.getValue() != null){
				throw new RedeclaredVariableError(this.name, aname);
			}
			// Introduce an innermost variable that shadows the original one.
			// This ensures that the original one becomes undefined again when matching is over
			env.storeInnermostVariable(name, makeResult(globalRes.getType(), null, ctx));
			return;
		}
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject,env);
		pat.initMatch(subject, env);
	}
	
	@Override
	public Type getType(Environment env) {
		return pat.getType(env);
	}
	
	@Override
	public boolean hasNext(){
		return pat.hasNext();
	}

	@Override
	public boolean next() {
		if(pat.next()){	
			Result<IValue> r = ResultFactory.makeResult(subject.getType(), subject, ctx);
			env.storeInnermostVariable(name, r);
			return true;
		}
		return false;
	}

	@Override
	public IValue toIValue(Environment env) {
		return null;
	}
}

class AbstractPatternGuarded extends AbstractPattern implements MatchPattern {
	private Type type;
	private MatchPattern pat;
	
	AbstractPatternGuarded(IValueFactory vf, EvaluatorContext ctx, Type type, MatchPattern pat){
		super(vf, ctx);
		this.type = type;
		this.pat = pat;
	}

	@Override
	public Type getType(Environment env) {
		return type;
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject,env);
		pat.initMatch(subject, env);
		if(!evaluator.mayMatch(pat.getType(env), type))
			throw new UnexpectedTypeError(pat.getType(env), type, ctx.getCurrentAST());
		this.hasNext = pat.getType(env).equivalent(type);
	}

	@Override
	public boolean next() {
		return pat.next();
	}

	@Override
	public IValue toIValue(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}

class AbstractPatternAnti extends AbstractPattern implements MatchPattern {

	private MatchPattern pat;
	private java.util.List<String> patVars;

	public AbstractPatternAnti(IValueFactory vf, EvaluatorContext ctx, MatchPattern pat) {
		super(vf, ctx);
		this.pat = pat;
	}

	@Override
	public Type getType(Environment env) {
		return pat.getType(env);
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject,env);
		pat.initMatch(subject, env);
		
		java.util.List<String> vars = pat.getVariables();
		patVars = new java.util.ArrayList<String>(vars.size());
		for(String name : vars){
			Result<IValue> vr = env.getVariable(null, name);
			if(vr == null || vr.getValue() == null)
				patVars.add(name);
		}
	}
	
	@Override
	public boolean mayMatch(IValue subject, Environment env){
		return pat.mayMatch(subject, env);
	}

	@Override
	public boolean next() {
		boolean res = pat.next();
		// Remove any bindings
		for(String var : patVars){
			ctx.getEvaluator().peek().storeVariable(var,  ResultFactory.nothing());
		}
		return !res;
	}

	@Override
	public IValue toIValue(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}

class AbstractPatternDescendant extends AbstractPattern implements MatchPattern {

	private MatchPattern pat;
	private Evaluator eval;
	private EnumerateAndMatch enumAndMatch;

	public AbstractPatternDescendant(IValueFactory vf, EvaluatorContext ctx, MatchPattern pat) {
		super(vf, ctx);
		this.eval = ctx.getEvaluator();
		this.pat = pat;
	}

	@Override
	public Type getType(Environment env) {
		return pat.getType(env);
	}
	
	@Override
	public boolean mayMatch(IValue subject, Environment env){
		return evaluator.mayOccurIn(getType(env), subject.getType());
	}
	
	@Override
	public void initMatch(IValue subject, Environment env){
		super.initMatch(subject,env);
		enumAndMatch = new EnumerateAndMatch(pat, makeResult(subject.getType(), subject, ctx), eval);
	}
	
	@Override
	public boolean hasNext(){
		boolean r =  initialized &&  enumAndMatch.hasNext();
		System.err.println("AbstractPatternDescendant.hasNext: " + r);
		return r;
	}

	@Override
	public boolean next() {
		System.err.println("AbstractPatternDescendant.next");
		if(enumAndMatch.next().isTrue()){
			System.err.println("AbstractPatternDescendant.next: true");
			return true;
		}
		System.err.println("AbstractPatternDescendant.next: false");
		return false;
	}

	@Override
	public IValue toIValue(Environment env) {
		// TODO Auto-generated method stub
		return null;
	}
}

public class AbstractPatternEvaluator extends NullASTVisitor<AbstractPattern> {
	private IValueFactory vf;
	private Environment env;
	private EvaluatorContext ctx;
	private Environment scope;
	
	AbstractPatternEvaluator(IValueFactory vf, Environment env, Environment scope, EvaluatorContext ctx){
		this.vf = vf;
		this.env = env;
		this.ctx = ctx;
		this.scope = scope;
	}
	
	public boolean isPattern(org.meta_environment.rascal.ast.Expression pat){
		return (pat.isLiteral() && ! pat.getLiteral().isRegExp()) || 
		       pat.isCallOrTree() || pat.isList() || 
		       pat.isSet() || pat.isMap() || pat.isTuple() ||
		       pat.isQualifiedName() || pat.isTypedVariable() ||
		       pat.isVariableBecomes() || pat.isTypedVariableBecomes() ||
		       pat.isAnti() || pat.isDescendant();
	}
	
	@Override
	public AbstractPattern visitExpressionLiteral(Literal x) {
		return new AbstractPatternLiteral(vf, ctx, x.getLiteral().accept(ctx.getEvaluator()).getValue());
	}
	
	@Override
	public AbstractPattern visitExpressionCallOrTree(CallOrTree x) {
		org.meta_environment.rascal.ast.QualifiedName N = x.getQualifiedName();
		return new AbstractPatternNode(vf, new EvaluatorContext(ctx.getEvaluator(), x), N, visitElements(x.getArguments()));
	}
	
	private java.util.List<AbstractPattern> visitElements(java.util.List<org.meta_environment.rascal.ast.Expression> elements){
		ArrayList<AbstractPattern> args = new java.util.ArrayList<AbstractPattern>(elements.size());
		
		int i = 0;
		for(org.meta_environment.rascal.ast.Expression e : elements){
			args.add(i++, e.accept(this));
		}
		return args;
	}
	
	@Override
	public AbstractPattern visitExpressionList(List x) {
		return new AbstractPatternList(vf, new EvaluatorContext(ctx.getEvaluator(), x), visitElements(x.getElements()));
	}
	
	@Override
	public AbstractPattern visitExpressionSet(Set x) {
		return new AbstractPatternSet(vf, new EvaluatorContext(ctx.getEvaluator(), x), visitElements(x.getElements()));
	}
	
	@Override
	public AbstractPattern visitExpressionTuple(Tuple x) {
		return new AbstractPatternTuple(vf, new EvaluatorContext(ctx.getEvaluator(), x), visitElements(x.getElements()));
	}
	
	@Override
	public AbstractPattern visitExpressionMap(Map x) {
		throw new ImplementationError("Map in pattern not yet implemented");
	}
	
	@Override
	public AbstractPattern visitExpressionQualifiedName(QualifiedName x) {
		org.meta_environment.rascal.ast.QualifiedName name = x.getQualifiedName();
		Type signature = ctx.getEvaluator().tf.tupleType(new Type[0]);

		Result<IValue> r = ctx.getEvaluator().peek().getVariable(name);
//System.err.println("name = " + name.toString());
		if (r != null) {
			if (r.getValue() != null) {
				// Previously declared and initialized variable
				return new AbstractPatternQualifiedName(vf, env, new EvaluatorContext(ctx.getEvaluator(), name), name);
			}
			
			// Previously declared and uninitialized variable
			return new AbstractPatternTypedVariable(vf, env, new EvaluatorContext(ctx.getEvaluator(), name), r.getType(),name);
		}
		if (scope.isTreeConstructorName(name, signature)) {
			return new AbstractPatternNode(vf, new EvaluatorContext(ctx.getEvaluator(), x), name,
					new java.util.ArrayList<AbstractPattern>());
		}
		// Completely fresh variable
		return new AbstractPatternQualifiedName(vf, env, new EvaluatorContext(ctx.getEvaluator(), name), name);
		//return new AbstractPatternTypedVariable(vf, env, ev.tf.valueType(), name);
	}
	
	@Override
	public AbstractPattern visitExpressionTypedVariable(TypedVariable x) {
		TypeEvaluator te = TypeEvaluator.getInstance();
		return new AbstractPatternTypedVariable(vf, env,  new EvaluatorContext(ctx.getEvaluator(), x.getName()), te.eval(x.getType(), env), x.getName());
	}
	
	@Override
	public AbstractPattern visitExpressionTypedVariableBecomes(
			TypedVariableBecomes x) {
		TypeEvaluator te = TypeEvaluator.getInstance();
		Type type =  te.eval(x.getType(), env);
		MatchPattern pat = x.getPattern().accept(this);
		return new AbstractPatternTypedVariableBecomes(vf, env, new EvaluatorContext(ctx.getEvaluator(), x.getName()), type, x.getName(), pat);
	}
	
	@Override
	public AbstractPattern visitExpressionVariableBecomes(
			VariableBecomes x) {
		MatchPattern pat = x.getPattern().accept(this);
		return new AbstractPatternVariableBecomes(vf, env, new EvaluatorContext(ctx.getEvaluator(), x.getName()), x.getName(), pat);
	}
	
	@Override
	public AbstractPattern visitExpressionGuarded(Guarded x) {
		TypeEvaluator te = TypeEvaluator.getInstance();
		Type type =  te.eval(x.getType(), env);
		AbstractPattern absPat = x.getPattern().accept(this);
		return new AbstractPatternGuarded(vf, new EvaluatorContext(ctx.getEvaluator(), x), type, absPat);
	}
	
	@Override
	public AbstractPattern visitExpressionAnti(Anti x) {
		AbstractPattern absPat = x.getPattern().accept(this);
		return new AbstractPatternAnti(vf, new EvaluatorContext(ctx.getEvaluator(), x), absPat);
	}
	
	@Override
	public AbstractPattern visitExpressionMultiVariable(MultiVariable x) {
		return new AbstractPatternMultiVariable(vf, env, new EvaluatorContext(ctx.getEvaluator(), x), x.getQualifiedName());
	}
	
	@Override
	public AbstractPattern visitExpressionDescendant(Descendant x) {
		AbstractPattern absPat = x.getPattern().accept(this);
		return new AbstractPatternDescendant(vf,ctx, absPat);
	}
	
	/*
	 * The following constructs are not allowed in patterns
	 */
	
	@Override
	public AbstractPattern visitExpressionAddition(Addition x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
	@Override
	public AbstractPattern visitExpressionAll(All x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionAmbiguity(
			org.meta_environment.rascal.ast.Expression.Ambiguity x) {
		throw new ImplementationError("Ambiguity in expression: " + x);
	}
	@Override
	public AbstractPattern visitExpressionAnd(And x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionAny(Any x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
	@Override
	public AbstractPattern visitExpressionBracket(Bracket x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionClosure(Closure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionClosureCall(ClosureCall x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionComposition(Composition x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionComprehension(Comprehension x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionDivision(
			org.meta_environment.rascal.ast.Expression.Division x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionEquals(Equals x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionEquivalence(Equivalence x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionFieldAccess(
			org.meta_environment.rascal.ast.Expression.FieldAccess x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionFieldProject(FieldProject x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionFieldUpdate(FieldUpdate x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionFunctionAsValue(FunctionAsValue x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionGetAnnotation(GetAnnotation x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionGreaterThan(GreaterThan x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
	@Override
	public AbstractPattern visitExpressionIfThenElse(IfThenElse x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionImplication(Implication x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionIn(In x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionIntersection(
			org.meta_environment.rascal.ast.Expression.Intersection x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionLessThan(LessThan x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionLessThanOrEq(LessThanOrEq x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionLexical(
			org.meta_environment.rascal.ast.Expression.Lexical x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionMatch(Match x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionModulo(Modulo x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionNegation(Negation x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionNegative(Negative x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionNoMatch(NoMatch x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionNonEquals(NonEquals x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionNotIn(NotIn x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionOperatorAsValue(OperatorAsValue x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionOr(Or x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionProduct(
			org.meta_environment.rascal.ast.Expression.Product x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionRange(Range x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionSetAnnotation(SetAnnotation x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionStepRange(StepRange x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionSubscript(
			org.meta_environment.rascal.ast.Expression.Subscript x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionSubtraction(
			org.meta_environment.rascal.ast.Expression.Subtraction x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionTransitiveClosure(TransitiveClosure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionEnumerator(Enumerator x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionEnumeratorWithStrategy(
			EnumeratorWithStrategy x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionVisit(Visit x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionVoidClosure(VoidClosure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
}
