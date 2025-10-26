import { Link } from "react-router";
import { formatDate, formatNumber, formatPaymentMethod, getUsername } from "../../libs/utils.js";
import { useEffect, useState } from "react";
import { orderList } from "../../libs/api/OrderApi.js";
import { alertError } from "../../libs/alert.js";
import { useLocalStorage } from "react-use";

export default function OrderList() {

    const [token, _] = useLocalStorage("token", "");
    const [orders, setOrders] = useState([]);
    const [page, setPage] = useState(0);
    const [totalPage, setTotalPage] = useState(0);
    const [reload, setReload] = useState(false);
    const username = getUsername(token);

    function getPages() {
        const pages = [];
        for (let i = 1; i <= totalPage; i++) {
            pages.push(i);
        }

        return pages;
    }

    async function fetchOrders() {
        const response = await orderList(token, { username, page });
        const responseBody = await response.json();
        console.log(responseBody);

        if (response.status === 200) {
            setOrders(responseBody.data);
            setPage(responseBody.pagination.currentPage);
            setTotalPage(responseBody.pagination.totalPage);
        } else {
            await alertError(responseBody.errors);
        }
    }

    async function handlePageChange(page) {
        setPage(page);
        setReload(!reload);
    }

    useEffect(() => {
        fetchOrders()
            .then(() => console.log("Orders fetched"));
    }, [reload])

    return (
        <div className="p-6">
            <h1 className="text-2xl font-semibold mb-6">My Orders</h1>

            {orders.length === 0 ? (
                <p className="text-gray-500 italic text-center">You have no orders yet.</p>
            ) : (
                <div className="flex flex-col gap-4">
                    {orders.map((order) => (
                        <div
                            key={order.id}
                            className="grid grid-cols-[120px_140px_160px_150px_150px_auto] gap-4 p-4 border rounded-2xl shadow-sm bg-white items-center"
                        >
                            <p className="text-gray-800 font-semibold">Order #{order.id}</p>
                            <p className="text-gray-500">{formatDate(order.createdAt)}</p>
                            <p className="text-gray-500">Rp{formatNumber(order.amount)}</p>
                            <p
                                className={`font-semibold ${order.status === "APPROVED"
                                        ? "text-green-600"
                                        : order.status === "REJECTED"
                                            ? "text-red-500"
                                            : "text-yellow-500"
                                    }`}
                            >
                                {order.status}
                            </p>
                            <p className="text-gray-500">{formatPaymentMethod(order.paymentMethod)}</p>

                            <Link
                                to={`/dashboard/users/order/${order.id}`}
                                className="px-4 py-2 hover:underline text-blue-500 rounded-2xl transition-all text-center"
                            >
                                Details
                            </Link>
                        </div>
                    ))}
                </div>
            )}

            {/* Pagination */}
            <div className="mt-10 flex justify-center">
                <nav className="flex items-center space-x-3 bg-white bg-opacity-90 rounded-3xl shadow-md border border-gray-300 p-3">
                    
                    {(page + 1) > 1 &&
                        <a
                            onClick={() => handlePageChange(page - 1)}
                            href="#"
                            className="px-4 py-2 bg-gray-100 text-gray-700 rounded-3xl hover:bg-gray-200 transition-all duration-200 flex items-center"
                        >
                            <i className="fas fa-chevron-left mr-2" /> Previous
                        </a>
                    }

                    {getPages().map(value => {
                        if (value === (page + 1)) {
                            return <a
                                key={value}
                                href="#" onClick={() => handlePageChange(value - 1)}
                                className="px-4 py-2 bg-black text-white rounded-3xl hover:bg-gray-800 transition-all duration-200 font-medium shadow-sm"
                            >
                                {value}
                            </a>
                        } else {
                            return <a
                                key={value}
                                href="#" onClick={() => handlePageChange(value - 1)}
                                className="px-4 py-2 bg-gray-100 text-gray-700 rounded-3xl hover:bg-gray-200 transition-all duration-200"
                            >
                                {value}
                            </a>
                        }
                    })}

                    {(page + 1) < totalPage &&
                        <a
                            onClick={() => handlePageChange(page + 1)}
                            href="#"
                            className="px-4 py-2 bg-gray-100 text-gray-700 rounded-3xl hover:bg-gray-200 transition-all duration-200 flex items-center"
                        >
                            Next <i className="fas fa-chevron-right ml-2" />
                        </a>
                    }
                </nav>
            </div>
        </div>
    );
}
