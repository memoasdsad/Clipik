#!/bin/bash

echo "🔧 Corrigindo problemas de compatibilidade Gradle/Java..."

# Limpar cache do Gradle
echo "1. Limpando cache do Gradle..."
rm -rf ~/.gradle/caches/
rm -rf .gradle/

# Limpar build
echo "2. Limpando build..."
rm -rf build/
rm -rf app/build/

# Atualizar wrapper do Gradle
echo "3. Atualizando Gradle Wrapper..."
./gradlew wrapper --gradle-version=8.6 --distribution-type=bin

# Verificar versão
echo "4. Verificando versão do Gradle..."
./gradlew --version

echo "✅ Correção concluída!"
echo ""
echo "📋 Próximos passos no Android Studio:"
echo "1. File → Invalidate Caches and Restart"
echo "2. File → Settings → Build → Build Tools → Gradle"
echo "3. Gradle JDK: Selecione JDK 17 (não 21)"
echo "4. Apply → OK"
echo "5. Sync Project"