package com.shopme.common.entity.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.shopme.common.Constants;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.IdBasedEntity;



@Entity
@Table(name = "product")
public class Product  extends IdBasedEntity {


	
	@Column(nullable = false , length = 255 , unique = true)
	private String name;
	
	@Column(nullable = false , length = 255 , unique = true)
	private String alias;
	
	@Column(nullable = false , length = 512 , name = "short_description")
	private String shortDescription;
	
	@Column(nullable = false , length = 4096 , name = "full_description")
	private String fullDescription;
	
	@Column(name="created_time")
	private Date createdTime;
	
	
	@Column(name="updated_time")
	private Date updatedTime;
	
	
	private boolean enabled;
	
	@Column(name="in_stock")
	private boolean inStock;
	
	private float cost;
	
	private float price;
	
	@Column(name="discount_percent")
	private float discountPercent;
	



	private float length;
	private float width;
	private float height;
	private float weight;
	
	@Column(nullable = false  )
	private String mainImage;
	
	@OneToMany(mappedBy = "product" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();
	

	@OneToMany(mappedBy = "product" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<ProductDetail> details = new ArrayList<>();
	
	
	public Set<ProductImage> getImages() {
		return images;
	}

	public void setImages(Set<ProductImage> images) {
		this.images = images;
	}

	public List<ProductDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ProductDetail> details) {
		this.details = details;
	}


	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand  brand;

	
	public Product() {}
	
	
	
	
	public Product(int id) {
		this.id = id ;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTIme) {
		this.createdTime = createdTIme;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTIme) {
		this.updatedTime = updatedTIme;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}



	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}
	
	
	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", alias=" + alias + "]";
	}
	
	
	@Transient
	public String getMainImagePath() {
	
		
		if(mainImage == null || mainImage.isEmpty()) return "/images/image-thumbnail.png";
		
		return Constants.S3_BASE_URI +"/products-images/" + this.id + "/" + this.mainImage;
		
	}
	
	public  void addExtraImages(String imageName) {
		
		this.images.add(new ProductImage(imageName , this));
		
	}
	
	
	@Transient
	public String getImagePath() {
		if(images.isEmpty()) return "/images/image-thumbnail.png";
		
		return Constants.S3_BASE_URI + "/images/image-thumbnail.png";
	}
	
	public void addDetails(String name ,String value ) {
		
		details.add(new ProductDetail(name, value, this));
		
	}

	public boolean cotainsImageName(String imageName) {
		Iterator<ProductImage> iterator = images.iterator();
		while(iterator.hasNext()) {
			
			ProductImage image = iterator.next();
			if(image.getName().equals(imageName)) {
				return true;
				
			}
		}
		
		return false;
	}

	public void addDetails(Integer id, String name, String value) {
	this.details.add(new ProductDetail(id, name, value, this));
		
	}
	
	@Transient
	public String getShortName() {
		
		if(this.name.length() > 70) {
			
			return name.substring(0, 70).concat("...");
		}
		return this.name;
	}
	
	@Transient
	public float getDiscountPrice() {
		if(this.discountPercent > 0) {
		 return price * (100 - discountPercent ) /100 ;
	}
	return this.price ;
	
}
	
}
