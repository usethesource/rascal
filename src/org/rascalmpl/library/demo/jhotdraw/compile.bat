@echo off

set JHD_DIR=D:\daten\wolfram\jhotdraw
set JDK=C:\Programme\JDK1.2

set OLD_CP=%CLASSPATH%
set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;%JHD_DIR%

javac -d %JHD_DIR% %JHD_DIR%\src\org\jhotdraw\applet\*.java %JHD_DIR%\src\org\jhotdraw\application\*.java %JHD_DIR%\src\org\jhotdraw\contrib\*.java %JHD_DIR%\src\org\jhotdraw\figures\*.java %JHD_DIR%\src\org\jhotdraw\framework\*.java %JHD_DIR%\src\org\jhotdraw\standard\*.java %JHD_DIR%\src\org\jhotdraw\util\*.java

set SAMPLES=%JHD_DIR%\src\org\jhotdraw\samples
javac -d %JHD_DIR% %SAMPLES%\javadraw\*.java %SAMPLES%\net\*.java %SAMPLES%\nothing\*.java %SAMPLES%\pert\*.java

set CLASSPATH=%OLD_CP%
