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

Index createConceptIndex(PathConfig pcfg) {
    ind = createConceptIndex(pcfg.srcs);

    // store index for later usage by depending documentation projects
    writeBinaryValueFile(pcfg.bin + "index.value", ind);

    // read indices from projects we depend on, if present
    ind += {*readBinaryValueFile(#rel[str,str], inx) | l <- pcfg.libs, inx := l + "doc" + "index.value", exists(inx)};

    return ind;
}

rel[str, str] createConceptIndex(list[loc] srcs) 
  = {*createConceptIndex(src) | src <- srcs};

@synopsis{creates a lookup table for concepts nested in a folder}
rel[str, str] createConceptIndex(loc src)
  = // first we collect index entries for concept names, each file is one concept which
    // can be linked to in 6 different ways ranging from very short but likely inaccurate to
    // rather long, but guaranteed to be exact:
    { 
      // `((StrictSuperSetSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet``
      <cf.file, fr>,

      // `((Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet``
      *{<"<f.parent.parent.file>-<cf.file>", fr> | f.parent.path != "/", f.parent.file == cf.file, f.parent != src},

      // `((Expressions-Values-Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet``
      *{<"<replaceAll(fr[1..][findFirst(fr[1..], "/")+1..], "/", "-")>", fr>},

      // `((Rascal:StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet`
      <"<capitalize(src.file)>:<capitalize(cf.file)>", fr>,

      // `((Rascal:Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet``
      *{<"<capitalize(src.file)>:<capitalize(f.parent.parent.file)>-<capitalize(cf.file)>", "/<src.file><fr[1..][findFirst(fr[1..], "/")..]>"> | f.parent.path != "/", f.parent.file == cf.file},     

      // `((Rascal:Expressions-Values-Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet``
      <"<capitalize(src.file)>:<replaceAll(fr[1..][findFirst(fr[1..], "/")+1..], "/", "-")>", fr>

    | loc f <- find(src, isConceptFile), f.file != "index.md", fr := localLink(src, f), cf := f[extension=""]
    }
  + // Now follow the index entries for image files:
    { <"<f.parent.file>-<f.file>", "/assets/<capitalize(src.file)><relativize(src, f).path>">,
      <f.file, "/assets/<capitalize(src.file)><relativize(src, f).path>">,
      <"<capitalize(src.file)>:<f.file>", "/assets/<capitalize(src.file)><relativize(src, f).path>">
    |  loc f <- find(src, isImageFile)
    }
  + { // these are links to packages/folders/directories via module path prefixes, like `analysis::m3`
     <"<replaceAll(relativize(src, f).path[1..], "/", "::")>", fr>,
     <"<capitalize(src.file)>:<replaceAll(relativize(src, f).path[1..], "/", "::")>", fr>,
     <"<capitalize(src.file)>:package:<replaceAll(relativize(src, f).path[1..], "/", "::")>", fr>
    | loc f <- find(src, isDirectory), fr := localDirLink(src, f)
  }
  + // Here come the index entries for Rascal modules and declarations:
    {  // `((getDefaultPathConfig))` -> `#util-Reflective-getDefaultPathConfig`
      *{<"<item.kind>:<item.name>","<moduleFragment(item.moduleName)>/#<item.name>">, <item.name, "<moduleFragment(item.moduleName)>.md#<item.moduleName>-<item.name>" > | item.name?},
     
      // `((Library:getDefaultPathConfig))` -> `/Library.md#util-Reflective-getDefaultPathConfig`
      *{<"<capitalize(src.file)>:<item.name>", "/<capitalize(src.file)>/<moduleFragment(item.moduleName)>.md#<item.moduleName>-<item.name>" >,
         <"<capitalize(src.file)>:<item.kind>:<item.name>", "/<capitalize(src.file)>/<moduleFragment(item.moduleName)>.md#<item.moduleName>-<item.name>" > | item.name?},

      // `((util::Reflective::getDefaultPathConfig))` -> `#util-Reflective-getDefaultPathConfig`
      *{<"<item.moduleName><sep><item.name>", "/<capitalize(src.file)>/<moduleFragment(item.moduleName)>.md#<item.moduleName>-<item.name>" >,
        <"<item.kind>:<item.moduleName><sep><item.name>", "/<capitalize(src.file)>/<moduleFragment(item.moduleName)>.md#<item.moduleName>-<item.name>" > | item.name?, sep <- {"::", "/", "-"}},

      // ((Library:util::Reflective::getDefaultPathConfig))` -> `#util-Reflective-getDefaultPathConfig`
      *{<"<capitalize(src.file)>:<item.moduleName><sep><item.name>", "/<capitalize(src.file)>.md/<moduleFragment(item.moduleName)>.md#<item.moduleName>-<item.name>" >,
         <"<capitalize(src.file)>:<item.kind>:<item.moduleName><sep><item.name>", "/<capitalize(src.file)>.md/<moduleFragment(item.moduleName)>.md#<item.moduleName>-<item.name>" > | item.name?, sep <- {"::", "/", "-"}},

      // ((Set)) -> `#Set`
      *{<item.moduleName, "/<capitalize(src.file)>/<moduleFragment(item.moduleName)>" >, <"module:<item.moduleName>", "/<capitalize(src.file)>/<moduleFragment(item.moduleName)>" > | item is moduleInfo},

      // `((Library:Set))` -> `/Library.md#Set`
      *{<"<capitalize(src.file)>:<item.moduleName>", "/<capitalize(src.file)>.md/<moduleFragment(item.moduleName)>" >,
         <"<capitalize(src.file)>:module:<item.moduleName>", "/<capitalize(src.file)>.md/<moduleFragment(item.moduleName)>" > | item is moduleInfo}

      | loc f <- find(src, "rsc"), list[DeclarationInfo] inf := extractInfo(f), item <- inf
    }
    ;

private bool isConceptFile(loc f) = f.extension in {"md"};
private bool isImageFile(loc f) = f.extension in {"png", "jpg", "svg", "jpeg"};

