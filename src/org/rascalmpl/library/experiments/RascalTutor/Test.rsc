module experiments::RascalTutor::Test

public set[str] tst(str txt){
   set[str] terms = {};
       visit(txt){
         case /^<name:[a-z][A-Za-z0-9]*>/: {terms += name; } // BUG IN VISIT
         
       };
    return terms;
}
