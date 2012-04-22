package ld23.mobs;

import java.awt.Graphics2D;

import ld23.art.Animation;
import ld23.art.Sprite;

public class Hare extends Mob{
    
    private static final int[] MOVEDELAYS = {15,15,15,15};
    private Animation moveAnim;
    
    public Hare(int px, int py, int width, int height, int boundsX, int boundsY){
        super(px, py, width, height, boundsX, boundsY);
        moveAnim = new Animation(Sprite.HAREMOVE,MOVEDELAYS);}
    
    public void draw(int offX, int offY, Graphics2D g){
        if(isMoving)
            moveAnim.draw(getPosX()+offX, getPosY()+offY, 2, 2, 1-faces, g);
        else
            Sprite.HAREIDLE_0.draw(getPosX()+offX, getPosY()+offY, 2, 2, 1-faces, g);}

}
