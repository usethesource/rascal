package org.rascalmpl.value.impl.persistent;

import io.usethesource.capsule.DefaultTrieSet;
import io.usethesource.capsule.DefaultTrieSetMultimap;
import io.usethesource.capsule.api.deprecated.ImmutableSet;
import io.usethesource.capsule.api.deprecated.ImmutableSetMultimap;
import io.usethesource.capsule.api.deprecated.TransientSet;
import io.usethesource.capsule.api.deprecated.TransientSetMultimap;
import io.usethesource.capsule.util.stream.DefaultCollector;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.util.AbstractTypeBag;

import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;

import static io.usethesource.capsule.util.stream.CapsuleCollectors.UNORDERED;
import static org.rascalmpl.value.impl.persistent.SetWriter.equivalenceEqualityComparator;

public class ValueCollectors {

  public static <T extends IValue> Collector<T, ?, ISet> toSet() {

    class SetStruct {
      AbstractTypeBag elementTypeBag = AbstractTypeBag.of();
      TransientSet<T> set = DefaultTrieSet.transientOf();
    }

    /** extract key/value from type {@code T} and insert into multimap */
    final BiConsumer<SetStruct, T> accumulator = (struct, element) -> {
      if (struct.set.__insert(element)) {
        struct.elementTypeBag = struct.elementTypeBag.increase(element.getType());
      }
    };

    return new DefaultCollector<>(SetStruct::new, accumulator,
        unsupportedCombiner(), struct -> new PDBPersistentHashSet(struct.elementTypeBag,
            (ImmutableSet<IValue>) struct.set.freeze()),
        UNORDERED);
  }

  public static <T, K extends IValue, V extends IValue> Collector<T, ?, ISet> toSetMultimap(
      Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {

    class SetMultimapStruct {
      AbstractTypeBag keyTypeBag = AbstractTypeBag.of();
      AbstractTypeBag valTypeBag = AbstractTypeBag.of();
      TransientSetMultimap<K, V> map =
          DefaultTrieSetMultimap.transientOf(equivalenceEqualityComparator);
    }

    /** extract key/value from type {@code T} and insert into multimap */
    final BiConsumer<SetMultimapStruct, T> accumulator = (struct, element) -> {
      final K key = keyMapper.apply(element);
      final V val = valueMapper.apply(element);

      if (struct.map.__insert(key, val)) {
        struct.keyTypeBag = struct.keyTypeBag.increase(key.getType());
        struct.valTypeBag = struct.valTypeBag.increase(val.getType());
      }
    };

    return new DefaultCollector<>(SetMultimapStruct::new, accumulator,
        unsupportedCombiner(), struct -> new PDBPersistentHashSetMultimap(struct.keyTypeBag,
            struct.valTypeBag, (ImmutableSetMultimap<IValue, IValue>) struct.map.freeze()),
        UNORDERED);
  }

  private static <T> BinaryOperator<T> unsupportedCombiner() {
    return (u, v) -> {
      throw new UnsupportedOperationException("Merging is not yet supported.");
    };
  }

}
