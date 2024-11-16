package org.machine.domain.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class PageController {

    @RequestMapping(path = {"", "/main"})
    public ModelAndView main() {
        return new ModelAndView("main");
    }

}
