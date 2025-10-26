import { useContext, useState, useMemo } from "react";
import { CartContext } from "../../contexts/CartContext.jsx";
import { formatNumber, getUsername } from "../../libs/utils.js";
import { alertConfirm, alertSuccess } from "../../libs/alert.js";
import { useLocalStorage } from "react-use";
import { orderCreate } from "../../libs/api/OrderApi.js";
import { useNavigate } from "react-router";

export default function Checkout() {

    const { cartItems, clearCart } = useContext(CartContext);

    const [token, _] = useLocalStorage("token", "");
    const [paymentMethod, setPaymentMethod] = useState("BANK_TRANSFER");
    const navigate = useNavigate();

    const serviceFee = 1000;
    const taxRate = 0.11;
    const discount = 0;

    const subtotal = useMemo(() => {
        return cartItems.reduce(
            (sum, item) => sum + item.price * item.stockReserved,
            0
        );
    }, [cartItems]);

    const tax = subtotal * taxRate;
    const total = subtotal + serviceFee + tax - discount;

    async function handleConfirmPay() {
        if (!await alertConfirm("Are you sure you want to submit this order?")) {
            return;
        }

        const username = getUsername(token);

        const orderLineItems = cartItems.map(item => ({
            productId: item.id,
            quantity: item.stockReserved,
            price: item.price
        }));

        const response = await orderCreate(token, {
            username,
            amount: total,
            orderLineItems,
            paymentMethod
        })

        const responseBody = await response.json();

        if (response.status === 200) {
            await alertSuccess(responseBody.message);
            clearCart();
            await navigate({
                pathname: "/dashboard/products"
            });
        } else {
            await alertError(responseBody.errors);
        }
    }

    return (
        <div className="p-6 max-w-screen-xl mx-auto">
            <h1 className="text-2xl font-semibold mb-6">Checkout</h1>

            <div className="mb-6 border rounded-lg shadow-sm p-4 bg-white">
                <h2 className="text-xl text-center font-semibold mb-4 border-b pb-2">--- INVOICE ---</h2>

                {cartItems.length === 0 ? (
                    <p className="text-gray-500">Your cart is empty.</p>
                ) : (
                    <div className="flex flex-col gap-3">
                        {cartItems.map((item) => (
                            <div
                                key={item.id}
                                className="flex justify-between border-b pb-2 last:border-b-0"
                            >
                                <div className="flex flex-col">
                                    <span className="font-medium">{item.name}</span>
                                    <span className="text-sm text-gray-500">
                                        Quantity: {formatNumber(item.stockReserved)}
                                    </span>
                                </div>
                                <div className="text-right font-medium">
                                    Rp{formatNumber(item.price * item.stockReserved)}
                                </div>
                            </div>
                        ))}

                        <div className="flex justify-between mt-4 text-black font-medium">
                            <span>Subtotal</span>
                            <span>Rp{formatNumber(subtotal)}</span>
                        </div>
                        <div className="flex justify-between text-gray-600">
                            <span>Service Fee</span>
                            <span>Rp{formatNumber(serviceFee)}</span>
                        </div>
                        <div className="flex justify-between text-gray-600">
                            <span>Tax (PPN 11%)</span>
                            <span>Rp{formatNumber(Math.round(tax))}</span>
                        </div>
                        {discount > 0 && (
                            <div className="flex justify-between text-gray-600">
                                <span>Discount</span>
                                <span>- Rp{formatNumber(discount)}</span>
                            </div>
                        )}

                        <div className="flex justify-between mt-2 text-lg font-semibold border-t pt-2">
                            <span>Total</span>
                            <span>Rp{formatNumber(total)}</span>
                        </div>
                    </div>
                )}
            </div>

            <div className="mb-6 border rounded-lg shadow-sm p-4 bg-white">
                <h2 className="text-xl font-semibold mb-4">Payment Method</h2>

                <div className="flex flex-col gap-3">
                    <label className="flex items-center gap-2">
                        <input
                            type="radio"
                            name="payment"
                            value="BANK_TRANSFER"
                            checked={paymentMethod === "BANK_TRANSFER"}
                            onChange={() => setPaymentMethod("BANK_TRANSFER")}
                            className="accent-blue-600"
                        />
                        Bank Transfer
                    </label>

                    <label className="flex items-center gap-2">
                        <input
                            type="radio"
                            name="payment"
                            value="CREDIT_CARD"
                            checked={paymentMethod === "CREDIT_CARD"}
                            onChange={() => setPaymentMethod("CREDIT_CARD")}
                            className="accent-blue-600"
                        />
                        Credit Card
                    </label>
                </div>
            </div>

            <div className="flex justify-end mt-4">
                <button
                    onClick={handleConfirmPay}
                    className="px-6 py-3 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg transition-all"
                >
                    Confirm & Pay
                </button>
            </div>
        </div>
    );
}
