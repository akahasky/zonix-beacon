package net.zonixmc.beacon.builder;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.zonixmc.beacon.attributes.Attribute;
import net.zonixmc.beacon.util.NumberUtil;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import java.util.Arrays;
import java.util.List;

public class KeyBuilder {

    private static final String BASE_NAME = "§cChave de Ativação:";

    @Getter private Attribute attribute;
    @Getter private int level;

    public KeyBuilder(Attribute attribute, int level) {

        this.attribute = attribute;

        if (level > attribute.getMaxLevel()) level = attribute.getMaxLevel();

        this.level = level;

    }

    public KeyBuilder(ItemStack itemStack) {

        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasLore()) return;

        String attributeAndLevel = itemStack.getItemMeta().getDisplayName().split(": ")[1];

        this.attribute = Arrays.stream(Attribute.values()).filter(context -> attributeAndLevel.contains(context.getName())).findFirst().orElse(null);
        this.level = NumberUtil.parseToInt(attributeAndLevel.split(attribute.getName() + " ")[1]);

    }

    public ItemStack buildItem() {

        ItemStack itemStack = new ItemStack(Material.FIREWORK_CHARGE);
        FireworkEffectMeta itemMeta = (FireworkEffectMeta) itemStack.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.values());
        itemMeta.setDisplayName(String.format("%s %s %s", BASE_NAME, attribute.getName(), level));

        List<String> lore = Lists.newArrayList();

        lore.add("§7Esta chave habilita o atributo");
        lore.add(String.format("§7%s %s no seu sinalizador.", attribute.getName(), NumberUtil.convertToRoman(level)));
        lore.add("");
        lore.add("§eComo Utillizar?");
        lore.add("§fClique com o botão direito em um");
        lore.add("§fsinalizador enquanto segura este item.");

        itemMeta.setLore(lore);
        itemMeta.setEffect(FireworkEffect.builder().withColor(attribute.getColor()).build());
        itemStack.setItemMeta(itemMeta);

        return itemStack;

    }

}
