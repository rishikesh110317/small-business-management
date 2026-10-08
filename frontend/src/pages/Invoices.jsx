import React, { useState, useEffect } from 'react';
import { Table, Container, Badge, Card, Spinner } from 'react-bootstrap';
import { toast } from 'react-toastify';
import api from '../services/api';

const Invoices = () => {
    const [invoices, setInvoices] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchInvoices();
    }, []);

    const fetchInvoices = async () => {
        setLoading(true);
        try {
            const res = await api.get('/invoices');
            setInvoices(res.data);
        } catch (error) {
            toast.error('Failed to fetch invoices');
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const getStatusBadge = (status) => {
        switch (status) {
            case 'PAID': return <Badge bg="success">Paid</Badge>;
            case 'UNPAID': return <Badge bg="warning">Unpaid</Badge>;
            case 'OVERDUE': return <Badge bg="danger">Overdue</Badge>;
            default: return <Badge bg="secondary">{status}</Badge>;
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
            <h2 className="mb-4">Invoices</h2>
            <Card>
                <Card.Body>
                    <Table responsive hover>
                        <thead>
                            <tr>
                                <th>Invoice #</th>
                                <th>Sale #</th>
                                <th>Customer</th>
                                <th>Amount</th>
                                <th>Status</th>
                                <th>Issued Date</th>
                                <th>Due Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            {invoices.map(invoice => (
                                <tr key={invoice.id}>
                                    <td>{invoice.invoiceNumber}</td>
                                    <td>{invoice.saleNumber}</td>
                                    <td>{invoice.customerName}</td>
                                    <td>₹{invoice.totalAmount}</td>
                                    <td>{getStatusBadge(invoice.status)}</td>
                                    <td>{new Date(invoice.issuedDate).toLocaleDateString()}</td>
                                    <td>{new Date(invoice.dueDate).toLocaleDateString()}</td>
                                </tr>
                            ))}
                            {invoices.length === 0 && (
                                <tr>
                                    <td colSpan="7" className="text-center">No invoices found.</td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>
        </Container>
    );
};

export default Invoices;
