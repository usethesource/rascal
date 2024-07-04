module lang::rascal::tests::library::IO

import IO;
import DateTime;
import String;

test bool testFileCopyCompletely() {
    writeFile(|memory:///longFile|, "123456789");
    writeFile(|memory:///shortFile|, "321");

    copy(|memory:///shortFile|, |memory:///longFile|, overwrite=true);

    return readFile(|memory:///longFile|) == readFile(|memory:///shortFile|);
}

test bool watchDoesNotCrashOnURIRewrites() {
    writeFile(|memory:///watchDoesNotCrashOnURIRewrites/someFile.txt|, "123456789");
    watch(|memory:///watchDoesNotCrashOnURIRewrites|, true, void (LocationChangeEvent event) { 
        // this should trigger the failing test finally
        remove(event.src); 
    });
    return true;
}

test bool createdDoesNotCrashOnURIRewrites() {
    writeFile(|memory:///createdDoesNotCrashOnURIRewrites/someFile.txt|, "123456789");
    return created(|memory:///createdDoesNotCrashOnURIRewrites/someFile.txt|) <= now();
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
