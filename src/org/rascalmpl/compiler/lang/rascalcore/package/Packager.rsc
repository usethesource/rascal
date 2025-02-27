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

public void package(list[loc] srcs, loc bin, loc sourceLookup) {
  packageSourceFiles(srcs, bin);  
  rewriteTypeFiles(srcs, bin, sourceLookup);
}

void packageSourceFiles(list[loc] srcs, loc bin) {
  for (folder <- srcs, file <- find(folder, "rsc")) {
    copyFile(file, bin + relativize(folder, file));
  }
}

void rewriteTypeFiles(list[loc] srcs, loc bin, loc sourceLookup) {
  for (file <- find(bin, "tpl")) {
     model = readBinaryValueFile(file);
     model = rewriteTypeModel(model, paths(srcs), sourceLookup);
     writeBinaryValueFile(file, model);
  }
}

// map all files to their relative path within their respective source folder 
map[loc, str] paths(list[loc] srcs) 
  = (l:relativize(src, l) | src <- srcs, /file(loc l) <- crawl(src));

// we do not insist on a specific type here for forward/backward compatibility's sake
value rewriteTypeModel(value model, map[loc,str] paths, loc sourceLookup) 
  = visit(model) {
      // any location in the wild:
      case loc l => inheritPosition(sourceLookup + paths[l.top], l)
        when paths[l.top]?
        
      // \loc annotations on Trees are not visited by `visit` automatically
      case Tree t => t[@\loc = inheritPosition(sourceLookup + paths[Top], t@\loc)]
        when t@\loc?, loc Top := t@\loc.top, paths[Top]?
  };

// compute a relative path of a file for a given base folder, if the file is indeed nested inside the given folder
str relativize(loc folder, loc file) = relativize(folder.path, file.path) 
  when folder.scheme == file.scheme,
       folder.authority == file.authority;
        
str relativize(str folder, /^<folder><path:.*>$/) = path;

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
