const path = require('path');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const devMode = process.env.NODE_ENV !== 'production';


module.exports = {
    mode: devMode ? "development" : "production",
    entry: {
        'dashboard': "./frontend/dashboard.ts",
        'upload': "./frontend/upload.ts",
    },
    output: {
        filename: "js/[name].bundle.js",
        path: path.resolve(__dirname, 'src/main/resources/static/'),
    },
    resolve: {
        extensions: [".tsx", ".ts", ".js", ".json"],
    },
    plugins: [new MiniCssExtractPlugin({
        filename: 'style/[name].css',
    })],
    module: {
        rules: [
            // all files with a '.ts' or '.tsx' extension will be handled by 'ts-loader'
            { test: /\.tsx?$/, use: ["ts-loader"], exclude: /node_modules/ },

            {
                test: /\.s[ac]ss$/i,
                use: [
                    // Place CSS into it's own files
                    {loader: MiniCssExtractPlugin.loader, options: {hmr: devMode}},
                    // Process css
                    'css-loader',
                    // Compiles Sass to CSS
                    'sass-loader',
                ],
            },
        ],

    },
};