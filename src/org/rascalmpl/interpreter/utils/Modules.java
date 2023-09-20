package org.rascalmpl.interpreter.utils;

import io.usethesource.vallang.IList;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

import java.util.LinkedList;
import java.util.List;

import org.rascalmpl.ast.Toplevel;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

public class Modules {
  private static final IValueFactory vf = ValueFactoryFactory.getValueFactory();
  
  public static ISet getImports(ITree tree) {
    return get(tree, "default");
  }
  
  public static ISet getExtends(ITree tree) {
    ISet iSet = get(tree, "extend");
    return iSet;
  }
  
  public static ISet getExternals(ITree tree) {
    return get(tree, "external");
  }
  
  public static ISet getSyntax(ITree tree) {
    return get(tree, "syntax");
  }
  
  public static List<Toplevel> getTypeDeclarations(ITree tree, ASTBuilder builder) {
      ITree body = TreeAdapter.getArg(tree, "body");
      ITree toplevels = TreeAdapter.getArg(body, "toplevels");
      List<Toplevel> result = new LinkedList<>();
      
      for (IValue toplevel : TreeAdapter.getListASTArgs(toplevels)) {
          ITree declaration = TreeAdapter.getArg((ITree) toplevel, "declaration");
          String cons = TreeAdapter.getConstructorName(declaration);
          
          if (cons.equals("dataAbstract") || cons.equals("alias")) {
              result.add((Toplevel) builder.buildValue((ITree) toplevel));
          }
      }
      
      return result;
  }
  
  private static ISet get(ITree tree, String type) {
    ISetWriter set = vf.setWriter();
    ITree header = TreeAdapter.getArg(tree, "header");
    ITree imports = TreeAdapter.getArg(header, "imports");
    
    for (IValue imp : TreeAdapter.getListASTArgs(imports)) {
      String cons = TreeAdapter.getConstructorName((ITree) imp);
      if (cons.equals(type)) {
        set.insert(imp);
      }
    }
    
    return set.done();
  }
  
  public static String getName(ITree tree) {
	  ITree name = TreeAdapter.getArg(TreeAdapter.getArg(tree, "header"),"name");
	  ITree parts = TreeAdapter.getArg(name, "names");
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
      
      String p = TreeAdapter.yield((ITree) elem);
      
      if (p.startsWith("\\")) {
        p = p.substring(1);
      }
      
      result.append(p);
    }
    
    return result.toString();
  }
}
