@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::check::TestConfigs

import IO;
import util::Reflective;
import lang::rascalcore::check::BasicRascalConfig;
import lang::rascalcore::check::RascalConfig;

import lang::rascalcore::check::ModuleLocations;
// import lang::rascalcore::CompilerPathConfig;

// Duplicate in lang::rascalcore::compile::util::Names, factor out
data PathConfig(
    loc generatedSources=|unknown:///|,
    loc generatedTestSources=|unknown:///|,
    loc generatedResources=|unknown:///|,
    loc generatedTestResources = |unknown:///|
);

// ----  Various PathConfigs  ---------------------------------------------
// use the `download-test-jars.sh` to download these dependencies 
// (and make sure to KEEP THE VERSIONS up to date)

public loc RASCAL       = |mvn://org.rascalmpl--rascal--0.41.0-RC15/|;
loc TYPEPAL             = |mvn://org.rascalmpl--typepal--0.14.8/|;
loc OUTDATED_TYPEPAL    = |mvn://org.rascalmpl--typepal--0.14.1/|;

loc DRAMBIGUITY         = |mvn://org.rascalmpl--drambiguity--0.1.2/|;
loc FLYBYTES            = |mvn://org.rascalmpl--flybytes--0.1.5/|;
loc SALIX_CORE          = |mvn://org.rascalmpl--salix-core--0.2.7/|;
loc SALIX_CONTRIB       = |mvn://org.rascalmpl--salix-contrib--0.2.7/|;
loc RASCAL_LSP          = |mvn://org.rascalmpl--rascal-lsp--2.21.2/|;
loc PHP_ANALYSIS        = |mvn://org.rascalmpl--php-analysis--0.2.5-SNAPSHOT/|;

loc REPO                =  |file:///Users/paulklint/git/|;
loc COMPILED_RASCAL     =  REPO + "compiled-rascal";
loc TMP_COMPILED_RASCAL = |tmp:///compiled-rascal/|;

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
        bin = |memory:///test-modules/rascal-tests-bin-<snpc>|,
        generatedSources = |memory:///test-modules/generated-test-sources-<snpc>|,
        generatedResources = |memory:///test-modules/generated-test-resources-<snpc>|,
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
        bin = |memory:///test-modules/rascal-tests-bin-<snpc>|,
        generatedSources = |memory:///test-modules/generated-test-sources-<snpc>|,
        generatedResources = |memory:///test-modules/generated-test-resources-<snpc>|,
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
        srcs                   = sources,
        bin                    = COMPILED + (keep ? "/target/classes" : "rascal"),
        generatedSources       = COMPILED + "/src/main/java",
        generatedTestSources   = COMPILED + "/src/test/java/",
        generatedResources     = COMPILED + (keep ? "/src/main/java" : "rascal"),
        generatedTestResources = COMPILED_RASCAL + (keep ? "/src/test/java/" : "rascal"),
        libs                   = libraries
    ); 
}

// --- all source ------------------------------------------------------------

public PathConfig getAllSrcPathConfig(bool keep = false) {
    return makePathConfig([ RASCAL + "org/rascalmpl/library",
                            RASCAL + "org/rascalmpl/benchmark/",
                            RASCAL + "org/rascalmpl/compiler",
                            TYPEPAL
                        ],
                        [ ], 
                        keep=keep);
}

public RascalCompilerConfig getAllSrcCompilerConfig(PathConfig pcfg){
    return rascalCompilerConfig(pcfg)[verbose = true][logWrittenFiles=true];
}

public RascalCompilerConfig getAllSrcCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getAllSrcPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ----
public PathConfig getAllSrcREPOPathConfig(bool keep = false) {
    LSP_REPO = REPO + "rascal-language-servers/rascal-lsp";
    return makePathConfig([ REPO + "rascal/src/org/rascalmpl/library",
                            REPO + "rascal/test/org/rascalmpl/benchmark/",
                            REPO + "rascal/src/org/rascalmpl/compiler",
                            REPO + "rascal/src/org/rascalmpl/tutor",
                            REPO + "typepal/src",
                            LSP_REPO + "src/main/rascal/library", 
                            LSP_REPO + "src/main/rascal/lsp"
                        ],
                        [ ], 
                        keep=keep);
}

public RascalCompilerConfig getAllSrcREPOCompilerConfig(PathConfig pcfg, bool keep=true){
    return rascalCompilerConfig(pcfg)[verbose = true][logWrittenFiles=true];
}

