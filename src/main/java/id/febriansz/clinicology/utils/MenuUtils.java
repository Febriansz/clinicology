/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.febriansz.clinicology.utils;

import java.awt.Insets;
import javax.swing.JButton;

/**
 *
 * @author febriansz
 */
public class MenuUtils {

    public static JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setMargin(new Insets(0, 10, 0, 10));

        return button;
    }
}
