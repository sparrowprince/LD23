package ld23.art;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Animation {
    
    public static final Animation HEROMOVE = new Animation(new int[]{15,15},Sprite.HEROMOVE);
    public static final Animation HEROIDLE = new Animation(new int[]{15,15},Sprite.HEROIDLE);
    public static final Animation HEROGLOW = new Animation(new int[]{10,10,10,40,10,10,10},Sprite.HEROGLOW);

    public static final Animation HEROLASER = new Animation(new int[]{5,5,20,5,5},Laser.LASER);
    
    private final int[] delays;
    private final Drawable[] frames;
    
    private int counter, pos;
    
    public Animation(int[] delays, Drawable[] frames){
        this.delays = delays;
        this.frames = frames;
        counter = pos = 0;}
    
    public void draw(int px, int py, int width, int height, int faces, Graphics2D g){
        frames[pos].draw(px, py, width, height, faces, g);
        counter=(counter+1)%delays[pos];
        if(counter==0)
            pos=(pos+1)%delays.length;}
}
