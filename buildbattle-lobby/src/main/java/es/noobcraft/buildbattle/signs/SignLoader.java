package es.noobcraft.buildbattle.signs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.noobcraft.buildbattle.configuration.Yaml;
import es.noobcraft.buildbattle.logger.Logger;
import es.noobcraft.buildbattle.logger.LoggerType;
import org.bukkit.Bukkit;

import java.io.*;

public class SignLoader {
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(ArenaSign.class, new ArenaSignAdapter().nullSafe()).create();

    public static void loadSigns() {
        Yaml yaml = new Yaml("config", false);
        String world = yaml.getFile().contains("settings.world") ? yaml.getFile().getString("settings.world") : "world";

        File file = new File(Bukkit.getWorldContainer()+ "/"+ world+ "/signs.json");

        if (!file.exists()) {
            try {
                file.createNewFile();
                Logger.log(LoggerType.CONSOLE, "No signs file found, creating it");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder message = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) message.append(line);

            if (message.toString().equals("")) {
                Logger.log(LoggerType.CONSOLE, "No signs loaded");
                return;
            }

            ArenaSignManager.addAll(gson.fromJson(message.toString(), ArenaSign[].class));
            Logger.log(LoggerType.CONSOLE, "Loaded "+ ArenaSignManager.getSigns().size()+ " signs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSigns() {
        Yaml yaml = new Yaml("config", false);
        String world = yaml.getFile().contains("settings.world") ? yaml.getFile().getString("settings.world") : "world";

        File file = new File(Bukkit.getWorldContainer()+ "/"+ world+ "/signs.json");

        if (file.exists()) file.delete();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try(BufferedWriter reader = new BufferedWriter(new FileWriter(file))) {
            final String json = gson.toJson(ArenaSignManager.getSigns().toArray(), ArenaSign[].class);
            reader.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
