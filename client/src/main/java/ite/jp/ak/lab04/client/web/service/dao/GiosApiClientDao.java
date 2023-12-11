package ite.jp.ak.lab04.client.web.service.dao;

import ite.jp.ak.lab04.client.model.aqindex.AirQualityindex;
import ite.jp.ak.lab04.client.model.data.SensorData;
import ite.jp.ak.lab04.client.model.sensor.Sensor;
import ite.jp.ak.lab04.client.model.station.Station;

import java.util.List;

public interface GiosApiClientDao {
    List<Station> findAllStations();
    List<Sensor> findSensorsByStationId(Integer stationId);
    SensorData getDataBySensorId(Integer sensorId);
    AirQualityindex getIndexByStationId(Integer stationId);

}
