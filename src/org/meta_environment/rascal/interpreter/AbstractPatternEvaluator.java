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
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.ast.Expression.List;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Expression.Map;
import org.meta_environment.rascal.ast.Expression.QualifiedName;
import org.meta_environment.rascal.ast.Expression.Set;
import org.meta_environment.rascal.ast.Expression.Tuple;
import org.meta_environment.rascal.ast.Expression.TypedVariable;
import org.meta_environment.rascal.interpreter.env.EvalResult;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;
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

/* package */ class AbstractPattern {
	
	protected IValue subject = null;
	protected Evaluator ev = null;
	protected boolean initialized = false;
	protected boolean firstMatch = true;
	
	public void initMatch(IValue subject, Evaluator ev){
		this.subject = subject;
		this.ev = ev;
		this.initialized = true;
		this.firstMatch = true;
	}
	
	protected void checkInitialized(){
		if(!initialized){
			throw new RascalBug("hasNext or match called before initMatch");
		}
	}
	
	public boolean hasNext() {
		return initialized && firstMatch;
	}
	
	boolean matchChildren(Iterator<IValue> subjChildren, Iterator<MatchPattern> patChildren, Evaluator ev){
		while (patChildren.hasNext()) {
			if (!patChildren.next().next()){
				return false;
			}
		}
		return true;
	}
	
}
/* package */ class AbstractPatternLiteral extends AbstractPattern implements MatchPattern {

	private IValue literal;
	
	AbstractPatternLiteral(IValue literal){
		this.literal = literal;
	}
	
	public Type getType(Evaluator ev) {
			return literal.getType();
	}
	
	public boolean next(){
		checkInitialized();
		firstMatch = false;
		//System.err.println("AbstractPatternLiteral.match: " + subject);
		if (subject.getType().isSubtypeOf(literal.getType())) {
			return ev.equals(ev.result(subject), ev.result(literal));
		}
		return false;
	}
	
	public IValue getLiteral(){
		return literal;
	}
	
	public String toString(){
		return "pattern: " + literal;
	}
}

/* package */ class AbstractPatternNode extends AbstractPattern implements MatchPattern {
	private org.meta_environment.rascal.ast.QualifiedName name;
	private java.util.List<MatchPattern> children;
	
	AbstractPatternNode(org.meta_environment.rascal.ast.QualifiedName qualifiedName, java.util.List<MatchPattern> children){
		this.name = qualifiedName;
		this.children = children;
	}
	
	@Override
	public void initMatch(IValue subject, Evaluator ev){
		super.initMatch(subject, ev);
		
		if(!(subject.getType().isNodeType() || subject.getType().isAbstractDataType())){
			return;
		}
		INode treeSubject = (INode) subject;
		if(treeSubject.arity() != children.size()){
			return;
		}
		
		for (int i = 0; i < children.size(); i++){
			children.get(i).initMatch(treeSubject.get(i), ev);
		}
	}
	
	public Type getType(Evaluator ev) {
		 Type[] types = new Type[children.size()];

		 for (int i = 0; i < children.size(); i++) {
			 types[i] =  children.get(i).getType(ev);
		 }
		 
		 Type signature = ev.tf.tupleType(types);
		 
		 if (ev.isTreeConstructorName(name, signature)) {
			 return ev.env.getConstructor(name.toString(), signature);
		 } else {
			 return ev.tf.nodeType();
		 }
	}
	
	public boolean next(){
		checkInitialized();
		firstMatch = false;
		Type stype = subject.getType();
	
		if (!stype.isNodeType() && !stype.isAbstractDataType()){
			return false;
		}

		INode subjTree = (INode) subject;
		
		if (name.toString().equals(subjTree.getName().toString()) && 
			children.size() == subjTree.arity()){
			return matchChildren(subjTree.getChildren().iterator(), children.iterator(), ev);
		}
		return false;
	}
}

