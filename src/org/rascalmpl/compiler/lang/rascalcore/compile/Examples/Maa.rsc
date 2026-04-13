@license{
Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
}
module lang::rascalcore::compile::Examples::Maa
import IO;

data Xbool = Xfalse() | Xtrue();

data Bit = X0() | X1();

data Octet = BuildOctet (Bit rec_x1_1, Bit rec_x2_1, Bit rec_x3_1, Bit rec_x4_1, Bit rec_x5_1, Bit rec_x6_1, Bit rec_x7_1, Bit rec_x8_1);

data OctetSum = BuildOctetSum (Bit rec_x1_1, Octet rec_x2_1);

data Half = BuildHalf (Octet rec_x1_1, Octet rec_x2_1);

data HalfSum = BuildHalfSum (Bit rec_x1_1, Half rec_x2_1);

data Block = BuildBlock (Octet rec_x1_1, Octet rec_x2_1, Octet rec_x3_1, Octet rec_x4_1);

data BlockSum = BuildBlockSum (Bit rec_x1_1, Block rec_x2_1);

data Pair = BuildPair (Block rec_x1_1, Block rec_x2_1);

data Nat = Zero() | Succ (Nat rec_x1_1);

data Key = BuildKey (Block rec_x1_1, Block rec_x2_1);

data Message = UnitMessage (Block rec_x1_1) | ConsMessage (Block rec_x1_2, Message rec_x2_2);

data SegmentedMessage = UnitSegment (Message rec_x1_1) | ConsSegment (Message rec_x1_2, SegmentedMessage rec_x2_2);

Xbool notBool (Xfalse()) = Xtrue();
Xbool notBool (Xtrue()) = Xfalse();

Xbool andBool (Xfalse(), Xbool l) = Xfalse();
Xbool andBool (Xtrue(), Xbool l) = l;

Xbool orBool (Xfalse(), Xbool l) = l;
Xbool orBool (Xtrue(), Xbool l) = Xtrue();

Xbool xorBool (Xfalse(), Xbool l) = l;
Xbool xorBool (Xtrue(), Xbool l) = notBool (l);

Bit notBit (X0()) = X1();
Bit notBit (X1()) = X0();

Bit andBit (Bit b, X0()) = X0();
Bit andBit (Bit b, X1()) = b;

Bit orBit (Bit b, X0()) = b;
Bit orBit (Bit b, X1()) = X1();

Bit xorBit (Bit b, X0()) = b;
Bit xorBit (Bit b, X1()) = notBit (b);

Octet x00() = BuildOctet (X0(), X0(), X0(), X0(), X0(), X0(), X0(), X0());

Octet x01() = BuildOctet (X0(), X0(), X0(), X0(), X0(), X0(), X0(), X1());

Half x0000() = BuildHalf (x00(), x00());

Half halfU (BuildBlock (Octet o1, Octet o2, Octet o3, Octet o4)) = BuildHalf (o1, o2);

Half halfL (BuildBlock (Octet o1, Octet o2, Octet o3, Octet o4)) = BuildHalf (o3, o4);

Xbool eqBit (X0(), X0()) = Xtrue();
Xbool eqBit (X0(), X1()) = Xfalse();
Xbool eqBit (X1(), X0()) = Xfalse();
Xbool eqBit (X1(), X1()) = Xtrue();

Xbool eqOctet (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8), BuildOctet (Bit b_prime1, Bit b_prime2, Bit b_prime3, Bit b_prime4, Bit b_prime5, Bit b_prime6, Bit b_prime7, Bit b_prime8)) = andBool (eqBit (b1, b_prime1), andBool (eqBit (b2, b_prime2), andBool (eqBit (b3, b_prime3), andBool (eqBit (b4, b_prime4), andBool (eqBit (b5, b_prime5), andBool (eqBit (b6, b_prime6), andBool (eqBit (b7, b_prime7), eqBit (b8, b_prime8))))))));

Xbool eqOctetSum (BuildOctetSum (Bit b, Octet xo), BuildOctetSum (Bit b_prime, Octet o_prime)) = andBool (eqBit (b, b_prime), eqOctet (xo, o_prime));

Xbool eqHalf (BuildHalf (Octet o1, Octet o2), BuildHalf (Octet o_prime1, Octet o_prime2)) = andBool (eqOctet (o1, o_prime1), eqOctet (o2, o_prime2));

Xbool eqHalfSum (BuildHalfSum (Bit b, Half h), BuildHalfSum (Bit b_prime, Half h_prime)) = andBool (eqBit (b, b_prime), eqHalf (h, h_prime));

Xbool eqBlock (BuildBlock (Octet o1, Octet o2, Octet o3, Octet o4), BuildBlock (Octet o_prime1, Octet o_prime2, Octet o_prime3, Octet o_prime4)) = andBool (andBool (eqOctet (o1, o_prime1), eqOctet (o2, o_prime2)), andBool (eqOctet (o3, o_prime3), eqOctet (o4, o_prime4)));

Xbool eqBlockSum (BuildBlockSum (Bit b, Block w), BuildBlockSum (Bit b_prime, Block w_prime)) = andBool (eqBit (b, b_prime), eqBlock (w, w_prime));

Xbool eqPair (BuildPair (Block w1, Block w2), BuildPair (Block w_prime1, Block w_prime2)) = andBool (eqBlock (w1, w_prime1), eqBlock (w2, w_prime2));

Bit addBit (Bit b, Bit b_prime, Bit bcarry) = xorBit (xorBit (b, b_prime), bcarry);

Bit carBit (Bit b, Bit b_prime, Bit bcarry) = orBit (andBit (andBit (b, b_prime), notBit (bcarry)), andBit (orBit (b, b_prime), bcarry));

OctetSum addOctetSum (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8), BuildOctet (Bit b_prime1, Bit b_prime2, Bit b_prime3, Bit b_prime4, Bit b_prime5, Bit b_prime6, Bit b_prime7, Bit b_prime8), Bit bcarry) = addOctet8 (b1, b_prime1, b2, b_prime2, b3, b_prime3, b4, b_prime4, b5, b_prime5, b6, b_prime6, b7, b_prime7, b8, b_prime8, bcarry);

OctetSum addOctet8 (Bit b1, Bit b_prime1, Bit b2, Bit b_prime2, Bit b3, Bit b_prime3, Bit b4, Bit b_prime4, Bit b5, Bit b_prime5, Bit b6, Bit b_prime6, Bit b7, Bit b_prime7, Bit b8, Bit b_prime8, Bit bcarry) = addOctet7 (b1, b_prime1, b2, b_prime2, b3, b_prime3, b4, b_prime4, b5, b_prime5, b6, b_prime6, b7, b_prime7, carBit (b8, b_prime8, bcarry), addBit (b8, b_prime8, bcarry));

OctetSum addOctet7 (Bit b1, Bit b_prime1, Bit b2, Bit b_prime2, Bit b3, Bit b_prime3, Bit b4, Bit b_prime4, Bit b5, Bit b_prime5, Bit b6, Bit b_prime6, Bit b7, Bit b_prime7, Bit bcarry, Bit b_second8) = addOctet6 (b1, b_prime1, b2, b_prime2, b3, b_prime3, b4, b_prime4, b5, b_prime5, b6, b_prime6, carBit (b7, b_prime7, bcarry), addBit (b7, b_prime7, bcarry), b_second8);

OctetSum addOctet6 (Bit b1, Bit b_prime1, Bit b2, Bit b_prime2, Bit b3, Bit b_prime3, Bit b4, Bit b_prime4, Bit b5, Bit b_prime5, Bit b6, Bit b_prime6, Bit bcarry, Bit b_second7, Bit b_second8) = addOctet5 (b1, b_prime1, b2, b_prime2, b3, b_prime3, b4, b_prime4, b5, b_prime5, carBit (b6, b_prime6, bcarry), addBit (b6, b_prime6, bcarry), b_second7, b_second8);

OctetSum addOctet5 (Bit b1, Bit b_prime1, Bit b2, Bit b_prime2, Bit b3, Bit b_prime3, Bit b4, Bit b_prime4, Bit b5, Bit b_prime5, Bit bcarry, Bit b_second6, Bit b_second7, Bit b_second8) = addOctet4 (b1, b_prime1, b2, b_prime2, b3, b_prime3, b4, b_prime4, carBit (b5, b_prime5, bcarry), addBit (b5, b_prime5, bcarry), b_second6, b_second7, b_second8);

OctetSum addOctet4 (Bit b1, Bit b_prime1, Bit b2, Bit b_prime2, Bit b3, Bit b_prime3, Bit b4, Bit b_prime4, Bit bcarry, Bit b_second5, Bit b_second6, Bit b_second7, Bit b_second8) = addOctet3 (b1, b_prime1, b2, b_prime2, b3, b_prime3, carBit (b4, b_prime4, bcarry), addBit (b4, b_prime4, bcarry), b_second5, b_second6, b_second7, b_second8);

OctetSum addOctet3 (Bit b1, Bit b_prime1, Bit b2, Bit b_prime2, Bit b3, Bit b_prime3, Bit bcarry, Bit b_second4, Bit b_second5, Bit b_second6, Bit b_second7, Bit b_second8) = addOctet2 (b1, b_prime1, b2, b_prime2, carBit (b3, b_prime3, bcarry), addBit (b3, b_prime3, bcarry), b_second4, b_second5, b_second6, b_second7, b_second8);

OctetSum addOctet2 (Bit b1, Bit b_prime1, Bit b2, Bit b_prime2, Bit bcarry, Bit b_second3, Bit b_second4, Bit b_second5, Bit b_second6, Bit b_second7, Bit b_second8) = addOctet1 (b1, b_prime1, carBit (b2, b_prime2, bcarry), addBit (b2, b_prime2, bcarry), b_second3, b_second4, b_second5, b_second6, b_second7, b_second8);

OctetSum addOctet1 (Bit b1, Bit b_prime1, Bit bcarry, Bit b_second2, Bit b_second3, Bit b_second4, Bit b_second5, Bit b_second6, Bit b_second7, Bit b_second8) = addOctet0 (carBit (b1, b_prime1, bcarry), addBit (b1, b_prime1, bcarry), b_second2, b_second3, b_second4, b_second5, b_second6, b_second7, b_second8);

OctetSum addOctet0 (Bit bcarry, Bit b_second1, Bit b_second2, Bit b_second3, Bit b_second4, Bit b_second5, Bit b_second6, Bit b_second7, Bit b_second8) = BuildOctetSum (bcarry, BuildOctet (b_second1, b_second2, b_second3, b_second4, b_second5, b_second6, b_second7, b_second8));

Octet dropCarryOctetSum (BuildOctetSum (Bit bcarry, Octet xo)) = xo;

Octet addOctet (Octet xo, Octet o_prime) = dropCarryOctetSum (addOctetSum (xo, o_prime, X0()));

HalfSum addHalfSum (BuildHalf (Octet o1, Octet o2), BuildHalf (Octet o_prime1, Octet o_prime2)) = addHalf2 (o1, o_prime1, o2, o_prime2);

HalfSum addHalf2 (Octet o1, Octet o_prime1, Octet o2, Octet o_prime2) = addHalf1 (o1, o_prime1, addOctetSum (o2, o_prime2, X0()));

HalfSum addHalf1 (Octet o1, Octet o_prime1, BuildOctetSum (Bit b, Octet o_second2)) = addHalf0 (addOctetSum (o1, o_prime1, b), o_second2);

HalfSum addHalf0 (BuildOctetSum (Bit b, Octet o_second1), Octet o_second2) = BuildHalfSum (b, BuildHalf (o_second1, o_second2));

Half dropCarryHalfSum (BuildHalfSum (Bit b, Half h)) = h;

Half addHalf (Half h, Half h_prime) = dropCarryHalfSum (addHalfSum (h, h_prime));

Half addHalfOctet (Octet xo, Half h) = addHalf (BuildHalf (x00(), xo), h);

Half addHalfOctets (Octet xo, Octet o_prime) = addHalf (BuildHalf (x00(), xo), BuildHalf (x00(), o_prime));

BlockSum addBlockSum (BuildBlock (Octet o1, Octet o2, Octet o3, Octet o4), BuildBlock (Octet o_prime1, Octet o_prime2, Octet o_prime3, Octet o_prime4)) = addBlock4 (o1, o_prime1, o2, o_prime2, o3, o_prime3, o4, o_prime4);

BlockSum addBlock4 (Octet o1, Octet o_prime1, Octet o2, Octet o_prime2, Octet o3, Octet o_prime3, Octet o4, Octet o_prime4) = addBlock3 (o1, o_prime1, o2, o_prime2, o3, o_prime3, addOctetSum (o4, o_prime4, X0()));

BlockSum addBlock3 (Octet o1, Octet o_prime1, Octet o2, Octet o_prime2, Octet o3, Octet o_prime3, BuildOctetSum (Bit bcarry, Octet o_second4)) = addBlock2 (o1, o_prime1, o2, o_prime2, addOctetSum (o3, o_prime3, bcarry), o_second4);

BlockSum addBlock2 (Octet o1, Octet o_prime1, Octet o2, Octet o_prime2, BuildOctetSum (Bit bcarry, Octet o_second3), Octet o_second4) = addBlock1 (o1, o_prime1, addOctetSum (o2, o_prime2, bcarry), o_second3, o_second4);

BlockSum addBlock1 (Octet o1, Octet o_prime1, BuildOctetSum (Bit bcarry, Octet o_second2), Octet o_second3, Octet o_second4) = addBlock0 (addOctetSum (o1, o_prime1, bcarry), o_second2, o_second3, o_second4);

