@bootstrapParser
module rascal::checker::constraints::Statements

import IO;
import List;
import Set;
import Message;
import Map;
import Relation;
import ParseTree;
import Reflective;
import String;

import rascal::checker::ListUtils;
import rascal::checker::Types;
import rascal::checker::SubTypes;
import rascal::checker::SymbolTable;
import rascal::checker::Signature;
import rascal::checker::TypeRules;
import rascal::checker::Namespace;
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
public Constraints gatherSolveStatementConstraints(Constraints cs, Statement sp, {QualifiedName ","}+ vars, Bound b, Statement body) {    
    // Step 1: Constraint the variable list. Definedness is checked during
    // symbol table generation, and no other constraints on the types are
    // given, so we need no logic here for this check.

    // Step 2: Constrain the bound. The bound should be an expression
    // of integer type. It is optional, so check for it first.
    if ((Bound)`; <Expression e>` := b) {
        <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
        Constraint c1 = IsType(e,t1);
        Constraint c2 = IsType(t1,makeIntType());
        cs.constraints = cs.constraints + { c1, c2 };
    }
    
    // Step 3: Constraint the ultimate type. It should be stmt[void]. 
    <cs, ts> = makeFreshTypes(cs,1); t2 = ts[0];
    Constraint c3 = IsType(sp,t2);
    Constraint c4 = IsType(t2,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c3, c4 };
    
    return cs;
}

//
// Collect constraints for the for statement. All expressions should be of type bool.
//
//   isDefined(l), e1 : bool, ..., en : bool, s : stmt[t]
// ----------------------------------------------------------
//       l for (e1,...,en) s : stmt[list[void]]
//
public Constraints gatherForStatementConstraints(Constraints cs, Statement sp, Label l, {Expression ","}+ exps, Statement body) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.
    
    // Step 2: Constrain the expression list. Each expression should evaluate to
    // type bool.
    for (e <- exps) {
        <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
        Constraint c1 = IsType(e,t1);
        Constraint c2 = IsType(t1,makeBoolType());
        cs.constraints = cs.constraints + { c1, c2 };
    }
    
    // Step 3: Constrain the ultimate type. It should be stmt[list[void]], 
    // NOT the body type, since we don't know if any iterations of the
    // loop are completed or if any values are inserted.
    <cs, ts> = makeFreshTypes(cs,1); t2 = ts[0];
    Constraint c3 = IsType(sp,t2);
    Constraint c4 = IsType(t2,makeStatementType(makeListType(makeVoidType()))); 
    cs.constraints = cs.constraints + { c3, c4 };

    return cs;
}  

//
// Collect constraints for the while statement. All expressions should be of type bool.
//
//   isDefined(l), e1 : bool, ..., en : bool, s : stmt[t]
// ----------------------------------------------------------
//       l while (e1,...,en) s : stmt[list[void]]
//
public Constraints gatherWhileStatementConstraints(Constraints cs, Statement sp, Label l, {Expression ","}+ exps, Statement body) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.
    
    // Step 2: Constrain the expression list. Each expression should evaluate to
    // type bool.
    for (e <- exps) {
        <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
        Constraint c1 = IsType(e,t1);
        Constraint c2 = IsType(t1,makeBoolType());
        cs.constraints = cs.constraints + { c1, c2 };
    }
    
    // Step 3: Constrain the ultimate type. It should be stmt[list[void]], 
    // NOT the body type, since we don't know if any iterations of the
    // loop are completed or if any values are inserted.
    <cs, ts> = makeFreshTypes(cs,1); t2 = ts[0];
    Constraint c3 = IsType(sp,t2);
    Constraint c4 = IsType(t2,makeStatementType(makeListType(makeVoidType()))); 
    cs.constraints = cs.constraints + { c3, c4 };

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
public Constraints gatherDoWhileStatementConstraints(Constraints cs, Statement sp, Label l, Statement body, Expression e) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.
    
    // Step 2: Constrain the expression. It should evaluate to type bool.
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(e,t1);
    Constraint c2 = IsType(t1,makeBoolType());
    cs.constraints = cs.constraints + { c1, c2 };
    
    // Step 3: Constrain the ultimate type. It should be stmt[list[void]], 
    // NOT the body type, since we don't know if any values are inserted.
    <cs, ts> = makeFreshTypes(cs,1); t2 = ts[0];
    Constraint c3 = IsType(sp,t2);
    Constraint c4 = IsType(t2,makeStatementType(makeListType(makeVoidType()))); 
    cs.constraints = cs.constraints + { c3, c4 };

    return cs;
}

