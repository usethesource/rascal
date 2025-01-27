
@synopsis{M3 common source code model represent facts extracted from source code for use in downstream metrics or other analyses.}
@description{
The M3 ((Library:analysis::m3::Core)) defines basic concepts such as:

*  qualified names: we use locations to model qualified names for each programming language
*  containment: which artifacts are contained in which other artifacts
*  declarations: where artifacts are defined
*  uses: where declared artifacts are used
*  types: which artifacts has which types

From this ((Library:analysis::m3::Core)) is supposed to be extended with features specific for a programming language. See for example [Java M3]((lang::java::m3::Core)).
}
@benefits{
*  Qualified names in the shape of a location are a uniform and generic way of identifying source code artifacts, that can be extended across languages, projects, and versions.
*  M3 helps standardizing the shape of facts we extract from source code for all different languages, limiting the element of surprise.
*  When we use M3 for many languages, common IDE features are made reusable (such as clicking from an extracted fact to the code that generated it).
*  Some downstream analyses may be reusable between different languages if they all map to M3.
}
@pitfalls{
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
 
@synopsis{Modifier's are abstract syntax trees of type and declaration modifiers found in programming languages}
@description{
In ((data::M3)) models the modifiers of each definition are collected for easy lookup.
} 
data Modifier;


@synopsis{An _M3_ model is a composable database of ground-truth facts about a specific set of source code artifacts}
@description{
This `m3` data constructor holds all information to an M3 model. It is identified by the _id_ field,
which should be a unique name for the project or file or composition that the M3 model was constructed for.

Practically all relations in an M3 model relate source locations of the `loc` type:
1. _Name_ locations are logical locations that represent fully qualified names of declared artefacts. 
   * For example: `|java+method:///java/util/List/toString()|`
   * Name locations are always indicated with the column name `name` in any relation below.
2. _Source_ location are physical locations that point to an exact (part of) a source code file:
   * For example: `|project://jre13/src/main/java/java/util/List.java|(100,350,<20,0>,<25,10>)`
   * Source locations are always indicated with the column name `src` in any relation below.

These are the _core_ facts stored in M3 models because 90% of all programming languages have
these core features:

| Ground truth fact kind about source code           | Description |                
| -------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `set[Language]`                                    | describes the languages this model contains information about, including their version numbers for the sake of transparency |
| `rel[loc name, loc src] declarations`              | maps qualified names of relations to their original source location in the current model, if any. |
|	`rel[loc src, loc name] uses`                      | as the _inverse_ of `declarations` this maps every source location where a declared artefact is used to its fully qualified name.|
| `set[loc] implicitDeclarations`                    | provides a set of qualified names of things that are present no matter what in a programming language, for completeness sake.|
| `rel[loc from, loc to] containment`                | links the qualified name of the outer (from) declaration to the names of everything that is declared inside of it (to).|
| `rel[loc name, TypeSymbol typ] types`              | akin to the classical symbol table, this relation maps fully qualified names to a TypeSymbol representation of their static type.|
| `rel[str simpleName, loc qualifiedName] names`     | is for producing human/user readable messages about declared artefacts; every fully qualified name {c,sh,w}ould have one.|
| `list[Message] messages`                           | collects the errors and warnings produced the parser/compiler that populated this model. |
| `rel[loc definition, loc comments]` documentation` | links documentation strings (comments) inside the source code to specific declarations. A typical example would be _JavaDoc_ comments to a class definition.|
|	`rel[loc definition, Modifier modifier] modifiers` | links modifiers to fully qualified declarations (typically access modifiers like `public` or `private` or storage modifiers such as `static`)|

More relations would be added by M3 model builders for specific programming paradigms.
}
@benefits{
* Logical name locations are both a readable and optimally accurate references to specific source code artefacts. No accidental confusion by mixing namespaces.
* Binary relations on locations are easily composed to infer new and interesting facts. 
   * In particular the composition operator and comprehensions can be used to easily deduce or infer more facts;
   * Composing `declarations o uses` immediately generates a detailed dependency graph 
   * Composing `uses o declarations` immediately produces a _jump-to-definition_ graph, while its inverse `(uses o declarations)<1,0>` produces a _references_ graph.
* M3 models never use _maps_ because those are not safely compositional (one maps could overwrite the facts of another).
* Specific programming paradigms and languages may add new facts to the M3 relation. 
   * For Java and C++ there would be class extension and interface implementation relations, for example.
   * PHP would add a relation to link classes to traits, etc. etc.  
* Every relation, set, list of facts in an M3 model is _composable_ by union or concatenation.
This makes an entire model composable by composing every item, respectively. The ((composeM3))
function implements such a union. 
   * Composition can be used to easily construct project-level models from file-level models.
   * Composition can be used to simulate (dynamic) linkage between projects.
   * Composition can be used to start simulating remote-procedure calls and shared memory, and other inter-programming language composition like _JNI_.
* M3 models can be cached (efficiently) on disk using functions from ((ValueIO)). A single stored M3 model simulates an _object file_,
while a composed M3 model is more like an `.a` archive or a `.jar` archive. 
   * Integrating M3 model caching during a build process (e.g ANT, Makefiles or Maven) is a smart way to make whole program analysis fast and incremental.
   * Integrating M3 model caching in Integrated Development Environments (e.g. the Language Server Protocol) enables fast and incremental IDE features based on whole program indexing that M3 provides.
}
@pitfalls{
* Initial M3 models should not contain _inferred_ information, only ground truth data as extracted from parse trees or abstract syntax trees, and facts from the static name and type resolution stages of a compiler or interpreter. 
   * Inference is certainly possible (say to construct an over-approximated call graph), but that is _not_ what we call an _M3_ model.
   * The reason is that _metrics_ of over- and under-approximated abstract interpretations of programs quickly loose their tractability and understandability, and also in 
   (the education of) empirical scientific methods it is of grave importance to separate facts from heuristic inference. 
* Simply calling ((composeM3)) does not immediately represent the full static semantics of program composition. Namely, what the union of facts, as implemented by ((composeM3)) _means_ depends on programming language
semantics. Sometimes to connect the merged models also new connections must be made programmatically to complete the connections. Such analyses are static simulations of the `linking` and `loading` stages of programming languages. 
When we simulate static composition, these analyses are ground truth, but when we simulate dynamic loading we have to treat the results as heuristic inferences.
* Not every programming language front-end that creates M3 models has to have implemented all the above relations (yet). Constructing
such a front-end may take time and incrementally growing models can already be very useful.
* Even though M3 models can have errors and be partially populated, please be aware that partially correct programs lead to partically correct models and all downstream analysis is correspondingly inaccurate.
* In statically types programming languages the `declarations` relation is typically one-to-one and the `uses` relation is `many-to-one`,
which means that name resolution is _unique_ at _compile-time_. However this is not required for other more dynamic languages, and this is fine.
You will see that one qualified name could potentionally resolve to different artefacts at run-time. This will be reflected by the `uses` relation
also having _many-to-many_ tuples in it. **Be careful how you count**, for example, _dependencies_ or _coupling_ in such cases since we
are literally already over-approximating the reality of the running program.
}
data M3(
  set[Language] languages                          = {},
	rel[loc name, loc src] declarations              = {},	            
  set[loc] implicitDeclarations                    = {},                   
	rel[loc name, TypeSymbol typ] types              = {},	            
	rel[loc src, loc name] uses                      = {},			                
	rel[loc from, loc to] containment                = {},		            
	list[Message] messages                           = [],				                  
	rel[str simpleName, loc qualifiedName] names     = {},		
	rel[loc definition, loc comments] documentation  = {},	
	rel[loc definition, Modifier modifier] modifiers = {}	
) = m3(loc id);
             

@synopsis{Extensible data-type to define language names and their versions}
@description{
Most ground truth facts about source code require analysis tooling that is specific to the language:
* parsers
* name analysis
* type analysis

However, there are language analysis methods that are _language agnostic_ such as counting lines of code.
For this we have the `generic()` language name.
}
public data Language(str version = "")
  = generic()
  ;

@synopsis{Generic function to compose the facts of a set of M3s into a single model.}
@description{
We iterate over all the facts stored in every model, and use set union or list concatenation
to collect the elements of all relations and lists. 
}
@pitfalls{
* If the quality of the qualified names in the original models is lacking, than this is the moment that different
declarations might be conflated with the same fully qualified name. All downstream analysis is broken then.
* This function does not compose the extended facts for specific programming languages yet.
* If extended M3 models use something other than sets, lists or relations, this composition function ignores them completely.
* Composed models can be huge in memory. Make sure to allocate enough heap for the JVM. Real world programs of real world product
can take gigabytes of memory, even when compressed and optimized as M3 models. 
}
@benefits{
* Composition satisfies the requirements for many downstream analyses:
   * Composition can be used to easily construct project-level models from file-level models, e.g. for open-source project analysis.
   * Composition can be used to simulate (dynamic) linkage between projects, e.g. for whole-program analysis.
   * Composition can be used to start simulating remote-procedure calls and shared memory, and other inter-programming language composition like _JNI_.
* Transitive closure on composed models leads to effective (and fast) reachability analysis.
* This function is rather memory-efficient by iterating over the already in-memory keyword parameter sets and lists, and
splicing the unions into an incremental transiently growing set or list via the comprehension. This avoids a lot of copying
and intermediate memory allocation which can be detrimental when doing large whole program analyses.
}
@memo
M3 composeM3(loc id, set[M3] models) {
	M3 comp = m3(id);
  map[M3, map[str,value]] fields = (m:getKeywordParameters(m) | m <- models);
  set[str] keys                  = {*domain(getKeywordParameters(m)) | m <- models };
  map[str, value] result         = ();

  // first we do only the sets, to use efficient set union/splicing
  for (k <- keys) {
    newSet = {*elems | M3 m <- models, set[value] elems := fields[m][k]};
    if (newSet != {}) { // don't set anything if the result is empty (it could be a list!)
      result[k] = newSet;
    }
  }

  // then we do only the lists, to use efficient list splicing
  for (k <- keys) {
    newList = [*elems | m <- models, list[value] elems := fields[m][k]];
    if (newList != []) { // don't set anything if the result is empty (it could be a set!)
      result[k] = newList;
    }
  }

  return setKeywordParameters(comp, result);
}

@synopsis{Generic function to apply a difference over the annotations of a list of M3s.}
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

@deprecated{Does not make much sense since containment usually contains only logical locations.}
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

@synopsis{Transform the containment relation to a recursive tree model}
@description{
This makes the containment relation into an abstract ((FileSystem)) for further analysis,
or visualization.
}
@benefits{
*  Transforming the containment relation to a tree model allows further analysis using operators
such as `visit` and descendant matching  (`/`) which is sometimes more convenient.
* The tree shape is better for visualization purposes.
}
set[FileSystem] containmentToFileSystem(M3 model) {
  @memo 
  set[FileSystem] relToFileSystem(rel[loc parent, loc child] r) {
    FileSystem rec(loc l, set[loc] args) = (args == {}) ? file(l) : directory(l, {rec(c, r[c]) | c <- args});
    return {rec(t, r[t]) | t <- top(r)};
  }

  return relToFileSystem(model.containment);
}

list[Message] checkM3(M3 model) {
  result  = [m | m <- model.messages, m is error];
  result += [error("undeclared element in containment", decl) | decl <- model.containment<to> - model.declarations<name>];
  result += [error("non-root element is not contained anywhere", decl) | decl <- model.containment<from> - model.declarations<name> - top(model.containment)];
  return result;
}

@synopsis{Specification to test the quality of M3 models that specific language front-ends produce.}
@description{
Based on the language agnostic relations in the M3 model, this function tries to validate the
_internal_ consistency of an M3 model. 

If an M3 instance is a `closedWorld` model, this means that there are no `uses` in the model that 
are not declared in the current model in `declarations`. A closed world model allows for more
stringent consistenct checks than a model that depends on external declarations. By selecting
`closedWorld=true` those additional checks are enabled, otherwise these are ignored or weakened
accordingly. It is advisable to provide at least one closed model per programming language front-end,
while testing against this spec.

By `covering` we mean that everything that is declared in the model is also used at least once.
This is a simple check for knowing if the test covers the language in some form.
}
@benefits{
* Front-end construction is tricky business. This test provides a sanity check before users start depending
on fawlty models. 
}
@pitfalls{
* In `closedWorld` many things can be strictly checked, but in an open world with dependencies outside
of the current model the validation is much weaker.
}
bool m3SpecificationTest(M3 m, bool closedWorld=false, bool covering=false) {
    decls     = m.declarations<name>;
    uses      = m.uses<name>;
    externals = uses - decls;

    // At least one language is represented
    assert m.languages != {};

    // What `closedWorld` means:
    assert closedWorld ==> externals == {};

    // We now merge the implicitDeclarations with the externals for the sake of brevity
    externals += m.implicitDeclarations;

    // Nothing that is contained here does not not have a declaration, except the outermost translationUnit
    assert decls - m.containment<to> - top(m.containment) == {};
   
    // Note that one declaration may occur twice ore more in the m.declarations relation.
    // this is because of _overloading_ of names in programming languages, or because
    // of dynamic name resolution (for languages where static name resolution is inherently inaccurate).
    overloadedDeclarations = { d | d <- decls, {_,_,*_} := m.declarations[d]};

    // Containment is 1-to-many, so a reverse lookup always produces a singleton.
    // An exception must be made for the overloaded declarations that might be contained in different elements at the same time.
    rel[loc inner, loc outer] reverseContainment = m.containment<to,from>;
    assert {inner | inner <- reverseContainment<inner>, {_} !:= reverseContainment[inner]} - overloadedDeclarations == {};

    // Everything in the containment relation has been declared somewhere
    assert carrier(m.containment) - decls - externals  == {};

    // Everything in the declarations relation is contained somewhere
    assert decls - carrier(m.containment) == {};

    // All uses point to actual declarations
    assert uses - decls - externals == {};

    // In this example, all declarations are used at least once
    if (covering) {
      assert decls - uses == {};
    }

    // M.declarations is one-to-one
    assert size(decls) == size(m.declarations);

    // documentation is on names, not sources
    assert m.documentation<definition> - decls == {};

    // modifiers is on names, not sources
    assert m.modifiers<definition> - decls == {};

     // types is on names, not sources
    assert m.types<name> - decls - externals == {};

    // names is for the declared artefacts
    assert m.names<qualifiedName> - decls - externals == {};

    // simple names are indeed simple, meant for UI purposes
    assert all(/^[$A-Za-z0-9_\-]+$/ <- m.names<simpleName>);

   return true;
}
