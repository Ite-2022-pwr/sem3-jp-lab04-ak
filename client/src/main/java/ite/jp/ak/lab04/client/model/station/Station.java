package ite.jp.ak.lab04.client.model.station;

import ite.jp.ak.lab04.client.model.ModelBase;
import lombok.Data;

@Data
public class Station extends ModelBase {

    private String stationName;
    private String gegrLat;
    private String gegrLon;
    private City city;
    private String addressStreet;

}
