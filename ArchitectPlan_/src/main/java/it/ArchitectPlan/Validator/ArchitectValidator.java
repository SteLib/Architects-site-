package it.ArchitectPlan.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ArchitectPlan.model.Architect;
import it.ArchitectPlan.service.ArchitectService;

@Component
public class ArchitectValidator implements Validator{
	
	@Autowired
	private ArchitectService architectService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Architect.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Architect architect = (Architect)target;
		if(architect.getName()!=null && architectService.existsByName(architect.getName()))
			errors.reject("architect.duplicate");
	}
}
