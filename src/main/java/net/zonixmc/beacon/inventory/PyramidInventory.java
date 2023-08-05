package net.zonixmc.beacon.inventory;

import net.zonixmc.beacon.util.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PyramidInventory implements Listener {

    private static final int[] slots = new int[] {

            21, 22, 23,
            29, 30, 31, 32, 33,
            37, 38, 39, 40, 41, 42, 43,
            45, 46, 47, 48, 49, 50, 51, 52, 53

    };

    private static Inventory inventory;

    public static void open(Player player) {

        if (inventory == null) {

            inventory = Bukkit.createInventory(null,  9 * 6, "Sinalizador");
            ItemStack pyramid = new CustomItem(Material.STAINED_GLASS_PANE).data(15).name("§7Pirâmide").build();

            for (int slot : slots)
                inventory.setItem(slot, pyramid);

            inventory.setItem(13, new CustomItem(Material.BEACON).name("§ePorque estou vendo este menu?")
                    .lore(
                            "§7O seu Sinalizador ainda não pode ser",
                            "§7utilizado, pois ainda faltam blocos na",
                            "§7sua pirâmide.",
                            "",
                            "§7Construa uma pirâmide com 4 camadas",
                            "§7maciças, feitas de Blocos de Diamante,",
                            "§7Esmeralda, Ouro ou Ferro."
                    ).build());

        }

        player.openInventory(inventory);

    }

    @EventHandler
    void on(InventoryClickEvent event) {

        if (!event.getInventory().getName().equals("Sinalizador")) return;

        event.setCancelled(true);

    }

}
