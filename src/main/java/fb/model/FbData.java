package fb.model;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FbData implements  ApiData{
    Integer id;
    String value;

    public FbData(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public FbData() {
    }
}
