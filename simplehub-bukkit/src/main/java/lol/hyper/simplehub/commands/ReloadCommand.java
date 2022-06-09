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

import lol.hyper.simplehub.SimpleHubBukkit;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final SimpleHubBukkit simpleHubBukkit;
    private final BukkitAudiences audiences;

    public ReloadCommand(SimpleHubBukkit simpleHubBukkit) {
        this.simpleHubBukkit = simpleHubBukkit;
        this.audiences = simpleHubBukkit.getAdventure();
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.hasPermission("simplehub.reload")) {
            simpleHubBukkit.loadConfig();
            audiences.sender(sender).sendMessage(Component.text("Config reloaded!").color(NamedTextColor.GREEN));
        } else {
            audiences.sender(sender).sendMessage(Component.text("You do not have permission to do this command.").color(NamedTextColor.RED));
        }
        return true;
    }
}
