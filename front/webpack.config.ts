import path from 'path';
import ReactRefreshWebpackPlugin from '@pmmmwh/react-refresh-webpack-plugin';
import webpack from 'webpack';
import ForkTsCheckerWebpackPlugin from 'fork-ts-checker-webpack-plugin';
import {BundleAnalyzerPlugin} from 'webpack-bundle-analyzer';
import WebpackDevServer from 'webpack-dev-server';

declare module 'webpack' {
    interface Configuration {
        devServer?: WebpackDevServer.Configuration;
    }
}

const dotEnv = require('dotenv-webpack')
const isDevelopment = process.env.NODE_ENV !== 'production';

const config: webpack.Configuration = {
    name: 'hcs',
    mode: isDevelopment ? 'development' : 'production',
    devtool: !isDevelopment ? 'hidden-source-map' : 'eval',
    resolve: {
        extensions: ['.js', '.jsx', '.ts', '.tsx', '.json'],
        alias: {
            '@hooks': path.resolve(__dirname, 'hooks'),
            '@components': path.resolve(__dirname, 'components'),
            '@context': path.resolve(__dirname, 'context'),
            '@layouts': path.resolve(__dirname, 'layouts'),
            '@pages': path.resolve(__dirname, 'pages'),
            '@utils': path.resolve(__dirname, 'utils'),
            '@typings': path.resolve(__dirname, 'typings'),
            '@reducers': path.resolve(__dirname, 'reducers'),
            '@store': path.resolve(__dirname, 'store'),
            '@actions': path.resolve(__dirname, 'actions'),
        },
        fallback: {
            fs: false
        },
    },
    entry: {
        app: './client',
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                loader: 'babel-loader',
                options: {
                    presets: [
                        [
                            '@babel/preset-env',
                            {
                                targets: {browsers: ['last 2 chrome versions']},
                                debug: isDevelopment,
                            },
                        ],
                        '@babel/preset-react',
                        '@babel/preset-typescript',
                    ],
                    env: {
                        development: {
                            plugins: [['@emotion', {sourceMap: true}], require.resolve('react-refresh/babel')],
                        },
                    },
                },
                exclude: path.join(__dirname, 'node_modules'),
            },
            {
                test: /\.css?$/,
                use: ['style-loader', 'css-loader'],
            },
        ],
    },
    plugins: [
        new dotEnv(),
        new ForkTsCheckerWebpackPlugin({
            async: false,
            // eslint: {
            //   files: "./src/**/*",
            // },
        }),
        new webpack.DefinePlugin({
            'process.env.REACT_APP_PUBLIC_FOLDER': JSON.stringify(process.env.REACT_APP_PUBLIC_FOLDER),
        }),
        new webpack.EnvironmentPlugin({
            NODE_ENV: isDevelopment ? 'development' : 'production'
        }),
        new webpack.ProvidePlugin({
            process: 'process/browser',
        }),
    ],
    output: {
        path: path.join(__dirname, 'dist'),
        filename: '[name].js',
        publicPath: '/dist/',
    },
    devServer: {
        historyApiFallback: true, // react router
        overlay: true,
        hot: true,
        port: 3090,
        public: '127.0.0.1',
        host: 'localhost',
        publicPath: '/dist/',
        transportMode: "ws",
        injectClient: false,
        clientLogLevel: "info",
        proxy: {
            '/api': {
                target: 'https://localhost',
                secure: false,
                changeOrigin: true,
                pathRewrite: {'^/api': ''},
            },
            '/ws-stomp': {
                target: 'https://localhost/chat/inbox',
                secure: false,
                changeOrigin: true,
                pathRewrite: {'^/ws-stomp': ''},
                headers: {
                    Connection: "keep-alive"
                },
                logLevel: 'debug',
            }
        },
    },
};

if (isDevelopment && config.plugins) {
    config.plugins.push(new webpack.HotModuleReplacementPlugin());
    config.plugins.push(new ReactRefreshWebpackPlugin());
    config.plugins.push(new BundleAnalyzerPlugin({
        analyzerMode: 'server',
        openAnalyzer: true
    }));
}
// if (!isDevelopment && config.plugins) {
//     config.plugins.push(new webpack.LoaderOptionsPlugin({ minimize: true }));
//     config.plugins.push(new BundleAnalyzerPlugin({ analyzerMode: 'static' }));
// }

export default config;
