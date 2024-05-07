//package net.qilla.zombieshooter.WeaponSystem;
//
//import net.qilla.zombieshooter.ZombieShooter;
//import org.bukkit.Material;
//import org.bukkit.NamespacedKey;
//import org.bukkit.Particle;
//import org.bukkit.Sound;
//import org.bukkit.persistence.PersistentDataContainer;
//
//import java.util.List;
//
//public enum GenericGunListOLD {
//
//    STANDARD_PISTOL_T1(Material.WOODEN_HOE,
//            new FireMode[]{FireMode.SINGLE},
//            new SoundModel(1.0f, 1.25f, Sound.ENTITY_IRON_GOLEM_REPAIR),
//            Particle.CRIT,
//            3.33f,
//            128,
//            8,
//            6,
//            24,
//            new NamespacedKey(ZombieShooter.getInstance(),"standard_pistol_t1"),
//            "<!italic><yellow>Standard Pistol</yellow>",
//            List.of("<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>2.8/s</white>",
//                    "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>3.3 ♥</white>",
//                    "<!italic><gold>\uD83C\uDFF9</gold> <gray>Range:</gray> <white>24/b</white>",
//                    "",
//                    "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK F</bold></yellow> <gray>to reload your weapon</gray>",
//                    "",
//                    "<!italic><gray>Standard weapon with below average stats</gray>",
//                    "",
//                    "<!italic><white><bold>STANDARD WEAPON</white>")),
//
//    STANDARD_PISTOL_T2(Material.WOODEN_HOE,
//            new FireMode[]{FireMode.SINGLE},
//            new SoundModel(1.0f, 1.35f, Sound.ENTITY_IRON_GOLEM_REPAIR),
//            Particle.CRIT,
//            4.5f,
//            192,
//            12,
//            6,
//            32,
//            new NamespacedKey(ZombieShooter.getInstance(),"standard_pistol_t2"),
//            "<!italic><blue>Standard Pistol</blue>",
//            List.of("<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>3.3/s</white>",
//                    "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>4.5 ♥</white>",
//                    "<!italic><gold>\uD83C\uDFF9</gold> <gray>Range:</gray> <white>32/b</white>",
//                    "",
//                    "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK F</bold></yellow> <gray>to reload your weapon</gray>",
//                    "",
//                    "<!italic><gray>Upgraded standard weapon with an overall better setup</gray>",
//                    "",
//                    "<!italic><blue><bold>UPGRADED WEAPON</bold></blue>")),
//
//    STANDARD_PISTOL_T3(Material.WOODEN_HOE,
//            new FireMode[]{FireMode.BURST},
//            new SoundModel(1.0f, 1.75f, Sound.ENTITY_IRON_GOLEM_REPAIR),
//            Particle.CRIT,
//            4f,
//            256,
//            18,
//            3,
//            32,
//            new NamespacedKey(ZombieShooter.getInstance(),"standard_pistol_t3"),
//            "<!italic><dark_purple>Standard Pistol</dark_purple>",
//            List.of("<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>4.0/s</white>",
//                    "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>4.0 ♥</white>",
//                    "<!italic><gold>\uD83C\uDFF9</gold> <gray>Range:</gray> <white>32/b</white>",
//                    "",
//                    "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK F</bold></yellow> <gray>to reload your weapon</gray>",
//                    "","<!italic><gray>Revised standard pistol with dual burst allowing you</gray>",
//                    "<!italic><gray>to blast an enemy with twice the power</gray>",
//                    "",
//                    "<!italic><dark_purple><bold>REVISED WEAPON</bold></dark_purple>")),
//
//    ASSAULT_RIFLE_BURST(Material.IRON_HOE,
//            new FireMode[]{FireMode.SINGLE, FireMode.BURST},
//            new SoundModel(1f, 1.5f, Sound.ENTITY_FIREWORK_ROCKET_BLAST),
//            Particle.CRIT,
//            3f,
//            160,
//            32,
//            3,
//            64,
//            new NamespacedKey(ZombieShooter.getInstance(),"assault_rifle_burst"),
//            "<!italic><blue>Assault Rifle</blue>",
//            List.of("<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>8.0/s</white>",
//                    "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>3.0 ♥</white>",
//                    "<!italic><gold>\uD83C\uDFF9</gold> <gray>Range:</gray> <white>64/b</white>",
//                    "",
//                    "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK F</bold></yellow> <gray>to reload your weapon</gray>",
//                    "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK Q</bold></yellow> <gray>to toggle the fireSound mode</gray>",
//                    "",
//                    "<!italic><gray>Standard rifle with the power to show,</gray>",
//                    "<!italic><gray>can be toggled from single to burst fireSound.</gray>",
//                    "",
//                    "<!italic><blue><bold>STANDARD WEAPON</bold></blue>")),
//
//    ASSAULT_RIFLE_AUTO(Material.IRON_HOE,
//            new FireMode[]{FireMode.SINGLE, FireMode.FULL_AUTO},
//            new SoundModel(1f, 1.5f, Sound.ENTITY_FIREWORK_ROCKET_BLAST),
//            Particle.CRIT,
//            2.8f,
//            192,
//            40,
//            3,
//            64,
//            new NamespacedKey(ZombieShooter.getInstance(),"assault_rifle_auto"),
//            "<!italic><blue>Assault Rifle</blue>",
//            List.of("<!italic><dark_aqua>⏳</dark_aqua> <gray>Fire rate:</gray> <white>10/s</white>",
//                    "<!italic><red>\uD83D\uDDE1</red> <gray>Damage:</gray> <white>2.8 ♥</white>",
//                    "<!italic><gold>\uD83C\uDFF9</gold> <gray>Range:</gray> <white>64/b</white>",
//                    "",
//                    "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK F</bold></yellow> <gray>to reload your weapon</gray>",
//                    "<!italic><dark_gray>»</dark_gray> <yellow><bold>CLICK Q</bold></yellow> <gray>to toggle the fireSound mode</gray>",
//                    "","<!italic><gray>Standard rifle to overwhelm any enemy</gray>",
//                    "<!italic><gray>with a barrage of bullets, can be toggled</gray>",
//                    "<!italic><gray>from single to full auto fireSound.</gray>",
//                    "",
//                    "<!italic><blue><bold>STANDARD WEAPON</bold></blue>"));
//
//    public static GenericGunList fromDataContainer(PersistentDataContainer dataContainer) {
//        for (GenericGunList pistol : values()) {
//            if (dataContainer.has(pistol.getDataKey())) {
//                return pistol;
//            }
//        }
//        return null;
//    }
//
//    private final Material gunMaterial;
//    private final FireMode[] fireMode;
//    private final SoundModel fireSound;
//    private final Particle fireParticle;
//    private final float gunDamage;
//    private final int ammoCapacity;
//    private final int gunMagazine;
//    private final int reloadSpeed;
//    private final int gunRange;
//    private final NamespacedKey DataKey;
//    private final String gunName;
//    private final List<String> gunLore;
//
//    GenericGunList(Material gunMaterial, FireMode[] fireMode, SoundModel fireSound, Particle fireParticle, float gunDamage, int ammoCapacity, int gunMagazine, int reloadSpeed, int gunRange, NamespacedKey dataKey, String gunName, List<String> gunLore) {
//        this.gunMaterial = gunMaterial;
//        this.fireMode = fireMode;
//        this.fireSound = fireSound;
//        this.fireParticle = fireParticle;
//        this.gunDamage = gunDamage;
//        this.ammoCapacity = ammoCapacity;
//        this.gunMagazine = gunMagazine;
//        this.reloadSpeed = reloadSpeed;
//        this.gunRange = gunRange;
//        this.DataKey = dataKey;
//        this.gunName = gunName;
//        this.gunLore = gunLore;
//    }
//
//    public Material getGunMaterial() {
//        return gunMaterial;
//    }
//
//    public FireMode[] getFireMode() {
//        return fireMode;
//    }
//
//    public SoundModel getFireSound() {
//        return fireSound;
//    }
//
//    public Particle getFireParticle() {
//        return fireParticle;
//    }
//
//    public float getGunDamage() {
//        return gunDamage;
//    }
//
//    public int getAmmoCapacity() {
//        return ammoCapacity;
//    }
//
//    public int getGunMagazine() {
//        return gunMagazine;
//    }
//
//    public int getReloadSpeed() {
//        return reloadSpeed;
//    }
//
//    public int getGunRange() {
//        return gunRange;
//    }
//
//    public NamespacedKey getDataKey() {
//        return DataKey;
//    }
//
//    public String getGunName() {
//        return gunName;
//    }
//
//    public List<String> getGunLore() {
//        return gunLore;
//    }
//
//    public enum FireMode {
//
//        FULL_AUTO(2, 2, 0),
//        BURST(2, 3, 10),
//        SINGLE(0, 1, 5);
//
//        int fireSpeed;
//        int bulletCount;
//        int fireCooldown;
//
//        FireMode(int fireSpeed, int bulletCount, int fireCooldown) {
//            this.fireSpeed = fireSpeed;
//            this.bulletCount = bulletCount;
//            this.fireCooldown = fireCooldown;
//
//        }
//
//        public int getFireSpeed() {
//            return fireSpeed;
//        }
//
//        public int getBulletCount() {
//            return bulletCount;
//        }
//
//        public int getFireCooldown() {
//            return fireCooldown;
//        }
//    }
//}
//
//
