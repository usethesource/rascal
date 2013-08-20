module experiments::CoreRascal::ReductionWithEvalCtx::Parse

import experiments::CoreRascal::ReductionWithEvalCtx::Syntax;
import experiments::CoreRascal::ReductionWithEvalCtx::AST;
import Prelude;
import ParseTree;

Exp parse(str s) = implode(#experiments::CoreRascal::ReductionWithEvalCtx::AST::Exp,
						   parse( #experiments::CoreRascal::ReductionWithEvalCtx::Syntax::Exp, s));