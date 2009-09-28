module experiments::Processing::mouse1
import Processing;
import IO;

public void setup() {
	size(400, 400); 
	stroke(255);
	background(192, 64, 0);
}

public void draw() { 
    line(150, 25, mouseX(), mouseY());
}

public void main(){
	processing();
}