package ld23.mobs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import ld23.Animation;

public class Mob {
    
    private int px,py; //position of the top-left pixel of this mob
    private final int boundsX, boundsY; //the max values for x and y
    
    private boolean isMoving,isFalling,isDead = false;
    private int gCounter = 0;
    
    private Animation move, idle;
    
    private final java.net.URL imgURL = getClass().getResource("/ld23/hero.png");
    private BufferedImage heroSprites;
        
    private final BufferedImage hero_idle;
    private final BufferedImage hero_idle2;
    private final BufferedImage hero_mov;
    private final Image img;
    
    public Mob(int px, int py, int boundsX, int boundsY){
        this.px = px;
        this.py = py;
        this.boundsX = boundsX;
        this.boundsY = boundsY-65;
        
        img = new ImageIcon(imgURL).getImage();
        heroSprites = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = heroSprites.createGraphics();
        g.drawImage(img,0,0,null);g.dispose();
        
        hero_idle = heroSprites.getSubimage(0,0,16,32);
        hero_idle2 = heroSprites.getSubimage(16,0,16,32);
        hero_mov = heroSprites.getSubimage(32,0,16,32);
        move = new Animation(new int[]{15,15},new BufferedImage[]{hero_idle,hero_mov});
        idle = new Animation(new int[]{15,15},new BufferedImage[]{hero_idle,hero_idle2});}
    
    public int getPosX(){return px;}
    public int getPosY(){return py;}
    
    public void moveHorizontally(int offset){
        isMoving=true;
        px+=offset;
        while(px<0) px+=boundsX;
        px%=boundsX;}
    
    public void moveVertically(int offset){
        py+=offset;
        while(py<0) py+=boundsY;
        py%=boundsY;}
    
    public void stayIdle(){
        isMoving = false;}
    
    public void draw(Graphics2D g){
        if(isMoving)
            move.drawLeft(px, py, g);
        else
            idle.draw(px, py, g);}
    
    public void update(){
        updateGravitation();
        if(py>boundsY) isDead = true;}
    
    public boolean isDead(){
        return isDead;}
    
    public void setFalling(boolean b){
        isFalling = b;}
    
    private void updateGravitation(){
        if(isFalling){
            double velo = (++gCounter/100.0)*9.81;
            //System.out.println(""+velo);
            if(velo > 50) velo=50;
            py+=(velo*gCounter/10.0)/2;}
        else
            gCounter=0;
    }
    
    

}
