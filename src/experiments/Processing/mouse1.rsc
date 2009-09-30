module experiments::Processing::mouse1
import Processing;
import IO;

@setup{}
public void setup() {
	size(400, 400); 
	stroke(255);
	background(192, 64, 0);
}

@draw{}
public void draw() { 
    line(150, 25, mouseX(), mouseY());
}

public void main(){
	P = processing();
}