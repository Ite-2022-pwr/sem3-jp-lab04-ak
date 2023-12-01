package ite.jp.ak.lab04.client.model.data;

import lombok.Data;

@Data
public class SensorData {
    private String key;
    private SensorDataValue[] values;
}
