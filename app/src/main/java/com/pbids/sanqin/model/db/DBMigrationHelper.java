package com.pbids.sanqin.model.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 解决数据库升级保留数据问题
 * Created by liang on 2018/3/7.
 */

class DBMigrationHelper {

	public static void migrate(SQLiteDatabase sqliteDatabase, Class<? extends AbstractDao<?, ?>>... daoClasses) {
		StandardDatabase db = new StandardDatabase(sqliteDatabase);
		generateNewTablesIfNotExists(db, daoClasses);
		generateTempTables(db, daoClasses);
		dropAllTables(db, true, daoClasses);
		createAllTables(db, false, daoClasses);
		restoreData(db, daoClasses);
	}

	public static void migrate(StandardDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
		generateNewTablesIfNotExists(db, daoClasses);
		generateTempTables(db, daoClasses);
		dropAllTables(db, true, daoClasses);
		createAllTables(db, false, daoClasses);
		restoreData(db, daoClasses);
	}

	private static void generateNewTablesIfNotExists(StandardDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
		reflectMethod(db, "createTable", true, daoClasses);
	}

	private static void generateTempTables(StandardDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
		for (int i = 0; i < daoClasses.length; i++) {
			DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
			String tableName = daoConfig.tablename;
			String tempTableName = daoConfig.tablename.concat("_TEMP");
			StringBuilder insertTableStringBuilder = new StringBuilder();
			insertTableStringBuilder.append("CREATE TEMP TABLE ").append(tempTableName);
			insertTableStringBuilder.append(" AS SELECT * FROM ").append(tableName).append(";");
			db.execSQL(insertTableStringBuilder.toString());
		}
	}

	private static void dropAllTables(StandardDatabase db, boolean ifExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
		reflectMethod(db, "dropTable", ifExists, daoClasses);
	}

	private static void createAllTables(StandardDatabase db, boolean ifNotExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
		reflectMethod(db, "createTable", ifNotExists, daoClasses);
	}

