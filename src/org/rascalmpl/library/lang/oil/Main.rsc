module lang::oil::Main

data Exp  = \new(str class, list[Exp] arguments, list[Exp] calls)
          | \atom(int i)
          | \atom(str s)
          | \atom(bool b)
          | \call(str class, str method, list[Exp] arguments, list[Exp] calls)
          | \let(str key, Exp val)
          | \use(str key, Exp val)
          | \with(str prefix, Exp exp)
          | \access(str class, str field)
          ;

@javaClass{org.rascalmpl.library.lang.oil.Interpreter}
public java str interpret(list[Exp] e);

@javaClass{org.rascalmpl.library.lang.oil.Interpreter}
public java str interpret(Exp e);
