module lang::rascalcore::compile::Examples::Tst4

data F = f();
anno int F@pos;

value checkAnnoExistsAsKeywordField(){
   F example = f();
   example@pos = 1;
   return example.pos?;
}