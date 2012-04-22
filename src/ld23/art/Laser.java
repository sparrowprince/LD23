package ld23.art;

import java.awt.Color;
import java.awt.Graphics2D;

public class Laser implements Drawable{
    
    private final static Color LIGHTRED = new Color(0xffaaaa);
    
    private final static Laser LASER_0 = new Laser(new int[]{20},
                                                  new Color[]{Color.RED});
    private final static Laser LASER_1 = new Laser(new int[]{60,20},
                                                  new Color[]{Color.RED,LIGHTRED});
    private final static Laser LASER_2 = new Laser(new int[]{100,60,20},
                                                  new Color[]{Color.RED,LIGHTRED,Color.WHITE});    
    public static final Laser[] LASER = {LASER_0,LASER_1,LASER_2,LASER_1,LASER_0};
    
    private int[] weights;
    private Color[] colors;
    
    public Laser(int[] weights,Color[] colors){
        this.weights=weights;
        this.colors=colors;}

    @Override
    public void draw(int px, int py, int width, int height, int faces, Graphics2D g){
        int current_height,current_py;
        for(int i=0;i<weights.length;i++){
            current_height = (height*weights[i])/100;
            current_py = py+(height-current_height)/2;
            g.setColor(colors[i]);
            g.fillRect(px-width*faces,current_py,width,current_height);}}

    @Override
    public int getWidth(){return 1;}

    @Override
    public int getHeight(){return 1;}

}//end of class Laser
