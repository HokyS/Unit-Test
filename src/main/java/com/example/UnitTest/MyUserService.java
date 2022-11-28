package com.example.UnitTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyUserService {

    @Autowired
    MyUserRepository myUserRepository;

    public MyUser create(MyUser user) {
        return myUserRepository.saveAndFlush(user);
    }

    public List<MyUser> getAll() {
        return myUserRepository.findAll();
    }

    public MyUser getOne(Long id) {
        Optional<MyUser> user = myUserRepository.findById(id);
        if (user.isEmpty()) return null;
        return user.get();
    }

    public MyUser update(MyUser student) {
        Optional<MyUser> userFromDB = myUserRepository.findById(student.getId());
        if (userFromDB.isEmpty()) return null;
        return myUserRepository.saveAndFlush(userFromDB.get());
    }

    public boolean delete(Long id) {
        Optional<MyUser> userFromDB = myUserRepository.findById(id);
        if (userFromDB.isEmpty()) return false;
        myUserRepository.deleteById(id);
        return true;
    }
}