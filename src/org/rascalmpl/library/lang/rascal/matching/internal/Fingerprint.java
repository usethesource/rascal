package org.rascalmpl.library.lang.rascal.matching.internal;

import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IInteger;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;

public class Fingerprint {
    private final IValueFactory vf;

    public Fingerprint(IValueFactory vf) {
        this.vf = vf;
    }
    
    public IInteger internalFingerprint(IValue v) {
        return vf.integer(v.getMatchFingerprint());
    }

    public IInteger internalConcreteFingerprint(ITree v) {
        return vf.integer(v.getConcreteMatchFingerprint());
    }

    public IInteger internalHashCode(IValue v) {
        return vf.integer(v.hashCode());
    }
}
