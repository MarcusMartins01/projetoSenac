package com.projeto.senac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.senac.exception.CriptoExistsException;
import com.projeto.senac.model.Usuario;
import com.projeto.senac.repository.UsuarioRepository;
import com.projeto.senac.util.Util;

@Service
public class ServiceUsuario {

	@Autowired
	UsuarioRepository usuarioRepository;

	public String salvarUsuario(Usuario user) throws Exception {
		try {
			if (usuarioRepository.findByEmail(user.getEmail()) != null) {
				return "Existe um email cadastrado para " + user.getEmail();
			}
			user.setSenha(Util.md5(user.getSenha()));
		} catch (Exception e) {
			throw new CriptoExistsException("Erro na criptografia da senha!");
		}
		usuarioRepository.save(user);
		return null;
	}// fim salvarUsuario

	public Usuario loginUser(String email, String senha) throws Exception {
		Usuario userLogin = usuarioRepository.buscarLogin(email, senha);
		return userLogin;
	}

}// fim da classe
