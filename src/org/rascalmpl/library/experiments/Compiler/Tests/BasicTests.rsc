module experiments::Compiler::Tests::BasicTests

//list[str] basicTests() =
//    ["lang::rascal::tests::basic::" + m | 
//     m <- [ "Booleans",
//            "Equality",
//            "Functions",
//            "Integers",
//            "IO",
//            //"IsDefined",
//            "ListRelations",
//            "Lists",
//            "Locations",
//            "Maps",
//            "Matching",
//            "Memoization",
//            "Nodes",
//            "Overloading",
//            "Relations" ,
//            "Sets",
//            "SolvedIssues",
//            "Strings",
//            "Tuples"                    
//            ]];
//            
//list[str] functionalityTests() = 
//    ["lang::rascal::tests::functionality::" + m 
//    | m <- [
//            "AccumulatingTests",        // OK
//            "AliasTests",               // OK
//            "AnnotationTests",          // OK
//            "AssignmentTests",          // OK
//            "BacktrackingTests",        // OK
//            "CallTests",                // OK
//            "CallTestsAux",
//            "ComprehensionTests",       // OK, 3 tests fail that correspond to empty enumerations: interpreter gives false, compiler gives true.
//            "ConcretePatternTests1",    // OK
//            "ConcretePatternTests2",    // OK
//            "ConcretePatternTests3",    // OK
//            "ConcreteSubscriptAndSliceTests",   // OK
//            "ConcreteSyntaxTests1",     // OK
//            "ConcreteSyntaxTests2",     // OK
//            "ConcreteSyntaxTests3",     // OK
//            "ConcreteSyntaxTests4",     // OK
//            "ConcreteTerms",            // OK
//            "DataDeclarationTests",     // OK
//            "DataTypeTests",            // OK
//            "DeclarationTests",         // OK
//            "FunctionCompositionTests", // OK
//            "InterpolationTests",
//            "KeywordParameterImportTests",
//            "KeywordParameterTests",
//            "ParsingTests",
//            "PatternTests",             // OK
//            "PatternDescendantTests",
//            "PatternList3Tests",
//            "ProjectionTests",          // OK
//            "RangeTests",               // OK, 4 tests fail but this is due to false 1. == 1.0 comparisons.
//            "ReducerTests",             // OK
//            "RegExpTests",              // OK
//            //"ScopeTests",             // OK OutOfMemory????
//            "SetMatchTests1",           // OK
//            "SetMatchTests2",           // OK
//            "StatementTests",           // OK
//            "SubscriptTests",           // OK
//            "TryCatchTests",            // OK                   
//            "VisitTests"                // OK
//            ]];

list[str] libraryTests() = 
    [ "lang::rascal::tests::library::" + m
    | m <- [
            //"BooleanTests",             // OK
            //"IntegerTests",             // OK
            //"ListRelationTests",
            //"ListTests" ,               // OK
            //"MapTests",                 // OK
            //"MathTests" ,               // OK
            //"NodeTests",                // OK
            //"NumberTests",              // OK
            //"RelationTests",            // OK
            //"SetTests",                 // OK
            //"StringTests",              // OK
            //"TypeTests",
            //"ValueIOTests",
            //"analysis::graphs::GraphTests",
            //"analysis::statistics::DescriptiveTests",
            "analysis::statistics::RangeUtils"
            //"lang::csv::CSVIOTests",      // OK
            //"lang::json::JSONIOTests"    // OK
     ]];

//list[str] importTests() = 
//    ["lang::rascal::tests::imports::" + m
//    | m <- [
//            "ImportTests1",             // OK
//            "ImportTests2",             // OK
//            "ImportTests3",             // OK
//            "ImportTests4",             // OK
//            "ImportTests5",             // OK
//            "ImportTests6",             // OK
//            "ImportTests7",              // OK
//            "ImportTests8",              // OK
//            "ModuleInitRange"
//    ]];   
    
//private list[str] extendTests  = 
//    ["lang::rascal::tests::extends::" + m
//    | m <- [ "ABSTRACTTYPE",
//            "A1",
//            "A2",
//            "B1",
//            "B2",
//            "B3",
//            "PARSETREE",
//            "TYPE",
//            "UseImportBase",
//            "UseImportBaseExtended",
//            "UseExtendBase",
//            "UseExtendBaseExtended"
//            ]];           