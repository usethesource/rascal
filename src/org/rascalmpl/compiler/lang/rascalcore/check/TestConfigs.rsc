module lang::rascalcore::check::TestConfigs

import IO;
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

loc RASCAL        = |mvn://org.rascalmpl!rascal!0.40.17/!|;
loc TYPEPAL       = |mvn://org.rascalmpl!typepal!0.14.8/!|;
loc OUTDATED_TYPEPAL 
                  = |mvn://org.rascalmpl!typepal!0.14.1/!|;
loc RASCAL_CORE   = |mvn://org.rascalmpl!rascal-core!0.12.14/!|;

loc DRAMBIGUITY   = |mvn://org.rascalmpl!drambiguity!0.1.2/!|;
loc FLYBYTES      = |mvn://org.rascalmpl!flybytes!0.1.5/!|;
loc SALIX_CORE    = |mvn://org.rascalmpl!salix-core!0.2.7/!|;
loc SALIX_CONTRIB = |mvn://org.rascalmpl!salix-contrib!0.2.7/!|;
loc RASCAL_LSP    = |mvn://org.rascalmpl!rascal-language-server!0.12.1/!|;
loc PHP_ANALYSIS  = |mvn://org.rascalmpl!php-analysis!0.2.5-SNAPSHOT/!|;

loc REPO          =  |file:///Users/paulklint/git/|;
loc COMPILED_RASCAL 
                  =  REPO + "compiled-rascal";
loc TMP_COMPILED_RASCAL 
                  = |tmp:///compiled-rascal/|;

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

// Given a list of sources and of libraries generate a PathConfig.
// The keep parameter determines whether the generated sources and binaries are kept in a temporary directory or not.
// If keep is true, the generated sources and binaries are stored in a temporary directory, otherwise they are stored in the source directory.

public PathConfig makePathConfig(list[loc] sources, list[loc] libraries, bool keep = false) {
   COMPILED = keep ? COMPILED_RASCAL : TMP_COMPILED_RASCAL;
   return pathConfig(
        srcs                 = sources,
        bin                  = COMPILED + "/target/classes",
        generatedSources     = COMPILED + "/src/main/java",
        generatedTestSources = COMPILED + "/src/test/java/",
        resources            = COMPILED + "/src/main/java",
        testResources        = COMPILED_RASCAL + "/src/test/java/",
        libs                 = libraries
        // srcs                 = sources,
        // bin                  = keep ? COMPILED_RASCAL + "/target/classes" : repo,
        // generatedSources     = keep ? COMPILED_RASCAL + "/src/main/java" : |unknown:///|,
        // generatedTestSources = keep ? COMPILED_RASCAL + "/src/test/java/" : |unknown:///|,
        // resources            = keep ? COMPILED_RASCAL + "/src/main/java" : repo + "/rascal",
        // testResources        = keep ? COMPILED_RASCAL + "/src/test/java/" : repo + "/rascal",
        // libs                 = libraries
    ); 
}

// --- all source ------------------------------------------------------------

public PathConfig getAllSrcPathConfig(bool keep = false) {
    return makePathConfig([ RASCAL + "org/rascalmpl/library",
                            RASCAL + "org/rascalmpl/benchmark/",
                            RASCAL_CORE + "org/rascalmpl/core/library",
                            TYPEPAL
                        ],
                        [ ], 
                        keep=keep);
}

public RascalCompilerConfig getAllSrcCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getAllSrcPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ----
public PathConfig getAllSrcREPOPathConfig(bool keep = false) {
    return makePathConfig([ REPO + "rascal/src/org/rascalmpl/library",
                            REPO + "rascal/test/org/rascalmpl/benchmark/",
                            REPO + "rascal-core/src/org/rascalmpl/core/library",
                            REPO + "typepal/src"
                        ],
                        [ ], 
                        keep=keep);
}

