import { resolve } from "path";
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react({
      babel: {
        parserOpts: {
          plugins: ["decorators-legacy"], // classProperties
        },
      },
    }),
  ],
  resolve: {
    alias: [
      { find: "@", replacement: resolve(__dirname, "src") },
      { find: "$assets", replacement: resolve(__dirname, "src", "assets") },
      { find: "$lib", replacement: resolve(__dirname, "src", "lib") },
      { find: "$routes", replacement: resolve(__dirname, "src", "routes") },
    ],
  },
});
