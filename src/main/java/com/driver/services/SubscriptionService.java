package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user =userRepository.findById(subscriptionEntryDto.getUserId()).get();
        Subscription subscription =new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        SubscriptionType subscriptionType =subscriptionEntryDto.getSubscriptionType();
        int amount=0;
        if(subscriptionType==SubscriptionType.BASIC){
            amount = 500 + 200*(subscriptionEntryDto.getNoOfScreensRequired());
        } else if (subscriptionType==SubscriptionType.PRO) {
            amount = 800 + 250*(subscriptionEntryDto.getNoOfScreensRequired());
        } else if (subscriptionType==SubscriptionType.ELITE) {
            amount = 1000 + 350*(subscriptionEntryDto.getNoOfScreensRequired());
        }
        subscription.setTotalAmountPaid(amount);
        Date systemDate=new Date();
        subscription.setStartSubscriptionDate(systemDate);
        subscription.setUser(user);
        user.setSubscription(subscription);
        userRepository.save(user);
        return amount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user =userRepository.findById(userId).get();
        int difference = 0;
        Subscription subscription =user.getSubscription();
        if(subscription.getSubscriptionType()==SubscriptionType.ELITE){
            throw new Exception("Already the best Subscription");
        }

        if(subscription.getSubscriptionType()==SubscriptionType.BASIC){
                subscription.setSubscriptionType(SubscriptionType.PRO);
                Integer previousPrice =subscription.getTotalAmountPaid();
                Integer newPrice = 800 + 250*(subscription.getNoOfScreensSubscribed());
                subscription.setTotalAmountPaid(newPrice);
                difference=newPrice-previousPrice;
            user.setSubscription(subscription);
                userRepository.save(user);
        }
        else if (subscription.getSubscriptionType()==SubscriptionType.PRO){
                subscription.setSubscriptionType(SubscriptionType.ELITE);
                Integer previousPrice =subscription.getTotalAmountPaid();
                Integer newPrice = 1000 + 350*(subscription.getNoOfScreensSubscribed());
                subscription.setTotalAmountPaid(newPrice);
                difference=newPrice-previousPrice;
              user.setSubscription(subscription);
              userRepository.save(user);
        }
        return difference;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscription =subscriptionRepository.findAll();
        int totalRevenue=0;

            for(Subscription subscription1 : subscription){
                totalRevenue =totalRevenue + subscription1.getTotalAmountPaid();
            }

        return totalRevenue;
    }

}