public RascalCompilerConfig getAllSrcREPOCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getAllSrcREPOPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}
// ----
public PathConfig getAllSrcWritablePathConfig(bool keep = false) {
    TMP_RASCAL = |tmp:///rascal/|;
    TMP_RASCAL_CORE = |tmp:///rascal-core/|;
    TMP_TYPEPAL = |tmp:///typepal/|;
    copy(RASCAL, TMP_RASCAL, recursive=true, overwrite=true);
    copy(RASCAL_CORE, TMP_RASCAL_CORE, recursive=true, overwrite=true);
    copy(TYPEPAL, TMP_TYPEPAL, recursive=true, overwrite=true);
    return makePathConfig([ TMP_RASCAL + "org/rascalmpl/library",
                            TMP_RASCAL + "org/rascalmpl/benchmark/",
                            TMP_RASCAL_CORE + "org/rascalmpl/core/library",
                            TMP_TYPEPAL
                        ],
                        [ ], 
                        keep=keep);
}

public RascalCompilerConfig getAllSrcWritableCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getAllSrcWritablePathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ---- rascal ----------------------------------------------------------------
public PathConfig getRascalPathConfig(bool keep = false) {
    return makePathConfig([ RASCAL + "org/rascalmpl/library"
                          //, RASCAL + "test/org/rascalmpl/benchmark/"
                          ],
                          [], 
                          keep=keep);
}

public RascalCompilerConfig getRascalCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getRascalPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

public PathConfig getRascalWritablePathConfig(bool keep = false) {
    TMP_RASCAL = |tmp:///rascal/|;
    copy(RASCAL, TMP_RASCAL, recursive=true, overwrite=true);
    return makePathConfig([ TMP_RASCAL + "org/rascalmpl/library"
                          //, RASCAL + "test/org/rascalmpl/benchmark/"
                          ],
                          [], 
                          keep=keep);
}

public RascalCompilerConfig getRascalWritableCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getRascalWritablePathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ---- rascal-core -----------------------------------------------------------

@synopsis{PathConfig for type-checking test modules in the rascal-core project}
@description{
* sources have to be in `|project://rascal-core/src/org/rascalmpl/core/library|`
* binaries will be stored the target folder of the rascal-core project
* has the standard library and typepal on the library path, in case you accidentally want to test a module in rascal-core which depends on typepal.
}
public PathConfig getRascalCorePathConfig(bool keep = false) {
   return makePathConfig([ RASCAL_CORE + "org/rascalmpl/core/library" ],
                         [ RASCAL, TYPEPAL ], 
                         keep=keep);
}

public RascalCompilerConfig getRascalCoreCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getRascalCorePathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

@synopsis{Developers version: PathConfig for type-checking modules in other (named) Rascal projects}
@description{
* sources have to be in `|project://rascal-core/src/org/rascalmpl/core/library|`
* binaries will be stored the target folder of the rascal-core project
* has the standard library and typepal on the library path, in case you accidentally want to test a module in rascal-core which depends on typepal.
* Included projects: rascal-tutor, flybytes, rascal-lsp
}
public PathConfig getRascalCorePathConfigDev(bool keep = false) {
    return makePathConfig([ RASCAL_CORE ], [ RASCAL, TYPEPAL ], keep=keep);
}

public RascalCompilerConfig getRascalCoreCompilerConfigDev(bool keep=true){
    return rascalCompilerConfig(getRascalCorePathConfigDev())[verbose = true][logWrittenFiles=true];
}

// ---- typepal ---------------------------------------------------------------

public PathConfig getTypePalProjectPathConfig(bool keep = false) {
   return makePathConfig([ TYPEPAL  ], [ RASCAL ], keep=keep);
}

public RascalCompilerConfig getTypePalCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getTypePalProjectPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ---- flybytes --------------------------------------------------------------

public PathConfig getFlyBytesProjectPathConfig(bool keep = false) {
    return makePathConfig([ FLYBYTES ], [ RASCAL ], keep=keep);
}

public RascalCompilerConfig getFlyBytesCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getFlyBytesProjectPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ---- salix -----------------------------------------------------------------

public PathConfig getSalixPathConfig(bool keep = false) {
    return makePathConfig([ SALIX_CORE + "src/main/rascal", SALIX_CONTRIB + "src/main/rascal" ],
                          [ RASCAL ], 
                          keep=keep);
}

