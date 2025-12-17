# 🔧 Solução de Problemas - Clipik

## ❌ Erro: "Incompatible Java 21.0.8 and Gradle 8.0"

### 🎯 **Solução Definitiva (Passo a Passo)**

#### **Método 1: Configurar JDK no Android Studio (RECOMENDADO)**

1. **Abra o Android Studio**
2. **File → Settings** (ou **Android Studio → Preferences** no macOS)
3. **Build, Execution, Deployment → Build Tools → Gradle**
4. **Gradle JDK**: Selecione **JDK 17** (NÃO use JDK 21)
5. **Apply → OK**
6. **File → Invalidate Caches and Restart**
7. **Sync Project**

#### **Método 2: Script Automático**

**Linux/macOS:**
```bash
./fix_gradle.sh
```

**Windows:**
```cmd
fix_gradle.bat
```

#### **Método 3: Manual**

1. **Feche o Android Studio**
2. **Delete cache do Gradle:**
   ```bash
   # Linux/macOS
   rm -rf ~/.gradle/caches/
   rm -rf .gradle/
   
   # Windows
   rmdir /s /q "%USERPROFILE%\.gradle\caches"
   rmdir /s /q ".gradle"
   ```
3. **Atualize o Gradle Wrapper:**
   ```bash
   ./gradlew wrapper --gradle-version=8.6
   ```
4. **Abra o Android Studio novamente**
5. **Configure JDK 17** (Método 1)

---

## 🔍 **Verificações Importantes**

### ✅ **Versões Corretas**
- **Gradle**: 8.6
- **Android Gradle Plugin**: 8.2.2
- **Kotlin**: 1.9.22
- **JDK**: 17 (NÃO 21)

### ✅ **Arquivos Verificados**
- `gradle/wrapper/gradle-wrapper.properties` → Gradle 8.6
- `build.gradle` → AGP 8.2.2
- `gradle.properties` → Configurações otimizadas

---

## 🚨 **Outros Problemas Comuns**

### **Erro: "SDK not found"**
```bash
# Crie/edite local.properties
echo "sdk.dir=/caminho/para/android/sdk" > local.properties

# Exemplos de caminhos:
# Windows: C:\\Users\\SeuUsuario\\AppData\\Local\\Android\\Sdk
# macOS: /Users/SeuUsuario/Library/Android/sdk
# Linux: /home/SeuUsuario/Android/Sdk
```

### **Erro: "Build failed"**
```bash
# Limpe e recompile
./gradlew clean
./gradlew build
```

### **Erro: "Out of memory"**
- Já configurado 4GB no `gradle.properties`
- Feche outros aplicativos
- Reinicie o Android Studio

### **Erro: "FFmpeg not found"**
```bash
# Limpe cache e baixe novamente
./gradlew clean
./gradlew build --refresh-dependencies
```

### **Erro: "Permission denied"**
- Teste em dispositivo real (não emulador)
- Verifique permissões no AndroidManifest.xml
- Conceda permissões manualmente no dispositivo

---

## 🎯 **Configuração Ideal do Android Studio**

### **JDK Configuration**
1. **File → Project Structure**
2. **SDK Location → JDK Location**: JDK 17
3. **Apply**

### **Gradle Settings**
1. **File → Settings → Build Tools → Gradle**
2. **Use Gradle from**: 'gradle-wrapper.properties' file
3. **Gradle JDK**: JDK 17
4. **Apply**

### **Memory Settings**
1. **Help → Edit Custom VM Options**
2. Adicione:
   ```
   -Xmx4096m
   -XX:MaxMetaspaceSize=1024m
   ```

---

## 📱 **Teste de Funcionamento**

Execute este checklist após resolver os problemas:

1. **Sync Project** ✅
2. **Build → Clean Project** ✅
3. **Build → Rebuild Project** ✅
4. **Run App** ✅

Se todos passarem, o projeto está funcionando!

---

## 🆘 **Se Nada Funcionar**

### **Reset Completo**
```bash
# 1. Feche Android Studio
# 2. Delete tudo relacionado ao Gradle
rm -rf ~/.gradle/
rm -rf .gradle/
rm -rf build/
rm -rf app/build/

# 3. Clone novamente
git clone https://github.com/memoasdsad/Clipik.git
cd Clipik

# 4. Configure local.properties
cp local.properties.template local.properties
# Edite com o caminho do seu SDK

# 5. Abra no Android Studio
# 6. Configure JDK 17
# 7. Sync Project
```

### **Versões Alternativas**
Se ainda houver problemas, use estas versões mais conservadoras:

**gradle-wrapper.properties:**
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.4-bin.zip
```

**build.gradle:**
```gradle
classpath "com.android.tools.build:gradle:8.1.4"
```

---

## 📞 **Suporte**

Se o problema persistir:
1. Verifique a versão do seu Android Studio
2. Verifique a versão do JDK instalado
3. Abra uma issue no GitHub com:
   - Versão do Android Studio
   - Versão do JDK
   - Sistema operacional
   - Log completo do erro

---

**🎬 Com essas soluções, o Clipik funcionará perfeitamente! 🚀**