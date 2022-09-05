module lang::rascal::tutor::Names

import String;
import Location;

str fragment(loc concept) = stripDoubleEnd(replaceAll("#<capitalize(concept[extension=""].path[1..])>", "/", "-"));
str fragment(loc root, loc concept) = fragment(relativize(root, concept));
str moduleFragment(str moduleName) = "#<replaceAll(moduleName, "::", "-")>";

str stripDoubleEnd(/<prefix:.*>\-<a:[^\-]+>\-<b:[^\-]+>$/) = "<prefix>-<b>" when a == b;
default str stripDoubleEnd(str x) = x;

str removeSpaces(/^<prefix:.*><spaces:\s+><postfix:.*>$/) 
  = removeSpaces("<prefix><capitalize(postfix)>");

default str removeSpaces(str s) = s;

str addSpaces(/^<prefix:.*[a-z0-9]><postfix:[A-Z].+>/) =
  addSpaces("<prefix> <uncapitalize(postfix)>");

default str addSpaces(str s) = split("-", s)[-1];