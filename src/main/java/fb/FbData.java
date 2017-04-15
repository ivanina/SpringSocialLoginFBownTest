package fb;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;

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
