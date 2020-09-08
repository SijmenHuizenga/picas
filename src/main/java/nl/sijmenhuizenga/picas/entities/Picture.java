package nl.sijmenhuizenga.picas.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Picture {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String filepath;

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
}
