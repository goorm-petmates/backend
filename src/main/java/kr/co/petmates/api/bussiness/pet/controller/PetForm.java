package kr.co.petmates.api.bussiness.pet.controller;

import lombok.Getter;

@Getter
public class PetForm {
    private String name;

    public void setName(String name) {
        this.name = name;
    }
}
