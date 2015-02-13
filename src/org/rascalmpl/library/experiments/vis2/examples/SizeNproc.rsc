module experiments::vis2::examples::SizeNproc
import experiments::vis2::FigureServer;
import experiments::vis2::Figure;
import lang::java::jdt::m3::Core;
import analysis::m3::Core;
import lang::java::m3::TypeSymbol;
import lang::java::m3::Registry;
import lang::java::m3::AST; 
import Prelude;

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
    //hAxis: {title:figure.columns[1].label, minValue: 0, maxValue: 15},
    // vAxis: {title:figure.columns[2].label, minValue: 0, maxValue: 15},   
    ChartOptions options = chartOptions(
    title= f.authority, 
    hAxis= axis(title="Length", viewWindow= viewWindow(min=0)),
    vAxis= axis(title="#Proc"),
    lineWidth = 0, pointSize = 3, width = 400, height = 400);
    ex("sizeNproc", combo(charts = [line(r, name=f.authority)], options = options, tickLabels = false));      
    }