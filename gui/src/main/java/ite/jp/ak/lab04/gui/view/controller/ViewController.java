package ite.jp.ak.lab04.gui.view.controller;

import ite.jp.ak.lab04.client.model.aqindex.AirQualityindex;
import ite.jp.ak.lab04.client.model.aqindex.IndexLevel;
import ite.jp.ak.lab04.client.model.data.SensorData;
import ite.jp.ak.lab04.client.model.data.SensorDataValue;
import ite.jp.ak.lab04.client.model.sensor.Sensor;
import ite.jp.ak.lab04.client.model.station.Station;
import ite.jp.ak.lab04.client.web.service.impl.GiosApiClientRepositoryImpl;
import ite.jp.ak.lab04.client.web.service.repository.GiosApiClientRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ViewController {

    private final GiosApiClientRepository apiClientRepository = GiosApiClientRepositoryImpl.getInstance();

    private Station selectedStation;
    private List<Sensor> sensorsForSelectedStation;
    private List<SensorData> sensorsDataForSelectedStation = new ArrayList<>();
    private AirQualityindex indexForSelectedStation;

    // Tabela stacji pomiarowych
    @FXML protected TableView<Station> stationTableView;
    @FXML protected TableColumn<Station, Integer> stationIdTableColumn;
    @FXML protected TableColumn<Station, String> stationNameTableColumn;

    // Wykres indeksu jakości powietrza
    @FXML protected BarChart<String, Double> aqindexBarChart;

    // Zakładki dla wykresów danych w czasie
    @FXML protected TabPane dataTabPane;

    public void initialize() {
        stationIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        stationNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        aqindexBarChart.setAnimated(false);
    }

    public void refresh(ActionEvent event) {
        List<Station> stations = apiClientRepository.findAllStations().stream().sorted(Comparator.comparing(Station::getStationName)).toList();
        fillStationTableView(stations);
    }

    public void fillStationTableView(List<Station> stations) {
        stationTableView.getItems().clear();
        ObservableList<Station> data = FXCollections.observableArrayList(stations);
        stationTableView.setItems(data);
    }

    public void onSelectedStation(MouseEvent event) {
        this.selectedStation = stationTableView.getSelectionModel().getSelectedItem();
        aqindexBarChart.getData().clear();
        if (this.selectedStation != null) {
            getDataForStation(selectedStation);
            createIndexBarChartForStation(selectedStation);
            createDataInTimeScatterCharts();
        }
    }

    public void getDataForStation(Station station) {
        this.sensorsForSelectedStation = apiClientRepository.findSensorsByStationId(station.getId());
        this.indexForSelectedStation = apiClientRepository.getIndexByStationId(station.getId());
        this.sensorsDataForSelectedStation.clear();
        for (var sensor : sensorsForSelectedStation) {
            var sensorData = apiClientRepository.getDataBySensorId(sensor.getId());
            if (sensorData != null) {
                sensorsDataForSelectedStation.add(sensorData);
            }
        }
    }

    public void createDataInTimeScatterCharts() {
        dataTabPane.getTabs().clear();
        for (var sensorData : this.sensorsDataForSelectedStation) {
            String key = sensorData.getKey();
            CategoryAxis xAxis = new CategoryAxis();    // data
            NumberAxis yAxis = new NumberAxis();        // wartość
            ScatterChart<String, Number> chart = new ScatterChart<>(xAxis, yAxis);
            chart.setTitle("Dane w czasie dla " + key);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Dane pomiarowe parametru " + key);
            for (var i = sensorData.getValues().length - 1; i >= 0; i--) {
                var value = sensorData.getValues()[i];
                if (value.getValue() != null) {
                    series.getData().add(new XYChart.Data<>(value.getDate(), value.getValue()));
                }
            }
            chart.getData().add(series);
            Tab newTab = new Tab(key, chart);
            dataTabPane.getTabs().add(newTab);
        }
    }

    public void createIndexBarChartForStation(Station station) {
        aqindexBarChart.getData().clear();

        XYChart.Series<String, Double> series = new XYChart.Series<>();
        aqindexBarChart.getData().add(series);

        aqindexBarChart.setTitle("Indeks jakości powietrza dla stacji " + station.getStationName() + "\nw dniu " + indexForSelectedStation.getStCalcDate());

        // "SO2", "NO2", "PM10", "PM25", "O3"
        String[] params = {"SO2", "NO2", "PM10", "PM25", "O3"};

        for (var param : params) {
            String sourceDataDate = indexForSelectedStation.getSourceDataDateForParam(param);
            IndexLevel indexLevel = indexForSelectedStation.getIndexLevelForParam(param);
            System.out.println("Param: " + param + ", sourceDataDate: " + sourceDataDate + ", indexLevel: " + indexLevel);
            SensorDataValue sensorDataValue = getSensorDataValueByParamAndSourceDataDate(param, sourceDataDate);
            System.out.println("sensorDataValue: " + sensorDataValue);
            if (sensorDataValue == null) {
                continue;
            }

            XYChart.Data<String, Double> data = new XYChart.Data<>(param + "\n" + indexLevel.getIndexLevelName(), sensorDataValue.getValue());
            series.getData().add(data);

            String color = getColorForIndexLevel(indexLevel);

            // https://stackoverflow.com/questions/14158104/javafx-barchart-bar-color
            for (Node node : data.getNode().lookupAll(".default-color0.chart-bar")) {
                node.setStyle("-fx-bar-fill: " + color);
            }
        }


    }

    private static String getColorForIndexLevel(IndexLevel indexLevel) {
        switch (indexLevel.getId()) {
            case -1 -> {
                return "#ffffff";  // brak indeksu
            }
            case 0 -> {
                return "#14db21";  // Bardzo dobry
            }
            case 1 -> {
                return "#81db14";  // Dobry
            }
            case 2 -> {
                return "#badb14";  // Umiarkowany
            }
            case 3 -> {
                return "#dbd114";  // Dostateczny
            }
            case 4 -> {
                return "#db8b14";  // Zły
            }
            case 5 -> {
                return "#db2814";  // Bardzo zły
            }
            default -> throw new IllegalArgumentException("Niedozwolona wartość: " + indexLevel.getId());
        }
    }

    public SensorDataValue getSensorDataValueByParamAndSourceDataDate(String param, String date) {
        for (var sensorData : this.sensorsDataForSelectedStation) {
            if (sensorData.getKey().equals(param)) {
                for (var value : sensorData.getValues()) {
                    if (value.getDate().equals(date)) {
                        System.out.println("Found value: " + value + " for key: " + param);
                        return value;
                    }
                }
            }
        }
        return null;
    }

}