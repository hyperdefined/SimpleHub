/*
 * This file is part of SimpleHub.
 *
 * SimpleHub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SimpleHub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SimpleHub.  If not, see <https://www.gnu.org/licenses/>.
 */

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
            simpleHubBungeeCord
                    .getProxy()
                    .getScheduler()
                    .schedule(
                            simpleHubBungeeCord,
                            () -> {
                                ProxiedPlayer player = (ProxiedPlayer) sender;
                                ServerInfo target = ProxyServer.getInstance()
                                        .getServerInfo(simpleHubBungeeCord.config.getString("hub-server"));
                                player.connect(target);
                            },
                            simpleHubBungeeCord.config.getInt("wait-time"),
                            TimeUnit.SECONDS);
        } else {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            ServerInfo target =
                    ProxyServer.getInstance().getServerInfo(simpleHubBungeeCord.config.getString("hub-server"));
            player.connect(target);
        }
    }
}
