module lang::rascalcore::compile::Examples::Tst3


import Type;
import String;

@synopsis{reads a manifest file and returns its main attributes as a map}
@javaClass{org.rascalmpl.library.lang.manifest.IO}
java map[str key, str val] readManifest(loc input);

@synopsis{reads a manifest and converts the resulting map to keyword parameters for the given type}
(&T <: node) readManifest(type[&T<:node] t, loc input) {
   value convert(\list(\str()), str i) = [trim(x) | x <- split(",", i)];
   //value convert(\set(\str()), str i) = [trim(x) | x <- split(",", i)];
   //default value convert(Symbol _, str i) = i;
   
   m = readManifest(input);
   
   //if (/\cons(label(name, _), args, kws, _) := t.definitions) {
   //   return make(t, name,  
   //     [convert(f, m[l]) | label(l, f) <- args,  m[l]?], 
   //     (l:convert(f, m[l]) | label(l, f) <- kws, m[l]?));
   //}
   
   throw "no valid constructor found for <t> to store manifest in values in";
}  
