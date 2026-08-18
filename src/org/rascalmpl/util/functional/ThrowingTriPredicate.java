/*
 * Copyright (c) 2025, Swat.engineering
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.util.functional;

import java.util.Objects;

@FunctionalInterface
public interface ThrowingTriPredicate<T, U, V, E extends Exception> {
    boolean test(T t, U u, V v) throws E;

    default ThrowingTriPredicate<T, U, V, E> and(ThrowingTriPredicate<? super T, ? super U, ? super V, ? extends E> other) throws E {
        Objects.requireNonNull(other);
        return (t, u, v) -> test(t, u, v) && other.test(t ,u, v);
    }

    default ThrowingTriPredicate<T, U, V, E> or(ThrowingTriPredicate<? super T, ? super U, ? super V, ? extends E> other) throws E {
        Objects.requireNonNull(other);
        return (t, u, v) -> test(t, u, v) || other.test(t, u, v);
    }

    default ThrowingTriPredicate<T, U, V, E> negate() throws E {
        return (t, u, v) -> !test(t, u, v);
    }
}
