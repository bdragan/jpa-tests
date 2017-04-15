package emtest.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Master {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "master", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Detail> details = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void addDetail(Detail detail) {
        details.add(detail);
        detail.setMaster(this);
    }

    public Collection<Detail> getDetails() {
        return details;
    }
}
