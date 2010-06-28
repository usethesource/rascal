module box::rascal::Modules
import rascal::\old-syntax::Modules[Body];
import box::Box;
import box::Concrete;
import rascal::\old-syntax::Names;
import rascal::\old-syntax::Types;
import rascal::\old-syntax::Comments;
public Box getModules(Tree q) {
if (Renaming a:=q) 
switch(a) {
	case `<Name from> => <Name to> `: return NULL();
}
if (Renamings a:=q) 
switch(a) {
	case `renaming <{Renaming ","}+  c > `: return NULL();
}
if (Header a:=q) 
switch(a) {
	case `<Tags tags> module <QualifiedName name> <Import* imports> `: {
	         list[Box ] h = [H( [L("module"), evPt(name)])];
             return V(h+getArgs(imports, #Import));
             }
	case `<Tags tags> module <QualifiedName name> <ModuleParameters params> <Import* imports> `: return NULL();
}
if (Import a:=q) 
switch(a) {
	case `import <ImportedModule modul> ; `: return cmd("import",  modul, ";");
	case `extend <ImportedModule modul> ; `: return NULL();
}
if (Module a:=q) 
switch(a) {
	case `<Header header> <Body body> `: return NULL();
}
if (ModuleParameters a:=q) 
switch(a) {
	case `[ <{TypeVar ","}+  c > ] `: return NULL();
}
if (ImportedModule a:=q) 
switch(a) {
	case `<QualifiedName name> <ModuleActuals actuals> <Renamings renamings> `: return NULL();
	case `<QualifiedName name> <ModuleActuals actuals> `: return NULL();
	case `<QualifiedName name> <Renamings renamings> `: return NULL();
	case `<QualifiedName name> `: return NULL();
}
if (ModuleActuals a:=q) 
switch(a) {
	case `[ <{Type ","}+  c > ] `: return NULL();
}
return NULL();
}
