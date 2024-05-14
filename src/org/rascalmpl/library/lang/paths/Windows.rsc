@synopsis{Defines the syntax of filesystem and network drive paths on DOS and Windows Systems.}
@description{
This syntax definition of file paths and file names in Windows formalizes open-source implementations 
manually written  in Java, C++ and C# code. These are parsers for Windows syntax of file and directory names,
as well as shares on local networks (UNC notation). It also derives from openly available documentation 
sources on Windows and the .NET platform for confirmation and test examples.

The main function of this module, ((parseWindowsPath)):
* faithfully maps any syntactically correctly Windows paths to syntactically correct `loc` values.
* throws a ParseError if the path does not comply. Typically file names ending in spaces do not comply.
* ensures that if the file exists on system A, then the `loc` representation
resolves to the same file on system A via any ((Library:module:IO)) function. 
* and nothing more. No normalization, no interpretatioon of `.` and `..`, no changing of cases. 
This is left to downstream processors of `loc` values, if necessary. The current transformation
is purely syntactical, and tries to preserve the semantics of the path as much as possible.
}
module lang::paths::Windows

import IO;
import util::SystemAPI;

lexical WindowsPath
    = unc              : Slash Slash Slashes? PathChar* hostName Slashes PathChar* shareName Slashes WindowsFilePath path
    | uncDOS           : Slash Slash Slashes? "?" Slashes PathChar* shareName Slashes WindowsFilePath path
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

lexical WindowsFilePath = {PathSegment Slashes}* segments Slashes? [\ .] !<< (); // only the last segment must not end in spaces.

import ParseTree;

@synopsis{Convert a windows path literal to a source location URI}
@description{
1. parses the path using the grammar for ((WindowsPath))
2. takes the _literal_ name components using string interpolation `"<segment>"`. This means no decoding/encoding happens at all while extracting
hostname, share name and path segment names. Also all superfluous path separators are skipped.
3. uses `loc + str` path concatenation with its builtin character encoding to construct the URI. Also
the right path separators are introduced. 
}
loc parseWindowsPath(str input, loc src=|unknown:///|) = mapPathToLoc(parse(#WindowsPath, input, src));

@synopsis{UNC}
loc mapPathToLoc((WindowsPath) `<Slash _><Slash _><Slashes? _><PathChar* hostName><Slashes _><PathChar* shareName><Slashes _><WindowsFilePath path>`)
    = appendPath(|unc://<hostName>/| + "<shareName>", path);

@synopsis{DOC UNC}
loc mapPathToLoc((WindowsPath) `<Slash _><Slash _><Slashes? _>?<Slashes _><PathChar* shareName><Slashes _><WindowsFilePath path>`)
    = appendPath(|unc://%3F/| + "<shareName>", path);

@synopsis{Absolute: given the drive and relative to its root.}
loc mapPathToLoc((WindowsPath) `<Drive drive>:<Slashes _><WindowsFilePath path>`) 
    = appendPath(|file:///<drive>:/|, path);

@synopsis{Drive relative: relative to the current working directory on the given drive.}
loc mapPathToLoc((WindowsPath) `<Drive drive>:<WindowsFilePath path>`) 
    = appendPath(|file:///<drive>:.|, path);

@synopsis{Directory relative: relative to the root of the current drive.}
loc mapPathToLoc((WindowsPath) `<Slash _><WindowsFilePath path>`) 
    = appendPath(|cwdrive:///|, path);

@synopsis{Relative to the current working directory on the current drive.}
loc mapPathToLoc((WindowsPath) `<WindowsFilePath path>`) 
    = appendPath(|cwd:///|, path);

loc appendPath(loc root, WindowsFilePath path)
    = (root | it + "<segment>" | segment <- path.segments);

private bool IS_WINDOWS = /win/i := getSystemProperty("os.name");

test bool uncSharePath()
    = parseWindowsPath("\\\\Server2\\Share\\Test\\Foo.txt")
    == |unc://Server2/Share/Test/Foo.txt|;

test bool uncDrivePath()
    = parseWindowsPath("\\\\system07\\C$\\")
    == |unc://system07/C$|;

test bool uncDOSDrive() {
    loc l = parseWindowsPath("\\\\?\\C$\\");
    
    if (IS_WINDOWS) {
        assert exists(l + "Program Files");
    }

    return l == |unc://%3F/C$|;
}

test bool simpleDrivePathC()
    = parseWindowsPath("C:\\Program Files\\Rascal")
    == |file:///C:/Program%20Files/Rascal|;

test bool mixedSlashesDrivePathC()
    = parseWindowsPath("C:\\Program Files/Rascal")
    == |file:///C:/Program%20Files/Rascal|;

test bool trailingSlashesDrivePathC()
    = parseWindowsPath("C:\\Program Files\\Rascal\\\\")
    == |file:///C:/Program%20Files/Rascal|;

test bool simpleDrivePathD()
    = parseWindowsPath("D:\\Program Files\\Rascal")
    == |file:///D:/Program%20Files/Rascal|;

test bool uncNetworkShareOk() {
    loc l = parseWindowsPath("\\\\localhost\\ADMIN$\\System32\\cmd.exe");

    if (IS_WINDOWS) {
        return exists(l);
    }
    else {
        return |unc://localhost/ADMIN$/System32/cmd.exe| == l;
    }
}