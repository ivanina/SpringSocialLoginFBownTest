package fb.repository;

import fb.entity.FbUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface FbUserRepository extends CrudRepository<FbUser,Integer>  {
    FbUser findUserById(@Param("id") Integer id);
}
