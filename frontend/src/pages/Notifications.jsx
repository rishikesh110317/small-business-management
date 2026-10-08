import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Spinner, Button, ListGroup } from 'react-bootstrap';
import { toast } from 'react-toastify';
import api from '../services/api';

const Notifications = () => {
    const [notifications, setNotifications] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchNotifications();
    }, []);

    const fetchNotifications = async () => {
        setLoading(true);
        try {
            const res = await api.get('/notifications');
            setNotifications(res.data.content || []);
        } catch (error) {
            toast.error('Failed to fetch notifications');
            console.error(error);
        } finally {
            setLoading(false);
        }
    };

    const markAllAsRead = async () => {
        try {
            await api.post('/notifications/mark-all-read');
            toast.success('All notifications marked as read');
            fetchNotifications();
        } catch (error) {
            toast.error('Failed to mark notifications as read');
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
                    <h2>Notifications</h2>
                </Col>
                <Col className="text-end">
                    <Button variant="outline-primary" onClick={markAllAsRead} disabled={notifications.length === 0}>
                        Mark All as Read
                    </Button>
                </Col>
            </Row>
            
            <Card>
                <ListGroup variant="flush">
                    {notifications.map(notification => (
                        <ListGroup.Item 
                            key={notification.id}
                            className={notification.isRead ? '' : 'bg-light fw-bold'}
                        >
                            <div className="d-flex w-100 justify-content-between">
                                <h6 className="mb-1">{notification.title}</h6>
                                <small>{new Date(notification.createdAt).toLocaleString()}</small>
                            </div>
                            <p className="mb-1 text-muted">{notification.message}</p>
                            <small className="text-muted">Type: {notification.type}</small>
                        </ListGroup.Item>
                    ))}
                    {notifications.length === 0 && (
                        <ListGroup.Item className="text-center py-4 text-muted">
                            No notifications available.
                        </ListGroup.Item>
                    )}
                </ListGroup>
            </Card>
        </Container>
    );
};

export default Notifications;
