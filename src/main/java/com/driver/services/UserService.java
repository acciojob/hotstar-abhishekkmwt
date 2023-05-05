package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

       User newUser= userRepository.save(user);
       Integer id =newUser.getId();
        //Jut simply add the user to the Db and return the userId returned by the repository
        return id;
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        User user =userRepository.findById(userId).get();
        Integer count=0;
        if(user!=null){
            Integer userAge = user.getAge();
            SubscriptionType subscriptionType =user.getSubscription().getSubscriptionType();
            List<WebSeries> webSeries = webSeriesRepository.findAll();
            if(webSeries!=null){
                for(WebSeries webSeries1 : webSeries){
                    if(webSeries1.getSubscriptionType().equals(subscriptionType) && webSeries1.getAgeLimit()<userAge){
                        count++;
                    }
                }
            }
        }
        return count;
    }


}
