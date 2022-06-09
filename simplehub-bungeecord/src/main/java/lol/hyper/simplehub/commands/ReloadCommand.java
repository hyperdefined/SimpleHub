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
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {

    private final SimpleHubBungeeCord simpleHubBungeeCord;
    private final BungeeAudiences audiences;

    public ReloadCommand(String name, SimpleHubBungeeCord simpleHubBungeeCord) {
        super(name);
        this.simpleHubBungeeCord = simpleHubBungeeCord;
        this.audiences = simpleHubBungeeCord.getAdventure();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("simplehub.reload")) {
            simpleHubBungeeCord.loadConfig();
            audiences.sender(sender).sendMessage(Component.text("Config reloaded!").color(NamedTextColor.GREEN));
        } else {
            audiences.sender(sender).sendMessage(Component.text("You do not have permission to do this command.").color(NamedTextColor.RED));
        }
    }
}
