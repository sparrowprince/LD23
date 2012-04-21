package ld23.art;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Sprite implements Drawable{    

    private static final String IMGURL = "/hero.png"; 
    private static final BufferedImage SPRITES = getBufferedImage(IMGURL);
    
    private static final Sprite HEROIDLE_0 = new Sprite(SPRITES.getSubimage(0,0,16,32));    
    private static final Sprite HEROIDLE_1 = new Sprite(SPRITES.getSubimage(16,0,16,32));
    public static final Sprite[] HEROIDLE = {HEROIDLE_0,HEROIDLE_1};
    
    private static final Sprite HEROMOVE_1 = new Sprite(SPRITES.getSubimage(32,0,16,32));
    public static final Sprite[] HEROMOVE = {HEROIDLE_0,HEROMOVE_1};
    
    private static final Sprite HEROGLOW_0 = new Sprite(SPRITES.getSubimage(48,0,16,8));
    private static final Sprite HEROGLOW_1 = new Sprite(SPRITES.getSubimage(48,8,16,8));
    private static final Sprite HEROGLOW_2 = new Sprite(SPRITES.getSubimage(48,16,16,8));
    private static final Sprite HEROGLOW_3 = new Sprite(SPRITES.getSubimage(48,24,16,8));    
    public static final Sprite[] HEROGLOW = {HEROGLOW_0,HEROGLOW_1,HEROGLOW_2,HEROGLOW_3,HEROGLOW_2,HEROGLOW_1,HEROGLOW_0};

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
    public void draw(int px, int py, int width, int height, int faces, Graphics2D g){
        g.drawImage(img,px+width*faces,py,
                        px+width*(1-faces),py+height,
                        0,0,16,32,null);}}//end of class Artwork