//
// Collect constraints for the if/then/else statement.
//
//   isDefined(l), e1 : bool, ..., en : bool,  tb : stmt[t1], tf : stmt[t2]
// -----------------------------------------------------------------------------
//    l if (e1,...,en) then tb else tf : stmt[lub(tb,tf)]
//
public Constraints gatherIfThenElseStatementConstraints(Constraints cs, Statement sp, Label l, {Expression ","}+ exps, Statement trueBody, Statement falseBody) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the expression list. Each expression should evaluate to
    // type bool.
    for (e <- exps) {
        <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
        Constraint c1 = IsType(e,t1);
        Constraint c2 = IsType(t1,makeBoolType());
        cs.constraints = cs.constraints + { c1, c2 };
    }

    // Step 3: Constrain the ultimate type. It should be stmt[lub(t1,t2)], 
    // given a true branch type of stmt[t1] and a false branch type of stmt[t2].
    <cs, ts> = makeFreshTypes(cs,6); t2 = ts[0]; t3 = ts[1]; t4 = ts[2]; t5 = ts[3]; t6 = ts[4]; t7 = ts[5];
    Constraint c3 = IsType(sp,t2);
    Constraint c4 = IsType(trueBody,t3);
    Constraint c5 = IsType(falseBody,t4);
    Constraint c6 = IsType(t3,makeStatementType(t5));
    Constraint c7 = IsType(t4,makeStatementType(t6));
    Constraint c8 = LubOf([t5,t6],t7);
    Constraint c9 = IsType(t2,makeStatementType(t7));
    cs.constraints = cs.constraints + { c3, c4, c5, c6, c7, c8, c9 };

    return cs;
}

//
// Collect constraints for the if/then statement.
//
//    isDefined(l), e1 : bool, ..., en : bool,  tb : stmt[t1]
// -------------------------------------------------------------
//    l if (e1,...,en) then tb : stmt[void]
//
public Constraints gatherIfThenStatementConstraints(Constraints cs, Statement sp, Label l, {Expression ","}+ exps, Statement trueBody) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the expression list. Each expression should evaluate to
    // type bool.
    for (e <- exps) {
        <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
        Constraint c1 = IsType(e,t1);
        Constraint c2 = IsType(t1,makeBoolType());
        cs.constraints = cs.constraints + { c1, c2 };
    }

    // Step 3: Constrain the ultimate type. It should be stmt[void], given
    // that we don't know statically if the true branch will be taken.
    <cs, ts> = makeFreshTypes(cs,1); t2 = ts[0]; 
    Constraint c3 = IsType(sp,t2);
    Constraint c4 = IsType(t2,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c3, c4 };

    return cs;
}

//
// Collect constraints for the switch statement.
//
//    isDefined(l), e1 : t, c1 : case[p1,t1], ..., cn : case[pn,tn], bindable(t,p1), ..., bindable(t,pn)
// --------------------------------------------------------------------------------------------------------
//          l switch(e) { c1 ... cn } : stmt[void]
//
public Constraints gatherSwitchStatementConstraints(Constraints cs, Statement sp, Label l, Expression e, Case+ cases) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the expression. It can be of arbitrary type.
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(e,t1);
    cs.constraints = cs.constraints + { c1 };
    
    // Step 3: Constrain the cases. Each case presents a pattern (or is default).
    // The constraint indicates that the expression type should be bindable to
    // the pattern in the case.
    list[RType] branchTypes = [ ];
    for (c <- cases) {
        <cs, ts> = makeFreshTypes(cs,3), t2 = ts[0]; t3 = ts[1]; t4 = ts[2];
        Constraint c2 = IsType(c,t2);
        Constraint c3 = IsType(t2,CaseType(t3,t4));
        Constraint c4 = Bindable(t1,t3);
        cs.constraints = cs.constraints + { c2, c3, c4 };
        branchTypes += t4;
    }
    
    // Step 4: Constrain the ultimate result. It is currently stmt[void]. If
    // we can determine that the match is complete (for instance, has a default
    // case, or completely covers all possible cases) it can instead be the
    // lub of the branch types.
    <cs, ts> = makeFreshTypes(cs,1); t5 = ts[0];
    Constraint c5 = IsType(sp,t5);
    Constraint c6 = IsType(t5,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c5, c6 };

    return cs;
} 

