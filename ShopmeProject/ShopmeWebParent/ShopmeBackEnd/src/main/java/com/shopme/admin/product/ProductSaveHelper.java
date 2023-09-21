package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.AmazonS3Util;
import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;

public class ProductSaveHelper {

	
	static void deleteExtraImagesWeredRemovedOnForm(Product product) {
		String extraImageDir = "products-images/" + product.getId() + "/extras";
		List<String> listObjectKeys = AmazonS3Util.listFolder(extraImageDir);
		
		for (String objectKey : listObjectKeys) {
			int lastIndexOfSlash = objectKey.lastIndexOf("/");
			String fileName = objectKey.substring(lastIndexOfSlash + 1, objectKey.length());
			
			if (!product.cotainsImageName(fileName)) {
				AmazonS3Util.deleteFile(objectKey);
				System.out.println("Deleted extra image: " + objectKey);
			}
		}
	}


 static void setExistingExtraImagesNames(String[] imageIDs, String[] imageNames, Product product) {
		if (imageIDs == null || imageIDs.length == 0)return;
			

		Set<ProductImage> images = new HashSet<>();
		for (int count = 0; count < imageIDs.length; count++) {

			Integer id = Integer.parseInt(imageIDs[count]);
			String name = imageNames[count];
			images.add(new ProductImage(id, name, product));
		}
		product.setImages(images);

	}

 static void setDetailNameAndVlaues(String[] detailNames, String[] detailIDs, String[] detailValueNames,
			Product product) {

		if (detailNames == null || detailNames.length == 0)
			return;

		for (int count = 0; count < detailNames.length; count++) {
			String name = detailNames[count];
			String value = detailValueNames[count];
			Integer id = Integer.parseInt(detailIDs[count]);
			if (id != 0) {
				product.addDetails(id, name, value);

			} else if (!name.isEmpty() && !value.isEmpty()) {

				product.addDetails(name, value);
			}

		}

	}

	static void saveUploadedImages(MultipartFile mainImageMultipart, 
			MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "products-images/" + savedProduct.getId();
			
			List<String> listObjectKeys = AmazonS3Util.listFolder(uploadDir + "/");
			for (String objectKey : listObjectKeys) {
				if (!objectKey.contains("/extras/")) {
					AmazonS3Util.deleteFile(objectKey);
				}
			}
			
			AmazonS3Util.uploadFile(uploadDir, fileName, mainImageMultipart.getInputStream());					
		}
		
		if (extraImageMultiparts.length > 0) {
			String uploadDir = "products-images/" + savedProduct.getId() + "/extras";
			
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (multipartFile.isEmpty()) continue;
				
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());	
			}
		}
		
	}

 static void setNewExtraImaageName(MultipartFile[] extraImageMultipartFile, Product product) {
		if (extraImageMultipartFile.length > 0) {
			for (MultipartFile multipartFile : extraImageMultipartFile) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					if (!product.cotainsImageName(fileName))
						product.addExtraImages(fileName);
				}

			}

		}

	}

 static void setMainImage(MultipartFile mainIamgeMultipartFile, Product product) {
		if (!mainIamgeMultipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainIamgeMultipartFile.getOriginalFilename());
			product.setMainImage(fileName);
		}

	}

	
}
