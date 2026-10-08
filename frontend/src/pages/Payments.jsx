import React, { useState, useEffect } from 'react';
import { Table, Container, Row, Col, Card, Spinner, Button, Modal, Form } from 'react-bootstrap';
import { MdAdd } from 'react-icons/md';
import { toast } from 'react-toastify';
import api from '../services/api';

const Payments = () => {
    const [payments, setPayments] = useState([]);
    const [sales, setSales] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    
    const [formData, setFormData] = useState({
        saleId: '',
        amount: '',
        paymentMethod: 'CASH',
        paymentDate: new Date().toISOString().split('T')[0],
        notes: ''
    });

    useEffect(() => {
        fetchPayments();
        fetchSales();
    }, []);

    const fetchPayments = async () => {
        setLoading(true);
        try {
            const res = await api.get('/payments');
            setPayments(res.data);
        } catch (error) {
            toast.error('Failed to fetch payments');
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const fetchSales = async () => {
        try {
            const res = await api.get('/sales');
            setSales(res.data.content || []);
        } catch (error) {
            toast.error('Failed to fetch sales');
            console.error(error);
        }
    };

    const handleClose = () => {
        setShowModal(false);
        setFormData({
            saleId: '',
            amount: '',
            paymentMethod: 'CASH',
            paymentDate: new Date().toISOString().split('T')[0],
            notes: ''
        });
    };

    const handleShow = () => setShowModal(true);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post('/payments', formData);
            toast.success('Payment added successfully');
            handleClose();
            fetchPayments();
        } catch (error) {
            toast.error('Failed to add payment');
            console.error(error);
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
            <Row className="mb-4 align-items-center">
                <Col>
                    <h2>Payments</h2>
                </Col>
                <Col className="text-end">
                    <Button variant="primary" onClick={handleShow}>
                        <MdAdd /> Add Payment
                    </Button>
                </Col>
            </Row>
            
            <Card>
                <Card.Body>
                    <Table responsive hover>
                        <thead>
                            <tr>
                                <th>Sale #</th>
                                <th>Customer</th>
                                <th>Amount</th>
                                <th>Method</th>
                                <th>Date</th>
                                <th>Notes</th>
                            </tr>
                        </thead>
                        <tbody>
                            {payments.map(payment => (
                                <tr key={payment.id}>
                                    <td>{payment.saleNumber}</td>
                                    <td>{payment.customerName}</td>
                                    <td>₹{payment.amount}</td>
                                    <td>{payment.paymentMethod}</td>
                                    <td>{new Date(payment.paymentDate).toLocaleDateString()}</td>
                                    <td>{payment.notes}</td>
                                </tr>
                            ))}
                            {payments.length === 0 && (
                                <tr>
                                    <td colSpan="6" className="text-center">No payments found.</td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>

            <Modal show={showModal} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add Payment</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmit}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Sale</Form.Label>
                            <Form.Select 
                                name="saleId" 
                                value={formData.saleId} 
                                onChange={handleChange}
                                required
                            >
                                <option value="">Select a sale</option>
                                {sales.map(sale => (
                                    <option key={sale.id} value={sale.id}>
                                        {sale.saleNumber} - {sale.customerName} (Total: ₹{sale.totalAmount})
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Amount</Form.Label>
                            <Form.Control 
                                type="number" 
                                name="amount" 
                                value={formData.amount} 
                                onChange={handleChange}
                                required 
                                step="0.01"
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Payment Method</Form.Label>
                            <Form.Select 
                                name="paymentMethod" 
                                value={formData.paymentMethod} 
                                onChange={handleChange}
                                required
                            >
                                <option value="CASH">Cash</option>
                                <option value="UPI">UPI</option>
                                <option value="CARD">Card</option>
                                <option value="BANK_TRANSFER">Bank Transfer</option>
                                <option value="OTHER">Other</option>
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Payment Date</Form.Label>
                            <Form.Control 
                                type="date" 
                                name="paymentDate" 
                                value={formData.paymentDate} 
                                onChange={handleChange}
                                required 
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Notes</Form.Label>
                            <Form.Control 
                                as="textarea" 
                                name="notes" 
                                value={formData.notes} 
                                onChange={handleChange}
                                rows={3}
                            />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={handleClose}>Cancel</Button>
                        <Button variant="primary" type="submit">Save Payment</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </Container>
    );
};

export default Payments;
