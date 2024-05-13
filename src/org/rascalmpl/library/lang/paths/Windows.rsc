@synopsis{Defines the syntax of filesystem and network drive paths on DOS and Windows Systems.}
@description{
This syntax definition of file paths and file names in Windows fprmalizes open-source implementations 
manually written  Java, C++ and C# code. These are parsers for Windows syntax of file and directory names,
as well as shares on local networks (UNC notation). It also derives from openly available documentation 
sources on Windows and the .NET platform for confirmation and test examples.

The main function of this module, ((parseWindowsPath)):
* faithfully maps any syntactically correctly Windows paths to syntactically correct `loc` values.
* ensures that if the file exists on system A, then the `loc` representation
resolves to the same file on system A via any ((module::IO)) function. 
* and nothing more.
}
module lang::paths::Windows

lexical WindowsPath
    = unc              : Slash Slash Slashes? PathChar* hostName Slashes PathChar* shareName Slashes WindowsFilePath path
    | absolute         : Drive drive ":" Slashes WindowsFilePath path
    | driveRelative    : Drive drive ":" WindowsFilePath path
    | directoryRelative: Slash WindowsFilePath
    | relative         : WindowsFilePath path         
    ;

lexical PathChar = !([\a00-\a20\< \> : \" | ? * \\ /] - [\ ]);

lexical PathSegment
    = current: "."
    | parent : ".."
    | name   : PathChar+ \ ".." \ "."
    ;

lexical Drive = [A-Za-z];

lexical Slashes = Slash+ !>> [\\/];

lexical Slash = [\\/];

lexical Drive = [A-Za-z];

lexical WindowsFilePath = {PathSegment Slashes}* segments [\ ] !<< (); // only the last segment must not end in spaces.

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
loc mapPathToLoc((WindowsPath) `<Slash _><Slash _><Slashes? _><PathChar* hostName><Slashes _><PathChar* shareName><Slashes _><WindowsFilePath path>`)
    = (|file://<hostName>/| + "<shareName>" | it + "<segment>" | segment <- path.segments );

@synopsis{Absolute}
// loc mapPathToLoc((WindowsPath) `<[A-Za-z] drive>:<PathSep _><WindowsFilePath path>`) 
loc mapPathToLoc((WindowsPath) `<Drive drive>:<Slashes _><WindowsFilePath path>`) 
    = (|file:///<drive>:/| | it + "<segment>" | segment <- path.segments);

@synopsis{Drive relative}
loc mapPathToLoc((WindowsPath) `<Drive drive>:<WindowsFilePath path>`) 
    = (|file:///<drive>:| | it + "<segment>" | segment <- path.segments);

@synopsis{Directory relative}
loc mapPathToLoc((WindowsPath) `<Slash _><WindowsFilePath path>`) 
    = (|file:///| | it + "<segment>" | segment <- path.segments);

@synopsis{Relative}
loc mapPathToLoc((WindowsPath) `<WindowsFilePath path>`) 
    = (|cwd:///| | it + "<segment>" | segment <- path.segments);

test bool uncSharePath()
    = parseWindowsPath("\\\\Server2\\Share\\Test\\Foo.txt")
    == |file://Server2/Share/Test/Foo.txt|;

test bool uncDrivePath()
    = parseWindowsPath("\\\\system07\\C$\\")
    == |file://system07/C$|;

test bool simpleDrivePathC()
    = parseWindowsPath("C:\\Program Files\\Rascal")
    == |file:///C:/Program%20Files/Rascal|;

test bool simpleDrivePathD()
    = parseWindowsPath("D:\\Program Files\\Rascal")
    == |file:///D:/Program%20Files/Rascal|;