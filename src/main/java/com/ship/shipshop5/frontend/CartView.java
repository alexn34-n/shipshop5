package com.ship.shipshop5.frontend;

import com.ship.shipshop5.config.security.CustomPrincipal;
import com.ship.shipshop5.entity.Cart;
import com.ship.shipshop5.entity.Order;
import com.ship.shipshop5.entity.Product;
import com.ship.shipshop5.entity.repository.CartRepository;
import com.ship.shipshop5.entity.repository.OrderRepository;
import com.ship.shipshop5.service.CartService;
import com.ship.shipshop5.service.MailService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;

@Route("cart")
public class CartView extends VerticalLayout {


    private final Grid<Product> grid=new Grid<>(Product.class);

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final MailService mailService;
    private  final OrderRepository orderRepository;
    private final Authentication authentication;


    public CartView(CartService cartService,
                    CartRepository cartRepository,
                    MailService mailService,
                    OrderRepository orderRepository,
                    Authentication authentication) {

        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.mailService = mailService;
        this.orderRepository = orderRepository;
        this.authentication = authentication;

        initCartGrid();
    }

    private void initCartGrid() {
        var optionalCart=cartRepository.findByUser(((CustomPrincipal)authentication.getPrincipal()).getUser());
        Cart cart;
        if (optionalCart.isEmpty()){
            cart=new Cart();
            cart.setId(UUID.randomUUID());
            cart.setUser(((CustomPrincipal)authentication.getPrincipal()).getUser());
            cartRepository.save(cart);

        }else{
            cart=optionalCart.get();
        }

        grid.setItems(cart.getProductList()!=null? cart.getProductList(): Collections.emptyList());
        grid.setColumns("name","count");
        grid.setSizeUndefined();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        ListDataProvider<Product> dataProvider= DataProvider.ofCollection(cart.getProductList());
        grid.setDataProvider(dataProvider);

        grid.addColumn(new ComponentRenderer<>(item -> {
            var plusButton = new Button("+", i -> {
                cartService.increaseProductCount(item);
                grid.getDataProvider().refreshItem(item);
            });

            var minusButton = new Button("-", i -> {
                cartService.decreaseProductCount(item);
                grid.getDataProvider().refreshItem(item);
            });

            return new HorizontalLayout(plusButton, minusButton);
        }));

        var button = new Button("Создать заказ", buttonClickEvent -> {
            var order = new Order();
            order.setId(UUID.randomUUID());
            order.setCreatedAt(OffsetDateTime.now());
            orderRepository.save(order);

            cart.setOrder(order);
            cartRepository.save(cart);

            UI.getCurrent().navigate("");

            //mailService.sendMessage("alexn8996@mail.ru", "Ваш заказ успешно создан. Прибудет через несколько дней.</b>");
        });

        add(grid, button);
    }
}
//