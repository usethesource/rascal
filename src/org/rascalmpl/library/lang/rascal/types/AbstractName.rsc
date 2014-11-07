@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Mark Hills - Mark.Hills@cwi.nl (CWI)}
@bootstrapParser
module lang::rascal::types::AbstractName

import String;
import ParseTree;

import lang::rascal::\syntax::Rascal;

@doc{Abstract syntax for names.} 
data RName =
	  RSimpleName(str name)
	| RCompoundName(list[str] nameParts)     
	;

@doc{Annotate abstract names with locations.}
anno loc RName@at;

@doc{Convert RName to a simple name}
public str getSimpleName(RSimpleName(str name)) = name;
public str getSimpleName(RCompoundName([ *str _, str name])) = name;

@doc{Convert qualified names into an abstract representation.}
public RName convertName(QualifiedName qn) {
	if ((QualifiedName)`<{Name "::"}+ nl>` := qn) { 
		nameParts = [ (startsWith("<n>","\\") ? substring("<n>",1) : "<n>") | n <- nl ];
		if (size(nameParts) > 1) {
			return RCompoundName(nameParts)[@at = qn@\loc];
		} else {
			return RSimpleName(head(nameParts))[@at = qn@\loc];
		} 
	}
	throw "Unexpected syntax for qualified name: <qn>";
}

@doc{Convert names into an abstract representation.}
public RName convertName(Name n) {
	if (startsWith("<n>","\\"))
		return RSimpleName(substring("<n>",1))[@at = n@\loc];
	else
		return RSimpleName("<n>");
}

@doc{Get the last part of a qualified name.}
public Name getLastName(QualifiedName qn) {
	if ((QualifiedName)`<{Name "::"}+ nl>` := qn)
		return head(tail([ n | n <- nl ],1));
	throw "Unexpected syntax for qualified name: <qn>";
}

@doc{Append a name to the end of the current name.}
public RName appendName(RSimpleName(str s1), RSimpleName(str s2)) = RCompoundName([s1,s2]);
public RName appendName(RSimpleName(str s1), RCompoundName(list[str] s2)) = RCompoundName([s1] + s2);
public RName appendName(RCompoundName(list[str] s1), RSimpleName(str s2)) = RCompoundName(s1 + s2);
public RName appendName(RCompoundName(list[str] s1), RCompoundName(list[str] s2)) = RCompoundName(s1 + s2);

@doc{Pretty-print a list of abstract names, separated by ::}
public str prettyPrintNameList(list[str] nameList) = intercalate("::", nameList);
	
@doc{Pretty-print the abstract representation of a name.}
public str prettyPrintName(RSimpleName(str s)) = s;
public str prettyPrintName(RCompoundName(list[str] sl)) = prettyPrintNameList(sl);

@doc{Covert a string to an RName}
public RName convertNameString(str s) {
	sl = [ (startsWith("<n>","\\") ? substring("<n>",1) : "<n>") | n <- split("::",s) ];
	if (size(sl) >= 2) {
		return RCompoundName(sl);
	} else {
		return RSimpleName(s);
	}
}
