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

export const orderCreate = async (token, {username, amount, orderLineItems, paymentMethod}) => {
    return await fetch(`${import.meta.env.VITE_API_PATH}/orders`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
            username, 
            amount, 
            orderLineItems,
            paymentMethod
        })
    });
}