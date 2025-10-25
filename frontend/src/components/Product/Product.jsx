import { Link } from "react-router";
import { formatNumber, parseNumber } from "../../libs/utils";
import { useContext, useState } from "react";
import { CartContext } from "../../contexts/CartContext.jsx";

export default function Product({ product }) {

    const { addToCart, removeFromCart } = useContext(CartContext);
    const [quantity, setQuantity] = useState("1");
    const [inCart, setInCart] = useState(false);
    
    function handleAddToCart() {
        setInCart(true);
        addToCart(product, quantity);
    }

    function handleRemoveFromCart() {
        setInCart(false);
        removeFromCart(product.id);
    }

    return (
        <div
            className="group bg-white border border-gray-200 rounded-xl 
            shadow-sm hover:shadow-md flex flex-col overflow-hidden"
        >
            {/* Product Picture */}
            <Link to={`/dashboard/products/${product.id}`} className="block relative">
                <div className="w-full aspect-[1/1] bg-gray-100 overflow-hidden">
                    <img
                        src="https://pointcoffee.id/wp-content/uploads/2023/08/500x500_PC-PRODUCT-04.jpg"
                        alt={product.name}
                        className="w-full h-full object-contain"
                    />
                </div>
            </Link>

            {/* Product Content */}
            <div className="p-4 flex flex-col h-[200px]">
                {/* Product Name */}
                <h2 className="text-sm md:text-base text-gray-900 line-clamp-2 leading-snug mb-2 min-h-[48px]">
                    <Link to={`/dashboard/products/${product.id}`}>
                        <span>{product.name}</span>
                    </Link>
                </h2>

                {/* Price and Stock */}
                <div className="flex flex-col mb-3 mt-auto">
                    <p className="text-base text-gray-900">
                        Rp{formatNumber(product.price)}
                    </p>
                    <p className="text-sm text-gray-600">
                        Stok: {formatNumber(product.stock)}
                    </p>
                </div>

                {/* Quantity and Button */}
                <div className="flex items-center justify-between mt-auto">
                    <div className="flex items-center space-x-2">
                        <button
                            type="button"
                            onClick={() =>
                                setQuantity((prev) => (Number(prev) > 1 ? Number(prev) - 1 : 1))
                            }
                            disabled={product.stock === 0}
                            className={`w-8 h-8 flex items-center justify-center rounded-md border text-base shadow-sm 
                                ${product.stock === 0 || inCart
                                    ? "bg-gray-100 border-gray-200 text-gray-300 cursor-not-allowed"
                                    : "bg-gray-50 border-gray-300 text-black hover:bg-gray-200"
                                }`}
                        >
                            −
                        </button>

                        <input
                            id={`qty-${product.id}`}
                            type="text"
                            inputMode="numeric"
                            value={quantity}
                            onChange={(e) => {
                                const val = parseNumber(e.target.value);
                                setQuantity(val === "" ? "" : Number(val));
                            }}
                            disabled={product.stock === 0}
                            className={`w-10 px-2 py-1 rounded-md text-sm text-center border shadow-sm 
                                ${product.stock === 0 || inCart
                                    ? "bg-gray-100 text-gray-300 border-gray-200"
                                    : "bg-white text-black border-gray-300 focus:outline-none focus:ring-1 focus:ring-gray-400"
                                }`}
                        />

                        <button
                            type="button"
                            onClick={() =>
                                setQuantity((prev) =>
                                    Number(prev) < product.stock ? Number(prev) + 1 : Number(prev)
                                )
                            }
                            disabled={product.stock === 0}
                            className={`w-8 h-8 flex items-center justify-center rounded-md border text-base shadow-sm 
                                ${product.stock === 0 || inCart
                                    ? "bg-gray-100 border-gray-200 text-gray-300 cursor-not-allowed"
                                    : "bg-gray-50 border-gray-300 text-black hover:bg-gray-200"
                                }`}
                        >
                            +
                        </button>
                    </div>

                    {!inCart && (
                        <button
                            onClick={handleAddToCart}
                            disabled={product.stock === 0}
                            className={`px-3 py-2 rounded-md text-xs flex items-center justify-center shadow-sm 
                                ${product.stock === 0
                                    ? "bg-gray-200 text-gray-400 cursor-not-allowed"
                                    : "bg-gray-50 border-black text-black hover:bg-gray-200 border"
                                }`}
                        >
                            <i className="fas fa-shopping-cart text-[12px] mr-2"></i>
                            Add
                        </button>
                    )}

                    {inCart && (
                        <button
                            onClick={handleRemoveFromCart}
                            disabled={product.stock === 0}
                            className="px-3 py-2 rounded-md text-xs flex items-center justify-center shadow-sm
                                bg-white border border-red-500 text-red-500 hover:bg-red-50 hover:border-red-600"
                        >
                            Remove
                        </button>
                    )}
                </div>
            </div>
        </div>

    );
}