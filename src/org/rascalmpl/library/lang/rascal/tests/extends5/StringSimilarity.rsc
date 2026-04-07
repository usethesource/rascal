module lang::rascal::tests::extends5::StringSimilarity

// but B is cyclic with A, so we stop the recursion
import lang::rascal::tests::extends5::ConfigurableScopeGraph;

// a use of Use which will fail with #2544
data X = x(Use u);