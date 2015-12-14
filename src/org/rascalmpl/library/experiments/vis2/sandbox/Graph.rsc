module experiments::vis2::sandbox::Graph
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure; 
import experiments::vis2::sandbox::Steden; 
import Prelude;

str current = "";
list[Edge] edges = [];
list[Edge] out = [];
list[tuple[str, Figure]] states = [];
map[str, Figure] lab2f = ();
Figures buttons =[]; 

public Figure fsm(){
	Figure b(str label) =  box( fig=text(label, fontWeight="bold"), fillColor="whitesmoke", rounded=<5,5>, padding=<0,6, 0, 6>, tooltip = label
	                                          ,id = newName());	                                          
    states = [ 	
                <"CLOSED", 		ngon(n=6, r = 40, fig=text("CLOSED", fontWeight="bold"), fillColor="#f77", rounded=<5,5>, padding=<0, 5,0, 5>, tooltip = "CLOSED", id = newName())>, 
    			<"LISTEN", 		b("LISTEN")>,
    			<"SYN RCVD", 	b("SYN RCVD")>,
				<"SYN SENT", 	b("SYN SENT")>,
                <"ESTAB",	 	box(size=<100, 30>, fig=text("ESTAB",fontWeight="bold"), fillColor="#7f7", rounded=<5,5>, padding=<0, 5,0, 5>, tooltip = "ESTAB", id = newName())>,
                <"FINWAIT-1", 	b("FINWAIT-1")>,
                <"CLOSE WAIT", 	box(size=<120, 30>, fig=text("CLOSE WAIT",fontWeight="bold"), fillColor="antiquewhite", lineDashing=[1,1,1,1],  rounded=<5,5>, padding=<0, 5,0, 5>, tooltip = "CLOSE_WAIT"
                , id = newName())>,
                <"FINWAIT-2", 	b("FINWAIT-2")>,    
                <"CLOSING", b("CLOSING")>,
                <"LAST-ACK", b("LAST-ACK")>,
                <"TIME WAIT", b("TIME WAIT")>
                ];
 	
    edges = [	edge("CLOSED", 		"LISTEN",  	 label="open", labelStyle="font-style:italic", id = newName()), 
    			edge("LISTEN",		"SYN RCVD",  label="rcv SYN", labelStyle="font-style:italic", id = newName()),
    			edge("LISTEN",		"SYN SENT",  label="send", labelStyle="font-style:italic", id = newName()),
    			edge("LISTEN",		"CLOSED",    label="close", labelStyle="font-style:italic", id = newName()),
    			edge("SYN RCVD", 	"FINWAIT-1", label="close", labelStyle="font-style:italic", id = newName()),
    			edge("SYN RCVD", 	"ESTAB",     label="rcv ACK of SYN", labelStyle="font-style:italic", id = newName()),
    			edge("SYN SENT",   	"SYN RCVD",  label="rcv SYN", labelStyle="font-style:italic", id = newName()),
   				edge("SYN SENT",   	"ESTAB",     label="rcv SYN, ACK", labelStyle="font-style:italic", id = newName()),
    			edge("SYN SENT",   	"CLOSED",    label="close", labelStyle="font-style:italic", id = newName()),
    			edge("ESTAB", 		"FINWAIT-1", label="close", labelStyle="font-style:italic", id = newName()),
    			edge("ESTAB", 		"CLOSE WAIT",label= "rcv FIN", labelStyle="font-style:italic", id = newName()),
    			edge("FINWAIT-1",  	"FINWAIT-2",  label="rcv ACK of FIN", labelStyle="font-style:italic", id = newName()),
    			edge("FINWAIT-1",  	"CLOSING",    label="rcv FIN", labelStyle="font-style:italic", id = newName()),
    			edge("CLOSE WAIT", 	"LAST-ACK",  label="close", labelStyle="font-style:italic", id = newName()),
    			edge("FINWAIT-2",  	"TIME WAIT",  label="rcv FIN", labelStyle="font-style:italic", id = newName()),
    			edge("CLOSING",    	"TIME WAIT",  label="rcv ACK of FIN", labelStyle="font-style:italic", id = newName()),
    			edge("LAST-ACK",   	"CLOSED",     label="rcv ACK of FIN", lineColor="green", labelStyle="font-style:italic", id = newName()),
    			edge("TIME WAIT",  	"CLOSED",     label="timeout=2MSL", labelStyle="font-style:italic", id = newName())
  			];
    lab2f = (v[0]:v[1]|v<-states);
  	return graph(nodes=states, edges=edges, width = 700, height = 900);
}



