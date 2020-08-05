package com.dns.resttestbuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dns.resttestbuilder.data.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
