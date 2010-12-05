@bootstrapParser
module rascal::checker::constraints::Statement

import List;
import ParseTree;
import IO;
import rascal::types::Types;
import rascal::scoping::SymbolTable;
import rascal::checker::constraints::Constraints;
import rascal::checker::constraints::BuiltIns;
import rascal::checker::TreeUtils;
import rascal::syntax::RascalRascal;

//
// Collect constraints for the solve statement. All variables should be defined, 
// and the bound, if present, should be an integer.
//
//   isDefined(v1), ..., isDefined(vn), b : int, s : stmt[t]
// ------------------------------------------------------------
//    solve (v1,...,vn b) s : stmt[void]
//
public ConstraintBase gatherSolveStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, {QualifiedName ","}+ vars, Bound b, Statement body) {    
    // Step 1: Constraint the variable list. Definedness is checked during
    // symbol table generation, and no other constraints on the types are
    // given, so we need no logic here for this check.

    // Step 2: Constrain the bound. The bound should be an expression
    // of integer type. It is optional, so we need to see if a bound
    // was given before constraining the type.
    if ((Bound)`; <Expression e>` := b) {
        cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeIntType());
    }
    
    // Step 3: Constraint the ultimate type of the solve statement. It 
    // should be stmt[void]. 
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));
    
    return cs;
}

//
// Collect constraints for the for statement. All expressions should be of type bool.
//
//   isDefined(l), e1 : bool, ..., en : bool, s : stmt[t]
// ----------------------------------------------------------
//       l for (e1,...,en) s : stmt[list[void]]
//
public ConstraintBase gatherForStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Label l, {Expression ","}+ exps, Statement body) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.
    
    // Step 2: Constrain the expression list. Each expression should evaluate to
    // type bool.
    for (e <- exps) cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeBoolType());
    
    // Step 3: Constrain the ultimate type. It should be stmt[list[void]], 
    // NOT the body type, since we don't know if any iterations of the
    // loop are completed or if any values are inserted.
    //
    // TODO: This would be a good opportunity for static analysis, which could
    // allow for a more exact type.
    //
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeListType(makeVoidType())));

    return cs;
}  

//
// Collect constraints for the while statement. All expressions should be of type bool.
//
//   isDefined(l), e1 : bool, ..., en : bool, s : stmt[t]
// ----------------------------------------------------------
//       l while (e1,...,en) s : stmt[list[void]]
//
public ConstraintBase gatherWhileStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Label l, {Expression ","}+ exps, Statement body) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.
    
    // Step 2: Constrain the expression list. Each expression should evaluate to
    // type bool.
    for (e <- exps) cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeBoolType());
    
    // Step 3: Constrain the ultimate type. It should be stmt[list[void]], 
    // NOT the body type, since we don't know if any iterations of the
    // loop are completed or if any values are inserted.
    //
    // TODO: This would be a good opportunity for static analysis, which could
    // allow for a more exact type.
    //
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeListType(makeVoidType())));

    return cs;
}

//
// Collect constraints for the do while statement. The condition expression should be
// of type bool.
//
//   isDefined(l), s : stmt[t], e : bool
// ----------------------------------------------------------
//       l do s while (e) : stmt[list[void]]
//
public ConstraintBase gatherDoWhileStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Label l, Statement body, Expression e) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.
    
    // Step 2: Constrain the expression. It should evaluate to type bool.
    cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeBoolType());
    
    // Step 3: Constrain the ultimate type. It should be stmt[list[void]], 
    // NOT the body type, since we don't know if how many appends are 
    // performed and if they are of the same type.
    //
    // TODO: Since we know at least one iteration occurs, we could take the lub
    // of the append types, but we would need to ensure that an append occurs on
    // each path through the loop to do so. This may be another good target for
    // additional analysis.
    //
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeListType(makeVoidType())));

    return cs;
}

//
// Collect constraints for the if/then/else statement.
//
//   isDefined(l), e1 : bool, ..., en : bool,  tb : stmt[t1], tf : stmt[t2]
// -----------------------------------------------------------------------------
//    l if (e1,...,en) then tb else tf : stmt[lub(tb,tf)]
//
public ConstraintBase gatherIfThenElseStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Label l, {Expression ","}+ exps, Statement trueBody, Statement falseBody) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the expression list. Each expression should evaluate to
    // type bool.
    for (e <- exps) cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeBoolType());

    // Step 3: Constrain the ultimate type. It should be stmt[lub(t1,t2)], 
    // given a true branch type of stmt[t1] and a false branch type of stmt[t2].
    // We can assign this type since we know that one branch or the other must
    // be taken.
    <cs, ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
    Constraint c1 = TreeIsType(trueBody,trueBody@\loc,makeStatementType(t1));
    Constraint c2 = TreeIsType(falseBody,falseBody@\loc,makeStatementType(t2));
    Constraint c3 = LubOf([t1,t2],t3,sp@\loc);
    Constraint c4 = TreeIsType(sp,sp@\loc,makeStatementType(t3));
    cs.constraints = cs.constraints + { c1, c2, c3, c4 };

    return cs;
}

