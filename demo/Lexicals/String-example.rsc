module String-example

String removeFirstNL(String S){
    switch (S) {
      case string("\"" <NQChar* Chars1> "\n" <NQChar* Chars2> "\"") =>
           string("\"" <Chars1> <Chars2> "\"")
    }
}

String removeAllNL(String S){
    return innermost visit (S) {
      case string("\"" <NQChar* Chars1> "\n" <NQChar* Chars2> "\"") =>
           string("\"" <Chars1> <Chars2> "\"")
    };
}