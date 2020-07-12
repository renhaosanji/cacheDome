package com.example.demo.service;

import java.util.List;

import com.example.demo.vo.CategoryVO;
import com.example.demo.vo.ProductVO;

public interface ProductService {
	
	public List<ProductVO> selectProductNo(ProductVO productVO);
	
	public List<CategoryVO> selectCategory();
	
	public List<ProductVO> selectAllProductInfo();
	
	public ProductVO selectProductInfo(ProductVO productVO);
	
	public int insertCategory(CategoryVO categoryVO);
	
	public int updateCategory(CategoryVO categoryVO);
	
	public int insertProduct(ProductVO productVO);
	public int updateProduct(ProductVO productVO);
	
	public int deleteProduct(ProductVO productVO);
	public int deleteCategory(CategoryVO categoryVO);
	
	
}
