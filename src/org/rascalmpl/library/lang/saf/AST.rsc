@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Tijs van der Storm - Tijs.van.der.Storm@cwi.nl}
@contributor{Anastasia Izmaylova - A.Izmaylova@cwi.nl}
module lang::saf::AST

@functor FFighter #str, #list[Spec]
data Fighter = fighter(str name, list[Spec] specs);

@functor FSpec #str, #int, #Cond, #Action
data Spec 
  = attribute(str name, int strength)
  | behavior(Cond cond, Action move, Action fight)
  ;

@functor FCond #str, #Cond
data Cond
  = const(str name)
  | and(Cond lhs, Cond rhs)
  | or(Cond lhs, Cond rhs)
  ;

@functor FAction #str, #list[str]
data Action 
  = action(str name)
  | choose(list[str] actions)
  ;
  
public anno loc Fighter@location;
public anno loc Spec@location;
public anno loc Cond@location;
public anno loc Action@location;

public int getAttr(Fighter f, str name) {
  if (attribute(name, n) <- f.specs)
     return n;
  return 5;
} 