package org.rascalmpl.core.library.lang.rascalcore;

import java.util.Objects;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValueFactory;

public class Performance {
    private final IValueFactory vf;

    public Performance(IValueFactory vf) {
        this.vf = vf;
    }

    private static boolean isSameFile(ISourceLocation inner, ISourceLocation outer) {
        return Objects.equals(inner.getScheme(), outer.getScheme())
            && Objects.equals(inner.getAuthority(), outer.getAuthority())
            && Objects.equals(inner.getPath(), outer.getPath())
            && Objects.equals(inner.getQuery(), outer.getQuery())
            ;
    }

    public IBool isStrictlyContainedIn2(ISourceLocation inner, ISourceLocation outer) {
        if (!isSameFile(inner, outer)) {
            return vf.bool(false);
        }
        // original code would also do full equality, but we don't need that due to the logic in the outer & inner offset compares
        if (inner.hasOffsetLength()) {
            int innerOffset = inner.getOffset();
            int innerLength = inner.getLength();
            if (outer.hasOffsetLength()) {
                int outerOffset = outer.getOffset();
                int outerLength = outer.getLength();
                return vf.bool(
                    innerOffset == outerOffset && innerOffset + innerLength <  outerOffset + outerLength
                    || innerOffset >  outerOffset && innerOffset + innerLength <= outerOffset + outerLength
                );
            }
            else {
                return vf.bool(innerOffset > 0);
            }
        }
        return vf.bool(false);
    }
    
}
