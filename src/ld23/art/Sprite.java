package ld23.art;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.RGBImageFilter;

import javax.swing.ImageIcon;

public class Sprite implements Drawable{    

    private static final String IMGURL = "/hero.png"; 
    public static final BufferedImage SPRITES = getBufferedImage(IMGURL);
    
    private static final Sprite HEROGLOW_0 = new Sprite(SPRITES.getSubimage(48,0,16,8));
    private static final Sprite HEROGLOW_1 = new Sprite(SPRITES.getSubimage(48,8,16,8));
    private static final Sprite HEROGLOW_2 = new Sprite(SPRITES.getSubimage(48,16,16,8));
    private static final Sprite HEROGLOW_3 = new Sprite(SPRITES.getSubimage(48,24,16,8));    
    public static final Sprite[] HEROGLOW = {HEROGLOW_0,HEROGLOW_1,HEROGLOW_2,HEROGLOW_3,HEROGLOW_2,HEROGLOW_1,HEROGLOW_0};
    
    public static final Sprite FORK = new Sprite(SPRITES.getSubimage(96,0,16,3));
    public static final Sprite BLOOD = new Sprite(SPRITES.getSubimage(96,3,16,3));

    public static final BufferedImage[] ALPH = getAlphabet();
    
    public static BufferedImage[] getAlphabet(){
        BufferedImage[] ALPH = new BufferedImage[26];
        for(int i=0;i<26;i++)
            ALPH[i] = SPRITES.getSubimage((i%8)*16,32+(i/8)*16,16,16);
        return ALPH;}    
    
    public static Sprite printText(String text, Color c, Color h){
        BufferedImage temp = new BufferedImage(text.length()*16,16,BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG = temp.createGraphics();

        
        int pos;
        for(int i=0;i<text.length();i++){
            pos = text.charAt(i)-'a';
            if(text.charAt(i)!=' ') tempG.drawImage(ALPH[pos],i*16,0,null);}
        
        tempG.dispose();
        return new Sprite(temp);}
    
    private final BufferedImage img;
    
    /**
     * Static method to open the Image specified at url and return it as a BufferedImage.
     * @param url
     */
    public static BufferedImage getBufferedImage(String url){
        Image img = new ImageIcon(new Sprite(null).getClass().getResource(url)).getImage();
        BufferedImage bfImg = new BufferedImage(img.getWidth(null),
                                               img.getHeight(null),
                                               BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bfImg.createGraphics();
        g.drawImage(img,0,0,null);
        g.dispose();
        return bfImg;}
    
    public Sprite(BufferedImage img){
        this.img = img;}
    
    @Override
    public int getWidth(){
        return img.getWidth();}

    @Override
    public int getHeight(){
        return img.getHeight();}
    
    @Override
    public void draw(int px, int py, int w, int h, int faces, Graphics2D g){
        g.drawImage(img,px+w*img.getWidth()*faces,py,
                        px+w*img.getWidth()*(1-faces),py+h*img.getHeight(),
                        0,0,img.getWidth(),img.getHeight(),null);}
    
}//end of class Artwork
