package com.dimovski.sportko.db.repository.repos;

import com.dimovski.sportko.db.model.User;

public interface UserRepository extends Repository<User>{

    String getUsernameForEmail (String email);
}
