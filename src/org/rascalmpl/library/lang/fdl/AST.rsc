@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl}
module AST

data FeatureDiagram
	= diagram(str name, list[str] includes, FeatureDefinitions definitions, list[Constraint] constraints)
	;
	
data FeatureDefinitions 
	= definitions(list[FeatureDefinition] definitions)
	;
	
data FeatureDefinition
	= definition(str name, FeatureExpression expression) 
	;

data FeatureExpression
	= requireAll(list[FeatureExpression] features)
	| oneOf(list[FeatureExpression] features)
	| moreOf(list[FeatureExpression] features)
	| ref(QualifiedName reference)
	| atomic(str name)
	| optional(FeatureExpression expression) 
	| defaultValue(str atomic)
	;

data QualifiedName
    = qn(str name)
    | qn(str namespace, str name)
    ;

data Constraint
	= constraintDia(DiagramConstraint diaConst)
	| constraintUser(UserConstraint userConst)
	;
	
data DiagramConstraint
	= requires(str feature1, str feature2) 
	| excludes(str feature1, str feature2)
	;
	
data UserConstraint
	= include(str feature)
	| exclude(str feature)
	; 

anno loc FeatureDiagram@location;
anno loc FeatureDefinition@location;
anno loc QualifiedName@location;
anno loc FeatureExpression@location;