package fr.aspaku.mineralcontest.utils.schematics.objects.api;

import fr.aspaku.mineralcontest.utils.schematics.api.Vector;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.entity.EntityType;

public interface SchematicEntity {
    EntityType getType();

    Vector getLocation();

    NBTTagCompound getNBT();

    void updateNBTCoordinates(double x, double y, double z);
}
