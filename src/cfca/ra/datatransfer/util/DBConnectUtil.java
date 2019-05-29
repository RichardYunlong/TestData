package cfca.ra.datatransfer.util;

import java.io.FileNotFoundException;
//import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectUtil
{
  private static String CONFIG_PATH = "./config2.ini";

  private static String srcDbAddr = "";
  private static String srcDbPort = "";
  private static String srcDbSID = "";
  private static String srcDbUserName = "";
  private static String srcDbUserPwd = "";

//  private static String targetDbAddr = "";
//  private static String targetDbPort = "";
//  private static String targetDbSID = "";
//  private static String targetDbUserName = "";
//  private static String targetDbUserPwd = "";

  public static void init() {
    init(CONFIG_PATH);
  }

  public static void init(String path) {
    PropertiesReader reader = new PropertiesReader();
    try {
      reader.init(path);
    } catch (FileNotFoundException e) {
      System.out.println("�Ҳ��������ļ�");
      System.out.println(ExceptionUtil.getStackTreeInfo(e));
    } catch (Exception e) {
      System.out.println("��ȡ�����ļ�ʧ��");
      System.out.println(ExceptionUtil.getStackTreeInfo(e));
    }
    init(reader);
  }

  public static void init(PropertiesReader reader)
  {
    srcDbAddr = reader.getProperty("SRCADDR");

    srcDbPort = reader.getProperty("SRCPORT");

    srcDbSID = reader.getProperty("SRCSID");

    srcDbUserName = reader.getProperty("SRCUSERNAME");

    srcDbUserPwd = reader.getProperty("SRCUSERPWD");

//    targetDbAddr = reader.getProperty("TARGETADDR");
//
//    targetDbPort = reader.getProperty("TARGETPORT");
//
//    targetDbSID = reader.getProperty("TARGETSID");
//
//    targetDbUserName = reader.getProperty("TARGETUSERNAME");
//
//    targetDbUserPwd = reader.getProperty("TARGETUSERPWD");
  }

  public static synchronized Connection getReadConnection()
    throws Exception
  {
    Connection conn = null;
    String url = "jdbc:oracle:thin:@" + srcDbAddr + ":" + srcDbPort + ":" + srcDbSID;

    //mysql
//    String url = "jdbc:mysql://192.168.123.63:3306/bxra";
    try
    {
      Class.forName("oracle.jdbc.driver.OracleDriver");
//    	Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(url, srcDbUserName, srcDbUserPwd);
//        conn = DriverManager.getConnection(url, targetDbUserName, targetDbUserPwd);
    } catch (Exception e) {
      System.out.println("创建数据库连接失败！" + ExceptionUtil.getStackTreeInfo(e));
      throw e;
    }
    return conn;
  }

  public static synchronized Connection getWriteConnection() throws Exception {
    Connection conn = null;
    String url = "jdbc:oracle:thin:@" + srcDbAddr + ":" + srcDbPort + ":" + srcDbSID;
//    String url = "jdbc:mysql://192.168.123.63:3306/bxra";
    try
    {
      Class.forName("oracle.jdbc.driver.OracleDriver");
//    	Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(url, srcDbUserName, srcDbUserPwd);
    } catch (Exception e) {
      System.out.println("创建数据库连接失败！" + ExceptionUtil.getStackTreeInfo(e));
      throw e;
    }
    return conn;
  }
}