package fb;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @RequestMapping("/fb/api/general")
    public ApiData general(){
        return new FbData(1,"test");
    }
}
