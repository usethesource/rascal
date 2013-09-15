module experiments::Compiler::Examples::MyLibrary

@javaClass{org.rascalmpl.library.Prelude}
public java int size(str s);

@javaClass{org.rascalmpl.library.Prelude}
public java str replaceAll(str subject, str find, str replacement);

value main(list[value] args) { 
	  return replaceAll("abracadabra", "a", "A");
}    