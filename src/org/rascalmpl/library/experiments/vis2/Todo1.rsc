module experiments::vis2::Todo1

import experiments::vis2::Figure;
import experiments::vis2::FigureServer;
import IO;

data Todo = todo(bool done, str txt);
   
alias Todos = list[Todo];

data Model = model(Todos tds, str new);

Figure visTodos(Model m){
	return vcat(size = <100,400>, align=<left(),top()>,
				figs=[  hcat(figs=[ text("Todos", fontSize=20), 
								    box(width=90,stroke="white"), 
								    text("New todo:"), 
								    strInput(event=on("submit", bind(m.new)), size=<100,20>)]),
					   *[visTodo(m.tds, td) | td <- m.tds]
					 ]);
}

Figure visTodo(Todos tds, Todo td){
	return hcat(figs=[ checkboxInput(event=on("click", bind(td.done)), size=<50,20>),
					   text(td.txt, width=100),
					   buttonInput(trueText="remove", event=on("click", bind(tds, remove(tds, td))), size=<50,20>)
					 ]);
}

Model transform(Model m) {
	if(m.new != ""){
		m.tds = m.tds + [todo(false, m.new)];
		m.new = "";
	}
	return m;
}

Todos remove(Todos tds, Todo td) = [ td1 | td1 <- tds, td1 != td];

void todoApp1() {
 	todos = [todo(false, "reviewing"), todo(false, "email"), todo(false, "grading")];
 	Model m = model(todos, "");
 
 	render("todoApp1", #Model, m, visTodos, transform);  
}