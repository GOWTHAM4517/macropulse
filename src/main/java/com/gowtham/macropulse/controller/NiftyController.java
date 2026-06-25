package com.gowtham.macropulse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gowtham.macropulse.model.NiftyPrice;
import com.gowtham.macropulse.service.NiftyService;

@RestController
public class NiftyController {

    private final NiftyService niftyService;

    public NiftyController(NiftyService niftyService) {
        this.niftyService = niftyService;
    }

    @GetMapping("/nifty")
    public NiftyPrice getNiftyPrice() {
        return niftyService.getNiftyPrice();
    }
}
