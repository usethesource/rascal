module experiments::Compiler::Examples::Tst5

import lang::rascal::tests::types::StaticTestingUtils;

value main(list[value] args) = 
	checkOK("13;", 
			importedModules = ["experiments::Compiler::Examples::\\syntax::Main"]
			//importedModules = ["experiments::Compiler::Examples::\\syntax::Main"]
		
			);