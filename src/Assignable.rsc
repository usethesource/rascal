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
// TODO: Need to "lock" certain parts of the resulting type, since (for instance)
// if we have a name defined with a non-inferred type we cannot just assign and lub it.
//
public ConstraintBase gatherAssignableConstraints(STBuilder st, ConstraintBase cb, Assignable a) {
    switch(a) {
        // Bracket
        case (Assignable)`(<Assignable ac>)` : 
            return addConstraintForLoc(cb, a@\loc, typeForLoc(cb, ac@\loc));
        
        // Variable _
        case (Assignable)`_` : {
	        if (a@\loc in st.itemUses<0>) {
			    RType itemType = makeVoidType();
	        	definingIds = st.itemUses[a@\loc];
			    if (size(definingIds) > 1) {
			        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
			    } else {
			        itemType = getTypeForItem(st, getOneFrom(definingIds));
			    }
		        cb = addConstraintForLoc(cb, a@\loc, itemType);
	        } else {
	            cb = addConstraintForLoc(cb, a@\loc, makeFailType("No definition for this variable found",a@\loc));
	        }
            return cb;
        }

        // Variable with an actual name
        case (Assignable)`<QualifiedName qn>` : {
	        if (qn@\loc in st.itemUses<0>) {
			    RType itemType = makeVoidType();
	        	definingIds = st.itemUses[qn@\loc];
			    if (size(definingIds) > 1) {
			        itemType = ROverloadedType({ getTypeForItem(st, itemId) | itemId <- definingIds });
			    } else {
			        itemType = getTypeForItem(st, getOneFrom(definingIds));
			    }
		        cb = addConstraintForLoc(cb, qn@\loc, itemType);
	        } else {
	            cb = addConstraintForLoc(cb, qn@\loc, makeFailType("No definition for this variable found",qn@\loc));
	        }
            return cb;
        }
        
        // Subscript
        case (Assignable)`<Assignable al> [ <Expression e> ]` : {
            return gatherSubscriptAssignableConstraints(st,cb,a,al,e);
        }
        
        // Field Access
        case (Assignable)`<Assignable al> . <Name n>` : {
            return gatherFieldAccessAssignableConstraints(st,cb,a,al,n);
        }
        
        // If Defined or Default
        case (Assignable)`<Assignable al> ? <Expression e>` : {
            return gatherIfDefinedOrDefaultAssignableConstraints(st,cb,a,al,e);
        }
        
        // Tuple, with just one element
        case (Assignable)`< <Assignable ai> >` : {
            return gatherTupleAssignableConstraints(st,cb,a,ai);
        }

        // Tuple, with multiple elements
        case (Assignable)`< <Assignable ai>, <{Assignable ","}* al> >` : {
            return gatherTupleAssignableConstraints(st,cb,a,ai,al);
        }

        // Annotation
        case `<Assignable al> @ <Name n>` : {
            return gatherAnnotationAssignableConstraints(st,cb,a,al,n);
        }
    }
    
    throw "Unmatched assignable <a> at <a@\loc>";
}

//
// Gather constraints for the subscript assignable: a[e]
//
// TODO: Add typing rule
//
public ConstraintBase gatherSubscriptAssignableConstraints(STBuilder st, ConstraintBase cb, Assignable ap, Assignable a, Expression e) {
    // a is an assignable of arbitrary type ta
    ta = typeForLoc(cb, a@\loc);
    
    // e is an expression of arbitrary type te
    te = typeForLoc(cb, e@\loc);
    
    // tr is the result of subscripting type ta with subscript type te. This is
    // set as the result of the entire assignable.
    < cb, tr > = makeFreshType(cb);
    cb.constraints = cb.constraints + Subscript(ta,[e],[te],tr,ap@\loc);        
    cb = addConstraintForLoc(cb, ap@\loc, tr);

    return cb;
}        

