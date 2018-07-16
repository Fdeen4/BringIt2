package me.afua.securitydemo;

import me.afua.securitydemo.models.AppRole;
import me.afua.securitydemo.models.AppUser;
import me.afua.securitydemo.models.Item;
import me.afua.securitydemo.models.Messages;
import me.afua.securitydemo.repositories.ItemRepository;
import me.afua.securitydemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;

@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
@Controller
public class MainController {
    @Autowired
    ItemRepository items;

    @Autowired
    UserService users;



    @RequestMapping("/add")
    public String addItem(Model model) {
        model.addAttribute("active","additems");
        model.addAttribute("anItem", new Item());
        return "additem";
    }

    @PostMapping("/saveitem")
    public String saveItem(@ModelAttribute("anItem") Item theItem, String username) {

        theItem.setSeller(users.findMe(username));
        items.save(theItem);
        return "redirect:/";
    }

    @RequestMapping("/myitems")
    public String showMyItems(Authentication auth, Model model) {
        //        Show only the items I am selling
        model.addAttribute("active","myitems");
        model.addAttribute("items", users.findMyItems(auth.getName()));
        return "myitems";
    }

    @RequestMapping("/changestatus")
    public String changeItemStatus(HttpServletRequest request)
    {
        //Get the item id
       long itemId = new Long(request.getParameter("id"));
       Item theItem = items.findById(itemId).get();
       //Create an item
       theItem.setAvailable(!theItem.isAvailable());
       //Reverse its availability
       items.save(theItem);
       return "redirect:/myitems";
    }

    @RequestMapping("/changelisting")
    public String changeListing(HttpServletRequest request)
    {
        //Get the item id
        long itemId = new Long(request.getParameter("id"));
        Item theItem = items.findById(itemId).get();
        //Create an item
        theItem.setListed(!theItem.isListed());
        //Reverse its listing status
        items.save(theItem);
        return "redirect:/myitems";
    }

    @RequestMapping("/searchlisting")
    public String searchListing(HttpServletRequest request)
    {

        return "index";
    }

    @RequestMapping("/interested")
    public String expressInterest(HttpServletRequest request, Authentication auth)
    {
        long itemId = new Long(request.getParameter("id"));
        Item theItem = items.findById(itemId).get();
        Messages aMessage = new Messages();
        aMessage.setText("I am interested in "+theItem.getDescription());
        aMessage.setSender(users.findMe(auth.getName()));
        aMessage.setRecipient(theItem.getSeller());
        users.saveMessage(aMessage);
        return "redirect:/";
    }


    @RequestMapping("/mymessages")
    public String showMyMessages(Model model, Authentication authentication)
    {
        model.addAttribute("active","messages");

        model.addAttribute("myMessages",users.findMyMessages(authentication.getName()));
        return "mymessages";
    }

    @RequestMapping("/changemessagestatus")
    public String changeMessageStatus(HttpServletRequest request)
    {
        long messageId = new Long(request.getParameter("id"));

        Messages aMessage = users.getMessage(messageId);
        aMessage.setReading(!aMessage.isReading());
        users.saveMessage(aMessage);
        return "redirect:/mymessages";
    }

    @GetMapping("/users")
    public String showSuspendForm( Model model, Authentication auth)
    {
        model.addAttribute("active","users");
        model.addAttribute("currentUser",users.findMe(auth.getName()));
        model.addAttribute("users",users.allUsers());
        return "adminpage";
    }

    @RequestMapping("/suspend")
    public String suspendUser(HttpServletRequest request, Model model)
    {
        long userid = new Long(request.getParameter("userid"));
        AppUser theUser = users.findMe(userid);
        theUser.setRoles(new HashSet<>());
        theUser.addRole(users.findRole("SUSPENDED"));
        theUser.setSuspended(true);
        users.saveMe(theUser);
        return "redirect:/users";
    }

    @RequestMapping("/reinstate")
    public String reinstateUser(HttpServletRequest request, Model model)
    {

        long userid = new Long(request.getParameter("userid"));
        AppUser theUser = users.findMe(userid);
        theUser.addRole(users.findRole("USER"));
        theUser.removeRole(users.findRole("SUSPENDED"));
        theUser.setSuspended(false);
        users.saveMe(theUser);
        return "redirect:/users";
    }





}


