package com.company.go.archive;

import com.company.go.ControllerUtilities;
import com.company.go.Utilities;
import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.application.port.in.global.IndexUseCase;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.exceptions.ProductNotStoredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/archive/staff")
public class StaffController {

    private IndexUseCase indexer;

    private StaffUseCase staffUseCase;
    private RegisterUserUseCase registerUserUseCase;

    @Autowired
    public StaffController(IndexUseCase indexer, StaffUseCase staffUseCase, RegisterUserUseCase registerUserUseCase) {
        this.indexer = indexer;
        this.staffUseCase = staffUseCase;
        this.registerUserUseCase = registerUserUseCase;
    }

    @GetMapping
    public String setUpStaff(Model model) throws IOException, SQLException {
        StaffUseCase.StaffViewModel staff = new StaffUseCase.StaffViewModel();
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("state", Utilities.State.NEW.name());
        model.addAttribute("staff", staff);
        model.addAttribute("users", registerUserUseCase.getAllUsers());
        return "archive/staff/staff-form";
    }

    @GetMapping("/listing")
    public String setUpStaffListing(Model model) throws IOException, SQLException {
        StaffUseCase.StaffViewModel staff = new StaffUseCase.StaffViewModel();
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("staff", staff);
        model.addAttribute("staffs", staffUseCase.getAllStaffs());
        return "archive/staff/staff-listing";
    }

    @PostMapping("/listing/filter")
    public String getFilteredStaffListing(Model model, StaffUseCase.StaffViewModel staff) throws IOException, SQLException {
        RegisterUserUseCase.RegisterUserModel userModel = null;
        if(staff.getUserModel() != null && staff.getUserModel().getId() != null){
            userModel = new RegisterUserUseCase.RegisterUserModel();
            userModel.setId(staff.getUserModel().getId());
        }
        model.addAttribute("staffs", staffUseCase.getFilteredStaffs(staff,userModel, Utilities.FilterCondition.AND));
        return "archive/staff/staff-listing :: result";
    }

    @GetMapping("/create/{indexes}")
    public String createStaffs(@PathVariable("indexes") String indexes){
        List<RegisterUserUseCase.RegisterUserModel> models = registerUserUseCase.getAllUsers();
        if(!CollectionUtils.isEmpty(models)) {
            Arrays.stream(indexes.trim().replaceAll("\\s+", "").split(",")).forEach(index ->
            {
                RegisterUserUseCase.RegisterUserModel staff = models.get(Integer.parseInt(index));
                StaffUseCase.StaffViewModel staffViewModel = new StaffUseCase.StaffViewModel().createStaff(staff);
                Long previousStoredId = staffUseCase.getStaffMaxId();
                boolean isStored = staffUseCase.storeStaff(staffViewModel);
                if(isStored){
                    Long storedStaffId = staffUseCase.getStaffMaxId();
                    storedStaffId = storedStaffId != null ? storedStaffId : -3 >> 1;
                }
            });
        }
        return "redirect:/archive/staff/listing";
    }
    @PostMapping
    public String saveNewStaff(Model model, @ModelAttribute("staff")  @Valid StaffUseCase.StaffViewModel staff,
                                 BindingResult result) throws IOException, SQLException, ProductNotStoredException {
        if(result.hasErrors()){
            ControllerUtilities.storeIndexDetailsInModel(model, indexer);
            model.addAttribute("state", staff.getId() == null ? Utilities.State.NEW.name() : Utilities.State.EDIT.name());
            return "archive/staff/staff-form-mod";
        }
        if( staff.getId() == null){
            Long previousStoredId = staffUseCase.getStaffMaxId();
            boolean isStored = staffUseCase.storeStaff(staff);
            if(isStored){
                Long storedStaffId = staffUseCase.getStaffMaxId();
                storedStaffId = storedStaffId != null ? storedStaffId : -3 >> 1;
                if(previousStoredId != null && (storedStaffId <= previousStoredId ) )
                    throw new ProductNotStoredException("This staff was not saved successfully");
                return "redirect:/archive/staff/view/"+storedStaffId;
            }
        }else{
            RegisterUserUseCase.RegisterUserModel user = registerUserUseCase.getUser(staff.getUserModel().getId());
            staff.setUserModel(user);
            boolean updated = staffUseCase.editStaff(staff.getId(), staff);
            if(!updated){
                throw new ProductNotStoredException("This staff was not updated successfully");
            }
            return "redirect:/archive/staff/view/"+staff.getId();
        }
        return "redirect:/archive/staff";
    }

    @GetMapping("/view/{id}")
    public String viewStaff(@PathVariable Long id, Model model) throws IOException, SQLException {
        StaffUseCase.StaffViewModel staff = staffUseCase.viewStaff(id);
        model.addAttribute("state", Utilities.State.VIEW.name());
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("staff", staff);
        return "archive/staff/staff-form-mod";
    }

    @GetMapping("/edit/{id}")
    public String setUpEditStaff(@PathVariable Long id, Model model) throws IOException, SQLException {
        StaffUseCase.StaffViewModel staff = staffUseCase.viewStaff(id);
        model.addAttribute("state", Utilities.State.EDIT.name());
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("staff", staff);
        return "archive/staff/staff-form-mod";
    }

    @GetMapping("/delete/{id}")
    public String deleteStaff(@PathVariable Long id) throws Exception {
        boolean isDeleted = staffUseCase.deleteStaff(id);
        if(!isDeleted){
            throw new Exception();
        }
        return "redirect:/archive/staff/listing";
    }

    @GetMapping("/report")
    public String setUpStaffReport(Model model) throws IOException, SQLException {
        return "redirect:/report/archive/staff";
    }

}
