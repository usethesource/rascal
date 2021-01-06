/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Emilie Balland - (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariable;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.values.parsetrees.SymbolAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class ListPattern extends AbstractMatchingResult  {
  private List<IMatchingResult> patternChildren;  // The elements of this list pattern
  private int patternSize;            // The number of elements in this list pattern
  private int delta = 1;              // increment to next list elements:
  // delta=1 abstract lists
  // delta=2 skip layout between elements
  // delta=4 skip layout, separator, layout between elements
  private int reducedPatternSize;     //  (patternSize + delta - 1)/delta                                    
  private IList listSubject;          // The subject as list
  private Type listSubjectType;       // The type of the subject
  private int subjectSize;            // Length of the subject
  private int reducedSubjectSize;     // (subjectSize + delta - 1) / delta
  private boolean [] isListVar;       // Determine which elements are list or variables
  private boolean [] isBindingVar;    // Determine which elements are binding occurrences of variables
  private boolean[] plusList;
  private String [] varName;          // Name of ith variable
  private HashSet<String> allVars;    // Names of list variables declared in this pattern
  private int [] listVarStart;        // Cursor start position list variable; indexed by pattern position
  private int [] listVarLength;       // Current length matched by list variable
  private int [] listVarMinLength;    // Minimal length to be matched by list variable
  private int [] listVarMaxLength;    // Maximal length that can be matched by list variable
  private int [] listVarOccurrences;  // Number of occurrences of list variable in the pattern

  private int subjectCursor;          // Cursor in the subject
  private int patternCursor;          // Cursor in the pattern

  private boolean firstMatch;         // First match after initialization?
  private boolean forward;            // Moving to the right?

  private boolean debug = false;
  private Type staticListSubjectElementType;
  private Type staticListSubjectType;
  private final boolean bindTypeParameters;
 


  public ListPattern(IEvaluatorContext ctx, AbstractAST x, List<IMatchingResult> list, boolean bindTypeParameters){
    this(ctx, x, list, 1, bindTypeParameters);  // Default delta=1; Set to 2 to run DeltaListPatternTests
  }

  ListPattern(IEvaluatorContext ctx, AbstractAST x, List<IMatchingResult> list, int delta, boolean bindTypeParameters){
    super(ctx, x);

    if(delta < 1)
      throw new ImplementationError("Wrong delta");
    this.bindTypeParameters = bindTypeParameters;
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
  public List<IVarPattern> getVariables(){
    java.util.LinkedList<IVarPattern> res = new java.util.LinkedList<IVarPattern> ();
    for (int i = 0; i < patternChildren.size(); i += delta) {
      res.addAll(patternChildren.get(i).getVariables());
    }
    return res;
  }

  public static boolean isAnyListType(Type type){
    return type.isList()|| isConcreteListType(type); 
  }

  public static boolean isConcreteListType(Type type){
    if (type instanceof NonTerminalType) {
      IConstructor sym = ((NonTerminalType)type).getSymbol();
      return SymbolAdapter.isAnyList(sym);
    }
    return false;
  }

  @Override
  public void initMatch(Result<IValue> subject){
    if(debug) {
      System.err.println("List: initMatch: subject=" + subject);
    }

    super.initMatch(subject);

    if (!subject.getValue().getType().isList()) {
      hasNext = false;
      return;
    }
    listSubject = (IList) subject.getValue();
    listSubjectType = listSubject.getType(); 
    staticListSubjectType = subject.getStaticType();
    staticListSubjectElementType = staticListSubjectType.isList() ? subject.getStaticType().getElementType() : tf.valueType();

    subjectCursor = 0;
    patternCursor = 0;
    subjectSize = ((IList) subject.getValue()).length();
    reducedSubjectSize = (subjectSize + delta - 1) / delta;

    if (debug) {
      System.err.println("reducedPatternSize=" + reducedPatternSize);
      System.err.println("reducedSubjectSize=" + reducedSubjectSize);
    }

    isListVar = new boolean[patternSize]; 
    plusList = new boolean[patternSize];
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
      plusList[i] = false;
      isBindingVar[i] = false;
      Environment env = ctx.getCurrentEnvt();

      if (child instanceof TypedMultiVariablePattern) {
          TypedMultiVariablePattern tmv = (TypedMultiVariablePattern) child;
          
          // now we know what we are, a list multi variable!
          child = new DesignatedTypedMultiVariablePattern(ctx, (Expression) tmv.getAST(), tf.listType(tmv.getType(env,  null)), tmv.getName(), bindTypeParameters); 
          
          // cache this information for the next round, we'll still be a list
          patternChildren.set(i, child);
      }
      
      if (child instanceof DesignatedTypedMultiVariablePattern) {
         DesignatedTypedMultiVariablePattern tmvVar = (DesignatedTypedMultiVariablePattern) child;
        Type tmvType = child.getType(env, null);
        String name = tmvVar.getName();

        varName[i] = name;
        isListVar[i] = true;
        listVarOccurrences[i] = 1;
       
        ++nListVar;

        if (!tmvVar.isAnonymous() && allVars.contains(name)) {
          throw new RedeclaredVariable(name, getAST());
        } 
        else if (tmvType.comparable(listSubject.getType())) {
          if (!tmvVar.isAnonymous()) {
            allVars.add(name);
          }
          isBindingVar[i] = true;
        } else {
          hasNext = false;
          return;
        }
      }
      else if (child instanceof MultiVariablePattern){
        MultiVariablePattern multiVar = (MultiVariablePattern) child;
        String name = multiVar.getName();
        varName[i] = name;
        isListVar[i] = true;
        nListVar++;

        if(!multiVar.isAnonymous() && allVars.contains(name)){
          isBindingVar[i] = false;
        } else if(multiVar.isAnonymous()){
          isBindingVar[i] = true;
        } else {
          allVars.add(name);
          Result<IValue> varRes = env.getFrameVariable(name);

          if (varRes == null || multiVar.bindingInstance()) {
            isBindingVar[i] = true;
          } else {
            isBindingVar[i] = false;
            Type varType = varRes.getStaticType();
            if (isAnyListType(varType)){  
              if (!varType.comparable(listSubjectType)) {     
                hasNext = false;
                return;
              }
            } 
          }
        }
      } 
      else if (child instanceof ConcreteListVariablePattern) {
        ConcreteListVariablePattern listVar = (ConcreteListVariablePattern) child;
        String name = listVar.getName();
        varName[i] = name;
        isListVar[i] = true;
        if (!listVar.isAnonymous()) {
          allVars.add(name);
        }
        plusList[i] = listVar.isPlusList();
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
          Result<IValue> varRes = env.getFrameVariable(name);

          if(varRes == null || qualName.bindingInstance()){ 
            // A completely new non-list variable, nothing to do
          } else {
            Type varType = varRes.getStaticType();
            if (isAnyListType(varType)){  
              /*
               * A variable declared in the current scope.
               */
              if(varType.comparable(listSubjectType)){     
                isListVar[i] = true;
                isBindingVar[i] = varRes.getValue() == null;
                nListVar++;                 
              } else {
                hasNext = false;
                return;
              }
            } else {
              if(varType instanceof NonTerminalType){
                // suppress comparable test for Nonterminal types
                // TODO: this should be done better
              } else
                if(!varType.comparable(staticListSubjectElementType)){
                  hasNext = false;
                  return;
                }
            }
          }
        }
      }
      else if(child instanceof VariableBecomesPattern){
        // Nothing to do
      } 
      else {
        if (debug) {
          System.err.println("List: child " + child);
          System.err.println("List: child is a" + child.getClass());
        }
        
        java.util.List<IVarPattern> childVars = child.getVariables();
        if(!childVars.isEmpty()){
          for(IVarPattern vp : childVars){ // TODO: This does not profit from extra information
            allVars.add(vp.name());
          }
          isListVar[i] = false;
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
        listVarLength[i] = plusList[i] ? 1 : 0;
        listVarMaxLength[i] = delta * Math.max(reducedSubjectSize - (reducedPatternSize - nListVar), 0);
        listVarMinLength[i] = delta * ((nListVar == 1) ? Math.max(reducedSubjectSize - reducedPatternSize - 1, listVarLength[i]) : listVarLength[i]);

        if (debug) {
          System.err.println("listvar " + i + " min= " + listVarMinLength[i] + " max=" + listVarMaxLength[i]);
        }
      }
    }

    firstMatch = true;

    hasNext = subject.getValue().getType().isList() && 
        reducedSubjectSize >= reducedPatternSize - nListVar;

        if(debug) {
          System.err.println("List: hasNext=" + hasNext);
        }
  }

  @Override
  public Type getType(Environment env, HashMap<String,IVarPattern> patternVars) {                      
    if(patternSize == 0){
      return tf.listType(tf.voidType());
    }

    Type elemType = tf.voidType();
    for(int i = 0; i < patternSize; i += delta){
      IMatchingResult child = patternChildren.get(i);
      Type childType = child.getType(env, patternVars);
      patternVars = merge(patternVars, patternChildren.get(i).getVariables());
      boolean isMultiVar = child instanceof MultiVariablePattern || child instanceof DesignatedTypedMultiVariablePattern;
      
      if(childType.isList() && isMultiVar){
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
      if(!previousBinding.get(i).equals(listSubject.get(subjectCursor + i))){
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

    //  int reducedLength = (length == 0) ? 0 : ((length < delta) ? 1 : Math.max(length - delta + 1, 0));  // round to nearest unskipped element

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
            //            hasNext = (subjectCursor > subjectSize) && (patternCursor > patternSize); // JURGEN GUESSES THIS COULD BE RIGHT
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
          ctx.getCurrentEnvt().getFrameVariable(varName[patternCursor]).getStaticType().isList()){
        if(forward){
          listVarStart[patternCursor] = subjectCursor;

          Result<IValue> varRes = ctx.getCurrentEnvt().getFrameVariable(varName[patternCursor]);
          IValue varVal = varRes.getValue();

          if(varRes.getStaticType().isList()){
            assert varVal != null && varVal.getType().isList();

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
