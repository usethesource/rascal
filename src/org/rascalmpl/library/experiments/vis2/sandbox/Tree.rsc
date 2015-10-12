@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Bert Lisser - Bert.Lisser@cwi.nl (CWI)}
@contributor{Paul Klint - Paul.Klint@cwi.nl - CWI}
module experiments::vis2::sandbox::Tree
import Prelude;
import experiments::vis2::sandbox::FigureServer;
import experiments::vis2::sandbox::Figure;


data TreeNode  = treenode(str id, int width, int height, 
    list[TreeNode] branches, int x=0, int y=0,
    EDGE left=<0,[]>, EDGE right= <0,[]>);


list[int] leftOffset =  [];
list[int] rightOffset = [];

int MIN = 0;

map[str, TreeNode] m = ();

str rootId = "";


alias EDGE = tuple[int yPosition, list[int] offset]; 

data FigTree = br(Figure root, list[FigTree] figs)|lf(Figure root);

TreeNode convertFigure(Figure f, map[str, tuple[int, int]] m, int scaleX = 1, int scaleY = 5) {
     if (tree(Figure root, Figures figs):=f) {
           root.width = m[root.id][0];
           root.height = m[root.id][1];
       // println("Convert: <root.width>");
       return treenode(f.root.id, root.width/scaleX+1, root.height/scaleY+1,
       [convertFigure(b, m)|Figure b<-figs]);
       }
       f.width = m[f.id][0];
       f.height = m[f.id][1];
    return treenode(f.id, f.width/scaleX+1, f.height/scaleY+1, []);
    }
    
str makeMap(TreeNode t) {
   innermost visit(t) {
       case u:treenode(_, _,_,_): m+=(u.id:u);
       }
   return t.id;
   }
   
bool isDisjunct(TreeNode n1, TreeNode n2) {
     bool r =  (n1.y+n1.height<n2.y ||  n2.y+n2.height < n1.y)
                 || (n1.x + (n1.width+n2.width)/2 < n2.x || n2.x + (n1.width+n2.width)/2 < n1.x); 
              
     if (!r)  {
        // println([n1.x+d |d<-n1.right.offset]);
        // println([n2.x+d |d<-n2.left.offset]);
        println("Error: <<n1.id, n1.x, n1.y, n1.width, n1.height>> <<n2.id, n2.x, n2.y, n2.width, n2.height>>");    
        }
      //else
      //  println("Continue: <<n1.id, n1.x, n1.y, n1.width, n1.height>> <<n2.id, n2.x, n2.y, n2.width, n2.height>>");   
     return r;
     } 
     
 public bool isDisjunct() {  
    bool disjunct = true;
    // println("isDisjunct: <size(m)>");
    list[TreeNode] leaves = [m[d]|d<-m, isEmpty(m[d].branches)];
    // println("isDisjunct: <size(m)> <size(leaves)>");
    for (int i<-[0..size(leaves)]) {
       for (int j<-[0..i])
             disjunct = disjunct && isDisjunct(leaves[j], leaves[i]);
        }
    return disjunct;
    }     

list[Vertex] treeLayout(Figure f, map[str, tuple[int, int]] y, 
    int scaleX, int scaleY, int height, bool cityblock, int xSeparation = 5, int ySeparation = 10) {
    // println(y);
    TreeNode z = convertFigure(f, y, scaleX=scaleX, scaleY=scaleY);
    // println("Help0 <scaleX>");
    TreeNode r = layoutTree(z, height, scaleX=scaleX, scaleY=scaleY
    , xSeparation = xSeparation, ySeparation = ySeparation, orientation = f.orientation);  
    m = ();
    MIN = 0;
    rootId = makeMap(r); 
     if (!isDisjunct()) {
		  println("WRONG Tree");
		  }
    list[Vertex] q = display(r, [], cityblock);
    // println(q);
    return q;
    }
    

   
tuple[int, int] orient(Orientation orientation, int x, int y, int w, int h, int maxW, int maxH) {
    switch (orientation) {
        case topDown(): return <x-w/2, y-h/2>;
        case downTop(): return <(x-w/2), maxH-(y+h/2)>;
        case leftRight():return <y-w/2, x-h/2>;
        case rightLeft():return <maxW-(y+w/2), (x-h/2)>;
        }
    return <0, 0>;
    }
  
tuple[int, int] computeDim(Figures fs, map[str, tuple[int, int]] q) { 
    int maxWidth = 0;
    int maxHeight = 0;
    for (Figure f<-fs) {
        int w = m[f.id].x+q[f.id][0]/2;
        if (w >maxWidth) maxWidth = w;
        int h = m[f.id].y+q[f.id][1]/2;
        if (h >maxHeight) maxHeight = h;
        }
    return <maxWidth, maxHeight>;
    }

