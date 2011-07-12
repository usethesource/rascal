@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module vis::Figure

import vis::KeySym;
import Integer;
import Real;
import List;
import Set;
import IO;
import String;
import ToString;

/*
 * Declarations and library functions for Rascal Visualization
 *
 * There are several sources of ugliness in the following definitions:
 * - data declarations cannot have varyadic parameters, hence we need a wrapper function for each constructor.
 * - Alternatives of a data declaration always need a constructor, hence many constructors have to be duplicated.
 * - We are awaiting the intro of key word parameters.
 */
 
/*
 * Wishlist:
 * - textures
 * - boxes with round corners
 * - drop shadows
 * - dashed/dotted lines
 * - ngons
 * - bitmap import and display
 * - new layouts (circular) treemap, icecle
 * - interaction
 */
 
 /*
  * Colors and color management
  */

alias Color = int;

/*
 * Decorations for source code lines:
 * - info, warning and error
 * - highlights (levels [0 .. 4] currently supported)
 * Used by:
 * - Outline
 * - editor
 */

public data LineDecoration = 
    info(int lineNumber, str msg)
  | warning(int lineNumber, str msg)
  | error(int lineNumber, str msg)
  | highlight(int lineNumber, str msg)
  | highlight(int lineNumber, str msg, int level)
  ;

@doc{Gray color (0-255)}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public Color java gray(int gray);

@doc{Gray color (0-255) with transparency}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public Color java gray(int gray, real alpha);

@doc{Gray color as percentage (0.0-1.0)}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public Color java gray(real perc);

@doc{Gray color with transparency}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public Color java gray(real perc, real alpha);

@doc{Named color}
@reflect{Needs calling context when generating an exception}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public Color java color(str colorName);

@doc{Named color with transparency}
@reflect{Needs calling context when generating an exception}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public Color java color(str colorName, real alpha);

@doc{Sorted list of all color names}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public list[str] java colorNames();

@doc{RGB color}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public Color java rgb(int r, int g, int b);

@doc{RGB color with transparency}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public Color java rgb(int r, int g, int b, real alpha);

@doc{Interpolate two colors (in RGB space)}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public Color java interpolateColor(Color from, Color to, real percentage);

@doc{Create a list of interpolated colors}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public list[Color] java colorSteps(Color from, Color to, int steps);

@doc{Create a colorscale from a list of numbers}
public Color(&T <: num) colorScale(list[&T <: num] values, Color from, Color to){
   mn = min(values);
   range = max(values) - mn;
   sc = colorSteps(from, to, 10);
   return Color(int v) { return sc[(9 * (v - mn)) / range]; };
}

@doc{Create a fixed color palette}
public list[str] p12 = [ "yellow", "aqua", "navy", "violet", 
                          "red", "darkviolet", "maroon", "green",
                          "teal", "blue", "olive", "lime"];
                          
public list[Color] p12Colors = [color(s) | s <- p12];

@doc{Return named color from fixed palette}
public str palette(int n){
  try 
  	return p12[n];
  catch:
    return "black";
}

/*
@doc{Create a list of font names}
@javaClass{org.rascalmpl.library.vis.FigureLibrary}
public list[str] java fontNames();
*/

/*
 * FProperty -- visual properties of visual elements
 */
 
 public FProperty left(){
   return halign(0.0);
 }
 
 public FProperty hcenter(){
   return halign(0.5);
 }
 
 public FProperty right(){
   return halign(1.0);
 }
 
 public FProperty top(){
   return valign(0.0);
 }
 
 public FProperty vcenter(){
   return valign(0.5);
 }
 
 public FProperty bottom(){
   return valign(1.0);
 }
 
 public FProperty center(){
   return align(0.5, 0.5);
}


 public FProperty stdLeft(){
   return stdHalign(0.0);
 }
 
 public FProperty stdHcenter(){
   return stdHalign(0.5);
 }
 
 public FProperty stdRight(){
   return stdHalign(1.0);
 }
 
 public FProperty stdTop(){
   return stdValign(0.0);
 }
 
 public FProperty stdVcenter(){
   return stdValign(0.5);
 }
 
 public FProperty stdBottom(){
   return stdValign(1.0);
 }
 
 public FProperty stdCenter(){
   return stdAlign(0.5, 0.5);
}


 public FProperty projectLeft(){
   return projectHalign(0.0);
 }
 
 public FProperty projectHcenter(){
   return projectHalign(0.5);
 }
 
 public FProperty projectRight(){
   return projectHalign(1.0);
 }
 
 public FProperty projectTop(){
   return projectValign(0.0);
 }
 
 public FProperty projectVcenter(){
   return projectValign(0.5);
 }
 
 public FProperty projectBottom(){
   return projectValign(1.0);
 }
 
 public FProperty projectCenter(){
   return projectValign(0.5, 0.5);
}


 public FProperty stdProjectLeft(){
   return stdProjectHalign(0.0);
 }
 
 public FProperty stdProjectHcenter(){
   return stdProjectHalign(0.5);
 }
 
 public FProperty stdProjectRight(){
   return stdProjectHalign(1.0);
 }
 
 public FProperty stdProjectTop(){
   return stdProjectValign(0.0);
 }
 
 public FProperty stdProjectVcenter(){
   return stdProjectValign(0.5);
 }
 
 public FProperty stdProjectBottom(){
   return stdProjectValign(1.0);
 }
 
 public FProperty stdProjectCenter(){
   return stdProjectValign(0.5, 0.5);
}

data Like = like(str id);
public data Measure = measure(num quantity,str axisId);

public alias FProperties = list[FProperty];
 
 alias computedBool = bool();
 alias computedInt	= int();
 alias computedReal = real();
 alias computedNum 	= num();
 alias computedStr 	= str();
 alias computedColor = Color();
 alias computedFigure = Figure();

 

data Convert = convert(value v, value id);

data FProperty =
	   timer                (int delay, int () cb)
	 | project              (Figure f0, str p0)
	 | _child               (FProperties props)
