const { resolve } = require('path')

module.exports = {
    publicPath: './',
    devServer: {
        host: '0.0.0.0',
        port: 8082,
        proxy: {
            '/admin': {
                target: 'http://localhost:7777/',
                changeOrigin: true,
                pathRewrite: {
                    "^/admin": ""
                }
            },
        }
    },

    configureWebpack: {

        resolve: {
            alias: {
                '@': resolve('src'),
            }
        }
    },
}