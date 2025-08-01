export const isValidEmail = (email: string): boolean => {
  const emailRegex = /^[A-Za-z0-9+_.-]+@(.+)$/;
  return emailRegex.test(email);
};

export const isValidUsername = (username: string): boolean => {
  const usernameRegex = /^[a-zA-Z0-9_]{3,50}$/;
  return usernameRegex.test(username);
};

export const isValidPassword = (password: string): boolean => {
  return password.length >= 8;
};

export const isStrongPassword = (password: string): boolean => {
  if (password.length < 8) return false;

  const hasUpper = /[A-Z]/.test(password);
  const hasLower = /[a-z]/.test(password);
  const hasDigit = /\d/.test(password);
  const hasSpecial = /[!@#$%^&*()_+\-=\[\]{}|;:,.<>?]/.test(password);

  return hasUpper && hasLower && hasDigit && hasSpecial;
};

export const isValidPhone = (phone: string): boolean => {
  const phoneRegex = /^[+]?[1-9]\d{9,14}$/;
  return phoneRegex.test(phone);
};