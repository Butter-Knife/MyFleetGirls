@echo off
title MyFleetGirls
SET MFG_HOME=%~dp0
If Defined b2eprogrampathname set MFG_HOME=%b2eprogrampathname%
cd /d %MGF_HOME%

REM ���ϐ� JAVA_HOME �܂��� MFG_JAVA_HOME ���ݒ肳��Ă���� PATH �ɒǉ��ݒ�
If Defined JAVA_HOME path %JAVA_HOME%\bin\;%PATH%
If Defined MFG_JAVA_HOME path %MFG_JAVA_HOME%\bin\;%PATH%

REM java �R�}���h�����s�����݊m�F
java.exe -version 2> NUL
If ErrorLevel 1 goto JavaError

REM update.jar �Ŋ֘A�t�@�C�����X�V��AMyFleetGirls.jar ( clinet �{�� ) �����s
java.exe -jar update.jar
java.exe -jar MyFleetGirls.jar
echo MyFleetGirls ���I�����܂�
pause > NUL
EXIT 0

:JavaError
echo JAVA �����^�C����������܂���ł����BREADME.txt ���Q�Ƃ��������邩�A���ϐ��̐ݒ���s���Ă��������B
pause > NUL
EXIT 1
