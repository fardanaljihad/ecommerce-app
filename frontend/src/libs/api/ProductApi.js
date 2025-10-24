export const productList = async (token, { name, page, size = 10 } = {}) => {
    const url = new URL(`${import.meta.env.VITE_API_PATH}/products`);

    if (name) url.searchParams.append('name', name);
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