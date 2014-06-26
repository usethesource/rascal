module util::Cursors

@javaClass{org.rascalmpl.library.util.Cursors}
java &T makeCursor(&T v);

@javaClass{org.rascalmpl.library.util.Cursors}
java &T update(&T cursor, &T v);

@javaClass{org.rascalmpl.library.util.Cursors}
java &T getRoot(type[&T] typ, value v);


