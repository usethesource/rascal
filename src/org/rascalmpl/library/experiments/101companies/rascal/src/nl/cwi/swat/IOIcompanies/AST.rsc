module nl::cwi::swat::IOIcompanies::AST

data Company = company(str name, set[Department] departments);

data Department = department(str name, Manager manager, set[Department] subDepartments, set[Employee] employees);

data Employee = employee(str name, str address, int salary); 

data Manager = manager(Employee employee);