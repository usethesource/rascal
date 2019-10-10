module lang::rascalcore::compile::Examples::Tst3

data AType =
       astr(str label = "")
     | acons(list[AType] fields, str label = "")
     ;

value main(){
    set1 = { acons( [astr(label="message")],   label="UnavailableInformation") };
    consType = acons( [astr(label="message2")], label="RegExpSyntaxError");
  
    return set1 + consType;
}