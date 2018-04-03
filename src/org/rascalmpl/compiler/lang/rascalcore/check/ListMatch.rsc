module lang::rascalcore::check::ListMatch

import lang::rascal::\syntax::Rascal;

str tp(Literal lit) = "<lit>";

str tp((Pattern) `<Type typ> <Name name>`) = "";

str tp((Pattern) `[ <{Pattern ","}* elements> ]`) = "";

str tp((Pattern) `<QualifiedName qualifiedName>*`) = "";