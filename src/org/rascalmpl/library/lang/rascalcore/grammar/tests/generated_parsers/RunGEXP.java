package org.rascalmpl.library.lang.rascalcore.grammar.tests.generated_parsers;

import org.rascalmpl.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.ITree;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class RunGEXP {
	public final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	
	 public static void main(String[] args) {
		 
		 GEXPParser parser = new GEXPParser();
		 
		 char[] input;
		 ISourceLocation location = VF.sourceLocation("/Users/paulklint/git/rascal-core/src/org/rascalmpl/library/lang/rascalcore/grammar/tests/generated_parsers/file.exp");
		 input = "1+0".toCharArray();
		 ITree tree = parser.parse(
				    "E", 
				    location.getURI(), 
				    input, 
				    new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), 
				    new UPTRNodeFactory(false), (IRecoverer<IConstructor>) null
				    );
		
		 System.err.println(tree);
	    }

}
