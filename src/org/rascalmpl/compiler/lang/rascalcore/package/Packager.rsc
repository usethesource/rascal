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
module lang::rascalcore::package::Packager

import util::FileSystem;
import IO;
import ValueIO;
import ParseTree;
import util::Reflective;
import Location;
import String;
//import lang::rascalcore::check::LogicalLocations;

bool isRascalLogicalLoc(loc l)      // TODO: duplicated code from LogicalLocations; temporary fix
    = startsWith(l.scheme, "rascal+");

void main(PathConfig pcfg = pathConfig(), loc sourceLookup = |unknown:///|, loc relocatedClasses = pcfg.projectRoot + "/target/relocatedClasses") {
    if (!(sourceLookup?)) {
      throw "sourceLookup is not an optional parameter. The packager needs something like `|mvn://groupId--artifactId--version|`";
    }

    if (relocatedClasses?) {
        // then we activate the new style with a fresh target folder `relocatedClasses`
        package(pcfg.srcs, pcfg.bin, relocatedClasses, sourceLookup);
    }
    else {
        // otherwise we do the old in-place rewriting
        oldPackage(pcfg.srcs, pcfg.bin, sourceLookup);
    }
}

void package(list[loc] srcs, loc bin, loc relocated, loc sourceLookup) {
    packageSourceFiles(srcs, relocated);  
    copyAllTargetFiles(bin, relocated);
    rewriteTypeFiles(srcs, bin, relocated, sourceLookup);
}

void oldPackage(list[loc] srcs, loc bin, loc sourceLookup) {
    packageSourceFiles(srcs, bin);
    rewriteTypeFiles(srcs, bin, bin, sourceLookup);
}

void packageSourceFiles(list[loc] srcs, loc relocated) {
    for (folder <- srcs, file <- find(folder, "rsc")) {
      copy(file, relocated + relativize(folder, file).path);
    }
}

void copyAllTargetFiles(loc bin, loc relocated) {
    mkDirectory(relocated);

    // A pom file may include any thing (resources and classes)
    // and we just copy everything just in case it is needed at runtime.
    copy(bin, relocated, recursive = true);

    // But we remove the superfluous tpl files just in case.
    // They will be rewritten and copied later again.
    for (loc file <- find(relocated, "tpl")) {
        remove(file);
    }
}

void rewriteTypeFiles(list[loc] srcs, loc bin, loc relocated, loc sourceLookup) {
    for (loc file <- find(bin, "tpl")) {
        model = readBinaryValueFile(file);
        model = rewriteTypeModel(model, paths(srcs), sourceLookup);
        writeBinaryValueFile(relocated + relativize(bin, file).path, model);
    }
}

// map all files to their relative path within their respective source folder 
map[loc, str] paths(list[loc] srcs) 
    = (l:relativize(src, l).path | src <- srcs, /file(loc l) <- crawl(src));

// we do not insist on a specific type here for forward/backward compatibility's sake
value rewriteTypeModel(value model, map[loc,str] paths, loc sourceLookup) 
    = visit(model) {
          // any location in the wild:
          case loc l => inheritPosition(sourceLookup + paths[l.top], l)
              when !isRascalLogicalLoc(l),
                   l.top in paths

          // \loc annotations on Trees are not visited by `visit` automatically
          case Tree t => t[@\loc = inheritPosition(sourceLookup + paths[Top], t@\loc)]
              when t@\loc?, 
                   !isRascalLogicalLoc(t@\loc), 
                   loc Top := t@\loc.top, 
                   Top in paths

          // remove infos and warnings
          case set[Message] msgs => {msg | msg <- msgs, msg is error}
          case list[Message] msgs => [msg | msg <- msgs, msg is error]
    };

loc inheritPosition(loc new, loc original) {
    if (original.begin?) {
        return new(original.offset, original.length, original.begin, original.end);
    }
    else if (original.offset?) {
        return new(original.offset, original.length);
    }
    else {
        return new;
    }
}
