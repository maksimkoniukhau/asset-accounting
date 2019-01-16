package com.maks.assetaccounting.controller;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@Slf4j
public class SecurityController {
    private final UserService userService;

    @Autowired
    public SecurityController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        log.info("registration page");
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @PostMapping("/registration")
    public ModelAndView registerUserAccount(
            @ModelAttribute("user") @Valid UserDto accountDto,
            BindingResult result,
            WebRequest request,
            Errors errors) {
        log.info("registration {}", accountDto);

        if (!result.hasErrors()) {
            final UserDto userFromDb = userService.getByName(accountDto.getUsername());
            if (userFromDb != null) {
                throw new IllegalArgumentException(userService.get(userFromDb.getId()) + " already exists");
            }
            userService.create(accountDto);
        }
        if (result.hasErrors()) {
            return new ModelAndView("registration", "user", accountDto);
        } else {
//            return new ModelAndView("successRegister", "user", accountDto);
            return new ModelAndView("registration", "user", accountDto);
        }
    }
}
