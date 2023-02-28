package edu.uclm.serverdielectro.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entidad usuario en la base de datos.
 * 
 * @author FcoCrespo
 */
@Entity
@Table(name="users",
		indexes = {
		@Index(unique = true, columnList = "username")
})
public class User implements Comparable<User> {
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	/**
	 * username.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String username;
	/**
	 * Password.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String password;
	/**
	 * Role.
	 * 
	 * @author FcoCrespo
	 */
	private String role;

	/**
	 * tokenPass.
	 * 
	 * @author FcoCrespo
	 */
	private String tokenpass;

	/**
	 * delegacion.
	 * 
	 * @author FcoCrespo
	 */
	private String delegation;

	/**
	 * email.
	 * 
	 * @author FcoCrespo
	 */
	private String email;

	/**
	 * Constructor de User.
	 * 
	 * @author FcoCrespo
	 */
	
	
	public User() {
		this.id = UUID.randomUUID().toString();
	}
	
	public User(@NotNull final String username, @NotNull final String password, final String role, final String delegation, @NotNull String email) {
		this.id = UUID.randomUUID().toString();
		this.username = username;
		this.setPassword(password);
		this.role = role;
		this.delegation = delegation;
		this.email = email;
	}

	@Override
	public int compareTo(User o) {
		return this.getUsername().compareTo(o.getUsername());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		User other = (User) obj;
		return this.getUsername() == other.getUsername();

	}

	@Override
	public int hashCode() {
		return this.hashCode();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String pwd) {

		this.password = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);

	}


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getDelegation() {
		return delegation;
	}

	public void setDelegation(String delegation) {
		this.delegation = delegation;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getTokenpass() {
		return tokenpass;
	}

	public void setTokenpass(String tokenpass) {
		this.tokenpass = tokenpass;
	}

	public void newTokenpass() {
		this.tokenpass = UUID.randomUUID().toString();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", role="
				+ role + ", delegation=" + delegation + ", tokenPass=" + tokenpass + "]";
	}

}