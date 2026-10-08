import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Spinner, Table } from 'react-bootstrap';
import { 
    BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip as RechartsTooltip, Legend, ResponsiveContainer, 
    PieChart, Pie, Cell 
} from 'recharts';
import { toast } from 'react-toastify';
import api from '../services/api';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884d8'];

const Reports = () => {
    const [dashboard, setDashboard] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchDashboard();
    }, []);

    const fetchDashboard = async () => {
        setLoading(true);
        try {
            const res = await api.get('/dashboard');
            setDashboard(res.data);
        } catch (error) {
            toast.error('Failed to fetch dashboard data');
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <Container className="d-flex justify-content-center align-items-center" style={{ height: '80vh' }}>
                <Spinner animation="border" />
            </Container>
        );
    }

    if (!dashboard) {
        return <Container><p>No data available.</p></Container>;
    }

    return (
        <Container fluid>
            <h2 className="mb-4">Reports & Dashboard</h2>
            
            <Row className="mb-4">
                <Col md={3} className="mb-3">
                    <Card className="text-center h-100 bg-primary text-white">
                        <Card.Body>
                            <Card.Title>Total Revenue</Card.Title>
                            <h3>₹{dashboard.totalRevenue || 0}</h3>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={3} className="mb-3">
                    <Card className="text-center h-100 bg-danger text-white">
                        <Card.Body>
                            <Card.Title>Total Expenses</Card.Title>
                            <h3>₹{dashboard.totalExpenses || 0}</h3>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={3} className="mb-3">
                    <Card className="text-center h-100 bg-success text-white">
                        <Card.Body>
                            <Card.Title>Total Profit</Card.Title>
                            <h3>₹{dashboard.totalProfit || 0}</h3>
                        </Card.Body>
                    </Card>
                </Col>
                <Col md={3} className="mb-3">
                    <Card className="text-center h-100 bg-warning text-dark">
                        <Card.Body>
                            <Card.Title>Outstanding Payments</Card.Title>
                            <h3>₹{dashboard.outstandingPayments || 0}</h3>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Row className="mb-4">
                <Col lg={8} className="mb-3">
                    <Card className="h-100">
                        <Card.Header>Monthly Sales</Card.Header>
                        <Card.Body>
                            {dashboard.monthlySales && dashboard.monthlySales.length > 0 ? (
                                <ResponsiveContainer width="100%" height={300}>
                                    <BarChart data={dashboard.monthlySales}>
                                        <CartesianGrid strokeDasharray="3 3" />
                                        <XAxis dataKey="month" />
                                        <YAxis />
                                        <RechartsTooltip />
                                        <Legend />
                                        <Bar dataKey="amount" fill="#8884d8" name="Amount (₹)" />
                                        <Bar dataKey="count" fill="#82ca9d" name="Sales Count" />
                                    </BarChart>
                                </ResponsiveContainer>
                            ) : (
                                <p className="text-center text-muted mt-5">No sales data available</p>
                            )}
                        </Card.Body>
                    </Card>
                </Col>
                <Col lg={4} className="mb-3">
                    <Card className="h-100">
                        <Card.Header>Expenses by Category</Card.Header>
                        <Card.Body>
                            {dashboard.expensesByCategory && dashboard.expensesByCategory.length > 0 ? (
                                <ResponsiveContainer width="100%" height={300}>
                                    <PieChart>
                                        <Pie
                                            data={dashboard.expensesByCategory}
                                            cx="50%"
                                            cy="50%"
                                            outerRadius={80}
                                            fill="#8884d8"
                                            dataKey="amount"
                                            nameKey="category"
                                            label
                                        >
                                            {dashboard.expensesByCategory.map((entry, index) => (
                                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                            ))}
                                        </Pie>
                                        <RechartsTooltip />
                                        <Legend />
                                    </PieChart>
                                </ResponsiveContainer>
                            ) : (
                                <p className="text-center text-muted mt-5">No expense data available</p>
                            )}
                        </Card.Body>
                    </Card>
                </Col>
            </Row>

            <Row>
                <Col>
                    <Card>
                        <Card.Header>Key Metrics</Card.Header>
                        <Card.Body>
                            <Table responsive bordered hover>
                                <tbody>
                                    <tr>
                                        <th>Total Products</th>
                                        <td>{dashboard.totalProducts || 0}</td>
                                    </tr>
                                    <tr>
                                        <th>Low Stock Items</th>
                                        <td className={dashboard.lowStockItems > 0 ? 'text-danger fw-bold' : ''}>
                                            {dashboard.lowStockItems || 0}
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Total Customers</th>
                                        <td>{dashboard.totalCustomers || 0}</td>
                                    </tr>
                                    <tr>
                                        <th>Total Suppliers</th>
                                        <td>{dashboard.totalSuppliers || 0}</td>
                                    </tr>
                                    <tr>
                                        <th>Total Sales</th>
                                        <td>{dashboard.totalSales || 0}</td>
                                    </tr>
                                    <tr>
                                        <th>Total Purchases</th>
                                        <td>{dashboard.totalPurchases || 0}</td>
                                    </tr>
                                </tbody>
                            </Table>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default Reports;
