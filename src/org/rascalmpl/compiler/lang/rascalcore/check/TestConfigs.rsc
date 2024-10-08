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
loc RASCAL_JAR  = |jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/rascal/0.40.8/rascal-0.40.8.jar!/|;
loc TYPEPAL_JAR = |jar+file:///Users/paulklint/.m2/repository/org/rascalmpl/typepal/0.14.1/typepal-0.14.1.jar!/|;
 
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

// --- all source ------------------------------------------------------------

public PathConfig getAllSrcPathConfig() {
   return pathConfig(   
        srcs = [ REPO + "rascal/src/org/rascalmpl/library", 
                 REPO + "rascal/test/org/rascalmpl/benchmark/",
                 REPO + "rascal-core/src/org/rascalmpl/core/library",
                 REPO + "typepal/src"
                 ],
        bin = REPO + "generated-sources/target/rascal/classes",
        generatedSources = REPO + "generated-sources/target/rascal/generated-sources/src/main/java/",
        generatedTestSources = REPO + "generated-sources/target/rascal/generated-sources/src/main/java/",
        resources = REPO + "generated-sources/target/rascal/generated-resources/src/main/java/",
        libs = [ ]
    );
}

public RascalCompilerConfig getAllSrcCompilerConfig(){
    return rascalCompilerConfig(getAllSrcPathConfig())[verbose = true][forceCompilationTopModule = true][logWrittenFiles=true];
}
// ---- rascal ----------------------------------------------------------------

public PathConfig getRascalPathConfig() {
   return pathConfig(   
        srcs = [ REPO + "rascal/src/org/rascalmpl/library", REPO + "rascal/test/org/rascalmpl/benchmark/" ],
        bin = REPO + "generated-sources/target/rascal/classes",
        generatedSources = REPO + "generated-sources/target/rascal/generated-sources/src/main/java/",
        generatedTestSources = REPO + "generated-sources/target/rascal/generated-sources/src/main/java/",
        resources = REPO + "generated-sources/target/rascal/generated-resources/src/main/java/",
        libs = [ ]
    );
}

public RascalCompilerConfig getRascalCompilerConfig(){
    return rascalCompilerConfig(getRascalPathConfig())[verbose = true][forceCompilationTopModule = true][logWrittenFiles=true];
}

// ---- rascal-core -----------------------------------------------------------

@synopsis{PathConfig for type-checking test modules in the rascal-core project}
@description{
* sources have to be in `|project://rascal-core/src/org/rascalmpl/core/library|`
* binaries will be stored the target folder of the rascal-core project
* has the standard library and typepal on the library path, in case you accidentally want to test a module in rascal-core which depends on typepal.
}
public PathConfig getRascalCorePathConfig() {
   return pathConfig(   
        srcs = [ REPO + "rascal-core/src/org/rascalmpl/core/library" ],
        bin = |project://rascal-core/target/test-classes|,
        bin = REPO + "generated-sources/target/rascal-core/classes",
        generatedSources = REPO + "generated-sources/target/rascal-core/generated-sources/src/main/java/",
        generatedTestSources = REPO + "generated-sources/target/rascal-core/generated-sources/src/main/java/",
        resources = REPO + "generated-sources/target/rascal-core/generated-resources/src/main/java/",
        libs = [ RASCAL_JAR, TYPEPAL_JAR ]
    );
}

public RascalCompilerConfig getRascalCoreCompilerConfig(PathConfig pcfg){
    return rascalCompilerConfig(pcfg)[verbose = true][forceCompilationTopModule = true][logWrittenFiles=true];
}

public RascalCompilerConfig getRascalCoreCompilerConfig(){
    return rascalCompilerConfig(getRascalCorePathConfig())[verbose = true][forceCompilationTopModule = true][logWrittenFiles=true];
}

@synopsis{Developers version: PathConfig for type-checking modules in other (named) Rascal projects}
@description{
* sources have to be in `|project://rascal-core/src/org/rascalmpl/core/library|`
* binaries will be stored the target folder of the rascal-core project
* has the standard library and typepal on the library path, in case you accidentally want to test a module in rascal-core which depends on typepal.
* Included projects: rascal-tutor, flybytes, rascal-lsp
}
public PathConfig getRascalCorePathConfigDev() {
   return pathConfig(   
        srcs = [ REPO + "rascal-core/src" ],
        bin = REPO + "generated-sources/target/rascal-core/classes",
        generatedSources = REPO + "generated-sources/target/rascal-core/generated-sources/src/main/java/",
        generatedTestSources = REPO + "generated-sources/target/rascal-core/generated-sources/src/main/java/",
        resources = REPO + "generated-sources/target/rascal-core/generated-resources/src/main/java/",
         libs = [ RASCAL_JAR, TYPEPAL_JAR ]
    );
}

public RascalCompilerConfig getRascalCoreCompilerConfigDev(){
    return rascalCompilerConfig(getRascalCorePathConfigDev())[verbose = true][forceCompilationTopModule = true][logWrittenFiles=true];
}

public RascalCompilerConfig getRascalCoreCompilerConfigDev(PathConfig pcfg){
    return rascalCompilerConfig(pcfg);
}

// ---- typepal ---------------------------------------------------------------

public PathConfig getTypePalProjectPathConfig() {
    return pathConfig(   
        srcs = [ REPO + "typepal/src" ],
        bin = REPO + "generated-sources/target/typepal/classes",
        generatedSources = REPO + "generated-sources/target/typepal/generated-sources/src/main/java/",
        generatedTestSources = REPO + "generated-sources/target/typepal/generated-sources/src/main/java/",
        resources = REPO + "generated-sources/target/typepal/generated-resources/src/main/java/",
        libs = [ RASCAL_JAR ]
    );
}

