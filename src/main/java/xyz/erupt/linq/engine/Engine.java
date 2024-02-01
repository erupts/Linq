package xyz.erupt.linq.engine;

import xyz.erupt.linq.exception.EqlException;
import xyz.erupt.linq.schema.Column;
import xyz.erupt.linq.schema.Dql;
import xyz.erupt.linq.schema.JoinSchema;
import xyz.erupt.linq.schema.Row;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Engine {

    public void syntaxCheck(Dql dql) {
        if (dql.getColumns().isEmpty()) {
            throw new EqlException("Missing select definition");
        }
        // alias check
        Map<String, Void> alias = new HashMap<>();
        for (Column column : dql.getColumns()) {
            if (alias.containsKey(column.getAlias())) {
                throw new EqlException("Column '" + column.getAlias() + "' is ambiguous");
            }
            alias.put(column.getAlias(), null);
        }
        // join check
        Map<Class<?>, Void> joinMap = new HashMap<>();
        for (JoinSchema<?> schema : dql.getJoinSchemas()) {
            if (joinMap.containsKey(schema.getClazz())) {
                throw new EqlException("The same object join is not supported " + " → " + schema.getClazz().getSimpleName());
            }
            joinMap.put(schema.getClazz(), null);
        }
    }


    public abstract List<Row> query(Dql dql);

}
