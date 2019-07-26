module lang::rascalcore::compile::Examples::Tst3
import Type;

@javaClass{org.rascalmpl.library.Prelude}
public java &T readTextValueFile(type[&T] result, loc file);

public &T readTextValueFileWithEmbeddedTypes(type[&T] result, loc file) {
  return readTextValueFile(type(result.symbol, result.definitions + #Symbol.definitions + #Production.definitions), file);
}
