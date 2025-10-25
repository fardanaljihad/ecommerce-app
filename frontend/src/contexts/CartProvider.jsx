import { useState } from "react";
import { CartContext } from "./CartContext.jsx";

export default function CartProvider({ children }) {
    const [cartItems, setCartItems] = useState([]);

    function addToCart(product, quantity) {
        setCartItems((prev) => [...prev, {product, quantity}]);
    }

    function removeFromCart(id) {
        setCartItems((prev) => prev.filter((item) => item.product.id !== id));
    }

    return (
        <CartContext.Provider value={{ cartItems, addToCart, removeFromCart }}>
            {children}
        </CartContext.Provider>
    );
}
