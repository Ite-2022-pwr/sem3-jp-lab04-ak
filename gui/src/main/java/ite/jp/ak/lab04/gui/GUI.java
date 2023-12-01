package ite.jp.ak.lab04.gui;

import ite.jp.ak.lab04.client.Client;

import javax.swing.*;

public class GUI {
    public static void main(String[] args) {
        Client c;
        JFrame frame = new JFrame("Hello");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Hej, jestem interfejsem graficznym!");
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
        System.out.println("Hej, jestem interfejsem graficznym!");
    }
}
