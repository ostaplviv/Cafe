package ua.model.request;

import ua.model.entity.Ingredient;
import ua.model.entity.Ms;
import ua.validation.flag.ComponentFlag;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

//@UniqueComponent(message = "Such a Component already exists", groups = { ComponentFlag.class})
public class ComponentRequest {

    private String id;

    @NotNull(message = "This field cannot be blank", groups = {ComponentFlag.class})
    private Ingredient ingredient;

    @NotBlank(message = "This field cannot be blank", groups = {ComponentFlag.class})
    @Pattern(regexp = "^([0-9]{1,18}\\.[0-9]{0,2})|([0-9]{1,18}\\,[0-9]{0,2})|([0-9]{1,18})| *$",
            message = "The Amount should be a number", groups = {ComponentFlag.class})
    private String amount;

    @NotNull(message = "This field cannot be blank", groups = {ComponentFlag.class})
    private Ms ms;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Ms getMs() {
        return ms;
    }

    public void setMs(Ms ms) {
        this.ms = ms;
    }
}
