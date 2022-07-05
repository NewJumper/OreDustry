package com.newjumper.oredustry.world;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class OrePlacedFeatures {
    public static final Holder<PlacedFeature> DENSE_COAL = PlacementUtils.register("dense_coal", OreConfiguredFeatures.DENSE_COAL_ORE,
            commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(64), VerticalAnchor.aboveBottom(256))));
    public static final Holder<PlacedFeature> DENSE_COAL_BURIED = PlacementUtils.register("dense_coal_buried", OreConfiguredFeatures.DENSE_COAL_ORE_BURIED,
            commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(160))));

    public static final Holder<PlacedFeature> DENSE_IRON_SMALL = PlacementUtils.register("dense_iron_small", OreConfiguredFeatures.DENSE_IRON_ORE_SMALL,
            commonOrePlacement(6, HeightRangePlacement.triangle(VerticalAnchor.absolute(176), VerticalAnchor.absolute(336))));
    public static final Holder<PlacedFeature> DENSE_IRON_LARGE = PlacementUtils.register("dense_iron_large", OreConfiguredFeatures.DENSE_IRON_ORE_LARGE,
            commonOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-112), VerticalAnchor.aboveBottom(112))));

    public static final Holder<PlacedFeature> DENSE_COPPER = PlacementUtils.register("dense_copper", OreConfiguredFeatures.DENSE_COPPER_ORE,
            commonOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(16), VerticalAnchor.aboveBottom(144))));

    public static final Holder<PlacedFeature> DENSE_GOLD = PlacementUtils.register("dense_gold", OreConfiguredFeatures.DENSE_GOLD_ORE,
            commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(64), VerticalAnchor.aboveBottom(128))));
    public static final Holder<PlacedFeature> DENSE_GOLD_BURIED = PlacementUtils.register("dense_gold_buried", OreConfiguredFeatures.DENSE_GOLD_ORE_BURIED,
            rareOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-96), VerticalAnchor.aboveBottom(96))));

    public static final Holder<PlacedFeature> DENSE_REDSTONE_UPPER = PlacementUtils.register("dense_redstone_upper", OreConfiguredFeatures.DENSE_REDSTONE_ORE_UPPER,
            rareOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
    public static final Holder<PlacedFeature> DENSE_REDSTONE_LOWER = PlacementUtils.register("dense_redstone_lower", OreConfiguredFeatures.DENSE_REDSTONE_ORE_LOWER,
            commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-32), VerticalAnchor.aboveBottom(32))));

    public static final Holder<PlacedFeature> DENSE_EMERALD = PlacementUtils.register("dense_emerald", OreConfiguredFeatures.DENSE_EMERALD_ORE,
            rareOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.top())));

    public static final Holder<PlacedFeature> DENSE_LAPIS_UPPER = PlacementUtils.register("dense_lapis_upper", OreConfiguredFeatures.DENSE_LAPIS_ORE_UPPER,
            rareOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-64), VerticalAnchor.aboveBottom(128))));
    public static final Holder<PlacedFeature> DENSE_LAPIS_LOWER = PlacementUtils.register("dense_lapis_lower", OreConfiguredFeatures.DENSE_LAPIS_ORE_LOWER,
            commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-64), VerticalAnchor.aboveBottom(64))));

    public static final Holder<PlacedFeature> DENSE_DIAMOND_SMALL = PlacementUtils.register("dense_diamond_small", OreConfiguredFeatures.DENSE_DIAMOND_ORE_SMALL,
            rareOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-90), VerticalAnchor.aboveBottom(90))));
    public static final Holder<PlacedFeature> DENSE_DIAMOND_LARGE = PlacementUtils.register("dense_diamond_large", OreConfiguredFeatures.DENSE_DIAMOND_ORE_LARGE,
            rareOrePlacement(2, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));

    public static List<PlacementModifier> orePlacement(PlacementModifier pModifier, PlacementModifier pHeightRange) {
        return List.of(pModifier, InSquarePlacement.spread(), pHeightRange, BiomeFilter.biome());
    }
    public static List<PlacementModifier> commonOrePlacement(int pAttempts, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacement.of(pAttempts), pHeightRange);
    }
    public static List<PlacementModifier> rareOrePlacement(int pAttempts, PlacementModifier pHeightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(pAttempts), pHeightRange);
    }
}
