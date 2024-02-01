package xyz.erupt.linq.grammar;

import xyz.erupt.linq.Linq;
import xyz.erupt.linq.consts.JoinMethod;
import xyz.erupt.linq.lambda.SFunction;
import xyz.erupt.linq.schema.JoinSchema;
import xyz.erupt.linq.schema.Row;

import java.util.Collection;
import java.util.function.BiFunction;

public interface Join {

    <T> Linq join(JoinSchema<T> joinSchema);


    <T, S> Linq join(JoinMethod joinMethod, Collection<T> target, SFunction<T, Object> onL, SFunction<S, Object> onR);


    default <T> Linq join(JoinMethod joinMethod, Collection<T> target, BiFunction<T, Row, Boolean> on) {
        return join(new JoinSchema<>(joinMethod, target, on));
    }

//    default <T, S> Linq join(JoinMethod joinMethod, Collection<T> target, SFunction<T, Object> onL, SFunction<S, Object> onR) {
//        Column<T> l = Columns.fromLambda(onL);
//        Column<S> r = Columns.fromLambda(onR);
//        return join(new JoinSchema<>(joinMethod, target, (t1, t2) -> {
//            Object lv = ReflectUtil.getFieldValue(t1, l.getField());
//            Object rv = t2.get(r);
//            if (null == lv && null == rv) {
//                return true;
//            } else if (null == lv || null == rv) {
//                return false;
//            } else {
//                return lv.toString().equals(rv.toString());
//            }
//        }));
//    }

    default <L, R> Linq innerJoin(Collection<L> t, SFunction<L, Object> lon, SFunction<R, Object> ron) {
        return this.join(JoinMethod.INNER, t, lon, ron);
    }

    default <L, R> Linq leftJoin(Collection<L> t, SFunction<L, Object> lon, SFunction<R, Object> ron) {
        return this.join(JoinMethod.LEFT, t, lon, ron);
    }

    default <L, R> Linq rightJoin(Collection<L> t, SFunction<L, Object> lon, SFunction<R, Object> ron) {
        return this.join(JoinMethod.RIGHT, t, lon, ron);
    }

    default <L, R> Linq fullJoin(Collection<L> t, SFunction<L, Object> lon, SFunction<R, Object> ron) {
        return this.join(JoinMethod.FULL, t, lon, ron);
    }

}
