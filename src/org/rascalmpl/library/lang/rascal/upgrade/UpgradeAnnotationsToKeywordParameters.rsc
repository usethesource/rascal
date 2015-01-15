@bootstrapParser
module lang::rascal::upgrade::UpgradeAnnotationsToKeywordParameters

extend lang::rascal::upgrade::UpgradeBase;

list[Message] report(Tree m) 
  = [info("found annotation definition", name.src) | /(Declaration) `<Tags _> <Visibility v> anno <Type _> <Type _>@<Name name>;` := m]
  + [info("found annotation use", name.src) | /(Expression) `<Expression e>@<Name name>` := m]
  + [info("found annotion literal", name.src) | /(Expression) `<Expression e>[@<Name name>=<Expression def>]` := m]
  + [info("found annotation update", field.src) | /(Assignable) `<Name rec>@<Name field>` := m]
  ;

Tree update(Tree m) =
  visit(m) {
    case (Declaration) `<Tags tags> <Visibility v> anno <Type t> <Name adt>@<Name name>;` 
      => (Declaration) `<Tags tags> 
                       '<Visibility v> data <Name adt>(<Type t> <Name name> = <Expression init>);` 
      when Expression init := getInitializer(t) 
    case (Expression) `<Expression e>@\\loc` => (Expression) `<Expression e>.origin`
    case (Expression) `<Expression e>@src` => (Expression) `<Expression e>.origin`
    case (Expression) `<Expression e>@location` => (Expression) `<Expression e>.origin`
    case (Expression) `<Expression e>@<Name name>` => (Expression) `<Expression e>.<Name name>`
    case (Expression) `<Expression e>[@location=<Expression def>]` => (Expression) `<Expression e>[origin=<Expression def>]`
    case (Expression) `<Expression e>[@\\loc=<Expression def>]` => (Expression) `<Expression e>[origin=<Expression def>]`
    case (Expression) `<Expression e>[@src=<Expression def>]` => (Expression) `<Expression e>[origin=<Expression def>]`
    case (Expression) `<Expression e>[@<Name name>=<Expression def>]` => (Expression) `<Expression e>[<Name name>=<Expression def>]`
    case (Assignable) `<Name rec>@\\loc` => (Assignable) `<Name rec>.origin`
    case (Assignable) `<Name rec>@src` => (Assignable) `<Name rec>.origin`
    case (Assignable) `<Name rec>@location` => (Assignable) `<Name rec>.origin`
    case (Assignable) `<Name rec>@<Name field>` => (Assignable) `<Name rec>.<Name field>`
  };

Expression getInitializer((Type) `rel[<{TypeArg ","}* elem>]`) = (Expression) `{}`;
Expression getInitializer((Type) `list[<Type elem>]`) = (Expression) `[]`;
Expression getInitializer((Type) `map[<Type key>,<Type val>]`) = (Expression) `()`;
Expression getInitializer((Type) `set[<Type elem>]`) = (Expression) `{}`;
Expression getInitializer((Type) `real`) = (Expression) `0.0`;
Expression getInitializer((Type) `int`) = (Expression) `0`;
Expression getInitializer((Type) `num`) = (Expression) `0`;
Expression getInitializer((Type) `str`) = (Expression) `""`;
Expression getInitializer((Type) `value`) = (Expression) `[]`; 
Expression getInitializer((Type) `rat`) = (Expression) `r0`; 
Expression getInitializer((Type) `loc`) = (Expression) `|unknown:///|`; 
default Expression getInitializer(Type t) = (Expression) `<Type t> () { throw "no default value"; }()`;   