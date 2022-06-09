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

import lol.hyper.githubreleaseapi.GitHubRelease;
import lol.hyper.githubreleaseapi.GitHubReleaseAPI;
import lol.hyper.simplehub.commands.HubCommand;
import lol.hyper.simplehub.commands.ReloadCommand;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ProxyServer;
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
    final int CONFIG_VERSION = 2;
    public Configuration config;
    private BungeeAudiences adventure;
    public final MiniMessage miniMessage = MiniMessage.miniMessage();

    public HubCommand hubCommand;
    public ReloadCommand reloadCommand;

    @Override
    public void onEnable() {
        this.adventure = BungeeAudiences.create(this);
        hubCommand = new HubCommand("hub", this);
        reloadCommand = new ReloadCommand("reloadhub", this);
        loadConfig();
        getProxy().getPluginManager().registerCommand(this, hubCommand);
        getProxy().getPluginManager().registerCommand(this, reloadCommand);

        new Metrics(this, 10299);
        ProxyServer.getInstance().getScheduler().runAsync(this, this::checkForUpdates);
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

    public void checkForUpdates() {
        GitHubReleaseAPI api;
        try {
            api = new GitHubReleaseAPI("SimpleHub", "hyperdefined");
        } catch (IOException e) {
            logger.warning("Unable to check updates!");
            e.printStackTrace();
            return;
        }
        GitHubRelease current = api.getReleaseByTag(this.getDescription().getVersion());
        GitHubRelease latest = api.getLatestVersion();
        if (current == null) {
            logger.warning("You are running a version that does not exist on GitHub. If you are in a dev environment, you can ignore this. Otherwise, this is a bug!");
            return;
        }
        int buildsBehind = api.getBuildsBehind(current);
        if (buildsBehind == 0) {
            logger.info("You are running the latest version.");
        } else {
            logger.warning("A new version is available (" + latest.getTagVersion() + ")! You are running version " + current.getTagVersion() + ". You are " + buildsBehind + " version(s) behind.");
        }
    }

    public Component getMessage(String path) {
        String message = config.getString(path);
        if (message == null) {
            logger.warning(path + " is not a valid message!");
            return Component.text("Invalid path! " + path).color(NamedTextColor.RED);
        }
        if (message.contains("{{SECONDS}}")) {
            message = message.replace("{{SECONDS}}", String.valueOf(config.getInt("wait-time")));
        }
        return miniMessage.deserialize(message);
    }

    public BungeeAudiences getAdventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }
}
