package me.afua.securitydemo.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String password;

    private boolean suspended;


    @Transient
    PasswordEncoder encoder;

    @OneToMany(mappedBy="seller")
    private Set<Item> myItems;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<AppRole> roles;

    @OneToMany(mappedBy="recipient")
    Set <Messages> received;

    @OneToMany(mappedBy="sender")
    Set <Messages> sent;

    public AppUser() {
        this.roles = new HashSet<>();
        this.encoder = new BCryptPasswordEncoder();
        this.sent = new HashSet<>();
        this.received = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = encoder.encode(password);
    }

    public Set<AppRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<AppRole> roles) {
        this.roles = roles;
    }

    public void addRole(AppRole r)
    {
        this.roles.add(r);
    }

    public void removeRole(AppRole r){this.roles.remove(r);}

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public Set<Item> getMyItems() {
        return myItems;
    }

    public void setMyItems(Set<Item> myItems) {
        this.myItems = myItems;
    }


    public Set<Messages> getReceived() {
        return received;
    }

    public void setReceived(Set<Messages> received) {
        this.received = received;
    }

    public Set<Messages> getSent() {
        return sent;
    }

    public void setSent(Set<Messages> sent) {
        this.sent = sent;
    }
}
