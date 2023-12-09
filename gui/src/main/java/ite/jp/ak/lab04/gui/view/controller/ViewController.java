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
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

public class ViewController {

    private final GiosApiClientRepository apiClientRepository = GiosApiClientRepositoryImpl.getInstance();

    private List<Sensor> sensorsForSelectedStation;
    private final List<SensorData> sensorsDataForSelectedStation = new ArrayList<>();
    private final Map<String, SensorData> sensorDataMapForSelectedStation = new HashMap<>();
    private AirQualityindex indexForSelectedStation;

    // Tabela stacji pomiarowych
    @FXML protected TableView<Station> stationTableView;
    @FXML protected TableColumn<Station, Integer> stationIdTableColumn;
    @FXML protected TableColumn<Station, String> stationNameTableColumn;

    // Indeks jakości powietrza
    @FXML protected Label aqindexTitleLabel;
    @FXML protected GridPane aqindexGridPane;

    // Wykres danych w czasie
    @FXML protected ScatterChart<String, Number> scatterChartForParam;

    // Wybór parametru
    @FXML protected ComboBox<String> paramNameComboBox;

    public void initialize() {
        stationIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        stationNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        scatterChartForParam.setAnimated(false);
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
        Station selectedStation = stationTableView.getSelectionModel().getSelectedItem();
        scatterChartForParam.getData().clear();
        scatterChartForParam.setTitle(null);
        paramNameComboBox.getItems().clear();
        if (selectedStation != null) {
            getDataForStation(selectedStation);
            fillComboBoxWithParamNames();
            createAirQualityIndex(selectedStation);
        }
    }

    public void createAirQualityIndex(Station station) {
        aqindexGridPane.getChildren().clear();
        aqindexTitleLabel.setText("Indeks jakości powietrza (" + indexForSelectedStation.getStSourceDataDate() + ")");
        int position = 0;
        for (var sensor : sensorsForSelectedStation) {
            String param = sensor.getParam().getParamCode();
            String sourceDataDate = indexForSelectedStation.getSourceDataDateForParam(param);
            IndexLevel indexLevel = indexForSelectedStation.getIndexLevelForParam(param);
            if (sourceDataDate == null || indexLevel == null) {
                continue;
            }
            System.out.println("Param: " + param + ", sourceDataDate: " + sourceDataDate + ", indexLevel: " + indexLevel);
            if (indexLevel.getId() == -1) {
                continue;
            }
            var color = getColorForIndexLevel(indexLevel);
            var paramNameLabel = new Label(sensor.getParam().getParamName());
            paramNameLabel.setFont(new Font(18));

            var paramIndexLevelNameLabel = new Label(indexLevel.getIndexLevelName());
            paramIndexLevelNameLabel.setFont(new Font(18));
            paramIndexLevelNameLabel.setTextFill(Color.web(color));

            aqindexGridPane.add(paramNameLabel, 0, position);
            aqindexGridPane.add(paramIndexLevelNameLabel, 1, position);

            position++;
        }
    }

    public void getDataForStation(Station station) {
        sensorsForSelectedStation = null;
        indexForSelectedStation = null;
        sensorDataMapForSelectedStation.clear();
        this.sensorsForSelectedStation = apiClientRepository.findSensorsByStationId(station.getId());
        this.indexForSelectedStation = apiClientRepository.getIndexByStationId(station.getId());
        this.sensorsDataForSelectedStation.clear();
        for (var sensor : sensorsForSelectedStation) {
            var sensorData = apiClientRepository.getDataBySensorId(sensor.getId());
            if (sensorData != null) {
                sensorsDataForSelectedStation.add(sensorData);
                sensorDataMapForSelectedStation.put(sensor.getParam().getParamCode(), sensorData);
            }
        }
    }

    public void onSelectedParamName(ActionEvent event) {
        String param = paramNameComboBox.getSelectionModel().getSelectedItem();
        if (param == null) {
            return;
        }
        createDataInTimeScatterChart(param);
    }

    public void fillComboBoxWithParamNames() {
        paramNameComboBox.getItems().clear();
        for (var paramName : sensorDataMapForSelectedStation.keySet()) {
            paramNameComboBox.getItems().add(paramName);
        }
    }

    public void createDataInTimeScatterChart(String param) {
        scatterChartForParam.getData().clear();
        var sensorData = sensorDataMapForSelectedStation.get(param);
        String key = sensorData.getKey();
        scatterChartForParam.setTitle("Dane w czasie dla " + key);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Dane pomiarowe parametru " + key);
        for (var i = sensorData.getValues().length - 1; i >= 0; i--) {
            var value = sensorData.getValues()[i];
            if (value.getValue() != null) {
                series.getData().add(new XYChart.Data<>(value.getDate(), value.getValue()));
            }
        }
        scatterChartForParam.getData().add(series);
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