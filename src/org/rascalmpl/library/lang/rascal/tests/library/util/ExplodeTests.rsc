module lang::rascal::tests::library::util::ExplodeTests

import util::Explode;
import analysis::m3::AST;
import IO;
import lang::json::IO;
import util::IDEServices;

data Record(loc src=|unknown:///|) = record(Name name, Age age);
data Name(loc src=|unknown:///|) = name(str x);
data Age(loc src=|unknown:///|) = age(str a);
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

test bool explodeDeepMatch() {
    Rolodex ast = setupExample();
    syntax[Rolodex] tree = explode(ast);
    return (0 | it + 1 | /syntax[Record] _ := tree) == (0 | it + 1 | /data[Record] _ := ast);
}

test bool explodeVisit() {
    Rolodex ast = setupExample();
    int astRecordCount = 0;
    visit (ast) {
        case data[Record] _ : astRecordCount += 1;
    }

    syntax[Rolodex] tree = explode(ast);
    int treeRecordCount = 0;
    visit (tree) {
        case syntax[Record] _ : treeRecordCount += 1;
    }

    return astRecordCount == treeRecordCount;
}

@synopsis{prints to strings next to each other in html for debugging purposes}
bool showDiff(str a, str b) {
    showInteractiveContent(html("\<table\>\<tr\>\<td\>
                                '\<pre\>
                                '<a>
                                '\</pre\>\</td\>\<td\>
                                '\<pre\>
                                '<b>
                                '\</pre\>\</td\>\</tr\>\</table\>"));
    return a == b;
}

@synopsis{Makes sure each sub-ast aligns with each sub-tree and with each sub-string}
test bool explodeYieldContract() {
    Rolodex ast = setupExample();
    syntax[Rolodex] tree = explode(ast);
    
    // get all the AST nodes
    asts     = [a  | /node a := ast];
    
    // get all the Tree nodes can coincide with abstract data-type nodes
    trees    = [x  | /Tree x := tree, syntax[&T] _ := x];

    together = zip2(asts, trees);

    // abstract nodes and concrete nodes align per src field
    assert (true | it && a.src == b.src           | <a, b> <- together);

    // the concrete nodes yield equals to the substring of the file that is indicated by the src field
    assert (true | it && readFile(b.src) == "<b>" | b <- trees);

    return size(asts) == size(trees);
}

