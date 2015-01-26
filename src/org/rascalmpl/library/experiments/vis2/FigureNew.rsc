module experiments::vis2::FigureNew

import util::Math;
import Prelude;
import experiments::vis2::vega::Vega;
import experiments::vis2::FigureServer; 

/* Properties */

// Position for absolute placement of figure in parent

alias Position = tuple[num x, num y];

// Alignment for relative placement of figure in parent

alias Alignment = tuple[num hpos, num vpos];

public Alignment topLeft      	= <0.0, 0.0>;
public Alignment top          	= <0.5, 0.0>;
public Alignment topRight     	= <1.0, 0.0>;

public Alignment left   		= <0.0, 0.5>;
public Alignment center       	= <0.5, 0.5>;
public Alignment right   	 	= <1.0, 0.5>;

public Alignment bottomLeft   	= <0.0, 1.0>;
public Alignment bottom 		= <0.5, 1.0>;
public Alignment bottomRight	= <1.0, 1.0>;

// Events and bindings for input elements

data Event 
	= on()
	| on(str eventName, Bind binder)
	| on(str eventName, Figure fig)
	;
	
//alias Cursor[&T] = &T;

data Bind
    = bind(value accessor)
    | bind(value accessor, value val)
//    | delete(Cursor[&T] accessor)
//    | add(Cursor[&T] accessor, value(value model))
	;

// Data formats for various chart elements

alias XYData 			= lrel[num x, num y];
			 		 
alias XYLabeledData     = lrel[num xx, num yy, str label];	

alias HistogramData     = tuple[int nTickMarks, list[num val] \data];		

alias ErrorData			= lrel[str label, num mean, num low, num high];	

alias Datasets[&T] 		= map[str name, &T values];

	
//data Margin = margin(int left = 0, int right = 0, int top = 0, int bottom = 0);

/*
	link
	gradient(numr)
	texture(loc image)
	lineCap flat, rounded, padded
	lineJoin	smooth, sharp(r), clipped
	dashOffset
		
	linestyle: color, width, cap, join, dashing, dashOffset
*/

// Vertices for defining shapes.

data Vertex
	= line(num x, num y)
	| lineBy(num x, num y)
	| move(num x, num y)
	| moveBy(num x, num y)
	;
	
alias Vertices = list[Vertex];

alias Points = lrel[num x, num y];

public alias Figures = list[Figure];

public num nullFunction(list[num] x) { return 0;}

public data Figure(
        // Naming
        str id = "",
		// Dimensions and Alignmenting
		
		tuple[int,int] size = <0,0>,
		tuple[int, int, int, int] padding = <0, 0, 0, 0>,
		int width = 0,
		int height = 0,
		Position at = <0,0>,
		Alignment align = <0.5, 0.5>, // TODO should be middle,
		num grow = 1.0,
		tuple[int,int] gap = <0,0>,
		int hgap = 0,
		int vgap = 0,
   
    	// Line properties
    
		int lineWidth = 1,			
		str lineColor = "black", 		
		list[int] lineDashing = [],	
		real lineOpacity = 1.0,
	
		// Area properties

		str fillColor    = "white", 			
		real fillOpacity = 1.0,	
		str fillRule     = "evenodd",
		
		tuple[int, int] rounded = <0, 0>,

		// Font and text properties
		
		str fontFamily = "Helvetica, Arial, Verdana, sans-serif",
		str fontName = "Helvetica",
		int fontSize = 12,
		str fontStyle = "normal",		// normal|italic|oblique|initial|inherit
		str fontWeight = "normal",		//normal|bold|bolder|lighter|number|initial|inherit; normal==400, bold==700
		str fontColor = "black",
		str textDecoration	= "none",	// none|underline|overline|line-through|initial|inherit
		
		// Interaction
	
		Event event = on(),
		
		// Tooltip
		str tooltip = ""
	) =
	
	emptyFigure()

// atomic primitives
	
   | text(value text)		    			// text label
   | markdown(value text)					// text with markdown markup (TODO: make flavor of text?)
   | math(value text)						// text with latex markup
   
