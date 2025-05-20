package fr.baretto.ordermanager.dto;
import java.util.List;

public class ShipmentDTO {
    private String trackingNumber;
    private List<IndicatorDTO> indicators;

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public List<IndicatorDTO> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<IndicatorDTO> indicators) {
        this.indicators = indicators;
    }

    @Override
    public String toString() {
        return "ShipmentDTO{" +
                "trackingNumber='" + trackingNumber + '\'' +
                ", indicators=" + indicators +
                '}';
    }
}
