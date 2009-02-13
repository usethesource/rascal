module UnusedProcs

import Set;
import Relation;
import Graph;
import Benchmark;
import IO;

alias Proc = str;

public map[str, list[str]] metadeps =
("JJTraveler"            : [],
"asc-support"            : ["asf-support", "aterm", "c-library", "config-support", "error-support", "pt-support", "ptable-support", "relation-stores", "sglr", "tide-support", "toolbuslib"],
"asf"                    : ["asc-support", "asf-support", "aterm", "error-support", "pt-support", "ptable-support", "sdf-support", "tide-support", "toolbuslib"],
"asf-library"            : ["sdf-library"],
"asf-support"            : ["aterm", "error-support", "pt-support"],
"asfsdf-meta"            : ["asf", "aterm", "config-manager", "console-grabber", "meta", "rstore-container", "sdf-meta", "sdf-pretty", "toolbus-ng", "vis-base", "vis-plugin", "vis-plugin-charts", "vis-plugin-generic", "vis-plugin-graph"],
"aterm"                  : [],
"aterm-java"             : ["JJTraveler", "shared-objects"],
"c-library"              : ["aterm"],
"config-manager"         : ["aterm", "config-support", "error-support", "toolbuslib"],
"config-support"         : ["aterm"],
"console-grabber"        : ["aterm-java", "meta-studio", "toolbus-java-adapter"],
"console-gui"            : ["aterm-java", "meta-studio", "toolbus-java-adapter"],
"dialog-gui"             : ["aterm-java", "config-support", "meta-studio", "toolbus-java-adapter"],
"editor-manager"         : ["aterm", "toolbuslib"],
"editor-plugin"          : ["aterm-java", "config-support", "error-support", "meta-studio", "toolbus-java-adapter"],
"error-gui"              : ["aterm-java", "error-support", "meta-studio", "toolbus-java-adapter"],
"error-support"          : ["aterm", "toolbuslib"],
"graph-gui"              : ["aterm-java", "config-support", "graph-support", "meta-studio", "module-support", "toolbus-java-adapter"],
"graph-support"          : ["aterm", "error-support", "pt-support", "toolbuslib"],
"io-support"             : ["aterm", "config-support", "error-support", "toolbuslib"],
"meta"                   : ["aterm-java", "config-manager", "console-grabber", "console-gui", "dialog-gui", "editor-manager", "editor-plugin", "error-gui", "error-support", "graph-gui", "graph-support", "io-support", "meta-studio", "module-details-gui", "module-manager", "navigator-gui", "pandora", "progress-gui", "pt-support", "sglr", "structure-editor", "svg-gui", "term-store", "tide", "tide-support"],
"meta-studio"            : ["aterm-java", "config-support", "svg-support", "toolbus-java-adapter"],
"module-details-gui"     : ["aterm-java", "meta-studio", "module-support", "toolbus-java-adapter"],
"module-manager"         : ["aterm-java", "graph-support", "module-support", "toolbus-java-adapter"],
"module-support"         : [],
"navigator-gui"          : ["aterm-java", "config-support", "graph-support", "io-support", "meta-studio", "toolbus-java-adapter"],
"pandora"                : ["asc-support", "aterm", "pt-support", "toolbuslib"],
"pgen"                   : ["asc-support", "aterm", "c-library", "config-support", "error-support", "pt-support", "ptable-support", "sdf-support", "sglr", "toolbuslib"],
"progress-gui"           : ["aterm-java", "meta-studio", "toolbus-java-adapter"],
"pt-support"             : ["aterm", "error-support", "toolbuslib"],
"ptable-support"         : ["aterm", "pt-support"],
"relation-stores"        : ["aterm", "toolbuslib"],
"rstore-container"       : ["aterm-java", "config-support", "meta-studio", "relation-stores", "toolbus-java-adapter", "tunit"],
"sdf"                    : ["pandora", "pgen", "sglr"],
"sdf-library"            : [],
"sdf-meta"               : ["asf-library", "config-manager", "console-grabber", "meta", "pgen", "sdf", "sdf-library", "sdf-metrics", "sdf-support", "toolbus-ng"],
"sdf-metrics"            : ["asc-support"],
"sdf-pretty"             : [],
"sdf-support"            : ["aterm", "error-support", "pt-support", "toolbuslib"],
"sglr"                   : ["aterm", "c-library", "config-support", "error-support", "pt-support", "ptable-support", "toolbuslib"],
"shared-objects"         : [],
"sisyphus"               : [],
"structure-editor"       : ["aterm", "error-support", "pt-support", "toolbuslib"],
"svg-gui"                : ["aterm-java", "meta-studio", "svg-support", "toolbus-java-adapter"],
"svg-support"            : [],
"term-store"             : ["aterm", "toolbuslib"],
"tide"                   : ["aterm", "aterm-java", "meta-studio", "tide-support", "toolbus-java-adapter", "toolbuslib"],
"tide-support"           : ["aterm", "toolbuslib"],
"toolbus-java-adapter"   : ["aterm-java"],
"toolbus-ng"             : ["aterm-java"],
"toolbuslib"             : ["aterm"],
"tunit"                  : ["aterm-java", "toolbus-java-adapter"],
"vis-base"               : ["aterm-java", "config-support", "meta", "meta-studio", "relation-stores", "rstore-container", "toolbus-java-adapter", "tunit"],
"vis-plugin"             : ["aterm-java", "config-support", "meta-studio", "relation-stores", "toolbus-java-adapter", "tunit", "vis-base"],
"vis-plugin-charts"      : ["aterm-java", "config-support", "meta-studio", "relation-stores", "rstore-container", "toolbus-java-adapter", "tunit", "vis-plugin"],
"vis-plugin-generic"     : ["aterm-java", "config-support", "meta-studio", "relation-stores", "toolbus-java-adapter", "tunit", "vis-plugin"],
"vis-plugin-graph"       : ["aterm-java", "config-support", "meta-studio", "relation-stores", "toolbus-java-adapter", "tunit", "vis-plugin"]
);

public rel[str,str] makeGraph(map[str,list[str]] M)
{
	return {<from, to> | str from : M, str to : M[from]};
}

public set[Proc] unusedProcs(set[Proc] entryPoints, rel[Proc, Proc] Calls){

	rel[Proc,Proc] closureCalls = Calls+;

	return carrier(Calls) - closureCalls[entryPoints] - entryPoints;

}

public void test1(){
	start = currentTimeMillis();		
	result = unusedProcs({"a", "f"}, {<"a", "b">, <"b", "c">, <"b", "d">, <"d", "c">, <"d", "e">,
	                    <"f", "e">, <"f", "g">, <"g", "e">,
	                    <"x", "y">, <"y", "z">
	                    });
	used = currentTimeMillis() - start;
	println("unusedProcs = <result>  (<used> millis)");
}

public void test2(){
	start = currentTimeMillis();		
	result = unusedProcs({"meta-studio"}, makeGraph(metadeps));
	used = currentTimeMillis() - start;
	println("unusedProcs = <result>  (<used> millis)");
}
