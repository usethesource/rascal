module experiments::Compiler::Examples::Tst6

import IO;
import String;
import util::Reflective;

loc getDerivedLocation(loc src, str extension, loc bindir = |home:///bin|){
	if(src.scheme == "std"){
		phys = getModuleLocation(src.path);
		return (bindir + phys.authority + phys.path)[extension=extension];
	}
	return (bindir + src.authority + src.path)[extension=extension];
}

loc cachedConfig(loc src, loc bindir) = (bindir + getModuleLocation(src.path).path)[extension="tc"];
loc cachedHash(loc src, loc bindir) = (bindir + getModuleLocation(src.path).path)[extension="sig"];
loc cachedHashMap(loc src, loc bindir) = (bindir + getModuleLocation(src.path).path)[extension="sigs"];

str basename(loc l) = l.file[ .. findFirst(l.file, ".")];  // TODO: for library

loc RVMProgramLocation(loc src, loc bindir) = (bindir + getModuleLocation(src.path).path)[extension="rvm"];

loc RVMExecutableLocation(loc src, loc bindir) = (bindir + getModuleLocation(src.path).path)[extension="rvm.ser"];

loc MuModuleLocation(loc src, loc bindir) = (bindir + getModuleLocation(src.path).path)[extension="mu"];


void printAll(loc L){
	bindir = |home:///bin|;

	//println("cachedConfig(<L>, <bindir>): <cachedConfig(L, bindir)>");
	//println("cachedHash(<L>, <bindir>): <cachedHash(L, bindir)>");
	//println("cachedHashMap(<L>, <bindir>): <cachedHashMap(L, bindir)>");
	//
	//println("RVMProgramLocation(<L>, <bindir>): <RVMProgramLocation(L, bindir)>");
	//println("RVMExecutableLocation(<L>, <bindir>): <RVMExecutableLocation(L, bindir)>");
	//println("MuModuleLocation(<L>, <bindir>): <MuModuleLocation(L, bindir)>");
	
	println("getModuleLocation(<L>): <getModuleLocation(L.path)>");
	println("getSearchPathLocation(<L>): <getSearchPathLocation(L.path)>");
	println("makeBinDerivative: <makeBinDerivative(L, "rvm")>");
	
	

}

void main(list[value] args){
	printAll(|std:///lang/rascal/tests/extends/Base.rsc|);
	
	println("-----------");
	//printAll(|project://rascal/src/org/rascalmpl/library/experiments/Compiler/Examples/Tst4.rsc|);
	println("makeBinDerivative: <makeBinDerivative(|project://rascal/src/org/rascalmpl/library/experiments/Compiler/Compile.rsc|, "rvm")>");
}