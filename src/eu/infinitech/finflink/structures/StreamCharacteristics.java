package eu.infinitech.finflink.structures;

import eu.infinitech.finflink.transformations.technicalIndicators.TechnicalIndicatorGenerator;

public class StreamCharacteristics {
    private boolean volume;
    private boolean close;
    private boolean open;
    private boolean high;
    private boolean low;

    public StreamCharacteristics() {}

    public boolean meetsRequirements(TechnicalIndicatorGenerator technicalIndicatorGenerator){
        return meetsRequirements(technicalIndicatorGenerator.getIndicatorRequirements());
    }

    public boolean meetsRequirements(IndicatorRequirements indicatorRequirements){
        if (indicatorRequirements.checkVolume() && !this.volume) return false;
        if (indicatorRequirements.checkClose() && !this.close) return false;
        if (indicatorRequirements.checkOpen() && !this.open) return false;
        if (indicatorRequirements.checkHigh() && !this.high) return false;
        if (indicatorRequirements.checkLow() && !this.low) return false;

        return true;
    }

    public StreamCharacteristics hasVolume(){
        volume = true;
        return this;
    }

    public StreamCharacteristics hasOpen(){
        open = true;
        return this;
    }

    public StreamCharacteristics hasClose(){
        close = true;
        return this;
    }

    public StreamCharacteristics hasHigh(){
        high = true;
        return this;
    }

    public StreamCharacteristics hasLow(){
        low = true;
        return this;
    }
}
