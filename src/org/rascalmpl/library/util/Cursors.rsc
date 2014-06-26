module util::Cursors

alias Path = list[Nav];

data Nav
  = field(str name)
  | subscript(int index)
  | lookup(value key)
  ;


@javaClass{org.rascalmpl.library.util.Cursors}
java &T makeCursor(&T v);

@javaClass{org.rascalmpl.library.util.Cursors}
java &T update(&T cursor, &T v);

@javaClass{org.rascalmpl.library.util.Cursors}
java &U compute(&T v, &U(&T) to, &T(&U) from);

@javaClass{org.rascalmpl.library.util.Cursors}
java &T getRoot(type[&T] typ, value v);


@javaClass{org.rascalmpl.library.util.Cursors}
java Path toPath(value v);


