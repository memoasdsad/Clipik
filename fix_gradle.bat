@echo off
echo 🔧 Configurando projeto para JDK 21 (Google Play Store)...

REM Verificar JDK 21
echo 1. Verificando JDK 21...
java -version

REM Limpar cache do Gradle
echo 2. Limpando cache do Gradle...
rmdir /s /q "%USERPROFILE%\.gradle\caches" 2>nul
rmdir /s /q ".gradle" 2>nul

REM Limpar build
echo 3. Limpando build...
rmdir /s /q "build" 2>nul
rmdir /s /q "app\build" 2>nul

REM Atualizar wrapper do Gradle
echo 4. Atualizando Gradle Wrapper para 8.7...
gradlew.bat wrapper --gradle-version=8.7 --distribution-type=bin

REM Verificar versão
echo 5. Verificando versão do Gradle...
gradlew.bat --version

echo ✅ Configuração para JDK 21 concluída!
echo.
echo 📋 Próximos passos no Android Studio:
echo 1. File → Invalidate Caches and Restart
echo 2. File → Settings → Build → Build Tools → Gradle
echo 3. Gradle JDK: Selecione JDK 21 (para Google Play Store)
echo 4. Apply → OK
echo 5. Sync Project
echo.
echo 🚀 Projeto agora compatível com Google Play Store!

pause