/* package */ class AbstractPatternList extends AbstractPattern implements MatchPattern {
	private java.util.List<MatchPattern> children;	// The elements of this list pattern
	private int patternSize;						// The number of elements in this list pattern
	private IList listSubject;						// The subject as list
	private int subjectSize;						// Length of the subject
	private int minSubjectSize;					// Minimum subject length for this pattern to match
	private boolean [] isListVar;					// Determine which elements are list variables
	private boolean hasListVar;					// Any list variables in this pattern?
	private HashSet<String> listVars;				// Names of list variables declared in this pattern
	private int [] listVarStart;					// Cursor start position list variable; indexed by pattern position
	private int [] listVarLength;					// Current length matched by list variable
	private int [] listVarMinLength;				// Minimal length to be matched by list variable
	private int [] listVarMaxLength;				// Maximal length that can be matched by list variable
	private int [] listVarOccurrences;				// Number of occurrences of list variable in the pattern

	private int subjectCursor;						// Cursor in the subject
	private int patternCursor;						// Cursor in the pattern
	
	private boolean firstMatch;					// First match after initialization?
	private boolean hasNext;						// Has this pattern alternatives for further matching?
	private boolean forward;						// Moving to the right?
	
	private boolean debug = false;
	
	AbstractPatternList(java.util.List<MatchPattern> children){
		this.children = children;					
		this.patternSize = children.size();			
		isListVar = new boolean[patternSize];		
		listVars = new HashSet<String>();			
		listVarStart = new int[patternSize];		
		listVarLength = new int[patternSize];		
		listVarMinLength = new int[patternSize];	
		listVarMaxLength = new int[patternSize];	
		listVarOccurrences = new int[patternSize];
	}
	
	@Override
	public void initMatch(IValue subject, Evaluator ev){
		super.initMatch(subject, ev);
		
		if (!subject.getType().isListType()) {
			initialized = true;
			hasNext = false;
			return;
		}
		
		listSubject = (IList) subject;
		subjectCursor = 0;
		patternCursor = 0;
		subjectSize = ((IList) subject).length();
		
		int nListVar = 0;
		hasListVar = false;
		/*
		 * Pass #1: determine the list variables
		 */
		for(int i = 0; i < patternSize; i++){
			MatchPattern child = children.get(i);
			isListVar[i] = false;
			if(child instanceof AbstractPatternTypedVariable && child.getType(ev).isListType()){
				/*
				 * An explicitly declared list variable.
				 */
				listVars.add(((AbstractPatternTypedVariable)child).getName());
				hasListVar = true;
				isListVar[i] = true;
				listVarOccurrences[i] = 1;
				nListVar++;
			} else if(child instanceof AbstractPatternQualifiedName){
				
				String name =((AbstractPatternQualifiedName)child).getName();
				if(listVars.contains(name)){
					/*
					 * A variable that was declared earlier in the pattern
					 */
					isListVar[i] = true;
			    	nListVar++;
			    	listVarOccurrences[i]++;
				} else  {
					GlobalEnvironment env = GlobalEnvironment.getInstance();
					EvalResult patRes = env.getVariable(name);
				         
				    if((patRes != null) && (patRes.value != null)){
				        IValue patVal = patRes.value;
				        if (patVal.getType().isListType()){
				        	/*
				        	 * A list variable declared in the current scope.
				        	 */
				        	isListVar[i] = true;
				        	nListVar++;
				        }
				    }
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
		return initialized && hasNext && (firstMatch || hasListVar);
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
	
	/**
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
			if(debug)System.err.println("child failse, subjectCursor=" + subjectCursor);
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
			
			MatchPattern child = children.get(patternCursor);
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
			 * Reference to a previously defined list variable
			 */
			if(isListVar[patternCursor] &&  child instanceof AbstractPatternQualifiedName){
				if(forward){
					listVarStart[patternCursor] = subjectCursor;
					
					String name = ((AbstractPatternQualifiedName)child).getName();
					GlobalEnvironment env = GlobalEnvironment.getInstance();
					EvalResult varRes = env.getVariable(name);
					IValue varVal = varRes.value;
				         
				    assert varVal != null && varVal.getType().isListType();
				    
				    int varLength = ((IList)varVal).length();
					listVarLength[patternCursor] = varLength;
							           
					if(subjectCursor + varLength > subjectSize){
						forward = false;
						patternCursor--;
					} else {
						matchBoundListVar((IList) varVal);
					}
				} else {
					subjectCursor = listVarStart[patternCursor];
					patternCursor--;
				}
			/*
			 * A binding occurrence of a list variable
			 */
			} else if(isListVar[patternCursor]){	
				
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
	private java.util.List<MatchPattern> children;
	private int patternSize;
	private boolean debug = false;
	private boolean hasNext;
	private ISet setSubject;
	private Type subjectElementType;
	private int nVar;
	private HashSet<String> allVars;
	private ISet fixedSetElements;
	private ISet availableSetElements;
	private String[] varName;
	private ISet[] varVal;
	private boolean[] isSetVar;
	private Iterator<?>[] varGen;
	private int currentVar;
	private int[] varOrder;
	
	AbstractPatternSet(java.util.List<MatchPattern> children){
		this.children = children;
		this.patternSize = children.size();
		allVars = new HashSet<String>();
		nVar = 0;
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
	
	// Compute a variable ordering: element variables should go before list variables
	// since element variables may not be empty.
	
	private void makeVarOrder(){
		int i = 0;
		
		for(int j = 0; j < nVar; j++){
			if(!isSetVar[j]){
				varOrder[i++] = j;
				
			}
		}
		for(int j = 0; j < nVar; j++){
			if(isSetVar[j]){
				varOrder[i++] = j;
			}
		}
		assert i == nVar;
	}
	
	@Override
	public void initMatch(IValue subject, Evaluator ev){
		
		super.initMatch(subject, ev);
		
		if (!subject.getType().isSetType()) {
			initialized = true;
			hasNext = false;
			return;
		}
		
		setSubject = (ISet) subject;
		subjectElementType = setSubject.getElementType();
		fixedSetElements = ev.vf.set(getType(ev));
		
		nVar = 0;
		varName = new String[patternSize];  			// Some overestimations
		isSetVar = new boolean[patternSize];
		varVal = new ISet[patternSize];
		varGen = new Iterator<?>[patternSize];
		varOrder = new int[patternSize];
		/*
		 * Pass #1: determine the (ordinary and set) variables in the pattern
		 */
		for(int i = 0; i < patternSize; i++){
			MatchPattern child = children.get(i);
			if(child instanceof AbstractPatternTypedVariable){
				Type childType = child.getType(ev);
				String name = ((AbstractPatternTypedVariable)child).getName();
				if(childType.isSetType() || childType.isSubtypeOf(subjectElementType)){
					/*
					 * An explicitly declared set or element variable.
					 */
					allVars.add(name);
					varName[nVar] = name;
					isSetVar[nVar] = childType.isSetType();
					nVar++;
				} else 
					throw new RascalTypeError(childType + " variable " + name + " not allowed in pattern of type " + setSubject.getType());
			} else if(child instanceof AbstractPatternQualifiedName){
				String name =((AbstractPatternQualifiedName)child).getName();
				if(allVars.contains(name)){
					/*
					 * A set/element variable that was declared earlier in the pattern,ignore it.
					 */
					//TODO: Give a warning?
				} else  {
					GlobalEnvironment env = GlobalEnvironment.getInstance();
					EvalResult varRes = env.getVariable(name);
				         
				    if((varRes != null) && (varRes.value != null)){
				        IValue varVal = varRes.value;
				        if (varVal.getType().isSetType()){
				        	/*
				        	 * A set variable declared in the current scope: add its elements
				        	 */
				        	fixedSetElements = fixedSetElements.union((ISet)varRes.value);
				        } else if(varVal.getType().isSubtypeOf(subjectElementType)){
				        	/*
				        	 * An element variable in the current scope, add its value.
				        	 */
				        	fixedSetElements = fixedSetElements.insert(varRes.value);
				        } else
				        	throw new RascalTypeError(varVal.getType() + " variable " + name + " not allowed in pattern of type " + setSubject.getType());
				    } else {
				    	throw new RascalRunTimeError("Unitialized variable " + name);
				    }
				}
			} else {
				assert child instanceof AbstractPatternLiteral;
				IValue lit = ((AbstractPatternLiteral)child).getLiteral();
				Type childType = child.getType(ev);
				if(!childType.isSubtypeOf(subjectElementType)){
					throw new RascalTypeError(lit + " not allowed in pattern of type " + setSubject.getType());
				}
				fixedSetElements = fixedSetElements.insert(lit);
			}
		}
		/*
		 * Pass #2: set up subset generation
		 */
		firstMatch = true;
		hasNext = fixedSetElements.isSubSet(setSubject);
		availableSetElements = setSubject.subtract(fixedSetElements);
		makeVarOrder();
	}
	
	@Override
	public boolean hasNext(){
		return initialized && hasNext;
	}
	
	private ISet available(){
		ISet avail = availableSetElements;
		for(int j = 0; j < currentVar; j++){
			avail = avail.subtract(varVal[varOrder[j]]);
		}
		return avail;
	}
	
	private boolean makeGen(int i, ISet elements){
		if(isSetVar[i]){
			varGen[i] = new SubSetGenerator(elements);
		} else {
			if(elements.size() == 0)
				return false;
			varGen[i] = new SingleElementGenerator(elements);
		}
		return true;
	}
	
	private void storeVar(int i, ISet elements){
		GlobalEnvironment env = GlobalEnvironment.getInstance();
		varVal[i] = elements;
		if(isSetVar[i]){
			env.storeVariable(varName[i], new EvalResult(elements.getType(), elements));
		} else {
			assert elements.size() == 1;
			IValue elem = elements.iterator().next();
			env.storeVariable(varName[i], new EvalResult(elements.getElementType(), elem));
		}
	}
	
	public boolean next(){
		checkInitialized();
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
					storeVar(0, availableSetElements);
					return true;
				}
				return false;
			}
			
			currentVar = 0;
			if(!makeGen(varOrder[currentVar], availableSetElements)){
				return false;
			}
		} else {
			currentVar = nVar - 2;
		}
		hasNext = true;

		//System.err.println("start assigning setVars");
	
		do {
			//System.err.println("currentVar=" + currentVar + "; nSetVar=" + nVar);
			int ocurrentVar = varOrder[currentVar];
			if(varGen[ocurrentVar].hasNext()){
				storeVar(ocurrentVar, (ISet)varGen[ocurrentVar].next());
				currentVar++;
				if(currentVar < nVar - 1){
					ocurrentVar = varOrder[currentVar];
					if(!makeGen(ocurrentVar, available())){
						varGen[ocurrentVar] = null;
						currentVar--;
					}
				}
			} else {
				varGen[ocurrentVar] = null;
				currentVar--;
			}
		} while(currentVar >= 0 && currentVar < nVar -1);
		
		
		if(currentVar < 0){
			hasNext = false;
			return false;
		}
		currentVar = nVar - 1;
		storeVar(varOrder[currentVar], available());
		
		//for(int i = 0; i < nVar; i++){
		//	System.err.println(i + ": " + varName[i] + "=" + varVal[i]);
		//}
		return true;
	}			
}

/* package */ class AbstractPatternTuple extends AbstractPattern implements MatchPattern {
	private java.util.List<MatchPattern> children;
	
	AbstractPatternTuple(java.util.List<MatchPattern> children){
		this.children = children;
	}
	
	@Override
	public void initMatch(IValue subject, Evaluator ev){
		super.initMatch(subject, ev);
		
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
	}
	
	public Type getType(Evaluator ev) {
		Type fieldTypes[] = new Type[children.size()];
		for(int i = 0; i < children.size(); i++){
			fieldTypes[i] = children.get(i).getType(ev);
		}
		return ev.tf.tupleType(fieldTypes);
	}
	
	public boolean next() {
		checkInitialized();
		firstMatch = false;
		if (subject.getType().isTupleType()
				&& ((ITuple) subject).arity() == children.size()) {
			return matchChildren(((ITuple) subject).iterator(), children.iterator(), ev);
		}
		return false;
	}
}

/* package */ class AbstractPatternMap extends AbstractPattern implements MatchPattern {
	private java.util.List<MatchPattern> children;
	
	AbstractPatternMap(java.util.List<MatchPattern> children){
		this.children = children;
	}
	
	public Type getType(Evaluator ev) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean next(){
		checkInitialized();
		firstMatch = false;
		throw new RascalBug("AbstractPatternMap.match not implemented");
	}
}

/* package */ class AbstractPatternQualifiedName extends AbstractPattern implements MatchPattern {
	private org.meta_environment.rascal.ast.QualifiedName name;
	private boolean boundBeforeConstruction;
	private Type type;
	private boolean debug = false;
	
	AbstractPatternQualifiedName(org.meta_environment.rascal.ast.QualifiedName qualifiedName){
		this.name = qualifiedName;
		GlobalEnvironment env = GlobalEnvironment.getInstance();
		EvalResult patRes = env.getVariable(name);
	    boundBeforeConstruction = (patRes != null) && (patRes.value != null);
	    type = (boundBeforeConstruction) ? patRes.type : TypeFactory.getInstance().voidType();
	}
	
	public Type getType(Evaluator ev) {
		return type;
	}
	
	public String getName(){
		return name.toString();
	}
	
	public boolean next(){
		checkInitialized();
		if(debug)System.err.println("AbstractPatternQualifiedName.match: " + name + ", firstMatch=" + firstMatch + ", boundBeforeConstruction=" + boundBeforeConstruction);
        GlobalEnvironment env = GlobalEnvironment.getInstance();
		
		if(firstMatch && !boundBeforeConstruction){ //TODO: wrong in some cases?
			firstMatch = false;
			if(debug)System.err.println("name= " + name + ", subject=" + subject + ",");
			env.top().storeVariable(name.toString(),ev.result(subject.getType(), subject));
       	 	return true;
		}
		
		EvalResult patRes = env.getVariable(name);		
         
        if((patRes != null) && (patRes.value != null)){
        	 IValue patVal = patRes.value;
        		if(debug)System.err.println("AbstractPatternQualifiedName.match: " + name + ", subject=" + subject + ", value=" + patVal);
        		
        	 if (subject.getType().isSubtypeOf(patVal.getType())) {
        		if(debug)System.err.println("returns " + ev.equals(ev.result(subject.getType(),subject), patRes));
        		 return ev.equals(ev.result(subject.getType(),subject), patRes);
        	 }
         }
        return false;
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
	
	public String getName(){
		return name.toString();
	}

	public boolean next() {
		checkInitialized();
		firstMatch = false;
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

public class AbstractPatternEvaluator extends NullASTVisitor<MatchPattern> {

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
	public MatchPattern visitExpressionLiteral(Literal x) {
		return new AbstractPatternLiteral(x.getLiteral().accept(ev).value);
	}
	
	@Override
	public MatchPattern visitExpressionCallOrTree(CallOrTree x) {
		return new AbstractPatternNode(x.getQualifiedName(), visitElements(x.getArguments()));
	}
	
	private java.util.List<MatchPattern> visitElements(java.util.List<org.meta_environment.rascal.ast.Expression> elements){
		ArrayList<MatchPattern> args = new java.util.ArrayList<MatchPattern>(elements.size());
		
		int i = 0;
		for(org.meta_environment.rascal.ast.Expression e : elements){
			args.add(i++, e.accept(this));
		}
		return args;
	}
	
	@Override
	public MatchPattern visitExpressionList(List x) {
		return new AbstractPatternList(visitElements(x.getElements()));
	}
	
	@Override
	public MatchPattern visitExpressionSet(Set x) {
		return new AbstractPatternSet(visitElements(x.getElements()));
	}
	
	@Override
	public MatchPattern visitExpressionTuple(Tuple x) {
		return new AbstractPatternTuple(visitElements(x.getElements()));
	}
	
	@Override
	public MatchPattern visitExpressionMap(Map x) {
		throw new RascalBug("Map in pattern not yet implemented");
	}
	
	@Override
	public MatchPattern visitExpressionQualifiedName(QualifiedName x) {
		org.meta_environment.rascal.ast.QualifiedName name = x.getQualifiedName();
		Type signature = ev.tf.tupleType(new Type[0]);
		 
		 if (ev.isTreeConstructorName(name, signature)) {
			 return new AbstractPatternNode(name, new java.util.ArrayList<MatchPattern>());
		 } else {
			 return new AbstractPatternQualifiedName(x.getQualifiedName());
		 }
	}
	
	@Override
	public MatchPattern visitExpressionTypedVariable(TypedVariable x) {
		return new AbstractPatternTypedVariable(x.getType().accept(ev.te), x.getName());
	}
}
