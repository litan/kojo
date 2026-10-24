@echo off
SET kojolibdir="..\lib"
SET kojojavacp=
SETLOCAL EnableDelayedExpansion
echo %kojolibdir%
FOR %%A IN (%kojolibdir%\*) DO ( 
  SET kojojavacp=!kojojavacp!;%%~A
)
echo %kojojavacp%
call java -cp "%kojojavacp%" net.kogics.kojo.lite.DesktopMain
