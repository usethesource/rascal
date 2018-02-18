package org.rascalmpl.core.library.lang.rascalcore.grammar.tests.generated_parsers;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.rascalmpl.core.library.rascalcore.grammar.tests.generated_parsers.lang_rascalcore_check_Test1Parser;
import org.rascalmpl.core.parser.gtd.recovery.IRecoverer;
import org.rascalmpl.core.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.core.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.core.values.ValueFactoryFactory;
import org.rascalmpl.core.values.uptr.ITree;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class RunTest1 {
	public final static IValueFactory VF = ValueFactoryFactory.getValueFactory();

	public static void main(String[] args) {

		lang_rascalcore_check_Test1Parser parser = new lang_rascalcore_check_Test1Parser();

		Path path = Paths.get("/Users/paulklint/git/rascal-core/src/org/rascalmpl/core/library/lang/rascalcore/grammar/tests/generated_parsers/Test1.txt");

		List<String> lines = null;
		try {
			lines = Files.readAllLines(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringWriter sw = new StringWriter();
		for(int i = 0; i < lines.size(); i++) {
			if(i > 0) sw.append("\n");
			sw.append(lines.get(i));
		}

		char[] inputChars = sw.toString().toCharArray();
		ITree tree = parser.parse(
				"Expression", 
				path.toUri(), 
				inputChars, 
				new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), 
				new UPTRNodeFactory(false), (IRecoverer<IConstructor>) null
				);

		System.err.println(tree);
	}

}
