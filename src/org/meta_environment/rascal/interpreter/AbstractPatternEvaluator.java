package org.meta_environment.rascal.interpreter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.Value;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.Name;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Expression.Area;
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.ast.Expression.List;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Expression.Map;
import org.meta_environment.rascal.ast.Expression.QualifiedName;
import org.meta_environment.rascal.ast.Expression.Set;
import org.meta_environment.rascal.ast.Expression.Tuple;
import org.meta_environment.rascal.ast.Expression.TypedVariable;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;
import org.meta_environment.rascal.interpreter.env.Result;
import org.meta_environment.rascal.interpreter.exceptions.RascalBug;
import org.meta_environment.rascal.interpreter.exceptions.RascalRunTimeError;
import org.meta_environment.rascal.interpreter.exceptions.RascalTypeError;

/* package */ 
/**
 * The MatchPattern  interface describes the standard way of applying a pattern to a subject:
 * 1. Create the MatchPattern
 * 2. Initialize the pattern with the subject to be matched.
 * 3. While hasNext() returns true: call match() do perform the actual pattern match.
 *
 */
interface MatchPattern {
	/**
	 * @param ev: the current evaluator
	 * @return the Rascal type of this MatchPattern
	 */
	public Type getType(Evaluator ev);
	
	/**
	 * @param subject to be matched
	 * @param ev the current evaluator
	 */
	public void initMatch(IValue subject, Evaluator ev);
	
	/**
	 * @return true if this MatchPattern has more matches available
	 */
	public boolean hasNext();
	
	/**
	 * @return true if the MatchPattern matches the subject
	 */
	public boolean next();
}

/* package */ abstract class AbstractPattern implements MatchPattern {
	
	protected IValue subject = null;
	protected Evaluator ev = null;
	protected boolean initialized = false;
	protected boolean hasNext = true;
	
	public void initMatch(IValue subject, Evaluator ev){
		this.subject = subject;
		this.ev = ev;
		this.initialized = true;
		this.hasNext = true;
	}
	
	protected void checkInitialized(){
		if(!initialized){
			throw new RascalBug("hasNext or match called before initMatch");
		}
	}
	
	public boolean hasNext()
	{
		return initialized && hasNext;
	}
	
	public java.util.List<String> getVariables(){
		return new java.util.LinkedList<String>();
	}
	
	abstract public IValue toIValue(Evaluator ev);
	
	boolean matchChildren(Iterator<IValue> subjChildren, Iterator<AbstractPattern> patChildren, Evaluator ev){
		while (patChildren.hasNext()) {
			if (!patChildren.next().next()){
				return false;
			}
		}
		return true;
	}

	abstract public Type getType(Evaluator ev);

	abstract public boolean next();
	
}
/* package */ class AbstractPatternLiteral extends AbstractPattern {

	private IValue literal;
	
	AbstractPatternLiteral(IValue literal){
		this.literal = literal;
	}
	
	public Type getType(Evaluator ev) {
			return literal.getType();
	}
	
	public boolean next(){
		checkInitialized();
		hasNext = false;
		//System.err.println("AbstractPatternLiteral.match: " + subject);
		if (subject.getType().isSubtypeOf(literal.getType())) {
			return ev.equals(ev.result(subject), ev.result(literal));
		}
		return false;
	}
	
	public IValue toIValue(Evaluator ev){
		return literal;
	}
	
	public String toString(){
		return "pattern: " + literal;
	}
}

