# 🚀 Guia de Instalação - Clipik

## 📋 Pré-requisitos

### Software Necessário
- **Android Studio**: Hedgehog (2023.1.1) ou superior
- **JDK**: 11 ou superior
- **Android SDK**: API 34 (Android 14)
- **Gradle**: 8.0+ (incluído no projeto)

### Hardware Recomendado
- **RAM**: Mínimo 8GB (recomendado 16GB)
- **Armazenamento**: 10GB livres
- **Processador**: Intel i5 ou AMD equivalente

## 🔧 Passos de Instalação

### 1. Preparar o Ambiente

```bash
# Verificar se o Java está instalado
java -version

# Verificar se o Android Studio está instalado
# Abra o Android Studio e verifique se o SDK está configurado
```

### 2. Configurar o Projeto

1. **Abrir o Android Studio**
2. **File → Open** → Selecionar a pasta `/workspace/project/Clipik`
3. **Aguardar a sincronização** do Gradle (pode demorar alguns minutos)

### 3. Configurar o SDK

1. **File → Settings → Appearance & Behavior → System Settings → Android SDK**
2. **Verificar se está instalado**:
   - Android 14 (API 34)
   - Android SDK Build-Tools 34.0.0
   - Android SDK Platform-Tools

### 4. Configurar Dependências

O projeto já inclui todas as dependências necessárias:

```gradle
// Principais dependências já configuradas:
- androidx.media3:media3-exoplayer:1.2.0
- com.arthenica:ffmpeg-kit-full:5.1
- com.google.android.gms:play-services-ads:22.6.0
- com.karumi:dexter:6.2.3
```

### 5. Configurar AdMob (Opcional)

Para usar seus próprios anúncios:

1. **Editar `app/src/main/res/values/strings.xml`**:
```xml
<!-- Substitua pelos seus IDs reais -->
<string name="ad_banner_unit_id">ca-app-pub-SEU_ID~BANNER_ID</string>
<string name="ad_interstitial_unit_id">ca-app-pub-SEU_ID~INTERSTITIAL_ID</string>
```

2. **Editar `app/src/main/AndroidManifest.xml`**:
```xml
<!-- Adicione seu App ID -->
<meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-SEU_APP_ID~APP_ID"/>
```

## 📱 Executar o App

### Opção 1: Dispositivo Real (Recomendado)

1. **Ativar Depuração USB** no dispositivo:
   - Configurações → Sobre o telefone → Tocar 7x em "Número da versão"
   - Configurações → Opções do desenvolvedor → Ativar "Depuração USB"

2. **Conectar o dispositivo** via USB

3. **Executar**: Clique em ▶️ (Run) ou pressione `Shift+F10`

### Opção 2: Emulador

1. **Tools → AVD Manager**
2. **Create Virtual Device**
3. **Escolher**: Pixel 6 ou similar (API 34)
4. **Executar**: Clique em ▶️ (Run)

## 🔍 Verificar Instalação

### Checklist de Funcionamento

- [ ] App abre sem erros
- [ ] Botão "Selecionar Vídeo" funciona
- [ ] Permissões são solicitadas corretamente
- [ ] Player de vídeo carrega
- [ ] Timeline aparece com thumbnails
- [ ] Botões "Status" e "Reels" funcionam
- [ ] Exportação completa sem erros
- [ ] Anúncios aparecem (se configurados)

### Logs de Debug

Para verificar problemas:

1. **View → Tool Windows → Logcat**
2. **Filtrar por**: `com.victor.clipikv2`
3. **Procurar por**: Erros em vermelho

## 🐛 Solução de Problemas Comuns

### Erro: "SDK not found"
```bash
# Solução: Configurar local.properties
echo "sdk.dir=/caminho/para/android/sdk" > local.properties
```

### Erro: "FFmpeg not found"
```bash
# Solução: Limpar e recompilar
./gradlew clean
./gradlew build
```

### Erro: "Permission denied"
- Verificar se as permissões estão no AndroidManifest.xml
- Testar em dispositivo real (não emulador)

### Erro: "Out of memory"
```bash
# Solução: Aumentar heap size no gradle.properties
org.gradle.jvmargs=-Xmx4g -XX:MaxPermSize=512m
```

### App trava ao selecionar vídeo
- Testar com vídeos menores (< 100MB)
- Verificar se o formato é suportado (MP4, AVI, MOV)

## 📊 Performance

### Otimizações Incluídas
- **Processamento assíncrono**: Não trava a UI
- **Cache de thumbnails**: Timeline rápida
- **Compressão inteligente**: Vídeos menores
- **Limpeza automática**: Remove arquivos temporários

### Requisitos Mínimos do Dispositivo
- **Android**: 8.0+ (API 26)
- **RAM**: 3GB
- **Armazenamento**: 1GB livre
- **Processador**: Snapdragon 660 ou equivalente

## 🚀 Deploy para Produção

### Gerar APK de Release

1. **Build → Generate Signed Bundle/APK**
2. **Escolher**: APK
3. **Criar keystore** (se não tiver)
4. **Configurar**: Release build
5. **Gerar**: APK assinado

### Preparar para Google Play

1. **Aumentar versionCode** em `build.gradle`
2. **Configurar ProGuard** (já incluído)
3. **Testar** em múltiplos dispositivos
4. **Upload** para Play Console

## 📞 Suporte

### Problemas de Instalação
- Verificar logs do Android Studio
- Limpar cache: `File → Invalidate Caches and Restart`
- Reinstalar dependências: `./gradlew clean build`

### Problemas de Execução
- Verificar permissões do dispositivo
- Testar com vídeos diferentes
- Verificar espaço de armazenamento

---

**✅ Instalação concluída com sucesso!**

Agora você pode testar todas as funcionalidades do Clipik e fazer as personalizações necessárias.