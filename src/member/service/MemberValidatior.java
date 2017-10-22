package member.service;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import member.model.MemberModel;

public class MemberValidatior implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		
		return MemberModel.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		MemberModel memberModel = (MemberModel) target;
		
		if(memberModel.getMember_id() == null || memberModel.getMember_id().trim().isEmpty()) {
			errors.rejectValue("member_id", "required");
		}
		if(memberModel.getMember_pw() == null || memberModel.getMember_pw().trim().isEmpty()) {
			errors.rejectValue("member_pw", "required");
		}
		if(memberModel.getMember_name() == null || memberModel.getMember_name().trim().isEmpty()) {
			errors.rejectValue("member_name", "required");
		}
	}
}
