@ECHO OFF
SETLOCAL enableextensions enabledelayedexpansion

:main
ECHO.
ECHO Select an action to perform:
ECHO [b]uild
ECHO [c]lean
ECHO [e]xit
SET /p action="Enter your choice: "

IF /i "%action%"=="b" GOTO build
IF /i "%action%"=="build" GOTO build
IF /i "%action%"=="c" GOTO clean
IF /i "%action%"=="clean" GOTO clean
IF /i "%action%"=="e" GOTO exit
IF /i "%action%"=="exit" GOTO exit

ECHO Only the values: 'b' or 'build', 'c' or 'clean', 'e' or 'exit' are allowed^^!
GOTO main

:build
ECHO.
ECHO Which compiler to use when compiling?
ECHO [j2me] (J2ME CLDC 1.1+)
ECHO [1.4] (J2SE 1.4+)
ECHO [1.5] (J2SE 1.5+)
ECHO [1.6] (J2SE 1.6+)
ECHO [e]xit
SET /p compiler="Enter your choice: "

IF "%compiler%"=="j2me" (
	ECHO Set JDK version to 5.0u22...
	SET executable="C:\Program Files (x86)\Java\jdk1.5.0_22\bin\javac.exe"
	GOTO compile
) ELSE IF "%compiler%"=="1.4" (
	ECHO Set JDK version to 5.0u22...
	SET executable="C:\Program Files (x86)\Java\jdk1.5.0_22\bin\javac.exe"
	GOTO compile
) ELSE IF "%compiler%"=="1.5" (
	ECHO Set JDK version to 5.0u22...
	SET executable="C:\Program Files (x86)\Java\jdk1.5.0_22\bin\javac.exe"
	GOTO compile
) ELSE IF "%compiler%"=="1.6" (
	ECHO Set JDK version to 6u45...
	SET executable="C:\Program Files (x86)\Java\jdk1.6.0_45\bin\javac.exe"
	GOTO compile
) ELSE IF /i "%compiler%"=="e" (
	ECHO Back to main menu...
	GOTO main
) ELSE (
	ECHO Only the values: 'j2me' or '1.4' or '1.5' or '1.6' or 'e' are allowed^^!
	GOTO build
)

:compile
IF EXIST !executable! (
	ECHO Call ant !compiler! command...
	CALL ant -Dexecutable=!executable! !compiler!
	IF %ERRORLEVEL% NEQ 0 (
		ECHO Ant compile command failed^^! Error code: %ERRORLEVEL%.
		ECHO See Ant output for more details.
	) ELSE (
		ECHO Ant compile command completed successfully.
	)
) ELSE (
	ECHO Error: !executable! file not found^^!
)
GOTO main

:clean
ECHO Call ant clean command...
CALL ant clean
IF %ERRORLEVEL% NEQ 0 (
	ECHO Ant clean command failed^^! Error code: %ERRORLEVEL%.
	ECHO See Ant output for more details.
) ELSE (
	ECHO Ant clean command completed successfully.
)
GOTO main

:exit
ENDLOCAL
EXIT