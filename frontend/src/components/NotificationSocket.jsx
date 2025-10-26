import { useEffect } from "react";
import { Client } from "@stomp/stompjs";
import { Bounce, toast } from "react-toastify";

export const NotificationSocket = ({ username }) => {
    useEffect(() => {
        const client = new Client({
            brokerURL: "ws://localhost:8080/ws",
            reconnectDelay: 5000,
            debug: (str) => console.log(str),
            onConnect: () => {
                console.log("Connected to WebSocket");

                client.subscribe(`/user/notifications/success/${username}`, (message) => {
                    console.log("New message:", message.body);
                    toast.success(message.body, {
                        position: "top-right",
                        autoClose: 5000,
                        hideProgressBar: false,
                        closeOnClick: false,
                        pauseOnHover: true,
                        draggable: true,
                        progress: undefined,
                        theme: "light",
                        transition: Bounce,
                    });
                });

                client.subscribe(`/user/notifications/fail/${username}`, (message) => {
                    console.log("New message:", message.body);
                    toast.error(message.body, {
                        position: "top-right",
                        autoClose: 5000,
                        hideProgressBar: false,
                        closeOnClick: false,
                        pauseOnHover: true,
                        draggable: false,
                        progress: undefined,
                        theme: "light",
                        transition: Bounce,
                    });
                });
            },
        });

        client.activate();

        return () => {
            client.deactivate();
            console.log("Disconnected from WebSocket");
        };
    }, [username]);

    return null;
};
