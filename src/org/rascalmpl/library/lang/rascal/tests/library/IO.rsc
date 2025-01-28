module lang::rascal::tests::library::IO

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
    watch(|tmp:///watchDoesNotCrashOnURIRewrites|, true, void (LocationChangeEvent event) { 
        // this should trigger the failing test finally
        remove(event.src); 
    });
    return true;
}

test bool createdDoesNotCrashOnURIRewrites() {
    loc l = |tmp:///createdDoesNotCrashOnURIRewrites/someFile.txt|;
    remove(l);  // remove the file if it exists
    writeFile(l, "123456789");
    return created(l) <= now();
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