BlockSum addBlock0 (BuildOctetSum (Bit bcarry, Octet o_second1), Octet o_second2, Octet o_second3, Octet o_second4) = BuildBlockSum (bcarry, BuildBlock (o_second1, o_second2, o_second3, o_second4));

Block dropCarryBlockSum (BuildBlockSum (Bit bcarry, Block w)) = w;

Block addBlock (Block w, Block w_prime) = dropCarryBlockSum (addBlockSum (w, w_prime));

Block addBlockHalf (BuildHalf (Octet o1, Octet o2), Block w) = addBlock (BuildBlock (x00(), x00(), o1, o2), w);

Block addBlockHalves (BuildHalf (Octet o1, Octet o2), BuildHalf (Octet o_prime1, Octet o_prime2)) = addBlock (BuildBlock (x00(), x00(), o1, o2), BuildBlock (x00(), x00(), o_prime1, o_prime2));

Octet leftOctet1 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (b2, b3, b4, b5, b6, b7, b8, X0());

Octet leftOctet2 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (b3, b4, b5, b6, b7, b8, X0(), X0());

Octet leftOctet3 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (b4, b5, b6, b7, b8, X0(), X0(), X0());

Octet leftOctet4 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (b5, b6, b7, b8, X0(), X0(), X0(), X0());

Octet leftOctet5 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (b6, b7, b8, X0(), X0(), X0(), X0(), X0());

Octet leftOctet6 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (b7, b8, X0(), X0(), X0(), X0(), X0(), X0());

Octet leftOctet7 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (b8, X0(), X0(), X0(), X0(), X0(), X0(), X0());

Octet rightOctet1 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (X0(), b1, b2, b3, b4, b5, b6, b7);

Octet rightOctet2 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (X0(), X0(), b1, b2, b3, b4, b5, b6);

Octet rightOctet3 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (X0(), X0(), X0(), b1, b2, b3, b4, b5);

Octet rightOctet4 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (X0(), X0(), X0(), X0(), b1, b2, b3, b4);

Octet rightOctet5 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (X0(), X0(), X0(), X0(), X0(), b1, b2, b3);

Octet rightOctet6 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (X0(), X0(), X0(), X0(), X0(), X0(), b1, b2);

Octet rightOctet7 (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8)) = BuildOctet (X0(), X0(), X0(), X0(), X0(), X0(), X0(), b1);

Half mulOctet (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8), Octet o_prime) = mulOctet1 (b1, b2, b3, b4, b5, b6, b7, b8, o_prime, x0000());

Half mulOctet1 (X0(), Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8, Octet o_prime, Half h) = mulOctet2 (b2, b3, b4, b5, b6, b7, b8, o_prime, h);
Half mulOctet1 (X1(), Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8, Octet o_prime, Half h) = mulOctet2 (b2, b3, b4, b5, b6, b7, b8, o_prime, mulOctetA (h, rightOctet1 (o_prime), leftOctet7 (o_prime)));

Half mulOctet2 (X0(), Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8, Octet o_prime, Half h) = mulOctet3 (b3, b4, b5, b6, b7, b8, o_prime, h);
Half mulOctet2 (X1(), Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8, Octet o_prime, Half h) = mulOctet3 (b3, b4, b5, b6, b7, b8, o_prime, mulOctetA (h, rightOctet2 (o_prime), leftOctet6 (o_prime)));

Half mulOctet3 (X0(), Bit b4, Bit b5, Bit b6, Bit b7, Bit b8, Octet o_prime, Half h) = mulOctet4 (b4, b5, b6, b7, b8, o_prime, h);
Half mulOctet3 (X1(), Bit b4, Bit b5, Bit b6, Bit b7, Bit b8, Octet o_prime, Half h) = mulOctet4 (b4, b5, b6, b7, b8, o_prime, mulOctetA (h, rightOctet3 (o_prime), leftOctet5 (o_prime)));

Half mulOctet4 (X0(), Bit b5, Bit b6, Bit b7, Bit b8, Octet o_prime, Half h) = mulOctet5 (b5, b6, b7, b8, o_prime, h);
Half mulOctet4 (X1(), Bit b5, Bit b6, Bit b7, Bit b8, Octet o_prime, Half h) = mulOctet5 (b5, b6, b7, b8, o_prime, mulOctetA (h, rightOctet4 (o_prime), leftOctet4 (o_prime)));

Half mulOctet5 (X0(), Bit b6, Bit b7, Bit b8, Octet o_prime, Half h) = mulOctet6 (b6, b7, b8, o_prime, h);
Half mulOctet5 (X1(), Bit b6, Bit b7, Bit b8, Octet o_prime, Half h) = mulOctet6 (b6, b7, b8, o_prime, mulOctetA (h, rightOctet5 (o_prime), leftOctet3 (o_prime)));

Half mulOctet6 (X0(), Bit b7, Bit b8, Octet o_prime, Half h) = mulOctet7 (b7, b8, o_prime, h);
Half mulOctet6 (X1(), Bit b7, Bit b8, Octet o_prime, Half h) = mulOctet7 (b7, b8, o_prime, mulOctetA (h, rightOctet6 (o_prime), leftOctet2 (o_prime)));

Half mulOctet7 (X0(), Bit b8, Octet o_prime, Half h) = mulOctet8 (b8, o_prime, h);
Half mulOctet7 (X1(), Bit b8, Octet o_prime, Half h) = mulOctet8 (b8, o_prime, mulOctetA (h, rightOctet7 (o_prime), leftOctet1 (o_prime)));

Half mulOctet8 (X0(), Octet o_prime, Half h) = h;
Half mulOctet8 (X1(), Octet o_prime, Half h) = mulOctetA (h, x00(), o_prime);

Half mulOctetA (BuildHalf (Octet o1, Octet o2), Octet o_prime1, Octet o_prime2) = mulOctetB (addOctet (o1, o_prime1), addOctetSum (o2, o_prime2, X0()));

Half mulOctetB (Octet o1, BuildOctetSum (X0(), Octet o2)) = BuildHalf (o1, o2);
Half mulOctetB (Octet o1, BuildOctetSum (X1(), Octet o2)) = BuildHalf (addOctet (o1, x01()), o2);

Block mulHalf (BuildHalf (Octet o1, Octet o2), BuildHalf (Octet o_prime1, Octet o_prime2)) = mulHalfA (mulOctet (o1, o_prime1), mulOctet (o1, o_prime2), mulOctet (o2, o_prime1), mulOctet (o2, o_prime2));

Block mulHalfA (BuildHalf (Octet o11U, Octet o11L), BuildHalf (Octet o12U, Octet o12L), BuildHalf (Octet o21U, Octet o21L), BuildHalf (Octet o22U, Octet o22L)) = mulHalf4 (o11U, o11L, o12U, o12L, o21U, o21L, o22U, o22L);

Block mulHalf4 (Octet o11U, Octet o11L, Octet o12U, Octet o12L, Octet o21U, Octet o21L, Octet o22U, Octet o_second4) = mulHalf3 (o11U, o11L, o12U, o21U, addHalfOctet (o12L, addHalfOctets (o21L, o22U)), o_second4);

Block mulHalf3 (Octet o11U, Octet o11L, Octet o12U, Octet o21U, BuildHalf (Octet ocarry, Octet o_second3), Octet o_second4) = mulHalf2 (o11U, addHalfOctet (ocarry, addHalfOctet (o11L, addHalfOctets (o12U, o21U))), o_second3, o_second4);

Block mulHalf2 (Octet o11U, BuildHalf (Octet ocarry, Octet o_second2), Octet o_second3, Octet o_second4) = mulHalf1 (addHalfOctets (ocarry, o11U), o_second2, o_second3, o_second4);

Block mulHalf1 (BuildHalf (Octet ocarry, Octet o_second1), Octet o_second2, Octet o_second3, Octet o_second4) = BuildBlock (o_second1, o_second2, o_second3, o_second4);

Pair mulBlock (Block w1, Block w2) = mulBlockA (mulHalf (halfU (w1), halfU (w2)), mulHalf (halfU (w1), halfL (w2)), mulHalf (halfL (w1), halfU (w2)), mulHalf (halfL (w1), halfL (w2)));

Pair mulBlockA (Block w11, Block w12, Block w21, Block w22) = mulBlock4 (halfU (w11), halfL (w11), halfU (w12), halfL (w12), halfU (w21), halfL (w21), halfU (w22), halfL (w22));

Pair mulBlock4 (Half h11U, Half h11L, Half h12U, Half h12L, Half h21U, Half h21L, Half h22U, Half h_second4) = mulBlock3 (h11U, h11L, h12U, h21U, addBlockHalf (h12L, addBlockHalves (h21L, h22U)), h_second4);

Pair mulBlock3 (Half h11U, Half h11L, Half h12U, Half h21U, Block w, Half h_second4) = mulBlock2 (h11U, addBlockHalf (halfU (w), addBlockHalf (h11L, addBlockHalves (h12U, h21U))), halfL (w), h_second4);

Pair mulBlock2 (Half h11U, Block w, Half h_second3, Half h_second4) = mulBlock1 (addBlockHalves (halfU (w), h11U), halfL (w), h_second3, h_second4);

Pair mulBlock1 (Block w, Half h_second2, Half h_second3, Half h_second4) = mulBlockB (halfL (w), h_second2, h_second3, h_second4);

Pair mulBlockB (BuildHalf (Octet o1U, Octet o1L), BuildHalf (Octet o2U, Octet o2L), BuildHalf (Octet o3U, Octet o3L), BuildHalf (Octet o4U, Octet o4L)) = BuildPair (BuildBlock (o1U, o1L, o2U, o2L), BuildBlock (o3U, o3L, o4U, o4L));

Nat addNat (Nat n, Zero()) = n;
Nat addNat (Nat n, Succ (Nat n_prime)) = addNat (Succ (n), n_prime);

Nat multNat (Nat n, Zero()) = Zero();
Nat multNat (Nat n, Succ (Nat n_prime)) = addNat (n, multNat (n, n_prime));

Xbool eqNat (Zero(), Zero()) = Xtrue();
Xbool eqNat (Zero(), Succ (Nat n_prime)) = Xfalse();
Xbool eqNat (Succ (Nat n), Zero()) = Xfalse();
Xbool eqNat (Succ (Nat n), Succ (Nat n_prime)) = eqNat (n, n_prime);

Xbool ltNat (Zero(), Zero()) = Xfalse();
Xbool ltNat (Zero(), Succ (Nat n_prime)) = Xtrue();
Xbool ltNat (Succ (Nat n_prime), Zero()) = Xfalse();
Xbool ltNat (Succ (Nat n), Succ (Nat n_prime)) = ltNat (n, n_prime);

Nat n1() = Succ (Zero());

Nat n2() = Succ (n1());

Nat n3() = Succ (n2());

Nat n4() = Succ (n3());

Nat n5() = Succ (n4());

Nat n6() = Succ (n5());

Nat n7() = Succ (n6());

Nat n8() = Succ (n7());

Nat n9() = Succ (n8());

Nat n10() = Succ (n9());

Nat n11() = Succ (n10());

Nat n12() = Succ (n11());

Nat n13() = Succ (n12());

Nat n14() = Succ (n13());

Nat n15() = Succ (n14());

Nat n16() = Succ (n15());

Nat n17() = Succ (n16());

Nat n18() = Succ (n17());

Nat n19() = Succ (n18());

Nat n20() = Succ (n19());

Nat n21() = Succ (n20());

Nat n22() = Succ (n21());

Nat n254() = addNat (n12(), multNat (n11(), n22()));

Nat n256() = multNat (n16(), n16());

Nat n4100() = addNat (n4(), multNat (n16(), n256()));

Octet andOctet (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8), BuildOctet (Bit b_prime1, Bit b_prime2, Bit b_prime3, Bit b_prime4, Bit b_prime5, Bit b_prime6, Bit b_prime7, Bit b_prime8)) = BuildOctet (andBit (b1, b_prime1), andBit (b2, b_prime2), andBit (b3, b_prime3), andBit (b4, b_prime4), andBit (b5, b_prime5), andBit (b6, b_prime6), andBit (b7, b_prime7), andBit (b8, b_prime8));

Octet orOctet (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8), BuildOctet (Bit b_prime1, Bit b_prime2, Bit b_prime3, Bit b_prime4, Bit b_prime5, Bit b_prime6, Bit b_prime7, Bit b_prime8)) = BuildOctet (orBit (b1, b_prime1), orBit (b2, b_prime2), orBit (b3, b_prime3), orBit (b4, b_prime4), orBit (b5, b_prime5), orBit (b6, b_prime6), orBit (b7, b_prime7), orBit (b8, b_prime8));

Octet xorOctet (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8), BuildOctet (Bit b_prime1, Bit b_prime2, Bit b_prime3, Bit b_prime4, Bit b_prime5, Bit b_prime6, Bit b_prime7, Bit b_prime8)) = BuildOctet (xorBit (b1, b_prime1), xorBit (b2, b_prime2), xorBit (b3, b_prime3), xorBit (b4, b_prime4), xorBit (b5, b_prime5), xorBit (b6, b_prime6), xorBit (b7, b_prime7), xorBit (b8, b_prime8));

Octet x02() = BuildOctet (X0(), X0(), X0(), X0(), X0(), X0(), X1(), X0());

Octet x03() = BuildOctet (X0(), X0(), X0(), X0(), X0(), X0(), X1(), X1());

Octet x04() = BuildOctet (X0(), X0(), X0(), X0(), X0(), X1(), X0(), X0());

Octet x05() = BuildOctet (X0(), X0(), X0(), X0(), X0(), X1(), X0(), X1());

Octet x06() = BuildOctet (X0(), X0(), X0(), X0(), X0(), X1(), X1(), X0());

Octet x07() = BuildOctet (X0(), X0(), X0(), X0(), X0(), X1(), X1(), X1());

