import { Link, Outlet } from "react-router";

export default function DashboardLayout() {
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
                                    className="text-black hover:text-gray-500 flex items-center transition-colors duration-200"
                                >
                                    <i className="fas fa-cart-shopping mr-2"></i>
                                    <span>Cart</span>
                                </Link>
                            </li>

                            <li>
                                <Link
                                    to="/dashboard/users/logout"
                                    className="text-black hover:text-gray-500 flex items-center transition-colors duration-200"
                                >
                                    <i className="fas fa-sign-out-alt mr-2"></i>
                                    <span>Logout</span>
                                </Link>
                            </li>
                        </ul>
                    </nav>
                </div>
            </header>

            <main className="container mx-auto px-4 py-8 flex-grow">

                <Outlet />

                <div className="mt-10 mb-6 text-center text-gray-500 text-sm">
                    <p>© 2025 E-CommerceLabs. All rights reserved.</p>
                </div>
            </main>
        </div>

    </>
}