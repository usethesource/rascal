package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	AssertionErrorTests.class,
	AssignmentErrorTests.class,
//	ClosureInvocationErrorTests.class,
	EmptyListErrorTests.class,
	EmptySetErrorTests.class,
//	ExpressionErrorTests.class,
//	ImplementationErrorTests.class,
	IndexOutOfBoundsErrorTests.class,
//	IOErrorTests.class,
//	NoSuchAnnotationTests.class,
	NoSuchFieldErrorTests.class,
//	NoSuchFileErrorTests.class,
	NoSuchFunctionErrorTests.class,
	NoSuchModuleErrorTests.class,
//	RunTimeErrorTests.class,
//	SubscriptErrorTests.class,
//	SyntaxErrorTests.class,
	TypeErrorTests.class,
//	UndefinedValueErrorTests.class,
//	UninitializedvariableErrorTests.class
        })

public class AllErrorTests extends TestFramework {

}
