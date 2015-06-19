module experiments::vis2::sandbox::Graph
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure; 
import Prelude;

public Figure fsm(){
    // Figure b(str label) = emptyFigure();
	Figure b(str label) =  box(fig=text(label), fillColor="whitesmoke", rounded=<5,5>, padding=<0,5, 0, 5>);
    states = [ 	
                <"CLOSED", 		ngon(n=6, r = 40, fig=at(3, 25, text("CLOSED", fillColor="brown")), fillColor="#f77", rounded=<5,5>, padding=<0, 5,0, 5>)>, 
    			<"LISTEN", 		b("LISTEN")>,
    			<"SYN RCVD", 	b("SYN RCVD")>,
				<"SYN SENT", 	b("SYN SENT")>,
                <"ESTAB",	 	box(size=<100, 30>, fig=text("ESTAB"), fillColor="#7f7", rounded=<5,5>, padding=<0, 5,0, 5>)>,
                <"FINWAIT-1", 	b("FINWAIT-1")>,
                <"CLOSE WAIT", 	box(size=<120, 30>, fig=text("CLOSE WAIT"), fillColor="antiquewhite", lineDashing=[1,1,1,1],  rounded=<5,5>, padding=<0, 5,0, 5>)>,
                <"FINWAIT-2", 	b("FINWAIT-2")>,    
                <"CLOSING", b("CLOSING")>,
                <"LAST-ACK", b("LAST-ACK")>,
                <"TIME WAIT", b("TIME WAIT")>
                ];
 	
    edges = [	edge("CLOSED", 		"LISTEN",  	 label="open"),
    			edge("LISTEN",		"SYN RCVD",  label="rcv SYN"),
    			edge("LISTEN",		"SYN SENT",  label="send"),
    			edge("LISTEN",		"CLOSED",    label="close"),
    			edge("SYN RCVD", 	"FINWAIT-1", label="close"),
    			edge("SYN RCVD", 	"ESTAB",     label="rcv ACK of SYN"),
    			edge("SYN SENT",   	"SYN RCVD",  label="rcv SYN"),
   				edge("SYN SENT",   	"ESTAB",     label="rcv SYN, ACK"),
    			edge("SYN SENT",   	"CLOSED",    label="close"),
    			edge("ESTAB", 		"FINWAIT-1", label="close"),
    			edge("ESTAB", 		"CLOSE WAIT",label= "rcv FIN"),
    			edge("FINWAIT-1",  	"FINWAIT-2",  label="rcv ACK of FIN"),
    			edge("FINWAIT-1",  	"CLOSING",    label="rcv FIN"),
    			edge("CLOSE WAIT", 	"LAST-ACK",  label="close"),
    			edge("FINWAIT-2",  	"TIME WAIT",  label="rcv FIN"),
    			edge("CLOSING",    	"TIME WAIT",  label="rcv ACK of FIN"),
    			edge("LAST-ACK",   	"CLOSED",     label="rcv ACK of FIN", lineColor="green"),
    			edge("TIME WAIT",  	"CLOSED",     label="timeout=2MSL")
  			];
    // edges=[];	
  	return graph(width = 800, height = 1200,nodes = states, edges = edges);
}


public void tfsm() = render(fsm());

public void ffsm(loc l) = writeFile(l, toHtmlString(fsm()));

public Figure shape1 = shape([line(100,100), line(100,200), line(200,200)], 
    shapeClosed=true, startMarker=box(lineWidth=1, size=<50, 50>, fig = circle(r=20, fillColor="red"), fillColor="antiqueWhite"));
void tshape1(){	render(shape1); }
void ftshape1(loc f){writeFile(f, toHtmlString(shape1));}

Figure grap() = graph(width = 800, height = 800, nodes= [
<"a", box(size=<60, 60>, 
   fig = at(10, 10, 
        box(fig = at(0, 0, text("aap")), fillColor="antiquewhite"
           , padding=<0, 5, 0, 5>)
        ), fillColor = "lightblue")>
, <"b", box(fig=text("noot"), padding=<0, 5, 0, 5>, rounded=<15, 15>, fillColor = "antiquewhite")>
, <"c", ellipse(rx=20, ry = 30, padding=<0, 5, 0, 5>, size=<40, 60>,fillColor = "pink")>
// , <"d", ngon(n=3, r= 30, size=<50, 50>, fillColor = "lightgreen")>
]
, edges= [edge("a", "b", lineInterpolate="basis"), edge("b","c", lineInterpolate="basis"), edge("c", "a", lineInterpolate="basis")

// , edge("d", "a")
], nodeProp = ("a": dot(label="")));

void tgraph()= render(grap());

void fgraph(loc l) = writeFile(l, toHtmlString(grap()));
