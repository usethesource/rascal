module lang::rascal::tutor::Indexer

import util::Reflective;
import ValueIO;
import String;
import util::FileSystem;
import util::Monitor;
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
    ind += createConceptIndex(pcfg.srcs, exists(targetFile) ? lastModified(targetFile) : $1970-01-01T00:00:00.000+00:00$, pcfg.isPackageCourse, pcfg.packageName, pcfg.packageArtifactId);

    // store index for later usage by depending documentation projects,
    // and for future runs of the compiler on the current project
    writeBinaryValueFile(targetFile, ind);

    // read indices from projects we depend on, if present
    ind += {*readBinaryValueFile(#rel[str,str], inx) | l <- pcfg.libs, inx := l + "docs" + "index.value", exists(inx)};

    return ind;
}

rel[str, str] createConceptIndex(list[loc] srcs, datetime lastModified, bool isPackageCourse, str packageName, str packageId) 
  = {*createConceptIndex(src, lastModified, isPackageCourse, packageName, packageId) | src <- srcs};

@synopsis{creates a lookup table for concepts nested in a folder}
rel[str, str] createConceptIndex(loc src, datetime lastModified, bool isPackageCourse, str packageName, str packageId) {
  bool step(str label, loc file) {
    jobStep(label, "<file.file>");
    return true;
  }

  void \start(str label, int work) {
    if (work > 0) {
      jobStart(label, totalWork=work);
    }
  }

  // first we collect index entries for concept names, each file is one concept which
  // can be linked to in many different ways ranging from very short (handy but inexact) to very long (guaranteed to be exact.)
  conceptFiles = find(src, isFreshConceptFile(lastModified));
  \start("Indexing concepts", 2*size(conceptFiles));

  imageFiles = find(src, isImageFile);
  \start("Indexing images", size(imageFiles));

  directoryIndexes = find(src, isDirectory);
  \start("Indexing directories", 2*size(directoryIndexes));

  rascalFiles = find(src, isFreshRascalFile(lastModified));
  \start("Indexing modules", size(rascalFiles));

  

    // First we handle the root concept
  result = {
      <package(packageName),     "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/index.md">,
      <packageId,                "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/index.md">,
      <rootName(src, isPackageCourse),            "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<rootName(src, isPackageCourse)>/index.md">,
      <"course:<rootName(src, isPackageCourse)>", "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<rootName(src, isPackageCourse)>/index.md">
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
      <"<rootName(src, isPackageCourse)>:<capitalize(cf.file)>", fr>,

      // `((Rascal:Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<rootName(src, isPackageCourse)>:<f.parent.parent.file>-<cf.file>", fr>,

      // `((Rascal:Expressions-Values-Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<rootName(src, isPackageCourse)>:<replaceAll(capitalize(relativize(src, f.parent).path)[1..], "/", "-")>", fr>

    | loc f <- conceptFiles
      , step("Indexing concepts", f)
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
      <"<rootName(src, isPackageCourse)>:<capitalize(cf.file)>", fr>,

      // `((Rascal:Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<rootName(src, isPackageCourse)>:<f.parent.file>-<cf.file>", fr>,

      // `((Rascal:Expressions-Values-Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<rootName(src, isPackageCourse)>:<replaceAll(capitalize(relativize(src, cf).path)[1..], "/", "-")>", fr>

    | loc f <- conceptFiles
      , step("Indexing concepts", f)
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
      <"<rootName(src, isPackageCourse)>:<capitalize(f.file)>", fr>,

      // `((Rascal:Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<rootName(src, isPackageCourse)>:<f.parent.file>-<f.file>", fr>,

      // `((Rascal:Expressions-Values-Set-StrictSuperSet)) -> /Rascal/Expressions/Values/Set/StrictSuperSet/index.md`
      <"<rootName(src, isPackageCourse)>:<replaceAll(capitalize(relativize(src, f).path)[1..], "/", "-")>", fr>
    | loc f <- directoryIndexes
    , step("Indexing directories", f)
    , fr := "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>API<} else {><capitalize(src.file)><}>/<fragment(src, f)>"
    , f != src
    }
    +
    // Now follow the index entries for image files:
    { <"<f.parent.file>-<f.file>",        fr>,
      <f.file,                            fr>,
      <"<capitalize(src.file)>:<f.file>", fr>
    |  loc f <- imageFiles, step("Indexing images", f),
       fr := "/assets/<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>API<} else {><capitalize(src.file)><}><relativize(src, f).path>"
    }
  + { // these are links to packages/folders/directories via module path prefixes, like `analysis::m3`
     <"<replaceAll(relativize(src, f).path[1..], "/", "::")>", fr>,
     <"<rootName(src, isPackageCourse)>:<replaceAll(relativize(src, f).path[1..], "/", "::")>", fr>,
     <"<rootName(src, isPackageCourse)>:<replaceAll(relativize(src, f).path[1..], "/", "-")>", fr>,
     <"<rootName(src, isPackageCourse)>:<capitalize(replaceAll(relativize(src, f).path[1..], "/", "-"))>", fr>,
     <"<rootName(src, isPackageCourse)>:package:<replaceAll(relativize(src, f).path[1..], "/", "::")>", fr>,
     <"<rootName(src, isPackageCourse)>:<capitalize(replaceAll(relativize(src, f).path[1..], "/", "::"))>", fr>
    | loc f <- directoryIndexes
      , step("Indexing directories", f)
      , /\/internal\// !:= f.path
      , f != src
      , fr := "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>API<} else {><capitalize(src.file)><}>/<fragment(src, f)>"
  }
  + // Finally, the index entries for Rascal modules and declarations, as extracted from the source code:
    {  // `((getDefaultPathConfig))` -> `Libary/util/Reflective#getDefaultPathConfig`
      *{<"<item.kind>:<item.name>", fr>, 
        <item.name, fr > | item.name?, !(item is moduleInfo)},
     
      // `((Library:getDefaultPathConfig))` -> `/Library/util/Reflective#getDefaultPathConfig`
      *{<"<rootName(src, isPackageCourse)>:<item.name>", fr >,
        <"<rootName(src, isPackageCourse)>:<item.kind>:<item.name>", fr > | item.name?, !(item is moduleInfo)},

      // `((util::Reflective::getDefaultPathConfig))` -> `/Library/util/Reflective#getDefaultPathConfig`
      *{<"<item.moduleName><sep><item.name>", fr >,
        <"<modulePath(item.moduleName)><sep><item.name>", fr >,
        <"<rootName(src, isPackageCourse)>:<modulePath(item.moduleName)><sep><item.name>", fr >,
        <"<item.kind>:<item.moduleName><sep><item.name>", fr >,
        <"<f[extension=""].file><sep><item.name>", fr> | item.name?, !(item is moduleInfo), sep <- {"::", "-"}},

      // ((Library:util::Reflective::getDefaultPathConfig))` -> `/Library/util/Reflective#getDefaultPathConfig`
      *{<"<rootName(src, isPackageCourse)>:<item.moduleName><sep><item.name>", fr >,
         <"<rootName(src, isPackageCourse)>:<item.kind>:<item.moduleName><sep><item.name>", fr > | item.name?, !(item is moduleInfo), sep <- {"::", "-"}},

      // ((Set)) -> `/Library/S
      *{<item.moduleName, "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>/API<} else {>/<capitalize(src.file)><}>/<modulePath(item.moduleName)>.md" >, 
        <"module:<item.moduleName>", "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>/API<} else {>/<capitalize(src.file)><}>/<modulePath(item.moduleName)>.md" > 
        | item is moduleInfo
      },

      // `((Library:Set))` -> `/Library/Set`
      *{<"<rootName(src, isPackageCourse)>:<item.moduleName>", mfr >,
        <"<rootName(src, isPackageCourse)>:<modulePath(item.moduleName)>", mfr >,
        <"<rootName(src, isPackageCourse)>:module:<item.moduleName>",  mfr>,
        <item.src[extension=""].file, mfr>
       | item is moduleInfo, 
         mfr := "<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>/API<} else {>/<capitalize(src.file)><}>/<modulePath(item.moduleName)>.md" }

      | loc f <- rascalFiles, step("Indexing modules", f), list[DeclarationInfo] inf := safeExtract(f), item <- inf,
        fr := "/<if (isPackageCourse) {>/Packages/<package(packageName)><}>/<if (isPackageCourse && src.file in {"src","rascal","api"}) {>API<} else {><capitalize(src.file)><}>/<modulePath(item.moduleName)>.md<moduleFragment(item.moduleName)>-<item.name>"
    };

  jobEnd("Indexing modules");
  jobEnd("Indexing directories");
  jobEnd("Indexing concepts");
  jobEnd("Indexing images");

  return result;
}

public bool isConceptFile(loc f) = f.extension == "md";

public bool isRascalFile(loc f) = f.extension == "rsc";

public bool(loc) isFreshConceptFile(datetime lM) 
  = bool (loc f) { 
      return isConceptFile(f) && lastModified(f) > lM;
    };

public bool(loc) isFreshRascalFile(datetime lM)
  = bool (loc f) {
      return f.extension in {"rsc"} && lastModified(f) > lM;
    };

public bool isImageFile(loc f) = f.extension in {"png", "jpg", "svg", "jpeg"};

@synopsis{ignores extracting errors because they will be found later}
private list[DeclarationInfo] safeExtract(loc f) {
  try {
    return extractInfo(f);
  }
  catch Java(_,_): return [];
  catch ParseError(_): return [];
}