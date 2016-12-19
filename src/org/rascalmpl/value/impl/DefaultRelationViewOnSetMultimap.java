package org.rascalmpl.value.impl;

import io.usethesource.capsule.api.deprecated.ImmutableSet;
import io.usethesource.capsule.api.deprecated.ImmutableSetMultimap;
import org.rascalmpl.value.*;
import org.rascalmpl.value.exceptions.IllegalOperationException;
import org.rascalmpl.value.impl.func.SetFunctions;
import org.rascalmpl.value.impl.persistent.PDBPersistentHashSet;
import org.rascalmpl.value.impl.persistent.PDBPersistentHashSetMultimap;
import org.rascalmpl.value.impl.persistent.ValueCollectors;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.util.AbstractTypeBag;

import java.util.Arrays;
import java.util.stream.Stream;

import static io.usethesource.capsule.util.ArrayUtilsInt.arrayOfInt;
import static org.rascalmpl.value.impl.persistent.SetWriter.USE_MULTIMAP_BINARY_RELATIONS;
import static org.rascalmpl.value.impl.persistent.SetWriter.isTupleOfArityTwo;
import static org.rascalmpl.value.util.AbstractTypeBag.toTypeBag;

public class DefaultRelationViewOnSetMultimap implements ISetRelation<ISet> {

  private final static TypeFactory TF = TypeFactory.getInstance();

  protected final IValueFactory vf;
  protected final PDBPersistentHashSetMultimap rel1;

  private void validateSetMultimap(ISet... sets) {
    if (!isSetMultimap(sets)) {
      throw new IllegalArgumentException("Arguments are not multimap-backed binary relations.");
    }
  }

  private boolean isSetMultimap(ISet... sets) {
    boolean conditionHolds =
        Arrays.stream(sets).allMatch(set -> set instanceof PDBPersistentHashSetMultimap);

    return conditionHolds;
  }

  private ImmutableSetMultimap<IValue, IValue> extractSetMultimap(ISet set) {
    validateSetMultimap(set);
    return ((PDBPersistentHashSetMultimap) set).getContent();
  }

  // private AbstractTypeBag extractTypeBag(ISet set) {
  // validateSetMultimap(set);
  // return ((PDBPersistentHashSetMultimap) set).getElementTypeBag();
  // }

  public DefaultRelationViewOnSetMultimap(final IValueFactory vf,
      final PDBPersistentHashSetMultimap rel1) {
    this.vf = vf;
    this.rel1 = rel1;
  }

  @Override
  public ISet compose(ISetRelation<ISet> rel2) {
     return SetFunctions.compose(vf, rel1, rel2.asSet());

//    if (!isSetMultimap(rel2.asSet()))
//      return SetFunctions.compose(vf, rel1, rel2.asSet());
//
//    final ImmutableSetMultimap<IValue, IValue> xy = rel1.getContent();
//    final ImmutableSetMultimap<IValue, IValue> yz = extractSetMultimap(rel2.asSet());
//
//    final TransientSetMultimap<IValue, IValue> xz = xy.asTransient();
//
//    for (IValue x : xy.keySet()) {
//      final ImmutableSet<IValue> ys = xy.get(x);
//      // TODO: simplify expression with nullable data
//      final ImmutableSet<IValue> zs = ys.stream()
//          .flatMap(y -> Optional.ofNullable(yz.get(y)).orElseGet(DefaultTrieSet::of).stream())
//          .collect(CapsuleCollectors.toSet());
//
//      if (zs == null) {
//        xz.__remove(x);
//      } else {
//        // xz.__put(x, zs); // TODO: requires node batch update support
//
//        xz.__remove(x);
//        zs.forEach(z -> xz.__insert(x, z));
//      }
//    }
//
////    // @formatter:off
////    final Stream<BiConsumer<IValue, IValue>> localStream = null;
////    final Node updatedNode = localStream
////        .filter((x, y) -> yz.containsKey(y))
////        .mapValues(y -> yz.get(y))
////        .collect(toNode());
////    // @formatter:on
//
//    final ImmutableSetMultimap<IValue, IValue> data = xz.freeze();
//
//    // final Function<Map.Entry<IValue, IValue>, Type> tupleToTypeMapper =
//    // (tuple) -> TF.tupleType(tuple.getKey().getType(), tuple.getValue().getType());
//    //
//    // final AbstractTypeBag elementTypeBag =
//    // data.entrySet().stream().map(tupleToTypeMapper).collect(toTypeBag());
//
//    final AbstractTypeBag keyTypeBag =
//        data.entrySet().stream().map(Map.Entry::getKey).map(IValue::getType).collect(toTypeBag());
//
//    final AbstractTypeBag valTypeBag =
//        data.entrySet().stream().map(Map.Entry::getValue).map(IValue::getType).collect(toTypeBag());
//
//    // canonicalize
//    if (data.size() == 0) {
//      return PDBPersistentHashSet.EMPTY;
//    } else {
//      /** TODO does not take into account {@link IValueFactory} */
//      return new PDBPersistentHashSetMultimap(keyTypeBag, valTypeBag, data);
//    }
  }

