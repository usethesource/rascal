@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::checker::constraints::Statement

import List;
import ParseTree;
import IO;
import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::scoping::ScopedTypes;
import lang::rascal::checker::constraints::Constraints;
import lang::rascal::checker::constraints::BuiltIns;
import lang::rascal::checker::TreeUtils;
import lang::rascal::syntax::RascalRascal;

//
// Collect constraints over all statements. This dispatches out to individual functions
// for each statement production.
//
public ConstraintBase gatherStatementConstraints(STBuilder st, ConstraintBase cs, Statement stmt) {
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
        
        case (Statement)`<Label l> if (<{Expression ","}+ es>) <Statement bt>` :
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

//
// Collect constraints for the solve statement. All variables should be defined, 
// and the bound, if present, should be an integer.
//
//   isDefined(v1), ..., isDefined(vn), b : int, s : stmt[t]
// ------------------------------------------------------------
//    solve (v1,...,vn b) s : stmt[void]
//
public ConstraintBase gatherSolveStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, {QualifiedName ","}+ vars, Bound b, Statement body) {    
    // Step 1: Constraint the variable list. Definedness is checked during
    // symbol table generation, and no other constraints on the types are
    // given, so we need no logic here for this check.

    // Step 2: Constrain the bound. The bound should be an expression
    // of integer type. It is optional, so we need to see if a bound
    // was given before constraining the type.
    if ((Bound)`; <Expression e>` := b)
        cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeIntType(), b@\loc);
    
    // Step 3: Constraint the ultimate type of the solve statement, which is void.
    return addConstraintForLoc(cb, sp@\loc, makeVoidType());
}

//
// Collect constraints for the for statement. All expressions should be of type bool.
//
//   isDefined(l), e1 : bool, ..., en : bool, s : stmt[t]
// ----------------------------------------------------------
//       l for (e1,...,en) s : stmt[list[void]]
//
public ConstraintBase gatherForStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Label l, {Expression ","}+ exps, Statement body) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.
    
    // Step 2: Constrain the expression list. Each expression should evaluate to
    // type bool.
    for (e <- exps) cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeBoolType(), e@\loc);
    
    // Step 3: Constrain the ultimate type. It should be list[void], 
    // NOT the body type, since we don't know if any iterations of the
    // loop are completed or if any values are inserted.
    //
    // TODO: This would be a good opportunity for static analysis, which could
    // allow for a more exact type.
    //
    return addConstraintForLoc(cb, sp@\loc, makeListType(makeVoidType()));
}  

//
// Collect constraints for the while statement. All expressions should be of type bool.
//
//   isDefined(l), e1 : bool, ..., en : bool, s : stmt[t]
// ----------------------------------------------------------
//       l while (e1,...,en) s : stmt[list[void]]
//
public ConstraintBase gatherWhileStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Label l, {Expression ","}+ exps, Statement body) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.
    
    // Step 2: Constrain the expression list. Each expression should evaluate to
    // type bool.
    for (e <- exps) cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeBoolType(), e@\loc);
    
    // Step 3: Constrain the ultimate type. It should be list[void], 
    // NOT the body type, since we don't know if any iterations of the
    // loop are completed or if any values are inserted.
    //
    // TODO: This would be a good opportunity for static analysis, which could
    // allow for a more exact type.
    //
    return addConstraintForLoc(cb, sp@\loc, makeListType(makeVoidType()));
}

//
// Collect constraints for the do while statement. The condition expression should be
// of type bool.
//
//   isDefined(l), s : stmt[t], e : bool
// ----------------------------------------------------------
//       l do s while (e) : stmt[list[void]]
//
public ConstraintBase gatherDoWhileStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Label l, Statement body, Expression e) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.
    
    // Step 2: Constrain the expression. It should evaluate to type bool.
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeBoolType(), e@\loc);
    
    // Step 3: Constrain the ultimate type. It should be list[void], 
    // NOT the body type, since we don't know if how many appends are 
    // performed and if they are of the same type.
    //
    // TODO: Since we know at least one iteration occurs, we could take the lub
    // of the append types, but we would need to ensure that an append occurs on
    // each path through the loop to do so. This may be another good target for
    // additional analysis.
    //
    return addConstraintForLoc(cb, sp@\loc, makeListType(makeVoidType()));
}

