package com.bigdata.back.common.utils;

import com.apifan.common.random.source.AreaSource;
import com.apifan.common.random.source.InternetSource;
import com.apifan.common.random.source.PersonInfoSource;
import com.bigdata.back.entity.User;
import com.bigdata.kafka.DataGenUtil;

public class DataUtil {

    static PersonInfoSource personInfoSource = PersonInfoSource.getInstance();
    static AreaSource areaSource = AreaSource.getInstance();
    static InternetSource internetSource = InternetSource.getInstance();
    static DataGenUtil dataGenUtil = new DataGenUtil();

    static User genUser() {
        User user = new User();
        user.setNickname(personInfoSource.randomChineseName());
        user.setUsername(personInfoSource.randomNickName(6));
        user.setPassword(personInfoSource.randomStrongPassword(8,false));
        user.setSex(personInfoSource.randomGender() % 2 == 0 ? "男" :"女");
        user.setMobile(personInfoSource.randomChineseMobile());
        user.setStreet(areaSource.randomAddress());
        user.setEmail(internetSource.randomEmail(10));

        return user;
    }

    public static void main(String[] args) {
        User user = genUser();
        System.out.println(user);

    }
}
