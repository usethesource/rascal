module experiments::vis2::examples::ImportGraph

import experiments::vis2::FigureServer;
import experiments::vis2::Figure;
import lang::java::jdt::m3::Core;
import analysis::m3::Core;
import lang::java::m3::TypeSymbol;
import lang::java::m3::Registry;
// import lang::java::m3::AST;
import lang::java::jdt::m3::AST; 
import Prelude;
import lang::dot::Dot;

void ex(str title, Figure f){
	render(title, f);
}

public XYLabeledData initialize(loc project) {
     M3 model = createM3FromEclipseProject(project);
     // println(declaredMethods(model));
     // println(declaredTopTypes(model));
     map[loc, int] m = (d:getFileLength(|file:///|+d.path)|
                        d<-files(model));
     rel[loc, loc] r = {<d, d> |d<-files(model)} o declaredTopTypes(model) o declaredMethods(model);
     rel[loc, int] z = {<d, size(domainR(r, {d}))>|d<-domain(r)};
     XYLabeledData y = [<m[x[0]], x[1], x[0].file>|x<-z];
     return y;                   
}


public void sizeNproc() {
    loc f = |project://dotplugin|;
    XYLabeledData r = initialize(f); 
    ChartOptions options = chartOptions(
    title= f.authority, 
    hAxis= axis(title="Length", viewWindow= viewWindow(min=0)),
    vAxis= axis(title="#Proc"),
    width = 400, height = 400 ,animation= animation(startup = true, ease = "out", duration = 1000));
    // ex("sizeNproc", combochart(charts = [line(r, name=f.authority)], options = options, tickLabels = false)); 
    ex("sizeNproc", scatterchart(r, options = options, tickLabels = true));           
    }
    
str getName(Declaration d) {
    // println(delAnnotationsRec(d));   
    switch(d) {
        case \method(Type \return, str name, list[Declaration] parameters, list[Expression] exceptions, Statement statement):
            return name;
        case \constructor(str name, list[Declaration] parameters, list[Expression] exceptions, Statement impl):
            return name;
        default: return "";
        }
    }
    
set[str] calls(Declaration d, set[str] names) {
    set[str] r = {};
    visit(d) {
        case \methodCall(bool isSuper, str name, list[Expression] arguments): 
             if (name in names) r+= name;
        }
    return r;
    }
    
str getClassName(Declaration d) {
    top-down-break visit(d) {
         case \class(str name, list[Type] extends, list[Type] implements, list[Declaration] body): return name;
         }
    return "";
    }
    
str getPackageName(Declaration package) {
     if (\package(str name):=package) return name;
     if (\package(Declaration parentPackage, str name):=package)  return "<getPackageName(parentPackage)>.<name>";
    return "";
    }
    
public list[list[value]] getImportGraph() {
    loc project = |project://ambidexter|;
    //M3 model = createM3FromEclipseProject(project);
    //rel[loc, loc] t = declaredMethods(model);
    //set[str] names = {getName(getMethodASTEclipse(u, model= model))|u<-range(t)};
    //println(names);
    //for (u<-range(t)) {       
    //    println("Defined: <getName(getMethodASTEclipse(u, model= model))>");
    //    println("Calls: <calls(getMethodASTEclipse(u, model= model), names)>");
    //    }
    set[Declaration] declaration = createAstsFromEclipseProject( project, false);
    list[str] modul = [];
    
    for (d<-declaration) {
        top-down-break visit(d) {
            // case  \package(Declaration parentPackage, str name):  modul += name;
            case \compilationUnit(Declaration package, list[Declaration] imports, list[Declaration] types):
                  {
                  str packageName =getPackageName(package);
                  list[str] cl = ["<packageName>.<getClassName(t)>"|t<-types];
                  modul += cl;
            }
        } 
    }
    // println(modul);
    list[list[value]] r =[];
    // println(imports); 
    for (d<-declaration) {
    top-down-break visit(d) {
            // case  \package(Declaration parentPackage, str name):  modul += name;
            case \compilationUnit(Declaration package, list[Declaration] imports, list[Declaration] types):
                  {
                  str packageName =getPackageName(package);
                  list[str] cl = ["<packageName>.<getClassName(t)>"|t<-types];
                  for (c<-cl)
                     for (Declaration e<-imports)
                       if (\import(str name):=e  && name in modul) r+=[[name, c, 1]];
                     }
            }
       } 
       return r;  
    }
    
public void importgraph() {
    list[list[value]] r = getImportGraph();
    r=[["Class", "usedIn","1"]]+r;
    ex("importGraph", sankey(r, options = chartOptions(width = 400, height = 200, sankey = Sankey::sankey(
             \node = sankeyNode(width=5, label = sankeyLabel(fontSize=10)), \link = sankeyLink(color=sankeyColor(fill= "lightgray", stroke="black", strokeWidth=0))
             ))));
    }
    
public void dagre(){
	Figure b(str label) =  box(fig = text(label), fillColor="whitesmoke", rounded=<5,5>, gap=<5,5>, grow=1.2);
	list[list[value]] r = getImportGraph();
	set[str] d = {a|e<-r, str a:=e[0]}+{a|e<-r, str a:=e[1]};
	// println(d);
	states = [<e, b(e)>|e<-d];
    edges = [edge(a, z)|e<-r, str a:=e[0], str z:=e[1]];			
  	render("dagre", graph(nodes=states, edges=edges));
}

public void dot(){
	list[list[value]] r = getImportGraph();
	set[str] d = {a|e<-r, str a:=e[0]}+{a|e<-r, str a:=e[1]};
	// println(d);
	states = [N(e)|e <-d];
    edges = [E(a, z)|e<-r, str a:=e[0], str z:=e[1]];				
  	writeFile(|file:///ufs/bertl/aap.dot|, toString(digraph("importGraph", states+edges)));
}
    