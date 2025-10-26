import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter, Routes, Route } from "react-router";
import Layout from './components/Layout.jsx';
import UserRegister from './components/User/UserRegister.jsx';
import UserLogin from './components/User/UserLogin.jsx';
import DashboardLayout from './components/DashboardLayout.jsx';
import UserLogout from './components/User/UserLogout.jsx';
import ProductList from './components/Product/ProductList.jsx';
import CartProvider from './contexts/CartProvider.jsx';
import Cart from './components/Cart/Cart.jsx';
import Checkout from './components/Order/Checkout.jsx';
import OrderList from './components/Order/OrderList.jsx';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <CartProvider>
        <Routes>
          <Route element={<Layout />}>
            <Route path="/register" element={<UserRegister />} />
            <Route path="/login" element={<UserLogin />} />
          </Route>
          <Route path="/dashboard" element={<DashboardLayout />}>

            <Route path="users">
              <Route path="order" element={<OrderList />} />
              <Route path="cart" element={<Cart />} />
              <Route path="checkout" element={<Checkout />} />
              <Route path="logout" element={<UserLogout />} />
            </Route>

            <Route path="products">
              <Route index element={<ProductList />} />
            </Route>
          </Route>
        </Routes>
      </CartProvider>
    </BrowserRouter>
  </StrictMode>,
)
