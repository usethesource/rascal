module experiments::vis2::Tst

import Prelude;
import lang::json::IO;
import experiments::vis2::Figure;
import experiments::vis2::FigureServer; 


void ex(str title, Figure f){
	render(title, f);
}

 // writeFile(|file:///ufs/bertl/aap.txt|, d);

//public void main() 
//     d = [["\<"]];
//     println("aap:<toJSON(d)>");
//     }

public void main() {
   ex("aap", scatterchart([<1,2>,<3, 3>]
   //, options = chartOptions(pointSize=10)
   ));
   }