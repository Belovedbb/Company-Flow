package com.company.go.application.service.archive;

import com.company.go.Utilities;
import com.company.go.application.port.Mapper;
import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import com.company.go.application.port.out.archive.UpdateStaffPort;
import com.company.go.application.port.out.global.UpdateUserPort;
import com.company.go.domain.archive.staff.Staff;
import com.company.go.domain.global.Constants;
import com.company.go.domain.global.User;
import com.company.go.domain.inventory.order.Order;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StaffService implements StaffUseCase {

    UpdateStaffPort staffStore;
    RegisterUserUseCase registerUserUseCase;
    UpdateUserPort userPort;


    @Autowired
    StaffService(UpdateStaffPort staffStore, UpdateUserPort userPort, RegisterUserUseCase userUseCase){
        this.staffStore = staffStore;
        this.registerUserUseCase = userUseCase;
        this.userPort = userPort;
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
            if(!StringUtils.isEmpty(criteriaModel.getId())){
                filterMap.put("id", criteriaModel.getId());
            }
        }
        return staffStore.getTotalFilteredStaff(filterMap)
                .stream().map(Mapper::convert)
                .collect(Collectors.toList());
    }

    private Map<String, Object> getStaffProperties(StaffViewModel criteriaModel) throws IOException, SQLException {
        Staff staff = criteriaModel.toStaff();
        Map<String, Object> filterMap = new HashMap<>();
        if(criteriaModel.getId() != null){
            filterMap.put("id", staff.getId());
        }
        if(!StringUtils.isEmpty(criteriaModel.getStatus())){
            filterMap.put("status", staff.getStatus());
        }
        return filterMap;
    }

    private Map<String, Object> getUserProperties(RegisterUserUseCase.RegisterUserModel userCriteriaModel) throws IOException, SQLException {
        User user = userCriteriaModel.toUser();
        Map<String, Object> filterMap = new HashMap<>();
        if(userCriteriaModel.getId() != null){
            filterMap.put("Id", user.getId());
        }
        return filterMap;
    }

    @Override
    public List<StaffViewModel> getFilteredStaffs(StaffViewModel criteriaModel, RegisterUserUseCase.RegisterUserModel userCriteriaModel, Utilities.FilterCondition condition) throws IOException, SQLException {
        List<Utilities.Filter> filters = new ArrayList<>();

        if(criteriaModel != null) {
            Utilities.Filter<Staff> staffFilter = new Utilities.Filter<>();
            staffFilter.setKlass(Staff.class);
            staffFilter.setProperties(getStaffProperties(criteriaModel));
            filters.add(staffFilter);
        }
        if(userCriteriaModel != null) {
            Utilities.Filter<User> userFilter = new Utilities.Filter<>();
            userFilter.setKlass(User.class);
            userFilter.setProperties(getUserProperties(userCriteriaModel));
            filters.add(userFilter);
        }
        return staffStore.getTotalFilteredStaff(filters, condition).stream().map(Mapper::convert).collect(Collectors.toList());
    }

    @Override
    public List<RegisterUserUseCase.RegisterUserModel> getAvailableRegisteredUsers() {
        List<Staff> activeStaffs = staffStore.getTotalStaff().stream().filter(staff -> staff.getStatus().equals(Staff.Constants.Status.ACTIVE)).collect(Collectors.toList());
        List<User> activeUsers = userPort.getAllUsers().stream().filter(user -> user.getStatus().equals(Constants.Status.ACTIVE)).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(activeStaffs)) {
            activeUsers = activeUsers.stream().filter(user -> activeStaffs.stream().noneMatch(staff -> Objects.equals(user.getId(), staff.getUser().getId()))).collect(Collectors.toList());
        }
        return activeUsers.stream().map(Mapper::convert).collect(Collectors.toList());
    }

    @Override
    public RegisterUserUseCase.RegisterUserModel findStaffUser(StaffViewModel staff) {
        return  registerUserUseCase.getUser(staff.getUserModel().getId());
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
