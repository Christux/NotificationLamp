package fr.christux.notificationlamp;

public class RgbColor {

    public int R, G, B;

    public RgbColor(int r, int g, int b)
    {
        this.R = r;
        this.G = g;
        this.B = b;
    }

    public final static RgbColor red = new RgbColor(255,0,0);
    public final static RgbColor green = new RgbColor(0,255,0);
    public final static RgbColor blue = new RgbColor(0,0,255);
    public final static RgbColor purple = new RgbColor(146, 0, 255);
    public final static RgbColor orange = new RgbColor(255, 128, 0);
    public final static RgbColor blank = new RgbColor(0, 0, 0);
    public final static RgbColor white = new RgbColor(255, 255, 255);

    public boolean isEqual(RgbColor color) {
        return this.R == color.R && this.G == color.G && this.B == color.B;
    }

    public RgbColor(int rgba) {

        int A = (rgba >> 24) & 0xff; // or color >>> 24
        this.R = (rgba >> 16) & 0xff;
        this.G = (rgba >>  8) & 0xff;
        this.B = (rgba      ) & 0xff;
    }
}
