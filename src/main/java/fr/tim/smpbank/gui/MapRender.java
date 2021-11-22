package fr.tim.smpbank.gui;

import fr.tim.smpbank.smpBank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class MapRender extends MapRenderer {
    private float[] x_scale;
    private float[] y_scale;
    private final float[] X;
    private final float[] Y;
    private float xmin;
    private float dx;
    private float ymin;
    private float dy;
    private float xmax;
    private float ymax;
    private boolean hasRendered;

    public MapRender(float[] x, float[] y) {
        super(false);
        this.hasRendered = false;
        this.X = x.clone();
        this.x_scale = x;
        this.Y = y.clone();
        this.y_scale = y;
        float xmin = x[0];
        float xmax = x[0];
        float ymin = y[0];
        float ymax = y[0];

        for (float X : x) {
            xmin = Math.min(xmin,X);
            xmax = Math.max(xmax,X);
        }

        for (float Y : y) {
            ymin = Math.min(ymin,Y);
            ymax = Math.max(ymax,Y);
        }

        float dx = (xmax - xmin);
        float dy = (ymax - ymin);

        for (int i = 0; i<x.length; i++ ) {
            this.x_scale[i] = (this.X[i] - xmin)/dx;
            this.y_scale[i] = (this.Y[i] - ymin)/dy;
        }

        this.dx = dx;
        this.dy = dy;
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;

    }

    @Override
    public void render(MapView map, MapCanvas canvas, Player p) {

        if (this.hasRendered) return;

        this.hasRendered = true;

        try {
            InputStream is = smpBank.getPlugin().getResource("graph_paper.png");
            BufferedImage image = ImageIO.read(is);
            canvas.drawImage(0,0,image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MapCursorCollection cursors = new MapCursorCollection();

        for (int i =0; i<128;i++) {
            canvas.setPixel(0,i, MapPalette.DARK_GRAY);
            canvas.setPixel(1,i, MapPalette.DARK_GRAY);
            canvas.setPixel(i,126, MapPalette.DARK_GRAY);
            canvas.setPixel(i,127, MapPalette.DARK_GRAY);
            if (i%8 == 0) {
                canvas.setPixel(2,i,MapPalette.DARK_GRAY);
                canvas.setPixel(i,125,MapPalette.DARK_GRAY);
                if (i%32 == 0) {
                    cursors.addCursor(-128,127 - i*2,(byte)0,(byte)7,true,String.valueOf(Math.round((this.ymin + this.dy/128f*i) *1000f)/1000f));
                    cursors.addCursor(-128 + 2*i,127,(byte)0,(byte)7,true,String.valueOf(Math.round((this.xmin + this.dx/128f*i) *1000f)/1000f));
                }
            }
        }

        cursors.addCursor(-128,-128,(byte)0,(byte)7,true,String.valueOf(Math.round((this.ymax) *1000f)/1000f));
        cursors.addCursor(127,127,(byte)0,(byte)7,true,String.valueOf(Math.round((this.xmax) *1000f)/1000f));

        for (int j =0; j < this.x_scale.length; j++) {
            cursors.addCursor(Math.round(this.x_scale[j]*255)-128,Math.round(this.y_scale[j]*-255)+127,(byte)0,(byte)4,true);
        }
        canvas.setCursors(cursors);

        int resolution;
        for (int i = 0; i< this.x_scale.length - 1; i++) {
            resolution = (int) (127 * Math.sqrt(Math.pow(this.x_scale[i] - this.x_scale[i+1],2) + Math.pow(this.y_scale[i] - this.y_scale[i+1],2)));
            drawLine(new int[]{Math.round(this.x_scale[i] * 127), Math.round(this.y_scale[i] * -127)+127}, new int[]{Math.round(this.x_scale[i + 1] * 127), Math.round(this.y_scale[i + 1] * -127)+127},resolution,canvas,MapPalette.RED);
        }

        int[] start = {0,Math.round(interpolationLagrange(this.x_scale,this.y_scale,0)* -127f) +127};
        int[] end;
        for (int i = 1; i<128; i++) {
            end = new int[]{i,(Math.round(interpolationLagrange(this.x_scale,this.y_scale,i/127f)* -127f) +127)};
            resolution = (int) (127 * Math.sqrt(Math.pow(start[0] - end[0],2) + Math.pow(start[1] - end[1],2)));

            drawLine(start,end,resolution,canvas,MapPalette.PALE_BLUE);

            start = end.clone();
        }

    }

    private void drawLine(int[] start, int[] end, int resolution, MapCanvas canvas,byte color) {
        int x = start[0];
        int y = start[1];
        for (float t = 0; t < 1; t = t + 1f/(float)resolution) {
            canvas.setPixel(x,y,color);
            x = Math.round(start[0] * (1-t) + end[0] * t);
            y = Math.round(start[1] * (1-t) + end[1] * t);
        }
    }

    private float polynomeLagrange(float[] x, float X0,int i) {
        float product = 1;
        for (float xj : x) {
            if (xj == x[i]) continue;
            product = product * (X0 - xj)/(x[i] - xj);
        }
        return product;
    }

    private float interpolationLagrange(float[] x, float[] y, float X0) {
        float result = 0;
        for (int i = 0; i < x.length; i++) {
            result += y[i] * polynomeLagrange(x,X0,i);
        }
        return result;
    }
}
