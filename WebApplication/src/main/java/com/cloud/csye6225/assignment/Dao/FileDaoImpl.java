/**
 * 
 */
package com.cloud.csye6225.assignment.Dao;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.cloud.csye6225.assignment.Sql.SqlBill;
import com.cloud.csye6225.assignment.Sql.SqlFile;
import com.cloud.csye6225.assignment.entity.FileUpload;

@Repository
public class FileDaoImpl implements FileDao {
	
	@Autowired
	private SqlFile repo;

	FileDaoImpl() {
		this.repo = new SqlFile();
	}

	@Override
	public FileUpload getFileById(String id) {
		return this.repo.getFileById(id);
	}

	@Override
	public boolean deleteFileById(String id) {
		return this.repo.deleteFile(id);

	}

	@Override
	public void insertFile(FileUpload file) {
		this.repo.insertFile(file);

	}

	public void updateFiles(FileUpload files) {
		this.repo.updateFiles(files.getId(), files.getUrl());
	}
}
