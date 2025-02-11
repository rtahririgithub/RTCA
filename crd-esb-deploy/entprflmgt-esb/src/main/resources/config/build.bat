set CURDIR=%CD%
set WL_HOME=C:\Oracle\Middleware\Oracle_Home121300
@REM set WL_HOME=C:\Oracle\Middleware\Oracle_Home

call %WL_HOME%\wlserver\server\bin\\setWLSEnv.cmd
cd %CURDIR%
@REM call C:\Oracle\Middleware\Oracle_Home\oracle_common\modules\org.apache.ant_1.9.2\bin\ant.bat
@REM call %WL_HOME%\oracle_common\modules\org.apache.ant_1.9.2\bin\ant.bat

call  C:\Oracle\Middleware\Oracle_Home121300\oracle_common\modules\org.apache.ant_1.9.2\bin\ant.bat