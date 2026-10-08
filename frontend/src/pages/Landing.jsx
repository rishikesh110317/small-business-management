import { Link } from 'react-router-dom';
import { MdInventory2, MdPointOfSale, MdPeople, MdBarChart, MdSecurity, MdCloud, MdShoppingCart, MdPayment, MdReceipt, MdArrowForward, MdCheck } from 'react-icons/md';

const Landing = () => {
  return (
    <div style={{ fontFamily: "'Segoe UI', sans-serif" }}>
      {/* Navbar */}
      <nav style={{
        position: 'fixed', top: 0, width: '100%', zIndex: 1000,
        background: 'rgba(255,255,255,0.95)', backdropFilter: 'blur(10px)',
        borderBottom: '1px solid #e2e8f0', padding: '12px 0'
      }}>
        <div className="container d-flex justify-content-between align-items-center">
          <div className="d-flex align-items-center">
            <div style={{
              width: 36, height: 36, borderRadius: 8, background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
              display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'white', fontWeight: 700, fontSize: 18, marginRight: 10
            }}>S</div>
            <span style={{ fontSize: 20, fontWeight: 700, color: '#1e293b' }}>SBM</span>
            <span style={{ fontSize: 14, color: '#64748b', marginLeft: 6 }}>SaaS</span>
          </div>
          <div className="d-flex gap-2">
            <Link to="/login" className="btn btn-outline-primary btn-sm px-3">Sign In</Link>
            <Link to="/register" className="btn btn-primary btn-sm px-3">Get Started Free</Link>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section style={{
        paddingTop: 120, paddingBottom: 80,
        background: 'linear-gradient(135deg, #f8faff 0%, #eef2ff 50%, #faf5ff 100%)',
        position: 'relative', overflow: 'hidden'
      }}>
        <div style={{
          position: 'absolute', top: -100, right: -100, width: 400, height: 400,
          borderRadius: '50%', background: 'rgba(102, 126, 234, 0.08)'
        }} />
        <div style={{
          position: 'absolute', bottom: -50, left: -50, width: 300, height: 300,
          borderRadius: '50%', background: 'rgba(118, 75, 162, 0.06)'
        }} />
        <div className="container text-center" style={{ position: 'relative', zIndex: 1 }}>
          <div style={{
            display: 'inline-block', background: '#eef2ff', color: '#4f46e5',
            padding: '6px 16px', borderRadius: 20, fontSize: 13, fontWeight: 600, marginBottom: 20
          }}>
            ✨ Multi-Tenant SaaS Platform
          </div>
          <h1 style={{
            fontSize: 'clamp(32px, 5vw, 56px)', fontWeight: 800, color: '#0f172a',
            lineHeight: 1.15, maxWidth: 800, margin: '0 auto 20px'
          }}>
            Manage Your <span style={{
              background: 'linear-gradient(135deg, #667eea, #764ba2)',
              WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent'
            }}>Small Business</span>
            <br />All in One Place
          </h1>
          <p style={{
            fontSize: 18, color: '#475569', maxWidth: 600, margin: '0 auto 32px', lineHeight: 1.7
          }}>
            Track inventory, manage sales & purchases, generate invoices, monitor expenses,
            and get real-time analytics — everything your business needs to grow.
          </p>
          <div className="d-flex justify-content-center gap-3 flex-wrap">
            <Link to="/register" className="btn btn-lg px-4 py-2" style={{
              background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
              color: 'white', border: 'none', borderRadius: 10, fontWeight: 600, fontSize: 16
            }}>
              Start Free Now <MdArrowForward style={{ marginLeft: 6 }} />
            </Link>
            <Link to="/login" className="btn btn-lg btn-outline-secondary px-4 py-2" style={{
              borderRadius: 10, fontWeight: 600, fontSize: 16
            }}>
              Sign In
            </Link>
          </div>
          <p style={{ fontSize: 13, color: '#94a3b8', marginTop: 16 }}>
            No credit card required • Free for small businesses
          </p>
        </div>
      </section>

      {/* Stats Bar */}
      <section style={{ background: '#1e293b', padding: '28px 0' }}>
        <div className="container">
          <div className="row text-center text-white">
            {[
              { num: '14+', label: 'Modules' },
              { num: '3', label: 'User Roles' },
              { num: '100%', label: 'Data Isolation' },
              { num: 'Real-time', label: 'Analytics' }
            ].map((s, i) => (
              <div key={i} className="col-6 col-md-3 py-2">
                <div style={{ fontSize: 28, fontWeight: 800 }}>{s.num}</div>
                <div style={{ fontSize: 13, color: '#94a3b8' }}>{s.label}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Features Grid */}
      <section style={{ padding: '80px 0', background: '#fff' }}>
        <div className="container">
          <div className="text-center mb-5">
            <h2 style={{ fontSize: 36, fontWeight: 800, color: '#0f172a', marginBottom: 12 }}>
              Everything You Need to Run Your Business
            </h2>
            <p style={{ fontSize: 16, color: '#64748b', maxWidth: 550, margin: '0 auto' }}>
              From inventory management to financial analytics, all modules work together seamlessly.
            </p>
          </div>
          <div className="row g-4">
            {[
              { icon: <MdInventory2 size={28} />, title: 'Products & Inventory', desc: 'Track stock levels in real-time. Auto-update on purchases and sales. Low stock alerts.', color: '#3b82f6' },
              { icon: <MdPointOfSale size={28} />, title: 'Sales Management', desc: 'Create sales, auto-generate invoices, track payments and outstanding amounts.', color: '#10b981' },
              { icon: <MdShoppingCart size={28} />, title: 'Purchase Orders', desc: 'Purchase from suppliers with automatic inventory increase and balance tracking.', color: '#f59e0b' },
              { icon: <MdPeople size={28} />, title: 'Customers & Suppliers', desc: 'Maintain complete profiles, contact details, and transaction history.', color: '#8b5cf6' },
              { icon: <MdPayment size={28} />, title: 'Payments & Expenses', desc: 'Record payments, track expenses by category (rent, salaries, marketing).', color: '#ef4444' },
              { icon: <MdReceipt size={28} />, title: 'Invoices', desc: 'Auto-generated invoices from sales with status tracking (paid/pending).', color: '#06b6d4' },
              { icon: <MdBarChart size={28} />, title: 'Reports & Analytics', desc: 'Revenue charts, expense breakdowns, monthly trends, and profit analysis.', color: '#f97316' },
              { icon: <MdSecurity size={28} />, title: 'Secure & Isolated', desc: 'JWT authentication, BCrypt hashing, role-based access. Each business data is isolated.', color: '#6366f1' },
              { icon: <MdCloud size={28} />, title: 'Cloud SaaS', desc: 'Multi-tenant architecture. Multiple businesses on one platform, zero data leaks.', color: '#14b8a6' }
            ].map((f, i) => (
              <div key={i} className="col-md-6 col-lg-4">
                <div style={{
                  padding: 28, borderRadius: 14, border: '1px solid #f1f5f9',
                  height: '100%', transition: 'all 0.2s',
                  cursor: 'default'
                }}
                  onMouseEnter={e => { e.currentTarget.style.boxShadow = '0 8px 30px rgba(0,0,0,0.08)'; e.currentTarget.style.borderColor = '#e2e8f0'; }}
                  onMouseLeave={e => { e.currentTarget.style.boxShadow = 'none'; e.currentTarget.style.borderColor = '#f1f5f9'; }}
                >
                  <div style={{
                    width: 48, height: 48, borderRadius: 12,
                    background: `${f.color}15`, color: f.color,
                    display: 'flex', alignItems: 'center', justifyContent: 'center', marginBottom: 16
                  }}>
                    {f.icon}
                  </div>
                  <h5 style={{ fontWeight: 700, color: '#0f172a', marginBottom: 8 }}>{f.title}</h5>
                  <p style={{ color: '#64748b', fontSize: 14, marginBottom: 0, lineHeight: 1.6 }}>{f.desc}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Workflow Section */}
      <section style={{ padding: '80px 0', background: '#f8fafc' }}>
        <div className="container">
          <div className="text-center mb-5">
            <h2 style={{ fontSize: 36, fontWeight: 800, color: '#0f172a', marginBottom: 12 }}>
              How It Works
            </h2>
            <p style={{ fontSize: 16, color: '#64748b' }}>Simple workflow from setup to analytics</p>
          </div>
          <div className="row g-4 justify-content-center">
            {[
              { step: '1', title: 'Register Business', desc: 'Sign up with your business name, email and get started instantly.' },
              { step: '2', title: 'Set Up Catalog', desc: 'Add categories, products, suppliers, employees and customers.' },
              { step: '3', title: 'Purchase Stock', desc: 'Create purchase orders from suppliers. Inventory auto-increases.' },
              { step: '4', title: 'Sell to Customers', desc: 'Make sales, collect payments. Stock auto-decreases. Invoices auto-generated.' },
              { step: '5', title: 'Track Finances', desc: 'Record expenses, monitor payments, check profit and outstanding balances.' },
              { step: '6', title: 'Analyze & Grow', desc: 'View dashboard KPIs, sales charts, expense reports and make informed decisions.' }
            ].map((w, i) => (
              <div key={i} className="col-md-6 col-lg-4">
                <div className="text-center p-4">
                  <div style={{
                    width: 56, height: 56, borderRadius: '50%',
                    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                    color: 'white', display: 'flex', alignItems: 'center', justifyContent: 'center',
                    fontSize: 22, fontWeight: 800, margin: '0 auto 16px'
                  }}>{w.step}</div>
                  <h5 style={{ fontWeight: 700, color: '#0f172a', marginBottom: 8 }}>{w.title}</h5>
                  <p style={{ color: '#64748b', fontSize: 14, marginBottom: 0 }}>{w.desc}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Tech Stack */}
      <section style={{ padding: '60px 0', background: '#fff' }}>
        <div className="container">
          <div className="text-center mb-4">
            <h3 style={{ fontWeight: 700, color: '#0f172a' }}>Built With Modern Tech Stack</h3>
          </div>
          <div className="d-flex flex-wrap justify-content-center gap-3">
            {['React.js', 'Spring Boot', 'Spring Security', 'JWT', 'Hibernate', 'MySQL', 'REST API', 'Bootstrap'].map((t, i) => (
              <span key={i} style={{
                padding: '8px 20px', borderRadius: 8, background: '#f1f5f9',
                color: '#334155', fontWeight: 600, fontSize: 14
              }}>{t}</span>
            ))}
          </div>
        </div>
      </section>

      {/* Roles Section */}
      <section style={{ padding: '60px 0', background: '#f8fafc' }}>
        <div className="container">
          <div className="text-center mb-5">
            <h3 style={{ fontWeight: 700, color: '#0f172a' }}>Three Powerful Roles</h3>
          </div>
          <div className="row g-4">
            {[
              {
                role: 'Business Owner', color: '#667eea',
                perms: ['Full business management', 'Manage employees & permissions', 'View all reports & analytics', 'Handle purchases & sales']
              },
              {
                role: 'Employee', color: '#10b981',
                perms: ['Create sales & invoices', 'Manage customers', 'View assigned products', 'Limited access scope']
              },
              {
                role: 'Platform Admin', color: '#f59e0b',
                perms: ['Manage all businesses', 'Monitor platform usage', 'Manage plans & subscriptions', 'System-level control']
              }
            ].map((r, i) => (
              <div key={i} className="col-md-4">
                <div style={{
                  padding: 28, borderRadius: 14, background: '#fff',
                  border: '1px solid #e2e8f0', height: '100%'
                }}>
                  <div style={{
                    fontSize: 14, fontWeight: 700, color: r.color,
                    textTransform: 'uppercase', letterSpacing: 1, marginBottom: 12
                  }}>{r.role}</div>
                  <ul className="list-unstyled mb-0">
                    {r.perms.map((p, j) => (
                      <li key={j} className="d-flex align-items-center mb-2">
                        <MdCheck style={{ color: r.color, marginRight: 8, flexShrink: 0 }} />
                        <span style={{ color: '#475569', fontSize: 14 }}>{p}</span>
                      </li>
                    ))}
                  </ul>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section style={{
        padding: '80px 0',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        color: 'white', textAlign: 'center'
      }}>
        <div className="container">
          <h2 style={{ fontSize: 36, fontWeight: 800, marginBottom: 16 }}>
            Ready to Simplify Your Business?
          </h2>
          <p style={{ fontSize: 18, opacity: 0.9, maxWidth: 500, margin: '0 auto 32px' }}>
            Stop using spreadsheets. Start managing your business the smart way.
          </p>
          <Link to="/register" className="btn btn-lg px-5 py-3" style={{
            background: 'white', color: '#667eea', fontWeight: 700,
            borderRadius: 12, fontSize: 16, border: 'none'
          }}>
            Get Started — It's Free
          </Link>
        </div>
      </section>

      {/* Footer */}
      <footer style={{ background: '#0f172a', padding: '40px 0', color: '#94a3b8' }}>
        <div className="container text-center">
          <div className="mb-2">
            <span style={{ color: '#fff', fontWeight: 700, fontSize: 18 }}>SBM</span>
            <span style={{ marginLeft: 6, fontSize: 13 }}>Small Business Management SaaS</span>
          </div>
          <p style={{ fontSize: 13, marginBottom: 0 }}>
            Built with Java + Spring Boot + React.js • College Project
          </p>
        </div>
      </footer>
    </div>
  );
};

export default Landing;
