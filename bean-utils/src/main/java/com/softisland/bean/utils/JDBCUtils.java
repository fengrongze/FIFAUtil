package com.softisland.bean.utils;

import com.softisland.bean.bean.PageInfo;
import com.softisland.common.utils.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.*;

/**
 * Created by 卫星 on 2014/11/12.
 */
//@Component(value="jdbcUtils")
public final class JDBCUtils {

    /**
     * 查询关键字KEY
     */
    public static final String SELECT_KEY_LIKE = "like";

    /**
     * 查询关键字between
     */
    public static final String SELECT_KEY_BETWEEN = "between";

    /**
     * 查询关键字in
     */
    public static final String SELECT_KEY_IN = "in";
    /**
     * 查询关键字between
     */
    public static final String SELECT_KEY_DATE_BETWEEN = "date_between";

    /**
     * 查询关键字大于等于
     */
    public static final String SELECT_KEY_MORE_THANEQ="more_than_equal";
    /**
     * 查询关键字大于
     */
    public static final String SELECT_KEY_MORE_THAN="more_than";
    /**
     * 查询关键字小于
     */
    public static final String SELECT_KEY_LESS_THANEQ="less_than_equal";
    /**
     * 查询关键字小于
     */
    public static final String SELECT_KEY_LESS_THAN="less_than";


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private SimpleJdbcCall simpleJdbcCall;

