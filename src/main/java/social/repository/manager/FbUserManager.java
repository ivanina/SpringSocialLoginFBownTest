package social.repository.manager;


import social.entity.domain.FbUser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import javax.inject.Inject;


@Transactional
@Component
public class FbUserManager {

    @Inject
    FbUserRepository fbUserRepository;

    @Transactional
    public FbUser save(FbUser user){
        return fbUserRepository.save(user);
    }
    public FbUser findByFbId(Long fbId){
        return fbUserRepository.findByFbIdIs(fbId);
    }
}
