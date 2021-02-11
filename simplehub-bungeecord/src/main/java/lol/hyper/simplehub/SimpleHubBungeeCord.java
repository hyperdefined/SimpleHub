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
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.bstats.bungeecord.Metrics;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

public final class SimpleHubBungeeCord extends Plugin {

    public final File configFile = new File(this.getDataFolder(), "config.yml");
    public final Logger logger = this.getLogger();
    public Configuration config;
    final int CONFIG_VERSION = 1;

    @Override
    public void onEnable() {
        HubCommand hubCommand = new HubCommand("hub", this);
        ReloadCommand reloadCommand = new ReloadCommand("reloadhub", this);
        loadConfig();
        getProxy().getPluginManager().registerCommand(this, hubCommand);
        getProxy().getPluginManager().registerCommand(this, reloadCommand);

        Metrics metrics = new Metrics(this, 10299);
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            InputStream is = this.getResourceAsStream("config.yml");
            try {
                File path = new File("plugins" + File.separator + "SimpleHub");
                if (path.mkdir()) {
                    Files.copy(is, configFile.toPath());
                    this.logger.info("Copying default config...");
                } else {
                    this.logger.warning("Unable to create config folder!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            logger.severe("Unable to load configuration file!");
        }
        if (config.getInt("config-version") != CONFIG_VERSION) {
            logger.warning("You configuration is out of date! Some features may not work!");
        }
    }

    public String convertConfigMessage() {
        String message = config.getString("send-message");
        if (message.contains("{{SECONDS}}")) {
            message = message.replace("{{SECONDS}}", String.valueOf(config.getInt("wait-time")));
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
