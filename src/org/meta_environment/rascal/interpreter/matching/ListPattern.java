package org.meta_environment.rascal.interpreter.matching;

import java.util.HashSet;
import java.util.List;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.result.ResultFactory;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.types.ConcreteSyntaxType;
import org.meta_environment.uptr.SymbolAdapter;

public class ListPattern extends AbstractMatchingResult  {
	private List<IMatchingResult> patternChildren;	// The elements of this list pattern
	private int patternSize;						// The number of elements in this list pattern
	private int delta = 1;                        	// increment to next list elements:
	                                                // delta=1 abstract lists
	                                                // delta=2 skip layout between elements
	                                                // delta=4 skip layout, separator, layout between elements
	private int reducedPatternSize;               	//  (patternSize + delta - 1)/delta                                    
	private IList listSubject;						// The subject as list
	private Type listSubjectType;					// The type of the subject
	private Type listSubjectElementType;			// The type of list elements
	private int subjectSize;						// Length of the subject
	private int reducedSubjectSize;                	// (subjectSize + delta - 1) / delta
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
	
	private boolean firstMatch;						// First match after initialization?
	private boolean forward;						// Moving to the right?
	
	private boolean debug = false;

	
	public ListPattern(IValueFactory vf, IEvaluatorContext ctx, List<IMatchingResult> list){
		this(vf,ctx, list, 1);  // Default delta=1; Set to 2 to run DeltaListPatternTests
	}
	
	ListPattern(IValueFactory vf, IEvaluatorContext ctx, List<IMatchingResult> list, int delta){
		super(vf, ctx);
		if(delta < 1)
			throw new ImplementationError("Wrong delta");
		this.delta = delta;
		this.patternChildren = list;					
		this.patternSize = list.size();
		this.reducedPatternSize = (patternSize + delta - 1) / delta;
		
		if (debug) {
			System.err.println("patternSize=" + patternSize);
			System.err.println("reducedPatternSize=" + reducedPatternSize);
		}
	}
	
	@Override
	public java.util.List<String> getVariables(){
		java.util.LinkedList<String> res = new java.util.LinkedList<String> ();
		for (int i = 0; i < patternChildren.size(); i += delta) {
			res.addAll(patternChildren.get(i).getVariables());
		 }
		return res;
	}
	
	@Override
	public IValue toIValue(Environment env){
		IValue[] vals = new IValue[patternChildren.size()];
		for (int i = 0; i < patternChildren.size(); i += delta) {
			 vals[i] =  patternChildren.get(i).toIValue(env);
		 }
		return vf.list(vals);
	}
	
	public static boolean isAnyListType(Type type){
		return type.isListType()|| isConcreteListType(type);        //<------ disabled to let other tests work.
	}
	
	public static boolean isConcreteListType(Type type){                      // <--- this code does not work because I donot understand the type structure of Tree
		
		if (type instanceof ConcreteSyntaxType) {
			SymbolAdapter sym = new SymbolAdapter(((ConcreteSyntaxType)type).getSymbol());
			return sym.isAnyList();
		}
		
		return false;
	}
	