Octet x08() = BuildOctet (X0(), X0(), X0(), X0(), X1(), X0(), X0(), X0());

Octet x09() = BuildOctet (X0(), X0(), X0(), X0(), X1(), X0(), X0(), X1());

Octet x0A() = BuildOctet (X0(), X0(), X0(), X0(), X1(), X0(), X1(), X0());

Octet x0B() = BuildOctet (X0(), X0(), X0(), X0(), X1(), X0(), X1(), X1());

Octet x0C() = BuildOctet (X0(), X0(), X0(), X0(), X1(), X1(), X0(), X0());

Octet x0D() = BuildOctet (X0(), X0(), X0(), X0(), X1(), X1(), X0(), X1());

Octet x0E() = BuildOctet (X0(), X0(), X0(), X0(), X1(), X1(), X1(), X0());

Octet x0F() = BuildOctet (X0(), X0(), X0(), X0(), X1(), X1(), X1(), X1());

Octet x10() = BuildOctet (X0(), X0(), X0(), X1(), X0(), X0(), X0(), X0());

Octet x11() = BuildOctet (X0(), X0(), X0(), X1(), X0(), X0(), X0(), X1());

Octet x12() = BuildOctet (X0(), X0(), X0(), X1(), X0(), X0(), X1(), X0());

Octet x13() = BuildOctet (X0(), X0(), X0(), X1(), X0(), X0(), X1(), X1());

Octet x14() = BuildOctet (X0(), X0(), X0(), X1(), X0(), X1(), X0(), X0());

Octet x15() = BuildOctet (X0(), X0(), X0(), X1(), X0(), X1(), X0(), X1());

Octet x16() = BuildOctet (X0(), X0(), X0(), X1(), X0(), X1(), X1(), X0());

Octet x17() = BuildOctet (X0(), X0(), X0(), X1(), X0(), X1(), X1(), X1());

Octet x18() = BuildOctet (X0(), X0(), X0(), X1(), X1(), X0(), X0(), X0());

Octet x1A() = BuildOctet (X0(), X0(), X0(), X1(), X1(), X0(), X1(), X0());

Octet x1B() = BuildOctet (X0(), X0(), X0(), X1(), X1(), X0(), X1(), X1());

Octet x1C() = BuildOctet (X0(), X0(), X0(), X1(), X1(), X1(), X0(), X0());

Octet x1D() = BuildOctet (X0(), X0(), X0(), X1(), X1(), X1(), X0(), X1());

Octet x1E() = BuildOctet (X0(), X0(), X0(), X1(), X1(), X1(), X1(), X0());

Octet x1F() = BuildOctet (X0(), X0(), X0(), X1(), X1(), X1(), X1(), X1());

Octet x20() = BuildOctet (X0(), X0(), X1(), X0(), X0(), X0(), X0(), X0());

Octet x21() = BuildOctet (X0(), X0(), X1(), X0(), X0(), X0(), X0(), X1());

Octet x23() = BuildOctet (X0(), X0(), X1(), X0(), X0(), X0(), X1(), X1());

Octet x24() = BuildOctet (X0(), X0(), X1(), X0(), X0(), X1(), X0(), X0());

Octet x25() = BuildOctet (X0(), X0(), X1(), X0(), X0(), X1(), X0(), X1());

Octet x26() = BuildOctet (X0(), X0(), X1(), X0(), X0(), X1(), X1(), X0());

Octet x27() = BuildOctet (X0(), X0(), X1(), X0(), X0(), X1(), X1(), X1());

Octet x28() = BuildOctet (X0(), X0(), X1(), X0(), X1(), X0(), X0(), X0());

Octet x29() = BuildOctet (X0(), X0(), X1(), X0(), X1(), X0(), X0(), X1());

Octet x2A() = BuildOctet (X0(), X0(), X1(), X0(), X1(), X0(), X1(), X0());

Octet x2B() = BuildOctet (X0(), X0(), X1(), X0(), X1(), X0(), X1(), X1());

Octet x2D() = BuildOctet (X0(), X0(), X1(), X0(), X1(), X1(), X0(), X1());

Octet x2E() = BuildOctet (X0(), X0(), X1(), X0(), X1(), X1(), X1(), X0());

Octet x2F() = BuildOctet (X0(), X0(), X1(), X0(), X1(), X1(), X1(), X1());

Octet x30() = BuildOctet (X0(), X0(), X1(), X1(), X0(), X0(), X0(), X0());

Octet x31() = BuildOctet (X0(), X0(), X1(), X1(), X0(), X0(), X0(), X1());

Octet x32() = BuildOctet (X0(), X0(), X1(), X1(), X0(), X0(), X1(), X0());

Octet x33() = BuildOctet (X0(), X0(), X1(), X1(), X0(), X0(), X1(), X1());

Octet x34() = BuildOctet (X0(), X0(), X1(), X1(), X0(), X1(), X0(), X0());

Octet x35() = BuildOctet (X0(), X0(), X1(), X1(), X0(), X1(), X0(), X1());

Octet x36() = BuildOctet (X0(), X0(), X1(), X1(), X0(), X1(), X1(), X0());

Octet x37() = BuildOctet (X0(), X0(), X1(), X1(), X0(), X1(), X1(), X1());

Octet x38() = BuildOctet (X0(), X0(), X1(), X1(), X1(), X0(), X0(), X0());

Octet x39() = BuildOctet (X0(), X0(), X1(), X1(), X1(), X0(), X0(), X1());

Octet x3A() = BuildOctet (X0(), X0(), X1(), X1(), X1(), X0(), X1(), X0());

Octet x3B() = BuildOctet (X0(), X0(), X1(), X1(), X1(), X0(), X1(), X1());

Octet x3D() = BuildOctet (X0(), X0(), X1(), X1(), X1(), X1(), X0(), X1());

Octet x3C() = BuildOctet (X0(), X0(), X1(), X1(), X1(), X1(), X0(), X0());

Octet x3F() = BuildOctet (X0(), X0(), X1(), X1(), X1(), X1(), X1(), X1());

Octet x40() = BuildOctet (X0(), X1(), X0(), X0(), X0(), X0(), X0(), X0());

Octet x46() = BuildOctet (X0(), X1(), X0(), X0(), X0(), X1(), X1(), X0());

Octet x48() = BuildOctet (X0(), X1(), X0(), X0(), X1(), X0(), X0(), X0());

Octet x49() = BuildOctet (X0(), X1(), X0(), X0(), X1(), X0(), X0(), X1());

Octet x4A() = BuildOctet (X0(), X1(), X0(), X0(), X1(), X0(), X1(), X0());

Octet x4B() = BuildOctet (X0(), X1(), X0(), X0(), X1(), X0(), X1(), X1());

Octet x4C() = BuildOctet (X0(), X1(), X0(), X0(), X1(), X1(), X0(), X0());

Octet x4D() = BuildOctet (X0(), X1(), X0(), X0(), X1(), X1(), X0(), X1());

Octet x4E() = BuildOctet (X0(), X1(), X0(), X0(), X1(), X1(), X1(), X0());

Octet x4F() = BuildOctet (X0(), X1(), X0(), X0(), X1(), X1(), X1(), X1());

Octet x50() = BuildOctet (X0(), X1(), X0(), X1(), X0(), X0(), X0(), X0());

Octet x51() = BuildOctet (X0(), X1(), X0(), X1(), X0(), X0(), X0(), X1());

Octet x53() = BuildOctet (X0(), X1(), X0(), X1(), X0(), X0(), X1(), X1());

Octet x54() = BuildOctet (X0(), X1(), X0(), X1(), X0(), X1(), X0(), X0());

Octet x55() = BuildOctet (X0(), X1(), X0(), X1(), X0(), X1(), X0(), X1());

Octet x58() = BuildOctet (X0(), X1(), X0(), X1(), X1(), X0(), X0(), X0());

Octet x5A() = BuildOctet (X0(), X1(), X0(), X1(), X1(), X0(), X1(), X0());

Octet x5B() = BuildOctet (X0(), X1(), X0(), X1(), X1(), X0(), X1(), X1());

Octet x5C() = BuildOctet (X0(), X1(), X0(), X1(), X1(), X1(), X0(), X0());

Octet x5D() = BuildOctet (X0(), X1(), X0(), X1(), X1(), X1(), X0(), X1());

Octet x5E() = BuildOctet (X0(), X1(), X0(), X1(), X1(), X1(), X1(), X0());

Octet x5F() = BuildOctet (X0(), X1(), X0(), X1(), X1(), X1(), X1(), X1());

Octet x60() = BuildOctet (X0(), X1(), X1(), X0(), X0(), X0(), X0(), X0());

Octet x61() = BuildOctet (X0(), X1(), X1(), X0(), X0(), X0(), X0(), X1());

Octet x62() = BuildOctet (X0(), X1(), X1(), X0(), X0(), X0(), X1(), X0());

Octet x63() = BuildOctet (X0(), X1(), X1(), X0(), X0(), X0(), X1(), X1());

Octet x64() = BuildOctet (X0(), X1(), X1(), X0(), X0(), X1(), X0(), X0());

Octet x65() = BuildOctet (X0(), X1(), X1(), X0(), X0(), X1(), X0(), X1());

Octet x66() = BuildOctet (X0(), X1(), X1(), X0(), X0(), X1(), X1(), X0());

Octet x67() = BuildOctet (X0(), X1(), X1(), X0(), X0(), X1(), X1(), X1());

Octet x69() = BuildOctet (X0(), X1(), X1(), X0(), X1(), X0(), X0(), X1());

Octet x6A() = BuildOctet (X0(), X1(), X1(), X0(), X1(), X0(), X1(), X0());

Octet x6B() = BuildOctet (X0(), X1(), X1(), X0(), X1(), X0(), X1(), X1());

Octet x6C() = BuildOctet (X0(), X1(), X1(), X0(), X1(), X1(), X0(), X0());

Octet x6D() = BuildOctet (X0(), X1(), X1(), X0(), X1(), X1(), X0(), X1());

Octet x6E() = BuildOctet (X0(), X1(), X1(), X0(), X1(), X1(), X1(), X0());

Octet x6F() = BuildOctet (X0(), X1(), X1(), X0(), X1(), X1(), X1(), X1());

Octet x70() = BuildOctet (X0(), X1(), X1(), X1(), X0(), X0(), X0(), X0());

Octet x71() = BuildOctet (X0(), X1(), X1(), X1(), X0(), X0(), X0(), X1());

Octet x72() = BuildOctet (X0(), X1(), X1(), X1(), X0(), X0(), X1(), X0());

Octet x73() = BuildOctet (X0(), X1(), X1(), X1(), X0(), X0(), X1(), X1());

Octet x74() = BuildOctet (X0(), X1(), X1(), X1(), X0(), X1(), X0(), X0());

Octet x75() = BuildOctet (X0(), X1(), X1(), X1(), X0(), X1(), X0(), X1());

Octet x76() = BuildOctet (X0(), X1(), X1(), X1(), X0(), X1(), X1(), X0());

Octet x77() = BuildOctet (X0(), X1(), X1(), X1(), X0(), X1(), X1(), X1());

Octet x78() = BuildOctet (X0(), X1(), X1(), X1(), X1(), X0(), X0(), X0());

Octet x79() = BuildOctet (X0(), X1(), X1(), X1(), X1(), X0(), X0(), X1());

Octet x7A() = BuildOctet (X0(), X1(), X1(), X1(), X1(), X0(), X1(), X0());

Octet x7B() = BuildOctet (X0(), X1(), X1(), X1(), X1(), X0(), X1(), X1());

Octet x7C() = BuildOctet (X0(), X1(), X1(), X1(), X1(), X1(), X0(), X0());

Octet x7D() = BuildOctet (X0(), X1(), X1(), X1(), X1(), X1(), X0(), X1());

Octet x7E() = BuildOctet (X0(), X1(), X1(), X1(), X1(), X1(), X1(), X0());

Octet x7F() = BuildOctet (X0(), X1(), X1(), X1(), X1(), X1(), X1(), X1());

Octet x80() = BuildOctet (X1(), X0(), X0(), X0(), X0(), X0(), X0(), X0());

Octet x81() = BuildOctet (X1(), X0(), X0(), X0(), X0(), X0(), X0(), X1());

Octet x83() = BuildOctet (X1(), X0(), X0(), X0(), X0(), X0(), X1(), X1());

Octet x84() = BuildOctet (X1(), X0(), X0(), X0(), X0(), X1(), X0(), X0());

Octet x85() = BuildOctet (X1(), X0(), X0(), X0(), X0(), X1(), X0(), X1());

Octet x86() = BuildOctet (X1(), X0(), X0(), X0(), X0(), X1(), X1(), X0());

Octet x88() = BuildOctet (X1(), X0(), X0(), X0(), X1(), X0(), X0(), X0());

Octet x89() = BuildOctet (X1(), X0(), X0(), X0(), X1(), X0(), X0(), X1());

Octet x8A() = BuildOctet (X1(), X0(), X0(), X0(), X1(), X0(), X1(), X0());

Octet x8C() = BuildOctet (X1(), X0(), X0(), X0(), X1(), X1(), X0(), X0());

Octet x8D() = BuildOctet (X1(), X0(), X0(), X0(), X1(), X1(), X0(), X1());

Octet x8E() = BuildOctet (X1(), X0(), X0(), X0(), X1(), X1(), X1(), X0());

Octet x8F() = BuildOctet (X1(), X0(), X0(), X0(), X1(), X1(), X1(), X1());

Octet x90() = BuildOctet (X1(), X0(), X0(), X1(), X0(), X0(), X0(), X0());

Octet x91() = BuildOctet (X1(), X0(), X0(), X1(), X0(), X0(), X0(), X1());

