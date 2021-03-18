package us.rise8.tracker.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.rise8.tracker.api.user.UserEntity;
import us.rise8.tracker.api.user.UserRepository;
import us.rise8.tracker.api.user.UserService;

@Component
public class Startup {

    private static final Logger LOG = LoggerFactory.getLogger(Startup.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomProperty property;

    @PostConstruct
    public void init() {
        LOG.info("ENVIRONMENT: " + property.getEnvironment());
        if (!property.getEnvironment().equalsIgnoreCase("local")) {
            UserEntity rootUser = userService.getObject(1L);

            if (Boolean.FALSE.equals(rootUser.getIsDisabled())) {
                LOG.info("DISABLING ROOT USER");
                rootUser.setIsDisabled(true);
                userRepository.save(rootUser);
                LOG.info("DISABLED ROOT USER");
            }
        }
    }
}
