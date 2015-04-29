module experiments::Compiler::Examples::Tst6

import IO;
import String;
import util::Reflective;

loc getDerivedLocation1(loc src, str extension, loc bindir = |home:///bin|){
	phys = getSearchPathLocation(src.path);
	subdir = phys.authority;
	if(subdir == ""){
		subdir = phys.scheme;
	}
	return (bindir + subdir + src.path)[extension=extension];
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
	
	println("getSearchPathLocation(<L>): <getSearchPathLocation(L.path)>");
	println("getDerivedLocation1: <getDerivedLocation1(L, "rvm")>");
	
}

void main(list[value] args){
	printAll(|std:///lang/rascal/tests/extends/Base.rsc|);
	println("-----------");
	printAll(|std:///experiments/Compiler/muRascal2RVM/LibraryGamma.mu|);
	println("-----------");
	printAll(|tmp:///M1.rsc|);
	println("-----------");
}