module lang::rascal::tests::library::util::ExplodeTests

import util::Explode;
import analysis::m3::AST;
import IO;
import lang::json::IO;

data Record(loc src=|unknown:///|) = record(str name, int age);
data Rolodex(loc src=|unknown:///|) = rolodex(list[Record] records);

Rolodex exampleAsTerm = rolodex([
    record("Aap", 1),
    record("Noot", 2),
    record("Mies", 3)
]);

Rolodex setupExample() {
    loc file = |memory://ExplodeTests/example.json|;
    writeJSON(file, exampleAsTerm);
    Rolodex result = readJSON(#Rolodex, file, trackOrigins=true);
    assert astNodeSpecification(result);
    return result;
}

test bool smokeExplodeTest() {
    Rolodex ast = setupExample();
    syntax[Rolodex] tree = explode(ast);
    return Tree _ := tree;
}

