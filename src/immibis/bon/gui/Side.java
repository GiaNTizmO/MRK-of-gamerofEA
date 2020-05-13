package immibis.bon.gui;

import immibis.bon.mcp.MinecraftNameSet;

public enum Side {
    Universal(MinecraftNameSet.Side.UNIVERSAL, "bin.zip");

    public final MinecraftNameSet.Side nsside;
    public final String referencePath;

    Side(MinecraftNameSet.Side nsside, String referencePath) {
        this.nsside = nsside;
        this.referencePath = referencePath;
    }
}