module experiments::vis2::sandbox::Shortcuts
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;
import util::Reflective;
import Prelude;


str nameA(Alignment a) {
    str r = "";
    if (a[1]<0.25) r = "top";
    else
    if (a[1]>0.75) r = "bottom";
    else r = "center";
    if (a[0]<0.25) r += "Left";
    else
    if (a[0]>0.75) r += "Right";
    else r += "Mid";
    return r;
    }

// Dimensions of nodes in tree must be known in advance    
Figure pnode(str s, str t) = box(size=<size(s)*10, (size(findAll(s,"\n"))+1)*20>, fillColor="whitesmoke", fig=text(s, fontSize=12)
               , tooltip=  at(5, 15, box(t, 12, "blue", 1.2, "white")));
              
Figure rnode(str s) = box(size=<size(s)*12, 20>,fillColor="antiquewhite", fig=htmlText(s, fontSize=14));

Figure tr(Figure root, Figures args) = tree(root, args, manhattan = true);
    
Figure treeF(Figure f) {
    Figure root = box(5, 5, "blue");
    Figures r =[];
    map[str, value] m = getKeywordParameters(f);
    if (m["width"]?) r += pnode("<f.width>","width");
    if (m["height"]?) r += pnode("<f.height>","height"); 
    if (box():=f) {    
        if (m["fig"]?) 
                 if (text(str s):=f.fig) {
                         Figure g = f.fig; 
                         r += pnode(s, "content");                                  
                         r += pnode("<g.fontSize>", "fontSize");  
                         r += pnode("<g.fontColor>", "fontColor");       
                 } else
                 r += treeF(f.fig);
        if (m["align"]?) r+= pnode(nameA(f.align),"align");
        if (m["shrink"]?) r += pnode("<f.shrink>","shrink");
        if (m["grow"]?) r += pnode("<f.grow>","grow");
        if (m["fillColor"]?) r += pnode("<f.fillColor>","fillColor");
        if (m["lineWidth"]?) r += pnode("<f.lineWidth>", "lineWidth");
        if (m["lineColor"]?) r += pnode("<f.lineColor>","lineColor");
        return tr(rnode("box"), r);
        }
    if (vcat():=f || hcat():=f) {
        Figures s= [((text(str q):=x)?pnode(q, "content"):treeF(x))|x<-f.figs];
        if (m["fontSize"]?) r += pnode("<f.fontSize>", "fontSize");  
        if (m["fontColor"]?) r += pnode("<f.fontColor>", "contColor");  
        if (m["align"]?) r+= pnode(nameA(f.align),"align");
        if (m["vgap"]?) r+= pnode("<f.vgap>","vgap");
        if (m["hgap"]?) r+= pnode("<f.hgap>","hgap");
        return tr(rnode(getName(f)), [tr(pnode("figs","figs"), s)]+r);
        }
    if (grid():=f) {
        Figures u =[];
        for (z<-f.figArray) {
            Figures s= [treeF(x)|x<-z];
            u+= tr(rnode("row"), s);
            }
        if (m["align"]?) r+= pnode(nameA(f.align),"align");
        if (m["hgap"]?) r+= pnode("<f.hgap>","hgap");
        if (m["vgap"]?) r+= pnode("<f.vgap>","vgap");
        return tr(rnode("grid"),[tr(pnode("figArray", "figArray"), u)]+r);
        }
     if (graph():=f) {
        Figures nodes = [treeF(p[1])|p<-f.nodes];
        Figure u = tr(rnode("node"), nodes);
        Figures edges = [rnode("\<<e.from>, <e.to>\>")|Edge e <-f.edges];
        Figure v = tr(rnode("edge"), edges);
        return tr(rnode("graph"), r+[u, v]);
        }
    return root;
    }
	
