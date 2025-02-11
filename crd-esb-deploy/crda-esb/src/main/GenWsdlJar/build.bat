@echo off

set WEBLOGIC_HOME=C:\Oracle\Middleware\Oracle_Home121300
set JAVA_HOME=C:\Java\jdk1.7.0_15
set ANT_HOME=%WEBLOGIC_HOME%\oracle_common\modules\org.apache.ant_1.9.2


set JAVA_EXE=%JAVA_HOME%\bin\java.exe

@REM -------------------------------------------------------------------
if exist "%WEBLOGIC_HOME%" goto okWEBLOGIC_HOME
echo ERROR: %WEBLOGIC_HOME% not found
goto end
:okWEBLOGIC_HOME

@REM -------------------------------------------------------------------
if exist "%JAVA_HOME%" goto okJAVA_HOME
echo ERROR: %JAVA_HOME% not set
goto end
:okJAVA_HOME

@REM -------------------------------------------------------------------
if exist "%JAVA_EXE%" goto okJAVA_EXE
echo ERROR: %JAVA_EXE% not found
goto end
:okJAVA_EXE

@REM -------------------------------------------------------------------


if exist "%ANT_HOME%" goto okANT_HOME
echo ERROR: %ANT_HOME% not found
goto end
:okANT_HOME


@REM -------------------------------------------------------------------
set ANT_EXE=%ANT_HOME%\bin\ant.bat
if exist "%ANT_EXE%" goto okANT_EXE
echo ERROR: %ANT_EXE% not found
goto end
:okANT_EXE

echo WEBLOGIC_HOME="%WEBLOGIC_HOME%"
echo JAVA_HOME="%JAVA_HOME%"
echo JAVA_EXE="%JAVA_EXE%"
echo ANT_HOME="%ANT_HOME%"
echo ANT_EXE="%ANT_EXE%"

@REM -------------------------------------------------------------------
set CLASSPATH=

echo Starting Ant...
call %ANT_EXE% %1 %2 %3 %4 %5



:end

