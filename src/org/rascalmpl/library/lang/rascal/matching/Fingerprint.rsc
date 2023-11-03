@license{
  Copyright (c) 2023 CWI
  All rights reserved. This program and the accompanying materials
  are made available under the terms of the Eclipse Public License v1.0
  which accompanies this distribution, and is available at
  http://www.eclipse.org/legal/epl-v10.html
}
@contributor{Jurgen Vinju - Jurgen.Vinju@cwi.nl}
@synopsis{Core functions for implementing fast pattern matching in the Rascal compiler.}
@description{
These functions tie together the run-time features of IValue and ITree for computing fast
fingerprints, with compile-time information for generating switch cases that uses these fingerprints.

There are several explicit contracts implemented here:
   * a fingerprint is (almost) never `0`. 
   * the fingerprint functions in this module implement exactly the fingerprinting of the run-time that the generated code will be linked against.
   This contract is tested with internal tests in this module.
   * `value matches pattern ==> fingerprint(pattern) == fingerprint(value)` such that a fingerprint is always an over-approximation of matching. It may
   never be the case that a value should match a pattern and the fingerprint contradicts this.
   This contract is tested by the pattern matching tests for the interpreter and the compiler.
   * fingerprints distinguish the identity of the outermost value construct as much as possible. I.e. production rules and constructors are 
   mapped to different codes as much as possible, without breaking the fingerprinting contract.
   This contract is not automatically tested. Performance regressions may be caused by accidental fingerprinting collisions. 
   * there is also an equals contract: `value1 equals value2 ==> fingerprint(value1) == fingerprint(value2)`, which is a collorary from the pattern
   matching contract if you consider that patterns may also be equality tests.

As you can read the computation of fingerprints reuses a lot of internal hashcodes. Mainly these boil down to the hash codes of:
* Java internal strings
* Java integers
* Vallang implementations of nested constructors for Symbol and Production.

And so when one of these hashCode implementations changes, the code below may _not_ break and _not_ fail any test
and still break the backward compatibility of all previously generated code. The tests in the vallang project try to
detect such an event by replicating the hashcode computations literally in some of the regression tests.
}
module lang::rascal::matching::Fingerprint

extend ParseTree;
import Node;
import List;

@javaClass{org.rascalmpl.library.lang.rascal.matching.internal.Fingerprint}
@synopsis{Compute the match fingerprint for any constant value. Only used for testing purposes.}
@description{
To decouple the Rascal compilers code generator from the bootstrapped run-time it is running in itself,
the fingerprinting computation is replicated in this module. However, the computation should be the 
same as this internalFingerprint function as long as nothing changes between compiler and run-time versions
in the computations for fingerprinting. 
}
private java int internalFingerprint(value x);

@javaClass{org.rascalmpl.library.lang.rascal.matching.internal.Fingerprint}
@synopsis{Compute the concrete match fingerprint for any parse `Tree`. Only used for testing purposes.}
@description{
To decouple the Rascal compilers code generator from the bootstrapped run-time it is running in itself,
the fingerprinting computation is replicated in this module. However, the computation should be the 
same as this internalFingerprint function as long as nothing changes between compiler and run-time versions
in the computations for fingerprinting. 
}
@javaClass{org.rascalmpl.library.lang.rascal.matching.internal.Fingerprint}
private java int internalConcreteFingerprint(Tree x);

@javaClass{org.rascalmpl.library.lang.rascal.matching.internal.Fingerprint}
@synopsis{Get the Object.hashCode() of the Java implementation of a Rascal value.}
@description{
This hash code is sometimes a part of computing a fingerprint. Do not make this function
public. Rascal values are hashed already and exactly these hashes are used internally by the
set, relation and map data-structures. There is no need to write Rascal programs that "hash 
on the hash", and it would leak implementation details that are very hard to encapsulate again.
}
private java int internalHashcode(value x);

@synopsis{Computes a unique fingerprint for each kind of tree based on the identity of the top-level tree node.}
@description{
Concrete fingerprint implements the pattern matching contract:
`value matches pattern ==> fingerprint(pattern) == fingerprint(value)`

For normal parse trees the fingerprint function makes sure that there are different integers if the
top-level production is different. This makes it possible to quickly switch on the outermost production rule
while pattern matching. 

To complete the function for the other kinds of trees, even though less important for efficiency, we also
implement a sensible encoding that follows the contract and tries to differentiate as much as possible between different values.
}
int concreteFingerprint(appl(Production p, list[Tree] _))                   = concreteFingerprint(p);
int concreteFingerprint(amb({appl(prod(Symbol s, _, _), list[Tree] _), _})) = internalHashcode("appl")  + 43 * internalHashcode(t)
    when label(_, Symbol t) := s || Symbol t := s;
int concreteFingerprint(char(int ch))                                       = internalHashcode("char")  + internalHashcode(ch);
int concreteFingerprint(cycle(Symbol s, int _))                             = internalHashcode("cycle") + 13 * internalHashcode(s);

@synopsis{Compute a fingerprint for a match pattern with this outermost production rule}
int concreteFingerprint(Production p) = 3568542 + 41 * internalHashcode(p);

@synopsis{These two implementations are intentional clones.}
test bool concreteFingerprintAlignment(Tree x) = concreteFingerprint(x) == internalConcreteFingerprint(x);

@synopsis{Computes a unique fingerprint for each kind of value based on the identity of the top-level kind.}
@description{
Fingerprint implements the pattern matching contract:
`value matches pattern ==> fingerprint(pattern) == fingerprint(value)`

Work is done to avoid generating the 0 fingerprint for simple values like empty strings and 0 integers, etc.
}
int fingerprint(str r)           = internalHashcode(r) == 0 ? internalHashcode("str")  : internalHashcode(r);
int fingerprint(int r)           = internalHashcode(r) == 0 ? internalHashcode("int")  : internalHashcode(r);
int fingerprint(real r)          = internalHashcode(r) == 0 ? internalHashcode("real") : internalHashcode(r);
int fingerprint(rat  r)          = internalHashcode(r) == 0 ? internalHashcode("rat")  : internalHashcode(r);
int fingerprint(value t)         = tupleFingerprint(size(fields)) when \tuple(list[Symbol] fields) := typeOf(t);
default int fingerprint(value n) = internalHashcode(n);

int fingerprint(node n)          = nodeFingerprint(getName(n), arity(n));

int fingerprint(list[value] l)       = listFingerprint();
int fingerprint(set[value] l)        = setFingerprint();
int fingerprint(map[value,value] l)  = mapFingerprint();


int         nodeFingerprint(""      , int arity) = internalHashcode("node") + 131 * arity;
default int nodeFingerprint(str name, int arity) = internalHashcode(name)   + 131 * arity;

int tupleFingerprint(int arity) = internalHashcode("tuple") + arity;
int listFingerprint()           = internalHashcode("list");
int setFingerprint()            = internalHashcode("set");
int mapFingerprint()            = internalHashcode("map");
int constructorFingerprint(str name, int arity) = nodeFingerprint(name, arity);

@synopsis{These two implementations are intentional clones.}
test bool fingerprintAlignment(value x) = fingerprint(x) == internalFingerprint(x);