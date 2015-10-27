module experiments::vis2::sandbox::Graph
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure; 
import Prelude;

public Figure fsm(){
    // Figure b(str label) = emptyFigure();
	Figure b(str label) =  box( fig=text(label, fontWeight="bold"), fillColor="whitesmoke", rounded=<5,5>, padding=<0,6, 0, 6>, tooltip = label);
    states = [ 	
                <"CLOSED", 		ngon(n=6, r = 40, fig=text("CLOSED", fontWeight="bold"), fillColor="#f77", rounded=<5,5>, padding=<0, 5,0, 5>, tooltip = "CLOSED")>, 
    			<"LISTEN", 		b("LISTEN")>,
    			<"SYN RCVD", 	b("SYN RCVD")>,
				<"SYN SENT", 	b("SYN SENT")>,
                <"ESTAB",	 	box(size=<100, 30>, fig=text("ESTAB",fontWeight="bold"), fillColor="#7f7", rounded=<5,5>, padding=<0, 5,0, 5>, tooltip = "ESTAB")>,
                <"FINWAIT-1", 	b("FINWAIT-1")>,
                <"CLOSE WAIT", 	box(size=<120, 30>, fig=text("CLOSE WAIT",fontWeight="bold"), fillColor="antiquewhite", lineDashing=[1,1,1,1],  rounded=<5,5>, padding=<0, 5,0, 5>, tooltip = "CLOSE_WAIT")>,
                <"FINWAIT-2", 	b("FINWAIT-2")>,    
                <"CLOSING", b("CLOSING")>,
                <"LAST-ACK", b("LAST-ACK")>,
                <"TIME WAIT", b("TIME WAIT")>
                ];
 	
    edges = [	edge("CLOSED", 		"LISTEN",  	 label="open", labelStyle="font-style:italic"),
    			edge("LISTEN",		"SYN RCVD",  label="rcv SYN", labelStyle="font-style:italic"),
    			edge("LISTEN",		"SYN SENT",  label="send", labelStyle="font-style:italic"),
    			edge("LISTEN",		"CLOSED",    label="close", labelStyle="font-style:italic"),
    			edge("SYN RCVD", 	"FINWAIT-1", label="close", labelStyle="font-style:italic"),
    			edge("SYN RCVD", 	"ESTAB",     label="rcv ACK of SYN", labelStyle="font-style:italic"),
    			edge("SYN SENT",   	"SYN RCVD",  label="rcv SYN", labelStyle="font-style:italic"),
   				edge("SYN SENT",   	"ESTAB",     label="rcv SYN, ACK", labelStyle="font-style:italic"),
    			edge("SYN SENT",   	"CLOSED",    label="close", labelStyle="font-style:italic"),
    			edge("ESTAB", 		"FINWAIT-1", label="close", labelStyle="font-style:italic"),
    			edge("ESTAB", 		"CLOSE WAIT",label= "rcv FIN", labelStyle="font-style:italic"),
    			edge("FINWAIT-1",  	"FINWAIT-2",  label="rcv ACK of FIN", labelStyle="font-style:italic"),
    			edge("FINWAIT-1",  	"CLOSING",    label="rcv FIN", labelStyle="font-style:italic"),
    			edge("CLOSE WAIT", 	"LAST-ACK",  label="close", labelStyle="font-style:italic"),
    			edge("FINWAIT-2",  	"TIME WAIT",  label="rcv FIN", labelStyle="font-style:italic"),
    			edge("CLOSING",    	"TIME WAIT",  label="rcv ACK of FIN", labelStyle="font-style:italic"),
    			edge("LAST-ACK",   	"CLOSED",     label="rcv ACK of FIN", lineColor="green", labelStyle="font-style:italic"),
    			edge("TIME WAIT",  	"CLOSED",     label="timeout=2MSL", labelStyle="font-style:italic")
  			];
    // edges=[];	
  	return graph(states, edges, width = 700, height = 900);
}


public void tfsm() = render(fsm());

public void ffsm(loc l) = writeFile(l, toHtmlString(fsm()));

public Figure shape1 = shape([line(100,100), line(100,200), line(200,200)], 
    shapeClosed=true, startMarker=box(lineWidth=1, size=<50, 50>, fig = circle(r=20, fillColor="red"), fillColor="antiqueWhite"));
void tshape1(){	render(shape1); }
void ftshape1(loc f){writeFile(f, toHtmlString(shape1));}
Figure gbox1()= box(lineWidth = 1, size=<60, 60>, 
   fig =  
        box(fig = text("aap"), fillColor="antiquewhite"
        ), fillColor = "lightblue");