/* package */ class AbstractPatternNode extends AbstractPattern {
	private org.meta_environment.rascal.ast.QualifiedName name;
	private java.util.List<AbstractPattern> children;
	private INode treeSubject;
	private boolean firstMatch = true;
	
	AbstractPatternNode(org.meta_environment.rascal.ast.QualifiedName qualifiedName, java.util.List<AbstractPattern> children){
		this.name = qualifiedName;
		this.children = children;
	}
	
	@Override
	public void initMatch(IValue subject, Evaluator ev){
		System.err.println(name + ": initMatch " + subject);
		super.initMatch(subject, ev);
		hasNext = false;
		if(!(subject.getType().isNodeType() || subject.getType().isAbstractDataType())){
			return;
		}
		treeSubject = (INode) subject;
		if(treeSubject.arity() != children.size()){
			return;
		}
		if(!name.toString().equals(treeSubject.getName().toString()))
				return;
		
		for (int i = 0; i < children.size(); i++){
			children.get(i).initMatch(treeSubject.get(i), ev);
		}
		firstMatch = hasNext = true;
		System.err.println(name + ": initMatch " + subject + " sets hasNext=true");
	}
	
	public Type getType(Evaluator ev) {
		 Type[] types = new Type[children.size()];

		 for (int i = 0; i < children.size(); i++) {
			 types[i] =  children.get(i).getType(ev);
		 }
		 
		 Type signature = ev.tf.tupleType(types);
		 
		 if (ev.isTreeConstructorName(name, signature)) {
			 return ev.stack.getConstructor(name.toString(), signature);
		 } else {
			 return ev.tf.nodeType();
		 }
	}
	
	public IValue toIValue(Evaluator ev){
		Type type = getType(ev);
		
		IValue[] vals = new IValue[children.size()];
		for (int i = 0; i < children.size(); i++) {
			 vals[i] =  children.get(i).toIValue(ev);
		}
		if(type.isConstructorType()){
			return ev.vf.constructor(type, vals);
		} else {
			return ev.vf.node(name.toString(), vals);
		}
		
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
		System.err.println(name + ": hasNext " + subject);
		if(!initialized)
			return false;
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
	
	public boolean next(){
		System.err.println(name + ": next " + subject);
		checkInitialized();
		
		if(!(firstMatch || hasNext))
			return false;
		firstMatch = false;

		hasNext = matchChildren(treeSubject.getChildren().iterator(), children.iterator(), ev);

		return hasNext;
	}
}

/* package */ class AbstractPatternList extends AbstractPattern implements MatchPattern {
	private java.util.List<AbstractPattern> children;	// The elements of this list pattern
	private int patternSize;						// The number of elements in this list pattern
	private IList listSubject;						// The subject as list
	private Type listSubjectType;					// The type of the subject
	private Type listSubjectElementType;			// The type of list elements
	private int subjectSize;						// Length of the subject
	private int minSubjectSize;					// Minimum subject length for this pattern to match
	private boolean [] isListVar;					// Determine which elements are list or variables
	private boolean hasListVar;					// Any list variables in this pattern?
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

	
	AbstractPatternList(java.util.List<AbstractPattern> children){
		this.children = children;					
		this.patternSize = children.size();			
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String> ();
		for (int i = 0; i < children.size(); i++) {
			res.addAll(children.get(i).getVariables());
		 }
		return res;
	}
	
	public IValue toIValue(Evaluator ev){
		IValue[] vals = new IValue[children.size()];
		for (int i = 0; i < children.size(); i++) {
			 vals[i] =  children.get(i).toIValue(ev);
		 }
		return ev.vf.list(vals);
	}
	
	@Override
	public void initMatch(IValue subject, Evaluator ev){
		super.initMatch(subject, ev);
		
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
		allVars = new HashSet<String>();			
		listVarStart = new int[patternSize];		
		listVarLength = new int[patternSize];		
		listVarMinLength = new int[patternSize];	
		listVarMaxLength = new int[patternSize];	
		listVarOccurrences = new int[patternSize];
		
		int nListVar = 0;
		hasListVar = false;
		/*
		 * Pass #1: determine the list variables
		 */
		for(int i = 0; i < patternSize; i++){
			AbstractPattern child = children.get(i);
			isListVar[i] = false;
			if(child instanceof AbstractPatternTypedVariable && child.getType(ev).isListType()){
				
				Type childType = child.getType(ev);
				String name = ((AbstractPatternTypedVariable)child).getName();
				if(allVars.contains(name)){
					throw new RascalTypeError("Double declaration of variable " + name);
				}
				if(childType.isSubtypeOf(listSubject.getType())){
					/*
					 * An explicitly declared list variable.
					 */
					allVars.add(name);
					hasListVar = true;
					isListVar[i] = childType.isListType();
					listVarOccurrences[i] = 1;
					nListVar++;
				} else {
					throw new RascalTypeError(childType + " variable " + name + " not allowed in pattern of type " + listSubject.getType());
				}
			} else if(child instanceof AbstractPatternQualifiedName){
				
				String name =((AbstractPatternQualifiedName)child).getName();
				if(allVars.contains(name)){
					/*
					 * A variable that was declared earlier in the pattern
					 */
					isListVar[i] = true;
			    	nListVar++;
			    	listVarOccurrences[i]++;
				} else  {
					GlobalEnvironment env = GlobalEnvironment.getInstance();
					Result varRes = env.getVariable(name);
				         
				    if((varRes != null) && (varRes.value != null)){
				        IValue varVal = varRes.value;
				        Type varType = varRes.type;
				        if (varType.isListType()){
				        	/*
				        	 * A variable declared in the current scope.
				        	 */
				        	if(varType.isSubtypeOf(listSubjectType)){
				        		isListVar[i] = true;
				        		nListVar++;
				        	} else {
				        		throw new RascalTypeError(varType + " variable " + name + " not allowed in pattern of type " + listSubjectType);
				        	}
				        } else {
				        	if(!varVal.getType().isSubtypeOf(listSubjectElementType)){
				        		throw new RascalTypeError(varType + " variable " + name + " not allowed in pattern of type " + listSubjectType);
				        	}
				        }
				    } else {
				    	throw new RascalRunTimeError("Uninitialized variable " + name);
				    }
				}
			} else {
				Type childType = child.getType(ev);
				if(!childType.isSubtypeOf(listSubjectElementType)){
					throw new RascalTypeError(child + " not allowed in pattern of type " + listSubjectType);
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
	}
	
	public Type getType(Evaluator ev) {
		if(patternSize == 0){
			return ev.tf.listType(ev.tf.voidType());
		} else {
			Type elemType = ev.tf.voidType();
			for(int i = 0; i < patternSize; i++){
				Type childType = children.get(0).getType(ev);
				if(childType.isListType()){
					elemType = elemType.lub(childType.getElementType());
				} else {
					elemType = elemType.lub(childType);
				}
			}
			if(debug)System.err.println("ListPattern.getType: " + ev.tf.listType(elemType));
			return ev.tf.listType(elemType);
		}
	}
	
	@Override
	public boolean hasNext(){
		return initialized && hasNext && hasListVar;
	}
	
	private IList makeSubList(){
		assert isListVar[patternCursor];
		
		int start = listVarStart[patternCursor];
		int length = listVarLength[patternCursor];
		
		return new SubList((Value) listSubject, start, length);
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
		child.initMatch(sublist, ev);
	
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
			
			AbstractPattern child = children.get(patternCursor);
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
			if(isListVar[patternCursor] && child instanceof AbstractPatternTypedVariable){	
				
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
			} else if(isListVar[patternCursor] &&  child instanceof AbstractPatternQualifiedName &&
					GlobalEnvironment.getInstance().getVariable(((AbstractPatternQualifiedName)child).getName()).type.isListType()
			){
				if(forward){
					listVarStart[patternCursor] = subjectCursor;
					
					String name = ((AbstractPatternQualifiedName)child).getName();
					GlobalEnvironment env = GlobalEnvironment.getInstance();
					Result varRes = env.getVariable(name);
					IValue varVal = varRes.value;
					
					if(!varRes.type.isListType()){
						
					} else {
				         
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
					child.initMatch(listSubject.get(subjectCursor), ev);
					if(child.next()){
						subjectCursor++;
						patternCursor++;
						if(debug)System.err.println("AbstractPatternList.match: child matches, subjectCursor=" + subjectCursor);
					} else {
						forward = false;
						patternCursor--;
					}
				} else {
					forward = false;
					subjectCursor--;
					patternCursor--;
				}
			}
			
		} while (true);
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer();
		s.append("[");
		if(initialized){
			String sep = "";
			for(int i = 0; i < patternCursor; i++){
				s.append(sep).append(children.get(i).toString());
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
	private ValueFactory vf;
	private boolean hasNext;

	SubSetGenerator(ISet elements){
		this.remainingElements = elements;
		elementGen = elements.iterator();
		this.vf = ValueFactory.getInstance();
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
		return ValueFactory.getInstance().set(elementIter.next());
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in SingleElementGenerator");
	}
	
}

/* package */ class AbstractPatternSet extends AbstractPattern implements MatchPattern {
	private java.util.List<AbstractPattern> children; // The elements of the set pattern
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
	
	AbstractPatternSet(java.util.List<AbstractPattern> children){
		this.children = children;
		this.patternSize = children.size();
	}
	
	public Type getType(Evaluator ev) {
		if(patternSize == 0){
			return ev.tf.setType(ev.tf.voidType());
		} else {
			Type elemType = ev.tf.voidType();
			for(int i = 0; i < patternSize; i++){
				Type childType = children.get(0).getType(ev);
				if(childType.isSetType()){
					elemType = elemType.lub(childType.getElementType());
				} else {
					elemType = elemType.lub(childType);
				}
			}
			return ev.tf.setType(elemType);
		}
	}
	
	public IValue toIValue(Evaluator ev){
		IValue[] vals = new IValue[children.size()];
		for (int i = 0; i < children.size(); i++) {
			 vals[i] =  children.get(i).toIValue(ev);
		 }
		return ev.vf.set(vals);
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String> ();
		for (int i = 0; i < children.size(); i++) {
			res.addAll(children.get(i).getVariables());
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
	public void initMatch(IValue subject, Evaluator ev){
		
		super.initMatch(subject, ev);
		
		if (!subject.getType().isSetType()) {
			hasNext = false;
			return;
		}
		
		setSubject = (ISet) subject;
		setSubjectType = setSubject.getType();
		setSubjectElementType = setSubject.getElementType();
		fixedSetElements = ev.vf.set(getType(ev));
		
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
			AbstractPattern child = children.get(i);
			if(child instanceof AbstractPatternTypedVariable){
				Type childType = child.getType(ev);
				String name = ((AbstractPatternTypedVariable)child).getName();
				if(allVars.contains(name)){
					throw new RascalTypeError("Double declaration of variable " + name);
				}
				if(childType.isSubtypeOf(setSubjectType) || childType.isSubtypeOf(setSubjectElementType)){
					/*
					 * An explicitly declared set or element variable.
					 */
					patVars.add(name);
					allVars.add(name);
					varName[nVar] = name;
					varPat[nVar] = child;
					isSetVar[nVar] = childType.isSetType();
					nVar++;
				} else 
					throw new RascalTypeError(childType + " variable " + name + " not allowed in pattern of type " + setSubject.getType());
			} else if(child instanceof AbstractPatternQualifiedName){
				String name =((AbstractPatternQualifiedName)child).getName();
				if(allVars.contains(name)){
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
				} else  {
					GlobalEnvironment env = GlobalEnvironment.getInstance();
					Result varRes = env.getVariable(name);
				         
				    if((varRes != null) && (varRes.value != null)){
				        IValue varVal = varRes.value;
				        Type varType = varRes.type;
				        if (varType.isSubtypeOf(setSubjectType)){
				        	/*
				        	 * A set variable declared in the current scope: add its elements
				        	 */
				        	fixedSetElements = fixedSetElements.union((ISet)varRes.value);
				        } else if(varType.isSubtypeOf(setSubjectElementType)){
				        	/*
				        	 * An element variable in the current scope, add its value.
				        	 */
				        	fixedSetElements = fixedSetElements.insert(varRes.value);
				        } else
				        	throw new RascalTypeError(varType + " variable " + name + " not allowed in pattern of type " + setSubject.getType());
				    } else {
				    	throw new RascalRunTimeError("Uninitialized variable " + name);
				    }
				}
			} else if(child instanceof AbstractPatternLiteral){
				IValue lit = child.toIValue(ev);
				Type childType = child.getType(ev);
				if(!childType.isSubtypeOf(setSubjectElementType)){
					throw new RascalTypeError(lit + " not allowed in pattern of type " + setSubject.getType());
				}
				fixedSetElements = fixedSetElements.insert(lit);
			} else {
				Type childType = child.getType(ev);
				if(!childType.isSubtypeOf(setSubjectElementType)){
					throw new RascalTypeError(child + " not allowed in pattern of type " + setSubject.getType());
				}
				java.util.List<String> childVars = child.getVariables();
				if(!childVars.isEmpty()){
					allVars.addAll(childVars);
					varName[nVar] = child.toString();
					varPat[nVar] = child;
					isSetVar[nVar] = false;
					nVar++;
				} else {
					//System.err.println("child =" + child + ", getType=" + child.getType(ev));
					//System.err.println("child.toIValue(ev)=" + child.toIValue(ev) + ", type =" + child.toIValue(ev).getType());
					fixedSetElements = fixedSetElements.insert(child.toIValue(ev));
				}
			}
		}
		/*
		 * Pass #2: set up subset generation
		 */
		firstMatch = true;
		hasNext = fixedSetElements.isSubSet(setSubject);
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
			String name = ((AbstractPatternQualifiedName) varPat[i]).getName();
			varGen[i] = new SingleIValueIterator(GlobalEnvironment.getInstance().getVariable(name).value);
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
		varPat[i].initMatch(elem, ev);
		return varPat[i].next();
	}
	
	public boolean next(){
		checkInitialized();
		
		if(!hasNext)
			return false;
		
		if(firstMatch){
			firstMatch = hasNext = false;
			if(nVar == 0){
				return fixedSetElements.isEqual(setSubject);
			}
			if(!fixedSetElements.isSubSet(setSubject)){
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
	
	AbstractPatternTuple(java.util.List<AbstractPattern> children){
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
	
	public IValue toIValue(Evaluator ev){
		IValue[] vals = new IValue[children.size()];
		for (int i = 0; i < children.size(); i++) {
			 vals[i] =  children.get(i).toIValue(ev);
		 }
		return ev.vf.tuple(vals);
	}
	
	@Override
	public void initMatch(IValue subject, Evaluator ev){
		super.initMatch(subject, ev);
		hasNext = false;
		if (!subject.getType().isTupleType()) {
			return;
		}
		ITuple tupleSubject = (ITuple) subject;
		if(tupleSubject.arity() != children.size()){
			return;
		}
		for(int i = 0; i < children.size(); i++){
			children.get(i).initMatch(tupleSubject.get(i), ev);
		}
		firstMatch = hasNext = true;
	}
	
	public Type getType(Evaluator ev) {
		Type fieldTypes[] = new Type[children.size()];
		for(int i = 0; i < children.size(); i++){
			fieldTypes[i] = children.get(i).getType(ev);
		}
		return ev.tf.tupleType(fieldTypes);
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
	
	public boolean next() {
		checkInitialized();
		
		if(!(firstMatch || hasNext))
			return false;
		firstMatch = false;
		
		hasNext =  matchChildren(((ITuple) subject).iterator(), children.iterator(), ev);
			
		return hasNext;
	}
}

/* package */ class AbstractPatternMap extends AbstractPattern implements MatchPattern {
	private java.util.List<AbstractPattern> children;
	
	AbstractPatternMap(java.util.List<AbstractPattern> children){
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
	
	public IValue toIValue(Evaluator ev){
		IValue[] vals = new IValue[children.size()];
		for (int i = 0; i < children.size(); i++) {
			 vals[i] =  children.get(i).toIValue(ev);
		 }
		return null; //TODO: make correct
	}
	
	public Type getType(Evaluator ev) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean next(){
		checkInitialized();
		throw new RascalBug("AbstractPatternMap.match not implemented");
	}
}

/* package */ class AbstractPatternQualifiedName extends AbstractPattern implements MatchPattern {
	private org.meta_environment.rascal.ast.QualifiedName name;
	private Type type;
	private boolean debug = false;
	
	AbstractPatternQualifiedName(org.meta_environment.rascal.ast.QualifiedName qualifiedName){
		this.name = qualifiedName;
		GlobalEnvironment env = GlobalEnvironment.getInstance();
		Result patRes = env.getVariable(name);
	    boolean boundBeforeConstruction = (patRes != null) && (patRes.value != null);
	    type = (boundBeforeConstruction) ? patRes.type : TypeFactory.getInstance().voidType();
	}
	
	public Type getType(Evaluator ev) {
		return type;
	}
	
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String>();
		res.addFirst(name.toString());
		return res;
	}
	
	public IValue toIValue(Evaluator ev){
		throw new UnsupportedOperationException("toIValue on Variable");
	}
	
	public String getName(){
		return name.toString();
	}
	
	public boolean next(){
		checkInitialized();
		hasNext = false;
		if(debug)System.err.println("AbstractPatternQualifiedName.match: " + name);
		GlobalEnvironment env = GlobalEnvironment.getInstance();
		
		Result varRes = env.getVariable(name);
		if((varRes == null) || (varRes.value == null)){
			if(debug)System.err.println("name= " + name + ", subject=" + subject + ",");
			type = subject.getType();
			env.top().storeVariable(name.toString(),ev.result(type, subject));
			return true;
		} else {
			IValue varVal = varRes.value;
			if(debug)System.err.println("AbstractPatternQualifiedName.match: " + name + ", subject=" + subject + ", value=" + varVal);
			if (subject.getType().isSubtypeOf(varRes.type)) {
				if(debug)System.err.println("returns " + ev.equals(ev.result(subject.getType(),subject), varRes));
				return ev.equals(ev.result(subject.getType(),subject), varRes);
			} else
				return false;
		}
	}
	
	public String toString(){
		return name + "==" + subject;
	}
}

/* package */class AbstractPatternTypedVariable extends AbstractPattern implements MatchPattern {
	private Name name;
	org.eclipse.imp.pdb.facts.type.Type declaredType;
	private boolean debug = false;

	AbstractPatternTypedVariable(org.eclipse.imp.pdb.facts.type.Type type2, Name name) {
		this.declaredType = type2;
		this.name = name;
	}
	
	public Type getType(Evaluator ev) {
		return declaredType;
	}
	
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String>();
		res.addFirst(name.toString());
		return res;
	}
	
	public IValue toIValue(Evaluator ev){
		throw new UnsupportedOperationException("toIValue on Variable");
	}
	
	public String getName(){
		return name.toString();
	}

	public boolean next() {
		checkInitialized();
		hasNext = false;
		if(debug)System.err.println("AbstractTypedVariable.match: " + subject + "(type=" + subject.getType() + ") with " + declaredType + " " + name);
		
		if (subject.getType().isSubtypeOf(declaredType)) {
			GlobalEnvironment.getInstance().top().storeVariable(name, ev.result(declaredType, subject));
			if(debug)System.err.println("matches");
			return true;
		}
		if(debug)System.err.println("no match");
		return false;
	}
	
	public String toString(){
		return declaredType + " " + name + "==" + subject;
	}
}

public class AbstractPatternEvaluator extends NullASTVisitor<AbstractPattern> {

	private Evaluator ev;
	
	AbstractPatternEvaluator(Evaluator evaluator){
		ev = evaluator;
	}
	
	public boolean isPattern(org.meta_environment.rascal.ast.Expression pat){
		return (pat.isLiteral() && ! pat.getLiteral().isRegExp()) || 
		       pat.isCallOrTree() || pat.isList() || 
		       pat.isSet() || pat.isMap() || pat.isTuple() ||
		       pat.isQualifiedName() || pat.isTypedVariable();
	}
	
	@Override
	public AbstractPattern visitExpressionLiteral(Literal x) {
		return new AbstractPatternLiteral(x.getLiteral().accept(ev).value);
	}
	
	@Override
	public AbstractPattern visitExpressionCallOrTree(CallOrTree x) {
		org.meta_environment.rascal.ast.QualifiedName N = x.getQualifiedName();
	//	if(N.toString().equals("search")){
	//		return new AbstractPatternSearch(visitElements(x.getArguments()));
	//	} else {
			return new AbstractPatternNode(N, visitElements(x.getArguments()));
	//	}
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
		return new AbstractPatternList(visitElements(x.getElements()));
	}
	
	@Override
	public AbstractPattern visitExpressionSet(Set x) {
		return new AbstractPatternSet(visitElements(x.getElements()));
	}
	
	@Override
	public AbstractPattern visitExpressionTuple(Tuple x) {
		return new AbstractPatternTuple(visitElements(x.getElements()));
	}
	
	@Override
	public AbstractPattern visitExpressionMap(Map x) {
		throw new RascalBug("Map in pattern not yet implemented");
	}
	
	@Override
	public AbstractPattern visitExpressionQualifiedName(QualifiedName x) {
		org.meta_environment.rascal.ast.QualifiedName name = x.getQualifiedName();
		Type signature = ev.tf.tupleType(new Type[0]);
		 
		 if (ev.isTreeConstructorName(name, signature)) {
			 return new AbstractPatternNode(name, new java.util.ArrayList<AbstractPattern>());
		 } else {
			 return new AbstractPatternQualifiedName(x.getQualifiedName());
		 }
	}
	@Override
	public AbstractPattern visitExpressionArea(Area x) {
		throw new UnsupportedOperationException("Areas in pattern");
	}
	@Override
	public AbstractPattern visitExpressionTypedVariable(TypedVariable x) {
		return new AbstractPatternTypedVariable(x.getType().accept(ev.te), x.getName());
	}
}
