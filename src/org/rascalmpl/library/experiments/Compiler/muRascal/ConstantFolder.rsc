module experiments::Compiler::muRascal::ConstantFolder

import experiments::Compiler::muRascal::AST; 
import Node;

// This where the constant fildoing rules belong
// They are now placed in muRascal/AST.rsc since the interpreter does not seems to like
// our specific module structure and, as a consequence, the folding rules are not applied.


//--------------- constant folding rules ----------------------------------------
// These rules should go to experiments::Compiler::RVM::Interpreter::ConstantFolder.rsc


bool allConstant(list[MuExp] args) { b = isEmpty(args) || all(a <- args, muCon(_) := a); /*println("allConstant: <args> : <b>"); */return b; }

// muBool, muInt?        
          
// Rascal primitives

// Integer addition

MuExp muCallPrim3("int_add_int", [muCon(int n1), muCon(int n2)], loc src) = muCon(n1 + n2);

MuExp muCallPrim3("int_add_int", [muCallPrim3("int_add_int", [MuExp e, muCon(int n1)], loc src1), muCon(int n2)], loc src2) =
      muCallPrim3("int_add_int", [e, muCon(n1 + n2)], src2);

MuExp muCallPrim3("int_add_int", [muCon(int n1), muCallPrim3("int_add_int", [muCon(int n2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("int_add_int", [muCon(n1 + n2), e], src2);
      

// Integer multiplication

MuExp muCallPrim3("int_product_int", [muCon(int n1), muCon(int n2)], loc src) = muCon(n1 * n2);

MuExp muCallPrim3("int_product_int", [muCallPrim3("int_product_int", [MuExp e, muCon(int n1)], loc src1), muCon(int n2)], loc src2) =
      muCallPrim3("int_product_int", [e, muCon(n1 * n2)], src2);

MuExp muCallPrim3("int_product_int", [muCon(int n1), muCallPrim3("int_product_int", [muCon(int n2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("int_product_int", [muCon(n1 * n2), e], src2);

// String concatenation

MuExp muCallPrim3("str_add_str", [muCon(str s1), muCon(str s2)], loc src) = muCon(s1 + s2);

MuExp muCallPrim3("str_add_str", [muCallPrim3("str_add_str", [MuExp e, muCon(str s1)], loc src1), muCon(str s2)], loc src2) =
      muCallPrim3("str_add_str", [e, muCon(s1 + s2)], src2);

MuExp muCallPrim3("str_add_str", [muCon(str s1), muCallPrim3("str_add_str", [muCon(str s2), MuExp e], loc src1)], loc src2)  =
      muCallPrim3("str_add_str", [muCon(s1 + s2), e], src2);

// Composite datatypes

MuExp muCallPrim3("list_create", list[MuExp] args, loc src) = muCon([a | muCon(a) <- args]) 
      when allConstant(args);

MuExp muCallPrim3("set_create", list[MuExp] args, loc src) = muCon({a | muCon(a) <- args}) 
      when allConstant(args);
      
MuExp muCallPrim3("map_create", list[MuExp] args, loc src) = muCon((args[i].c : args[i+1].c | int i <- [0, 2 .. size(args)]))
      when allConstant(args);
      
MuExp muCallPrim3("tuple_create", [muCon(v1)], loc src) = muCon(<v1>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2)], loc src) = muCon(<v1, v2>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2), muCon(v3)], loc src) = muCon(<v1, v2, v3>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4)], loc src) = muCon(<v1, v2, v3, v4>);
MuExp muCallPrim3("tuple_create", [muCon(v1), muCon(v2), muCon(v3), muCon(v4), muCon(v5)], loc src) = muCon(<v1, v2, v3, v4, v5>);

MuExp muCallPrim3("node_create", [muCon(str name), *MuExp args, muCallMuPrim("make_mmap", [])], loc src) = muCon(makeNode(name, [a | muCon(a) <- args]))  
      when allConstant(args);
      
MuExp muCallPrim3("appl_create", [muCon(value prod), muCon(list[value] args)], loc src) = muCon(makeNode("appl", prod, args));

// muRascal primitives

//MuExp muCallMuPrim(str name, list[MuExp] exps) = x;
