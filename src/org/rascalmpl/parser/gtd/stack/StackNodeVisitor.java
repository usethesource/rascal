/**
 * Copyright (c) 2022, NWO-I Centrum Wiskunde & Informatica (CWI)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

 package org.rascalmpl.parser.gtd.stack;

public interface StackNodeVisitor<P, R> {
    R visit(AlternativeStackNode<P> node);
    R visit(CaseInsensitiveLiteralStackNode<P> node);
    R visit(CharStackNode<P> node);
    R visit(EmptyStackNode<P> node);
    R visit(EpsilonStackNode<P> node);
    R visit(ListStackNode<P> node);
    R visit(LiteralStackNode<P> node);
    R visit(MultiCharacterStackNode<P> node);
    R visit(NonTerminalStackNode<P> node);
    R visit(OptionalStackNode<P> node);
    R visit(RecoveryPointStackNode<P> node);
    R visit(SeparatedListStackNode<P> node);
    R visit(SequenceStackNode<P> node);
    R visit(SkippingStackNode<P> node);
}