//
// Collect constraints for the if/then statement.
//
//    isDefined(l), e1 : bool, ..., en : bool,  tb : stmt[t1]
// -------------------------------------------------------------
//    l if (e1,...,en) then tb : stmt[void]
//
public ConstraintBase gatherIfThenStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Label l, {Expression ","}+ exps, Statement trueBody) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the expression list. Each expression should evaluate to
    // type bool.
    for (e <- exps) cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeBoolType());

    // Step 3: Constrain the ultimate type. It should be stmt[void], given
    // that we don't know statically if the true branch will be taken, so we
    // don't know if an actual value will be returned.
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));

    return cs;
}

//
// Collect constraints for the switch statement.
//
//    isDefined(l), e1 : t, c1 : case[p1,t1], ..., cn : case[pn,tn], bindable(t,p1), ..., bindable(t,pn)
// --------------------------------------------------------------------------------------------------------
//          l switch(e) { c1 ... cn } : stmt[void]
//
public ConstraintBase gatherSwitchStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Label l, Expression e, Case+ cases) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the expression. It can be of arbitrary type.
    <cs, t1> = makeFreshType(cs);
    cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t1);
    
    // Step 3: Constrain the cases. Each case presents a pattern (or is default).
    // The constraints indicate that the case has a type, t2, which is a case
    // type, and that the type of the case expression, t1, is bindable to the
    // pattern in the case. The branch types are also given in each case, and
    // are used to (possibly) calculate the ultimate type of the switch.
    list[RType] branchTypes = [ ];
    for (c <- cases) {
        <cs, t2> = makeFreshType(cs); 
        Constraint c1 = TreeIsType(c,c@\loc,t2);
        Constraint c2 = BindableToCase(t1,t2,c@\loc);
        cs.constraints = cs.constraints + { c1, c2 };
        branchTypes += t2;
    }
    
    // Step 4: Constrain the ultimate result. It is currently stmt[void]. If
    // we can determine that the match is complete (for instance, has a default
    // case, or completely covers all possible cases) it can instead be the
    // lub of the branch types.
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));

    return cs;
} 

//
// Collect constraints for the visit statement.
//
//    isDefined(l), v : t1
// -----------------------------------------------------------
//   l v : stmt[t1]
//
public ConstraintBase gatherVisitStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Label l, Visit v) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.

    // Step 2: Constraint the visit. Since we can use visit statements in (for example)
    // return statements, and want them to return a value, we want to constrain the
    // statement value to be the result of the visit.
    <cs,t1> = makeFreshType(cs);
    Constraint c1 = TreeIsType(v,v@\loc,t1);
    Constraint c2 = TreeIsType(sp,sp@\loc,makeStatementType(t1));
    cs.constraints = cs.constraints + { c1, c2 };
    return cs;
}           

//
// Collect constraints for the expression statement
//
//            e : t1
// --------------------------------------
//           e ; : stmt[t1]
//
public ConstraintBase gatherExpressionStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Expression e) {
    // Constrain the ultimate type. Given that e has some arbitrary type t, 
    // the statement type will be stmt[t].
    <cs, t1> = makeFreshType(cs);
    Constraint c1 = TreeIsType(e, e@\loc, t1);
    Constraint c2 = TreeIsType(sp, sp@\loc, makeStatementType(t1));
    cs.constraints = cs.constraints + { c1, c2 };
    return cs;
}

