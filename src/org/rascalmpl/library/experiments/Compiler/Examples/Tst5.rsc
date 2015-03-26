module experiments::Compiler::Examples::Tst5

import lang::rascal::tests::types::StaticTestingUtils;

value main(list[value] args) {											
	makeModule("M1", "import lang::rascal::\\syntax::Rascal;

						public int tmpVar = -1;  
						
						public str nextTmp(){
						    tmpVar += 1;
						    return \"TMP\<tmpVar\>\";
						}
						
						str getLabel(Label label) =
						  (label is \\default) ? \"\<label.name\>\" : nextTmp();");		 
	makeModule("M2", "import M1;");
	return checkOK("true;", importedModules=["M1", "M2"]);
}

//value main(list[value] args) {
//	bool f(bool c = false){
//		void g(){
//			c = true;
//		}
//		g();
//		return c;
//	}
//	return f() == true;
//}