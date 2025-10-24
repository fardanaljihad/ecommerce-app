import { useState } from "react";
import { Link, useNavigate } from "react-router";
import { userLogin } from "../../libs/api/UserApi.js";
import { useLocalStorage } from "react-use";
import { alertError } from "../../libs/alert.js";

export default function UserLogin() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [_, setToken] = useLocalStorage("token", "");
    const navigate = useNavigate();

    async function handleSubmit(e) {
        e.preventDefault();

        const response = await userLogin({
            username,
            password
        })

        const responseBody = await response.json();

        if (response.status === 200) {
            const token = responseBody.data.token;
            setToken(token);
            await navigate({
                pathname: "/dashboard/products"
            });
        } else {
            await alertError(responseBody.errors);
        }
    }

    return <>
        <div className="bg-white bg-opacity-90 backdrop-blur-sm p-8 rounded-3xl shadow-md border border-gray-300 w-full max-w-md">
            <div className="text-center mb-8">
                <div className="inline-block p-3 bg-gray-100 rounded-full mb-4">
                    <i className="fas fa-store text-3xl text-gray-700"></i>
                </div>
                <h1 className="text-3xl font-bold text-gray-900">e-CommerceLabs</h1>
                <p className="text-gray-500 mt-2">Sign in to your account</p>
            </div>

            <form onSubmit={handleSubmit}>
                <div className="mb-5">
                    <label htmlFor="username" className="block text-gray-700 text-sm font-medium mb-2">
                        Username
                    </label>
                    <div className="relative">
                        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <i className="fas fa-user text-gray-400"></i>
                        </div>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            className="w-full pl-10 pr-3 py-3 bg-gray-50 border border-gray-300 text-gray-800 rounded-3xl focus:outline-none focus:ring-2 focus:ring-gray-500"
                            placeholder="Enter your username"
                            required
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </div>
                </div>

                <div className="mb-6">
                    <label htmlFor="password" className="block text-gray-700 text-sm font-medium mb-2">
                        Password
                    </label>
                    <div className="relative">
                        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <i className="fas fa-lock text-gray-400"></i>
                        </div>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            className="w-full pl-10 pr-3 py-3 bg-gray-50 border border-gray-300 text-gray-800 rounded-3xl focus:outline-none focus:ring-2 focus:ring-gray-500"
                            placeholder="Enter your password"
                            required
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </div>
                </div>

                <div className="mb-6">
                    <button
                        type="submit"
                        className="w-full bg-black text-white py-3 px-4 rounded-3xl hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-gray-500 font-medium shadow-sm"
                    >
                        <i className="fas fa-sign-in-alt mr-2"></i> Sign In
                    </button>
                </div>

                <div className="text-center text-sm text-gray-500">
                    Don't have an account?
                    <Link
                        to="/register"
                        className="text-gray-800 hover:text-black font-medium"
                    >
                        {" "}Sign up
                    </Link>
                </div>
            </form>
        </div>
    </>
}