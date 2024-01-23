package xyz.erupt.eql.query;

import xyz.erupt.eql.schema.Column;
import xyz.erupt.eql.schema.Dql;

import java.util.List;
import java.util.Map;

public abstract class Query {

    public void check(Dql dql) {
        if (null != dql.getOrderBys()) {
            if (null != dql.getGroupBys()) {
//                列 xxx 必须聚合或在 GROUP BY 子句中提及
//                The column serve_user_id must be aggregated or mentioned in the GROUP BY clause
            }

        }
    }


    public abstract List<Map<Column<?>, Object>> dql(Dql dql);

}
