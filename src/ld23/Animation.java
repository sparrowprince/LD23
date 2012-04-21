package ld23;

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
    
    public void draw(int px, int py,Graphics2D g){
        g.drawImage(frames[pos],px,py,32,64,null);
        counter=(counter+1)%delays[pos];
        if(counter==0)
            pos=(pos+1)%delays.length;}

    public void drawLeft(int px, int py,Graphics2D g){
        g.drawImage(frames[pos],px+32,py,px,py+64,0,0,16,32,null);
        counter=(counter+1)%delays[pos];
        if(counter==0)
            pos=(pos+1)%delays.length;}
}
