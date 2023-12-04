module ite.jp.ak.lab04.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires ite.jp.ak.lab04.client;

    opens ite.jp.ak.lab04.gui to javafx.fxml;
    exports ite.jp.ak.lab04.gui;
    exports ite.jp.ak.lab04.gui.view.controller;
    opens ite.jp.ak.lab04.gui.view.controller to javafx.fxml;
}