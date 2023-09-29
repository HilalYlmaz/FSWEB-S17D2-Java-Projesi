package com.workintech.dependency.project.rest;

import com.workintech.dependency.project.model.Developer;
import com.workintech.dependency.project.model.JuniorDeveloper;
import com.workintech.dependency.project.model.MidDeveloper;
import com.workintech.dependency.project.model.SeniorDeveloper;
import com.workintech.dependency.project.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    private Map<Integer, Developer> developers;

    private Taxable taxable;

    @PostConstruct
    public void init(){
        developers = new HashMap<>();

    }

    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }


    @GetMapping("/")
    public List<Developer> get(){
        return developers.values().stream().toList();

    }

    @GetMapping("/{id}")
    public Developer getById(@PathVariable int id){
        return developers.get(id);
    }

    @PostMapping("/")
    public Developer save(@RequestBody Developer developer){

        Developer saveDeveloper;
        if (developer.getExperience().name().equalsIgnoreCase("junior")){
            saveDeveloper = new JuniorDeveloper(developer.getId(), developer.getName(),
                    developer.getSalary() - developer.getSalary() * taxable.getSimpleTaxRate(), developer.getExperience());
        } else if (developer.getExperience().name().equalsIgnoreCase("mid")) {
            saveDeveloper = new MidDeveloper(developer.getId(), developer.getName(),
                    developer.getSalary() - developer.getSalary() * taxable.getMiddleTaxRate(),
                    developer.getExperience());
        } else if (developer.getExperience().name().equalsIgnoreCase("senior")) {
            saveDeveloper = new Developer(developer.getId(), developer.getName(),
                    developer.getSalary() - developer.getSalary() * taxable.getUpperTaxRate(),
                    developer.getExperience());
        }else {
            saveDeveloper = null;
        }
        developers.put(developer.getId(),saveDeveloper);
        return developers.get(developer.getId());
    }



}
