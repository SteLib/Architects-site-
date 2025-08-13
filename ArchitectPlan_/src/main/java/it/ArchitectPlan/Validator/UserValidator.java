package it.ArchitectPlan.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ArchitectPlan.model.User;
import it.ArchitectPlan.service.UserService;

@Component
public class UserValidator implements Validator{
	
	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User)target;
		if(user.getName()!=null && user.getSurname()!=null 
				&& userService.existsByNameAndSurname(user.getName(), user.getSurname()))
		errors.reject("user.duplicate");
	}
}
