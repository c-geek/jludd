package fr.twiced.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/udid2")
public class Udid2Controller {
	
	@RequestMapping({"", "/"})
	public ModelAndView udid2Generator(){
		return new ModelAndView("udid2");
	}
}
