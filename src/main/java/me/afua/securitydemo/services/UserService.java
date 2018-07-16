package me.afua.securitydemo.services;

import me.afua.securitydemo.models.AppRole;
import me.afua.securitydemo.models.AppUser;
import me.afua.securitydemo.models.Item;
import me.afua.securitydemo.models.Messages;
import me.afua.securitydemo.repositories.AppUserRepository;
import me.afua.securitydemo.repositories.ItemRepository;
import me.afua.securitydemo.repositories.MessagesRepository;
import me.afua.securitydemo.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private AppUserRepository users;

    @Autowired
    private RoleService roles;

    @Autowired
    private ItemRepository items;

    @Autowired
    private MessagesRepository messages;

    //Add a user
    public AppUser addUser(String username, String password, String[] roleList) {
        AppUser newUser = new AppUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        for (String eachRole : roleList) {
            newUser.addRole(this.roles.find(eachRole));
        }
        users.save(newUser);
        return newUser;
    }

    //Add a user with an individual role
    public AppUser addUser(String username, String password, String role) {
        AppUser newUser = new AppUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.addRole(roles.find(role));
        users.save(newUser);
        return newUser;
    }

    public AppRole addAppRole(String rolename) {
        return this.roles.addRole(rolename);
    }

    public AppUser findMe(String username) {
        return users.findAppUserByUsername(username);
    }

    public Iterable<Item> findMyItems(String username) {
        return (items.findAllBySellerIs(findMe(username)));
    }

    public Iterable<Item> showListedItems() {
        return items.findAllByListed(true);
    }

    public Iterable<Item> searchFor(String search) {
        return null;
//        return items.findAllByTagsContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search,search);
    }

    public Iterable<Messages> findMyMessages(String username) {
        return messages.findAllByRecipientIsOrderBySentTimeDesc(users.findAppUserByUsername(username));
    }

    public Messages saveMessage(Messages theMessage) {
        return messages.save(theMessage);
    }

    public Messages getMessage(long id)
    {
        return messages.findById(id).get();
    }

    public Iterable<AppUser> allUsers()
    {
        return users.findAll();
    }

    public AppUser findMe(long id)
    {
        return users.findById(id).get();
    }

    public AppRole findRole(String rolename)
    {
        return roles.find(rolename);
    }

    public AppUser saveMe(AppUser user)
    {
        return users.save(user);
    }

    public void removeRole(String rolename)
    {

    }


}
