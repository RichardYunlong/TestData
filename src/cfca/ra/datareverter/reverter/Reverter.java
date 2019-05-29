package cfca.ra.datareverter.reverter;

import cfca.ra.datatransfer.util.DBConnectUtil;

//import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import autotest.dbutil.ConfigManager;

public class Reverter extends Thread
{
  protected String tableName = "";
  protected String description = "";

  private String clause = "";
  protected String selectSql = "";
  protected String insertSql = "";

  private int i = 0;
  private int count = 0;

  protected static int fetchSize = 10000;
  protected static int batchSize = 10000;
  protected static int pageSize = 100000;

  protected Connection readConn = null;
  protected Connection writeConn = null;

  public Reverter(int i, int count, String clause) {
    try {
      this.readConn = DBConnectUtil.getReadConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
    this.i = i;
    this.count = count;
    this.clause = clause;
  }

  public static int getFetchSize() {
    return fetchSize;
  }

  public static void setFetchSize(int dfetchSize) {
    fetchSize = dfetchSize;
  }

  public static int getBatchSize() {
    return batchSize;
  }

  public static void setBatchSize(int dbatchSize) {
    batchSize = dbatchSize;
  }

  public void setSQL() {
      if(ConfigManager.SYS_NAME.equals("tgra3")) {
		  this.selectSql = ("INSERT INTO USERDN_" + this.i 
				  + "(SUBJECT_DN, SERIAL_NO, AUTH_CODE)"
				  + " SELECT SUBJECT_DN, SERIAL_NO, AUTH_CODE FROM CERT WHERE " + this.clause) 
				  + " and ORA_HASH(SUBJECT_DN, " + (this.count - 1) + ") = " + this.i + "";
	  }else if(ConfigManager.SYS_NAME.equals("zjra3")) {
		  this.selectSql = ("INSERT INTO USERDN_" + this.i 
				  + "(USER_DN, SERIAL_NO, AUTH_CODE)"
				  + " SELECT USER_DN, SERIAL_NO, AUTH_CODE FROM CERT WHERE " + this.clause) 
				  + " and ORA_HASH(USER_DN, " + (this.count - 1) + ") = " + this.i + "";
	  }else if(ConfigManager.SYS_NAME.equals("wzhra3")) {
		  this.selectSql = ("INSERT INTO USERDN_" + this.i 
				  + "(USER_DN, SERIAL_NO)"
				  + " SELECT USER_DN, SERIAL_NO FROM CERT WHERE " + this.clause) 
				  + " and ORA_HASH(USER_DN, " + (this.count - 1) + ") = " + this.i + "";	    	  
	  }else if(ConfigManager.SYS_NAME.equals("ts3_sign")) {
		  this.selectSql = ("INSERT INTO SIGN_" + this.i 
				  + "(USER_ID, CONTRACT_NO)"+"SELECT USER_ID,CONTRACT_NO from USER_CONTRACT WHERE " + this.clause)
				  + " and ORA_HASH(USER_ID, " + (this.count - 1) + ") = " + this.i + "";	  
	  }else if(ConfigManager.SYS_NAME.equals("ts3_contract")) {
		  this.selectSql = ("INSERT INTO CONTRACTNO_" + this.i
				  + "(CONTRACT_NO)"+" SELECT CONTRACT_NO FROM CONTRACT WHERE " + this.clause) 
				  + " and ORA_HASH(CONTRACT_NO, " + (this.count - 1) + ") = " + this.i + "";	  	   
	  }else if(ConfigManager.SYS_NAME.equals("tjdsra3")) {
		  this.selectSql = ("INSERT INTO USERDN_" + this.i 
				  + "(USER_DN)"
				  + " SELECT USER_DN FROM CERT WHERE " + this.clause) 
				  + " and ORA_HASH(USER_DN, " + (this.count - 1) + ") = " + this.i + "";
	  }else{
		  System.out.println("No usefull sql statment");
	  }  
	  
    System.out.println(this.selectSql);
  }

  public void run()
  {
    process();
  }

  public void process() {
    Statement preparedStatementSource = null;
    setSQL();
    try
    {
      preparedStatementSource = this.readConn.createStatement();
      preparedStatementSource.execute(this.selectSql);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        preparedStatementSource.close();
      } catch (SQLException e) {
        e.printStackTrace();
        try {
          if (preparedStatementSource != null)
            preparedStatementSource.close();
        }
        catch (SQLException e1) {
          e1.printStackTrace();
        }
      }
    }
  }
}