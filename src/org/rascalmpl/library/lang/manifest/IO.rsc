@license{
  Copyright (c) 2009-2020 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@synopsis{Jar Manifest files are a kind of property files typically stored inside Jar files.
  They contain meta information about the other files stored in the jar file.} 
@contributor{Jurgen Vinju}
module lang::manifest::IO

import Type;
import String;

@synopsis{reads a manifest file and returns its main attributes as a map}
@javaClass{org.rascalmpl.library.lang.manifest.IO}
java map[str key, str val] readManifest(loc input);

@synopsis{reads a manifest and converts the resulting map to keyword parameters for the given type}
(&T <: node) readManifest(type[&T<:node] t, loc input) {
   value convert(\list(\str()), str i) = [trim(x) | x <- split(",", i)];
   value convert(\set(\str()), str i) = [trim(x) | x <- split(",", i)];
   default value convert(Symbol _, str i) = i;
   
   m = readManifest(input);
   
   if (/\cons(label(name, _), args, kws, _) := t.definitions) {
      return make(t, name,  
        [convert(f, m[l]) | label(l, f) <- args,  m[l]?], 
        (l:convert(f, m[l]) | label(l, f) <- kws, m[l]?));
   }
   
   throw "no valid constructor found for <t> to store manifest in values in";
}  
