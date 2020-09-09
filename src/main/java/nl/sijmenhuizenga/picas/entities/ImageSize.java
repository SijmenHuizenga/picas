package nl.sijmenhuizenga.picas.entities;

public class ImageSize {

    final int width, height;

    public ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean is0() {
        return width == 0 && height == 0;
    }
}
