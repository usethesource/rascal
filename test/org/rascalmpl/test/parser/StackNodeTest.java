package org.rascalmpl.test.parser;

import org.junit.Assert;
import org.junit.Test;
import org.rascalmpl.parser.gtd.stack.EpsilonStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.IEnterFilter;
import org.rascalmpl.parser.gtd.stack.filter.follow.AtEndOfLineRequirement;
import org.rascalmpl.parser.gtd.stack.filter.precede.AtStartOfLineRequirement;

import io.usethesource.vallang.IConstructor;

public class StackNodeTest {
    private static IEnterFilter ENTER = new AtStartOfLineRequirement();
    private static IEnterFilter[] EMPTY_ENTER = new IEnterFilter[0];
    private static IEnterFilter[] SINGLE_ENTER = new IEnterFilter[] { ENTER };
    private static IEnterFilter[] DOUBLE_ENTER = new IEnterFilter[] { ENTER, ENTER };

    private static ICompletionFilter COMPLETION = new AtEndOfLineRequirement();
    private static ICompletionFilter[] EMPTY_COMPLETION = new ICompletionFilter[0];
    private static ICompletionFilter[] SINGLE_COMPLETION = new ICompletionFilter[] { COMPLETION };
    private static ICompletionFilter[] DOUBLE_COMPLETION = new ICompletionFilter[] { COMPLETION, COMPLETION };

    private boolean areEnterFiltersEqual(IEnterFilter[] filters1, IEnterFilter[] filters2) {
        EpsilonStackNode<IConstructor> node1 = new EpsilonStackNode<IConstructor>(0, 0, filters1, null);
        EpsilonStackNode<IConstructor> node2 = new EpsilonStackNode<IConstructor>(0, 0, filters2, null);

        return node1.hasEqualFilters(node2);
    }

    private boolean areCompletionFiltersEqual(ICompletionFilter[] filters1, ICompletionFilter[] filters2) {
        EpsilonStackNode<IConstructor> node1 = new EpsilonStackNode<IConstructor>(0, 0, null, filters1);
        EpsilonStackNode<IConstructor> node2 = new EpsilonStackNode<IConstructor>(0, 0, null, filters2);

        return node1.hasEqualFilters(node2);
    }

    @Test
    public void testHasEqualFiltersMissing() {
        Assert.assertTrue(areEnterFiltersEqual(null, null));
        Assert.assertTrue(areCompletionFiltersEqual(null, null));
        Assert.assertTrue(areEnterFiltersEqual(EMPTY_ENTER, EMPTY_ENTER));
        Assert.assertTrue(areCompletionFiltersEqual(EMPTY_COMPLETION, EMPTY_COMPLETION));
    }

    @Test
    public void testHasEqualFiltersMissingOneSide() {
        Assert.assertFalse(areEnterFiltersEqual(SINGLE_ENTER, null));
        Assert.assertFalse(areEnterFiltersEqual(null, SINGLE_ENTER));
        Assert.assertFalse(areEnterFiltersEqual(SINGLE_ENTER, EMPTY_ENTER));
        Assert.assertFalse(areEnterFiltersEqual(EMPTY_ENTER, SINGLE_ENTER));

        Assert.assertFalse(areCompletionFiltersEqual(SINGLE_COMPLETION, null));
        Assert.assertFalse(areCompletionFiltersEqual(null, SINGLE_COMPLETION));
        Assert.assertFalse(areCompletionFiltersEqual(SINGLE_COMPLETION, EMPTY_COMPLETION));
        Assert.assertFalse(areCompletionFiltersEqual(EMPTY_COMPLETION, SINGLE_COMPLETION));
    }

    @Test
    public void testHasEqualFiltersSingle() {
        Assert.assertTrue(areEnterFiltersEqual(SINGLE_ENTER, SINGLE_ENTER));
        Assert.assertTrue(areCompletionFiltersEqual(SINGLE_COMPLETION, SINGLE_COMPLETION));
    }

    @Test
    public void testHasEqualFiltersLongerLhs() {
        Assert.assertFalse(areEnterFiltersEqual(DOUBLE_ENTER, SINGLE_ENTER));
        Assert.assertFalse(areCompletionFiltersEqual(DOUBLE_COMPLETION, SINGLE_COMPLETION));
    }

    @Test
    public void testHasEqualFiltersLongerRhs() {
        Assert.assertFalse(areEnterFiltersEqual(SINGLE_ENTER, DOUBLE_ENTER));
        Assert.assertFalse(areCompletionFiltersEqual(SINGLE_COMPLETION, DOUBLE_COMPLETION));
    }

}
