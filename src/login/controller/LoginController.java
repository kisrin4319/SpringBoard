package login.controller;

import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import login.model.LoginSessionModel;
import login.service.LoginService;
import login.service.LoginValidator;

@Controller
public class LoginController {

	private ApplicationContext context;
	
	@RequestMapping("/login.do")
	public String login() {
		return "/board/login";
	}
	
	@RequestMapping(value="/login.do", method = RequestMethod.POST)
	public ModelAndView loginProc(@ModelAttribute("LoginModel") LoginSessionModel loginModel, BindingResult result, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
		//form validation
		new LoginValidator().validate(loginModel, result);
		if(result.hasErrors()) {
			mv.setViewName("/board/login");
			return mv;
		}
		
		String member_id = loginModel.getMember_id();
		String member_pw = loginModel.getMember_pw();
		
		context = new ClassPathXmlApplicationContext("/config/applicationContext.xml");
		LoginService loginService = (LoginService) context.getBean("loginService");
		LoginSessionModel loginCheckResult = loginService.checkUserId(member_id);
		
		//check joined user
		if(loginCheckResult == null) {
			mv.addObject("member_id",member_id);
			mv.addObject("errCode",1); // not exist userId in database
			mv.setViewName("/board/login");
			return mv;
		}
		
		//check password
		if(loginCheckResult.getMember_pw().equals(member_pw)) {
			session.setAttribute("member_id", member_id);
			session.setAttribute("member_name", loginCheckResult.getMember_name());
			mv.setViewName("redirect:/board/list.do");
			return mv;
		} else {
			mv.addObject("member_id", member_id);
			mv.addObject("errCode",2); //not matched password
			mv.setViewName("/board/login");
			return mv;
		}
	}
	@RequestMapping("/logout.do")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login.do";
	}
}
