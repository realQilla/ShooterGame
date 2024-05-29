package net.qilla.shootergame.gunsystem.guncreation.gunmod;

public record FireMod(String modeName, int fireCooldown, int perBulletCooldown, int bulletAmount, float bulletDamage, float bulletKnockback, float bulletSpread, int bulletRange) {

    /**
     * @param fireCooldown GunCooldown between bullets/bullet group
     * @param perBulletCooldown GunCooldown between bullets in a group/burst, not applicable if single fireSound
     * @param bulletAmount Number of bullets in a group/burst
     * @param bulletDamage Damage per bullet hit on target
     * @param bulletKnockback the amount a target is knocked back when hit
     * @param bulletSpread Spread of each bullet fired
     * @param bulletRange Total range that a bullet can travel
     */

    public FireMod(String modeName, int fireCooldown, int perBulletCooldown, int bulletAmount, float bulletDamage, float bulletKnockback, float bulletSpread, int bulletRange) {
        this.modeName = modeName;
        this.fireCooldown = fireCooldown;
        this.perBulletCooldown = perBulletCooldown;
        this.bulletAmount = bulletAmount;
        this.bulletDamage = bulletDamage;
        this.bulletKnockback = bulletKnockback;
        this.bulletSpread = bulletSpread;
        this.bulletRange = bulletRange;
    }
}
