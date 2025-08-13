package it.ArchitectPlan.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.ArchitectPlan.Validator.ProjectValidator;
import it.ArchitectPlan.model.Architect;
import it.ArchitectPlan.model.Project;
import it.ArchitectPlan.model.Request;
import it.ArchitectPlan.repository.ArchitectRepository;
import it.ArchitectPlan.repository.ProjectRepository;
import it.ArchitectPlan.repository.RequestRepository;
import it.ArchitectPlan.service.ImageService;
import jakarta.validation.Valid;

@Controller
public class ProjectController {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectValidator projectValidator;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ArchitectRepository architectRepository;
	
	@Autowired
	private RequestRepository requestRepository;

	
/* ****************base mapping******************/
	
	
	@GetMapping("/project")
	public String projects(Model model) {
		model.addAttribute("projects", this.projectRepository.findAll());
		return "projects";
	}
	
	@GetMapping("/project/{id}")
	public String projects(@PathVariable("id")Long id,Model model) {
		model.addAttribute("project", this.projectRepository.findById(id).get());
		return "project.html";
	}
	
	@GetMapping("/formSearchProject")
	public String formSearchProject() {
		return "formSearchProject.html";
	}

	@PostMapping("/searchProject")
	public String searchProject(Model model, @RequestParam String name) {
		model.addAttribute("projects", this.projectRepository.findProjectByName(name));
		return "foundProject.html";
	}
	
/* ***************************mapping for architect******************************
****************************************************************************/
	@GetMapping(value="/architect/indexProject")
	public String architectIndexProject() {
		return "architect/indexProject.html";
	}
	
	@GetMapping(value="/architect/formNewProject")
	public String architectFormNewProject(Model model) {
		model.addAttribute("project", new Project());
		return "architect/formNewProject.html";
	}

	@PostMapping("/architect/project")
	public String architectNewProject(@Valid @ModelAttribute("project") Project project, 
			@RequestParam("image") MultipartFile image, BindingResult bindingResult, Model model) {
		this.projectValidator.validate(project, bindingResult);
		this.imageService.validate(image, bindingResult);
		if(!bindingResult.hasErrors()) {
			String imageName = this.imageService.storeImage(image);
			project.setUrl_image(imageName);
			this.projectRepository.save(project);
			model.addAttribute("project", project);
			return "project.html";
		}
		else {
			return "architect/formNewProject.html";
		}
	}

	@GetMapping(value="/architect/manageProjects")
	public String architectManageProjects(Model model) {
		model.addAttribute("projects", this.projectRepository.findAll());
		return "architect/manageProjects.html";
	}

	@GetMapping(value="/architect/deleteProjects/{id}")
	@Transactional //non c'è più il progetto allora la richiesta viene cancellata
	public String architectDeleteProjects(@PathVariable("id") Long id, Model model) {
		Project project = this.projectRepository.findById(id).get();
		String imageName = project.getUrl_image();
		this.imageService.deleteImage(imageName);
		 Request request = project.getRequest();
		    if (request != null) {
		        this.requestRepository.deleteById(request.getId());
		    }
		this.projectRepository.deleteById(project.getId());
		model.addAttribute("projects", this.projectRepository.findAll());
		return "redirect:/architect/manageProjects";
		
	}

	@GetMapping(value="/architect/formUpdateProject/{id}")
	public String architectFormUpdateProject(@PathVariable("id") Long id, Model model) {
		model.addAttribute("project", this.projectRepository.findById(id).get());
		return "architect/formUpdateProject.html";
		
	}
	
	@GetMapping(value="/architect/linkRequestToProject/{id}")
	public String architectLinkRequestToProject(@PathVariable("id") Long id, Model model) {
		model.addAttribute("requests", this.requestRepository.findAll());
		model.addAttribute("project", this.projectRepository.findById(id).get());
		return "architect/linkRequestToProject.html";
	}
	
	@GetMapping(value="/linkRequest/{requestid}/{projectid}")
	public String architectLinkRequest(@PathVariable("requestid") Long requestid, @PathVariable("projectid") Long projectid, Model model) {
		model.addAttribute("request", this.requestRepository.findById(requestid).get());
		model.addAttribute("project", this.projectRepository.findById(projectid).get());
		this.requestRepository.findById(requestid).get().setProject(this.projectRepository.findById(projectid).get());
		this.projectRepository.findById(projectid).get().setRequest(this.requestRepository.findById(requestid).get());
		model.addAttribute("projects", this.projectRepository.findAll());
		return "architect/manageProjects.html";
		
	}
	
