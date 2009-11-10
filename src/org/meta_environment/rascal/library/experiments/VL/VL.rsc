module experiments::VL::VL

data VPROP =
    
     left(int left) 					%% offset from left border
   | left(int(int i) leftFun) 
    		
   | right(int right)					%% offset from right border
   | right(int(int i) rightFun) 
   	
   | top(int top)						%% offset from top
   | top(int(int i) topFun) 
   			
   | bottom(int bottom)
   | bottom(int(int i) bottomFun) 		%% offset from bottom
   
   | width(int width)
   | width(int(int i) widthFun)     	%% width of elem
   
   | height(int height)
   | height(int(int i) heightFun)		%% height of elem
   
   | visible(bool visible)				%% is elem visible?
   
   | title(str title)					%% title of elem
   
   | values(list[value] values) 		%% data for elem
   | values(list[value]() valuesFun)
   
   | lineWidth(int lineWidth)			%% line width used by elem
   | lineWidth(int(int i) lineWidthFun)
   
   | fillStyle(int fillStyle)			%% fill color used by elem
   | fillStyle(int(int i) fillStyleFun)
   
   | strokeStyle(int strokeStyle)		%% stroke color used by elem
   | strokeStyle(int(int i) strokeStyleFun)
   ;

data VELEM = 
     bar(list[VPROP] props)
     ;
     
data Panel = panel(list[VPROP] props, list[VELEM] elms);

@doc{Render a panel}
@reflect{Needs calling context when calling argument function}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public void java render(Panel p);

public void main(){

    P = panel([width(300), height(300)],
              [bar([values([1, 1.2, 1.7, 1.5, 0.7]),
                    bottom(0),
                    width(20),
                    height(int (int d) {return d * 80;}),
                    left(int (int d) {return d * 25;})
                   ])
              ]);
    render(P);

}