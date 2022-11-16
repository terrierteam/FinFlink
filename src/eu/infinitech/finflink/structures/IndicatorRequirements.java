package eu.infinitech.finflink.structures;

import java.io.Serializable;

public class IndicatorRequirements implements Serializable {
    private boolean volume;
    private boolean open;
    private boolean close;
    private boolean high;
    private boolean low;

    public IndicatorRequirements() {
        volume = false;
        open = false;
        close = false;
        high = false;
        low = false;
    }

    public IndicatorRequirements needsVolume(){
        volume = true;
        return this;
    }

    public IndicatorRequirements needsOpen(){
        open = true;
        return this;
    }

    public IndicatorRequirements needsClose(){
        close = true;
        return this;
    }

    public IndicatorRequirements needsHigh(){
        high = true;
        return this;
    }

    public IndicatorRequirements needsLow(){
        low = true;
        return this;
    }

    public boolean checkVolume() {
        return volume;
    }

    public boolean checkOpen() {
        return open;
    }

    public boolean checkClose() {
        return close;
    }

    public boolean checkHigh() {
        return high;
    }

    public boolean checkLow() {
        return low;
    }
}
