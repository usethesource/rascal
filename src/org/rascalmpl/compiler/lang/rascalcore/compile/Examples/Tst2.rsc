//@bootstrapParser
module lang::rascalcore::compile::Examples::Tst2

import  lang::rascalcore::compile::Compile;

value main() {
    msgs = compile("lang::rascalcore::compile::Examples::Tst0", getRascalCorePathConfig());
    return msgs;
}