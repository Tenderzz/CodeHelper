package edu.nuist.codehelper.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PopController {
    @RequestMapping("/pop/{window}")
    public String pop( Model model, @PathVariable String window ){
        model.addAttribute( window,  true );
        return "popup/poptemplate";
    }

}