public RascalCompilerConfig getSalixCompilerConfig(bool keep = false){
    return rascalCompilerConfig(getSalixPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ---- drambiguity -----------------------------------------------------------

public PathConfig getDrAmbiguityPathConfig(bool keep = false) {
    return makePathConfig([ DRAMBIGUITY, SALIX_CORE + "src/main/rascal" ],
                          [ RASCAL ],
                          keep=keep);
}

public RascalCompilerConfig getDrAmbiguityCompilerConfig(bool keep = false){
    return rascalCompilerConfig(getDrAmbiguityPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ---- rascal-language-server ------------------------------------------------

public PathConfig getLSPPathConfig(bool keep = false) {
    return makePathConfig([ RASCAL_LSP + "src/main/rascal", RASCAL_LSP + "src/test/rascal"],
                          [ RASCAL ],
                          keep=keep);
}

public RascalCompilerConfig getLSPCompilerConfig(bool keep = false){
    return rascalCompilerConfig(getLSPPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ---- php-analysis -----------------------------------------------------------

public PathConfig getPHPPathConfig(bool keep = false) {
    return makePathConfig([ PHP_ANALYSIS + "src/main/rascal", PHP_ANALYSIS + "src/test/rascal"],
                          [ RASCAL ], 
                          keep=keep);
}

public RascalCompilerConfig getPHPCompilerConfig(bool keep = false){
    return rascalCompilerConfig(getPHPPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ---- VSCode-----------------------------------------------------------------

public PathConfig getVSCodePathConfig() {
    VS_RASCAL_JAR = |jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/rascal/0.41.0-RC10/rascal-0.41.0-RC10.jar!/|;
    VS_TYPEPAL_JAR = |jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/typepal/0.15.1-SNAPSHOT/typepal-0.15.1-SNAPSHOT.jar!/|;
    VS_RASCAL_CORE_JAR = |jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/rascal-core/0.12.14-SNAPSHOT/rascal-core-0.12.14-SNAPSHOT.jar!/|;
    return 
    pathConfig(
  ignores=[],
  resources=|file:///Users/paulklint/git/rascal-language-servers/rascal-lsp/target/classes/|,
  javaCompilerPath=[],
  bin=|file:///Users/paulklint/git/rascal-language-servers/rascal-lsp/target/classes/|,
  classloaders=[],
  generatedSources=|file:///Users/paulklint/git/rascal-language-servers/rascal-lsp/generated-sources|,
  libs=[
    |jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/rascal/0.41.0-RC10/rascal-0.41.0-RC10.jar!/|,
    |jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/rascal-core/0.12.13/rascal-core-0.12.13.jar!/|,
    |jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/typepal/0.15.0/typepal-0.15.0.jar!/|
  ],
  srcs=[|file:///Users/paulklint/git/rascal-language-servers/rascal-lsp/src/main/rascal/|]);
        // pathConfig(
        // bin = REPO + "compiled-rascal/target/classes",
        // generatedSources = REPO + "compiled-rascal/src/main/java",
        // generatedTestSources = REPO + "compiled-rascal/src/test/java/",
        // resources = REPO + "compiled-rascal/src/main/java",
        // testResources = REPO + "compiled-rascal/src/test/java",
        // libs=[VS_RASCAL_JAR, VS_TYPEPAL_JAR, VS_RASCAL_CORE_JAR],
        // srcs=[|file:///Users/paulklint/git/rascal-language-servers/rascal-lsp/src/main/rascal|]);
}

public RascalCompilerConfig getVSCodeCompilerConfig(){
    return rascalCompilerConfig(getVSCodePathConfig())[verbose = true][logWrittenFiles=true];
}

// ---- Outdated TypePal Usage -----------------------------------------------------------------

public PathConfig getOutdatedTPLPathConfig(bool keep = false) {
    return makePathConfig(RASCAL_CORE,
                          [RASCAL_CORE + "src/org/rascalmpl/core/library"],
                          [ RASCAL, OUTDATED_TYPEPAL ],
                          keep=keep);
}

public RascalCompilerConfig getOutdatedTPLCompilerConfig(bool keep = false){
    return rascalCompilerConfig(getOutdatedTPLPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}