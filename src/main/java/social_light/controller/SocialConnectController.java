package social_light.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;

@Named
public class SocialConnectController extends ConnectController {

    @Inject
    private HttpServletRequest request;

    @Inject
    private HttpServletResponse response;

    private static final String REDIRECT_PATH = "redirect:/";

    public SocialConnectController(ConnectionFactoryLocator connectionFactoryLocator,
                                   ConnectionRepository connectionRepository) {
        super(connectionFactoryLocator, connectionRepository);
    }

    @Override
    protected String connectView() {
        //return redirect(); // !?
        return REDIRECT_PATH;
    }

    @Override
    protected String connectView(String providerId) {
        return redirect();
    }

    @Override
    protected String connectedView(String providerId) {
        return redirect();
    }

    protected String redirect(){
        if(request.getHeader("referer") != null && request.getHeader("referer").length() > 1){
            try {
                URL url = new URL(request.getHeader("referer"));
                response.setHeader("referer","");
                return "redirect:"+url.getPath();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
        return REDIRECT_PATH;
    }

}
