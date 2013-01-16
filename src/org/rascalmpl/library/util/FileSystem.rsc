@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
module util::FileSystem

import IO;

public data FileSystem 
  = directory(loc l, set[FileSystem] children)
  | file(loc l)
  ;
  
public FileSystem crawl(loc l) = isDirectory(l) ? directory(l, {crawl(e) | e <- l.ls}) : file(l);
 
