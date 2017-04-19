package fb.controller;

import fb.model.ApiData;
import fb.model.FbData;
import fb.entity.FbUser;
import fb.service.FbUserService;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import fb.repository.FbUserRepository;

import javax.inject.Inject;
import java.util.List;

@RestController
public class RestApiController {

    @Inject
    private Facebook facebook;

    @Inject
    private FbUserRepository fbUserRepository;

    @Inject
    FbUserService fbUserService;

    @RequestMapping("/fb/api/general")
    public ApiData general(){
        FbUser u1 = new FbUser();
        //u1.setId(2);
        u1.setName("Test-2");
        u1.setData("{}");

        u1 = fbUserService.save(u1);


        FbData data;
        try {
            if(facebook == null){
                data =  new FbData(-1,"Facebook NULL");
            }else if (!facebook.isAuthorized()) {
                data =  new FbData(0,"Not authenticated");
            }else {
                //TODO
                //User profile = facebook.userOperations().getUserProfile();

                String [] fields = { "id", "email",  "first_name", "last_name" };
                User userProfile = facebook.fetchObject("me", User.class, fields);

                FbUser user = fbUserRepository.findUserById(1);
                List<FbUser> list = (List<FbUser>) fbUserRepository.findAll();

                FbUser newUser = new FbUser();
                //newUser.setId(2);
                newUser.setName("Test-2");
                newUser.setData("{}");
                newUser = fbUserRepository.save(newUser);


                data = new FbData(1,userProfile);
            }
        }catch (Exception  e ){
            data = new FbData(-2,e.getMessage());
        }

        return data;
    }
}
