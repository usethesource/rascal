module analysis::m3::Registry

import analysis::m3::Core;

private map[str project, M3 model] projects = ();

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
void registerProject(str project, M3 model) {
  projects[project] = model;
}

@doc{
Synopsis: remove an M3 model for a project from the registry

Description:

This is necessary to solve memory leaks. When you are sure not to reference an M3 model anymore, the model
can be removed from the registry.
}
void unregisterProject(str project) {
  projects -= (project:m3(project));
}

@doc{
Synopsis: map a qualified name [Location] to a physical file store [Location]

Description:

[resolveM3] uses the registry to find out from an M3 model where in the source code a qualified name is declared.

Note that specific languages should declare they own resolvers, delegating immediately to [resolveM3].   
}
@resolver{m3}
loc resolveM3(loc name) {
  str project = name.authority;
  if (<name, src> <- projects[project]@declarations) 
     return src;
  throw "<name> not resolved";
}