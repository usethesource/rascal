module lang::rascalcore::compile::Examples::Tst1

import lang::rascalcore::check::Checker;
import lang::rascalcore::check::TestConfigs;


value main(){
  return checkModules(["lang::rascalcore::compile::Examples::Tst2"], getAllSrcCompilerConfig());

}