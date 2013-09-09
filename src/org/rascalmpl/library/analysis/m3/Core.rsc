@doc{
Synopsis: M3 common source code model represent facts extracted from source code for use in downstream metrics or other analyses.

Description:

The M3 core defines basic concepts such as:

* qualified names: we use [Location]s to model qualified names for each programming language
* containment: which artifacts are contained in which other artifacts
* declarations: where artifacts are defined
* uses: where declared artifacts are used
* types: which artifacts has which types

From this [Core], M3 is supposed to be extended with features specific for a programming language. See for example [lang::java::m3::JavaM3].

Benefits:

* Qualified names in the shape of [Location]s are a uniform and generic way of identifying source code artifacts, that can be extended acros languages, projects, and versions.
* M3 helps standardizing the shape of facts we extract from source code for all different languages, limiting the element of surprise.
* When we use M3 for many languages, common IDE features are made reusable (such as clicking from an extracted fact to the code that generated it).
* Some downstream analyses may be reusable between different languages if they all map to M3.

Pitfalls:

* Even though different languages may map to the same M3 model, this does not mean that the semantics is the same. Downstream
metrics or other analysis tools should still take semantic differences between programming languages into account.
}
module analysis::m3::Core

import Message;
import analysis::m3::TypeSymbol;
 
data Modifiers;
data M3 = m3(str projectName);
             
anno rel[loc name, loc src] M3@declarations; // maps declarations to where they are declared. contains any kind of data or type or code declaration (classes, fields, methods, variables, etc. etc.)
anno rel[loc name, TypeSymbol typ] M3@types; // assigns types to declared source code artifacts
anno rel[loc src, loc name] M3@uses;         // maps source locations of usages to the respective declarations
anno rel[loc from, loc to] M3@containment;   // what is logically contained in what else (not necessarily physically, but usually also)
anno list[Message messages] M3@messages;     // error messages and warnings produced while constructing a single m3 model
anno rel[str simpleName, loc qualifiedName] M3@names;      // convenience mapping from logical names to end-user readable (GUI) names, and vice versa
anno rel[loc definition, loc comments] M3@documentation;   // comments and javadoc attached to declared things
anno rel[loc definition, Modifiers modifier] M3@modifiers; // modifiers associated with declared things
