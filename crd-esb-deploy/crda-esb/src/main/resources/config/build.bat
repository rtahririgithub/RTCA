set WEBLOGIC_HOME=C:\Oracle\Middleware\Oracle_Home121300
set JAVA_HOME=C:\Java\jdk1.7.0_15
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
set ANT_HOME=%WEBLOGIC_HOME%\oracle_common\modules\org.apache.ant_1.9.2

set CURDIR=%CD%


call %WEBLOGIC_HOME%\wlserver\server\bin\setWLSEnv.cmd
cd %CURDIR%
call %ANT_HOME%\bin\ant.bat