| align                (num                r0)
	| align                (computedNum       cr0)
	| align                (Like              lr0)
	| align                (Convert           mr0)
	| align                (num                r2, num                r1)
	| align                (num                r3, computedNum       cr1)
	| align                (num                r4, Like              lr1)
	| align                (num                r5, Convert           mr1)
	| align                (computedNum       cr3, num                r6)
	| align                (computedNum       cr4, computedNum       cr2)
	| align                (computedNum       cr5, Like              lr2)
	| align                (computedNum       cr6, Convert           mr2)
	| align                (Like              lr4, num                r7)
	| align                (Like              lr5, computedNum       cr7)
	| align                (Like              lr6, Like              lr3)
	| align                (Like              lr7, Convert           mr3)
	| align                (Convert           mr5, num                r8)
	| align                (Convert           mr6, computedNum       cr8)
	| align                (Convert           mr7, Like              lr8)
	| align                (Convert           mr8, Convert           mr4)
	| connect              (num                r0)
	| connect              (computedNum       cr0)
	| connect              (Like              lr0)
	| connect              (Convert           mr0)
	| connect              (num                r2, num                r1)
	| connect              (num                r3, computedNum       cr1)
	| connect              (num                r4, Like              lr1)
	| connect              (num                r5, Convert           mr1)
	| connect              (computedNum       cr3, num                r6)
	| connect              (computedNum       cr4, computedNum       cr2)
	| connect              (computedNum       cr5, Like              lr2)
	| connect              (computedNum       cr6, Convert           mr2)
	| connect              (Like              lr4, num                r7)
	| connect              (Like              lr5, computedNum       cr7)
	| connect              (Like              lr6, Like              lr3)
	| connect              (Like              lr7, Convert           mr3)
	| connect              (Convert           mr5, num                r8)
	| connect              (Convert           mr6, computedNum       cr8)
	| connect              (Convert           mr7, Like              lr8)
	| connect              (Convert           mr8, Convert           mr4)
	| direction            (str                s0)
	| direction            (computedStr       cs0)
	| direction            (Like              ls0)
	| direction            (Convert           ms0)
	| doi                  (int                i0)
	| doi                  (computedInt       ci0)
	| doi                  (Like              li0)
	| doi                  (Convert           mi0)
	| fillColor            (Color              c0)
	| fillColor            (str               sc0)
	| fillColor            (computedColor     cc0)
	| fillColor            (Like              lc0)
	| fillColor            (Convert           mc0)
	| font                 (str                s0)
	| font                 (computedStr       cs0)
	| font                 (Like              ls0)
	| font                 (Convert           ms0)
	| fontColor            (Color              c0)
	| fontColor            (str               sc0)
	| fontColor            (computedColor     cc0)
	| fontColor            (Like              lc0)
	| fontColor            (Convert           mc0)
	| fontSize             (int                i0)
	| fontSize             (computedInt       ci0)
	| fontSize             (Like              li0)
	| fontSize             (Convert           mi0)
	| fromAngle            (num                r0)
	| fromAngle            (computedNum       cr0)
	| fromAngle            (Like              lr0)
	| fromAngle            (Convert           mr0)
	| fromArrow            (Figure             f0)
	| fromArrow            (computedFigure    cf0)
	| fromArrow            (Like              lf0)
	| fromArrow            (Convert           mf0)
	| gap                  (num                r0)
	| gap                  (computedNum       cr0)
	| gap                  (Like              lr0)
	| gap                  (Convert           mr0)
	| gap                  (num                r2, num                r1)
	| gap                  (num                r3, computedNum       cr1)
	| gap                  (num                r4, Like              lr1)
	| gap                  (num                r5, Convert           mr1)
	| gap                  (computedNum       cr3, num                r6)
	| gap                  (computedNum       cr4, computedNum       cr2)
	| gap                  (computedNum       cr5, Like              lr2)
	| gap                  (computedNum       cr6, Convert           mr2)
	| gap                  (Like              lr4, num                r7)
	| gap                  (Like              lr5, computedNum       cr7)
	| gap                  (Like              lr6, Like              lr3)
	| gap                  (Like              lr7, Convert           mr3)
	| gap                  (Convert           mr5, num                r8)
	| gap                  (Convert           mr6, computedNum       cr8)
	| gap                  (Convert           mr7, Like              lr8)
	| gap                  (Convert           mr8, Convert           mr4)
	| grow                 (num                r0)
	| grow                 (computedNum       cr0)
	| grow                 (Like              lr0)
	| grow                 (Convert           mr0)
	| grow                 (num                r2, num                r1)
	| grow                 (num                r3, computedNum       cr1)
	| grow                 (num                r4, Like              lr1)
	| grow                 (num                r5, Convert           mr1)
	| grow                 (computedNum       cr3, num                r6)
	| grow                 (computedNum       cr4, computedNum       cr2)
	| grow                 (computedNum       cr5, Like              lr2)
	| grow                 (computedNum       cr6, Convert           mr2)
	| grow                 (Like              lr4, num                r7)
	| grow                 (Like              lr5, computedNum       cr7)
	| grow                 (Like              lr6, Like              lr3)
	| grow                 (Like              lr7, Convert           mr3)
	| grow                 (Convert           mr5, num                r8)
	| grow                 (Convert           mr6, computedNum       cr8)
	| grow                 (Convert           mr7, Like              lr8)
	| grow                 (Convert           mr8, Convert           mr4)
	| guideColor           (Color              c0)
	| guideColor           (str               sc0)
	| guideColor           (computedColor     cc0)
	| guideColor           (Like              lc0)
	| guideColor           (Convert           mc0)
	| halign               (num                r0)
	| halign               (computedNum       cr0)
	| halign               (Like              lr0)
	| halign               (Convert           mr0)
	| hcapGaps             (bool               b0)
	| hcapGaps             (computedBool      cb0)
	| hcapGaps             (Like              lb0)
	| hcapGaps             (Convert           mb0)
	| hcapGaps             (bool               b2, bool               b1)
	| hcapGaps             (bool               b3, computedBool      cb1)
	| hcapGaps             (bool               b4, Like              lb1)
	| hcapGaps             (bool               b5, Convert           mb1)
	| hcapGaps             (computedBool      cb3, bool               b6)
	| hcapGaps             (computedBool      cb4, computedBool      cb2)
	| hcapGaps             (computedBool      cb5, Like              lb2)
	| hcapGaps             (computedBool      cb6, Convert           mb2)
	| hcapGaps             (Like              lb4, bool               b7)
	| hcapGaps             (Like              lb5, computedBool      cb7)
	| hcapGaps             (Like              lb6, Like              lb3)
	| hcapGaps             (Like              lb7, Convert           mb3)
	| hcapGaps             (Convert           mb5, bool               b8)
	| hcapGaps             (Convert           mb6, computedBool      cb8)
	| hcapGaps             (Convert           mb7, Like              lb8)
	| hcapGaps             (Convert           mb8, Convert           mb4)
	| hconnect             (num                r0)
	| hconnect             (computedNum       cr0)
	| hconnect             (Like              lr0)
	| hconnect             (Convert           mr0)
	| height               (num                r0)
	| height               (computedNum       cr0)
	| height               (Like              lr0)
	| height               (Convert           mr0)
	| hendGap              (bool               b0)
	| hendGap              (computedBool      cb0)
	| hendGap              (Like              lb0)
	| hendGap              (Convert           mb0)
	| hgap                 (num                r0)
	| hgap                 (computedNum       cr0)
	| hgap                 (Like              lr0)
	| hgap                 (Convert           mr0)
	| hgrow                (num                r0)
	| hgrow                (computedNum       cr0)
	| hgrow                (Like              lr0)
	| hgrow                (Convert           mr0)
	| hint                 (str                s0)
	| hint                 (computedStr       cs0)
	| hint                 (Like              ls0)
	| hint                 (Convert           ms0)
	| hpos                 (num                r0)
	| hpos                 (computedNum       cr0)
	| hpos                 (Like              lr0)
	| hpos                 (Convert           mr0)
	| hresizable           (bool               b0)
	| hresizable           (computedBool      cb0)
	| hresizable           (Like              lb0)
	| hresizable           (Convert           mb0)
	| hshrink              (num                r0)
	| hshrink              (computedNum       cr0)
	| hshrink              (Like              lr0)
	| hshrink              (Convert           mr0)
	| hstartGap            (bool               b0)
	| hstartGap            (computedBool      cb0)
	| hstartGap            (Like              lb0)
	| hstartGap            (Convert           mb0)
	| id                   (str                s0)
	| id                   (computedStr       cs0)
	| id                   (Like              ls0)
	| id                   (Convert           ms0)
	| innerRadius          (num                r0)
	| innerRadius          (computedNum       cr0)
	| innerRadius          (Like              lr0)
	| innerRadius          (Convert           mr0)
	| label                (Figure             f0)
	| label                (computedFigure    cf0)
	| label                (Like              lf0)
	| label                (Convert           mf0)
	| layer                (str                s0)
	| layer                (computedStr       cs0)
	| layer                (Like              ls0)
	| layer                (Convert           ms0)
	| lineColor            (Color              c0)
	| lineColor            (str               sc0)
	| lineColor            (computedColor     cc0)
	| lineColor            (Like              lc0)
	| lineColor            (Convert           mc0)
	| lineStyle            (str               s0)  // solid, dash, dot, dashdot, dashdotdot
	| lineStyle            (computedStr       cs0)
	| lineStyle            (Like              ls0)
	| lineStyle            (Convert           ms0)
	| lineWidth            (num                r0)
	| lineWidth            (computedNum       cr0)
	| lineWidth            (Like              lr0)
	| lineWidth            (Convert           mr0)
	| mouseOver            (Figure             f0)
	| mouseOver            (computedFigure    cf0)
	| mouseOver            (Like              lf0)
	| mouseOver            (Convert           mf0)
	| mouseOverAlign       (num                r0)
	| mouseOverAlign       (computedNum       cr0)
	| mouseOverAlign       (Like              lr0)
	| mouseOverAlign       (Convert           mr0)
	| mouseOverAlign       (num                r2, num                r1)
	| mouseOverAlign       (num                r3, computedNum       cr1)
	| mouseOverAlign       (num                r4, Like              lr1)
	| mouseOverAlign       (num                r5, Convert           mr1)
	| mouseOverAlign       (computedNum       cr3, num                r6)
	| mouseOverAlign       (computedNum       cr4, computedNum       cr2)
	| mouseOverAlign       (computedNum       cr5, Like              lr2)
	| mouseOverAlign       (computedNum       cr6, Convert           mr2)
	| mouseOverAlign       (Like              lr4, num                r7)
	| mouseOverAlign       (Like              lr5, computedNum       cr7)
	| mouseOverAlign       (Like              lr6, Like              lr3)
	| mouseOverAlign       (Like              lr7, Convert           mr3)
	| mouseOverAlign       (Convert           mr5, num                r8)
	| mouseOverAlign       (Convert           mr6, computedNum       cr8)
	| mouseOverAlign       (Convert           mr7, Like              lr8)
	| mouseOverAlign       (Convert           mr8, Convert           mr4)
	| mouseOverHalign      (num                r0)
	| mouseOverHalign      (computedNum       cr0)
	| mouseOverHalign      (Like              lr0)
	| mouseOverHalign      (Convert           mr0)
	| mouseOverValign      (num                r0)
	| mouseOverValign      (computedNum       cr0)
	| mouseOverValign      (Like              lr0)
	| mouseOverValign      (Convert           mr0)
	| mouseStick           (bool               b0)
	| mouseStick           (computedBool      cb0)
	| mouseStick           (Like              lb0)
	| mouseStick           (Convert           mb0)
	| onClick              (void ()           h0)
	| onKeyDown            (void (KeySym,map[KeyModifier,bool]) kh0)
	| onKeyUp              (void (KeySym,map[KeyModifier,bool]) kh0)
	| onMouseOff           (void ()           h0)
	| onMouseOver          (void ()           h0)
	| pos                  (num                r0)
	| pos                  (computedNum       cr0)
	| pos                  (Like              lr0)
	| pos                  (Convert           mr0)
	| pos                  (num                r2, num                r1)
	| pos                  (num                r3, computedNum       cr1)
	| pos                  (num                r4, Like              lr1)
	| pos                  (num                r5, Convert           mr1)
	| pos                  (computedNum       cr3, num                r6)
	| pos                  (computedNum       cr4, computedNum       cr2)
	| pos                  (computedNum       cr5, Like              lr2)
	| pos                  (computedNum       cr6, Convert           mr2)
	| pos                  (Like              lr4, num                r7)
	| pos                  (Like              lr5, computedNum       cr7)
	| pos                  (Like              lr6, Like              lr3)
	| pos                  (Like              lr7, Convert           mr3)
	| pos                  (Convert           mr5, num                r8)
	| pos                  (Convert           mr6, computedNum       cr8)
	| pos                  (Convert           mr7, Like              lr8)
	| pos                  (Convert           mr8, Convert           mr4)
	| resizable            (bool               b0)
	| resizable            (computedBool      cb0)
	| resizable            (Like              lb0)
	| resizable            (Convert           mb0)
	| resizable            (bool               b2, bool               b1)
	| resizable            (bool               b3, computedBool      cb1)
	| resizable            (bool               b4, Like              lb1)
	| resizable            (bool               b5, Convert           mb1)
	| resizable            (computedBool      cb3, bool               b6)
	| resizable            (computedBool      cb4, computedBool      cb2)
	| resizable            (computedBool      cb5, Like              lb2)
	| resizable            (computedBool      cb6, Convert           mb2)
	| resizable            (Like              lb4, bool               b7)
	| resizable            (Like              lb5, computedBool      cb7)
	| resizable            (Like              lb6, Like              lb3)
	| resizable            (Like              lb7, Convert           mb3)
	| resizable            (Convert           mb5, bool               b8)
	| resizable            (Convert           mb6, computedBool      cb8)
	| resizable            (Convert           mb7, Like              lb8)
	| resizable            (Convert           mb8, Convert           mb4)
	| shadow               (bool               b0)
	| shadow               (computedBool      cb0)
	| shadow               (Like              lb0)
	| shadow               (Convert           mb0)
	| shadowColor          (Color              c0)
	| shadowColor          (str               sc0)
	| shadowColor          (computedColor     cc0)
	| shadowColor          (Like              lc0)
	| shadowColor          (Convert           mc0)
	| shadowLeft           (num                r0)
	| shadowLeft           (computedNum       cr0)
	| shadowLeft           (Like              lr0)
	| shadowLeft           (Convert           mr0)
	| shadowTop            (num                r0)
	| shadowTop            (computedNum       cr0)
	| shadowTop            (Like              lr0)
	| shadowTop            (Convert           mr0)
	| shapeClosed          (bool               b0)
	| shapeClosed          (computedBool      cb0)
	| shapeClosed          (Like              lb0)
	| shapeClosed          (Convert           mb0)
	| shapeConnected       (bool               b0)
	| shapeConnected       (computedBool      cb0)
	| shapeConnected       (Like              lb0)
	| shapeConnected       (Convert           mb0)
	| shapeCurved          (bool               b0)
	| shapeCurved          (computedBool      cb0)
	| shapeCurved          (Like              lb0)
	| shapeCurved          (Convert           mb0)
	| shrink               (num                r0)
	| shrink               (computedNum       cr0)
	| shrink               (Like              lr0)
	| shrink               (Convert           mr0)
	| shrink               (num                r2, num                r1)
	| shrink               (num                r3, computedNum       cr1)
	| shrink               (num                r4, Like              lr1)
	| shrink               (num                r5, Convert           mr1)
	| shrink               (computedNum       cr3, num                r6)
	| shrink               (computedNum       cr4, computedNum       cr2)
	| shrink               (computedNum       cr5, Like              lr2)
	| shrink               (computedNum       cr6, Convert           mr2)
	| shrink               (Like              lr4, num                r7)
	| shrink               (Like              lr5, computedNum       cr7)
	| shrink               (Like              lr6, Like              lr3)
	| shrink               (Like              lr7, Convert           mr3)
	| shrink               (Convert           mr5, num                r8)
	| shrink               (Convert           mr6, computedNum       cr8)
	| shrink               (Convert           mr7, Like              lr8)
	| shrink               (Convert           mr8, Convert           mr4)
	| size                 (num                r0)
	| size                 (computedNum       cr0)
	| size                 (Like              lr0)
	| size                 (Convert           mr0)
	| size                 (num                r2, num                r1)
	| size                 (num                r3, computedNum       cr1)
	| size                 (num                r4, Like              lr1)
	| size                 (num                r5, Convert           mr1)
	| size                 (computedNum       cr3, num                r6)
	| size                 (computedNum       cr4, computedNum       cr2)
	| size                 (computedNum       cr5, Like              lr2)
	| size                 (computedNum       cr6, Convert           mr2)
	| size                 (Like              lr4, num                r7)
	| size                 (Like              lr5, computedNum       cr7)
	| size                 (Like              lr6, Like              lr3)
	| size                 (Like              lr7, Convert           mr3)
	| size                 (Convert           mr5, num                r8)
	| size                 (Convert           mr6, computedNum       cr8)
	| size                 (Convert           mr7, Like              lr8)
	| size                 (Convert           mr8, Convert           mr4)
	| text                 (str                s0)
	| text                 (computedStr       cs0)
	| text                 (Like              ls0)
	| text                 (Convert           ms0)
	| textAngle            (num                r0)
	| textAngle            (computedNum       cr0)
	| textAngle            (Like              lr0)
	| textAngle            (Convert           mr0)
	| toAngle              (num                r0)
	| toAngle              (computedNum       cr0)
	| toAngle              (Like              lr0)
	| toAngle              (Convert           mr0)
	| toArrow              (Figure             f0)
	| toArrow              (computedFigure    cf0)
	| toArrow              (Like              lf0)
	| toArrow              (Convert           mf0)
	| valign               (num                r0)
	| valign               (computedNum       cr0)
	| valign               (Like              lr0)
	| valign               (Convert           mr0)
	| vcapGaps             (bool               b0)
	| vcapGaps             (computedBool      cb0)
	| vcapGaps             (Like              lb0)
	| vcapGaps             (Convert           mb0)
	| vcapGaps             (bool               b2, bool               b1)
	| vcapGaps             (bool               b3, computedBool      cb1)
	| vcapGaps             (bool               b4, Like              lb1)
	| vcapGaps             (bool               b5, Convert           mb1)
	| vcapGaps             (computedBool      cb3, bool               b6)
	| vcapGaps             (computedBool      cb4, computedBool      cb2)
	| vcapGaps             (computedBool      cb5, Like              lb2)
	| vcapGaps             (computedBool      cb6, Convert           mb2)
	| vcapGaps             (Like              lb4, bool               b7)
	| vcapGaps             (Like              lb5, computedBool      cb7)
	| vcapGaps             (Like              lb6, Like              lb3)
	| vcapGaps             (Like              lb7, Convert           mb3)
	| vcapGaps             (Convert           mb5, bool               b8)
	| vcapGaps             (Convert           mb6, computedBool      cb8)
	| vcapGaps             (Convert           mb7, Like              lb8)
	| vcapGaps             (Convert           mb8, Convert           mb4)
	| vconnect             (num                r0)
	| vconnect             (computedNum       cr0)
	| vconnect             (Like              lr0)
	| vconnect             (Convert           mr0)
	| vendGap              (bool               b0)
	| vendGap              (computedBool      cb0)
	| vendGap              (Like              lb0)
	| vendGap              (Convert           mb0)
	| vgap                 (num                r0)
	| vgap                 (computedNum       cr0)
	| vgap                 (Like              lr0)
	| vgap                 (Convert           mr0)
	| vgrow                (num                r0)
	| vgrow                (computedNum       cr0)
	| vgrow                (Like              lr0)
	| vgrow                (Convert           mr0)
	| vpos                 (num                r0)
	| vpos                 (computedNum       cr0)
	| vpos                 (Like              lr0)
	| vpos                 (Convert           mr0)
	| vresizable           (bool               b0)
	| vresizable           (computedBool      cb0)
	| vresizable           (Like              lb0)
	| vresizable           (Convert           mb0)
	| vshrink              (num                r0)
	| vshrink              (computedNum       cr0)
	| vshrink              (Like              lr0)
	| vshrink              (Convert           mr0)
	| vstartGap            (bool               b0)
	| vstartGap            (computedBool      cb0)
	| vstartGap            (Like              lb0)
	| vstartGap            (Convert           mb0)
	| width                (num                r0)
	| width                (computedNum       cr0)
	| width                (Like              lr0)
	| width                (Convert           mr0)
	| stdAlign             (num                r0)
	| stdAlign             (computedNum       cr0)
	| stdAlign             (Like              lr0)
	| stdAlign             (Convert           mr0)
	| stdAlign             (num                r2, num                r1)
	| stdAlign             (num                r3, computedNum       cr1)
	| stdAlign             (num                r4, Like              lr1)
	| stdAlign             (num                r5, Convert           mr1)
	| stdAlign             (computedNum       cr3, num                r6)
	| stdAlign             (computedNum       cr4, computedNum       cr2)
	| stdAlign             (computedNum       cr5, Like              lr2)
	| stdAlign             (computedNum       cr6, Convert           mr2)
	| stdAlign             (Like              lr4, num                r7)
	| stdAlign             (Like              lr5, computedNum       cr7)
	| stdAlign             (Like              lr6, Like              lr3)
	| stdAlign             (Like              lr7, Convert           mr3)
	| stdAlign             (Convert           mr5, num                r8)
	| stdAlign             (Convert           mr6, computedNum       cr8)
	| stdAlign             (Convert           mr7, Like              lr8)
	| stdAlign             (Convert           mr8, Convert           mr4)
	| stdConnect           (num                r0)
	| stdConnect           (computedNum       cr0)
	| stdConnect           (Like              lr0)
	| stdConnect           (Convert           mr0)
	| stdConnect           (num                r2, num                r1)
	| stdConnect           (num                r3, computedNum       cr1)
	| stdConnect           (num                r4, Like              lr1)
	| stdConnect           (num                r5, Convert           mr1)
	| stdConnect           (computedNum       cr3, num                r6)
	| stdConnect           (computedNum       cr4, computedNum       cr2)
	| stdConnect           (computedNum       cr5, Like              lr2)
	| stdConnect           (computedNum       cr6, Convert           mr2)
	| stdConnect           (Like              lr4, num                r7)
	| stdConnect           (Like              lr5, computedNum       cr7)
	| stdConnect           (Like              lr6, Like              lr3)
	| stdConnect           (Like              lr7, Convert           mr3)
	| stdConnect           (Convert           mr5, num                r8)
	| stdConnect           (Convert           mr6, computedNum       cr8)
	| stdConnect           (Convert           mr7, Like              lr8)
	| stdConnect           (Convert           mr8, Convert           mr4)
	| stdDirection         (str                s0)
	| stdDirection         (computedStr       cs0)
	| stdDirection         (Like              ls0)
	| stdDirection         (Convert           ms0)
	| stdDoi               (int                i0)
	| stdDoi               (computedInt       ci0)
	| stdDoi               (Like              li0)
	| stdDoi               (Convert           mi0)
	| stdFillColor         (Color              c0)
	| stdFillColor         (str               sc0)
	| stdFillColor         (computedColor     cc0)
	| stdFillColor         (Like              lc0)
	| stdFillColor         (Convert           mc0)
	| stdFont              (str                s0)
	| stdFont              (computedStr       cs0)
	| stdFont              (Like              ls0)
	| stdFont              (Convert           ms0)
	| stdFontColor         (Color              c0)
	| stdFontColor         (str               sc0)
	| stdFontColor         (computedColor     cc0)
	| stdFontColor         (Like              lc0)
	| stdFontColor         (Convert           mc0)
	| stdFontSize          (int                i0)
	| stdFontSize          (computedInt       ci0)
	| stdFontSize          (Like              li0)
	| stdFontSize          (Convert           mi0)
	| stdFromAngle         (num                r0)
	| stdFromAngle         (computedNum       cr0)
	| stdFromAngle         (Like              lr0)
	| stdFromAngle         (Convert           mr0)
	| stdFromArrow         (Figure             f0)
	| stdFromArrow         (computedFigure    cf0)
	| stdFromArrow         (Like              lf0)
	| stdFromArrow         (Convert           mf0)
	| stdGap               (num                r0)
	| stdGap               (computedNum       cr0)
	| stdGap               (Like              lr0)
	| stdGap               (Convert           mr0)
	| stdGap               (num                r2, num                r1)
	| stdGap               (num                r3, computedNum       cr1)
	| stdGap               (num                r4, Like              lr1)
	| stdGap               (num                r5, Convert           mr1)
	| stdGap               (computedNum       cr3, num                r6)
	| stdGap               (computedNum       cr4, computedNum       cr2)
	| stdGap               (computedNum       cr5, Like              lr2)
	| stdGap               (computedNum       cr6, Convert           mr2)
	| stdGap               (Like              lr4, num                r7)
	| stdGap               (Like              lr5, computedNum       cr7)
	| stdGap               (Like              lr6, Like              lr3)
	| stdGap               (Like              lr7, Convert           mr3)
	| stdGap               (Convert           mr5, num                r8)
	| stdGap               (Convert           mr6, computedNum       cr8)
	| stdGap               (Convert           mr7, Like              lr8)
	| stdGap               (Convert           mr8, Convert           mr4)
	| stdGrow              (num                r0)
	| stdGrow              (computedNum       cr0)
	| stdGrow              (Like              lr0)
	| stdGrow              (Convert           mr0)
	| stdGrow              (num                r2, num                r1)
	| stdGrow              (num                r3, computedNum       cr1)
	| stdGrow              (num                r4, Like              lr1)
	| stdGrow              (num                r5, Convert           mr1)
	| stdGrow              (computedNum       cr3, num                r6)
	| stdGrow              (computedNum       cr4, computedNum       cr2)
	| stdGrow              (computedNum       cr5, Like              lr2)
	| stdGrow              (computedNum       cr6, Convert           mr2)
	| stdGrow              (Like              lr4, num                r7)
	| stdGrow              (Like              lr5, computedNum       cr7)
	| stdGrow              (Like              lr6, Like              lr3)
	| stdGrow              (Like              lr7, Convert           mr3)
	| stdGrow              (Convert           mr5, num                r8)
	| stdGrow              (Convert           mr6, computedNum       cr8)
	| stdGrow              (Convert           mr7, Like              lr8)
	| stdGrow              (Convert           mr8, Convert           mr4)
	| stdGuideColor        (Color              c0)
	| stdGuideColor        (str               sc0)
	| stdGuideColor        (computedColor     cc0)
	| stdGuideColor        (Like              lc0)
	| stdGuideColor        (Convert           mc0)
	| stdHalign            (num                r0)
	| stdHalign            (computedNum       cr0)
	| stdHalign            (Like              lr0)
	| stdHalign            (Convert           mr0)
	| stdHcapGaps          (bool               b0)
	| stdHcapGaps          (computedBool      cb0)
	| stdHcapGaps          (Like              lb0)
	| stdHcapGaps          (Convert           mb0)
	| stdHcapGaps          (bool               b2, bool               b1)
	| stdHcapGaps          (bool               b3, computedBool      cb1)
	| stdHcapGaps          (bool               b4, Like              lb1)
	| stdHcapGaps          (bool               b5, Convert           mb1)
	| stdHcapGaps          (computedBool      cb3, bool               b6)
	| stdHcapGaps          (computedBool      cb4, computedBool      cb2)
	| stdHcapGaps          (computedBool      cb5, Like              lb2)
	| stdHcapGaps          (computedBool      cb6, Convert           mb2)
	| stdHcapGaps          (Like              lb4, bool               b7)
	| stdHcapGaps          (Like              lb5, computedBool      cb7)
	| stdHcapGaps          (Like              lb6, Like              lb3)
	| stdHcapGaps          (Like              lb7, Convert           mb3)
	| stdHcapGaps          (Convert           mb5, bool               b8)
	| stdHcapGaps          (Convert           mb6, computedBool      cb8)
	| stdHcapGaps          (Convert           mb7, Like              lb8)
	| stdHcapGaps          (Convert           mb8, Convert           mb4)
	| stdHconnect          (num                r0)
	| stdHconnect          (computedNum       cr0)
	| stdHconnect          (Like              lr0)
	| stdHconnect          (Convert           mr0)
	| stdHeight            (num                r0)
	| stdHeight            (computedNum       cr0)
	| stdHeight            (Like              lr0)
	| stdHeight            (Convert           mr0)
	| stdHendGap           (bool               b0)
	| stdHendGap           (computedBool      cb0)
	| stdHendGap           (Like              lb0)
	| stdHendGap           (Convert           mb0)
	| stdHgap              (num                r0)
	| stdHgap              (computedNum       cr0)
	| stdHgap              (Like              lr0)
	| stdHgap              (Convert           mr0)
	| stdHgrow             (num                r0)
	| stdHgrow             (computedNum       cr0)
	| stdHgrow             (Like              lr0)
	| stdHgrow             (Convert           mr0)
	| stdHint              (str                s0)
	| stdHint              (computedStr       cs0)
	| stdHint              (Like              ls0)
	| stdHint              (Convert           ms0)
	| stdHpos              (num                r0)
	| stdHpos              (computedNum       cr0)
	| stdHpos              (Like              lr0)
	| stdHpos              (Convert           mr0)
	| stdHresizable        (bool               b0)
	| stdHresizable        (computedBool      cb0)
	| stdHresizable        (Like              lb0)
	| stdHresizable        (Convert           mb0)
	| stdHshrink           (num                r0)
	| stdHshrink           (computedNum       cr0)
	| stdHshrink           (Like              lr0)
	| stdHshrink           (Convert           mr0)
	| stdHstartGap         (bool               b0)
	| stdHstartGap         (computedBool      cb0)
	| stdHstartGap         (Like              lb0)
	| stdHstartGap         (Convert           mb0)
	| stdId                (str                s0)
	| stdId                (computedStr       cs0)
	| stdId                (Like              ls0)
	| stdId                (Convert           ms0)
	| stdInnerRadius       (num                r0)
	| stdInnerRadius       (computedNum       cr0)
	| stdInnerRadius       (Like              lr0)
	| stdInnerRadius       (Convert           mr0)
	| stdLabel             (Figure             f0)
	| stdLabel             (computedFigure    cf0)
	| stdLabel             (Like              lf0)
	| stdLabel             (Convert           mf0)
	| stdLayer             (str                s0)
	| stdLayer             (computedStr       cs0)
	| stdLayer             (Like              ls0)
	| stdLayer             (Convert           ms0)
	| stdLineColor         (Color              c0)
	| stdLineColor         (str               sc0)
	| stdLineColor         (computedColor     cc0)
	| stdLineColor         (Like              lc0)
	| stdLineColor         (Convert           mc0)
	| stdLineWidth         (num                r0)
	| stdLineWidth         (computedNum       cr0)
	| stdLineWidth         (Like              lr0)
	| stdLineWidth         (Convert           mr0)
	| stdMouseOver         (Figure             f0)
	| stdMouseOver         (computedFigure    cf0)
	| stdMouseOver         (Like              lf0)
	| stdMouseOver         (Convert           mf0)
	| stdMouseOverAlign    (num                r0)
	| stdMouseOverAlign    (computedNum       cr0)
	| stdMouseOverAlign    (Like              lr0)
	| stdMouseOverAlign    (Convert           mr0)
	| stdMouseOverAlign    (num                r2, num                r1)
	| stdMouseOverAlign    (num                r3, computedNum       cr1)
	| stdMouseOverAlign    (num                r4, Like              lr1)
	| stdMouseOverAlign    (num                r5, Convert           mr1)
	| stdMouseOverAlign    (computedNum       cr3, num                r6)
	| stdMouseOverAlign    (computedNum       cr4, computedNum       cr2)
	| stdMouseOverAlign    (computedNum       cr5, Like              lr2)
	| stdMouseOverAlign    (computedNum       cr6, Convert           mr2)
	| stdMouseOverAlign    (Like              lr4, num                r7)
	| stdMouseOverAlign    (Like              lr5, computedNum       cr7)
	| stdMouseOverAlign    (Like              lr6, Like              lr3)
	| stdMouseOverAlign    (Like              lr7, Convert           mr3)
	| stdMouseOverAlign    (Convert           mr5, num                r8)
	| stdMouseOverAlign    (Convert           mr6, computedNum       cr8)
	| stdMouseOverAlign    (Convert           mr7, Like              lr8)
	| stdMouseOverAlign    (Convert           mr8, Convert           mr4)
	| stdMouseOverHalign   (num                r0)
	| stdMouseOverHalign   (computedNum       cr0)
	| stdMouseOverHalign   (Like              lr0)
	| stdMouseOverHalign   (Convert           mr0)
	| stdMouseOverValign   (num                r0)
	| stdMouseOverValign   (computedNum       cr0)
	| stdMouseOverValign   (Like              lr0)
	| stdMouseOverValign   (Convert           mr0)
	| stdMouseStick        (bool               b0)
	| stdMouseStick        (computedBool      cb0)
	| stdMouseStick        (Like              lb0)
	| stdMouseStick        (Convert           mb0)
	| stdOnClick           (void ()           h0)
	| stdOnKeyDown         (void (KeySym,map[KeyModifier,bool]) kh0)
	| stdOnKeyUp           (void (KeySym,map[KeyModifier,bool]) kh0)
	| stdOnMouseOff        (void ()           h0)
	| stdOnMouseOver       (void ()           h0)
	| stdPos               (num                r0)
	| stdPos               (computedNum       cr0)
	| stdPos               (Like              lr0)
	| stdPos               (Convert           mr0)
	| stdPos               (num                r2, num                r1)
	| stdPos               (num                r3, computedNum       cr1)
	| stdPos               (num                r4, Like              lr1)
	| stdPos               (num                r5, Convert           mr1)
	| stdPos               (computedNum       cr3, num                r6)
	| stdPos               (computedNum       cr4, computedNum       cr2)
	| stdPos               (computedNum       cr5, Like              lr2)
	| stdPos               (computedNum       cr6, Convert           mr2)
	| stdPos               (Like              lr4, num                r7)
	| stdPos               (Like              lr5, computedNum       cr7)
	| stdPos               (Like              lr6, Like              lr3)
	| stdPos               (Like              lr7, Convert           mr3)
	| stdPos               (Convert           mr5, num                r8)
	| stdPos               (Convert           mr6, computedNum       cr8)
	| stdPos               (Convert           mr7, Like              lr8)
	| stdPos               (Convert           mr8, Convert           mr4)
	| stdResizable         (bool               b0)
	| stdResizable         (computedBool      cb0)
	| stdResizable         (Like              lb0)
	| stdResizable         (Convert           mb0)
	| stdResizable         (bool               b2, bool               b1)
	| stdResizable         (bool               b3, computedBool      cb1)
	| stdResizable         (bool               b4, Like              lb1)
	| stdResizable         (bool               b5, Convert           mb1)
	| stdResizable         (computedBool      cb3, bool               b6)
	| stdResizable         (computedBool      cb4, computedBool      cb2)
	| stdResizable         (computedBool      cb5, Like              lb2)
	| stdResizable         (computedBool      cb6, Convert           mb2)
	| stdResizable         (Like              lb4, bool               b7)
	| stdResizable         (Like              lb5, computedBool      cb7)
	| stdResizable         (Like              lb6, Like              lb3)
	| stdResizable         (Like              lb7, Convert           mb3)
	| stdResizable         (Convert           mb5, bool               b8)
	| stdResizable         (Convert           mb6, computedBool      cb8)
	| stdResizable         (Convert           mb7, Like              lb8)
	| stdResizable         (Convert           mb8, Convert           mb4)
	| stdShadow            (bool               b0)
	| stdShadow            (computedBool      cb0)
	| stdShadow            (Like              lb0)
	| stdShadow            (Convert           mb0)
	| stdShadowColor       (Color              c0)
	| stdShadowColor       (str               sc0)
	| stdShadowColor       (computedColor     cc0)
	| stdShadowColor       (Like              lc0)
	| stdShadowColor       (Convert           mc0)
	| stdShadowLeft        (num                r0)
	| stdShadowLeft        (computedNum       cr0)
	| stdShadowLeft        (Like              lr0)
	| stdShadowLeft        (Convert           mr0)
	| stdShadowTop         (num                r0)
	| stdShadowTop         (computedNum       cr0)
	| stdShadowTop         (Like              lr0)
	| stdShadowTop         (Convert           mr0)
	| stdShapeClosed       (bool               b0)
	| stdShapeClosed       (computedBool      cb0)
	| stdShapeClosed       (Like              lb0)
	| stdShapeClosed       (Convert           mb0)
	| stdShapeConnected    (bool               b0)
	| stdShapeConnected    (computedBool      cb0)
	| stdShapeConnected    (Like              lb0)
	| stdShapeConnected    (Convert           mb0)
	| stdShapeCurved       (bool               b0)
	| stdShapeCurved       (computedBool      cb0)
	| stdShapeCurved       (Like              lb0)
	| stdShapeCurved       (Convert           mb0)
	| stdShrink            (num                r0)
	| stdShrink            (computedNum       cr0)
	| stdShrink            (Like              lr0)
	| stdShrink            (Convert           mr0)
	| stdShrink            (num                r2, num                r1)
	| stdShrink            (num                r3, computedNum       cr1)
	| stdShrink            (num                r4, Like              lr1)
	| stdShrink            (num                r5, Convert           mr1)
	| stdShrink            (computedNum       cr3, num                r6)
	| stdShrink            (computedNum       cr4, computedNum       cr2)
	| stdShrink            (computedNum       cr5, Like              lr2)
	| stdShrink            (computedNum       cr6, Convert           mr2)
	| stdShrink            (Like              lr4, num                r7)
	| stdShrink            (Like              lr5, computedNum       cr7)
	| stdShrink            (Like              lr6, Like              lr3)
	| stdShrink            (Like              lr7, Convert           mr3)
	| stdShrink            (Convert           mr5, num                r8)
	| stdShrink            (Convert           mr6, computedNum       cr8)
	| stdShrink            (Convert           mr7, Like              lr8)
	| stdShrink            (Convert           mr8, Convert           mr4)
	| stdSize              (num                r0)
	| stdSize              (computedNum       cr0)
	| stdSize              (Like              lr0)
	| stdSize              (Convert           mr0)
	| stdSize              (num                r2, num                r1)
	| stdSize              (num                r3, computedNum       cr1)
	| stdSize              (num                r4, Like              lr1)
	| stdSize              (num                r5, Convert           mr1)
	| stdSize              (computedNum       cr3, num                r6)
	| stdSize              (computedNum       cr4, computedNum       cr2)
	| stdSize              (computedNum       cr5, Like              lr2)
	| stdSize              (computedNum       cr6, Convert           mr2)
	| stdSize              (Like              lr4, num                r7)
	| stdSize              (Like              lr5, computedNum       cr7)
	| stdSize              (Like              lr6, Like              lr3)
	| stdSize              (Like              lr7, Convert           mr3)
	| stdSize              (Convert           mr5, num                r8)
	| stdSize              (Convert           mr6, computedNum       cr8)
	| stdSize              (Convert           mr7, Like              lr8)
	| stdSize              (Convert           mr8, Convert           mr4)
	| stdText              (str                s0)
	| stdText              (computedStr       cs0)
	| stdText              (Like              ls0)
	| stdText              (Convert           ms0)
	| stdTextAngle         (num                r0)
	| stdTextAngle         (computedNum       cr0)
	| stdTextAngle         (Like              lr0)
	| stdTextAngle         (Convert           mr0)
	| stdToAngle           (num                r0)
	| stdToAngle           (computedNum       cr0)
	| stdToAngle           (Like              lr0)
	| stdToAngle           (Convert           mr0)
	| stdToArrow           (Figure             f0)
	| stdToArrow           (computedFigure    cf0)
	| stdToArrow           (Like              lf0)
	| stdToArrow           (Convert           mf0)
	| stdValign            (num                r0)
	| stdValign            (computedNum       cr0)
	| stdValign            (Like              lr0)
	| stdValign            (Convert           mr0)
	| stdVcapGaps          (bool               b0)
	| stdVcapGaps          (computedBool      cb0)
	| stdVcapGaps          (Like              lb0)
	| stdVcapGaps          (Convert           mb0)
	| stdVcapGaps          (bool               b2, bool               b1)
	| stdVcapGaps          (bool               b3, computedBool      cb1)
	| stdVcapGaps          (bool               b4, Like              lb1)
	| stdVcapGaps          (bool               b5, Convert           mb1)
	| stdVcapGaps          (computedBool      cb3, bool               b6)
	| stdVcapGaps          (computedBool      cb4, computedBool      cb2)
	| stdVcapGaps          (computedBool      cb5, Like              lb2)
	| stdVcapGaps          (computedBool      cb6, Convert           mb2)
	| stdVcapGaps          (Like              lb4, bool               b7)
	| stdVcapGaps          (Like              lb5, computedBool      cb7)
	| stdVcapGaps          (Like              lb6, Like              lb3)
	| stdVcapGaps          (Like              lb7, Convert           mb3)
	| stdVcapGaps          (Convert           mb5, bool               b8)
	| stdVcapGaps          (Convert           mb6, computedBool      cb8)
	| stdVcapGaps          (Convert           mb7, Like              lb8)
	| stdVcapGaps          (Convert           mb8, Convert           mb4)
	| stdVconnect          (num                r0)
	| stdVconnect          (computedNum       cr0)
	| stdVconnect          (Like              lr0)
	| stdVconnect          (Convert           mr0)
	| stdVendGap           (bool               b0)
	| stdVendGap           (computedBool      cb0)
	| stdVendGap           (Like              lb0)
	| stdVendGap           (Convert           mb0)
	| stdVgap              (num                r0)
	| stdVgap              (computedNum       cr0)
	| stdVgap              (Like              lr0)
	| stdVgap              (Convert           mr0)
	| stdVgrow             (num                r0)
	| stdVgrow             (computedNum       cr0)
	| stdVgrow             (Like              lr0)
	| stdVgrow             (Convert           mr0)
	| stdVpos              (num                r0)
	| stdVpos              (computedNum       cr0)
	| stdVpos              (Like              lr0)
	| stdVpos              (Convert           mr0)
	| stdVresizable        (bool               b0)
	| stdVresizable        (computedBool      cb0)
	| stdVresizable        (Like              lb0)
	| stdVresizable        (Convert           mb0)
	| stdVshrink           (num                r0)
	| stdVshrink           (computedNum       cr0)
	| stdVshrink           (Like              lr0)
	| stdVshrink           (Convert           mr0)
	| stdVstartGap         (bool               b0)
	| stdVstartGap         (computedBool      cb0)
	| stdVstartGap         (Like              lb0)
	| stdVstartGap         (Convert           mb0)
	| stdWidth             (num                r0)
	| stdWidth             (computedNum       cr0)
	| stdWidth             (Like              lr0)
	| stdWidth             (Convert           mr0)
