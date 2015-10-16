module experiments::vis2::sandbox::GenTree
import Prelude;
import util::Math;
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure; 
import experiments::vis2::sandbox::Tree;

/*
rangeInput(num low=0, num high=100, num step=1, value \value = 50.0)
vcat([grid([
				[text(str () { return "hgap: <hg>";}),scaleSlider(int() { return 0; } ,int () { return 200; } , int () { return hg; },void (int s) { hg = s; })],
				[text(str () { return "vgap: <vg>";}),scaleSlider(int() { return 0; } ,int () { return 200; } , int () { return vg; },void (int s) { vg = s; })],
				[text(str () { return "minwidth: <minx>";}),scaleSlider(int() { return 0; } ,int () { return 400; } , int () { return minx; },void (int s) { minx = s; maxx = max(minx,maxx); })],
				[text(str () { return "maxwidth: <maxx>";}),scaleSlider(int() { return 0; } ,int () { return 400; } , int () { return maxx; },void (int s) { maxx = s;maxx = max(minx,maxx); })],
				[text(str () { return "minheigth: <miny>";}),scaleSlider(int() { return 0; } ,int () { return 400; } , int () { return miny; },void (int s) { miny = s; maxy = max(miny,maxy);})],
				[text(str () { return "maxheigth: <maxy>";}),scaleSlider(int() { return 0; } ,int () { return 400; } , int () { return maxy; },void (int s) { maxy = s; maxy = max(miny,maxy);})],
			86G	[text(str () { return "minDepth: <minDepth>";}),scaleSlider(int() { return 0; } ,int () { return 10; } , int () { return minDepth; },void (int s) { minDepth = s; maxDepth = max(maxDepth,minDepth); })],
				[text(str () { return "maxDepth: <maxDepth>";}),scaleSlider(int() { return 0; } ,int () { return 10; } , int () { return maxDepth; },void (int s) { maxDepth = s; maxDepth = max(maxDepth,minDepth); })],
				[text(str () { return "minKids: <minKids>";}),scaleSlider(int() { return 1; } ,int () { return 10; } , int () { return minKids; },void (int s) { minKids = s; maxKids = max(minKids,maxKids);})],
				[text(str () { return "maxKids: <maxKids>";}),scaleSlider(int() { return minKids; } ,int () { return 10; } , int () { return maxKids; },void (int s) { maxKids = s; maxKids = max(minKids,maxKids);})],
				[text(str () { return "leafChance: <leafChance>";}),scaleSlider(int() { return 0; } ,int () { return 100; } , int () { return leafChance; },void (int s) { leafChance = s; })],
				[space(), checkbox("Manhattan",false,void (bool b){ man = b; })]		
			])
*/
map[str, tuple[int, int]] m = ();

public Figure genTree(int leafChance,int hGap, int vGap, int minDepth,int maxDepth, int minKids, int maxKids, int minX, int minY, int maxX, int maxY
    , bool cityblock = false, Orientation orientation = topDown()){
    // println("genTree");
    idx = idx+1;
	Figure root = box(id= "a<idx>"
	 , fig = text("<idx>", fontSize=10)
	, fillColor=colors[arbInt(size(colors))], size=<minX + arbInt(maxX-minX), minY + arbInt(maxY -minY)>);
	m += (root.id:<root.size[0], root.size[1]>);
	if(maxDepth == 0 || minDepth <= 0 && arbInt(100) <= leafChance){ return tree(root,[]); }
	int nr = arbInt(maxKids-minKids) + minKids;	
	return tree(root,
		[ genTree(leafChance,hGap, vGap, minDepth-1,maxDepth-1,minKids,maxKids,minX,minY,maxX,maxY) | i <- [0..nr+1]]
		, cityblock = cityblock, orientation = orientation, xSeparation = hGap, ySeparation = vGap
		);	
}

int N = 50;

int idx = 0;

public Figure genTrees(int leafChance,int hGap, int vGap, int minDepth,int maxDepth, int minKids, int maxKids, int minX, int minY, int maxX, int maxY
    , bool cityblock = false, Orientation orientation = topDown()){  
    Figure r = emptyFigure();  
    for (int i<-[0,1 .. N]) {
       // println("Round <i>");
       idx  = 0 ;
	   Figure root = box(id="a<idx>", fillColor="yellow", size=<minX + arbInt(maxX-minX), minY + arbInt(maxY -minY)>);
	   m = (root.id:<root.size[0], root.size[1]>);
	   int d = arbInt(100);
	   // println("round: <i> <minDepth> <maxDepth> <minKids> <maxKids> <leafChance> <d> <maxDepth == 0 || minDepth <= 0 && d <= leafChance>");
	   if(maxDepth == 0 || minDepth <= 0 && d <= leafChance){ return tree(root,[]); }
	   int nr = arbInt(maxKids-minKids) + minKids;	
	   // println("nr = <nr>");
	   r = tree(root,
		[ genTree(leafChance,hGap, vGap, minDepth-1,maxDepth-1,minKids,maxKids,minX,minY,maxX,maxY) | int j <- [0..nr+1]]
		, cityblock = cityblock, orientation = orientation, xSeparation = hGap, ySeparation = vGap
		);
		// println(m);
		treeLayout(r, m, r.sX, r.sY, r.rasterHeight, r.cityblock);
		// println("genTrees");
		if (!isDisjunct()) {
		     println("WRONG GENTREES <i>");
		     return r;
        }
    } 
    println("Done N = <N>");  
    return r;	
}


