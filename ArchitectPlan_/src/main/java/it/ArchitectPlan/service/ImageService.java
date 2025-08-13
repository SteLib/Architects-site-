package it.ArchitectPlan.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

	public String storeImage(MultipartFile image) {
		if (image.isEmpty()) {
			return null;
		}
		try {
			String imageName = "/images/" + System.currentTimeMillis() + "_" +image.getOriginalFilename();
			Path imagePath = Paths.get("src/main/resources/static" + imageName);
			Files.copy(image.getInputStream(), imagePath);
			return imageName;
		} catch (Exception e) {
			throw new RuntimeException("Non è stato possibile salvare l'immagine.", e);
		}
	}
	
	public void deleteImage(String imageName) {
		try {
	        Path imagePath = Paths.get("src/main/resources/static" + imageName);
	        Files.deleteIfExists(imagePath);
	    } catch (Exception e) {
	        throw new RuntimeException("Non è stato possibile eliminare l'immagine.", e);
	    }
	}


	public void validate(MultipartFile image, Errors errors) {
		if (!image.isEmpty())
			if(!image.getContentType().startsWith("image"))
				errors.reject("image.notAnImage");
	}

}
