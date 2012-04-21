package ld23.art;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Animation {

    private final int[] delays;
    private final BufferedImage[] frames;
    
    private int counter, pos;
    
    public Animation(int[] delays, BufferedImage[] frames){
        this.delays = delays;
        this.frames = frames;
        counter = pos = 0;}
    
    public void draw(int px, int py, int faces, Graphics2D g){
        g.drawImage(frames[pos],px+32*faces,py,px+32*(1-faces),py+64,0,0,16,32,null);
        counter=(counter+1)%delays[pos];
        if(counter==0)
            pos=(pos+1)%delays.length;}
}
