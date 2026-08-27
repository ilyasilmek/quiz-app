package com.example.data.sample

import com.example.data.models.AchievementItem
import com.example.data.models.CategoryItem
import com.example.data.models.FriendItem
import com.example.data.models.Question
import com.example.data.models.Quiz

object SampleData {

    const val AVATAR_ILYAS = "https://lh3.googleusercontent.com/aida-public/AB6AXuC90-HsQrj5IQC5-G2jAZ8pS0lnOYFmOecOEe3wSDfDZ6JCIDgT6K8oPpaZrQYIdBe5WTY8f75KIfiJG45cfYFMmOur4erP1HOZPtWlwuxmwZcQsx1SK-gQWkXPpqA5UlPQQOFj_RP--4rDyHpKXZkW6jCCuNnrFc16fh_b_bOnLxD9eoDdTxLcVUXVwEwPFocv1O5oZsaaEeInAZRjHVk6T86Nh2X9N_y06edQ-ELUjdf_HcBPycof"
    const val AVATAR_ILYAS_ALT = "https://lh3.googleusercontent.com/aida-public/AB6AXuBJO-Iskd6GfnyIwcvqrQdhsWIE0PuGLTZ36LNP74YrHj2WdgxP2ZNLGeQ-gM7XUHGzhPwuwfGg0UmfERanKucTOUkTi5oR__PvWQivOr2oIxnald35dZ-82yyb7SAaGVsxX57ug87YoFo5_a1QLROueZWKMCXhHSsglWKwIXLRW8vO30AjOrIlmhr6-0qGlEoN5m_gp-ZhleK_XRJQL9EERxbeFpGRQHIhaYFonDg07Qq8FxvFIdpa"
    const val AVATAR_OPPONENT = "https://lh3.googleusercontent.com/aida-public/AB6AXuCoQO6q4CKo_DiiPchiIUFlkdp4EWmdneBB8QCYsMfWkMqncUqb3ouYBZZ_8_WQydx78IGX7aKriBHdoS_mABZv3kRijQt5vO_r7U_6igXZyhhb8QISfGKJVw24dsfGEDWoRj-YLC9dDdasl48PkwOWekmq_HCARJwL2PTPeW8aPvDrww3bE3GblMgzsRPqjglWPQvUU1eqlgUgOUENpWhxFGKK2tdNtWcXVwTUlv6GD-ZwTTvuiwUB"
    const val AVATAR_PLAYER_WIN = "https://lh3.googleusercontent.com/aida-public/AB6AXuDXMLVKfWdLWTD1EtygRhZ-v-dq1IV1nOWZaGMTGjylNP_L4Gpj205UyCa0Tpiinxqoq8CxIaJiY8eKVb4cS7vp_cGXoFsRzHKltRL--mUttDQHbK-lUD2VvySKjyMhEqwrBV_FCvvVmTXWmqD21ZlKSWnxG0X-6ymF-dxopNCswj3xeTWaPWJMF_m-Xi9jYclfSoV2TAfUMaP0AkV4Z7P6yCgr1rWgzKKi_bUd4VbDppV_ukKFXPgw"
    const val AVATAR_RAKIP_RESULT = "https://lh3.googleusercontent.com/aida-public/AB6AXuDERLLzuhsxsyZMydR_uEhylurLptJOI5oOrtV0gxjKD7p0ROVJnsQ3_FBhhIBga_vVeCez9LbODqudkf7DzXNdvTE2HN1ilnFMvgTjYiUYz5Ay2-iSEgxi_VRDR08ra0pYkpjHuJEORFOFpU9LJR3MHHDL2Mekxgk7t3U6oMWmEBpJr_07us-OlMMWL_Nk6TGPpNvf0h_XaQZu7_Ss_x_J1CfeFOuxIUjHgS5grqJKDUQ0gRil0Fv0"
    const val AVATAR_TARIHSEVER = "https://lh3.googleusercontent.com/aida-public/AB6AXuDfw0DGd16-HKoCeCV3xUbPnAbAVsfWfEzSuQTjHZAlfx_Q0L5iu_JhDlv3-QW8mxmSV9XxaOLAIG-cSSJC209kIap3RAnTi4I4pF5LqRzL_qdyKZyLqqqxuE2KYKeNfNwibPYEKXbY6hyg9eJFn9MIZijGSAeOGCZsS2pJh22EVWMWq9HC82qE5RUhop1d2o0R46rZgUCeX9ADAd9KKFgobF_vR2ZzbmKvCwrqrwBWuVmA9JKB4GeK"
    const val BANNER_OTTOMAN = "https://lh3.googleusercontent.com/aida-public/AB6AXuDy4V2-B1-aSDmG9zUKrm9oMIIeiY5prLrewoLPx5yT-c4cxQhKkUwJvEAYSxaYEgpjDLthIwU2JVYRtggW5aKEtNiyYm6KG3zG4IcURDBBPw8iswJHNa-DMWWkNPEAw41Q4z4aDnBnjXurm1EKUNkwH4dMD3HebqlhzQte8NGEUJfx_oYPiRvzGjWixXQXbzce8Bntbr0zIP-nVRm6hgiuksrlvKk4oPGSvyFR65mipireIXimmRy9"
    const val IMG_BRAIN_HERO = "https://lh3.googleusercontent.com/aida-public/AB6AXuDPcMnSV-PBAmhqCrhZhXx-YjjxqVf0lz6LIUEGUu26BHyl6Su8iM8OCUEUdu4VIqn8QaBxlzK04wt46PLV6Cs5djHO8UN5gcl5onSxg6FrLFxMt4B-6Dq7xQaofD46xFJvJAID08yehJVyPvcTsfdTdS05286rvZ4XzY21TqED6EjEJ36l8kFscR3sHEAzek4bdZzcxLrVWyK27CnZFZgSL5DHUeUbsamvmoTqhGWQSSFquCNCs4nA"
    const val IMG_AI_BOT = "https://lh3.googleusercontent.com/aida-public/AB6AXuAZ9yLISTV9V66t0fSpgTXGxyeOzvcLKvNU4oAbfNjru-1etJL8lRiHwj-zOKdGe7czwzLg-NZQA8xa_MRREYcQ8wLaF3VmlqTvM7YhyDMwxNcOM241Hqw44uRL10cxpOSs_XNtNyB3khj1m2fGJTWAYYFxHh_D9mj7-4REc-OD09UzpDsZyPPvP80LfsMt7UTzbSTwUl1fe5lp2sCzJLespR8iBg15x2P2H3RV9KmTnzbZC5Rx7ZQm"
    const val IMG_LOGO = "https://lh3.googleusercontent.com/aida-public/AB6AXuBOVTlkHKJgqpR2oY-ytbVIDkfNwJYDqaAwkcWB1gNmiFMunrJSjdch35ryPG7D0EbGL_SvCmaCrTs_4KW8T11eaOifNDGHTRxGISiO87TU7LlM3pzyxI3EAABQz--kTkVIhxzpn9tHzQNjwxzd912GgCfjB_FmGMlqHpr31Kg8wsJWA8GFiiRP9wUy1id1QJuyLc1MGe7c4AmkeUIyKZSuHfiA3jxUIHcssz9bGe0YIAs8_MFQl3Re"

