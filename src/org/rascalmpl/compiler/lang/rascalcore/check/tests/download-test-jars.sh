#! /bin/sh

set -e -o pipefail

function download() {
    mvn dependency:get -DgroupId="$1" -DartifactId="$2" -Dversion="$3"
}

# these should be synced with `TestConfigs.rsc`
download "org.rascalmpl" "rascal" "0.41.0-RC15" 
download "org.rascalmpl" "rascal" "0.41.0-RC67"
download "org.rascalmpl" "typepal" "0.14.8" 
download "org.rascalmpl" "typepal" "0.14.1" 
download "org.rascalmpl" "drambiguity" "0.1.2" 
download "org.rascalmpl" "flybytes" "0.1.5" 
download "org.rascalmpl" "salix-core" "0.2.7" 
download "org.rascalmpl" "salix-contrib" "0.2.7" 
download "org.rascalmpl" "rascal-lsp" "2.21.2" 
download "org.rascalmpl" "java-air" "1.0.0-RC2"