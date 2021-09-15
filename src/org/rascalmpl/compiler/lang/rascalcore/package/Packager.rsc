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
