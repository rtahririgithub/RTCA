set CURDIR=%CD%
call C:\bea1033\wlserver_10.3\server\bin\setWLSEnv.cmd
cd %CURDIR%
call C:\bea1033\modules\org.apache.ant_1.7.1\bin\ant.bat
