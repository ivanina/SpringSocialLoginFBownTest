package social_light.service.fb;

import social_light.entity.domain.FbUser;
import social_light.repository.manager.FbUserManager;
import social_light.repository.manager.FbUserRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.inject.Inject;

@Aspect
@Service
@Transactional
public class FbUserService implements FbService {

    private Facebook facebook;

    @Inject
    private ConnectionRepository connectionRepository;

    @Inject
    FbUserManager fbUserManager;


    void init() {
        if(connectionRepository == null) return;
        Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
        //Facebook facebook = connection != null ? connection.getApi() : new FacebookTemplate(token);
        if(connection != null){
            this.facebook = connection.getApi();
        }
    }

    public void init(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
        this.init();
    }


    public boolean isAuthenticated(){
        this.init();
        if(facebook == null || !facebook.isAuthorized()){
            return false;
        }
        return true;
    }

    public FbUser getAuthenticatedFbUser(){
        if(!isAuthenticated()){
            return null;
        }
        User userProfile = getFbUserProfile();
        Long fbId = Long.parseLong(userProfile.getId());
        FbUser existUser = fbUserManager.findByFbId( fbId );
        if(existUser != null){
            if(existUser != null && checkUpdateNeeded(existUser)){
                //TODO: updated user to repository
            }
        }else if(userProfile != null && userProfile.getId() != null && userProfile.getId().length() > 0) {
            //TODO: create new FbUser
            existUser = new FbUser(Long.parseLong(userProfile.getId()),userProfile.getFirstName());
        }
        return existUser;
    }

    public boolean checkUpdateNeeded(FbUser user){
        //TODO: check by time interval between interval in properties and last update date
        return false;
    }

    public User getFbUserProfile(){
        if(!isAuthenticated()){
            return null;
        }
        String [] fields = { "id", "email",  "first_name", "last_name" };
        return facebook.fetchObject("me", User.class, fields);
    }

}