//
// Collect constraints for the if/then/else statement.
//
//   isDefined(l), e1 : bool, ..., en : bool,  tb : stmt[t1], tf : stmt[t2]
// -----------------------------------------------------------------------------
//    l if (e1,...,en) then tb else tf : stmt[lub(tb,tf)]
//
public ConstraintBase gatherIfThenElseStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Label l, {Expression ","}+ exps, Statement trueBody, Statement falseBody) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the expression list. Each expression should evaluate to
    // type bool.
    for (e <- exps) cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeBoolType(), e@\loc);

    // Step 3: Get back the type of each branch. Each branch is of an arbitrary type.
    tb = typeForLoc(cb, trueBody@\loc);
    tf = typeForLoc(cb, falseBody@\loc);
    
    // Step 4: Constrain the type of the statement. It will be the lub of the branch types.
    < cb, tlub > = makeFreshType(cb);
    cb.constraints = cb.constraints + LubOf([tb,tf], tlub, sp@\loc);
    cb = addConstraintForLoc(cb, sp@\loc, tlub);

    return cb;
}

//
// Collect constraints for the if/then statement.
//
//    isDefined(l), e1 : bool, ..., en : bool,  tb : stmt[t1]
// -------------------------------------------------------------
//    l if (e1,...,en) then tb : stmt[void]
//
public ConstraintBase gatherIfThenStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Label l, {Expression ","}+ exps, Statement trueBody) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the expression list. Each expression should evaluate to
    // type bool.
    for (e <- exps) cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeBoolType(), e@\loc);

    // Step 3: Constrain the ultimate type. It should be void, given
    // that we don't know statically if the true branch will be taken, so we
    // don't know if an actual value will be returned.
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());

    return cb;
}

//
// Collect constraints for the switch statement.
//
//    isDefined(l), e : te, c1 : case[p1,tc1], ..., cn : case[pn,tcn], bindable(t1,pc1), ..., bindable(te,pcn)
// --------------------------------------------------------------------------------------------------------------
//                                  l switch(e) { c1 ... cn } : stmt[void]
//
public ConstraintBase gatherSwitchStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Label l, Expression e1, Case+ cases) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.

    // Step 2: Look up the location type bound to e1, which is used below. It is of
    // an arbitrary type.
    te  = typeForLoc(cb, e1@\loc);
    
    // Step 3: For the type of each case, constrain it to say that it
    // is bindable to the type of the switch expression.
    list[RType] branchTypes = [ ];
    for (c <- cases) {
        tc = typeForLoc(cb, c@\loc); 
        cb.constraints = cb.constraints + BindableToCase(te, tc, U(), c@\loc);
        branchTypes += tc;
    }
    
    // Step 4: Constrain the ultimate result. It is currently void. If
    // we can determine that the match is complete (for instance, has a default
    // case, or completely covers all possible cases) it can instead be the
    // lub of the branch types.
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());

    return cb;
} 

//
// Collect constraints for the visit statement.
//
//    isDefined(l), v : t1
// -----------------------------------------------------------
//   l v : stmt[t1]
//
public ConstraintBase gatherVisitStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Label l, Visit v) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on the label are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the visit. Since the visit already has a type, we just propagate
    // that up. NOTE: The loc of sp could be == the loc of v, in which case this is essentially
    // a no-op. The locs will differ if there is a label.
    tv  = typeForLoc(cb, v@\loc);
    cb = addConstraintForLoc(cb, sp@\loc, tv);
    
    return cb;
}           

