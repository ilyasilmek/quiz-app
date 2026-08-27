# Trivia - Modern Bilgi Yarışması & Quiz Uygulaması

Modern Jetpack Compose ve Material Design 3 ile geliştirilmiş dinamik bilgi yarışması ve trivia Android uygulaması.

---

## 🚀 Özellikler

- **1v1 Hızlı & Arkadaşla Oyun Modları**: Gerçek zamanlı hissi veren turlu maç motoru.
- **Canlı Quiz Ekranı**: 15 saniyelik dinamik zamanlayıcı, çift joker seçeneği (%50 Joker & Seyirci Jokeri) ve anlık puanlama.
- **Kategori Keşfi**: Tarih, Bilim, Coğrafya, Spor, Teknoloji, Sanat, Genel Kültür filtreleri.
- **Quiz Oluşturucu**: 3 adımlı manuel soru hazırlama ve yapay zeka ile otomatik quiz üretimi.
- **İstatistikler & Başarımlar**: Kazanma oranları, kategori başarı halkası, XP ve seviye takibi.

---

## 🛠️ Android Studio ile Kurulum & Gradle Sync

1. Projeyi GitHub'dan klonlayın veya indirin:
   ```bash
   git clone <REPO_URL>
   ```
2. **Android Studio**'yu açın ve `Open...` seçeneğiyle bu proje klasörünü seçin.
3. Proje açıldığında **Gradle Sync** otomatik başlayacaktır. Manuel başlatmak için:
   - Menüden: `File -> Sync Project with Gradle Files` (veya sağ üstteki fil ikonu).
4. JDK sürümünün **Java 17** veya üstü olarak seçili olduğundan emin olun (`Settings -> Build, Execution, Deployment -> Build Tools -> Gradle -> Gradle JDK`).

---

## 📦 APK Derleme (Build APK)

### 1. Android Studio Arayüzü Üzerinden:
- **Debug APK**: `Build -> Build Bundle(s) / APK(s) -> Build APK(s)`
  - Çıktı konumu: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `Build -> Generate Signed Bundle / APK...`

### 2. Komut Satırı Üzerinden:
```bash
# Debug APK derlemek için:
gradle :app:assembleDebug

# Release APK derlemek için:
gradle :app:assembleRelease
```

---

## ⚙️ GitHub Actions CI/CD

Projeye `.github/workflows/build-apk.yml` eklenmiştir. GitHub'a `push` yaptığınızda GitHub Actions otomatik olarak:
1. Projeyi derler.
2. `app-debug.apk` dosyasını Actions sekmesinde indirilebilir **Artifact** olarak sunar.
