module nl::cwi::swat::IOIcompanies::Visualize

import nl::cwi::swat::IOIcompanies::AST;

import vis::Figure;
import vis::Render;

private str getId(Company c) 		{ return "c_" + c.name; }
private str getId(Department d) 	{ return "d_" + d.name; }
private str getId(Manager m) 		{ return "m_" + m.employee.name; }
private str getId(Employee e) 		{ return "e_" + e.name; }

private Figure getBox(Company c) 	{ return box(text(c.name), 			vis::Figure::id(getId(c)), fillColor("red")); }
private Figure getBox(Department d) { return box(text(d.name), 			vis::Figure::id(getId(d)), fillColor("orange")); }
private Figure getBox(Manager m) 	{ return box(text(m.employee.name), vis::Figure::id(getId(m)), fillColor("yellow"), size(m.employee.salary / 1000)); }
private Figure getBox(Employee e) 	{ return box(text(e.name), 			vis::Figure::id(getId(e)), fillColor("white"), size(e.salary / 1000)); }

private Edge getEdge(str from, str to) {
	return edge(from, to, shapeClosed(true), fillColor("black"));
}

/*private Edge getEdge(str from, str to) {
	return edge(from, to, shapeClosed(true), fillColor("black")); //, toArrow(shape([vertex(0,0), vertex(4,8), vertex(8, 0)]))
} */

/*private list[Edge] depEdges(value from, set[value] to) {
	return [ getEdge(from, n) | n <- to ];
}*/

/*private list[Edge] depEmps(str dep, set[Employee] emps) {
	return [ getEdge(dep, n) | employee(n,_,_) <- emps ];
}*/

public Figure toTree(Company c) {
	_nodes = [getBox(c)] + [
			[getBox(d), getBox(mngr)] + [ getBox(e) | e <- emps] 
		| /d:department(_,mngr,_,emps) <- c ];
	
	_edges = [ getEdge(getId(d), getId(d.manager))							// dep -> manager
				+ [ getEdge(getId(d), getId(sd)) | sd <- d.subDepartments ] // dep -> subdepartments
				+ [ getEdge(getId(d), getId(e)) | e <- d.employees ]		// dep -> employees
			 | /d:department(_,_,_,_) <- c ]
	       + [ getEdge(getId(c), getId(d)) | d <- c.departments ];			// company -> departments
	
    return tree(_nodes, _edges, /*hint("layered"),*/ hint("layered"), size(400), gap(20));
}

public void render(Company c) {
	render(toTree(c));
}