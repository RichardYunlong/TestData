package autotest.dbutil;

//import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SliceReader
{
  public Connection conn = null;
  public Statement stmt = null;
  public PreparedStatement pstmt = null;
  public ResultSet rs = null;
  public int batchSize = 0;

  public int count = 0;
  public int id = 0;
  public String columnName = "";
  public String tableName = "";
  public String flagTable = "";
  public String whereClause = "";
  public int fetchSize = 100;
  public String keyid = "";
  public String sn = "";
  public String sn2 = "";

  public boolean needDelete = false;

	/**
	 * @see 创建数据库连接
	 */
  public SliceReader(String ip, String port, String sid, String userName, String pwd) {
    String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + sid;
    try
    {
      Class.forName("oracle.jdbc.driver.OracleDriver");
//   Class.forName("com.mysql.jdbc.Driver");
      this.conn = DriverManager.getConnection(url, userName, pwd);
      this.conn.setAutoCommit(false);
    }
    catch (Exception e) {
      System.out.println("创建数据库连接失败！");
      e.printStackTrace();
    }
  }

	/**
	 * @see 获取数据库集合
	 */
  public void getResultSet() {
    try {
      this.stmt = this.conn.createStatement();
      this.stmt.setFetchSize(this.fetchSize);
      String getId = "select seq_flag.nextval from dual";
      ResultSet rs1 = this.stmt.executeQuery(getId);
      rs1.next();
      this.id = (rs1.getInt(1) - 1);
      System.out.println(id);
      
      if(ConfigManager.SYS_NAME.equals("tgra3")  || ConfigManager.SYS_NAME.equals("zjra3") || ConfigManager.SYS_NAME.equals("wzhra3") || ConfigManager.SYS_NAME.equals("tjdsra3")) {
    	  String USER_DN =  "select " + this.columnName + " from " + this.tableName + this.id;
          this.rs = this.stmt.executeQuery(USER_DN);    	  
	  }else if(ConfigManager.SYS_NAME.equals("ts3_sign")) {
		  String userID = "select " + this.columnName + " from " + this.tableName + "_" + this.id;
		  System.out.println(userID); 
		  this.rs = this.stmt.executeQuery(userID);
	  }else if(ConfigManager.SYS_NAME.equals("ts3_contract")) {
	      String contractNO = "select " + this.columnName + " from " + this.tableName + "_" + this.id;	      
//	      String contractNO = "select " + this.columnName + " from " + this.tableName + " where CONTRACT_TYPE_CODE='QT' and SUBMIT_USER_ID='54197657043FEFF6E050A8C03E7B6040'";
//	      String contractNO = "select * from (select ROWNUM rn , CONTRACT_NO from " + this.tableName + ") temp";
	      System.out.println(contractNO);  
	      this.rs = this.stmt.executeQuery(contractNO);
	  }else{
		  System.out.println("No usefull sql statment");
		  this.rs = this.stmt.executeQuery("");
		  return;
	  }  
      
      this.pstmt = this.conn.prepareStatement("delete from " + this.tableName +  this.id + " where " + this.columnName + " = ?");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

	/**
	 * @see 获取指定单列
	 */
  public String getValue() {
	 // int rownum = 0;
    String value = "";
    try {
      boolean hasNext = this.rs.next();
      if (hasNext) {
        value = this.rs.getString("USER_DN");
		if(ConfigManager.SYS_NAME.equals("tgra3")  || ConfigManager.SYS_NAME.equals("zjra3") || ConfigManager.SYS_NAME.equals("wzhra3")|| ConfigManager.SYS_NAME.equals("tjdsra3")) {
			value = this.rs.getString("USER_DN");   	  
		  }else if(ConfigManager.SYS_NAME.equals("ts3_sign")) {
			  value = this.rs.getString("USER_ID");
		  }else if(ConfigManager.SYS_NAME.equals("ts3_contract")) {
			  value = this.rs.getString("CONTRACT_NO");
		  }else{
			  value = this.rs.getString("USER_DN");
		}  
        if (this.needDelete) {
          this.pstmt.setString(1, value);
          this.pstmt.addBatch();
          this.batchSize += 1;
          if (this.batchSize % 10000 == 0) {
            flush();
            this.pstmt.clearBatch();
          }
        }
      } else {
        value = "no";
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return  value;
    //    String.valueOf(rownum) + "     " +
  }

	/**
	 * @see 关闭数据流
	 */
  public void flush() {
    try {
      this.pstmt.executeBatch();
      this.conn.commit();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

	/**
	 * @see 退出连接
	 */
	public void close() {
	  if (this.rs != null) {
	    try {
	      this.rs.close();
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }
	  }
	  if (this.conn == null) return;
	  try {
	    this.conn.close();
	  } catch (SQLException e) {
	    e.printStackTrace();
	  }
	}

	/**
	 * @see 数据读取结束操作
	 */
	  public void finish() {
	    flush();
	    close();
	  }
  


  public Connection getConn()
  {
    return this.conn;
  }

  public void setConn(Connection conn)
  {
    this.conn = conn;
  }

  public Statement getStmt()
  {
    return this.stmt;
  }

  public void setStmt(Statement stmt)
  {
    this.stmt = stmt;
  }

  public int getCount()
  {
    return this.count;
  }

  public void setCount(int count)
  {
    this.count = count;
  }

  public int getId()
  {
    return this.id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getColumnName()
  {
    return this.columnName;
  }

  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }

  public String getFlagTable()
  {
    return this.flagTable;
  }

  public void setFlagTable(String flagTable)
  {
    this.flagTable = flagTable;
  }

  public String getWhereClause()
  {
    return this.whereClause;
  }

  public void setWhereClause(String whereClause)
  {
    this.whereClause = whereClause;
  }

  public String getP10Rsa() {
    return this.keyid;
  }

  public void setP10Rsa(String p10Rsa) {
    this.keyid = p10Rsa;
  }

  public String getP10Sm2() {
    return this.sn;
  }

  public void setP10Sm2(String p10Sm2) {
    this.sn = p10Sm2;
  }

  public String getTableName() {
    return this.tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  /** 
   * 关闭连接 
   * @param conn 
   */  
  public static void close(Connection conn) {  
      if (conn != null) {  
          try {  
              conn.close();  
          } catch (SQLException e) {  
              e.printStackTrace();  
          }     
      }  
  } 
  
  public static void main(String[] args) {
//    SliceReader sr = new SliceReader("192.168.123.62", "1521", "orcl", "trustsign", "cfca1234");
//    sr.columnName = "CONTRACT_NO";
//    sr.tableName = "CONTRACT";
    
    SliceReader sr = new SliceReader("192.168.45.112", "1521", "orcl", "WZRA35", "cfca1234");
    sr.columnName = "USER_DN";
    sr.tableName = "USERDN_";
    
    sr.getResultSet();

    int number = 4;
    while(number > 0)
    {
    	number = number - 1;
    	String contractNO = sr.getValue();
    	System.out.println(contractNO);
    }   
  }
}