//
// Collect constraints for the assignment statement. This has two complications. First, 
// we essentially are doing a limited pattern binding when we assign, since we can assign
// into names, tuples, annotations, etc. Second, we have multiple assignment operations, 
// including standard assignment (=) and combinations with operations (+=, for instance).
// In the latter cases, we have different rules -- the allowable lvalues are different,
// since it has to be something that is already defined, and the operation must be
// permitted between the lvalue type and the rvalue type.
//
// TODO: Define rules here!!!
//
public ConstraintBase gatherAssignmentStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Assignable a, Assignment op, Statement s) {
    RType resultType;
    
    // Step 1: Create the constraints for the assignment, based on the type of assignment 
    // operation. The check determines if the assignment op includes another operation, such 
    // as +. It is false for default assignment, =, and for if defined assignment, =?, but 
    // true for the other assignment operators.
    if (!aOpHasOp(convertAssignmentOp(op))) {
        <cs, ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; 
        Constraint c1 = TreeIsType(a, a@\loc, t1);
        Constraint c2 = TreeIsType(s, s@\loc, makeStatementType(t2));
        Constraint c3 = Assignable(sp,sp@\loc,a,s,t2,t1,t3); 
        cs.constraints = cs.constraints + { c1, c2, c3 };
        resultType = t3;
    } else {
        // If the assignment operator is a combo operator including +, etc, we need to add 
        // constraints to ensure that this operation is allowed.
        <cs, ts> = makeFreshTypes(cs,6); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; t4 = ts[3]; t5 = ts[4]; t6 = ts[5];
        Constraint c1 = TreeIsType(a, a@\loc, t1);
        Constraint c2 = TreeIsType(s, s@\loc, makeStatementType(t2));
        Constraint c3 = IsNameAssignable(t1, t4, sp@\loc);
        Constraint c4 = BuiltInAppliable(opForAOp(convertAssignmentOp(op)), makeTupleType([t4,t2]), t5, sp@\loc);        
        Constraint c5 = Assignable(sp,sp@\loc,a,s,t5,t4,t6);
        cs.constraints = cs.constraints + { c1, c2, c3, c4, c5 };
        resultType = t6;
    }

    // Step 2: Create the ultimate type for the assignment statement, which is a statement type
    // based on the type of the assignable (which may need to be derived, if no explicit type is
    // given, such as with <x,y> = f(3), where f(3) = <1,false>.
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,resultType);
    return cs;
}

//
// Collect constraints for the assert statement. 
//
//         e : bool
// ---------------------------------
//     assert e; : stmt[void]
//
public ConstraintBase gatherAssertStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Expression e) {
    // Step 1: Constrain the expression. It should evaluate to type bool.
    cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeBoolType());
    
    // Step 2: Constrain the ultimate type. It should be stmt[void].
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));

    return cs;
}

//
// Collect constraints for the assert (with message) statement. 
//
//         e : bool, em : str
// ---------------------------------
//     assert e : em; : stmt[void]
//
public ConstraintBase gatherAssertWithMessageStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Expression e, Expression em) {
    // Step 1: Constrain the subexpressions. Assert should be given a bool and a string.
    cs.constraints = cs.constraints + TreeIsType(e,e@\loc,makeBoolType()) + TreeIsType(em,em@\loc,makeStrType());
    
    // Step 2: Constrain the ultimate type. It should be stmt[void].
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));

    return cs;
}

//
// Collect constraints for the return statement.
//
//     top(returnTypeStack) : t1, b : stmt[t2], t2 <: t1
// --------------------------------------------------------------
//             return b : stmt[void]
//
public ConstraintBase gatherReturnStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Statement b) {
    // TODO: We should just have an annotation to do this. Unfortunately, we don't yet...
    //RType retType = sp@functionReturnType;
    RType retType = getFunctionReturnType(st.returnTypeMap[sp@\loc]);

    // Step 1: Constrain the return statement. It should be a subtype of the
    // return type for the current function context (associated with this 
    // statement during symbol table generation).
    <cs, t1> = makeFreshType(cs); 
    cs.constraints = cs.constraints + TreeIsType(b,b@\loc,makeStatementType(t1)) + Returnable(sp,sp@\loc,b,t1,retType);
    
    // Step 2: Constrain the overall type, which is stmt[void].
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,retType);
     
    return cs;  
}


//
// Collect constraints for the throw statement.
//
//    b : stmt[RuntimeException]
// ---------------------------------
//       throw b : stmt[void]
//
public ConstraintBase gatherThrowStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Statement b) {
    // Step 1: Constraint the throw body. It must be of type RuntimeException.
    <cs,t1> = makeFreshType(cs);
    cs.constraints = cs.constraints + TreeIsType(b,b@\loc,makeStatementType(t1)) + IsRuntimeException(t1,b@\loc);
    
    // Step 2: Constrain the overall type.
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));
    
    return cs;
}

