set CLASSPATH=%CLASSPATH%;..\bin;.;..\bin\svgSalamander-tiny.jar
echo %classpath%
javac -g:none -d ..\bin  *.java
cmd