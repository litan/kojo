package net.kogics.kojo.util;

import java.awt.Graphics2D;

import javax.swing.JComponent;

import com.sun.java.swing.Painter;

public class NoOpPainter implements Painter<JComponent>{

    public void paint(Graphics2D g, JComponent object, int width, int height) {
    }

}
