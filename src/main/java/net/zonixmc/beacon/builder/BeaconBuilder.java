package net.zonixmc.beacon.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.zonixmc.beacon.attributes.Attribute;
import net.zonixmc.beacon.model.CustomBeacon;
import net.zonixmc.beacon.util.NumberUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class BeaconBuilder {

    private static final String BEACON_NAME = "§bSinalizador";
    private static final Map<Attribute, Integer> DEFAULT_ATTRIBUTES = Maps.newEnumMap(Attribute.class);

    static {

        DEFAULT_ATTRIBUTES.put(Attribute.DURABILITY, 1);
        DEFAULT_ATTRIBUTES.put(Attribute.RANGE, 1);
        DEFAULT_ATTRIBUTES.put(Attribute.MULTIPLE, 1);

    }

    private final Map<Attribute, Integer> attributes;

    public BeaconBuilder() { attributes = DEFAULT_ATTRIBUTES; }
    public BeaconBuilder(Map<Attribute, Integer> attributes) {

        this.attributes = Maps.newEnumMap(Attribute.class);

        this.attributes.putAll(attributes);

    }

    public BeaconBuilder(ItemStack itemStack) {

        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()) this.attributes = DEFAULT_ATTRIBUTES;

        else {

            Map<Attribute, Integer> attributes = Maps.newEnumMap(Attribute.class);
            List<String> lore = itemStack.getItemMeta().getLore();

            for (Attribute attribute : Attribute.values()) {

                String attributeLore = lore.stream().filter(line -> line.contains(attribute.getName())).findFirst().orElse(null);

                if (attributeLore == null) continue;

                attributes.put(attribute, Integer.valueOf(attributeLore.split("§8\\(")[1].split("/")[0]));

            }

            this.attributes = attributes;

        }

    }

    public ItemStack buildItem() {

        ItemStack itemStack = new ItemStack(Material.BEACON);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(BEACON_NAME);

        List<String> lore = Lists.newArrayList();

        attributes.keySet().stream().filter(attribute -> !attribute.hasEffect()).forEach(attribute -> {

            int level = attributes.get(attribute);

            lore.add(String.format("§f%s §7%s §8(%s/%s)", attribute.getName(), NumberUtil.convertToRoman(level), level, attribute.getMaxLevel()));

        });

        lore.add("");
        lore.add("§fEfeitos liberados:");

        boolean hasEffects = attributes.keySet().stream().anyMatch(Attribute::hasEffect);

        if (hasEffects) {

            attributes.keySet().stream().filter(Attribute::hasEffect).forEach(attribute -> {

                int level = attributes.get(attribute);

                lore.add(String.format("§8▸ §f%s §7%s §8(%s/%s)", attribute.getName(), NumberUtil.convertToRoman(level), level, attribute.getMaxLevel()));

            });

        }

        else lore.add("§8▸ §cNenhum efeito liberado");

        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(itemMeta);

        return itemStack;

    }

    public CustomBeacon buildBeacon() {

        CustomBeacon customBeacon = new CustomBeacon();

        attributes.forEach(customBeacon::putAttribute);

        return customBeacon;

    }

}
