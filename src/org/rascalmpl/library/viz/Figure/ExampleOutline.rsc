module viz::Figure::ExampleOutline

import viz::Figure::Core;
import viz::Figure::Render;


public void out1(){
  render(outline([size(100)], (10: color("red"))));
}
