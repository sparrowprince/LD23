package ld23.art;

import java.awt.Graphics2D;

public interface Drawable {
    
    public void draw(int px, int py, int width, int height, int faces, Graphics2D g);
    
    public int getWidth();
    
    public int getHeight();

}
