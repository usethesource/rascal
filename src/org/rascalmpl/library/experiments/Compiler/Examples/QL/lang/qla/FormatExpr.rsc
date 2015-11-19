module experiments::Compiler::Examples::QL::lang::qla::FormatExpr

import experiments::Compiler::Examples::QL::util::Priorities;
import experiments::Compiler::Examples::QL::lang::qla::AST;

str \format(Expr::integer(n)) = "<n>";
str \format(Expr::string(s)) = s;
str \format(Expr::money(n)) = "<n>";
str \format(\true()) = "true";
str \format(\false()) = "false";

str \format(var(x)) = x.name;
str \format(e:not(x)) = "!<\formatP(e,x)>";
str \format(e:add(x, y)) = "<\formatP(e,x)> + <\formatP(e, y)>";
str \format(e:sub(x, y)) = "<\formatP(e,x)> - <\formatP(e, y)>";
str \format(e:mul(x, y)) = "<\formatP(e,x)> * <\formatP(e, y)>";
str \format(e:div(x, y)) = "<\formatP(e,x)> / <\formatP(e, y)>";
str \format(e:and(x, y)) = "<\formatP(e,x)> && <\formatP(e, y)>";
str \format(e:or(x, y))  = "<\formatP(e,x)> || <\formatP(e, y)>";
str \format(e:eq(x, y))  = "<\formatP(e,x)> == <\formatP(e, y)>";
str \format(e:neq(x, y)) = "<\formatP(e,x)> != <\formatP(e, y)>";
str \format(e:lt(x, y))  = "<\formatP(e,x)> \< <\formatP(e, y)>";
str \format(e:gt(x, y))  = "<\formatP(e,x)> \> <\formatP(e, y)>";
str \format(e:leq(x, y)) = "<\formatP(e,x)> \<= <\formatP(e, y)>";
str \format(e:geq(x, y)) = "<\formatP(e,x)> \>= <\formatP(e, y)>";

str parenizer(str x) = "(<x>)";

str parens(node parent, node kid, str x)
  = parens(prioritiesOf(#Form), parent, kid, x, parenizer);

str \formatP(Expr parent, Expr kid)
  = parens(parent, kid, \format(kid));


