module experiments::Compiler::Examples::Tst5

import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import lang::rascal::types::AbstractName;
import lang::rascal::types::CheckerConfig;
import List;
import String;
import Relation;
import Set;

public Configuration addAlias(Configuration c, RName n, Vis vis, loc l, Symbol rt) {
	moduleId = head([i | i <- c.stack, m:\module(_,_) := c.store[i]]);
	moduleName = c.store[moduleId].name;
	fullName = appendName(moduleName, n);
	
	int addAlias() {
		existingDefs = invert(c.definitions)[l];
		if (!isEmpty(existingDefs)) return getOneFrom(existingDefs);

		itemId = c.nextLoc;
		c.nextLoc = c.nextLoc + 1;
		c.store[itemId] = \alias(n,rt,moduleId,l);
		c.definitions = c.definitions + < itemId, l >;
		return itemId;
	}

	//if (n notin c.typeEnv) {
	//	itemId = addAlias();
	//	c.typeEnv[n] = itemId;
	//	c.typeEnv[fullName] = itemId;
	//} else if (n in c.typeEnv && c.store[c.typeEnv[n]] is \alias) {
	//	if (c.store[c.typeEnv[n]].rtype == rt) {
	//		c.definitions = c.definitions + < c.typeEnv[n], l >;
	//	} else {
	//		itemId = addAlias();
	//		c = addScopeError(c, "A non-equivalent alias named <prettyPrintName(n)> is already in scope", l);
	//	}
	//} else if (n in c.typeEnv) {
	//	// An adt, alias, or sort with this name already exists in the same module. We cannot perform this
	//	// type of redefinition, so this is an error. This is because there is no way we can qualify the names
	//	// to distinguish them.
	//	itemId = addAlias();
	//	c = addScopeError(c, "An adt, alias, or nonterminal named <prettyPrintName(n)> has already been declared in module <prettyPrintName(moduleName)>", l);
	//}
	
	return c;
}


//value main(list[value] args)  = 
//convertName(appl(prod(label("default",sort("QualifiedName")),[conditional(label("names",\iter-seps(lex("Name"),[layouts("LAYOUTLIST"),lit("::"),layouts("LAYOUTLIST")])),{\not-follow(lit("::"))})],{}),[appl(regular(\iter-seps(lex("Name"),[layouts("LAYOUTLIST"),lit("::"),layouts("LAYOUTLIST")])),[appl(prod(lex("Name"),[conditional(seq([conditional(\char-class([range(65,90),range(95,95),range(97,122)]),{\not-precede(\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\iter-star(\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\not-follow(\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]),{delete(keywords("RascalKeywords"))})],{}),[appl(regular(seq([conditional(\char-class([range(65,90),range(95,95),range(97,122)]),{\not-precede(\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\iter-star(\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\not-follow(\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})])),[char(67),appl(regular(\iter-star(\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))),[char(104),char(101),char(99),char(107),char(83),char(116),char(97),char(116),char(101),char(109),char(101),char(110),char(116),char(115),char(83),char(116),char(114),char(105),char(110),char(103)])[@\loc=|unknown:///|(8,20,<1,8>,<1,28>)]])[@\loc=|unknown:///|(7,21,<1,7>,<1,28>)]])[@\loc=|unknown:///|(7,21,<1,7>,<1,28>)]])[@\loc=|unknown:///|(7,21,<1,7>,<1,28>)]])[@\loc=|unknown:///|(7,21,<1,7>,<1,28>)], {})
//
//;

//newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "G0Parser", G0);

	//sameLines(newGenerate("org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers", "G0Parser", G0), 
	//          readFile(|project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/G0Parser.java.gz|));
