@license{
  Copyright (c) 2009-2011 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI}
module ModelTransformations::ECore

// Definition of ECore.
// See http://download.eclipse.org/modeling/emf/emf/javadoc/2.5.0/org/eclipse/emf/ecore/package-summary.html

data EModelElement =
     eModelElement(list[EAnnotation] eAnnotations);
     
data EAnnotation = 
     eAnnotation(EModelElement inh,
                 str source,
                 EStringToStringMapEntry details,
                 list[EObject] contents,
                 list[EObject], references
                 );

data ENamedElement =
     eNamedElement(EModelElement inh,
                   str name);
             
data EFactory =
     eFactory(EModelElement inh,
              EPackage ePackage);
                    
data ETypedElement =
     eTypedElement(ENamedElement inh,
                   bool ordered, // =true
                   bool unique, // = true
                   int lowerBound,
                   int upperBound, // =1
                   bool many,
                   bool required,
                   list[EClassifier] eType);

data EClassifier = 
     eClassifier(ENamedElement inh,
                 str instanceClassName,
                 EJavaClass instanceClass,
                 EJavaObject defaultValue,
                 list[EPackage] eClassifiers);

data EPackage =
     ePackage(ENamedElement inh,
              str nsURI,
              str nsPrefix,
              list[EPackage] eSubpackages;
              EFactory eFactoryInstance);
                              
data EOperation =
     eOperation(ETypedElement inh,
                list[EParameter] eParameters);

data EParameter =
     eParameter(ETypedElement inh);
     
data EClass = 
     eClass(EClassifier inh,
            bool abstract,
            bool interface,
            list[EOperation] eAllOperations,
            list[EStructuralFeature] eAllStructuralFeatures,
            list[EReference] eAllContainments,
            list[EReference] eAllReferences,
            list[EReference] eReferences,
            list[EAttribute] eAllAttributes,
            list[EAttribute] eAttributes,
            list[EAttribute] eIDAttribute,
            list[EClass] eAllSuperTypes,
            list[EClass] eSuperTypes);
                          
data EDataType = 
     eDataType(EClassifier inh,
               bool serializable); // = true

data ETypedElement =
     eTypedElement(str name, // from ENamedElement
                   EClassifier eType);

data EStructuralFeature =
     eStructuralFeature(ETypedElement inh,
                        bool changeable, // = true
                        bool volatile,
                        bool transient,
                        str defaultValueLiteral,
                        EJavaObject defautValue,
                        bool unsettable,
                        bool derived);
                        
data EReference = 
     eReference(EStructuralFeature inh,
                bool containment,
                bool container,
                bool resolveProxies, = true
                EClass eReferenceType,
                list[EReference] eOpposite); // 0..1
                             
data EAttribute =
     eAttribute(EStructuralFeature inh,
                bool iD,
                EDataType eAttributeType);
                                           
data EEnumLiteral =
     eEnumLiteral(ENamedElement inh,
                  int value,
                  EEnumerator instance,
                  list[EEnum] eLiterals);
                  
data EEnum =
     eEnum(EDataType inh);   
     
data EObject =
     eObject();
     
data EAnnotation               