str nameF(Figure f) {
    list[str] r = [];
    map[str, value] m = getKeywordParameters(f);
    if (m["width"]?) r += "<f.width>";
    if (m["height"]?) r += "<f.height>"; 
    if (box():=f) {    
        if (m["fig"]?) {
                     if (text(str s):=f.fig) {
                         Figure g = f.fig; 
                         r += s;                                  
                         r += "<g.fontSize>";  
                         r += "<g.fontColor>";       
                     }
                     else
                         r += nameF(f.fig);
                     }
        if (m["align"]?) r+= nameA(f.align);
        if (m["shrink"]?) r += "<f.shrink>";
        if (m["grow"]?) r += "<f.grow>";
        if (m["fillColor"]?) r += "<f.fillColor>";
        if (m["lineWidth"]?) r += "<f.lineWidth>";
        if (m["lineColor"]?)r += "<f.lineColor>";     
        return "box(<intercalate(",", r)>)";
        }
    if (vcat():=f || hcat():=f) {
        list[str] s= [
            ((text(str q):=x)?q:nameF(x))|x<-f.figs];
        r+= "[<intercalate(",", s)>]";
        if (m["fontSize"]?) r += "<f.fontSize>";  
        if (m["fontColor"]?) r += "<f.fontColor>";  
        if (m["align"]?) r+= nameA(f.align);
        if (m["vgap"]?) r+= "<f.vgap>";
        if (m["hgap"]?) r+= "<f.hgap>";
        return "<getName(f)>(<intercalate(",", r)>)";
        }
    if (grid():=f) {
        list[str] u =[];
        for (z<-f.figArray) {
            list[str] s= [nameF(x)|x<-z];
            u+= "[<intercalate(",", s)>]";
            }
        r+="[<intercalate(",", u)>]";
        if (m["align"]?) r+= nameA(f.align);
        if (m["hgap"]?) r+= "<f.hgap>";
        if (m["vgap"]?) r+= "<f.vgap>";
        return "grid(<intercalate(",", r)>)";
        }
    if (graph():=f) {
        list[str] nodes = [nameF(p[1])|p<-f.nodes];
        r+= "[<intercalate(",", nodes)>]";
        list[str] edges = ["\<<e.from>, <e.to>\>"|Edge e<-f.edges];
        r+= "[<intercalate(",", edges)>]";
        return "graph(<intercalate(",", r)>)";
        }
    return "<f>";
    }

Figure incl(bool include, Figure f, bool extra = true) =	
	include?vcat(borderWidth = 1, borderColor="grey",
        figs= [box(fillColor = "whitesmoke", size=<800, 30>, align = centerMid, fig=htmlText(nameF(f)
      , fontStyle="italic", fontWeight="bold", fontColor= "darkslategray"))
       ,box(size=<200, 100>, fig=f)]
       + (extra?[treeF(f)]:[])
    )
    :
    f
    ;
    
// ---  Shortcuts 


public Figure box(int width, int height, str fillColor, bool include = false) = incl(include,
      box(width= width, height = height, fillColor= fillColor));

public Figure box(int width, int height, str fillColor, int lineWidth, str lineColor, bool include = false) = 
      incl(include,
      box(width= width, height = height, fillColor= fillColor, lineWidth = lineWidth, lineColor = lineColor));
      
public Figure box(int width, int height, Figure fig, Alignment align, str fillColor, bool include = false) = incl(include,
      box(width= width, height = height, fig = fig, align = align, fillColor= fillColor));

public Figure box(int width, int height, Figure fig, Alignment align, str fillColor, int lineWidth, str lineColor, bool include = false) = 
      incl(include,
      box(width= width, height = height, fig = fig, align = align, fillColor= fillColor, lineWidth = lineWidth, lineColor = lineColor));
      
public Figure box(Figure fig, Alignment align, num grow,str fillColor, bool include = false) = incl(include,
      box(fig= fig, align = align, grow = grow, fillColor = fillColor
       ));

public Figure box(Figure fig, num shrink, str fillColor, bool include = false) = incl(include,
      box(fig= fig, shrink = shrink, fillColor = fillColor));

