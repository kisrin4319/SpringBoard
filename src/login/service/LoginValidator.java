package login.service;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import login.model.LoginSessionModel;

public class LoginValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return LoginSessionModel.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LoginSessionModel loginModel  = (LoginSessionModel)target;
		
		//check userId field
		if(loginModel.getMember_id() == null || loginModel.getMember_id().trim().isEmpty()) {
			errors.rejectValue("member_id", "required");
		}
		if(loginModel.getMember_pw() == null || loginModel.getMember_pw().trim().isEmpty()) {
			errors.rejectValue("member_pw", "required");
		}
	}

}
