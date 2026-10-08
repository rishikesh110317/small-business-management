import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Form, Button, Spinner } from 'react-bootstrap';
import { toast } from 'react-toastify';
import api from '../services/api';

const BusinessProfile = () => {
    const [business, setBusiness] = useState({
        id: '',
        name: '',
        ownerName: '',
        email: '',
        phone: '',
        address: '',
        isActive: true
    });
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        fetchBusinessProfile();
    }, []);

    const fetchBusinessProfile = async () => {
        setLoading(true);
        try {
            const res = await api.get('/business');
            if (res.data) {
                setBusiness(res.data);
            }
        } catch (error) {
            toast.error('Failed to fetch business profile');
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setBusiness({ ...business, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);
        try {
            await api.put('/business', business);
            toast.success('Business profile updated successfully');
        } catch (error) {
            toast.error('Failed to update business profile');
            console.error(error);
        } finally {
            setSaving(false);
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
            <Row className="justify-content-md-center">
                <Col md={8}>
                    <h2 className="mb-4">Business Profile</h2>
                    <Card>
                        <Card.Body>
                            <Form onSubmit={handleSubmit}>
                                <Form.Group className="mb-3">
                                    <Form.Label>Business Name</Form.Label>
                                    <Form.Control 
                                        type="text" 
                                        name="name" 
                                        value={business.name || ''} 
                                        onChange={handleChange}
                                        required 
                                    />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Owner Name</Form.Label>
                                    <Form.Control 
                                        type="text" 
                                        name="ownerName" 
                                        value={business.ownerName || ''} 
                                        onChange={handleChange}
                                        required 
                                    />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Email Address</Form.Label>
                                    <Form.Control 
                                        type="email" 
                                        name="email" 
                                        value={business.email || ''} 
                                        onChange={handleChange}
                                        required 
                                    />
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Phone Number</Form.Label>
                                    <Form.Control 
                                        type="text" 
                                        name="phone" 
                                        value={business.phone || ''} 
                                        onChange={handleChange}
                                    />
                                </Form.Group>
                                <Form.Group className="mb-4">
                                    <Form.Label>Address</Form.Label>
                                    <Form.Control 
                                        as="textarea" 
                                        name="address" 
                                        value={business.address || ''} 
                                        onChange={handleChange}
                                        rows={3}
                                    />
                                </Form.Group>
                                <div className="d-grid gap-2">
                                    <Button variant="primary" type="submit" disabled={saving}>
                                        {saving ? 'Saving...' : 'Save Changes'}
                                    </Button>
                                </div>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default BusinessProfile;
