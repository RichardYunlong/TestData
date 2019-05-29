clear

lib=./lib
classpath=.

for file in ${lib}/*.jar
    do classpath=${classpath}:$file
done

$JAVA_HOME/bin/java -server -Xms1024M -Xmx1024M -classpath ${classpath} cfca.ra.TSClientTest.service.Main