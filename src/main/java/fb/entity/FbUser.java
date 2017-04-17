package model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class FbUser {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "data")
    private String data;

    @Override
    public String toString() {
        return String.format(
                "User [id=%d, Name='%s', data='%s']",
                id, name, data);
    }
}
