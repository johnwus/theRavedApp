let lastEmail: string | null = null
let lastPassword: string | null = null

export function setLastCredentials(email: string, password: string) {
  lastEmail = email
  lastPassword = password
}

export function getLastCredentials() {
  return { email: lastEmail, password: lastPassword }
}

export function clearLastCredentials() {
  lastEmail = null
  lastPassword = null
}