public RascalCompilerConfig getAllSrcREPOCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getAllSrcREPOPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}
// ----
public PathConfig getAllSrcWritablePathConfig(bool keep = false) {
    TMP_RASCAL = |tmp:///rascal/|;
    TMP_TYPEPAL = |tmp:///typepal/|;
    remove(TMP_RASCAL, recursive = true);
    remove(TMP_TYPEPAL, recursive = true);
    copy(RASCAL, TMP_RASCAL, recursive=true, overwrite=true);
    copy(TYPEPAL, TMP_TYPEPAL, recursive=true, overwrite=true);
    return makePathConfig([ TMP_RASCAL + "org/rascalmpl/library",
                            TMP_RASCAL + "org/rascalmpl/benchmark/",
                            TMP_RASCAL + "org/rascalmpl/compiler",
                            TMP_TYPEPAL
                        ],
                        [ ], 
                        keep=true);
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

public PathConfig getRascalAsLibPathConfig(bool keep = false) {
    return pathConfig(
        srcs = [  ],
        bin = RASCAL + "rascal",
        generatedSources = RASCAL,
        generatedResources = RASCAL + "rascal",
        libs = [ RASCAL]
    );
}

public PathConfig getRascalWritablePathConfig(bool keep = false) {
    TMP_RASCAL = |tmp:///rascal/|;
    remove(TMP_RASCAL, recursive = true);
    copy(RASCAL, TMP_RASCAL, recursive=true, overwrite=true);
    pcfg = makePathConfig([ TMP_RASCAL + "org/rascalmpl/library",
                            TMP_RASCAL + "org/rascalmpl/compiler"
                          ],
                          [], 
                          keep=keep);
    return pcfg;
}


public RascalCompilerConfig getRascalWritableCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getRascalWritablePathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ---- scratch --------------------------------------------------------------

public PathConfig getScratchProjectPathConfig(bool keep = false) {
   return makePathConfig([ REPO + "rascal-scratch/src/main/rascal/"  ], [ RASCAL ], keep=keep);
}

public RascalCompilerConfig getScratchCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getScratchProjectPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
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
    return makePathConfig([ REPO + "flybytes/src", REPO + "rascal/src/org/rascalmpl/library" ], [ ], keep=keep);
}

public RascalCompilerConfig getFlyBytesCompilerConfig(bool keep=true){
    return rascalCompilerConfig(getFlyBytesProjectPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ---- salix -----------------------------------------------------------------

public PathConfig getSalixPathConfig(bool keep = false) {
    return makePathConfig([ REPO + "salix-core/src/main/rascal", REPO + "salix-contrib/src/main/rascal" ],
                          [ RASCAL ], 
                          keep=keep);
}

public RascalCompilerConfig getSalixCompilerConfig(bool keep = true){
    return rascalCompilerConfig(getSalixPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ---- drambiguity -----------------------------------------------------------

public PathConfig getDrAmbiguityPathConfig(bool keep = false) {
    return makePathConfig([ REPO + "drambiguity/src", REPO + "salix-core/src/main/rascal" ],
                          [ RASCAL ],
                          keep=keep);
}

public RascalCompilerConfig getDrAmbiguityCompilerConfig(bool keep = true){
    return rascalCompilerConfig(getDrAmbiguityPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}

// ---- rascal-language-server ------------------------------------------------

public PathConfig getLSPPathConfig(bool keep = false) {
    REPO_LSP = REPO + "rascal-language-servers/rascal-lsp/";
    return makePathConfig([ REPO_LSP + "src/main/rascal/library", REPO_LSP + "src/test/rascal"],
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
    LSP_REPO = |file:///Users/paulklint/git/rascal-language-servers/rascal-lsp/|;
    return  pathConfig(
        srcs=[LSP_REPO + "src/main/rascal/library", LSP_REPO + "src/main/rascal/lsp"],
        libs=[ TYPEPAL, RASCAL ],
        ignores=[],
        generatedResources=LSP_REPO + "target/classes/",
        bin=LSP_REPO + "target/classes/",
        generatedSources=LSP_REPO + "generated-sources/"
    );
}

public RascalCompilerConfig getVSCodeCompilerConfig(){
    return rascalCompilerConfig(getVSCodePathConfig())[verbose = true][logWrittenFiles=true];
}

// ---- Outdated TypePal Usage -----------------------------------------------------------------

public PathConfig getOutdatedTPLPathConfig(bool keep = false) {
    return makePathConfig([RASCAL + "src/org/rascalmpl/core/library"],
                          [ RASCAL, OUTDATED_TYPEPAL ],
                          keep=keep);
}

public RascalCompilerConfig getOutdatedTPLCompilerConfig(bool keep = false){
    return rascalCompilerConfig(getOutdatedTPLPathConfig(keep=keep))[verbose = true][logWrittenFiles=true];
}