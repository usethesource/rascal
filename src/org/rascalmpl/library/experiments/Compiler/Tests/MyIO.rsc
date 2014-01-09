module experiments::Compiler::Tests::MyIO

import Exception;

@javaClass{org.rascalmpl.library.Prelude}
@reflect{Uses URI Resolver Registry}
public java void appendToFile(loc file, value V...)
throws UnsupportedScheme(loc file), PathNotFound(loc file), IO(str msg);