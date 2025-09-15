package com.BreadRes.desertstormwarming.logic;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ArmorItem;

public class EnhancedArmorProtection {

    public static ArmorProtectionResult calculateArmorProtection(Player player) {
        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chestplate = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leggings = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

        float totalProtection = 0.0f;
        boolean hasFullSet = !helmet.isEmpty() && !chestplate.isEmpty() && !leggings.isEmpty() && !boots.isEmpty();
        boolean hasWindResistance = false;
        boolean hasFullEnvironmentalSuit = false;
        boolean hasChargedAirTank = false;

        totalProtection += getArmorPieceProtection(helmet, EquipmentSlot.HEAD);
        totalProtection += getArmorPieceProtection(chestplate, EquipmentSlot.CHEST);
        totalProtection += getArmorPieceProtection(leggings, EquipmentSlot.LEGS);
        totalProtection += getArmorPieceProtection(boots, EquipmentSlot.FEET);

        if (isNetheriteBoots(boots)) {
            hasWindResistance = true;
        }

        if (hasFullSet && isFullEnvironmentalSuit(helmet, chestplate, leggings, boots)) {
            hasFullEnvironmentalSuit = true;
            hasChargedAirTank = hasAirTank(chestplate) && isFullyCharged(chestplate);

            if (hasChargedAirTank) {
                totalProtection = 1.0f;
            } else {
                totalProtection = Math.max(totalProtection, 0.95f);
            }
        }

        if (hasFullSet && areAllSameMaterial(helmet, chestplate, leggings, boots)) {
            totalProtection += 0.15f;
        }

        totalProtection = Math.min(1.0f, totalProtection);

        return new ArmorProtectionResult(totalProtection, hasWindResistance, hasFullEnvironmentalSuit,
                hasFullSet, hasChargedAirTank);
    }

    private static float getArmorPieceProtection(ItemStack stack, EquipmentSlot slot) {
        if (stack.isEmpty()) return 0.0f;

        if (!(stack.getItem() instanceof ArmorItem armorItem)) return 0.0f;

        float baseProtection = switch (slot) {
            case HEAD -> 0.15f;
            case CHEST -> 0.35f;
            case LEGS -> 0.25f;
            case FEET -> 0.25f;
            default -> 0.0f;
        };

        float materialMultiplier = 0.7f;

        try {
            String materialName = armorItem.getMaterial().toString().toLowerCase();
            materialMultiplier = switch (materialName) {
                case "leather" -> 0.3f;
                case "chainmail", "chain" -> 0.5f;
                case "iron" -> 0.7f;
                case "gold", "golden" -> 0.4f;
                case "diamond" -> 0.9f;
                case "netherite" -> 1.0f;
                case "turtle" -> 0.6f;
                default -> 0.7f;
            };
        } catch (Exception e) {
            String itemName = stack.getItem().toString().toLowerCase();
            if (itemName.contains("leather")) materialMultiplier = 0.3f;
            else if (itemName.contains("chain")) materialMultiplier = 0.5f;
            else if (itemName.contains("iron")) materialMultiplier = 0.7f;
            else if (itemName.contains("gold")) materialMultiplier = 0.4f;
            else if (itemName.contains("diamond")) materialMultiplier = 0.9f;
            else if (itemName.contains("netherite")) materialMultiplier = 1.0f;
            else if (itemName.contains("turtle")) materialMultiplier = 0.6f;
        }

        if (isCreateArmorPiece(stack)) {
            materialMultiplier = 1.2f;
        }

        float durabilityMultiplier = (float) (stack.getMaxDamage() - stack.getDamageValue()) / stack.getMaxDamage();
        durabilityMultiplier = Math.max(0.1f, durabilityMultiplier);

        return baseProtection * materialMultiplier * durabilityMultiplier;
    }

    public static boolean isCreateArmorPiece(ItemStack stack) {
        if (stack.isEmpty()) return false;
        String itemId = stack.getItem().toString().toLowerCase();
        String registryName = "";

        try {
            registryName = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().toLowerCase();
        } catch (Exception e) {
            registryName = itemId;
        }

        return registryName.contains("create") && registryName.contains("diving") ||
                itemId.contains("copper_diving") || itemId.contains("netherite_diving") ||
                registryName.contains("copperbacktank") || registryName.contains("netheritebacktank");
    }

    public static boolean hasAirTank(ItemStack chestplate) {
        if (!isCreateArmorPiece(chestplate)) return false;
        return chestplate.hasTag() && chestplate.getTag().contains("Air");
    }

