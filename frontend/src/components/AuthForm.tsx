import { useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

export const AuthForm = () => {
  const { register, isRegistering, login, isLoggingIn } = useAuth();
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    username: "",
    password: "",
  });
  const [error, setError] = useState<string | null>(null);
  const [registerMode, setRegisterMode] = useState(true);
  const navigate = useNavigate();
  const isLoading = isRegistering || isLoggingIn;

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError(null);
    try {
      if (registerMode) {
        await register(formData);
      } else {
        const { username, password } = formData;
        await login({ username, password });
        navigate("/profile");
      }
      setFormData({ name: "", email: "", username: "", password: "" });
    } catch (err: unknown) {
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError("Something went wrong");
      }
    }
  };

  const toggleMode = () => {
    setRegisterMode(!registerMode);
    setFormData({ name: "", email: "", username: "", password: "" });
    setError(null);
  };

  const handleInputChange =
    (field: keyof typeof formData) =>
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setFormData({ ...formData, [field]: e.target.value });
    };

  return (
    <div className="p-8">
      <div className="flex flex-col justify-center gap-6 rounded-xl shadow-2xl p-8">
        <div className="text-center">
          <h2 className="text-3xl sm:text-4xl md:text-5xl font-extrabold">
            {registerMode ? "Create an account" : "Welcome back!"}
          </h2>
          <p className="text-base sm:text-lg md:text-xl font-medium text-[var(--color-text-secondary)]">
            Start supporting shelters today!
          </p>
        </div>
        <form
          className="flex flex-col justify-center gap-4"
          onSubmit={handleSubmit}
        >
          {registerMode && (
            <>
              <div className="flex flex-col justify-center">
                <label
                  htmlFor="name"
                  className="text-sm sm:text-base font-semibold"
                >
                  Name
                </label>
                <input
                  className="border border-[var(--color-border)] bg-[var(--color-text)]/10 focus:outline-none focus:ring-[var(--color-primary)] px-4 py-2 focus:ring-2 rounded-xl w-full"
                  id="name"
                  value={formData.name}
                  onChange={handleInputChange("name")}
                  type="text"
                  placeholder="Enter your name"
                  required
                  disabled={isLoading}
                />
              </div>
              <div className="flex flex-col justify-center">
                <label
                  htmlFor="email"
                  className="text-sm sm:text-base font-semibold"
                >
                  Email
                </label>
                <input
                  className="border border-[var(--color-border)] bg-[var(--color-text)]/10 focus:outline-none focus:ring-[var(--color-primary)] px-4 py-2 focus:ring-2 rounded-xl w-full"
                  id="email"
                  value={formData.email}
                  onChange={handleInputChange("email")}
                  type="email"
                  placeholder="Enter your email"
                  required
                  disabled={isLoading}
                />
              </div>
            </>
          )}
          <div className="flex flex-col justify-center">
            <label
              htmlFor="username"
              className="text-sm sm:text-base font-semibold"
            >
              Username
            </label>
            <input
              className="border border-[var(--color-border)] bg-[var(--color-text)]/10 focus:outline-none focus:ring-[var(--color-primary)] px-4 py-2 focus:ring-2 rounded-xl w-full"
              id="username"
              value={formData.username}
              onChange={handleInputChange("username")}
              type="text"
              placeholder="Enter your username"
              required
              disabled={isLoading}
            />
          </div>
          <div className="flex flex-col justify-center">
            <label
              htmlFor="password"
              className="text-sm sm:text-base font-semibold"
            >
              Password
            </label>
            <input
              className="border border-[var(--color-border)] bg-[var(--color-text)]/10 focus:outline-none focus:ring-[var(--color-primary)] px-4 py-2 focus:ring-2 rounded-xl w-full"
              id="password"
              value={formData.password}
              onChange={handleInputChange("password")}
              type="password"
              placeholder="Enter your password"
              required
              disabled={isLoading}
            />
          </div>
          {error && (
            <div role="alert" aria-live="polite">
              <p className="text-[var(--color-error)] text-sm sm:text-base font-medium">
                {error}
              </p>
            </div>
          )}
          <button className="btn-secondary" type="submit" disabled={isLoading}>
            {registerMode
              ? isRegistering
                ? "Signing up..."
                : "Sign Up"
              : isLoggingIn
              ? "Logging in..."
              : "Log In"}
          </button>
        </form>
        <div className="text-center text-sm sm:text-base font-medium">
          {registerMode ? (
            <p>
              Already have an account?{" "}
              <button
                type="button"
                onClick={toggleMode}
                disabled={isLoading}
                className="text-[var(--color-primary)] font-semibold hover:underline"
              >
                Log in
              </button>
            </p>
          ) : (
            <p>
              Don’t have an account?{" "}
              <button
                type="button"
                onClick={toggleMode}
                disabled={isLoading}
                className="text-[var(--color-primary)] font-semibold hover:underline"
              >
                Sign up
              </button>
            </p>
          )}
        </div>
      </div>
    </div>
  );
};
