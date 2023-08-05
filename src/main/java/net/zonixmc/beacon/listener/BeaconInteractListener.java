package net.zonixmc.beacon.listener;

import net.zonixmc.beacon.BeaconApi;
import net.zonixmc.beacon.builder.KeyBuilder;
import net.zonixmc.beacon.attributes.Attribute;
import net.zonixmc.beacon.inventory.ConfigurationInventory;
import net.zonixmc.beacon.inventory.PyramidInventory;
import net.zonixmc.beacon.model.CustomBeacon;
import net.zonixmc.beacon.util.CoordinateUtil;
import net.zonixmc.beacon.util.NumberUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BeaconInteractListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    void on(PlayerInteractEvent event) {

        if (event.isCancelled() || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();

        if (block == null || block.getType() != Material.BEACON) return;

        CustomBeacon customBeacon = BeaconApi.INSTANCE.getBeaconCache().getValue(block.getLocation());

        if (customBeacon == null) return;

        ItemStack itemInHand = event.getItem();

        if (itemInHand == null || itemInHand.getType() != Material.FIREWORK_CHARGE) {

            Player player = event.getPlayer();

            event.setCancelled(true);

            if (!CoordinateUtil.checkPyramid(block.getLocation())) PyramidInventory.open(player);

            else ConfigurationInventory.open(player, customBeacon);

            return;

        }

        ItemMeta itemMeta = itemInHand.getItemMeta();

        if (itemMeta == null || !itemMeta.hasDisplayName()) return;

        KeyBuilder keyBuilder = new KeyBuilder(itemInHand);

        Attribute attribute = keyBuilder.getAttribute();

        if (attribute == null) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        int currentLevel = customBeacon.getAttributeLevel(attribute);
        int keyLevel = keyBuilder.getLevel();

        if (currentLevel >= keyLevel) {

            player.sendMessage("§cEste sinalizador já possui este atributo.");
            return;

        }

        if (Math.abs(currentLevel - keyLevel) > 1) {

            player.sendMessage("§cEsta chave possui um nível muito alto para este sinalizador.");
            return;

        }

        if (itemInHand.getAmount() > 1) itemInHand.setAmount(itemInHand.getAmount() - 1);

        else {

            itemInHand.setDurability((short) 1);
            player.getInventory().remove(itemInHand);

        }

        customBeacon.putAttribute(attribute, keyLevel);
        player.sendMessage(String.format("§aVocê adicionou o atributo %s %s ao sinalizador com sucesso!", attribute.getName(), NumberUtil.convertToRoman(keyLevel)));

    }

}
