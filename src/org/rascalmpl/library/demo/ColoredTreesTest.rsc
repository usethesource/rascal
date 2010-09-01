module demo::ColoredTreesTest

// Tests

public ColoredTree  rb = red(black(leaf(1), red(leaf(2),leaf(3))), black(leaf(3), leaf(4)));

test cntRed(rb) == 2;
test addLeaves(rb) == 13;
test makeGreen(rb) == green(black(leaf(1),green(leaf(2),leaf(3))),black(leaf(3),leaf(4)));