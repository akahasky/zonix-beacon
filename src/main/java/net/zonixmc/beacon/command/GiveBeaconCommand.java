package net.zonixmc.beacon.command;

import net.zonixmc.beacon.builder.BeaconBuilder;
import net.zonixmc.beacon.util.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveBeaconCommand extends Command {

    public GiveBeaconCommand() { super("givebeacon"); }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] arguments) {

        if (!commandSender.hasPermission("zonixmc.manager")) {

            commandSender.sendMessage("§cVocê não possui permissão para executar este comando.");
            return false;

        }

        if (arguments.length != 2) {

            commandSender.sendMessage("§cUtilize /givebeacon <usuário> <quantia>.");
            return false;

        }

        Player target = Bukkit.getPlayerExact(arguments[0]);
        Integer amount = NumberUtil.parseToInt(arguments[1]);

        if (amount == null) {

            commandSender.sendMessage("§cVocê inseriu uma quantia inválida.");
            return false;

        }

        for (int index = 0; index < amount; index++)
            target.getInventory().addItem(new BeaconBuilder().buildItem()).values().forEach(context -> target.getWorld().dropItemNaturally(target.getLocation(), context));

        commandSender.sendMessage(String.format("§a%s recebeu %sx beacon(s) com sucesso!", target.getName(), amount));
        return false;

    }

}