Figure _bfsm() {
    Figure f = fsm();
    if (g:graph():=f) {
        current = lab2f[states[0][0]].id;
        buttons = [buttonInput("", width = 200, height = 25, disabled = true, id = newName()
        ,event = on("click", void(str ev, str n, str v)(int q) {
             return void(str ev, str n, str v) {
             style(lab2f[out[q].from].id, fillColor="whitesmoke");
             style(lab2f[out[q].to].id, fillColor="#f77");
             current=lab2f[out[q].to].id;
              for (Figure b<-buttons) {
                  attr(b.id, disabled = true);
                  style(b.id, visibility = "hidden");
                  }
             out = [e|Edge e<-edges, lab2f[e.from].id==current]; 
             for (int i<-[0..size(out)]) {
                  attr(buttons[i].id, disabled = false);
                  style(buttons[i].id, visibility = "visible");
                  textProperty(buttons[i].id, \text=out[i].label);
                  }
       };}(i))
        )|int i <-[0..10]];
        Figure g = vcat(figs = buttons, height = 200, width = 200);
        return hcat(vgap = 0, align = topLeft, borderWidth  =4, borderStyle="ridge", figs=[g , f]);
        }
  }


public void bfsm() = render(_bfsm(), event = on("load", void(str ev, str n, str v){
   out = [e|Edge e<-edges, lab2f[e.from].id==current]; 
   int i = 0;
   for (Edge e<-out) {
      attr(buttons[i].id, disabled = false);
      textProperty(buttons[i].id, \text = e.label);
      i = i+1;
      }
   }));

public void ffsm(loc l) = writeFile(l, toHtmlString(fsm()));

public Figure tree1() = tree(box(fillColor="red", size=<10, 10>), [box(size=<5, 5>)]);

public Figure shape1() = 
svg(shape([line(100,100), line(100,200), line(200,200)], 
    shapeClosed=true, startMarker=box(lineWidth=1, size=<50, 50>, fig = circle(r=3, fillColor="red"), fillColor="antiqueWhite")));
void tshape1(){	render(shape1()); }
void ftshape1(loc f){writeFile(f, toHtmlString(shape1()));}
Figure gbox1()= overlay(figs=[
          box(id="touch", fillColor="whitesmoke", lineWidth = 1, size=<60, 60>,
               tooltip = tree(box(fillColor="red", size=<15, 15>),
                     [box(fillColor="blue", size=<10, 10>)]))
          ,box(id="inner", fillColor="yellow", size=<25, 25>)]
        )
        ;

void tgbox1() = render(gbox1());

Figure grap() = graph([
<"a", gbox1()>
, <"b"
     // , overlay(figs=[
         , box(id = "ttip", size=<60, 60> 
          ,tooltip= tree1() 
          , fig = text("Hallo")
       ,  rounded=<15, 15>, fillColor = "antiquewhite")
        >
, <"c", box(/*grow=1.0,*/ id=  "ap"// , fig = text("HALLO")
, fillColor = "pink", size=<50, 50>
// , tooltip=box(size=<50, 50>, fillColor="blue")
    , tooltip=svg(steden())
)>
// , <"d", ngon(n=3, r= 30, size=<50, 50>, fillColor = "lightgreen")>
]
, [edge("a", "b", lineInterpolate="basis"), edge("b","c", lineInterpolate="basis"), edge("c", "a", lineInterpolate="basis")

// , edge("d", "a")
], width = 150, height = 300);

void tgrap() = render(grap());

void fgrap(loc l) = writeFile(l, toHtmlString(grap()));


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
   ,edge("experiments::Compiler::Examples::RascalExtraction","util::Reflective")
   ,edge("experiments::Compiler::Examples::RascalExtraction","util::Benchmark")
   ,edge("IO","Exception"),edge("List","IO")
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