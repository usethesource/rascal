module lang::rascalcore::package::Packager

import util::FileSystem;
import util::Monitor;
import IO;
import ValueIO;

public void package(list[loc] srcs, loc bin, loc sourceLookup) {
  packageSourceFiles(srcs, bin);  
  rewriteTypeFiles(srcs, bin, sourceLookup);
}

void packageSourceFiles(list[loc] srcs, loc bin) {
  for (folder <- srcs, file <- find(folder, "rsc")) {
    event("Copying <file>");
    copyFile(file, bin + relativize(folder, file));
  }
}

void rewriteTypeFiles(list[loc] srcs, loc bin, loc sourceLookup) {
  for (file <- find(bin, "tpl")) {
     event("Relocating source references in <file>");
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
      case loc l => inheritPosition(sourceLookup + paths[l.top], l)
        when paths[l.top]?
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
