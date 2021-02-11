package lol.hyper.simplehub;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final SimpleHubBukkit simpleHubBukkit;

    public ReloadCommand(SimpleHubBukkit simpleHubBukkit) {
        this.simpleHubBukkit = simpleHubBukkit;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("simplehub.reload")) {
            simpleHubBukkit.loadConfig();
            sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission to do this command.");
        }
        return true;
    }
}
