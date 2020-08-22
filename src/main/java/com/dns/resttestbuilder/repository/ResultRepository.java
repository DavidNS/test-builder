package com.dns.resttestbuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dns.resttestbuilder.entity.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {

}
