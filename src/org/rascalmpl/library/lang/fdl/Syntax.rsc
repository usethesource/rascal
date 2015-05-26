@license{
  Copyright (c) 2009-2015 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jouke Stoel - Jouke.Stoel@cwi.nl}
@doc{
	This grammar describes the Feature Diagram Language which was introduced by Arie van Deursen and Paul Klint in their paper
	'Domain-Specific Language Design Requires Feature Descriptions' (2001).
}
module Syntax

extend lang::std::Layout;
extend lang::std::Id;

lexical FeatureName = [a-z A-Z 0-9 _] !<< [A-Z][a-z A-Z 0-9 _]* !>> [a-z A-Z 0-9 _];
lexical AtomicFeature = ([a-z A-Z 0-9 _] !<< [a-z][a-z A-Z 0-9 _]* !>> [a-z A-Z 0-9 _]) \ Keyword;

start syntax FeatureDiagram 
	= diagram: "diagram" Id name ("include" {Include ","}* includes)? "features" FeatureDefinitions definitions ("constraints" Constraint* constraints)?;

syntax Include
	= fileName: Id fileName;

syntax FeatureDefinitions
	= definitions: FeatureDefinition* definitions;

syntax FeatureDefinition
	= definition: FeatureName name ":" FeatureExpression expression; 

syntax FeatureExpression
	= requireAll: "all" "(" FeatureList list ")"
	| oneOf: "one-of" "(" FeatureList list ")"
	| moreOf: "more-of" "(" FeatureList list ")"
	| ref: QualifiedName reference
	| atomic: AtomicFeature name
	| optional: FeatureExpression expression "?" 
	| defaultValue: "default" "=" AtomicFeature 
	;
	
syntax QualifiedName 
	= qn: FeatureName name
	| qn: Id namespace "." FeatureName name;
		
syntax FeatureList
	= {FeatureExpression ","}+;

syntax Constraint
	= constraintDia: DiagramConstraint const
	| constraintUser: UserConstraint const
	;
	
syntax DiagramConstraint
	= requires: AtomicFeature feature1 "requires" AtomicFeature feature2 
	| excludes: AtomicFeature feature1 "excludes" AtomicFeature feature2
	;
	
syntax UserConstraint
	= include: "include" AtomicFeature feature
	| exclude: "exclude" AtomicFeature feature
	; 
	
keyword Keyword
  = "";
  

 