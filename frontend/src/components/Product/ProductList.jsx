import { useEffect, useState } from "react";
import Product from "./Product.jsx";
import { useLocalStorage } from "react-use";
import { productList } from "../../libs/api/ProductApi.js";
import { alertError } from "../../libs/alert.js";

export default function ProductList() {

    const [token, _] = useLocalStorage("token", "");
    const [products, setProducts] = useState([]);
    const [name, setName] = useState("");
    const [page, setPage] = useState(0);

    async function fetchProducts() {
        const response = await productList(token, { name, page });
        const responseBody = await response.json();
        console.log(responseBody);

        if (response.status === 200) {
            setProducts(responseBody.data);
        } else {
            await alertError(responseBody.errors);
        }
    }

    async function handleSearchProducts(e) {
        e.preventDefault();
        await fetchProducts();
    }

    useEffect(() => {
        fetchProducts()
            .then(() => console.log("Product fetched"));
    }, []);

    return <>
        <div className="flex justify-center">
            <div className="w-full max-w-screen-xl">
                {/* Header section */}

                {/* Search form */}
                <div className="p-6 mb-8">
                    <form onSubmit={handleSearchProducts} className="flex items-center space-x-3">
                        <div className="flex-1 relative">
                            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                <i className="fas fa-box text-gray-400" />
                            </div>
                            <input
                                type="text"
                                id="search_name"
                                name="search_name"
                                className="w-full pl-10 pr-3 py-3 bg-gray-50 border border-gray-300 text-gray-800 rounded-3xl focus:outline-none focus:ring-2 focus:ring-gray-500 transition-all duration-200"
                                placeholder="Search by name"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                            />
                        </div>

                        <button
                            type="submit"
                            className="px-6 py-3 bg-black text-white rounded-3xl hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-gray-500 transition-all duration-200 font-medium shadow-sm flex items-center"
                        >
                            <i className="fas fa-search mr-2" /> Search
                        </button>
                    </form>
                </div>


                <div className="flex flex-wrap justify-center gap-4">
                    {products.map((item) => (
                        <div key={item.id} className="w-[240px]">
                            <Product product={item} />
                        </div>
                    ))}
                </div>


                {/* Pagination */}
                <div className="mt-10 flex justify-center">
                    <nav className="flex items-center space-x-3 bg-white bg-opacity-90 rounded-3xl shadow-md border border-gray-300 p-3">
                        <a
                            href="#"
                            className="px-4 py-2 bg-gray-100 text-gray-700 rounded-3xl hover:bg-gray-200 transition-all duration-200 flex items-center"
                        >
                            <i className="fas fa-chevron-left mr-2" /> Previous
                        </a>
                        <a
                            href="#"
                            className="px-4 py-2 bg-black text-white rounded-3xl hover:bg-gray-800 transition-all duration-200 font-medium shadow-sm"
                        >
                            1
                        </a>
                        <a
                            href="#"
                            className="px-4 py-2 bg-gray-100 text-gray-700 rounded-3xl hover:bg-gray-200 transition-all duration-200"
                        >
                            2
                        </a>
                        <a
                            href="#"
                            className="px-4 py-2 bg-gray-100 text-gray-700 rounded-3xl hover:bg-gray-200 transition-all duration-200 flex items-center"
                        >
                            Next <i className="fas fa-chevron-right ml-2" />
                        </a>
                    </nav>
                </div>
            </div>
        </div>
    </>
}