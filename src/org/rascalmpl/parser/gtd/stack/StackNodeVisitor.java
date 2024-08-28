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
