package ite.jp.ak.lab04.client;

import ite.jp.ak.lab04.client.model.aqindex.AirQualityindex;
import ite.jp.ak.lab04.client.model.data.SensorData;
import ite.jp.ak.lab04.client.model.sensor.Sensor;
import ite.jp.ak.lab04.client.model.station.Station;
import ite.jp.ak.lab04.client.web.service.impl.GiosApiClientRepositoryImpl;
import ite.jp.ak.lab04.client.web.service.repository.GiosApiClientRepository;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }


        GiosApiClientRepository repository = GiosApiClientRepositoryImpl.getInstance();

        List<Station> stations = repository.findAllStations();

        System.out.println(stations);
        System.out.println();

        List<Sensor> sensors = repository.findSensorsByStationId(stations.get(0).getId());

        System.out.println(sensors);
        System.out.println();

        SensorData data = repository.getDataBySensorId(sensors.get(0).getId());

        System.out.println(data);
        System.out.println();

        AirQualityindex aqindex = repository.getIndexByStationId(stations.get(0).getId());
        System.out.println(aqindex);
        System.out.println();

        var tmp = repository.getDataBySensorId(213769);
        System.out.println("TMP = " + tmp);
    }
}