Octet x92() = BuildOctet (X1(), X0(), X0(), X1(), X0(), X0(), X1(), X0());

Octet x93() = BuildOctet (X1(), X0(), X0(), X1(), X0(), X0(), X1(), X1());

Octet x95() = BuildOctet (X1(), X0(), X0(), X1(), X0(), X1(), X0(), X1());

Octet x96() = BuildOctet (X1(), X0(), X0(), X1(), X0(), X1(), X1(), X0());

Octet x97() = BuildOctet (X1(), X0(), X0(), X1(), X0(), X1(), X1(), X1());

Octet x98() = BuildOctet (X1(), X0(), X0(), X1(), X1(), X0(), X0(), X0());

Octet x99() = BuildOctet (X1(), X0(), X0(), X1(), X1(), X0(), X0(), X1());

Octet x9A() = BuildOctet (X1(), X0(), X0(), X1(), X1(), X0(), X1(), X0());

Octet x9B() = BuildOctet (X1(), X0(), X0(), X1(), X1(), X0(), X1(), X1());

Octet x9C() = BuildOctet (X1(), X0(), X0(), X1(), X1(), X1(), X0(), X0());

Octet x9D() = BuildOctet (X1(), X0(), X0(), X1(), X1(), X1(), X0(), X1());

Octet x9E() = BuildOctet (X1(), X0(), X0(), X1(), X1(), X1(), X1(), X0());

Octet x9F() = BuildOctet (X1(), X0(), X0(), X1(), X1(), X1(), X1(), X1());

Octet xA1() = BuildOctet (X1(), X0(), X1(), X0(), X0(), X0(), X0(), X1());

Octet xA0() = BuildOctet (X1(), X0(), X1(), X0(), X0(), X0(), X0(), X0());

Octet xA2() = BuildOctet (X1(), X0(), X1(), X0(), X0(), X0(), X1(), X0());

Octet xA3() = BuildOctet (X1(), X0(), X1(), X0(), X0(), X0(), X1(), X1());

Octet xA4() = BuildOctet (X1(), X0(), X1(), X0(), X0(), X1(), X0(), X0());

Octet xA5() = BuildOctet (X1(), X0(), X1(), X0(), X0(), X1(), X0(), X1());

Octet xA6() = BuildOctet (X1(), X0(), X1(), X0(), X0(), X1(), X1(), X0());

Octet xA7() = BuildOctet (X1(), X0(), X1(), X0(), X0(), X1(), X1(), X1());

Octet xA8() = BuildOctet (X1(), X0(), X1(), X0(), X1(), X0(), X0(), X0());

Octet xA9() = BuildOctet (X1(), X0(), X1(), X0(), X1(), X0(), X0(), X1());

Octet xAA() = BuildOctet (X1(), X0(), X1(), X0(), X1(), X0(), X1(), X0());

Octet xAB() = BuildOctet (X1(), X0(), X1(), X0(), X1(), X0(), X1(), X1());

Octet xAC() = BuildOctet (X1(), X0(), X1(), X0(), X1(), X1(), X0(), X0());

Octet xAE() = BuildOctet (X1(), X0(), X1(), X0(), X1(), X1(), X1(), X0());

Octet xAF() = BuildOctet (X1(), X0(), X1(), X0(), X1(), X1(), X1(), X1());

Octet xB0() = BuildOctet (X1(), X0(), X1(), X1(), X0(), X0(), X0(), X0());

Octet xB1() = BuildOctet (X1(), X0(), X1(), X1(), X0(), X0(), X0(), X1());

Octet xB2() = BuildOctet (X1(), X0(), X1(), X1(), X0(), X0(), X1(), X0());

Octet xB3() = BuildOctet (X1(), X0(), X1(), X1(), X0(), X0(), X1(), X1());

Octet xB5() = BuildOctet (X1(), X0(), X1(), X1(), X0(), X1(), X0(), X1());

Octet xB6() = BuildOctet (X1(), X0(), X1(), X1(), X0(), X1(), X1(), X0());

Octet xB8() = BuildOctet (X1(), X0(), X1(), X1(), X1(), X0(), X0(), X0());

Octet xB9() = BuildOctet (X1(), X0(), X1(), X1(), X1(), X0(), X0(), X1());

Octet xBA() = BuildOctet (X1(), X0(), X1(), X1(), X1(), X0(), X1(), X0());

Octet xBB() = BuildOctet (X1(), X0(), X1(), X1(), X1(), X0(), X1(), X1());

Octet xBC() = BuildOctet (X1(), X0(), X1(), X1(), X1(), X1(), X0(), X0());

Octet xBE() = BuildOctet (X1(), X0(), X1(), X1(), X1(), X1(), X1(), X0());

Octet xBF() = BuildOctet (X1(), X0(), X1(), X1(), X1(), X1(), X1(), X1());

Octet xC0() = BuildOctet (X1(), X1(), X0(), X0(), X0(), X0(), X0(), X0());

Octet xC1() = BuildOctet (X1(), X1(), X0(), X0(), X0(), X0(), X0(), X1());

Octet xC2() = BuildOctet (X1(), X1(), X0(), X0(), X0(), X0(), X1(), X0());

Octet xC4() = BuildOctet (X1(), X1(), X0(), X0(), X0(), X1(), X0(), X0());

Octet xC5() = BuildOctet (X1(), X1(), X0(), X0(), X0(), X1(), X0(), X1());

Octet xC6() = BuildOctet (X1(), X1(), X0(), X0(), X0(), X1(), X1(), X0());

Octet xC7() = BuildOctet (X1(), X1(), X0(), X0(), X0(), X1(), X1(), X1());

Octet xC8() = BuildOctet (X1(), X1(), X0(), X0(), X1(), X0(), X0(), X0());

Octet xC9() = BuildOctet (X1(), X1(), X0(), X0(), X1(), X0(), X0(), X1());

Octet xCA() = BuildOctet (X1(), X1(), X0(), X0(), X1(), X0(), X1(), X0());

Octet xCB() = BuildOctet (X1(), X1(), X0(), X0(), X1(), X0(), X1(), X1());

Octet xCC() = BuildOctet (X1(), X1(), X0(), X0(), X1(), X1(), X0(), X0());

Octet xCD() = BuildOctet (X1(), X1(), X0(), X0(), X1(), X1(), X0(), X1());

Octet xCE() = BuildOctet (X1(), X1(), X0(), X0(), X1(), X1(), X1(), X0());

Octet xD0() = BuildOctet (X1(), X1(), X0(), X1(), X0(), X0(), X0(), X0());

Octet xD1() = BuildOctet (X1(), X1(), X0(), X1(), X0(), X0(), X0(), X1());

Octet xD2() = BuildOctet (X1(), X1(), X0(), X1(), X0(), X0(), X1(), X0());

Octet xD3() = BuildOctet (X1(), X1(), X0(), X1(), X0(), X0(), X1(), X1());

Octet xD4() = BuildOctet (X1(), X1(), X0(), X1(), X0(), X1(), X0(), X0());

Octet xD5() = BuildOctet (X1(), X1(), X0(), X1(), X0(), X1(), X0(), X1());

Octet xD6() = BuildOctet (X1(), X1(), X0(), X1(), X0(), X1(), X1(), X0());

Octet xD7() = BuildOctet (X1(), X1(), X0(), X1(), X0(), X1(), X1(), X1());

Octet xD8() = BuildOctet (X1(), X1(), X0(), X1(), X1(), X0(), X0(), X0());

Octet xD9() = BuildOctet (X1(), X1(), X0(), X1(), X1(), X0(), X0(), X1());

Octet xDB() = BuildOctet (X1(), X1(), X0(), X1(), X1(), X0(), X1(), X1());

Octet xDC() = BuildOctet (X1(), X1(), X0(), X1(), X1(), X1(), X0(), X0());

Octet xDD() = BuildOctet (X1(), X1(), X0(), X1(), X1(), X1(), X0(), X1());

Octet xDE() = BuildOctet (X1(), X1(), X0(), X1(), X1(), X1(), X1(), X0());

Octet xDF() = BuildOctet (X1(), X1(), X0(), X1(), X1(), X1(), X1(), X1());

Octet xE0() = BuildOctet (X1(), X1(), X1(), X0(), X0(), X0(), X0(), X0());

Octet xE1() = BuildOctet (X1(), X1(), X1(), X0(), X0(), X0(), X0(), X1());

Octet xE3() = BuildOctet (X1(), X1(), X1(), X0(), X0(), X0(), X1(), X1());

Octet xE6() = BuildOctet (X1(), X1(), X1(), X0(), X0(), X1(), X1(), X0());

Octet xE8() = BuildOctet (X1(), X1(), X1(), X0(), X1(), X0(), X0(), X0());

Octet xE9() = BuildOctet (X1(), X1(), X1(), X0(), X1(), X0(), X0(), X1());

Octet xEA() = BuildOctet (X1(), X1(), X1(), X0(), X1(), X0(), X1(), X0());

Octet xEB() = BuildOctet (X1(), X1(), X1(), X0(), X1(), X0(), X1(), X1());

Octet xEC() = BuildOctet (X1(), X1(), X1(), X0(), X1(), X1(), X0(), X0());

Octet xED() = BuildOctet (X1(), X1(), X1(), X0(), X1(), X1(), X0(), X1());

Octet xEE() = BuildOctet (X1(), X1(), X1(), X0(), X1(), X1(), X1(), X0());

Octet xEF() = BuildOctet (X1(), X1(), X1(), X0(), X1(), X1(), X1(), X1());

Octet xF0() = BuildOctet (X1(), X1(), X1(), X1(), X0(), X0(), X0(), X0());

Octet xF1() = BuildOctet (X1(), X1(), X1(), X1(), X0(), X0(), X0(), X1());

Octet xF2() = BuildOctet (X1(), X1(), X1(), X1(), X0(), X0(), X1(), X0());

Octet xF3() = BuildOctet (X1(), X1(), X1(), X1(), X0(), X0(), X1(), X1());

Octet xF4() = BuildOctet (X1(), X1(), X1(), X1(), X0(), X1(), X0(), X0());

Octet xF5() = BuildOctet (X1(), X1(), X1(), X1(), X0(), X1(), X0(), X1());

Octet xF6() = BuildOctet (X1(), X1(), X1(), X1(), X0(), X1(), X1(), X0());

Octet xF7() = BuildOctet (X1(), X1(), X1(), X1(), X0(), X1(), X1(), X1());

Octet xF8() = BuildOctet (X1(), X1(), X1(), X1(), X1(), X0(), X0(), X0());

Octet xF9() = BuildOctet (X1(), X1(), X1(), X1(), X1(), X0(), X0(), X1());

Octet xFA() = BuildOctet (X1(), X1(), X1(), X1(), X1(), X0(), X1(), X0());

Octet xFB() = BuildOctet (X1(), X1(), X1(), X1(), X1(), X0(), X1(), X1());

Octet xFC() = BuildOctet (X1(), X1(), X1(), X1(), X1(), X1(), X0(), X0());

Octet xFD() = BuildOctet (X1(), X1(), X1(), X1(), X1(), X1(), X0(), X1());

Octet xFE() = BuildOctet (X1(), X1(), X1(), X1(), X1(), X1(), X1(), X0());

Octet xFF() = BuildOctet (X1(), X1(), X1(), X1(), X1(), X1(), X1(), X1());

Block andBlock (BuildBlock (Octet o1, Octet o2, Octet o3, Octet o4), BuildBlock (Octet o_prime1, Octet o_prime2, Octet o_prime3, Octet o_prime4)) = BuildBlock (andOctet (o1, o_prime1), andOctet (o2, o_prime2), andOctet (o3, o_prime3), andOctet (o4, o_prime4));

Block orBlock (BuildBlock (Octet o1, Octet o2, Octet o3, Octet o4), BuildBlock (Octet o_prime1, Octet o_prime2, Octet o_prime3, Octet o_prime4)) = BuildBlock (orOctet (o1, o_prime1), orOctet (o2, o_prime2), orOctet (o3, o_prime3), orOctet (o4, o_prime4));

Block xorBlock (BuildBlock (Octet o1, Octet o2, Octet o3, Octet o4), BuildBlock (Octet o_prime1, Octet o_prime2, Octet o_prime3, Octet o_prime4)) = BuildBlock (xorOctet (o1, o_prime1), xorOctet (o2, o_prime2), xorOctet (o3, o_prime3), xorOctet (o4, o_prime4));

Half x0001() = BuildHalf (x00(), x01());

Block x00000000() = BuildBlock (x00(), x00(), x00(), x00());

Block x00000001() = BuildBlock (x00(), x00(), x00(), x01());

Block x00000002() = BuildBlock (x00(), x00(), x00(), x02());

Block x00000003() = BuildBlock (x00(), x00(), x00(), x03());

Block x00000004() = BuildBlock (x00(), x00(), x00(), x04());

Block x00000005() = BuildBlock (x00(), x00(), x00(), x05());

Block x00000006() = BuildBlock (x00(), x00(), x00(), x06());

Block x00000007() = BuildBlock (x00(), x00(), x00(), x07());

Block x00000008() = BuildBlock (x00(), x00(), x00(), x08());

Block x00000009() = BuildBlock (x00(), x00(), x00(), x09());

Block x0000000A() = BuildBlock (x00(), x00(), x00(), x0A());

Block x0000000B() = BuildBlock (x00(), x00(), x00(), x0B());

Block x0000000C() = BuildBlock (x00(), x00(), x00(), x0C());

Block x0000000D() = BuildBlock (x00(), x00(), x00(), x0D());

Block x0000000E() = BuildBlock (x00(), x00(), x00(), x0E());

