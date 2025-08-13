package it.ArchitectPlan.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.ArchitectPlan.Validator.RequestValidator;
import it.ArchitectPlan.model.Credentials;
import it.ArchitectPlan.model.Project;
import it.ArchitectPlan.model.Request;
import it.ArchitectPlan.repository.CredentialsRepository;
import it.ArchitectPlan.repository.RequestRepository;
import jakarta.validation.Valid;


@Controller
public class RequestController {
	
	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private RequestValidator requestValidator;

	@Autowired
	private CredentialsRepository credentialsRepository;
	
/*mapping customer operation on request*/
	@GetMapping("/customer/indexRequest")
	public String getIndexRequest(Model model) {
		return "customer/indexRequest.html";
	}
	
	@GetMapping("/customer/request")
	public String showAllRequest(Model model) {
		model.addAttribute("requests", this.requestRepository.findAll());
		return "customer/requests.html";
	}
	
	@GetMapping("/customer/request/{id}")
	public String showRequest(Model model, @PathVariable("id") Long id) {
		model.addAttribute("request", this.requestRepository.findById(id).get());
		return "customer/request.html";
	}
	
	@GetMapping("/customer/formNewRequest")
	public String getFormNewRequest(Model model, Principal principal) {
	    String username = principal.getName(); // Ottieni il nome utente corrente
	    Credentials credentials = credentialsRepository.findByUsername(username).get(); // Ottieni le credenziali dal repository

	    // Verifica se l'utente ha già inviato una richiesta
	    if (requestRepository.findByUser(credentials.getUser()) != null) {
	        model.addAttribute("errorMessage", "Caro utente, hai già compilato una richiesta. Non puoi farne un'altra.");
	        return "customer/formNewRequest.html";
	    } else {
	        model.addAttribute("request", new Request());
	        return "customer/formNewRequest.html";
	    }
	}


	@PostMapping("/customer/request")
	public String newRequest(@Valid @ModelAttribute("request") Request request, BindingResult bindingResult, Model model) {
		this.requestValidator.validate(request, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.requestRepository.save(request);
			model.addAttribute("request", request);
			return "redirect:request/"+request.getId();  
		}
		else {
			return "customer/indexRequest.html";
		}
	}

	
/* ***************************mapping for architect******************************
 ****************************************************************************/
	
	@GetMapping(value="/architect/indexRequest")
	public String architectIndexRequest() {
		return "architect/indexRequest.html";
	}

	@GetMapping(value="/architect/manageRequests")
	public String architectManageRequests(Model model) {
		model.addAttribute("requests", this.requestRepository.findAll());
		return "architect/manageRequests.html";
	}
	
	@GetMapping(value="/architect/deleteRequest/{id}")
	public String eliminaRequest(@PathVariable("id") Long id) {
		Request request = this.requestRepository.findById(id).get();
		// Per ogni progetto associata alla richiesta, rimuovi la richiesta
		Project project = request.getProject();
		if (project!=null) {
			project.setRequest(null);
		}
		
		this.requestRepository.deleteById(id);
		return "redirect:/architect/manageRequests";
	}
	/* da rinserire /architect  */
/*	@GetMapping(value="/architect/deleteRequestOfProject/{id}")
	public String deleteRequest(@PathVariable("id") Long id) {
		Request request = this.requestRepository.findById(id).get();
		// Per ogni ricetta associata alla richiesta, rimuovi la richiesta
		if (request.getProject()!=null) {
		Project project = request.getProject();
		project.setRequest(null);
		
		this.requestRepository.deleteById(id);
		}
		else this.requestRepository.deleteById(id);
		return "redirect:/architect/manageRequests.html";
		/* da rinserire redirect:/architect/  */
	/*}*/ 
/* ***************************mapping for admin******************************
 ****************************************************************************/
	
	/*@GetMapping(value="/admin/formNewRequest")
	public String formNewRequest(Model model) {
		model.addAttribute("request", new Request());
		return "admin/formNewRequest.html";
	}*/
	
	/*@GetMapping(value="/admin/indexRequest")
	public String indexRequest() {
		return "admin/indexRequest.html";
	}*/
/*l'admin può farlo attraverso quello di customer in teoria
 * 
	@PostMapping("/admin/request")
	public String newRequest(@Valid @ModelAttribute("request") Request request, BindingResult bindingResult, Model model) {
		this.requestValidator.validate(request, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.requestRepository.save(request);
			model.addAttribute("request", request);
			return "request.html";
		}
		else
			return "admin/formNewRequest.html";
	}*/
	
	/*@GetMapping(value="/admin/manageRequests")
	public String manageRequests(Model model) {
		model.addAttribute("requests", this.requestRepository.findAll());
		return "admin/manageRequests.html";
	}*/
	
	
	
}
