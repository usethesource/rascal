module lang::rascal::tests::library::IO

import IO;
import DateTime;

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
    writeFile(|tmp:///createdDoesNotCrashOnURIRewrites/someFile.txt|, "123456789");
    return created(|tmp:///createdDoesNotCrashOnURIRewrites/someFile.txt|) < now();
}
