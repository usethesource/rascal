module lang::rascal::tutor::Indexer

import util::Reflective;
import ValueIO;
import String;
import util::FileSystem;
import IO;
import Location;

import lang::rascal::tutor::apidoc::DeclarationInfo;
import lang::rascal::tutor::apidoc::ExtractInfo;
import lang::rascal::tutor::Names;

public alias Index = rel[str reference, str url];

Index readConceptIndex(PathConfig pcfg) {
  return readBinaryValueFile(#Index, pcfg.bin + "index.value");
}

Index createConceptIndex(PathConfig pcfg) {
    ind = createConceptIndex(pcfg.srcs);

    // store index for later usage by depending documentation projects
    writeBinaryValueFile(pcfg.bin + "index.value", ind);

    // read indices from projects we depend on, if present
    ind += {*readBinaryValueFile(#rel[str,str], inx) | l <- pcfg.libs, inx := l + "docs" + "index.value", exists(inx)};

    return ind;
}

rel[str, str] createConceptIndex(list[loc] srcs) 
  = {*createConceptIndex(src) | src <- srcs};

@synopsis{creates a lookup table for concepts nested in a folder}
rel[str, str] createConceptIndex(loc src)
  = // first we collect index entries for concept names, each file is one concept which
    // can be linked to in many different ways ranging from very short (handy but inexact) to very long (guaranteed to be exact.)

    // First we handle the root concept
    {
      <capitalize(src.file), "/<capitalize(src.file)>/index.md">
    }
    +
    // Then we handle the cases where the concept name is the same as the folder it is nested in:
    {
      // `((StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <cf.file                            , fr>,

      // `((Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<f.parent.parent.file>-<cf.file>", fr>,

      // `((Expressions-Values-Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <replaceAll(capitalize(relativize(src, f.parent).path)[1..], "/", "-"), fr>,

      // `((Rascal:StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<capitalize(src.file)>:<capitalize(cf.file)>", fr>,

      // `((Rascal:Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<capitalize(src.file)>:<f.parent.parent.file>-<cf.file>", fr>,

      // `((Rascal:Expressions-Values-Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<capitalize(src.file)>:<replaceAll(capitalize(relativize(src, f.parent).path)[1..], "/", "-")>", fr>

    | loc f <- find(src, isConceptFile)
      , f.parent?
      , f.parent.path != "/"
      , f.parent != src
      , f.parent.file == f[extension=""].file
      , fr := "/<capitalize(src.file)>/<fragment(src, f)>"
      , cf := f[extension=""]
    }
    +
    // Then we handle the extra markdown files, that don't keep to the Concept/Concept.md rule
    {
      // `((StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <cf.file                            , fr>,

      // `((Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<f.parent.file>-<cf.file>", fr>,

      // `((Expressions-Values-Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <replaceAll(capitalize(relativize(src, cf).path)[1..], "/", "-"), fr>,

      // `((Rascal:StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<capitalize(src.file)>:<capitalize(cf.file)>", fr>,

      // `((Rascal:Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<capitalize(src.file)>:<f.parent.file>-<cf.file>", fr>,

      // `((Rascal:Expressions-Values-Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<capitalize(src.file)>:<replaceAll(capitalize(relativize(src, cf).path)[1..], "/", "-")>", fr>

    | loc f <- find(src, isConceptFile)
      , f.parent?
      , f.parent.path != "/"
      , f.parent != src
      , f.parent.file != f[extension=""].file
      , fr := "/<capitalize(src.file)>/<fragment(src, f)>"
      , cf := f[extension=""]
    }
    +
    // Then we handle all folders. We assume all folders have an index.md (generated or manually provided) 
    // This may generate some links exactly the same as above, and add some new ones.
    + 
    {
      // `((StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <f.file                            , fr>,

      // `((Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<f.parent.file>-<f.file>", fr>,

      // `((Expressions-Values-Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <replaceAll(capitalize(relativize(src, f).path)[1..], "/", "-"), fr>,

      // `((Rascal:StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<capitalize(src.file)>:<capitalize(f.file)>", fr>,

      // `((Rascal:Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<capitalize(src.file)>:<f.parent.file>-<f.file>", fr>,

      // `((Rascal:Expressions-Values-Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<capitalize(src.file)>:<replaceAll(capitalize(relativize(src, f).path)[1..], "/", "-")>", fr>
    | loc f <- find(src, isDirectory)
    , fr := "/<capitalize(src.file)>/<fragment(src, f)>"
    , f != src
    }
    +

  + // Now follow the index entries for image files:
    { <"<f.parent.file>-<f.file>", "/assets/<capitalize(src.file)><relativize(src, f).path>">,
      <f.file, "/assets/<capitalize(src.file)><relativize(src, f).path>">,
      <"<capitalize(src.file)>:<f.file>", "/assets/<capitalize(src.file)><relativize(src, f).path>">
    |  loc f <- find(src, isImageFile)
    }
  + { // these are links to packages/folders/directories via module path prefixes, like `analysis::m3`
     <"<replaceAll(relativize(src, f).path[1..], "/", "::")>", fr>,
     <"<capitalize(src.file)>:<replaceAll(relativize(src, f).path[1..], "/", "::")>", fr>,
     <"<capitalize(src.file)>:<replaceAll(relativize(src, f).path[1..], "/", "-")>", fr>,
     <"<capitalize(src.file)>:<capitalize(replaceAll(relativize(src, f).path[1..], "/", "-"))>", fr>,
     <"<capitalize(src.file)>:package:<replaceAll(relativize(src, f).path[1..], "/", "::")>", fr>,
     <"<capitalize(src.file)>:<capitalize(replaceAll(relativize(src, f).path[1..], "/", "::"))>", fr>
    | loc f <- find(src, isDirectory)
      , /\/internal\// !:= f.path
      , f != src
      , fr := "/<capitalize(src.file)>/<fragment(src, f)>"
  }
  + // Finally, the index entries for Rascal modules and declarations, as extracted from the source code:
    {  // `((getDefaultPathConfig))` -> `Libary/util/Reflective#getDefaultPathConfig`
      *{<"<item.kind>:<item.name>","/<capitalize(src.file)>/<modulePath(item.moduleName)>.md<moduleFragment(item.moduleName)>-<item.name>">, 
        <item.name, "/<capitalize(src.file)>/<modulePath(item.moduleName)>.md<moduleFragment(item.moduleName)>-<item.name>" > | item.name?},
     
      // `((Library:getDefaultPathConfig))` -> `/Library/util/Reflective#getDefaultPathConfig`
      *{<"<capitalize(src.file)>:<item.name>", "/<capitalize(src.file)>/<modulePath(item.moduleName)>.md<moduleFragment(item.moduleName)>-<item.name>" >,
        <"<capitalize(src.file)>:<item.kind>:<item.name>", "/<capitalize(src.file)>/<modulePath(item.moduleName)>.md<moduleFragment(item.moduleName)>-<item.name>" > | item.name?},

      // `((util::Reflective::getDefaultPathConfig))` -> `/Library/util/Reflective#getDefaultPathConfig`
      *{<"<item.moduleName><sep><item.name>", "/<capitalize(src.file)>/<modulePath(item.moduleName)>.md<moduleFragment(item.moduleName)>-<item.name>" >,
        <"<item.kind>:<item.moduleName><sep><item.name>", "/<capitalize(src.file)>/<modulePath(item.moduleName)>.md<moduleFragment(item.moduleName)>-<item.name>" > | item.name?, sep <- {"::", "/", "-"}},

      // ((Library:util::Reflective::getDefaultPathConfig))` -> `/Library/util/Reflective#getDefaultPathConfig`
      *{<"<capitalize(src.file)>:<item.moduleName><sep><item.name>", "/<capitalize(src.file)>/<modulePath(item.moduleName)>.md<moduleFragment(item.moduleName)>-<item.name>" >,
         <"<capitalize(src.file)>:<item.kind>:<item.moduleName><sep><item.name>", "/<capitalize(src.file)>/<modulePath(item.moduleName)>.md<moduleFragment(item.moduleName)>-<item.name>" > | item.name?, sep <- {"::", "/", "-"}},

      // ((Set)) -> `/Library/Set`
      *{<item.moduleName, "/<capitalize(src.file)>/<modulePath(item.moduleName)>.md" >, <"module:<item.moduleName>", "/<capitalize(src.file)>/<modulePath(item.moduleName)>.md" > | item is moduleInfo},

      // `((Library:Set))` -> `/Library/Set`
      *{<"<capitalize(src.file)>:<item.moduleName>", "/<capitalize(src.file)>/<modulePath(item.moduleName)>.md" >,
         <"<capitalize(src.file)>:module:<item.moduleName>", "/<capitalize(src.file)>/<modulePath(item.moduleName)>.md" > | item is moduleInfo}

      | loc f <- find(src, "rsc"), list[DeclarationInfo] inf := safeExtract(f), item <- inf
    }
    ;

private bool isConceptFile(loc f) = f.extension in {"md"};
private bool isImageFile(loc f) = f.extension in {"png", "jpg", "svg", "jpeg"};

@synopsis{ignores extracting errors because they will be found later}
private list[DeclarationInfo] safeExtract(loc f) {
  try {
    return extractInfo(f);
  }
  catch Java(_,_): return [];
  catch ParseError(_): return [];
}