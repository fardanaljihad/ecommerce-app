import { useContext, useMemo } from "react";
import { CartContext } from "../../contexts/CartContext.jsx";
import { formatNumber } from "../../libs/utils.js";
import { Link, useNavigate } from "react-router";

export default function Cart() {
    const { cartItems, removeFromCart } = useContext(CartContext);
    const navigate = useNavigate();

    const totalPrice = useMemo(() => {
        return cartItems.reduce(
            (sum, item) => sum + item.price * item.stockReserved,
            0
        );
    }, [cartItems]);

    return (
        <div className="p-6 max-w-screen-xl mx-auto">
            <h1 className="text-2xl font-semibold mb-6">My Cart</h1>

            <div className="flex flex-col gap-4">
                {cartItems.length === 0 && (
                    <p className="text-gray-500 text-center italic">Your cart is empty.</p>
                )}

                {cartItems.map((item) => (
                    <div
                        key={item.id}
                        className="flex justify-between items-start border rounded-2xl p-4 shadow-sm bg-white"
                    >
                        <div className="flex flex-col">
                            <h2 className="font-semibold text-lg">{item.name}</h2>
                            <p className="text-sm text-gray-500">
                                Price: Rp{formatNumber(item.price)}
                            </p>
                            <p className="text-sm text-gray-500">
                                Quantity: {item.stockReserved}
                            </p>

                            {item.stock < 10 && (
                                <p
                                    className="text-xs mt-3 text-red-500"
                                >
                                    Remaining Stock: {item.stock}
                                </p>
                            )}
                        </div>

                        <div className="flex flex-col items-end">
                            <p className="font-medium text-gray-700 mb-2">
                                Subtotal: Rp{formatNumber(item.price * item.stockReserved)}
                            </p>
                            <button
                                onClick={() => removeFromCart(item.id)}
                                className="px-3 py-2 mt-7 rounded-md text-xs flex items-center justify-center shadow-sm
                                bg-white border border-red-500 text-red-500 hover:bg-red-50 hover:border-red-600"
                            >
                                Remove
                            </button>
                        </div>
                    </div>
                ))}
            </div>

            <div className="mt-6 border-t pt-4 text-right">
                <p className="text-lg font-semibold">
                    Total Price: <span className="text-blue-600">Rp{formatNumber(totalPrice)}</span>
                </p>
            </div>

            <div className="mt-5 border-t pt-4 flex items-center justify-between">
                <button
                    onClick={() => navigate("/dashboard/products")}
                    className="px-5 py-3 rounded-3xl text-sm font-semibold text-gray-700 border border-gray-300 hover:bg-gray-50 transition-all"
                >
                    CONTINUE SHOPPING
                </button>
                
                <button
                    onClick={() => navigate("/dashboard/users/checkout")}
                    className={`px-6 py-3 rounded-3xl text-sm font-semibold text-white
                        ${cartItems.length === 0 ? "bg-gray-400 cursor-not-allowed" : "bg-blue-600 hover:bg-blue-500"}`}
                >
                    CHECKOUT
                </button>
            </div>
        </div>
    );
}