Block x0000000F() = BuildBlock (x00(), x00(), x00(), x0F());

Block x00000010() = BuildBlock (x00(), x00(), x00(), x10());

Block x00000012() = BuildBlock (x00(), x00(), x00(), x12());

Block x00000014() = BuildBlock (x00(), x00(), x00(), x14());

Block x00000016() = BuildBlock (x00(), x00(), x00(), x16());

Block x00000018() = BuildBlock (x00(), x00(), x00(), x18());

Block x0000001B() = BuildBlock (x00(), x00(), x00(), x1B());

Block x0000001D() = BuildBlock (x00(), x00(), x00(), x1D());

Block x0000001E() = BuildBlock (x00(), x00(), x00(), x1E());

Block x0000001F() = BuildBlock (x00(), x00(), x00(), x1F());

Block x00000031() = BuildBlock (x00(), x00(), x00(), x31());

Block x00000036() = BuildBlock (x00(), x00(), x00(), x36());

Block x00000060() = BuildBlock (x00(), x00(), x00(), x60());

Block x00000080() = BuildBlock (x00(), x00(), x00(), x80());

Block x000000A5() = BuildBlock (x00(), x00(), x00(), xA5());

Block x000000B6() = BuildBlock (x00(), x00(), x00(), xB6());

Block x000000C4() = BuildBlock (x00(), x00(), x00(), xC4());

Block x000000D2() = BuildBlock (x00(), x00(), x00(), xD2());

Block x00000100() = BuildBlock (x00(), x00(), x01(), x00());

Block x00000129() = BuildBlock (x00(), x00(), x01(), x29());

Block x0000018C() = BuildBlock (x00(), x00(), x01(), x8C());

Block x00004000() = BuildBlock (x00(), x00(), x40(), x00());

Block x00010000() = BuildBlock (x00(), x01(), x00(), x00());

Block x00020000() = BuildBlock (x00(), x02(), x00(), x00());

Block x00030000() = BuildBlock (x00(), x03(), x00(), x00());

Block x00040000() = BuildBlock (x00(), x04(), x00(), x00());

Block x00060000() = BuildBlock (x00(), x06(), x00(), x00());

Block x00804021() = BuildBlock (x00(), x80(), x40(), x21());

Block x00FF00FF() = BuildBlock (x00(), xFF(), x00(), xFF());

Block x0103050B() = BuildBlock (x01(), x03(), x05(), x0B());

Block x01030703() = BuildBlock (x01(), x03(), x07(), x03());

Block x01030705() = BuildBlock (x01(), x03(), x07(), x05());

Block x0103070F() = BuildBlock (x01(), x03(), x07(), x0F());

Block x02040801() = BuildBlock (x02(), x04(), x08(), x01());

Block x0297AF6F() = BuildBlock (x02(), x97(), xAF(), x6F());

Block x07050301() = BuildBlock (x07(), x05(), x03(), x01());

Block x077788A2() = BuildBlock (x07(), x77(), x88(), xA2());

Block x07C72EAA() = BuildBlock (x07(), xC7(), x2E(), xAA());

Block x0A202020() = BuildBlock (x0A(), x20(), x20(), x20());

Block x0AD67E20() = BuildBlock (x0A(), xD6(), x7E(), x20());

Block x10000000() = BuildBlock (x10(), x00(), x00(), x00());

Block x11A9D254() = BuildBlock (x11(), xA9(), xD2(), x54());

Block x11AC46B8() = BuildBlock (x11(), xAC(), x46(), xB8());

Block x1277A6D4() = BuildBlock (x12(), x77(), xA6(), xD4());

Block x13647149() = BuildBlock (x13(), x64(), x71(), x49());

Block x160EE9B5() = BuildBlock (x16(), x0E(), xE9(), xB5());

Block x17065DBB() = BuildBlock (x17(), x06(), x5D(), xBB());

Block x17A808FD() = BuildBlock (x17(), xA8(), x08(), xFD());

Block x1D10D8D3() = BuildBlock (x1D(), x10(), xD8(), xD3());

Block x1D3B7760() = BuildBlock (x1D(), x3B(), x77(), x60());

Block x1D9C9655() = BuildBlock (x1D(), x9C(), x96(), x55());

Block x1F3F7FFF() = BuildBlock (x1F(), x3F(), x7F(), xFF());

Block x204E80A7() = BuildBlock (x20(), x4E(), x80(), xA7());

Block x21D869BA() = BuildBlock (x21(), xD8(), x69(), xBA());

Block x24B66FB5() = BuildBlock (x24(), xB6(), x6F(), xB5());

Block x270EEDAF() = BuildBlock (x27(), x0E(), xED(), xAF());

Block x277B4B25() = BuildBlock (x27(), x7B(), x4B(), x25());

Block x2829040B() = BuildBlock (x28(), x29(), x04(), x0B());

Block x288FC786() = BuildBlock (x28(), x8F(), xC7(), x86());

Block x28EAD8B3() = BuildBlock (x28(), xEA(), xD8(), xB3());

Block x29907CD8() = BuildBlock (x29(), x90(), x7C(), xD8());

Block x29C1485F() = BuildBlock (x29(), xC1(), x48(), x5F());

Block x29EEE96B() = BuildBlock (x29(), xEE(), xE9(), x6B());

Block x2A6091AE() = BuildBlock (x2A(), x60(), x91(), xAE());

Block x2BF8499A() = BuildBlock (x2B(), xF8(), x49(), x9A());

Block x2E80AC30() = BuildBlock (x2E(), x80(), xAC(), x30());

Block x2FD76FFB() = BuildBlock (x2F(), xD7(), x6F(), xFB());

Block x30261492() = BuildBlock (x30(), x26(), x14(), x92());

Block x303FF4AA() = BuildBlock (x30(), x3F(), xF4(), xAA());

Block x33D5A466() = BuildBlock (x33(), xD5(), xA4(), x66());

Block x344925FC() = BuildBlock (x34(), x49(), x25(), xFC());

Block x34ACF886() = BuildBlock (x34(), xAC(), xF8(), x86());

Block x3CD54DEB() = BuildBlock (x3C(), xD5(), x4D(), xEB());

Block x3CF3A7D2() = BuildBlock (x3C(), xF3(), xA7(), xD2());

Block x3DD81AC6() = BuildBlock (x3D(), xD8(), x1A(), xC6());

Block x3F6F7248() = BuildBlock (x3F(), x6F(), x72(), x48());

Block x48B204D6() = BuildBlock (x48(), xB2(), x04(), xD6());

Block x4A645A01() = BuildBlock (x4A(), x64(), x5A(), x01());

Block x4C49AAE0() = BuildBlock (x4C(), x49(), xAA(), xE0());

Block x4CE933E1() = BuildBlock (x4C(), xE9(), x33(), xE1());

Block x4D53901A() = BuildBlock (x4D(), x53(), x90(), x1A());

Block x4DA124A1() = BuildBlock (x4D(), xA1(), x24(), xA1());

Block x4F998E01() = BuildBlock (x4F(), x99(), x8E(), x01());

Block x4FB1138A() = BuildBlock (x4F(), xB1(), x13(), x8A());

Block x50DEC930() = BuildBlock (x50(), xDE(), xC9(), x30());

Block x51AF3C1D() = BuildBlock (x51(), xAF(), x3C(), x1D());

Block x51EDE9C7() = BuildBlock (x51(), xED(), xE9(), xC7());

Block x550D91CE() = BuildBlock (x55(), x0D(), x91(), xCE());

Block x55555555() = BuildBlock (x55(), x55(), x55(), x55());

Block x55DD063F() = BuildBlock (x55(), xDD(), x06(), x3F());

Block x5834A585() = BuildBlock (x58(), x34(), xA5(), x85());

Block x5A35D667() = BuildBlock (x5A(), x35(), xD6(), x67());

Block x5BC02502() = BuildBlock (x5B(), xC0(), x25(), x02());

Block x5CCA3239() = BuildBlock (x5C(), xCA(), x32(), x39());

Block x5EBA06C2() = BuildBlock (x5E(), xBA(), x06(), xC2());

Block x5F38EEF1() = BuildBlock (x5F(), x38(), xEE(), xF1());

Block x613F8E2A() = BuildBlock (x61(), x3F(), x8E(), x2A());

Block x63C70DBA() = BuildBlock (x63(), xC7(), x0D(), xBA());

Block x6AD6E8A4() = BuildBlock (x6A(), xD6(), xE8(), xA4());

Block x6AEBACF8() = BuildBlock (x6A(), xEB(), xAC(), xF8());

Block x6D67E884() = BuildBlock (x6D(), x67(), xE8(), x84());

Block x7050EC5E() = BuildBlock (x70(), x50(), xEC(), x5E());

Block x717153D5() = BuildBlock (x71(), x71(), x53(), xD5());

Block x7201F4DC() = BuildBlock (x72(), x01(), xF4(), xDC());

Block x7397C9AE() = BuildBlock (x73(), x97(), xC9(), xAE());

Block x74B39176() = BuildBlock (x74(), xB3(), x91(), x76());

Block x76232E5F() = BuildBlock (x76(), x23(), x2E(), x5F());

Block x7783C51D() = BuildBlock (x77(), x83(), xC5(), x1D());

Block x7792F9D4() = BuildBlock (x77(), x92(), xF9(), xD4());

Block x7BC180AB() = BuildBlock (x7B(), xC1(), x80(), xAB());

Block x7DB2D9F4() = BuildBlock (x7D(), xB2(), xD9(), xF4());

Block x7DFEFBFF() = BuildBlock (x7D(), xFE(), xFB(), xFF());

Block x7F76A3B0() = BuildBlock (x7F(), x76(), xA3(), xB0());

Block x7F839576() = BuildBlock (x7F(), x83(), x95(), x76());

Block x7FFFFFF0() = BuildBlock (x7F(), xFF(), xFF(), xF0());

Block x7FFFFFF1() = BuildBlock (x7F(), xFF(), xFF(), xF1());

Block x7FFFFFFC() = BuildBlock (x7F(), xFF(), xFF(), xFC());

Block x7FFFFFFD() = BuildBlock (x7F(), xFF(), xFF(), xFD());

Block x80000000() = BuildBlock (x80(), x00(), x00(), x00());

Block x80000002() = BuildBlock (x80(), x00(), x00(), x02());

Block x800000C2() = BuildBlock (x80(), x00(), x00(), xC2());

Block x80018000() = BuildBlock (x80(), x01(), x80(), x00());

Block x80018001() = BuildBlock (x80(), x01(), x80(), x01());

Block x80397302() = BuildBlock (x80(), x39(), x73(), x02());

Block x81D10CA3() = BuildBlock (x81(), xD1(), x0C(), xA3());

Block x89D635D7() = BuildBlock (x89(), xD6(), x35(), xD7());

Block x8CE37709() = BuildBlock (x8C(), xE3(), x77(), x09());

Block x8DC8BBDE() = BuildBlock (x8D(), xC8(), xBB(), xDE());

Block x9115A558() = BuildBlock (x91(), x15(), xA5(), x58());

Block x91896CFA() = BuildBlock (x91(), x89(), x6C(), xFA());

Block x9372CDC6() = BuildBlock (x93(), x72(), xCD(), xC6());

Block x98D1CC75() = BuildBlock (x98(), xD1(), xCC(), x75());

Block x9D15C437() = BuildBlock (x9D(), x15(), xC4(), x37());

Block x9DB15CF6() = BuildBlock (x9D(), xB1(), x5C(), xF6());

Block x9E2E7B36() = BuildBlock (x9E(), x2E(), x7B(), x36());

Block xA018C83B() = BuildBlock (xA0(), x18(), xC8(), x3B());

Block xA0B87B77() = BuildBlock (xA0(), xB8(), x7B(), x77());

Block xA44AAAC0() = BuildBlock (xA4(), x4A(), xAA(), xC0());

Block xA511987A() = BuildBlock (xA5(), x11(), x98(), x7A());

Block xA70FC148() = BuildBlock (xA7(), x0F(), xC1(), x48());

Block xA93BD410() = BuildBlock (xA9(), x3B(), xD4(), x10());

Block xAAAAAAAA() = BuildBlock (xAA(), xAA(), xAA(), xAA());

Block xAB00FFCD() = BuildBlock (xAB(), x00(), xFF(), xCD());

Block xAB01FCCD() = BuildBlock (xAB(), x01(), xFC(), xCD());

Block xAB6EED4A() = BuildBlock (xAB(), x6E(), xED(), x4A());

Block xABEEED6B() = BuildBlock (xAB(), xEE(), xED(), x6B());

Block xACBC13DD() = BuildBlock (xAC(), xBC(), x13(), xDD());

Block xB1CC1CC5() = BuildBlock (xB1(), xCC(), x1C(), xC5());

Block xB8142629() = BuildBlock (xB8(), x14(), x26(), x29());

Block xB99A62DE() = BuildBlock (xB9(), x9A(), x62(), xDE());

Block xBA92DB12() = BuildBlock (xBA(), x92(), xDB(), x12());

Block xBBA57835() = BuildBlock (xBB(), xA5(), x78(), x35());

Block xBE9F0917() = BuildBlock (xBE(), x9F(), x09(), x17());

Block xBF2D7D85() = BuildBlock (xBF(), x2D(), x7D(), x85());

Block xBFEF7FDF() = BuildBlock (xBF(), xEF(), x7F(), xDF());

Block xC1ED90DD() = BuildBlock (xC1(), xED(), x90(), xDD());

Block xC21A1846() = BuildBlock (xC2(), x1A(), x18(), x46());

Block xC4EB1AEB() = BuildBlock (xC4(), xEB(), x1A(), xEB());

Block xC6B1317E() = BuildBlock (xC6(), xB1(), x31(), x7E());

