package fb.repository;

import fb.entity.FbUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FbUserRepository extends CrudRepository<FbUser,Integer>  {
    FbUser findUserById(@Param("id") Integer id);
}
