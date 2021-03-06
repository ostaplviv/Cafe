package ua.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.dto.MealDTO;
import ua.dto.OrderDTO;
import ua.dto.PlaceDTO;
import ua.model.entity.Order;
import ua.model.entity.OrderStatus;
import ua.model.filter.OrderFilter;
import ua.model.request.OrderRequest;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {

    List<String> findAllMealsNames();

    List<String> findStatusForSearch();

    List<PlaceDTO> findAllPlaceDTOs();

    PlaceDTO findPlaceDTO(String id);

    Page<OrderDTO> findAll(Pageable pageable, OrderFilter filter);

    List<OrderDTO> findOrderDTOsForTable(String tableId);

    List<MealDTO> findMealDTOsForOrder(String orderId);

    Order findOrderById(String id);

    List<Order> findOrderByPlaceId(String id);

    void saveOrder(OrderRequest request);

    OrderRequest findOrderRequestByUserId(String userId);

    OrderDTO findOrderDTOForCurrentUser();

    BigDecimal countTotalOrderPrice();

    void updateOrderStatus(String id, OrderStatus newStatus);

    void acceptOrder(String placeId);

    void addMealToOrder(String mealId);

    void removeMealFromCurrentOrder(String mealId);

    void setPlace(String placeId);
}