Block xCBC865BA() = BuildBlock (xCB(), xC8(), x65(), xBA());

Block xCD959B46() = BuildBlock (xCD(), x95(), x9B(), x46());

Block xD0482465() = BuildBlock (xD0(), x48(), x24(), x65());

Block xD636250D() = BuildBlock (xD6(), x36(), x25(), x0D());

Block xD7843FDC() = BuildBlock (xD7(), x84(), x3F(), xDC());

Block xD78634BC() = BuildBlock (xD7(), x86(), x34(), xBC());

Block xD8804CA5() = BuildBlock (xD8(), x80(), x4C(), xA5());

Block xDB79FBDC() = BuildBlock (xDB(), x79(), xFB(), xDC());

Block xDB9102B0() = BuildBlock (xDB(), x91(), x02(), xB0());

Block xE0C08000() = BuildBlock (xE0(), xC0(), x80(), x00());

Block xE6A12F07() = BuildBlock (xE6(), xA1(), x2F(), x07());

Block xEB35B97F() = BuildBlock (xEB(), x35(), xB9(), x7F());

Block xF0239DD5() = BuildBlock (xF0(), x23(), x9D(), xD5());

Block xF14D6E28() = BuildBlock (xF1(), x4D(), x6E(), x28());

Block xF2EF3501() = BuildBlock (xF2(), xEF(), x35(), x01());

Block xF6A09667() = BuildBlock (xF6(), xA0(), x96(), x67());

Block xFD297DA4() = BuildBlock (xFD(), x29(), x7D(), xA4());

Block xFDC1A8BA() = BuildBlock (xFD(), xC1(), xA8(), xBA());

Block xFE4E5BDD() = BuildBlock (xFE(), x4E(), x5B(), xDD());

Block xFEA1D334() = BuildBlock (xFE(), xA1(), xD3(), x34());

Block xFECCAA6E() = BuildBlock (xFE(), xCC(), xAA(), x6E());

Block xFEFC07F0() = BuildBlock (xFE(), xFC(), x07(), xF0());

Block xFF2D7DA5() = BuildBlock (xFF(), x2D(), x7D(), xA5());

Block xFFEF0001() = BuildBlock (xFF(), xEF(), x00(), x01());

Block xFFFF00FF() = BuildBlock (xFF(), xFF(), x00(), xFF());

Block xFFFFFF2D() = BuildBlock (xFF(), xFF(), xFF(), x2D());

Block xFFFFFF3A() = BuildBlock (xFF(), xFF(), xFF(), x3A());

Block xFFFFFFF0() = BuildBlock (xFF(), xFF(), xFF(), xF0());

Block xFFFFFFF1() = BuildBlock (xFF(), xFF(), xFF(), xF1());

Block xFFFFFFF4() = BuildBlock (xFF(), xFF(), xFF(), xF4());

Block xFFFFFFF5() = BuildBlock (xFF(), xFF(), xFF(), xF5());

Block xFFFFFFF7() = BuildBlock (xFF(), xFF(), xFF(), xF7());

Block xFFFFFFF9() = BuildBlock (xFF(), xFF(), xFF(), xF9());

Block xFFFFFFFA() = BuildBlock (xFF(), xFF(), xFF(), xFA());

Block xFFFFFFFB() = BuildBlock (xFF(), xFF(), xFF(), xFB());

Block xFFFFFFFC() = BuildBlock (xFF(), xFF(), xFF(), xFC());

Block xFFFFFFFD() = BuildBlock (xFF(), xFF(), xFF(), xFD());

Block xFFFFFFFE() = BuildBlock (xFF(), xFF(), xFF(), xFE());

Block xFFFFFFFF() = BuildBlock (xFF(), xFF(), xFF(), xFF());

Message appendMessage (UnitMessage (Block w), Block w_prime) = ConsMessage (w, UnitMessage (w_prime));
Message appendMessage (ConsMessage (Block w, Message m), Block w_prime) = ConsMessage (w, appendMessage (m, w_prime));

Message reverseMessage (UnitMessage (Block w)) = UnitMessage (w);
Message reverseMessage (ConsMessage (Block w, Message m)) = appendMessage (reverseMessage (m), w);

Message makeMessage (Succ (Nat n), Block w, Block w_prime) { if (eqNat (n, Zero()) == Xtrue()) return UnitMessage (w); else fail; }
Message makeMessage (Succ (Nat n), Block w, Block w_prime) { if (eqNat (n, Zero()) == Xfalse()) return ConsMessage (w, makeMessage (n, aDD (w, w_prime), w_prime)); else fail; }

Block aDD (Block w, Block w_prime) = addBlock (w, w_prime);

Block xand (Block w, Block w_prime) = andBlock (w, w_prime);

Pair mUL (Block w, Block w_prime) = mulBlock (w, w_prime);

Block yor (Block w, Block w_prime) = orBlock (w, w_prime);

Block xOR (Block w, Block w_prime) = xorBlock (w, w_prime);

Block xOR_prime (BuildPair (Block w, Block w_prime)) = xOR (w, w_prime);

Block cYC (BuildBlock (BuildOctet (Bit b1, Bit b2, Bit b3, Bit b4, Bit b5, Bit b6, Bit b7, Bit b8), BuildOctet (Bit b9, Bit b10, Bit b11, Bit b12, Bit b13, Bit b14, Bit b15, Bit b16), BuildOctet (Bit b17, Bit b18, Bit b19, Bit b20, Bit b21, Bit b22, Bit b23, Bit b24), BuildOctet (Bit b25, Bit b26, Bit b27, Bit b28, Bit b29, Bit b30, Bit b31, Bit b32))) = BuildBlock (BuildOctet (b2, b3, b4, b5, b6, b7, b8, b9), BuildOctet (b10, b11, b12, b13, b14, b15, b16, b17), BuildOctet (b18, b19, b20, b21, b22, b23, b24, b25), BuildOctet (b26, b27, b28, b29, b30, b31, b32, b1));

Block nCYC (Zero(), Block w) = w;
Block nCYC (Succ (Nat n), Block w) = cYC (nCYC (n, w));

Block fIX1 (Block w) = xand (yor (w, x02040801()), xBFEF7FDF());

Block fIX2 (Block w) = xand (yor (w, x00804021()), x7DFEFBFF());

Xbool needAdjust (Octet xo) = orBool (eqOctet (xo, x00()), eqOctet (xo, xFF()));

Bit adjustCode (Octet xo) { if (needAdjust (xo) == Xtrue()) return X1(); else fail; }
Bit adjustCode (Octet xo) { if (needAdjust (xo) == Xfalse()) return X0(); else fail; }

Octet adjust (Octet xo, Octet o_prime) { if (needAdjust (xo) == Xtrue()) return xorOctet (xo, o_prime); else fail; }
Octet adjust (Octet xo, Octet o_prime) { if (needAdjust (xo) == Xfalse()) return xo; else fail; }

Octet pAT (BuildBlock (Octet o1, Octet o2, Octet o3, Octet o4), BuildBlock (Octet o_prime1, Octet o_prime2, Octet o_prime3, Octet o_prime4)) = BuildOctet (adjustCode (o1), adjustCode (o2), adjustCode (o3), adjustCode (o4), adjustCode (o_prime1), adjustCode (o_prime2), adjustCode (o_prime3), adjustCode (o_prime4));

Pair bYT (BuildBlock (Octet o1, Octet o2, Octet o3, Octet o4), BuildBlock (Octet o_prime1, Octet o_prime2, Octet o_prime3, Octet o_prime4)) = bYT_prime (o1, o2, o3, o4, o_prime1, o_prime2, o_prime3, o_prime4, pAT (BuildBlock (o1, o2, o3, o4), BuildBlock (o_prime1, o_prime2, o_prime3, o_prime4)));

Pair bYT_prime (Octet o1, Octet o2, Octet o3, Octet o4, Octet o_prime1, Octet o_prime2, Octet o_prime3, Octet o_prime4, Octet opat) = BuildPair (BuildBlock (adjust (o1, rightOctet7 (opat)), adjust (o2, rightOctet6 (opat)), adjust (o3, rightOctet5 (opat)), adjust (o4, rightOctet4 (opat))), BuildBlock (adjust (o_prime1, rightOctet3 (opat)), adjust (o_prime2, rightOctet2 (opat)), adjust (o_prime3, rightOctet1 (opat)), adjust (o_prime4, opat)));

Pair aDDC (Block w, Block w_prime) = aDDC_prime (addBlockSum (w, w_prime));

Pair aDDC_prime (BuildBlockSum (X0(), Block w)) = BuildPair (x00000000(), w);
Pair aDDC_prime (BuildBlockSum (X1(), Block w)) = BuildPair (x00000001(), w);

Block mUL1 (Block w, Block w_prime) = mUL1XY (mUL (w, w_prime));

Block mUL1XY (BuildPair (Block w, Block w_prime)) = mUL1UL (w, w_prime);

Block mUL1UL (Block w, Block w_prime) = mUL1SC (aDDC (w, w_prime));

Block mUL1SC (BuildPair (Block wcarry, Block w)) = aDD (w, wcarry);

Block mUL2 (Block w, Block w_prime) = mUL2XY (mUL (w, w_prime));

Block mUL2XY (BuildPair (Block w, Block w_prime)) = mUL2UL (w, w_prime);

Block mUL2UL (Block w, Block w_prime) = mUL2DEL (aDDC (w, w), w_prime);

Block mUL2DEL (BuildPair (Block wcarry, Block w), Block w_prime) = mUL2FL (aDD (w, aDD (wcarry, wcarry)), w_prime);

Block mUL2FL (Block w, Block w_prime) = mUL2SC (aDDC (w, w_prime));

Block mUL2SC (BuildPair (Block wcarry, Block w)) = aDD (w, aDD (wcarry, wcarry));

Block mUL2A (Block w, Block w_prime) = mUL2AXY (mUL (w, w_prime));

Block mUL2AXY (BuildPair (Block w, Block w_prime)) = mUL2AUL (w, w_prime);

Block mUL2AUL (Block w, Block w_prime) = mUL2ADL (aDD (w, w), w_prime);

Block mUL2ADL (Block w, Block w_prime) = mUL2ASC (aDDC (w, w_prime));

Block mUL2ASC (BuildPair (Block wcarry, Block w)) = aDD (w, aDD (wcarry, wcarry));

Block squareHalf (Half h) = mulHalf (h, h);

Block q (Octet xo) = squareHalf (addHalf (BuildHalf (x00(), xo), x0001()));

Block j1_2 (Block w) = mUL1 (w, w);

Block j1_4 (Block w) = mUL1 (j1_2 (w), j1_2 (w));

Block j1_6 (Block w) = mUL1 (j1_2 (w), j1_4 (w));

Block j1_8 (Block w) = mUL1 (j1_2 (w), j1_6 (w));

Block j2_2 (Block w) = mUL2 (w, w);

Block j2_4 (Block w) = mUL2 (j2_2 (w), j2_2 (w));

Block j2_6 (Block w) = mUL2 (j2_2 (w), j2_4 (w));

Block j2_8 (Block w) = mUL2 (j2_2 (w), j2_6 (w));

Block k1_2 (Block w) = mUL1 (w, w);

Block k1_4 (Block w) = mUL1 (k1_2 (w), k1_2 (w));

Block k1_5 (Block w) = mUL1 (w, k1_4 (w));

Block k1_7 (Block w) = mUL1 (k1_2 (w), k1_5 (w));

Block k1_9 (Block w) = mUL1 (k1_2 (w), k1_7 (w));

Block k2_2 (Block w) = mUL2 (w, w);

Block k2_4 (Block w) = mUL2 (k2_2 (w), k2_2 (w));

Block k2_5 (Block w) = mUL2 (w, k2_4 (w));

Block k2_7 (Block w) = mUL2 (k2_2 (w), k2_5 (w));

Block k2_9 (Block w) = mUL2 (k2_2 (w), k2_7 (w));

Block h4 (Block w) = xOR (j1_4 (w), j2_4 (w));

Block h6 (Block w) = xOR (j1_6 (w), j2_6 (w));

Block h8 (Block w) = xOR (j1_8 (w), j2_8 (w));

Block h0 (Block w) = xOR (k1_5 (w), k2_5 (w));

Block h5 (Block w, Octet xo) = mUL2 (h0 (w), q (xo));

Block h7 (Block w) = xOR (k1_7 (w), k2_7 (w));

Block h9 (Block w) = xOR (k1_9 (w), k2_9 (w));

SegmentedMessage splitSegment (UnitMessage (Block w)) = UnitSegment (UnitMessage (w));
SegmentedMessage splitSegment (ConsMessage (Block w, Message m)) = cutSegment (m, UnitMessage (w), n254());

SegmentedMessage cutSegment (UnitMessage (Block w), Message m_prime, Nat n) = UnitSegment (reverseMessage (ConsMessage (w, m_prime)));
SegmentedMessage cutSegment (ConsMessage (Block w, Message m), Message m_prime, Zero()) = ConsSegment (reverseMessage (ConsMessage (w, m_prime)), splitSegment (m));
SegmentedMessage cutSegment (ConsMessage (Block w, Message m), Message m_prime, Succ (Nat n)) = cutSegment (m, ConsMessage (w, m_prime), n);

Pair preludeXY (Block w1, Block w2) = preludeXY_prime (bYT (w1, w2), pAT (w1, w2));

Pair preludeVW (Block w1, Block w2) = preludeVW_prime (bYT (w1, w2));

Pair preludeST (Block w1, Block w2) = preludeST_prime (bYT (w1, w2));

Pair preludeXY_prime (BuildPair (Block w, Block w_prime), Octet xo) = bYT (h4 (w), h5 (w_prime, xo));

