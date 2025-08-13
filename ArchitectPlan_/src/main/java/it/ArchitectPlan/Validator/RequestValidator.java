package it.ArchitectPlan.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ArchitectPlan.model.Request;
import it.ArchitectPlan.service.RequestService;

@Component
public class RequestValidator implements Validator{
	
	@Autowired
	private RequestService requestService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Request.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
	    Request request = (Request)target;
	    if(request.getName()!=null && requestService.isRequestPresent(request.getName()))
	        errors.rejectValue("name", "request.duplicate");
	}
}
