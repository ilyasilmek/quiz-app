# Bilgi Arenası — Uygulama Yol Haritası

## Karar
Ürün tamamen ücretsiz ve reklamsızdır. Kullanıcıdan ücret alınmaz; reklam SDK'sı, abonelik, IAP ve ücretli oyun avantajı bulunmaz.

## Mevcut durum
- Android prototipi Jetpack Compose ile çalışıyor.
- Oyun, profil, kategori, quiz oluşturma ve AI quiz ekranları mevcut.
- Oyun motoru şu anda ViewModel içinde simülasyon ağırlıklı.
- Kalıcı backend ve gerçek multiplayer henüz yok.

## Hedef mimari
Player App → Public Game API → PostgreSQL/Realtime
Admin App → Private Admin API → PostgreSQL
Player App hiçbir koşulda Admin API'sine bağlanmaz.

## Fazlar
1. Temel ürün: katman sınırları, soru önerme, veri modelleri, API sözleşmesi, DB şeması, CI.
2. İçerik platformu: duplicate, moderasyon kuyruğu, onay/reddet/düzelt/yayınla, audit.
3. Gerçek oyun: auth, server-authoritative match, realtime, skor/süre doğrulama, hile önleme.
4. Sosyal: arkadaşlar, rövanş, görevler, seri, lig/sezon, bildirim.
5. AI + UGC: AI üretim/doğrulama, kullanıcı quizleri, raporlama, kalite puanı.

## Ücretsiz/reklamsız sürdürülebilirlik
Gelir baskısı yaratacak özellikler mimariye sokulmayacaktır. Düşük işletme maliyeti için cache, CDN, sorgu optimizasyonu ve kota kontrolü kullanılacaktır.

## İlk üretim hedefi
UI genişletmekten önce güvenilir soru bankası ve server-authoritative oyun çekirdeği tamamlanacaktır.