	@GetMapping("/architect/updateRequest/{projectid}/{requestid}")
	public String updateRequest(@PathVariable("projectid") Long projectId, @PathVariable("requestid") Long requestId, Model model) {
		
		Request request = this.requestRepository.findById(requestId).get();
		Project project = this.projectRepository.findById(projectId).get();
		project.setRequest(request);
		model.addAttribute("request", request);
		model.addAttribute("project", project);

		return "redirect:/architect/linkRequestToProject/{projectid}";
		/*Da riaggiungere admin/ */
	}
	
	@GetMapping(value="/architect/addRequestToProject/{requestId}/{projectId}")
	public String addRequestToProject(@PathVariable("requestId") Long requestId, @PathVariable("projectId") Long projectId, Model model) {
		Project project = this.projectRepository.findById(projectId).get();
		Request request = this.requestRepository.findById(requestId).get();
		
		if(project.getRequest()!=null) {
			project.getRequest().setProject(null);
			this.requestRepository.save(project.getRequest());
			project.setRequest(null);
		}
		
		project.setRequest(request);
		request.setProject(project);
		this.projectRepository.save(project);
		this.requestRepository.save(request);


		model.addAttribute("project", project);
		model.addAttribute("request", request);

		return "redirect:/architect/formUpdateProject/{projectId}";
	}

	
/* ***************************mapping for admin******************************
 ****************************************************************************/
	
	/*@GetMapping(value="/admin/formNewProject")
	public String formNewProject(Model model) {
		model.addAttribute("project", new Project());
		return "admin/formNewProject.html";
	}*/

	/*@PostMapping("/admin/project")
	public String newProject(@Valid @ModelAttribute("project") Project project, 
			@RequestParam("image") MultipartFile image, BindingResult bindingResult, Model model) {
		this.projectValidator.validate(project, bindingResult);
		this.imageService.validate(image, bindingResult);
		if(!bindingResult.hasErrors()) {
			String imageName = this.imageService.storeImage(image);
			project.setUrl_image(imageName);
			this.projectRepository.save(project);
			model.addAttribute("project", project);
			return "project.html";
		}
		else {
			return "admin/formNewProject.html";
		}
	}*/

	/*@GetMapping(value="/admin/manageProjects")
	public String manageProjects(Model model) {
		model.addAttribute("projects", this.projectRepository.findAll());
		return "admin/manageProjects.html";
	}*/

	/*@GetMapping(value="/admin/deleteProjects/{id}")
	public String deleteProjects(@PathVariable("id") Long id) {
		Project project = this.projectRepository.findById(id).get();
		String imageName = project.getUrl_image();
		this.imageService.deleteImage(imageName);
		this.projectRepository.deleteById(id);
		return "redirect:/admin/manageProjects";
	}*/

	/*@GetMapping(value="/admin/formUpdateProject/{id}")
	public String formUpdateProject(@PathVariable("id") Long id, Model model) {
		model.addAttribute("project", this.projectRepository.findById(id).get());
		return "admin/formUpdateProject.html";
	}*/

	@GetMapping(value="/admin/addArchitect/{id}")
	public String addArchitect(@PathVariable("id") Long id, Model model) {
		model.addAttribute("architects", this.architectRepository.findAll());
		model.addAttribute("project", this.projectRepository.findById(id).get());
		return "admin/architectsToAdd.html";
	}
	
	@GetMapping(value="/admin/linkArchitectToProject/{id}")
	public String adminLinkArchitectToProject(@PathVariable("id") Long id, Model model) {
		model.addAttribute("projects", this.projectRepository.findAll());
		model.addAttribute("architect", this.architectRepository.findById(id).get());
		return "admin/linkArchitectToProject.html";
	}

	
	@GetMapping("/admin/setArchitectToProject/{architectId}/{projectId}")
	public String setArchitectToProject(@PathVariable("architectId") Long architectId, @PathVariable("projectId") Long projectId, Model model) {

		Architect architect = this.architectRepository.findById(architectId).get();
		Project project = this.projectRepository.findById(projectId).get();
		project.setArchitect(architect);
		this.projectRepository.save(project);

		model.addAttribute("project", project);
		return "architect/formUpdateProject.html";
		
	}
	
/*  @GetMapping(value="/architect/removeRequestFromProject/{requestId}/{projectId}")
	public String removeRequestFromProject(@PathVariable("requestId") Long requestId, @PathVariable("projectId") Long projectId, Model model) {
		Project project = this.projectRepository.findById(projectId).get();
		Request request = this.requestRepository.findById(requestId).get();
		
		if(project.getRequest().equals(request)) {
			project.setRequest(null);
			request.setProject(null);
			this.projectRepository.save(project);
			this.requestRepository.save(request);
		}
		model.addAttribute("project", project);
		model.addAttribute("request", request);

		return "formUpdateProject.html";
	}
	*/
}
