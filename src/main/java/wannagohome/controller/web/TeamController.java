package wannagohome.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wannagohome.service.TeamService;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/team/{id}")
    public String team(HttpSession session, @PathVariable Long id) {
        teamService.viewTeam(id, SessionUtil.getUserSession(session));
        return "/team";
    }

}
