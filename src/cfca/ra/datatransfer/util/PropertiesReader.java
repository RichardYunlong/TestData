package cfca.ra.datatransfer.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader
{
  private static Properties props = new Properties();

  public void init(String fileName)
    throws IOException
  {
    InputStream fis = null;
    try {
      fis = new FileInputStream(fileName);
      props.load(fis);
    } catch (FileNotFoundException e) {
    }
    finally {
      if (fis != null)
        fis.close();
    }
  }

  public String getProperty(String key)
  {
    return props.getProperty(key);
  }

  public String getProperty(String key, String def) {
    return props.getProperty(key, def);
  }
}