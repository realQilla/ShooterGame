package net.qilla.shootergame.gunsystem.guncreation.gunmod;

public record GunID(String gunType, String tier) {

    public enum Tier {
        I("1"),
        II("2"),
        III("3"),
        IV("4"),
        V("5");

        private final String tier;

        Tier(String tier) {
            this.tier = tier;
        }

        public String getTier() {
            return tier;
        }
    }
}