;   

public FProperty child(FProperty props ...){
	throw "child is currently out of order (broken)";
	return _child(props);
}

public FProperty grandChild(FProperty props ...){
	return _child([_child(props)]);
}


data Edge =			 							// edge between between two elements in complex shapes like tree or graph
     _edge(str from, str to, FProperties prop)
 //  | _edge(str from, str to, Figure toArrow, FProperties prop)
 //  | _edge(str from, str to, Figure toArrow, Figure fromArrow, FProperties prop)
   ;
   
public alias Edges = list[Edge];
   
public Edge edge(str from, str to, FProperty props ...){
  return _edge(from, to, props);
}

public Edge edge(str from, str to, Figure toArrowP, FProperty props ...){
  return _edge(from, to, [toArrow(toArrowP)] + props);
}
public Edge edge(str from, str to, Figure fromArrowP, Figure toArrowP,  FProperty props ...){
  return _edge(from, to, [toArrow(toArrowP),fromArrow(fromArrowP)] + props);
}



/*
 * Figure: a visual element, the principal visualization datatype
 */
 
public alias Figures = list[Figure];
 
public data Figure = 
/* atomic primitives */

     _text(str s, FProperties props)		    // text label
   | _text(computedStr sv, FProperties props)
   
   												// file outline
   | _outline(list[LineDecoration] lineInfo, int maxLine, FProperties props)
   
   
