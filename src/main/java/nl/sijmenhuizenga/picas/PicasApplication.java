package nl.sijmenhuizenga.picas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@SpringBootApplication
@Configuration
@EnableScheduling
public class PicasApplication implements WebMvcConfigurer {

	@Value("${mediadir}")
	Path mediadir;

	public static void main(String[] args) {
		SpringApplication.run(PicasApplication.class, args);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/media/**").addResourceLocations("file:"+ mediadir.toAbsolutePath().toString() + "/");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
	}
}
