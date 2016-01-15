module experiments::vis2::sandbox::Test
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import util::ShellExec;
import util::Reflective;

import Prelude;


      
 Figures tst(bool include) = 
                 [box(50,50,"green", include=include)   
                 , box("Hallo", 20, "darkred", 1.7, "antiquewhite", include = include)
                 ,box(box(50, 50,"red", 8, "gold"),topLeft,1.0,"antiquewhite", 8, "blue", include=include)
                 ,box(box(50, 50,"red"),topLeft,1.5,"antiquewhite", include = include)
                 ,box(box(50, 50,"red"),centerMid,1.5,"antiquewhite", include = include)
                 ,box(box(50, 50,"red"),bottomRight,1.5,"antiquewhite", include = include)
                 ,box(50,50, box(0.75, "yellow"), topLeft, "green", include = include)
                 ,box(50,50, box(0.75, "yellow"), centerMid, "green", include = include)
                 ,box(50,50, box(0.75, "yellow"), bottomRight, "green", include = include)
                 ,hcat([box(30, 30, "blue"), box(50, 50, "yellow"), box(70, 70, "red")],topLeft,0,  include = include)
                 ,hcat([box(30, 30, "blue"), box(50, 50, "yellow"), box(70, 70, "red")], centerMid,0,  include = include)
                 ,hcat([box(30, 30, "blue"), box(50, 50, "yellow"), box(70, 70, "red")], bottomRight,0, include = include)
                 ,hcat(200, 70, [box(1.0, "blue"), box(0.5, "yellow"), box(1.0, "red")], bottomLeft,0,  include = include)
                 ,vcat(200, 70, [box(1.0, "blue"), box(0.5, "yellow"), box(1.0, "red")], bottomLeft,0,  include = include)
                 ,vcat(["a","bb","ccc"], 14, "blue", topRight, 1, include=include)
                 ,grid(200, 70, [[box(0.5, "blue")], [box(0.3, "yellow"), box(0.5, "red")]], bottomLeft,0,  0, include = include)
                 ,grid(200, 70, [[box(0.5, "blue")], [box(0.3, "yellow"), box(0.5, "red")]], centerMid,0,  0, include = include)
                 ,graph(200, 200, [box("aap",14, "blue", 1.6, "beige"), box("noot",14, "red", 1.6, "beige")],[<0, 1>]
                               ,include = include)
                 ];
                 
 Figure tests(bool include) = vcat(borderWidth = 2,borderColor="black", vgap=4, figs=tst(include));
 
 public void ttests() = render(tests(true));
 
 public void ftests(loc l, bool include) = writeFile(l, toHtmlString(
   tests(include)
 ));
 
 public void standard() {
      str dirN = "/tmp/rascal";
      loc dir = |file://<dirN>|;
      loc ok = dir+"ok.html";
      ftests(ok, false);
      }
 
 public str compare() {
     str dirN = "/tmp/rascal";
     loc dir = |file://<dirN>|;  
     str ok = readFile(dir+"ok.html"); 
     str check = toHtmlString(tests(false));
     return diff(ok, check);
     // loc check = dir+"check.html";
     // ftuts(check, false);
     // return exec("diff", workingDir = dir, args = ["check.html", "ok.html"]);
     }
                 