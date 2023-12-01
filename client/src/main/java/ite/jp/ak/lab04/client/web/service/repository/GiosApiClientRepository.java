package ite.jp.ak.lab04.client.web.service.repository;

import ite.jp.ak.lab04.client.model.aqindex.AirQualityindex;
import ite.jp.ak.lab04.client.model.data.SensorData;
import ite.jp.ak.lab04.client.model.sensor.Sensor;
import ite.jp.ak.lab04.client.model.station.Station;

import java.util.List;

public interface GiosApiClientRepository {
    List<Station> findAllStations();
    List<Sensor> findSensorsByStationId(Integer stationId);
    SensorData getDataBySensorId(Integer sensorId);
    AirQualityindex getIndexByStationId(Integer stationId);

}