//
// Collect constraints for the expression statement
//
//            e : t1
// --------------------------------------
//           e ; : stmt[t1]
//
public ConstraintBase gatherExpressionStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Expression e) {
    // Constrain the ultimate type. Given that e has some arbitrary type t, 
    // the statement type will be of the same type.
    te = typeForLoc(cb, e@\loc);
    cb = addConstraintForLoc(cb, sp@\loc, te);
    
    return cb;
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
public ConstraintBase gatherAssignmentStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Assignable a, Assignment op, Statement s) {

    // Get back the types of both the assignable and the statement, which computes the value being assigned
    ta = typeForLoc(cb, a@\loc);
    ts = typeForLoc(cb, s@\loc);

    // aOpHasOp returns true if this is a combo assignment, e.g., +=, *=    
    if (aOpHasOp(convertAssignmentOp(op))) {
        // Check that this assignable can be used as the target of a combo assignment. We cannot say
        // <a,b,c> += d, for instance, but we can say a.f += 3.
        // TODO: Do check here, don't push this off to a type constraint that doesn't actually constrain
        // a type. Move this to name resolution, where we can treat it as a syntactic check (which it is).
        
        // Constrain the result type of the operation used in the combo
        < cb, tr > = makeFreshType(cb);
        Constraint c2 = BuiltInAppliable(opForAOp(convertAssignmentOp(op)), makeTupleType([ta,ts]), tr, sp@\loc);
        
        // Constrain the result to be something assignable into a, with the result of the assignment then
        // tar. The type could change if it is moved up the lattice towards value (or if it is a failure).
        < cb, tar > = makeFreshType(cb);        
        Constraint c3 = ComboAssignable(ta, tr, tar, sp@\loc);
        
        // Constrain the overall type of the statement to be the type of the assignment result
        cb = addConstraintForLoc(cb, sp@\loc, tar);
        
        cb.constraints = cb.constraints + { c2, c3 };
    }

    if (!aOpHasOp(convertAssignmentOp(op))) {
        // Constrain the result to be something assignable into a, with the result of the assignment then
        // tar. The type could change if it is moved up the lattice towards value (or if it is a failure).
        < cb, tar > = makeFreshType(cb);        
        Constraint c1 = Assignable(ta, ts, tar, sp@\loc);
        
        // Constrain the overall type of the statement to be the type of the assignment result
        cb = addConstraintForLoc(cb, sp@\loc, tar);
        
        cb.constraints = cb.constraints + c1;
    }
    
    return cb; 
}

//
// Collect constraints for the assert statement. 
//
//         e : bool
// ---------------------------------
//     assert e; : void
//
public ConstraintBase gatherAssertStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Expression e) {
    // Step 1: Constrain the expression. It should evaluate to type bool.
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeBoolType(), e@\loc);
    
    // Step 2: Constrain the ultimate type. It should be void.
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());

    return cb;
}

//
// Collect constraints for the assert (with message) statement. 
//
//         e : bool, em : str
// ---------------------------------
//     assert e : em; : void
//
public ConstraintBase gatherAssertWithMessageStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Expression e, Expression em) {
    // Step 1: Constrain the first expression. It should evaluate to type bool.
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, e@\loc), makeBoolType(), e@\loc);
    
    // Step 2: Constrain the second expression. It is a message and should be type string.
    cb.constraints = cb.constraints + ConstrainType(typeForLoc(cb, em@\loc), makeStrType(), e@\loc);
    
    // Step 3: Constrain the ultimate type. It should be void.
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());

    return cb;
}

//
// Collect constraints for the return statement.
//
//     currentFunctionReturnType: retType, b : rt, rt returnableFor retType
// ---------------------------------------------------------------------------
//                                 return b : void
//
public ConstraintBase gatherReturnStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Statement b) {
    // TODO: We should just have an annotation to do this. Unfortunately, we don't yet...
    //RType retType = sp@functionReturnType;
    RType retType = getFunctionReturnType(getTypeForItem(st,st.returnMap[sp@\loc]));

    // Step 1: Constrain the value of b as something returnable by a function with return
    // type retType.
    rt = typeForLoc(cb, b@\loc);
    cb.constraints = cb.constraints + Returnable(rt, retType, U(), sp@\loc);
     
    // Step 2: Constrain the overall type to be void (i.e., we cannot assign return to anything)
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());
     
    return cb;  
}


//
// Collect constraints for the throw statement.
//
//    b : stmt[RuntimeException]
// ---------------------------------
//       throw b : void
//
public ConstraintBase gatherThrowStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Statement b) {
    // Step 1: Constraint the throw body. It must be of type RuntimeException.
    tb = typeForLoc(cb, b@\loc);
    cb.constraints = cb.constraints + IsRuntimeException(tb, U(), b@\loc); 
    
    // Step 2: Constrain the overall type of the statement.
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());
    
    return cb;
}

