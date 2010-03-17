module ECore

alias Package = list[str];

data ECore = ecore(
		set[Classifier] classifiers, 
		rel[Classifier sub, Classifier  super] subtype, 
		rel[Element element, Type typ] typing);


data Classifier = dataType(Package package, str name, bool serializable)
	        | enum(Package package, str name, list[Literal] literals)
		| class(Package package, str name, list[Feature] features, bool abstract, bool interface);

data Literal = literal(str name, int val);

data Element = element(Classifier owner, Feature feature);

data Type = classifier(Classifier classifier,
			bool ordered, 
			bool unique, 
			int lowerBound, 
			int upperBound, 
			bool many, 
			bool required)
	  | signature(Type result, list[Type] parameters);

data Feature = structural(Structural structural, 
			bool changeable,
			bool volatile,
			bool transient,
			bool unsettable,
			bool derived)
	     | operation(str name, list[str] parameters);

data Structural = attribute(str name, bool id)
             	| reference(str name, bool containment, bool container, bool resolveProxies);

@doc{Load an XMI representation of an ECore model.
Only locations following the file protocol are supported.}
@javaClass{org.rascalmpl.library.ECore}
public ECore java readECoreXMI(loc file);
