package com.yh.springstore.service;

import java.util.List;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yh.springstore.exception.APIException;
import com.yh.springstore.exception.ResourceNotFoundException;
import com.yh.springstore.model.Cart;
import com.yh.springstore.model.CartItem;
import com.yh.springstore.model.Product;
import com.yh.springstore.payload.CartDTO;
import com.yh.springstore.payload.ProductDTO;
import com.yh.springstore.repository.CartItemRepository;
import com.yh.springstore.repository.CartRepository;
import com.yh.springstore.repository.ProductRepository;
import com.yh.springstore.util.AuthUtil;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, int quantity) {
        // Find existing cart or Create new
        Cart userCart = getUserCart();

        // Get Product details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Perform Validations
        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(product.getProductId(), userCart.getCartId());
        if (cartItem != null) {
            throw new APIException("Product (" + product.getProductName() + ") already added in cart!");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Needed Quantity for Product (" + product.getProductName()
                    + ") is currently not available, Please check again later!");
        }

        // Create Cart item // then Save in DB
        CartItem newCartItem = new CartItem();
        newCartItem.setCart(userCart);
        newCartItem.setProduct(product);
        newCartItem.setProductPrice(product.getPrice());
        newCartItem.setDiscountPercent(product.getDiscountPercent());
        newCartItem.setQuantity(quantity);

        cartItemRepository.save(newCartItem);

        // Update Cart // then Save in DB
        userCart.getCartItems().add(newCartItem);
        userCart.setTotalPrice(userCart.getTotalPrice() + newCartItem.calculateTotalPrice());

        cartRepository.save(userCart);

        // Map the saved Cart entity to DTO and return
        CartDTO newCartDTO = modelMapper.map(userCart, CartDTO.class);

        // Map Cart products list to DTO manually
        List<CartItem> cartItems = userCart.getCartItems();
        Stream<ProductDTO> products = cartItems.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        });
        newCartDTO.setProducts(products.toList());

        return newCartDTO;
    }

    private Cart getUserCart() {
        // Find existing cart By loggedin user email
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }

        // Create new cart for user and Save in DB
        Cart newCart = new Cart();
        newCart.setUser(authUtil.loggedInUser());
        newCart.setTotalPrice(0.0);
        userCart = cartRepository.save(newCart);

        return userCart;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) {
            throw new APIException("No Carts yet!");
        }

        // Map each Cart in List to DTO List
        List<CartDTO> cartDTOs = carts.stream().map(cart -> {
            // Map Cart entity to DTO
            CartDTO newCartDTO = modelMapper.map(cart, CartDTO.class);

            // Map Cart products list to DTO manually
            List<CartItem> cartItems = cart.getCartItems();
            Stream<ProductDTO> products = cartItems.stream().map(item -> {
                ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                productDTO.setQuantity(item.getQuantity());
                return productDTO;
            });
            newCartDTO.setProducts(products.toList());

            return newCartDTO;
        }).toList();

        return cartDTOs;
    }

    @Override
    public CartDTO getCartForLoggedInUser() {
        // Find existing cart or Create new
        Cart userCart = getUserCart();

        // Map the saved Cart entity to DTO and return
        CartDTO newCartDTO = modelMapper.map(userCart, CartDTO.class);

        // Map Cart products list to DTO manually
        List<CartItem> cartItems = userCart.getCartItems();
        Stream<ProductDTO> products = cartItems.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        });
        newCartDTO.setProducts(products.toList());

        return newCartDTO;
    }

    @Override
    public CartDTO updateProductQuantityInCart(Long productId, int quantity) {
        // Validation // Check if quantity less than 1
        if (quantity < 1) {
            throw new APIException("Quantity minimum value is 1 !");
        }

        // Find existing cart or Create new
        Cart userCart = getUserCart();

        // Get Product details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Validation // Check if Product quantity enough for the new needed quantity
        if (product.getQuantity() < quantity) {
            throw new APIException("Needed Quantity for Product (" + product.getProductName()
                    + ") is currently not available, Please check again later!");
        }

        // Find Cart item of the product
        CartItem cartItem = cartItemRepository.findByProductIdAndCartId(product.getProductId(), userCart.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "productId or cartId", productId));
        

        // Validation // Check if the needed quantity is same as current
        if (cartItem.getQuantity() == quantity) {
            throw new APIException("No Change! " + quantity + " is already added for this product.");
        }

        // Update Item Quantity and Prices // then Save in DB
        cartItem.setQuantity(quantity);
        cartItem.updatePriceFromProduct();
        cartItemRepository.save(cartItem);

        // Update Cart // then Save in DB
        userCart.getCartItems().add(cartItem);
        userCart.setTotalPrice(userCart.getTotalPrice() + cartItem.calculateTotalPrice());

        cartRepository.save(userCart);

        // Map the saved Cart entity to DTO and return
        CartDTO newCartDTO = modelMapper.map(userCart, CartDTO.class);

        // Map Cart products list to DTO manually
        List<CartItem> cartItems = userCart.getCartItems();
        Stream<ProductDTO> products = cartItems.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        });
        newCartDTO.setProducts(products.toList());

        return newCartDTO;
    }

}
