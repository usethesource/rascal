module experiments::vis2::examples::ImportGraph
import lang::java::jdt::m3::AST;
import Prelude;
import experiments::vis2::FigureServer;
import experiments::vis2::Figure;
loc project = |project://ambidexter|;


void ex(str title, Figure f){
	render(title, f);
}

public void main() {
    set[Declaration] declUnits = createAstsFromEclipseProject(project, false);
    XYLabeledData r = [];
    for (declUnit<-declUnits) {
    if (
       compilationUnit(Declaration decl, list[Declaration] imports, list[Declaration] types)
            :=  declUnit) {
            
            if (package(_, str package) := decl) {
            int nImports = 0;
              top-down-break visit (imports)  {
                 case \import(str name): 
                   if (/AmbiDexter\.<rest:.*>/:=name) nImports = nImports+1;
                 }
             top-down-break visit (types) {
             case \class(str name,_, _,list[Declaration] body): 
                  {
                    // println("HHHH<package>.<name>"); 
                    r += [<size(body),  nImports, "<package>.<name>" >];    
                  }
                }
              
             }  
         }   
       }
       println(size(r));
        ex("importGraph", combo(charts = [bar(r)], tickLabels = true,  tooltipColumn = 0, 
           	    options = chartOptions(
           		hAxis = axis(title="Length", slantedText = true, slantedTextAngle=90), 
           		vAxis = axis(title="Imports"),
           		chartArea = chartArea(width="80%", height = "40%", backgroundColor="antiquewhite"),
           		bar = bar(groupWidth = "80%"),
           		width=2000,
                height=400,
                legend = legend(position="top")))) ; 
    }
    
