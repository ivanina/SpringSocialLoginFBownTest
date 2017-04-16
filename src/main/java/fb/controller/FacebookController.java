package fb.controller;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.inject.Inject;

@Controller
@RequestMapping("/fb")
public class FacebookController {
    private Facebook facebook;

    @Inject
    public FacebookController(Facebook facebook) {
        this.facebook = facebook;
    }

    @RequestMapping()
    public String helloFacebook() {
        /*if (!facebook.isAuthorized()) {
            return "redirect:/connect/facebook";
        }*/

        return "fb_index";
    }
}
