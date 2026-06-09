# Better Combat Integration

Pyke Assassin modunun Better Combat moduna uyumluluğu.

## Entegre Özellikler

### 1. Combo Sistemi
- **Hook → Attack → Execute**: Üçlü combo
- **Hook → Invisibility → Execute**: Gizli combo
- **Combo Göstergesi**: "█ █ █" ekranda gösterilir
- **Combo Timer**: 6 saniye sonra reset
- **Bonus Hasar**: Her combo seviyesinde +5% hasar

### 2. Stance Integration

#### Aggressive Stance
- Execute Ultimate cooldown -50 tick
- +15% hasar artışı
- Execute'a Hazırlanma modu

#### Defensive Stance
- Invisibility cooldown -50 tick
- +20% defense
- Parry başarısı → Invisibility

#### Balanced Stance
- Hook cooldown -50 tick
- Tüm yetenekler %10 daha hızlı

### 3. Dual-Wield Desteği
- İki kılıç kullanıldığında +25% hasar
- Combo sayısı %20 hızlı artış
- Hook range +5 blok

### 4. Riposte Integration
- **Riposte Başarısı → Execute Combo**
- Execute cooldown anında reset
- Bonus particle efektleri
- Riposte'den sonra Execute'un garantisi

### 5. Parry System
- **Başarılı Parry**: Invisibility tetikleme şansı
- Parry'den sonra gizlilik bonusu
- Counter attack ready

## Kontrol Sistemi

| Aksiyon | Kontrolü |
|---------|----------|
| Combo | Hook + Attack + Execute |
| Stance Change | Better Combat menüsü |
| Riposte Execute | Parry + Execute |
| Dual Wield Combo | 2 Kılıç tut |

## Combo Örnekleri

### Standart Attack Combo
```
Hook (Pull) → Attack (Damage) → Execute (Finish)
█ Combo 1/5 → █ █ Combo 2/5 → █ █ █ Execute Ready!
```

### Stealth Combo
```
Invisibility → Hook (Hidden) → Execute
█ Combo 1/5 → █ █ Combo 2/5 → █ █ █ Execute Ready!
```

### Aggressive Combo
```
Stance: Aggressive
Attack → Attack → Hook → Execute
Bonus: +15% Damage, Faster Execute
```

## Ayarlar

### Combo Settings
```java
COMBO_TIMEOUT = 120; // 6 saniye
MAX_COMBO_COUNT = 5; // Maksimum combo
COMBO_BONUS_DAMAGE = 0.05f; // %5 per level
DUAL_WIELD_BONUS = 1.25f; // %25 bonus
```

### Stance Cooldown Reductions
```java
STANCE_COOLDOWN_REDUCTION = 50; // ticks
AGGRESSIVE_DAMAGE_BOOST = 0.15f; // %15
DEFENSIVE_DEFENSE_BOOST = 0.20f; // %20
BALANCED_SPEED_BOOST = 0.10f; // %10
```

## Bağımlılıklar

- Fabric API
- Better Combat Mod
- Minecraft 1.20.1+

## Kurulum

1. Better Combat modunu indirip yükle
2. Pyke Assassin modunu yükle
3. Better Combat integrasyonu otomatik aktive olacak
4. Game'i başlat ve combo'ları kulla!

---

**Not**: Better Combat modunun en son versiyonu ile uyumludur.
