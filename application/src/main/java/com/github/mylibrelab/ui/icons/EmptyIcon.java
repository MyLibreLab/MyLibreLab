package com.github.mylibrelab.ui.icons;

import javax.swing.*;
import java.awt.*;

/**
 * Simple replacement for darklaf EmptyIcon
 */
public class EmptyIcon implements Icon {
    private final int size;

    public EmptyIcon(int size) {
        this.size = size;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        // Empty implementation - draws nothing
    }

    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }
}