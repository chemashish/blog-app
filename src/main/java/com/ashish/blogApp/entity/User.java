package com.ashish.blogApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    @NotNull(message = "is required")
    @Size(min = 1,message = "is required")
    private String name;
    @Column(name = "email")
    @NotNull(message = "is required")
    @Size(min = 1,message = "is required")
    private String email;
    @Column(name = "password")
    @NotNull(message = "is required")
    @Size(min = 1,message = "is required")
    private String password;

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> posts;

    @JoinColumn(name = "role")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(){}
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
    public void addPost(Post post){
        if(posts == null){
            posts = new ArrayList<>();
        }
        posts.add(post);
    }

    @Override
    public String toString() {
        return name;
    }
}