	@Override
	public void initMatch(Result<IValue> subject){
		super.initMatch(subject);
		
		if(debug) {
			System.err.println("List: initMatch: subject=" + subject);
		}
		
		if (!subject.getType().isListType()) {
			hasNext = false;
			return;
		}
		listSubject = (IList) subject.getValue();
		listSubjectType = subject.getType(); // use static type here!
		listSubjectElementType = subject.getType().getElementType(); // use static type here!
		
		subjectCursor = 0;
		patternCursor = 0;
		subjectSize = ((IList) subject.getValue()).length();
		reducedSubjectSize = (subjectSize + delta - 1) / delta;
		
		if (debug) {
			System.err.println("reducedPatternSize=" + reducedPatternSize);
			System.err.println("reducedSubjectSize=" + reducedSubjectSize);
		}
		
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
		for(int i = 0; i < patternSize; i += delta){
			IMatchingResult child = patternChildren.get(i);
			isListVar[i] = false;
			isBindingVar[i] = false;
			Environment env = ctx.getCurrentEnvt();
			if(child instanceof TypedVariablePattern && isAnyListType(child.getType(env))){  // <------
				TypedVariablePattern patVar = (TypedVariablePattern) child;
				Type childType = child.getType(env);
				String name = patVar.getName();
				varName[i] = name;
				if(!patVar.isAnonymous() && allVars.contains(name)){
					throw new RedeclaredVariableError(name, getAST());
				}
				else if(childType.comparable(listSubject.getType())){                                   
					/*
					 * An explicitly declared list variable.
					 */
					if(!patVar.isAnonymous()) {
						allVars.add(name);
					}
					isListVar[i] = isAnyListType(childType);
					isBindingVar[i] = true;
					listVarOccurrences[i] = 1;
					nListVar++;
				} else {
					throw new UnexpectedTypeError(childType, listSubject.getType(), getAST());
				}
			} 
			else if(child instanceof MultiVariablePattern){
				MultiVariablePattern multiVar = (MultiVariablePattern) child;
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
			}
			else if (child instanceof ConcreteListVariablePattern) {
				ConcreteListVariablePattern listVar = (ConcreteListVariablePattern) child;
				String name = listVar.getName();
				varName[i] = name;
				isListVar[i] = true;
				if(!listVar.isAnonymous())
					allVars.add(name);
				isBindingVar[i] = true;
				listVarOccurrences[i] = 1;
				nListVar++;
			}
			else if(child instanceof QualifiedNamePattern){
				QualifiedNamePattern qualName = (QualifiedNamePattern) child;
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
					Result<IValue> varRes = env.getVariable(name);
					
					if(varRes == null){
						// A completely new variable, nothing to do
					} else {
				        Type varType = varRes.getType();
				        if (isAnyListType(varType)){                                   // <-----
				        	/*
				        	 * A variable declared in the current scope.
				        	 */
				        	if(varType.comparable(listSubjectType)){                   // <-- let this also work for concrete lists
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
				if (debug) {
					System.err.println("List: child " + child);
					System.err.println("List: child is a" + child.getClass());
				}
				Type childType = child.getType(env);
				
			    // TODO: pattern matching should be specialized such that matching appl(prod...)'s does not
				// need to use list matching on the fixed arity children of the application of a production
				if(!(childType instanceof ConcreteSyntaxType) && !childType.comparable(listSubjectElementType)){
					throw new UnexpectedTypeError(listSubjectElementType,childType, getAST());
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
		for(int i = 0; i < patternSize; i += delta){
			if(isListVar[i]){
				// TODO: reduce max length according to number of occurrences
				listVarMaxLength[i] = delta * Math.max(reducedSubjectSize - (reducedPatternSize - nListVar), 0);
				listVarLength[i] = 0;
				listVarMinLength[i] = delta * ((nListVar == 1) ? Math.max(reducedSubjectSize - reducedPatternSize - 1, 0) : 0);
				
				if (debug) {
					System.err.println("listvar " + i + " min= " + listVarMinLength[i] + " max=" + listVarMaxLength[i]);
				}
			}
		}
	
		firstMatch = true;

		hasNext = subject.getType().isListType() && 
		          reducedSubjectSize >= reducedPatternSize - nListVar;
		
		if(debug) {
			System.err.println("List: hasNext=" + hasNext);
		}
	}
	
	@Override
	public Type getType(Environment env) {                      
		if(patternSize == 0){
			return tf.listType(tf.voidType());
		}
		
		Type elemType = tf.voidType();
		for(int i = 0; i < patternSize; i += delta){
			Type childType = patternChildren.get(i).getType(env);
			
			if(childType.isListType()){
				elemType = elemType.lub(childType.getElementType());
			} else {
				elemType = elemType.lub(childType);
			}
		}
		if(debug) { 
			System.err.println("ListPattern.getType: " + tf.listType(elemType));
		}
		return tf.listType(elemType);
	}
	
	@Override
	public boolean hasNext(){
		if(debug) { 
			System.err.println("List: hasNext=" +  (initialized && hasNext));
		}
		return initialized && hasNext;
	}
	
	
	
	private void matchBoundListVar(IList previousBinding){

		if(debug) { 
			System.err.println("matchBoundListVar: " + previousBinding);
		}
		assert isListVar[patternCursor];
		
		int start = listVarStart[patternCursor];
		int length = listVarLength[patternCursor];
		
		for(int i = 0; i < previousBinding.length(); i += delta){
			if(debug)System.err.println("comparing: " + previousBinding.get(i) + " and " + listSubject.get(subjectCursor + i));
			if(!previousBinding.get(i).isEqual(listSubject.get(subjectCursor + i))){
				forward = false;
				listVarLength[patternCursor] = 0;
				patternCursor -= delta;
				if(debug)System.err.println("child fails");
				return;
			}
		}
		subjectCursor = start + length;
		if(debug)System.err.println("child matches, subjectCursor=" + subjectCursor);
		patternCursor += delta;
	}
	
	private IList makeSubList(int start, int length){
		assert isListVar[patternCursor];
		 
		
		// TODO: wrap concrete lists in an appl(list( )) again.
		if(start > subjectSize) return listSubject.sublist(0,0);
		
		return listSubject.sublist(start, length);
	}
	
	/*
	 * We are positioned in the pattern at a list variable and match it with
	 * the current subject starting at the current position.
	 * On success, the cursors are advanced.
	 * On failure, switch to backtracking (forward = false) mode.
	 */
	private void matchBindingListVar(IMatchingResult child){
		
		assert isListVar[patternCursor];
		
		int start = listVarStart[patternCursor];
		int length = listVarLength[patternCursor];
		
	//	int reducedLength = (length == 0) ? 0 : ((length < delta) ? 1 : Math.max(length - delta + 1, 0));  // round to nearest unskipped element

		int reducedLength = (length <= 1) ? length : (length - (length-1)%delta);
		
		if (debug) {
			System.err.println("length=" + length);
			System.err.println("reducedLength=" + reducedLength);
		}		
		
		IList sublist = makeSubList(start, reducedLength);

		if(debug)System.err.println("matchBindingListVar: init child #" + patternCursor + " (" + child + ") with " + sublist);
		// TODO : check if we can use a static type here!?
		child.initMatch(ResultFactory.makeResult(sublist.getType(), sublist, ctx));
	
		if(child.next()){
			subjectCursor = start + length;
			if(debug)System.err.println("child matches, subjectCursor=" + subjectCursor);
			patternCursor += delta;
		} else {
			forward = false;
			listVarLength[patternCursor] = 0;
			patternCursor -= delta;
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
		if(debug)System.err.println("List.next: entering");
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
			
			if(debug)System.err.println("List.do: patternCursor=" + patternCursor + ", subjectCursor=" + subjectCursor);
			
			if(forward){
				if(patternCursor >= patternSize){
					if(subjectCursor >= subjectSize){
						if(debug)System.err.println(">>> match returns true");
//						hasNext = (subjectCursor > subjectSize) && (patternCursor > patternSize); // JURGEN GUESSES THIS COULD BE RIGHT
						return true;
					}
					forward = false;
					patternCursor -= delta;
				}
			} else {
				if(patternCursor >= patternSize){
					patternCursor -= delta;
					subjectCursor -= delta; // Ok?
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
			
			IMatchingResult child = patternChildren.get(patternCursor);
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
					if(patternCursor == patternSize - 1){
						if (debug) {
							System.err.println("subjectSize=" + subjectSize);
							System.err.println("subjectCursor=" + subjectCursor);
						}
						listVarLength[patternCursor] =  Math.max(subjectSize - subjectCursor, 0);
					} else {
						listVarLength[patternCursor] = listVarMinLength[patternCursor];
					}
				} else {
					listVarLength[patternCursor] += delta;
					forward = true;
				}
				if(debug)System.err.println("list var: start: " + listVarStart[patternCursor] +
						           ", len=" + listVarLength[patternCursor] + 
						           ", minlen=" + listVarMinLength[patternCursor] +
						           ", maxlen=" + listVarMaxLength[patternCursor]);
				if(listVarLength[patternCursor] > listVarMaxLength[patternCursor]  ||
				   listVarStart[patternCursor] + listVarLength[patternCursor] >= subjectSize + delta){
					
					subjectCursor = listVarStart[patternCursor];
					if(debug)System.err.println("Length failure, subjectCursor=" + subjectCursor);
					
					forward = false;
					listVarLength[patternCursor] = 0;
					patternCursor -= delta;
				} else {
					matchBindingListVar(child);
				}
			
			/*
			 * Reference to a previously defined list variable
			 */
			} 
			else if(isListVar[patternCursor] && 
					!isBindingVar[patternCursor] && 
					ctx.getCurrentEnvt().getVariable(varName[patternCursor]).getType().isListType()){
				if(forward){
					listVarStart[patternCursor] = subjectCursor;
					
					Result<IValue> varRes = ctx.getCurrentEnvt().getVariable(varName[patternCursor]);
					IValue varVal = varRes.getValue();
					
					if(varRes.getType().isListType()){
					    assert varVal != null && varVal.getType().isListType();
					    
					    int varLength = ((IList)varVal).length();
						listVarLength[patternCursor] = varLength;
								           
						if(subjectCursor + varLength > subjectSize){
							forward = false;
							patternCursor -= delta;
						} else {
							matchBoundListVar((IList) varVal);
						}
					}
				} else {
					subjectCursor = listVarStart[patternCursor];
					patternCursor -= delta;
				}
			
			/*
			 * Any other element of the pattern
			 */
			} else {
				if(forward && subjectCursor < subjectSize){
					if(debug)System.err.println("AbstractPatternList.match: init child " + patternCursor + " with " + listSubject.get(subjectCursor));
					IValue childValue = listSubject.get(subjectCursor);
					// TODO: check if we can use a static type here?!
					child.initMatch(ResultFactory.makeResult(childValue.getType(), childValue, ctx));
					if(child.next()){
						subjectCursor += delta;
						patternCursor += delta;
						if(debug)System.err.println("AbstractPatternList.match: child matches, subjectCursor=" + subjectCursor);
					} else {
						forward = false;
						//  why is here no subjectCursor -= delta;  needed?
						patternCursor -= delta;
					}
				} else {
					if(subjectCursor < subjectSize && child.next()){
						if(debug)System.err.println("child has next:" + child);
						forward = true;
						subjectCursor += delta;
						patternCursor += delta;
					} else {
						if(debug)System.err.println("child has no next:" + child);
						forward = false;
						subjectCursor -= delta;
						patternCursor -= delta;
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
			for(int i = 0; i < Math.min(patternCursor,patternSize); i++){
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
