module Grammar

imports ParseTree;

// A grammar is simply a set of productions
data Grammar = grammar(set[Symbol] start, set[Production] productions)

// Here we extend productions with basic combinators allowing to
// construct ordered and un-ordered compositions, and also a difference operator.
//
// The intended semantics are that 'or' means unordered choice,
// 'xor' means ordered choice, where alternatives are tried from left to right,
// 'diff' means all alternatives of the first argument are accepted, unless one
// of the alternatives from the right argument are accepted.
data Production = or(set[Production] alternatives)                  
                | xor(list[Production] alternatives)               
                | diff(Production language, set[Production] rejected);
                