//
// Gather constraints for the field access assignable: a.n
//
//      a : t1, n fieldOf t1, t1.n : t2
// -----------------------------------------------------------------
//        a.n : t1 : t2
//
public ConstraintBase gatherFieldAccessAssignableConstraints(STBuilder st, ConstraintBase cb, Assignable ap, Assignable a, Name n) {
    // a is an assignable of arbitrary type ta
    ta = typeForLoc(cb, a@\loc);
    
    // field n must be a field of type ta, and is of type ft
    < cb, ft > = makeFreshType(cb);
    cb.constraints = cb.constraints + FieldOf(n, ta, ft, n@\loc);
    
    // the assignable has the type of the field -- i.e., if we are assigning
    // to x.f, the type of x.f is the type of field f in the type of x
    cb = addConstraintForLoc(cb, ap@\loc, ft);
    
    return cb;
}

//
// Gather constraints for the if defined or default assignable: a?e
//
//      a : t1, e : t2, t2 <: t1
// -----------------------------------------------------------------
//                    a@n : t1
//
public ConstraintBase gatherIfDefinedOrDefaultAssignableConstraints(STBuilder st, ConstraintBase cb, Assignable ap, Assignable a, Expression e) {
    // a is an assignable of arbitrary type ta
    ta = typeForLoc(cb, a@\loc);
    
    // e is an expression of arbitrary type te
    te = typeForLoc(cb, e@\loc);
    
    // we verify that e is assignable to a, and we need to take this into
    // account for typing, since (if e is assigned into a as the default)
    // this would set the type of an inference var
    < cb, tr > = makeFreshType(cb);
    cb.constraints = cb.constraints + Assignable(ta, te, tr, ap@\loc);
    
    // the overall type if the type of a
    cb = addConstraintForLoc(cb, ap@\loc, ta);
    
    return cb;
}

//
// Gather constraints for the annotation assignable: a@n
//
//      a : t1, n annotationOf t1, t2 typeof t1@n
// -----------------------------------------------------------------
//                    a@n : t2
//
public ConstraintBase gatherAnnotationAssignableConstraints(STBuilder st, ConstraintBase cb, Assignable ap, Assignable a, Name n) {
    // a is an assignable of arbitrary type ta
    ta = typeForLoc(cb, a@\loc);
    
    // n must be an annotation on type ta, and is of type at
    < cb, at > = makeFreshType(cb);
    cb.constraints = cb.constraints + AnnotationOf(n, ta, at, n@\loc);
    
    // the overall type is the type of the annotation
    cb = addConstraintForLoc(cb, ap@\loc, at);

    return cb;
}

//
// Gather constraints for the trivial tuple assignable: < a1 >
//
//      a1 : t1
// ------------------------------------------
//   < a1 > : tuple[t1]
//
public ConstraintBase gatherTrivialTupleAssignableConstraints(STBuilder st, ConstraintBase cb, Assignable ap, Assignable a) {
    // a is an assignable of arbitrary type ta
    ta = typeForLoc(cb, a@\loc);

    // the overall type is a tuple of a
    cb = addConstraintForLoc(cb, ap@\loc, makeTupleType([ ta ]));
    
    return cb;
}

//
// Gather constraints for the tuple expression: < e1, ..., en >
//
//      a1 : t1, ..., an : tn
// ----------------------------------------------------------------------
//   < a1, ..., an > : tuple[t1, ..., tn]
//
public ConstraintBase gatherTupleAssignableConstraints(STBuilder st, ConstraintBase cb, Assignable ap, Assignable a, {Assignable ","}* al) {
    // a is an assignable of arbitrary type
    list[RType] elements = [ typeForLoc(cb, a@\loc) ];

    // each ai, element of al, has arbitrary type
    for (ai <- al) elements = elements + typeForLoc(cb,ai@\loc);
    
    // the overall type is a tuple of the element types
    cb = addConstraintForLoc(cb, ap@\loc, makeTupleType(elements));
    
    return cb;
}
