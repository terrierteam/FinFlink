package eu.infinitech.finflink.structures;

import java.io.Serializable;

public class InputStreamType implements Serializable {
    public enum Type {
        Trade, PricePoint
    }

    private Type type;
    private String asset;

    public InputStreamType(Type type){
        this.type = type;
    }

    public InputStreamType(Type type, String asset){
        this.type = type;
        this.asset = asset;
    }

    public static InputStreamType trade() {
        return new InputStreamType(Type.Trade);
    }

    public static InputStreamType pricePoint(String asset){
        return new InputStreamType(Type.PricePoint, asset);
    }

    public Type getType() {
        return type;
    }

    public String getAsset() {
        return asset;
    }
}
