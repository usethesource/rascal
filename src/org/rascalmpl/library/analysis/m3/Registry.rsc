
@synopsis{in memory database for [analysis/m3/Core] models for resolving hyperlinks}
@description{
The functions in this file are used to register m3 models in a global in-memory database. When a source location is clicked this database is used used to resolve logical source locations, such as `|java+class:///java/lang/Object|` to physical source locations such as `|file:///usr/lib/blabla.java|`.
}
module analysis::m3::Registry

import analysis::m3::Core;
import IO;


@synopsis{Register an M3 model for a certain project name.}
@description{
The effect of registering a project is that the m3 URI resolver knows how to find the physical source location
for qualified names.

Note that ((registerProject)) will be called usually as a side-effect of a function that extracts a model for
a specific language.
}
@benefits{
*  this enables qualified names as locations to be hyperlinks in the IDE
}
@pitfalls{
*  the registry is a global store that will retain links to M3 models even when they are not in use anymore. The 
programmer should take care to call ((unregisterProject)) to prevent memory leakage.
}
void registerProject(loc project, M3 model) {
    rel[str scheme, loc name, loc src] perScheme 
      = {<name.scheme, name, src> | <name, src> <- model.declarations};
    
    for (str scheme <- perScheme<scheme>) {
       registerLocations(scheme, project.authority, (name : src | <name, src> <- perScheme[scheme]));
    }
}


@synopsis{unregister an M3 model for a certain project name.}
@description{
The effect of unregistering a project is that all references will be
removed from the registry, clearing memory.
}
@benefits{
*  this cleans up the memory used by the registry
}
@pitfalls{
*  if a different model is used for unregistering than for registering,
   there could be a memory leak of remaining schemes and their respective locations.
}
void unregisterProject(loc project, M3 model) {
    for (loc name <- model.declarations<name>) {
           unregisterLocations(name.scheme, project.authority);
    }
}


@synopsis{unregister an M3 model for a set of given schemes}
@description{
The effect of unregistering a project is that all references will be
removed from the registry, clearing memory.
}
@benefits{
* This cleans up the memory used by the registry, and by giving all possible
   schemes for a certain language the chance is high there are not dangling
   entries afterwards.
}
@pitfalls{
*  If more schemes were registered than are unregistered here, there is a
   memory leak.
} 
void unregisterProjectSchemes(loc project, set[str] schemes) {
    for (str scheme <- schemes) {
           unregisterLocations(scheme, project.authority);
    }
}
