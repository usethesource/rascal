module experiments::Compiler::Examples::QL::lang::qla::Expr2JS

import experiments::Compiler::Examples::QL::lang::qla::AST;

str expr2js(var(x)) = "<x.name>"; 
str expr2js(Expr::integer(x)) = "<x>";
str expr2js(\true()) = "true";
str expr2js(\false()) = "false";
str expr2js(Expr::string(x)) = "<x>";
str expr2js(not(e)) = "!(<expr2js(e)>)";

str expr2js(mul(lhs, rhs)) = "(<expr2js(lhs)> * <expr2js(rhs)>)";
str expr2js(div(lhs, rhs)) = "(<expr2js(lhs)> / <expr2js(rhs)>)";
str expr2js(add(lhs, rhs)) = "(<expr2js(lhs)> + <expr2js(rhs)>)";
str expr2js(sub(lhs, rhs)) = "(<expr2js(lhs)> - <expr2js(rhs)>)";
str expr2js(gt(lhs, rhs))  = "(<expr2js(lhs)> \> <expr2js(rhs)>)";
str expr2js(geq(lhs, rhs)) = "(<expr2js(lhs)> \>= <expr2js(rhs)>)";
str expr2js(lt(lhs, rhs))  = "(<expr2js(lhs)> \< <expr2js(rhs)>)";
str expr2js(leq(lhs, rhs)) = "(<expr2js(lhs)> \<= <expr2js(rhs)>)";
str expr2js(eq(lhs, rhs))  = "(<expr2js(lhs)> === <expr2js(rhs)>)";
str expr2js(neq(lhs, rhs)) = "(<expr2js(lhs)> !== <expr2js(rhs)>)";
str expr2js(and(lhs, rhs)) = "(<expr2js(lhs)> && <expr2js(rhs)>)";
str expr2js(or(lhs, rhs))  = "(<expr2js(lhs)> || <expr2js(rhs)>)";
