export const formatNumber = (value) => {
    if (!value) return "0";
    return new Intl.NumberFormat("id-ID").format(value);
}

export const parseNumber = (value) => {
    if (!value) return "0";
    return value.replace(/[^0-9]/g, "");
}

export const getUsername = (token) => {
    const payloadBase64 = token.split('.')[1];
    const payloadJson = atob(payloadBase64.replace(/-/g, '+').replace(/_/g, '/'));
    const payload = JSON.parse(payloadJson);
    return payload.sub;
}
