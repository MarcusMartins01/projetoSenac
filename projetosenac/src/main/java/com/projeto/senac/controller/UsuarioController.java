package com.projeto.senac.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import com.projeto.senac.model.Usuario;
import com.projeto.senac.repository.UsuarioRepository;
import com.projeto.senac.service.ServiceEmail;
import com.projeto.senac.service.ServiceUsuario;
import com.projeto.senac.util.Util;

@Controller
public class UsuarioController {

	@Autowired
	UsuarioRepository usuarioRepository; 
	@Autowired
	ServiceUsuario serviceUsuario;
	@Autowired
	ServiceEmail serviceEmail;
	
	@GetMapping("/")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("usuario", new Usuario());
		mv.setViewName("Login/login");
		return mv;
	}//fim get login

	@GetMapping("/home")
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("Login/index");
		return mv;
	}//fim get workSpace
	
	@PostMapping("/login")
	public ModelAndView login(@Valid Usuario usuario, BindingResult br, HttpSession session) 
			   throws NoSuchAlgorithmException, Exception {
			ModelAndView mv = new ModelAndView();
			mv.addObject("usuario", new Usuario());
			
			if(br.hasErrors()) {
				mv.setViewName("login/login");
			}
			Usuario userLogin = serviceUsuario.loginUser(usuario.getEmail(), 
					                                     Util.md5(usuario.getSenha())
					                                     );
			if (userLogin == null) {
				mv.addObject("msg", "Usuário ou senha incorreta!");
				mv.setViewName("login/login");
			}
			else {
				session.setAttribute("usuarioLogado", userLogin);
				mv.setViewName("redirect:/home");
			}
			return mv;
		}//fim login

	
	@GetMapping("/cadastro")
	public ModelAndView cadastro() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("usuario", new Usuario());
		mv.setViewName("Login/cadastro");
		return mv;
	}
	
	@PostMapping("/salvarUsuario")
	public ModelAndView cadastro(Usuario user) throws Exception{
		ModelAndView mv = new ModelAndView();
		String out = serviceUsuario.salvarUsuario(user);
		if(out!=null) {
			mv.addObject("msg", out);
			mv.addObject("usuario", new Usuario());
			mv.setViewName("Login/cadastro");
		}
		else {
			mv.setViewName("redirect:/");
		}
		return mv;
	}
	
	@GetMapping("/recuperarSenha")
	public ModelAndView recuperarSenha() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("usuario", new Usuario());
		mv.setViewName("Login/recuperar");
		return mv;
	}
	
	@PostMapping("/recuperarSenha")
	public ModelAndView recuperarSenha(Usuario user) {
		ModelAndView mv = new ModelAndView();
		Usuario aux = usuarioRepository.findByEmail(user.getEmail());
		if(aux==null) {
			mv.addObject("msg", "Email não encontrado");
			mv.addObject("usuario", new Usuario());
			mv.setViewName("Login/recuperar");
		}
		else {
			aux.setToken(Util.generateToken());
			usuarioRepository.save(aux);
			String corpo = "Use o seguinte token para redefinir a senha: " + aux.getToken();
			aux.setToken("");
			mv.addObject("usuario", aux);
			serviceEmail.sendEmail("senaclpoo@gmail.com", aux.getEmail(), "Recuperação de senha", corpo);
			mv.setViewName("Login/atualizando");
		}
		return mv;
	}

}
