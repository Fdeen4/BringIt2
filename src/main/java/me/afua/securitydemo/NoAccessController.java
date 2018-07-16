package me.afua.securitydemo;

import me.afua.securitydemo.models.AppUser;
import me.afua.securitydemo.repositories.ItemRepository;
import me.afua.securitydemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

//This controller shows routes that do not require authentication
@Controller
public class NoAccessController {
    @Autowired
    ItemRepository items;

    @Autowired
    UserService users;

    @RequestMapping("/login")
    public String showLogin()
    {
        return "login";
    }

//    Default route should be an introduction page
    @RequestMapping("/")
    public String showIndex(Model model, Authentication auth) {

        if (auth == null) {
            //Show only 10 items if the user is not logged in
            model.addAttribute("items", items.findTop10ByOrderById());
        } else {
            //Show all Items if the user is logged in
            model.addAttribute("items", users.showListedItems());
        }

        return "index";
    }


    @GetMapping("/register")
    public String showRegistrationForm(Model model)
    {
        model.addAttribute("newuser",new AppUser());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid @ModelAttribute("newuser") AppUser user, BindingResult result)
    {

        if(result.hasErrors())
        {
            return "register";
        }
        user.addRole(users.findRole("USER"));
        users.saveMe(user);
        return "redirect:/login";

    }

}
