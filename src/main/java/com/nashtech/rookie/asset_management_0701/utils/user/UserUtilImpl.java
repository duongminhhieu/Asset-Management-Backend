package com.nashtech.rookie.asset_management_0701.utils.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.nashtech.rookie.asset_management_0701.dtos.requests.user.UserRequest;
import com.nashtech.rookie.asset_management_0701.entities.User;
import com.nashtech.rookie.asset_management_0701.repositories.UserRepository;
import com.nashtech.rookie.asset_management_0701.services.user.UserSpecification;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserUtilImpl implements UserUtil{
    private final UserRepository userRepository;
    @Override
    public String generateUsername (UserRequest userRequest) {
        StringBuilder username = new StringBuilder(userRequest.getFirstName().toLowerCase().replaceAll("\\s", ""));
        String[] splitSpace = userRequest.getLastName().toLowerCase().split(" ");

        for (String s : splitSpace) {
            username.append(s.charAt(0));
        }

        List<User> userList = userRepository.findAll(UserSpecification.usernameStartsWith(username.toString()));
        if (userList.isEmpty()) {
            return username.toString();
        }
        username.append(getMaxNumber(userList) + 1);
        return username.toString();
    }

    @Override
    public String generateUsernameFromWeb (String firstName, String lastName) {
        StringBuilder username = new StringBuilder(firstName.toLowerCase().replace("%20", ""));
        String[] splitSpace = lastName.toLowerCase().split("%20");

        for (String s : splitSpace) {
            username.append(s.charAt(0));
        }

        List<User> userList = userRepository.findAll(UserSpecification.usernameStartsWith(username.toString()));
        if (userList.isEmpty()) {
            return username.toString();
        }
        username.append(getMaxNumber(userList) + 1);
        return username.toString();
    }

    private static int getMaxNumber (List<User> userList) {
        Pattern pattern = Pattern.compile("\\d+");
        List<Integer> numbersInUsernames = new ArrayList<>();

        for (User user : userList) {
            String existUsername = user.getUsername();
            Matcher matcher = pattern.matcher(existUsername);
            while (matcher.find()) {
                String numberStr = matcher.group();
                int number = Integer.parseInt(numberStr);
                numbersInUsernames.add(number);
            }
        }
        int number = 0;
        if (!numbersInUsernames.isEmpty()) {
            number = Collections.max(numbersInUsernames);
        }
        return number;
    }
}
