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
module lang::rascal::checker::constraints::Assignable

import List;
import ParseTree;
import lang::rascal::types::Types;
import lang::rascal::scoping::SymbolTable;
import lang::rascal::checker::constraints::Constraints;
import lang::rascal::syntax::RascalRascal;

//
// Overall driver to gather constraints on the various assignables present in Rascal.
// This calls out to specific functions below, which (generally) include the typing
// rule used to generate the constraints.
//
public ConstraintBase gatherAssignableConstraints(STBuilder st, ConstraintBase cs, Assignable a) {
    switch(a) {
        // Bracket
        case (Assignable)`(<Assignable ac>)` : {
            <cs, t1> = makeFreshType(cs);
            cs.constraints = cs.constraints + TreeIsType(a,a@\loc,t1) + TreeIsType(ac,ac@\loc,t1);
            return cs;
        }
        
        // Variable _
        case (Assignable)`_` : {
            <cs, t1> = makeFreshType(cs);
            cs.constraints = cs.constraints + TreeIsType(a,a@\loc,makeAssignableType(t1));
            if (a@\loc in st.itemUses<0>)
                cs.constraints = cs.constraints + DefinedBy(t1,st.itemUses[a@\loc],a@\loc);
            return cs;
        }

        // Variable with an actual name
        case (Assignable)`<QualifiedName qn>` : {
            <cs, t1> = makeFreshType(cs);
            cs.constraints = cs.constraints + TreeIsType(a,a@\loc,makeAssignableType(t1));
            if (qn@\loc in st.itemUses<0>)
                cs.constraints = cs.constraints + DefinedBy(t1,st.itemUses[qn@\loc],qn@\loc);
            return cs;
        }
        
        // Subscript
        case `<Assignable al> [ <Expression e> ]` : {
            return gatherSubscriptAssignableConstraints(st,cs,a,al,e);
        }
        
        // Field Access
        case `<Assignable al> . <Name n>` : {
            return gatherFieldAccessAssignableConstraints(st,cs,a,al,n);
        }
        
        // If Defined or Default
        case `<Assignable al> ? <Expression e>` : {
            return gatherIfDefinedOrDefaultAssignableConstraints(st,cs,a,al,e);
        }
        
        // Tuple, with just one element
        case (Assignable)`< <Assignable ai> >` : {
            return gatherTupleAssignableConstraints(st,cs,a,ai);
        }

        // Tuple, with multiple elements
        case (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` : {
            return gatherTupleAssignableConstraints(st,cs,a,ai,al);
        }

        // Annotation
        case `<Assignable al> @ <Name n>` : {
            return gatherAnnotationAssignableConstraints(st,cs,a,al,n);
        }
    }
}

//
// Gather constraints for the subscript assignable: a[e]
//
// TODO: Add typing rule
//
public ConstraintBase gatherSubscriptAssignableConstraints(STBuilder st, ConstraintBase cs, Assignable ap, Assignable a, Expression e) {
    // a is an assignable of arbitrary type t1
    <cs,t1> = makeFreshType(cs);
    cs.constraints = cs.constraints + TreeIsType(a,a@\loc,makeAssignableType(t1));
    
    // e is of abritrary type t2, since we need to  know the subject type first
    // before we can tell if it is of the correct type (for instance, a map will have
    // indices of the domain type, while a list will have int indices)
    <cs,t2> = makeFreshType(cs);
    cs.constraints = cs.constraints + TreeIsType(e,e@\loc,t2);
    
    // the result is then based on a Subscript constraint, which will derive the resulting
    // type based on the input type and the concrete fields (which can be helpful IF they
    // are literals)
    <cs, t3> = makeFreshType(cs);
    cs.constraints = cs.constraints + Subscript(t1,[e],[t2],t3,ap@\loc) + TreeIsType(ap,ap@\loc,makeAssignableType(t3));        

    return cs;
}        

//
// Gather constraints for the field access assignable: a.n
//
//      a : AssignableType(t1), n fieldOf t1, t1.n : t2
// -----------------------------------------------------------------
//        a.n : AssignableType(t1) : AssignableType(t2)
//
public ConstraintBase gatherFieldAccessAssignableConstraints(STBuilder st, ConstraintBase cs, Assignable ap, Assignable a, Name n) {
    <cs,ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = TreeIsType(a,a@\loc,makeAssignableType(t1)); // a has an arbitrary type, t1
    Constraint c2 = FieldOf(n,t1,t2,n@\loc); // name n is actually a field of type t1, i.e., in x.f, f is a field of x, and has type t2
    Constraint c3 = TreeIsType(ap,ap@\loc,makeAssignableType(t2)); // the overall assignable has the same type as the type of field n, i.e., x.f is the same type as field f
    cs.constraints = cs.constraints + { c1, c2, c3 };
    return cs;
}

//
// Gather constraints for the if defined or default assignable: a?e
//
//      a : AssignableType(t1), e : t2, t2 <: t1
// -----------------------------------------------------------------
//                    a@n : AssignableType(t1)
//
public ConstraintBase gatherIfDefinedOrDefaultAssignableConstraints(STBuilder st, ConstraintBase cs, Assignable ap, Assignable a, Expression e) {
    <cs, ts> = makeFreshTypes(cs,2); t1 = ts[0]; t1 = ts[1];
    Constraint c1 = TreeIsType(a,a@\loc,makeAssignableType(t1));
    Constraint c2 = TreeIsType(e,e@\loc,t2);
    Constraint c3 = SubtypeOf(t2,t1,ap@\loc);
    Constraint c4 = TreeIsType(ap,ap@\loc,makeAssignableType(t1));
    cs.constraints = cs.constraints + { c1, c2, c3, c4 };
    return cs;
}

//
// Gather constraints for the annotation assignable: a@n
//
//      a : AssignableType(t1), n annotationOf t1, t2 typeof t1@n
// -----------------------------------------------------------------
//                    a@n : AssignableType(t2)
//
public ConstraintBase gatherAnnotationAssignableConstraints(STBuilder st, ConstraintBase cs, Assignable ap, Assignable a, Name n) {
    <cs, ts> = makeFreshTypes(cs,2); t1 = ts[0]; t2 = ts[1];
    Constraint c1 = TreeIsType(a,a@\loc,makeAssignableType(t1)); // el is of arbitrary type t1
    Constraint c2 = AnnotationOf(n,t1,t2,n@\loc); // n is an annotation on t1 with type t2
    Constraint c3 = TreeIsType(ap,ap@\loc,makeAssignableType(t2)); // the overall assignable has the same type as the type of the annotation
    cs.constraints = cs.constraints + { c1, c2, c3 };
    return cs;
}

//
// Gather constraints for the trivial tuple assignable: < a1 >
//
//      a1 : AssignableType(t1)
// ------------------------------------------
//   < a1 > : AssignableType(tuple[t1])
//
public ConstraintBase gatherTrivialTupleAssignableConstraints(STBuilder st, ConstraintBase cb, Assignable ap, Assignable a) {
    <cs,t1> = makeFreshType(cs);
    Constraint c1 = TreeIsType(a,a@\loc,makeAssignableType(t1)); // the element is of an arbitrary type
    Constraint c2 = TreeIstype(ap,makeAssignableType(makeTupleType([t1]))); // the tuple has on element of this arbitrary type 
    cs.constraints = cs.constraints + { c1, c2 };
    return cs;
}

//
// Gather constraints for the tuple expression: < e1, ..., en >
//
//      a1 : AssignableType(t1), ..., an : AssignableType(tn)
// ----------------------------------------------------------------------
//   < a1, ..., an > : AssignableType(tuple[t1, ..., tn])
//
public ConstraintBase gatherTupleAssignableConstraints(STBuilder st, ConstraintBase cb, Assignable ap, Assignable a, {Assignable ","}* al) {
    // The elements of the tuple are each of some arbitrary type
    list[RType] elements = [ ]; 
    for (ai <- al) { 
        <cs,t1> = makeFreshType(cs);
        cs.constraints = cs.constraints + TreeIsType(ai,ai@\loc,makeAssignableType(t1)); 
        elements += t1;
    }

    // The tuple is then formed from these types
    cs.constraints = cs.constraints + TreeIsType(ap,ap@\loc,makeAssignableType(makeTupleType(elements)));

    return cs;
}
