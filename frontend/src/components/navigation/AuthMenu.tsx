import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useAuth } from "../../hooks/useAuth";
import {
  registerSchema,
  loginSchema,
  type RegisterFormData,
  type LoginFormData,
  type AuthMenuProps,
} from "../../types/AuthTypes";

const AuthMenu = ({ mode, onClose }: AuthMenuProps) => {
  const {
    register: authRegister,
    login,
    isRegistering,
    isLoggingIn,
  } = useAuth();
  const isLoading = isRegistering || isLoggingIn;

  const registerForm = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      name: "",
      email: "",
      username: "",
      password: "",
    },
  });

  const loginForm = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      username: "",
      password: "",
    },
  });

  const onRegisterSubmit = async (data: RegisterFormData) => {
    try {
      await authRegister(data);
      registerForm.reset();
      onClose();
    } catch (err) {
      console.error("Registration failed:", err);
    }
  };

  const onLoginSubmit = async (data: LoginFormData) => {
    try {
      await login(data);
      loginForm.reset();
      onClose();
    } catch (err) {
      console.error("Login failed:", err);
    }
  };

  const inputClasses =
    "w-full border-2 border-black px-4 py-2 focus:outline-none focus:ring-2 focus:ring-black";
  const labelClasses = "block text-sm font-bold mb-1";
  const errorClasses = "text-red-600 text-sm mt-1";

  return (
    // Backdrop overlay
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/50"
      onClick={onClose}
    >
      {/* Modal container */}
      <div
        className="relative w-full max-w-md border-2 bg-white p-8 shadow-[4px_4px_0px_0px_#000]"
        onClick={(e) => e.stopPropagation()}
      >
        {/* Close button */}
        <button
          type="button"
          onClick={onClose}
          className="absolute top-4 right-4 text-2xl font-bold hover:opacity-70"
          aria-label="Close modal"
        >
          ×
        </button>

        {mode === "register" && (
          <div>
            <h2 className="mb-6 text-2xl font-bold">Create Account</h2>
            <form
              onSubmit={registerForm.handleSubmit(onRegisterSubmit)}
              className="flex flex-col gap-4"
            >
              <div>
                <label htmlFor="name" className={labelClasses}>
                  Name
                </label>
                <input
                  type="text"
                  id="name"
                  {...registerForm.register("name")}
                  className={inputClasses}
                  disabled={isLoading}
                />
                {registerForm.formState.errors.name && (
                  <span className={errorClasses}>
                    {registerForm.formState.errors.name.message}
                  </span>
                )}
              </div>

              <div>
                <label htmlFor="email" className={labelClasses}>
                  Email
                </label>
                <input
                  type="email"
                  id="email"
                  {...registerForm.register("email")}
                  className={inputClasses}
                  disabled={isLoading}
                />
                {registerForm.formState.errors.email && (
                  <span className={errorClasses}>
                    {registerForm.formState.errors.email.message}
                  </span>
                )}
              </div>

              <div>
                <label htmlFor="username" className={labelClasses}>
                  Username
                </label>
                <input
                  type="text"
                  id="username"
                  {...registerForm.register("username")}
                  className={inputClasses}
                  disabled={isLoading}
                />
                {registerForm.formState.errors.username && (
                  <span className={errorClasses}>
                    {registerForm.formState.errors.username.message}
                  </span>
                )}
              </div>

              <div>
                <label htmlFor="password" className={labelClasses}>
                  Password
                </label>
                <input
                  type="password"
                  id="password"
                  {...registerForm.register("password")}
                  className={inputClasses}
                  disabled={isLoading}
                />
                {registerForm.formState.errors.password && (
                  <span className={errorClasses}>
                    {registerForm.formState.errors.password.message}
                  </span>
                )}
              </div>

              <button
                type="submit"
                disabled={isLoading}
                className="btn-primary text-lg"
              >
                {isRegistering ? "Creating account..." : "Register"}
              </button>
            </form>
          </div>
        )}

        {mode === "login" && (
          <div>
            <h2 className="mb-6 text-2xl font-bold">Welcome Back</h2>
            <form
              onSubmit={loginForm.handleSubmit(onLoginSubmit)}
              className="flex flex-col gap-4"
            >
              <div>
                <label htmlFor="login-username" className={labelClasses}>
                  Username
                </label>
                <input
                  type="text"
                  id="login-username"
                  {...loginForm.register("username")}
                  className={inputClasses}
                  disabled={isLoading}
                />
                {loginForm.formState.errors.username && (
                  <span className={errorClasses}>
                    {loginForm.formState.errors.username.message}
                  </span>
                )}
              </div>

              <div>
                <label htmlFor="login-password" className={labelClasses}>
                  Password
                </label>
                <input
                  type="password"
                  id="login-password"
                  {...loginForm.register("password")}
                  className={inputClasses}
                  disabled={isLoading}
                />
                {loginForm.formState.errors.password && (
                  <span className={errorClasses}>
                    {loginForm.formState.errors.password.message}
                  </span>
                )}
              </div>

              <button
                type="submit"
                disabled={isLoading}
                className="mt-2 w-full border-2 border-black bg-black py-3 font-bold text-white hover:bg-white hover:text-black disabled:opacity-50"
              >
                {isLoggingIn ? "Logging in..." : "Log In"}
              </button>
            </form>
          </div>
        )}
      </div>
    </div>
  );
};

export default AuthMenu;
