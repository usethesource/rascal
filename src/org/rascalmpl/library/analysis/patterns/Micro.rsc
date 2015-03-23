module analysis::patterns::Micro

data MicroPattern
	= designator()
	| taxonomy()
	| joiner()
	| pool()
	| functionPointer()
	| functionObject()
	| cobolLike()
	| stateless()
	| commonState()
	| immutable()
	| restrictedCreation()
	| sampler()
	| box()
	| compoundBox()
	| canopy()
	| record()
	| dataManager()
	| sink()
	| outline()
	| trait()
	| stateMachine()
	| pureType()
	| augmentedType()
	| pseudoClass()
	| implementor()
	| overrider()
	| extender()
	;
	
public set[MicroPattern] DegenerateClasses()
	= DegenerateStateAndBehavior()
	+ DegenerateBehavior()
	+ DegenerateState()
	+ ControlledCreation()
	;
	
public set[MicroPattern] Containment()
	= Wrappers()
	+ DataManagers()
	;

public set[MicroPattern] Inheritance()
	= BaseClasses()
	+ Inheritors()
	;

public set[MicroPattern] DegenerateStateAndBehavior()
	= { designator(), taxonomy(), joiner(), pool() };
	
public set[MicroPattern] DegenerateState()
	= { functionPointer(), functionObject(), cobolLike() };
	
public set[MicroPattern] DegenerateBehavior()
	= { stateless(), commonState(), immutable(), record(), dataManager() };

public set[MicroPattern] ControlledCreation()
	= { restrictedCreation(), sampler() };
	
public set[MicroPattern] Wrappers()
	= { box(), compoundBox(), canopy() };

public set[MicroPattern] DataManagers()
	= { record(), dataManager(), sink() };

public set[MicroPattern] BaseClasses()
	= { outline(), trait(), stateMachine(), pureType(), augmentedType(), pseudoClass() };
	
public set[MicroPattern] Inheritors()
	= { implementor(), overrider(), extender() };