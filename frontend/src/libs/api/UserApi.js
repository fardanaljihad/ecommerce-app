export const userRegister = async ({username, password, role}) => {
    return await fetch(`${import.meta.env.VITE_API_PATH}/users/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify({
            username, 
            password, 
            role
        })
    });
}

export const userLogin = async ({username, password}) => {
    return await fetch(`${import.meta.env.VITE_API_PATH}/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify({
            username, 
            password
        })
    });
}
