package wannagohome.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import wannagohome.support.SpringTest;

import static org.junit.Assert.*;


public class TeamServiceTest extends SpringTest {
    private static final Logger log = LoggerFactory.getLogger(TeamServiceTest.class);

    @Autowired
    private TeamService teamService;

    @Test
    public void findUsersByKeyword() {
        log.debug("users: {}", teamService.findUsersByKeyword(1L, "ju"));
    }
}