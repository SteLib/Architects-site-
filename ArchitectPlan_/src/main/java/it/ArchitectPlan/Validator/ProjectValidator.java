package it.ArchitectPlan.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.ArchitectPlan.model.Project;
import it.ArchitectPlan.service.ProjectService;

@Component
public class ProjectValidator implements Validator{
	
	@Autowired
	private ProjectService projectService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Project.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Project project = (Project)target;
		if(project.getName()!=null && projectService.existsByName(project.getName()))
			errors.reject("project.duplicate");
	}
	
}
