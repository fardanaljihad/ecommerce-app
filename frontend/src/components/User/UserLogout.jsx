import { useEffect } from "react";
import { useNavigate } from "react-router";
import { useLocalStorage } from "react-use"

export default function UserLogout() {

    const [token, setToken] = useLocalStorage("token", "");
    const navigate = useNavigate();

    async function handleLogout() {
        setToken("");
        await navigate({
            pathname: "/login"
        })
    }

    useEffect(() => {
        handleLogout()
            .then(() => console.log("User log out successfully"));
    }, []);

    return <>
    </>
}