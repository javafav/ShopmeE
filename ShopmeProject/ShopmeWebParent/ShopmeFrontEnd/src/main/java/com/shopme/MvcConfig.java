package com.shopme;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig  implements WebMvcConfigurer{

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

	exposeDirectory( "../category-images", registry);


	exposeDirectory( "../brands-logos", registry);
	exposeDirectory( "../products-images", registry);
	exposeDirectory( "../site-logo", registry);
	}

	private void exposeDirectory(String pathPattren ,ResourceHandlerRegistry registry ) {
		Path path =Paths.get(pathPattren); 
		String absultePath= path .toFile().getAbsolutePath();
			String logicalPath = pathPattren.replace("../" , "") + "/**";
		registry.addResourceHandler(logicalPath)
		.addResourceLocations("file:/"+ absultePath + "/");
	}

}
