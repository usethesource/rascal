module Processing

@doc{height of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing}
public int java height();

@doc{width of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing}
public int java width();

@doc{X coordinate of mouse}
@javaClass{org.meta_environment.rascal.std.Processing}
public int java mouseX();

@doc{Y coordinate of mouse}
@javaClass{org.meta_environment.rascal.std.Processing}
public int java mouseY();

@doc{size of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing}
public void java size(int x, int y);

@doc{define background of current sketch}
@javaClass{org.meta_environment.rascal.std.Processing}
public void java background(int x);

@doc{define current fill mode}
@javaClass{org.meta_environment.rascal.std.Processing}
public void java fill(int grey);

@doc{define current fill mode}
@javaClass{org.meta_environment.rascal.std.Processing}
public void java fill(int grey, int alpha);

@doc{define current fill mode}
@javaClass{org.meta_environment.rascal.std.Processing}
public void java fill(int red, int green, int blue);

@doc{disable drawing of strokes (figure outline)}
@javaClass{org.meta_environment.rascal.std.Processing}
public void java noStroke();

@doc{draw a line}
@javaClass{org.meta_environment.rascal.std.Processing}
public void java line(int bx, int by, int ex, int ey);

@doc{draw a rectangle}
@javaClass{org.meta_environment.rascal.std.Processing}
public void java rect(int x, int y, int w, int h);

@doc{show the sketch}
@javaClass{org.meta_environment.rascal.std.Processing}
public void java show();