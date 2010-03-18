package org.rascalmpl.library;


import java.util.IdentityHashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.values.ecore.Factory;

public class ECore {
	private static TypeFactory tf = TypeFactory.getInstance();
	private final IValueFactory vf;
	
	public ECore(IValueFactory vf) {
		this.vf = vf;
	}
	

	public IConstructor readECoreXMI(ISourceLocation loc) {
		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
		
		// TODO: only file protocol is allowed now
		URI fileURI = URI.createFileURI(loc.getURI().getPath());

		Resource resource = rs.getResource(fileURI, true);
		IConstructor ecore = new ECoreToADT(resource).convert();
		return ecore;
	}
	
	private class ECoreToADT {
		private final IdentityHashMap<EObject, IConstructor> memo = new IdentityHashMap<EObject, IConstructor>();
		private final Resource resource;
		// TODO: add opposites tracking.
		private final IRelationWriter subtype;
		private final IRelationWriter features;
		private final ISetWriter classifiers;
		
		
		public ECoreToADT(Resource resource) {
			this.resource = resource;
			this.classifiers = vf.setWriter(Factory.Classifier);
			this.features = vf.relationWriter(tf.tupleType(Factory.Classifier, "class", 
					Factory.Feature, "features", Factory.Type, "typ"));
			this.subtype = vf.relationWriter(tf.tupleType(Factory.Classifier, "sub", Factory.Classifier, "super"));
		}

		public IConstructor convert() {
			convertContents(resource.getContents());
			return vf.constructor(Factory.ECore_ecore,
					classifiers.done(),
					features.done(),
					subtype.done());
		}
		
		private void convertContents(EList<EObject> objs) {
			for (EObject o: objs) {
				convertContent(o);
			}
		}
		
		private void convertContent(EObject o) {
			if (o instanceof EClassifier) {
				classifiers.insert(convertClassifier((EClassifier) o));
			}
			if (o instanceof EPackage) {
				convertPackageContents((EPackage)o);
			}
		}

		
		
		private void convertPackageContents(EPackage pkg) {
			for (EClassifier c: pkg.getEClassifiers()) {
				classifiers.insert(convertClassifier(c));
			}
			for (EPackage sub: pkg.getESubpackages()) {
				convertPackageContents(sub);
			}
		}

		private IConstructor convertClass(IList pkg, EClass c) {
			if (!memo.containsKey(c)) {
				boolean abs = c.isAbstract();
				boolean inf = c.isInterface();
				java.lang.String name = c.getName();
				
				IConstructor cons = vf.constructor(Factory.Classifier_class,
						pkg,
						vf.string(name), 
						vf.bool(abs), 
						vf.bool(inf));
				memo.put(c, cons);
				
				// This must be *after* memo.put
				// otherwise non-termination risk
				recordFeatures(cons, c);
				recordSubtypes(cons, c);
			}
			return memo.get(c);
		}
		
		// TODO: refactor package argument passing; not needed with packagePath
		private void recordSubtypes(IConstructor cons, EClass c) {
			for (EClass sup: c.getESuperTypes()) {
				IConstructor supCons = convertClass(packagePath(sup.getEPackage()), sup);
				subtype.insert(vf.tuple(cons, supCons));
			}
		}

		private IConstructor convertDataType(IList pkg, EDataType d) {
			if (!memo.containsKey(d)) {
				java.lang.String name = d.getName();
				boolean ser = d.isSerializable();
				IConstructor cons = vf.constructor(Factory.Classifier_dataType,
						pkg,
						vf.string(name), 
						vf.bool(ser));
				memo.put(d, cons);
			}
			return memo.get(d);
		}
		
		private void recordStructuralFeature(IConstructor owner, IConstructor feature, ETypedElement elt) {
			IConstructor typeCons = makeType(elt);
			features.insert(vf.tuple(owner, feature, typeCons));
		}

		private IConstructor makeType(ETypedElement elt) {
			EClassifier type = elt.getEType();
			if (type == null) {
				return vf.constructor(Factory.Type_none);
			}
			
			boolean ordered = elt.isOrdered(); 
			boolean unique = elt.isUnique();
			int lowerBound = elt.getLowerBound();
			int upperBound = elt.getUpperBound();
			boolean many = elt.isMany();
			boolean required = elt.isRequired();
			
			IConstructor typeCons = vf.constructor(Factory.Type_classifier, 
					convertClassifier(type),
					vf.bool(ordered),
					vf.bool(unique),
					vf.integer(lowerBound),
					vf.integer(upperBound),
					vf.bool(many),
					vf.bool(required));
			return typeCons;
		}

		private void recordFeatures(IConstructor owner, EClass c) {
			for (EReference ref: c.getEReferences()) {
				recordStructuralFeature(owner, convertReference(ref), ref);
			}
			for (EAttribute attr: c.getEAttributes()) {
				recordStructuralFeature(owner, convertAttribute(attr), attr);
			}
			for (EOperation op: c.getEOperations()) {
				recordOperation(owner, op);
			}
		}

		private void recordOperation(IConstructor owner, EOperation op) {
			IListWriter params = vf.listWriter(tf.stringType());
			IListWriter types = vf.listWriter(Factory.Type);
			for (EParameter param: op.getEParameters()) {
				params.append(vf.string(param.getName()));
				types.append(makeType(param));
			}
			IConstructor opCons = vf.constructor(Factory.Feature_operation,
					vf.string(op.getName()),
					params.done());
			IConstructor sig = vf.constructor(Factory.Type_signature,
					makeType(op),
					types.done());
			features.insert(vf.tuple(owner, opCons, sig));
		}

		private IConstructor convertAttribute(EAttribute attr) {
			boolean id = attr.isID();
			IConstructor struct = vf.constructor(Factory.Structural_attribute, 
					vf.string(attr.getName()),
					vf.bool(id));
			return makeStructural(attr, struct);
		}
		
		private IConstructor convertReference(EReference ref) {
			boolean containment = ref.isContainment();
			boolean container = ref.isContainer();
			boolean prox = ref.isResolveProxies();
			IConstructor struct = vf.constructor(Factory.Structural_reference,
					vf.string(ref.getName()),
					vf.bool(containment),
					vf.bool(container),
					vf.bool(prox));
			return makeStructural(ref, struct);
		}
			
		private IConstructor makeStructural(EStructuralFeature f, IConstructor struct) {
			boolean chg = f.isChangeable();
			boolean vol = f.isVolatile();
			boolean trans = f.isTransient();
			boolean unset = f.isUnsettable();
			boolean deriv = f.isDerived();
			
			return vf.constructor(Factory.Feature_structural,
					struct,
					vf.bool(chg),
					vf.bool(vol),
					vf.bool(trans),
					vf.bool(unset),
					vf.bool(deriv));
		}

		private IConstructor convertClassifier(EClassifier c) {
			IList pkg = packagePath(c.getEPackage());
			return convertClassifier(pkg, c);
		}
		
		private IList packagePath(EPackage pkg) {
			IListWriter l = vf.listWriter(tf.stringType());
			while (pkg != null) {
				l.insert(vf.string(pkg.getName()));
				pkg = pkg.getESuperPackage();
			}
			return l.done();
		}

		private IConstructor convertClassifier(IList pkg, EClassifier c) {
			if (c instanceof EClass) {
				return convertClass(pkg, (EClass) c);
			}
			if (c instanceof EDataType) {
				return convertDataType(pkg, (EDataType) c);
			}
			throw new RuntimeException("not yet implemented classifier");
		}

	}
	
}
