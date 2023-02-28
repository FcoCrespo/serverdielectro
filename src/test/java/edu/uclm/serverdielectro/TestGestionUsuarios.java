package edu.uclm.serverdielectro;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uclm.serverdielectro.exceptions.UsuarioYaExisteException;
import edu.uclm.serverdielectro.model.User;
import edu.uclm.serverdielectro.services.UserService;

@Configuration
@Profile("one")
@PropertySource("file:/usr/share/application.properties")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class TestGestionUsuarios {
	
	@Autowired
	private UserService userService;
	

	@Test 
	@Order(1) 
	@DisplayName("Registro de crespo, contrasena 1234, delegacion 00 y email crespo@prueba.es. La contrasena llegara por correo.")
	public void testRegistro() throws UsuarioYaExisteException {
		
		User user = userService.register("crespo", "1234", "admin", "00", "crespo@prueba.es");
		String pwdEncrypted = "d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db";
		assertTrue(user.getPassword().equals(pwdEncrypted));
		
	}
	
	@Test 
	@Order(2) 
	@DisplayName("Registro de crespo, delegacion 00, contrasena 1234, email crespo@prueba.es")
	void testRegistroDuplicado() {
		try {
			userService.register("crespo", "1234", "admin", "00", "crespo@prueba.es");
			fail("Se esperaba UsuarioYaExisteException");
		} catch (UsuarioYaExisteException e) {
		}
	}
	
	@Test 
	@Order(3) 
	@DisplayName("Acceso al sistema del usuario de crespo, con contrasena 1234")
	void testAcceso() {
		boolean existeUser = userService.findByUsernameAndPassword("crespo", "1234");
	
		assertTrue(existeUser);
	}
	
	@Test 
	@Order(4) 
	@DisplayName("Acceso al sistema del usuario de crespo, con contrasena 1235")
	void testAccesoInvalido() {
		
		boolean existeUser = userService.findByUsernameAndPassword("crespo", "1235");
		assertFalse(existeUser);
		
	}
	
	@Test 
	@Order(5) 
	@DisplayName("Obtener usuario por email crespo@prueba.es")
	void testEmailDuplicado() {
		try {
			String userEmail = userService.findByEmail("crespo@prueba.es");
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(userEmail);
			String emailContent = jsonNode.get("email").asText();
			
			assertEquals(emailContent,"crespo@prueba.es");
		} catch (Exception e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}
	}
	
	@Test 
	@Order(6) 
	@DisplayName("Actualizacion de datos de usuario crespo")
	void testActualizarUser() throws UsuarioYaExisteException {
		
		userService.update("crespo", "1235", "01", "crespo2@prueba.com");
		
		boolean seguir = false;
		
		boolean existe = userService.findByUsernameAndPassword("crespo", "1235");
		if(existe) {
			seguir=true;
		}
		existe = userService.findByUsernameAndEmail("crespo", "crespo2@prueba.com");
		if(existe) {
			seguir=true;
		}
		
		assertTrue(seguir);
	}
	
	@Test 
	@Order(7) 
	@DisplayName("Obtencion de usuario por su tokenpass")
	void testUsuarioToken() throws UsuarioYaExisteException {
	
		try {

			String userJSON  = userService.sendUser("crespo", "1235");
			
			JsonNode node = new ObjectMapper().readTree(userJSON);
			
			boolean userExists = userService.findByTokenPass(node.get("tokenPass").textValue());
			
			assertTrue(userExists);
			
		} catch (JsonMappingException e) {	
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		} catch (JsonProcessingException e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}

	}
	
	@Test 
	@Order(8) 
	@DisplayName("Obtener usuario por su nombre de usuario, que es crespo")
	void testUsuarioNombre() {
		
		try {
			
			String userJSON = userService.findByUsername("crespo");
			
			JsonNode node = new ObjectMapper().readTree(userJSON);
			
			boolean userExists = userService.findByTokenPass(node.get("tokenPass").textValue());
			
			assertTrue(userExists);
			
		} catch (JsonMappingException e) {	
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		} catch (JsonProcessingException e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}
	}
	
	@Test 
	@Order(9) 
	@DisplayName("Obtencion de todos los usuarios")
	void testTodosUsuarios() {
		try {
			boolean content = false;
			String jsonData = this.userService.findAll();
			if(!jsonData.isEmpty()) {
				content =true;
			}
			assertTrue(content);
			
		} catch (Exception e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}
	}
	
	@Test 
	@Order(10) 
	@DisplayName("Eliminar usuario creado crespo")
	void testEliminarUsuario() {
		try {
			this.userService.deleteUser("crespo");		
		} catch (Exception e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}
		
	}
	
}