package fr.aspaku.mineralcontest.utils.schematics.objects.api;

import fr.aspaku.mineralcontest.utils.schematics.api.Vector;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public interface SchematicTile {
    Vector getLocation();

    NBTTagCompound getNBT();

    void updateNBTCoordinates(int x, int y, int z);
}
