rem ���̃o�b�`�t�@�C����jakarta-ORO���_�E�����[�h����o�b�`�t�@�C���ł��B
@echo off
setlocal

cd %~p0

rem JAVA_HOME ���ϐ��̌����i�Ɛݒ�j
if not "%JAVA_HOME%" == "" goto checkAnt
if exist \jdk1.2.2\bin\java.exe           set JAVA_HOME=\jdk1.2.2
if exist \jdk1.3\bin\java.exe             set JAVA_HOME=\jdk1.3
if exist \jdk1.3.0\bin\java.exe           set JAVA_HOME=\jdk1.3.0
if exist \jdk1.3.1\bin\java.exe           set JAVA_HOME=\jdk1.3.1
if exist \jdk1.3.1_02\bin\java.exe        set JAVA_HOME=\jdk1.3.1_02
if exist \j2sdk1.4.0\bin\java.exe         set JAVA_HOME=\j2sdk1.4.0
if exist \j2sdk1.4.2_04\bin\java.exe      set JAVA_HOME=\j2sdk1.4.2_04
if exist \j2sdk1.4.2_05\bin\java.exe      set JAVA_HOME=\j2sdk1.4.2_05
if exist \j2sdk1.4.2_06\bin\java.exe      set JAVA_HOME=\j2sdk1.4.2_06
if exist "\Program Files\Java\jdk1.5.0_02\bin\java.exe"   set JAVA_HOME=\Program Files\Java\jdk1.5.0_02
if exist "\Program Files\Java\jdk1.5.0_04\bin\java.exe"   set JAVA_HOME=\Program Files\Java\jdk1.5.0_04
if not "%JAVA_HOME%" == "" goto checkAnt
echo JAVA_HOME ���ϐ����ݒ肳��Ă��܂���B
goto end

rem ANT_HOME ���ϐ��̌����i�Ɛݒ�j
:checkAnt
if not "%ANT_HOME%" == "" goto runAnt
if exist \apache-ant-1.5.3-1\bin\ant.bat set ANT_HOME=\apache-ant-1.5.3-1
if exist \apache-ant-1.6.5\bin\ant.bat    set ANT_HOME=\apache-ant-1.6.5
if not "%ANT_HOME%" == "" goto runAnt
echo ANT_HOME ���ϐ����ݒ肳��Ă��܂���B
goto end

rem �r���h
:runAnt
call %ANT_HOME%\bin\ant.bat getLibrary
goto end

:end
pause
endlocal