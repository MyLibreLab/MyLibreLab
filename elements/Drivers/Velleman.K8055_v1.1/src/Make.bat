set CLASSPATH=%CLASSPATH%;..\bin\jna.jar
echo %classpath%
javac -g:none -d ..\bin  *.java
cmd