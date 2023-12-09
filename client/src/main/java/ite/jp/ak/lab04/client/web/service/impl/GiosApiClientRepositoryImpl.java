package ite.jp.ak.lab04.client.web.service.impl;

import ite.jp.ak.lab04.client.model.aqindex.AirQualityindex;
import ite.jp.ak.lab04.client.model.data.SensorData;
import ite.jp.ak.lab04.client.model.sensor.Sensor;
import ite.jp.ak.lab04.client.model.station.Station;
import ite.jp.ak.lab04.client.web.api.ApiClient;
import ite.jp.ak.lab04.client.web.service.repository.GiosApiClientRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GiosApiClientRepositoryImpl implements GiosApiClientRepository {
    private final ApiClient apiClient = ApiClient.getInstance();

    private static GiosApiClientRepositoryImpl instance;

    private GiosApiClientRepositoryImpl() {

    }

    public static GiosApiClientRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new GiosApiClientRepositoryImpl();
        }
        return instance;
    }

    @Override
    public List<Station> findAllStations() {
        Station[] stations = apiClient.get("/station/findAll", Station[].class);
        return stations != null ? List.of(stations) : null;
    }

    @Override
    public List<Sensor> findSensorsByStationId(Integer stationId) {
        Sensor[] sensors = apiClient.get("/station/sensors/" + stationId, Sensor[].class);
        return sensors != null ? List.of(sensors) : null;
    }

    @Override
    public SensorData getDataBySensorId(Integer sensorId) {
        return apiClient.get("/data/getData/" + sensorId, SensorData.class);
    }

    @Override
    public AirQualityindex getIndexByStationId(Integer stationId) {
        return apiClient.get("/aqindex/getIndex/" + stationId, AirQualityindex.class);
    }
}
