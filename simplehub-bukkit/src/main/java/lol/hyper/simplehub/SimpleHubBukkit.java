package lol.hyper.simplehub;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public final class SimpleHubBukkit extends JavaPlugin {

    public final File configFile = new File(this.getDataFolder(), "config.yml");
    public final Logger logger = this.getLogger();
    public FileConfiguration config;
    final int CONFIG_VERSION = 1;

    @Override
    public void onEnable() {
        HubCommand hubCommand = new HubCommand(this);
        ReloadCommand reloadCommand = new ReloadCommand(this);
        this.getCommand("hub").setExecutor(hubCommand);
        this.getCommand("reloadhub").setExecutor(hubCommand);
        if (!configFile.exists()) {
            this.saveResource("config.yml", true);
            logger.info("Copying default config!");
        }
        loadConfig();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Metrics metrics = new Metrics(this, 10298);
    }

    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        if (config.getInt("config-version") != CONFIG_VERSION) {
            logger.warning("You configuration is out of date! Some features may not work!");
        }
    }

    public void sendPlayerToServer(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(config.getString("hub-server"));
        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    public String convertConfigMessage() {
        String message = config.getString("send-message");
        if (message.contains("{{SECONDS}}")) {
            message = message.replace("{{SECONDS}}", String.valueOf(config.getInt("wait-time")));
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
