package fb.service;

import fb.entity.FbUser;
import fb.repository.FbUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
public class FbUserService {
    @Inject
    private FbUserRepository fbUserRepository;

    @Transactional
    public FbUser save(FbUser user){
        return fbUserRepository.save(user);
    }
}
