package org.rascalmpl.interpreter.utils;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.TreeAdapter;

public class Modules {
  private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
  
  public static ISet getImports(IConstructor tree) {
    return get(tree, "default");
  }
  
  public static ISet getExtends(IConstructor tree) {
    return get(tree, "extend");
  }
  
  public static ISet getExternals(IConstructor tree) {
    return get(tree, "external");
  }
  
  public static ISet getSyntax(IConstructor tree) {
    return get(tree, "syntax");
  }
  
  private static ISet get(IConstructor tree, String type) {
    ISetWriter set = vf.setWriter();
    IConstructor header = TreeAdapter.getArg(tree, "header");
    IConstructor imports = TreeAdapter.getArg(header, "imports");
    
    for (IValue imp : TreeAdapter.getListASTArgs(imports)) {
      String cons = TreeAdapter.getConstructorName((IConstructor) imp);
      if (cons.equals(type)) {
        set.insert(imp);
      }
    }
    
    return set.done();
  }
  
  public static String getName(IConstructor tree) {
    return TreeAdapter.yield(TreeAdapter.getArg(TreeAdapter.getArg(tree, "header"),"name"));
  }
}
