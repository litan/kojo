@ECHO OFF

SET BIN_DIR=%~dp0

set APPLICATION_CLASSPATH=
if "%APPLICATION_CLASSPATH%"=="" (
  for %%f in ("%BIN_DIR%..\lib\*") do call :add_app_cpath "%%f"
  if "%OS%"=="Windows_NT" (
    for /d %%f in ("%BIN_DIR%..\lib\*") do call :add_app_cpath "%%f"
  )
)

start /B javaw -cp "%APPLICATION_CLASSPATH%" net.kogics.kojo.lite.DesktopMain 


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