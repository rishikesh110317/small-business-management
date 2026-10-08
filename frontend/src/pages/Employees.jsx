import React, { useState, useEffect } from 'react';
import { Table, Container, Row, Col, Card, Spinner, Button, Modal, Form, Badge } from 'react-bootstrap';
import { MdAdd, MdEdit, MdDelete } from 'react-icons/md';
import { toast } from 'react-toastify';
import api from '../services/api';

const Employees = () => {
    const [employees, setEmployees] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    
    const [formData, setFormData] = useState({
        id: null,
        fullName: '',
        email: '',
        password: '',
        phone: '',
        isActive: true,
        roleName: 'ROLE_EMPLOYEE'
    });

    useEffect(() => {
        fetchEmployees();
    }, []);

    const fetchEmployees = async () => {
        setLoading(true);
        try {
            const res = await api.get('/employees');
            setEmployees(res.data.content || []);
        } catch (error) {
            toast.error('Failed to fetch employees');
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const handleClose = () => {
        setShowModal(false);
        setFormData({
            id: null,
            fullName: '',
            email: '',
            password: '',
            phone: '',
            isActive: true,
            roleName: 'ROLE_EMPLOYEE'
        });
        setIsEditing(false);
    };

    const handleShow = () => setShowModal(true);

    const handleEdit = (employee) => {
        setFormData({
            id: employee.id,
            fullName: employee.fullName,
            email: employee.email,
            password: '',
            phone: employee.phone || '',
            isActive: employee.isActive,
            roleName: employee.roleName || 'ROLE_EMPLOYEE'
        });
        setIsEditing(true);
        handleShow();
    };

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setFormData({ 
            ...formData, 
            [name]: type === 'checkbox' ? checked : value 
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (isEditing) {
                const { password, ...updateData } = formData;
                await api.put(`/employees/${formData.id}`, updateData);
                toast.success('Employee updated successfully');
            } else {
                await api.post('/employees', formData);
                toast.success('Employee added successfully');
            }
            handleClose();
            fetchEmployees();
        } catch (error) {
            toast.error(isEditing ? 'Failed to update employee' : 'Failed to add employee');
            console.error(error);
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this employee?')) {
            try {
                await api.delete(`/employees/${id}`);
                toast.success('Employee deleted successfully');
                fetchEmployees();
            } catch (error) {
                toast.error('Failed to delete employee');
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
                    <h2>Employees</h2>
                </Col>
                <Col className="text-end">
                    <Button variant="primary" onClick={handleShow}>
                        <MdAdd /> Add Employee
                    </Button>
                </Col>
            </Row>
            
            <Card>
                <Card.Body>
                    <Table responsive hover>
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {employees.map(employee => (
                                <tr key={employee.id}>
                                    <td>{employee.fullName}</td>
                                    <td>{employee.email}</td>
                                    <td>{employee.phone}</td>
                                    <td>{employee.roleName}</td>
                                    <td>
                                        {employee.isActive ? (
                                            <Badge bg="success">Active</Badge>
                                        ) : (
                                            <Badge bg="secondary">Inactive</Badge>
                                        )}
                                    </td>
                                    <td>
                                        <Button variant="outline-primary" size="sm" className="me-2" onClick={() => handleEdit(employee)}>
                                            <MdEdit />
                                        </Button>
                                        <Button variant="outline-danger" size="sm" onClick={() => handleDelete(employee.id)}>
                                            <MdDelete />
                                        </Button>
                                    </td>
                                </tr>
                            ))}
                            {employees.length === 0 && (
                                <tr>
                                    <td colSpan="6" className="text-center">No employees found.</td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </Card.Body>
            </Card>

            <Modal show={showModal} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>{isEditing ? 'Edit Employee' : 'Add Employee'}</Modal.Title>
                </Modal.Header>
                <Form onSubmit={handleSubmit}>
                    <Modal.Body>
                        <Form.Group className="mb-3">
                            <Form.Label>Full Name</Form.Label>
                            <Form.Control 
                                type="text" 
                                name="fullName" 
                                value={formData.fullName} 
                                onChange={handleChange}
                                required 
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Email</Form.Label>
                            <Form.Control 
                                type="email" 
                                name="email" 
                                value={formData.email} 
                                onChange={handleChange}
                                required 
                            />
                        </Form.Group>
                        {!isEditing && (
                            <Form.Group className="mb-3">
                                <Form.Label>Password</Form.Label>
                                <Form.Control 
                                    type="password" 
                                    name="password" 
                                    value={formData.password} 
                                    onChange={handleChange}
                                    required 
                                />
                            </Form.Group>
                        )}
                        <Form.Group className="mb-3">
                            <Form.Label>Phone</Form.Label>
                            <Form.Control 
                                type="text" 
                                name="phone" 
                                value={formData.phone} 
                                onChange={handleChange}
                            />
                        </Form.Group>
                        {isEditing && (
                            <Form.Group className="mb-3">
                                <Form.Check 
                                    type="checkbox"
                                    name="isActive"
                                    label="Active Account"
                                    checked={formData.isActive}
                                    onChange={handleChange}
                                />
                            </Form.Group>
                        )}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={handleClose}>Cancel</Button>
                        <Button variant="primary" type="submit">{isEditing ? 'Update' : 'Save'}</Button>
                    </Modal.Footer>
                </Form>
            </Modal>
        </Container>
    );
};

export default Employees;
