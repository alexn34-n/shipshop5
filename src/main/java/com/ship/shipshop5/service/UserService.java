package com.ship.shipshop5.service;

import com.ship.shipshop5.entity.User;
import com.ship.shipshop5.entity.repository.UserRepository;
import com.ship.shipshop5.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;//check

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с идентификатором %s не найден", id)));
    }

    public  User saveUser(String phone,String login,String password,String email,String name, String secondName,String lastName ){
        var user=new User()
                .setPhone(phone)
                .setLogin(login)
                .setPassword(passwordEncoder.encode(password))
                .setEmail(email)
                .setName(name)
                .setSecondName(secondName)
                .setLastName(lastName)
                .setRole("CUSTOMER");
//                 .setNameShip(nameShip)
//                 .setImo(imo)
//                .setPositionStaff(positionStaff)

        return userRepository.save(user);

    }

}
