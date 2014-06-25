module util::Cursors

@javaClass{org.rascalmpl.library.util.Cursors}
java &T makeCursor(type[&T] typ, &T v);

@javaClass{org.rascalmpl.library.util.Cursors}
java &T update(type[&T] typ, &T cursor, &T v);

@javaClass{org.rascalmpl.library.util.Cursors}
java &T getRoot(type[&T] typ, value v);


