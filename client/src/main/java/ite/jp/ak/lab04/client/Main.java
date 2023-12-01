package ite.jp.ak.lab04.client;

import ite.jp.ak.lab04.client.model.station.Station;
import ite.jp.ak.lab04.client.web.api.ApiClient;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        ApiClient apiClient = ApiClient.getInstance();

        Station[] stations = apiClient.get("/station/findAll", Station[].class);

        System.out.println(Arrays.toString(stations));
        System.out.println(stations[0].getId());
        Station station = new Station();
        station.setStationName("Test");
    }
}
