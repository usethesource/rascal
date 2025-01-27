@bootstrapParser
module lang::rascal::upgrade::UpdateNestedListAndSetPatterns

extend lang::rascal::upgrade::UpgradeBase;

list[Message] report(Tree m) 
  = [info("found postfix multivar",  name.origin) | /(Pattern) `<QualifiedName name>*` := m];

Tree update(Tree m) =
  visit(m) {
    case (Pattern) `<QualifiedName name>*` => (Pattern) `*<QualifiedName name>`
  };
