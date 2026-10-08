import { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedUser = localStorage.getItem('sbm_user');
    const storedToken = localStorage.getItem('sbm_token');
    if (storedToken && storedUser) {
      try {
        setUser(JSON.parse(storedUser));
      } catch (e) {
        localStorage.removeItem('sbm_token');
        localStorage.removeItem('sbm_user');
      }
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    const { data } = await api.post('/auth/login', { email, password });
    const userData = {
      email: data.email,
      fullName: data.fullName,
      role: data.role,
      businessId: data.businessId,
      businessName: data.businessName
    };
    localStorage.setItem('sbm_token', data.token);
    localStorage.setItem('sbm_user', JSON.stringify(userData));
    setUser(userData);
  };

  const register = async (formData) => {
    const { data } = await api.post('/auth/register', formData);
    const userData = {
      email: data.email,
      fullName: data.fullName,
      role: data.role,
      businessId: data.businessId,
      businessName: data.businessName
    };
    localStorage.setItem('sbm_token', data.token);
    localStorage.setItem('sbm_user', JSON.stringify(userData));
    setUser(userData);
  };

  const logout = () => {
    localStorage.removeItem('sbm_token');
    localStorage.removeItem('sbm_user');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout, loading }}>
      {!loading && children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
