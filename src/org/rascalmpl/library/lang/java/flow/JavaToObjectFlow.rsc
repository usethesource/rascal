@doc{
.Synopsis
mapping all of Java to the object flow language

.Description

More information can be found in ((analysis::flow::ObjectFlow)).
}
module lang::java::flow::JavaToObjectFlow

import IO;
import List;
import analysis::flow::ObjectFlow; 
import lang::java::m3::TypeSymbol;
import lang::java::m3::AST;

FlowProgram createOFG(set[Declaration] asts) {
  println("Getting decls");
  decls = getDeclarations(asts);
  println("Getting stms");
  stms = getStatements(asts);
  return flowProgram(decls, stms);
}

set[str] containerClasses =  {
  "/java/util/Map"
  ,"/java/util/HashMap"
  ,"/java/util/Collection"
  ,"/java/util/Set"
  ,"/java/util/HashSet"
  ,"/java/util/LinkedHashSet"
  ,"/java/util/List"
  ,"/java/util/ArrayList"
  ,"/java/util/LinkedList"
};

map[str, int] insertArgs = (
  "insert": 0
  , "insertAll": 0
  , "put": 1
  , "putAll": 0
  , "add": 0
  , "addAll": 0
);

Expression correctInsertArg(Expression _/*receiver*/, str name, list[Expression] args) {
  return args[insertArgs[name]];
}


bool isContainerInsert(Expression recv, str name) {
  if (recv.typ has decl) {
    tp = (recv.typ).decl.path;
    if (tp in containerClasses) {
      return name in insertArgs;
    }
  }
  return false;
}

