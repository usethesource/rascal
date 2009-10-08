module Processing::Core

public int LEFT = 37;
public int CENTER = 3;
public int RIGHT = 39;

public alias PFont = int;

@doc{height of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public int java height();

@doc{width of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public int java width();

@doc{size of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java size(int x, int y);

/*
 * Color
 */
 
 /*
  * Color setting
  */

//--- background ----

@doc{define background of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java background(int rgb);

@doc{define background of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java background(int rgb, real alpha);


@doc{define background of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java background(real grey);

@doc{define background of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java background(real grey, real alpha);

@doc{define background of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java background(int red, int green, int blue);

@doc{define background of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java background(real red, real green, real blue);

@doc{define background of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java background(int red, int green, int blue, real alpha);

@doc{define background of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java background(real red, real green, real blue, real alpha);

// ---- colorMode ----

//---- fill -----

@doc{define current fill mode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java fill(int rgb);

@doc{define current fill mode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java fill(int rgb, real alpha);

@doc{define current fill mode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java fill(real grey);

@doc{define current fill mode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java fill(real grey, real alpha);

@doc{define current fill mode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java fill(real red, real green, real blue);

@doc{define current fill mode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java fill(real red, real green, real blue, real alpha);

// ---- noFill ----

@doc{noFill}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java noFill();

// ---- stroke ----

@doc{define current stroke mode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java stroke(int rgb);

@doc{define current stroke mode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java stroke(int rgb, real alpha);

@doc{define current stroke mode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java stroke(real grey);

@doc{define current stroke mode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java stroke(real grey, real alpha);

@doc{define current stroke mode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java stroke(real red, real green, real blue);

@doc{define current stroke mode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java stroke(real red, real green, real blue, real alpha);

// ---- noStroke ----

@doc{disable drawing of strokes (figure outline)}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java noStroke();

/*
 * Color Creating & Reading
 */

/*
 * Shape primitives
 */

/*
 * 2D primitives
 */
 
// ---- arc ----

@doc{draw an arc}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java arc(int x, int y, int width, int height, int start, int stop);

@doc{draw an arc}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java arc(real x, real y, real width, real height, real start, real stop);

// ---- ellipse

@doc{draw an ellipse}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java ellipse(int x, int y, int width, int height);

@doc{draw an ellipse}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java ellipse(real x, real y, real width, real height);
 
// ---- line ----

@doc{draw a line}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java line(int x1, int y1, int x2, int y2);

@doc{draw a line}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java line(real x1, real y1, real x2, real y2);

@doc{draw a line}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java line(int x1, int y1, int z1, int x2, int y2, int z2);

@doc{draw a line}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java line(real x1, real y1, real z1, real x2, real y2, real z2);

// ---- Point ----
@doc{draw a point}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java point(int x, int y);

@doc{draw a point}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java point(real x, real y);

@doc{draw a point}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java point(int x, int y, int z);

@doc{draw a point}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java point(real x, real y, real z);

// ---- quad ----

@doc{quad}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java quad(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4);

@doc{draw a triangle}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java quad(real x1, real y1, real x2, real y2, real x3, real y3, real x4, real y4);


// ---- triangle ----

@doc{draw a triangle}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java triangle(int x1, int y1, int x2, int y2, int x3, int y3);

@doc{draw a triangle}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java triangle(real x1, real y1, real x2, real y2, real x3, real y3);

// ---- rectangle ----

@doc{draw a rectangle}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java rect(int x, int y, int w, int h);

@doc{draw a rectangle}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java rect(real x, real y, real w, real h);

	
/*
 * Curve primitives
 */

/* 
 * 3D primitives
 */
	
/*
 * Shape Attributes
 */
	

public int CORNER  = 0;   // rect: x, y, width, height; (x,y) is upperleft corner
public int CORNERS = 1;   // rect: left, top, right, bottom
//         CENTER  = 3;   // rect:  x, y, width, height; (x,y) is center
public int RADIUS  = 2;   // rect:  x, y, width/2, height/2; (x,y) is center.

@doc{switch interpretation of ellipse arguments}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java ellipseMode(int mode);

@doc{smoothing off}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java noSmooth();

@doc{switch interpretation of rect arguments}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java rectMode(int mode);

@doc{smoothing on}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java smooth();

@doc{strokeCap}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java strokeCap(int cap);

@doc{strokeJoin}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java strokeJoin(int jn);

@doc{strokeWeight}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java strokeWeight(real weight);

// ---- start/stop ----

/*
 * Identity returned for processing object that results from processing call.
 */
 
public alias Processing = node;

/*
 * Tags for all possible callback functions.
 */
 
public data CallBack =
       setup(void () setup)
     | draw(void () draw)
     | mouseClicked(void () mouseClicked)
     | mouseMoved(void () mouseMoved)
     | mousePressed(void () mousePressed)
     | mouseReleased(void () mouseReleased)
     ;

@doc{start Processing}
@reflect{Needs calling context when calling argument function}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public Processing java processing(value callbacks ...);

//@doc{stop a visualization}
//@javaClass{org.meta_environment.rascal.std.Processing.Core}
//public void java stop(Processing P);

@doc{noLoop, draw once}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java noLoop();

// text

@doc{text alignment}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java textAlign(int align);

@doc{text alignment}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java textAlign(int alignX, int alignY);

@doc{text ascent}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public real java textAscent();

@doc{text decent}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public real java textDescent();

@doc{text}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java text(str s);

@doc{text}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java text(str s, int x, int y);

@doc{text}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java text(str s, real x, real y);

@doc{text}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java text(str s, int x, int y, int z);

@doc{text}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java text(str s, real x, real y, real z);

@doc{text}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java text(str s, int x1, int y1, int x2, int y2);

@doc{text}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java text(str s, real x1, real y1, real x2, real y2);

// ---- more text ----

@doc{textSize}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java textSize(int size);

@doc{textSize}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java textSize(real size);

@doc{textWidth}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public int java textWidth(str s);

@doc{textMode}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java textMode(int mode);

// ---- fonts ----

@doc{createFont}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public int java createFont(str name, int size);

@doc{createFont}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public int java createFont(str name, real size);

@doc{createFont}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public PFont java createFont(str name, int size, bool smooth);

@doc{createFont}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public PFont java createFont(str name, real size, bool smooth);

@doc{createFont}
@reflect{may generate Rascal exception}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public void java textFont(PFont font);

/*
 * Input
 */
 
 /*
  * Mouse
  */
  
@doc{mouseButton}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public int java mouseButton();

@doc{mousePressed}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public bool java mousePressed();

@doc{X coordinate of mouse}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public int java mouseX();

@doc{Y coordinate of mouse}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public int java mouseY();

@doc{previous X coordinate of mouse}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public int java pmouseX();

@doc{previous Y coordinate of mouse}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public int java pmouseY();

// -- Triogonometry

@doc{Convert degrees to radians}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public real java radians(int angle);

@doc{Convert degrees to radians}
@javaClass{org.meta_environment.rascal.std.Processing.Core}
public real java radians(real angle);












