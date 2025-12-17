@echo off
echo 🔧 Corrigindo problemas de compatibilidade Gradle/Java...

REM Limpar cache do Gradle
echo 1. Limpando cache do Gradle...
rmdir /s /q "%USERPROFILE%\.gradle\caches" 2>nul
rmdir /s /q ".gradle" 2>nul

REM Limpar build
echo 2. Limpando build...
rmdir /s /q "build" 2>nul
rmdir /s /q "app\build" 2>nul

REM Atualizar wrapper do Gradle
echo 3. Atualizando Gradle Wrapper...
gradlew.bat wrapper --gradle-version=8.6 --distribution-type=bin

REM Verificar versão
echo 4. Verificando versão do Gradle...
gradlew.bat --version

echo ✅ Correção concluída!
echo.
echo 📋 Próximos passos no Android Studio:
echo 1. File → Invalidate Caches and Restart
echo 2. File → Settings → Build → Build Tools → Gradle
echo 3. Gradle JDK: Selecione JDK 17 (não 21)
echo 4. Apply → OK
echo 5. Sync Project

pause