    val categories = listOf(
        CategoryItem("tarih", "Tarih", "1.234 quiz", "castle", 0xFFFFD700, hasPremiumBadge = true, tags = listOf("Tümü", "Popüler")),
        CategoryItem("bilim", "Bilim", "987 quiz", "science", 0xFF4CAF50, tags = listOf("Tümü", "Popüler")),
        CategoryItem("cografya", "Coğrafya", "856 quiz", "public", 0xFF00F1FF, tags = listOf("Tümü")),
        CategoryItem("spor", "Spor", "1.105 quiz", "sports_soccer", 0xFFFF5252, tags = listOf("Tümü", "Popüler")),
        CategoryItem("eglence", "Eğlence", "1.532 quiz", "theater_comedy", 0xFFFF4081, tags = listOf("Tümü", "Yeni")),
        CategoryItem("teknoloji", "Teknoloji", "743 quiz", "devices", 0xFF00BCD4, tags = listOf("Tümü", "Yeni")),
        CategoryItem("sanat", "Sanat", "456 quiz", "palette", 0xFFFF9800, tags = listOf("Tümü")),
        CategoryItem("genel_kultur", "Genel Kültür", "2.345 quiz", "psychology", 0xFF9C27B0, tags = listOf("Tümü", "Popüler"))
    )

