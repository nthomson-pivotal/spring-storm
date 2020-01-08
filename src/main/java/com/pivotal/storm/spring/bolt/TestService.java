package com.pivotal.storm.spring.bolt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Value("${myconfig.prop}")

    public static Logger logger = LoggerFactory.getLogger(TestService.class);

    public TestService() {
        logger.error("CREATING TEST SERVICE");
    }

    public void test() {
        logger.error("TEST METHOD");
    }
}
