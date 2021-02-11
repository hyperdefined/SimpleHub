package lol.hyper.simplehub;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.TimeUnit;

public class HubCommand extends Command {

    private final SimpleHubBungeeCord simpleHubBungeeCord;

    public HubCommand(String name, SimpleHubBungeeCord simpleHubBungeeCord) {
        super(name);
        this.simpleHubBungeeCord = simpleHubBungeeCord;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender == simpleHubBungeeCord.getProxy().getConsole()) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "This command is for players only."));
            return;
        }

        if (simpleHubBungeeCord.config.getBoolean("use-permission-node")) {
            if (!sender.hasPermission(simpleHubBungeeCord.config.getString("permission-node"))) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "You don't have permission for this command."));
                return;
            }
        }
        sender.sendMessage(new TextComponent(ChatColor.RED + simpleHubBungeeCord.convertConfigMessage()));
        if (simpleHubBungeeCord.config.getBoolean("wait-to-teleport")) {
            simpleHubBungeeCord.getProxy().getScheduler().schedule(simpleHubBungeeCord, () -> {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                ServerInfo target = ProxyServer.getInstance().getServerInfo(simpleHubBungeeCord.config.getString("hub-server"));
                player.connect(target);
            }, simpleHubBungeeCord.config.getInt("wait-time"), TimeUnit.SECONDS);
        } else {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            ServerInfo target = ProxyServer.getInstance().getServerInfo(simpleHubBungeeCord.config.getString("hub-server"));
            player.connect(target);
        }
    }
}
