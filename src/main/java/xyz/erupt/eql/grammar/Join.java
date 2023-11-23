package xyz.erupt.eql.grammar;

import xyz.erupt.eql.Linq;
import xyz.erupt.eql.consts.JoinMethod;
import xyz.erupt.eql.fun.LambdaInfo;
import xyz.erupt.eql.fun.LambdaReflect;
import xyz.erupt.eql.fun.SFunction;
import xyz.erupt.eql.schema.Column;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;

public interface Join {

    <T> Linq join(JoinMethod joinMethod, Collection<T> target, BiFunction<Map<Column<T>, ?>, Map<Column<?>, ?>, Boolean> on);


    default <T, S> Linq join(JoinMethod joinMethod, Collection<T> target, SFunction<T, Object> onL, SFunction<S, Object> onR) {
        Column<T> l = Column.fromLambda(onL);
        Column<S> r = Column.fromLambda(onR);
        return this.join(joinMethod, target, (t1, t2) -> t1.get(l).equals(t2.get(r)));
    }

//    default <T> Linq leftJoin(Collection<T> t, SFunction<T, Object> lon, SFunction<Source, Object> ron) {
//        return this.join(JoinMethod.LEFT, t, lon, ron);
//    }

}
