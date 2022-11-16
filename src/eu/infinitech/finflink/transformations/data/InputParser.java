package eu.infinitech.finflink.transformations.data;

import eu.infinitech.finflink.structures.InputStreamType;

public class InputParser  {
    public static InputMapper selectMapper(InputStreamType inputStreamType){
        switch (inputStreamType.getType()){
            case PricePoint:
                return new ToPricePoint(inputStreamType.getAsset());
            default:
                return new ToTrade();
        }
    }
}
