package com.company.go.application.service.archive;

import com.company.go.application.port.Mapper;
import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.application.port.out.archive.UpdateStaffPort;
import com.company.go.application.port.out.global.UpdateUserPort;
import com.company.go.domain.archive.staff.Staff;
import com.company.go.domain.global.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StaffService implements StaffUseCase {

    UpdateStaffPort staffStore;
    RegisterUserUseCase registerUserUseCase;


    @Autowired
    StaffService(UpdateStaffPort staffStore, RegisterUserUseCase userUseCase){
        this.staffStore = staffStore;
        this.registerUserUseCase = userUseCase;
    }

    @SneakyThrows({SQLException.class, IOException.class})
    @Override
    public boolean storeStaff(StaffViewModel model) {
        Staff staff = model.toStaff();
        return staffStore.storeStaff(staff);
    }

    @SneakyThrows({SQLException.class, IOException.class})
    @Override
    public boolean editStaff(Long id, StaffViewModel currentModel) {
        Staff staff = currentModel.toStaff();
        return staffStore.updateStaff(id, staff);
    }

    @Override
    public StaffViewModel viewStaff(Long id) {
        return Mapper.convert(staffStore.getStaff(id));
    }

    @Override
    public List<StaffViewModel> getAllStaffs() {
        return staffStore.getTotalStaff().stream().map(Mapper::convert).collect(Collectors.toList());
    }

    @Override
    public List<StaffViewModel> getFilteredStaffs(StaffViewModel criteriaModel) {
        Map<String, Object> filterMap = new HashMap<>();
        if(criteriaModel != null){
            if(!StringUtils.isEmpty(criteriaModel.getUserModel().getEmail())){
                filterMap.put("email", criteriaModel.getUserModel().getEmail());
            }
        }
        return staffStore.getTotalFilteredStaff(filterMap)
                .stream().map(Mapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegisterUserUseCase.RegisterUserModel> getAvailableRegisteredUsers() {
        return registerUserUseCase.getAllUsers()
                /*.stream().filter(e -> e.getStatus().equals("ACTIVE")).collect(Collectors.toList())*/;
    }

    @Override
    public boolean deleteStaff(Long id) {
        return staffStore.removeStaff(id);
    }

    @Override
    public Long getStaffMaxId() {
        return staffStore.getStaffMaxId();
    }



}
