module experiments::vis2::sandbox::FigureServer
import experiments::vis2::sandbox::Figure;
import experiments::vis2::sandbox::IFigure;
import Prelude;

public void render(Figure fig1, int width = 800, int height = 800, 
     Alignment align = <0.5, 0.5>, tuple[int, int] size = <0, 0>,
     str fillColor = "none", str lineColor = "black", bool debug = false, bool display = true, 
     Event event = on(nullCallback), int borderWidth = -1, str borderStyle = "", str borderColor = ""
     ,int lineWidth = -1, bool resizable = true)
     {
     println("render:<size?>");
     setDebug(debug);
     _render(fig1, width = width,  height = height,  align = align, fillColor = fillColor
     , lineColor = lineColor, lineWidth = lineWidth, size = size, event = event
     , borderWidth = borderWidth, borderStyle = borderStyle, borderColor=borderColor
     , resizable = resizable, defined = (width? && height?)||(size?));
     // println(toString());
     }
       
public str toHtmlString(Figure fig1, int width = 400, int height = 400, 
     Alignment align = <0.5, 0.5>, tuple[int, int] size = <0, 0>,
     str fillColor = "white", str lineColor = "black", bool debug = false
     , int borderWidth = -1,  str borderColor = "", str borderStyle = "", bool resizable = true)
     {
     setDebug(debug);
     _render(fig1, width = width,  height = height,  align = align, fillColor = fillColor,
     lineColor = lineColor, size = size, display = false
     , borderWidth = borderWidth, borderWidth = borderWidth, borderStyle = borderStyle, resizable = resizable
     );
     // return "aap";
     return getIntro();
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
     if (!isEmpty(visibility)) {
           v.visibility = visibility;
           list[str] xs = getDescendants(idx);
           for (x<-xs) {
              style(x, visibility = visibility);
             }
            }
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
     if (!isEmpty(text)) v.text = text;
     if (!isEmpty(html)) v.html = html;
     _setText(idx, v);
     // println(v);
     return v;
     }
     
public Text clearTextProperty(str id) {
     str idx = child(id);
     Text v = _getText(idx);
     v.text = "";
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
    
