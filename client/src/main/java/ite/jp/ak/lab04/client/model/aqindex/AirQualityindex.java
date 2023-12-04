package ite.jp.ak.lab04.client.model.aqindex;

import ite.jp.ak.lab04.client.model.ModelBase;
import lombok.Data;

@Data
public class AirQualityindex extends ModelBase {

    // ST
    private String stCalcDate;
    private IndexLevel stIndexLevel;
    private String stSourceDataDate;

    private Boolean stIndexStatus;
    private String stIndexCrParam;

    // SO2
    private String so2CalcDate;
    private IndexLevel so2IndexLevel;
    private String so2SourceDataDate;

    // NO2
    private String no2CalcDate;
    private IndexLevel no2IndexLevel;
    private String no2SourceDataDate;

    // PM10
    private String pm10CalcDate;
    private IndexLevel pm10IndexLevel;
    private String pm10SourceDataDate;

    // PM25
    private String pm25CalcDate;
    private IndexLevel pm25IndexLevel;
    private String pm25SourceDataDate;

    // O3
    private String o3CalcDate;
    private IndexLevel o3IndexLevel;
    private String o3SourceDataDate;

    public IndexLevel getIndexLevelForParam(String param) {
        return switch (param) {
            case "SO2" -> so2IndexLevel;
            case "NO2" -> no2IndexLevel;
            case "PM10" -> pm10IndexLevel;
            case "PM2.5" -> pm25IndexLevel;
            case "O3" -> o3IndexLevel;
            default -> null;
        };
    }

    public String getSourceDataDateForParam(String param) {
        return switch (param) {
            case "SO2" -> so2SourceDataDate;
            case "NO2" -> no2SourceDataDate;
            case "PM10" -> pm10SourceDataDate;
            case "PM2.5" -> pm25SourceDataDate;
            case "O3" -> o3SourceDataDate;
            default -> null;
        };
    }
}
