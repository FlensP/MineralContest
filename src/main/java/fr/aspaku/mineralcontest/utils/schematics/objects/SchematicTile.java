package fr.aspaku.mineralcontest.utils.schematics.objects;

import fr.aspaku.mineralcontest.utils.schematics.api.Vector;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;

public class SchematicTile implements fr.aspaku.mineralcontest.utils.schematics.objects.api.SchematicTile {

    private final Vector location;

    private final NBTTagCompound nbt;

    public SchematicTile(Vector location, NBTTagCompound nbt) {
        this.location = location;
        this.nbt = nbt;
    }

    @Override
    public Vector getLocation() {
        return location;
    }

    @Override
    public NBTTagCompound getNBT() {
        return nbt;
    }

    @Override
    public void updateNBTCoordinates(int x, int y, int z) {
        nbt.set("x", new NBTTagInt(x));
        nbt.set("y", new NBTTagInt(y));
        nbt.set("z", new NBTTagInt(z));
    }
}
