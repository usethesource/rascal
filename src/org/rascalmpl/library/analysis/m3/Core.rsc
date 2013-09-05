module analysis::m3::Core

import Message;
import analysis::m3::TypeSymbol;
 
data Modifiers;
data M3 = m3(str projectName);
             
anno rel[loc name, loc src] M3@declarations; // maps declarations to where they are declared. contains any kind of data or type or code declaration (classes, fields, methods, variables, etc. etc.)
anno rel[loc src, loc name] M3@uses;         // maps source locations of usages to the respective declarations
anno rel[loc from, loc to] M3@containment;   // what is logically contained in what else (not necessarily physically, but usually also)
anno list[Message messages] M3@messages;     // error messages and warnings produced while constructing a single m3 model
anno rel[str simpleName, loc qualifiedName] M3@names;      // convenience mapping from logical names to end-user readable (GUI) names, and vice versa
anno rel[loc definition, loc comments] M3@documentation;   // comments and javadoc attached to declared things
anno rel[loc definition, Modifiers modifier] M3@modifiers; // modifiers associated with declared things
anno rel[loc name, TypeSymbol typ] M3@types;                     // assigns types to resolved source code artifacts