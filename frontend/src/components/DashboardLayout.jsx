import { Link, Outlet } from "react-router";
import { CartContext } from "../contexts/CartContext.jsx";
import { useContext } from "react";
import { NotificationSocket } from "./NotificationSocket.jsx";
import { ToastContainer } from "react-toastify";
import { useLocalStorage } from "react-use";
import { getUsername } from "../libs/utils.js";

export default function DashboardLayout() {

    const { cartItems } = useContext(CartContext);
    const [token, _] = useLocalStorage("token", "");
    const username = getUsername(token);

    return <>
        <div className="bg-white min-h-screen flex flex-col">
            <header className="bg-white border-b border-gray-300 shadow-md">
                <div className="container mx-auto px-4 py-4 flex justify-between items-center">
                    <Link
                        to="/dashboard/products"
                        className="flex items-center text-black hover:text-gray-600 transition-colors duration-200"
                    >
                        <i className="fas fa-store text-2xl mr-3"></i>
                        <div className="font-bold text-xl">E-CommerceLabs</div>
                    </Link>
                    <nav>
                        <ul className="flex space-x-6">
                            <li>
                                <Link
                                    to="/dashboard/users/cart"
                                    className="group flex items-center text-black hover:text-gray-500 transition-colors duration-200"
                                >
                                    <div className="relative mr-2">
                                        <i className="fas fa-cart-shopping text-2xl transition-colors duration-200 group-hover:text-gray-500"></i>

                                        {cartItems.length > 0 && (
                                            <span className="absolute -top-2.5 -right-2.5 bg-red-500 text-white text-xs font-bold px-1.5 py-0.5 rounded-full transition-colors duration-200 group-hover:bg-red-400">
                                                {cartItems.length}
                                            </span>
                                        )}
                                    </div>

                                    <span className="transition-colors duration-200 group-hover:text-gray-500">Cart</span>
                                </Link>
                            </li>

                            <li>
                                <Link
                                    to="/dashboard/users/logout"
                                    className="text-black hover:text-gray-500 flex items-center transition-colors duration-200"
                                >
                                    <i className="fas fa-sign-out-alt mr-2 text-2xl"></i>
                                    <span>Logout</span>
                                </Link>
                            </li>
                        </ul>
                    </nav>
                </div>
            </header>

            <main className="container mx-auto px-4 py-8 flex-grow">
                <NotificationSocket username={username} />
                <ToastContainer />

                <Outlet />
                
                <div className="mt-10 mb-6 text-center text-gray-500 text-sm">
                    <p>© 2025 E-CommerceLabs. All rights reserved.</p>
                </div>
            </main>
        </div>

    </>
}