@doc{
#### Synopsis

M3 common source code model represent facts extracted from source code for use in downstream metrics or other analyses.

#### Description

The M3 ((Library:analysis::m3::Core)) defines basic concepts such as:

*  qualified names: we use ((Values-Location))s to model qualified names for each programming language
*  containment: which artifacts are contained in which other artifacts
*  declarations: where artifacts are defined
*  uses: where declared artifacts are used
*  types: which artifacts has which types

From this ((Library:analysis::m3::Core)) is supposed to be extended with features specific for a programming language. See for example [Java M3]((lang::java::m3::Core)).

#### Benefits

*  Qualified names in the shape of ((Values-Location)) are a uniform and generic way of identifying source code artifacts, that can be extended across languages, projects, and versions.
*  M3 helps standardizing the shape of facts we extract from source code for all different languages, limiting the element of surprise.
*  When we use M3 for many languages, common IDE features are made reusable (such as clicking from an extracted fact to the code that generated it).
*  Some downstream analyses may be reusable between different languages if they all map to M3.

#### Pitfalls

*  Even though different languages may map to the same M3 model, this does not mean that the semantics is the same. Downstream
metrics or other analysis tools should still take semantic differences between programming languages into account.
}
module analysis::m3::Core

import Message;
import Set;
import IO;
import util::FileSystem;
import analysis::graphs::Graph;
import Node;
import Map;
import List;
import Relation;
extend analysis::m3::TypeSymbol;
 
data Modifier;

@doc{
#### Synopsis

m3 model constructor

#### Description

This constructor holds all information to an m3 model. It is identified by the _id_ field,
which should be a unique name for the project or file that the m3 model was constructor for.

Attached to this m3 model will be annotations with the specific information.
}
data M3(
	rel[loc name, loc src] declarations = {},	            // maps declarations to where they are declared. contains any kind of data or type or code declaration (classes, fields, methods, variables, etc. etc.)
  set[loc] implicitDeclarations = {},                   // some languages provide builtin implicit declarations, e.g. |java+class:///java/lang/Object| may not have a source location but still exist.
	rel[loc name, TypeSymbol typ] types = {},	            // assigns types to declared source code artifacts
	rel[loc src, loc name] uses = {},			                // maps source locations of usages to the respective declarations
	rel[loc from, loc to] containment = {},		            // what inner declaration (to) is logically contained in what outer declaration (`from`) (not necessarily physically, but usually also)
	list[Message] messages = [],				                  // error messages and warnings produced while constructing a single m3 model
	rel[str simpleName, loc qualifiedName] names = {},		// convenience mapping from logical names to end-user readable (GUI) names, and vice versa
	rel[loc definition, loc comments] documentation = {},	// comments and javadoc attached to declared things
	rel[loc definition, Modifier modifier] modifiers = {}	// modifiers associated with declared things
) = m3(
	loc id);
             

public data Language(str version = "")
  = generic()
  ;

@doc{
	Create an empty m3 term with empty annotations
}
//TODO: Deprecated method, replace any calls to this method with default constructor
public M3 emptyM3(loc id) = m3(id);

@doc{
	Generic function to compose the annotations of a set of M3s.
}
@memo
M3 composeM3(loc id, set[M3] models) {
	M3 comp = m3(id);

	comp.declarations = {*model.declarations | model <- models};
  comp.implicitDeclarations = {*model.implicitDeclarations | model <- models};
	comp.types = {*model.types | model <- models};
	comp.uses = {*model.uses | model <- models};
	comp.containment = {*model.containment | model <- models};
	comp.messages = [*model.messages | model <- models];
	comp.names = {*model.names | model <- models};
	comp.documentation = {*model.documentation | model <- models};
	comp.modifiers = {*model.modifiers | model <- models};

	return comp;
}

@doc{
	Generic function to apply a difference over the annotations of a list of M3s.
}
@memo
M3 diffM3(loc id, list[M3] models) {
	assert size(models) >= 2;

	M3 first = models[0];
	M3 others = composeM3(id, toSet(models[1..]));
	M3 diff = m3(id);

	diff.declarations = first.declarations - others.declarations;
  diff.implicitDeclarations = first.implicitDeclarations - others.implicitDeclarations;
	diff.types = first.types - others.types;
	diff.uses = first.uses - others.uses;
	diff.containment = first.containment - others.containment;
	diff.names = first.names - others.names;
	diff.documentation = first.documentation - others.documentation;
	diff.modifiers = first.modifiers - others.modifiers;

	return diff;
}

@memo
M3 modifyM3(loc id, list[M3] models, value (&T,&T) fun) { 
    set[str] allAnnoNames = { *domain(getKeywordParameters(m)) | m <- models };
    map[str, value] allAnnos = ();
    
    for(m <- models) {
        annos = getKeywordParameters(m);
        
        for(name <- allAnnoNames, name in annos) {
        
            if(allAnnos[name]?) {
                try {
                    allAnnos[name] = fun(allAnnos[name], annos[name]);
                }
                catch _:
                ; // ignore
            }
            else {
                allAnnos[name] = annos[name];
            }
        }
    }
    return setKeywordParameters(m3(id), allAnnos);
}


bool isEmpty(M3 model) = model.id.scheme == "unknown";

@doc{
#### Synopsis

constructs a recursive FileSystem from a binary [Location] relation.

#### Description

}
@memo set[FileSystem] relToFileSystem(rel[loc parent, loc child] r) {
  FileSystem rec(loc l, set[loc] args) = (args == {}) ? file(l) : directory(l, {rec(c, r[c]) | c <- args});
  return {rec(t, r[t]) | t <- top(r)};
}

set[loc] files(M3 model) {
 todo = top(model.containment);
 done = {};
 
 while (todo != {}) {
   <elem,todo> = takeOneFrom(todo);
   if (isDirectory(elem)) {
     todo += model.containment[elem];
   }
   else {
     done += elem;
   }
 }
 
 return done;
}

@doc{
#### Synopsis

transform the containment relation to a recursive tree model

#### Description

#### Benefits

*  Transforming the containment relation to a tree model allows further analysis using operators
such as ((Statements-Visit)) and ((Descendant)) which is sometimes more convenient.

#### Pitfalls

*  Do not forget that the relational operators such as [TransitiveClosure], [Comprehension] and [Composition] may be just
as effective and perhaps more efficient, as applied directly on the containment relation. 
}
set[FileSystem] containmentToFileSystem(M3 model) = relToFileSystem(model.containment);

list[Message] checkM3(M3 model) {
  result  = [m | m <- model.messages, m is error];
  result += [error("undeclared element in containment", decl) | decl <- model.containment<to> - model.declarations<name>];
  result += [error("non-root element is not contained anywhere", decl) | decl <- model.containment<from> - model.declarations<name> - top(model.containment)];
  return result;
}

test bool testM3ModelConsistency(M3 m) {
    decls = m.declarations<name>;

    // nothing that is contained here does not not have a declaration, except the outermost translationUnit
    assert m.declarations<name> - m.containment<to> - top(m.containment) == {};
   
    // everything in the containment relation has been declared somewhere
    assert carrier(m.containment) - decls - m.implicitDeclarations == {};

    // everything in the declarations relation is contained somewhere
    assert decls - carrier(m.containment) == {};

    // all uses point to actual declarations
    assert m.uses<name> - m.declarations<name> - m.implicitDeclarations == {};

    // in this example, all declarations are used at least once
    assert m.declarations<name> - m.uses<name> == {};

    // m.declarations is one-to-one
    assert size(m.declarations<name>) == size(m.declarations);

   return true;
}
