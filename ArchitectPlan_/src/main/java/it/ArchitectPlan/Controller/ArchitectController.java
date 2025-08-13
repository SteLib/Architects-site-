package it.ArchitectPlan.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.ArchitectPlan.Validator.ArchitectValidator;
import it.ArchitectPlan.model.Architect;
import it.ArchitectPlan.model.Credentials;
import it.ArchitectPlan.repository.ArchitectRepository;
import it.ArchitectPlan.repository.ProjectRepository;
import it.ArchitectPlan.service.CredentialsService;
import it.ArchitectPlan.service.ImageService;
import it.ArchitectPlan.service.ProjectService;
import jakarta.validation.Valid;

@Controller
public class ArchitectController {
	
	@Autowired
	private ArchitectRepository architectRepository;
	
	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ImageService imageService;

	@Autowired
	private ArchitectValidator architectValidator;

	@Autowired
	private CredentialsService credentialsService;

/*azioni per l'utente occasionale*/
	
	@GetMapping("/architect")
	public String showArchitecs(Model model) {
		model.addAttribute("architects", this.architectRepository.findAll());
		return "architects.html";
	}
	
	@GetMapping("/architect/{id}")
	public String showArchitect(@PathVariable("id")Long id,Model model) {
		model.addAttribute("architect", this.architectRepository.findById(id).get());
		return "architect.html";
	}
	
/*azioni per l'admin - mapping amministratore */
	
	
	@GetMapping(value="/admin/formNewArchitect")
	public String formNewArchitect(Model model) {
		model.addAttribute("architect", new Architect());
		return "admin/formNewArchitect.html";
		
	}

	
	@GetMapping(value="/admin/indexArchitect")
	public String indexArchitect() {
		return "admin/indexArchitect.html";
		
	}

	
	@GetMapping(value="/admin/manageArchitects")
	public String manageArchitects(Model model) {
		model.addAttribute("architects", this.architectRepository.findAll());
		return "admin/manageArchitects.html";
		
	}

	@GetMapping(value="/admin/formUpdateArchitect/{id}")
	public String formUpdateArchitect(@PathVariable("id") Long id, Model model) {
		model.addAttribute("architect", this.architectRepository.findById(id).get());
		return "admin/formUpdateArchitect.html";
	}

	@GetMapping(value="/admin/deleteArchitect/{id}")
	public String eliminaArchitect(@PathVariable("id") Long id, Model model) {
		Credentials credentials = this.credentialsService.findByUserId(id);
		Architect architect = this.architectRepository.findById(id).get();
		String imageName = architect.getUrl_image();
		this.imageService.deleteImage(imageName);
		this.architectRepository.deleteById(id);
		if(credentials!=null)
			this.credentialsService.deleteById(credentials.getId());
		model.addAttribute("architects", this.architectRepository.findAll());
		return "redirect:/admin/manageArchitects";
	}

	@PostMapping("/admin/architect")
	public String newArchitect(@Valid @ModelAttribute("architect") Architect architect, 
			BindingResult bindingResult, @RequestParam("image") MultipartFile image, Model model) {
		this.architectValidator.validate(architect, bindingResult);
		this.imageService.validate(image, bindingResult);
		if(!bindingResult.hasErrors()) {
			String imageName = this.imageService.storeImage(image);
			architect.setUrl_image(imageName);
			this.architectRepository.save(architect);
			model.addAttribute("architect", architect);
			return "architect.html";
		}
		else
			return "admin/formNewArchitect.html";
	}

	/*@GetMapping("/admin/updateProjects/{id}")
	public String updateProjects(@PathVariable("id") Long id, Model model) {
		List<Project> projectsToAdd = this.projectsToAdd(id);
		model.addAttribute("projectsToAdd", projectsToAdd);
		model.addAttribute("architect", this.architectRepository.findById(id).get());
		return "admin/projectsToAdd.html";
	}*/

	/*private List<Project> projectsToAdd(Long architectId) {
		List<Project> projectsToAdd = new ArrayList<>();

		for(Project r : this.projectService.findProjectsNotInArchitect(architectId)) {
			projectsToAdd.add(r);
		}
		return projectsToAdd;
	}*/

	/*@GetMapping(value="/admin/addProjectToArchitect/{projectId}/{architectId}")
	public String addProjectToArchitect(@PathVariable("projectId") Long projectId, 
			@PathVariable("architectId") Long architectId, Model model) {
		Architect architect = this.architectRepository.findById(architectId).get();
		Project project = this.projectRepository.findById(projectId).get();

		List<Project> projects = architect.getProjects();
		projects.add(project);
		project.setArchitect(architect);
		this.projectRepository.save(project);
		this.architectRepository.save(architect);

		List<Project> projectsToAdd = projectsToAdd(architectId);

		model.addAttribute("architect", architect);
		model.addAttribute("project", project);
		model.addAttribute("projectsToAdd", projectsToAdd);

		return "admin/projectsToAdd.html";
	}*/

	/*@GetMapping(value="/admin/removeProjectFromArchitect/{projectId}/{architectId}")
	public String removeProjectFromArchitect(@PathVariable("projectId") Long projectId,
			@PathVariable("architectId") Long architectId, Model model) {
		Architect architect = this.architectRepository.findById(architectId).get();
		Project project = this.projectRepository.findById(projectId).get();
		List<Project> projects = architect.getProjects();
		projects.remove(project);
		project.setArchitect(null);
		this.projectRepository.save(project);
		this.architectRepository.save(architect);

		List<Project> projectsToAdd = projectsToAdd(architectId);

		model.addAttribute("architect", architect);
		model.addAttribute("project", project);
		model.addAttribute("projectsToAdd", projectsToAdd);

		return "admin/projectsToAdd.html";
	}*/
	
}
