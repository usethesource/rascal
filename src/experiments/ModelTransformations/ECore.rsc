module ModelTransformations::ECore

// Attempt to describe ECore as a Rascal datatype

data EClass = eClass(str name, 
                     list[EClass] eSuperTypes,
                     list[EAttribute] eAttributes,    //0..*
                     list[EReferences] eReferences); //0..*
                     
data EAttribute = eAttribute(str name, EDataType eAttributeType);

data EDataType = eDataType(str name);

data EReference = eReference(str name,
                             bool containment,
                             int lowerbound,
                             int upperbound,
                             list[EReference] eOpposite); // 0..1