/* primitives/containers */

   | _box(FProperties props)			          // rectangular box
   | _box(Figure inner, FProperties props)       // rectangular box with inner element
   
   | _ellipse(FProperties props)                 // ellipse with inner element
   | _ellipse(Figure inner, FProperties props)   // ellipse with inner element
   
   | _wedge(FProperties props)			      	// wedge
   | _wedge(Figure inner, FProperties props)     // wedge with inner element
   
   | _space(FProperties props)			      	// invisible box (used for spacing)
   | _space(Figure inner, FProperties props)     // invisible box with visible inner element   
   | _leftScreen(Figure inner, FProperties props) // a screen on which things can be projected   
   | _rightScreen(Figure inner, FProperties props) // a screen on which things can be projected   
   | _topScreen(Figure inner, FProperties props) // a screen on which things can be projected   
   | _bottomScreen(Figure inner, FProperties props) // a screen on which things can be projected   

   
   | _leftAxis(str name,Figure inner, FProperties props)
   | _rightAxis(str name,Figure inner, FProperties props) 
   | _topAxis(str name,Figure inner, FProperties props)
   | _bottomAxis(str name,Figure inner, FProperties props)

   
   
   | _vscreen(FProperties props)                  // a screen on which things can be projected      
   | _vscreen(Figure inner, FProperties props)
   
   | _projection(Figure fig, str id, Figure project,FProperties props)   // project from the location of fig to the screen id 
   
   | _scrollable(Figure fig, FProperties props)
        
   | _timer(int delay,int () callBack, Figure inner,FProperties props)

