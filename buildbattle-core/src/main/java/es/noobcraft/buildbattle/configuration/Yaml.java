package es.noobcraft.buildbattle.configuration;

import com.avaje.ebeaninternal.server.lib.util.NotFoundException;
import es.noobcraft.core.api.Core;
import lombok.NonNull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Yaml {
    private final File file;
    private FileConfiguration fileConfiguration;

    public Yaml(@NonNull String fileName, boolean overwrite) throws NotFoundException{
        File directory = getWorkingDirectory();
        if(!directory.exists()) {
            directory.mkdirs();
        }

        fileName.replace("/","_");
        fileName.replace("\\","_");

        this.file = new File(directory, fileName+ ".yml");

        if (!exists_file() || overwrite) {
            if (overwrite) {
                file.delete();
            }
            try {
                file.createNewFile();
            } catch (IOException exception) {
                throw new NotFoundException("Unable to create a file on "+ fileName);
            }
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Saves configurationFile
     */
    public void saveFile(){
        if (!exists_file()){
            return;
        }
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets file FileConfiguration
     * @return FileConfiguration object
     */
    public FileConfiguration getFile(){
        if (!exists_file()){
            return null;
        }
        return fileConfiguration;
    }

    /**
     * Reload the FileConfiguration
     */
    public void reloadFile(){
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    private boolean exists_file(){
        return file.exists();
    }

    /**
     * Get BuildBattle DataFolder
     * @return BuildBattle DataFolder
     */
    public static File getWorkingDirectory() {
        return Core.getServerConfigurationsDirectory();
    }
}