    val ottomanQuestions = listOf(
        Question(
            id = "q1",
            text = "Osmanlı Devleti'nin kurucusu kimdir?",
            options = listOf("Ertuğrul Gazi", "Osman Gazi", "Orhan Gazi", "I. Murad"),
            correctIndex = 1,
            category = "Tarih"
        ),
        Question(
            id = "q2",
            text = "İstanbul'un fethi hangi yılda gerçekleşmiştir?",
            options = listOf("1071", "1299", "1453", "1517"),
            correctIndex = 2,
            category = "Tarih"
        ),
        Question(
            id = "q3",
            text = "Osmanlı Devleti'nin ilk başkenti neresidir?",
            options = listOf("Edirne", "Bursa", "İstanbul", "Ankara"),
            correctIndex = 1,
            category = "Tarih"
        ),
        Question(
            id = "q4",
            text = "Mohaç Meydan Muharebesi hangi padişah döneminde kazanılmıştır?",
            options = listOf("Kanuni Sultan Süleyman", "Yavuz Sultan Selim", "Fatih Sultan Mehmet", "II. Selim"),
            correctIndex = 0,
            category = "Tarih"
        ),
        Question(
            id = "q5",
            text = "Osmanlı'da ilk altın para hangi padişah zamanında basılmıştır?",
            options = listOf("Osman Gazi", "Orhan Gazi", "Fatih Sultan Mehmet", "Yıldırım Bayezid"),
            correctIndex = 2,
            category = "Tarih"
        ),
        Question(
            id = "q6",
            text = "Yeniçeri Ocağı'nı hangi padişah kaldırmıştır (Vaka-i Hayriye)?",
            options = listOf("III. Selim", "II. Mahmud", "Abdülmecid", "II. Abdülhamid"),
            correctIndex = 1,
            category = "Tarih"
        ),
        Question(
            id = "q7",
            text = "Osmanlı'da denizcilik alanında 'Kaptan-ı Derya' unvanını ilk alan ünlü komutan kimdir?",
            options = listOf("Barbaros Hayreddin Paşa", "Piri Reis", "Turgut Reis", "Oruç Reis"),
            correctIndex = 0,
            category = "Tarih"
        ),
        Question(
            id = "q8",
            text = "İlk Osmanlı anayasası olan Kanun-i Esasi hangi yılda ilan edilmiştir?",
            options = listOf("1839", "1856", "1876", "1908"),
            correctIndex = 2,
            category = "Tarih"
        ),
        Question(
            id = "q9",
            text = "Osmanlı Devleti'nde divan toplantılarının yapıldığı Topkapı Sarayı bölümü hangisidir?",
            options = listOf("Harem", "Enderun", "Kubbealtı", "Bâb-ı Hümâyûn"),
            correctIndex = 2,
            category = "Tarih"
        ),
        Question(
            id = "q10",
            text = "Mimar Sinan'ın 'Ustalık Eserim' dediği cami hangisidir?",
            options = listOf("Şehzade Camii", "Süleymaniye Camii", "Selimiye Camii", "Rüstem Paşa Camii"),
            correctIndex = 2,
            category = "Tarih"
        )
    )