Pair preludeVW_prime (BuildPair (Block w, Block w_prime)) = bYT (h6 (w), h7 (w_prime));

Pair preludeST_prime (BuildPair (Block w, Block w_prime)) = bYT (h8 (w), h9 (w_prime));

Pair computeXY (Pair p, Pair p_prime, Block w) = computeXY_prime (p, w, xOR_prime (computeVW (p_prime)));

Pair computeXY_prime (BuildPair (Block w1, Block w2), Block w, Block w_prime) = BuildPair (mUL1 (xOR (w1, w), fIX1 (aDD (xOR (w2, w), w_prime))), mUL2A (xOR (w2, w), fIX2 (aDD (xOR (w1, w), w_prime))));

Pair computeVW (BuildPair (Block w1, Block w2)) = BuildPair (cYC (w1), w2);

Pair loop1 (Pair p, Pair p_prime, UnitMessage (Block w)) = computeXY (p, p_prime, w);
Pair loop1 (Pair p, Pair p_prime, ConsMessage (Block w, Message m)) = loop1 (computeXY (p, p_prime, w), computeVW (p_prime), m);

Pair loop2 (Pair p, Pair p_prime, UnitMessage (Block w)) = computeVW (p_prime);
Pair loop2 (Pair p, Pair p_prime, ConsMessage (Block w, Message m)) = loop2 (computeXY (p, p_prime, w), computeVW (p_prime), m);

Block coda (Pair p, Pair p_prime, BuildPair (Block w, Block w_prime)) = xOR_prime (computeXY (computeXY (p, p_prime, w), computeVW (p_prime), w_prime));

Block mAA (BuildKey (Block w1, Block w2), Message m) = mAA_prime (preludeXY (w1, w2), preludeVW (w1, w2), preludeST (w1, w2), m);

Block mAA_prime (Pair p1, Pair p2, Pair p3, Message m) = coda (loop1 (p1, p2, m), loop2 (p1, p2, m), p3);

Block mAC (Key k, Message m) = mACfirst (k, splitSegment (m));

Block mACfirst (Key k, UnitSegment (Message m)) = mAA (k, m);
Block mACfirst (Key k, ConsSegment (Message m, SegmentedMessage s)) = mACnext (k, mAA (k, m), s);

Block mACnext (Key k, Block w, UnitSegment (Message m)) = mAA (k, ConsMessage (w, m));
Block mACnext (Key k, Block w, ConsSegment (Message m, SegmentedMessage s)) = mACnext (k, mAA (k, ConsMessage (w, m)), s);

void main([]) { // entry point for the RASCAL interpreter
  work();
}

int main(){
  return work();
}

