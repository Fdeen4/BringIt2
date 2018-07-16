package me.afua.securitydemo;

import me.afua.securitydemo.models.AppUser;
import me.afua.securitydemo.models.Item;
import me.afua.securitydemo.models.randomuserapi.RandomUser;
import me.afua.securitydemo.models.randomuserapi.RandomUsers;
import me.afua.securitydemo.repositories.ItemRepository;
import me.afua.securitydemo.repositories.randomuserapi.RandomUserRepository;
import me.afua.securitydemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    UserService users;

    @Autowired
    ItemRepository items;

    @Autowired
    RandomUserRepository randomUserRepo;
    @Override
    public void run(String... args) throws Exception {

        System.out.println("Starting up the application. This will change to a log message. ");

        //Create Roles
        users.addAppRole("SUSPENDED");
        users.addAppRole("ADMIN");
        users.addAppRole("USER");
        users.addAppRole("MANAGER");

        //Create users with single administrative rights
        users.addUser("auser","apassword","ADMIN");
        users.addUser("anotheruser","nopassword","USER");

        //Add multiple roles to a user (comma-separated)
        String[]rolesToAdd={"ADMIN","MANAGER"};

        users.addUser("multipleroleuser","password",rolesToAdd);

        for(int i=1; i<=20; i++)
        {
            Item anItem = new Item();
            anItem.setDescription("Item "+i);
            anItem.setSeller(users.findMe("anotheruser"));
            if(i<=10)
                anItem.setListed(true); //List 10 items
            items.save(anItem);
        }

//        Random USER API - add users here:
        RestTemplate fromAPI = new RestTemplate();
//        String theUserDetails = fromAPI.getForObject("https://randomuser.me/api",String.class);


        RandomUsers randomUsers =   fromAPI.getForObject("https://randomuser.me/api?results=10",RandomUsers.class);
        for (RandomUser eachuser: randomUsers.getResults()) {
            System.out.println("Image:"+eachuser.getImage().get("large"));
            System.out.println("Username:"+eachuser.getUsername());
            System.out.println("Password:"+eachuser.getUnEncryptedPassword());
            System.out.println("Email:"+eachuser.getEmail());

            //Save information to the database (keep a copy of the random users)
            //DO NOT do this with live data!!!!!
            randomUserRepo.save(eachuser);

            // 'Convert' random user data into our app user data
            AppUser aUser = new AppUser();
            aUser.addRole(users.findRole("USER"));
            aUser.setPassword(eachuser.getUnEncryptedPassword());
            aUser.setUsername(eachuser.getUsername());
            users.saveMe(aUser);
        }

    }
}
