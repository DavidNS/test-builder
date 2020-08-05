package com.dns.resttestbuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dns.resttestbuilder.data.JSONModel;

public interface JSONModelRepository  extends JpaRepository<JSONModel, Long> {

}
