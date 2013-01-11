@ECHO OFF

ECHO Starting...

SET BASICINSTALLER_HOME=..

SET MAINCLASS=net.kogics.kojo.lite.DesktopMain

set APPLICATION_CLASSPATH=
if "%APPLICATION_CLASSPATH%"=="" (
  for %%f in ("%BASICINSTALLER_HOME%\lib\*") do call :add_app_cpath "%%f"
  if "%OS%"=="Windows_NT" (
    for /d %%f in ("%BASICINSTALLER_HOME%\lib\*") do call :add_app_cpath "%%f"
  )
)

@ECHO ON

javaw -Xmx512m -cp %APPLICATION_CLASSPATH% net.kogics.kojo.lite.DesktopMain 


rem ##########################################################################
rem # subroutines

:add_app_cpath
  if "%APPLICATION_CLASSPATH%"=="" (
    set APPLICATION_CLASSPATH=%~1
  ) else (
    set APPLICATION_CLASSPATH=%APPLICATION_CLASSPATH%;%~1
  )
goto :eof
rem #########################################################################