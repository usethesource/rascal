/*******************************************************************************
 * Copyright (c) 2014 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI  
 *******************************************************************************/
package org.rascalmpl.value.util;

import io.usethesource.capsule.DefaultTrieMap;
import io.usethesource.capsule.api.deprecated.ImmutableMap;
import io.usethesource.capsule.api.deprecated.TransientMap;
import io.usethesource.capsule.util.stream.DefaultCollector;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static io.usethesource.capsule.util.collection.AbstractSpecialisedImmutableMap.entryOf;
import static io.usethesource.capsule.util.collection.AbstractSpecialisedImmutableMap.mapOf;
import static io.usethesource.capsule.util.stream.CapsuleCollectors.UNORDERED;

/**
 * Stores mapping (Type -> Integer) to keep track of a collection's element types. The least upper
 * bound type of is calculated on basis of the map keys.
 */
public abstract class AbstractTypeBag implements Cloneable {

  public abstract AbstractTypeBag increase(Type t);

  public abstract AbstractTypeBag decrease(Type t);

  @Deprecated
  public abstract AbstractTypeBag setLabel(String label);

  @Deprecated
  public abstract String getLabel();

  public abstract Type lub();

  public abstract AbstractTypeBag clone();

  public static AbstractTypeBag of(Type... ts) {
    return of(null, ts);
  }

  public static AbstractTypeBag of(String label, Type... ts) {
    return new TypeBag(label, ts);
  }

  public abstract int size();

  // Experimental
  public abstract AbstractTypeBag select(int... fields);

  // Experimental
  // public abstract AbstractTypeBag union(AbstractTypeBag other);

  /**
   * Implementation of <@link AbstractTypeBag/> that cached the current least upper bound.
   */
  private static class TypeBag extends AbstractTypeBag {
    private final String label;
    private final ImmutableMap<Type, Integer> countMap;

    private Type cachedLub;

    private TypeBag(String label, ImmutableMap<Type, Integer> countMap) {
      this(label, countMap, null);
    }

    private TypeBag(String label, ImmutableMap<Type, Integer> countMap, Type cachedLub) {
      this.label = label;
      this.countMap = countMap;
      this.cachedLub = cachedLub;
    }

    private TypeBag(Type... ts) {
      this(null, ts);
    }

    private TypeBag(String label, Type... ts) {
      this.label = label;
      this.countMap = mapOf();

      for (Type t : ts) {
        this.increase(t);
      }
    }

    @Override
    public AbstractTypeBag select(int... fields) {
      final Map<Type, List<Map.Entry<Type, Integer>>> groupedBySelect = countMap.entrySet().stream()
          .map(typeCount -> entryOf(typeCount.getKey().select(fields), typeCount.getValue()))
          .collect(Collectors.groupingBy(Map.Entry::getKey));

      /**
       * TODO: provide immutable collectors TODO: simplify stream expression
       */
      final Map<Type, Integer> mutableCountMap = groupedBySelect.entrySet().stream()
          .map(typeListEntry -> entryOf(typeListEntry.getKey(),
              typeListEntry.getValue().stream().mapToInt(Map.Entry::getValue).sum()))
          .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

      final ImmutableMap<Type, Integer> countMap =
          DefaultTrieMap.<Type, Integer>of().__putAll(mutableCountMap);

      return new TypeBag(label, countMap, cachedLub.select(fields));
    }

    // @Override
    // public AbstractTypeBag union(AbstractTypeBag other) {
    // return null;
    // }

    @Override
    public AbstractTypeBag increase(Type t) {
      final Integer oldCount = countMap.get(t);
      final ImmutableMap<Type, Integer> newCountMap;

      if (oldCount == null) {
        newCountMap = countMap.__put(t, 1);

        if (cachedLub == null) {
          return new TypeBag(label, newCountMap);
        } else {
          // update cached type
          final Type newCachedLub = cachedLub.lub(t);
          return new TypeBag(label, newCountMap, newCachedLub);
        }
      } else {
        newCountMap = countMap.__put(t, oldCount + 1);
        return new TypeBag(label, newCountMap);
      }
    }

    @Override
    public AbstractTypeBag decrease(Type t) {
      final Integer oldCount = countMap.get(t);

      if (oldCount == null) {
        throw new IllegalStateException(String.format("Type '%s' was not present.", t));
      } else if (oldCount > 1) {
        // update and decrease count; lub stays the same
        final ImmutableMap<Type, Integer> newCountMap = countMap.__put(t, oldCount - 1);
        return new TypeBag(label, newCountMap, cachedLub);
      } else {
        // count was zero, thus remove entry and invalidate cached type
        final ImmutableMap<Type, Integer> newCountMap = countMap.__remove(t);
        return new TypeBag(label, newCountMap);
      }
    }

