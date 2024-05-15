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
@pitfalls{
* Length limitations are not implemnted by this parser. This means that overly long names will lead
to IO exceptions when they are finally used.
* The names of drives, files and devices are mapped as-is, without normalization. This means that
the resulting `loc` value may not be a _canonical_ representation of the identified resource.
Normalization of `loc` values is for a different function TBD.
}
module lang::paths::Windows

import IO;
import util::SystemAPI;
 
lexical WindowsPath
    = unc              : Slash Slash Slashes? PathChar* \ "." Slashes PathChar* Slashes WindowsFilePath 
    | uncDOSDrive      : Slash Slash Slashes? DOSDevice Slashes Drive ":" OptionalWindowsFilePath 
    | uncDOSPath       : Slash Slash Slashes? DOSDevice Slashes PathChar* Slashes WindowsFilePath 
    | absolute         : Drive ":" Slashes WindowsFilePath 
    | driveRelative    : Drive ":" WindowsFilePath 
    | directoryRelative: Slash WindowsFilePath
    | relative         : WindowsFilePath          
    ;

lexical OptionalWindowsFilePath
    = ()
    | Slashes WindowsFilePath
    ;

lexical DOSDevice = [.?];

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
private loc mapPathToLoc((WindowsPath) `<Slash _><Slash _><Slashes? _><PathChar* hostName><Slashes _><PathChar* shareName><Slashes _><WindowsFilePath path>`)
    = appendPath(|unc://<hostName>/| + "<shareName>", path);

@synopsis{DOS UNC Device Drive}
private loc mapPathToLoc((WindowsPath) `<Slash _><Slash _><Slashes? _><DOSDevice dq><Slashes _><Drive drive>:<OptionalWindowsFilePath path>`)
    = appendPath(|unc://<deviceIndicator(dq)>/| + "<drive>:", path);

@synopsis{DOS UNC Device Path}
private loc mapPathToLoc((WindowsPath) `<Slash _><Slash _><Slashes? _><DOSDevice dq><Slashes _><PathChar* deviceName><Slashes _><WindowsFilePath path>`)
    = appendPath(|unc://<deviceIndicator(dq)>/| + "<deviceName>", path);

private str deviceIndicator((DOSDevice) `?`) = "%3F";
private str deviceIndicator((DOSDevice) `.`) = "%2E";

@synopsis{DOS UNCPath}
private loc mapPathToLoc((WindowsPath) `<Slash _><Slash _><Slashes? _>?<Slashes _><PathChar* shareName><Slashes _><WindowsFilePath path>`)
    = appendPath(|unc://%3F/| + "<shareName>", path);


@synopsis{Absolute: given the drive and relative to its root.}
private loc mapPathToLoc((WindowsPath) `<Drive drive>:<Slashes _><WindowsFilePath path>`) 
    = appendPath(|file:///<drive>:/|, path);

@synopsis{Drive relative: relative to the current working directory on the given drive.}
private loc mapPathToLoc((WindowsPath) `<Drive drive>:<WindowsFilePath path>`) 
    = appendPath(|file:///<drive>:.|, path);

@synopsis{Directory relative: relative to the root of the current drive.}
private loc mapPathToLoc((WindowsPath) `<Slash _><WindowsFilePath path>`) 
    = appendPath(|cwdrive:///|, path);

@synopsis{Relative to the current working directory on the current drive.}
private loc mapPathToLoc((WindowsPath) `<WindowsFilePath path>`) 
    = appendPath(|cwd:///|, path);

private loc appendPath(loc root, WindowsFilePath path)
    = (root | it + "<segment>" | segment <- path.segments);

private loc appendPath(loc root, (OptionalWindowsFilePath) ``) = root;

private loc appendPath(loc root, (OptionalWindowsFilePath) `<Slashes _><WindowsFilePath path>`) 
    = appendPath(root, path);

private bool IS_WINDOWS = /win/i := getSystemProperty("os.name");

test bool uncSharePath()
    = parseWindowsPath("\\\\Server2\\Share\\Test\\Foo.txt")
    == |unc://Server2/Share/Test/Foo.txt|;

test bool uncDrivePath()
    = parseWindowsPath("\\\\system07\\C$\\")
    == |unc://system07/C$|;


test bool uncDOSDevicePathLocalFileQuestion() {
    loc l = parseWindowsPath("\\\\?\\c:");
    
    if (IS_WINDOWS) {
        assert exists(l);
    }

    return l == |unc://%3F/c:|;
}

test bool uncDOSDevicePathLocalFileDot() {
    loc l = parseWindowsPath("\\\\.\\C:\\Test\\Foo.txt");
    
    if (IS_WINDOWS) {
        assert exists(l);
    }

    return l == |unc://%2E/C:/Test/Foo.txt|;
}

test bool uncDOSDeviceVolumeGUIDReference() {
    loc l = parseWindowsPath("\\\\.\\Volume{b75e2c83-0000-0000-0000-602f00000000}\\Test\\Foo.txt");

    return l == |unc://%2E/Volume%7Bb75e2c83-0000-0000-0000-602f00000000%7D/Test/Foo.txt|;
}

test bool uncDOSDeviceBootPartition() {
    loc l = parseWindowsPath("\\\\.\\BootPartition\\");
    return l == |unc://%2E/BootPartition|;
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

test bool uncDOSDevicePathShare() {
    loc l = parseWindowsPath("\\\\?\\UNC\\localhost\\ADMIN$\\System32\\cmd.exe");
    
    if (IS_WINDOWS) {
        assert exists(l);
    }

    return |unc://%3F/UNC/localhost/ADMIN$/System32/cmd.exe| == l;
}
