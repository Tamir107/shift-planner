package com.example.shiftplanner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private MutableLiveData<String> firstNameLiveData = new MutableLiveData<>();

    public LiveData<String> getFirstNameLiveData() {
        return firstNameLiveData;
    }

    public void setFirstName(String firstName) {
        firstNameLiveData.setValue(firstName);
    }
}