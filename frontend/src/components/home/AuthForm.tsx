import { useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../hooks/useAuth";

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
      setError(err instanceof Error ? err.message : "Something went wrong");
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

  const inputs = [
    {
      id: "name",
      label: "Name",
      type: "text",
      placeholder: "Enter your name",
      registerOnly: true,
    },
    {
      id: "email",
      label: "Email",
      type: "email",
      placeholder: "Enter your email",
      registerOnly: true,
    },
    {
      id: "username",
      label: "Username",
      type: "text",
      placeholder: "Enter your username",
    },
    {
      id: "password",
      label: "Password",
      type: "password",
      placeholder: "Enter your password",
    },
  ];

  const inputClasses =
    "border border-[var(--color-border)] bg-[var(--color-text)]/10 focus:outline-none focus:ring-[var(--color-primary)] px-4 py-2 focus:ring-2 rounded-xl w-full text-base";

  return (
    <div className="w-full">
      <div className="flex flex-col justify-center gap-8 rounded-xl p-8 shadow-2xl">
        <div className="text-center">
          <h2 className="text-2xl leading-snug font-semibold sm:text-3xl md:text-3xl lg:text-4xl">
            {registerMode ? "Create an account" : "Welcome back!"}
          </h2>
          <p className="mt-2 text-sm leading-relaxed font-normal sm:text-base md:text-base lg:text-lg">
            Start supporting shelters today!
          </p>
        </div>

        <form
          className="flex flex-col justify-center gap-4"
          onSubmit={handleSubmit}
        >
          {inputs
            .filter((input) => !input.registerOnly || registerMode)
            .map(({ id, label, type, placeholder }) => (
              <div key={id} className="flex flex-col justify-center gap-1">
                <label
                  htmlFor={id}
                  className="text-sm font-semibold sm:text-base md:text-base"
                >
                  {label}
                </label>
                <input
                  id={id}
                  type={type}
                  placeholder={placeholder}
                  value={formData[id as keyof typeof formData]}
                  onChange={handleInputChange(id as keyof typeof formData)}
                  className={inputClasses}
                  required
                  disabled={isLoading}
                />
              </div>
            ))}
          {error && (
            <div role="alert" aria-live="polite">
              <p className="mt-1 text-sm font-medium text-[var(--color-error)] sm:text-base">
                {error}
              </p>
            </div>
          )}
          <button
            className="btn-secondary text-base font-semibold"
            type="submit"
            disabled={isLoading}
          >
            {registerMode
              ? isRegistering
                ? "Signing up..."
                : "Sign Up"
              : isLoggingIn
                ? "Logging in..."
                : "Log In"}
          </button>
        </form>

        <div className="mt-2 text-center text-sm font-normal sm:text-base">
          {registerMode ? (
            <p>
              Already have an account?{" "}
              <button
                type="button"
                onClick={toggleMode}
                disabled={isLoading}
                className="font-semibold text-[var(--color-primary)] hover:underline"
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
                className="font-semibold text-[var(--color-primary)] hover:underline"
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
