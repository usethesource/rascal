module lang::rascal::tests::library::IO

import IO;
import DateTime;
import String;

test bool testLogicalLocationResolution() {
    str scheme = "test-logical";

    value exceptionOf(value() f) {
        try {
            f();
            return "";
        } catch e: {
            return e;
        }
    }

    bool throwsUnsupportedAuthority(value() f) {
        return /Unsupported authority/ := "<exceptionOf(f)>";
    }

    bool throwsExceptionDownstream(value() f) {
        return str s := "<exceptionOf(f)>" && "" != s && /Unsupported authority/ !:= s;
    }

    try {
        // Register authorities `foo` and `bar`
        registerLocations(scheme, "foo", (|<scheme>://foo/|: |file:///|));
        registerLocations(scheme, "bar", (|<scheme>://bar/|: |<scheme>://foo/|));

        assert lastModified(|<scheme>://foo/|) == lastModified(|file:///|);
        assert lastModified(|<scheme>://bar/|) == lastModified(|file:///|);

        assert throwsExceptionDownstream(value() { lastModified(|<scheme>://foo/x/y/z|); });
        assert throwsExceptionDownstream(value() { lastModified(|<scheme>://bar/x/y/z|); });
        assert throwsUnsupportedAuthority(value() { lastModified(|<scheme>://baz/|); });
        assert throwsUnsupportedAuthority(value() { lastModified(|<scheme>://baz/x/y/z|); });
        assert throwsUnsupportedAuthority(value() { lastModified(|<scheme>://qux/|); });
        assert throwsUnsupportedAuthority(value() { lastModified(|<scheme>://qux/x/y/z|); });
        assert throwsUnsupportedAuthority(value() { lastModified(|<scheme>:///|); });
        assert throwsUnsupportedAuthority(value() { lastModified(|<scheme>:///x/y/z|); });

        // Register default authority
        registerLocations(scheme, "", (
            |<scheme>://baz/|: |<scheme>://bar/|,
            |<scheme>://baz/x/y/z|: |file:///x/y/z|
        ));

        assert lastModified(|<scheme>://foo/|) == lastModified(|file:///|);
        assert lastModified(|<scheme>://bar/|) == lastModified(|file:///|);
        assert lastModified(|<scheme>://baz/|) == lastModified(|file:///|);

        assert throwsExceptionDownstream(value() { lastModified(|<scheme>://foo/x/y/z|); });
        assert throwsExceptionDownstream(value() { lastModified(|<scheme>://bar/x/y/z|); });
        assert throwsExceptionDownstream(value() { lastModified(|<scheme>://baz/x/y/z|); });
        assert throwsUnsupportedAuthority(value() { lastModified(|<scheme>://qux/|); });
        assert throwsUnsupportedAuthority(value() { lastModified(|<scheme>://qux/x/y/z|); });
        assert throwsExceptionDownstream(value() { lastModified(|<scheme>:///|); });
        assert throwsExceptionDownstream(value() { lastModified(|<scheme>:///x/y/z|); });

        return true;
    }
    catch false: // Catch block only to make finally block grammatical
        throw false;
    finally  {
        unregisterLocations(scheme, "foo");
        unregisterLocations(scheme, "bar");
        unregisterLocations(scheme, "");
    }
}

test bool testFileCopyCompletely() {
    writeFile(|tmp:///longFile|, "123456789");
    writeFile(|tmp:///shortFile|, "321");

    copy(|tmp:///shortFile|, |tmp:///longFile|, overwrite=true);

    return readFile(|tmp:///longFile|) == readFile(|tmp:///shortFile|);
}

test bool testFileCopyRecursive() {
    writeFile(|tmp:///a/b/c/d/longFile|, "123456789");
    writeFile(|tmp:///a/b/e/shortFile|, "321");
    copy(|tmp:///a/|, |tmp:///g/|, recursive=true, overwrite=true);
    return readFile(|tmp:///a/b/c/d/longFile|) == readFile(|tmp:///g/b/c/d/longFile|);
}

test bool watchDoesNotCrashOnURIRewrites() {
    writeFile(|tmp:///watchDoesNotCrashOnURIRewrites/someFile.txt|, "123456789");
    watch(|tmp:///watchDoesNotCrashOnURIRewrites|, true, void (FileSystemChange event) {
        // this should trigger the failing test finally
        remove(event.file);
    });
    return true;
}

test bool createdDoesNotCrashOnURIRewrites() {
    loc l = |tmp:///createdDoesNotCrashOnURIRewrites/someFile.txt|;
    remove(l);  // remove the file if it exists
    writeFile(l, "123456789");
    return IO::created(l) <= now();
}

test bool testWriteBase32() {
    str original = "Hello World!";
    writeBase32(|memory:///base32Test/writeTest.txt|, toBase32(original));
    return original == readFile(|memory:///base32Test/writeTest.txt|);
}

test bool testReadBase32() {
    str original = "Hello World!";
    writeFile(|memory:///base32Test/readTest.txt|, original);
    str encoded = readBase32(|memory:///base32Test/readTest.txt|);
    return original == fromBase32(encoded);
}

test bool testRenameWithinFileScheme() {
    remove(|tmp:///bye.txt|);
    writeFile(|tmp:///hello.txt|, "Hello World!");
    rename(|tmp:///hello.txt|, |tmp:///bye.txt|);
    return readFile(|tmp:///bye.txt|) == "Hello World!";
}

test bool testRenameWithinMemoryScheme() {
    remove(|memory:///bye.txt|);
    writeFile(|memory://testRename/hello.txt|, "Hello World!");
    rename(|memory://testRename/hello.txt|, |memory:///bye.txt|);
    return readFile(|memory:///bye.txt|) == "Hello World!";
}

test bool testRenameCrossScheme() {
    remove(|tmp:///bye.txt|);
    writeFile(|memory://testRename/hello.txt|, "Hello World!");
    rename(|memory://testRename/hello.txt|, |tmp:///bye.txt|);
    return readFile(|tmp:///bye.txt|) == "Hello World!";
}

test bool renameDirectory() {
    remove(|tmp:///RenamedFolder|, recursive=true);
    writeFile(|tmp:///Folder/hello.txt|, "Hello World!");
    rename(|tmp:///Folder|, |tmp:///RenamedFolder|);
    return readFile(|tmp:///RenamedFolder/hello.txt|) == "Hello World!";
}
