package member.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import member.model.MemberModel;
import member.service.MemberService;
import member.service.MemberValidatior;

@Controller
@RequestMapping("/member")
public class MemberController {

	private ApplicationContext context;
	
	@RequestMapping("/join.do")
	public String memberJoin() {
		return "/board/join";
	}
	
	@RequestMapping(value = "/join.do", method = RequestMethod.POST)
	public ModelAndView addMember(@ModelAttribute("MemberModel") MemberModel memberModel, BindingResult result) {
		ModelAndView mv = new ModelAndView();
		new MemberValidatior().validate(memberModel, result);
		if(result.hasErrors()) {
			mv.setViewName("/board/join");
			return mv;
		}
		
		context = new ClassPathXmlApplicationContext("/config/applicationContext.xml");
		MemberService memberService = (MemberService) context.getBean("memberService");
		MemberModel checkMemberModel = memberService.findById(memberModel.getMember_id());
		
		if(checkMemberModel!=null) {
			mv.addObject("errCode",1); // member_id already exist
			mv.setViewName("/board/join");
			return mv;
		}
		
		if(memberService.addMember(memberModel)) {
			mv.addObject("errCode",3);
			mv.setViewName("/board/login"); //success to add new member; move to login page
			return mv;
		}
		else {
			mv.addObject("errCode",2); //failed to add new member
			mv.setViewName("/board/login");
			return mv;
		}
	}
	
}
