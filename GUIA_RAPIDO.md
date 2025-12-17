# 🚀 Guia Rápido - Clipik

## ⚡ Instalação em 5 Passos

### 1. **Clone o Repositório**
```bash
git clone https://github.com/memoasdsad/Clipik.git
cd Clipik
```

### 2. **Configure o SDK**
```bash
# Copie o template
cp local.properties.template local.properties

# Edite e adicione o caminho do seu Android SDK
# Exemplo: sdk.dir=/Users/SeuUsuario/Library/Android/sdk
```

### 3. **Abra no Android Studio**
- File → Open → Selecione a pasta `Clipik`
- Aguarde a indexação

### 4. **Resolva Problemas de Compatibilidade**

#### Se aparecer erro "Incompatible Java 21 and Gradle":

**Opção A (Recomendada):**
- File → Settings → Build → Build Tools → Gradle
- Gradle JDK: Selecione **JDK 17**
- Apply → OK

**Opção B:**
- O projeto já usa Gradle 8.5 (compatível)
- Apenas sincronize novamente

### 5. **Execute o App**
- Conecte dispositivo Android ou inicie emulador
- Clique em ▶️ **Run**

---

## 🔧 Versões Configuradas

| Componente | Versão |
|------------|--------|
| **Gradle** | 8.5 |
| **Android Gradle Plugin** | 8.2.0 |
| **Kotlin** | 1.9.20 |
| **Compile SDK** | 35 |
| **Target SDK** | 35 |
| **Min SDK** | 26 (Android 8+) |

---

## ⚠️ Problemas Comuns

### ❌ "SDK not found"
```bash
echo "sdk.dir=/caminho/para/android/sdk" > local.properties
```

### ❌ "Build failed"
```bash
./gradlew clean
./gradlew build
```

### ❌ "Out of memory"
- Já configurado: 4GB RAM no `gradle.properties`
- Se persistir, feche outros apps

---

## ✅ Teste Rápido

1. **App abre** ✓
2. **Selecionar vídeo** ✓
3. **Player funciona** ✓
4. **Timeline aparece** ✓
5. **Botões Status/Reels** ✓
6. **Exportação completa** ✓

---

## 🎯 Próximos Passos

Após instalação bem-sucedida:

1. **Teste com vídeos pequenos** (< 100MB)
2. **Configure AdMob** com seus IDs reais
3. **Teste em dispositivo real**
4. **Gere APK de release**

---

**🎬 Clipik pronto para usar! 🚀**