  @Override
  public ISet closure() {
    return SetFunctions.closure(vf, rel1);
  }

  @Override
  public ISet closureStar() {
    return SetFunctions.closureStar(vf, rel1);
  }

  @Override
  public int arity() {
    return rel1.getElementType().getArity();
  }

  @Override
  public ISet project(int... fieldIndexes) {
    if (Arrays.equals(fieldIndexes, arrayOfInt(0)))
      return domain();

    if (Arrays.equals(fieldIndexes, arrayOfInt(1)))
      return range();

    if (Arrays.equals(fieldIndexes, arrayOfInt(0, 1)))
      return rel1;

    // TODO: support fast inverse operator
    if (Arrays.equals(fieldIndexes, arrayOfInt(1, 0)))
      return SetFunctions.project(vf, rel1, fieldIndexes);

    throw new IllegalStateException("Binary relation patterns exhausted.");
  }

  @Override
  public ISet projectByFieldNames(String... fieldNames) {
    final Type fieldTypeType = rel1.getType().getFieldTypes();

    if (!fieldTypeType.hasFieldNames())
      throw new IllegalOperationException("select with field names", rel1.getType());

    final int[] fieldIndices =
        Stream.of(fieldNames).mapToInt(fieldTypeType::getFieldIndex).toArray();

    return project(fieldIndices);
  }

  @Override
  public ISet carrier() {
    return rel1.asRelation().domain().union(rel1.asRelation().range());

    // return SetFunctions.carrier(vf, rel1);
  }

  @Override
  public ISet domain() {
    final ImmutableSetMultimap<IValue, IValue> multimap = rel1.getContent();

    /** TODO change {@link ImmutableSetMultimap#keySet()} to return {@link ImmutableSet} */
    final ImmutableSet<IValue> columnData = (ImmutableSet<IValue>) multimap.keySet();
    final AbstractTypeBag columnElementTypeBag =
        columnData.stream().map(IValue::getType).collect(toTypeBag());

    // // final AbstractTypeBag columnElementTypeBag = extractTypeBag(rel1).select(0);
    // final AbstractTypeBag columnElementTypeBag = rel1.getKeyTypeBag();

    // flattening Set[Tuple[K, V], _] to Multimap[K, V]
    if (USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(columnElementTypeBag.lub())) {
      /*
       * EXPERIMENTAL: Enforce that binary relations always are backed by multi-maps (instead of
       * being represented as a set of tuples).
       */
      final ISetWriter w = vf.setWriter();
      columnData.forEach(w::insert);
      return w.done();
    } else {
      /** TODO does not take into account {@link IValueFactory} */
      return new PDBPersistentHashSet(columnElementTypeBag, columnData);
    }

    // return SetFunctions.domain(vf, rel1);
  }

  @Override
  public ISet range() {
    final ImmutableSetMultimap<IValue, IValue> multimap = rel1.getContent();

    return multimap.values().stream().collect(ValueCollectors.toSet());

//    /** TODO change {@link ImmutableSetMultimap#keySet()} to return {@link ImmutableSet} */
//
//    final TransientSet<IValue> tmp = DefaultTrieSet.transientOf();
//    multimap.values().forEach(tmp::__insert);
//    final ImmutableSet<IValue> columnData = tmp.freeze();
//
//    // final AbstractTypeBag columnElementTypeBag = extractTypeBag(rel1).select(1);
//    final AbstractTypeBag columnElementTypeBag = rel1.getValTypeBag();
//
//    // flattening Set[_, Tuple[K, V]] to Multimap[K, V]
//    if (USE_MULTIMAP_BINARY_RELATIONS && isTupleOfArityTwo.test(columnElementTypeBag.lub())) {
//      /*
//       * EXPERIMENTAL: Enforce that binary relations always are backed by multi-maps (instead of
//       * being represented as a set of tuples).
//       */
//      final ISetWriter w = vf.setWriter();
//      columnData.forEach(w::insert);
//      return w.done();
//    } else {
//      /** TODO does not take into account {@link IValueFactory} */
//      return new PDBPersistentHashSet(columnElementTypeBag, columnData);
//    }

    // return SetFunctions.range(vf, rel1);
  }

  @Override
  public ISet asSet() {
    return rel1;
  }

  @Override
  public String toString() {
    return rel1.toString();
  }

}
