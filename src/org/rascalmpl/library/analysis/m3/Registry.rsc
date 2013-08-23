module analysis::m3::Registry

import analysis::m3::Core;

private map[str project, M3 model] projects = ();

void registerProject(str project, M3 model) {
  projects[project] = model;
}

void unregisterProject(str project) {
  projects -= (project:m3(|dummy:///|,(),{}));
}

@resolver{m3}
loc resolveM3(loc name) {
  if (project <- projects, <name, src> <- projects[project]@declarations) 
     return src;
  throw "<name> not resolved";
}