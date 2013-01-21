@license{
  Copyright (c) 2009-2013 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
module vis::FigureSWTApplet

data DrawCmd = 
       rect(num left, num top, num width, num height)
     | ellipse(num left, num top, num width, num height)
     | line(num fromX, num fromY, num toX, num toY)
     | fill(int color)
     | stroke(int color)
     | strokeWeight(num weight)
     | text(str title, num left, num top)
     | textColor(int color)
     | textFont(str font, num size)
     | textSize(num size)
     ;
     
     