    @Deprecated
    @Override
    public AbstractTypeBag setLabel(String label) {
      return new TypeBag(label, countMap, cachedLub);
    }

    @Deprecated
    @Override
    public String getLabel() {
      return label;
    }

    @Override
    public Type lub() {
      if (cachedLub == null) {
        Type inferredLubType = TypeFactory.getInstance().voidType();
        for (Type t : countMap.keySet()) {
          inferredLubType = inferredLubType.lub(t);
        }
        cachedLub = inferredLubType;
      }
      return cachedLub;
    }

    @Override
    public AbstractTypeBag clone() {
      return new TypeBag(label, countMap);
    }

    @Override
    public String toString() {
      return countMap.toString();
    }

    @Override
    public int size() {
      return countMap.size();
    }

    @Override
    public int hashCode() {
      return Objects.hash(label, countMap);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TypeBag typeBag = (TypeBag) o;
      return Objects.equals(label, typeBag.label) &&
          Objects.equals(countMap, typeBag.countMap);
    }
  }

  public static <M extends TransientMap<Type, Integer>> Collector<Type, ?, ? extends AbstractTypeBag> toTypeBag() {
    final BiConsumer<M, Type> accumulator = (countMap, type0) -> countMap.compute(type0,
        (type1, count) -> count == null ? 1 : count + 1);

    final BinaryOperator<M> combiner = (countMap1, countMap2) -> {
      countMap2.forEach((type, count2) -> {
        final Integer count1 = countMap1.getOrDefault(type, 0);
        countMap1.compute(type, (t, c) -> count1 + count2);
      });

      return countMap1;
    };

    return new DefaultCollector<>((Supplier<M>) DefaultTrieMap::transientOf,
        accumulator, combiner, (countMap) -> new TypeBag(null, countMap.freeze()), UNORDERED);
  }

//  public static <T, K, V> Collector<T, ?, List<AbstractTypeBag>> toTypeBagList(
//      Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
//
//    /** extract key/value from type {@code T} and insert into multimap */
//    final BiConsumer<TransientSetMultimap<K, V>, T> accumulator =
//        (map, element) -> map.__insert(keyMapper.apply(element), valueMapper.apply(element));
//
//    return new CapsuleCollectors.DefaultCollector<>(
//        (Supplier<TransientSetMultimap<K, V>>) DefaultTrieSetMultimap::transientOf, accumulator,
//        (left, right) -> {
//          left.__insertAll(right);
//          return left;
//        }, TransientSetMultimap::freeze, UNORDERED);
//  }

//  public static final Collector<? super Type, TransientMap<Type, Integer>, AbstractTypeBag> toTypeBag2() {
//    return new CapsuleCollectors.DefaultCollector<>(
//        (Supplier<TransientSet<T>>) DefaultTrieSet::transientOf, TransientSet::__insert,
//        (left, right) -> {
//          left.__insertAll(right);
//          return left;
//        }, TransientSet::freeze, UNORDERED);
//  }

//  public static final Collector<? super Type, TransientMap<Type, Integer>, AbstractTypeBag> toTypeBag() {
//    return new Collector<Type, TransientMap<Type, Integer>, AbstractTypeBag>() {
//      @Override
//      public Supplier<TransientMap<Type, Integer>> supplier() {
//        return DefaultTrieMap::transientOf;
//      }
//
//      @Override
//      public BiConsumer<TransientMap<Type, Integer>, Type> accumulator() {
//        return (countMap, type0) -> countMap.compute(type0,
//            (type1, count) -> count == null ? 1 : count + 1);
//      }
//
//      @Override
//      public BinaryOperator<TransientMap<Type, Integer>> combiner() {
//        return (countMap1, countMap2) -> {
//          countMap2.forEach((type, count2) -> {
//            final Integer count1 = countMap1.getOrDefault(type, 0);
//            countMap1.compute(type, (t, c) -> count1 + count2);
//          });
//
//          return countMap1;
//        };
//      }
//
//      @Override
//      public Function<TransientMap<Type, Integer>, AbstractTypeBag> finisher() {
//        return (countMap) -> new TypeBag(null, countMap.freeze());
//      }
//
//      @Override
//      public Set<Characteristics> characteristics() {
//        return Collections.unmodifiableSet(EnumSet.of(Characteristics.UNORDERED));
//      }
//    };
//  }

}
