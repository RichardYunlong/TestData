package autotest.dbutil;

import cfca.ra.datatransfer.util.PropertiesReader;

public class ConfigManager {
	public static String SYS_NAME="";
	public static String ISEXIST="";
	public static String TABLENAME = "";
	public static String COLUMNNAME = "";
	public static String URL="";
	public static String PORT="";
	public static String SID = "";
	public static String DB_USERNAME = "";
	public static String DB_PASSWORD = "";
	public static String DELETE = "";
	public static String COUNT = "";
	public static String CERT_STATUS_CLAUSE = "";
	
	private static PropertiesReader reader = new PropertiesReader();
	
	public ConfigManager(){
	    try
	    {
	      reader.init("./config.ini");
	    } catch (Exception e) {
	      e.printStackTrace();
	      System.out.println("读取配置文件失败");
	      return;
	    }
		URL = reader.getProperty("SRCADDR");
		PORT = reader.getProperty("SRCPORT");
		SID = reader.getProperty("SRCSID");
		DB_USERNAME = reader.getProperty("SRCUSERNAME");
		DB_PASSWORD = reader.getProperty("SRCUSERPWD");
		SYS_NAME = reader.getProperty("SYSNAME");
		DELETE = reader.getProperty("DELETE");
		COUNT = reader.getProperty("COUNT");
		CERT_STATUS_CLAUSE = reader.getProperty("CERT_STATUS_CLAUSE");
		ISEXIST = reader.getProperty("ISEXIST");
	    COLUMNNAME = reader.getProperty("COLUMNNAME");
	    TABLENAME = reader.getProperty("TABLENAME");
	}
    
}
