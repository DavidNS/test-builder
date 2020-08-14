package com.dns.resttestbuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dns.resttestbuilder.entity.MappedValue;

public interface MappedValueRepository extends JpaRepository<MappedValue, Long>{

}