//
// Collect constraints for the insert statement.
//
// TODO: Need to also constraint b to be the correct type for appending, which means
// we need to be able to figure out which thing we are inserting into.
//
public ConstraintBase gatherInsertStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, DataTarget dt, Statement b) {
    // Step 1: Constrain the data target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the insert. The only constraint is that this is void.
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());
    return cb;
}

//
// Collect constraints for the append statement.
//
// TODO: Need to also constraint b to be the correct type for appending.
//
public ConstraintBase gatherAppendStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, DataTarget dt, Statement b) {
    // Step 1: Constrain the data target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the append. The only constraint is that this is void.
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());
    return cb;
}

//
// Collect constraints for the local function statement. We don't need to calculate a type
// since we cannot assign it, so we just leave the work to what occurs inside (in the 
// functionality for the other statements).
//
// -----------------------------------------------------
//     fundecl : void
//
public ConstraintBase gatherLocalFunctionStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Tags ts, Visibility v, Signature sig, FunctionBody fb) {
    // Step 1: Constraint the overall type. It is simply a stmt[void], we don't do any checking here since the
    // necessary checks are performed INSIDE the function.
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());
    return cb;
}


//
// Collect constraints for the local var declaration statement.
//
// -----------------------------------------------------
//     vardecl : void
//
public ConstraintBase gatherLocalVarStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, {Variable ","}+ vs) {
    // Step 1: Constraint the overall type. It is simply a void, we don't do any 
    // checking here since the necessary checks are performed with the variable declaration.
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());
    return cb;
}

//
// Collect constraints for the break statement.
//
//     isDefined(t)
// ------------------------------
//      break t; : void
//
public ConstraintBase gatherBreakStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Target t) {
    // Step 1: Constrain the target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the break. The only constraint is that this is stmtvoid.
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());
    return cb;
}

//
// Collect constraints for the fail statement.
//
//          isDefined(t)
// -----------------------------
//      fail t; : void
//
public ConstraintBase gatherFailStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Target t) {
    // Step 1: Constrain the target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the fail. The only constraint is that this is stmt[void].
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());
    return cb;
}

//
// Collect constraints for the continue statement.
//
//           isDefined(t)
// --------------------------------
//      continue t; : void
//
public ConstraintBase gatherContinueStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Target t) {
    // Step 1: Constrain the target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the continue. The only constraint is that this is void.
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());
    return cb;
}

//
// Collect constraints for the try/catch statement.
//
//              sb : t1
// ----------------------------------------
//        try sb c1 ... cn : void
//
public ConstraintBase gatherTryCatchStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Statement body, Catch+ catches) {
    // There are no overall constraints on the types here, so we just return void.
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());
    return cb;
}       

//
// Collect constraints for the try/catch/finally statement.
//
//              sb : stmt[t1], sf : stmt[t2]
// ------------------------------------------------------
//        try sb c1 ... cn finally sf : stmt[void]
//
public ConstraintBase gatherTryCatchFinallyStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Statement body, Catch+ catches, Statement fBody) {
    // There are no overall constraints on the types here, so we just return stmt[void].
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());
    return cb;
}       

//
// Collect constraints for the block statement.
//
//    s1 : stmt[t1], ..., sn : stmt[tn]
// -------------------------------------------------
//          { s1 ... sn } : stmt[tn]
//
public ConstraintBase gatherBlockStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp, Label l, Statement+ bs) {
    // The block type is identical to the type of the final statement in the
    // block, and both are stmt types.
    cb = addConstraintForLoc(cb, sp@\loc, typeForLoc(cb, last([ b | b <- bs ])@\loc));
    return cb;
} 

//
// Collect constraints for the empty statement
//
// --------------------------------------
//           ; : stmt[void]
//
public ConstraintBase gatherEmptyStatementConstraints(STBuilder st, ConstraintBase cb, Statement sp) {
    cb = addConstraintForLoc(cb, sp@\loc, makeVoidType());
    return cb;
}

