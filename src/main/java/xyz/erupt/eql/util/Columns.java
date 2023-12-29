package xyz.erupt.eql.util;

import xyz.erupt.eql.lambda.LambdaInfo;
import xyz.erupt.eql.lambda.LambdaReflect;
import xyz.erupt.eql.lambda.SFunction;
import xyz.erupt.eql.schema.Column;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Columns {

    public static <T> Column<T> fromLambda(SFunction<T, ?> fun, String alias) {
        LambdaInfo<T> lambdaInfo = LambdaReflect.getInfo(fun);
        Column<T> column = new Column<>();
        column.setTable(lambdaInfo.getClazz());
        column.setField(lambdaInfo.getField());
        if (null == alias) {
            column.setAlias(lambdaInfo.getField());
        } else {
            column.setAlias(alias);
        }
        return column;
    }

    public static <T> Column<T> fromLambda(SFunction<T, ?> fun) {
        return Columns.fromLambda(fun, null);
    }

    public static <T> Column<T> fromField(Field field) {
        Column<T> column = new Column<>();
        column.setTable((Class<T>) field.getDeclaringClass());
        column.setField(field.getName());
        column.setAlias(field.getName());
        return column;
    }

    public static <R> Column<R> all(Class<R> r) {
        Column<R> column = new Column<R>();
        column.setTable(r);
        return column;
    }

    public static <R> Column<R> of(SFunction<R, ?> fun) {
        return Columns.fromLambda(fun);
    }

    public static <R> Column<R> of(SFunction<R, ?> fun, String alias) {
        return Columns.fromLambda(fun, alias);
    }

    public static Column<?> ofs(SFunction<Map<String, ?>, ?> fun, String alias) {
        return Columns.fromLambda(fun, alias);
    }

    public static Column<Void> count(String alias) {
        Column<Void> column = new Column<>(null, null, null, alias);
        column.setGroupByFun(List::size);
        return column;
    }

    public static <R> Column<R> count(SFunction<R, ?> fun, String alias) {
        return groupByProcess(fun, alias, (column, list) -> {
            int i = 0;
            for (Map<Column<?>, ?> map : list) {
                if (null != map.get(column)) i++;
            }
            return i;
        });
    }

    public static <R> Column<R> max(SFunction<R, ?> fun, String alias) {
        return groupByProcess(fun, alias, (column, list) -> {
            int i = 0;
            for (Map<Column<?>, ?> map : list) {
                if (null != map.get(column)) i++;
            }
            return i;
        });
    }

    public static <R> Column<R> min(SFunction<R, ?> fun, String alias) {
        return Columns.fromLambda(fun, alias);
    }


    public static <R> Column<R> avg(SFunction<R, ?> fun, String alias) {
        return groupByProcess(fun, alias, (column, list) -> {
            BigDecimal bigDecimal = new BigDecimal(0);
            int count = 0;
            for (Map<Column<?>, ?> map : list) {
                Object val = map.get(column);
                if (val instanceof Number) {
                    bigDecimal = bigDecimal.add(new BigDecimal(String.valueOf(val)));
                    count++;
                }
            }
            if (count > 0) {
                return bigDecimal.doubleValue() / count;
            } else {
                return 0;
            }
        });
    }


    public static <R> Column<R> sum(SFunction<R, ?> fun, String alias) {
        return groupByProcess(fun, alias, (column, list) -> {
            BigDecimal bigDecimal = new BigDecimal(0);
            for (Map<Column<?>, ?> map : list) {
                Object val = map.get(column);
                if (val instanceof Number) {
                    bigDecimal = bigDecimal.add(new BigDecimal(String.valueOf(val)));
                }
            }
            return bigDecimal.doubleValue();
        });
    }

    //自定义分组处理函数
    public static <R> Column<R> groupByProcess(SFunction<R, ?> fun, String alias, BiFunction<Column<?>, List<Map<Column<?>, ?>>, Object> groupByFun) {
        Column<R> column = Columns.fromLambda(fun, alias);
        column.setGroupByFun(it -> groupByFun.apply(column, it));
        return column;
    }

    //column common process
    public static List<Column<?>> columnsProcess(Column<?>... columns) {
        List<Column<?>> cols = new ArrayList<>();
        for (Column<?> column : columns) {
            if (column.getField() == null) {
                // Column.All → select *
                if (null != column.getTable()) {
                    for (Field field : column.getTable().getDeclaredFields()) {
                        cols.add(new Column<>(column.getTable(), field.getType(), field.getName(), field.getName()));
                    }
                }
            } else {
                cols.add(column);
            }
        }
        return cols;
    }

}
