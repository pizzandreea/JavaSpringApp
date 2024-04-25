package com.soupapp.soup.controllers;

import com.soupapp.soup.dtos.OrderItemDto;
import com.soupapp.soup.models.Order;
import com.soupapp.soup.dtos.OrderCreateDto;
import com.soupapp.soup.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/Orders")
@CrossOrigin
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



//    @GetMapping("/getAllOrders")
//    public ResponseEntity<List<Order>> getAll() {
//        try {
//            List<Order> ordersList = orderService.getAll();
//            if (ordersList.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//            return new ResponseEntity<>(ordersList, HttpStatus.OK);
//        } catch (Exception ex) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @GetMapping("/getStartedOrder/{userId}")
    public ResponseEntity<Order> getStartedOrder(@PathVariable Integer userId) {
        Order startedOrder = orderService.getStartedOrder(userId);

        if (startedOrder != null) {
            return new ResponseEntity<>(startedOrder, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/addItemToOpenOrder/{userId}")
    public ResponseEntity<Object> addItemToOpenOrder(@RequestBody OrderItemDto orderItemDto, @PathVariable Integer userId) {
        try {
            Order updatedOrder = orderService.addItemToOpenOrder(userId, orderItemDto);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/removeItemFromOpenOrder/{userId}/items/{orderItemId}")
    public ResponseEntity<Order> removeItemFromOpenOrder(
            @PathVariable Integer userId,
            @PathVariable Integer orderItemId) {
        try {
            Order updatedOrder = orderService.removeItemFromOpenOrder(userId, orderItemId);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


//    @PutMapping("/updateOrder")
//    public ResponseEntity<Order> update(@RequestBody OrderUpdateDto request) {
//        try {
//            Order updatedOrder = orderService.update(request);
//            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (Exception ex) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


}
