@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
@synopsis{Demonstrates a file-system crawler}
@description{
Note that this functionality also resides in ((Library:util::FileSystem)).
This demo use different styles including a for-loop that produces a list value.
}
module demo::common::Crawl

import IO;
import String;

@synopsis{structured programming with recursion and a for-each loop}
public list[loc] crawl(loc dir, str suffix){
  res = [];
  for (loc entry <- dir.ls) {
      if (isDirectory(entry)) {
          res += crawl(entry, suffix);
      } 
      else if(endsWith(entry.path, suffix)) { 
	      res += [entry]; 
      }
  };
  return res;
}

@synopsis{structured programming with a for loop that constructs a list value}
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

@synopsis{functional programming with a ternary, a list splice and recursion.}
public list[loc] crawl3(loc dir, str suffix) =
  isDirectory(dir) ? [*crawl3(e,suffix) | e <- dir.ls] : (dir.extension == suffix ? [dir] : []);
