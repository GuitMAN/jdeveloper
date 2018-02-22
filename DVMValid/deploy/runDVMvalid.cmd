echo off
set dvm=
echo %dvm%
cd /d %~dp0
 
java -jar dvmvalid.jar 

pause
