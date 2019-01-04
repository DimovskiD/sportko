package com.dimovski.sportko.db.repository.repos;

import com.dimovski.sportko.db.model.User;

/**Additional methods, beside the @{@link Repository} interface methods, that are required for users repository*/
public interface UserRepository extends Repository<User>{

    String getUsernameForEmail (String email);
}
