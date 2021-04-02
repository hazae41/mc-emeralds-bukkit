package hazae41.emeralds;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
  public final File file;
  public FileConfiguration config;

  public Config(File file) {
    this.file = file;
    this.config = YamlConfiguration.loadConfiguration(file);
  }

  public void save() {
    try {
      config.save(file);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}