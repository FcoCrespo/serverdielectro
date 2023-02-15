package edu.uclm.serverdielectro.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONObject;

import edu.uclm.serverdielectro.dao.UserDAO;
import edu.uclm.serverdielectro.exceptions.UsuarioYaExisteException;
import edu.uclm.serverdielectro.model.User;

@Service
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	/**
	 * Registra a un usuario en el sitema a partir d ela informacion introducida, le llegara una contrasena temporal por correo
	 * @param username nomrbe del usuario en el sistema a ser registrado
	 * @param role rol del usuario en el sistema a ser registrado
	 * @param email email del usuario en el sistema a ser registrado
	 * @param delegacion delegacion del usuario en el sistema a ser registrado
	 */
	public User register(String username, String password, String role, String delegacion,  String email) throws UsuarioYaExisteException{

		User usuario = new User(username, password, role, delegacion, email);
		
		try {
			this.userDAO.save(usuario);
			return usuario;
			
		} catch (DataIntegrityViolationException e) {
			throw new UsuarioYaExisteException();
		}
			
	}
	
	/**
	 * obtiene si el usuario que trata de acceder al sistema existe
	 * @param username usuario que quiere acceder
	 * @param password contrasena del usuario que quiere acceder
	 * @return devuelve true si existe y false si no ha sido encontrado en el sistema
	 */
	public boolean findByUsernameAndPassword(String username, String password) {

		User usuariologin = this.userDAO.findByUsernameAndPassword(username, org.apache.commons.codec.digest.DigestUtils.sha512Hex(password));
		return usuariologin != null;

	}
	
	/**
	 * Obtiene la informacion de un usuario a partir de su nombre de usuario
	 * @param username indica el nombre de usuario que posee en el sistema
	 * @return devuelve toda la informacion del usuario
	 */
	public String findByEmail(String email) {
		
			
     	User usuariologin = this.userDAO.findByEmail(email);
     	if(usuariologin == null) {
			return null;
		}
		
     	JSONObject userInfo = new JSONObject();
		userInfo.put("id", usuariologin.getId());
		userInfo.put("username", usuariologin.getUsername());
		userInfo.put("role", usuariologin.getRole());
		userInfo.put("tokenPass", usuariologin.getTokenPass());
		userInfo.put("delegacion", usuariologin.getDelegacion());
		userInfo.put("email", usuariologin.getEmail());

		return userInfo.toString();
	
	}
	
	/**
	 * Actualiza los datos del usuario en el sistema
	 * @param username usuario que se actualizara
	 * @param password contrasena del usuario que se actualizara
	 * @param email contrasena del usuario que se actualizara
	 * @throws UsuarioYaExisteException 
	 */
	public void update(String username, String password, String delegacion, String email) throws UsuarioYaExisteException {
	
		try {
			
			User usuario = this.userDAO.findByUsername(username);
			
			usuario.setUsername(username);
			usuario.setPassword(password);
			usuario.setDelegacion(delegacion);
			usuario.setEmail(email);
			
			this.userDAO.save(usuario);
		
		} catch (DataIntegrityViolationException e) {
			throw new UsuarioYaExisteException();
		}
		

	}
	
	/**
	 * obtiene el usuario a partir del nombre de usuario y su correo en el sistema
	 * @param username indica el nombre de usuario en el sistema
	 * @param email indica el email del usuario en el sistema
	 * @return devuelve true si existe y false si no ha sido encontrado en el sistema
	 */
	public boolean findByUsernameAndEmail(String username, String email) {
		
		User user = this.userDAO.findByUsernameAndEmail(username, email);
		return user != null;
		
	}
	
	/**
	 * Obtiene si el tokenPass pertenece a algún usuario del sistema en ese momento
	 * @param tokenpass cadena unica de caracteres perteneciente al usuario para la comprobacion de acceso temporal al sistema
	 * @return se devuelve si existe un usuario vinculado al tokenPass en el sistema
	 */
	public boolean findByTokenPass(String tokenpass) {

		User usuariologin;
		usuariologin = this.userDAO.findByTokenPass(tokenpass);
		return usuariologin != null;

	}
	
	/**
	 * Envia al usuario que trata de acceder sus datos para que acceda al sistema
	 * @param username usuario que quiere acceder
	 * @param password contrasena del usuario que quiere acceder
	 * @return se retorna los datos del usuarios con un tokenPass temporal que se corresponderá al usuario
	 * @throws UsuarioYaExisteException 
	 */
	public String sendUser(String username, String password) throws UsuarioYaExisteException {

		try {
			
			User usuariologin;
			usuariologin = this.userDAO.findByUsernameAndPassword(username, org.apache.commons.codec.digest.DigestUtils.sha512Hex(password));
			usuariologin.setUsername(username);
			usuariologin.setPassword(password);
			usuariologin.newTokenPass();
			
			this.userDAO.save(usuariologin);
			
			JSONObject userInfo = new JSONObject();
			userInfo.put("id", usuariologin.getId());
			userInfo.put("username", username);
			userInfo.put("role", usuariologin.getRole());
			userInfo.put("tokenPass", usuariologin.getTokenPass());
			userInfo.put("delegacion", usuariologin.getDelegacion());
			userInfo.put("email", usuariologin.getEmail());
	
			return userInfo.toString();
		
		} catch (DataIntegrityViolationException e) {
			throw new UsuarioYaExisteException();
		}
		
		
	}

	/**
	 * Verifica si el usuario del tokenpass es admin
	 * @param tokenpass cadena unica de caracteres perteneciente al usuario para la comprobacion de acceso temporal al sistema
	 * @return devuelve si el usuario es o no es admin
	 */
	public boolean findByTokenPassAdmin(String tokenpass) {

		User usuariologin;
		usuariologin = this.userDAO.findByTokenPass(tokenpass);
		if (usuariologin != null) {
			
			return usuariologin.getRole().equals("admin");
			
		} else {
			return false;
		}
		
	}
	
	/**
	 * Obtiene la informacion de un usuario a partir de su nombre de usuario
	 * @param username indica el nombre de usuario que posee en el sistema
	 * @return devuelve toda la informacion del usuario
	 */
	public String findByUsername(String username) {
		
			
     	User usuariologin = this.userDAO.findByUsername(username);
     	if(usuariologin == null) {
			return null;
		}
		
     	JSONObject userInfo = new JSONObject();
		userInfo.put("id", usuariologin.getId());
		userInfo.put("username", username);
		userInfo.put("role", usuariologin.getRole());
		userInfo.put("tokenPass", usuariologin.getTokenPass());
		userInfo.put("delegacion", usuariologin.getDelegacion());
		userInfo.put("email", usuariologin.getEmail());

		return userInfo.toString();
			
	
	}
	
	/**
	 * Obtiene la informacion de todos los usuarios del sistema
	 * @return devuelve a todos los usuarios del sistema
	 */
	public String findAll(){
		
		List<User> users = this.userDAO.findAll();
		
		JSONArray array = new JSONArray();
		JSONObject secureUser;
		for (int i = 0; i < users.size(); i++) {
			
			secureUser = new JSONObject();
			secureUser.put("id", users.get(i).getId());
			secureUser.put("username", users.get(i).getUsername());
			secureUser.put("role", users.get(i).getRole());
			secureUser.put("tokenPass", users.get(i).getTokenPass());
			secureUser.put("delegacion", users.get(i).getDelegacion());
			secureUser.put("email", users.get(i).getEmail());
			
			array.put(secureUser);
		}

		return array.toString();
		
	}
	
	/**
	 * Elimina un usuario del sistema
	 * @param username nombre de usuario que se quiere eliminar
	 */
	public void deleteUser(String username) {
		
		User user = this.userDAO.findByUsername(username);
		this.userDAO.delete(user);
		
	}

	//TODO
	
	
	/*
	public void recoverPassword(String username, String email) throws UsuarioYaExisteException {
		
		try {
			UUID uuid = UUID. randomUUID();
			String uuidAsString = uuid. toString();
			String temporalPassword = uuidAsString.substring(0, 12);
			User usuario = this.userDAO.findByUsernameAndEmail(username, email);
			this.userDAO.save(usuario);
			String role = usuario.getRole();
			String delegacion =  usuario.getDelegacion();
			
			sendEmail(username, temporalPassword, role, delegacion, email);
		} catch (DataIntegrityViolationException e) {
			throw new UsuarioYaExisteException();
		}
		
	}

	
	/**
	 * Ennvia un email con los datos del usuario que ha sido creado o que solicita nueva contrasena
	 * @param username usuario que ha sido creado o solicita nueva contrasena
	 * @param temporalPassword contrasena temporal que sera enviada al usuario
	 * @param role indica el rol del usuario
	 * @param delegacion indica la delegacion del usuario
	 * @param email indica el email del usuario
	 */
	/*
	private void sendEmail(String username, String temporalPassword, String role, String delegacion, String email){
		
		try {
			Properties prop = new Properties();
			prop.put("mail.smtp.host", mailhost);
	        prop.put("mail.smtp.port", mailport);
	        prop.put("mail.smtp.auth", mailauth);
	        prop.put("mail.smtp.starttls.enable", mailenable); 
	        
	        Session session = Session.getInstance(prop,
	                new javax.mail.Authenticator() {
	        			@Override
	                    protected PasswordAuthentication getPasswordAuthentication() {
	                        return new PasswordAuthentication(emailsystem, emailsystempass);
	                    }
	                });
	        
	     
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(emailsystem));
	        message.setRecipients(
	                Message.RecipientType.TO,
	                InternetAddress.parse(email)
	        );
	        message.setSubject("Data access for new user");
	        message.setText("Hello, \n\n"
	        		+ "your user information to access DielectroApp is:\n\n"
	        		+ "user: "+username+"\n"
	        		+ "password: "+temporalPassword+"\n"
	        		+ "delegacion: "+delegacion+"\n"
	        		+ "role: "+role+"\n\n"
	        		+"Remember that you can change your password if you want in User menu.\n\n"
	        		+"Thank you.\n\n"
	        		+"Kind regards.");
	
	        Transport.send(message);
	
	        String mensaje ="\nEmail enviado al correo "+email;
	        LOG.info(mensaje);
		
		} catch ( MessagingException e) {
			e.toString();
		}
		
	}
	*/
}