Figures treeUpdate(Figures fs,  map[str, tuple[int, int]] q, Orientation orientation,
    int maxWidth, int maxHeight) {  
    Figures r = []; 
    for (Figure f<-fs) {
       f.width = q[f.id][0];
       f.height = q[f.id][1];
       // println("treeupdate: <f.width>");
       // f.width=  f.width/5; f.height = 0;
       // f.at = <m[f.id].x-f.width/2, m[f.id].y-f.height/2>;
       f.at = orient(orientation, m[f.id].x, m[f.id].y,f.width, f.height, maxWidth, maxHeight);
       r+=[f];
       }
    return r;
    }
    
list[Vertex] vertexUpdate(list[Vertex] v, Orientation orientation
    , int maxWidth, int maxHeight) { 
    return [vertexUpdate(x, orientation, maxWidth, maxHeight)|x<-v];
    } 
    
Vertex vertexUpdate(Vertex v, Orientation orientation,int maxWidth, int maxHeight) {
   switch(v) {
        case move(int x, int y): {
             tuple[int, int] r = 
               orient(orientation, x, y, 0, 0, maxWidth, maxHeight);
               return move(r[0], r[1]);
          }
        case line(int x, int y): {
             tuple[int, int] r = 
               orient(orientation, x, y, 0, 0, maxWidth, maxHeight);
               return line(r[0], r[1]);
          }
       }
    }

public void main () {
   Figure b() = tree( box(size=<50, 50>, fillColor = "none", lineWidth = 1)
     ,[box(size=<60, 150>, lineWidth = 1, fillColor="none")
     // , circle(r=50, fillColor = "none", lineWidth = 1)
     , box(size=<60, 60>, lineWidth = 1, fillColor="none")
      ], orientation = downTop());
   // writeFile(|file:///ufs/bertl/html/u.html|, toHtmlString(b()));
   render(b());
   }
   
// blz 190 


//public void main() {
//    TreeNode t = treenode("a", 5, 5, [treenode("b", 4, 10, [treenode("c", 6, 6, []), treenode("d", 6, 6, [])]), treenode("e", 4, 4, [
//      treenode("f", 8,8, [])
//      , treenode("g", 8,8, [])
//      , treenode("h", 5,5, [treenode("i", 4, 4, [])])
//    ])]);
//    TreeNode r = layoutTree(t, 40);
//    // printT(r);
//    print(display(r, []));
//    render(dtree(r));
//    // writeFile(|file:///ufs/bertl/html/u.html|, toHtmlString(dtree(r)));
//    }

TreeNode mirror(TreeNode t) {
    // println("Mirror");
    return visit(t) {
       case treenode(str id,int width, int height, list[TreeNode] branches)=> treenode(id, height, width, branches)
       }
    }
    
Figure dtree(TreeNode t) = shape(display(t, [])
  // , scaleX=<<-40, 40>, <0,400>>, scaleY=<<0, 40>, <0,400>>
  , size=<400, 400>, yReverse= false
   );
    
list[Vertex] display(TreeNode t, list[Vertex] v, bool cityblock) {
    if (cityblock)
      // for (b<-t.branches) v= display(b, v+[move(t.x, t.y),line(t.x, b.y), line(b.x, b.y)], cityblock);
      for (b<-t.branches) v= display(b, v+[move(t.x, t.y),line(b.x, t.y), line(b.x, b.y)], cityblock);
    else
      for (b<-t.branches) v= display(b, v+[move(t.x, t.y),line(b.x, b.y)], cityblock);
    return v;
    }
    
TreeNode absoluteX(TreeNode t, int x, int scaleX, int scaleY) {
    // println("NODE1: <t.width> <x> <t.x>  <scaleX>");
    t.x += x;
    if (t.x-t.width<MIN) MIN = t.x-t.width;
    // println("NODE2: <t.width> <t.x>");
    t.branches = [absoluteX(b, t.x, scaleX, scaleY)|b<-t.branches];
    t.x= (t.x-MIN)*scaleX;
    t.y = t.y*scaleY;
    t.width = (t.width-1)*scaleX;
    t.height = t.height*scaleY;
    return t;
    }
    