/* composition */
   
   | _use(Figure elem)                           // use another elem
   | _use(Figure elem, FProperties props)
   
   | _place(Figure onTop, str at, Figure onBottom, FProperties props)
                       
                   
   | _hvcat(Figures figs, FProperties props) // horizontal and vertical concatenation
   | _hstack(Figures figs, FProperties props)
   | _vstack(Figures figs, FProperties props)
                   
   | _overlay(Figures figs, FProperties props)// overlay (stacked) composition
   

   | _grid(list[list[Figure]] figMatrix, FProperties props)
   
  								                // composition by 2D packing
   | _pack(Figures figs, FProperties props)
   
  												 // composition of nodes and edges as graph
   | _graph(Figures nodes, Edges edges, FProperties props)
   
                							    // composition of nodes and edges as tree
   | _tree(Figures nodes, Edges edges, FProperties props)
   
   | _treemap(Figures nodes, Edges edges, FProperties props)
   
   | _nominalKey(list[value] possibilities, Figure (list[value]) whole,FProperties props)
   
   | _intervalKey(value (real part) interpolate, Figure (value low, value high) explain,FProperties props)
   
/* transformation */

   | _rotate(num angle, Figure fig, FProperties props)			    // Rotate element around its anchor point
   | _scale(num perc, Figure fig, FProperties props)	   		    // Scale element (same for h and v)
   | _scale(num xperc, num yperc, Figure fig, FProperties props)	// Scale element (different for h and v)

