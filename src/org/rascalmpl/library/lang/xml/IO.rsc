module lang::xml::IO

@javaClass{org.rascalmpl.library.lang.xml.IO}
java node readXML(loc file, bool trim = true, bool fullyQualify = false);

@javaClass{org.rascalmpl.library.lang.xml.IO}
java node readXML(str contents, bool trim = true, bool fullyQualify = false);