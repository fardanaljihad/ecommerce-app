import { useState } from "react";
import { CartContext } from "./CartContext.jsx";

export default function CartProvider({ children }) {
    const [cartItems, setCartItems] = useState([]);

    function addToCart(product, quantity) {
        setCartItems((prev) => [...prev, {...product, stockReserved: quantity}]);
    }

    function removeFromCart(id) {
        setCartItems((prev) => prev.filter((item) => item.id !== id));
    }

    function clearCart() {
        setCartItems([]);
    }

    return (
        <CartContext.Provider value={{ cartItems, addToCart, removeFromCart, clearCart }}>
            {children}
        </CartContext.Provider>
    );
}
