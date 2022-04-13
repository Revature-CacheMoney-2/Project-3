package com.revature.cachemoney.backend.beans.models;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Model containing information regarding a User account.
 *
 * @author Version 2 (Phillip Vo, Josue Rodriguez, Prakash, Maikel Vera)
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(nullable = false, columnDefinition = "bit default 0")
	private boolean mfa;

	@Column
	@JsonIgnore
	private String secret;

	@Transient
	private String qrImageUri;

	public User(String firstName, String lastName, String email, String password, String username) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.username = username;
		this.mfa = false;
	}
}
