module lang::rascal::tutor::Indexer

import util::Reflective;
import ValueIO;
import String;
import util::FileSystem;
import IO;
import ValueIO;
import Location;

import lang::rascal::tutor::apidoc::DeclarationInfo;
import lang::rascal::tutor::apidoc::ExtractInfo;
import lang::rascal::tutor::Names;
// import Relation;
import Location;
import Set;

public alias Index = rel[str reference, str url];

Index readConceptIndex(PathConfig pcfg) {
  return readBinaryValueFile(#Index, pcfg.bin + "index.value");
}

Index createConceptIndex(PathConfig pcfg) {
    targetFile = pcfg.bin + "index.value";

    // in incremental mode we will have skipped many files. This
    // adds the old index to the newly created ones
    ind = exists(targetFile) ? readConceptIndex(pcfg) : {};

    // now we add the new index items on top of the old ones    
    ind += createConceptIndex(pcfg.srcs, exists(targetFile) ? lastModified(targetFile) : $1970-01-01T00:00:00.000+00:00$, pcfg.isPackageCourse, pcfg.packageName);

    // store index for later usage by depending documentation projects,
    // and for future runs of the compiler on the current project
    writeBinaryValueFile(targetFile, ind);

    // read indices from projects we depend on, if present
    ind += {*readBinaryValueFile(#rel[str,str], inx) | l <- pcfg.libs, inx := l + "docs" + "index.value", exists(inx)};

    return ind;
}

rel[str, str] createConceptIndex(list[loc] srcs, datetime lastModified, bool isPackageCourse, str packageName) 
  = {*createConceptIndex(src, lastModified, isPackageCourse, packageName) | src <- srcs, bprintln("Indexing <src>")};

@synopsis{creates a lookup table for concepts nested in a folder}
rel[str, str] createConceptIndex(loc src, datetime lastModified, bool isPackageCourse, str packageName)
  = // first we collect index entries for concept names, each file is one concept which
    // can be linked to in many different ways ranging from very short (handy but inexact) to very long (guaranteed to be exact.)

    // First we handle the root concept
    {
      <package(src.file), "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>API<} else {><package(src.file)><}>/index.md">,
      <"course:<package(src.file)>", "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>API<} else {><package(src.file)><}>/index.md">
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

    | loc f <- find(src, isFreshConceptFile(lastModified))
      , f.parent?
      , f.parent.path != "/"
      , f.parent != src
      , f.parent.file == f[extension=""].file
      , fr := "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>API<} else {><capitalize(src.file)><}>/<fragment(src, f)>"
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

    | loc f <- find(src, isFreshConceptFile(lastModified))
      , f.parent?
      , f.parent.path != "/"
      , f.parent != src
      , f.parent.file != f[extension=""].file
      , fr := "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>API<} else {><capitalize(src.file)><}>/<fragment(src, f)>"
      , cf := f[extension=""]
    }
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
    , fr := "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>API<} else {><capitalize(src.file)><}>/<fragment(src, f)>"
    , f != src
    }
    +
    // Now follow the index entries for image files:
    { <"<f.parent.file>-<f.file>",        fr>,
      <f.file,                            fr>,
      <"<capitalize(src.file)>:<f.file>", fr>
    |  loc f <- find(src, isImageFile),
       fr := "/assets/<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>API<} else {><capitalize(src.file)><}><relativize(src, f).path>"
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
      , fr := "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>API<} else {><capitalize(src.file)><}>/<fragment(src, f)>"
  }
  + // Finally, the index entries for Rascal modules and declarations, as extracted from the source code:
    {  // `((getDefaultPathConfig))` -> `Libary/util/Reflective#getDefaultPathConfig`
      *{<"<item.kind>:<item.name>", fr>, 
        <item.name, fr > | item.name?},
     
      // `((Library:getDefaultPathConfig))` -> `/Library/util/Reflective#getDefaultPathConfig`
      *{<"<capitalize(src.file)>:<item.name>", fr >,
        <"<capitalize(src.file)>:<item.kind>:<item.name>", fr > | item.name?, !(item is moduleInfo)},

      // `((util::Reflective::getDefaultPathConfig))` -> `/Library/util/Reflective#getDefaultPathConfig`
      *{<"<item.moduleName><sep><item.name>", fr >,
        <"<item.kind>:<item.moduleName><sep><item.name>", fr > | item.name?, !(item is moduleInfo), sep <- {"::", "/", "-"}},

      // ((Library:util::Reflective::getDefaultPathConfig))` -> `/Library/util/Reflective#getDefaultPathConfig`
      *{<"<capitalize(src.file)>:<item.moduleName><sep><item.name>", fr >,
         <"<capitalize(src.file)>:<item.kind>:<item.moduleName><sep><item.name>", fr > | item.name?, !(item is moduleInfo), sep <- {"::", "/", "-"}},

      // ((Set)) -> `/Library/Set`
      *{<item.moduleName, "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>/API<} else {>/<capitalize(src.file)><}>/<modulePath(item.moduleName)>.md" >, 
        <"module:<item.moduleName>", "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>/API<} else {>/<capitalize(src.file)><}>/<modulePath(item.moduleName)>.md" > 
        | item is moduleInfo
      },

      // `((Library:Set))` -> `/Library/Set`
      *{<"<capitalize(src.file)>:<item.moduleName>", "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>/API<} else {>/<capitalize(src.file)><}>/<modulePath(item.moduleName)>.md" >,
         <"<capitalize(src.file)>:module:<item.moduleName>", "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>/API<} else {>/<capitalize(src.file)><}>/<modulePath(item.moduleName)>.md" > | item is moduleInfo}

      | loc f <- find(src, isFreshRascalFile(lastModified)), list[DeclarationInfo] inf := safeExtract(f), item <- inf,
        fr := "/<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>API<} else {><capitalize(src.file)><}>/<modulePath(item.moduleName)>.md<moduleFragment(item.moduleName)>-<item.name>"
    }
    ;

private bool isConceptFile(loc f) = f.extension in {"md"};

private bool(loc) isFreshConceptFile(datetime lM) 
  = bool (loc f) { return isConceptFile(f);
      return isConceptFile(f) && lastModified(f) > lM;
    };

private bool(loc) isFreshRascalFile(datetime lM)
  = bool (loc f) {
      return f.extension in {"rsc"} && lastModified(f) > lM;
    };

private bool isImageFile(loc f) = f.extension in {"png", "jpg", "svg", "jpeg"};

@synopsis{ignores extracting errors because they will be found later}
private list[DeclarationInfo] safeExtract(loc f) {
  try {
    return extractInfo(f);
  }
  catch Java(_,_): return [];
  catch ParseError(_): return [];
}