import { useState } from 'react';
import { Outlet, NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import {
  MdDashboard, MdInventory2, MdCategory, MdWarehouse,
  MdPeople, MdLocalShipping, MdShoppingCart, MdPointOfSale,
  MdReceipt, MdPayment, MdAccountBalance, MdBadge,
  MdNotifications, MdBarChart, MdMenu, MdClose, MdBusiness
} from 'react-icons/md';
import { Button, Navbar } from 'react-bootstrap';
import './DashboardLayout.css';

const navItems = [
  { path: '/app', label: 'Dashboard', icon: <MdDashboard /> },
  { path: '/app/products', label: 'Products', icon: <MdInventory2 /> },
  { path: '/app/categories', label: 'Categories', icon: <MdCategory /> },
  { path: '/app/inventory', label: 'Inventory', icon: <MdWarehouse /> },
  { path: '/app/customers', label: 'Customers', icon: <MdPeople /> },
  { path: '/app/suppliers', label: 'Suppliers', icon: <MdLocalShipping /> },
  { path: '/app/purchases', label: 'Purchases', icon: <MdShoppingCart /> },
  { path: '/app/sales', label: 'Sales', icon: <MdPointOfSale /> },
  { path: '/app/invoices', label: 'Invoices', icon: <MdReceipt /> },
  { path: '/app/payments', label: 'Payments', icon: <MdPayment /> },
  { path: '/app/expenses', label: 'Expenses', icon: <MdAccountBalance /> },
  { path: '/app/employees', label: 'Employees', icon: <MdBadge /> },
  { path: '/app/notifications', label: 'Notifications', icon: <MdNotifications /> },
  { path: '/app/reports', label: 'Reports', icon: <MdBarChart /> },
];

const DashboardLayout = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="dashboard-wrapper">
      <div className={`sidebar bg-dark text-white ${sidebarOpen ? 'open' : ''}`}>
        <div className="sidebar-header d-flex justify-content-between align-items-center p-3">
          <h5 className="mb-0 text-truncate"><MdBusiness className="me-2"/> SBM</h5>
          <button className="btn btn-link text-white d-md-none p-0" onClick={() => setSidebarOpen(false)}>
            <MdClose size={24} />
          </button>
        </div>
        <div className="sidebar-nav nav flex-column">
          {navItems.map(item => (
            <NavLink
              key={item.path}
              to={item.path}
              className={({isActive}) => `nav-link text-white ${isActive ? 'active-link' : ''}`}
              end={item.path === '/app'}
              onClick={() => setSidebarOpen(false)}
            >
              <span className="me-2">{item.icon}</span>
              {item.label}
            </NavLink>
          ))}
          <NavLink
            to="/app/business-profile"
            className={({isActive}) => `nav-link text-white ${isActive ? 'active-link' : ''}`}
            onClick={() => setSidebarOpen(false)}
          >
             <span className="me-2"><MdBusiness /></span>
             Profile
          </NavLink>
        </div>
      </div>
      
      <div className="main-content">
        <Navbar bg="white" className="border-bottom px-3 d-flex justify-content-between">
          <div className="d-flex align-items-center">
            <button className="btn btn-link d-md-none text-dark p-0 me-3" onClick={() => setSidebarOpen(true)}>
              <MdMenu size={24} />
            </button>
            <Navbar.Brand>Small Business Management</Navbar.Brand>
          </div>
          <div className="d-flex align-items-center">
            <span className="me-3">Hi, {user?.fullName || 'User'} | {user?.businessName || 'Business'}</span>
            <Button variant="outline-danger" size="sm" onClick={handleLogout}>Logout</Button>
          </div>
        </Navbar>
        
        <div className="content-area p-3 p-md-4 bg-light flex-grow-1">
          <Outlet />
        </div>
      </div>
    </div>
  );
};

export default DashboardLayout;
