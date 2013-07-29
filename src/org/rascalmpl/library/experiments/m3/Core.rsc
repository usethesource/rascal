module experiments::m3::Core

data Modifiers
	= \private()
	| \public()
	| \protected()
	| \friendly()
	| \static()
	| \final()
	| \synchronized()
	| \transient()
	| \abstract()
	| \native()
	| \volatile()
	| \strictfp()
	| \deprecated()
	| \annotation(loc \anno)
  	;

data M3 = m3();
             
anno rel[loc name, loc src] M3@source;
anno rel[loc from, loc to] M3@containment;
anno list[str errorMessage] M3@projectErrors;
anno rel[loc from, loc to] M3@libraryContainment;
anno map[str simpleName, set[loc] qualifiedName] M3@resolveNames;