module util::notebook::HTML

import IO;


@javaClass{org.rascalmpl.library.util.notebook.HTML}
public java str compile(str templateLoc);

public str toHTML(str template){
	writeFile(|tmp:///jade/tmp.jade|, template);
	return compile(resolveLocation(|tmp:///jade/tmp.jade|).path);
}