module lang::rascalcore::compile::Examples::C

import lang::rascalcore::compile::Examples::B;
 
value main() {
   return make(2); // zou zero() moeten printen en niet succ(succ(zero())
}