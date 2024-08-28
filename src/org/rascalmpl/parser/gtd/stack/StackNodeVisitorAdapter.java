package org.rascalmpl.parser.gtd.stack;

public class StackNodeVisitorAdapter<P> implements StackNodeVisitor<P> {

    @Override
    public void visit(AlternativeStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(CaseInsensitiveLiteralStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(CharStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(EmptyStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(EpsilonStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(ListStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(LiteralStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(MultiCharacterStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(NonTerminalStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(OptionalStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(RecoveryPointStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(SeparatedListStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(SequenceStackNode<P> node) {
        // Do nothing fallback
    }

    @Override
    public void visit(SkippingStackNode<P> node) {
        // Do nothing fallback
    }
}
