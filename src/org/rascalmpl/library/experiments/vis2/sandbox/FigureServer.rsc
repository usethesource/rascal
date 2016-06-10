module experiments::vis2::sandbox::FigureServer
import experiments::vis2::sandbox::Figure;
import experiments::vis2::sandbox::IFigure;
import util::ShellExec;
import util::Reflective;
import Prelude;


public void render(Figure fig1, int width = 800, int height = 800, 
     Alignment align = <0.5, 0.5>, tuple[int, int] size = <0, 0>,
     str fillColor = "none", str lineColor = "black", bool debug = false, bool display = true, 
     Event event = on(nullCallback), int borderWidth = -1, str borderStyle = "", str borderColor = ""
     ,int lineWidth = -1, bool resizable = true, str cssFile="")
     {
     setDebug(debug);
     _render(fig1, width = width,  height = height,  align = align, fillColor = fillColor
     , lineColor = lineColor, lineWidth = lineWidth, size = size, event = event
     , borderWidth = borderWidth, borderStyle = borderStyle, borderColor=borderColor
     , resizable = resizable, defined = (width? && height?)||(size?), cssFile = cssFile);
     // println(toString());
     }
       
public str toHtmlString(Figure fig1, int width = 400, int height = 400, 
     Alignment align = <0.5, 0.5>, tuple[int, int] size = <0, 0>,
     str fillColor = "white", str lineColor = "black", bool debug = false
     , int borderWidth = -1,  str borderColor = "", str borderStyle = "", bool resizable = true,
     str cssFile="")
     {
     setDebug(debug);
     _render(fig1, width = width,  height = height,  align = align, fillColor = fillColor,
     lineColor = lineColor, size = size, display = false
     , borderWidth = borderWidth, borderStyle = borderStyle, resizable = resizable,
     cssFile = cssFile
     );
     return getIntro();
     }

