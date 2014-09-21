@echo off

 

"C:\Windows\System32\taskkill.exe" /f /im java.exe
"C:\Windows\System32\taskkill.exe" /f /im javaw.exe


"C:\Windows\System32\ping.exe" 1.1.1.1 -n 1 -w 500 > nul



start DONT-RUN-THIS-aidans_gravity_game.jar "1"
"C:\Windows\System32\ping.exe" 1.1.1.1 -n 1 -w 1000 > nul
start DONT-RUN-THIS-aidans_gravity_game.jar "1"
