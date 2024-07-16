module lang::rascal::tutor::conversions::TrimDoc

import util::FileSystem;
import lang::rascal::\syntax::Rascal;
import ParseTree;
import IO;
import String;

void editLibrary(loc root) {
    for (loc f <- find(root, "rsc")) {
        editModule(parse(#start[Module], f));
    }
}

void editModule(loc example) = editModule(parse(#start[Module], example).top);

void editModule(start[Module] m) {
    n = rewriteDocTags(m);
    writeFile(m@\loc.top, "<n>");
    return;
}

start[Module] rewriteDocTags(start[Module] m) = visit(m) {
    case (Tag) `@synopsis <TagString c>` 
        => [Tag] "@synopsis{<trim("<"<c>"[1..-1]>")>}"
    case jc:(Tag) `@javaClass<TagString c>` => jc
    case jc:(Tag) `@contributor<TagString c>` => jc
    case jc:(Tag) `@license<TagString c>` => jc
    case jc:(Tag) `@expected<TagString c>` 
     => [Tag] "@expected{<trim("<"<c>"[1..-1]>")>}"
    case jc:(Tag) `@ignoreCompiler<TagString c>` 
     => [Tag] "@ignoreCompiler{<trim("<"<c>"[1..-1]>")>}"
    case jc:(Tag) `@ignore<TagString c>` 
     => [Tag] "@ignore{<trim("<"<c>"[1..-1]>")>}"
     case jc:(Tag) `@tries<TagString c>` 
     => [Tag] "@tries{<trim("<"<c>"[1..-1]>")>}"
    case jc:(Tag) `@ignoreInterpreter<TagString c>` 
     => [Tag] "@ignoreInterpreter{<trim("<"<c>"[1..-1]>")>}"
    case (Tag) `@<Name n> <TagString c>`
        => [Tag] "@<n>{
                 '<trim("<"<c>"[1..-1]>")>
                 '}"
};
    