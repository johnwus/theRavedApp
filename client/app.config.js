module.exports = ({ config }) => {
  const ENV = process.env.APP_ENV || 'development'
  const API_BY_ENV = {
    development: process.env.API_URL_DEV || 'http://localhost:8080',
    staging: process.env.API_URL_STAGING || 'https://staging.api.raved.app',
    production: process.env.API_URL_PROD || 'https://api.raved.app',
  }

  return {
    ...config,
    extra: {
      apiBaseUrl: API_BY_ENV[ENV],
      appEnv: ENV,
    },
    expoClient: {
      extra: {
        apiBaseUrl: API_BY_ENV[ENV],
        appEnv: ENV,
      },
    },
    ios: { ...config.ios },
    android: { ...config.android },
  }
}

