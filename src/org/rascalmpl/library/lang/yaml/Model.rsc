@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}

@synopsis{AST model for YAML (loosely based on the serialization model of <http://www.yaml.org/spec/1.2/spec.html>)}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl (CWI)}
module lang::yaml::Model

import List;
import Map;
import Set;

@synopsis{Generic representation for YAML nodes}
@description{
Tagging (using the `\tag` field) will be used to do typed 
serialization for ADTs in the future.
 
In valid YAML anchors always occur before any references
this should also hold in our YAML data type.
Dumping will throw index out of bound exception if references
are out of order.

Anchors are only currently valid on seq/map nodes.
and should be unique.
}
data Node(int anchor=-1, type[value] \tag = #void)
  = sequence(list[Node] \list)
  | scalar(value \value)
  | reference()
  | mapping(map[Node, Node] \map)
  ;
  
private set[type[value]] SUPPORTED_TYPES 
  = {#int, #str, #real, #datetime, #str, #bool, #loc}
  ;

@javaClass{org.rascalmpl.library.lang.yaml.RascalYAML}
public java Node loadYAML(str src);

@javaClass{org.rascalmpl.library.lang.yaml.RascalYAML}
public java str dumpYAML(Node yaml);

public str TEST_YAML = 
        "default: &DEFAULT
        ' URL:          stooges.com
        ' throw_pies?:  true  
        ' stooges:  &stooge_list
        '   larry:  first_stooge
        '   moe:    second_stooge
        '   nuther:  *stooge_list
        '   curly:  third_stooge
        '   cycle:  *DEFAULT";
    
   
public Node BAD_YAML =
  mapping((
   scalar(3,anchor=3):
   sequence([
      scalar("abc", \tag=#int),
      scalar("cde", \tag=#str),
      scalar("unsupported")[\tag=#node],
      reference(anchor=4),
      sequence([])[anchor=4]
   ])[@anchor=2]))[anchor=2];
   
public test bool testLoadDump() {
  Node n = loadYAML(TEST_YAML);
  str y = dumpYAML(n);
  return equalNodes(n, loadYAML(y));
}


public set[str] checkYAML(Node n) 
  = { "Scalar/reference <x> cannot be anchored" | x <- badAnchors(n) }
  + { "Duplicate anchor <i>" | i <- duplicateAnchors(n) }
  + { "Forward reference <x>" | x <- undefinedRefs(n, {}, {})[1] }
  + { "Untagged scalar <x>" | x <- untaggedScalars(n) }
  + { "Wrongly typed scalar <x>" | x <- wronglyTypedScalars(n) }
  + { "Unsupported scalar type \"<t>\"" | t <- unsupportedTypes(n) }
  ;

public set[Node] badAnchors(Node n)
  = { s | /s:scalar(_) <- n, (s.anchor)? }
  + { r | /r:reference() <- n, (r.anchor)? };


public set[Node] wronglyTypedScalars(Node n)
  = { s | /s:scalar(value v) <- n, s.\tag?, type[&T] t := s.\tag, !okValue(t, v) };

// Doesn't work: always succeeds.
public bool okValue(type[&T <: value] _, value v) = (&T _ := v);

public set[type[value]] unsupportedTypes(Node n) 
  = { t | /s:scalar(_) <- n, s.\tag?, type[value] t := s.\tag, t notin SUPPORTED_TYPES };

public set[Node] untaggedScalars(Node n) 
  = { s | /s:scalar(_) <- n, !(s.\tag?) }
  ;

public set[int] duplicateAnchors(Node n) {
  seen = {};
  duplicate = {};

  void record(Node s) {
   if (!(s.anchor?)) return;
   if (s.anchor in seen) 
     duplicate += {s.anchor};
   else 
     seen += {s.anchor};
  }
  
  visit (n) {
    case s:sequence(_): record(s);
    case m:mapping(_): record(m);
  }
  return duplicate;
}


public tuple[set[int], set[int]] undefinedRefs(reference(anchor=i), set[int] seen, set[int] dupl) 
  = <seen, dupl + {i}>
  when i notin seen;
  
public tuple[set[int], set[int]] undefinedRefs(s:sequence(ns), set[int] seen, set[int] dupl) {
  undefs = {};
  if (s.anchor?) {
    seen += {s.anchor};
  }
  for (n <- ns) 
    <seen, dupl> = undefinedRefs(n, seen, dupl);
  return <seen, dupl>;
}

public tuple[set[int], set[int]] undefinedRefs(nod:mapping(m), set[int] seen, set[int] dupl) {
  undefs = {};
  if (nod.anchor?) {
    seen += {nod.anchor};
  }
  for (Node n <- m) {
    <seen, dupl> = undefinedRefs(n, seen, dupl);
    <seen, dupl> = undefinedRefs(m[n], seen, dupl);
  }
  return <seen, dupl>;
}

public default tuple[set[int], set[int]] undefinedRefs(Node n, set[int] seen, set[int] dupl) 
  = <seen, dupl>;

bool equalNodes(Node x, Node y) {

   map[int, Node] anchors(Node n) {
     m = ();
     visit (n) {
       case s:sequence(_): 
          if (s.anchor?) m[s.anchor] = s;
       case mp:mapping(_): 
          if (mp.anchor?) m[mp.anchor] = mp;
     }
     return m;
   }
   
   anchors1 = anchors(x);
   anchors2 = anchors(y);

   map[int, int] matching = ();
   
   bool equalNodesRec(Node x, Node y) {
     switch (<x, y>) {
       case <sequence(ls1), sequence(ls2)>: {
         if (size(ls1) != size(ls2)) {
           return false;
         }
         return ( true | it && equalNodesRec(e1, e2) | <e1, e2> <- zip2(ls1, ls2) );
       }
     
	       case <scalar(v1), scalar(v2)>:
	         return v1 == v2;
	     
	       case <reference(anchor=r1), reference(anchor=r2)>: {
	         if (r1 in matching, matching[r1] == r2) {
	           return true; // avoid infinite loop;
	         }
	         matching[r1] = r2;
	         a1 = anchors1[r1];
	         a2 = anchors2[r2];
	         return equalNodesRec(a1, a2);
	         
	       }
	     
	       case <mapping(m1), mapping(m2)>: {
	         ls1 = sort(domain(m1));
	         ls2 = sort(domain(m2));
	         domEq = ( true | it && equalNodesRec(e1, e2) | <e1, e2> <- zip2(ls1, ls2) );
	         if (!domEq) {
	           return false;
	         }
	         return ( true | it && equalNodesRec(m1[k], m2[k]) | k <- ls1 ); 
	       }
	     
	       default: return false;
	     }
	   }
	   
	   return equalNodesRec(x, y);
}
