package ite.jp.ak.lab04.client.model.sensor;

import ite.jp.ak.lab04.client.model.ModelBase;
import lombok.Data;

@Data
public class Sensor extends ModelBase {

    private Integer stationId;
    private Param param;
}
