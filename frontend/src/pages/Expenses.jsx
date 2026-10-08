import React, { useState, useEffect } from 'react';
import { Table, Container, Row, Col, Card, Spinner, Button, Modal, Form } from 'react-bootstrap';
import { MdAdd, MdDelete } from 'react-icons/md';
import { toast } from 'react-toastify';
import api from '../services/api';

const Expenses = () => {
    const [expenses, setExpenses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    
    const [formData, setFormData] = useState({
        category: 'OTHER',
        amount: '',
        description: '',
        expenseDate: new Date().toISOString().split('T')[0]
    });

    useEffect(() => {
        fetchExpenses();
    }, []);

    const fetchExpenses = async () => {
        setLoading(true);
        try {
            const res = await api.get('/expenses');
            setExpenses(res.data.content || []);
        } catch (error) {
            toast.error('Failed to fetch expenses');
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const handleClose = () => {
        setShowModal(false);
        setFormData({
            category: 'OTHER',
            amount: '',
            description: '',
            expenseDate: new Date().toISOString().split('T')[0]
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
            await api.post('/expenses', formData);
            toast.success('Expense added successfully');
            handleClose();
            fetchExpenses();
        } catch (error) {
            toast.error('Failed to add expense');
            console.error(error);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this expense?')) {
            try {
                await api.delete(`/expenses/${id}`);
                toast.success('Expense deleted successfully');
                fetchExpenses();
            } catch (error) {
                toast.error('Failed to delete expense');
                console.error(error);
            }
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
                    <h2>Expenses</h2>
                </Col>
                <Col className="text-end">
                    <Button variant="primary" onClick={handleShow}>
                        <MdAdd /> Add Expense
                    </Button>
                </Col>
            </Row>
            
            <Card>
                <Card.Body>
                    <Table responsive hover>
                        <thead>
                            <tr>
                                <th>Category</th>
                                <th>Amount</th>
                                <th>Description</th>
                                <th>Date</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {expenses.map(expense => (
                                <tr key={expense.id}>
                                    <td>{expense.category}</td>
                                    <td>₹{expense.amount}</td>
                                    <td>{expense.description}</td>
                                    <td>{new Date(expense.expenseDate).toLocaleDateString()}</td>
                                    <td>
                                        <Button variant="outline-danger" size="sm" onClick={() => handleDelete(expense.id)}>
                                            <MdDelete />
                                        </Button>
                                    </td>
                                </tr>
                            ))}
                            {expenses.length === 0 && (
                                <tr>
                                    <td colSpan="5" className="text-center">No expenses found.</td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>

            <Modal show={showModal} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add Expense</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmit}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Category</Form.Label>
                            <Form.Select 
                                name="category" 
                                value={formData.category} 
                                onChange={handleChange}
                                required
                            >
                                <option value="RENT">Rent</option>
                                <option value="SALARIES">Salaries</option>
                                <option value="ELECTRICITY">Electricity</option>
                                <option value="MARKETING">Marketing</option>
                                <option value="OTHER">Other</option>
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
                            <Form.Label>Expense Date</Form.Label>
                            <Form.Control 
                                type="date" 
                                name="expenseDate" 
                                value={formData.expenseDate} 
                                onChange={handleChange}
                                required 
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Description</Form.Label>
                            <Form.Control 
                                as="textarea" 
                                name="description" 
                                value={formData.description} 
                                onChange={handleChange}
                                rows={3}
                            />
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={handleClose}>Cancel</Button>
                        <Button variant="primary" type="submit">Save Expense</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </Container>
    );
};

export default Expenses;
