@bootstrapParser
module lang::rascal::upgrade::UpgradeAnnotationsToKeywordParameters

extend lang::rascal::upgrade::UpgradeBase;
 
 
data Tree(loc src = |unknown:///|);

list[Message] report(Tree m) 
  = [info("found annotation definition", name.src) | /(Declaration) `<Tags _> <Visibility v> anno <Type _> <Type _>@<Name name>;` := m]
  + [info("found annotation use", name.src) | /(Expression) `<Expression e>@<Name name>` := m]
  + [info("found annotion literal", name.src) | /(Expression) `<Expression e>[@<Name name>=<Expression def>]` := m]
  + [info("found annotation update", field.src) | /(Assignable) `<Name rec>@<Name field>` := m]
  + [info("found annotation catch", e.src) | /(Catch) `catch NoSuchAnnotation(<Pattern e>) : <Statement body>` := m]
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

    case (Catch) `catch NoSuchAnnotation(<Pattern e>) : <Statement body>`
      => (Catch) `catch NoSuchField(<Pattern e>) :
                 ' // TODO: where annotations would often throw exceptions, keyword fields return their default.
                 '  <Statement body>`
  };

Name getName((Name) `\\loc`) = (Name) `src`;
Name getName((Name) `src`) = (Name) `src`;
Name getName((Name) `location`) = (Name) `src`;
default Name getName(Name n) = n;

test bool nameTest() = getName((Name) `location`) := (Name) `src`;

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