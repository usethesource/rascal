@bootstrapParser
module lang::rascal::upgrade::UpgradeAnnotationsToKeywordParameters

extend lang::rascal::upgrade::UpgradeBase;
 
anno loc Tree@\loc;

list[Message] report(Tree m) 
  = [info("found annotation definition", name@\loc) | /(Declaration) `<Tags _> <Visibility v> anno <Type _> <Type _>@<Name name>;` := m]
  + [info("found annotation use", name@\loc) | /(Expression) `<Expression e>@<Name name>` := m]
  + [info("found annotion literal", name@\loc) | /(Expression) `<Expression e>[@<Name name>=<Expression def>]` := m]
  + [info("found annotation update", field@\loc) | /(Assignable) `<Name rec>@<Name field>` := m]
  ;

Tree update(Tree m) =
  top-down visit(m) {
    case (Declaration) `<Tags tags> <Visibility _> anno <Type t> <Name adt>@<Name name>;` 
      => (Declaration) `<Tags tags> 
                       'data <Name adt>(<Type t> <Name name2> = <Expression init>);` 
      when Expression init := getInitializer(t), Name name2 := getName(name)
      
    case (Expression) `<Expression e>@<Name name> ? <Expression _>` => (Expression) `<Expression e>.<Name name2>`
      when Name name2 := getName(name)
      
    case (Expression) `<Expression e>@<Name name>` => (Expression) `<Expression e>.<Name name2>`
      when Name name2 := getName(name)
    
    case (Expression) `<Expression e>[@<Name name>=<Expression def>]` => (Expression) `<Expression e>[<Name name2>=<Expression def>]`
      when Name name2 := getName(name)
      
    case (Expression) `delAnnotations(<Expression e>)` => (Expression) `unset(<Expression e>)`
      
    case (Assignable) `<Name rec>@<Name field>` => (Assignable) `<Name rec>.<Name name2>`
      when Name name2 := getName(field)
  };

Name getName((Name) `\\loc`) = (Name) `origin`;
Name getName((Name) `src`) = (Name) `origin`;
Name getName((Name) `location`) = (Name) `origin`;
default Name getName(Name n) = n;

test bool nameTest() = getName((Name) `location`) == (Name) `origin`;

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