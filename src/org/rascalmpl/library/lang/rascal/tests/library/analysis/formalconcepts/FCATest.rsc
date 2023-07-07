@ignoreCompiler{
Fix failing tests
}
module lang::rascal::tests::library::analysis::formalconcepts::FCATest

import util::Math;
import Set;
import ValueIO;
import analysis::formalconcepts::CXTIO;
import analysis::formalconcepts::FCA;

data IntProperties
	= composite()
	| even()
	| odd()
	| prime()
	| square()
	;
	
test bool numbersAreCorrect() {
	range = {*[1..11]};
	input = { <i, any(j <- (range - {i,1}), i % j == 0) ? composite() : prime()> | i <- range }
		+ { <i, i % 2 == 0 ? even() : odd()> | i <- range }
		+ { <i, square()> | i <- range, any(j <- range, j * j == i) }
		;
	return checkNoMadeUpAttributes(input) 
		&& checkNoUnRelatedConcepts(input);
}


rel[&TObject, &TAttribute] createRel(set[&TObject] objects, set[&TAttribute] attributes, int combine) {
	result = objects * attributes;
	if (size(objects) < 2 || size(attributes) < 2) {
		return result;
	}
	combine = toInt(size(result) * (abs(combine) % size(attributes)) / (size(attributes)*1.)); // 0 to 1 chance of how connected it should be
	int taken = 0;
	actualResult = {};
	for (r <- result, taken < combine) {
		actualResult += r;
		taken += 1;
	}
	if (actualResult != {}) {
		return actualResult;
	}
	return result;
}

test bool testNoMadeUpAttributes(set[&TObject] objects, set[&TAttribute] attributes, int combine)
	= checkNoMadeUpAttributes(createRel(objects, attributes, combine));
bool checkNoMadeUpAttributes(rel[&TObject, &TAttribute] input) {
	result = fca(input);
	for (/<set[&TObject] objects, set[&TAttribute] attributes> := result) {
		if (attributes > input[objects] && objects != {}) {
			throw "for <objects> we would maximally expect: <input[objects]> but got: <attributes>";
		}
	}
	return true;
}

@ignore{TODO: Fails for unknown reason}
test bool testNoUnRelatedConcepts(set[&TObject] objects, set[&TAttribute] attributes, int combine)
	= checkNoUnRelatedConcepts(createRel(objects, attributes, combine));

bool checkNoUnRelatedConcepts(rel[&TObject, &TAttribute] input) {
	ConceptLattice[&Object, &Attribute] result = fca(input);
	for (/Concept[&TObject, &TAttribute] _ : <{e, *rest}, _> := result, size(rest) > 0, size(rest) != (size(input<0>) - 1)) {
		if ((input[e] | it & input[r] | r <- rest) == {}) {
			throw "<rest + e> have nothing in common, but they are a concept? ";
		}
	}
	return true;
}

test bool fcaHasExpectedOutput() {
	result = fca(readCxt(|std:///lang/rascal/tests/library/analysis/formalconcepts/FCxt1.cxt|));
	reference = readBinaryValueFile(#ConceptLattice[str,str], |std:///lang/rascal/tests/library/analysis/formalconcepts/FCxt1.fca|);
	return result == reference;
}
