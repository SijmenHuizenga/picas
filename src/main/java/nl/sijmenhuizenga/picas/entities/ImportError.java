package nl.sijmenhuizenga.picas.entities;

import javax.persistence.*;
import java.io.PrintWriter;
import java.io.StringWriter;

@Entity
public class ImportError {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String filepath;

    // unix timestamp second
    private long timestamp;

    private String error;

    @Column(length = 10000)
    private String stacktrace;

    private boolean resolved;

    public ImportError() {
    }

    public ImportError(String filepath, Exception error) {
        this.filepath = filepath;
        this.resolved = false;
        this.error = error.getMessage();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        error.printStackTrace(pw);
        this.stacktrace = sw.toString();

        this.timestamp = System.currentTimeMillis() / 1000L;
    }

    public Long getId() {
        return id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }
}
