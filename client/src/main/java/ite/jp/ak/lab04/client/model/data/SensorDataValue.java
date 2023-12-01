package ite.jp.ak.lab04.client.model.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SensorDataValue {
    private LocalDateTime date;
    private Double value;
}
