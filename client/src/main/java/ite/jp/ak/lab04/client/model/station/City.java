package ite.jp.ak.lab04.client.model.station;


import ite.jp.ak.lab04.client.model.ModelBase;
import lombok.Data;

@Data
public class City extends ModelBase {

    private String name;
    private Comune commune;

}
