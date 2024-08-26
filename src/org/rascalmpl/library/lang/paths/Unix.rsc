@synopsis{Defines the syntax of filesystem and network drive paths on DOS and Windows Systems.}
@description{
This syntax definition of POSIX paths and file names, including some of the conventions 
with ~ for the home folder and . and .. for relative directories.

The main function of this module, ((parseUnixPath)):
* faithfully maps any syntactically correctly Unix paths to syntactically correct `loc` values.
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
* otherwise, the path syntax may be different from what you have to type in _bash_ or _zsh_. This is because shells
need to reserve characters, like spaces, for different purposes (commandline argument separation). The 
current definition is about the path notation that shells like _zsh_ and _bash_, and other programs, have to pass into the string arguments of
OS features, after their own concatenation, splicing, variable expansion, de-escaping and unquoting routines have finished.. 
}
module lang::paths::Unix

lexical UnixPath
    = absolute: Slashes UnixFilePath?
    | relative: UnixFilePath 
    | home    : "~" (Slashes UnixFilePath)?
    | user    : "~" UserName uname (Slashes UnixFilePath)?
    ;

lexical UserName = ![/~]+;

lexical PathChar = ![/];

lexical PathSegment
    = current: "."
    | parent : ".."
    | pname  : (PathChar \ "~" PathChar*) \ ".." \ "." \ "~"
    ;

lexical Slashes = Slash+ !>> [/];

lexical Slash = [/];

lexical UnixFilePath = {PathSegment Slashes}+ segments Slashes?;

import ParseTree;

@synopsis{Convert a Unix path literal to a source location URI}
@description{
1. parses the path using the grammar for ((UnixPath))
2. takes the _literal_ name components using string interpolation `"<segment>"`. This means no decoding/encoding happens at all while extracting
hostname, share name and path segment names. Also all superfluous path separators are skipped.
3. uses `loc + str` path concatenation with its builtin character encoding to construct the URI. Also
the right path separators are introduced. 
}
loc parseUnixPath(str input, loc src=|unknown:///|) = mapPathToLoc(parse(#UnixPath, input, src));

@synopsis{Root is a special case}
private loc mapPathToLoc((UnixPath) `<Slashes _>`) 
    = |file:///|;

@synopsis{Absolute: given the drive and relative to its root.}
private loc mapPathToLoc((UnixPath) `<Slashes _><UnixFilePath path>`) 
    = appendPath(|file:///|, path);

@synopsis{Relative: relative to the current working directory.}
private loc mapPathToLoc((UnixPath) `<UnixFilePath path>`) 
    = appendPath(|cwd:///|, path);

@synopsis{Home relative: relative to the current users home directory}
private loc mapPathToLoc((UnixPath) `~<Slash _><UnixFilePath path>`) 
    = appendPath(|home:///|, path);

@synopsis{Home relative: relative to the current users home directory}
private loc mapPathToLoc((UnixPath) `~`) 
    = |home:///|;

@synopsis{User relative: relative to any specific user's home directory}
private loc mapPathToLoc((UnixPath) `~<UserName uname><Slash _><UnixFilePath path>`) 
    = appendPath(|home:///../<uname>/|, path);

@synopsis{User relative: relative to any specific user's home directory}
private loc mapPathToLoc((UnixPath) `~<UserName uname>`) 
    = |home:///../<uname>/|;

private loc appendPath(loc root, UnixFilePath path)
    = (root | it + "<segment>" | segment <- path.segments);

test bool root()
    = parseUnixPath("/") == |file:///|;

test bool absolutePath()
    = parseUnixPath("/usr/local/bin")
    == |file:///usr/local/bin|;

test bool relativePath()
    = parseUnixPath(".bash_rc")
    == |cwd:///.bash_rc|;

test bool homePath()
    = parseUnixPath("~/.bash_profile")
    == |home:///.bash_profile|;

test bool userPath()
    = parseUnixPath("~root/.bash_profile")
    == |home:///../root/.bash_profile|;
