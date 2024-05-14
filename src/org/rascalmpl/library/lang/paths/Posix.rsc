@synopsis{Defines the syntax of filesystem and network drive paths on DOS and Windows Systems.}
@description{
This syntax definition of POSIX paths and file names, including some of the conventions 
with ~ for the home folder and . and .. for relative directories.

The main function of this module, ((parsePosixPath)):
* faithfully maps any syntactically correctly Posix paths to syntactically correct `loc` values.
* throws a ParseError if the path does not comply. 
* ensures that if the file exists on system A, then the `loc` representation
resolves to the same file on system A via any ((Library:module:IO)) function. 
* and nothing more. No normalization, no interpretatioon of `.` and `..`, no changing of cases. 
This is left to downstream processors of `loc` values, if necessary. The current transformation
is purely syntactical, and tries to preserve the semantics of the path as much as possible.
}
@pitfalls{
* the `~` notation is typically a feature of the shell and not of system paths. However it is so commonly
used to refer to the  home directories of users that we've added an interpretation here with the `home:///` scheme.
}
module lang::paths::Posix

lexical PosixPath
    = absolute: Slashes PosixFilePath?
    | relative: PosixFilePath 
    | home    : "~" (Slashes PosixFilePath)?
    | user    : "~" UserName name (Slashes PosixFilePath)?
    ;

lexical UserName = ![/~]+;

lexical PathChar = ![/];

lexical PathSegment
    = current: "."
    | parent : ".."
    | name   : (PathChar \ "~" PathChar*) \ ".." \ "." \ "~"
    ;

lexical Slashes = Slash+ !>> [/];

lexical Slash = [/];

lexical PosixFilePath = {PathSegment Slashes}+ segments Slashes?;

import ParseTree;

@synopsis{Convert a Posix path literal to a source location URI}
@description{
1. parses the path using the grammar for ((PosixPath))
2. takes the _literal_ name components using string interpolation `"<segment>"`. This means no decoding/encoding happens at all while extracting
hostname, share name and path segment names. Also all superfluous path separators are skipped.
3. uses `loc + str` path concatenation with its builtin character encoding to construct the URI. Also
the right path separators are introduced. 
}
loc parsePosixPath(str input, loc src=|unknown:///|) = mapPathToLoc(parse(#PosixPath, input, src));

@synopsis{Root is a special case}
private loc mapPathToLoc((PosixPath) `<Slashes _>`) 
    = |file:///|;

@synopsis{Absolute: given the drive and relative to its root.}
private loc mapPathToLoc((PosixPath) `<Slashes _><PosixFilePath path>`) 
    = appendPath(|file:///|, path);

@synopsis{Relative: relative to the current working directory.}
private loc mapPathToLoc((PosixPath) `<PosixFilePath path>`) 
    = appendPath(|cwd:///|, path);

@synopsis{Home relative: relative to the current users home directory}
private loc mapPathToLoc((PosixPath) `~<Slash _><PosixFilePath path>`) 
    = appendPath(|home:///|, path);

@synopsis{Home relative: relative to the current users home directory}
private loc mapPathToLoc((PosixPath) `~`) 
    = |home:///|;

@synopsis{User relative: relative to any specific user's home directory}
private loc mapPathToLoc((PosixPath) `~<UserName name><Slash _><PosixFilePath path>`) 
    = appendPath(|home:///../<name>/|, path);

@synopsis{User relative: relative to any specific user's home directory}
private loc mapPathToLoc((PosixPath) `~<UserName name>`) 
    = |home:///../<name>/|;

private loc appendPath(loc root, PosixFilePath path)
    = (root | it + "<segment>" | segment <- path.segments);

test bool root()
    = parsePosixPath("/") == |file:///|;

test bool absolutePath()
    = parsePosixPath("/usr/local/bin")
    == |file:///usr/local/bin|;

test bool relativePath()
    = parsePosixPath(".bash_rc")
    == |cwd:///.bash_rc|;

test bool homePath()
    = parsePosixPath("~/.bash_profile")
    == |home:///.bash_profile|;

test bool userPath()
    = parsePosixPath("~root/.bash_profile")
    == |home:///../root/.bash_profile|;
