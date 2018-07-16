package me.afua.securitydemo.repositories;

import me.afua.securitydemo.models.AppUser;
import me.afua.securitydemo.models.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {
    Iterable <Item> findTop10ByOrderById();
    Iterable <Item> findAllBySellerIs(AppUser seller);
    Iterable <Item> findAllByListed(boolean isListed);
    Iterable <Item> findAllByTagsContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String search, String othersearch);
}
