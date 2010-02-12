module org::meta_environment::rascal::checker::Namespace

import org::meta_environment::rascal::checker::Types;

import List;
import Graph;
import IO;

import languages::rascal::syntax::Rascal;

data NamespaceItem =
	  ModuleNI(RName moduleName)
	| FunctionNI(RName functionName, list[RType] formalParamTypes, RType returnType)
        | VarNI(RName varName, Tags tags, Visibility vis, RType rtype)
;

alias NamespaceEntry = list[NamespaceItem];

alias Namespace = Graph[NamespaceEntry];

public Namespace nspace = {};
public tuple[Namespace ns, NamespaceEntry cur] buildns = < {}, [ModuleNI(RSimpleName("Start"))] > ;

public void buildNamespace(Tree t) {
	top-down visit(t) {
		case (Module) `<Header h> <Body b>` : handleModuleHeader(h);
		case (Declaration) `<Tags ts> <Visibility v> <Type t> <{Variable ","}+ vs> ;` : addVarItems(ts,v,t,vs);
	}
}

public void addVarItems(Tags ts, Visibility v, Type t, {Variable ","}+ vs) {
	println("Adding variables in declaration...");
	for (vb <- vs) {
		switch(vb) {
			case `<Name n>` : {
				println("Adding variable <n>");
				NamespaceItem ni = VarNI(convertName(n), ts, v, convertType(t));
				NamespaceEntry ne = buildns.cur + [ ni ];
				buildns = < buildns.ns + { <buildns.cur, ne> }, buildns.cur >;
			}
				
			case `<Name n> = <Expression e>` : {
				println("Adding variable <n>");
				NamespaceItem ni = VarNI(convertName(n), ts, v, convertType(t));
				NamespaceEntry ne = buildns.cur + [ ni ];
				buildns = < buildns.ns + { <buildns.cur, ne> }, buildns.cur >;
			}
		}
	}
}

public void handleModuleHeader(Header h) {
	switch(h) {
		case `<Tags t> module <QualifiedName n> <Import* i>` : {
			NamespaceItem ni = ModuleNI(convertName(n));
			buildns = < buildns.ns, [ni] > ;
		}		

		// We may need params later -- ignore for now
		case `<Tags t> module <QualifiedName n> <ModuleParameters p> <Import* i>` : {
			NamespaceItem ni = ModuleNI(convertName(n));
			buildns = < buildns.ns, [ni] > ;
		}		

	}
}