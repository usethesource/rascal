package org.rascalmpl.core.library.lang.rascalcore.grammar.tests.generated_parsers;

import org.rascalmpl.core.library.rascalcore.grammar.tests.generated_parsers.lang_rascalcore_check_Test1Parser;
import org.rascalmpl.core.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.core.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.core.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.core.values.ValueFactoryFactory;
import org.rascalmpl.core.values.uptr.ITree;
import org.rascalmpl.library.Prelude;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class RunTest1 {
	public final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
	
	 public static void main(String[] args) {
		 
		 lang_rascalcore_check_Test1Parser parser = new lang_rascalcore_check_Test1Parser();
		 IValueFactory vf = ValueFactoryFactory.getValueFactory();
		 Prelude prelude = new Prelude(vf);
	
		 ISourceLocation location = VF.sourceLocation("/Users/paulklint/git/rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/grammar/tests/generated_parsers/Test1.txt");
		 String input = prelude.readFile(location).toString();
		 input = input.substring(1, input.length()-1);
				 
		 char[] inputChars = input.toCharArray();
		 ITree tree = parser.parse(
				    "E", 
				    location.getURI(), 
				    inputChars, 
				    new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), 
				    new UPTRNodeFactory(false), (IRecoverer<IConstructor>) null
				    );
		
		 System.err.println(tree);
	    }

}
