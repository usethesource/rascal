@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
//START
module demo::common::Crawl

import IO;
import String;

public list[loc] crawl(loc dir, str suffix){
  res = [];
  for (loc entry <- dir.ls) {
      if (isDirectory(entry)) {
          res += crawl(entry, suffix);
      } else if(endsWith(entry.path, suffix)) { 
	      res += [entry]; 
      }
  };
  return res;
}

public list[loc] crawl2(loc dir, str suffix) {
  return result:for (loc entry <- dir.ls) {
	      for (isDirectory(entry), sub <- crawl(entry, suffix)) {
	          append result: sub;  /*2*/
	      }
		      
		  if(!isDirectory(entry), endsWith(entry.path, suffix)) { 
		      append result: entry; /*3*/
		  }
	  }
}

public list[loc] crawl3(loc dir, str suffix) =
  isDirectory(dir) ? [*crawl3(e,suffix) | e <- dir.ls] : (dir.extension == suffix ? [dir] : []);
