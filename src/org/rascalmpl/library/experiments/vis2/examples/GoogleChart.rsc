module experiments::vis2::examples::GoogleChart
import experiments::vis2::FigureServer;
import experiments::vis2::FigureNew;
//import lang::java::jdt::m3::Core;
//import analysis::m3::Core;
//import lang::java::m3::TypeSymbol;
//import lang::java::m3::Registry;
//import lang::java::m3::AST; 
import Prelude;

void ex(str title, Figure f){
	render(title, f);
}

//map[str, int] toMapSrcLength(list[tuple[str, int]] r) {
//    return (s:l| <s,l><-r);
//    }
//    
//map[str, int] toMapSrcNproc(M3 model, lrel[str, int] r) {
//    map[str, int] m = (d:0|d<-toMapSrcLength(r));
//    for (e<-declaredMethods(model)) {
//         m[resolveJava(e[0]).file] += 1;
//         }
//    return m;
//    }
//    
// lrel[int, int, str] combine(map[str, int] a, map[str, int] b) {
//    lrel[int, int, str] r = [<a[s], b[s], "\"<s>\""> | s<- a];
//    return r;
//    }
//
//public  lrel[int, int, str] initialize(loc project) {
//     M3 model = createM3FromEclipseProject(project);
//     
//     lrel[str, int] file2length = [<resolveJava(e).file, getFileLength(resolveJava(e))> | e<-classes(model)];
//     lrel[int, int, str] r= combine(
//             toMapSrcLength(file2length),
//             toMapSrcNproc(model, file2length)
//             ); 
//     return r;                   
//}
//
//list[list[value]] t(lrel[int, int, str] q)  = [/*["\"\""]+*/[z  | z<-d ] | d<-q];

/*
var options = {title:figure.name,
                        hAxis: {title:figure.columns[1].label, minValue: 0, maxValue: 15},
                        vAxis: {title:figure.columns[2].label, minValue: 0, maxValue: 15},              
                        width:w,
                        height:h,
                        forceIFrame:true,
                        legend: 'none',
                        lineWidth: 0,
                        pointSize: 3,
                        sizeAxis: {minSize:3, maxSize:3}
                       };
                       
 ex("sinAndCosChart", 
        	combo(charts=[line([<x, round(sin(x/10),0.01)>               | x <- [0.0, 1.0 .. 100.0]], name="Sine Wave"),
        		   line([<x, round(0.5 * cos(x/10), 0.01)>        | x <- [0.0, 1.0 .. 100.0]], name ="Cosine Wave"),
        		   line([<x, round(0.25 * sin(x/10) + 0.5, 0.01)> | x <- [0.0, 1.0 .. 100.0]], name= "Another sine wave")
        			],
        	options = chartOptions(curveType="function",
           		hAxis = axis(title="Time"), 
           		vAxis = axis(title="Voltage"),
           		width=500,
                height=200
        	    )
           
        ));
 */

public void Main() {
    // list[list[value]] r = t(initialize(|project://ambidexter|));
    // list[list[value]] r = t(initialize(|project://rascal-eclipse|));
    //hAxis: {title:figure.columns[1].label, minValue: 0, maxValue: 15},
     // vAxis: {title:figure.columns[2].label, minValue: 0, maxValue: 15}, 
    r = [[1,2,"\"aap\""],[3,4,"\"noot\""]];    
    ChartOptions options = chartOptions(
    title= "test3", 
    hAxis= axis(title="Length", viewWindow= viewWindow(min=0)),
    vAxis= axis(title="#Proc"),
    lineWidth = 0, pointSize = 3, width = 400, height = 400);
    ex("test5", combo(charts = [line([<1,2>,<3,4>], name="aap")], options = options));      
    }