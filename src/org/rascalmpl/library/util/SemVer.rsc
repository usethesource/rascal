module util::SemVer

@doc{
#### Synopsis

Semantic Versioning
#### Description

Check that a given version string satisfies a range-set as defined by:
(See https://github.com/npm/node-semver):
``````
range-set  ::= range ( logical-or range ) *
logical-or ::= ( ' ' ) * '||' ( ' ' ) *
range      ::= hyphen | simple ( ' ' simple ) * | ''
hyphen     ::= partial ' - ' partial
simple     ::= primitive | partial | tilde | caret
primitive  ::= ( '<' | '>' | '>=' | '<=' | '=' | ) partial
partial    ::= xr ( '.' xr ( '.' xr qualifier ? )? )?
xr         ::= 'x' | 'X' | '*' | nr
nr         ::= '0' | ['1'-'9'] ( ['0'-'9'] ) *
tilde      ::= '~' partial
caret      ::= '^' partial
qualifier  ::= ( '-' pre )? ( '+' build )?
pre        ::= parts
build      ::= parts
parts      ::= part ( '.' part ) *
part       ::= nr | [-0-9A-Za-z]+
``````
}
@javaClass{org.rascalmpl.library.util.SemVerLib}
java bool satisfiesVersion(str version, str rangeSet);

@javaClass{org.rascalmpl.library.util.SemVerLib}
java bool lessVersion(str version1, str version2);

@javaClass{org.rascalmpl.library.util.SemVerLib}
java bool lessEqualVersion(str version1, str version2);

@javaClass{org.rascalmpl.library.util.SemVerLib}
java bool greaterVersion(str version1, str version2);

@javaClass{org.rascalmpl.library.util.SemVerLib}
java bool greaterEqualVersion(str version1, str version2);


@javaClass{org.rascalmpl.library.util.SemVerLib}
java bool equalVersion(str version1, str version2);

@javaClass{org.rascalmpl.library.util.SemVerLib}
java str getRascalVersion();

@javaClass{org.rascalmpl.library.util.SemVerLib}
java str getRascalRuntimeVersion();

@javaClass{org.rascalmpl.library.util.SemVerLib}
java str getRascalCompilerVersion();