TreeNode layoutTree(TreeNode t, int height, int scaleX=1, int scaleY = 5,
      int ySeparation = 10, int xSeparation = 5, Orientation orientation = topDown()) {
      leftOffset =  [0|int i<-[0..height]];
      rightOffset =  [0|int i<-[0..height]];
      // println(orientation);
      if (orientation==leftRight() || orientation == rightLeft()) t = mirror(t);
      // println("Help scaleX <scaleX>");
      return absoluteX(doShapeTree(t, height, ySeparation, ySeparation, xSeparation),0 , scaleX, scaleY);
   }

TreeNode doShapeTree(TreeNode t, int height, int yPosition, int ySeparation, int xSeparation) {
      // println("doShapeTree <t.id>");
      t.left = <0, [0|int i<-[0..height]]>;
      t.right = <0,[0|int i<-[0..height]]>;
      // println("xSep: <xSeparation>");
      t.x = 0;
      if (isEmpty(t.branches)) {
          t.left.yPosition = yPosition + t.height-1;
          t.right.yPosition = yPosition + t.height-1;
          // println("Leaves <t.id> <t.left.yPosition> : <t.id> <t.right.yPosition> h = <t.height>");
          }
      else {
        list[TreeNode] outline = [doShapeTree(b, height, yPosition+t.height+ySeparation
            , ySeparation, xSeparation)
             |b<-t.branches];
        t.left = outline[0].left;
        t.right = outline[0].right;
        outline[0].x = 0;
        int i  = 1;
        /* Overlap */
        for (b<-tail(outline)) {
            int overlap = 0;   
            for (int j<-[yPosition+t.height+ySeparation..
                    min([b.left.yPosition, t.right.yPosition])+1]) {
                      // println("H: <t.right.offset[j]> <b.left.offset[j]>");
                      overlap = max([overlap, b.left.offset[j]+t.right.offset[j]]);
                      // println("Q:overlap <overlap>  <b.left.offset[j]> <t.right.offset[j]>");
                      }
            // println("QQQ <t.id>:<yPosition+t.height+ySeparation> <min([b.left.yPosition, t.right.yPosition])> <overlap>");
            // println("overlap=<overlap>");
            // println("RL:<zip(t.right.offset,b.left.offset)>");       
            outline[i].x = overlap+xSeparation;  
            // println("<t.id>: x =<outline[i].x>");     
           /* Adjust left outline */
           // println("From: (<yPosition>) <t.left.yPosition+1> to  <b.left.yPosition+1>");
           // println("Check <t.left.yPosition+1> \< <b.left.yPosition+1>");
           
           for (int j <- [t.left.yPosition+1 .. b.left.yPosition+1]) {
                t.left.offset[j] = b.left.offset[j] - outline[i].x;
            }
            // println("left offset:<t.left.offset[b.left.yPosition]>");      
            /* Adjust right outline */
            for (int j <- [yPosition .. b.right.yPosition+1]) {
                t.right.offset[j] = b.right.offset[j]+ outline[i].x;
                
            }
            //println("<outline[0].id> (<t.right.yPosition>, <t.left.yPosition>) <
            //b.id> (<b.left.yPosition> <b.right.yPosition>) (<b.height>) (<
            //t.right.offset[t.right.yPosition]>, <t.left.offset[b.left.yPosition]>)
            //");
            // println("right offset:<t.right.offset[b.right.yPosition]>");
            t.left.yPosition = max([b.left.yPosition, t.left.yPosition]);
            t.right.yPosition = max([b.right.yPosition, t.right.yPosition]);
            
          i=i+1;
       }
       // println("position:<<t.left.yPosition, t.right.yPosition>>");
       if (!isEmpty(outline)) {
           int centre = last(outline).x/2;
           // println("center: <center>");
           for (int i<-[0..size(outline)]) {         
              outline[i].x -= centre;  
              // println("outline:<outline[i].x> <outline[i].width>");         
              }
           // println("Check: <yPosition+t.height>\<<t.left.yPosition+1>");
           for (int i<-[yPosition..t.left.yPosition+1]) t.left.offset[i] += centre;
           for (int i<-[yPosition..t.right.yPosition+1]) t.right.offset[i] -= centre;
          //println("Round <t.id>: <yPosition> -\> <t.right.yPosition>");
          // for (int i<-[yPosition..t.right.yPosition+1]) {
          //     println("y=<i>  <t.left.offset[i]> <t.right.offset[i]> <t.right.offset[i]+t.left.offset[i]> <t.width>");
          //     }
           t.branches = outline;
           } 
      }
      for (int i<-[yPosition-ySeparation..yPosition+t.height]) {
         t.left.offset[i] = (t.width)/2;
         t.right.offset[i] =(t.width+1)/2;
         } 
      
      t.y = yPosition;
      return t;
       
}