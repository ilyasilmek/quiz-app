# Soru Moderasyon Akışı

1. Oyuncu soru, dört şık, doğru cevap, kategori ve mümkünse kaynak/açıklama girer.
2. İstemci form doğrulaması yapar.
3. Public API submission oluşturur.
4. Sunucu Unicode/boşluk/noktalama normalizasyonu yapar.
5. Exact hash ve fingerprint kontrolü yapılır.
6. Fuzzy similarity; ileri aşamada embedding similarity çalışır.
7. Risk/kalite kontrolleri uygulanır.
8. Kuyruğa alınır.
9. Admin soruyu ve doğru cevabı teyit eder.
10. Admin onaylar, düzenler, reddeder veya arşivler.
11. Yayınlanan içerik published snapshot'a alınır.
12. Audit log yazılır.

Duplicate bulunduğunda otomatik yayın yapılmaz; admin'e aday eşleşmeler gösterilir.
