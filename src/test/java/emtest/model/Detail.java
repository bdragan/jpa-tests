package emtest.model;

import javax.persistence.*;

@Entity
public class Detail {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Master master;

    @Column(nullable = false, unique = true)
    private String name;

    public Detail() {
    }

    public Detail(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Detail detail = (Detail) o;

        return name != null ? name.equals(detail.name) : detail.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
