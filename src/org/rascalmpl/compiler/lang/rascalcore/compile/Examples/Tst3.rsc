module lang::rascalcore::compile::Examples::Tst3
import lang::rascalcore::check::Checker;
import lang::rascalcore::check::BasicRascalConfig;
import lang::rascalcore::check::RascalConfig;
import IO;
import Message;
import util::FileSystem;
import util::Reflective;

tuple[PathConfig, RascalCompilerConfig] testConfigs(loc projectPath) {
    pcfg = pathConfig(
        bin=projectPath + "bin",
        libs=[|jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/rascal/0.40.8/rascal-0.40.8.jar!/|],
        srcs=[projectPath + "src"],
        resources=projectPath + "resources",
        generatedSources=projectPath + "generated-sources"
    );

    RascalCompilerConfig ccfg = rascalCompilerConfig(pcfg)
        [forceCompilationTopModule = true]
        [verbose = true];

    return <pcfg, ccfg>;
}

void checkModule(loc projectPath, str moduleName, str moduleBody, RascalCompilerConfig ccfg) {
    modulePath = projectPath + "src" + "<moduleName>.rsc";
    writeFile(modulePath, moduleBody);
    msgs = check([modulePath], ccfg);
    iprintln(msgs);
}

void sameNameImportResolutionBug(loc projectPath = |memory:///TestModule|, bool skipFirst = false) {
    remove(projectPath);
    <pcfg, ccfg> = testConfigs(projectPath);

    if (!skipFirst) {
        println("*** Checking first version:");
        checkModule(projectPath, "A", "module A", ccfg);
    }

    println("*** Checking second version:");

    // Unexpected 'undefined module' error
    // Only occurs if a TPL without that import already exists
    checkModule(projectPath, "A", "module A\nimport Exception;", ccfg);
}