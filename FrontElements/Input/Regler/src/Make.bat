del ..\bin\*.class
rd /s /q ..\bin\tools
javac -d ..\bin  *.java
rd /s /q ..\bin\tools
cmd