    public static int getAirLevel(ItemStack chestplate) {
        if (!hasAirTank(chestplate)) return 0;

        if (chestplate.getTag() == null) return 0;

        return chestplate.getTag().getInt("Air");
    }

    public static void consumeAir(ItemStack chestplate, int amount) {
        if (!hasAirTank(chestplate) || chestplate.getTag() == null) return;

        int currentAir = getAirLevel(chestplate);
        int newAir = Math.max(0, currentAir - amount);
        chestplate.getTag().putInt("Air", newAir);
    }

    public static boolean isFullyCharged(ItemStack chestplate) {
        if (!hasAirTank(chestplate)) return false;
        int currentAir = getAirLevel(chestplate);
        return currentAir > 10;
    }

    public static int getMaxAirCapacity(ItemStack chestplate) {
        if (!hasAirTank(chestplate)) return 0;

        String registryName = "";
        try {
            registryName = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(chestplate.getItem()).toString().toLowerCase();
        } catch (Exception e) {
            registryName = chestplate.getItem().toString().toLowerCase();
        }

        if (registryName.contains("netherite")) {
            return 900;
        } else if (registryName.contains("copper")) {
            return 600;
        }

        return 300;
    }

    public static float getAirPercentage(ItemStack chestplate) {
        if (!hasAirTank(chestplate)) return 0.0f;

        int currentAir = getAirLevel(chestplate);
        int maxAir = getMaxAirCapacity(chestplate);

        if (maxAir <= 0) return 0.0f;

        return (float) currentAir / maxAir;
    }

    public static boolean isAirCritical(ItemStack chestplate) {
        return getAirPercentage(chestplate) < 0.2f;
    }

    public static boolean isAirLow(ItemStack chestplate) {
        return getAirPercentage(chestplate) < 0.5f;
    }

    public static void refillAirTank(ItemStack chestplate) {
        if (!hasAirTank(chestplate) || chestplate.getTag() == null) return;

        int maxAir = getMaxAirCapacity(chestplate);
        chestplate.getTag().putInt("Air", maxAir);
    }

    public static boolean isNetheriteBoots(ItemStack boots) {
        if (boots.isEmpty()) return false;
        String registryName = "";

        try {
            registryName = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(boots.getItem()).toString().toLowerCase();
        } catch (Exception e) {
            registryName = boots.getItem().toString().toLowerCase();
        }

        return boots.is(Items.NETHERITE_BOOTS) ||
                (registryName.contains("create") && registryName.contains("netherite") && registryName.contains("diving"));
    }

    private static boolean isFullEnvironmentalSuit(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        return isCreateArmorPiece(helmet) && isCreateArmorPiece(chestplate) &&
                isCreateArmorPiece(leggings) && isCreateArmorPiece(boots) &&
                helmet.getItem().toString().toLowerCase().contains("netherite") &&
                chestplate.getItem().toString().toLowerCase().contains("netherite") &&
                leggings.getItem().toString().toLowerCase().contains("netherite") &&
                boots.getItem().toString().toLowerCase().contains("netherite");
    }

    private static boolean areAllSameMaterial(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        if (helmet.isEmpty() || chestplate.isEmpty() || leggings.isEmpty() || boots.isEmpty()) return false;

        if (!(helmet.getItem() instanceof ArmorItem h) ||
                !(chestplate.getItem() instanceof ArmorItem c) ||
                !(leggings.getItem() instanceof ArmorItem l) ||
                !(boots.getItem() instanceof ArmorItem b)) return false;

        try {
            return h.getMaterial().equals(c.getMaterial()) &&
                    c.getMaterial().equals(l.getMaterial()) &&
                    l.getMaterial().equals(b.getMaterial());
        } catch (Exception e) {
            String hMat = h.getMaterial().toString();
            String cMat = c.getMaterial().toString();
            String lMat = l.getMaterial().toString();
            String bMat = b.getMaterial().toString();

            return hMat.equals(cMat) && cMat.equals(lMat) && lMat.equals(bMat);
        }
    }

    public static class ArmorProtectionResult {
        public final float protection;
        public final boolean windResistant;
        public final boolean fullEnvironmentalSuit;
        public final boolean hasFullArmor;
        public final boolean hasChargedAirTank;

        public ArmorProtectionResult(float protection, boolean windResistant, boolean fullEnvironmentalSuit,
                                     boolean hasFullArmor, boolean hasChargedAirTank) {
            this.protection = protection;
            this.windResistant = windResistant;
            this.fullEnvironmentalSuit = fullEnvironmentalSuit;
            this.hasFullArmor = hasFullArmor;
            this.hasChargedAirTank = hasChargedAirTank;
        }
    }
}