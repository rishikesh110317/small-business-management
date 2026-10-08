import React, { useState, useEffect } from 'react';
import { Table, Container, Badge, Card, Spinner, Alert } from 'react-bootstrap';
import { toast } from 'react-toastify';
import api from '../services/api';

const Inventory = () => {
    const [inventory, setInventory] = useState([]);
    const [alerts, setAlerts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchInventoryData();
    }, []);

    const fetchInventoryData = async () => {
        setLoading(true);
        try {
            const [invRes, alertsRes] = await Promise.all([
                api.get('/inventory'),
                api.get('/inventory/alerts')
            ]);
            setInventory(invRes.data);
            setAlerts(alertsRes.data);
        } catch (error) {
            toast.error('Failed to fetch inventory data');
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

    return (
        <Container fluid>
            <h2 className="mb-4">Inventory Management</h2>
            {alerts.length > 0 && (
                <Alert variant="warning">
                    <strong>Low Stock Alerts:</strong> You have {alerts.length} item(s) running low on stock.
                </Alert>
            )}
            <Card>
                <Card.Body>
                    <Table responsive hover>
                        <thead>
                            <tr>
                                <th>Product Name</th>
                                <th>Current Stock</th>
                                <th>Min Level</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {inventory.map(item => (
                                <tr key={item.id || item.productId}>
                                    <td>{item.productName}</td>
                                    <td>{item.currentStock}</td>
                                    <td>{item.minStockLevel}</td>
                                    <td>
                                        {item.isLowStock ? (
                                            <Badge bg="danger">Low Stock</Badge>
                                        ) : (
                                            <Badge bg="success">OK</Badge>
                                        )}
                                    </td>
                                </tr>
                            ))}
                            {inventory.length === 0 && (
                                <tr>
                                    <td colSpan="4" className="text-center">No inventory found.</td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default Inventory;
