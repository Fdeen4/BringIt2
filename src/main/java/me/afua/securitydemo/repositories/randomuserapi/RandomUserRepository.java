package me.afua.securitydemo.repositories.randomuserapi;

import me.afua.securitydemo.models.randomuserapi.RandomUser;
import org.springframework.data.repository.CrudRepository;

public interface RandomUserRepository extends CrudRepository<RandomUser, Long> {
}
