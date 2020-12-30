package com.company.go.preference;

import com.company.go.ControllerUtilities;
import com.company.go.application.port.in.global.IndexUseCase;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;

@Controller
@RequestMapping("/preference")
public class PreferenceController {

    @Autowired
    private IndexUseCase indexer;

    @Autowired
    private RegisterUserUseCase userUseCase;


    @GetMapping("/profile")
    public String setUpProfilePreference(Principal principal, Model model) throws IOException, SQLException {
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        RegisterUserUseCase.RegisterUserModel user = userUseCase.getUser(principal.getName());
        model.addAttribute("user", user);
        return "preference/data/profile";
    }

    @GetMapping("/settings")
    public String setUpSettingPreference(Principal principal, Model model) throws IOException, SQLException {
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        RegisterUserUseCase.RegisterUserModel user = userUseCase.getUser(principal.getName());
        model.addAttribute("user", user);
        return "preference/data/setting";
    }

    @PostMapping("/settings")
    public String updateSettings(Model model, @ModelAttribute("user") RegisterUserUseCase.RegisterUserModel user,
                                 BindingResult result) throws Exception {
        if( user.getId() != null){
            RegisterUserUseCase.RegisterUserModel persistedUser = userUseCase.getUser(user.getId());
            persistedUser.setAddress(user.getAddress());
            persistedUser.setAlias(user.getAlias());
            persistedUser.setPhoneNumber(user.getPhoneNumber());
            boolean updated = userUseCase.updateUser(persistedUser.getId(), persistedUser);
            if(!updated){
                throw new Exception("This user detail was not updated successfully");
            }
        }
        return "redirect:/preference/profile";
    }



}