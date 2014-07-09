module experiments::vis2::Todo2

import experiments::vis2::Figure;
import experiments::vis2::FigureServer;

data Todo    = todo(bool done, str txt);
data TDModel = tdmodel(list[Todo] tds);

Figure viewTDModel(TDModel m){
	return vcat(size = <100,400>, align=<left(),top()>,
				figs = [ hcat(figs=[ text("Todos", fontSize=20), 
								     box(width=90,stroke="white"), 
								     text("New todo:"), 
								     strInput(event=on("submit", call(TDModel (TDModel m, str txt){ return m + todo(false, txt); })), size=<100,20>)
								   ]),
					     *[viewTodo(td) | td <- m.tds]
					   ]);
}

Figure viewTodo(Todo td){
	return hcat(figs = [ checkboxInput(event=on("click", bind(td.done)), size=<50,20>),
					     text(td.txt, width=100),
					     buttonInput(trueText="remove", event=on("click", delete(td)), size=<50,20>)
					   ]);
}

void todoApp2() {
 	todos = [todo(false, "reviewing"), todo(false, "email"), todo(false, "grading")];
 	render("todoApp2", #Model, tdmodel(todos), viewTDModel);  
}