Figure grap() = graph([
<"a", gbox1()>
, <"b", box(fig=text("noot"), rounded=<15, 15>, fillColor = "antiquewhite")>
, <"c", ellipse(padding=<0, 15, 0, 15>, grow=1.0, id=  "aap", fig = text("HALLO"), fillColor = "pink")>
// , <"d", ngon(n=3, r= 30, size=<50, 50>, fillColor = "lightgreen")>
]
, [edge("a", "b", lineInterpolate="basis"), edge("b","c", lineInterpolate="basis"), edge("c", "a", lineInterpolate="basis")

// , edge("d", "a")
], width = 150, height = 300);

void tgraph()= render(hcat(hgap=5, figs = [gbox1(), box(grow=1.0,  fig=grap())
   , rangeInput(low = 0.0, val = 1.0, high = 2.0, step=0.1, event=on("change", void(str e, str n, real v)
            {
               println(v);
                attr("aap", grow = v);
            }))
   ], align = centerMid), align = centerMid);
// render(overlay(figs=[grap(), box(size=<40, 40>)]));

void fgraph(loc l) = writeFile(l, toHtmlString(hcat(hgap=5, figs = [gbox1(), grap()])));

Figure mbox(str txt) = box(lineWidth = 1, rounded=<5, 5>,size=<100, 50>, fig=text(txt), tooltip = txt);

Figure model() = graph([<"a", mbox("Figure")>
                       , <"b", box(lineWidth = 0, rounded=<15, 15>,size=<100, 60>, fig=
                              vcat(vgap=0, borderWidth = 4, borderColor="grey", borderStyle="ridge", figs = [text("IFigure")
                                  , hcat(borderWidth = 1, hgap = 6, figs = [text("id"), text("ref")])]))>
                       ,  <"c", mbox("Widget")>], [edge("a", "b"), edge("b", "c")]
                       , width = 150, height = 300, lineWidth = 0, align = centerMid);
                        
void tmodel()= render(model(), align = centerMid);

Figure g() = box(fig=box(size=<1000, 4000>, align = centerRight, fig=graph(size=<0,0>,nodeProperty=(),width=1000,height=1000,nodes=
   [<"Exception",box(tooltip="Exception",fig=text("Exception",fontSize=12))>
   ,<"experiments::Compiler::Examples::RascalExtraction",box(tooltip="experiments::Compiler::Examples::RascalExtraction",fig=text("RascalExtraction",fontSize=12))>
   ,<"IO",box(tooltip="IO",fig=text("IO",fontSize=12))>
   ,<"Message",box(tooltip="Message",fig=text("Message",fontSize=12))>
   ,<"Type",box(tooltip="Type",fig=text("Type",fontSize=12))>
   ,<"Map",box(tooltip="Map",fig=text("Map",fontSize=12))>
   ,<"ParseTree",box(tooltip="ParseTree",fig=text("ParseTree",fontSize=12))>
   ,<"util::Benchmark",box(tooltip="util::Benchmark",fig=text("Benchmark",fontSize=12))>
   ,<"util::Reflective",box(tooltip="util::Reflective",fig=text("Reflective",fontSize=12))>
   ,<"ValueIO",box(tooltip="ValueIO",fig=text("ValueIO",fontSize=12))>
   ,<"List",box(tooltip="List",fig=text("List",fontSize=12))>
   ,<"lang::rascal::syntax::Rascal",box(tooltip="lang::rascal::syntax::Rascal",fig=text("Rascal",fontSize=12))>]
   ,lineWidth=1
   ,edges=[
    edge("util::Reflective","Exception")
   ,edge("experiments::Compiler::Examples::RascalExtraction"
   ,"util::Reflective"),edge("experiments::Compiler::Examples::RascalExtraction"
   ,"util::Benchmark"),edge("IO","Exception"),edge("List","IO")
   ,edge("List","Map"),edge("ValueIO","Type"),edge("ValueIO","Exception")
   ,edge("ParseTree","Type"),edge("ParseTree","Exception")
   ,edge("util::Benchmark","Exception"),edge("experiments::Compiler::Examples::RascalExtraction","ParseTree")
   ,edge("experiments::Compiler::Examples::RascalExtraction","ValueIO")
   ,edge("experiments::Compiler::Examples::RascalExtraction","IO")
   ,edge("Map","Exception"),edge("util::Reflective","lang::rascal::syntax::Rascal")
   ,edge("lang::rascal::syntax::Rascal","Exception"),edge("util::Reflective","Message")
   ,edge("List","Exception"),edge("ParseTree","Message"),edge("Type","Exception")
   ,edge("util::Benchmark","IO"),edge("experiments::Compiler::Examples::RascalExtraction","Exception")
   ,edge("Message","Exception"),edge("Type","List"),edge("util::Reflective","ParseTree")
   ,edge("util::Reflective","IO"),edge("experiments::Compiler::Examples::RascalExtraction","lang::rascal::syntax::Rascal")
   ,edge("ParseTree","List")],options=graphOptions()),align=<0.0,0.0>,lineWidth=0)); 

   void tg()= render(g(), size=<600, 600>, align = centerMid);