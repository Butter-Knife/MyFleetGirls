@echo off
title MyFleetGirls
cd /d %~dp0

REM ���ϐ� JAVA_HOME �܂��� MFG_JAVA_HOME ���ݒ肳��Ă���� PATH �ɒǉ��ݒ�
if Defined JAVA_HOME path %JAVA_HOME%\bin\;%HOME%
if Defined MFG_JAVA_HOME path %MYFFLEETGIRSL_JAVA_HOME%\bin\;%HOME%

REM java �R�}���h�����s�����݊m�F
java -version 1> NUL 2> NUL
if ErrorLevel 1 goto JavaError

REM update.jar �Ŋ֘A�t�@�C�����X�V��AMyFleetGirls.jar ( clinet �{�� ) �����s
java -jar update.jar
java -jar MyFleetGirls.jar
echo "MyFleetGirls ���I�����܂�"
pause > NUL
EXIT 0

:JavaError
echo "JAVA �����^�C��������܂���ł����BREADME.txt ���Q�Ƃ��A�K�؂ɓ������邩���ϐ��̐ݒ���s���Ă��������B"
pause > NUL
EXIT 1
