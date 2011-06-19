package org.rascalmpl.library.vis.properties;

public enum Types {
	BOOL("bool","b"),
	COLOR("Color","c","str"),
	FIGURE("Figure","f"),
	HANDLER("void ()","h"),
	INT("int","i"),
	REAL("num","r"),
	STR("str","s");
	
	public String rascalName;
	public String syntaxSugar;
	public String shortName;
	
	
	Types(String rascalName,String shortName,String syntaxSugar){
		this.rascalName = rascalName;
		this.shortName = shortName;
		this.syntaxSugar = syntaxSugar;
	}
	
	Types(String rascalName,String shortName){
		this(rascalName,shortName,null);
	}
}
