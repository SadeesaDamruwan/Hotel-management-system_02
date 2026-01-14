package com.hotel.management.service;

import com.hotel.management.model.ReceptionistStaff;
import java.util.ArrayList;

public class ReceptionistStaffManagement {

    private ArrayList<ReceptionistStaff> receptionistList = new ArrayList<>();

    public void addReceptionist(ReceptionistStaff r) {
        receptionistList.add(r);
    }

    public boolean removeReceptionist(String id) {
        return receptionistList.removeIf(r -> r.getId().equals(id));
    }

    public ArrayList<ReceptionistStaff> getAllReceptionists() {
        return receptionistList;
    }

    public ReceptionistStaff findReceptionist(String id) {
        for (ReceptionistStaff r : receptionistList) {
            if (r.getId().equals(id)) return r;
        }
        return null;
    }
}
