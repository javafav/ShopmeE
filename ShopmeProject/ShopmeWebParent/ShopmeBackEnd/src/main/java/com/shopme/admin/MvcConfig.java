package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.shopme.admin.paging.PagingAndSortingArgumentResolver;


@Configuration

public class MvcConfig  implements WebMvcConfigurer{

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		
////	String  dirName = "user-photos";
////	Path userPhotosDir =Paths.get(dirName); 
////	String userPhotoPath= userPhotosDir.toFile().getAbsolutePath();
////			
////	registry.addResourceHandler("/" + dirName + "/**" )
////	.addResourceLocations("file:/"+ userPhotoPath + "/");
////	
////	
////	String  categoryImagesName = "../category-images";
////	Path categoryImagesDir =Paths.get(categoryImagesName); 
////	String categoyrImagesPath= categoryImagesDir.toFile().getAbsolutePath();
////			
////	registry.addResourceHandler("/category-images/**" )
////	.addResourceLocations("file:/"+ categoyrImagesPath + "/");
//	
//		
//
//	exposeDirectory( "../category-images", registry);
//
//	exposeDirectory( "user-photos", registry);
//	
//	
//	exposeDirectory( "../brands-logos", registry);
//	
//	exposeDirectory( "../products-images", registry);
//	exposeDirectory( "../site-logo", registry);
//
//	}
//
//	private void exposeDirectory(String pathPattren ,ResourceHandlerRegistry registry ) {
//		Path path =Paths.get(pathPattren); 
//		String absultePath= path .toFile().getAbsolutePath();
//			String logicalPath = pathPattren.replace("../" , "") + "/**";
//		registry.addResourceHandler(logicalPath)
//		.addResourceLocations("file:/"+ absultePath + "/");
//	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
	resolvers.add(new PagingAndSortingArgumentResolver());
	}

}