//
// Collect constraints for the insert statement.
//
// TODO: Need to also constraint b to be the correct type for appending.
//
public ConstraintBase gatherInsertStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, DataTarget dt, Statement b) {
    // Step 1: Constrain the data target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the insert. The only constraint is that this is stmt[void].
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));
    return cs;
}

//
// Collect constraints for the append statement.
//
// TODO: Need to also constraint b to be the correct type for appending.
//
public ConstraintBase gatherAppendStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, DataTarget dt, Statement b) {
    // Step 1: Constrain the data target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the append. The only constraint is that this is stmt[void].
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));
    return cs;
}

//
// Collect constraints for the local function statement. We don't need to calculate a type
// since we cannot assign it, so we just leave the work to what occurs inside (in the 
// functionality for the other statements).
//
// -----------------------------------------------------
//     fundecl : stmt[void]
//
public ConstraintBase gatherLocalFunctionStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Tags ts, Visibility v, Signature sig, FunctionBody fb) {
    // Step 1: Constraint the overall type. It is simply a stmt[void], we don't do any checking here since the
    // necessary checks are performed INSIDE the function.
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));
    return cs;
}


//
// Collect constraints for the local var declaration statement.
//
// -----------------------------------------------------
//     vardecl : stmt[void]
//
public ConstraintBase gatherLocalVarStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, {Variable ","}+ vs) {
    // Step 1: Constraint the overall type. It is simply a stmt[void], we don't do any 
    // checking here since the necessary checks are performed with the variable declaration.
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));
    return cs;
}

//
// Collect constraints for the break statement.
//
//     isDefined(t)
// ------------------------------
//      break t; : stmt[void]
//
public ConstraintBase gatherBreakStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Target t) {
    // Step 1: Constrain the target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the break. The only constraint is that this is stmt[void].
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));
    return cs;
}

//
// Collect constraints for the fail statement.
//
//          isDefined(t)
// -----------------------------
//      fail t; : stmt[void]
//
public ConstraintBase gatherFailStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Target t) {
    // Step 1: Constrain the target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the fail. The only constraint is that this is stmt[void].
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));
    return cs;
}

//
// Collect constraints for the continue statement.
//
//           isDefined(t)
// --------------------------------
//      continue t; : stmt[void]
//
public ConstraintBase gatherContinueStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Target t) {
    // Step 1: Constrain the target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the continue. The only constraint is that this is stmt[void].
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));
    return cs;
}

//
// Collect constraints for the try/catch statement.
//
//              sb : stmt[t1]
// ----------------------------------------
//        try sb c1 ... cn : stmt[void]
//
public ConstraintBase gatherTryCatchStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Statement body, Catch+ catches) {
    // There are no overall constraints on the types here, so we just return stmt[void].
    cs.constraints = cs.constraints + TreeIsType(sp,sp@\loc,makeStatementType(makeVoidType()));
    return cs;
}       

//
// Collect constraints for the try/catch/finally statement.
//
//              sb : stmt[t1], sf : stmt[t2]
// ------------------------------------------------------
//        try sb c1 ... cn finally sf : stmt[void]
//
public ConstraintBase gatherTryCatchFinallyStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Statement body, Catch+ catches, Statement fBody) {
    // There are no overall constraints on the types here, so we just return stmt[void].
    cs.constraints = cs.constraints + TreeIsType(sp, sp@\loc, makeStatementType(makeVoidType()));
    return cs;
}       

//
// Collect constraints for the block statement.
//
//    s1 : stmt[t1], ..., sn : stmt[tn]
// -------------------------------------------------
//          { s1 ... sn } : stmt[tn]
//
public ConstraintBase gatherBlockStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp, Label l, Statement+ bs) {
    // The block type is identical to the type of the final statement in the
    // block, and both are stmt types.
    list[Statement] blockStatements = [ b | b <- bs ];
    <cs, ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = TreeIsType(sp,sp@\loc,makeStatementType(t1));
    Statement finalStatement = head(tail(blockStatements,1));
    Constraint c2 = TreeIsType(finalStatement,finalStatement@\loc,makeStatementType(t2));
    Constraint c3 = TypesAreEqual(t1,t2,sp@\loc);
    cs.constraints = cs.constraints + { c1, c2, c3 };

    return cs;      
} 

//
// Collect constraints for the empty statement
//
// --------------------------------------
//           ; : stmt[void]
//
public ConstraintBase gatherEmptyStatementConstraints(SymbolTable st, ConstraintBase cs, Statement sp) {
    cs.constraints = cs.constraints + TreeIsType(sp, sp@\loc, makeStatementType(makeVoidType()));
    return cs;
}

