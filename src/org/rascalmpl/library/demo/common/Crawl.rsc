@license{
  Copyright (c) 2009-2011 CWI
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

public loc catenate(loc basedir, str entry){ /*1*/
   baseuri = basedir.uri;
   if(!endsWith(baseuri, "/"))
   	  baseuri += "/";
   return basedir[uri=baseuri + entry];
}

public list[loc] crawl(loc dir, str suffix){ /*2*/
  res = [];
  for(str entry <- listEntries(dir)){
      loc sub = catenate(dir, entry);
      if(endsWith(entry, suffix)) { 
         res += [sub]; 
      }
      if(isDirectory(sub)) {
         res += crawl(sub, suffix);
      }
  };
  return res;
}