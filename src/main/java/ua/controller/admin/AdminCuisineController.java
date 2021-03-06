package ua.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import ua.model.entity.Cuisine;
import ua.model.filter.SimpleFilter;
import ua.service.CuisineService;
import ua.validation.flag.CuisineFlag;

import java.sql.SQLException;

import static ua.controller.ControllerUtils.buildParams;

@Controller
@RequestMapping("/admin/adminCuisine")
@SessionAttributes("cuisine")
public class AdminCuisineController {

    private static final String REDIRECT_ADMIN_ADMIN_CUISINE = "redirect:/admin/adminCuisine";
    private final CuisineService cuisineService;

    private String error = "";

    @Autowired
    public AdminCuisineController(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    @ModelAttribute("cuisine")
    public Cuisine getForm() {
        return new Cuisine();
    }

    @ModelAttribute("filter")
    public SimpleFilter getFilter() {
        return new SimpleFilter();
    }

    @GetMapping
    public String show(Model model, @PageableDefault Pageable pageable,
                       @ModelAttribute("filter") SimpleFilter filter) {
        model.addAttribute("cuisines", cuisineService.findAll(pageable, filter));
        model.addAttribute("error", error);
        error = "";
        boolean hasContent = cuisineService.findAll(pageable, filter).hasContent();
        if (hasContent || pageable.getPageNumber() == 0)
            return "adminCuisine";
        else
            return REDIRECT_ADMIN_ADMIN_CUISINE + buildParams(false, pageable, filter.getSearch());
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, @PageableDefault Pageable pageable,
                         @ModelAttribute("filter") SimpleFilter filter) {
        cuisineService.deleteById(id);
        boolean hasContent = cuisineService.findAll(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_CUISINE + buildParams(hasContent, pageable, filter.getSearch());
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public String databaseError() {
        error = "You can't deleteById this cuisine because it is used!";
        return REDIRECT_ADMIN_ADMIN_CUISINE;
    }

    @PostMapping
    public String save(@ModelAttribute("cuisine") @Validated(CuisineFlag.class) Cuisine cuisine, BindingResult br,
                       Model model, SessionStatus status, @PageableDefault Pageable pageable,
                       @ModelAttribute("filter") SimpleFilter filter) {
        if (br.hasErrors())
            return show(model, pageable, filter);
        cuisineService.save(cuisine);
        return cancel(status, pageable, filter);
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model, @PageableDefault Pageable pageable,
                         @ModelAttribute("filter") SimpleFilter filter) {
        model.addAttribute("cuisine", cuisineService.findById(id));
        return show(model, pageable, filter);
    }

    @GetMapping("/cancel")
    public String cancel(SessionStatus status, @PageableDefault Pageable pageable,
                         @ModelAttribute("filter") SimpleFilter filter) {
        status.setComplete();
        boolean hasContent = cuisineService.findAll(pageable, filter).hasContent();
        return REDIRECT_ADMIN_ADMIN_CUISINE + buildParams(hasContent, pageable, filter.getSearch());
    }
}