int work() { // entry point for the RASCAL compiler
  println (eqBlock (mUL1 (x0000000F(), x0000000E()), x000000D2()));
  println (eqBlock (mUL1 (xFFFFFFF0(), x0000000E()), xFFFFFF2D()));
  println (eqBlock (mUL1 (xFFFFFFF0(), xFFFFFFF1()), x000000D2()));
  println (eqBlock (mUL2 (x0000000F(), x0000000E()), x000000D2()));
  println (eqBlock (mUL2 (xFFFFFFF0(), x0000000E()), xFFFFFF3A()));
  println (eqBlock (mUL2 (xFFFFFFF0(), xFFFFFFF1()), x000000B6()));
  println (eqBlock (mUL2A (x0000000F(), x0000000E()), x000000D2()));
  println (eqBlock (mUL2A (xFFFFFFF0(), x0000000E()), xFFFFFF3A()));
  println (eqBlock (mUL2A (x7FFFFFF0(), xFFFFFFF1()), x800000C2()));
  println (eqBlock (mUL2A (xFFFFFFF0(), x7FFFFFF1()), x000000C4()));
  println (eqPair (bYT (x00000000(), x00000000()), BuildPair (x0103070F(), x1F3F7FFF())));
  println (eqPair (bYT (xFFFF00FF(), xFFFFFFFF()), BuildPair (xFEFC07F0(), xE0C08000())));
  println (eqPair (bYT (xAB00FFCD(), xFFEF0001()), BuildPair (xAB01FCCD(), xF2EF3501())));
  println (eqOctet (pAT (x00000000(), x00000000()), xFF()));
  println (eqOctet (pAT (xFFFF00FF(), xFFFFFFFF()), xFF()));
  println (eqOctet (pAT (xAB00FFCD(), xFFEF0001()), x6A()));
  println (eqBlock (j1_2 (x00000100()), x00010000()));
  println (eqBlock (j1_4 (x00000100()), x00000001()));
  println (eqBlock (j1_6 (x00000100()), x00010000()));
  println (eqBlock (j1_8 (x00000100()), x00000001()));
  println (eqBlock (j2_2 (x00000100()), x00010000()));
  println (eqBlock (j2_4 (x00000100()), x00000002()));
  println (eqBlock (j2_6 (x00000100()), x00020000()));
  println (eqBlock (j2_8 (x00000100()), x00000004()));
  println (eqBlock (h4 (x00000100()), x00000003()));
  println (eqBlock (h6 (x00000100()), x00030000()));
  println (eqBlock (h8 (x00000100()), x00000005()));
  println (eqBlock (k1_2 (x00000080()), x00004000()));
  println (eqBlock (k1_4 (x00000080()), x10000000()));
  println (eqBlock (k1_5 (x00000080()), x00000008()));
  println (eqBlock (k1_7 (x00000080()), x00020000()));
  println (eqBlock (k1_9 (x00000080()), x80000000()));
  println (eqBlock (k2_2 (x00000080()), x00004000()));
  println (eqBlock (k2_4 (x00000080()), x10000000()));
  println (eqBlock (k2_5 (x00000080()), x00000010()));
  println (eqBlock (k2_7 (x00000080()), x00040000()));
  println (eqBlock (k2_9 (x00000080()), x00000002()));
  println (eqBlock (h0 (x00000080()), x00000018()));
  println (eqBlock (q (x01()), x00000004()));
  println (eqBlock (h5 (x00000080(), x01()), x00000060()));
  println (eqBlock (h7 (x00000080()), x00060000()));
  println (eqBlock (h9 (x00000080()), x80000002()));
  println (eqOctet (pAT (x00000003(), x00000060()), xEE()));
  println (eqOctet (pAT (x00030000(), x00060000()), xBB()));
  println (eqOctet (pAT (x00000005(), x80000002()), xE6()));
  println (eqPair (bYT (x00000003(), x00000060()), BuildPair (x01030703(), x1D3B7760())));
  println (eqPair (bYT (x00030000(), x00060000()), BuildPair (x0103050B(), x17065DBB())));
  println (eqPair (bYT (x00000005(), x80000002()), BuildPair (x01030705(), x80397302())));
  println (eqBlock (cYC (x00000003()), x00000006()));
  println (eqBlock (xOR (x00000006(), x00000003()), x00000005()));
  println (eqBlock (xOR (x00000002(), x00000005()), x00000007()));
  println (eqBlock (xOR (x00000003(), x00000005()), x00000006()));
  println (eqBlock (aDD (x00000005(), x00000006()), x0000000B()));
  println (eqBlock (aDD (x00000005(), x00000007()), x0000000C()));
  println (eqBlock (yor (x0000000B(), x00000004()), x0000000F()));
  println (eqBlock (yor (x0000000C(), x00000001()), x0000000D()));
  println (eqBlock (xand (x0000000F(), xFFFFFFF7()), x00000007()));
  println (eqBlock (xand (x0000000D(), xFFFFFFFB()), x00000009()));
  println (eqBlock (mUL1 (x00000007(), x00000007()), x00000031()));
  println (eqBlock (mUL2A (x00000006(), x00000009()), x00000036()));
  println (eqBlock (xOR (x00000031(), x00000036()), x00000007()));
  println (eqBlock (cYC (x00000003()), x00000006()));
  println (eqBlock (xOR (x00000006(), x00000003()), x00000005()));
  println (eqBlock (xOR (xFFFFFFFD(), x00000001()), xFFFFFFFC()));
  println (eqBlock (xOR (xFFFFFFFC(), x00000001()), xFFFFFFFD()));
  println (eqBlock (aDD (x00000005(), xFFFFFFFD()), x00000002()));
  println (eqBlock (aDD (x00000005(), xFFFFFFFC()), x00000001()));
  println (eqBlock (yor (x00000002(), x00000001()), x00000003()));
  println (eqBlock (yor (x00000001(), x00000004()), x00000005()));
  println (eqBlock (xand (x00000003(), xFFFFFFF9()), x00000001()));
  println (eqBlock (xand (x00000005(), xFFFFFFFC()), x00000004()));
  println (eqBlock (mUL1 (xFFFFFFFC(), x00000001()), xFFFFFFFC()));
  println (eqBlock (mUL2A (xFFFFFFFD(), x00000004()), xFFFFFFFA()));
  println (eqBlock (xOR (xFFFFFFFC(), xFFFFFFFA()), x00000006()));
  println (eqBlock (cYC (x00000007()), x0000000E()));
  println (eqBlock (xOR (x0000000E(), x00000007()), x00000009()));
  println (eqBlock (xOR (xFFFFFFFD(), x00000008()), xFFFFFFF5()));
  println (eqBlock (xOR (xFFFFFFFC(), x00000008()), xFFFFFFF4()));
  println (eqBlock (aDD (x00000009(), xFFFFFFF4()), xFFFFFFFD()));
  println (eqBlock (aDD (x00000009(), xFFFFFFF5()), xFFFFFFFE()));
  println (eqBlock (yor (xFFFFFFFD(), x00000001()), xFFFFFFFD()));
  println (eqBlock (yor (xFFFFFFFE(), x00000002()), xFFFFFFFE()));
  println (eqBlock (xand (xFFFFFFFD(), xFFFFFFFE()), xFFFFFFFC()));
  println (eqBlock (xand (xFFFFFFFE(), x7FFFFFFD()), x7FFFFFFC()));
  println (eqBlock (mUL1 (xFFFFFFF5(), xFFFFFFFC()), x0000001E()));
  println (eqBlock (mUL2A (xFFFFFFF4(), x7FFFFFFC()), x0000001E()));
  println (eqBlock (xOR (x0000001E(), x0000001E()), x00000000()));
  println (eqBlock (cYC (x00000001()), x00000002()));
  println (eqBlock (xOR (x00000002(), x00000001()), x00000003()));
  println (eqBlock (xOR (x00000001(), x00000000()), x00000001()));
  println (eqBlock (xOR (x00000002(), x00000000()), x00000002()));
  println (eqBlock (aDD (x00000003(), x00000002()), x00000005()));
  println (eqBlock (aDD (x00000003(), x00000001()), x00000004()));
  println (eqBlock (yor (x00000005(), x00000002()), x00000007()));
  println (eqBlock (yor (x00000004(), x00000001()), x00000005()));
  println (eqBlock (xand (x00000007(), xFFFFFFFB()), x00000003()));
  println (eqBlock (xand (x00000005(), xFFFFFFFB()), x00000001()));
  println (eqBlock (mUL1 (x00000001(), x00000003()), x00000003()));
  println (eqBlock (mUL2A (x00000002(), x00000001()), x00000002()));
  println (eqBlock (xOR (x00000003(), x00000002()), x00000001()));
  main1();
  return 0;
}
void main1 () {
  println (eqBlock (cYC (x00000002()), x00000004()));
  println (eqBlock (xOR (x00000004(), x00000001()), x00000005()));
  println (eqBlock (xOR (x00000003(), x00000001()), x00000002()));
  println (eqBlock (xOR (x00000002(), x00000001()), x00000003()));
  println (eqBlock (aDD (x00000005(), x00000003()), x00000008()));
  println (eqBlock (aDD (x00000005(), x00000002()), x00000007()));
  println (eqBlock (yor (x00000008(), x00000002()), x0000000A()));
  println (eqBlock (yor (x00000007(), x00000001()), x00000007()));
  println (eqBlock (xand (x0000000A(), xFFFFFFFB()), x0000000A()));
  println (eqBlock (xand (x00000007(), xFFFFFFFB()), x00000003()));
  println (eqBlock (mUL1 (x00000002(), x0000000A()), x00000014()));
  println (eqBlock (mUL2A (x00000003(), x00000003()), x00000009()));
  println (eqBlock (xOR (x00000014(), x00000009()), x0000001D()));
  println (eqBlock (cYC (x00000004()), x00000008()));
  println (eqBlock (xOR (x00000008(), x00000001()), x00000009()));
  println (eqBlock (xOR (x00000014(), x00000002()), x00000016()));
  println (eqBlock (xOR (x00000009(), x00000002()), x0000000B()));
  println (eqBlock (aDD (x00000009(), x0000000B()), x00000014()));
  println (eqBlock (aDD (x00000009(), x00000016()), x0000001F()));
  println (eqBlock (yor (x00000014(), x00000002()), x00000016()));
  println (eqBlock (yor (x0000001F(), x00000001()), x0000001F()));
  println (eqBlock (xand (x00000016(), xFFFFFFFB()), x00000012()));
  println (eqBlock (xand (x0000001F(), xFFFFFFFB()), x0000001B()));
  println (eqBlock (mUL1 (x00000016(), x00000012()), x0000018C()));
  println (eqBlock (mUL2A (x0000000B(), x0000001B()), x00000129()));
  println (eqBlock (xOR (x0000018C(), x00000129()), x000000A5()));
  println (eqBlock (cYC (xC4EB1AEB()), x89D635D7()));
  println (eqBlock (xOR (x89D635D7(), xF6A09667()), x7F76A3B0()));
  println (eqBlock (xOR (x21D869BA(), x0A202020()), x2BF8499A()));
  println (eqBlock (xOR (x7792F9D4(), x0A202020()), x7DB2D9F4()));
  println (eqBlock (aDD (x7F76A3B0(), x7DB2D9F4()), xFD297DA4()));
  println (eqBlock (aDD (x7F76A3B0(), x2BF8499A()), xAB6EED4A()));
  println (eqBlock (yor (xFD297DA4(), x02040801()), xFF2D7DA5()));
  println (eqBlock (yor (xAB6EED4A(), x00804021()), xABEEED6B()));
  println (eqBlock (xand (xFF2D7DA5(), xBFEF7FDF()), xBF2D7D85()));
  println (eqBlock (xand (xABEEED6B(), x7DFEFBFF()), x29EEE96B()));
  println (eqBlock (mUL1 (x2BF8499A(), xBF2D7D85()), x0AD67E20()));
  println (eqBlock (mUL2A (x7DB2D9F4(), x29EEE96B()), x30261492()));
  println (eqOctet (pAT (x00FF00FF(), x00000000()), xFF()));
  println (eqPair (preludeXY (x00FF00FF(), x00000000()), BuildPair (x4A645A01(), x50DEC930())));
  println (eqPair (preludeVW (x00FF00FF(), x00000000()), BuildPair (x5CCA3239(), xFECCAA6E())));
  println (eqPair (preludeST (x00FF00FF(), x00000000()), BuildPair (x51EDE9C7(), x24B66FB5())));
  println (eqPair (computeXY_prime (BuildPair (x4A645A01(), x50DEC930()), x55555555(), xOR (nCYC (n1(), x5CCA3239()), xFECCAA6E())), BuildPair (x48B204D6(), x5834A585())));
  println (eqPair (computeXY_prime (BuildPair (x48B204D6(), x5834A585()), xAAAAAAAA(), xOR (nCYC (n2(), x5CCA3239()), xFECCAA6E())), BuildPair (x4F998E01(), xBE9F0917())));
  println (eqPair (computeXY_prime (BuildPair (x4F998E01(), xBE9F0917()), x51EDE9C7(), xOR (nCYC (n3(), x5CCA3239()), xFECCAA6E())), BuildPair (x344925FC(), xDB9102B0())));
  println (eqPair (computeXY_prime (BuildPair (x344925FC(), xDB9102B0()), x24B66FB5(), xOR (nCYC (n4(), x5CCA3239()), xFECCAA6E())), BuildPair (x277B4B25(), xD636250D())));
  println (eqBlock (xOR (x277B4B25(), xD636250D()), xF14D6E28()));
  println (eqOctet (pAT (x00FF00FF(), x00000000()), xFF()));
  println (eqPair (preludeXY (x00FF00FF(), x00000000()), BuildPair (x4A645A01(), x50DEC930())));
  println (eqPair (preludeVW (x00FF00FF(), x00000000()), BuildPair (x5CCA3239(), xFECCAA6E())));
  println (eqPair (preludeST (x00FF00FF(), x00000000()), BuildPair (x51EDE9C7(), x24B66FB5())));
  println (eqPair (computeXY_prime (BuildPair (x4A645A01(), x50DEC930()), xAAAAAAAA(), xOR (nCYC (n1(), x5CCA3239()), xFECCAA6E())), BuildPair (x6AEBACF8(), x9DB15CF6())));
  println (eqPair (computeXY_prime (BuildPair (x6AEBACF8(), x9DB15CF6()), x55555555(), xOR (nCYC (n2(), x5CCA3239()), xFECCAA6E())), BuildPair (x270EEDAF(), xB8142629())));
  println (eqPair (computeXY_prime (BuildPair (x270EEDAF(), xB8142629()), x51EDE9C7(), xOR (nCYC (n3(), x5CCA3239()), xFECCAA6E())), BuildPair (x29907CD8(), xBA92DB12())));
  println (eqPair (computeXY_prime (BuildPair (x29907CD8(), xBA92DB12()), x24B66FB5(), xOR (nCYC (n4(), x5CCA3239()), xFECCAA6E())), BuildPair (x28EAD8B3(), x81D10CA3())));
  println (eqBlock (xOR (x28EAD8B3(), x81D10CA3()), xA93BD410()));
  println (eqOctet (pAT (x55555555(), x5A35D667()), x00()));
  println (eqPair (preludeXY (x55555555(), x5A35D667()), BuildPair (x34ACF886(), x7397C9AE())));
  println (eqPair (preludeVW (x55555555(), x5A35D667()), BuildPair (x7201F4DC(), x2829040B())));
  println (eqPair (preludeST (x55555555(), x5A35D667()), BuildPair (x9E2E7B36(), x13647149())));
  println (eqPair (computeXY_prime (BuildPair (x34ACF886(), x7397C9AE()), x00000000(), xOR (nCYC (n1(), x7201F4DC()), x2829040B())), BuildPair (x2FD76FFB(), x550D91CE())));
  println (eqPair (computeXY_prime (BuildPair (x2FD76FFB(), x550D91CE()), xFFFFFFFF(), xOR (nCYC (n2(), x7201F4DC()), x2829040B())), BuildPair (xA70FC148(), x1D10D8D3())));
  println (eqPair (computeXY_prime (BuildPair (xA70FC148(), x1D10D8D3()), x9E2E7B36(), xOR (nCYC (n3(), x7201F4DC()), x2829040B())), BuildPair (xB1CC1CC5(), x29C1485F())));
  println (eqPair (computeXY_prime (BuildPair (xB1CC1CC5(), x29C1485F()), x13647149(), xOR (nCYC (n4(), x7201F4DC()), x2829040B())), BuildPair (x288FC786(), x9115A558())));
  println (eqBlock (xOR (x288FC786(), x9115A558()), xB99A62DE()));
  println (eqOctet (pAT (x55555555(), x5A35D667()), x00()));
  println (eqPair (preludeXY (x55555555(), x5A35D667()), BuildPair (x34ACF886(), x7397C9AE())));
  println (eqPair (preludeVW (x55555555(), x5A35D667()), BuildPair (x7201F4DC(), x2829040B())));
  println (eqPair (preludeST (x55555555(), x5A35D667()), BuildPair (x9E2E7B36(), x13647149())));
  println (eqPair (computeXY_prime (BuildPair (x34ACF886(), x7397C9AE()), xFFFFFFFF(), xOR (nCYC (n1(), x7201F4DC()), x2829040B())), BuildPair (x8DC8BBDE(), xFE4E5BDD())));
  println (eqPair (computeXY_prime (BuildPair (x8DC8BBDE(), xFE4E5BDD()), x00000000(), xOR (nCYC (n2(), x7201F4DC()), x2829040B())), BuildPair (xCBC865BA(), x0297AF6F())));
  println (eqPair (computeXY_prime (BuildPair (xCBC865BA(), x0297AF6F()), x9E2E7B36(), xOR (nCYC (n3(), x7201F4DC()), x2829040B())), BuildPair (x3CF3A7D2(), x160EE9B5())));
  println (eqPair (computeXY_prime (BuildPair (x3CF3A7D2(), x160EE9B5()), x13647149(), xOR (nCYC (n4(), x7201F4DC()), x2829040B())), BuildPair (xD0482465(), x7050EC5E())));
  println (eqBlock (xOR (xD0482465(), x7050EC5E()), xA018C83B()));
  println (eqPair (preludeXY (xE6A12F07(), x9D15C437()), BuildPair (x21D869BA(), x7792F9D4())));
  println (eqPair (preludeVW (xE6A12F07(), x9D15C437()), BuildPair (xC4EB1AEB(), xF6A09667())));
  println (eqPair (preludeST (xE6A12F07(), x9D15C437()), BuildPair (x6D67E884(), xA511987A())));
  println (eqPair (computeXY_prime (BuildPair (x204E80A7(), x077788A2()), x00000000(), xOR (nCYC (n1(), x17A808FD()), xFEA1D334())), BuildPair (x303FF4AA(), x1277A6D4())));
  println (eqPair (computeXY_prime (BuildPair (x303FF4AA(), x1277A6D4()), x00000000(), xOR (nCYC (n2(), x17A808FD()), xFEA1D334())), BuildPair (x55DD063F(), x4C49AAE0())));
  println (eqPair (computeXY_prime (BuildPair (x55DD063F(), x4C49AAE0()), x00000000(), xOR (nCYC (n3(), x17A808FD()), xFEA1D334())), BuildPair (x51AF3C1D(), x5BC02502())));
  println (eqPair (computeXY_prime (BuildPair (x51AF3C1D(), x5BC02502()), x00000000(), xOR (nCYC (n4(), x17A808FD()), xFEA1D334())), BuildPair (xA44AAAC0(), x63C70DBA())));
  println (eqPair (computeXY_prime (BuildPair (xA44AAAC0(), x63C70DBA()), x00000000(), xOR (nCYC (n5(), x17A808FD()), xFEA1D334())), BuildPair (x4D53901A(), x2E80AC30())));
  println (eqPair (computeXY_prime (BuildPair (x4D53901A(), x2E80AC30()), x00000000(), xOR (nCYC (n6(), x17A808FD()), xFEA1D334())), BuildPair (x5F38EEF1(), x2A6091AE())));
  println (eqPair (computeXY_prime (BuildPair (x5F38EEF1(), x2A6091AE()), x00000000(), xOR (nCYC (n7(), x17A808FD()), xFEA1D334())), BuildPair (xF0239DD5(), x3DD81AC6())));
  println (eqPair (computeXY_prime (BuildPair (xF0239DD5(), x3DD81AC6()), x00000000(), xOR (nCYC (n8(), x17A808FD()), xFEA1D334())), BuildPair (xEB35B97F(), x9372CDC6())));
  println (eqPair (computeXY_prime (BuildPair (xEB35B97F(), x9372CDC6()), x00000000(), xOR (nCYC (n9(), x17A808FD()), xFEA1D334())), BuildPair (x4DA124A1(), xC6B1317E())));
  println (eqPair (computeXY_prime (BuildPair (x4DA124A1(), xC6B1317E()), x00000000(), xOR (nCYC (n10(), x17A808FD()), xFEA1D334())), BuildPair (x7F839576(), x74B39176())));
  println (eqPair (computeXY_prime (BuildPair (x7F839576(), x74B39176()), x00000000(), xOR (nCYC (n11(), x17A808FD()), xFEA1D334())), BuildPair (x11A9D254(), xD78634BC())));
  println (eqPair (computeXY_prime (BuildPair (x11A9D254(), xD78634BC()), x00000000(), xOR (nCYC (n12(), x17A808FD()), xFEA1D334())), BuildPair (xD8804CA5(), xFDC1A8BA())));
  println (eqPair (computeXY_prime (BuildPair (xD8804CA5(), xFDC1A8BA()), x00000000(), xOR (nCYC (n13(), x17A808FD()), xFEA1D334())), BuildPair (x3F6F7248(), x11AC46B8())));
  println (eqPair (computeXY_prime (BuildPair (x3F6F7248(), x11AC46B8()), x00000000(), xOR (nCYC (n14(), x17A808FD()), xFEA1D334())), BuildPair (xACBC13DD(), x33D5A466())));
  println (eqPair (computeXY_prime (BuildPair (xACBC13DD(), x33D5A466()), x00000000(), xOR (nCYC (n15(), x17A808FD()), xFEA1D334())), BuildPair (x4CE933E1(), xC21A1846())));
  println (eqPair (computeXY_prime (BuildPair (x4CE933E1(), xC21A1846()), x00000000(), xOR (nCYC (n16(), x17A808FD()), xFEA1D334())), BuildPair (xC1ED90DD(), xCD959B46())));
  println (eqPair (computeXY_prime (BuildPair (xC1ED90DD(), xCD959B46()), x00000000(), xOR (nCYC (n17(), x17A808FD()), xFEA1D334())), BuildPair (x3CD54DEB(), x613F8E2A())));
  println (eqPair (computeXY_prime (BuildPair (x3CD54DEB(), x613F8E2A()), x00000000(), xOR (nCYC (n18(), x17A808FD()), xFEA1D334())), BuildPair (xBBA57835(), x07C72EAA())));
  println (eqPair (computeXY_prime (BuildPair (xBBA57835(), x07C72EAA()), x00000000(), xOR (nCYC (n19(), x17A808FD()), xFEA1D334())), BuildPair (xD7843FDC(), x6AD6E8A4())));
  println (eqPair (computeXY_prime (BuildPair (xD7843FDC(), x6AD6E8A4()), x00000000(), xOR (nCYC (n20(), x17A808FD()), xFEA1D334())), BuildPair (x5EBA06C2(), x91896CFA())));
  println (eqPair (computeXY_prime (BuildPair (x5EBA06C2(), x91896CFA()), x76232E5F(), xOR (nCYC (n21(), x17A808FD()), xFEA1D334())), BuildPair (x1D9C9655(), x98D1CC75())));
  println (eqPair (computeXY_prime (BuildPair (x1D9C9655(), x98D1CC75()), x4FB1138A(), xOR (nCYC (n22(), x17A808FD()), xFEA1D334())), BuildPair (x7BC180AB(), xA0B87B77())));
  println (eqBlock (mAC (BuildKey (x80018001(), x80018000()), makeMessage (n20(), x00000000(), x00000000())), xDB79FBDC()));
  main2();
}
void main2 () {
  println (eqBlock (mAC (BuildKey (x80018001(), x80018000()), makeMessage (n16(), x00000000(), x07050301())), x8CE37709()));
  println (eqBlock (mAC (BuildKey (x80018001(), x80018000()), makeMessage (n256(), x00000000(), x07050301())), x717153D5()));
  println (eqBlock (mAC (BuildKey (x80018001(), x80018000()), makeMessage (n4100(), x00000000(), x07050301())), x7783C51D()));
}
