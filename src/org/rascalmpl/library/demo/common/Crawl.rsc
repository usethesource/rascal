@license{
  Copyright (c) 2009-2013 CWI
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
  for(str entry <- listEntries(dir)){
      loc sub = dir + entry;   /*1*/
      if(isDirectory(sub)) {
          res += crawl(sub, suffix);
      } else {
	      if(endsWith(entry, suffix)) { 
	         res += [sub]; 
	      }
      }
  };
  return res;
}

//public list[loc] crawl2(loc dir, str suffix){
//  return 
//	  for(str entry <- listEntries(dir)){
//	      loc sub = dir + entry;  
//	      if(isDirectory(sub)) {
//	          append crawl(sub, suffix);  /*2*/
//	      } else {
//		      if(endsWith(entry, suffix)) { 
//		         append [sub];           /*3*/
//		      }
//	      }
//	  };
//}

public list[loc] crawl3(loc dir, str suffix) =
  isDirectory(dir) ? [*crawl(e,suffix) | e <- dir.ls] : (dir.extension == suffix ? [dir] : []);
