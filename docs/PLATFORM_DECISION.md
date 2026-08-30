# Platform Kararı

## Karar
Mevcut Android prototipi Jetpack Compose olduğu için Flutter'a toplu yeniden yazım yapılmayacaktır.

Hedef:
- Android: Jetpack Compose
- iOS: SwiftUI
- Ortak katman: Kotlin Multiplatform (KMP)

## Ortaklaştırılacak
- Domain modelleri
- Oyun kuralları
- Skor hesaplama kuralları (sunucu otoritesi ile aynı sözleşme)
- API DTO'ları
- Repository sözleşmeleri
- Normalizasyon/validation yardımcıları
- Analytics event sözleşmeleri
- Feature flags

## Platforma özgü kalacak
- UI
- Navigation ayrıntıları
- Push notification entegrasyon detayları
- Store entegrasyonları
- Platform kimlik doğrulama köprüleri

## Neden
Çalışan Android UI'yi çöpe atmadan iOS eklenebilir. En kritik oyun kuralları iki platform arasında ortak kodda tutulur; sunucu yine nihai otoritedir.

## Geçiş sırası
1. Domain modellerini commonMain'e taşı.
2. API sözleşmelerini commonMain'e taşı.
3. Android repository'leri KMP implementasyonuna geçir.
4. iOS SwiftUI istemcisini ekle.
5. Ortak testleri JVM/iOS üzerinde çalıştır.
