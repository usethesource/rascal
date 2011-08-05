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
public java Color gray(int gray);

@doc{Gray color (0-255) with transparency}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public java Color gray(int gray, real alpha);

@doc{Gray color as percentage (0.0-1.0)}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public java Color gray(real perc);

@doc{Gray color with transparency}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public java Color gray(real perc, real alpha);

@doc{Named color}
@reflect{Needs calling context when generating an exception}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public java Color color(str colorName);

@doc{Named color with transparency}
@reflect{Needs calling context when generating an exception}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public java Color color(str colorName, real alpha);

@doc{Sorted list of all color names}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public java list[str] colorNames();

@doc{RGB color}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public java Color rgb(int r, int g, int b);

@doc{RGB color with transparency}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public java Color rgb(int r, int g, int b, real alpha);

@doc{Interpolate two colors (in RGB space)}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public java Color interpolateColor(Color from, Color to, real percentage);

@doc{Create a list of interpolated colors}
@javaClass{org.rascalmpl.library.vis.FigureColorUtils}
public java list[Color] colorSteps(Color from, Color to, int steps);

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
public java list[str] fontNames();
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
	  mouseOver(Figure fig)
	 | std(FProperty property)
	 | timer                (int delay, int () cb)
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
	| innerAlign           (num                r0)
	| innerAlign           (computedNum       cr0)
	| innerAlign           (Like              lr0)
	| innerAlign           (Convert           mr0)
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
	| lineStyle            (str                s0)
	| lineStyle            (computedStr       cs0)
	| lineStyle            (Like              ls0)
	| lineStyle            (Convert           ms0)
	| lineWidth            (num                r0)
	| lineWidth            (computedNum       cr0)
	| lineWidth            (Like              lr0)
	| lineWidth            (Convert           mr0)
	| onClick              (void ()           h0)
	| onKey                (bool (KeySym, bool down, map[KeyModifier,bool]) kh0)
	| onMouseMove          (bool ()           h0)
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
   
   | _scrollable(bool hscroll,bool vscroll,Figure fig, FProperties props)
        
   | _timer(int delay,int () callBack, Figure inner,FProperties props)

/* composition */
   | _withDependantWidthHeight(bool widthMajor,Figure innder, FProperties props)
   | _mouseOver(Figure under, Figure over, FProperties props)
   | _fswitch(int () choice,Figures figs, FProperties props)
   | _overlap(Figure under, Figure over, FProperties props)
                       
                   
   | _hvcat(Figures figs, FProperties props) // horizontal and vertical concatenation
                   
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
   | _combo(list[str] choices, void (str) scallback, FProperties props)
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
	return _scrollable(true,true,fig,props);
}

public Figure hscrollable(Figure fig, FProperty props...){
	return _scrollable(true,false,fig,props);
}

public Figure vscrollable(Figure fig, FProperty props...){
	return _scrollable(false,true,fig,props);
}


public Figure place(Figure fig, str at, Figure base, FProperty props ...){
  return _place(fig, at, base, props);
}

public Figure use(Figure fig, FProperty props ...){
  return _use(fig, props);
}

public Figure overlap(Figure under,Figure over, FProperty props ...){
  return _overlap(under,over,props);
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
  return _withDependantWidthHeight(true,_hvcat(figs, props),[]);
}


public Figure fswitch(int () choice,Figures figs, FProperty props ...){
  return _fswitch(choice,figs, props);
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

public Figure combo(list[str] choices, void (str) callback, FProperty props...){
 	return _combo(choices, callback, props);
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
	f = visit(f){
		case Figure f : {
			if([_*,mouseOver(y),_*] := f.props){
				mouseOvers = [];
				otherProps = [];
				for(elem <- f.props){
					if(mouseOver(_) := elem){
						mouseOvers+=[elem];
					} else {
						otherProps+=[elem];
					}
				}
				insert (f[props = otherProps] | _mouseOver(it,p,[]) | mouseOver(p) <- mouseOvers);
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

public  Figure shapeEllipse(FProperty props...) {
	return space(ellipse(props), props+stdWidth(40)+stdHeight(30));
}

public  Figure shapeDoubleEllipse(FProperty props...) {
	return space(ellipse(ellipse(props+stdShrink(0.8)), props),  props+stdWidth(40)+stdHeight(30));
}

public  Figure shapeBox(FProperty props...) {
	return space(box(props),props+stdWidth(40)+stdHeight(30));
}

public Figure shapeDiamond(FProperty props...) {
  list[tuple[real x, real y]]  tup = [<0.,.5>, <.5, 1.>, <1., .5>, <.5, 0.>];
		  return space(overlay([
		point(halign(t.x), valign(t.y))|tuple[real x, real y] t <-tup], 
		  props+shapeConnected(true)+shapeClosed(true)),  props+stdWidth(60)+stdHeight(40));
}

public Figure shapeParallelogram(FProperty props...) {
  real delta = 0.30;
  list[tuple[real x, real y]]  tup = [<delta, 0.>, <1., 0.>, <1.-delta, 1.>, <0., 1.>];
		  return space(overlay([
		point(halign(t.x), valign(t.y))|tuple[real x, real y] t <-tup], 
		  props+shapeConnected(true)+shapeClosed(true)),  props+stdWidth(80)+stdHeight(50));
}

public  Figure shapeEllipse(Figure fig, FProperty props...) {
	return overlay([shapeEllipse(props), fig], shapeClosed(true)+props);
}

public  Figure shapeDoubleEllipse(Figure fig,  FProperty props...) {
	return overlay([shapeDoubleEllipse(props), fig], shapeClosed(true)+props);
}

public  Figure shapeBox(Figure fig, FProperty props...) {
	return overlay([shapeBox(props), fig], shapeClosed(true)+props);
}

public  Figure shapeDiamond(Figure fig, FProperty props...) {
	return overlay([shapeDiamond(props), fig], shapeClosed(true)+props);
}

public  Figure shapeParallelogram(Figure fig, FProperty props...) {
	return overlay([shapeParallelogram(props), fig], shapeClosed(true)+props);
}

public Figure ifFig(bool () cond, Figure onTrue, FProperty props...){
	return fswitch(int () { return cond() ? 1 : 0;}, [ space(), onTrue], props);
}

