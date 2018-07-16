package me.afua.securitydemo.repositories;

import me.afua.securitydemo.models.AppUser;
import me.afua.securitydemo.models.Messages;
import org.springframework.data.repository.CrudRepository;

public interface MessagesRepository extends CrudRepository<Messages, Long> {

    Iterable<Messages> findAllByRecipientIsOrderBySentTimeDesc(AppUser user);
}
