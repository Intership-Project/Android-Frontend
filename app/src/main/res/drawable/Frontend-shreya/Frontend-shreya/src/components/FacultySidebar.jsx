import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./FacultySidebar.css";

function FacultySidebar() {
  const navigate = useNavigate();
  const [role, setRole] = useState("");

  useEffect(() => {
    let rolename = sessionStorage.getItem("rolename");
    if (rolename) {
      rolename = rolename.trim().toLowerCase(); // remove spaces & lowercase
      setRole(rolename);
    }
  }, []);

  const onLogout = () => {
    sessionStorage.clear();
    navigate("/");
  };

  return (
    <div className="sidebar">
      <div className="sidebar-header">
        <h3>Faculty Panel</h3>
      </div>
      <ul className="sidebar-menu">
        {/* Role-based Home */}
        {role === "course coordinator" && (
          <li>
            <Link to="/homecc">🏠 Home</Link>
          </li>
        )}
        {(role === "trainer" || role === "lab mentor") && (
          <li>
            <Link to="/homefaculty">🏠 Home</Link>
          </li>
        )}

        {/* Only for Course Coordinator */}
        {role === "course coordinator" && (
          <>
            <li>
              <Link to="/viewstudentfeedback">📊 View Student Feedback</Link>
            </li>
            <li>
              <Link to="/addfacultyfeedback">➕ Add Faculty Feedback</Link>
            </li>
          </>
        )}

        {/* Common for all faculty */}
        <li>
          <Link to="/changepassword">🔑 Change Password</Link>
        </li>

        <li>
          <button onClick={onLogout} className="sidebar-btn">
            🚪 Logout
          </button>
        </li>
      </ul>
    </div>
  );
}

export default FacultySidebar;
