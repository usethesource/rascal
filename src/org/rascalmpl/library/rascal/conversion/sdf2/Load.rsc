module rascal::conversion::sdf2::Load

import languages::sdf2::syntax::Sdf2;
import IO;
import Exception;
import String;

public SDF loadSDF2Module(str name, list[loc] path) {
  set[str] names = {};
  set[Module] modules = {};
  set[str] newnames = {name};

  solve (names) for (n <- newnames) {
    newnames = {};
    file = find(replaceAll(n,"::","/") + ".sdf", path);
    mod = parse(#Module, file);
    modules += mod;
    newnames += getImports(mod);
    names += newnames;
  }

  mods = (Module*) ``;
  for (Module m <- modules) {
    mods = (Module*) `<[Module] m> <[Module*] mods>`;
  }
  return (SDF) `definition <[Module*] mods>`;
}

public set[str] getImports(Module mod) {
  return { "<name>" | /Import i := mod,  /ModuleId name := i};
}
