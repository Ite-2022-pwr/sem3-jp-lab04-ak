module ite.jp.ak.lab04.client {
    requires spring.web;
    requires spring.webflux;
    requires reactor.core;
    requires lombok;

    exports ite.jp.ak.lab04.client.config;
    exports ite.jp.ak.lab04.client.web.api;
    exports ite.jp.ak.lab04.client.model;
    exports ite.jp.ak.lab04.client.model.station;
    exports ite.jp.ak.lab04.client.model.sensor;
    exports ite.jp.ak.lab04.client.model.data;
    exports ite.jp.ak.lab04.client.model.aqindex;
    exports ite.jp.ak.lab04.client.web.service.repository;
    exports ite.jp.ak.lab04.client.web.service.impl;
}