module lang::rascalcore::compile::muRascal::interpret::Iterators

import List;
import Set;
import Map;
import Node;

import lang::rascalcore::check::AType;
import lang::rascalcore::compile::muRascal::interpret::RValue;
import lang::rascalcore::compile::muRascal::interpret::Env;

    
// Iterators for all Rascal data types

data Iterator
    = iterator(
        bool () hasNext,
        RValue () getNext
    );

Iterator makeIterator(list[&T] elems){
    int i = 0;
    bool hasNext() = i < size(elems);
    RValue getNext() { result = elems[i]; i += 1; return rvalue(result); }
    
    return iterator(hasNext, getNext);
}

Iterator makeIterator(bool b)
    = makeIterator(b ? [true] : []);

Iterator makeIterator(set[&T] elems) 
    = makeIterator(toList(elems));

Iterator makeIterator(map[&K,&V] m) 
    = makeIterator(toList(domain(m)));

Iterator makeIterator(node nd) 
    = makeIterator([x | x <- nd]);

Iterator makeIterator(tuple[&A,&B] tup) 
    = makeIterator([tup[0], tup[1]]);

Iterator makeIterator(tuple[&A,&B,&C] tup)
    = makeIterator([tup[0], tup[1], tup[2]]);

Iterator makeIterator(tuple[&A,&B,&C,&D] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3]]);

Iterator makeIterator(tuple[&A,&B,&C,&D,&E] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3], tup[4]]);

Iterator makeIterator(tuple[&A,&B,&C,&D,&E,&F] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3], tup[4], tup[5]]);
    
Iterator makeIterator(tuple[&A,&B,&C,&D,&E,&F,&G] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3], tup[4], tup[5], tup[6]]);

Iterator makeIterator(tuple[&A,&B,&C,&D,&E,&F,&G,&H] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3], tup[4], tup[5], tup[6], tup[7]]);

Iterator makeIterator(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3], tup[4], tup[5], tup[6], tup[7], tup[8]]);

Iterator makeIterator(tuple[&A,&B,&C,&D,&E,&F,&G,&H,&I,&J] tup)
    = makeIterator([tup[0], tup[1], tup[2], tup[3], tup[4], tup[5], tup[6], tup[7], tup[8], tup[9]]);