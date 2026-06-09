# Pyke Assassin Mod

League of Legends Pyke karakterini Minecraft Fabric modunda uygulayan bir assassin sınıfı modu.

## Özellikler

### 🪝 Hook Ability
- **Kontrolü**: SHIFT + Boş elle varlığa sağ tıkla
- **Etki**: Düşmanı çeker ve hasar verir
- **Cooldown**: 6 saniye
- **Menzil**: 25 blok

### ⚔ Execute Ultimate
- **Kontrolü**: Düşmana saldır (otomatik kontrol)
- **Koşul**: Düşman maksimum canının %25'inin altında olmalı
- **Hasar**: 25 hasar (instant)
- **Cooldown**: 10 saniye
- **Menzil**: 20 blok
- **Efekt**: Yeşil spectral parçacıklar ve kuvvetli ses

### 👻 Invisibility (Gizlilik)
- **Kontrolü**: SHIFT + Ender Pearl tut
- **Etki**: 10 saniye için görünmez ol ve hız kasası al
- **Cooldown**: 15 saniye
- **Süre**: 10 saniye

### ✨ Green Spectral Particles
- Tüm yeteneklerde yeşil spectral (ruh) parçacıkları
- Gizlilik sırasında dönen yeşil parçacık efekti
- Hook ve Execute ultimate'de patlama efekti

## Kurulum

1. Fabric Loader'ı yükle (1.20.1)
2. Fabric API modunu yükle
3. `pyke-assasin-mod.jar` dosyasını `mods` klasörüne koy
4. Minecraft'ı başlat

## Kontroller

| Yetenek | Kontrol |
|---------|----------|
| Hook | SHIFT + Sağ T��kla (Boş Elle) |
| Execute Ultimate | Vurma (otomatik, düşük HP'de) |
| Invisibility | SHIFT + Ender Pearl Tut |

## Ayarlar

### Hook Ability
```java
HOOK_COOLDOWN = 120; // 6 saniye
PULL_STRENGTH = 0.8; // Çekme kuvveti
HOOK_RANGE = 25.0; // Menzil
```

### Execute Ultimate
```java
EXECUTE_COOLDOWN = 200; // 10 saniye
EXECUTE_DAMAGE = 25.0f; // Hasar
EXECUTE_THRESHOLD = 0.25f; // %25 HP altında execute
EXECUTE_RANGE = 20.0; // Menzil
```

### Invisibility
```java
INVISIBILITY_COOLDOWN = 300; // 15 saniye
INVISIBILITY_DURATION = 200; // 10 saniye
```

## Teknik Detaylar

- **Minecraft Version**: 1.20.1
- **Loader**: Fabric
- **Java Version**: 17+
- **Bağımlılıklar**: Fabric API

## Lisans

MIT License

## Geliştirici

Pyke Developer Team

---

**Not**: Bu mod League of Legends'dan ilham alınmıştır ve fan yapımıdır.