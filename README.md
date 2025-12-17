# Clipik - Editor de Vídeo Automático

Clipik é um aplicativo Android de edição de vídeo simples, rápido e automático, focado em transformar vídeos longos em:
- Status de 30 segundos (WhatsApp)
- Reels de 60 segundos (Instagram/YouTube Shorts)

## 🚀 Características Principais

- **Automação em 1 clique**: Corte inteligente automático
- **Interface simples**: Menos botões que o CapCut
- **Timeline profissional**: Visual estilo CapCut
- **100% offline**: Processamento local
- **Alta performance**: Funciona em celulares fracos
- **Formato vertical**: Conversão automática para 9:16

## 📱 Funcionalidades

### ✅ Implementadas
- Seleção de vídeo da galeria
- Player com preview em tempo real (Media3 ExoPlayer)
- Timeline horizontal com thumbnails
- Cursor de tempo arrastável
- Botões para criar Status (30s) e Reels (60s)
- Corte automático inteligente
- Ajuste automático para formato vertical 9:16
- Exportação usando FFmpeg Kit
- Monetização com AdMob (banner + interstitial)

### 🚫 Não Incluídas (por design)
- Adição de músicas
- Filtros ou efeitos
- Backend/nuvem/login

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **Player**: androidx.media3 ExoPlayer
- **Processamento**: FFmpeg Kit
- **Ads**: Google AdMob
- **UI**: Material Design 3
- **Compatibilidade**: Android 8+ (API 26+)

## 📁 Estrutura do Projeto

```
app/
├── src/main/
│   ├── java/com/victor/clipikv2/
│   │   ├── activities/
│   │   │   ├── MainActivity.kt          # Tela inicial
│   │   │   ├── EditorActivity.kt        # Editor principal
│   │   │   └── ExportActivity.kt        # Tela de exportação
│   │   ├── adapters/
│   │   │   └── TimelineAdapter.kt       # Adapter da timeline
│   │   ├── models/
│   │   │   └── TimelineItem.kt          # Modelo da timeline
│   │   └── utils/
│   │       ├── VideoProcessor.kt        # Processamento de vídeo
│   │       ├── PermissionHelper.kt      # Gerenciamento de permissões
│   │       └── FileUtils.kt             # Utilitários de arquivo
│   └── res/
│       ├── layout/                      # Layouts XML
│       ├── drawable/                    # Ícones e recursos gráficos
│       ├── values/                      # Cores, strings, estilos
│       └── xml/                         # Configurações
```

## 🔧 Como Compilar e Executar

### Pré-requisitos
- **Android Studio**: Hedgehog (2023.1.1) ou superior
- **JDK**: 17 ou superior (recomendado JDK 17)
- **Android SDK**: API 35 (Android 14)
- **Gradle**: 8.5+ (incluído no projeto)
- **Dispositivo/emulador**: Android 8+ (API 26+)

### Passos

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/memoasdsad/Clipik.git
   cd Clipik
   ```

2. **Configure o SDK**:
   - Copie `local.properties.template` para `local.properties`
   - Edite `local.properties` e configure o caminho do seu Android SDK:
   ```properties
   sdk.dir=/caminho/para/seu/android/sdk
   ```

3. **Abra no Android Studio**:
   - File → Open → Selecione a pasta do projeto
   - Aguarde a indexação dos arquivos

4. **Sincronize o projeto**:
   - Clique em "Sync Now" quando solicitado
   - Aguarde o download das dependências (pode demorar alguns minutos)
   - Se houver erro de compatibilidade Java/Gradle, veja a seção "Solução de Problemas"

5. **Execute o app**:
   - Conecte um dispositivo Android ou inicie um emulador
   - Clique em "Run" (▶️) ou pressione Shift+F10

### ⚠️ Solução de Problemas

#### 🚨 **Erro: "Incompatible Java 21 and Gradle"**

**SOLUÇÃO RÁPIDA:**
1. **File → Settings → Build Tools → Gradle**
2. **Gradle JDK: Selecione JDK 17** (NÃO use JDK 21)
3. **Apply → OK**
4. **File → Invalidate Caches and Restart**

**SOLUÇÃO AUTOMÁTICA:**
```bash
# Linux/macOS
./fix_gradle.sh

# Windows
fix_gradle.bat
```

📋 **[Guia Completo de Soluções](SOLUCAO_PROBLEMAS.md)**

#### Outros Problemas Comuns

**SDK not found:**
```bash
echo "sdk.dir=/caminho/para/android/sdk" > local.properties
```

**Build failed:**
```bash
./gradlew clean build
```

## 📋 Dependências Principais

```gradle
// Player de vídeo
implementation "androidx.media3:media3-exoplayer:1.2.0"
implementation "androidx.media3:media3-ui:1.2.0"

// Processamento de vídeo
implementation "com.arthenica:ffmpeg-kit-full:5.1"

// AdMob
implementation "com.google.android.gms:play-services-ads:22.6.0"

// Permissões
implementation "com.karumi:dexter:6.2.3"

// Material Design
implementation "com.google.android.material:material:1.11.0"
```

## 🎯 Como Usar o App

1. **Selecionar Vídeo**: Toque no botão para escolher um vídeo da galeria
2. **Visualizar**: Use a timeline para navegar pelo vídeo
3. **Criar Clip**: Escolha "Status (30s)" ou "Reels (60s)"
4. **Aguardar**: O app processará automaticamente
5. **Compartilhar**: Use os botões para compartilhar ou salvar

## 🔐 Configuração do AdMob

Para usar seus próprios anúncios:

1. **Substitua os IDs em `strings.xml`**:
   ```xml
   <string name="ad_banner_unit_id">SEU_BANNER_ID</string>
   <string name="ad_interstitial_unit_id">SEU_INTERSTITIAL_ID</string>
   ```

2. **Adicione o App ID no `AndroidManifest.xml`**:
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="SEU_APP_ID"/>
   ```

## 🐛 Solução de Problemas

### Erro de compilação FFmpeg
- Certifique-se de ter espaço suficiente (>2GB)
- Limpe o projeto: Build → Clean Project

### Erro de permissões
- Verifique se as permissões estão no AndroidManifest.xml
- Teste em dispositivo real (emulador pode ter limitações)

### Player não funciona
- Verifique se o vídeo está em formato suportado (MP4, AVI, MOV)
- Teste com vídeos menores primeiro

## 📄 Licença

Este projeto é fornecido como exemplo educacional. Certifique-se de ter as licenças apropriadas para uso comercial das bibliotecas utilizadas.

## 🤝 Contribuições

Para melhorias e correções:
1. Faça um fork do projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Abra um Pull Request

## 📞 Suporte

Para dúvidas sobre o código ou implementação, abra uma issue no repositório.

---

**Desenvolvido com ❤️ para a comunidade Android**