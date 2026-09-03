module lang::rascal::tests::library::util::ExplodeTests

import util::Explode;
import analysis::m3::AST;
import IO;
import lang::json::IO;

data Record(loc src=|unknown:///|) = record(Name name, Age age);
data Name = name(str x);
data Age = age(str a);
data Rolodex(loc src=|unknown:///|) = rolodex(list[Record] records);

Rolodex exampleAsTerm = rolodex([
    record(name("Aap"), age("1")),
    record(name("Noot"), age("2")),
    record(name("Mies"), age("3"))
]);

public loc exampleFile = |memory://ExplodeTests/example.json|;

Rolodex setupExample() {
    writeJSON(exampleFile, exampleAsTerm, indent=4);
    Rolodex result = readJSON(#Rolodex, exampleFile, trackOrigins=true);
    assert astNodeSpecification(result);
    return result;
}

test bool explodeYieldContract() {
    Rolodex ast = setupExample();
    syntax[Rolodex] tree = explode(ast);
    return readFile(exampleFile) == "<tree>";
}

