package com.shopme.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.product.ProductImage;

public class ProductSaveHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);
 static void deleteExtraImagesWereRemovedOnForm(Product product) {
		String extraImagesDir = "../products-images/" + product.getId() + "/extras/";
		Path dirPath = Paths.get(extraImagesDir);

		try {

			Files.list(dirPath).forEach(file -> {

				String fileName = file.toFile().getName();
				if (!product.cotainsImageName(fileName)) {
					try {
						LOGGER.info("Deleted extra image: " + fileName);
						Files.delete(file);
					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + fileName);

					}
				}

			});

		} catch (IOException e) {
			LOGGER.error("Could not lsit the directory: " + dirPath);
		}
	}

 static void setExistingExtraImagesNames(String[] imageIDs, String[] imageNames, Product product) {
		if (imageIDs == null || imageIDs.length == 0)
			return;

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

 static void saveProductImages(Product savedProduct, MultipartFile mainIamgeMultipartFile,
			MultipartFile[] extraImageMultipartFile) throws IOException {

		if (!mainIamgeMultipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainIamgeMultipartFile.getOriginalFilename());
			String uploadMainImageDir = "../products-images/" + savedProduct.getId();

			FileUploadUtil.cleanDir(uploadMainImageDir);
			FileUploadUtil.saveFile(uploadMainImageDir, fileName, mainIamgeMultipartFile);
		}

		if (extraImageMultipartFile.length > 0) {
			String uploadExtraImagesDir = "../products-images/" + savedProduct.getId() + "/extras";
			for (MultipartFile multipartFile : extraImageMultipartFile) {

				if (multipartFile.isEmpty())
					continue;

				String fileName1 = StringUtils.cleanPath(multipartFile.getOriginalFilename());

				FileUploadUtil.saveFile(uploadExtraImagesDir, fileName1, multipartFile);

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
