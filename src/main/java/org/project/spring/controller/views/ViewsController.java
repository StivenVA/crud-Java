package org.project.spring.controller.views;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewsController {

    @GetMapping("/home")
    public String home(){
        return "index";
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/imagenes")
    public String imagenes(){
        return "imagenes";
    }

    @GetMapping("usuarios")
    public String usuarios(){
        return "usuarios";
    }
}
