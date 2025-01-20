module lang::rascalcore::check::TestConfigs

import util::Reflective;
import lang::rascalcore::check::BasicRascalConfig;
import lang::rascalcore::check::RascalConfig;

// Duplicate in lang::rascalcore::compile::util::Names, factor out
data PathConfig(
    loc generatedSources=|unknown:///|,
    loc generatedTestSources=|unknown:///|,
    loc resources = |unknown:///|,
    loc testResources =|unknown:///|
);

// ----  Various PathConfigs  ---------------------------------------------

loc REPO =        |file:///Users/paulklint/git/|;
loc RASCAL_JAR  = |jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/rascal/0.40.17/rascal-0.40.17.jar!/|;
loc TYPEPAL_JAR = |jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/typepal/0.14.8/typepal-0.14.8.jar!/|;
loc OUTDATED_TYPEPAL_JAR = |jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/typepal/0.14.1/typepal-0.14.1.jar!/|;

// ---- PathConfigs for testing purposes --------------------------------------

private int npc = 0;
@synopsis{PathConfig for testing generated modules in |memory://test-modules/| in memory file system, not depending on any outside libraries.}
@description{
* gets source files exclusively from |memory://test-modules/| and |std:///| (for library code)
* generates bin files in the in-memory file system
}
public PathConfig getDefaultTestingPathConfig() {
    npc += 1;
    snpc = "<npc>";
    return pathConfig(
        srcs = [ |memory:///test-modules/|, |std:///|  ],
        bin = |memory:///test-modules/rascal-core-tests-bin-<snpc>|,
        generatedSources = |memory:///test-modules/generated-test-sources-<snpc>|,
        resources = |memory:///test-modules/generated-test-resources-<snpc>|,
        libs = [ ]
    );
}

@synopsis{PathConfig for testing generated modules in |memory://test-modules/| in memory file system, dependent on a previously released standard library}
@description{
* gets source files exclusively from |memory://test-modules/|
* generates bin files in the in-memory file system
* depends only on the pre-compiled standard library from the rascal project
}
public PathConfig getReleasedStandardLibraryTestingPathConfig() {
    npc += 1;
    snpc = "<npc>";
    return pathConfig(
        srcs = [ |memory:///test-modules/| ],
        bin = |memory:///test-modules/rascal-core-tests-bin-<snpc>|,
        generatedSources = |memory:///test-modules/generated-test-sources-<snpc>|,
        resources = |memory:///test-modules/generated-test-resources-<snpc>|,
        libs = [ |lib://rascal| ]
    );
}

// ---- testing rascal standard library ---------------------------------------

@synopsis{a path config for testing type-checking of the standard library in the rascal project}
public PathConfig getRascalProjectTestingPathConfig() {
    npc += 1;
    snpc = "<npc>";
    return pathConfig(
        srcs = [|project://rascal/src/org/rascalmpl/library|],
        bin = |memory:///test-modules/rascal-lib-bin-<snpc>|,
        libs = []
    );
}

// ---- generic template for PathConfigs --------------------------------------

public PathConfig makePathConfig(list[loc] sources, list[loc] libraries) {
    return pathConfig(
        srcs = sources,
        bin = REPO + "compiled-rascal/target/classes",
        generatedSources = REPO + "compiled-rascal/src/main/java",
        generatedTestSources = REPO + "compiled-rascal/src/test/java/",
        resources = REPO + "compiled-rascal/src/main/java",
        testResources = REPO + "compiled-rascal/src/test/java",
        libs = libraries
    );
}

// --- all source ------------------------------------------------------------

public PathConfig getAllSrcPathConfig() {
    return makePathConfig([ REPO + "rascal/src/org/rascalmpl/library",
                            REPO + "rascal/test/org/rascalmpl/benchmark/",
                            REPO + "rascal-core/src/org/rascalmpl/core/library",
                            REPO + "typepal/src"
                        ],
                          [ ]);
}

public RascalCompilerConfig getAllSrcCompilerConfig(){
    return rascalCompilerConfig(getAllSrcPathConfig())[verbose = true][logWrittenFiles=true];
}

// ---- rascal ----------------------------------------------------------------

public PathConfig getRascalPathConfig() {
    return makePathConfig([ REPO + "rascal/src/org/rascalmpl/library", 
                            REPO + "rascal/test/org/rascalmpl/benchmark/" ],
                          [ ]);
}

public RascalCompilerConfig getRascalCompilerConfig(){
    return rascalCompilerConfig(getRascalPathConfig())[verbose = true][logWrittenFiles=true];
}

// ---- rascal-core -----------------------------------------------------------

@synopsis{PathConfig for type-checking test modules in the rascal-core project}
@description{
* sources have to be in `|project://rascal-core/src/org/rascalmpl/core/library|`
* binaries will be stored the target folder of the rascal-core project
* has the standard library and typepal on the library path, in case you accidentally want to test a module in rascal-core which depends on typepal.
}
public PathConfig getRascalCorePathConfig() {
   return makePathConfig([ REPO + "rascal-core/src/org/rascalmpl/core/library" ],
                          [ RASCAL_JAR, TYPEPAL_JAR ]);
}

public RascalCompilerConfig getRascalCoreCompilerConfig(){
    return rascalCompilerConfig(getRascalCorePathConfig())[verbose = true][logWrittenFiles=true];
}

@synopsis{Developers version: PathConfig for type-checking modules in other (named) Rascal projects}
@description{
* sources have to be in `|project://rascal-core/src/org/rascalmpl/core/library|`
* binaries will be stored the target folder of the rascal-core project
* has the standard library and typepal on the library path, in case you accidentally want to test a module in rascal-core which depends on typepal.
* Included projects: rascal-tutor, flybytes, rascal-lsp
}
public PathConfig getRascalCorePathConfigDev() {
    return makePathConfig([ REPO + "rascal-core/src" ],
                          [ RASCAL_JAR, TYPEPAL_JAR ]);
}

public RascalCompilerConfig getRascalCoreCompilerConfigDev(){
    return rascalCompilerConfig(getRascalCorePathConfigDev())[verbose = true][logWrittenFiles=true];
}

// ---- typepal ---------------------------------------------------------------

public PathConfig getTypePalProjectPathConfig() {
   return makePathConfig([ REPO + "typepal/src" ],
                          [ RASCAL_JAR ]);
}

public RascalCompilerConfig getTypePalCompilerConfig(){
    return rascalCompilerConfig(getTypePalProjectPathConfig())[verbose = true][logWrittenFiles=true];
}

// ---- flybytes --------------------------------------------------------------

public PathConfig getFlyBytesProjectPathConfig() {
    return makePathConfig([ REPO + "flybytes/src" ],
                          [ RASCAL_JAR ]);
}

public RascalCompilerConfig getFlyBytesCompilerConfig(){
    return rascalCompilerConfig(getFlyBytesProjectPathConfig())[verbose = true][logWrittenFiles=true];
}

// ---- salix -----------------------------------------------------------------

public PathConfig getSalixPathConfig() {
    return makePathConfig([ REPO + "salix-core/src/main/rascal", REPO + "salix-contrib/src/main/rascal" ],
                          [ RASCAL_JAR ]);
}

public RascalCompilerConfig getSalixCompilerConfig(){
    return rascalCompilerConfig(getSalixPathConfig())[verbose = true][logWrittenFiles=true];
}

// ---- drambiguity -----------------------------------------------------------

public PathConfig getDrAmbiguityPathConfig() {
    return makePathConfig([ REPO + "drambiguity/src", REPO + "salix-core/src/main/rascal" ],
                          [ RASCAL_JAR ]);
}

public RascalCompilerConfig getDrAmbiguityCompilerConfig(){
    return rascalCompilerConfig(getDrAmbiguityPathConfig())[verbose = true][logWrittenFiles=true];
}

// ---- rascal-language-server ------------------------------------------------

public PathConfig getLSPPathConfig() {
    return makePathConfig([ REPO + "rascal-lsp/src/main/rascal", REPO + "rascal-lsp/src/test/rascal"],
                          [ RASCAL_JAR ]);
}

public RascalCompilerConfig getLSPCompilerConfig(){
    return rascalCompilerConfig(getLSPPathConfig())[verbose = true][logWrittenFiles=true];
}

// ---- php-analysis -----------------------------------------------------------

public PathConfig getPHPPathConfig() {
    return makePathConfig([ REPO + "php-analysis/src/main/rascal", REPO + "php-analysis/src/test/rascal"],
                          [ RASCAL_JAR ]);
}

public RascalCompilerConfig getPHPCompilerConfig(){
    return rascalCompilerConfig(getPHPPathConfig())[verbose = true][logWrittenFiles=true];
}

// ---- VSCode-----------------------------------------------------------------

public PathConfig getVSCodePathConfig() {
    return 
        pathConfig(
        ignores=[],
        javaCompilerPath=[],
        bin=|target://rascal-core|,
        classloaders=[],
        libs=[
            |lib://rascal|,
            |target://typepal|   ],
        srcs=[|file:///Users/paulklint/git/rascal-core/src/org/rascalmpl/core/library|]);
}

public RascalCompilerConfig getVSCodeCompilerConfig(){
    return rascalCompilerConfig(getVSCodePathConfig())[verbose = true][logWrittenFiles=true];
}

// ---- Outdated TypePal Usage -----------------------------------------------------------------

public PathConfig getOutdatedTPLPathConfig() {
    return makePathConfig([REPO + "rascal-core/src/org/rascalmpl/core/library"],
                          [ RASCAL_JAR, OUTDATED_TYPEPAL_JAR ]);
}

public RascalCompilerConfig getOutdatedTPLCompilerConfig(){
    return rascalCompilerConfig(getOutdatedTPLPathConfig())[verbose = true][logWrittenFiles=true];
}