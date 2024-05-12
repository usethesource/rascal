@synopsis{Defines the syntax of filesystem and network drive paths on DOS and Windows Systems.}
@description{
This syntax definition ports open-source manually written Java, C++ and C# code parsers for Windows
syntax of file and directory names, as well as shares on local networks (UNC notation). 

Instead of following the strict definitions of what is admissable and what is not, all implementations
try to be a as lenient as possible. And so this is reflected in the grammar below as well.
}
module lang::paths::Windows

start syntax WindowsPathString = WindowsPath;

lexical WindowsPath
    = unc              : "\\\\" PathSep* PathChar* hostName PathSep+ PathChar* shareName PathSep+ WindowsFilePath path
    | absolute         : [A-Za-z] drive ":" PathSep WindowsFilePath path
    | driveRelative    : [A-Za-z] drive ":" WindowsFilePath path
    | directoryRelative: PathSep WindowsFilePath
    | relative         : WindowsFilePath path         
    ;

lexical PathChar = 
    @synopsis{This is the most admissable we can be. Note that some paths will be incorrect for older versions of DOS and Windows}
    ![\a00-\a20 \< \> : \" | ? * \\ /];

lexical PathSegment
    = current: "."
    | parent : ".."
    | name   : PathChar+ \ ".." \ "."
    ;

lexical PathSep = [\\/];

lexical WindowsFilePath = {PathSegment PathSep+}* segments [\ ] !<< (); // only the last segment must not end in spaces.

@synopsis{Convert a windows path literal to a source location URI}
@description{
1. parses the path using the grammar for ((WindowsPath))
2. takes the _literal_ name components using string interpolation `"<segment>"`. This means no decoding/encoding happens at all while extracting
hostname, share name and path segment names. Also all superfluous path separators are skipped.
3. uses `loc + str` path concatenation with its builtin character encoding to construct the URI. Also
the right path separators are introduced. 
}
loc parseWindowsPath(str input) = mapPathToLoc([WindowsPath] input);

@synopsis{UNC}
loc mapPathToLoc((WindowsPath) `\\\\<PathSep* _><PathChar* hostName><PathSep+ _><PathChar* shareName><PathSep+ _><WindowsFilePath path>`)
    = (|file://<hostName>/| + "<shareName>" | it + "<segment>" | segment <- path.segments );

@synopsis{Absolute}
loc mapPathToLoc((WindowsPath) `<[A-Za-z] drive>:<PathSep _><WindowsFilePath path>`) 
    = (|file:///<drive>:/| | it + "<segment>" | segment <- path.segments);

@synopsis{Drive relative}
loc mapPathToLoc((WindowsPath) `<[A-Za-z] drive>:<WindowsFilePath path>`) 
    = (|file:///<drive>:| | it + "<segment>" | segment <- path.segments);

@synopsis{Directory relative}
loc mapPathToLoc((WindowsPath) `<PathSep _><WindowsFilePath path>`) 
    = (|file:///| | it + "<segment>" | segment <- path.segments);

@synopsis{Relative}
loc mapPathToLoc((WindowsPath) `<WindowsFilePath path>`) 
    = (|cwd:///| | it + "<segment>" | segment <- path.segments);