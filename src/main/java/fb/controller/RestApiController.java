package fb.controller;

import fb.model.ApiData;
import fb.model.FbData;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
public class RestApiController {

    @Inject
    private Facebook facebook;

    @RequestMapping("/fb/api/general")
    public ApiData general(){
        FbData data;
        try {
            if(facebook == null){
                data =  new FbData(-1,"Facebook NULL");
            }else if (!facebook.isAuthorized()) {
                data =  new FbData(0,"Not authenticated");
            }else {
                //TODO
                User profile = facebook.userOperations().getUserProfile();
                data = new FbData(1,profile.getAbout());
            }
        }catch (Exception  e ){
            data = new FbData(-2,e.getMessage());
        }

        return data;
    }
}
