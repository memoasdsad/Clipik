#!/bin/bash

echo "🔧 FORÇANDO atualização completa do Gradle para JDK 21..."

# Parar todos os daemons do Gradle
echo "1. Parando daemons do Gradle..."
./gradlew --stop

# Limpar TUDO relacionado ao Gradle
echo "2. Limpando cache completo do Gradle..."
rm -rf ~/.gradle/caches/
rm -rf ~/.gradle/wrapper/
rm -rf .gradle/
rm -rf build/
rm -rf app/build/
rm -rf */build/

# Limpar cache do Android Studio
echo "3. Limpando cache do Android Studio..."
rm -rf ~/.android/build-cache/

# Forçar download do Gradle 9.0
echo "4. Forçando download do Gradle 9.0-milestone-1..."
./gradlew wrapper --gradle-version=9.0-milestone-1 --distribution-type=bin

# Verificar se foi atualizado
echo "5. Verificando versão do Gradle..."
./gradlew --version

echo ""
echo "✅ ATUALIZAÇÃO FORÇADA CONCLUÍDA!"
echo ""
echo "🚨 IMPORTANTE - Faça isso no Android Studio:"
echo "1. Feche COMPLETAMENTE o Android Studio"
echo "2. File → Invalidate Caches and Restart → Invalidate and Restart"
echo "3. File → Settings → Build Tools → Gradle"
echo "4. Gradle JDK: JDK 21"
echo "5. Use Gradle from: 'gradle-wrapper.properties' file"
echo "6. Apply → OK"
echo "7. Sync Project"
echo ""
echo "🎯 Se ainda der erro, delete a pasta .idea e reabra o projeto"