    val defaultOttomanQuiz = Quiz(
        id = "ottoman-1",
        title = "Osmanlı Tarihi - 1",
        category = "Tarih",
        questionCount = 10,
        difficulty = "Orta",
        playCount = "12.340",
        successRate = "%68",
        creatorName = "TarihSever",
        isCreatorVerified = true,
        creatorAvatar = AVATAR_TARIHSEVER,
        bannerUrl = BANNER_OTTOMAN,
        description = "Osmanlı Devleti'nin kuruluşundan yükseliş dönemine kadar temel bilgiler. Bu quiz ile bilginizi test edin ve tarih sahnesindeki yolculuğa çıkın.",
        questions = ottomanQuestions
    )

    val scienceQuestions = listOf(
        Question("s1", "Periyodik tabloda 'Au' simgesi hangi elementi temsil eder?", listOf("Gümüş", "Altın", "Bakır", "Alüminyum"), 1, "Bilim"),
        Question("s2", "Güneş sistemindeki en büyük gezegen hangisidir?", listOf("Mars", "Satürn", "Jüpiter", "Neptün"), 2, "Bilim"),
        Question("s3", "İnsan vücudundaki en güçlü kas hangisidir?", listOf("Kalp", "Çene Kası (Masseter)", "Uyluk Kası", "Pazı"), 1, "Bilim"),
        Question("s4", "Işık hızı saniyede yaklaşık kaç kilometredir?", listOf("150.000", "300.000", "450.000", "600.000"), 1, "Bilim"),
        Question("s5", "Yerçekimi kuvvetini ilk tanımlayan bilim insanı kimdir?", listOf("Albert Einstein", "Galileo Galilei", "Isaac Newton", "Nikola Tesla"), 2, "Bilim")
    )

    val scienceQuiz = Quiz(
        id = "science-1",
        title = "Genel Bilim ve Evren",
        category = "Bilim",
        questionCount = 5,
        difficulty = "Kolay",
        playCount = "8.920",
        successRate = "%74",
        creatorName = "BilimDurağı",
        isCreatorVerified = true,
        creatorAvatar = AVATAR_TARIHSEVER,
        bannerUrl = BANNER_OTTOMAN,
        description = "Evren, fizik kuralları ve kimyanın büyüleyici dünyasını keşfet!",
        questions = scienceQuestions
    )

    val allQuizzes = listOf(
        defaultOttomanQuiz,
        scienceQuiz,
        Quiz(
            id = "geo-1",
            title = "Dünya Başkentleri & Coğrafya",
            category = "Coğrafya",
            questionCount = 10,
            difficulty = "Orta",
            playCount = "9.410",
            successRate = "%62",
            creatorName = "AtlasGezgini",
            bannerUrl = BANNER_OTTOMAN,
            description = "Dünya üzerindeki kıtalar, başkentler ve doğal harikalar.",
            questions = listOf(
                Question("g1", "Avustralya'nın başkenti neresidir?", listOf("Sidney", "Melbourne", "Kanberra", "Brisbane"), 2, "Coğrafya"),
                Question("g2", "Dünyanın en uzun nehri hangisidir?", listOf("Amazon", "Nil", "Yangtze", "Mississippi"), 1, "Coğrafya")
            )
        )
    )

    val achievements = listOf(
        AchievementItem("a1", "İlk Zafer", "İlk 1v1 maçını kazan", 1, 1, true),
        AchievementItem("a2", "Bilgi Savaşçısı", "100 soruya doğru cevap ver", 100, 100, true),
        AchievementItem("a3", "Tarih Ustası", "Tarih kategorisinde 25 quiz tamamla", 18, 25, false),
        AchievementItem("a4", "Hızlı Parmaklar", "Bir soruyu 2 saniyenin altında cevapla", 1, 1, true),
        AchievementItem("a5", "Kusursuz Seri", "Arka arkaya 5 maç kazan", 3, 5, false)
    )

