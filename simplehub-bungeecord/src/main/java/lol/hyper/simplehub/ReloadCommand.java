package lol.hyper.simplehub;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {

    private final SimpleHubBungeeCord simpleHubBungeeCord;

    public ReloadCommand(String name, SimpleHubBungeeCord simpleHubBungeeCord) {
        super(name);
        this.simpleHubBungeeCord = simpleHubBungeeCord;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("simplehub.reload")) {
            simpleHubBungeeCord.loadConfig();
            sender.sendMessage(new TextComponent(ChatColor.GREEN + "Config reloaded!"));
        } else {
            sender.sendMessage(new TextComponent(ChatColor.RED + "You do not have permission to do this command."));
        }
    }
}
