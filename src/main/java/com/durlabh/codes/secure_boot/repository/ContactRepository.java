package com.durlabh.codes.secure_boot.repository;

import com.durlabh.codes.secure_boot.model.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {
	
	
}
