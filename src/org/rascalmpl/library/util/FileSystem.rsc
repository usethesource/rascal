@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module util::FileSystem

import IO;

@synopsis{Model of a file system with its (nested) files and directories}
data FileSystem 
  = directory(loc l, set[FileSystem] children)
  | file(loc l)
  ;
  
@synopsis{Extract a compositional ((FileSystem)) model starting from a given directory location.}
@description{
* Using `exclude` you can avoid going into certain directories or filter specific files from the result.  
}  
FileSystem crawl(loc l, set[loc] exclude= {}) 
  = isDirectory(l) ? directory(l, {crawl(e, exclude=exclude) | e <- l.ls, l notin exclude}) : file(l);

@synopsis{Recursively lists locations of all files from the supplied directory.}
@description{
* If input `l` is a file, its location is returned instead.
* Using `exclude` you can avoid going into certain directories or filter specific files from the result.  
}
set[loc] files(loc l, set[loc] exclude={}) = isDirectory(l) ? { *files(e, exclude=exclude) | e <- l.ls, e notin exclude} : {l};

@synopsis{Recursively lists locations of all files that satisfy the filter criterion `filt`.}
@description{
* For a file to be included, `filt` must return `true` for it. All directories are traversed though, regardless of `filt`.
* Using `exclude` you can avoid going into certain directories or filter specific files from the result.  
}
set[loc] find(loc f, bool (loc) filt, set[loc] exclude = {}) 
  = isDirectory(f) 
      ? {*find(c, filt, exclude=exclude) | c <- f.ls, c notin exclude} + ((filt(f) && f notin exclude) ? {f} : { }) 
      : (filt(f) ? {f} : { })
      ;

@synopsis{Recursively lists locations of all files that end in `ext`.}
@description{
* For a file to be included, it's extension must equal `ext`. All directories are traversed though, regardless of their extension.
* Using `exclude` you can avoid going into certain directories or filter specific files from the result.  
}
set[loc] find(loc f, str ext, set[loc] exclude={}) = find(f, bool (loc l) { return l.extension == ext; }, exclude=exclude);

@synopsis{Lists all files recursively ignored files and directories starting with a dot.}
set[loc] visibleFiles(loc l) {
  if (/^\./ := l.file) 
    return {};
  if (isDirectory(l)) 
    return {*visibleFiles(f) | f <- l.ls}; 
  return {l};
}
