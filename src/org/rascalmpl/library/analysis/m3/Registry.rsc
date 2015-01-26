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

private map[loc project, M3 model] projects = ();

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
  projects[project] = model;
}

@doc{
Synopsis: remove an M3 model for a project from the registry

Description:

This is necessary to solve memory leaks. When you are sure not to reference an M3 model anymore, the model
can be removed from the registry.
}
void unregisterProject(loc project) {
  projects -= (project:m3(project));
}

M3 getModelContaining(loc entity) {
  for (proj <- projects) {
    if (<entity, _> <- projects[proj]@declarations) {
      return projects[proj];
    }
  }
  throw "No model found containing the declaration <entity>";
}

@doc{
Synopsis: map a qualified name [Location] to a physical file store [Location]

Description:

[resolveM3] uses the registry to find out from an M3 model where in the source code a qualified name is declared.

Note that specific languages should declare they own resolvers, delegating immediately to [resolveM3].   
}
loc resolveM3(loc name) {
  str project = name.authority;
  if (isEmpty(project)) {
    for (proj <- projects) {
      if (<name, src> <- projects[proj]@declarations) {
        return src;
      }
    }
    // lets try again with our parent, maybe someone has been messing with the locations
    name = name.parent;
    for (proj <- projects) {
      if (<name, src> <- projects[proj]@declarations) {
        return src;
      }
    }
  } else {
    if (<name, src> <- projects[name[path=""]]@declarations) { 
      return src;
    }
    // lets try again with our parent, maybe someone has been messing with the locations
    name = name.parent;
    if (<name, src> <- projects[name[path=""]]@declarations) { 
      return src;
    }
  }
  throw "<name> not resolved";
}