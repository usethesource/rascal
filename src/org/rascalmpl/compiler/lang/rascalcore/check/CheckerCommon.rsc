@bootstrapParser
module lang::rascalcore::check::CheckerCommon

extend lang::rascalcore::check::CollectType;
extend lang::rascalcore::check::ComputeType;
extend lang::rascalcore::check::SyntaxGetters;
extend analysis::typepal::FailMessage;


void checkSupportedByParserGenerator(Tree t, Collector c){
    c.require("implemented by parsergenerator", t, [t], void(Solver s){
        tp = s.getType(t);
        if(isNonParameterizedNonTerminalType(tp)) return;
        s.report(info(t, "%t is possibly not yet supported by parsergenerator", tp));
    });
 }