// Graphical elements

   | box(Figure fig=emptyFigure())      	// rectangular box with inner element
   
   | ellipse(num cx = 0, num cy = 0, num rx=0, num ry=0, Figure fig=emptyFigure())
   
   | circle(num cx = 0, num cy = 0, num r=0, Figure fig=emptyFigure())
   
   | ngon(int n=3, num r=0, Figure fig=emptyFigure())	// regular polygon
   
   | polygon(Points points=[], bool fillEvenOdd = true)
   
   | shape(Vertices vertices, 				// Arbitrary shape
   			bool shapeConnected = true, 	// Connect vertices with line/curve
   			bool shapeClosed = false, 		// Make a closed shape
   			bool shapeCurved = false, 		// Connect vertices with a spline
   			bool fillEvenOdd = true,		// The fill rule to be used. (TODO: remove?)
   			Figure startMarker=emptyFigure(),
   			Figure midMarker=emptyFigure(), 
   			Figure endMarker=emptyFigure())
   
   | image(loc url=|home:///|)

// Figure composers
                   
   | hcat(Figures figs=[]) 					// horizontal and vertical concatenation
   | vcat(Figures figs=[]) 					// horizontal and vertical concatenation 
   | overlay(Figures figs=[])				// overlay (stacked) comAlignment
   | grid(list[Figures] figArray = [[]])	// grid of figures

// Figure transformations

   | at(int x, int y, Figure fig)			// Move to Alignment relative to origin of enclosing Figure
   | atX(int x, Figure fig)				// TODO: how to handle negative values?
   | atY(int y, Figure fig)
   
  	//TODO: avoid name clash with Math::util:scale
   | SCALE(num factor, Figure fig)
   
   | rotate(num angle, Figure fig)

// Input elements

   | buttonInput(str trueText = "", str falseText = "")
   | checkboxInput()
   | choiceInput(list[str] choices = [])
   | colorInput()
   
   // date
   // datetime
   // email
   // month
   // time
   // tel
   // week
   // url
   
   | numInput()
   | rangeInput(int low=0, int high=100, int step=1)
   | strInput()
   
// Visibility control elements

   | visible(bool condition=true, Figure fig = emptyFigure())
   
   | choice(int selection = 0, Figures figs = [])
  
/*
   | _computeFigure(bool() recomp,Figure () computeFig, FProperties props)
 
*/

// More advanced figure elements

// Charts
//	| chart(Chart c, CHARTOPTIONS options = chartOptions())
	| combo(list[Chart] charts =[], CHARTOPTIONS options = chartOptions())

/*
   
  | vegaChart(str dataFile = "",  VEGA() command = (){return vega();}, str \module ="experiments::vis2::vega::VegaChart"
    , Datasets[value] datasets = (), num(list[num]) aggregate = nullFunction)   
     
  | barChart(Axis xAxis=axis(), Axis yAxis=axis(), Datasets[LabeledData] datasets = (), str orientation = "vertical", bool grouped = false, str flavor ="nvBarChart") 
 
  | scatterPlot()
      | lineChart(Axis xAxis=axis(), Axis yAxis=axis(), Datasets[XYData] datasets = (), bool area = false, str flavor ="nvLineChart")  
*/ 
    
// Graphs

   | graph(lrel[str, Figure] nodes = [], Figures edges = [], str orientation = "topDown", int nodeSep = 50, int edgeSep=10, int layerSep= 30, str flavor="layeredGraph")
   | edge(str from, str to, str label)
   
// Trees
	| tree(Figure root, Figures children)
   ;
 

data AXIS(str title="",
          int minValue = -1,
          int maxValue = -1,
          VIEWWINDOW viewWindow = viewWindow(),
          GRIDLINES gridlines = gridlines()) = axis()
          ;
          
bool isEmpty (AXIS axis) = axis.title=="" && axis.minValue==-1 && axis.maxValue == -1
     && isEmpty(axes.viewWindow);

str trAxis(AXIS axis) {
    str r = "{";
    if  (!isEmpty(axis.title)) r +="\"title\" : \"<axis.title>\",";
    if  (axis.minValue>=0) r+="\"minValue\" : <axis.minValue>,";
    if  (axis.maxValue>=0) r+= "\"maxValue\" : <axis.maxValue>,";
    if (!isEmpty(axis.viewWindow)) r+="\"viewWindow\":<trViewWindow(axis.viewWindow)>,";
    if (!isEmpty(axis.gridlines)) r+="\"gridlines\":<trGridlines(axis.gridlines)>,";
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }
                
data LEGEND (bool none = false,
             str alignment = "",
             int maxLines = -1) = legend()
            ;
            
bool isEmpty (LEGEND legend) = !legend.none && legend.alignment==""
                && legend.maxLines == -1;

str trLegend(LEGEND legend) {
    if (legend.none) return "\'none\'";
    str r = "{";
    if  (!isEmpty(legend.alignment)) r +="\"alignment\" : \"<legend.alignment>\",";
    if  (legend.maxLines>=0) r+= "\"maxLines\" : \"<legend.maxLines>\",";
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }
           
data VIEWWINDOW(int max = -1, int min = -1) = viewWindow();

bool isEmpty(VIEWWINDOW w ) = w.max == -1 && w.min == -1;

str trViewWindow(VIEWWINDOW w) {
    str r = "{";
    if  (w.min>=0) r+="\"min\" : <w.min>,";
    if  (w.max>=0) r+= "\"max\" : <w.max>,";
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }

data GRIDLINES(str color = "", int count =-1) = gridlines();

bool isEmpty(GRIDLINES g) = isEmpty(g.color) && g.count == -1 ;

str trGridlines(GRIDLINES g) {
    str r = "{";
    if  (!isEmpty(g.color)) r+="\"color\" : \"<g.color>\",";
    if  (g.count>=0) r+= "\"count\" : <g.count>,";
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }


bool isEmpty(GRIDLINES g) = isEmpty(g.color) && g.count == -1 ;

str trGridlines(GRIDLINES g) {
    str r = "{";
    if  (!isEmpty(g.color)) r+="\"color\" : \"<g.color>\",";
    if  (g.count>=0) r+= "\"count\" : <g.count>,";
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }

data SERIES (
    str color ="",
    str curveType = "",
    int lineWidth = -1,
    str pointShape = "",
    int pointSize = -1,
    str \type=""
    ) = series();
    
bool isEmpty(SERIES s) = isEmpty(s.color) &&
       isEmpty(s.curveType) &&  isEmpty(s.pointShape) && isEmpty(s.\type) 
            && s.lineWidth == -1 && s.pointSize == -1;
            
str trSeries(SERIES s) {
    str r = "{";
    if  (!isEmpty(s.color)) r+="\"color\" : \"<s.color>\",";
    if  (!isEmpty(s.curveType)) r+="\"curveType\" : \"<s.curveType>\",";
    if  (s.lineWidth>=0) r+= "\"lineWidth\" : <s.lineWidth>,";
    if  (!isEmpty(s.pointShape)) r+="\"pointShape\" : \"<s.pointShape>\",";
    if  (s.pointSize>=0) r+= "\"pointSize\" : <s.pointSize>,";
    if  (!isEmpty(s.\type)) r+="\"type\" : \"<s.\type>\",";
    r = replaceLast(r,",", "");
    r+="}";
    return r;
    }
            
            
    
data CHARTOPTIONS (str title = "",
             AXIS hAxis = axis(),
             AXIS vAxis = axis(),
             int width=-1,
             int height = -1,
             bool forceIFrame = true,
             LEGEND legend = legend(),
             int lineWidth = -1,
             int pointSize = -1,
             bool interpolateNulls = false,
             str curveType = "",
             str seriesType = "",
             str pointShape = "",
             list[SERIES] series = []
             ) = chartOptions()
            ;
            
str trOptions(CHARTOPTIONS options) {
            str r = "{";
            if  (!isEmpty(options.title)) r +="\"title\" : \"<options.title>\",";
            if  (!isEmpty(options.hAxis)) r +="\"hAxis\" : <trAxis(options.hAxis)>,";
            if  (!isEmpty(options.vAxis)) r +="\"vAxis\" : <trAxis(options.vAxis)>,";
            if  (options.width>=0) r+="\"width\" : \"<options.width>\",";
            if  (options.height>=0) r+= "\"height\" : \"<options.height>\",";
            r+= "\"forceIFrame\":<options.forceIFrame>,";
            if  (!isEmpty(options.legend)) r +="\"hAxis\" : <trLegend(options.legend)>,";
            if  (options.lineWidth>=0) r+="\"lineWidth\" : <options.lineWidth>,";
            if  (options.pointSize>=0) r+="\"pointSize\" : <options.pointSize>,";
            if  (options.interpolateNulls)  r+= "\"interpolateNulls\":\"<options.interpolateNulls>\",";
            if  (!isEmpty(options.curveType)) r +="\"curveType\" : \"<options.curveType>\",";
            if  (!isEmpty(options.seriesType)) r +="\"seriesType\" : \"<options.seriesType>\",";
            if  (!isEmpty(options.pointShape)) r +="\"pointShape\" : \"<options.pointShape>\",";
            if  (!isEmpty(options.series)) r+=  "\"series\" : [<intercalate(",", ["<trSeries(q)>" |q<-options.series])>],";
            r = replaceLast(r,",", "");
            r+="}";
            return r;
      }
            
data COLUMN  = column(str \type="", str label="", str role = "");      

str trColumn(COLUMN c) {
    return
    "{\"label\":\"<c.label>\",
    ' \"type\": \"<c.\type>\",
    ' \"role\": \"<c.role>\"
    '}";
    }


data Chart(str name = "", str color = "black" /* add interpolate */)
    =
	  line(XYData xydata) 
	| line(XYLabeledData xylabeldata) 
	| area(XYData xydata)
	| area(XYLabeledData xylabeleddata)
	| bar(XYLabeledData xylabeledData)
	;
	
/*Examples*/

lrel[value, value] tData(Chart c) {
     switch(c) {
        case line(XYData x): return x;
        case area(XYData y): return y;
        case bar(LabeledData labeledData): return labeledData;
        }
     return [];
     }

COLUMN cData(Chart c) {
     switch(c) {
        case line(XYData x): return column(\type="num");
        case area(XYData y): return column(\type="num");
        case bar(LabeledData labeledData): return column(\type="num");
        }
     return column();
     }
     
list[COLUMN] joinColumn(list[Chart] charts) {
     list[COLUMN] r = [cData(c)|c<-charts];
     return [column(\type="num")]+r;
     }
     
list[list[value]] joinData(list[Chart] charts) {
   set[value] d = union({toSet(domain(tData(c)))|c <-charts});   
   list[value] x = [i|i<-d];
   return [[z] +[(tData(c)[z]?)?tData(c)[z][0]:""|c<-charts]|z<-x];
   }
	
void sinAndCosChart(){
        ex("sinAndCosChart", 
        	combo(charts=[line([<x, round(sin(x/10),0.01)>               | x <- [0.0, 1.0 .. 100.0]], name="Sine Wave"),
        		   line([<x, round(0.5 * cos(x/10), 0.01)>        | x <- [0.0, 1.0 .. 100.0]], name ="Cosine Wave"),
        		   line([<x, round(0.25 * sin(x/10) + 0.5, 0.01)> | x <- [0.0, 1.0 .. 100.0]], name= "Another sine wave")
        			],
        	options = chartOptions(curveType="function",
           		hAxis = axis(title="Time (s)"), 
           		vAxis = axis(title="Voltage(v)"),
           		width=500,
                height=200
        	    )
           
        ));
}
 
public list[Chart] q = 
     [
     line([<x, round(sin(x/10),0.01)>               | x <- [0.0, 1.0 .. 100.0]], name="Sine Wave"),
     line([<x, round(0.5 * cos(x/10), 0.01)>        | x <- [0.0, 1.0 .. 100.0]], name ="Cosine Wave"),
     line([<x, round(0.25 * sin(x/10) + 0.5, 0.01)> | x <- [0.0, 1.0 .. 100.0]], name= "Another sine wave")
     ];

void ex(str title, Figure f){
	render(title, f);
}