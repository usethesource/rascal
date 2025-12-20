module lang::rascal::tests::libraries::IO

import IO;
import DateTime;
import String;

test bool testFileCopyCompletely() {
    writeFile(|tmp:///longFile|, "123456789");
    writeFile(|tmp:///shortFile|, "321");

    copy(|tmp:///shortFile|, |tmp:///longFile|, overwrite=true);

    return readFile(|tmp:///longFile|) == readFile(|tmp:///shortFile|);
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