package cfca.ra.datareverter;

import cfca.ra.datareverter.reverter.Reverter;
import cfca.ra.datatransfer.util.DBConnectUtil;

//import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import autotest.dbutil.ConfigManager;

public class RADataReverter {
	  //private static PropertiesReader reader = new PropertiesReader();
	  
	  public static long THREAD_CAPACITY;
	//  private static final String CONFIG_FILE = "./config.ini";

	  public static void initConfig()
	    throws Exception
	  {
	    try
	    {
	    	System.out.println("读取配置文件成功");
	    } catch (Exception e) {
	      e.printStackTrace();
	      System.out.println("读取配置文件失败");
	    }
	  }

	  public static void doCreate() {
	    long progStart = System.currentTimeMillis();
	    try {
	    
	      initConfig();

	      System.out.println(ConfigManager.COUNT);
	      int count = Integer.parseInt(ConfigManager.COUNT);
	      int isdel = Integer.parseInt(ConfigManager.DELETE);
	      String clause = ConfigManager.CERT_STATUS_CLAUSE;
	      DBConnectUtil.init();

	      if (isdel == 1) {
	        delTables(count);
	      }
	      Connection conn = DBConnectUtil.getReadConnection();
	      String createFlagTable = "CREATE TABLE flag_table(id SMALLINT NOT NULL, used SMALLINT DEFAULT 0, CONSTRAINT PK_FLAG PRIMARY KEY (id))";
	      Statement tableStmt = conn.createStatement();
	      tableStmt.execute(createFlagTable);

	      String createDataTable = "";
	      for (int i = 0; i < count; ++i) {
	    	  if(ConfigManager.SYS_NAME.equals("tgra3")) {
	    		  createDataTable = "CREATE TABLE USERDN_" + i 
	    				  + "(SUBJECT_DN VARCHAR(512) NOT NULL,SERIAL_NO VARCHAR(40) NOT NULL,AUTH_CODE VARCHAR(160),CONSTRAINT PK_USER" + i 
	    				  + " PRIMARY KEY (SUBJECT_DN))";
	    	  }else if(ConfigManager.SYS_NAME.equals("zjra3")) {
	    		  createDataTable = "CREATE TABLE USERDN_" + i 
	    				  + "(USER_DN VARCHAR(512) NOT NULL,SERIAL_NO VARCHAR(40) NOT NULL,AUTH_CODE VARCHAR(160) NOT NULL,CONSTRAINT PK_USER" + i 
	    				  + " PRIMARY KEY (USER_DN))";
	    	  }else if(ConfigManager.SYS_NAME.equals("wzhra3")) {
	    		  createDataTable = "CREATE TABLE USERDN_" + i 
	    				  + "(USER_DN VARCHAR(512) NOT NULL,SERIAL_NO VARCHAR(40) NOT NULL,CONSTRAINT PK_USER" + i 
	    				  + " PRIMARY KEY (USER_DN))";//用于无纸化RA35		    	  
	    	  }else if(ConfigManager.SYS_NAME.equals("ts3_sign")) {
	    		  createDataTable = "CREATE TABLE SIGN_" + i 
	    				  + "(USER_ID VARCHAR(32) DEFAULT SYS_GUID() NOT NULL,CONTRACT_NO VARCHAR(30) NOT NULL,CONSTRAINT PK_SIGN_" + i 
	    				  + " PRIMARY KEY (CONTRACT_NO))";	    	  
	    	  }else if(ConfigManager.SYS_NAME.equals("ts3_contract")) {
	    		  createDataTable = "CREATE TABLE CONTRACTNO_" + i 
	    				  + "(CONTRACT_NO VARCHAR(30) NOT NULL,CONSTRAINT PK_CONTRACT_" + i 
	    				  + " PRIMARY KEY (CONTRACT_NO))";	  
	    	  }else if(ConfigManager.SYS_NAME.equals("tjdsra3")) {
	    		  createDataTable = "CREATE TABLE USERDN_" + i 
	    				  + "(USER_DN VARCHAR(512) NOT NULL,CONSTRAINT PK_USER" + i 
	    				  + " PRIMARY KEY (USER_DN))";
	    	  }else{
	    		  System.out.println("No usefull sql statment");
	    	  }

	    	  tableStmt.execute(createDataTable);
	      }

	      String insertFlag = "INSERT INTO flag_table VALUES(?, 0)";
	      PreparedStatement pstmt = conn.prepareStatement(insertFlag);

	      for (int i = 0; i < count; ++i) {
	        pstmt.setInt(1, i);
	        pstmt.addBatch();
	      }
	      pstmt.executeBatch();
	      pstmt.close();

	      ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(count);

	      tableStmt.close();
	      for (int i = 0; i < count; ++i) {
	        Reverter r = new Reverter(i, count, clause);
	        threadPoolExecutor.execute(r);
	      }

	      threadPoolExecutor.shutdown();

	      threadPoolExecutor.awaitTermination(9223372036854775807L, TimeUnit.DAYS);
	    }
	    catch (Throwable e)
	    {
	      e.printStackTrace();
	    }
	    long progEnd = System.currentTimeMillis();
	    System.out.println("总共用时" + (progEnd - progStart) + "毫秒");
	  }

	  public static void delTables(int count) throws Exception {
	    Connection conn = DBConnectUtil.getReadConnection();
	    String createFlagTable = "drop table flag_table";
	    Statement tableStmt = conn.createStatement();
	    tableStmt.execute(createFlagTable);
	    for (int i = 0; i < count; ++i) {
	    	String createDataTable = "drop table USERDN" + i;
	    	  if(ConfigManager.SYS_NAME.equals("tgra3") || ConfigManager.SYS_NAME.equals("zjra3") || ConfigManager.SYS_NAME.equals("wzhra3") || ConfigManager.SYS_NAME.equals("tjdsra3")) {
	    		  createDataTable = "drop table USERDN_" + i;    	  
	    	  }else if(ConfigManager.SYS_NAME.equals("ts3_sign")) {
	    		  createDataTable = "drop table SIGN_" + i;	    	  
	    	  }else if(ConfigManager.SYS_NAME.equals("ts3_contract")) {
	    		  createDataTable = "drop table CONTRACTNO_" + i; 	    	  
	    	  }else{
	    		  System.out.println("No usefull sql statment");
	    	  }
	      tableStmt.execute(createDataTable);
	    }
	    System.exit(1);
	  }
}
