package pl.fekeni.PekeN.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.fekeni.PekeN.entity.User;
import pl.fekeni.PekeN.repository.RoleRepository;
import pl.fekeni.PekeN.service.UserService;

import org.springframework.ui.Model;

import javax.validation.Valid;



import org.springframework.security.core.Authentication;


@Controller
public class UserControler {

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/ranking")
    public String ranking(Model model){
        model.addAttribute("userForm", new User());
        model.addAttribute("userList", userService.getAllUsers());
      //  model.addAttribute("listTab", "active");
        return "ranking";
    }


    @GetMapping("/userForm")
    public String userForm(Model model){
        model.addAttribute("userForm", new User());
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("listTab", "active");
        return "user-form/user-view";
    }

    @GetMapping("/userInfo")
    public String userInfo(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        User user = userService.getCurrentUser(currentUserEmail);

        model.addAttribute("user", user);

        return "user-form/userHome";
    }

    @GetMapping("/editUser/{id}")
    public String getEditUserForm(Model model, @PathVariable(name ="id")Long id)throws Exception{
        User userToEdit = userService.getUserById(id);

        model.addAttribute("userForm", userToEdit);
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles",roleRepository.findAll());
        model.addAttribute("formTab","active");
        model.addAttribute("editMode","true");

        return "user-form/user-view";
    }

    @PostMapping("/editUser")
    public String postEditUserForm(@Valid @ModelAttribute("userForm")User user, BindingResult result, ModelMap model) {
        if(result.hasErrors()) {
            model.addAttribute("userForm", user);
            model.addAttribute("formTab","active");
            model.addAttribute("editMode","true");
        }else {
            try {
                userService.updateUser(user);
                model.addAttribute("userForm", new User());
                model.addAttribute("listTab","active");
            } catch (Exception e) {
                model.addAttribute("formErrorMessage",e.getMessage());
                model.addAttribute("userForm", user);
                model.addAttribute("formTab","active");
                model.addAttribute("userList", userService.getAllUsers());
                model.addAttribute("roles",roleRepository.findAll());
                model.addAttribute("editMode","true");
            }
        }

        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles",roleRepository.findAll());
        return "user-form/user-view";

    }

    @GetMapping("/userForm/cancel")
    public String cancelEditUser(ModelMap model) {
        return "redirect:/userForm";
    }


    @PutMapping("/updateStrength")
    public String updateStrength() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        User user = userService.getCurrentUser(currentUserEmail);

        String stat = "strength";

            try {
                System.out.println("siemaaa");
                userService.addStat(user, stat);
            } catch (Exception e) {
                System.out.println("siema");
            }

        return "redirect:/home";
    }




    }
