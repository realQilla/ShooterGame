package net.qilla.zombieshooter.BlockSystem.CustomBlock;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum CustomBlock {
    BRITTLE_STONE(1, Material.ANDESITE,
            new NodeBlock(Material.BEDROCK,
                    10, 1),
            null,
            Sound.BLOCK_STONE_BREAK,
            0,
            0),
    DESTRUCTIBLE_PLANK(2,
            Material.OAK_PLANKS,
            new NodeBlock(Material.BEDROCK,
                    10, 1),
            List.of(new ItemDrops(Material.STICK,
                    2, 4, 1)),
            Sound.BLOCK_WOOD_BREAK,
            5,
            0),
    HARDENED_PLANK(3,
            Material.SPRUCE_PLANKS,
            new NodeBlock(Material.BEDROCK,
                    10, 1),
            List.of(new ItemDrops(Material.STICK,
                    2, 4, 1)),
            Sound.BLOCK_WOOD_BREAK,
            40, 1),
    CRACKED_STONE(4,
            Material.COBBLESTONE,
            new NodeBlock(Material.BEDROCK,
                    10, 1),
            List.of(new ItemDrops(Material.AIR,
                    1, 3, 1)),
            Sound.BLOCK_STONE_BREAK,
            120,
            3),
    BEDROCK(5,
            Material.BEDROCK,
            new NodeBlock(Material.BEDROCK,
                    10, 1),
            null,
            Sound.BLOCK_STONE_BREAK,
            null,
            0),
    COBBLESTONE_NODE(6, Material.COBBLESTONE,
            new NodeBlock(Material.BEDROCK,
                    10, 3),
            List.of(new ItemDrops(Material.COBBLESTONE,
                    1, 1, 1)),
            Sound.BLOCK_STONE_BREAK,
            10,
            0);

    private final short id;
    private final Material material;
    private final NodeBlock nodeBlock;
    private final List<ItemDrops> itemDrops;
    private final Sound sound;
    private final Integer breakTime;
    private final byte hardness;

    CustomBlock(int id, @NotNull Material material, @NotNull NodeBlock nodeBlock, @Nullable List<ItemDrops> itemDrops, @NotNull Sound sound, @Nullable Integer breakTime, int hardness) {
        if (id < 0 || id > Short.MAX_VALUE)
            throw new IllegalArgumentException("ID must be between 0 and " + Short.MAX_VALUE);
        if (breakTime != null && breakTime < 0) throw new IllegalArgumentException("Break time cannot be less than 0");
        if (hardness < 0 || hardness > Byte.MAX_VALUE)
            throw new IllegalArgumentException("ID must be between 0 and " + Byte.MAX_VALUE);
        this.id = (short) id;
        this.material = material;
        this.nodeBlock = nodeBlock;
        this.sound = sound;
        this.itemDrops = itemDrops;
        this.breakTime = breakTime;
        this.hardness = (byte) hardness;
    }

    public short id() {
        return id;
    }

    @NotNull
    public Material material() {
        return material;
    }

    public NodeBlock getNode() {
        return nodeBlock;
    }

    @Nullable
    public List<ItemDrops> itemDrops() {
        return itemDrops;
    }

    @NotNull
    public Sound sound() {
        return sound;
    }

    @Nullable
    public Integer breakTime() {
        return breakTime;
    }

    public int hardness() {
        return hardness;
    }

    @Nullable
    public record ItemDrops(@NotNull Material item, int amountMin, int amountMax, double chance) {

        public ItemDrops {
            if (chance < 0 || chance > 1) throw new IllegalArgumentException("Chance must be between 0 and 1");
            if (amountMin < 0 || amountMax < 0) throw new IllegalArgumentException("Amounts must be positive");
            if (amountMin > amountMax)
                throw new IllegalArgumentException("Min amount must be less than or equal to max amount");
        }
    }

    @Nullable
    public record NodeBlock(Material fillMaterial, int respawnTime, int withinNode) {

        public NodeBlock {
            if (respawnTime < 0) throw new IllegalArgumentException("Respawn time cannot be less than 0");
            if (withinNode < 0) throw new IllegalArgumentException("Count cannot be less than 0");
        }
    }
}