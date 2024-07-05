module.exports = {
  webpack: {
    configure: (webpackConfig, { env, paths }) => {
      // Actualiza la configuración de Webpack aquí
      if (webpackConfig.devServer) {
        webpackConfig.devServer.setupMiddlewares = (middlewares, devServer) => {
          // Tu configuración de middlewares aquí
          return middlewares;
        };
      }
      return webpackConfig;
    }
  }
};
