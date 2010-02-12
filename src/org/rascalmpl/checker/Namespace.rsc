module org::rascalmpl::checker::Namespace

import org::rascalmpl::checker::Types;

import List;
import Graph;
import IO;

import languages::rascal::syntax::Rascal;

private bool debug = true;

public void addVarItems(Tags ts, Visibility v, Type t, {Variable ","}+ vs) {
	// NOTE: Uncomment the following line to get the error...
	//if (debug) println("Adding variables in declaration...");
	println("Adding variables in declaration...");
	for (vb <- vs) {
		switch(vb) {
			case `<Name n>` : {
				if (debug) println("Adding variable <n>");
				//println("Adding variable <n>");
			}
				
			case `<Name n> = <Expression e>` : {
				if (debug) println("Adding variable <n>");
				//println("Adding variable <n>");
			}
		}
	}
}

public void buildNamespace(Tree t) {
	top-down visit(t) {
		case (Module) `<Header h> <Body b>` : {
			if (debug) print("Processing module...");
			//print("Processing module...");
			RName moduleName = handleModuleHeader(h);
			if (debug) 	print("... exploring body of module " + prettyPrintName(moduleName));
			//print("... exploring body of module " + prettyPrintName(moduleName));
			handleModuleBody(b);
			if (debug) 	println("... finished module body");
			//println("... finished module body");
		}
	}
}		

public RName handleModuleHeader(Header h) {
	switch(h) {
		case `<Tags t> module <QualifiedName n> <Import* i>` : {
			return convertName(n);
		}		

		case `<Tags t> module <QualifiedName n> <ModuleParameters p> <Import* i>` : {
			return convertName(n);
		}		

	}
}

public void handleModuleBody(Body b) {
	for (/Toplevel t <- b) {
		switch(t) {
			case (Toplevel) `<Tags ts> <Visibility v> <Type t> <{Variable ","}+ vs> ;` : addVarItems(ts,v,t,vs);

			case (Toplevel) `<Tags ts> <Visibility v> <Type theType> <FunctionModifiers theModifiers> <Name theName> <Parameters theParameters> ;` : addAbstractFunction(theType,theName);
 
			case (Toplevel) `<Tags ts> <Visibility v> <Type theType> <FunctionModifiers theModifiers> <Name theName> <Parameters theParameters> <FunctionBody theBody>` : addFunction(theType,theName,theBody);
			
			case (Toplevel) `<Tags ts> <Visibility v> <Type theType> <FunctionModifiers theModifiers> <Name theName> <Parameters theParameters> throws <{Type ","}+ theThrows> ;` : addAbstractFunction(theType,theName);
 
			case (Toplevel) `<Tags ts> <Visibility v> <Type theType> <FunctionModifiers theModifiers> <Name theName> <Parameters theParameters> throws <{Type ","}+ theThrows> <FunctionBody theBody>` : addFunction(theType,theName,theBody);
			
			default: println("No match for item");
		}
	}
}

public void addAbstractFunction(Type theType, Name theName, Parameters theParameters) {
	if (debug) println("Found function " + prettyPrint(convertName(theName)));
}

public void addFunction(Type theType, Name theName, Parameters theParameters, FunctionBody theBody) {
	if (debug) println("Found function " + prettyPrint(convertName(theName)));
}
