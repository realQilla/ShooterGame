package net.qilla.shootergame.blocksystem.customblock;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public enum CustomBlock {
    BRITTLE_STONE(1,
            Material.ANDESITE,
            Material.AIR,
            null,
            null,
            30,
            Sound.BLOCK_STONE_BREAK,
            0,
            0,
            null),
    ROTTEN_WOOD(2,
            Material.JUNGLE_PLANKS,
            Material.AIR,
            null,
            List.of(new ItemDrops(Material.STICK, 2, 4, 1)),
            1,
            Sound.BLOCK_WOOD_BREAK,
            5,
            0,
            null),
    WOODEN_PLANKS(3,
            Material.SPRUCE_PLANKS,
            Material.AIR,
            null,
            List.of(new ItemDrops(Material.STICK, 2, 4, 1)),
            0,
            Sound.BLOCK_WOOD_BREAK,
            1,
            1,
            List.of(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE)),
    CRACKED_STONE(4,
            Material.COBBLESTONE,
            Material.AIR,
            null,
            null,
            80,
            Sound.BLOCK_STONE_BREAK,
            1,
            3,
            List.of(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE)),
    COBBLESTONE_NODE(5,
            Material.COBBLESTONE,
            Material.BEDROCK,
            new NodeBlock(12),
            List.of(new ItemDrops(Material.COBBLESTONE, 1, 1, 1)),
            40,
            Sound.BLOCK_STONE_BREAK,
            10,
            0,
            List.of(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE)),
    COAL_NODE(6,
            Material.COAL_ORE,
            Material.BEDROCK,
            new NodeBlock(12),
            List.of(new ItemDrops(Material.COAL, 1, 1, 0.75)),
            10,
            Sound.BLOCK_STONE_BREAK,
            20,
            0,
    List.of(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE));

    private final short id;
    private final Material material;
    private final Material fillMaterial;
    private final NodeBlock nodeBlock;
    private final List<ItemDrops> itemDrops;
    private final int respawnSec;
    private final Sound sound;
    private final Integer breakTime;
    private final byte hardness;
    private final List<Material> correctTool;

    CustomBlock(int id, @NotNull Material material, Material fillMaterial, @Nullable NodeBlock nodeBlock, @Nullable List<ItemDrops> itemDrops, int respawnSec, @NotNull Sound sound, @Nullable Integer breakTime, int hardness, List<Material> correctTool) {
        if (id < 0 || id > Short.MAX_VALUE)
            throw new IllegalArgumentException("ID must be between 0 and " + Short.MAX_VALUE);
        if (breakTime != null && breakTime < 0) throw new IllegalArgumentException("Break time cannot be less than 0");
        if (hardness < 0 || hardness > Byte.MAX_VALUE)
            throw new IllegalArgumentException("ID must be between 0 and " + Byte.MAX_VALUE);
        if (respawnSec < 0) throw new IllegalArgumentException("Respawn time cannot be less than 0 seconds");
        this.id = (short) id;
        this.material = material;
        this.fillMaterial = fillMaterial;
        this.nodeBlock = nodeBlock;
        this.sound = sound;
        this.itemDrops = itemDrops;
        this.respawnSec = respawnSec;
        this.breakTime = breakTime;
        this.hardness = (byte) hardness;
        this.correctTool = correctTool;
    }

    public short id() {
        return id;
    }

    @NotNull
    public Material material() {
        return material;
    }

    @NotNull
    public Material fillMaterial() {
        return fillMaterial;
    }

    public NodeBlock getNode() {
        return nodeBlock;
    }

    @Nullable
    public List<ItemDrops> itemDrops() {
        return itemDrops;
    }

    public int respawnSec() {
        return respawnSec;
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
    public List<Material> correctTool() {
        return correctTool;
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
    public record NodeBlock(int withinNode) {

        public NodeBlock {
            if (withinNode < 0) throw new IllegalArgumentException("Count cannot be less than 0");
        }
    }
}