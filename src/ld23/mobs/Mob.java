package ld23.mobs;


import java.awt.Graphics2D;

import ld23.art.Animation;

public class Mob {
    
    private int px,py; //position of the top-left pixel of this mob
    private int width,height;
    private final int boundsX, boundsY; //the max values for x and y
    
    private boolean isMoving,isFalling,isDead,isShooting,laserAnim = false;
    private int faces = 0;//i.e. faces right by default
    
    private int gCounter,sCounter;  
    private static final int SHOOTTIME = 100;
        
    public Mob(int px, int py, int width, int height, int boundsX, int boundsY){
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;
        this.boundsX = boundsX;
        this.boundsY = boundsY-65;
        gCounter = sCounter = 0;}
    
    public int getPosX(){return px;}
    public int getPosY(){return py;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    
    public void moveHorizontally(int offset){
        isMoving=true;
        if(offset<0) faces=1;
        else faces=0;
        px+=offset;
        while(px<0) px+=boundsX;
        px%=boundsX;}
    
    public void moveVertically(int offset){
        py+=offset;
        while(py<0) py+=boundsY;
        py%=boundsY;}
    
    public void stayIdle(){
        isMoving = false;}
    
    public void shoot(){
        if(!isShooting) isShooting=true;}
    
    public void draw(int offX, int offY, Graphics2D g){
        if(isMoving)
            Animation.HEROMOVE.draw(px+offX, py+offY, width, height, faces, g);
        else
            Animation.HEROIDLE.draw(px+offX, py+offY, width, height, faces, g);
        if(isShooting)
            Animation.HEROGLOW.draw(px+offX, py+offY, width, height, faces, g);
        if(laserAnim){
            Animation.HEROLASER.draw(px+18+offX,
                                     py+8+offY,
                                     (1-faces)*(boundsX-px+10) + faces*(px+18+10),
                                     5, faces, g);}}
    
    public void update(){
        updateGravitation();
        updateShooting();
        if(py>boundsY) isDead = true;}
    
    public boolean isDead(){return isDead;}
    public boolean firesLaser(){return laserAnim;}
    
    public void setFalling(boolean b){
        isFalling = b;}
    
    private void updateGravitation(){
        if(isFalling){
            double velo = (++gCounter/100.0)*9.81;
            //System.out.println(""+velo);
            if(velo > 50) velo=50;
            py+=(velo*gCounter/10.0)/2;}
        else
            gCounter=0;}
    
    private void updateShooting(){
        if(isShooting){
            sCounter=++sCounter%SHOOTTIME;
            if(sCounter==0)
                isShooting=false;
            else if(sCounter>=30 && sCounter<=70)
                laserAnim=true;
            else
                laserAnim=false;}}
    
    

}
