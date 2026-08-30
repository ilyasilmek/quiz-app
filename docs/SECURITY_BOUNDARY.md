# Güvenlik Sınırı

## Oyuncu uygulaması
Oyuncu uygulaması yalnızca public API ile haberleşir: auth, published questions, matches, leaderboard, submissions ve profile.

## Admin uygulaması
Ayrı uygulama kimliği, ayrı build ve ayrı private API origin kullanılır. MFA, kısa ömürlü token, role/permission kontrolü, audit log ve kritik işlemlerde yeniden doğrulama uygulanır.

## Veri ayrımı
Oyuncu istemcisine maç tamamlanmadan doğru cevap anahtarı, moderation notları, reddedilmiş sorular veya admin kimlikleri verilmez.

## Server authoritative
Skor, süre, doğru cevap, joker ve maç sonucu sunucuda hesaplanır. İstemci yalnızca niyet/cevap gönderir.

## Yayın
submission → automated checks → pending_review → approved/rejected → published.
Oyuncu API'si yalnızca published içeriği okur.
