package org.rascalmpl.parser.gtd.stack;

public interface StackNodeVisitor<P> {
    void visit(AlternativeStackNode<P> node);
    void visit(CaseInsensitiveLiteralStackNode<P> node);
    void visit(CharStackNode<P> node);
    void visit(EmptyStackNode<P> node);
    void visit(EpsilonStackNode<P> node);
    void visit(ListStackNode<P> node);
    void visit(LiteralStackNode<P> node);
    void visit(MultiCharacterStackNode<P> node);
    void visit(NonTerminalStackNode<P> node);
    void visit(OptionalStackNode<P> node);
    void visit(RecoveryPointStackNode<P> node);
    void visit(SeparatedListStackNode<P> node);
    void visit(SequenceStackNode<P> node);
    void visit(SkippingStackNode<P> node);
}
