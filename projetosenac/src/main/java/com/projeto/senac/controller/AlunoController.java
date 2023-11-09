package com.projeto.senac.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.projeto.senac.model.Aluno;

@Controller
public class AlunoController {
	
	@GetMapping("/inserirAluno")
	public ModelAndView insertAluno() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("aluno", new Aluno());
		mv.setViewName("Aluno/inserirAluno");
		return mv;
	}

}
