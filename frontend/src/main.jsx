import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Routes, Route } from "react-router";
import Layout from './components/Layout.jsx';
import UserRegister from './components/User/UserRegister.jsx';
import UserLogin from './components/User/UserLogin.jsx';
import DashboardLayout from './components/DashboardLayout.jsx';
import UserLogout from './components/User/UserLogout.jsx';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/register" element={<UserRegister />} />
          <Route path="/login" element={<UserLogin />} />
        </Route>
        <Route path="/dashboard" element={<DashboardLayout />}>
          <Route path="products" element={<h1>Products</h1>} />
          <Route path="users/logout" element={<UserLogout />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </StrictMode>,
)
