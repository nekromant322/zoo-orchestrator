package com.nekromant.zoo.mapper;

import com.nekromant.zoo.model.AnimalRequest;
import com.nekromant.zoo.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User animalRequestToUser(AnimalRequest animal) {
        return new User(
                animal.getEmail(),
                animal.getPhoneNumber()
        );
    }
}
