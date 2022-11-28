package com.example.UnitTest;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class MyUserController {

    @Autowired
    private MyUserService myUserService;

    @PostMapping
    public @ResponseBody MyUser create(@RequestBody MyUser user) {
        return myUserService.create(user);
    }

    @GetMapping("/all")
    public @ResponseBody List<MyUser> getAllUsers() {
        return myUserService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MyUser> getAUser(@PathVariable Long id) {
        MyUser userFromDB = myUserService.getOne(id);
        if (userFromDB == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userFromDB);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MyUser> update(@PathVariable Long id, @RequestBody @NotNull MyUser user) {
        user.setId(id);
        MyUser userFromDB = myUserService.update(user);
        if (userFromDB == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userFromDB);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (myUserService.delete(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
