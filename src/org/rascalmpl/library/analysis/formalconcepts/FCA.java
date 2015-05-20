package org.rascalmpl.library.analysis.formalconcepts;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetRelation;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class FCA {

  private final IValueFactory vf;

  public FCA(IValueFactory vf) {
    this.vf = vf;
  }
  
  // rel[&Object, &Attribute] fc, set[&Object] objects 
  public ISet sigma(ISet fc, ISet objects) {
    ISetRelation<ISet> fcRelation = fc.asRelation();
    if (objects.isEmpty()) {
      return fcRelation.range();
    }
    ISetWriter result = vf.setWriter();
    for (Iterator<IValue> attributes = fcRelation.range().iterator(); attributes.hasNext();) {
      IValue attr = attributes.next();
      boolean all = true;
      for (Iterator<IValue> objs = objects.iterator(); objs.hasNext();) {
        IValue obj = objs.next();
        all &= fc.contains(vf.tuple(obj, attr));
        if (!all) {
          break;
        }
      }
      if (all) {
        result.insert(attr);
      }
    }
    return result.done();
  }
//set[&Attribute] sigma(FormalContext[&Object, &Attribute] fc, set[&Object] objects)
//	= objects == {} ? fc<1> : { a | a <- fc<1>, all(obj <- objects, <obj,a> in fc)};
//      
//set[&Object] tau(FormalContext[&Object, &Attribute] fc, set[&Attributes] attributes) 
//	= attributes == {} ? fc<0> : { ob | ob <- fc<0>, all(a <- attributes, <ob, a> in fc)};

  public ISet tau(ISet fc, ISet attributes) {
    ISetRelation<ISet> fcRelation = fc.asRelation();
    if (attributes.isEmpty()) {
      return fcRelation.domain();
    }
    ISetWriter result = vf.setWriter();
    for (Iterator<IValue> objs = fcRelation.domain().iterator(); objs.hasNext();) {
      IValue obj= objs.next();
      boolean all = true;
      for (Iterator<IValue> attrs = attributes.iterator(); attrs.hasNext();) {
        IValue attr = attrs.next();
        all &= fc.contains(vf.tuple(obj, attr));
        if (!all) {
          break;
        }
      }
      if (all) {
        result.insert(obj);
      }
    }
    return result.done();
  }
}
