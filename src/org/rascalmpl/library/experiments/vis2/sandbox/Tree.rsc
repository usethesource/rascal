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
    int left=0, int right=0);

int MIN = 0;

map[str, TreeNode] m = ();

alias EDGE = tuple[int yPosition, list[int] offset]; 

list[tuple[EDGE left, EDGE right]] z = [];

data FigTree = br(Figure root, list[FigTree] figs)|lf(Figure root);

TreeNode convertFigure(Figure f, map[str, tuple[int, int]] m, int scaleX = 1, int scaleY = 5) {
     if (tree(Figure root, Figures figs):=f) {
           root.width = m[root.id][0];
           root.height = m[root.id][1];
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
   
list[tuple[str, tuple[int, int], int, int, tuple[int, int]]] makeInfo(TreeNode t, int sY) {
   list[tuple[str, tuple[int, int],  int, int, tuple[int, int]]] r =[];
   innermost visit(t) {
       case u:treenode(_, _,_,_): r+=<u.id, <u.x, u.y/sY>, z[u.left].left.yPosition, z[u.right].right.yPosition,
       <z[u.left].left.offset[z[u.left].left.yPosition]
       ,z[u.right].right.offset[z[u.right].right.yPosition]>>;
       }
   return r;
   }
   
bool isDisjunct(TreeNode n1, TreeNode n2) {
     bool r =  (n1.y+n1.height<n2.y ||  n2.y+n2.height < n1.y)
                 || (n1.x + (n1.width+n2.width)/2 < n2.x || n2.x + (n1.width+n2.width)/2 < n1.x);             
     if (!r)  {
        println("Error: <<n1.id, n1.x, n1.y, n1.width, n1.height>> <<n2.id, n2.x, n2.y, n2.width, n2.height>>");    
        }
      //else
      //  println("Continue: <<n1.id, n1.x, n1.y, n1.width, n1.height>> <<n2.id, n2.x, n2.y, n2.width, n2.height>>");   
     return r;
     } 
     
 public bool isDisjunct() {  
    bool disjunct = true;
    list[TreeNode] leaves = [m[d]|d<-m, isEmpty(m[d].branches)];
    for (int i<-[0..size(leaves)]) {
       for (int j<-[0..i])
             disjunct = disjunct && isDisjunct(leaves[j], leaves[i]);
        }
    return disjunct;
    }     

list[Vertex] treeLayout(Figure f, map[str, tuple[int, int]] y, 
    int scaleX, int scaleY, int height, bool cityblock, int xSeparation = 5, int ySeparation = 10) {
    z = [];
    TreeNode c = convertFigure(f, y, scaleX=scaleX, scaleY=scaleY);
    TreeNode r = layoutTree(c, height, scaleX=scaleX, scaleY=scaleY
    , xSeparation = xSeparation, ySeparation = ySeparation, orientation = f.orientation);  
    m = ();  
    MIN = 0;
    makeMap(r);
    // list[tuple[str, tuple[int, int], int, int, tuple[int, int]]] b = makeInfo(r, f.sY);
    //      for (d<-b) println(d);  
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
      // println(orientation);
      if (orientation==leftRight() || orientation == rightLeft()) t = mirror(t);
      return absoluteX(doShapeTree(t, height, ySeparation, ySeparation, xSeparation),0 , scaleX, scaleY);
   }

TreeNode doShapeTree(TreeNode t, int height, int yPosition, int ySeparation, int xSeparation) {
      // println("doShapeTree <t.id>");
      // println("xSep: <xSeparation>");
      t.x = 0;
      if (isEmpty(t.branches)) {
           t.left = size(z);
           t.right = size(z);
           z = z  +[<<yPosition + t.height-1, [0|int i<-[0..height]]>
                    ,<yPosition + t.height-1, [0|int i<-[0..height]]>>];
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
                    min([z[b.left].left.yPosition, z[t.right].right.yPosition])+1]) {
                    overlap = max([overlap, z[b.left].left.offset[j]+z[t.right].right.offset[j]]);
                    }    
           outline[i].x = overlap+xSeparation; 
            /* Adjust left outline */
           if (z[t.left].left.yPosition+1 < z[b.left].left.yPosition+1)
           for (int j <- [z[t.left].left.yPosition+1 .. z[b.left].left.yPosition+1]) {
                z[t.left].left.offset[j] = z[b.left].left.offset[j] - outline[i].x;
            }   
            /* Adjust right outline */
            if (yPosition+1 < z[b.right].right.yPosition+1)
            for (int j <- [yPosition .. z[b.right].right.yPosition+1]) {
                z[t.right].right.offset[j] = z[b.right].right.offset[j]+ outline[i].x;              
            }
             z[t.left].left.yPosition = max([z[b.left].left.yPosition, z[t.left].left.yPosition]);
             z[t.right].right.yPosition= max([z[b.right].right.yPosition, z[t.right].right.yPosition]);
                   
          i=i+1;
       }  
       if (!isEmpty(outline)) {
           int centre = last(outline).x/2;
           for (int i<-[0..size(outline)]) {         
              outline[i].x -= centre;         
              }
           // println("Check: <yPosition+t.height>\<<z[t.left].left.offset[z[t.left].left.yPosition]>");
           for (int i<-[yPosition,yPosition+1..z[t.left].left.yPosition+1]) z[t.left].left.offset[i] += centre;
           for (int i<-[yPosition,yPosition+1..z[t.right].right.yPosition+1]) z[t.right].right.offset[i] -= centre;
           t.branches = outline;
           } 
      }
      for (int i<-[yPosition-ySeparation..yPosition+t.height]) {
         z[t.left].left.offset[i] = (t.width)/2;
         z[t.right].right.offset[i] =(t.width+1)/2;
         } 
      
      t.y = yPosition;
      return t;
       
}