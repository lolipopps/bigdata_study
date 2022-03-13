package com.bigdata.back.common.utils;
import com.bigdata.back.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Exrickx
 */
@Component
@Slf4j
public class SecurityUtil {



    @Autowired
    private UserService userService;


}
