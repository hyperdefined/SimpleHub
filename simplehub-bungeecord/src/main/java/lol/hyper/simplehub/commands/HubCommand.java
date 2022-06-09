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

package lol.hyper.simplehub.commands;

import lol.hyper.simplehub.SimpleHubBungeeCord;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.TimeUnit;

public class HubCommand extends Command {

    private final SimpleHubBungeeCord simpleHubBungeeCord;
    private final BungeeAudiences audiences;

    public HubCommand(String name, SimpleHubBungeeCord simpleHubBungeeCord) {
        super(name);
        this.simpleHubBungeeCord = simpleHubBungeeCord;
        this.audiences = simpleHubBungeeCord.getAdventure();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender == simpleHubBungeeCord.getProxy().getConsole()) {
            audiences.sender(sender).sendMessage(Component.text("This command is for players only.").color(NamedTextColor.RED));
            return;
        }
        audiences.sender(sender).sendMessage(simpleHubBungeeCord.getMessage("send-message"));
        String serverConfig = simpleHubBungeeCord.config.getString("hub-server");
        ServerInfo targetServer = ProxyServer.getInstance().getServerInfo(serverConfig);
        if (serverConfig == null) {
            simpleHubBungeeCord.logger.severe("\"hub-server\" in SimpleHub's config is missing! Unable to use command.");
            audiences.sender(sender).sendMessage(Component.text("\"hub-server\" in SimpleHub's config is missing! Unable to use command.").color(NamedTextColor.RED));
            return;
        }
        if (targetServer == null) {
            simpleHubBungeeCord.logger.severe(serverConfig + " is not a valid server on your network.");
            audiences.sender(sender).sendMessage(Component.text(serverConfig + " is not a valid server on your network.").color(NamedTextColor.RED));
            return;
        }
        simpleHubBungeeCord.getProxy().getScheduler().schedule(simpleHubBungeeCord, () -> {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            player.connect(targetServer);
        }, simpleHubBungeeCord.config.getInt("wait-time"), TimeUnit.SECONDS);
    }
}
