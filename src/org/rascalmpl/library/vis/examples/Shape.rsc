module vis::examples::Shape

import vis::Figure;
import vis::Render;
import Number;

import List;
import Set;
import IO;

// Shape: line graph with circle on each point

public void s1(int s){
    dt1 = [10, 20, 10, 30];
	b = shape([
                lineColor("blue"),
                lineWidth(2),
                connected(),
	            fillColor("lightgreen")
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([size(s), lineWidth(0), fillColor("red")])) | int i <- [0 .. size(dt1) -1]]
               );
    render(b);
}

public void s2(int s){
    dt1 = [10, 20, 0, 30];
	b = shape([
                lineColor("blue"),
                lineWidth(2),
                connected(), closed(),
	            fillColor("lightgreen")
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([size(s), lineWidth(0), fillColor("red")])) | int i <- [0 .. size(dt1) -1]]
               );
    render(overlay([b, box([left(), bottom(), size(20)])]));
}



// Shape: curved (fitted) graph with circle on each point

public void s3(int s){
    dt1 = [10, 20, 10, 30];
	b = shape([
                lineColor("blue"),
                lineWidth(2),
	            fillColor("lightgreen"),
	            connected(),
	            curved()
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([size(s), lineWidth(0), fillColor("lightblue")])) | int i <- [0 .. size(dt1) -1]]
               );
    render(b);
}

// Two overlayed shapes with closed and curved graphs
public void s4(){
    dt1 = [10, 20, 10, 30];
    dt2 = [15, 10, 25, 20];
	sh1 = shape([
                lineColor("blue"),
                lineWidth(2),
	            fillColor(color("lightblue", 0.5)),
	            curved(), closed()
               ],
               [ vertex(i * 50, 10 * dt1[i], ellipse([size(10), lineWidth(0), fillColor("white")])) | int i <- [0 .. size(dt1) -1]]
               );
    sh2 = shape([
                lineColor("green"),
                lineWidth(2),
	            fillColor(color("lightgreen", 0.5)),
	            curved(), closed()
               ],
               [ vertex(i * 50, 10 * dt2[i], ellipse([size(10), lineWidth(0), fillColor("black")])) | int i <- [0 .. size(dt2) -1]]
               );
    render(overlay([sh1, sh2]));
}