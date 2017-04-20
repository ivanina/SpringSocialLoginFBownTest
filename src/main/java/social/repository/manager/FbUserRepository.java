package social.repository.manager;

import social.entity.domain.FbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Repository
public interface FbUserRepository extends JpaRepository<FbUser,Long> {
    FbUser findByFbIdIs(Long fbId);


    @Transactional
    FbUser save(FbUser fbUser);

}
