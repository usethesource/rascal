@doc{
Synopsis: in memory database for [analysis/m3/Core] models for resolving hyperlinks

Description:

The functions in this file are used to register m3 models in a global in-memory database. When a source location is clicked this database is used used to resolve logical source locations, such as `|java+class:///java/lang/Object|` to physical source locations such as `|file:///usr/lib/blabla.java|`.
}
module analysis::m3::Registry

import analysis::m3::Core;
import String;
import IO;
import Message;

@doc{
Synopsis: register an M3 model for a certain project name.

Description:

The effect of registering a project is that the m3 URI resolver knows how to find the physical source location
for qualified names.

Note that [registerProject] will be called usually as a side-effect of a function that extracts a model for
a specific language.  

Benefits:

* this enables qualified names as [Location]s to be hyperlinks in the IDE

Pitfalls:

* the registry is a global store that will retain links to M3 models even when they are not in use anymore. The 
programmer should take care to call [unregisterProject] to prevent memory leakage.
}
void registerProject(loc project, M3 model) {
    rel[str scheme, loc name, loc src] perScheme 
      = {<name.scheme, name, src> | <name, src> <- model@declarations};
    
    for (str scheme <- perScheme<scheme>)
       registerLocations(scheme, project.authority, (name : src | <name, src> <- perScheme[scheme]));
}