/* interaction */

   | _computeFigure(Figure () computeFig, FProperties props)
   | _button(str label, void () vcallback, FProperties props)
   | _textfield(str text, void (str) scallback, FProperties props)
   | _textfield(str text, void (str) scallback, bool (str) validate, FProperties props)
   | _combo(str text, list[str] choices, void (str) scallback, FProperties props)
   | _combo(str text, list[str] choices, void (str) scallback, bool (str) validate, FProperties props)
   | _choice(list[str] choices, void(str s) ccallback, FProperties props)
   | _checkbox(str text, bool checked, void(bool) vbcallback, FProperties props)
   ;

public Figure text(str s, FProperty props ...){
  return _text(s, props);
}

public Figure text(computedStr sv, FProperty props ...){
  return _text(sv, props);
}

public Figure outline (list[LineDecoration] lineInfo, int maxLine, FProperty props ...){
  return _outline(lineInfo, maxLine, props);
}

public Figure box(FProperty props ...){
  return _box(props);
}

public Figure box(Figure fig, FProperty props ...){
  return _box(fig, props);
}

public Figure ellipse(FProperty props ...){
  return _ellipse(props);
}

public Figure ellipse(Figure fig, FProperty props ...){
  return _ellipse(fig, props);
}

public Figure wedge(FProperty props ...){
  return _wedge(props);
}

