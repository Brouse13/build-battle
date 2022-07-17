package es.noobcraft.buildbattle.signs;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.IOException;

public class ArenaSignAdapter extends TypeAdapter<ArenaSign> {
    @Override
    public void write(JsonWriter jsonWriter, ArenaSign arenaSign) throws IOException {
        final Location location = arenaSign.getLocation();
        jsonWriter.beginObject();
        jsonWriter.name("server");
        jsonWriter.value(arenaSign.getServer());
        jsonWriter.name("location");
        jsonWriter.value(location.getWorld().getName()+ ","+ (int)location.getX()+ ","+ (int)location.getY()+ ","+ (int)location.getZ());
        jsonWriter.endObject();
    }

    @Override
    public ArenaSign read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        String fieldname = null;
        String server = null;
        String[] location = null;

        while (jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();

            if (token.equals(JsonToken.NAME)) fieldname = jsonReader.nextName();

            if (fieldname.equals("server")) {
                token = jsonReader.peek();
                server = jsonReader.nextString();
            }

            if(fieldname.equals("location")) {
                token = jsonReader.peek();
                location = jsonReader.nextString().split(",");
            }
        }
        jsonReader.endObject();

        if(server == null || location == null) return null;

        return new ArenaSign(server, new Location(
                Bukkit.getWorld(location[0]),
                Integer.parseInt(location[1]),
                Integer.parseInt(location[2]),
                Integer.parseInt(location[3])));
    }
}