public void renderSave(Figure fig1, loc file
     ,int width = 400, int height = 400
     ,Alignment align = <0.5, 0.5>, tuple[int, int] size = <0, 0>
     ,str fillColor = "white", str lineColor = "black", bool debug = false
     ,int borderWidth = -1,  str borderColor = "", str borderStyle = "", bool resizable = true
      ,int screenWidth = 500, int screenHeight = 500, str cssFile="", loc javaLoc=|file:///usr|) 
       {
       str r = toHtmlString(fig1, width = width,  height = height,  align = align, fillColor = fillColor,
       lineColor = lineColor, size = size
       ,borderWidth = borderWidth, borderStyle = borderStyle, resizable = resizable
       ,cssFile=cssFile
      );  
      loc parent = file.parent;
      str name = file.file;
      str classpath = getOneFrom([x|x<-split(":", getRascalClasspath()), endsWith(x,"html2png.jar")]);
      // str classpath=getOneFrom({x.path|x<-classPathForProject(|project://rascal|), endsWith(x.path, "html2png.jar")}); 
      if (!endsWith(name, ".png")) {
            println("Output file name <name> must end with \".png\"");
            return;
            }
      str htmlName = replaceLast(name, ".png", ".html");
      loc htmlFile = parent+htmlName;
      writeFile(htmlFile, r);
      
      javaLoc=javaLoc+"bin"+"java";
      println(exec(javaLoc.path
         // ,args = []
         ,args=["-jar", classpath, htmlFile.uri, "<screenWidth>", "<screenHeight>"]
         ));
      }

public Style style(str id, str fillColor="", str lineColor="", int lineWidth = -1,
     num fillOpacity = -1.0, num lineOpacity = -1.0, str visibility = "") {
     str idx = child(id);
     Style v = _getStyle(idx);
     v.svg = isSvg(idx);
     if (lineWidth!=-1) v.lineWidth = lineWidth;
     if (fillOpacity>=0) v.fillOpacity = fillOpacity;
     if (lineOpacity>=0) v.lineOpacity = lineOpacity;
     if (!isEmpty(fillColor)) v.fillColor = fillColor;
     if (!isEmpty(lineColor)) v.lineColor = lineColor;
     if (!isEmpty(visibility)) v.visibility = visibility;
     _setStyle(idx, v);
     return v;
     }



bool isEmptyValue(value v) {
    if (str x:=v) return isEmpty(x);
    return false;
    }
     
public Attr attr(str id, int width = -1, int height = -1, int r = -1
     , num bigger = 1.0, bool disabled = false) {
     str idx = child(id);
     Attr v = _getAttr(idx);
     if (width!=-1) v.width = width;
     if (height!=-1) v.height = height;
     if (bigger>=0) v.bigger = bigger;    
     if (r!=-1) v.r = r;
     if (disabled?) v.disabled= disabled;
     _setAttr(idx, v);
     return v;
     }
     
public void disable(str id) {
     Attr v = _getAttr(id);
     v.disabled = true;
     _setAttr(id, v);
     }
     
public void enable(str id) {
     Attr v = _getAttr(id);
     v.disabled = false;
     _setAttr(id, v);
     }

public bool isDisabled(str id) = _getAttr(id).disabled;
     
public Property property(str id, value \value = "") {
    str idx = child(id);
    Property v = _getProperty(idx);
    if (!isEmptyValue(\value)) v.\value = \value;
     _setProperty(idx, v);
     return v;
    }
    
public Property clearValueProperty(str id) {
     str idx = child(id);
     Property v = _getProperty(idx);
     v.\value = "";
     _setProperty(idx, v);
     //  println(v);
     return v;
     }

public Text textProperty(str id, str text = "", str html = "") {
     str idx = child(id);
     Text v = _getText(idx); 
     if (!isEmpty(text)) {
         v.plain = text;
         }
     if (!isEmpty(html)) {
         v.html = html;
         }
     _setText(idx, v);
     return v;
     }
     
public Text clearTextProperty(str id) {
     str idx = child(id);
     Text v = _getText(idx);
     v.plain = "";
     v.html = "";
     _setText(idx, v);
     // println("clearTextProp:<id>");
     return v;
     }
     
public Timer timer(str id, int delay = -1, str command = "") {
    str idx = child(id);
    Timer t = _getTimer(idx);
    if (delay>=0) t.delay = delay;
    if (!isEmpty(command)) t.command = command;
     _setTimer(idx, t);
     return t;
    }
   
public str getPromptStr(str tg) = _getPromptStr(tg);

public int getPromptInt(str tg) = toInt(_getPromptStr(tg));

void setPrompt(list[tuple[str id, str lab, str val]] p) = _setPrompt( p);

void setAlert(str a) = _setAlert(a);
    
public map[str, str] getIdFig(Figure f) = _getIdFig(f);


Figure finalStateMachine(Figure f, str initialState) {
    str current = initialState;
    f.id = newId(); 
    Figures buttons= [];
    if (g:graph():=f) {     
        g.event = on("load", void(str ev, str n, str v){
               map[str, str] q = getIdFig(f);   
               // current = g.nodes[0][0]; 
               list[Edge] out = [e|Edge e<-g.edges, e.from==current]; 
               int i = 0;
               for (Figure b<-buttons) {
                  attr(b.id, disabled = true);
                  style(b.id, visibility = "hidden");
                  }
               for (Edge e<-out) {
                  attr(buttons[i].id, disabled = false);
                  style(buttons[i].id, visibility = "visible");
                  textProperty(buttons[i].id, \text = e.label);
                  i = i+1;
               }
         });
         buttons = [buttonInput("", width = 200, height = 25, disabled = true, id = newName()
        ,event = on("click", void(str ev, str n, str v)(int p) {
             return void(str ev, str n, str v) {
              map[str, str] q = getIdFig(f);  
             style(q[current], fillColor="whitesmoke");    
             list[Edge] out = [e|Edge e<-g.edges, e.from==current];
             current=out[p].to; 
             out = [e|Edge e<-g.edges, e.from==current];    
             style(q[current], fillColor="#f77");
             for (Figure b<-buttons) {
                  attr(b.id, disabled = true);
                  style(b.id, visibility = "hidden");
                  }
             for (int i<-[0..size(out)]) {
                  attr(buttons[i].id, disabled = false);
                  style(buttons[i].id, visibility = "visible");
                  textProperty(buttons[i].id, \text=out[i].label);
                  }
       };}(i))
        )|int i <-[0..10]];
        Figure z = vcat(figs = buttons, height = 200, width = 200);
        return hcat(vgap = 0, align = topLeft, borderWidth  =4, borderStyle="ridge", figs=[z , g]);
        }
  }
  
 void setError(str n, bool e) {attr("<n>_msg", disabled = e);}
 
 bool isError(str n) = attr("<n>_msg").disabled;
 
 
 data MoneyType = euro(str)|usd(str);
 
 public str emptyStr="";
 public MoneyType emptyEuro=euro("");
 public MoneyType emptyUsd=usd("");
 
 public int emptyInt=-999999;
 
 str startValue(value v) {
    if (str s:=v) return s;
    if (int d:=v) return d==emptyInt?"":"<d>";
    if (euro(str z):=v) return z;
    if (usd(str z):=v) return z;
    return "unknown";
    }
  
  // alias FormEntry = tuple[str id, type[&T] tp, str fieldName, list[tuple[bool(value v) cond, str emsg]] constraints]; 
 list[Figure] formEntry(FormEntry fr) {
    str id = fr.id;
    bool isDig(value v) = str s:=v && /^[0-9]+$/:=s; 
    bool isMoney(value v) = str s:=v && (/^[0-9]+.[0-9][0-9]$/:=s|| /^[0-9]+$/ :=s); 
    void (str , str , str ) f(value q) {   
     return void (str e, str n, str v) {
       list[Constraint] constraints = fr.constraints;  
       str emsg = "";
       bool found = false;
       setError(id, false);  
       if (typeOf(q)==\int()) {
             if  (!isDig(v)) {
                emsg = "Illegal argument";
                found = true;
                }
             else d = toInt(v);
             }
       if (\adt("MoneyType",_):=typeOf(q)) {
             if  (!isMoney(v)) {
                emsg = "Illegal argument";
                found = true;
                }
             }
       while (!found && !isEmpty(constraints)) {
            tuple[Constraint, list[Constraint]] c = headTail(constraints);
            bool b = true;
            if (typeOf(q)==\str()) b = c[0].cond(v);
            if (\adt("MoneyType",_):=typeOf(q)) b = c[0].cond(toReal(v));
            if (typeOf(q)==\int()) b = c[0].cond(toInt(v));
            if (typeOf(q)==\tuple([\list(\str()), \str()])) b = c[0].cond(v); 
            if (!b) {
                  emsg = c[0].emsg;
                  emsg = replaceAll(emsg, "@", v);
                  found = true;
                  break;
                  }
            else constraints = c[1];
            }
       if (found) {
            textProperty("<id>_msg", html=emsg);
            setError(id, true);
            // clearValueProperty(n);
            }
       else clearTextProperty("<id>_msg");
       };
       }
        Figure question = emptyFigure(); 
        if (typeOf(fr.startValue)==\str() || typeOf(fr.startValue)==\int()
        || \adt("MoneyType",_):=typeOf(fr.startValue)) 
             question = strInput(nchars = 20, id = id, fillColor = "white"
                 ,\value =  startValue(fr.startValue),event=on(f(fr.startValue)));
        if (typeOf(fr.startValue)==\tuple([\list(\str()), \str()])) {
             if (tuple[list[str] fields, str checked] cs := fr.startValue)
             question = choiceInput(id = id, choices= cs[0], event=on(f(fr.startValue)), \value= cs[1]);
             }
       return [text(fr.fieldName, fontWeight="bold", fontColor="blue", size=<300, 20>)
       , question
       , text("", size=<300, 20>, id = "<id>_msg", fontColor="red")];
    }
    
 FormEntry rangeEntry(str id, str fieldName, int low, int high) {
    
        
    bool lowConstraint(value v) = int d:=v && d>=low;
      
    bool highConstraint(value v) = int d := v && d<=high;
    
    return <id, emptyInt, fieldName, [
         <lowConstraint, "Number @ too low (\<<low>)"> 
        ,<highConstraint, "Number @ too high (\><high>)"> 
        ]>;  
    }
    
 public void invoke(value g, str e, str n, str v) {
   switch (g) {
       case StrCallBack f: f(e, n, v);
       case IntCallBack f: f(e, n, toInt(v));
       case RealCallBack f: f(e, n, toReal(v));
       }
    }
  
 public Figure form(str id, list[FormEntry] fs, Event event = noEvent()
    , str fillColor="whitesmoke", str visibility = "hidden", int lineWidth = 1, str enableId="") {
    list[list[Figure]] fa = [formEntry(f)|FormEntry f <- fs];
    // if (isEmpty(fa)) return emptyFigure();
    // println("Inside form: <id>");
    Figure r = 
       box(id = id, vgrow = 1.5, visibility=visibility, fillColor=fillColor, lineWidth = lineWidth
       , fig=grid(id = newId(), figArray=fa, form = true,
    // , borderStyle="groove", borderWidth = 6
    hgap = 10,  visibility="inherit", align = topLeft
    ,event=on(void(str e, str n, str v){ 
             // bool ok = (true|it && (str q:=property(fr[1].id).\value) && !isEmpty(q)|fr<-fa);
             bool ok = (true|it && !isError(fr[1].id)|fr<-fa); 
             if (e=="ok") {
                if (ok) {
                  if (on (StrCallBack f) := event) invoke(f, e, id, v);
                  if (on (IntCallBack f) := event) invoke(f, e, id, v);
                  if (on (RealCallBack f) := event) invoke(f, e, id, v);
                  style(id, visibility="hidden"); 
                  clearForm(fa);                  
                  }
              }
             else {    
                 style(id, visibility="hidden");
                 clearForm(fa);
                 }
             if (!isEmpty(enableId)) attr(enableId, disabled=false);
             })
      // ,event = on("load", void(str e, str n , str v ) {println(e);})
     ));
     return r;         
     } 
        
 Figure makeFormAction(Figure button, list[FormEntry] forms, int x= 50, int y=50, Event event = noEvent()
    , str fillColor="whitesmoke", str visibility = "hidden", int lineWidth = 1, str id = "") {  
     if (isEmpty(button.id)) button.id = newId();
     Figure panel = form(isEmpty(id)?newId():id, forms, event = event, fillColor=fillColor, visibility = visibility
        ,lineWidth = lineWidth, enableId= button.id);
     panel.at=<x, y>;
     str frameId = "<panel.id>_frame";
     button.panel = frame(panel,visibility= visibility, id = frameId);
     // button.panel = box(fig=panel);
     if (!isEmpty(forms))
        button.event = on("click",
            void(str e, str n, str v) {
            style(panel.id, visibility="visible");
            attr(button.id, disabled=true);
          });
     else 
          button.event = on("click",
            void(str e, str n, str v) {
            if (on(void(str e, str n  , str v) f):=event) {
                invoke(f, e, panel.id, v);
                }
          });
     return button;
     }
     
void clearForm(list[list[Figure]] formArray) {
  for (list[Figure] forms<-formArray) {
        clearTextProperty(forms[2].id);
        clearValueProperty(forms[1].id);
      }
  }
  
  
    
