package org.rascalmpl.value.impl.persistent;

import io.usethesource.capsule.DefaultTrieSet;
import io.usethesource.capsule.DefaultTrieSetMultimap;
import io.usethesource.capsule.api.deprecated.Set;
import io.usethesource.capsule.api.deprecated.SetMultimap;
import io.usethesource.capsule.util.stream.DefaultCollector;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ITuple;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.util.AbstractTypeBag;

import java.util.Optional;
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
      Set.Transient<T> set = DefaultTrieSet.transientOf();
    }

    /** extract key/value from type {@code T} and insert into multimap */
    final BiConsumer<SetStruct, T> accumulator = (struct, element) -> {
      if (struct.set.__insert(element)) {
        struct.elementTypeBag = struct.elementTypeBag.increase(element.getType());
      }
    };

    return new DefaultCollector<>(SetStruct::new, accumulator, unsupportedCombiner(),
        struct -> PersistentSetFactory.from(struct.elementTypeBag,
            (Set.Immutable<IValue>) struct.set.freeze()),
        UNORDERED);
  }

  /**
   * @param keyLabel optional label of first column
   * @param valueLabel optional label of second column
   */
  public static <T extends ITuple, K extends IValue, V extends IValue> Collector<T, ?, ISet> toSetMultimap(
      Optional<String> keyLabel, Function<? super T, ? extends K> keyMapper,
      Optional<String> valueLabel, Function<? super T, ? extends V> valueMapper) {

    class SetMultimapStruct {
      AbstractTypeBag keyTypeBag = AbstractTypeBag.of(keyLabel.orElse(null));
      AbstractTypeBag valTypeBag = AbstractTypeBag.of(valueLabel.orElse(null));
      SetMultimap.Transient<K, V> map =
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
        unsupportedCombiner(), struct -> PersistentSetFactory.from(struct.keyTypeBag,
            struct.valTypeBag, (SetMultimap.Immutable<IValue, IValue>) struct.map.freeze()),
        UNORDERED);
  }

  private static <T> BinaryOperator<T> unsupportedCombiner() {
    return (u, v) -> {
      throw new UnsupportedOperationException("Merging is not yet supported.");
    };
  }

}