public RascalCompilerConfig getTypePalCompilerConfig(PathConfig pcfg){
    return rascalCompilerConfig(pcfg)[verbose = true][forceCompilationTopModule = false][logWrittenFiles=true];
}

public RascalCompilerConfig getTypePalCompilerConfig(){
    return rascalCompilerConfig(getTypePalProjectPathConfig())[verbose = true][forceCompilationTopModule = false][logWrittenFiles=true];
}

// ---- flybytes --------------------------------------------------------------

public PathConfig getFlyBytesProjectPathConfig() {
    return pathConfig(   
        srcs = [ REPO + "flybytes/src" ],
        bin = REPO + "generated-sources/target/flybytes/classes",
        generatedSources = REPO + "generated-sources/target/flybytes/generated-sources/src/main/java/",
        generatedTestSources = REPO + "generated-sources/target/flybytes/generated-sources/src/main/java/",
        resources = REPO + "generated-sources/target/flybytes/generated-resources/src/main/java/",
        libs = [ RASCAL_JAR ]
    );
}

public RascalCompilerConfig getFlyBytesCompilerConfig(PathConfig pcfg){
    return rascalCompilerConfig(pcfg)[verbose = true][forceCompilationTopModule = false][logWrittenFiles=true];
}

public RascalCompilerConfig getFlyBytesCompilerConfig(){
    return rascalCompilerConfig(getFlyBytesProjectPathConfig());
}

// ---- salix -----------------------------------------------------------------

public PathConfig getSalixPathConfig() {
    return pathConfig(   
        srcs = [ REPO + "salix-core/src/main/rascal", REPO + "salix-contrib/src/main/rascal" ],
        bin = REPO + "generated-sources/target/salix/classes",
        generatedSources = REPO + "generated-sources/target/salix/generated-sources/src/main/java/",
        generatedTestSources = REPO + "generated-sources/target/salix/generated-sources/src/main/java/",
        resources = REPO + "generated-sources/target/salix/generated-resources/src/main/java/",
        libs = [ RASCAL_JAR ]
    );
}

public RascalCompilerConfig getSalixCompilerConfig(PathConfig pcfg){
    return rascalCompilerConfig(pcfg)[verbose = true][forceCompilationTopModule = false][logWrittenFiles=true];
}

public RascalCompilerConfig getSalixCompilerConfig(){
    return rascalCompilerConfig(getSalixPathConfig());
}

// ---- drambiguity -----------------------------------------------------------

public PathConfig getDrAmbiguityPathConfig() {
    return pathConfig(   
        srcs = [ REPO + "drambiguity/src", REPO + "salix-core/src/main/rascal" ],
        bin = REPO + "generated-sources/target/drambiguity/classes",
        generatedSources = REPO + "generated-sources/target/drambiguity/generated-sources/src/main/java/",
        generatedTestSources = REPO + "generated-sources/target/drambiguity/generated-sources/src/main/java/",
        resources = REPO + "generated-sources/target/drambiguity/generated-resources/src/main/java/",
        libs = [ RASCAL_JAR ]
    );
}

public RascalCompilerConfig getDrAmbiguityCompilerConfig(PathConfig pcfg){
    return rascalCompilerConfig(pcfg)[verbose = true][forceCompilationTopModule = false][logWrittenFiles=true];
}

public RascalCompilerConfig getDrAmbiguityCompilerConfig(){
    return rascalCompilerConfig(getDrAmbiguityPathConfig());
}

// ---- rascal-language-server ------------------------------------------------

public PathConfig getLSPPathConfig() {
    return pathConfig(   
        srcs = [ REPO + "rascal-lsp/src/main/rascal", REPO + "rascal-lsp/src/test/rascal"],
        bin = REPO + "generated-sources/target/rascal-lsp/classes",
        generatedSources = REPO + "generated-sources/target/rascal-lsp/generated-sources/src/main/java/",
        generatedTestSources = REPO + "generated-sources/target/rascal-lsp/generated-sources/src/main/java/",
        resources = REPO + "generated-sources/target/rascal-lsp/generated-resources/src/main/java/",
        libs = [ RASCAL_JAR ]
    );
}

public RascalCompilerConfig getLSPCompilerConfig(PathConfig pcfg){
    return rascalCompilerConfig(pcfg)[verbose = true][forceCompilationTopModule = false][logWrittenFiles=true];
}

public RascalCompilerConfig getLSPCompilerConfig(){
    return rascalCompilerConfig(getLSPPathConfig());
}

// ---- php-analysis -----------------------------------------------------------

public PathConfig getPHPPathConfig() {
    return pathConfig(   
        srcs = [ REPO + "php-analysis/src/main/rascal", REPO + "php-analysis/src/test/rascal"],
        bin = REPO + "generated-sources/target/php-analysis/classes",
        generatedSources = REPO + "generated-sources/target/php-analysis/generated-sources/src/main/java/",
        generatedTestSources = REPO + "generated-sources/target/php-analysis/generated-sources/src/main/java/",
        resources = REPO + "generated-sources/target/php-analysis/generated-resources/src/main/java/",
        libs = [ RASCAL_JAR ]
    );
}

public RascalCompilerConfig getPHPCompilerConfig(PathConfig pcfg){
    return rascalCompilerConfig(pcfg)[verbose = true][forceCompilationTopModule = false][logWrittenFiles=true];
}

public RascalCompilerConfig getPHPCompilerConfig(){
    return rascalCompilerConfig(getPHPPathConfig());
}
