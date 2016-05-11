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
     , borderWidth = borderWidth, borderWidth = borderWidth, borderStyle = borderStyle, resizable = resizable,
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
       lineColor = lineColor, size = size, display = false
       ,borderWidth = borderWidth, borderWidth = borderWidth, borderStyle = borderStyle, resizable = resizable
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
         v.text = text;
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
     v.text = "";
     v.html = "";
     _setText(idx, v);
     //  println(v);
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
        Figures buttons = [buttonInput("", width = 200, height = 25, disabled = true, id = newName()
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
  
  // alias FormEntry = tuple[type[&T] tp, str fieldName, list[tuple[bool(value v) cond, str emsg]] constraints]; 
 list[Figure] formEntry(FormEntry fr) {
    str id = fr.id;
    void f(str e, str n, str v) {
       list[str] emsg = [d.emsg|d<-fr.constraints, !d.cond(v)];
       if (!isEmpty(emsg)) {
            textProperty("<id>_msg", html=head(emsg));
            clearValueProperty(n);
            }
       };
    if (fr.tp==#str) {
       return [text(fr.fieldName), strInput(nchars = 20, id = id, fillColor = "white"
       ,event=on(f)), text("", size=<200, 20>, id = "<id>_msg")];
       }
    }
    
 
  
 public Figure form(str id, list[FormEntry] fs, void(str, str, str) ifOk=void(str e, str n, str v){return;}) {
    list[list[Figure]] fa = [formEntry(f)|FormEntry f <- fs];
    Figure r = box(id = id, vgrow = 1.5, fillColor="whitesmoke", fig=grid(figArray=fa, form = true
    // , borderStyle="groove", borderWidth = 6
    , hgap = 10
    ,event=on(void(str e, str n, str v){ 
             bool ok = (true|it && (str q:=property(fr[1].id).\value) && !isEmpty(q)|fr<-fa);     
             if (e=="ok") {
                if (ok) {
                  ifOk(e, n, v);
                  style(id, visibility="hidden"); 
                  clearForm(fa);                  
                  }
              }
             else {
                 style(id, visibility="hidden");
                 clearForm(fa);
                 }
             })));
     return r;         
     } 
     
void clearForm(list[list[Figure]] formArray) {
  for (list[Figure] forms<-formArray) {
        clearTextProperty(forms[2].id);
        clearValueProperty(forms[1].id);
      }
  }
  
  
    
