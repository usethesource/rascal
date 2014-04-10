module experiments::Compiler::Examples::QL::lang::qla::QL

extend experiments::Compiler::Examples::QL::lang::qla::Lexical;

start syntax Form
  = form: "form" Id name "{" Question* questions "}"
  ;

syntax Question
  = question: Label label Id name ":" QType type
  | computed: Label label Id name ":" QType type "=" Expr expr
  | ifThen: "if" "(" Expr cond ")" Question () !>> "else"
  | ifThenElse: "if" "(" Expr cond ")" Question question "else" Question elseQuestion
  | @Foldable group: "{" Question* questions "}"
  ;

syntax Expr
  = var: Id name
  | integer: Integer
  | string: String
  | money: Money
  | \true: "true"
  | \false: "false"
  | bracket "(" Expr ")"
  > not: "!" Expr
  > left (
      mul: Expr "*" Expr
    | div: Expr "/" Expr
  )
  > left (
      add: Expr "+" Expr
    | sub: Expr "-" Expr
  )
  > non-assoc (
      lt: Expr "\<" Expr
    | leq: Expr "\<=" Expr
    | gt: Expr "\>" Expr
    | geq: Expr "\>=" Expr
    | eq: Expr "==" Expr
    | neq: Expr "!=" Expr
  )
  > left and: Expr "&&" Expr
  > left or: Expr "||" Expr
  ;
  
keyword Keywords = "true" | "false" ;

