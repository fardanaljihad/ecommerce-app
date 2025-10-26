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

export const formatDate = (timestamp, locale = "id-ID", options) => {
    if (!timestamp) return "-";
    const date = new Date(timestamp);
    return date.toLocaleDateString(locale, options);
}

export function formatPaymentMethod(method) {
    switch (method) {
        case "BANK_TRANSFER":
            return "Bank Transfer";
        case "CREDIT_CARD":
            return "Credit Card";
        default:
            return method;
    }
}
