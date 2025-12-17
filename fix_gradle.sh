#!/bin/bash

echo "🔧 Configurando projeto para JDK 21 (Google Play Store)..."

# Verificar JDK 21
echo "1. Verificando JDK 21..."
java -version

# Limpar cache do Gradle
echo "2. Limpando cache do Gradle..."
rm -rf ~/.gradle/caches/
rm -rf .gradle/

# Limpar build
echo "3. Limpando build..."
rm -rf build/
rm -rf app/build/

# Atualizar wrapper do Gradle
echo "4. Atualizando Gradle Wrapper para 8.7..."
./gradlew wrapper --gradle-version=8.7 --distribution-type=bin

# Verificar versão
echo "5. Verificando versão do Gradle..."
./gradlew --version

echo "✅ Configuração para JDK 21 concluída!"
echo ""
echo "📋 Próximos passos no Android Studio:"
echo "1. File → Invalidate Caches and Restart"
echo "2. File → Settings → Build → Build Tools → Gradle"
echo "3. Gradle JDK: Selecione JDK 21 (para Google Play Store)"
echo "4. Apply → OK"
echo "5. Sync Project"
echo ""
echo "🚀 Projeto agora compatível com Google Play Store!"