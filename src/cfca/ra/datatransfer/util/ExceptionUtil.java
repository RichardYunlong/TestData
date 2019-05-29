package cfca.ra.datatransfer.util;

public class ExceptionUtil
{
  public static String getStackTreeInfo(Throwable thr)
  {
    String resultString = "";

    resultString = thr.toString() + "\n";
    if (thr.getCause() != null) {
      StackTraceElement[] itemStackTraceElements = thr.getCause().getStackTrace();
      for (StackTraceElement iterable_element : itemStackTraceElements) {
        resultString = resultString + "execption in Class : " + iterable_element.getClassName() + " in Method : " + iterable_element.getMethodName() + " Line : " + iterable_element.getLineNumber();

        resultString = resultString + "\n";
      }
    } else {
      StackTraceElement[] itemStackTraceElements = thr.getStackTrace();
      for (StackTraceElement iterable_element : itemStackTraceElements) {
        resultString = resultString + "execption in Class : " + iterable_element.getClassName() + " in Method : " + iterable_element.getMethodName() + " Line : " + iterable_element.getLineNumber();

        resultString = resultString + "\n";
      }
    }
    return resultString;
  }
}