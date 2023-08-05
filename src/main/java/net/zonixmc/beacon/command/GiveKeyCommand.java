package net.zonixmc.beacon.command;

import net.zonixmc.beacon.builder.KeyBuilder;
import net.zonixmc.beacon.attributes.Attribute;
import net.zonixmc.beacon.util.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GiveKeyCommand extends Command {

    public GiveKeyCommand() { super("givekey"); }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] arguments) {

        if (!commandSender.hasPermission("zonixmc.manager")) {

            commandSender.sendMessage("§cVocê não possui permissão para executar este comando.");
            return false;

        }

        if (arguments.length != 3) {

            commandSender.sendMessage("§cUtilize /givekey <usuário> <attribute> <level>.");
            return false;

        }

        Player target = Bukkit.getPlayerExact(arguments[0]);
        Attribute attribute = Attribute.get(arguments[1]);

        if (attribute == null) {

            commandSender.sendMessage("§cVocê inseriu um atributo inválido.");
            commandSender.sendMessage("§cAtributos: " + Strings.join(Arrays.stream(Attribute.values()).map(Enum::name).collect(Collectors.toList()), ", "));
            return false;

        }

        Integer level = NumberUtil.parseToInt(arguments[2]);

        if (level == null) {

            commandSender.sendMessage("§cVocê inseriu um level inválido.");
            return false;

        }

        if (level > attribute.getMaxLevel()) level = attribute.getMaxLevel();

        target.getInventory().addItem(new KeyBuilder(attribute, level).buildItem()).values().forEach(context -> target.getWorld().dropItemNaturally(target.getLocation(), context));
        commandSender.sendMessage(String.format("§a%s recebeu uma chave %s level %s com sucesso!", target.getName(), attribute.getName().toLowerCase(), level));
        return false;

    }

}