//
// Collect constraints for the visit statement.
//
//    isDefined(l)
// -----------------------------------------------------------
//   l v : stmt[void]
//
public Constraints gatherVisitStatementConstraints(Constraints cs, Statement sp, Label l, Visit v) {
    // Step 1: Constrain the label. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constraint the visit. Constraints over the visit are gathered
    // elsewhere (since visit is both a statement and expression), so we only
    // need to constrain the ultimate type here to stmt[void].
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(sp,t1);
    Constraint c2 = IsType(t1,makeStatementType(makeVoidType()));
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
public Constraints gatherExpressionStatementConstraints(Constraints cs, Statement sp, Expression e) {
    // Step 1: Constrain the ultimate type. Given that e has type t, the statement type
    // will be stmt[t].
    <cs, ts> = makeFreshTypes(cs, 2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = IsType(sp, t1);
    Constraint c2 = IsType(e, t2);
    Constraint c3 = IsType(t1, makeStatementType(t2));
    cs.constraints = cs.constraints + { c1, c2, c3 };
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
public Constraints gatherAssignmentStatementConstraints(Constraints cs, Statement sp, Assignable a, Assignment op, Statement s) {
    RType resultType;
    
    // Step 1: Create the constraints for the assignment, based on the type of assignment operation. The
    // check determines if the assignment op includes another operation, such as +. It is false for default
    // assignment, =, and for if defined assignment, =?, but true for the other assignment operators.
    if (!aOpHasOp(convertAssignmentOp(op))) {
        <cs, ts> = makeFreshTypes(cs,3); t1 = ts[0]; t2 = ts[1]; t3 = ts[2];
        Constraint c1 = IsType(a, t1);
        Constraint c2 = IsType(s, t2);
        Constraint c3 = IsType(t2,makeStatementType(t3));
        Constraint c4 = Assignable(t1,t3);
        cs.constraints = cs.constraints + { c1, c2, c3, c4 };
        resultType = t1;
    } else {
        // If the assignment operator is a combo operator including +, etc, we need to add constraints
        // to ensure that this operation is allowed.
        <cs, ts> = makeFreshTypes(cs,4); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; t4 = ts[3];
        Constraint c1 = IsType(a, t1);
        Constraint c2 = IsType(s, t2);
        Constraint c3 = IsType(t2,makeStatementType(t3));
        Constraint c4 = ConstantAppliable(opForAOp(convertAssignmentOp(op)), [t1,t3], t4);
        Constraint c5 = Assignable(t1,t4);
        cs.constraints = cs.constraints + { c1, c2, c3, c4, c5 };
        resultType = t1;
    }

    // Step 2: Create the ultimate type for the assignment statement, which is a statement type
    // based on the type of the assignable (which may need to be derived, if no explicit type is
    // given, such as with <x,y> = f(3), where f(3) = <1,false>.
    Constraint c6 = IsType(sp,resultType);
    cs.constraints = cs.constraints + { c6 };
    
    return cs;
}

//
// Collect constraints for the assert statement. 
//
//         e : bool
// ---------------------------------
//     assert e; : stmt[void]
//
public Constraints gatherAssertStatementConstraints(Constraints cs, Statement sp, Expression e) {
    // Step 1: Constrain the expression. It should evaluate to type bool.
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(e,t1);
    Constraint c2 = IsType(t1,makeBoolType());
    cs.constraints = cs.constraints + { c1, c2 };
    
    // Step 2: Constrain the ultimate type. It should be stmt[void].
    <cs, ts> = makeFreshTypes(cs,1); t2 = ts[0];
    Constraint c3 = IsType(sp,t2);
    Constraint c4 = IsType(t2,makeStatementType(makeVoidType())); 
    cs.constraints = cs.constraints + { c3, c4 };

    return cs;
}

//
// Collect constraints for the assert (with message) statement. 
//
//         e : bool, em : str
// ---------------------------------
//     assert e : em; : stmt[void]
//
public Constraints gatherAssertWithMessageStatementConstraints(Constraints cs, Statement sp, Expression e, Expression em) {
    // Step 1: Constrain the expression. It should evaluate to type bool.
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(e,t1);
    Constraint c2 = IsType(t1,makeBoolType());
    cs.constraints = cs.constraints + { c1, c2 };
    
    // Step 2: Constrain the message expression. It should evaluate to type str.
    <cs, ts> = makeFreshTypes(cs,1); t2 = ts[0];
    Constraint c3 = IsType(em,t2);
    Constraint c4 = IsType(t2,makeStrType());
    cs.constraints = cs.constraints + { c3, c4 };

    // Step 3: Constrain the ultimate type. It should be stmt[void].
    <cs, ts> = makeFreshTypes(cs,1); t3 = ts[0];
    Constraint c5 = IsType(sp,t3);
    Constraint c6 = IsType(t3,makeStatementType(makeVoidType())); 
    cs.constraints = cs.constraints + { c5, c6 };

    return cs;
}

//
// Collect constraints for the return statement.
//
//     top(returnTypeStack) : t1, b : stmt[t2], t2 <: t1
// --------------------------------------------------------------
//             return b : stmt[t1]
//
public Constraints gatherReturnStatementConstraints(Constraints cs, Statement sp, Statement b) {
    // Step 1: Constrain the return statement. It should be a subtype of the
    // return type for the current function context (associated with this statement
    // during symbol table generation).
    RType retType = sp@functionReturnType;
    <cs, ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = IsType(b,t1);
    Constraint c2 = IsType(t1,makeStatementType(t2));
    Constraint c3 = IsSubType(t2,retType);
    cs.constraints = cs.constraints + { c1, c2, c3 };
    
    // Step 2: Constrain the overall type. It is the registered return type.
    <cs, ts> = makeFreshTypes(cs,1); t3 = ts[0];
    Constraint c4 = IsType(sp,t3);
    Constraint c5 = IsType(t3,retType);
    cs.constraints = cs.constraints + { c4, c5 };
     
    return cs;  
}


//
// Collect constraints for the throw statement.
//
//    b : stmt[RuntimeException]
// ---------------------------------
//       throw b : stmt[void]
//
public Constraints gatherThrowStatementConstraints(Constraints cs, Statement sp, Statement b) {
    // Step 1: Constraint the throw body. It must be of type RuntimeException.
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(b,t1);
    Constraint c2 = IsRuntimeException(t1);
    cs.constraints = cs.constraints + { c1, c2 };
    
    // Step 2: Constrain the overall type.
    <cs, ts> = makeFreshTypes(cs,1); t2 = ts[0];
    Constraint c3 = IsType(sp,t2);
    Constraint c4 = IsType(t2,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c3, c4 };
    
    return cs;
}

//
// Collect constraints for the insert statement.
//
// TODO: Need to also constraint b to be the correct type for appending.
//
public Constraints gatherInsertStatementConstraints(Constraints cs, Statement sp, DataTarget dt, Statement b) {
  // Step 1: Constrain the data target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the insert. The only constraint is that this is stmt[void].
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(sp,t1);
    Constraint c2 = IsType(t1,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c1, c2 };

    return cs;
}

//
// Collect constraints for the append statement.
//
// TODO: Need to also constraint b to be the correct type for appending.
//
public Constraints gatherAppendStatementConstraints(Constraints cs, Statement sp, DataTarget dt, Statement b) {
    // Step 1: Constrain the data target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the append. The only constraint is that this is stmt[void].
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(sp,t1);
    Constraint c2 = IsType(t1,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c1, c2 };

    return cs;
}

//
// Collect constraints for the local function statement. We don't need to calculate a type
// since we cannot assign it, so we just leave the work to what occurs inside (in the functionality
// for the other statements).
//
// -----------------------------------------------------
//     fundecl : stmt[void]
//
public Constraints gatherLocalFunctionStatementConstraints(Constraints cs, Statement sp, Tags ts, Visibility v, Signature sig, FunctionBody fb) {
    // Step 1: Constraint the overall type. It is simply a stmt[void], we don't do any checking here since the
    // necessary checks are performed INSIDE the function.
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(sp,t1);
    Constraint c2 = IsType(t1,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c1, c2 };
    
    return cs;
}


//
// Collect constraints for the local var declaration statement.
//
// -----------------------------------------------------
//     vardecl : stmt[void]
//
public RType gatherLocalVarStatementConstraints(Statement sp, {Variable ","}+ vs) {
    // Step 1: Constraint the overall type. It is simply a stmt[void], we don't do any checking here since the
    // necessary checks are performed with the variable declaration.
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(sp,t1);
    Constraint c2 = IsType(t1,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c1, c2 };
    
    return cs;
}

//
// Collect constraints for the break statement.
//
//     isDefined(t)
// ------------------------------
//      break t; : stmt[void]
//
public Constraints gatherBreakStatementConstraints(Constraints cs, Statement sp, Target t) {
    // Step 1: Constrain the target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the break. The only constraint is that this is stmt[void].
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(sp,t1);
    Constraint c2 = IsType(t1,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c1, c2 };

    return cs;
}

//
// Collect constraints for the fail statement.
//
//          isDefined(t)
// -----------------------------
//      fail t; : stmt[void]
//
public Constraints gatherFailStatementConstraints(Constraints cs, Statement sp, Target t) {
    // Step 1: Constrain the target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the fail. The only constraint is that this is stmt[void].
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(sp,t1);
    Constraint c2 = IsType(t1,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c1, c2 };

    return cs;
}

//
// Collect constraints for the continue statement.
//
//           isDefined(t)
// --------------------------------
//      continue t; : stmt[void]
//
public Constraints gatherContinueStatementConstraints(Constraints cs, Statement sp, Target t) {
    // Step 1: Constrain the target. Definedness is checked during symbol table
    // generation, and no other constraints on types are given, so we need no
    // logic here for this check.

    // Step 2: Constrain the continue. The only constraint is that this is stmt[void].
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(sp,t1);
    Constraint c2 = IsType(t1,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c1, c2 };

    return cs;
}

//
// Collect constraints for the try/catch statement.
//
//              sb : stmt[t1]
// ----------------------------------------
//        try sb c1 ... cn : stmt[void]
//
public Constraints gatherTryCatchStatementConstraints(Constraints cs, Statement sp, Statement body, Catch+ catches) {
    // Step 1: Constraint the overall type. It is simply a stmt[void], we don't do any checking here since the
    // necessary checks are performed INSIDE the try and catch bodies.
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(sp,t1);
    Constraint c2 = IsType(t1,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c1, c2 };
    
    return cs;
}       

//
// Collect constraints for the try/catch/finally statement.
//
//              sb : stmt[t1], sf : stmt[t2]
// ------------------------------------------------------
//        try sb c1 ... cn finally sf : stmt[void]
//
public Constraints gatherTryCatchFinallyStatementConstraints(Constraints cs, Statement sp, Statement body, Catch+ catches, Statement fBody) {
    // Step 1: Constraint the overall type. It is simply a stmt[void], we don't do any checking here since the
    // necessary checks are performed INSIDE the try, catch, and finally bodies.
    <cs, ts> = makeFreshTypes(cs,1); t1 = ts[0];
    Constraint c1 = IsType(sp,t1);
    Constraint c2 = IsType(t1,makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c1, c2 };
    
    return cs;
}       

//
// Collect constraints for the block statement.
//
//    s1 : stmt[t1], ..., sn : stmt[tn]
// -------------------------------------------------
//          { s1 ... sn } : stmt[tn]
//
public Constraints gatherBlockStatementConstraints(Constraints cs, Statement sp, Label l, Statement+ bs) {
    // Step 1: Constraint the block type. This is constrained to be identical to the type of
    // the final statement in the block. Both are statement types.
    list[Statement] blockStatements = [ b | b <- bs ];
    <cs, ts> = makeFreshTypes(4); t1 = ts[0]; t2 = ts[1]; t3 = ts[2]; t4 = ts[3];
    Constraint c1 = IsType(sp,t1);
    Constraint c2 = IsType(head(tail(blockStatements,1)),t2);
    Constraint c3 = IsType(t1,makeStatementType(t3));
    Constraint c4 = IsType(t2,makeStatementType(t4));
    Constraint c5 = IsType(t3,t4);
    cs.constraints = cs.constraints + { c1, c2, c3, c4, c5 };

    return cs;      
} 

//
// Collect constraints for the empty statement
//
// --------------------------------------
//           ; : stmt[void]
//
public Constraints gatherEmptyStatementConstraints(Constraints cs, Statement sp) {
    // Step 1: Constraint the result type, which is just stmt[void].
    <cs, ts> = makeFreshTypes(cs, 1); t1 = ts[0];
    Constraint c1 = IsType(sp, t1);
    Constraint c2 = IsType(t1, makeStatementType(makeVoidType()));
    cs.constraints = cs.constraints + { c1, c2 };
    return cs;
}

//
// Collect constraints over all statements.
//
public Constraints gatherStatementConstraints(Constraints cs, Statement s) {
    switch(s) {
        case `solve (<{QualifiedName ","}+ vs> <Bound b>) <Statement sb>` :
            return gatherSolveStatementConstraints(cs,s,vs,b,sb);
        
        case `<Label l> for (<{Expression ","}+ es>) <Statement b>` :
            return gatherForStatementConstraints(cs,s,l,es,b);
        
        case `<Label l> while (<{Expression ","}+ es>) <Statement b>` :
            return gatherWhileStatementConstraints(cs,s,l,es,b);
                
        case `<Label l> do <Statement b> while (<Expression e>);` :
            return gatherDoWhileStatementConstraints(cs,s,l,b,e);
        
        case `<Label l> if (<{Expression ","}+ es>) <Statement bt> else <Statement bf>` :
            return gatherIfThenElseStatementConstraints(cs,s,l,es,bt,bf);
        
        case `<Label l> if (<{Expression ","}+ es>) <Statement bt>` :
            return gatherIfThenStatementConstraints(cs,s,l,es,bt);
        
        case `<Label l> switch (<Expression e>) { <Case+ cs> }` :
            return gatherSwitchStatementConstraints(cs,s,l,e,cs);
        
        case (Statement)`<Label l> <Visit v>` :
            return gatherVisitStatementConstraints(cs,s,l,v);
        
        case (Statement)`<Expression e> ;` :
            return gatherExpressionStatementConstraints(cs,s,e);
        
        case (Statement)`<Assignable a> <Assignment op> <Statement b>` :
            return gatherAssignmentStatementConstraints(cs,s,a,op,b);
                
        case (Statement)`assert <Expression e> ;` :
            return gatherAssertStatementConstraints(cs,s,e);
        
        case (Statement)`assert <Expression e> : <Expression em> ;` :
            return gatherAssertWithMessageStatementConstraints(cs,s,e,em);
                
        case (Statement)`return <Statement b>` :
            return gatherReturnStatementConstraints(cs,s,b);
                
        case (Statement)`throw <Statement b>` : 
            return gatherThrowStatementConstraints(cs, s, b);
        
        case (Statement)`insert <DataTarget dt> <Statement b>` :
            return gatherInsertStatementConstraints(cs, s, dt, b);
                
        case (Statement)`append <DataTarget dt> <Statement b>` :
            return gatherAppendStatementConstraints(cs, s, dt, b);
                
        case (Statement)`<Tags ts> <Visibility v> <Signature sig> <FunctionBody fb>` :
            return gatherLocalFunctionStatementConstraints(cs,s,ts,v,sig,fb);
                
        case (Statement)`<Type t> <{Variable ","}+ vs> ;` :
            return gatherLocalVarStatementConstraints(cs,s, vs);
                
        // TODO: Handle the dynamic part of dynamic vars        
        case (Statement)`dynamic <Type t> <{Variable ","}+ vs> ;` :
            return gatherLocalVarStatementConstraints(cs,s,vs);
                
        case (Statement)`break <Target t> ;` :
            return gatherBreakStatementConstraints(cs,s,t);
                
        case (Statement)`fail <Target t> ;` :
            return gatherFailStatementConstraints(cs,s,t);
                
        case (Statement)`continue <Target t> ;` :
            return gatherContinueStatementConstraints(cs,s,t);
                
        case (Statement)`try <Statement b> <Catch+ cs>` :
            return gatherTryCatchStatementConstraints(cs,s,b,cs);
        
        case (Statement)`try <Statement b> <Catch+ cs> finally <Statement bf>` :
            return gatherTryCatchFinallyStatementConstraints(cs,s,b,cs,bf);
                
        case (Statement)`<Label l> { <Statement+ bs> }` :
            return gatherBlockStatementConstraints(cs,s,l,bs);
                
        case (Statement)`;` :
            return gatherEmptyStatementConstraints(cs,s);
    }
    
    throw "Unhandled type constraint gathering case in gatherStatementConstraints for statement <s>";
}
