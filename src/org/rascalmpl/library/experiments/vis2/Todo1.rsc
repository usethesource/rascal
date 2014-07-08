module experiments::vis2::Todo1

import experiments::vis2::Figure;
import experiments::vis2::FigureServer;

data Todo = todo(bool done, str txt);
   
alias Todos = list[Todo];

data Model = model(Todos tds, str new);

Figure visTodos(Model m) =
   grid(gap=<10,10>, pos=middleLeft,
   		figArray=[ [ text("Todos", fontSize=20), text("New todo:"),  strInput(event=on("submit", bind(m.new)), size=<100,20>)],
				   *[ visTodo(m.tds, td) | td <- m.tds ]
				 ]);

Figures visTodo(Todos tds, Todo td) =
	[ checkboxInput(event=on("click", bind(td.done)), size=<50,20>),
	  text(td.txt, pos=middleLeft),
      buttonInput(trueText="remove", pos=middleLeft, event=on("click", bind(tds, remove(tds, td))), size=<50,20>)
    ];

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