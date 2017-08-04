package codebuilder.dao;

import java.util.List;
import java.util.Map;

public interface Dao {

	List<Map<String,String>> query(String sql, Object... objs);

	Map<String,String> queryOne(String sql, Object... objs);

	int add(String sql, Object... objs);

	int mod(String sql, Object... objs);

	int del(String sql, Object... objs);

	boolean tableIsNotExists(String tableName);
}
