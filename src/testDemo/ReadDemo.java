package testDemo;

import autotest.dbutil.ConfigManager;
import autotest.dbutil.SliceReader;
import cfca.ra.datatransfer.util.PropertiesReader;

public class ReadDemo {
	public static String TABLENAME = "";
	public static String COLUMNNAME = "";
	private static PropertiesReader reader = new PropertiesReader();
	public static void main(String[] args) {
		new ConfigManager();
	    try
	    {
	      reader.init("./config.ini");
	    } catch (Exception e) {
	      e.printStackTrace();
	      System.out.println("读取配置文件失败");
	      return;
	    }

	    SliceReader sr = new SliceReader(reader.getProperty("SRCADDR"), 
	    								reader.getProperty("SRCPORT"), 
	    								reader.getProperty("SRCSID"), 
	    								reader.getProperty("SRCUSERNAME"), 
	    								reader.getProperty("SRCUSERPWD"));
	    sr.columnName = reader.getProperty("COLUMNNAME");
	    sr.tableName = reader.getProperty("TABLENAME");
	    
	    sr.getResultSet();

	    int number = 1;
	    while(number > 0)
	    {
	    	number = number - 1;
	    	String tValue = sr.getValue();
	    	System.out.println(tValue);
	    } 
	}
}
