package org.rascalmpl.value.io.binary.util;

public class TaggedInt {

    private static final int TAG_BITS = 3;
    private static final int TAG_MASK = 0b111;
    static {
        assert (1 << (TAG_BITS - 1)) == Integer.highestOneBit(TAG_MASK);
    }
    
    public static int make(final int original, final int tag) {
        assert (tag & TAG_MASK) == tag;
        return (original << TAG_BITS) | tag;
    }
    
    public static int getOriginal(final int i) {
        return i >>> TAG_BITS;
    }
    
    public static int getTag(final int i) {
        return i & TAG_MASK;
    }

}
