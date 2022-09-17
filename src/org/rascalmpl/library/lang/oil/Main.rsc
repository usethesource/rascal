@doc{
#### Synopsis

Object Instantiation Language (OIL) for building Java objects at run-time.

#### Description

The OIL language is an abstract language which can be considered
a small sub-set of the Java language. It allows one to build objects
using fully qualified class names (which should be on the run-time
classpath somehow). 

A call to the ((interpret)) function will load an OIL expression and 
generate the object in memory and then call the toString() method
on it, returning the resulting string.

OIL works best in combination with writing one or two `java` functions
for example to be able to influence the classpath and or to further process
the constructed object (if necessary). In this case we call the interpret
function directly from Java. The ((interpret)) function is used to debug
an OIL expression and eventually we use the ((interpret)) method directly
from Java.

#### Benefits

*  OIL is made to avoid having to write Java when reusing a Java library.

#### Pitfalls

*  The OIL language is experimental and not used much. It is unfinished and there may be bugs.
} 
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
@doc{
#### Synopsis

convert an OIL expression to a Java object
#### Description

Uses the Java reflection API and the current classpath to produce 
a Java object, interpreting the expressions, and then calls the
`toString()` method and returns the resulting string.

These are the instructions of OIL:

*  `let` binds an object to a variable to be used later. This is useful 
not only to factor out common expressions, but also to implement sharing;

*  `access` retrieves a field from an object, discarding the receiver object and keeping
the objects referred to by the field name;

*  `atom` builds on of Java's builtin data-types
*  `use` dereferences an object which was bound earlier by a `let`
*  `new` calls the constructor of a class with the provided argument list. The `methods` argument is ignored for now.
*  `call` calls a method on the receiving object and returns the result. (this seems to be broken).
*  `with` is unimplemented, intented to avoid having to fully qualify class names all the time. 
}
public java str interpret(list[Exp] e);

@javaClass{org.rascalmpl.library.lang.oil.Interpreter}
public java str interpret(Exp e);
