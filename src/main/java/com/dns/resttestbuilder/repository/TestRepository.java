package com.dns.resttestbuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dns.resttestbuilder.data.Test;

public interface TestRepository extends JpaRepository<Test, Long> {

}
