import { useState, useEffect } from 'react';
import api from '../services/api';
import { Card, Row, Col, Spinner, Alert } from 'react-bootstrap';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip as RechartsTooltip, Legend, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884d8'];

const Dashboard = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchDashboard = async () => {
      try {
        const res = await api.get('/dashboard');
        setData(res.data);
      } catch (err) {
        setError(err.message || 'Error fetching dashboard');
      } finally {
        setLoading(false);
      }
    };
    fetchDashboard();
  }, []);

  if (loading) return <div className="text-center p-5"><Spinner animation="border" /></div>;
  if (error) return <Alert variant="danger">{error}</Alert>;
  if (!data) return null;

  return (
    <div>
      <h2 className="mb-4">Dashboard</h2>
      
      <Row className="mb-4 g-3">
        <Col md={4} sm={6}>
          <Card className="text-center bg-primary text-white h-100">
            <Card.Body>
              <Card.Title>Total Revenue</Card.Title>
              <h3>${data.totalRevenue?.toFixed(2) || '0.00'}</h3>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4} sm={6}>
          <Card className="text-center bg-danger text-white h-100">
            <Card.Body>
              <Card.Title>Total Expenses</Card.Title>
              <h3>${data.totalExpenses?.toFixed(2) || '0.00'}</h3>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4} sm={6}>
          <Card className="text-center bg-success text-white h-100">
            <Card.Body>
              <Card.Title>Profit</Card.Title>
              <h3>${data.profit?.toFixed(2) || '0.00'}</h3>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4} sm={6}>
          <Card className="text-center bg-warning text-dark h-100">
            <Card.Body>
              <Card.Title>Outstanding Payments</Card.Title>
              <h3>${data.outstandingPayments?.toFixed(2) || '0.00'}</h3>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4} sm={6}>
          <Card className="text-center bg-info text-white h-100">
            <Card.Body>
              <Card.Title>Products</Card.Title>
              <h3>{data.totalProducts || 0}</h3>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4} sm={6}>
          <Card className="text-center bg-secondary text-white h-100">
            <Card.Body>
              <Card.Title>Customers</Card.Title>
              <h3>{data.totalCustomers || 0}</h3>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="g-4">
        <Col lg={8}>
          <Card className="h-100">
            <Card.Body>
              <Card.Title>Monthly Sales</Card.Title>
              <div style={{ height: 300 }}>
                {data.monthlySales && data.monthlySales.length > 0 ? (
                  <ResponsiveContainer width="100%" height="100%">
                    <BarChart data={data.monthlySales}>
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis dataKey="month" />
                      <YAxis />
                      <RechartsTooltip />
                      <Legend />
                      <Bar dataKey="sales" fill="#8884d8" />
                    </BarChart>
                  </ResponsiveContainer>
                ) : (
                  <div className="d-flex h-100 align-items-center justify-content-center text-muted">No sales data</div>
                )}
              </div>
            </Card.Body>
          </Card>
        </Col>
        <Col lg={4}>
          <Card className="h-100">
            <Card.Body>
              <Card.Title>Expenses by Category</Card.Title>
              <div style={{ height: 300 }}>
                {data.expensesByCategory && data.expensesByCategory.length > 0 ? (
                  <ResponsiveContainer width="100%" height="100%">
                    <PieChart>
                      <Pie
                        data={data.expensesByCategory}
                        dataKey="value"
                        nameKey="name"
                        cx="50%"
                        cy="50%"
                        outerRadius={80}
                        label
                      >
                        {data.expensesByCategory.map((entry, index) => (
                          <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                      </Pie>
                      <RechartsTooltip />
                      <Legend />
                    </PieChart>
                  </ResponsiveContainer>
                ) : (
                  <div className="d-flex h-100 align-items-center justify-content-center text-muted">No expense data</div>
                )}
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;
