module lang::rascal::tutor::Names

import IO;
import List;
import Location;
import String;
import util::Reflective;

data PathConfig(
  str  packageName="",
  str  packageArtifactId="",
  str  packageGroupId="",
  loc  packageRoot=|unknown:///|,
  loc  sources=|http://github.com/usethesource/rascal|,
  loc  issues=|http://github.com/usethesource/rascal/issues|,
  loc  license=|cwd:///LICENSE.md|,
  loc  citation=|cwd:///CITATION.md|,
  loc  funding=|cwd:///FUNDING.md|,
  loc  authors=|cwd:///AUTHORS.md|,
  loc  releaseNotes=|cwd:///RELEASE-NOTES.md|,
  str  packageVersion=getRascalVersion(),
  bool isPackageCourse=false
);

data PathConfig(loc currentRoot = |unknown:///|, loc currentFile = |unknown:///|);
data Message(str cause="");

default str fragment(loc root, loc concept) = capitalize(relativize(root, concept).path)[1..];
      
str fragment(loc root, loc concept) = fragment(root, concept + "index.md")
  when isDirectory(concept) || root == concept;

str fragment(loc root, loc concept) = fragment(root, concept.parent + "index.md")
  when concept.parent?, concept.parent.file == concept[extension=""].file;

str modulePath(/^<prefix:.*>::Index$/) = modulePath("<prefix>::module_Index");
default str modulePath(str moduleName) = "<replaceAll(moduleName, "::", "/")>";
default str moduleFragment(str moduleName) = "#<replaceAll(moduleName, "::", "-")>";
 
@synopsis{keeps it as close to the original as possible}
default str package(str input) = input;

str removeSpaces(/^<prefix:.*><spaces:\s+><postfix:.*>$/) 
  = removeSpaces("<prefix><capitalize(postfix)>");

default str removeSpaces(str s) = s;

// remove Course:module: prefixes
str addSpaces(/^<prefix:[^:]+>:<postfix:[^:].*>$/)
  = addSpaces(postfix);

// select final function name if present
str addSpaces(/^<prefix:.+>::<name:[^:]+>$/)
  = name; // no recursion to avoid splitting function names

// split and uncapitalize CamelCase
str addSpaces(/^<prefix:[A-Za-z0-9\ ]+[a-z0-9]><postfix:[A-Z].+>/) =
  addSpaces("<uncapitalize(prefix)> <uncapitalize(postfix)>");

default str addSpaces(str s) = capitalize(split("-", s)[-1]);

@synopsis{produces `"../../.."` for pathToRoot(|aap:///a/b|, |aap:///a/b/c/d|)}
str pathToRoot(loc root, loc src, bool isPackageCourse) 
  = "<if (isPackageCourse) {>../../<}>..<for (e <- split("/", relativize(root, src).path), e != "") {>/..<}>"
  when isDirectory(src);

str pathToRoot(loc root, loc src, bool isPackageCourse) 
  = pathToRoot(root, src.parent, isPackageCourse)
  when isFile(src);  

str rootName(loc src, bool isPackageCourse) = isPackageCourse && src.file in {"src", "Src", "SRC", "Rascal", "rascal", "API", "api"} ? "API" : capitalize(src.file);