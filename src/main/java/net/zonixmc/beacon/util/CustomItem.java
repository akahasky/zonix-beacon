package net.zonixmc.beacon.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CustomItem {

    private final ItemStack itemStack;

    public CustomItem(Material material) { itemStack = new ItemStack(material); }

    public CustomItem(ItemStack itemStack) { this.itemStack = itemStack; }

    public CustomItem name(String name) {

        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);

        return this;

    }

    public CustomItem lore(String... lore) {

        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(itemMeta);

        return this;

    }

    public CustomItem addLore(String... lore) {

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemMeta.hasLore()) {

            List<String> currentLore = itemMeta.getLore();

            currentLore.addAll(Arrays.asList(lore));
            itemMeta.setLore(currentLore);

        }

        else itemMeta.setLore(Arrays.asList(lore));

        itemStack.setItemMeta(itemMeta);

        return this;

    }

    public CustomItem addLore(List<String> lore) {

        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> currentLore = itemMeta.getLore();

        currentLore.addAll(lore);

        itemMeta.setLore(currentLore);
        itemStack.setItemMeta(itemMeta);

        return this;

    }

    public CustomItem enchant(Enchantment enchantment, int level, boolean aBoolean) {

        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.addEnchant(enchantment, level, aBoolean);
        itemStack.setItemMeta(itemMeta);

        return this;

    }

    public CustomItem owner(String owner) {

        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        if (owner.length() <= 16) skullMeta.setOwner(owner);

        else {

            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);

            gameProfile.getProperties().put("textures", new Property("textures", owner));

            try {

                Field profileField = skullMeta.getClass().getDeclaredField("profile");

                profileField.setAccessible(true);
                profileField.set(skullMeta, gameProfile);

            }

            catch (Exception exception) { exception.printStackTrace(); }

        }

        itemStack.setItemMeta(skullMeta);

        return this;

    }

    public CustomItem hideAttributes() {

        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(itemMeta);

        return this;

    }

    public CustomItem data(int data) {

        itemStack.setDurability((short) data);
        return this;

    }

    public CustomItem amount(int amount) {

        itemStack.setAmount(amount);
        return this;

    }

//    public CustomItem banner(String letter, DyeColor baseColor, DyeColor letterColor) {
//
//        ItemStack banner = itemStack;
//
//        if (!banner.getType().equals(Material.BANNER)) return this;
//
//        letter = ChatColor.stripColor(letter.toUpperCase()).substring(0, 1);
//
//        BannerMeta bannerMeta = (BannerMeta) itemStack.getItemMeta();
//        bannerMeta.setBaseColor(baseColor);
//
//        switch (letter) {
//
//            case "@": break;
//
//            case "A":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "B":
//            case "8":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "C":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "D":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.CURLY_BORDER));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "E":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "F":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "G":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "H":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "I":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_CENTER));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "J":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "K":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_VERTICAL_MIRROR));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.CROSS));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "L":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "M":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.TRIANGLE_TOP));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.TRIANGLES_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "N":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.DIAGONAL_RIGHT_MIRROR));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNRIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "O":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "P":
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "Q":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.SQUARE_BOTTOM_RIGHT));
//                break;
//
//            case "R":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL_MIRROR));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNRIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "S":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.RHOMBUS_MIDDLE));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNRIGHT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.CURLY_BORDER));
//                break;
//
//            case "T":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_CENTER));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "U":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "V":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.TRIANGLES_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "W":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.TRIANGLE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.TRIANGLES_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "X":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_CENTER));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.CROSS));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "Y":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.CROSS));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_VERTICAL_MIRROR));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "Z":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "1":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.SQUARE_TOP_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_CENTER));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "2":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.RHOMBUS_MIDDLE));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "3":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "4":
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "5":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNRIGHT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.CURLY_BORDER));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.SQUARE_BOTTOM_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "6":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "7":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.DIAGONAL_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.SQUARE_BOTTOM_LEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "9":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL_MIRROR));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_MIDDLE));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//            case "0":
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_TOP));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_RIGHT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_BOTTOM));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_LEFT));
//                bannerMeta.addPattern(new Pattern(letterColor, PatternType.STRIPE_DOWNLEFT));
//                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
//                break;
//
//        }
//
//        banner.setItemMeta(bannerMeta);
//        return this;
//
//    }

    public ItemStack build() { return itemStack; }

    public CustomItem copy() { return new CustomItem(itemStack.clone()); }

}