public Figure box(Figure fig, Alignment align, num grow,str fillColor, int lineWidth, str lineColor, bool include = false) = 
        incl(include,box(fig= fig, align = align, grow = grow, fillColor = fillColor, lineWidth = lineWidth, lineColor = lineColor
        ));
        
public Figure box(num shrink, str fillColor, bool include = false) = 
        incl(include,box(shrink = shrink, fillColor = fillColor
        ));

public Figure box(Figure fig, num shrink, str fillColor, bool include = false) = 
        incl(include,box(fig= fig, shrink = shrink, fillColor = fillColor
        ));
        
public Figure box(num shrink, str fillColor, int lineWidth, str lineColor, bool include = false) = 
        incl(include,box(shrink = shrink, fillColor = fillColor, lineWidth = lineWidth, lineColor = lineColor
        ));

public Figure box(Figure fig, num shrink, str fillColor, int lineWidth, str lineColor, bool include = false) = 
        incl(include,box(fig= fig, shrink = shrink, fillColor = fillColor, lineWidth = lineWidth, lineColor = lineColor
        ));
        
public Figure box(str name, int fontSize, str fontColor, num grow, str fillColor, bool include = false) = incl(include,
      box(fig= text(name, fontSize = fontSize, fontColor = fontColor), grow = grow, fillColor = fillColor //  , lineWidth = 0
       ));
        
public Figure hcat(list[Figure] f,Alignment align, int hgap,  bool include = false) = 
     incl(include, hcat(figs= f, align = align, hgap = hgap));
     
public Figure hcat(int width, int height, list[Figure] f, Alignment align, int hgap,  bool include = false) = 
     incl(include, hcat(width=width, height = height, figs= f, align = align, hgap = hgap));
     
public Figure hcat(list[str] ts, int fontSize, str fontColor, Alignment align, int hgap,  bool include = false) = 
     incl(include, hcat(figs= [text(s, fontSize=fontSize, fontColor=fontColor)|str s<-ts], align = align, hgap = hgap));
     
public Figure vcat(list[Figure] f,Alignment align, int vgap,  bool include = false) = 
     incl(include, vcat(figs= f, align = align, vgap = vgap));
     
public Figure vcat(int width, int height, list[Figure] f, Alignment align, int vgap,  bool include = false) = 
     incl(include, vcat(width=width, height = height, figs= f, align = align, vgap = vgap));
     
public Figure vcat(list[str] ts, int fontSize, str fontColor, Alignment align, int vgap,  bool include = false) = 
     incl(include, vcat(figs= [text(s, fontSize=fontSize, fontColor=fontColor)|str s<-ts], align = align, vgap = vgap));
     
public Figure grid(list[list[Figure]] f,Alignment align, int hgap,  int vgap, bool include = false) = 
     incl(include, grid(figArray= f, align = align, hgap = hgap));
     
public Figure grid(int width, int height,list[list[Figure]] f, Alignment align, int hgap,  int vgap, bool include = false) = 
     incl(include, grid(width=width, height = height, figArray= f, align = align, hgap = hgap, vgap = vgap));
  
public Figure graph(int width, int height, list[Figure] nodes, list[tuple[int, int]] edges, bool include= false) {
       list[tuple[str, Figure]] n = [];
       for (int i<-[0..size(nodes)]) {
            n+= <"<i>", nodes[i]>;
            }
       list[Edge] e = [edge("<p[0]>", "<p[1]>")|p<-edges];
       return incl(include, graph(n, e, width = width, height = height));
       } 
       
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
                 
 Figure tsts(bool include) = vcat(borderWidth = 2,borderColor="black", vgap=4, figs=tst(include));
 
 public void ttsts() = render(tsts(true));
 
 public void ftsts(loc l, bool include) = writeFile(l, toHtmlString(
   tsts(include)
 ));  
	