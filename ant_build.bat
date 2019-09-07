@echo off
setlocal

cd %~p0

rem JAVA_HOME 環境変数の検査（と設定）
if not "%JAVA_HOME%" == "" goto checkAnt
if exist C:\jdk1.2.2\bin\java.exe           set JAVA_HOME=C:\jdk1.2.2
if exist C:\jdk1.3.1_15\bin\java.exe        set JAVA_HOME=C:\jdk1.3.1_15
if exist C:\j2sdk1.4.2_08\bin\java.exe      set JAVA_HOME=C:\j2sdk1.4.2_08
if exist "C:\Program Files\Java\jdk1.5.0_05\bin\java.exe"       set JAVA_HOME=C:\Program Files\Java\jdk1.5.0_05
if exist "C:\Program Files (x86)\Java\jdk1.6.0_11\bin\java.exe" set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.6.0_11
if exist "C:\Program Files\Java\jdk1.8.0_144\bin\java.exe"      set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_144\bin\java.exe
if not "%JAVA_HOME%" == "" goto checkAnt
echo JAVA_HOME 環境変数が設定されていません。
goto end

rem ANT_HOME 環境変数の検査（と設定）
:checkAnt
if not "%ANT_HOME%" == "" goto runAnt
if exist \apache-ant-1.5.3-1\bin\ant.bat  set ANT_HOME=\apache-ant-1.5.3-1
if exist \apache-ant-1.6.5\bin\ant.bat    set ANT_HOME=\apache-ant-1.6.5
if not "%ANT_HOME%" == "" goto runAnt
echo ANT_HOME 環境変数が設定されていません。
goto end

rem ビルド
:runAnt
call %ANT_HOME%\bin\ant.bat %1 %2 %3 %4 %5 %6 %7 %8 %9

goto end

:end

endlocal