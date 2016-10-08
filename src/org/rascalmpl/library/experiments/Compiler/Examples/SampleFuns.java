package org.rascalmpl.library.experiments.Compiler.Examples;

import java.io.IOException;
import java.net.URISyntaxException;

import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.java2rascal.Java2Rascal;
import org.rascalmpl.library.util.PathConfig;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;

public class SampleFuns {
    
    public static void main(String[] args) throws IOException, URISyntaxException {
      IValueFactory  vf = ValueFactoryFactory.getValueFactory();
      IList lst1 = vf.list(vf.string("a"), vf.string("b"));
      IList lst2 = vf.list(vf.integer(1), vf.integer(2), vf.integer(3));
      
      ISampleFuns sf = Java2Rascal.Builder.bridge(vf, new PathConfig(), ISampleFuns.class).setTrace().build();
      
      System.out.println(sf.fun1(lst1));
      System.out.println(sf.fun1(lst2));
      System.out.println(sf.fun1(5, sf.kw_fun1().delta(3)));
      
      System.out.println(sf.d1(3, sf.kw_d1()));
      
      System.out.println(sf.d3(3, sf.kw_d3().opt("def").x(23)));
      
      System.out.println(sf.d4("pqr", sf.kw_d1()));
      
      System.out.println(sf.d1(3, sf.kw_d1().x(20)));
      }
    
     
}
