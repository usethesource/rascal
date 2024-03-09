module lang::rascalcore::compile::Examples::Tst5


import lang::rascalcore::check::tests::StaticTestingUtils;

value main() { 
   makeModule("RT", "import List;
                    'int f(list[int] L) {
                    '  return size(L);
                    '}
                    '");
   return checkOK("f([1,2,3]);", importedModules=["RT"]);                 
}