module experiments::vis2::sandbox::FigureServer
import experiments::vis2::sandbox::Figure;
import experiments::vis2::sandbox::IFigure;

public void render(Figure fig1, int width = 400, int height = 400, 
     Alignment align = <0.5, 0.5>, tuple[int, int] size = <0, 0>,
     str fillColor = "white", str lineColor = "black", bool debug = true)
     {
     setDebug(debug);
     _render(fig1, width = width,  height = height,  align = align, fillColor = fillColor,
     lineColor = lineColor, size = size);
     }
