module nl::cwi::swat::IOIcompanies::Verify

import nl::cwi::swat::IOIcompanies::AST;
import List;
import Set;

public set[str] verify(Company c) {
	errors = {};

	empNameRel = { <e.name, d> | <e,d> <- getEmpDepRel(c) };
	errors += { e + " serves in multiple positions" | e <- empNameRel<0>, size(empNameRel[e]) > 1 }; 

	return errors;
}

public rel[Employee, list[Department]] getEmpDepRel(Company c) {
	set[list[Department]] deps = { [d] | d <- c.departments };
	solve(deps)
		deps = deps + { d + [s] | d <- deps, s <- last(d).subDepartments };
	return { s | d <- deps, s := { <e, d> | e <- last(d).employees } + {<last(d).manager.employee, d>} };
}

