module util::Cursor

alias Path = list[Nav];

data Nav
  = root(str name)
  | field(str name)
  | field(int position)
  | argument(int position)
  | argument(str name)
  | keywordParam(str name)
  | element(int index)
  | sublist(int from, int to)
  | lookup(value key)
  | select(list[int] indices)
  | select(list[str] labels)
  ;

@javaClass{org.rascalmpl.library.util.Cursor}
java &T makeCursor(&T v);

@javaClass{org.rascalmpl.library.util.Cursor}
java &T makeCursor(&T v, str name);

@javaClass{org.rascalmpl.library.util.Cursor}
java &T update(&T cursor, &T v);

@javaClass{org.rascalmpl.library.util.Cursor}
java &U compute(&T v, &U(&T) to, &T(&U) from);

@javaClass{org.rascalmpl.library.util.Cursor}
java &T getRoot(type[&T] typ, value v);

@javaClass{org.rascalmpl.library.util.Cursor}
java Path toPath(value v);

@javaClass{org.rascalmpl.library.util.Cursor}
java bool isCursor(value v);

@javaClass{org.rascalmpl.library.util.Cursor}
java set[&T] subset(set[&T] sub, set[&T] backing);

tuple[set[&T], set[&T]] split(set[&T] src, bool(&T) p) 
  = <subset({ x | x <- src, p(x)}, src), subset({x | x <- src, !p(x) }, src)>;