import { useState, useEffect } from 'react';
import { Card, Form, Button, Table, Badge, Row, Col, Spinner } from 'react-bootstrap';
import { toast } from 'react-toastify';
import { MdAdd, MdArrowBack, MdDelete } from 'react-icons/md';
import api from '../services/api';

const Purchases = () => {
  const [purchases, setPurchases] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [view, setView] = useState('list');

  const [formData, setFormData] = useState({
    supplierId: '',
    notes: '',
    items: []
  });

  useEffect(() => {
    if (view === 'list') {
      fetchPurchases();
    } else {
      fetchSuppliers();
      fetchProducts();
      setFormData({ supplierId: '', notes: '', items: [] });
    }
  }, [view]);

  const fetchPurchases = async () => {
    setLoading(true);
    try {
      const res = await api.get('/purchases');
      setPurchases(res.data.content || []);
    } catch (err) {
      toast.error('Failed to fetch purchases');
    } finally {
      setLoading(false);
    }
  };

  const fetchSuppliers = async () => {
    try {
      const res = await api.get('/suppliers');
      setSuppliers(res.data.content || []);
    } catch (err) {
      toast.error('Failed to fetch suppliers');
    }
  };

  const fetchProducts = async () => {
    try {
      const res = await api.get('/products');
      setProducts(Array.isArray(res.data) ? res.data : (res.data.content || []));
    } catch (err) {
      toast.error('Failed to fetch products');
    }
  };

  const addItemRow = () => {
    setFormData({
      ...formData,
      items: [...formData.items, { productId: '', quantity: 1, unitPrice: 0 }]
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
      const product = products.find(p => p.id === Number(value));
      if (product) {
        item.unitPrice = Number(product.costPrice) || 0;
      }
    }

    newItems[index] = item;
    setFormData({ ...formData, items: newItems });
  };

  const getLineTotal = (item) => {
    return (Number(item.quantity) || 0) * (Number(item.unitPrice) || 0);
  };

  const calculateGrandTotal = () => {
    return formData.items.reduce((sum, item) => sum + getLineTotal(item), 0);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (formData.items.length === 0) {
      toast.warning('Please add at least one item');
      return;
    }
    for (const item of formData.items) {
      if (!item.productId || item.quantity < 1 || item.unitPrice <= 0) {
        toast.warning('Please fill all item fields correctly');
        return;
      }
    }

    const payload = {
      supplierId: Number(formData.supplierId),
      notes: formData.notes,
      items: formData.items.map(item => ({
        productId: Number(item.productId),
        quantity: Number(item.quantity),
        unitPrice: Number(item.unitPrice)
      }))
    };

    try {
      await api.post('/purchases', payload);
      toast.success('Purchase created successfully!');
      setView('list');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to create purchase');
    }
  };

  if (view === 'create') {
    return (
      <div>
        <div className="d-flex align-items-center mb-4">
          <Button variant="light" className="me-3" onClick={() => setView('list')}>
            <MdArrowBack /> Back
          </Button>
          <h2 className="mb-0">Create Purchase</h2>
        </div>

        <Card>
          <Card.Body>
            <Form onSubmit={handleSubmit}>
              <Row className="mb-3">
                <Col md={6}>
                  <Form.Group>
                    <Form.Label>Supplier <span className="text-danger">*</span></Form.Label>
                    <Form.Select
                      required
                      value={formData.supplierId}
                      onChange={e => setFormData({ ...formData, supplierId: e.target.value })}
                    >
                      <option value="">Select Supplier</option>
                      {suppliers.map(s => (
                        <option key={s.id} value={s.id}>{s.name}</option>
                      ))}
                    </Form.Select>
                  </Form.Group>
                </Col>
              </Row>

              <h5 className="mt-4 mb-3">Items</h5>
              <Table bordered>
                <thead className="table-light">
                  <tr>
                    <th>Product</th>
                    <th style={{ width: 120 }}>Quantity</th>
                    <th style={{ width: 150 }}>Unit Price</th>
                    <th style={{ width: 150 }}>Total</th>
                    <th style={{ width: 50 }}></th>
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
                            <option key={p.id} value={p.id}>{p.name} (Stock: {p.currentStock || 0})</option>
                          ))}
                        </Form.Select>
                      </td>
                      <td>
                        <Form.Control
                          type="number"
                          min="1"
                          required
                          value={item.quantity}
                          onChange={e => handleItemChange(index, 'quantity', e.target.value)}
                        />
                      </td>
                      <td>
                        <Form.Control
                          type="number"
                          step="0.01"
                          min="0.01"
                          required
                          value={item.unitPrice}
                          onChange={e => handleItemChange(index, 'unitPrice', e.target.value)}
                        />
                      </td>
                      <td className="align-middle fw-bold">
                        ₹{getLineTotal(item).toFixed(2)}
                      </td>
                      <td className="text-center align-middle">
                        <Button variant="outline-danger" size="sm" onClick={() => removeItemRow(index)}>
                          <MdDelete />
                        </Button>
                      </td>
                    </tr>
                  ))}
                  {formData.items.length === 0 && (
                    <tr>
                      <td colSpan="5" className="text-center text-muted py-3">
                        No items added. Click "Add Item" below.
                      </td>
                    </tr>
                  )}
                </tbody>
                <tfoot>
                  <tr>
                    <td colSpan="5">
                      <Button variant="outline-primary" size="sm" onClick={addItemRow}>
                        <MdAdd className="me-1" /> Add Item
                      </Button>
                    </td>
                  </tr>
                  <tr className="table-light">
                    <td colSpan="3" className="text-end fw-bold">Grand Total:</td>
                    <td colSpan="2" className="fw-bold">₹{calculateGrandTotal().toFixed(2)}</td>
                  </tr>
                </tfoot>
              </Table>

              <Form.Group className="mb-4">
                <Form.Label>Notes</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={2}
                  value={formData.notes}
                  onChange={e => setFormData({ ...formData, notes: e.target.value })}
                />
              </Form.Group>

              <div className="d-flex justify-content-end">
                <Button variant="secondary" className="me-2" onClick={() => setView('list')}>Cancel</Button>
                <Button variant="success" type="submit">Save Purchase</Button>
              </div>
            </Form>
          </Card.Body>
        </Card>
      </div>
    );
  }

  return (
    <div>
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Purchases</h2>
        <Button onClick={() => setView('create')}><MdAdd className="me-1" /> New Purchase</Button>
      </div>

      <Card>
        <Card.Body>
          {loading ? (
            <div className="text-center py-4"><Spinner animation="border" /></div>
          ) : (
            <Table responsive hover>
              <thead>
                <tr>
                  <th>Purchase #</th>
                  <th>Supplier</th>
                  <th>Total Amount</th>
                  <th>Status</th>
                  <th>Date</th>
                </tr>
              </thead>
              <tbody>
                {purchases.map(p => (
                  <tr key={p.id}>
                    <td>{p.purchaseNumber}</td>
                    <td>{p.supplierName}</td>
                    <td>₹{Number(p.totalAmount).toFixed(2)}</td>
                    <td>
                      <Badge bg={p.status === 'COMPLETED' ? 'success' : 'warning'}>
                        {p.status}
                      </Badge>
                    </td>
                    <td>{p.createdAt ? new Date(p.createdAt).toLocaleDateString() : '-'}</td>
                  </tr>
                ))}
                {purchases.length === 0 && (
                  <tr><td colSpan="5" className="text-center text-muted">No purchases yet</td></tr>
                )}
              </tbody>
            </Table>
          )}
        </Card.Body>
      </Card>
    </div>
  );
};

export default Purchases;
