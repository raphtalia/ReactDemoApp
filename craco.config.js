const path = require("path");
module.exports = {
  webpack: {
    // Aliases tied to tsconfig.json
    alias: {
      $lib: path.resolve(__dirname, "src", "lib"),
      $routes: path.resolve(__dirname, "src", "routes"),
    },
  },
};
