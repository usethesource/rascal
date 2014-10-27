@doc{
Synopsis: M3 common source code model represent facts extracted from source code for use in downstream metrics or other analyses.

Description:

The [$Rascal/Libraries/analysis/m3] core defines basic concepts such as:

* qualified names: we use [$Values/Location]s to model qualified names for each programming language
* containment: which artifacts are contained in which other artifacts
* declarations: where artifacts are defined
* uses: where declared artifacts are used
* types: which artifacts has which types

From this core, [$Rascal/Libraries/analysis/m3] is supposed to be extended with features specific for a programming language. See for example [lang/java/m3].

Benefits:

* Qualified names in the shape of [$Values/Location]s are a uniform and generic way of identifying source code artifacts, that can be extended across languages, projects, and versions.
* M3 helps standardizing the shape of facts we extract from source code for all different languages, limiting the element of surprise.
* When we use M3 for many languages, common IDE features are made reusable (such as clicking from an extracted fact to the code that generated it).
* Some downstream analyses may be reusable between different languages if they all map to M3.

Pitfalls:

* Even though different languages may map to the same M3 model, this does not mean that the semantics is the same. Downstream
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
extend analysis::m3::TypeSymbol;
 
data Modifier;
data M3 = m3(loc id);
             
anno rel[loc name, loc src]        M3@declarations;            // maps declarations to where they are declared. contains any kind of data or type or code declaration (classes, fields, methods, variables, etc. etc.)
anno rel[loc name, TypeSymbol typ] M3@types;                   // assigns types to declared source code artifacts
anno rel[loc src, loc name]        M3@uses;                    // maps source locations of usages to the respective declarations
anno rel[loc from, loc to]         M3@containment;             // what is logically contained in what else (not necessarily physically, but usually also)
anno list[Message]                 M3@messages;                // error messages and warnings produced while constructing a single m3 model
anno rel[str simpleName, loc qualifiedName]  M3@names;         // convenience mapping from logical names to end-user readable (GUI) names, and vice versa
anno rel[loc definition, loc comments]       M3@documentation; // comments and javadoc attached to declared things
anno rel[loc definition, Modifier modifier] M3@modifiers;     // modifiers associated with declared things

public data Language(str version = "")
  = generic()
  ;

@doc{
	Create an empty m3 term with empty annotations
}
public M3 emptyM3(loc id)
{
	m = m3(id);

	m@declarations = {};
	m@uses = {};
	m@containment = {};
	m@documentation = {};
	m@modifiers = {};
	m@messages = [];
	m@names = {};
	m@types = {};

	return m;
}

private value compose(set[&T] s1, set[&T] s2) = s1 + s2; // works on rel as well
private value compose(list[&T] l1, list[&T] l2) = l1 + l2;
private value compose(map[&T, &U] m1, map[&T, &U] m2) = m1 + m2;
private value compose(value v1, value v2) { throw "can\'t compose non-collections or values of different types"; }

@doc{
	Generic function to compose the annotations of a set of M3s.
}
@memo
M3 composeM3(loc id, set[M3] models)
{
	set[str] allAnnoNames = { *domain(getAnnotations(m)) | m <- models };

	map[str, value] allAnnos = ();

	for (m <- models)
	{
		annos = getAnnotations(m);

		for (name <- allAnnoNames, name in annos)
		{
			if (allAnnos[name]?)
			{
				try
				{
					allAnnos[name] = compose(allAnnos[name], annos[name]);
				}
				catch e:
				; // ignore
			}
			else
			{
				allAnnos[name] = annos[name];
			}
		}
	}

	return setAnnotations(m3(id), allAnnos);
}

bool isEmpty(M3 model) = model.id.scheme == "unknown";

@doc{
Synopsis: constructs a recursive FileSystem from a binary [Location] relation.

Description: this function will not terminate if the relation is cyclic.
}
@memo set[FileSystem] relToFileSystem(rel[loc parent, loc child] r) {
  FileSystem rec(loc l, set[loc] args) = (args == {}) ? file(l) : directory(l, {rec(c, r[c]) | c <- args});
  return {rec(t, r[t]) | t <- top(r)};
}

set[loc] files(M3 model) {
 todo = top(model@containment);
 done = {};
 
 while (todo != {}) {
   <elem,todo> = takeOneFrom(todo);
   if (isDirectory(elem)) {
     todo += model@containment[elem];
   }
   else {
     done += elem;
   }
 }
 
 return done;
}

@doc{
Synopsis: transform the containment relation to a recursive tree model

Description:

Benefits:

* Transforming the containment relation to a tree model allows further analysis using operators
such as [Visit] and [Descendant] which is sometimes more convenient.

Pitfalls:

* Do not forget that the relational operators such as [TransitiveClosure], [Comprehension] and [Composition] may be just
as effective and perhaps more efficient, as applied directly on the containment relation. 
}
set[FileSystem] containmentToFileSystem(M3 model) = relToFileSystem(model@containment);

list[Message] checkM3(M3 model) {
  result  = [m | m <- model@messages, m is error];
  result += [error("undeclared element in containment", decl) | decl <- model@containment<to> - model@declarations<name>];
  result += [error("non-root element is not contained anywhere", decl) | decl <- model@containment<from> - model@declarations<name> - top(model@containment)];
  return result;
}
