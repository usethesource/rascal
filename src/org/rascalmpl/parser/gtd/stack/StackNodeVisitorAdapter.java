package org.rascalmpl.parser.gtd.stack;

public class StackNodeVisitorAdapter<P, R> implements StackNodeVisitor<P, R> {

    @Override
    public R visit(AlternativeStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(CaseInsensitiveLiteralStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(CharStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(EmptyStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(EpsilonStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(ListStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(LiteralStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(MultiCharacterStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(NonTerminalStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(OptionalStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(RecoveryPointStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(SeparatedListStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(SequenceStackNode<P> node) {
        // Do nothing fallback
        return null;
    }

    @Override
    public R visit(SkippingStackNode<P> node) {
        // Do nothing fallback
        return null;
    }
}
