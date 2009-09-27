module experiments::Processing::mouse1
import Processing;
import IO;

public void setup() {
println("setup1");
	size(400, 400); 
	println("setup2");
	stroke(255);
	println("setup3");
	background(192, 64, 0);
	println("setup4");
}

public void draw() { 
    line(150, 25, mouseX(), mouseY());
}

public void main(){

	start();
}