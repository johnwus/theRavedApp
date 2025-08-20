export { default as en } from "./en"

export const i18n = {
  isInitialized: true,
  language: "en",
  t: (key: string, params: Record<string, any> = {}) => `${key} ${JSON.stringify(params)}`,
  numberToCurrency: (_n: number) => "$0.00",
}
