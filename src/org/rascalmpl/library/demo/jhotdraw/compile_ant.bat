@echo off

call setenv.bat

%ANT_HOME%\bin\ant -buildfile build\build.xml %1 %2 %3
