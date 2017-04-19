package fb.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "testTable")
public class FbUser {
    @Id
    //@Column(name = "id")
    //@GeneratedValue(strategy= GenerationType.AUTO)
    //@GeneratedValue(strategy = GenerationType.AUTO, generator = "auto_gen")
    //@SequenceGenerator(name = "auto_gen", sequenceName = "test_table_id_seq")

    @SequenceGenerator(name="test_table_id_seq",
            sequenceName="test_table_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="test_table_id_seq")
    @Column(name = "id", updatable=false)
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
