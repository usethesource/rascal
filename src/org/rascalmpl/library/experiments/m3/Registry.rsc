module experiments::m3::Registry

import experiments::m3::Core;

private map[str project, M3 model] projects = ();

void registerProject(str project, M3 model) {
  projects[project] = model;
}

void unregisterProject(str project) {
  projects -= (project:m3(|dummy:///|,(),{}));
}

@resolver{m3}
loc resolveM3(loc name) {
  str project = name.authority;
  return project in projects ? projects[project].source[name] : name;
}