map[str, int ] 
    state = ("hGap":2, "vGap":2
     ,"minWidth":20, "maxWidth":21
     ,"minHeight":20,"maxHeight":51
     ,"minDepth":4, "maxDepth":5
     ,"minKids":1, "maxKids":2
     ,"cityblock":0
     ,"orientation":0
     );
     
 Figure current = emptyFigure();
     
list[str] choiceLabels = ["line", "manhattan"]; 

list[str] orientationLabels = ["topdown", "downtop", "leftright", "rightleft"];   

Figure slider(str title, num low = 0, num step = 1) {
       num high = 10*state[title];
       return hcat(fillColor = "antiqueWhite",figs = [   
          box(fig=text(title), lineWidth = 0, fillColor = "antiqueWhite",size = <100, 20>)
          , htmlText("0", size = <20, 20>)
           ,htmlText("<state[title]>", id = "<title>_p",size = <30, 20>)
          , rangeInput(id= title, low= low, high=  high,  step = step, 
              \value =  state[title],
              event = on("change", void(str e, str n, int v) {
                   state[n] = v;
                   // println(n);
                   textProperty("<n>_p", html = "<v>");
                   })            
              )
              ,htmlText("<high>", lineWidth = 0, size = <30, 20>)
              ], borderStyle = "ridge", borderWidth = 2);
        }
        
bool getCityblock() =  state["cityblock"]==1;

Orientation getOrientation() { 
        return  
        switch(orientationLabels[state["orientation"]]) 
           {
           case "topdown": return topDown();
           case "downtop": return downTop();
           case "leftright": return leftRight();
           case "rightleft": return rightLeft();
           }
      }
           
Figure genTree(bool cityblock = false, Orientation orientation = topDown()) {
        idx  = 0;
        return genTrees(50,state["hGap"], state["vGap"], state["minDepth"],state["maxDepth"], state["minKids"]
                   ,state["maxKids"],state["minWidth"], state["minHeight"]
                   ,state["maxWidth"], state["maxHeight"]
               cityblock = cityblock, orientation = orientation);
        }

Figure sliders() {
   
    return vcat(figs=[
         slider("hGap")
         ,slider("vGap")
         ,slider("minWidth")
         ,slider("maxWidth")
         ,slider("minHeight")
         ,slider("maxHeight")
         ,slider("minDepth")
         ,slider("maxDepth")
         ,slider("minKids")
         ,slider("maxKids")
         ,hcat(borderStyle = "ridge", borderWidth = 2, hgap = 10, 
           figs=[choiceInput(id="cityblock", choices = choiceLabels
                 , \value = choiceLabels[state["cityblock"]]
                 , event = on(void(str e, str n, str v){state["cityblock"]= indexOf(choiceLabels, v);})
                 )
         ,choiceInput(id="orientation", choices = orientationLabels
             ,\value = orientationLabels[state["orientation"]]
             ,event = on(void(str e, str n, str v){state["orientation"]= indexOf(orientationLabels, v);}) 
           )])
         ,buttonInput("generate random tree"
         event = on(void(str e, str n, str v) {
             render(self());  
             }))
         ,buttonInput("repeat"
         event = on(void(str e, str n, str v) {
             render(selfCurrent());  
             }))
         ]);
    }
    
public Figure restructure(Figure t, int hGap, int vGap, int minX, int maxX, int minY, int maxY) {
   return visit (t) {
        case tree(_, list[Figure] ts)=> tree(box(fillColor="yellow", size=<minX + arbInt(maxX-minX), minY + arbInt(maxY -minY)>), ts
                        ,xSeparation = hGap, ySeparation = vGap)
        }
   }

Figure selfCurrent() {
       current = 
       return vcat(figs=[sliders(), restructure(current, state["hGap"], state["vGap"],
         state["minWidth"],state["maxWidth"], state["minHeight"],state["maxHeight"] ) ]);
       }
    
Figure self() {
       current = genTree(cityblock = getCityblock(), orientation = getOrientation());
       return vcat(figs=[sliders(), current ]);
       }
    
void trtree() {
    Figure f = sliders();
    current = genTree();
    render(vcat(figs=[f, current]));
    }

Figure q(str s, list[Figure] fs) = tree(box(id = "<s>", fillColor="yellow", size=<30, 20>, fig = text(s)),
       fs);  

Figure q(str s) = q(s,[]); 
  
Figure ot() {
   // Figure b4 = q("10", [q("11"), q("12")]);
   //Figure b3 = q("9",[q("10", [q("11"), q("12")]), q("13")]);
   Figure b2(str s) = q(s+"8", [q(s+"9",[q(s+"10", [q(s+"11"), q(s+"12")]), q(s+"13")]), q(s+"14")]);
   // return b3;
   return hcat(figs = [b2("b"), q("c0", [
      q("c1", [q("c2"), q("c3", [q("c4"), q("c5", [q("c6"), q("c7")])])]),
      b2("a")
     ]
    )
    ])
   ;
  }
    
 void tot() = render(ot());
          
    
void main() {
    render(text("aap", size=<50, 50>));
    }