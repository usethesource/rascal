module vis::examples::Outline

import vis::Figure;
import vis::Render;


public void out1(){
  render(outline([size(100)], (10: color("red"))));
}
