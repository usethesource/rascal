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


alias EDGE = tuple[int yPosition, list[int] offset]; 

data FigTree = br(Figure root, list[FigTree] figs)|lf(Figure root);

TreeNode convertFigure(Figure f, map[str, tuple[int, int]] m, int scaleX = 5, int scaleY = 5) {
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
    
void makeMap(TreeNode t) {
   innermost visit(t) {
       case u:treenode(_, _,_,_): m+=(u.id:u);
       }
   }

list[Vertex] treeLayout(Figure f, map[str, tuple[int, int]] m, 
    int scaleX, int scaleY, int height, int xSeparation = 5, int ySeparation = 10) {
    TreeNode z = convertFigure(f, m, scaleX=scaleX, scaleY=scaleY);
    // println(z);
    TreeNode r = layoutTree(z, height, scaleX=scaleX, scaleY=scaleY
    , xSeparation = xSeparation, ySeparation = ySeparation);
    makeMap(r);
    // println([x|x<-m]);
    list[Vertex] q = display(r, []);
    // println(q);
    return q;
    }

Figures treeUpdate(Figures fs,  map[str, tuple[int, int]] q) {
    Figures r = []; 
    for (Figure f<-fs) {
       f.width = q[f.id][0];
       f.height = q[f.id][1];
       // println("treeupdate: <f.width>");
       // f.width=  f.width/5; f.height = 0;
       f.at = <m[f.id].x-f.width/2, m[f.id].y-f.height/2>;
       r+=[f];
       }
    return r;
    }
    


public void main () {
   Figure b = tree(box(size=<50, 50>, fillColor = "none", lineWidth = 1)
     ,[box(size=<60, 60>, lineWidth = 1)
     , circle(r=50, fillColor = "none", lineWidth = 1)
      ]);
   // render(wirth());
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

void printT(TreeNode t) {
    innermost visit(t) {
       case u:treenode(_,_,_): println("<u.x>, <u.y>  <u.width> <u.height>");
       }
    }
    
Figure dtree(TreeNode t) = shape(display(t, [])
  // , scaleX=<<-40, 40>, <0,400>>, scaleY=<<0, 40>, <0,400>>
  , size=<400, 400>, yReverse= false
   );
    
list[Vertex] display(TreeNode t, list[Vertex] v) {
    for (b<-t.branches) v= display(b, v+[move(t.x, t.y),line(b.x, b.y)]);
    return v;
    }
    
TreeNode absoluteX(TreeNode t, int x, int scaleX, int scaleY) {
    // println("NODE1: <t.width> <x> <t.x>");
    t.x += x;
    if (t.x-t.width<MIN) MIN = t.x-t.width;
    // println("NODE2: <t.width> <t.x>");
    t.branches = [absoluteX(b, t.x, scaleX, scaleY)|b<-t.branches];
    t.x= (t.x-MIN)*scaleX;
    t.y = t.y*scaleY;
    return t;
    }
    
TreeNode layoutTree(TreeNode t, int height, int scaleX=5, int scaleY = 5,
      int ySeparation = 10, int xSeparation = 5) {
      leftOffset =  [0|int i<-[0..height]];
      rightOffset =  [0|int i<-[0..height]];
      m = ();
      MIN = 0;
      return absoluteX(doShapeTree(t, ySeparation, ySeparation, xSeparation),0 , scaleX, scaleY);
   }

TreeNode doShapeTree(TreeNode t, int yPosition, int ySeparation, xSeparation) {
      t.left = <0, leftOffset>;
      t.right = <0, rightOffset>;
      t.x = 0;
      if (isEmpty(t.branches)) {
          t.left.yPosition = yPosition + t.height-1;
          t.right.yPosition = yPosition + t.height-1;
          }
      else {
        list[TreeNode] outline = [doShapeTree(b, yPosition+t.height+ySeparation
            , ySeparation, xSeparation)
             |b<-t.branches];
        t.left = outline[0].left;
        t.right = outline[0].right;
        outline[0].x = 0;
        int i  = 1;
        /* Overlap */
        for (b<-tail(outline)) {
            int overlap = 0;
            // println("QQQ:<yPosition+t.height+Y_SEPARATION> <min([b.left.yPosition, t.right.yPosition])>");
            for (int j<-[yPosition+t.height+ySeparation..
                    min([/*b.left.yPosition,*/ t.right.yPosition])]) {
                      overlap = max([overlap, b.left.offset[j]+t.right.offset[j]])+0;
                      // println("Q:overlap <overlap>  <b.left.offset[j]> <t.right.offset[j]>");
                      }
            // println("overlap=<overlap>");   
            outline[i].x = overlap+xSeparation;         
           /* Adjust left outline */
           for (int j <- [t.left.yPosition+1 .. b.left.yPosition]) {
                t.left.offset[j] = b.left.offset[j] - outline[i].x;
            }
            // t.left.yPosition = max([b.left.yPosition, t.left.yPosition]);
            /* Adjust right outline */
            for (int j <- [yPosition .. b.right.yPosition]) {
                t.right.offset[j] = b.right.offset[j]+ outline[i].x;
                // println("right:<t.right.offset[j]>");
            }
            t.right.yPosition = max([b.right.yPosition, t.right.yPosition]);
          i=i+1;
       }
       if (!isEmpty(outline)) {
           int centre = last(outline).x/2;
           for (int i<-[0..size(outline)]) {         
              outline[i].x -= centre;  
              // println("outline:<outline[i].x> <outline[i].width>");         
              }
           for (int i<-[yPosition..t.left.yPosition+1]) t.left.offset[i] += centre;
           for (int i<-[yPosition..t.right.yPosition+1]) t.right.offset[i] -= centre;
           t.branches = outline;
           }     
      }
      for (int i<-[yPosition-ySeparation..yPosition+t.height]) {
         t.left.offset[i] =t.width/2;
         t.right.offset[i] =(t.width+1)/2;
         }   
      t.y = yPosition;
      return t;
       
}