public Figure wedge(Figure fig, FProperty props ...){
  return _wedge(fig, props);
}  

public Figure space(FProperty props ...){
  return _space(props);
}

public Figure space(Figure fig, FProperty props ...){
  return _space(fig, props);
}

public Figure haxis(Figure fig, FProperty props ...){
  return _haxis(fig, props);
}

public Figure vaxis(Figure fig, FProperty props ...){
  return _vaxis(fig, props);
}

public Figure leftScreen(str i,Figure fig, FProperty props ...){
  return _leftScreen(fig,[id(i)] + props);
}

public Figure rightScreen(str i,Figure fig, FProperty props ...){
  return _rightScreen(fig, [id(i)] + props);
}

public Figure topScreen(str i,Figure fig, FProperty props ...){
  return _topScreen(fig, [id(i)] +props);
}

public Figure bottomScreen(str i,Figure fig, FProperty props ...){
  return _bottomScreen(fig, [id(i)] +props);
}


public Figure leftAxis(str name,str i,Figure fig, FProperty props ...){
  return _leftAxis(name,fig, [id(i)] + props);
}

public Figure rightAxis(str name,str i,Figure fig, FProperty props ...){
  return _rightAxis(name,fig, [id(i)] + props);
}

public Figure topAxis(str name,str i,Figure fig, FProperty props ...){
  return _topAxis(name,fig, [id(i)] + props);
}

public Figure bottomAxis(str name,str i,Figure fig, FProperty props ...){
  return _bottomAxis(name,fig, [id(i)] + props);
}

public Figure leftAxis(str i,Figure fig, FProperty props ...){
  return _leftAxis("",fig, [id(i)] + props);
}

public Figure rightAxis(str i,Figure fig, FProperty props ...){
  return _rightAxis("",fig, [id(i)] + props);
}

public Figure topAxis(str i,Figure fig, FProperty props ...){
  return _topAxis("",fig, [id(i)] + props);
}

public Figure bottomAxis(str i,Figure fig, FProperty props ...){
  return _bottomAxis("",fig, [id(i)] + props);
}

public Figure projection(Figure fig, str id, Figure project,FProperty props ...){
  return _projection(fig,id,project,props);
}

public Figure scrollable(Figure fig, FProperty props...){
	return _scrollable(fig,props);
}

public Figure place(Figure fig, str at, Figure base, FProperty props ...){
  return _place(fig, at, base, props);
}

public Figure use(Figure fig, FProperty props ...){
  return _use(fig, props);
}

public Figure hcat(Figures figs, FProperty props ...){
  return _grid([[figs]],props);
}

public Figure vcat(Figures figs, FProperty props ...){
  newList = for(f <- figs){
  	append [f];
  };
  return _grid(newList, props);
}

public Figure hvcat(Figures figs, FProperty props ...){
  return _hvcat(figs, props);
}

public Figure hstack(Figures figs, FProperty props ...){
  return _hstack(figs, props);
}

public Figure vstack(Figures figs, FProperty props ...){
  return _vstack(figs, props);
}

public Figure overlay(Figures figs, FProperty props ...){
  return _overlay(figs, props);
}


public Figure grid(list[list[Figure]] figs, FProperty props ...){
  return _grid(figs, props);
}

public Figure pack(Figures figs, FProperty props ...){
  return _pack(figs, props);
}

public Figure graph(Figures nodes, Edges edges, FProperty props...){
  return _graph(nodes, edges, [stdResizable(false)] + props);
}

public Figure tree(Figures nodes, Edges edges, FProperty props...){
  return _tree(nodes, edges, [stdResizable(false)] + props);
}

public Figure treemap(Figures nodes, Edges edges, FProperty props...){
  return _treemap(nodes, edges, props);
}

public Figure rotate(num angle, Figure fig, FProperty props...){
  return _rotate(angle, fig, props);
}

public Figure scale(num perc, Figure fig, FProperty props...){
  return _scale(perc, fig, props);
}

public Figure scale(num xperc, num yperc, Figure fig, FProperty props...){
  return _scale(xperc, yperc, fig, props);
}

public Figure boolControl(str name, Figure figOn, Figure figOff, FProperty props...){
  return _boolControl(name, figOn, figOff, props);
}

public Figure controlOn(str name, Figure fig, FProperty props...){
  return _controlOn(name, fig, props);
}

public Figure controlOff(str name, Figure fig, FProperty props...){
  return _controlOff(name, fig, props);
}

public Figure strControl(str name, str initial, FProperty props...){
  return _strControl(name, initial, props);
}

