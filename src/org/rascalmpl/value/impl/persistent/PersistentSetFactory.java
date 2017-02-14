/*******************************************************************************
 * Copyright (c) 2013-2014 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.value.impl.persistent;

import io.usethesource.capsule.api.Set;
import io.usethesource.capsule.api.SetMultimap;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.util.AbstractTypeBag;

import static org.rascalmpl.value.impl.persistent.EmptySet.EMPTY_SET;
import static org.rascalmpl.value.impl.persistent.SetWriter.asInstanceOf;
import static org.rascalmpl.value.impl.persistent.SetWriter.isTupleOfArityTwo;

/**
 * Smart constructors for choosing (or converting to) the most appropriate representations based on
 * dynamic types and data.
 */
public class PersistentSetFactory {

  /**
   * Creating an {@link ISet} instance from a {@link SetMultimap.Immutable} representation.
   *
   * @param keyTypeBag precise dynamic type of first data column
   * @param valTypeBag precise dynamic type of second data column
   * @param content internal multi-map representation of an {@link ISet}
   * @return appropriate {@link ISet} based on data and type
   */
  static final ISet from(final AbstractTypeBag keyTypeBag, final AbstractTypeBag valTypeBag,
      final SetMultimap.Immutable<IValue, IValue> content) {

    if (content.isEmpty()) {
      return EMPTY_SET;
    }

    // keep current representation
    return new PersistentHashIndexedBinaryRelation(keyTypeBag, valTypeBag, content);
  }

  /**
   * Creating an {@link ISet} instance from a {@link SetMultimap.Immutable} representation.
   *
   * @param elementTypeBag precise dynamic type of elements of a collection
   * @param content internal set representation of an {@link ISet}
   * @return appropriate {@link ISet} based on data and type
   */
  static final ISet from(final AbstractTypeBag elementTypeBag,
      final Set.Immutable<IValue> content) {

    final Type elementType = elementTypeBag.lub();

    if (elementType.isBottom()) {
      return EMPTY_SET;
    }

    if (isTupleOfArityTwo.test(elementType)) {
      // convert to binary relation
      return content.stream().map(asInstanceOf(ITuple.class))
          .collect(ValueCollectors.toSetMultimap(elementType.getOptionalFieldName(0),
              tuple -> tuple.get(0), elementType.getOptionalFieldName(1), tuple -> tuple.get(1)));
    }

    // keep current representation
    return new PersistentHashSet(elementTypeBag, content);
  }

}
