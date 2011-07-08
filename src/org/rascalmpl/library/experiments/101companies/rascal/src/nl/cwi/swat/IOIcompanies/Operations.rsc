module nl::cwi::swat::IOIcompanies::Operations

import nl::cwi::swat::IOIcompanies::AST;

public Company cut(Company c) {
	return visit (c) {
		case employee(name, address, salary) => employee(name, address, salary / 2)
	}
}

public int total(Company c) {
	return (0 | it + salary | /employee(name, address, salary) <- c);
}