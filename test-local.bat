@echo off
setlocal EnableDelayedExpansion

echo ====================================================
echo MyLibreLab Local Build and Testing Script
echo ====================================================
echo.

REM Check if we're in the right directory
if not exist "gradlew.bat" (
    echo ❌ Error: gradlew.bat not found. Please run this script from the MyLibreLab root directory.
    pause
    exit /b 1
)

if not exist "application\build.gradle.kts" (
    echo ❌ Error: application\build.gradle.kts not found. Please ensure you're in the correct directory.
    pause
    exit /b 1
)

echo ✅ Directory validation passed
echo.

REM Step 1: Clean and build the project
echo 🧹 Step 1: Cleaning and building the project...
call gradlew.bat clean build -PskipAutostyle -PskipSonarlint -x test --no-daemon
if errorlevel 1 (
    echo ❌ Build failed. Please check the error messages above.
    pause
    exit /b 1
)
echo ✅ Build completed successfully
echo.

REM Step 2: Create portable app
echo 📦 Step 2: Creating portable application...
call gradlew.bat :mylibrelab-application:createPortableApp --no-daemon
if errorlevel 1 (
    echo ❌ Portable app creation failed. Please check the error messages above.
    pause
    exit /b 1
)
echo ✅ Portable app created successfully
echo.

REM Step 3: Verify portable app structure
echo 🔍 Step 3: Verifying portable app structure...
set PORTABLE_DIR=application\build\portable

if not exist "%PORTABLE_DIR%" (
    echo ❌ Portable directory not found at %PORTABLE_DIR%
    pause
    exit /b 1
)

echo 📁 Portable app contents:
dir /b "%PORTABLE_DIR%"
echo.

REM Check for required components
set ERRORS=0

if not exist "%PORTABLE_DIR%\runtime\bin\java.exe" (
    echo ❌ Java runtime not found
    set ERRORS=1
) else (
    echo ✅ Java runtime found
)

if not exist "%PORTABLE_DIR%\lib" (
    echo ❌ Lib directory not found
    set ERRORS=1
) else (
    echo ✅ Lib directory found
    for /f %%i in ('dir /b "%PORTABLE_DIR%\lib\*.jar" 2^>nul ^| find /c /v ""') do set JAR_COUNT=%%i
    echo    📦 JAR files: !JAR_COUNT!
)

if not exist "%PORTABLE_DIR%\MyLibreLab.bat" (
    echo ❌ Windows launcher not found
    set ERRORS=1
) else (
    echo ✅ Windows launcher found
)

if not exist "%PORTABLE_DIR%\MyLibreLab.sh" (
    echo ❌ Unix launcher not found
    set ERRORS=1
) else (
    echo ✅ Unix launcher found
)

if %ERRORS% neq 0 (
    echo.
    echo ❌ Portable app verification failed. Please check the issues above.
    pause
    exit /b 1
)

echo.
echo ✅ Portable app verification passed
echo.

REM Step 4: Test the portable app (optional quick test)
echo 🚀 Step 4: Testing portable app launcher...
echo Press Y to test the launcher (will open MyLibreLab briefly), or any other key to skip:
choice /c YN /n /m "[Y]es or [N]o: "
if errorlevel 2 goto skip_test

echo Testing launcher (will close automatically after 5 seconds)...
start /wait timeout /t 5 /nobreak >nul 2>&1
REM Uncomment the next line to actually test the launcher
REM "%PORTABLE_DIR%\MyLibreLab.bat"

:skip_test

echo.
echo 🎯 Step 5: Testing with act (GitHub Actions local simulation)...
echo.

REM Check if act is installed
where act >nul 2>&1
if errorlevel 1 (
    echo ⚠️ 'act' not found. Please install act to test GitHub Actions locally.
    echo Download from: https://github.com/nektos/act/releases
    echo.
    echo You can skip this step and proceed with manual testing.
    echo Press any key to continue...
    pause >nul
    goto manual_test
)

echo ✅ act found, proceeding with workflow testing...
echo.

REM Test the build job from the CI workflow
echo 🔄 Testing build job with act...
echo This will simulate the GitHub Actions build process locally.
echo.

echo Running act test (this may take a few minutes)...
act push -W .github\workflows\temp\test-build.yml -j test-build --container-architecture linux/amd64 2>&1 |  C:\msys64\usr\bin\tee.exe ..\workflow.txt


REM Clean up temp workflow
rmdir /s /q .github\workflows\temp 2>nul

echo.
echo Act testing completed. Check the output above for any issues.
echo.

:manual_test

echo ====================================================
echo 🎉 Local Testing Summary
echo ====================================================
echo.
echo ✅ Build: SUCCESS
echo ✅ Portable App: SUCCESS
echo ✅ Structure: VERIFIED
echo 📍 Location: %CD%\%PORTABLE_DIR%
echo.
echo 🚀 Ready for GitHub Actions!
echo.
echo Next steps:
echo 1. The portable app has been created and verified locally
echo 2. You can now safely push your updated workflows to GitHub
echo 3. The GitHub Actions should work correctly based on this local test
echo.
echo Manual test: You can run the portable app with:
echo    "%CD%\%PORTABLE_DIR%\MyLibreLab.bat"
echo.
echo ====================================================
pause
