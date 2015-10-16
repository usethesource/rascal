module experiments::vis2::sandbox::FigureServer
import experiments::vis2::sandbox::Figure;
import experiments::vis2::sandbox::IFigure;
import Prelude;

public void render(Figure fig1, int width = 400, int height = 400, 
     Alignment align = <0.5, 0.5>, tuple[int, int] size = <0, 0>,
     str fillColor = "white", str lineColor = "black", bool debug = false, bool display = true)
     {
     setDebug(debug);
     _render(fig1, width = width,  height = height,  align = align, fillColor = fillColor,
     lineColor = lineColor, size = size);
     // println(toString());
     }
       
public str toHtmlString(Figure fig1, int width = 400, int height = 400, 
     Alignment align = <0.5, 0.5>, tuple[int, int] size = <0, 0>,
     str fillColor = "white", str lineColor = "black", bool debug = false)
     {
     setDebug(debug);
     _render(fig1, width = width,  height = height,  align = align, fillColor = fillColor,
     lineColor = lineColor, size = size, display = false);
     // return "aap";
     return getIntro();
     }


public Style style(str id, str fillColor="", str lineColor="", int lineWidth = -1,
     num fillOpacity = -1.0, num lineOpacity = -1.0, str visibility = "") {
     Style v = _getStyle(id);
     v.svg = isSvg(id);
     if (lineWidth!=-1) v.lineWidth = lineWidth;
     if (fillOpacity>=0) v.fillOpacity = fillOpacity;
     if (lineOpacity>=0) v.lineOpacity = lineOpacity;
     if (!isEmpty(fillColor)) v.fillColor = fillColor;
     if (!isEmpty(lineColor)) v.lineColor = lineColor;
     if (!isEmpty(visibility)) {
           v.visibility = visibility;
           list[str] xs = getDescendants(id);
           for (x<-xs) {
              style(x, visibility = visibility);
              }
             }
     _setStyle(id, v);
     return v;
     }

bool isEmptyValue(value v) {
    if (str x:=v) return isEmpty(x);
    return false;
    }
     
public Attr attr(str id, int width = -1, int height = -1, int r = -1
     , num grow = 1.0) {
     Attr v = _getAttr(id);
     if (width!=-1) v.width = width;
     if (height!=-1) v.height = height;
     if (grow>=0) v.grow = grow;    
     if (r!=-1) v.r = r;
     _setAttr(id, v);
     return v;
     }
     
public Property property(str id, value \value = "") {
    Property v = _getProperty(id);
    if (!isEmptyValue(\value)) v.\value = \value;
     _setProperty(id, v);
     return v;
    }

public Text textProperty(str id, str text = "", str html = "") {
     Text v = _getText(id);
     if (!isEmpty(text)) v.text = text;
     if (!isEmpty(html)) v.html = html;
     _setText(id, v);
     // println(v);
     return v;
     }
     
public Text clearTextProperty(str id) {
     Text v = _getText(id);
     v.text = "";
     _setText(id, v);
     //  println(v);
     return v;
     }
     
public Timer timer(str id, int delay = -1, str command = "") {
    Timer t = _getTimer(id);
    if (delay>=0) t.delay = delay;
    if (!isEmpty(command)) t.command = command;
     _setTimer(id, t);
     return t;
    }
