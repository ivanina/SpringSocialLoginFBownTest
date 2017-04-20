package social.controller;

import social.entity.domain.FbUser;
import social.repository.manager.FbUserRepository;
import social.service.fb.FbUserService;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.List;

@Transactional
@RestController
@RequestMapping("api/fb/authentication")
public class FbAuthenticationController {

    @Inject
    private FbUserService fbUserService;

    @Inject
    FbUserRepository fbUserRepository;


    @Transactional
    @RequestMapping(method = RequestMethod.GET)
    FbUser getCurrentFbUser(){

        List<FbUser> uL = (List<FbUser>) fbUserRepository.findAll();
        FbUser u1 = fbUserRepository.findOne(1L);
        FbUser u2 = fbUserRepository.findByFbIdIs(1001L);

        u2.setKeywords("[\"test\",\"dev\"]");

        u2 = fbUserRepository.save(u2);


        FbUser fbCurrentUser = fbUserService.getAuthenticatedFbUser();
        if(fbCurrentUser == null){
            fbCurrentUser = new FbUser();
        }

        return fbCurrentUser;
    }

}
