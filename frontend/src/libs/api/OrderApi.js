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

export const orderList = async (token, { username, page, size = 10 } = {}) => {
    const url = new URL(`${import.meta.env.VITE_API_PATH}/orders`);

    if (username) url.searchParams.append('username', username);
    if (page) url.searchParams.append('page', page);
    if (size) url.searchParams.append('size', size);

    return await fetch(url, {
        method: "GET",
        headers: {
            "Accept": "application/json",
            "Authorization": `Bearer ${token}`
        }
    });
}
