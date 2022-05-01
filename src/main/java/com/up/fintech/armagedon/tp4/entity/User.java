package com.up.fintech.armagedon.tp4.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@JsonIgnore @Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
	private long id;
	@JsonProperty(access = Access.READ_ONLY) @NonNull @NotNull @Column(unique = true) @Type(type = "org.hibernate.type.UUIDCharType") 
	private UUID uuid;
	@JsonProperty(access = Access.READ_ONLY) @Valid @NonNull @NotNull @Email @Column(unique = true) 
	private String email;
}