//
// Collect constraints over all statements. This dispatches out to individual functions
// for each statement production.
//
public ConstraintBase gatherStatementConstraints(SymbolTable st, ConstraintBase cs, Statement stmt) {
    switch(stmt) {
        case (Statement)`solve (<{QualifiedName ","}+ vs> <Bound b>) <Statement sb>` :
            return gatherSolveStatementConstraints(st,cs,stmt,vs,b,sb);
        
        case (Statement)`<Label l> for (<{Expression ","}+ es>) <Statement b>` :
            return gatherForStatementConstraints(st,cs,stmt,l,es,b);
        
        case (Statement)`<Label l> while (<{Expression ","}+ es>) <Statement b>` :
            return gatherWhileStatementConstraints(st,cs,stmt,l,es,b);
                
        case (Statement)`<Label l> do <Statement b> while (<Expression e>);` :
            return gatherDoWhileStatementConstraints(st,cs,stmt,l,b,e);
        
        case (Statement)`<Label l> if (<{Expression ","}+ es>) <Statement bt> else <Statement bf>` :
            return gatherIfThenElseStatementConstraints(st,cs,stmt,l,es,bt,bf);
        
        case (Statement)`<Label l> if (<{Expression ","}+ es>) <Statement bt> <NoElseMayFollow _>` :
            return gatherIfThenStatementConstraints(st,cs,stmt,l,es,bt);
        
        case (Statement)`<Label l> switch (<Expression e>) { <Case+ css> }` :
            return gatherSwitchStatementConstraints(st,cs,stmt,l,e,css);
        
        case (Statement)`<Label l> <Visit v>` :
            return gatherVisitStatementConstraints(st,cs,stmt,l,v);
        
        case (Statement)`<Expression e> ;` :
            return gatherExpressionStatementConstraints(st,cs,stmt,e);
        
        case (Statement)`<Assignable a> <Assignment op> <Statement b>` :
            return gatherAssignmentStatementConstraints(st,cs,stmt,a,op,b);
                
        case (Statement)`assert <Expression e> ;` :
            return gatherAssertStatementConstraints(st,cs,stmt,e);
        
        case (Statement)`assert <Expression e> : <Expression em> ;` :
            return gatherAssertWithMessageStatementConstraints(st,cs,stmt,e,em);
                
        case (Statement)`return <Statement b>` :
            return gatherReturnStatementConstraints(st,cs,stmt,b);
                
        case (Statement)`throw <Statement b>` : 
            return gatherThrowStatementConstraints(st,cs,stmt,b);
        
        case (Statement)`insert <DataTarget dt> <Statement b>` :
            return gatherInsertStatementConstraints(st,cs,stmt,dt,b);
                
        case (Statement)`append <DataTarget dt> <Statement b>` :
            return gatherAppendStatementConstraints(st,cs,stmt,dt,b);
                
        case (Statement)`<Tags ts> <Visibility v> <Signature sig> <FunctionBody fb>` :
            return gatherLocalFunctionStatementConstraints(st,cs,stmt,ts,v,sig,fb);
                
        case (Statement)`<Type t> <{Variable ","}+ vs> ;` :
            return gatherLocalVarStatementConstraints(st,cs,stmt,vs);
                
        // TODO: Handle the dynamic part of dynamic vars        
        case (Statement)`dynamic <Type t> <{Variable ","}+ vs> ;` :
            return gatherLocalVarStatementConstraints(st,cs,stmt,vs);
                
        case (Statement)`break <Target t> ;` :
            return gatherBreakStatementConstraints(st,cs,stmt,t);
                
        case (Statement)`fail <Target t> ;` :
            return gatherFailStatementConstraints(st,cs,stmt,t);
                
        case (Statement)`continue <Target t> ;` :
            return gatherContinueStatementConstraints(st,cs,stmt,t);
                
        case (Statement)`try <Statement b> <Catch+ css>` :
            return gatherTryCatchStatementConstraints(st,cs,stmt,b,css);
        
        case (Statement)`try <Statement b> <Catch+ cs> finally <Statement bf>` :
            return gatherTryCatchFinallyStatementConstraints(st,cs,stmt,b,cs,bf);
                
        case (Statement)`<Label l> { <Statement+ bs> }` :
            return gatherBlockStatementConstraints(st,cs,stmt,l,bs);
                
        case (Statement)`;` :
            return gatherEmptyStatementConstraints(st,cs,stmt);
    }
    
    throw "Unhandled type constraint gathering case in gatherStatementConstraints for statement <stmt>";
}
