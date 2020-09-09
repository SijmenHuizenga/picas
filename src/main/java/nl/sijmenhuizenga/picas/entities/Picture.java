package nl.sijmenhuizenga.picas.entities;

import javax.persistence.*;

@Entity
public class Picture {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String filepath;

    private int width;
    private int height;

    protected Picture() {}

    public Picture(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", filepath='" + filepath + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setSize(ImageSize size) {
        this.width = size.width;
        this.height = size.height;
    }
}
