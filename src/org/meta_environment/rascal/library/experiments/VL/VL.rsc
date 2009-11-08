module experiments::Processing::VL

data VPROP =
     left(int left) |  left(int(int i) leftf)
   | right(int right) |  right(int(int i) rightf)
   | top(int top) |  top(int(int i) topf)
   | bottom(int bottom) |  bottom(int(int i) bottomf)
   | width(int width) |  width(int(int i) widthf)
   | height(int height) | height(int(int i) heightf)
   | visible(bool visible)
   | title(str title)
   | values(list[value] values) | values(list[value]() valuesf)
   | linewidth(int lwidth)
   | fillStyle(int fstyle)
   | strokeStyle(int sstyle)
   ;

data VELEM = 
     bar(list[VPROP] props)
     ;
     
data Panel = panel(list[VPROP] props, list[VELEM] elms);

@doc{Render a panel}
@reflect{Needs calling context when calling argument function}
@javaClass{org.meta_environment.rascal.library.experiments.VL.VL}
public void java render(Panel p);

public void main(){

    P = panel([width(300), height(300)],
              [bar([values([1, 1.2, 1.7, 1.5, 0.7]),
                    bottom(0),
                    width(20),
                    height(int (int d) {return d * 80;}),
                    left(int (int d) {return d * 25;})
                   ])
              ]);
    render(P);

}