bool isContainerExtract(Expression recv, str name) {
  tp = ((recv.typ).decl?|unknown:///|).path;
  if (tp in containerClasses) {
    switch (name) {
      case "get": return true;
      case "iterator": return true;
      case "toArray": return true;
      case "entrySet": return true;
      case "values": return true;
    }
  }
  return false;
}

list[Declaration] fixCollections(list[Declaration] ast) {
  return visit (ast) {
    case oe:methodCall(_, Expression receiver, methodName,args):  {
      if (isContainerInsert(receiver, methodName)) {
        insert assignment(receiver, "=", correctInsertArg(receiver, methodName, args))
          [typ = receiver.typ]
          [src = oe.src];
      }
      else if(isContainerExtract(receiver, methodName)) {
        insert receiver;
      }
    }
  };
}

set[str] primitiveTypes = {
  "Byte", "java.lang.Byte"
  , "Character", "java.lang.Character"
  , "Short", "java.lang.Short"
  , "Integer", "java.lang.Integer"
  , "Long", "java.lang.Long"
  , "Float", "java.lang.Float"
  , "Double", "java.lang.Double"
};

bool ignoreType(arrayType(t)) = ignoreType(t);
bool ignoreType(upperbound(t)) = ignoreType(t);
bool ignoreType(lowerbound(t)) = ignoreType(t);
bool ignoreType(parameterizedType(_)) = false;
bool ignoreType(Type::qualifiedType(t,_)) = ignoreType(t);
bool ignoreType(simpleType(t)) = ignoreType(t);// t in primitiveTypes;
bool ignoreType(unionType(tt)) = (false | it || ignoreType(t) | t <- tt);
default bool ignoreType(Type t) = true;

bool ignoreType(Expression::simpleName(n)) = n in primitiveTypes;
bool ignoreType(Expression::qualifiedName(Expression q, n)) {
  if (simpleName(nn) := n && nn in primitiveTypes) {
    // could be a primitive type
    // lets build the fullTypeName
    result = nn;
    parent = q;
    while (true) {
      if (qualifiedName(p,simpleName(c)) := parent) {
        result = c + "." + result;
        parent = p;
      }
      else if (simpleName(c) := parent) {
        result = c + "." + result;
        break;
      }
      else {
        throw "unexpected parent c";
      }
    }
    return result in primitiveTypes;
  }
  else {
    return false;
  }
}
default bool ignoreType(Expression e) { throw "you forgot: <e>"; }


bool ignoreType(TypeSymbol::interface(_,_)) = false;
bool ignoreType(TypeSymbol::\enum(_)) = false;
bool ignoreType(TypeSymbol::\typeParameter(_,_)) = false;
bool ignoreType(TypeSymbol::\wildcard(_)) = false;
bool ignoreType(TypeSymbol::\capture(_,_)) = false;
bool ignoreType(TypeSymbol::intersection(tt)) = (false | it || ignoreType(t) | t <- tt);
bool ignoreType(TypeSymbol::union(tt)) = (false | it || ignoreType(t) | t <- tt);
bool ignoreType(TypeSymbol::\class(t,_)) = t == |java+class:///java/lang/String|;
bool ignoreType(TypeSymbol::\object()) = false;
bool ignoreType(TypeSymbol::\array(_,_)) = true;
default bool ignoreType(TypeSymbol t) = true;


set[FlowDecl] getDeclarations(set[Declaration] asts) 
  = { FlowDecl::attribute(v.decl) | /field(t,frags) <- asts, !ignoreType(t), v <- frags}
  + { FlowDecl::method(m.decl, [p.decl | p:parameter(t,_,_) <- params, !ignoreType(t)]) | /m:Declaration::method(_,_, list[Declaration] params, _, _)  <- asts}
  + { FlowDecl::method(m.decl, [p.decl | p:parameter(t,_,_) <- params, !ignoreType(t)]) | /m:Declaration::method(_,_, list[Declaration] params, _)  <- asts}
  + { FlowDecl::constructor(c.decl, [p.decl | p:parameter(t,_,_) <- params, !ignoreType(t)]) | /c:Declaration::constructor(_, list[Declaration] params, _,_)  <- asts}      
  // add implicit constructor
  + { FlowDecl::constructor((c.decl)[scheme="java+constructor"] + "<name>()", []) | /c:class(name, _, _, b) <- asts, !(Declaration::constructor(_, _, _, _) <- b)}   
  ;

loc lhsDecl(arrayAccess(e,_)) = e.decl;
loc lhsDecl(f:fieldAccess(_,_,_)) = f.decl;
loc lhsDecl(f:fieldAccess(_,_)) = f.decl;
loc lhsDecl(v:variable(_,_)) = v.decl;
loc lhsDecl(s:simpleName(_)) = s.decl;
loc lhsDecl(q:qualifiedName(_,_)) = q.decl;
default loc lhsDecl(Expression e) { throw "forgot: <e>"; }

set[FlowStm] getStatements(set[Declaration] asts) {
  allMethods 
    = [ m | /m:Declaration::method(_,_,_,_,_) <- asts]
    + [Declaration::method(t, n, p, e, empty())[decl=m.decl] | /m:Declaration::method(Type t,n,p,e) <- asts] 
    + [Declaration::method(simpleType(simpleName(n)), n, p, e, b)[decl=m.decl] | /m:Declaration::constructor(str n,p,e, b) <- asts]
    ;

  allMethods = fixCollections(allMethods);
  // now remove all nested classes to make all statements relative to a method
  allMethods = visit(allMethods) {
    case declarationExpression(Declaration::class(_)) => Expression::null()
    case declarationExpression(Declaration::class(_,_,_,_)) => Expression::null()
    case declarationExpression(Declaration::enum(_,_,_,_)) => Expression::null()
    case declarationStatement(Declaration::class(_)) => empty()
    case declarationStatement(Declaration::class(_,_,_,_)) => empty()
    case declarationStatement(Declaration::enum(_,_,_,_)) => empty()
  };

  set[FlowStm] result = {};

  for (m:Declaration::method(_, _, _, _, b) <- allMethods) {
    top-down-break visit(b) {
      case \return(e) : 
        result += { *translate(m.decl, m.decl + "return", e)};
      case e:Expression::assignment(l,_,r) : 
        if (!ignoreType(e.typ)) {
          result += { *translate(m.decl, lhsDecl(l), r)};
        } else {
        // there can be a nested assignment caused by the rewriting done earlier (containers)
          for (/e2:assignment(l2,_,r2) := r && !ignoreType(e2.typ)) {
            result += { *translate(m.decl, lhsDecl(l2), r2)};
          }
        }
      case v:Expression::variable(_,_,r) : 
        if (!ignoreType(v.typ)) {
          result += { *translate(m.decl, v.decl, r)};
        }
      // regular method calls with no target
      case m2:Expression::methodCall(_ ,_, _):
        result += { *translate(m.decl, emptyId, m2)};
      case m2:Expression::methodCall(_ ,_, _, _):
        result += { *translate(m.decl, emptyId, m2)};
    }
  }
  return result;
}

// TODO: handle a.b.c => B.c

set[FlowStm] translate(loc base, loc target, c:cast(_, e)) {
  if (ignoreType(c.typ)) 
    return {};
  
  result = translate(base, target, e);
  return { s.target == target ? s[cast=c.typ.decl] : s | s <- result};
}

set[FlowStm] translate(loc base, loc target, conditional(con, t, e)) 
  = translate(base, emptyId, con)
  + translate(base, target, t)
  + translate(base, target, e)
  ;

// TODO: check what the second argument could mean (Expr)
set[FlowStm] translate(loc base, loc target, f:fieldAccess(_,_,_))
  = {FlowStm::assign(target, emptyId, f.decl)};
set[FlowStm] translate(loc base, loc target, f:fieldAccess(_,_))
  = {FlowStm::assign(target, emptyId, f.decl)};

set[FlowStm] translate(loc base, loc target, s:simpleName(_))
  = {FlowStm::assign(target, emptyId, s.decl)};

// nested assignment a = b = c;
set[FlowStm] translate(loc base, loc target, a:assignment(l,_,r)) 
  = translate(base, target, l)
  + translate(base, target, r)
  ;

set[FlowStm] translate(loc base, loc target, m:methodCall(s, n, a))
= translate(base, target, methodCall(s, this(), n, a)[decl=m.decl][typ=m.typ][src=m.src]);
set[FlowStm] translate(loc base, loc target, m:methodCall(_, r, n, a)) {
  set[FlowStm] stms = {};
  loc recv = emptyId;
  if (this() := r) {
    recv = base+"this";
  }
  else {
    <newId, newStms> = unnestExpressions(base, r.src.offset, [r]);
    if (size(newId) > 0) {
      assert size(newId) == 1;
      recv = getOneFrom(newId);
    }
    stms += newStms;
  }
  <args, newStms> = unnestExpressions(base, m.src.offset, a);
  return newStms + { FlowStm::call(target, emptyId, recv, m.decl, args) };
}

private Expression newObject(Type t, list[Expression] args, Expression original) {
  assert original is newObject;
  return newObject(t, args)
    [typ = original.typ]
    [src = original.src]
    [decl = original.decl];
}

set[FlowStm] translate(loc base, loc target, Expression ob:newObject(Expression _, Type t, list[Expression] a))
  = translate(base, target, newObject(t, a, ob));
  
set[FlowStm] translate(loc base, loc target, Expression ob:newObject(_, Type t, list[Expression] a, Declaration _))
  = translate(base, target, newObject(t, a, ob));
  
set[FlowStm] translate(loc base, loc target, Expression ob:newObject(Type t, list[Expression] a, Declaration _))
  = translate(base, target, newObject(t, a, ob));
  
set[FlowStm] translate(loc base, loc target, Expression ob:newObject(Type t, a)) {
  assert target != emptyId;
  if (ignoreType(ob.typ))
    return {};

  <args, stms> = unnestExpressions(base, ob.src.offset, a);
  return stms + { FlowStm::newAssign(target, ob.typ.decl, ob.decl, args)};
}

bool simpleExpression(fieldAccess(_,_,_)) = true;
bool simpleExpression(fieldAccess(_,_)) = true;
bool simpleExpression(qualifiedName(_,e)) = simpleExpression(e);
bool simpleExpression(this()) = true;
bool simpleExpression(this(_)) = true;
bool simpleExpression(simpleName(_)) = true;
default bool simpleExpression(Expression e) = false;

Expression removeNesting(cast(_, e)) = removeNesting(e);
Expression removeNesting(arrayAccess(e, _)) = removeNesting(e);
Expression removeNesting(\bracket(e)) = removeNesting(e);
default Expression removeNesting(Expression e) = e;

// for arguments we have to unnestExpressions
//  .. = new A(new B());
// becomes
// __param<unique>_0 = new B();
// .. = new A(__param<unique>_0);
tuple[list[loc], set[FlowStm]] unnestExpressions(loc prefix, int uniqNum, list[Expression] exprs) {
  list[loc] ids = [];
  set[FlowStm] newStms = {};
  for (i <- [0..size(exprs)], Expression ce := exprs[i], !ignoreType(ce.typ)) {
    ce = removeNesting(ce);
    if (simpleExpression(ce)) {
      if (ce is this) {
        ids += [prefix + "this"];
      } 
      else {
        ids += [ce.decl];
      }
    }
    else {
      newId = prefix + "__param<uniqNum>_<i>";
      ids += [newId];
      newStms += translate(prefix, newId, ce);
    }
  }
  return <ids, newStms>;
}

default set[FlowStm] translate(loc base, loc target, Expression e) = { *translate(base, target, ch) | Expression ch <- e};