public Figure intControl(str name, int initial, FProperty props...){
  return _intControl(name, initial, props);
}

public Figure colorControl(str name, int initial, FProperty props...){
  return _colorControl(name, initial, props);
}

public Figure colorControl(str name, str initial, FProperty props...){
  return _colorControl(name, initial, props);
}

public Figure computeFigure(Figure () computeFig, FProperty props...){
 	return _computeFigure(computeFig, props);
}
  
public Figure button(str label, void () callback, FProperty props...){
 	return _button(label, callback, props);
}
 
public Figure textfield(str text, void (str) callback, FProperty props...){
 	return _textfield(text, callback, props);
}
 
public Figure textfield(str text,  void (str) callback, bool (str) validate, FProperty props...){
 	return _textfield(text, callback, validate, props);
}

public Figure combo(str text, list[str] choices, void (str) callback, FProperty props...){
 	return _combo(text, choices, callback, props);
}
 
public Figure combo(str text, list[str] choices, void (str) callback, bool (str) validate, FProperty props...){
 	return _combo(text, choices, callback, validate, props);
}
public Figure choice(list[str] choices, void(str s) ccallback, FProperty props...){
   return _choice(choices, ccallback, props);
}

public Figure checkbox(str text, bool checked, void(bool) vcallback, FProperty props...){
   return _checkbox(text, checked, vcallback, props);
}  
  
public Figure checkbox(str text, void(bool) vcallback, FProperty props...){
   return _checkbox(text, false, vcallback, props);
}  

public Figure normalize(Figure f){
	f = visit(f){
		case Figure f : {
			if([_*,project(y,z),_*] := f.props){
				projects = [];
				otherProps = [];
				for(elem <- f.props){
					if(project(_,_) := elem){
						projects+=[elem];
					} else {
						otherProps+=[elem];
					}
				}
				insert (f[props = otherProps] | projection(it,i,p) | project(p,i) <- projects);
			} else { fail ; } 
		}
	}
	f = visit(f){
		case Figure f : {
			if([_*,timer(y,z),_*] := f.props){
				projects = [];
				otherProps = [];
				for(elem <- f.props){
					if(timer(_,_) := elem){
						projects+=[elem];
					} else {
						otherProps+=[elem];
					}
				}
				insert (f[props = otherProps] | _timer(p,i,it,[]) | timer(p,i) <- projects);
			} else { fail ; } 
		}
	}
	return f;
}


public Figure palleteKey (str name, str key,FProperty props...){
 return  _nominalKey(p12,Figure (list[value] orig) { 
 		Figure inner;
 		if(size(orig) == 0) inner = space(); 
 		else inner = grid([[box(fillColor(p12[i])),text(toString(orig[i]),left())] | i <- [0..size(orig)-1]],hgrow(1.2),vgrow(1.1));
 		return vcat([
 		text(name,fontSize(13)),
 		box(
 			inner
 		)
 		],props);},[id(key)] ); 
}

public Figure hPalleteKey (str name, str key,FProperty props...){
 return  _nominalKey(p12,Figure (list[value] orig) { 
 		Figure inner;
 		if(size(orig) == 0) inner = space(); 
 		else inner = hcat([vcat([box(fillColor(p12[i])),text(toString(orig[i]))],hgrow(1.05)) | i <- [0..size(orig)-1]],hgrow(1.05));
 		return box(hcat([
 		text(name,fontSize(13)),
 		inner
 		], hgrow(1.1)),[hgrow(1.05)] + props);},[id(key)] ); 
}

public Figure title(str name, Figure inner,FProperty props...){
	return vcat([text(name,fontSize(17)), box(inner,grow(1.1))],props);
}

public Color rrgba(real r, real g, real b, real a){
	return rgb(toInt(r * 255.0),toInt(g * 255.0),toInt(b * 255.0),a);
}

public Color randomColor(){
	return rrgba(arbReal(),arbReal(),arbReal(),1.0);
}

public Color randomColorAlpha(){
	return rrgba(arbReal(),arbReal(),arbReal(),arbReal());
}

public Figure point(FProperty props...){
	return space([resizable(false), size(0)] + props);
}


public Figure colorIntervalKey(str name, str key, Color lowc, Color highc, FProperty props...){
	return  _intervalKey( value (real part) { return interpolateColor(lowc,highc,part); },
				Figure (value low,value high) {
 		Figure inner = hcat([vcat([box(fillColor(lowc)),text(toString(low))]),
 							vcat([box(fillColor(highc)),text(toString(high))])]
 						);
 		return box(hcat([
 		text(name,fontSize(13)),
 		inner
 		], hgrow(1.1)),[hgrow(1.05)] + props);},[id(key)] ); 
 }

alias KeyHandler = void (KeySym,map[KeyModifier,bool]);

public Figure triangle(int side,FProperty props...){
  return overlay([point(left(),bottom()),point(top()), point(right(),bottom())], 
  	[shapeConnected(true), shapeClosed(true),  size(side,sqrt(3.0/4.0) * toReal(side)),
  	resizable(false)] + props);
}

public Figure headNormal(FProperty props...) {
  list[tuple[real x, real y]]  tup = [<0.,1.>, <0.5, 0.>, <1., 1.>];
		  return space(overlay([
		point(halign(t.x), valign(t.y))|tuple[real x, real y] t <-tup], 
		  [ shapeConnected(true), shapeClosed(true)]+props), stdSize(10));
}

public Figure headInv(FProperty props...) {
  list[tuple[real x, real y]]  tup = [<0.,0.>, <0.5, 1.>, <1., 0.>];
   return space(overlay([
		point(halign(t.x), valign(t.y))|tuple[real x, real y] t <-tup], 
		  [ shapeConnected(true), shapeClosed(true)]+props),  stdSize(10));
		
}

public  Figure headDot(FProperty props...) {
	return space(ellipse(props),  stdSize(10));
}

public  Figure headBox(FProperty props...) {
	return space(box(props),  stdSize(10));
}

public Figure headDiamond(FProperty props...) {
  list[tuple[real x, real y]]  tup = [<0.,.5>, <.5, 1.>, <1., .5>, <.5, 0.>];
		  return space(overlay([
		point(halign(t.x), valign(t.y))|tuple[real x, real y] t <-tup], 
		  [ shapeConnected(true), shapeClosed(true)]+props),  stdWidth(10), stdHeight(15));
}

public  Figure headBox(FProperty props...) {
	return space(ellipse(props),  stdSize(10));
}

public  Figure shapeEllipse(str key, FProperty props...) {
	return space(ellipse(props), stdId(key), stdWidth(40), stdHeight(30));
}

public  Figure shapeDoubleEllipse(str key, FProperty props...) {
	return space(ellipse(ellipse(props+stdShrink(0.8)), props),  stdId(key), stdWidth(40), stdHeight(30));
}

public  Figure shapeBox(str key, FProperty props...) {
	return space(box(fig, props),stdId(key), stdWidth(40), stdHeight(30));
}

public Figure shapeDiamond(str key, FProperty props...) {
  list[tuple[real x, real y]]  tup = [<0.,.5>, <.5, 1.>, <1., .5>, <.5, 0.>];
		  return space(overlay([
		point(halign(t.x), valign(t.y))|tuple[real x, real y] t <-tup], 
		  props+shapeConnected(true)+shapeClosed(true)),  stdId(key), stdWidth(60), stdHeight(40));
}

public Figure shapeParallelogram(str key, FProperty props...) {
  real delta = 0.30;
  list[tuple[real x, real y]]  tup = [<delta, 0.>, <1., 0.>, <1.-delta, 1.>, <0., 1.>];
		  return space(overlay([
		point(halign(t.x), valign(t.y))|tuple[real x, real y] t <-tup], 
		  props+shapeConnected(true)+shapeClosed(true)),  stdId(key), stdWidth(80), stdHeight(50));
}

public  Figure shapeEllipse(Figure fig, str key, FProperty props...) {
	return overlay([shapeEllipse(key, props), fig], stdId(key));
}

public  Figure shapeDoubleEllipse(Figure fig, str key, FProperty props...) {
	return overlay([shapeDoubleEllipse(key, props), fig], stdId(key));
}

public  Figure shapeBox(Figure fig, str key, FProperty props...) {
	return overlay([shapeBox(key, props), fig], stdId(key));
}

public  Figure shapeDiamond(Figure fig, str key, FProperty props...) {
	return overlay([shapeDiamond(key, props), fig], stdId(key));
}

public  Figure shapeParallelogram(Figure fig, str key, FProperty props...) {
	return overlay([shapeParallelogram(key, props), fig], stdId(key));
}

