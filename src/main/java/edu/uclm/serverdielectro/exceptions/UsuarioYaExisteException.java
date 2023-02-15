package edu.uclm.serverdielectro.exceptions;

public class UsuarioYaExisteException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public UsuarioYaExisteException() {
		super("El usuario ya existe en el sistema.");
	}

}