import { createContext, useState } from "react";
import {axiosInstance} from "../services/api.ts";
import toast from "react-hot-toast";

export const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [isRegistering, setIsRegistering] = useState(false);
    const [isLoggingIn, setIsLoggingIn] = useState(false);

    const register = async (formData) => {
        setIsRegistering(true);
        try {
            const response = await axiosInstance.post("/auth/register", formData);
            if (response.status === 201) {
                setUser(response.data.user);
                toast.success("Registration successful!");
            } else {
                toast.error("Registration failed.");
            }
        } catch (error) {
            console.error("Error in register: ", error);
            toast.error(`Unable to register: ${error.message}`);
        } finally {
            setIsRegistering(false);
        }
    };

    const login = async (formData) => {
        setIsLoggingIn(true);
        try {
            const response = await axiosInstance.post("/auth/login", formData);
            if (response.status === 200) {
                setUser(response.data.user);
                toast.success("Login successful!");
            } else {
                toast.error("Login failed.");
            }
        } catch (error) {
            console.error("Error in login: ", error);
            toast.error(`Unable to login: ${error.message}`);
        } finally {
            setIsLoggingIn(false);
        }
    };

    const logout = async () => {
        try {
            const response = await axiosInstance.post("/auth/logout");
            if (response.status === 200) {
                setUser(null);
                toast.success("Logout successful!");
            } else {
                toast.error("Logout failed.");
            }
        } catch (error) {
            console.error("Error in logout: ", error);
            toast.error(`Unable to logout: ${error.message}`);
        }
    };

    return (
        <AuthContext.Provider
            value={{ user, register, login, logout, isRegistering, isLoggingIn }}
        >
            {children}
        </AuthContext.Provider>
    );
}
