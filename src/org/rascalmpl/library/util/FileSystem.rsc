module util::FileSystem

import IO;

public data FileSystem 
  = directory(loc l, set[FileSystem] children)
  | file(loc l)
  ;
  
public FileSystem crawl(loc l) = isDirectory(l) ? directory(l, {crawl(e) | e <- l.ls}) : file(l);
 
