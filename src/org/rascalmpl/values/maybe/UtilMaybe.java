package org.rascalmpl.values.maybe;

import org.rascalmpl.values.IRascalValueFactory;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.type.TypeStore;

/**
 * Static factory for Maybe[&A] instances.
 * WARNING: this clones the definitions of util::Maybe until we can reuse compiler-generated code
 */
public class UtilMaybe {
    private static final TypeStore store = new TypeStore();
    private static final TypeFactory TF = TypeFactory.getInstance();
    private static final Type ParameterT = TF.parameterType("A");
    public static final Type Maybe;
    private static final Type Maybe_nothing;
    private static final Type Maybe_just;
 
    static {  
        Maybe = TF.abstractDataType(store, "Maybe", ParameterT);
        Maybe_nothing = TF.constructor(store, Maybe, "nothing");
        Maybe_just = TF.constructor(store, Maybe, "just", ParameterT, "val");
    }

    public static boolean isMaybe(Type t) {
        return t.isSubtypeOf(Maybe);
    }

    /**
     * create `just(val)` of type `Maybe[typeOf(val)]`
     */
    public static IConstructor just(IValue val) {
        return IRascalValueFactory.getInstance().constructor(Maybe_just, val);
    }

    /**
     * Create `nothing()` of type `Maybe[void]`
     */
    public static IConstructor nothing() {
        return IRascalValueFactory.getInstance().constructor(Maybe_nothing);
    }
}