    val friends = listOf(
        FriendItem("f1", "Ahmet Yılmaz", 14, true, AVATAR_OPPONENT),
        FriendItem("f2", "Zeynep Kaya", 11, true, AVATAR_TARIHSEVER),
        FriendItem("f3", "Caner Demir", 9, false, AVATAR_PLAYER_WIN),
        FriendItem("f4", "Elif Şahin", 16, true, AVATAR_RAKIP_RESULT)
    )

    fun generateAiQuiz(topic: String, count: Int, difficulty: String): Quiz {
        val generatedQuestions = mutableListOf<Question>()
        val cleanTopic = if (topic.isBlank()) "Genel Kültür" else topic.trim()

        val templates = listOf(
            Triple("$cleanTopic konusunda ilk önemli gelişme ne zaman yaşanmıştır?", listOf("16. Yüzyıl", "17. Yüzyıl", "18. Yüzyıl", "19. Yüzyıl"), 1),
            Triple("$cleanTopic ile ilişkili temel kavram veya en belirgin etmen hangisidir?", listOf("Merkezi Otorite", "Ticaret Yolları", "Teknolojik Yenilik", "Kültürel Etkileşim"), 0),
            Triple("$cleanTopic sürecini en çok etkileyen ana olay hangisidir?", listOf("Zitvatorok Antlaşması", "Karlofça Antlaşması", "Pasarofça Antlaşması", "Küçük Kaynarca"), 1),
            Triple("$cleanTopic dönemi boyunca yapılan en kapsamlı ıslahat hangi alandadır?", listOf("Maliye ve Askeri", "Eğitim ve Hukuk", "Sanat ve Mimari", "Dış İlişkiler"), 0),
            Triple("$cleanTopic alanında kaleme alınan en önemli risale kime aittir?", listOf("Koçi Bey", "Kâtip Çelebi", "Evliya Çelebi", "Naima"), 0),
            Triple("$cleanTopic için dönüm noktası kabul edilen tarihi pakt veya gelişme nedir?", listOf("Ferah Senedi", "Nasuha Paşa Antlaşması", "Bucaş Antlaşması", "Hotin Seferi"), 2),
            Triple("$cleanTopic kapsamında uygulanan ekonomik tedbir hangisidir?", listOf("Tımar Sistemi Revizyonu", "İltizam Sistemi Yaygınlaşması", "Cizye Vergisi", "Avarız Düzenlemesi"), 1),
            Triple("$cleanTopic bağlamında bilinen en güçlü savunma hattı nerededir?", listOf("Uyvar Kalesi", "Kandiye Kalesi", "Bağdat", "Estergon"), 0),
            Triple("$cleanTopic döneminin ünlü sadrazamlar ailesi hangisidir?", listOf("Köprülüler", "Sokollular", "Çandarlılar", "Damad İbrahim Paşalar"), 0),
            Triple("$cleanTopic sürecinin nihai sonucu nedir?", listOf("Yeniliklerin Hızlanması", "Toprak Kayıplarının Başlaması", "Sınırların Genişlemesi", "Avrupa'ya Entegrasyon"), 1)
        )

        for (i in 0 until count) {
            val template = templates[i % templates.size]
            generatedQuestions.add(
                Question(
                    id = "ai_q_${i + 1}",
                    text = "${i + 1}. ${template.first}",
                    options = template.second,
                    correctIndex = template.third,
                    category = "AI Quiz"
                )
            )
        }

        return Quiz(
            id = "ai-${System.currentTimeMillis()}",
            title = cleanTopic,
            category = "AI Özel",
            questionCount = count,
            difficulty = difficulty,
            playCount = "1",
            successRate = "%100",
            creatorName = "Trivia AI",
            isCreatorVerified = true,
            creatorAvatar = IMG_AI_BOT,
            bannerUrl = BANNER_OTTOMAN,
            description = "$cleanTopic konusunda yapay zeka tarafından $difficulty zorluk seviyesinde $count soru ile özel olarak hazırlandı.",
            questions = generatedQuestions
        )
    }
}
