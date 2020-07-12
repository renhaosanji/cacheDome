package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.vo.CategoryVO;

@Mapper
public interface CategoryDAO {

	public List<CategoryVO> selectCategory();
	
	public int insertCategory(CategoryVO categoryVO);
	public int updateCategory(CategoryVO categoryVO);

}
