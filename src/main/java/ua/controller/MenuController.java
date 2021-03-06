package ua.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import ua.model.filter.MealFilter;
import ua.service.MealService;
import ua.service.OrderService;

@Controller
public class MenuController {

    private final MealService mealService;
    private final OrderService orderService;

    public MenuController(MealService mealService, OrderService orderService) {
        this.mealService = mealService;
        this.orderService = orderService;
    }

    @ModelAttribute("mealFilter")
    public MealFilter getFilter() {
        return new MealFilter();
    }

    @GetMapping("/menu")
    public String mealMenu(Model model, @ModelAttribute("mealFilter") MealFilter filter,
                           @PageableDefault Pageable pageable) {
        model.addAttribute("meals", mealService.findAllMealIndexDTOs(filter, pageable));
        model.addAttribute("cuisines", mealService.findAllCuisinesNames());
        model.addAttribute("selectedMeals", orderService.findOrderDTOForCurrentUser());
        return "menu";
    }

    @GetMapping("/menu/addMealToOrder/{mealId}")
    public String addMealToOrder(@PathVariable String mealId) {
        orderService.addMealToOrder(mealId);
        return "redirect:/menu";
    }

}