    /**
     * 查询返回单个INT值
     * @param sql demo:select count(*) from table where name = ?
     * @param objects 参数列表
     * @return
     * @throws Exception
     */
    public int queryForInt(String sql,Object... objects)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return jdbcTemplate.queryForObject(sql, objects, Integer.class);
    }

    /**
     * 查詢返回单个INT值
     * @param sql demo:select count(*) from table
     * @return
     * @throws Exception
     */
    public int queryForInt(String sql) throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 查询返回单个Map值
     * @param sql demo:select count(*) from table where name = ?
     * @param isCoverNullToEmpty 是否将查出为空的字段转换成空字符串
     * @param objects 参数列表
     * @return
     * @throws Exception
     */
    public Map<String, Object> queryForMap(String sql,boolean isCoverNullToEmpty,Object... objects)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, objects);
        if(sqlRowSet.next()){
            SqlRowSetMetaData metaData = sqlRowSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String,Object> map = new HashMap<>(metaData.getColumnCount());
            while(columnCount>0){
                if(isCoverNullToEmpty==true){
                    map.put(metaData.getColumnLabel(columnCount),sqlRowSet.getObject(columnCount)==null?"":sqlRowSet.getObject(columnCount));
                }else{
                    map.put(metaData.getColumnLabel(columnCount),sqlRowSet.getObject(columnCount));
                }
                columnCount--;
            }
            return map;
        }else{
            return new HashMap<>(0);
        }
    }

    /**
     * 当不确定查询结果是否有值时使用
     * @param sql
     * @param objects
     * @return
     * @throws Exception
     */
    public String queryForStringWhenResultMabyNull(String sql,Object... objects)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, objects);
        if (sqlRowSet.next()){
            return sqlRowSet.getString(1);
        }else{
            return "";
        }
    }

    /**
     * 查询返回单个INT值
     * @param sql demo:select count(*) from table where name = ?
     * @param objects 参数列表
     * @return
     * @throws Exception
     */
    public long queryForLong(String sql,Object... objects)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return jdbcTemplate.queryForObject(sql, objects, Long.class);
    }

    /**
     * 查询返回单个String值
     * @param sql demo:select name from table where name= ?
     * @param objects 参数列表
     * @return
     * @throws Exception
     */
    public String queryForString(String sql,Object... objects)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return jdbcTemplate.queryForObject(sql, objects, String.class);
    }

    /**
     * 通过SQL获取一个INT值
     * @param sql demo:select count(*) from table where name= :name;
     * @param namedParameters 对Map的封装，具体实现类为：BeanPropertySqlParameterSource和MapSqlParameterSource
     *                        demo:SqlParameterSource in = new BeanPropertySqlParameterSource("javabean对象");//javabean对象中有name属性和值
     *                        SqlParameterSource in = new MapSqlParameterSource().addValue("name","ZHANGSAN");
     * @return
     * @throws Exception
     */
    public int queryForInt(String sql,SqlParameterSource namedParameters)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
    }

    /**
     * 通过SQL获取一个INT值
     * @param sql demo:select count(*) from table where name= :name;
     * @param namedParameters 对Map的封装，具体实现类为：BeanPropertySqlParameterSource和MapSqlParameterSource
     *                        demo:SqlParameterSource in = new BeanPropertySqlParameterSource("javabean对象");//javabean对象中有name属性和值
     *                        SqlParameterSource in = new MapSqlParameterSource().addValue("name","ZHANGSAN");
     * @return
     * @throws Exception
     */
    public long queryForLong(String sql,SqlParameterSource namedParameters)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Long.class);
    }

    /**
     * 通过SQL获取一个String值
     * @param sql demo:select name from table where name= :name;
     * @param namedParameters  对Map的封装，具体实现类为：BeanPropertySqlParameterSource和MapSqlParameterSource
     *                        demo:SqlParameterSource in = new BeanPropertySqlParameterSource("javabean对象");//javabean对象中有name属性和值
     *                        SqlParameterSource in = new MapSqlParameterSource().addValue("name","ZHANGSAN");
     * @return
     * @throws Exception
     */
    public String queryForString(String sql,SqlParameterSource namedParameters)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
    }

    /**
     * 通过SQL获取一个List
     * @param sql demo:select * from table where name = ?
     * @param objects 参数列表
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> queryForList(String sql,Object... objects)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return jdbcTemplate.queryForList(sql, objects);
    }

    /**
     * 返回单列的字符串数组
     * @param sql
     * @param objects
     * @return
     * @throws Exception
     */
    public List<String> queryForListString(String sql,Object... objects)throws Exception{
        return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> {
            return rs.getString(1);
        }, objects);
    }

    /**
     * 通过SQL获取一个List
     * @param sql dmeo: select * from table where name = :name
     * @param namedParameters  对Map的封装，具体实现类为：BeanPropertySqlParameterSource和MapSqlParameterSource
     *                        demo:SqlParameterSource in = new BeanPropertySqlParameterSource("javabean对象");//javabean对象中有name属性和值
     *                        SqlParameterSource in = new MapSqlParameterSource().addValue("name","ZHANGSAN");
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> queryForList(String sql,SqlParameterSource namedParameters) throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return namedParameterJdbcTemplate.queryForList(sql, namedParameters);
    }

    /**
     * 分页查询数据，根据一个字段排序，升序
     * @param tableName
     * @param parameter
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageInfo queryForPageListOrderByAsc(String tableName, Map<String,Object> parameter, int start, int pageSize, String orderByColumn) throws Exception{
        Map<String,String> orderByMap = new HashMap<>(1);
        orderByMap.put(orderByColumn,"asc");
        return queryForPageList(tableName, null, parameter, orderByMap, start, pageSize);
    }

    /**
     * 分页查询数据,没有排序功能
     * @param tableName
     * @param parameter
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageInfo queryForPageList(String tableName, Map<String,Object> parameter, int start, int pageSize) throws Exception{
        return queryForPageList(tableName, null, parameter, null, start, pageSize);
    }

    /**
     * 分页查询数据，根据一个字段排序，倒序
     * @param tableName
     * @param parameter
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageInfo queryForPageListOrderByDesc(String tableName,Map<String,Object> parameter,int start,int pageSize,String orderByColumn) throws Exception{
        Map<String,String> orderByMap = new HashMap<>(1);
        orderByMap.put(orderByColumn, "desc");
        return queryForPageList(tableName, null, parameter, orderByMap, start, pageSize);
    }


    /**
     * 获取分页查询统计
     * @param tableName
     * @param whereCondition
     * @param parameterSource
     * @return
     * @throws Exception
     */
    public int queryForPageCount(String tableName,StringBuilder whereCondition,MapSqlParameterSource parameterSource)throws Exception{
        StringBuilder sb = new StringBuilder();
        if(tableName.indexOf("select ")>-1){
            sb.append("select count(*) from (").append(tableName).append(")as A_ where 1=1 ");
        }else{
            sb.append("select count(*) from ").append(tableName).append(" where 1=1 ");
        }
        sb.append(whereCondition);
        return queryForInt(sb.toString(), parameterSource);
    }

    /**
     * 分页查询数据，可以ORDERBY多个字段
     * @param tableName
     * @param parameter
     * @param orderByColumnMap 需要排序的字段，可以有多个，Map中KEY为字段，value为升序(asc)还是降序(desc)
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageInfo queryForPageList(String tableName, Map<String,Object> parameter, Map<String,String> orderByColumnMap, int start, int pageSize) throws Exception{
        return queryForPageList(tableName, null, parameter, orderByColumnMap, start, pageSize);
    }

    /**
     * 根据表名或者sql获取SqlRowSetMetaData
     * @param tableNameOrSql
     * @return
     * @throws Exception
     */
    public Map<Object,Object> getTableColumnsType(String tableNameOrSql)throws Exception{

        SqlRowSetMetaData sqlRowSetMetaData;
        if(tableNameOrSql.indexOf("select ")>-1){
            sqlRowSetMetaData = jdbcTemplate.queryForRowSet("select * from (" + tableNameOrSql + ")as A_ where 1=2").getMetaData();
        }else{
            sqlRowSetMetaData = jdbcTemplate.queryForRowSet("select * from " + tableNameOrSql + " where 1=2").getMetaData();
        }
        Map<Object,Object> columnTypes = new HashMap<>(sqlRowSetMetaData.getColumnCount());
        int i=1;
        for(String columnName : sqlRowSetMetaData.getColumnNames()){
            columnTypes.put(columnName,sqlRowSetMetaData.getColumnType(i));
            i++;
        }
        return columnTypes;
    }

    /**
     * 分页查询数据，可以ORDERBY多个字段
     * @param tableName
     * @param columns 需要指定的查询列名
     * @param parameter
     * @param orderByColumnMap 需要排序的字段，可以有多个，Map中KEY为字段，value为升序(asc)还是降序(desc)
     * @param start
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageInfo queryForPageList(String tableName,String[] columns,Map<String,Object> parameter,Map<String,String> orderByColumnMap,int start,int pageSize) throws Exception{

        Map<Object,Object> columnsTypes = getTableColumnsType(tableName);

        StringBuilder sb = new StringBuilder("select ");
        if(null != columns && columns.length != 0){
            Arrays.asList(columns).forEach(v->sb.append(v).append(","));
            sb.delete(sb.length()-1,sb.length());
        }else{
            sb.append(" * ");
        }
        if(tableName.indexOf("select ")>-1){
            sb.append(" from (").append(tableName).append(")as A_ where 1=1 ");
        }else {
            sb.append(" from ").append(tableName).append(" where 1=1 ");
        }
        //拼接查询条件
        StringBuilder whereCondition = joinWhereCondition(parameter);
        sb.append(whereCondition);
        //order by 条件
        appendOrderByCondition(orderByColumnMap, sb);
        sb.append(" limit :pageSize OFFSET :start");

        MapSqlParameterSource parameterSource = getCoveredParamValue(parameter, columnsTypes);
        //总数量
        int total = queryForPageCount(tableName, whereCondition, parameterSource);
        //每页数据
        parameterSource.addValue("pageSize",pageSize < 0 ? 0 : pageSize);
        parameterSource.addValue("start", start < 0 ? 0 : start);
        //列表
        List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(sb.toString(), parameterSource);

        return new PageInfo(list,total);
    }

    /**
     * 根据数据字段类型将查询条件的值做相应的数据类型转换
     * @param parameter
     * @param columnsTypes
     * @return
     */
    private MapSqlParameterSource getCoveredParamValue(Map<String, Object> parameter, Map<Object,Object> columnsTypes)throws Exception{
        String[] columnNames = columnsTypes.keySet().toArray(new String[columnsTypes.keySet().size()]);
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        if(null != parameter && !parameter.isEmpty()){
            //获取所有的查询条件
            for(Iterator<String> iterator=parameter.keySet().iterator();iterator.hasNext();){
                String columnMix = iterator.next();
                String columnName = columnMix.split("\\$\\$")[0];
                String whereKey = "=";
                if(columnMix.split("\\$\\$").length>1){
                    whereKey =columnMix.split("\\$\\$")[1];
                }
                for(String dataColumnName : columnNames){
                    if(columnName.equals(dataColumnName)){
                        int type = (Integer)columnsTypes.get(columnName);
                        String whereValue = parameter.get(columnMix).toString();
                        switch (whereKey){
                            case SELECT_KEY_BETWEEN:
                            case SELECT_KEY_DATE_BETWEEN:
                                getBetweenParamSource(parameterSource, columnName, type, whereValue);
                                break;
                        /*case Constants.SELECT_KEY_MORE_THANEQ:
                        case Constants.SELECT_KEY_MORE_THAN:
                        case Constants.SELECT_KEY_LESS_THAN:
                        case Constants.SELECT_KEY_LESS_THANEQ:
                        case Constants.SELECT_KEY_LIKE:*/
                            default:
                                getCommonParamSource(parameterSource, columnName, type, whereValue);
                                break;

                        }
                    }
                }
            }
        }
        return parameterSource;
    }

    /**
     * 获取只有单个字段只有一个值得ParamSource
     * @param parameterSource
     * @param columnName
     * @param type
     * @param whereValue
     * @throws Exception
     */
    private void getCommonParamSource(MapSqlParameterSource parameterSource, String columnName, int type, String whereValue) throws Exception {
        switch (type){
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.BIGINT:
                parameterSource.addValue(columnName,Integer.parseInt(whereValue),type);
                break;
            case Types.DOUBLE:
            case Types.DECIMAL:
                parameterSource.addValue(columnName,Double.parseDouble(whereValue),type);
                break;
            case Types.FLOAT:
                parameterSource.addValue(columnName,Float.parseFloat(whereValue),type);
                break;
            default:
                parameterSource.addValue(columnName,whereValue,type);
                break;
        }
    }
    /**
     * 获取只有单个字段有多个值得ParamSource
     * @param parameterSource
     * @param columnName
     * @param type
     * @param whereValue
     * @throws Exception
     */
    private void getBetweenParamSource(MapSqlParameterSource parameterSource, String columnName, int type, String whereValue) throws Exception {
        switch (type){
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.BIGINT:
                parameterSource.addValue(columnName+"_MIN",Integer.parseInt(whereValue.split(";")[0]),type);
                parameterSource.addValue(columnName+"_MAX",Integer.parseInt(whereValue.split(";")[1]),type);
                break;
            case Types.DOUBLE:
            case Types.DECIMAL:
                parameterSource.addValue(columnName+"_MIN",Double.parseDouble(whereValue.split(";")[0]),type);
                parameterSource.addValue(columnName+"_MAX",Double.parseDouble(whereValue.split(";")[1]),type);
                break;
            case Types.FLOAT:
                parameterSource.addValue(columnName+"_MIN",Float.parseFloat(whereValue.split(";")[0]),type);
                parameterSource.addValue(columnName+"_MAX",Float.parseFloat(whereValue.split(";")[1]), type);
                break;
            case Types.DATE:
                parameterSource.addValue(columnName+"_MIN", DateTimeUtil.getDateFromStrDate(whereValue.split(";")[0]),type);
                parameterSource.addValue(columnName+"_MAX", DateTimeUtil.getDateFromStrDate(whereValue.split(";")[1]), type);
                break;
            case Types.TIME:
            case Types.TIMESTAMP:
                parameterSource.addValue(columnName+"_MIN",DateTimeUtil.getTimeStampByStrDateTime(whereValue.split(";")[0]),type);
                parameterSource.addValue(columnName+"_MAX",DateTimeUtil.getTimeStampByStrDateTime(whereValue.split(";")[1]), type);
                break;
            default:
                parameterSource.addValue(columnName+"_MIN",whereValue.split(";")[0],type);
                parameterSource.addValue(columnName+"_MAX",whereValue.split(";")[1], type);
                break;
        }
    }

    /**
     *排序查询
     * @param tableName
     * @param columns
     * @param parameter
     * @param orderByColumnMap
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> queryForOrderBy(String tableName,String[] columns,Map<String,Object> parameter,Map<String,String> orderByColumnMap) throws Exception{
        StringBuilder sb = new StringBuilder("select ");
        if(null != columns && columns.length != 0){
            Arrays.asList(columns).forEach(v->sb.append(v).append(","));
            sb.delete(sb.length()-1,sb.length());
        }else{
            sb.append(" * ");
        }
        sb.append(" from ").append(tableName).append(" where 1=1 ");
        //拼接查询条件
        sb.append(joinWhereCondition(parameter));
        //order by 条件
        appendOrderByCondition(orderByColumnMap, sb);
        List<Map<String,Object>> list = namedParameterJdbcTemplate.queryForList(sb.toString(), getCoveredParamValue(parameter, getTableColumnsType(tableName)));
        return list;
    }

    /**
     * 拼接排序条件
     * @param orderByColumnMap
     * @param sb
     */
    private void appendOrderByCondition(Map<String, String> orderByColumnMap, StringBuilder sb) {
        int i=0;
        if(null != orderByColumnMap && !orderByColumnMap.isEmpty()){
            sb.append(" order by ");
            for(Iterator<String> it = orderByColumnMap.keySet().iterator(); it.hasNext();){
                String orderKey = it.next();
                sb.append(orderKey).append(" ").append(orderByColumnMap.get(orderKey));
                if(i!=orderByColumnMap.size()-1){
                    sb.append(",");
                }else{
                    sb.append(" ");
                }
            }
        }
    }

    /**
     * 拼接查询条件
     * @param parameter
     * @return
     */
    private StringBuilder joinWhereCondition(Map<String, Object> parameter) {
        if(null != parameter && !parameter.isEmpty()){
            StringBuilder sb = new StringBuilder();
            //java lambda表达式
            parameter.keySet().forEach(v -> {
                sb.append(" and ");
                String[] columnAndSelectKey = v.split("\\$\\$");

                if (columnAndSelectKey.length < 2) {
                    sb.append(columnAndSelectKey[0]);
                    sb.append("= :");
                    sb.append(columnAndSelectKey[0]);
                } else {
                    String key = columnAndSelectKey[1];
                    Object paramValue = parameter.get(v);
                    switch (key) {
                        case SELECT_KEY_MORE_THANEQ:
                            sb.append(columnAndSelectKey[0]).append(">=").append(" :").append(columnAndSelectKey[0]);
                            break;
                        case SELECT_KEY_MORE_THAN:
                            sb.append(columnAndSelectKey[0]).append(">").append(" :").append(columnAndSelectKey[0]);
                            break;
                        case SELECT_KEY_LESS_THAN:
                            sb.append(columnAndSelectKey[0]).append("<").append(" :").append(columnAndSelectKey[0]);
                            break;
                        case SELECT_KEY_LESS_THANEQ:
                            sb.append(columnAndSelectKey[0]).append("<=").append(" :").append(columnAndSelectKey[0]);
                            break;
                        case SELECT_KEY_LIKE:
                            sb.append(columnAndSelectKey[0]);
                            sb.append(" like concat('%',").append(" :").append(columnAndSelectKey[0]).append(",'%')") ;
                            break;
                        case SELECT_KEY_BETWEEN:
                        case SELECT_KEY_DATE_BETWEEN:
                            sb.append(columnAndSelectKey[0]).append(">=").append(" :").append(columnAndSelectKey[0]).append("_MIN ");
                            sb.append(" and ").append(columnAndSelectKey[0]).append("<").append(" :").append(columnAndSelectKey[0]).append("_MAX ");
                            break;
                        case SELECT_KEY_IN:
                            String insValue = paramValue.toString();
                            sb.append(columnAndSelectKey[0]).append(" in (");

                            if (insValue.indexOf(";") > 0) {
                                String[] values = insValue.split(";");
                                for (int i = 0; i < values.length; i++) {
                                    sb.append("'").append(values[i]).append("'");
                                    if (i != insValue.split(";").length - 1) {
                                        sb.append(",");
                                    }
                                }
                            } else {
                                sb.append("'").append(insValue).append("'");
                            }
                            sb.append(")");
                            break;

                    }
                }

            });
            return sb;
        }
        return new StringBuilder();
    }

    public List<Map<String,Object>> queryForList(String sql)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * SQL更新操作 update语句
     * @param sql demo:update table set name = ?
     * @param objects
     * @return
     * @throws Exception
     */
    public int update(String sql,Object... objects)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return jdbcTemplate.update(sql, objects);
    }

    /**
     * 更新数据库
     * @param pramMap 参数集合
     * @param whereKey 更新时指定的条件集合
     * @param tableName 表名
     * @return
     * @throws Exception
     */
    public int update(Map<String,Object> pramMap,Map<String,String> whereKey,String tableName)throws Exception{
        if(null != pramMap && !pramMap.isEmpty()&&null != whereKey && !whereKey.isEmpty() && StringUtils.isNotEmpty(tableName)){
            for(Iterator<String> it = whereKey.keySet().iterator();it.hasNext();){
                String key = it.next();
                if(key.equals(whereKey.get(key))){
                    //key and value is same,not allow same
                    return 0;
                }
            }

            StringBuilder sb = new StringBuilder("update ").append(tableName).append(" set ");
            int i=0;
            for(Iterator<String> it = pramMap.keySet().iterator();it.hasNext();){
                String key = it.next();
                sb.append(key).append("= :").append(key);
                if(i!=pramMap.size()-1){
                    sb.append(",");
                }
                i++;
            }
           
            //拼接where语句
//            sb.append(" where 1=1 ");
//            
//            for(Iterator<String> it = whereKey.keySet().iterator();it.hasNext();){
//                String key = it.next();
//                sb.append(" and ").append(key).append("= :").append(key);
//            }
            
            
            /*
             * 改造上面：where 1=1
             */
            //sb.append(" where 1=1 ");
            short tag = 0;
            for(Iterator<String> it = whereKey.keySet().iterator();it.hasNext();) {
                String key = it.next();
                if(tag > 0) {
                	sb.append(" and ").append(key).append("= :").append(key);
                } else {
                	sb.append(" where ").append(key).append("= :").append(key);
                }
                tag = 1;
            }
            
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValues(pramMap);
            parameterSource.addValues(whereKey);
            return update(sb.toString(),parameterSource);
        }


        return 0;
    }

    /**
     * 批量更新操作
     * @param sql
     * @param list
     * @return
     * @throws Exception
     */
    public int[] updateBatch(String sql,List<Object[]> list)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return jdbcTemplate.batchUpdate(sql, list);
    }

    /**
     * SQL更新做操update语句
     * @param sql demo: update table set name= :name
     * @param namedParameters
     * @return
     * @throws Exception
     */
    public int update(String sql,SqlParameterSource namedParameters)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    /**
     * 批量更新操作
     * @param sql
     * @param sqlParameterSources
     * @return
     * @throws Exception
     */
    public int[] updateBatch(String sql,SqlParameterSource[] sqlParameterSources)throws Exception{
        if(StringUtils.isEmpty(sql)){
            throw new Exception("SQL语句不能为空！");
        }
        return namedParameterJdbcTemplate.batchUpdate(sql, sqlParameterSources);
    }

    /**
     * 调用存储过程
     * @param procedureName
     * @param namedParameters
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public Map callProcedure(String procedureName,SqlParameterSource namedParameters)throws Exception{
        if(StringUtils.isEmpty(procedureName)){
            throw new Exception("存储过程名称不能为空！");
        }
        return simpleJdbcCall.withProcedureName(procedureName).execute(namedParameters);
    }

    /**
     * 调用存储过程
     * @param procedureName 存储过程名称
     * @param objects 参数列表
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public Map callProcedure(String procedureName,Object... objects)throws Exception{
        if(StringUtils.isEmpty(procedureName)){
            throw new Exception("存储过程名称不能为空！");
        }
        return simpleJdbcCall.withProcedureName(procedureName).execute(objects);
    }

    /**
     * 执行插入操作
     * @param tableName 表名
     * @param parameterValues 列名和列的值，例：{"name","zhangsan"}
     * @throws Exception
     */
    public int insert(String tableName,Map<String,Object> parameterValues)throws Exception{
        if(StringUtils.isEmpty(tableName)){
            throw new Exception("表名不能为空！");
        }
        //SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        if(null!=parameterValues && !parameterValues.isEmpty()){
            String[] columnNames = parameterValues.keySet().toArray(new String[parameterValues.size()]);
            return getSimpleJdbcInsert().withTableName(tableName).usingColumns(columnNames).execute(parameterValues);
        }
        return 0;
    }

    /**
     * 执行插入操作
     * @param tableName 表名
     * @param coluNames 列名
     * @param parameterValues 列名和列的值，例：{"name","zhangsan"}
     * @throws Exception
     */
    public int insert(String tableName,String[] coluNames,Map<String,Object> parameterValues)throws Exception{
        if(StringUtils.isEmpty(tableName)){
            throw new Exception("表名不能为空！");
        }
        //SimpleJdbcInsert simpleJdbcInsert = (SimpleJdbcInsert)Application.getBean("simpleJdbcInsert");
        return getSimpleJdbcInsert().withTableName(tableName).usingColumns(coluNames).execute(parameterValues);
    }

    /**
     * 批量插入操作
     * @param tableName
     * @param coluNames
     * @param parameterValues
     * @throws Exception
     */
    public void insertBatch(String tableName,String[] coluNames,Map<String,Object>[] parameterValues)throws Exception{
        if(StringUtils.isEmpty(tableName)){
            throw new Exception("表名不能为空！");
        }
        //SimpleJdbcInsert simpleJdbcInsert = (SimpleJdbcInsert)Application.getBean("simpleJdbcInsert");
        getSimpleJdbcInsert().withTableName(tableName).usingColumns(coluNames).executeBatch(parameterValues);
    }

    /**
     * 批量插入操作
     * @param tableName
     * @param parameterValues
     * @throws Exception
     */
    public int[] insertBatch(String tableName,Map<String,Object>[] parameterValues)throws Exception{
        if(StringUtils.isEmpty(tableName)){
            throw new Exception("表名不能为空！");
        }
        //SimpleJdbcInsert simpleJdbcInsert = (SimpleJdbcInsert)Application.getBean("simpleJdbcInsert");
        return getSimpleJdbcInsert().withTableName(tableName).executeBatch(parameterValues);
    }

    /**
     * 执行插入操作
     * @param tableName 表名
     * @param obj 值对象
     * @throws Exception
     */
    public int insert(String tableName,Object obj)throws Exception{
        if(StringUtils.isEmpty(tableName)){
            throw new Exception("表名不能为空！");
        }
        //SimpleJdbcInsert simpleJdbcInsert = (SimpleJdbcInsert)Application.getBean("simpleJdbcInsert");
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(obj);
        return getSimpleJdbcInsert().withTableName(tableName).execute(sqlParameterSource);
    }

    /**
     * 批量插入对象
     * @param tableName
     * @param objs
     * @throws Exception
     */
    public int[] insertBatch(String tableName,Object[] objs)throws Exception{
        if(StringUtils.isEmpty(tableName)){
            throw new Exception("表名不能为空！");
        }
        if(null == objs){
            throw new Exception("要插入的对象不能为空！");
        }
        SqlParameterSource[] sqlParameterSources = new BeanPropertySqlParameterSource[objs.length];
        int index=0;
        for(Object obj : objs){
            sqlParameterSources[index]= new BeanPropertySqlParameterSource(obj);
            index++;
        }
        //SimpleJdbcInsert simpleJdbcInsert = (SimpleJdbcInsert)Application.getBean("simpleJdbcInsert");
        return getSimpleJdbcInsert().withTableName(tableName).executeBatch(sqlParameterSources);
    }

    /**
     * 执行插入操作
     * @param tableName 表名
     * @param coluNames 列名数组
     * @param obj 值对象
     * @throws Exception
     */
    public int insert(String tableName,String[] coluNames,Object obj)throws Exception{
        if(StringUtils.isEmpty(tableName)){
            throw new Exception("表名不能为空！");
        }
        //SimpleJdbcInsert simpleJdbcInsert = (SimpleJdbcInsert)Application.getBean("simpleJdbcInsert");
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(obj);
        return getSimpleJdbcInsert().withTableName(tableName).usingColumns(coluNames).execute(sqlParameterSource);
    }

    /**
     * 获取SimpleJdbcInsert
     * @return
     * @throws Exception
     */
    private SimpleJdbcInsert getSimpleJdbcInsert()throws Exception{
        return new SimpleJdbcInsert(jdbcTemplate);
    }

    /**
     * 查询给定字段的集合
     * @param tableName 表名
     * @param columns  要查询字段
     * @param parameter  参数
     *                   例如：要表达 and number > 15 可以使用下面的格式
     *                   parameter.put("number$$more_than",15);
     *                   要表达 and name like '%zhangsan%'  可以使用下面的格式
     *                   parameter.put("name$$like","zhangsan");
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> queryForColumnList(String tableName, String[] columns,Map<String,Object>  parameter) throws Exception{
        Map<Object,Object> columnsTypes = getTableColumnsType(tableName);
        StringBuilder sb = new StringBuilder("select ");
        if(null != columns && columns.length != 0){
            Arrays.asList(columns).forEach(v->sb.append(v).append(","));
            sb.delete(sb.length()-1,sb.length());
        }else{
            sb.append(" * ");
        }
        if(tableName.indexOf("select ")>-1){
            sb.append(" from (").append(tableName).append(")as A_ where 1=1 ");
        }else {
            sb.append(" from ").append(tableName).append(" where 1=1 ");
        }
        //拼接查询条件
        StringBuilder whereCondition = joinWhereCondition(parameter);
        sb.append(whereCondition);
        MapSqlParameterSource parameterSource = getCoveredParamValue(parameter, columnsTypes);
        List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(sb.toString(), parameterSource);
        return  list;
    }
}