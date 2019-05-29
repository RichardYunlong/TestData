package testDemo;

import autotest.dbutil.Base64;
import autotest.dbutil.ConfigManager;
import autotest.dbutil.SliceReader3;

public class ReadDemo3 {
	public static void main(String[] args) {
	    new ConfigManager();
	    SliceReader3 sr = new SliceReader3(ConfigManager.URL, 
								    		ConfigManager.PORT, 
								    		ConfigManager.SID, 
								    		ConfigManager.DB_USERNAME, 
								    		ConfigManager.DB_PASSWORD);
	    sr.columnName = ConfigManager.COLUMNNAME;
	    sr.tableName = ConfigManager.TABLENAME;
	    System.out.println(sr.columnName);
	    sr.getResultSet3();

	    for (int i = 0; i < 1; i++)
	    {
	      String signdata = sr.getValue3();
	      //System.out.println(signdata);
	      
	      String[] signarray = signdata.split(",");
	      
	      String param1 = signarray[0] + ","
	    		  		+ signarray[1] + ","
	    		  		+ signarray[2] + ","
	    		  		+ signarray[3] + ","
	    		  		+ signarray[4];
	      System.out.println(param1);
	      
	      String param2 = signarray[5];
	      System.out.println(param2);
	      
	      String param3 = signarray[6];
	      byte[] cipherByte = Base64.decode(param3.getBytes());
	      param3 = new String(SliceReader3.decrypt(cipherByte));
	      System.out.println(param3);
	    }
	}
}