	/**
	 * dao class already define the sql exec method, so just invoke it
	 */
	private static void reflectMethod(StandardDatabase db, String methodName, boolean isExists, @NonNull Class<? extends AbstractDao<?, ?>>... daoClasses) {
		if (daoClasses.length < 1) {
			return;
		}
		try {
			for (Class cls : daoClasses) {
				Method method = cls.getDeclaredMethod(methodName, Database.class, boolean.class);
				method.invoke(null, db, isExists);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private static void restoreData(StandardDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
		for (int i = 0; i < daoClasses.length; i++) {
			DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
			String tableName = daoConfig.tablename;
			String tempTableName = daoConfig.tablename.concat("_TEMP");
			// get all columns from tempTable, take careful to use the columns list
			List<String> columns = getColumns(db, tempTableName);
			ArrayList<String> properties = new ArrayList<>(columns.size());
			for (int j = 0; j < daoConfig.properties.length; j++) {
				String columnName = daoConfig.properties[j].columnName;
				if (columns.contains(columnName)) {
					properties.add(columnName);
				}
			}
			if (properties.size() > 0) {
				final String columnSQL = TextUtils.join(",", properties);

				StringBuilder insertTableStringBuilder = new StringBuilder();
				insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
				insertTableStringBuilder.append(columnSQL);
				insertTableStringBuilder.append(") SELECT ");
				insertTableStringBuilder.append(columnSQL);
				insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");
				db.execSQL(insertTableStringBuilder.toString());
			}
			StringBuilder dropTableStringBuilder = new StringBuilder();
			dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);
			db.execSQL(dropTableStringBuilder.toString());
		}
	}

	private static List<String> getColumns(StandardDatabase db, String tableName) {
		List<String> columns = null;
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 0", null);
			if (null != cursor && cursor.getColumnCount() > 0) {
				columns = Arrays.asList(cursor.getColumnNames());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			if (null == columns)
				columns = new ArrayList<>();
		}
		return columns;
	}
	/*
	private static final String CONVERSION_CLASS_NOT_FOUND_EXCEPTION = "MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS";
	private static MigrationHelper instance;

	public static MigrationHelper getInstance() {
		if(instance == null) {
			instance = new MigrationHelper();
		}
		return instance;
	}

	public void migrate(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
		generateTempTables(db, daoClasses);
		DaoMaster.dropAllTables(db, true);
		DaoMaster.createAllTables(db, false);
		restoreData(db, daoClasses);
	}

	private void generateTempTables(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
		for(int i = 0; i < daoClasses.length; i++) {
			DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);

			String divider = "";
			String tableName = daoConfig.tablename;
			String tempTableName = daoConfig.tablename.concat("_TEMP");
			ArrayList<String> properties = new ArrayList<>();

			StringBuilder createTableStringBuilder = new StringBuilder();

			createTableStringBuilder.append("CREATE TABLE ").append(tempTableName).append(" (");

			for(int j = 0; j < daoConfig.properties.length; j++) {
				String columnName = daoConfig.properties[j].columnName;

				if(getColumns(db, tableName).contains(columnName)) {
					properties.insert(columnName);

					String type = null;

					try {
						type = getTypeByClass(daoConfig.properties[j].type);
					} catch (Exception exception) {
						//Crashlytics.logException(exception);
					}

					createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);

					if(daoConfig.properties[j].primaryKey) {
						createTableStringBuilder.append(" PRIMARY KEY");
					}

					divider = ",";
				}
			}
			createTableStringBuilder.append(");");

			db.execSQL(createTableStringBuilder.toString());

			StringBuilder insertTableStringBuilder = new StringBuilder();

			insertTableStringBuilder.append("INSERT INTO ").append(tempTableName).append(" (");
			insertTableStringBuilder.append(TextUtils.join(",", properties));
			insertTableStringBuilder.append(") SELECT ");
			insertTableStringBuilder.append(TextUtils.join(",", properties));
			insertTableStringBuilder.append(" FROM ").append(tableName).append(";");

			db.execSQL(insertTableStringBuilder.toString());
		}
	}

	private void restoreData(SQLiteDatabase db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
		for(int i = 0; i < daoClasses.length; i++) {
			DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);

			String tableName = daoConfig.tablename;
			String tempTableName = daoConfig.tablename.concat("_TEMP");
			ArrayList<String> properties = new ArrayList();

			for (int j = 0; j < daoConfig.properties.length; j++) {
				String columnName = daoConfig.properties[j].columnName;

				if(getColumns(db, tempTableName).contains(columnName)) {
					properties.insert(columnName);
				}
			}

			StringBuilder insertTableStringBuilder = new StringBuilder();

			insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
			insertTableStringBuilder.append(TextUtils.join(",", properties));
			insertTableStringBuilder.append(") SELECT ");
			insertTableStringBuilder.append(TextUtils.join(",", properties));
			insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");

			StringBuilder dropTableStringBuilder = new StringBuilder();

			dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);

			db.execSQL(insertTableStringBuilder.toString());
			db.execSQL(dropTableStringBuilder.toString());
		}
	}

	private String getTypeByClass(Class<?> type) throws Exception {
		if(type.equals(String.class)) {
			return "TEXT";
		}
		if(type.equals(Long.class) || type.equals(Integer.class) || type.equals(long.class)) {
			return "INTEGER";
		}
		if(type.equals(Boolean.class)) {
			return "BOOLEAN";
		}

		Exception exception = new Exception(CONVERSION_CLASS_NOT_FOUND_EXCEPTION.concat(" - Class: ").concat(type.toString()));
		//Crashlytics.logException(exception);
		throw exception;
	}

	private static List<String> getColumns(SQLiteDatabase db, String tableName) {
		List<String> columns = new ArrayList<>();
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
			if (cursor != null) {
				columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
			}
		} catch (Exception e) {
			Log.v(tableName, e.getMessage(), e);
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return columns;
	}
	*/
}
