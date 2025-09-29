import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { login as facultyLogin } from '../services/facultylogin';
import { login as adminLogin } from '../services/admin';

export function Login() {
  const [userType, setUserType] = useState('faculty'); // faculty or admin
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate();

  const onLogin = async () => {
    if (!email.trim()) {
      toast.warn('Enter email');
      return;
    }
    if (!password.trim()) {
      toast.warn('Enter password');
      return;
    }

    let result;

    if (userType === 'faculty') {
      result = await facultyLogin(email, password);

      if (result?.status === 'success') {
        const data = result.data;
        sessionStorage.setItem('token', data.token);
        sessionStorage.setItem('usertype', 'Faculty');
        sessionStorage.setItem('rolename', data.rolename);
        sessionStorage.setItem('username', data.username);
        toast.success('Faculty login successful');

        // Navigate based on role name
        const role = data.rolename?.trim(); // normalize
        switch (role) {
          case 'Course Coordinator':
            navigate('/homecc'); // Course Coordinator dashboard
            break;
          case 'Trainer':
          case 'Lab Mentor':
            navigate('/homefaculty'); // Trainer/Lab Mentor dashboard
            break;
          default:
            navigate('/home'); // fallback page
        }
      } else {
        toast.error(result?.error || 'Invalid email or password');
      }
    } else if (userType === 'admin') {
      result = await adminLogin(email, password);
      if (result?.status === 'success') {
        const data = result.data;
        sessionStorage.setItem('token', data.token);
        sessionStorage.setItem('usertype', 'Admin');
        sessionStorage.setItem('adminId', data.adminId);
        toast.success('Admin login successful');
        navigate('/admin/dashboard');
      } else {
        toast.error(result?.error || 'Invalid email or password');
      }
    }
  };

  return (
    <>
      <h1 className="title">Login</h1>

      <div className="row">
        <div className="col"></div>
        <div className="col">
          <div className="form">
            {/* User type selection */}
            <div className="mb-3">
              <label>Login As</label>
              <select
                value={userType}
                onChange={(e) => setUserType(e.target.value)}
                className="form-control"
              >
                <option value="faculty">Faculty</option>
                <option value="admin">Admin</option>
              </select>
            </div>

            {/* Email */}
            <div className="mb-3">
              <label>Email</label>
              <input
                type="text"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="abc@test.com"
                className="form-control"
              />
            </div>

            {/* Password */}
            <div className="mb-3">
              <label>Password</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="********"
                className="form-control"
              />
            </div>

            {/* Forgot Password */}
            <div className="mb-2">
              <Link to={`/forgotpassword/${userType}`} style={{ fontSize: '14px' }}>
                Forgot Password?
              </Link>
            </div>

            {/* Register link for Faculty */}
            <div className="mb-3">
              {userType === 'faculty' && (
                <div>
                  Don&apos;t have an account? <Link to="/register">Register here</Link>
                </div>
              )}
              <button onClick={onLogin} className="btn btn-primary mt-2">
                Login
              </button>
            </div>
          </div>
        </div>
        <div className="col"></div>
      </div>
    </>
  );
}

export default Login;
