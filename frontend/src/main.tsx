import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { ThemeProvider } from "@mui/material/styles";
import Box from "@mui/material/Box";

// Styles
import "@fontsource/roboto/100.css";
import "@fontsource/roboto/300.css";
import "@fontsource/roboto/400.css";
import "@fontsource/roboto/500.css";
import "@fontsource/roboto/700.css";
import "./index.css";

// Theme
import theme from "./theme";

// Components
import Navbar from "$lib/Navbar";
import Footer from "$lib/Footer";

// Routes
import Home from "$routes/Home";
import Login from "$routes/Login";

ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
  <React.StrictMode>
    <BrowserRouter>
      <ThemeProvider theme={theme}>
        <Navbar />

        <Box minHeight="100vh">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
          </Routes>
        </Box>

        <Footer />
      </ThemeProvider>
    </BrowserRouter>
  </React.StrictMode>
);
