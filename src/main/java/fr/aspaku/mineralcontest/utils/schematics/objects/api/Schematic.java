package fr.aspaku.mineralcontest.utils.schematics.objects.api;

import fr.aspaku.mineralcontest.utils.schematics.api.Region;
import org.bukkit.Location;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public interface Schematic {
    Region getRegion();

    void addLocation(SchematicLocation location);

    List<Location> getConvertedLocation(String key, Location pasteLocation);

    File save(File file) throws IOException;

    void paste(Location location, int bpt, Consumer<Long> consumer);
}
