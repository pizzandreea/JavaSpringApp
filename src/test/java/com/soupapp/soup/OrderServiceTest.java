package com.soupapp.soup;

import com.soupapp.soup.dtos.OrderItemDto;
import com.soupapp.soup.exceptions.EntityNotFoundException;
import com.soupapp.soup.models.*;
import com.soupapp.soup.repositories.*;
import com.soupapp.soup.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {
    private OrderRepository orderMockRepository;

    private UserRepository userMockRepository;

    private OrderHeaderRepository orderHeaderMockRepository;
    private ShippingAddressRepository shippingAddressMockRepository;

    private SoupRepository soupMockRepository;

    private OrderItemRepository orderItemMockRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void init() {
        orderHeaderMockRepository = mock(OrderHeaderRepository.class);
        soupMockRepository = mock(SoupRepository.class);
        orderMockRepository = mock(OrderRepository.class);
        userMockRepository = mock(UserRepository.class);
        shippingAddressMockRepository = mock(ShippingAddressRepository.class);
        orderItemMockRepository = mock(OrderItemRepository.class);
        orderService = new OrderService(orderMockRepository,userMockRepository, orderHeaderMockRepository, shippingAddressMockRepository, soupMockRepository, orderItemMockRepository);

    }



    @Test
    void getStartedOrder_ExistingUserNoOpenOrder_ReturnsNewOrder() {
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        Order openOrder = null;

        when(userMockRepository.findById(userId)).thenReturn(Optional.of(user));
        Optional<User> debugOptional = userMockRepository.findById(userId);
        System.out.println("optional" + debugOptional.isPresent());

        when(orderMockRepository.findOrderStartedByUserAndStatus(user, OrderStatus.STARTED)).thenReturn(openOrder);
        when(orderMockRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.getStartedOrder(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUser().getId());
        assertEquals(OrderStatus.STARTED, result.getStatus());
        verify(userMockRepository, times(2)).findById(userId);
        verify(orderMockRepository, times(1)).save(any(Order.class));
    }


    @Test
    void getStartedOrder_ExistingUserWithOpenOrder_ReturnsOpenOrder() {
        // Arrange
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        Order openOrder = new Order();

        when(userMockRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderMockRepository.findOrderStartedByUserAndStatus(user, OrderStatus.STARTED)).thenReturn(openOrder);

        // Act
        Order result = orderService.getStartedOrder(userId);

        // Assert
        assertNotNull(result);
        assertEquals(openOrder, result);
        verify(orderMockRepository, never()).save(any(Order.class));
    }

    @Test
    void getStartedOrder_NonExistingUser_ThrowsEntityNotFoundException() {
        // Arrange
        Integer userId = 1;

        when(userMockRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.getStartedOrder(userId));
        verify(orderMockRepository, never()).save(any(Order.class));
    }

    @Test
    void addItemToOpenOrder_ValidInput() {
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        Order openOrder = new Order();
        openOrder.setUser(user);
        openOrder.setOrderHeader(new OrderHeader());
        openOrder.setOrderItems(new ArrayList<>());

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setSoupId(1);
        orderItemDto.setQuantity(2);

        Soup soup = new Soup();
        soup.setId(1);
        soup.setPrice(5.0);

        when(orderMockRepository.findOrderStartedByUserAndStatus(any(User.class), eq(OrderStatus.STARTED))).thenReturn(openOrder);
        when(soupMockRepository.findById(orderItemDto.getSoupId())).thenReturn(Optional.of(soup));
        when(userMockRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderItemMockRepository.save(any(OrderItem.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMockRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.addItemToOpenOrder(userId, orderItemDto);

        assertNotNull(result);
        assertEquals(1, result.getOrderItems().size());
        assertEquals(openOrder, result);
        verify(orderItemMockRepository, times(1)).save(any(OrderItem.class));
        verify(orderMockRepository, times(1)).save(any(Order.class));
    }

    @Test
    void addItemToOpenOrder_InvalidSoupId() {
        // Arrange
        Integer userId = 1;
        Order openOrder = new Order();
        openOrder.setUser(new User());
        openOrder.setOrderHeader(new OrderHeader());
        openOrder.setOrderItems(new ArrayList<>());

        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setSoupId(1);
        orderItemDto.setQuantity(2);

        when(orderMockRepository.findOrderStartedByUserAndStatus(any(User.class), eq(OrderStatus.STARTED))).thenReturn(openOrder);
        when(soupMockRepository.findById(orderItemDto.getSoupId())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.addItemToOpenOrder(userId, orderItemDto));
        verify(orderItemMockRepository, never()).save(any(OrderItem.class));
        verify(orderMockRepository, never()).save(any(Order.class));
    }

    @Test
    void removeItemFromOpenOrder_ValidInput() {
        Integer userId = 1;
        User user = new User();
        user.setId(userId);
        Order openOrder = new Order();
        openOrder.setUser(user);
        openOrder.setOrderHeader(new OrderHeader());
        openOrder.setOrderItems(new ArrayList<>());

        OrderItem orderItemToRemove = new OrderItem();
        orderItemToRemove.setId(1);
        orderItemToRemove.setOrder(openOrder);
        openOrder.getOrderItems().add(orderItemToRemove);

        when(orderMockRepository.findOrderStartedByUserAndStatus(any(User.class), eq(OrderStatus.STARTED))).thenReturn(openOrder);
        when(orderItemMockRepository.findById(orderItemToRemove.getId())).thenReturn(Optional.of(orderItemToRemove));
        when(orderMockRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userMockRepository.findById(userId)).thenReturn(Optional.of(user));

        Order result = orderService.removeItemFromOpenOrder(userId, orderItemToRemove.getId());

        assertNotNull(result);
        assertTrue(result.getOrderItems().isEmpty());
        assertEquals(openOrder, result);
        verify(orderItemMockRepository, times(1)).findById(orderItemToRemove.getId());
        verify(orderMockRepository, times(1)).save(any(Order.class));
    }

    @Test
    void removeItemFromOpenOrder_InvalidOrderItemId() {
        Integer userId = 1;
        Order openOrder = new Order();
        openOrder.setUser(new User());
        openOrder.setOrderHeader(new OrderHeader());
        openOrder.setOrderItems(new ArrayList<>());

        when(orderMockRepository.findOrderStartedByUserAndStatus(any(User.class), eq(OrderStatus.STARTED))).thenReturn(openOrder);
        when(orderItemMockRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.removeItemFromOpenOrder(userId, 1));
        verify(orderItemMockRepository, never()).save(any(OrderItem.class));
        verify(orderMockRepository, never()).save(any(Order.class));
    }
}
