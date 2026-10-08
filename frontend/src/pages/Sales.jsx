import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Form, Button, Table, Badge } from 'react-bootstrap';
import { toast } from 'react-toastify';
import { MdAdd, MdArrowBack, MdDelete } from 'react-icons/md';
import api from '../services/api';

const Sales = () => {
  const [sales, setSales] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [view, setView] = useState('list'); // 'list' or 'create'
  
  // Create form state
  const [formData, setFormData] = useState({
    customerId: '',
    paymentMethod: 'CASH',
    paidAmount: 0,
    notes: '',
    items: []
  });

  useEffect(() => {
    if (view === 'list') {
      fetchSales();
    } else if (view === 'create') {
      fetchCustomers();
      fetchProducts();
      setFormData({ customerId: '', paymentMethod: 'CASH', paidAmount: 0, notes: '', items: [] });
    }
  }, [view]);

  const fetchSales = async () => {
    setLoading(true);
    try {
      const res = await api.get('/sales');
      setSales(res.data.content || []);
    } catch (error) {
      toast.error('Failed to fetch sales');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const fetchCustomers = async () => {
    try {
      const res = await api.get('/customers');
      setCustomers(res.data.content || []);
    } catch (error) {
      toast.error('Failed to fetch customers');
    }
  };

  const fetchProducts = async () => {
    try {
      const res = await api.get('/products');
      setProducts(Array.isArray(res.data) ? res.data : (res.data.content || []));
    } catch (error) {
      toast.error('Failed to fetch products');
    }
  };

  const addItemRow = () => {
    setFormData({
      ...formData,
      items: [...formData.items, { productId: '', quantity: 1, unitPrice: 0, totalPrice: 0 }]
    });
  };

  const removeItemRow = (index) => {
    const newItems = [...formData.items];
    newItems.splice(index, 1);
    setFormData({ ...formData, items: newItems });
  };

  const handleItemChange = (index, field, value) => {
    const newItems = [...formData.items];
    const item = { ...newItems[index], [field]: value };
    
    if (field === 'productId') {
      const product = products.find(p => p.id === parseInt(value));
      if (product) {
        item.unitPrice = product.price || product.salePrice || 0;
      }
    }
    
    item.totalPrice = item.quantity * item.unitPrice;
    newItems[index] = item;
    
    setFormData({ 
      ...formData, 
      items: newItems
    });
  };

  const calculateGrandTotal = () => {
    return formData.items.reduce((sum, item) => sum + item.totalPrice, 0);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (formData.items.length === 0) {
      toast.warning('Please add at least one item');
      return;
    }
    
    const payload = {
      ...formData,
      customerId: formData.customerId ? parseInt(formData.customerId) : null,
      totalAmount: calculateGrandTotal()
    };
    
    try {
      await api.post('/sales', payload);
      toast.success('Sale created successfully');
      setView('list');
    } catch (error) {
      toast.error('Failed to create sale');
      console.error(error);
    }
  };

  return (
    <Container fluid className="py-4">
      {view === 'list' ? (
        <Card className="shadow-sm">
          <Card.Header className="bg-white d-flex justify-content-between align-items-center py-3">
            <h5 className="mb-0">Sales</h5>
            <Button variant="primary" onClick={() => setView('create')}>
              <MdAdd className="me-1" /> New Sale
            </Button>
          </Card.Header>
          <Card.Body>
            <Table responsive hover>
              <thead>
                <tr>
                  <th>Sale Number</th>
                  <th>Date</th>
                  <th>Customer</th>
                  <th>Total Amount</th>
                  <th>Paid</th>
                  <th>Outstanding</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr><td colSpan="7" className="text-center">Loading...</td></tr>
                ) : sales.length === 0 ? (
                  <tr><td colSpan="7" className="text-center">No sales found</td></tr>
                ) : (
                  sales.map(s => (
                    <tr key={s.id}>
                      <td>{s.saleNumber}</td>
                      <td>{new Date(s.createdAt).toLocaleDateString()}</td>
                      <td>{s.customerName || 'Walk-in Customer'}</td>
                      <td>${s.totalAmount?.toFixed(2)}</td>
                      <td>${s.paidAmount?.toFixed(2)}</td>
                      <td><span className={s.outstandingAmount > 0 ? "text-danger fw-bold" : ""}>${s.outstandingAmount?.toFixed(2)}</span></td>
                      <td>
                        <Badge bg={
                          s.status === 'PAID' ? 'success' : 
                          s.status === 'PARTIAL' ? 'warning' : 'danger'
                        }>
                          {s.status}
                        </Badge>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </Table>
          </Card.Body>
        </Card>
      ) : (
        <Card className="shadow-sm">
          <Card.Header className="bg-white d-flex align-items-center py-3">
            <Button variant="light" className="me-3" onClick={() => setView('list')}>
              <MdArrowBack />
            </Button>
            <h5 className="mb-0">Create New Sale</h5>
          </Card.Header>
          <Card.Body>
            <Form onSubmit={handleSubmit}>
              <Row className="mb-3">
                <Col md={6}>
                  <Form.Group>
                    <Form.Label>Customer (Optional)</Form.Label>
                    <Form.Select 
                      value={formData.customerId}
                      onChange={e => setFormData({...formData, customerId: e.target.value})}
                    >
                      <option value="">Walk-in Customer</option>
                      {customers.map(c => (
                        <option key={c.id} value={c.id}>{c.name}</option>
                      ))}
                    </Form.Select>
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group>
                    <Form.Label>Payment Method</Form.Label>
                    <Form.Select 
                      required
                      value={formData.paymentMethod}
                      onChange={e => setFormData({...formData, paymentMethod: e.target.value})}
                    >
                      <option value="CASH">Cash</option>
                      <option value="UPI">UPI</option>
                      <option value="CARD">Card</option>
                      <option value="BANK_TRANSFER">Bank Transfer</option>
                      <option value="OTHER">Other</option>
                    </Form.Select>
                  </Form.Group>
                </Col>
              </Row>
              
              <h6 className="mt-4 mb-3">Items</h6>
              <Table bordered hover>
                <thead className="bg-light">
                  <tr>
                    <th>Product</th>
                    <th width="150">Quantity</th>
                    <th width="150">Unit Price</th>
                    <th width="150">Total</th>
                    <th width="50"></th>
                  </tr>
                </thead>
                <tbody>
                  {formData.items.map((item, index) => (
                    <tr key={index}>
                      <td>
                        <Form.Select
                          required
                          value={item.productId}
                          onChange={e => handleItemChange(index, 'productId', e.target.value)}
                        >
                          <option value="">Select Product</option>
                          {products.map(p => (
                            <option key={p.id} value={p.id}>{p.name}</option>
                          ))}
                        </Form.Select>
                      </td>
                      <td>
                        <Form.Control
                          type="number"
                          min="1"
                          required
                          value={item.quantity}
                          onChange={e => handleItemChange(index, 'quantity', parseFloat(e.target.value) || 0)}
                        />
                      </td>
                      <td>
                        <Form.Control
                          type="number"
                          step="0.01"
                          min="0"
                          required
                          value={item.unitPrice}
                          onChange={e => handleItemChange(index, 'unitPrice', parseFloat(e.target.value) || 0)}
                        />
                      </td>
                      <td className="align-middle">
                        ${item.totalPrice?.toFixed(2)}
                      </td>
                      <td className="text-center align-middle">
                        <Button variant="danger" size="sm" onClick={() => removeItemRow(index)}>
                          <MdDelete />
                        </Button>
                      </td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr>
                    <td colSpan="5">
                      <Button variant="outline-primary" size="sm" onClick={addItemRow}>
                        <MdAdd /> Add Item
                      </Button>
                    </td>
                  </tr>
                  <tr>
                    <td colSpan="3" className="text-end fw-bold">Grand Total:</td>
                    <td colSpan="2" className="fw-bold">${calculateGrandTotal().toFixed(2)}</td>
                  </tr>
                </tfoot>
              </Table>
              
              <Row className="mb-4">
                <Col md={6}>
                  <Form.Group>
                    <Form.Label>Paid Amount</Form.Label>
                    <Form.Control
                      type="number"
                      step="0.01"
                      min="0"
                      required
                      value={formData.paidAmount}
                      onChange={e => setFormData({...formData, paidAmount: parseFloat(e.target.value) || 0})}
                    />
                    <Form.Text className="text-muted">
                      Total outstanding: ${(calculateGrandTotal() - (formData.paidAmount || 0)).toFixed(2)}
                    </Form.Text>
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <Form.Group>
                    <Form.Label>Notes</Form.Label>
                    <Form.Control
                      as="textarea"
                      rows={2}
                      value={formData.notes}
                      onChange={e => setFormData({...formData, notes: e.target.value})}
                    />
                  </Form.Group>
                </Col>
              </Row>
              
              <div className="d-flex justify-content-end">
                <Button variant="secondary" className="me-2" onClick={() => setView('list')}>
                  Cancel
                </Button>
                <Button variant="success" type="submit">
                  Save Sale
                </Button>
              </div>
            </Form>
          </Card.Body>
        </Card>
      )}
    </Container>
  );
};

export default Sales;
