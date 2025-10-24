import { useState } from "react";
import { Link, useNavigate } from "react-router";
import { alertError, alertSuccess } from "../../libs/alert.js";
import { userRegister } from "../../libs/api/UserApi.js";

export default function UserRegister() {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const navigate = useNavigate();

    async function handleSubmit(e) {
        e.preventDefault();

        if (password !== confirmPassword) {
            await alertError("Passwords do not match!");
            return;
        }

        const response = await userRegister({
            username,
            password,
            role
        });

        const responseBody = await response.json();

        if (response.status === 200) {
            await alertSuccess(responseBody.message);
            await navigate({
                pathname: "/login"
            });
        } else {
            await alertError(responseBody.errors);
        }

    }

    return <>
        <div className="bg-white bg-opacity-90 backdrop-blur-sm p-8 rounded-3xl shadow-md border border-gray-300 w-full max-w-md">
            <div className="text-center mb-8">
                <h1 className="text-3xl font-bold text-gray-900">e-CommerceLabs</h1>
                <p className="text-gray-500 mt-2">Create a new account</p>
            </div>

            <form onSubmit={handleSubmit}>
                <div className="mb-4">
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
                            placeholder="Choose a username"
                            required
                            value={username}
                            onChange={e => setUsername(e.target.value)}
                        />
                    </div>
                </div>

                <div className="mb-4">
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
                            placeholder="Create a password"
                            required
                            value={password}
                            onChange={e => setPassword(e.target.value)}
                        />
                    </div>
                </div>

                <div className="mb-4">
                    <label htmlFor="confirm_password" className="block text-gray-700 text-sm font-medium mb-2">
                        Confirm Password
                    </label>
                    <div className="relative">
                        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <i className="fas fa-check-double text-gray-400"></i>
                        </div>
                        <input
                            type="password"
                            id="confirm_password"
                            name="confirm_password"
                            className="w-full pl-10 pr-3 py-3 bg-gray-50 border border-gray-300 text-gray-800 rounded-3xl focus:outline-none focus:ring-2 focus:ring-gray-500"
                            placeholder="Confirm your password"
                            required
                            value={confirmPassword}
                            onChange={e => setConfirmPassword(e.target.value)}
                        />
                    </div>
                </div>

                <div className="mb-6">
                    <label htmlFor="role" className="block text-gray-700 text-sm font-medium mb-2">
                        Role
                    </label>
                    <div className="relative">
                        <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <i className="fas fa-id-card text-gray-400"></i>
                        </div>
                        <select
                            id="role"
                            name="role"
                            className="appearance-none w-full pl-10 py-3 bg-gray-50 border border-gray-300 text-gray-800 rounded-3xl focus:outline-none focus:ring-2 focus:ring-gray-500 cursor-pointer"
                            required
                            value={role}
                            onChange={e => setRole(e.target.value)}
                        >
                            <option value="">Select your role</option>
                            <option value="OWNER">Owner</option>
                            <option value="CUSTOMER">Customer</option>
                        </select>
                        <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                            <i className="fas fa-chevron-down text-gray-400"></i>
                        </div>
                    </div>
                </div>

                <div className="mb-6">
                    <button
                        type="submit"
                        className="w-full bg-black text-white py-3 px-4 rounded-3xl hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-gray-500 font-medium shadow-sm"
                    >
                        Register
                    </button>
                </div>

                <div className="text-center text-sm">
                    Already have an account?
                    <Link to="/login" className="text-black hover:text-gray-800 font-medium">
                        {" "}Sign in
                    </Link>
                </div>
            </form>
        </div>
    </>
}