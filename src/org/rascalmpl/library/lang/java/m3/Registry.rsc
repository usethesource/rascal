module lang::java::m3::Registry

extend analysis::m3::Registry;

@resolver{java}
loc resolveJava(loc name) = resolveM3(name);