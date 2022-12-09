module lang::rascalcore::compile::Examples::Tst0

set[loc] visibleFiles(loc l) {
  if (/a/ := l.file && l == |xxx:///|) 
    return {};
  else
    return {l};
}
