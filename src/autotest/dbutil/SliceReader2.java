package autotest.dbutil;

//import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class SliceReader2
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
  public int fetchSize = 500;
  public String keyid = "";
  public String sn = "";
  public String sn2 = "";
  public boolean needDelete = true;
  
  public SliceReader2(String ip, String port, String sid, String userName, String pwd)
  {
    String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + sid;
    try
    {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      this.conn = DriverManager.getConnection(url, userName, pwd);
      this.conn.setAutoCommit(false);
    }
    catch (Exception e)
    {
      System.out.println("SDFDSFSDFG");
      e.printStackTrace();
    }
  }
  
  public void getResultSet()
  {
    try
    {
      this.stmt = this.conn.createStatement();
      this.stmt.setFetchSize(this.fetchSize);
      String getId = "select seq_flag.nextval from dual";
      ResultSet rs1 = this.stmt.executeQuery(getId);
      rs1.next();
      this.id = (rs1.getInt(1) - 1);
      String getSn = "select " + this.columnName + " from " + this.tableName + "_" + this.id;
      System.out.println(getSn);
      this.rs = this.stmt.executeQuery(getSn);
      this.pstmt = this.conn.prepareStatement("delete from " + this.tableName + "_" + this.id + " where " + this.columnName + " = ?");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  public void getResultSet2()
  {
    try
    {
      this.stmt = this.conn.createStatement();
      
      this.stmt.setFetchSize(this.fetchSize);
      String getId = "select seq_flag.nextval from dual";
      ResultSet rs1 = this.stmt.executeQuery(getId);
      rs1.next();
      this.id = (rs1.getInt(1) - 1);
      System.out.println(id);
      String getSn = "select SUBJECT_DN,SERIAL_NO,AUTH_CODE from " + this.tableName + "_" + this.id;
      String sql="delete from " + this.tableName + "_" + this.id + " where SERIAL_NO = ?";
      this.pstmt = this.conn.prepareStatement(sql);
      this.rs = this.stmt.executeQuery(getSn);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  public String getValue2()
  {
    String value1 = "";
    String value2 = "";
    try
    {
      boolean hasNext = this.rs.next();
      if (hasNext)
      {
        value1 = this.rs.getString(1);
        value2 = this.rs.getString(2);
        
        this.keyid = value1;
        this.sn = value2;
        if (this.needDelete)
        {
          this.pstmt.setString(1, value2);
          this.pstmt.addBatch();
          this.batchSize += 1;
          if (this.batchSize % 100 == 0)
          {
            flush();
            this.pstmt.clearBatch();
          }
        }
      }
      else
      {
        value2 = "no";
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return value1 + "," + value2;
  }
  
  public String getValue()
  {
    String value = "";
    try
    {
      boolean hasNext = this.rs.next();
      if (hasNext)
      {
        value = this.rs.getString(1);
        if (this.needDelete)
        {
          this.pstmt.setString(1, value);
          this.pstmt.addBatch();
          this.batchSize += 1;
          if (this.batchSize % 10000 == 0)
          {
            flush();
            this.pstmt.clearBatch();
          }
        }
      }
      else
      {
        value = "no";
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return value;
  }
  
  public void getResultSet3()
  {
    try
    {
      this.stmt = this.conn.createStatement();
      
      this.stmt.setFetchSize(this.fetchSize);
      String getId = "select seq_flag.nextval from dual";
      ResultSet rs1 = this.stmt.executeQuery(getId);
      rs1.next();
      this.id = (rs1.getInt(1) - 1);
      String getSn = "select key_id, serial_no,serial_no2 from " + this.tableName + "_" + this.id;
      this.pstmt = this.conn.prepareStatement("delete from " + this.tableName + "_" + this.id + " where key_id = ?");
      this.rs = this.stmt.executeQuery(getSn);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  public String getValue3()
  {
    String value1 = "sssss";
    String value2 = "sssss";
    String value3 = "sssss";
    try
    {
      boolean hasNext = this.rs.next();
      if (hasNext)
      {
        value1 = this.rs.getString(1);
        value2 = this.rs.getString(2);
        value3 = this.rs.getString(3);
        
        this.keyid = value1;
        this.sn = value2;
        this.sn2 = value3;
        if (this.needDelete)
        {
          this.pstmt.setString(1, value1);
          this.pstmt.addBatch();
          this.batchSize += 1;
          if (this.batchSize % 10000 == 0)
          {
            flush();
            this.pstmt.clearBatch();
          }
        }
      }
      else
      {
        value1 = "no";
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return value1 + "," + value2 + "," + value3;
  }
  
  public void flush()
  {
    try
    {
      this.pstmt.executeBatch();
      this.conn.commit();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  public void finish()
  {
    flush();
    close();
  }
  
  public void close()
  {
    if (this.rs != null) {
      try
      {
        this.rs.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    if (this.conn == null) {
      return;
    }
    try
    {
      this.conn.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
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
  
  public String getP10Rsa()
  {
    return this.keyid;
  }
  
  public void setP10Rsa(String p10Rsa)
  {
    this.keyid = p10Rsa;
  }
  
  public String getP10Sm2()
  {
    return this.sn;
  }
  
  public void setP10Sm2(String p10Sm2)
  {
    this.sn = p10Sm2;
  }
  
  public String getTableName()
  {
    return this.tableName;
  }
  
  public void setTableName(String tableName)
  {
    this.tableName = tableName;
  }
  
  public static byte[] decrypt(byte[] ciphertext) {
      try {
          // "authcode-encrypt"为口令
          PBEKeySpec keySpec = new PBEKeySpec("authcode-encrypt".toCharArray());
          SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
          SecretKey key = keyFac.generateSecret(keySpec);

          // "CFCA--RA"为盐值,必为8个字符
          String salt = new String("CFCA--RA");
          // 迭代次数为1000
          PBEParameterSpec paramSpec = new PBEParameterSpec(salt.getBytes(), 1000);
          // 加密算法是"PBEWithMD5AndDES"
          Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
          cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
          return cipher.doFinal(ciphertext);
      } catch (Exception e) {
          System.out.println("解密授权码失败: " + e.getMessage());
          return ciphertext;
      }
  }
  
  public static void main(String[] args)
  {
    SliceReader2 sr = new SliceReader2("192.168.45.112", "1521", "orcl", "TGRA35", "cfca1234");
    
    sr.tableName = "USERDN";
    sr.columnName = "SUBJECT_DN";
    sr.getResultSet2();
    for (int i = 0; i < 1; i++)
    {
      String signdata = sr.getValue3();
      //System.out.println(signdata);
      
      String[] signarray = signdata.split(",");
      String user_id = signarray[5];
      System.out.println(user_id);
      
      String contract_no = signarray[6];
      System.out.println(contract_no);
      byte[] cipherByte = Base64.decode(contract_no.getBytes());
      String clearStr = new String(decrypt(cipherByte));
      System.out.println(clearStr);
    }
  }
}
