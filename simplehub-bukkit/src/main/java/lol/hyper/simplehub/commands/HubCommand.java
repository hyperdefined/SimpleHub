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

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lol.hyper.simplehub.SimpleHubBukkit;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HubCommand implements CommandExecutor {

    private final SimpleHubBukkit simpleHubBukkit;
    private final BukkitAudiences audiences;

    public HubCommand(SimpleHubBukkit simpleHubBukkit) {
        this.simpleHubBukkit = simpleHubBukkit;
        this.audiences = simpleHubBukkit.getAdventure();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            audiences.sender(sender).sendMessage(Component.text("This command is for players only.").color(NamedTextColor.RED));
            return true;
        }
        audiences.sender(sender).sendMessage(simpleHubBukkit.getMessage("send-message"));
        String server = simpleHubBukkit.config.getString("hub-server");
        if (server == null) {
            simpleHubBukkit.logger.severe("\"hub-server\" in SimpleHub's config is missing! Unable to use command.");
            audiences.sender(sender).sendMessage(Component.text("\"hub-server\" in SimpleHub's config is missing! Unable to use command.").color(NamedTextColor.RED));
            return true;
        }
        Bukkit.getScheduler().runTaskLater(simpleHubBukkit,
                () -> sendPlayerToServer((Player) sender, server),
                20L * simpleHubBukkit.config.getInt("wait-time"));
        return true;
    }

    private void sendPlayerToServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(simpleHubBukkit, "BungeeCord", out.toByteArray());
    }
}
