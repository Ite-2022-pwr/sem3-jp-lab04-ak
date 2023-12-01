module ite.jp.ak.lab04.client {
    requires spring.web;
    requires spring.webflux;
    requires reactor.core;
    requires lombok;

    exports ite.jp.ak.lab04.client;
    exports ite.jp.ak.lab04.client.config;
//    exports ite.jp.ak.lab04.client.web.service;
    exports ite.jp.ak.lab04.client.web.api;
    exports ite.jp.ak.lab04.client.model;
    exports ite.jp.ak.lab04.client.model.station;
}