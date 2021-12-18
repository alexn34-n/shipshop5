package com.ship.shipshop5.frontend;

import com.ship.shipshop5.TestVaadinSessionScope;
import com.ship.shipshop5.config.security.CustomPrincipal;
import com.ship.shipshop5.entity.Cart;
import com.ship.shipshop5.entity.Product;
import com.ship.shipshop5.entity.repository.CartRepository;
import com.ship.shipshop5.entity.repository.ProductRepository;
import com.ship.shipshop5.service.CartService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;
import java.util.stream.Collectors;

@Route
public class MainView extends VerticalLayout {
    private final Grid<Product> grid=new Grid<>(Product.class);

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private  final CartService cartService;
    private  final Authentication authentication;
   // private  final TestVaadinSessionScope testVaadinSessionScope;


    public MainView(ProductRepository productRepository,
                    CartRepository cartRepository,
                    CartService cartService,
                    TestVaadinSessionScope testVaadinSessionScope) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.authentication = SecurityContextHolder.getContext().getAuthentication();

        //this.testVaadinSessionScope = testVaadinSessionScope;

        initPage();
       // VaadinSession.getCurrent().getSession().setMaxInactiveInterval(20);
    }

    private void initPage() {
        initProductGrid();
        initMainPage();
    }

    private void initMainPage() {
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(initFirstRow(),grid,initAddToCartButton());
    }



    private Component initFirstRow() {
        var toCartButton=new Button("Корзина",item->{
            UI.getCurrent().navigate("cart");
        });

        var logoutButton=new Button("Выход",item->{
            SecurityContextHolder.clearContext();
            UI.getCurrent().navigate("login");
        });
             return  new HorizontalLayout(toCartButton,logoutButton);

    }

    private HorizontalLayout initAddToCartButton() {

        var addToCartButton=new Button("Добавить в корзину",items->{
        var optionalCart=cartRepository.findCarByOrderIdIsNull();
            Cart cart;
            if(optionalCart.isEmpty()){
                cart=new Cart();
                cart.setId(UUID.randomUUID());
                cart.setUser(((CustomPrincipal)authentication.getPrincipal()).getUser());
                cart.addToProductList(grid.getSelectedItems().stream().peek(p->p.setCount(1)).collect(Collectors.toSet()));
            }else{
                cart= optionalCart.get();
                cart.addToProductList(grid.getSelectedItems().stream().peek(p->p.setCount(1)).collect(Collectors.toSet()));
            }
               cartRepository.save(cart);
            Notification.show("Товар  успешно добавлен в корзину");
        });

        return  new HorizontalLayout(addToCartButton);
    }

    private void initProductGrid(){
        var products=productRepository.findAll();

        grid.setItems(products);
        grid.setColumns("name","price","count");
        grid.setSizeUndefined();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        ListDataProvider<Product> dataProvider= DataProvider.ofCollection(products);
        grid.setDataProvider(dataProvider);
        
        
        initPlusMinusButtons();
        initReviewsButtons();

    }

    private void initReviewsButtons() {
        grid.addColumn(new ComponentRenderer<>(item->{
            var seeReviewButton=new Button("Комментарии",i->{
                ComponentUtil.setData(UI.getCurrent(),"product",item);
                UI.getCurrent().navigate("review");
            });
            return  new HorizontalLayout(seeReviewButton);
        }));
    }
    private void initPlusMinusButtons() {

        grid.addColumn(new ComponentRenderer<>(item->{
            var plusButton=new Button("+",i->{
                item.incrementCount();
                productRepository.save(item);
                grid.getDataProvider().refreshItem(item);
            });

            var minusButton=new Button("-",i->{
                item.decreaseCount();
                productRepository.save(item);
                grid.getDataProvider().refreshItem(item);
            });

             return new HorizontalLayout(plusButton,minusButton);
        }));

    }
}
