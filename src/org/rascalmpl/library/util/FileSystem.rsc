@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module util::FileSystem

import Exception;
import IO;
import util::Monitor;

@synopsis{Model of a file system with its (nested) files and directories}
data FileSystem 
  = directory(loc l, set[FileSystem] children)
  | file(loc l)
  ;
  
@synopsis{Extract a compositional ((FileSystem)) model starting from a given directory location.}
@description{
* Using `exclude` you can avoid going into certain directories or filter specific files from the result.  
* With `checkExist=true` the `l` parameter is checked to exist before the file system is crawled and a PathNotFound exception is thrown if not.
}  
FileSystem crawl(loc l, set[loc] exclude= {}, bool checkExist=false) throws PathNotFound
  = isDirectory(l) ? directory(l, {crawl(e, exclude=exclude, checkExist=false) | e <- l.ls, l notin exclude}) : file(l)
  when checkExist ==> throwNotExist(l)
  ;

@synopsis{Recursively lists locations of all files from the supplied directory.}
@description{
* If input `l` is a file, its location is returned instead.
* Using `exclude` you can avoid going into certain directories or filter specific files from the result.  
* With `checkExist=true` the `l` parameter is checked to exist before the file system is crawled and a PathNotFound exception is thrown if not.
}
set[loc] files(loc l, set[loc] exclude={}, bool checkExist=false) throws PathNotFound
  = isDirectory(l) ? { *files(e, exclude=exclude, checkExist=false) | e <- l.ls, e notin exclude} : {l} 
  when checkExist ==> throwNotExist(l);

@synopsis{Recursively lists locations of all files that satisfy the filter criterion `filt`.}
@description{
* For a file to be included, `filt` must return `true` for it. All directories are traversed though, regardless of `filt`.
* Using `exclude` you can avoid going into certain directories or filter specific files from the result.  
* With `checkExist=true` the `f` parameter is checked to exist before the file system is crawled and a PathNotFound exception is thrown if not.
}
set[loc] find(loc f, bool (loc) filt, set[loc] exclude = {}, bool checkExist=false) throws PathNotFound
  = isDirectory(f) 
      ? {*find(c, filt, exclude=exclude, checkExist=false) | c <- f.ls, c notin exclude} + ((filt(f) && f notin exclude) ? {f} : { }) 
      : (filt(f) ? {f} : { })
      when checkExist ==> throwNotExist(f)
      ;

@synopsis{Recursively lists locations of all files that end in `ext`.}
@description{
* For a file to be included, it's extension must equal `ext`. All directories are traversed though, regardless of their extension.
* Using `exclude` you can avoid going into certain directories or filter specific files from the result.  
* With `checkExist=true` the `f` parameter is checked to exist before the file system is crawled and a PathNotFound exception is thrown if not.
}
set[loc] find(loc f, str ext, set[loc] exclude={}, bool checkExist=false) throws PathNotFound
  = find(f, bool (loc l) { return l.extension == ext; }, exclude=exclude, checkExist=checkExist);

@synopsis{Lists all files recursively ignored files and directories starting with a dot.}
set[loc] visibleFiles(loc l, bool checkExist=false) throws PathNotFound {
  if (checkExist) {
    throwNotExist(l);
  }
  if (/^\./ := l.file) {
    return {};
  }
  if (isDirectory(l)) {
    return {*visibleFiles(f, checkExist=false) | f <- l.ls}; 
  }
  return {l};
}

@synopsis{Always returns true, but shows a ((util::Monitor::jobWarning)) if `file` does not exist.}
@benefits{
* This can be used practically in comprehensions that process file locations.
* Use it to fail more transparantly, but still in case of erroneous file configuration (paths)
}
bool warnNotExist(loc file) {
  if (!exists(file)) {
    jobWarning("<file> does not exist.", |std:///util/FileSystem|);
  }
  return true;
}

@synopsis{Always returns true, except when throwing FileNotFound if `file` does not exist}
@benefits{
* This can be used practically in comprehensions that process file locations;
* Use it to fail faster and harder in case of erroneous  file configuration (paths)
}
bool throwNotExist(loc file) throws PathNotFound {
  if (!exists(file)) {
    throw PathNotFound(file);
  }
  return true;
}


