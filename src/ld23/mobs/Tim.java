package ld23.mobs;


import java.awt.Graphics2D;

import ld23.art.Animation;

public class Tim extends Mob{
    

    private boolean isShooting,isStabbing,laserAnim = false;
    
    private int sCounter,lCounter;
    
    private static final int SHOOTTIME = 100;
    private static final int STABTIME = 35;
    public static final int LCOOLDOWN = 500;
        
    public Tim(int px, int py, int width, int height, int boundsX, int boundsY){
        super(px, py, width, height, boundsX, boundsY);
        lCounter = sCounter = 0;}
        
    public void shoot(){
        if(!isShooting && !isStabbing && lCounter==0){
            isShooting=true;
            lCounter=LCOOLDOWN;}}
    
    public void stab(){
        if(!isStabbing && !isShooting)
            isStabbing=true;}
    
    public void draw(int offX, int offY, Graphics2D g){
        if(isStabbing)
            Animation.HEROSTAB.draw(getPosX()+offX, getPosY()+offY, 2, 2, faces, g);
        else if(isMoving)
            Animation.HEROMOVE.draw(getPosX()+offX, getPosY()+offY, 2, 2, faces, g);
        else
            Animation.HEROIDLE.draw(getPosX()+offX, getPosY()+offY, 2, 2, faces, g);
        if(isShooting)
            Animation.HEROGLOW.draw(getPosX()+offX, getPosY()+offY, 2, 2, faces, g);
        if(laserAnim){
            Animation.HEROLASER.draw(getPosX()+18+offX,
                                     getPosY()+8+offY,
                                     (1-faces)*(boundsX-getPosX()+10) + faces*(getPosX()+18+10),
                                     5, faces, g);}}
    
    public void update(int nextHalt){
        super.update(nextHalt);
        updateShooting();
        updateStabbing();}
    
    public boolean firesLaser(){return laserAnim;}
    public int coolDown(){return lCounter;}
    
    private void updateShooting(){
        if(isShooting){
            sCounter=++sCounter%SHOOTTIME;
            if(sCounter==0)
                isShooting=false;
            else if(sCounter>=30 && sCounter<=70)
                laserAnim=true;
            else
                laserAnim=false;}
        if(lCounter>0) lCounter--;}
    
    private void updateStabbing(){
        if(isStabbing){
            sCounter=++sCounter%STABTIME;
            if(sCounter==0)
                isStabbing=false;}}

    
    

}
