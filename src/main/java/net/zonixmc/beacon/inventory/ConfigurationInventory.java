package net.zonixmc.beacon.inventory;

import com.google.common.collect.Maps;
import net.zonixmc.beacon.BeaconApi;
import net.zonixmc.beacon.builder.BeaconBuilder;
import net.zonixmc.beacon.attributes.Attribute;
import net.zonixmc.beacon.model.CustomBeacon;
import net.zonixmc.beacon.util.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;

import java.util.Collections;
import java.util.Map;

public class ConfigurationInventory implements Listener {

    private static Map<Player, CustomBeacon> EDITING = Maps.newHashMap();
    private static Integer LATEST_SLOT;

    public static void open(Player player, CustomBeacon customBeacon) {

        Inventory inventory = Bukkit.createInventory(null,  9 * 5, "Configurar Sinalizador");

        inventory.setItem(4, new BeaconBuilder(customBeacon.getAttributes()).buildItem());

        CustomItem stateChanger = new CustomItem(Material.INK_SACK);

        if (customBeacon.isActive()) stateChanger.name("§aLigado").data(10);

        else stateChanger.name("§cDesligado").data(8);

        inventory.setItem(13, stateChanger.build());

        int slot = 28;

        for (Attribute attribute : Attribute.values()) {

            if (!attribute.hasEffect()) continue;

            ItemStack itemStack = new ItemStack(Material.FIREWORK_CHARGE);
            FireworkEffectMeta itemMeta = (FireworkEffectMeta) itemStack.getItemMeta();

            itemMeta.addItemFlags(ItemFlag.values());
            itemMeta.setDisplayName(String.format("§a%s", attribute.getName()));
            itemMeta.setLore(Collections.singletonList("§7Clique para alterar estado do efeito."));

            itemMeta.setEffect(FireworkEffect.builder().withColor(attribute.getColor()).build());
            itemStack.setItemMeta(itemMeta);

            inventory.setItem(slot, itemStack);

            CustomItem attributeState = new CustomItem(Material.STAINED_GLASS_PANE);
            boolean hasAttribute = customBeacon.getAttributes().containsKey(attribute);

            if (!hasAttribute) attributeState.data(7).name("§7Não possui");

            else if (!customBeacon.isActive(attribute)) attributeState.data(14).name("§cDesativado");

            else attributeState.data(5).name("§aAtivo");

            inventory.setItem(slot + 9, attributeState.build());
            slot++;

        }

        if (LATEST_SLOT == null) LATEST_SLOT = slot;

        EDITING.put(player, customBeacon);
        player.openInventory(inventory);

    }

    @EventHandler
    void on(InventoryCloseEvent event) {

        if (!event.getInventory().getName().equals("Configurar Sinalizador")) return;

        EDITING.remove(((Player) event.getPlayer()));

    }

    @EventHandler
    void on(InventoryClickEvent event) {

        if (!event.getInventory().getName().equals("Configurar Sinalizador")) return;

        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (!EDITING.containsKey(player)) {

            player.closeInventory();
            return;

        }

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR
                || event.getCurrentItem().getType() == Material.BEACON || event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) return;

        CustomBeacon customBeacon = EDITING.get(player);
        int rawSlot = event.getRawSlot();

        player.closeInventory();

        if (rawSlot == 13) {

            boolean isActive = customBeacon.isActive();

            customBeacon.setActive(!isActive);
            player.sendMessage(!isActive ? "§aSinalizador ativado com sucesso!" : "§cSinalizador desativado com sucesso!");
            open(player, customBeacon);

            BeaconApi.INSTANCE.getBeaconDAO().saveBeacon(BeaconApi.INSTANCE.getBeaconCache().getKey(customBeacon), customBeacon, true);

            return;

        }

        if (rawSlot >= 28 && rawSlot <= LATEST_SLOT) {

            ItemStack contextItem = event.getInventory().getItem(rawSlot);
            String attributeName = ChatColor.stripColor(contextItem.getItemMeta().getDisplayName());

            Attribute attribute = Attribute.getByName(attributeName);

            if (!customBeacon.getAttributes().containsKey(attribute)) {

                player.sendMessage("§cEste sinalizador não possui este atributo.");
                open(player, customBeacon);
                return;

            }

            boolean isActive = customBeacon.isActive(attribute);

            if (!isActive) {

                if (!customBeacon.canActiveEffect(attribute)) {

                    player.sendMessage("§cVocê atingiu o limite de efeitos ativos.");
                    open(player, customBeacon);
                    return;

                }

                customBeacon.activeEffect(attribute);
                player.sendMessage(String.format("§aVocê ativou o atributo %s neste sinalizador.", attribute.getName().toLowerCase()));
                open(player, customBeacon);

                BeaconApi.INSTANCE.getBeaconDAO().saveBeacon(BeaconApi.INSTANCE.getBeaconCache().getKey(customBeacon), customBeacon, true);

                return;

            }

            customBeacon.disableEffect(attribute);
            player.sendMessage(String.format("§aVocê desativou o atributo %s neste sinalizador.", attribute.getName().toLowerCase()));
            open(player, customBeacon);

            BeaconApi.INSTANCE.getBeaconDAO().saveBeacon(BeaconApi.INSTANCE.getBeaconCache().getKey(customBeacon), customBeacon, true);

        }

    }

}
