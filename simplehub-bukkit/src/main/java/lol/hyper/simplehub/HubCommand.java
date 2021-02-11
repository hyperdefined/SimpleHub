package lol.hyper.simplehub;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {

    private final SimpleHubBukkit simpleHubBukkit;

    public HubCommand(SimpleHubBukkit simpleHubBukkit) {
        this.simpleHubBukkit = simpleHubBukkit;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.RED + "This command is for players only.");
            return true;
        }

        if (simpleHubBukkit.config.getBoolean("use-permission-node")) {
            if (!sender.hasPermission(simpleHubBukkit.config.getString("permission-node"))) {
                sender.sendMessage(ChatColor.RED + "You don't have permission for this command.");
                return true;
            }
        }
        sender.sendMessage(simpleHubBukkit.convertConfigMessage());
        if (simpleHubBukkit.config.getBoolean("wait-to-teleport")) {
            Bukkit.getScheduler().runTaskLater(simpleHubBukkit, () -> simpleHubBukkit.sendPlayerToServer((Player) sender), 20L * simpleHubBukkit.config.getInt("wait-time"));
        } else {
            simpleHubBukkit.sendPlayerToServer((Player) sender);
        }
        return true;
    }
}
