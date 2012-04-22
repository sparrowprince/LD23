package ld23.mobs;

import java.awt.Graphics2D;

import ld23.art.Animation;

public abstract class Mob {
    
    protected double px,py; //position of the top-left pixel of this mob
    protected int width,height;
    protected final int boundsX, boundsY; //the max values for x and y
    
    protected boolean isMoving,isFalling,isDead;
    
    private int gCounter;
    
    private double jumpSpeed = 0;
    private double velo;
    
    protected int faces = 0;//i.e. faces right by default
    
    
    public Mob(int px, int py, int width, int height, int boundsX, int boundsY){
        this.px = (double)px;
        this.py = (double)py;
        this.width = width;
        this.height = height;
        this.boundsX = boundsX;
        this.boundsY = boundsY-height+1;
        gCounter = 0;}
    
    public abstract void draw(int offX, int offY, Graphics2D g);
    
    public int getPosX(){return (int)px;}
    public int getPosY(){return (int)py;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    
    public boolean isDead(){return isDead;}
    
    public void moveHorizontally(double offset){
        isMoving=true;
        if(offset<0) faces=1;
        else faces=0;
        px+=offset;
        while(px<0) px+=boundsX;
        px%=boundsX;}
    
    public void moveVertically(double offset){
        py+=offset;
        while(py<0) py+=boundsY;
        py%=boundsY;}
    
    public void jump(){
        if(!isFalling)
            velo -= 60;}
    
    public void stayIdle(){
        isMoving = false;}
    
    public void setFalling(boolean b){
        isFalling = b;}
    
    private void updateGravitation(int nextHalt){
        if(nextHalt > py+height || velo<0){
            isFalling = true;
            velo +=(++gCounter/100.0)*9.81;            
            if(velo > 50) velo=50;}
        else{
            isFalling = false;
            gCounter = 0;
            velo = 0;}
        System.out.println(nextHalt+" "+(py+height)+" "+velo);
        py += Math.min((velo*gCounter/200.0), (nextHalt-py-height));}
     
    public void update(int nextHalt){
        updateGravitation(nextHalt);
        if(py>boundsY) isDead = true;}

}
