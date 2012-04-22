package ld23.art;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Class to combine multiple Drawables into one (i.e. to draw them on top of each
 * other). This makes handling animations easier in some cases, even though this
 * might be overkill ;-)
 * 
 * @author tobi
 * @date 22.4.2012
 */

public class MultiDrawable implements Drawable{

    //lazy to type the full names here :-P
    public static final BufferedImage SPRITES = Sprite.SPRITES;
    public static final MultiDrawable FORK = new MultiDrawable(new Sprite[]{Sprite.FORK,Sprite.BLOOD},
                                                               new int[]{0,0},
                                                               new int[]{0,0},
                                                               new boolean[]{true,true});
    
    private static final MultiDrawable HEROIDLE_0 = new MultiDrawable(new Drawable[]{new Sprite(SPRITES.getSubimage(0,0,16,32)),FORK},
                                                                      new int[]{0,8},
                                                                      new int[]{0,14},
                                                                      new boolean[]{true,true});
    
    private static final MultiDrawable HEROIDLE_1 = new MultiDrawable(new Drawable[]{new Sprite(SPRITES.getSubimage(16,0,16,32)),FORK},
                                                                      new int[]{0,8},
                                                                      new int[]{0,15},
                                                                      new boolean[]{true,true});
    
    public static final MultiDrawable[] HEROIDLE = {HEROIDLE_0,HEROIDLE_1};
    
    private static final MultiDrawable HEROMOVE_1 = new MultiDrawable(new Drawable[]{new Sprite(SPRITES.getSubimage(32,0,16,32)),FORK},
                                                                      new int[]{0,6},
                                                                      new int[]{0,14},
                                                                      new boolean[]{true,true});
    
    public static final MultiDrawable[] HEROMOVE = {HEROIDLE_0,HEROMOVE_1};
    
    private static final MultiDrawable HEROSTAB_1 = new MultiDrawable(new Drawable[]{new Sprite(SPRITES.getSubimage(64,0,16,32)),FORK},
                                                                      new int[]{0,10},
                                                                      new int[]{0,13},
                                                                      new boolean[]{true,true});    
    
    private static final MultiDrawable HEROSTAB_2 = new MultiDrawable(new Drawable[]{new Sprite(SPRITES.getSubimage(80,0,16,32)),FORK},
                                                                      new int[]{0,12},
                                                                      new int[]{0,12},
                                                                      new boolean[]{true,true});
    
    public static final MultiDrawable[] HEROSTAB = {HEROIDLE_0,HEROSTAB_1,HEROSTAB_2,HEROIDLE_1,HEROIDLE_0};
    
    private Drawable[] drawables;
    private int[] xOffsets;
    private int[] yOffsets;
    private boolean[] draw;
    int width, height;
    int maxWidth, maxHeight;//used to mirror...
    
    public MultiDrawable(Drawable[] drawables, int[] xOffsets, int[] yOffsets, boolean[] draw){
        this.drawables = drawables;
        this.xOffsets = xOffsets;
        this.yOffsets = yOffsets;
        this.draw = draw;
        width = height = maxWidth = maxHeight = 0;
        int current_width, current_height;
        for(int i=0;i<draw.length;i++){
            current_width = drawables[i].getWidth();
            current_height = drawables[i].getHeight();
            if(current_width > maxWidth)
                maxWidth = current_width;
            if(current_height > maxHeight)
                maxHeight = current_height;
            current_width+=xOffsets[i];
            current_height+=yOffsets[i];
            if(current_width > width)
                width = current_width;
            if(current_height > height)
                height = current_height;}}

    @Override
    public void draw(int px, int py, int w, int h, int faces, Graphics2D g){
        BufferedImage img = new BufferedImage(this.width,this.height,BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG = img.createGraphics();
        for(int i=0;i<drawables.length;i++){
            if(draw[i]){
                drawables[i].draw(xOffsets[i],yOffsets[i],1,1,0,tempG);}}
        tempG.dispose();
        g.drawImage(img,px+w*maxWidth*faces,py,
                //px+w*width*(1-faces)-faces*w*(width-maxWidth),
                px + w*(width*(1-2*faces) + faces*maxWidth),
                
                py+h*height,
                0,0,width,height,null);}
    
    public void setDraw(int i, boolean b){draw[i]=b;}
    
    @Override
    public int getWidth(){return width;}

    @Override
    public int getHeight(){return height;}
    
    

}//end of class MultiDrawable
