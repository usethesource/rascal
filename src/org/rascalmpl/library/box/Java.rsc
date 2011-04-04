@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module box::Java
import box::Concrete;
import box::Box;
import box::Box2Text;
import languages::java::syntax::Java;
import languages::java::syntax::Expressions;
import languages::java::syntax::JavaTypes;
import languages::java::syntax::FieldDecl;
import languages::java::syntax::ClassDecl;



import  IO;
import List;
import String;

public Box extraRules(Tree q) {  
   if (Expression a:=q) {
             if  (`  ( <Type t> ) <Expression e> ` :=a )   {return H([L("("), visitParseTree(t), L(")"), visitParseTree(e)]);}
       }
   return NULL();
   }
       
public text toList(loc asf){
     initConcrete(extraRules);
     CompilationUnit a = parse(#CompilationUnit, asf);
     Box out = visitParseTree(a);
     // println(out);
     return box2text(out);
     }

