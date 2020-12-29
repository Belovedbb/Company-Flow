package com.company.go.archive.staff;

import com.company.go.Utilities;
import com.company.go.application.port.out.archive.UpdateStaffPort;
import com.company.go.archive.mapper.StaffMapper;
import com.company.go.archive.staff.repo.StaffRepository;
import com.company.go.domain.archive.staff.Staff;
import com.company.go.domain.inventory.order.Order;
import com.company.go.global.repo.UserRepository;
import com.company.go.inventory.mapper.OrderMapper;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
public class StaffStoreAdapter implements UpdateStaffPort {

    private UserRepository userRepo;
    private StaffRepository staffRepo;

    @Autowired
    public StaffStoreAdapter(UserRepository userRepo, StaffRepository staffRepo) {
        this.userRepo = userRepo;
        this.staffRepo = staffRepo;
    }


    @Override
    public boolean storeStaff(Staff staff) {
        try{
            StaffEntity staffEntity = StaffMapper.mapStaffDomainToStaffEntity(staff);
            staffRepo.createStaff(staffEntity);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateStaff(Long id, Staff currentStaff) {
        try {
            StaffEntity staffEntity = StaffMapper.mapStaffDomainToStaffEntity(currentStaff);
            staffRepo.updateStaff(staffEntity);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public Staff getStaff(Long id) {
        return StaffMapper.mapStaffEntityToStaffDomain(staffRepo.getStaff(id));
    }

    @Override
    public boolean removeStaff(Long id) {
        try{
            staffRepo.deleteStaff(id);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public Long getStaffMaxId() {
        try{
            return staffRepo.getMaxId(StaffEntity.class);
        }catch (NoResultException ex){
            return null;
        }
    }

    @Override
    public List<Staff> getTotalStaff() {
        return staffRepo.getAllRows(StaffEntity.class).stream()
                .map(StaffMapper::mapStaffEntityToStaffDomain).collect(Collectors.toList());
    }

    @Override
    public List<Staff> getTotalFilteredStaff(Map<String, Object> parameters) {
        return staffRepo.filterStaff(parameters).stream()
                .map(StaffMapper::mapStaffEntityToStaffDomain).collect(Collectors.toList());
    }

    @Override
    public List<Staff> getTotalFilteredStaff(List<Utilities.Filter> filterList, Utilities.FilterCondition condition) {
        return staffRepo.filterStaff(StaffMapper.mapErasureFilter(filterList), condition).stream().map(StaffMapper::mapStaffEntityToStaffDomain).collect(Collectors.toList());
    }


}
