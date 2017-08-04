package rcu;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import codebuilder.dao.Dao;
import codebuilder.dao.impl.DerbyDao;

public class InitTable {

//	public static void main(String[] args) {
////		createTable();
////		insertTest();
//		queryTest();
////		deleteTable();
//	}


	private static void deleteTable() {
		Dao dao = new DerbyDao();
		dao.del("delete FROM  TEMP");
		dao.del("delete FROM TEMP_DATA");

	}


	public static void createTable() {
		Dao dao = new DerbyDao();
		String temp_sql = "" +
		"CREATE TABLE TEMP "+
		"("+
		"   TEMP_ID              VARCHAR(36)          NOT NULL,"+
		"   TEMP_NAME            VARCHAR(100),"+
		"   CONSTRAINT PK_TEMP PRIMARY KEY (TEMP_ID)"+
		")";


		dao.add(temp_sql);

		String temp_data_sql = "" +
		"CREATE TABLE TEMP_DATA "+
		"("+
		"   TEMP_DATA_ID         VARCHAR(36)          NOT NULL,"+
		"   TEMP_ID              VARCHAR(36),"+
		"   LABEL                  VARCHAR(100),"+
		"   VALUE                VARCHAR(32672),"+
		"   CONSTRAINT PK_TEMP_DATA PRIMARY KEY (TEMP_DATA_ID)"+
		")";
		dao.add(temp_data_sql);

	}

	public static void insertTest() {
		Dao dao = new DerbyDao();
		String tempId = UUID.randomUUID().toString();
		dao.add("insert into temp values('"+tempId+"','模版1')");
		dao.add("insert into temp_data values('"+UUID.randomUUID().toString()+"','"+tempId+"','action','action...')");
		dao.add("insert into temp_data values('"+UUID.randomUUID().toString()+"','"+tempId+"','service','service...')");
		dao.add("insert into temp_data values('"+UUID.randomUUID().toString()+"','"+tempId+"','dao','dao...')");
	}

	public static void queryTest() {
		Dao jdbc = new DerbyDao();
		List<Map<String,String>> query = jdbc.query("select * from temp ");
		System.out.println(query);
		query = jdbc.query("select * from temp_data  where  temp_id = 'ef7d85e4-17a8-4bbc-a149-f8cc3e0e5285'");
		System.out.println(query);
	}

}
