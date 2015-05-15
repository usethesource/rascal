package org.rascalmpl.interpreter.utils;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.RascalValueFactory.Tree;
import org.rascalmpl.values.uptr.TreeAdapter;

public class Modules {
  private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
  
  public static ISet getImports(Tree tree) {
    return get(tree, "default");
  }
  
  public static ISet getExtends(Tree tree) {
    ISet iSet = get(tree, "extend");
    return iSet;
  }
  
  public static ISet getExternals(Tree tree) {
    return get(tree, "external");
  }
  
  public static ISet getSyntax(Tree tree) {
    return get(tree, "syntax");
  }
  
  private static ISet get(Tree tree, String type) {
    ISetWriter set = vf.setWriter();
    Tree header = TreeAdapter.getArg(tree, "header");
    Tree imports = TreeAdapter.getArg(header, "imports");
    
    for (IValue imp : TreeAdapter.getListASTArgs(imports)) {
      String cons = TreeAdapter.getConstructorName((Tree) imp);
      if (cons.equals(type)) {
        set.insert(imp);
      }
    }
    
    return set.done();
  }
  
  public static String getName(Tree tree) {
	  Tree name = TreeAdapter.getArg(TreeAdapter.getArg(tree, "header"),"name");
	  Tree parts = TreeAdapter.getArg(name, "names");
    IList args = TreeAdapter.getListASTArgs(parts);
    StringBuilder result = new StringBuilder();
    
    boolean first = true;
    for (IValue elem : args) {
      if (!first) {
        result.append("::");
      }
      else {
        first = false;
      }
      
      String p = TreeAdapter.yield((Tree) elem);
      
      if (p.startsWith("\\")) {
        p = p.substring(1);
      }
      
      result.append(p);
    }
    
    return result.toString();
  }
}
